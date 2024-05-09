ALTER TABLE `user`
    CHANGE `password` `password` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL, -- Oauth 가입 회원의 경우 null 가능하도록
    ADD COLUMN `oauth_user_id`  varchar(255) NULL, -- oauth 인증 서버 유저 식별자
    ADD COLUMN `oauth_provider` varchar(20)  NULL; -- oauth 인증 제공자(ex - naver, kakao)
