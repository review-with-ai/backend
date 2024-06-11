ALTER TABLE `payment`
    ADD UNIQUE INDEX `unique_payment_seq` (`subscription_id`,`seq`) USING BTREE;
