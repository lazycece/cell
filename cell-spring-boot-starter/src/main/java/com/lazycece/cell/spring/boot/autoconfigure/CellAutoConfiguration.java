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

package com.lazycece.cell.spring.boot.autoconfigure;

import com.lazycece.cell.core.buffer.CellBufferManager;
import com.lazycece.cell.core.configuration.BufferConfiguration;
import com.lazycece.cell.specification.configuration.CellSpecConfiguration;
import com.lazycece.cell.specification.impl.CellFacadeImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author lazycece
 * @date 2023/10/31
 */
@Configuration
@MapperScan(basePackages = {"com.lazycece.cell.core.infra.dal.mapper"})
@ComponentScan(basePackages = {"com.lazycece.cell"})
@EnableConfigurationProperties({CellProperties.class})
public class CellAutoConfiguration implements BeanPostProcessor, InitializingBean {

    private final Logger log = LoggerFactory.getLogger(CellAutoConfiguration.class);
    private final CellProperties cellProperties;
    private BufferConfiguration bufferConfiguration;
    private CellSpecConfiguration specConfiguration;

    @Autowired
    public CellAutoConfiguration(CellProperties cellProperties) {
        this.cellProperties = cellProperties;
    }

    /**
     * @see InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() {
        CellProperties.CellSpecProperties spec = cellProperties.getSpecification();
        specConfiguration = ConfigurationHelper.assembleCellSpecConfiguration(spec);
        CellProperties.CellBufferProperties buffer = cellProperties.getBuffer();
        bufferConfiguration = ConfigurationHelper.assembleBufferConfiguration(spec, buffer);
        log.info("Cell auto configuration successful.");
    }

    /**
     * @see BeanPostProcessor#postProcessAfterInitialization
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        cellFacadeInterceptor(bean);
        cellBufferManagerInterceptor(bean);
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    private void cellFacadeInterceptor(Object bean) {
        if (bean instanceof CellFacadeImpl cellFacade) {
            cellFacade.setCellTypeClass(cellProperties.getCellTypeClass());
            cellFacade.setConfiguration(specConfiguration);
            log.info("Cell auto configuration: set cell specification configuration.");
        }
    }

    private void cellBufferManagerInterceptor(Object bean) {
        if (bean instanceof CellBufferManager cellBufferManager) {
            cellBufferManager.setBufferConfig(bufferConfiguration);
            log.info("Cell auto configuration: set cell buffer configuration.");
        }
    }
}
