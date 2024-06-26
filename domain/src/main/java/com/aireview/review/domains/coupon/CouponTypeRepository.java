package com.aireview.review.domains.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponTypeRepository extends JpaRepository<CouponType, Long> {
}
