CREATE TABLE `user` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
                        `nickname` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
                        `email` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
                        `status` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;