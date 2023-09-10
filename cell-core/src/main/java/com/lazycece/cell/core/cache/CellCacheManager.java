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

package com.lazycece.cell.core.cache;

import com.lazycece.cell.core.model.CellTable;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lazycece
 * @date 2023/9/9
 */
public class CellCacheManager {

    private static final ConcurrentHashMap<String, CellTable> cacheMap = new ConcurrentHashMap<>();

    public static void put(String name, CellTable cellTable) {
        cacheMap.put(name, cellTable);
    }

    public static CellTable get(String name) {
        return cacheMap.get(name);
    }
}
