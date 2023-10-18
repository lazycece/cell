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

package com.lazycece.cell.core.buffer;

import com.lazycece.cell.core.exception.CellException;
import com.lazycece.cell.core.infra.repository.CellTableRepository;
import com.lazycece.cell.core.model.CellTable;
import com.lazycece.cell.core.model.spec.CellBuilder;
import com.lazycece.cell.core.model.spec.CellType;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lazycece
 * @date 2023/9/9
 */
public class CellBufferManager {

    private static final ConcurrentHashMap<String/*name*/, CellBuffer> CACHE_MAP = new ConcurrentHashMap<>();
    private ExecutorService service = new ThreadPoolExecutor(5, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new UpdateThreadFactory());
    private CellTableRepository cellTableRepository;
    private AtomicBoolean ready = new AtomicBoolean(false);

    public void init() {
        if (ready.compareAndSet(false, true)) {

        }

    }

    public static void register(List<CellTable> cellTableList) {
        // 初始化cache
        // TODO: 2023/10/17  从数据库里捞取所有的数据进行cache
    }

    public static int getSequence(String name) {
        CellBuffer cellBuffer = CACHE_MAP.get(name);
        if (cellBuffer == null) {
            throw new CellException(String.format("cell (%s) buffer is null.", name));
        }


        // TODO: 2023/10/17  if 阙值进行扩容

    }


}
