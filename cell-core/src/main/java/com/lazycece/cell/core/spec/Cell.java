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

package com.lazycece.cell.core.spec;

import java.util.Date;

/**
 * Define the cell model .
 *
 * @author lazycece
 * @date 2023/8/30
 */
public class Cell {

    /**
     * The cell code
     */
    private String code;
    /**
     * The production date
     */
    private Date date;
    /**
     * The data center
     */
    private Integer dataCenterId;
    /**
     * The machine
     */
    private Integer machineId;
    /**
     * The sequence
     */
    private Integer sequence;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(Integer dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
}
