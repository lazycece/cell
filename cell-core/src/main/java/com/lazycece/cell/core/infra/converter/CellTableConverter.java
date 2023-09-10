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

package com.lazycece.cell.core.infra.converter;

import com.lazycece.cell.core.infra.dal.po.CellTablePO;
import com.lazycece.cell.core.model.CellTable;

import java.util.Date;

/**
 * @author lazycece
 * @date 2023/9/10
 */
public class CellTableConverter {

    public static CellTablePO toCellTablePO(CellTable model) {
        if (model == null) {
            return null;
        }
        CellTablePO po = new CellTablePO();
        po.setName(model.getName());
        po.setCode(model.getCode());
        po.setValue(model.getValue());
        po.setMinValue(model.getMinValue());
        po.setMaxValue(model.getMaxValue());
        po.setStep(model.getStep());
        po.setCreateTime(new Date());
        po.setUpdateTime(new Date());
        return po;
    }

    public static CellTable toCellTable(CellTablePO po) {
        if (po == null) {
            return null;
        }
        CellTable model = new CellTable();
        model.setName(po.getName());
        model.setCode(po.getCode());
        model.setValue(po.getValue());
        model.setMinValue(po.getMinValue());
        model.setMaxValue(po.getMaxValue());
        model.setStep(po.getStep());
        model.setCreateTime(po.getCreateTime());
        model.setUpdateTime(po.getUpdateTime());
        return model;
    }
}
