package com.aireview.review.domains.category;


import com.aireview.review.domains.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "title", columnDefinition = "varchar(50)", nullable = false)
    private String title;

    @Column(name = "order", columnDefinition = "tinyint", nullable = false)
    private Byte order;

    @Column(name = "is_deleted", columnDefinition = "tinyint(1)", nullable = false)
    private boolean isDeleted;

    public static Category of(Long userId, String title, Byte order) {
        return new Category(userId, title, order, false);
    }

    public Category(Long userId, String title, Byte order, boolean isDeleted) {
        this.userId = userId;
        this.title = title;
        this.order = order;
        this.isDeleted = isDeleted;
    }
}
