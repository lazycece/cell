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

import com.lazycece.cell.core.configuration.BufferConfiguration;
import com.lazycece.cell.core.exception.CellAssert;
import com.lazycece.cell.specification.configuration.CellSpecConfiguration;
import com.lazycece.cell.specification.model.CellSpec;

/**
 * @author lazycece
 * @date 2023/11/10
 */
public class ConfigurationHelper {


    public static CellSpecConfiguration assembleCellSpecConfiguration(CellProperties.CellSpecProperties spec) {
        // check specification
        CellAssert.isTrue(spec.getDataCenter() < Math.pow(10, CellSpec.CELL_DATA_CENTER_LEN), "Cell specification check: dataCenter length limit %s", CellSpec.CELL_DATA_CENTER_LEN);
        CellAssert.isTrue(spec.getMachine() < Math.pow(10, CellSpec.CELL_MACHINE_LEN), "Cell specification check: machine length limit %s", CellSpec.CELL_MACHINE_LEN);
        CellAssert.isTrue(spec.getMinValue() < spec.getMaxValue(), "Cell specification check: limit minValue<maxValue");
        CellAssert.isTrue(spec.getStep() < (spec.getMaxValue() - spec.getMinValue()), "Cell specification check: limit step<(maxValue-minValue)");

        // assemble specification configuration
        CellSpecConfiguration specConfiguration = new CellSpecConfiguration();
        specConfiguration.setPattern(spec.getPattern());
        specConfiguration.setDataCenter(spec.getDataCenter());
        specConfiguration.setMachine(spec.getMachine());
        specConfiguration.setMinValue(spec.getMinValue());
        specConfiguration.setMaxValue(spec.getMaxValue());
        specConfiguration.setStep(spec.getStep());
        return specConfiguration;
    }

    public static BufferConfiguration assembleBufferConfiguration(CellProperties.CellSpecProperties spec, CellProperties.CellBufferProperties buffer) {
        // assemble buffer configuration
        int multiple = (int) Math.pow(2, buffer.getExpansionStepElasticityTime());
        BufferConfiguration bufferConfiguration = new BufferConfiguration();
        bufferConfiguration.setExpansionThreshold(buffer.getExpansionThreshold());
        bufferConfiguration.setExpansionInterval(buffer.getExpansionInterval().toMillis());
        bufferConfiguration.setExpansionMinStep(spec.getStep() / multiple);
        bufferConfiguration.setExpansionMaxStep(spec.getStep() * multiple);
        bufferConfiguration.setThreadPoolCoreSize(buffer.getThreadPoolCoreSize());
        bufferConfiguration.setThreadPoolMaxSize(buffer.getThreadPoolMaxSize());
        bufferConfiguration.setThreadPoolKeepAliveTime(buffer.getThreadPoolKeepAliveTime().toSeconds());
        return bufferConfiguration;
    }
}
