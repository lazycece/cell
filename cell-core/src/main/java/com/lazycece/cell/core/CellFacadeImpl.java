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

package com.lazycece.cell.core;

import com.lazycece.cell.core.buffer.CellBufferManager;
import com.lazycece.cell.core.configuration.CellConfiguration;
import com.lazycece.cell.core.exception.CellException;
import com.lazycece.cell.core.infra.repository.CellRegistryRepository;
import com.lazycece.cell.core.model.CellRegistry;
import com.lazycece.cell.core.model.spec.Cell;
import com.lazycece.cell.core.model.spec.CellBuilder;
import com.lazycece.cell.core.model.spec.CellType;
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
    @Autowired
    private CellRegistryRepository cellRegistryRepository;
    @Autowired
    private TransactionTemplate transactionTemplate;
    private CellConfiguration cellConfiguration;

    /**
     * @see CellFacade#generateId
     */
    @Override
    public String generateId(CellType cellType) {
        if (cellType == null) {
            throw new CellException("invalid cell type.");
        }
        int value = CellBufferManager.getInstance().getSequence(cellType.name());
        Cell cell = CellBuilder.builder()
                .code(cellType.code())
                .date(new Date())
                .dataCenter(cellConfiguration.getDataCenter())
                .machine(cellConfiguration.getMachine())
                .sequence(value)
                .build();
        return cell.toString();
    }

    /**
     * @see InitializingBean#afterPropertiesSet
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        boolean exist = cellRegistryRepository.existCellRegistry();
        if (!exist) {
            throw new CellException("There is no cell registry table in db.");
        }
        initCellRegistry();
        CellBufferManager.getInstance().initCache();
        log.info("Cell started successfully !");
    }

    private void initCellRegistry() {
        Class<? extends CellType> cellTypeClass = cellConfiguration.getCellTypeClass();
        if (cellTypeClass == null) {
            throw new CellException("Cell configuration (cell type class) not exist.");
        }
        if (!cellTypeClass.isEnum()) {
            throw new CellException("Cell configuration (cell type class) not enum.");
        }
        Arrays.asList(cellTypeClass.getEnumConstants())
                .forEach(cellType ->
                        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                            @Override
                            protected void doInTransactionWithoutResult(TransactionStatus status) {
                                CellRegistry cellRegistry = cellRegistryRepository.lockQueryByName(cellType.name());
                                if (cellRegistry == null) {
                                    cellRegistry = CellRegistry.init(cellType, cellConfiguration);
                                    cellRegistryRepository.save(cellRegistry);
                                }
                            }
                        })
                );
    }
}
