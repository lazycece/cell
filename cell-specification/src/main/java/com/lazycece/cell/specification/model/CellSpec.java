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

package com.lazycece.cell.specification.model;

import com.lazycece.cell.specification.exception.CellSpecException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Define the cell specification .
 *
 * <p>
 * It has three pattern: day, hour, minute
 * </p>
 * <p>
 * The day pattern, length of cell-id is 24, as follow:
 * <ul>
 * <li>component: date(8) + code(3) + center(1) + machine(2) + sequence(10)</li>
 * <li>example: [20231004] [001] [1] [01] [2147483647]</li>
 * <li>support max qps = <code>Integer.MAX_VALUE</code>/24/60/60 = 24855 </li>
 * </ul>
 * </p>
 * <p>
 * The hour pattern, length of cell-id is 26, as follow:
 * <ul>
 * <li>component: date(8) + code(3) + center(1) + machine(2) + hour(2) + sequence(10)</li>
 * <li>example: [20231004] [001] [0] [01] [11] [2147483647]</li>
 * <li>support max qps = <code>Integer.MAX_VALUE</code>/60/60 = 596523 </li>
 * </ul>
 * </p>
 * <p>
 * The minute pattern, length of cell-id is 28, as follow:
 * <ul>
 * <li>component: date(8) + code(3) + center(1) + machine(2) + hour(2) + minute(2) + sequence(10)</li>
 * <li>example: [20231004] [001] [0] [01] [11] [58] [2147483647]</li>
 * <li>support max qps = <code>Integer.MAX_VALUE</code>/60 = 35791394 </li>
 * </ul>
 * </p>
 *
 * @author lazycece
 * @date 2023/8/31
 * @see Cell
 * @see CellPattern
 */
public class CellSpec {

    private static final String CELL_DATE_FORMAT = "yyyyMMdd";
    private static final String CELL_TIME_FORMAT = "HHmmss";
    private static final int CELL_CODE_LEN = 3;
    private static final int CELL_DATA_CENTER_LEN = 1;
    private static final int CELL_MACHINE_LEN = 2;
    private static final int CELL_HOUR_LEN = 2;
    private static final int CELL_MINUTE_LEN = 4;
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
        String time = timeElement(cell.getDate(), cell.getPattern());
        String sequence = sequenceElement(cell.getSequence());
        return String.format("%s%s%s%s%s%s", date, code, dataCenter, machine, time, sequence);
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
        return fillElement(dataCenter, CELL_DATA_CENTER_LEN);
    }

    private String machineElement(Integer machine) {
        notNull(machine);
        expectedLength(String.valueOf(machine), CELL_MACHINE_LEN);
        return fillElement(machine, CELL_MACHINE_LEN);
    }

    private String timeElement(Date date, CellPattern pattern) {
        notNull(date);
        notNull(pattern);
        SimpleDateFormat sdf = new SimpleDateFormat(CELL_TIME_FORMAT);
        String time = sdf.format(date);
        return switch (pattern) {
            case HOUR -> time.substring(0, CELL_HOUR_LEN);
            case MINUTE -> time.substring(0, CELL_MINUTE_LEN);
            default -> "";
        };
    }

    private String sequenceElement(Integer sequence) {
        notNull(sequence);
        expectedLength(String.valueOf(sequence), CELL_SEQUENCE_LEN);
        return fillElement(sequence, CELL_SEQUENCE_LEN);
    }

    private String fillElement(Integer element, int len) {
        String value = String.valueOf(element);
        int gap = len - value.length();
        while (gap-- > 0) {
            value = String.format("%s%s", 0, value);
        }
        return value;
    }

    private static void notNull(Object element) {
        if (element == null) {
            throw new CellSpecException("cell element is null");
        }
    }

    private static void notBlank(String element) {
        if (element == null || element.trim().length() == 0) {
            throw new CellSpecException("cell element is blank");
        }
    }

    private static void expectedLength(String element, int len) {
        if (element.length() <= len) {
            throw new CellSpecException(String.format("cell element(%s) not expected length(%s)", element, len));
        }
    }
}
