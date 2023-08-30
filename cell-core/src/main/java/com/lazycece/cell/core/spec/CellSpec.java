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

/**
 * Define the cell specification .
 *
 * <p>date(8) + cellId(3) + center(1) + machine(2) + sequence(10)</p>
 * <p>For example: [20991231] [003] [0] [01] [0000000001]</p>
 *
 * @author lazycece
 * @date 2023/8/30
 */
public class CellSpec {

    /**
     * The production date
     */
    private String date;
    /**
     * The cell id
     */
    private String cellId;
    /**
     * The data center
     */
    private String dataCenter;
    /**
     * The machine
     */
    private String machine;
    /**
     * The cell sequence
     */
    private String sequence;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    public String getDataCenter() {
        return dataCenter;
    }

    public void setDataCenter(String dataCenter) {
        this.dataCenter = dataCenter;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
}
