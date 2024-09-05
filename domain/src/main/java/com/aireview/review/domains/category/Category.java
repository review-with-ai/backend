package com.aireview.review.domains.category;


import com.aireview.review.domains.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = {"userId", "title"})
@ToString
public class Category extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "title", columnDefinition = "varchar(50)", nullable = false)
    private String title;

    @Column(name = "order", columnDefinition = "tinyint", nullable = false)
    private Byte order;

    @Where(clause = "is_deleted=false")
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
