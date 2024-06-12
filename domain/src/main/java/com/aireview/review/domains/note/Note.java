package com.aireview.review.domains.note;

import com.aireview.review.domains.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = {"userId", "createdAt"})
@ToString
public class Note extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "title", columnDefinition = "varchar(100)", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "varchar(5000)", nullable = false)
    private String content;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "is_deleted", columnDefinition = "tinyint(1)", nullable = false)
    private boolean isDeleted;

    public Note of(Long userId, String title, String content, Long categoryId) {
        return new Note(userId, title, content, categoryId, false);
    }

    public Note(Long userId, String title, String content, Long categoryId, boolean isDeleted) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.isDeleted = isDeleted;
    }
}
