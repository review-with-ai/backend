ALTER TABLE category
    ADD COLUMN created_by BIGINT NOT NULL, -- 생성자 식별자
    ADD COLUMN updated_by BIGINT NOT NULL; -- 수정자 식별자
ALTER TABLE note
    ADD COLUMN created_by BIGINT NOT NULL, -- 생성자 식별자
    ADD COLUMN updated_by BIGINT NOT NULL; -- 수정자 식별자
ALTER TABLE quiz
    ADD COLUMN created_by BIGINT NOT NULL, -- 생성자 식별자
    ADD COLUMN updated_by BIGINT NOT NULL; -- 수정자 식별자
ALTER TABLE subscription
    ADD COLUMN created_by BIGINT NOT NULL, -- 생성자 식별자
    ADD COLUMN updated_by BIGINT NOT NULL; -- 수정자 식별자
ALTER TABLE coupon
    ADD COLUMN created_by BIGINT NOT NULL, -- 생성자 식별자
    ADD COLUMN updated_by BIGINT NOT NULL; -- 수정자 식별자
ALTER TABLE coupon_type
    ADD COLUMN created_by BIGINT NOT NULL, -- 생성자 식별자
    ADD COLUMN updated_by BIGINT NOT NULL; -- 수정자 식별자
ALTER TABLE user
    ADD COLUMN created_by BIGINT NOT NULL, -- 생성자 식별자
    ADD COLUMN updated_by BIGINT NOT NULL; -- 수정자 식별자
ALTER TABLE payment
    ADD COLUMN created_at datetime NOT NULL, -- 생성 시각
    ADD COLUMN updated_at datetime NOT NULL, -- 수정 시각
    ADD COLUMN updated_by BIGINT   NOT NULL; -- 수정자 식별자
