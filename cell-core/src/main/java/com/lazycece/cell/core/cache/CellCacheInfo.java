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

package com.lazycece.cell.core.cache;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author lazycece
 * @date 2023/9/11
 */
public class CellCacheInfo {

    /**
     * cell name
     */
    private String name;
    /**
     * cell code
     */
    private String code;
    /**
     * current value
     */
    private AtomicInteger value;
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

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
}
