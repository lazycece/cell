
CREATE TABLE IF NOT EXISTS `cell`(
    `id` INT UNSIGNED AUTO_INCREMENT COMMENT 'pk id',
    `name` VARCHAR(255) NOT NULL COMMENT 'cell name',
    `value` INT NOT NULL COMMENT 'current value',
    `min_value` INT NOT NULL COMMENT 'min value',
    `max_value` INT NOT NULL COMMENT 'max value',
    `step` INT NOT NULL COMMENT 'step',
    `create_time` TIMESTAMP NOT NULL COMMENT 'create time',
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
    CONSTRAINT pk_id PRIMARY KEY (`id`),
    CONSTRAINT uk_name UNIQUE KEY (`name`)
) COMMENT 'cell table'
;
