CREATE TABLE `user`
(
    `id`        bigint                                                          NOT NULL AUTO_INCREMENT, -- 사용자 PK
    `name`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    NOT NULL,                -- 사용자 이름
    `nickname`  varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    NOT NULL,                -- 사용자 닉네임
    `email`     varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    NOT NULL,                -- 사용자 이메일(로그인 시 아이디 역할)
    `password`  varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL,                -- 암호화된 패스워드
    `status`    enum ('ENABLED','BLOCKED','DELETED') COLLATE utf8mb4_general_ci NOT NULL,                -- 상태(활성화, 정지, 삭제)
    `updated_at` datetime                                                        NOT NULL,                -- 마지막으로 업데이트 된 날짜 및 시간
    `created_at` datetime                                                        NOT NULL,                -- 생성된 날짜 및 시간
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;
