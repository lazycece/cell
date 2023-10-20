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

package com.lazycece.cell.core.configuration;

/**
 * @author lazycece
 * @date 2023/10/21
 */
public class CellBufferConfiguration {

    /**
     * Cell buffer expansion threshold, default value is 0.7
     */
    private double expansionThreshold = 0.7;

    /**
     * Cell buffer thread pool core size, default value is 5
     */
    private int threadPoolCoreSize = 5;

    /**
     * Cell buffer thread pool max size, default value is ${@code Integer.MAX_VALUE}
     */
    private int threadPoolMaxSize = Integer.MAX_VALUE;

    /**
     * Cell buffer thread pool keep alive time, default value is 60s
     */
    private long threadPoolKeepAliveTime = 60L;

    public double getExpansionThreshold() {
        return expansionThreshold;
    }

    public void setExpansionThreshold(double expansionThreshold) {
        this.expansionThreshold = expansionThreshold;
    }

    public int getThreadPoolCoreSize() {
        return threadPoolCoreSize;
    }

    public void setThreadPoolCoreSize(int threadPoolCoreSize) {
        this.threadPoolCoreSize = threadPoolCoreSize;
    }

    public int getThreadPoolMaxSize() {
        return threadPoolMaxSize;
    }

    public void setThreadPoolMaxSize(int threadPoolMaxSize) {
        this.threadPoolMaxSize = threadPoolMaxSize;
    }

    public long getThreadPoolKeepAliveTime() {
        return threadPoolKeepAliveTime;
    }

    public void setThreadPoolKeepAliveTime(long threadPoolKeepAliveTime) {
        this.threadPoolKeepAliveTime = threadPoolKeepAliveTime;
    }
}
