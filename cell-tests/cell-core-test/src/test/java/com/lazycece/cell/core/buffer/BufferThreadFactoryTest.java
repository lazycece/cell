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

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lazycece
 * @date 2023/11/5
 */
public class BufferThreadFactoryTest {

    @Test
    public void test() {
        ExecutorService executorService = new ThreadPoolExecutor(
                1,
                100,
                10,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new BufferThreadFactory("test"),
                new ThreadPoolExecutor.AbortPolicy());
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                Thread thread = Thread.currentThread();
                System.out.printf("%s-%s-%s%n", thread.getName(), thread.isDaemon(), thread.getPriority());
            });
        }
    }
}
