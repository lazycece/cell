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
import com.lazycece.cell.core.exception.CellAssert;
import com.lazycece.cell.specification.CellFacade;
import com.lazycece.cell.specification.configuration.CellSpecConfiguration;
import com.lazycece.cell.specification.impl.CellFacadeImpl;
import com.lazycece.cell.specification.model.CellSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author lazycece
 * @date 2023/10/31
 */
@Configuration
@ComponentScan(basePackages = {"com.lazycece.cell"})
@EnableConfigurationProperties({CellProperties.class})
@AutoConfigureBefore(value = {CellFacade.class, CellBufferManager.class})
public class CellAutoConfiguration implements BeanPostProcessor, InitializingBean {

    private final Logger log = LoggerFactory.getLogger(CellAutoConfiguration.class);
    private BufferConfiguration bufferConfiguration;
    @Autowired
    private CellProperties cellProperties;

    /**
     * @see InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() {
        // check cell
        CellAssert.notNull(cellProperties.getCellTypeClass(), "Cell check: cellTypeClass is null");

        // check specification
        CellSpecConfiguration spec = cellProperties.getSpecification();
        CellAssert.isTrue(spec.getDataCenter() < Math.pow(10, CellSpec.CELL_DATA_CENTER_LEN), "Cell specification check: dataCenter length limit %s", CellSpec.CELL_DATA_CENTER_LEN);
        CellAssert.isTrue(spec.getMachine() < Math.pow(10, CellSpec.CELL_MACHINE_LEN), "Cell specification check: machine length limit %s", CellSpec.CELL_MACHINE_LEN);
        CellAssert.isTrue(spec.getMinValue() < spec.getMaxValue(), "Cell specification check: limit minValue<maxValue");
        CellAssert.isTrue(spec.getStep() < spec.getMaxValue() && spec.getStep() > spec.getMinValue(), "Cell specification check: limit minValue<step<maxValue");

        // assemble buffer configuration
        CellProperties.CellBufferProperties buffer = cellProperties.getBuffer();
        int multiple = (int) Math.pow(2, buffer.getExpansionStepElasticityTime());
        bufferConfiguration = new BufferConfiguration();
        bufferConfiguration.setExpansionThreshold(buffer.getExpansionThreshold());
        bufferConfiguration.setExpansionInterval(buffer.getExpansionInterval());
        bufferConfiguration.setExpansionMinStep(spec.getStep() / multiple);
        bufferConfiguration.setExpansionMaxStep(spec.getStep() * multiple);
        bufferConfiguration.setThreadPoolCoreSize(buffer.getThreadPoolCoreSize());
        bufferConfiguration.setThreadPoolMaxSize(buffer.getThreadPoolMaxSize());
        bufferConfiguration.setThreadPoolKeepAliveTime(buffer.getThreadPoolKeepAliveTime());

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
            cellFacade.setConfiguration(cellProperties.getSpecification());
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
