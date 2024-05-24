package com.aireview.review.domain.user;

import com.aireview.review.config.EmailAttributeConverter;
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
@Table(name = "user", indexes = {
        @Index(name = "user_email_idx", columnList = "email", unique = true)
})
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(50)", nullable = false)
    private String name;

    @Column(name = "nickname", columnDefinition = "varchar(20)", nullable = false)
    private String nickname;

    @Column(name = "password", columnDefinition = "varchar(256)")
    private String password;

    @Column(name = "email", columnDefinition = "varchar(255)", nullable = false, unique = true)
    @Convert(converter = EmailAttributeConverter.class)
    private Email email;

    @Column(name = "oauth_provider", columnDefinition = "varchar(20)")
    @Enumerated(EnumType.STRING)
    private OAuthProvider oauthProvider;

    @Column(name = "oauth_user_id", columnDefinition = "varchar(255)")
    private String oauthUserId;

    @Column(name = "has_subscribed", columnDefinition = "tinyint(1)", nullable = false)
    private boolean hasSubscribed;

    @Column(name = "status", columnDefinition = "enum('ENABLED','BLOCKED','DELETED')", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        ENABLED, BLOCKED, DELETED;
    }

    public static User of(String name, String nickname, Email email, String password) {
        return new User(null, name, nickname, password, email, null, null, false, Status.ENABLED);
    }

    public static User oauthUserOf(String name, String nickname, Email email, OAuthProvider oauthProvider, String oAuthUserId) {
        return new User(null, name, nickname, null, email, oauthProvider, oAuthUserId, false, Status.ENABLED);
    }

    public User(
            Long id,
            String name,
            String nickname,
            String password,
            Email email,
            OAuthProvider oauthProvider,
            String oAuthUserId,
            Boolean hasSubscribed,
            Status status) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.oauthProvider = oauthProvider;
        this.oauthUserId = oAuthUserId;
        this.hasSubscribed = hasSubscribed;
        this.status = status;
    }
}
