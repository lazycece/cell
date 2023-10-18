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
import com.lazycece.cell.core.exception.CellException;
import com.lazycece.cell.core.infra.repository.CellTableRepository;
import com.lazycece.cell.core.model.CellConfig;
import com.lazycece.cell.core.model.spec.Cell;
import com.lazycece.cell.core.model.spec.CellBuilder;
import com.lazycece.cell.core.model.spec.CellType;

import java.util.Date;

/**
 * Cell facade service implement.
 *
 * @author lazycece
 * @date 2023/9/8
 */
public class CellFacadeImpl implements CellFacade {

    private CellTableRepository cellTableRepository;
    private CellConfig cellConfig;

    // TODO: 2023/10/17  启动 》监听器 》 检测表是否存在 》 注册扫描 》 初始化缓存 》 cell启动成功
    /**
     * @see CellFacade#generateId
     */
    @Override
    public String generateId(CellType cellType) {
        if (cellType == null) {
            throw new CellException("invalid cell type.");
        }
        int value = CellBufferManager.getSequence(cellType.name());
        Cell cell = CellBuilder.builder()
                .code(cellType.code())
                .date(new Date())
                .dataCenter(cellConfig.dataCenter())
                .machine(cellConfig.machine())
                .sequence(value)
                .build();
        return cell.toString();
    }
}
