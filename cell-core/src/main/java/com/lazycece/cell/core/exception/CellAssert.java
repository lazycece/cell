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

import java.util.Collection;

/**
 * @author lazycece
 * @date 2023/10/26
 */
public class CellAssert {

    public static void isTrue(final boolean bool, final String strFormat, final Object... args) {
        if (!bool) {
            throw new CellException(String.format(strFormat, args));
        }
    }

    public static void notNull(final Object obj, final String strFormat, final Object... args) {
        isTrue(obj != null, strFormat, args);
    }

    public static void notEmpty(final Collection<?> coll, final String strFormat, final Object... args) {
        isTrue(coll != null && !coll.isEmpty(), strFormat, args);
    }
}
