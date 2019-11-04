
-- 创建数据库shard0、shard1、shard2

---------------------shard0、shard1数据库---------------------
DROP TABLE IF EXISTS dictionary;
CREATE TABLE `dictionary` (
  `dictionary_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`dictionary_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

DROP TABLE IF EXISTS order0;
CREATE TABLE `order0` (
  `order_id` bigint(32) NOT NULL,
  `user_id` bigint(32) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `total_price` int(10) NOT NULL,
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

DROP TABLE IF EXISTS order1;
CREATE TABLE `order1` (
  `order_id` bigint(32) NOT NULL,
  `user_id` bigint(32) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `total_price` int(10) NOT NULL,
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS order_item0;
CREATE TABLE `order_item0` (
  `order_item_id` bigint(32) NOT NULL,
  `order_id` bigint(32) NOT NULL,
  `name` varchar(255) NOT NULL,
  `price` int(10) NOT NULL,
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`order_item_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

DROP TABLE IF EXISTS order_item1;
CREATE TABLE `order_item1` (
  `order_item_id` bigint(32) NOT NULL,
  `order_id` bigint(32) NOT NULL,
  `name` varchar(255) NOT NULL,
  `price` int(10) NOT NULL,
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`order_item_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;


DROP TABLE IF EXISTS user;
CREATE TABLE `user` (
  `user_id` bigint(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `age` int(11) NOT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

---------------------shard2数据库---------------------
DROP TABLE IF EXISTS other_table;
CREATE TABLE `other_table` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;


