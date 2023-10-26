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

import com.lazycece.cell.core.exception.CellAssert;
import com.lazycece.cell.core.infra.converter.CellRegistryConverter;
import com.lazycece.cell.core.infra.dal.mapper.CellRegistryMapper;
import com.lazycece.cell.core.infra.dal.po.CellRegistryPO;
import com.lazycece.cell.core.model.CellRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * @author lazycece
 * @date 2023/9/9
 */
@Repository
public class CellRegistryRepositoryImpl implements CellRegistryRepository {

    @Autowired
    private CellRegistryMapper cellRegistryMapper;
    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * @see CellRegistryRepository#existCellRegistry()
     */
    @Override
    public boolean existCellRegistry() {
        return cellRegistryMapper.existCellRegistry() > 0;
    }

    /**
     * @see CellRegistryRepository#save(CellRegistry)
     */
    @Override
    public void save(CellRegistry cellRegistry) {
        CellRegistryPO po = CellRegistryConverter.toCellRegistryPO(cellRegistry);
        cellRegistryMapper.insert(po);
    }

    /**
     * @see CellRegistryRepository#queryAllName()
     */
    @Override
    public List<String> queryAllName() {
        return cellRegistryMapper.findAllName();
    }

    /**
     * @see CellRegistryRepository#queryByName
     */
    @Override
    public CellRegistry queryByName(String name) {
        CellRegistryPO po = cellRegistryMapper.findByName(name);
        return CellRegistryConverter.toCellRegistry(po);
    }

    /**
     * @see CellRegistryRepository#updateValueAndGet(String)
     */
    @Override
    public CellRegistry updateValueAndGet(String name) {
        return transactionTemplate.execute(status -> {
            int result = cellRegistryMapper.updateValueByName(name);
            CellAssert.isTrue(result > 0, "To update cell's value fail (%s)", name);
            CellRegistry cellRegistry = queryByName(name);
            CellAssert.notNull(cellRegistry, "Cell registry (%s) is null", name);
            if (cellRegistry.needReset()) {
                result = cellRegistryMapper.updateValueByReset(name);
                CellAssert.isTrue(result > 0, "To reset cell's value fail (%s)", name);
                return queryByName(name);
            }
            return cellRegistry;
        });
    }

    /**
     * @see CellRegistryRepository#updateValueAndGet(String, Integer)
     */
    @Override
    public CellRegistry updateValueAndGet(String name, Integer step) {
        return transactionTemplate.execute(status -> {
            int result = cellRegistryMapper.updateValueByNameWithGivenStep(name, step);
            CellAssert.isTrue(result > 0, "To update cell's value fail (%s)", name);
            CellRegistry cellRegistry = queryByName(name);
            CellAssert.notNull(cellRegistry, "Cell registry is null (%s)", name);
            if (cellRegistry.needReset()) {
                result = cellRegistryMapper.updateValueByReset(name);
                CellAssert.isTrue(result > 0, "To reset cell's value fail (%s)", name);
                return queryByName(name);
            }
            return cellRegistry;
        });
    }
}
