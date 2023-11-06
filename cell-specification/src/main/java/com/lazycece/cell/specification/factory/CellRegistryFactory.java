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

package com.lazycece.cell.specification.factory;

import com.lazycece.cell.core.model.CellRegistry;
import com.lazycece.cell.specification.configuration.CellSpecConfiguration;
import com.lazycece.cell.specification.model.CellType;

import java.util.Date;

/**
 * @author lazycece
 * @date 2023/10/31
 */
public class CellRegistryFactory {

    public static CellRegistry build(CellType cellType, CellSpecConfiguration configuration) {
        CellRegistry cellRegistry = new CellRegistry();
        cellRegistry.setName(cellType.getName());
        cellRegistry.setValue(configuration.getMinValue());
        cellRegistry.setMinValue(configuration.getMinValue());
        cellRegistry.setMaxValue(configuration.getMaxValue());
        cellRegistry.setStep(configuration.getStep());
        cellRegistry.setCreateTime(new Date());
        cellRegistry.setUpdateTime(new Date());
        return cellRegistry;
    }
}
