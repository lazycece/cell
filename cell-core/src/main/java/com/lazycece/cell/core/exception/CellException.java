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

/**
 * @author lazycece
 * @date 2023/8/30
 */
public class CellException extends RuntimeException {

    public CellException() {
        super();
    }

    public CellException(String message) {
        super(message);
    }

    public CellException(String message, Throwable cause) {
        super(message, cause);
    }

    public CellException(Throwable cause) {
        super(cause);
    }
}
