
CREATE TABLE IF NOT EXISTS `cell_registry`(
    `id` INT UNSIGNED AUTO_INCREMENT COMMENT 'pk id',
    `name` VARCHAR(255) NOT NULL COMMENT 'cell name',
    `value` BIGINT NOT NULL COMMENT 'current value',
    `min_value` BIGINT NOT NULL COMMENT 'min value',
    `max_value` BIGINT NOT NULL COMMENT 'max value',
    `step` INT NOT NULL COMMENT 'step',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
 ) COMMENT 'cell registry'
;
