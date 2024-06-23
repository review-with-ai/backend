package com.aireview.review.model;

import com.aireview.review.domains.category.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserCategoriesResponse {
    private Long userId;
    private Byte count;
    private List<CategoryResponse> categories;

    public static UserCategoriesResponse from(Category category) {
        // TODO: 6/23/24
        return null;
    }
}
