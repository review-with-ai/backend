ALTER TABLE subscription
    MODIFY COLUMN status varchar(10)
        COMMENT '구독 상태 예시 : ACTIVE(활성화), CANCELED(현재구독주기끝에취소예정), EXPIRED(만료됨)';
ALTER TABLE subscription
    ADD COLUMN sid varchar(100)
        DEFAULT NULL
        COMMENT '카카오페이 정기 결제용 sid';
ALTER TABLE subscription
    ADD COLUMN partner_user_id varchar(100)
        NOT NULL
        COMMENT '카카오페이 정기 결제용 userId 첫 주문시의 userId 저장 필요';

