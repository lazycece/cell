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

package com.lazycece.cell.specification;

import com.lazycece.cell.specification.exception.CellSpecException;
import com.lazycece.cell.specification.model.Cell;
import com.lazycece.cell.specification.model.CellBuilder;
import com.lazycece.cell.specification.model.CellPattern;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author lazycece
 * @date 2023/11/4
 */
public class CellSpecTest {

    @Test
    public void testSpec() {
        // case: default pattern
        Cell cell = CellBuilder.builder()
                .pattern(CellPattern.DAY)
                .code("101")
                .date(new Date())
                .dataCenter(2)
                .machine(12)
                .sequence(1217589)
                .build();
        assertThat(cell.toString().length()).isEqualTo(24);

        // case: error code
        cell.setCode("0004");
        assertThatThrownBy(cell::toString).isInstanceOf(CellSpecException.class);

        // case: error data center
        cell.setDataCenter(10);
        assertThatThrownBy(cell::toString).isInstanceOf(CellSpecException.class);

        // case: error machine
        cell.setMachine(100);
        assertThatThrownBy(cell::toString).isInstanceOf(CellSpecException.class);

    }

    @Test
    public void testPattern() {
        String cellId = CellBuilder.builder()
                .pattern(CellPattern.DAY)
                .code("101")
                .date(new Date())
                .dataCenter(2)
                .machine(12)
                .sequence(1217589)
                .build()
                .toString();
        assertThat(cellId.length()).isEqualTo(24);
        System.out.println(cellId);

        cellId = CellBuilder.builder()
                .pattern(CellPattern.HOUR)
                .code("101")
                .date(new Date())
                .dataCenter(2)
                .machine(12)
                .sequence(1217589)
                .build()
                .toString();
        assertThat(cellId.length()).isEqualTo(26);
        System.out.println(cellId);

        cellId = CellBuilder.builder()
                .pattern(CellPattern.MINUTE)
                .code("101")
                .date(new Date())
                .dataCenter(2)
                .machine(12)
                .sequence(1217589)
                .build()
                .toString();
        assertThat(cellId.length()).isEqualTo(28);
        System.out.println(cellId);

//        20231105 101 2 12       0001217589
//        20231105 101 2 12 01    0001217589
//        20231105 101 2 12 01 40 0001217589

    }
}
