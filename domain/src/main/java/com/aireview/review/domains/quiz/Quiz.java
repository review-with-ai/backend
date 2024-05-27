package com.aireview.review.domain.quiz;

import com.aireview.review.domain.BaseTimeEntity;
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
public class Quiz extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "note_id", nullable = false, updatable = false)
    private Long noteId;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "question", columnDefinition = "varchar(100)", nullable = false)
    private String question;

    @Column(name = "answer", columnDefinition = "varchar(300)", nullable = false)
    private String answer;

    @Column(name = "status", columnDefinition = "enum('REVIEWING','DONE')", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "is_deleted", columnDefinition = "tinyint(1)", nullable = false)
    private boolean isDeleted;

    public enum Status {
        REVIEWING, DONE;
    }

    public static Quiz of(Long noteId, Long userId, String question, String answer) {
        return new Quiz(noteId, userId, question, answer, Status.REVIEWING, false);
    }

    public Quiz(Long noteId, Long userId, String question, String answer, Status status, boolean isDeleted) {
        this.noteId = noteId;
        this.userId = userId;
        this.question = question;
        this.answer = answer;
        this.status = status;
        this.isDeleted = isDeleted;
    }
}
