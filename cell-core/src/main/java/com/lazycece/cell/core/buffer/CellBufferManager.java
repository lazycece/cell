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

import com.lazycece.cell.core.configuration.CellBufferConfiguration;
import com.lazycece.cell.core.exception.CellException;
import com.lazycece.cell.core.infra.repository.CellRegistryRepository;
import com.lazycece.cell.core.model.CellRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

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
    private CellBufferConfiguration bufferConfig;
    private volatile boolean ready = false;
    private final ExecutorService executorService = new ThreadPoolExecutor(
            bufferConfig.getThreadPoolCoreSize(),
            bufferConfig.getThreadPoolMaxSize(),
            bufferConfig.getThreadPoolKeepAliveTime(),
            TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new BufferThreadFactory("CellBufferRefresh"),
            new ThreadPoolExecutor.AbortPolicy());
    @Autowired
    private CellRegistryRepository cellRegistryRepository;
    @Autowired
    private TransactionTemplate transactionTemplate;

    @PostConstruct
    public void init() {
        INSTANCE = this;
    }

    public static CellBufferManager getInstance() {
        return INSTANCE;
    }

    public synchronized void initCache() {
        if (ready) {
            log.info("Cell Buffer Manager has already been initialized.");
            return;
        }
        log.info("Begin to init Cell Buffer Manager.");

        List<CellRegistry> cellRegistryList = cellRegistryRepository.queryAll();
        if (cellRegistryList == null || cellRegistryList.size() == 0) {
            throw new CellException("Init cell buffer manager fail, no cell registrys.");
        }
        cellRegistryList.forEach(cellRegistry -> refresh(cellRegistry.getName()));

        ready = true;
        log.info("Finish init Cell Buffer Manager.");
    }

    private void refresh(String name) {
        CellBuffer cellBuffer = CACHE_MAP.getOrDefault(name, new CellBuffer());
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                CellRegistry cellRegistry = cellRegistryRepository.lockQueryByName(name);
                if (cellRegistry == null) {
                    throw new CellException(String.format("Cell registry ( %s ) not exist.", name));
                }
                // TODO: 2023/10/20  最大值检测；
                // TODO: 2023/10/20 值滚动，每天从头开始？or 翻滚？
                cellRegistryRepository.updateValueByName(name, cellRegistry.getValue() + cellRegistry.getStep());
            }
        });


    }

    public long getSequence(String name) {
        if (!ready) {
            throw new CellException("Cell Buffer Manager is not ready yet.");
        }
        CellBuffer cellBuffer = CACHE_MAP.get(name);
        if (cellBuffer == null) {
            throw new CellException(String.format("cell (%s) buffer is null.", name));
        }

        if (!cellBuffer.needExpansion(bufferConfig.getExpansionThreshold())) {
            return cellBuffer.nextValue();
        }
        //  do expand
        doExpand(cellBuffer);
        return cellBuffer.nextValue();
    }

    private void doExpand(CellBuffer cellBuffer) {
        Lock lock = cellBuffer.getLock().writeLock();
        lock.lock();

        try {
            executorService.execute(() -> {
                refresh(cellBuffer.getName());
                cellBuffer.setNextReady(true);
            });
            // TODO: 2023/10/19
        } finally {
            lock.unlock();
        }

    }
}
