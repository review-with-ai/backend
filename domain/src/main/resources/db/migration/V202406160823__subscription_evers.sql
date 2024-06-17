CREATE TABLE `revinfo`
(
    `rev`      bigint NOT NULL AUTO_INCREMENT,
    `revtstmp` bigint DEFAULT NULL,
    PRIMARY KEY (`rev`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3;

CREATE TABLE `subscription_aud`
(
    `rev`             bigint NOT NULL COMMENT 'rev 테이블 식별자',
    `revtype`         tinyint                            DEFAULT NULL COMMENT '0-insert, 1-update, 2-delete',
    `cancelled_at`    datetime                           DEFAULT NULL,
    `end_date`        date                               DEFAULT NULL,
    `id`              bigint NOT NULL,
    `start_date`      date                               DEFAULT NULL,
    `user_id`         bigint                             DEFAULT NULL,
    `partner_user_id` varchar(50)                        DEFAULT NULL,
    `sid`             varchar(100)                       DEFAULT NULL,
    `status`          enum ('ACTIVE','CANCEL','EXPIRED') DEFAULT NULL,
    PRIMARY KEY (`rev`, `id`),
    CONSTRAINT `FKni5i9nnkyymy3o3km6rtsb44a` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3;

