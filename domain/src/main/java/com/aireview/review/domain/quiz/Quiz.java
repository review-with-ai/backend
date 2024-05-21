package com.aireview.review.domain.quiz;

import com.aireview.review.domain.BaseTimeEntity;
import com.aireview.review.domain.note.Note;
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
public class Quiz extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(50)", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Note note;

    @Column(name = "name", columnDefinition = "varchar(50)", nullable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

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

    public static Quiz of(Note note, User user, String question, String answer) {
        return new Quiz(note, user, question, answer, Status.REVIEWING, false);
    }

    public Quiz(Note note, User user, String question, String answer, Status status, boolean isDeleted) {
        this.note = note;
        this.user = user;
        this.question = question;
        this.answer = answer;
        this.status = status;
        this.isDeleted = isDeleted;
    }
}
