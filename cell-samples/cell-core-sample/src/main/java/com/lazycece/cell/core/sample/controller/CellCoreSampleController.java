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

import com.lazycece.cell.core.buffer.CellBufferManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lazycece
 * @date 2023/11/5
 */
@RestController
@ConditionalOnBean(value = CellBufferManager.class)
public class CellCoreSampleController implements BeanPostProcessor {

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
}
