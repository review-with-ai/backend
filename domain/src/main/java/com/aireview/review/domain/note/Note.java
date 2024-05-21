package com.aireview.review.domain.note;

import com.aireview.review.domain.BaseTimeEntity;
import com.aireview.review.domain.category.Category;
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
public class Note extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @Column(name = "title", columnDefinition = "varchar(100)", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "varchar(5000)", nullable = false)
    private String content;

    @ManyToOne(optional = false)
    private Category category;

    @Column(name = "is_deleted", columnDefinition = "tinyint(1)", nullable = false)
    private boolean isDeleted;

    public Note of(User user, String title, String content, Category category) {
        return new Note(user, title, content, category, false);
    }

    public Note(User user, String title, String content, Category category, boolean isDeleted) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.category = category;
        this.isDeleted = isDeleted;
    }
}
