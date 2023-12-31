# Cell ( 细胞 )
[![Maven Central](https://img.shields.io/maven-central/v/com.lazycece.cell/cell-spring-boot-starter)](https://search.maven.org/search?q=cell-spring-boot-starter)
[![License](https://img.shields.io/badge/license-Apache--2.0-green)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![GitHub release](https://img.shields.io/badge/release-download-orange.svg)](https://github.com/lazycece/cell/releases)

![cell-logo](/document/logo/cell-logo2.png)

Cell（细胞）是一个高性能的分布式ID生成框架，提供规范性、可理解性的ID生成策略，开箱即用。
在现有提供的策略之外，亦可自定义ID规范并利用Cell的内核扩展出符合诉求的ID生成器。

## Architecture
![cell-architecture](/document/design/cell-archirecture.png)


## 环境依赖

Cell 环境依赖如下:

|cell|Java|Spring Boot|
|---|---|---|
|1.x|17+|3.x|

## 快速开始

### Maven Dependency

```xml
<dependency>
    <groupId>com.lazycece.cell</groupId>
    <artifactId>cell-spring-boot-starter</artifactId>
    <version>${cell.version}</version>
</dependency>
```

### 脚本执行

在数据库中执行[脚本](/document/script/cell_registry.sql)创建注册表，并注册相关内容:

```sql
CREATE TABLE IF NOT EXISTS `cell_registry`(
    `id` INT UNSIGNED AUTO_INCREMENT COMMENT 'pk id',
    `name` VARCHAR(255) NOT NULL COMMENT 'cell name',
    `value` INT NOT NULL COMMENT 'current value',
    `min_value` INT NOT NULL COMMENT 'min value',
    `max_value` INT NOT NULL COMMENT 'max value',
    `step` INT NOT NULL COMMENT 'step',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
 ) COMMENT 'cell registry'
;

insert into cell_registry(`name`,`value`,`min_value`,`max_value`,`step`,`create_time`,`update_time`)
values("order",0,0,100000,100,now(),now())
;
```

### Coding

```java
@RestController
public class CellSpringSampleController {

    @Autowired
    private CellFacade cellFacade;

    @GetMapping("/cell-spec/orderId")
    public String getOrderId() {
        return cellFacade.generateId(CellEnum.ORDER);
    }
}
```

更多样例详情可查看 [cell-samples](/cell-samples)


## Cell 简介

Cell 分三个模块：

- cell-core: Cell内核，用于生成唯一的序列号
- cell-specification: Cell规范，用于定义并生成ID
- cell-spring-boot-starter: Cell启动器，用于Spring Boot 项目的快速引入，并进行配置扩展

### Cell Core
Cell内核是利用数据库注册模式来生成唯一序列号。 Cell注册表模型如下：
> 数据库脚本位置 [./document/script](/document/script/cell_registry.sql)

|字段名称 |类型 |描述 |
|--- |--- |--- |
|id |INT |数据库主键ID |
|name |VARCHAR |cell名称，拥有唯一性 |
|value |INT |cell序列号当前值 |
|min_value |INT |cell序列号最小值|
|max_value |INT |cell序列号最大值，达到最大值后从最小值进行循环|
|step |INT |cell序列号获取步长 |
|create_time |TIMESTAMP |创建时间 |
|update_time |TIMESTAMP |更新时间 |

Cell内核通过缓存来控制对外提供序列号值，确保获取序列号的唯一性。可引用该模块并结合自己业务中ID的规范来定制化出更具个性化的ID值。

### Cell Specification

Cell的ID规范由时间、领域标识码、数据中心、机房和唯一序列号等部分内容组成。从ID的长度和时间范围内ID生成支持
的最大范围考虑，Cell支持三种模式：天、小时、分钟。

天级别模式，cell-id的长度为24位：
- 组件构成：日期（8）+ 领域标识码（3）+ 数据中心（1）+ 机房（2）+ 序列号（10）
- ID样例：[20231004] [101] [1] [01] [1314230822]
- 支持最大qps = Integer.MAX_VALUE/24/60/60 = 24855

小时级别模式，cell-id的长度为26位：
- 组件构成：日期（8）+ 领域标识码（3）+ 数据中心（1）+ 机房（2）+ 小时（2）+ 序列号（10）
- ID样例： [20231004] [101] [1] [01] [11] [1314230822]
- 支持最大qps = Integer.MAX_VALUE/60/60 = 596523

分钟级别模式，cell-id的长度为28位：
- 组件构成：日期（8）+ 领域标识码（3）+ 数据中心（1）+ 机房（2）+ 小时（2）+ 分钟（2）+ 序列号（10）
- ID样例： [20231004] [101] [1] [01] [11] [58] [1314230822]
- 支持最大qps = Integer.MAX_VALUE/60 = 35791394


### Cell Spring Boot Starter

Cell 在 Spring Boot 项目中使用时，支持直接在配置文件中对Cell进行定制化。

|配置 |默认值 |描述 |
|--- |---   |--- |
|cell.cell-type-class |无 |如果有配置，服务启动时会自动注册到数据库表；如果不设置，需要自己手动在数据库表设置值 |
|cell.specification.pattern |DAY |天模式 |
|cell.specification.data-center |1 |数据中心，值范围[0,9] |
|cell.specification.machine |1 |机房，值范围[0,99] |
|cell.specification.min-value |0 |序列号最小值，自动注册Cell时使用 |
|cell.specification.max-value |Integer.MAX_VALUE |序列号最大值，自动注册Cell时使用 |
|cell.specification.step |6000 |序列号刷新步长，自动注册Cell时使用 |
|cell.buffer.expansion-step-elasticity-time |2 |buffer扩容时步长弹性次数，用于动态调整buffer大小 |
|cell.buffer.expansion-threshold |0.75 |buffer扩容阙值 |
|cell.buffer.expansion-interval |10m |buffer扩容时间间隔，动态扩缩容的衡量标准|
|cell.buffer.thread-pool-core-size |5 |buffer的核心线程数 |
|cell.buffer.thread-pool-max-size |Integer.MAX_VALUE |buffer的最大线程数 |
|cell.buffer.thread-pool-keep-alive-time |60s |线程活跃时间 |

## License

[Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0.html)