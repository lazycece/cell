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

package com.lazycece.cell.specification.model;

import java.util.Date;

/**
 * @author lazycece
 * @date 2023/8/30
 */
public class CellBuilder {

    private final Cell cell;

    private CellBuilder(Cell cell) {
        this.cell = cell;
    }

    public static CellBuilder builder() {
        return new CellBuilder(new Cell());
    }

    public CellBuilder code(String code) {
        cell.setCode(code);
        return this;
    }

    public CellBuilder date(Date date) {
        cell.setDate(date);
        return this;
    }

    public CellBuilder dataCenter(Integer dataCenter) {
        cell.setDataCenter(dataCenter);
        return this;
    }

    public CellBuilder machine(Integer machine) {
        cell.setMachine(machine);
        return this;
    }

    public CellBuilder sequence(Long sequence) {
        cell.setSequence(sequence);
        return this;
    }

    public Cell build() {
        return cell;
    }
}
