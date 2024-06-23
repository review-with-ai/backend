package com.aireview.review.service;

import com.aireview.review.domains.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void findUserCategories(Long userId, Pageable pageable) {
        categoryRepository.findByUserId(userId, pageable);
    }
}
