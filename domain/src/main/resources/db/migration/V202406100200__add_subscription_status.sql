ALTER TABLE subscription
    MODIFY COLUMN status enum (
        'PENDING',
        'ACTIVE',
        'CANCELED',
        'EXPIRED')
        COMMENT 'PENDING(결제전), ACTIVE(활성화), CANCELED(현재구독주기끝에취소예정), EXPIRED(만료됨)';
ALTER TABLE subscription
    ADD COLUMN sid varchar(100)
        COMMENT '카카오페이 정기 결제용 sid';

