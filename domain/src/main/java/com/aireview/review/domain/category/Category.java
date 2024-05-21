package com.aireview.review.domain.category;


import com.aireview.review.domain.BaseTimeEntity;
import com.aireview.review.domain.user.User;
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

    @Column(name = "user_id", columnDefinition = "bigint", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @Column(name = "title", columnDefinition = "varchar(50)", nullable = false)
    private String title;

    @Column(name = "order", columnDefinition = "tinyint", nullable = false)
    private Byte order;

    @Column(name = "is_deleted", columnDefinition = "tinyint(1)", nullable = false)
    private boolean isDeleted;

    public static Category of(User user, String title, Byte order){
        return new Category(user, title, order, false);
    }

    public Category(User user, String title, Byte order, boolean isDeleted) {
        this.user = user;
        this.title = title;
        this.order = order;
        this.isDeleted = isDeleted;
    }
}
