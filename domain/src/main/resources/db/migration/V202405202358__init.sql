CREATE TABLE `user`
(
    `id`             bigint                                                                                NOT NULL AUTO_INCREMENT COMMENT '유저 식별자',
    `name`           varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci                          NOT NULL COMMENT '유저 이름',
    `nickname`       varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci                          NOT NULL COMMENT '유저 닉네임',
    `email`          varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci                         NOT NULL COMMENT '유저 이메일',
    `password`       varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '유저 패스워드. oauth 가입 유저의 경우 null',
    `oauth_user_id`  varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'oauth 인증 서버의 유저 식별자',
    `oauth_provider` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci  DEFAULT NULL COMMENT 'oauth 인증 제공자 ex) naver, kakao',
    `has_subscribed` tinyint(1)                                                                            NOT NULL COMMENT '이용권 구독 여부. 1이면 구독 0이면 구독 안함.',
    `status`         enum ('ENABLED','BLOCKED','DELETED') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '유저 상태(활성, 정지, 삭제)',
    `created_at`     datetime                                                                              NOT NULL COMMENT '유저 가입 시각',
    `updated_at`     datetime                                                                              NOT NULL COMMENT '유저 정보 마지막으로 수정된 시각',
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_email_idx` (`email`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3;


CREATE TABLE `category`
(
    `id`         bigint                                                       NOT NULL AUTO_INCREMENT COMMENT '카테고리 식별자',
    `user_id`    bigint                                                       NOT NULL COMMENT '카테고리 생성한 유저 식별자',
    `title`      varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '카테고리 이름',
    `order`      tinyint                                                      NOT NULL COMMENT '카테고리 노출 순서',
    `created_at` datetime                                                     NOT NULL COMMENT '카테고리 생성 시각',
    `updated_at` datetime                                                     NOT NULL COMMENT '카테고리가 마지막으로 수정된 시각',
    `is_deleted` tinyint(1)                                                   NOT NULL COMMENT '카테고리 삭제 여부. 1이면 삭제된 상태.',
    PRIMARY KEY (`id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `category_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3;

CREATE TABLE `coupon_type`
(
    `id`          bigint                                                                                      NOT NULL AUTO_INCREMENT COMMENT '쿠폰 타입 식별자',
    `name`        varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci                               NOT NULL COMMENT '쿠폰 타입 이름 ex-선착순 이용권 쿠폰',
    `description` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci                               NOT NULL COMMENT '쿠폰 타입 설명',
    `created_at`  datetime                                                                                    NOT NULL COMMENT '쿠폰 타입 생성 시각',
    `updated_at`  datetime                                                                                    NOT NULL COMMENT '쿠폰 타입이 마지막으로 업데이트된 시각',
    `status`      enum ('AVAILABLE','UNAVAILABLE','EXPIRED') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '쿠폰 타입 상태. 사용 중/사용 불가',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3;

CREATE TABLE `coupon`
(
    `id`             bigint                                                                                   NOT NULL AUTO_INCREMENT COMMENT '쿠폰 식별자',
    `code`           varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci                             NOT NULL COMMENT '쿠폰 코드(랜덤 스트링, 유저에게 노출되는 쿠폰 코드)',
    `user_id`        bigint                                                                                   NOT NULL COMMENT '쿠폰을 발급받은 유저',
    `coupon_type_id` bigint                                                                                   NOT NULL COMMENT '쿠폰 타입 식별자',
    `issued_at`      datetime                                                                                 NOT NULL COMMENT '쿠폰 발급 시각',
    `used_at`        datetime DEFAULT NULL COMMENT '쿠폰 사용 시각(쿠폰 사용 전 null)',
    `created_at`     datetime                                                                                 NOT NULL COMMENT '쿠폰 생성 시각',
    `updated_at`     datetime                                                                                 NOT NULL COMMENT '쿠폰이 마지막으로 업데이트 된 시각',
    `status`         enum ('AVAILABLE','USED','UNAVAILABLE') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '쿠폰 상태(사용 가능, 사용 후, 사용 불가능)',
    PRIMARY KEY (`id`),
    KEY `coupon_type_id` (`coupon_type_id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `coupon_ibfk_1` FOREIGN KEY (`coupon_type_id`) REFERENCES `coupon_type` (`id`),
    CONSTRAINT `coupon_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3;


CREATE TABLE `note`
(
    `id`          bigint                                                         NOT NULL AUTO_INCREMENT COMMENT '노트 식별자',
    `title`       varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci  NOT NULL COMMENT '노트 제목',
    `content`     varchar(5000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '노트 내용',
    `user_id`     bigint                                                         NOT NULL COMMENT '노트 작성자 식별자',
    `category_id` bigint                                                         NOT NULL COMMENT '노트가 속한 카테고리 식별자',
    `created_at`  datetime                                                       NOT NULL COMMENT '노트 생성 시각',
    `updated_at`  datetime                                                       NOT NULL COMMENT '노트가 마지막으로 업데이트 된 시각',
    `is_deleted`  tinyint(1)                                                     NOT NULL COMMENT '노트 삭제 여부. 1이면 삭제된 상태.',
    PRIMARY KEY (`id`),
    KEY `user_id` (`user_id`),
    KEY `category_id` (`category_id`),
    CONSTRAINT `note_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `note_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3;

CREATE TABLE `quiz`
(
    `id`         bigint                                                                     NOT NULL AUTO_INCREMENT COMMENT '퀴즈 식별자',
    `note_id`    bigint                                                                     NOT NULL COMMENT '퀴즈과 관련된 노트 식별자',
    `user_id`    bigint                                                                     NOT NULL COMMENT '퀴즈와 관련된 노트 작성자의 식별자.',
    `question`   varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci              NOT NULL COMMENT '질문',
    `answer`     varchar(300) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci              NOT NULL COMMENT '답변',
    `status`     enum ('REVIEWING','DONE') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '복습 상태(복습중/복습완료)',
    `created_at` datetime                                                                   NOT NULL COMMENT '퀴즈가 생성된 시각',
    `updated_at` datetime                                                                   NOT NULL COMMENT '퀴즈가 마지막으로 업데이트된 시각',
    `is_deleted` tinyint(1)                                                                 NOT NULL COMMENT '퀴즈 삭제 여부. 1이면 삭제',
    PRIMARY KEY (`id`),
    KEY `note_id` (`note_id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `question_ibfk_1` FOREIGN KEY (`note_id`) REFERENCES `note` (`id`),
    CONSTRAINT `question_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3;

CREATE TABLE `subscription`
(
    `id`           bigint                                                                                 NOT NULL AUTO_INCREMENT COMMENT '구독 식별자',
    `user_id`      bigint                                                                                 NOT NULL COMMENT '구독한 유저 식별자',
    `start_date`   date                                                                                   NOT NULL COMMENT '구독 시작 날짜',
    `end_date`     date                                                                                   NOT NULL COMMENT '구독 만료 날짜. 취소하지 않을 경우 최대 날짜(2100-01-01). 구독 취소시 구독 만료 날짜.',
    `cancelled_at` datetime DEFAULT NULL COMMENT '구독 취소 시각',
    `created_at`   datetime                                                                               NOT NULL COMMENT '구독 생성 시각',
    `updated_at`   datetime                                                                               NOT NULL COMMENT '구독이 마지막으로 업데이트된 시각',
    `status`       enum ('ACTIVE','CANCELLED','EXPIRED') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '구독 상태(구독중, 구독 취소 예정, 만료)',
    PRIMARY KEY (`id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `subscription_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3;

