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
     * current value
     */
    private final BufferValue[] bufferValues = new BufferValue[2];

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
     * buffer refresh time (milliseconds)
     */
    private long refreshTimestamp = 0;

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
        name = cellRegistry.getName();
        maxValue = cellRegistry.getMaxValue();
        int pos = refreshTimestamp == 0 ? pointer : nextPointer();
        bufferValues[pos] = new BufferValue(new AtomicInteger(cellRegistry.getValue()), cellRegistry.getStep());
        refreshTimestamp = System.currentTimeMillis();
    }

    /**
     * Get need to expansion or not.
     *
     * @param threshold expansion threshold
     * @return true or false
     */
    public boolean needExpansion(double threshold) {
        if (nextReady) {
            return false;
        }
        BufferValue bufferValue = currentBufferValue();
        int value = bufferValue.getValue().intValue();
        int step = bufferValue.getStep();
        double percentage = value % step / (double) step;
        return percentage >= threshold;
    }

    /**
     * Get current cell buffer value.
     *
     * @return see ${@link BufferValue}
     */
    public BufferValue currentBufferValue() {
        return bufferValues[pointer];
    }

    /**
     * Get next value.
     *
     * @return next value
     */
    public int nextValue() {
        int nextVal = currentBufferValue().getValue().getAndIncrement();
        if (nextVal > maxValue && nextReady) {
            resetPointer();
            setNextReady(false);
            nextVal = currentBufferValue().getValue().getAndIncrement();
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