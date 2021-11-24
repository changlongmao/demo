CREATE TABLE `user` (
                        `id` char(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                        `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                        `rear_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                        `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                        `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                        `is_delete` tinyint(1) DEFAULT '1',
                        PRIMARY KEY (`id`) USING BTREE,
                        KEY `idx_username` (`username`) USING BTREE,
                        KEY `联合索引` (`username`,`rear_name`,`password`,`create_time`) USING BTREE,
                        KEY `idx_rear_name` (`rear_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;