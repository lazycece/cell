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

package com.lazycece.cell.core.model;

import com.lazycece.cell.core.model.spec.CellType;

/**
 * @author lazycece
 * @date 2023/10/16
 */
public interface CellConfig {

    /**
     * The data center
     */
    Integer dataCenter();

    /**
     * The machine
     */
    Integer machine();

    /**
     * The cell type
     *
     * @return see ${@link CellType}
     */
    default CellType cellType() {
        return null;
    }

    /**
     * min value
     */
    Integer minValue();

    /**
     * max value
     */
    Integer maxValue();

    /**
     * the step, that the interval size of the value.
     */
    Integer step();
}
