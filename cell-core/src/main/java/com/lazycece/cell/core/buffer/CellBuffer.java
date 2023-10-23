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

import java.util.concurrent.atomic.AtomicLong;
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
     * Cell buffer version
     */
    private Integer version = 0;

    /**
     * max value
     */
    private Long maxValue;

    /**
     * the step, that the interval size of the value.
     */
    private Integer step;

    /**
     * current value
     */
    private final ValueInfo[] valueInfos = new ValueInfo[2];

    /**
     * value info array  position pointer
     */
    private volatile int pointer;

    /**
     * show expansion value ready or not
     */
    private volatile boolean nextReady = false;

    /**
     * cell buffer lock
     */
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    // TODO: 2023/10/19  note
    public void fillBuffer(CellRegistry cellRegistry) {
        name = cellRegistry.getName();
        maxValue = cellRegistry.getMaxValue();
        step = cellRegistry.getStep();
        if (version == 0) {
            valueInfos[pointer].setValue(new AtomicLong(cellRegistry.getValue()));
        } else {
            valueInfos[nextPointer()].setValue(new AtomicLong(cellRegistry.getValue()));
        }
        ++version;
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
        double currentValue = valueInfos[pointer].getValue().doubleValue();
        double percentage = currentValue % step / step;
        return percentage >= threshold;
    }

    /**
     * Get next value.
     *
     * @return next value
     */
    public long nextValue() {
        long nextVal = valueInfos[pointer].getValue().getAndIncrement();
        if (nextVal <= maxValue) {
            return nextVal;
        }
        resetPointer();
        setNextReady(false);
        return valueInfos[pointer].getValue().incrementAndGet();
    }

    /**
     * Reset value array pointer.
     */
    private void resetPointer() {
        pointer = nextPointer();
    }

    private int nextPointer() {
        return (pointer + 1) % 2;
    }

    public void setNextReady(boolean nextReady) {
        this.nextReady = nextReady;
    }

    public String getName() {
        return name;
    }

    public ReentrantReadWriteLock getLock() {
        return lock;
    }
}