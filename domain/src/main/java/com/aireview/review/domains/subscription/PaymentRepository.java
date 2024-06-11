package com.aireview.review.domains.subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query(value = "SELECT seq FROM payment where subscription_id = :subscriptionId ORDER BY seq DESC LIMIT 1", nativeQuery = true)
    Byte findMaxSeqBySubscriptionId(Long subscriptionId);
}
