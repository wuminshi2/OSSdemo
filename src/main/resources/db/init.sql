-- 创建数据库
CREATE DATABASE IF NOT EXISTS ossdemo
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE ossdemo;

-- 建表：file（MyBatis-Plus 默认映射）
DROP TABLE IF EXISTS `file`;

CREATE TABLE `file` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT,
                        `file_name` VARCHAR(255) NOT NULL,
                        `oss_url` VARCHAR(2048) NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;