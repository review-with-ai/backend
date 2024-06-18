package com.aireview.review.config;

import com.aireview.review.common.Authenticated;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
        basePackages = "com.aireview.review.domains",
        repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
public class JpaConfiguration implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof Authenticated)) {
            return Optional.of(-1L);
        }
        Authenticated authenticated = (Authenticated) authentication.getPrincipal();
        return Optional.of(authenticated.getUserId());
    }
}
