CREATE TABLE `payment`
(
    `id`              bigint           NOT NULL AUTO_INCREMENT COMMENT '결제 식별자',
    `subscription_id` bigint           NOT NULL COMMENT '구독권 식별자',
    `tid`             varchar(100)     NOT NULL COMMENT '카카오페이 결제 고유번호',
    `sid`             varchar(100)     NOT NULL COMMENT '결제에 사용된 카카오 페이 정기 결제용 ID, 첫 결제인 경우 null',
    `order_id`        varchar(100)     NOT NULL COMMENT '결제에 사용된 카카오 페이 주문 id',
    `amount`          int unsigned     NOT NULL COMMENT '결제 금액',
    `seq`             tinyint unsigned NOT NULL COMMENT '결제 회차',
    `event_type`      varchar(30)      NOT NULL COMMENT '이벤트 타입(SUCCESS, FAIL, CANCEL)',
    `details`         varchar(100) DEFAULT NULL COMMENT '참고 ex: 구독 취소 사유 등.',
    `timestamp`       timestamp        NOT NULL COMMENT '이벤트 발생 시각',
    `created_by`      bigint           NOT NULL COMMENT '결제한 사용자 식별자',
    PRIMARY KEY (`id`),
    KEY `subscription_id` (`subscription_id`),
    CONSTRAINT `payment_ibfk_1` FOREIGN KEY (`subscription_id`) REFERENCES `subscription` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb3;
