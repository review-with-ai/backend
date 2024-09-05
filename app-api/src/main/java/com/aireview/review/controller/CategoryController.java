package com.aireview.review.controller;

import com.aireview.review.common.Authenticated;
import com.aireview.review.model.UserCategoriesResponse;
import com.aireview.review.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<UserCategoriesResponse> getUserCategories(
            @AuthenticationPrincipal Authenticated auth,
            @PageableDefault(size = 10, page = 0, sort = {"order"}, direction = Sort.Direction.ASC) Pageable pageable
    ) {
        categoryService.findUserCategories(auth.getUserId(), pageable);
        // TODO: 6/23/24  
        return null;
    }
}
