/*
 *    Copyright 2023 lazycece<lazycece@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.lazycece.cell.core.buffer;

import com.lazycece.cell.core.configuration.BufferConfiguration;
import com.lazycece.cell.core.exception.CellAssert;
import com.lazycece.cell.core.infra.repository.CellRegistryRepository;
import com.lazycece.cell.core.model.CellRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;

/**
 * @author lazycece
 * @date 2023/9/9
 */
@Component
public class CellBufferManager {

    private final Logger log = LoggerFactory.getLogger(CellBufferManager.class);
    private static CellBufferManager INSTANCE;
    private final ConcurrentHashMap<String/*name*/, CellBuffer> CACHE_MAP = new ConcurrentHashMap<>();
    private BufferConfiguration bufferConfig;
    private volatile boolean ready = false;
    private final ExecutorService executorService = new ThreadPoolExecutor(
            bufferConfig.getThreadPoolCoreSize(),
            bufferConfig.getThreadPoolMaxSize(),
            bufferConfig.getThreadPoolKeepAliveTime(),
            TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new BufferThreadFactory("CellBufferRefresher"),
            new ThreadPoolExecutor.AbortPolicy());
    @Autowired
    private CellRegistryRepository cellRegistryRepository;

    @PostConstruct
    public void init() {
        INSTANCE = this;
    }

    public static CellBufferManager getInstance() {
        return INSTANCE;
    }

    /**
     * initialize cell buffer cache.
     */
    public synchronized void initCache() {
        if (ready) {
            log.info("Cell Buffer Manager has already been initialized.");
            return;
        }
        log.info("Begin to init Cell Buffer Manager.");

        List<String> cellNameList = cellRegistryRepository.queryAllName();
        CellAssert.notEmpty(cellNameList, "Init cell buffer manager fail, no cell registry information.");

        cellNameList.forEach(name -> {
            CellRegistry cellRegistry = cellRegistryRepository.updateValueAndGet(name);
            CellAssert.notNull(cellRegistry, "Cell registry (%s) not exist", name);
            CellBuffer cellBuffer = new CellBuffer();
            cellBuffer.fillBuffer(cellRegistry);
            CACHE_MAP.put(name, cellBuffer);
        });

        ready = true;
        log.info("Finish init Cell Buffer Manager.");
    }

    /**
     * Get cell sequence value.
     *
     * @param name cell name
     * @return value
     */
    public int getSequence(String name) {
        CellAssert.isTrue(ready, "Cell Buffer Manager is not ready yet.");

        CellBuffer cellBuffer = CACHE_MAP.get(name);
        CellAssert.notNull(cellBuffer, "cell (%s) buffer is null.", name);

        if (!cellBuffer.needExpansion(bufferConfig.getExpansionThreshold())) {
            return cellBuffer.nextValue();
        }
        return doExpandAndGet(cellBuffer);
    }

    /**
     * To expand cell buffer and get sequence value.
     *
     * @param cellBuffer ${@link CellBuffer}
     * @return value
     */
    private int doExpandAndGet(CellBuffer cellBuffer) {
        Lock rLock = cellBuffer.getLock().readLock();
        rLock.lock();
        try {
            while (true) {
                if (cellBuffer.needExpansion(bufferConfig.getExpansionThreshold())
                        && cellBuffer.getExpanding().compareAndSet(false, true)) {
                    executorService.execute(() -> {
                        boolean update = false;
                        try {
                            refresh(cellBuffer);
                            update = true;
                        } finally {
                            if (update) {
                                Lock wLock = cellBuffer.getLock().writeLock();
                                cellBuffer.setNextReady(true);
                                wLock.unlock();
                            }
                        }
                        cellBuffer.getExpanding().compareAndSet(true, false);
                    });
                }
                int value = cellBuffer.nextValue();
                if (value <= cellBuffer.getMaxValue()) {
                    return value;
                }
                spinWaitAndSleep(cellBuffer);
            }
        } finally {
            rLock.unlock();
        }
    }

    /**
     * Refresh cell buffer.
     *
     * @param cellBuffer ${@link CellBuffer}
     */
    private void refresh(CellBuffer cellBuffer) {
        int minStep = bufferConfig.getMinStep();
        int maxStep = bufferConfig.getMaxStep();
        long bufferRefreshInterval = bufferConfig.getBufferRefreshInterval();

        int step = cellBuffer.currentBufferValue().getStep();
        long interval = System.currentTimeMillis() - cellBuffer.getRefreshTimestamp();

        // Dynamically adjust the expansion speed based on actual consumption
        if (interval < bufferRefreshInterval) {
            step = step * 2 <= maxStep ? step * 2 : step;
        } else if (interval >= 2 * bufferRefreshInterval) {
            step = step / 2 >= minStep ? step / 2 : step;
        }

        CellRegistry cellRegistry = cellRegistryRepository.updateValueAndGet(cellBuffer.getName(), step);
        CellAssert.notNull(cellRegistry, "Cell registry (%s) not exist", cellBuffer.getName());
        cellRegistry.setStep(step);

        cellBuffer.fillBuffer(cellRegistry);

        log.info("Refresh cell buffer ({}) completed, step={}, interval={}, minStep={}, maxStep={}, bufferRefreshInterval={}",
                cellBuffer.getName(), step, interval, minStep, maxStep, bufferRefreshInterval);
    }

    /**
     * Let current thread to spin wait while the cell buffer is expanding,
     * but sleep with max spin.
     *
     * @param cellBuffer ${@link CellBuffer}
     */
    private void spinWaitAndSleep(CellBuffer cellBuffer) {
        int spinNum = 0;
        while (cellBuffer.getExpanding().get()) {
            spinNum += 1;
            if (spinNum > 10000) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    log.warn("Thread {} Interrupted", Thread.currentThread().getName());
                }
                break;
            }
        }
    }

    public void setBufferConfig(BufferConfiguration bufferConfig) {
        this.bufferConfig = bufferConfig;
    }
}
