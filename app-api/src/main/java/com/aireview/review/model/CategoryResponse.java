package com.aireview.review.model;

import com.aireview.review.domains.category.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryResponse {
    private String title;
    private Byte order;

    public CategoryResponse(String title, Byte order) {
        this.title = title;
        this.order = order;
    }

    public static CategoryResponse from(Category category){
        return new CategoryResponse(category.getTitle(), category.getOrder());
    }
}
