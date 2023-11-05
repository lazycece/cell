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

package com.lazycece.cell.specification.impl;

import com.lazycece.cell.core.buffer.CellBufferManager;
import com.lazycece.cell.core.exception.CellAssert;
import com.lazycece.cell.specification.configuration.CellConfiguration;
import com.lazycece.cell.core.infra.repository.CellRegistryRepository;
import com.lazycece.cell.core.model.CellRegistry;
import com.lazycece.cell.specification.CellFacade;
import com.lazycece.cell.specification.factory.CellRegistryFactory;
import com.lazycece.cell.specification.model.Cell;
import com.lazycece.cell.specification.model.CellBuilder;
import com.lazycece.cell.specification.model.CellType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.Date;

/**
 * Cell facade service implement.
 *
 * @author lazycece
 * @date 2023/9/8
 */
@Component
public class CellFacadeImpl implements CellFacade, InitializingBean {

    private final Logger log = LoggerFactory.getLogger(CellFacadeImpl.class);
    private CellConfiguration configuration = new CellConfiguration();
    @Autowired
    private CellRegistryRepository cellRegistryRepository;
    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * @see CellFacade#generateId
     */
    @Override
    public String generateId(CellType cellType) {
        CellAssert.notNull(cellType, "invalid cell type.");
        int value = CellBufferManager.getInstance().getSequence(cellType.getName());
        Cell cell = CellBuilder.builder()
                .pattern(configuration.getPattern())
                .code(cellType.getCode())
                .date(new Date())
                .dataCenter(configuration.getDataCenter())
                .machine(configuration.getMachine())
                .sequence(value)
                .build();
        return cell.toString();
    }

    /**
     * @see InitializingBean#afterPropertiesSet
     */
    @Override
    public void afterPropertiesSet() {
        boolean exist = cellRegistryRepository.existCellRegistry();
        CellAssert.isTrue(exist, "The expected table(cell_registry) not exist in db.");
        initCellRegistry();
        CellBufferManager.getInstance().initCache();
        log.info("Cell started successfully.");
    }

    /**
     * init cell registry to db.
     */
    private void initCellRegistry() {
        Class<? extends CellType> cellTypeClass = configuration.getCellTypeClass();
        CellAssert.notNull(cellTypeClass, "Cell configuration (cell type class) not exist.");
        CellAssert.isTrue(cellTypeClass.isEnum(), "Cell configuration (cell type class) not enum.");

        Arrays.asList(cellTypeClass.getEnumConstants())
                .forEach(cellType ->
                        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                            @Override
                            protected void doInTransactionWithoutResult(TransactionStatus status) {
                                CellRegistry cellRegistry = cellRegistryRepository.lockQueryByName(cellType.getName());
                                if (cellRegistry == null) {
                                    cellRegistry = CellRegistryFactory.build(cellType, configuration);
                                    cellRegistryRepository.save(cellRegistry);
                                }
                            }
                        })
                );
    }

    public void setConfiguration(CellConfiguration configuration) {
        this.configuration = configuration;
    }
}
