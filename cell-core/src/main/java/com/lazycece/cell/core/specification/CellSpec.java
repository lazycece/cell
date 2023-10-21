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

package com.lazycece.cell.core.specification;

import com.lazycece.cell.core.exception.CellException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Define the cell specification .
 * <p>
 * The length of cell-id is 24, and consists of the following information:
 * <br/>
 * date(8) + code(3) + center(1) + machine(2) + sequence(10).
 * <br/>
 * For example: [20991231] [003] [0] [01] [0000000001]
 * </p>
 *
 * @author lazycece
 * @date 2023/8/31
 * @see Cell
 */
public class CellSpec {

    private static final String CELL_DATE_FORMAT = "yyyyMMdd";
    private static final int CELL_CODE_LEN = 3;
    private static final int CELL_DATA_CENTER_LEN = 1;
    private static final int CELL_MACHINE_LEN = 2;
    private static final int CELL_SEQUENCE_LEN = 10;

    private static final CellSpec instance = new CellSpec();

    public static CellSpec getInstance() {
        return instance;
    }

    public String cellId(Cell cell) {
        String date = dateElement(cell.getDate());
        String code = codeElement(cell.getCode());
        String dataCenter = dataCenterElement(cell.getDataCenter());
        String machine = machineElement(cell.getMachine());
        String sequence = sequenceElement(cell.getSequence());
        return String.format("%s%s%s%s%s", date, code, dataCenter, machine, sequence);
    }


    private String dateElement(Date date) {
        notNull(date);
        SimpleDateFormat sdf = new SimpleDateFormat(CELL_DATE_FORMAT);
        return sdf.format(date);
    }

    private String codeElement(String code) {
        notBlank(code);
        expectedLength(code, CELL_CODE_LEN);
        return code;
    }

    private String dataCenterElement(Integer dataCenter) {
        notNull(dataCenter);
        expectedLength(String.valueOf(dataCenter), CELL_DATA_CENTER_LEN);
        return fillElement(Integer.toUnsignedLong(dataCenter), CELL_DATA_CENTER_LEN);
    }

    private String machineElement(Integer machine) {
        notNull(machine);
        expectedLength(String.valueOf(machine), CELL_MACHINE_LEN);
        return fillElement(Integer.toUnsignedLong(machine), CELL_MACHINE_LEN);
    }

    private String sequenceElement(Long sequence) {
        notNull(sequence);
        expectedLength(String.valueOf(sequence), CELL_SEQUENCE_LEN);
        return fillElement(sequence, CELL_SEQUENCE_LEN);
    }

    private String fillElement(Long element, int len) {
        String value = String.valueOf(element);
        int gap = len - value.length();
        while (gap-- > 0) {
            value = String.format("%s%s", 0, value);
        }
        return value;
    }

    private static void notNull(Object element) {
        if (element == null) {
            throw new CellException("cell element is null");
        }
    }

    private static void notBlank(String element) {
        if (element == null || element.trim().length() == 0) {
            throw new CellException("cell element is blank");
        }
    }

    private static void expectedLength(String element, int len) {
        if (element.length() != len) {
            throw new CellException(String.format("cell element(%s) not expected length(%s)", element, len));
        }
    }
}
