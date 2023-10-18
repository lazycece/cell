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
     * min value
     */
    private Integer minValue;

    /**
     * max value
     */
    private Integer maxValue;

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

}