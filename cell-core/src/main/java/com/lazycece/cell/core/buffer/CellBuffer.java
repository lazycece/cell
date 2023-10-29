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

import com.lazycece.cell.core.model.CellRegistry;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author lazycece
 * @date 2023/9/12
 */
public class CellBuffer {
    /**
     * cell name
     */
    private String name;

    /**
     * max value
     */
    private long maxValue;

    /**
     * the step, that the interval size of the value
     */
    private volatile int step;

    /**
     * buffer refresh time (milliseconds)
     */
    private long refreshTimestamp = 0;

    /**
     * current value
     */
    private final ValueInfo[] valueInfos = new ValueInfo[2];

    /**
     * value info array  position pointer
     */
    private volatile int pointer = 0;

    /**
     * show expansion value ready or not
     */
    private volatile boolean nextReady = false;

    /**
     * indicates the buffer is expanding or not.
     */
    private AtomicBoolean expanding = new AtomicBoolean(false);

    /**
     * cell buffer lock
     */
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Fill the buffer with cell registry information.
     *
     * @param cellRegistry ${@link CellRegistry}
     */
    public void fillBuffer(CellRegistry cellRegistry) {
        if (refreshTimestamp == 0) {
            valueInfos[pointer].setValue(new AtomicInteger(cellRegistry.getValue()));
        } else {
            valueInfos[nextPointer()].setValue(new AtomicInteger(cellRegistry.getValue()));
        }
        step = cellRegistry.getStep();
        refreshTimestamp = System.currentTimeMillis();
        name = cellRegistry.getName();
        maxValue = cellRegistry.getMaxValue();
    }

    /**
     * Get need to expansion or not.
     *
     * @param threshold expansion threshold
     * @return true or false
     */
    public synchronized boolean needExpansion(double threshold) {
        if (nextReady) {
            return false;
        }
        int currentValue = valueInfos[pointer].getValue().intValue();
        double percentage = currentValue % step / (double) step;
        return percentage >= threshold;
    }

    /**
     * Get next value.
     *
     * @return next value
     */
    public int nextValue() {
        int nextVal = valueInfos[pointer].getValue().getAndIncrement();
        if (nextVal > maxValue && nextReady) {
            resetPointer();
            nextReady = false;
            nextVal = valueInfos[pointer].getValue().getAndIncrement();
        }
        return nextVal;
    }

    /**
     * Reset value array pointer.
     */
    private void resetPointer() {
        pointer = nextPointer();
    }

    /**
     * Get next pointer value.
     *
     * @return pointer value
     */
    private int nextPointer() {
        return (pointer + 1) % 2;
    }

    public String getName() {
        return name;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public long getRefreshTimestamp() {
        return refreshTimestamp;
    }

    public int getStep() {
        return step;
    }

    public void setNextReady(boolean nextReady) {
        this.nextReady = nextReady;
    }

    public AtomicBoolean getExpanding() {
        return expanding;
    }

    public ReentrantReadWriteLock getLock() {
        return lock;
    }
}