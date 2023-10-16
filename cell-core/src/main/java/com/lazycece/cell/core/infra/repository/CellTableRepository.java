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

package com.lazycece.cell.core.infra.repository;

import com.lazycece.cell.core.model.CellTable;

/**
 * @author lazycece
 * @date 2023/9/9
 */
public interface CellTableRepository {

    /**
     * Detect the cell table exist or not.
     *
     * @return true or false
     */
    boolean existCellTable();

    /**
     * Save the cell table information.
     *
     * @param cellTable ${@link CellTable}
     */
    void save(CellTable cellTable);

    /**
     * Query by cell name.
     *
     * @param name name
     * @return call table
     */
    CellTable queryByName(String name);

    /**
     * Update by cell name.
     *
     * @param name  name
     * @param value value
     * @return true or false
     */
    boolean updateValueByName(String name, Integer value);
}
