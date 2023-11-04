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

package com.lazycece.cell.specification.configuration;


import com.lazycece.cell.specification.model.CellPattern;
import com.lazycece.cell.specification.model.CellType;

/**
 * @author lazycece
 * @date 2023/10/20
 */
public class CellConfiguration {

    /**
     * The cell type class
     */
    private Class<? extends CellType> cellTypeClass;

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

    public Class<? extends CellType> getCellTypeClass() {
        return cellTypeClass;
    }

    public void setCellTypeClass(Class<? extends CellType> cellTypeClass) {
        this.cellTypeClass = cellTypeClass;
    }

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
