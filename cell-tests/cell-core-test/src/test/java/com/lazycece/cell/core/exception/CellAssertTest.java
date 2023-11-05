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

package com.lazycece.cell.core.exception;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author lazycece
 * @date 2023/11/5
 */
public class CellAssertTest {

    @Test
    public void test() {
        // isTure
        CellAssert.isTrue(true, "test");
        assertThatThrownBy(() -> CellAssert.isTrue(false, "test"))
                .isInstanceOf(CellException.class);

        // notNull
        CellAssert.notNull(new Object(), "test");
        assertThatThrownBy(() -> CellAssert.notNull(null, "test"))
                .isInstanceOf(CellException.class);

        // notEmpty;
        CellAssert.notEmpty(List.of(1, 2), "test");
        assertThatThrownBy(() -> CellAssert.notEmpty(List.of(), "test"))
                .isInstanceOf(CellException.class);
    }
}
