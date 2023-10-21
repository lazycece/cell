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
public class CellRegistryRepositoryImpl implements CellRegistryRepository {

    /**
     * @see CellRegistryRepository#existCellRegistry()
     */
    @Override
    public boolean existCellRegistry() {
        return false;
    }

    /**
     * @see CellRegistryRepository#save
     */
    @Override
    public void save(CellRegistry cellRegistry) {

    }

    /**
     * @see CellRegistryRepository#queryAll
     */
    @Override
    public List<CellRegistry> queryAll() {
        return null;
    }

    /**
     * @see CellRegistryRepository#lockQueryByName
     */
    @Override
    public CellRegistry lockQueryByName(String name) {
        return null;
    }

    /**
     * @see CellRegistryRepository#updateValueByName
     */
    @Override
    public boolean updateValueByName(String name, Integer value) {
        return false;
    }
}
