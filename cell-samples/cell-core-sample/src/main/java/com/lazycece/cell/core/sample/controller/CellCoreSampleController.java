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

package com.lazycece.cell.core.sample.controller;

import com.lazycece.cell.core.buffer.BufferThreadFactory;
import com.lazycece.cell.core.buffer.CellBufferManager;
import com.lazycece.cell.core.exception.CellException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;

/**
 * @author lazycece
 * @date 2023/11/5
 */
@RestController
@ConditionalOnBean(value = CellBufferManager.class)
public class CellCoreSampleController implements BeanPostProcessor, CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(CellCoreSampleController.class);

    @GetMapping("/cell-core/{name}/getSequence")
    public int getSequence(@PathVariable String name) {
        return CellBufferManager.getInstance().getSequence(name);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof CellBufferManager) {
            CellBufferManager.getInstance().initCache();
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    @Override
    public void run(String... args) {
        run();
    }

    private void run() {
        ExecutorService executorService = new ThreadPoolExecutor(
                10,
                10000,
                60,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new BufferThreadFactory("GetSequenceRun"),
                new ThreadPoolExecutor.AbortPolicy()
        );

        for (int i = 0; i < 999; i++) {
            executorService.execute(() -> {
                try {
                    int sequence = CellBufferManager.getInstance().getSequence("order");
                    String element = fillElement(sequence, 3);
                    log.info("sequence element is [{}]", element);
                } catch (CellException e) {
                    log.error("cell exception", e);
                }
            });
        }
    }

    private String fillElement(Integer element, int len) {
        String value = String.valueOf(element);
        int gap = len - value.length();
        while (gap-- > 0) {
            value = String.format("%s%s", 0, value);
        }
        return value;
    }
}
