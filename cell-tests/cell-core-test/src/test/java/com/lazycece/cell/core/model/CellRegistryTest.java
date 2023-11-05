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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author lazycece
 * @date 2023/11/5
 */
public class CellRegistryTest {

    @Test
    public void testNeedReset() {
        CellRegistry cellRegistry = new CellRegistry();
        // case: value>maxValue
        cellRegistry.setValue(1000);
        cellRegistry.setMaxValue(1000);
        cellRegistry.setStep(100);
        assertThat(cellRegistry.needReset()).isTrue();

        // case: match threshold
        cellRegistry.setValue(975);
        assertThat(cellRegistry.needReset()).isTrue();

        // case: not match threshold
        cellRegistry.setValue(974);
        assertThat(cellRegistry.needReset()).isFalse();

    }
}
