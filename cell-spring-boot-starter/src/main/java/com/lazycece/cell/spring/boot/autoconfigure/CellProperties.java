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

package com.lazycece.cell.spring.boot.autoconfigure;

import com.lazycece.cell.specification.model.CellPattern;
import com.lazycece.cell.specification.model.CellType;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author lazycece
 * @date 2023/11/4
 */
@ConfigurationProperties(prefix = "cell")
public class CellProperties {

    /**
     * The cell type class, can be null.
     * <p>If not null, will be registered automatically</p>
     */
    private Class<? extends CellType> cellTypeClass;

    /**
     * The cell specification properties configuration
     */
    private CellSpecProperties specification = new CellSpecProperties();

    /**
     * The cell buffer properties
     */
    private CellBufferProperties buffer = new CellBufferProperties();

    public Class<? extends CellType> getCellTypeClass() {
        return cellTypeClass;
    }

    public void setCellTypeClass(Class<? extends CellType> cellTypeClass) {
        this.cellTypeClass = cellTypeClass;
    }

    public CellSpecProperties getSpecification() {
        return specification;
    }

    public void setSpecification(CellSpecProperties specification) {
        this.specification = specification;
    }

    public CellBufferProperties getBuffer() {
        return buffer;
    }

    public void setBuffer(CellBufferProperties buffer) {
        this.buffer = buffer;
    }

    public static class CellSpecProperties {

        /**
         * The cell pattern
         *
         * @see CellPattern
         */
        private CellPattern pattern = CellPattern.DAY;

        /**
         * The data center
         */
        private Integer dataCenter = 1;

        /**
         * The machine
         */
        private Integer machine = 1;

        /**
         * min value
         */
        private Integer minValue = 0;

        /**
         * max value
         */
        private Integer maxValue = Integer.MAX_VALUE;

        /**
         * the step, that the interval size of the value.
         */
        private Integer step = 6000;

        public CellPattern getPattern() {
            return pattern;
        }

        public void setPattern(CellPattern pattern) {
            this.pattern = pattern;
        }

        public Integer getDataCenter() {
            return dataCenter;
        }

        public void setDataCenter(Integer dataCenter) {
            this.dataCenter = dataCenter;
        }

        public Integer getMachine() {
            return machine;
        }

        public void setMachine(Integer machine) {
            this.machine = machine;
        }

        public Integer getMinValue() {
            return minValue;
        }

        public void setMinValue(Integer minValue) {
            this.minValue = minValue;
        }

        public Integer getMaxValue() {
            return maxValue;
        }

        public void setMaxValue(Integer maxValue) {
            this.maxValue = maxValue;
        }

        public Integer getStep() {
            return step;
        }

        public void setStep(Integer step) {
            this.step = step;
        }
    }

    public static class CellBufferProperties {

        /**
         * Cell buffer expansion threshold, default value is 0.75
         */
        private double expansionThreshold = 0.75;

        /**
         * Cell buffer expansion elasticity time. If value is m,
         * indicates the step interval is [step/(2^m),step*(2^m)]
         */
        private int expansionStepElasticityTime = 2;

        /**
         * Cell buffer refresh interval time (milliseconds)
         * <p>
         * Compute by qps and default step, for example
         * <li>condition: step=6000, qps=10</li>
         * <li>then: interval=step/qps=600s </li>
         * </p>
         */
        private Duration expansionInterval = Duration.ofMinutes(10);

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
        private Duration threadPoolKeepAliveTime = Duration.ofSeconds(60);

        public double getExpansionThreshold() {
            return expansionThreshold;
        }

        public void setExpansionThreshold(double expansionThreshold) {
            this.expansionThreshold = expansionThreshold;
        }

        public int getExpansionStepElasticityTime() {
            return expansionStepElasticityTime;
        }

        public void setExpansionStepElasticityTime(int expansionStepElasticityTime) {
            this.expansionStepElasticityTime = expansionStepElasticityTime;
        }

        public Duration getExpansionInterval() {
            return expansionInterval;
        }

        public void setExpansionInterval(Duration expansionInterval) {
            this.expansionInterval = expansionInterval;
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

        public Duration getThreadPoolKeepAliveTime() {
            return threadPoolKeepAliveTime;
        }

        public void setThreadPoolKeepAliveTime(Duration threadPoolKeepAliveTime) {
            this.threadPoolKeepAliveTime = threadPoolKeepAliveTime;
        }
    }
}
