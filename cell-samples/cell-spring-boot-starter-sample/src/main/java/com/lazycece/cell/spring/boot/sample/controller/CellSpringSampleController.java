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

package com.lazycece.cell.spring.boot.sample.controller;

import com.lazycece.cell.specification.CellFacade;
import com.lazycece.cell.spring.boot.sample.model.CellEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lazycece
 * @date 2023/11/6
 */
@RestController
public class CellSpringSampleController {

    @Autowired
    private CellFacade cellFacade;

    @GetMapping("/cell-spec/orderId")
    public String getOrderId() {
        return cellFacade.generateId(CellEnum.ORDER);
    }

    @GetMapping("/cell-spec/goodsId")
    public String getGoodsId() {
        return cellFacade.generateId(CellEnum.GOODS);
    }
}
