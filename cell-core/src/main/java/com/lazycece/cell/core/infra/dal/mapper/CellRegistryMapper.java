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

package com.lazycece.cell.core.infra.dal.mapper;

import com.lazycece.cell.core.infra.dal.po.CellRegistryPO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author lazycece
 * @date 2023/9/10
 */
@Mapper
public interface CellRegistryMapper {

    /**
     * To judge cell registry table exist or not.
     */
    @Select("SELECT count(*) FROM information_schema.TABLES WHERE table_name ='cell_registry'")
    int existCellRegistry();

    /**
     * Insert cell registry.
     *
     * @param po ${@link CellRegistryPO}
     */
    @Insert({"INSERT INTO cell_registry(name, value, min_value, max_value, step, create_time, update_time) ",
            "VALUES (#{name},#{value},#{minValue},#{maxValue},#{step},#{createTime},#{updateTime})"})
    void insert(CellRegistryPO po);

    /**
     * Find cell registry by cell's name.
     *
     * @param name cell name
     * @return see ${@link CellRegistryPO}
     */
    @Select("SELECT id, name, value, min_value, max_value, step, create_time, update_time FROM cell_registry WHERE name = #{name}")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "value", property = "value"),
            @Result(column = "min_value", property = "minValue"),
            @Result(column = "max_value", property = "maxValue"),
            @Result(column = "step", property = "step"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    CellRegistryPO findByName(String name);

    /**
     * Find cell registry by cell's name.
     *
     * @param name cell name
     * @return see ${@link CellRegistryPO}
     */
    @Select("SELECT id, name, value, min_value, max_value, step, create_time, update_time FROM cell_registry WHERE name = #{name} FOR UPDATE")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "value", property = "value"),
            @Result(column = "min_value", property = "minValue"),
            @Result(column = "max_value", property = "maxValue"),
            @Result(column = "step", property = "step"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    CellRegistryPO lockFindByName(String name);

    /**
     * Find all cell registry name.
     *
     * @return cell registry name list
     */
    @Select("SELECT name FROM cell_registry")
    List<String> findAllName();

    /**
     * Update by cell registry name.
     *
     * @return result
     */
    @Update("UPDATE cell_registry SET value = value + step WHERE name = #{name}")
    int updateValueByName(String name);

    /**
     * Update by cell registry name, use custom step.
     *
     * @return result
     */
    @Update("UPDATE cell_registry SET value = value + #{step} WHERE name = #{name}")
    int updateValueByNameWithGivenStep(@Param("name") String name, @Param("step") Integer step);

    /**
     * Update by cell registry name, to reset value.
     *
     * @return result
     */
    @Update("UPDATE cell_registry SET value = min_value WHERE name = #{name}")
    int updateValueByReset(String name);
}
