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

package com.lazycece.cell.core.model;

import com.lazycece.cell.core.configuration.CellConfiguration;
import com.lazycece.cell.core.specification.CellType;

import java.util.Date;

/**
 * The cell registry table.
 *
 * @author lazycece
 * @date 2023/8/30
 */
public class CellRegistry {

    /**
     * cell name
     */
    private String name;
    /**
     * current value
     */
    private Long value;
    /**
     * min value
     */
    private Long minValue;
    /**
     * max value
     */
    private Long maxValue;
    /**
     * the step, that the interval size of the value.
     */
    private Integer step;
    /**
     * create time
     */
    private Date createTime;
    /**
     * update time
     */
    private Date updateTime;

    public static CellRegistry init(CellType cellType, CellConfiguration configuration) {
        CellRegistry cellRegistry = new CellRegistry();
        cellRegistry.setName(cellType.name());
        cellRegistry.setValue(configuration.getMinValue());
        cellRegistry.setMinValue(configuration.getMinValue());
        cellRegistry.setMaxValue(configuration.getMaxValue());
        cellRegistry.setStep(cellRegistry.getStep());
        cellRegistry.setCreateTime(new Date());
        cellRegistry.setUpdateTime(new Date());
        return cellRegistry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Long getMinValue() {
        return minValue;
    }

    public void setMinValue(Long minValue) {
        this.minValue = minValue;
    }

    public Long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Long maxValue) {
        this.maxValue = maxValue;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
