package com.aireview.review.domains.subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long>, RevisionRepository<Subscription, Long, Long> {
    Optional<Subscription> findByUserIdAndStatus(Long userId, Subscription.Status status);

    Optional<Subscription> findByUserIdAndStatusIn(Long userId, Set<Subscription.Status> statuses);
}
