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

import com.lazycece.cell.core.model.CellRegistry;

import java.util.List;

/**
 * @author lazycece
 * @date 2023/9/9
 */
public interface CellRegistryRepository {

    /**
     * Detect the cell registry table exist or not.
     *
     * @return true or false
     */
    boolean existCellRegistry();

    /**
     * Save the cell registry information.
     *
     * @param cellRegistry ${@link CellRegistry}
     */
    void save(CellRegistry cellRegistry);

    /**
     * Query all cell registry name.
     *
     * @return cell registry name list
     */
    List<String> queryAllName();

    /**
     * Query cell registry.
     *
     * @param name name
     * @return see ${@link CellRegistry}
     */
    CellRegistry queryByName(String name);

    /**
     * Query cell registry with lock.
     *
     * @param name name
     * @return see ${@link CellRegistry}
     */
    CellRegistry lockQueryByName(String name);

    /**
     * Update cell's value , and get cell registry information.
     * <p>using origin step value</p>
     *
     * @param name name
     * @return see ${@link CellRegistry}
     */
    CellRegistry updateValueAndGet(String name);

    /**
     * Update cell's value , and get cell registry information.
     * <p>using given step value</p>
     *
     * @param name name
     * @return see ${@link CellRegistry}
     */
    CellRegistry updateValueAndGet(String name, Integer step);
}
