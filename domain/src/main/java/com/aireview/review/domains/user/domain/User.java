package com.aireview.review.domains.user.domain;

import com.aireview.review.domains.BaseTimeEntity;
import com.aireview.review.validation.Nickname;
import com.aireview.review.validation.Password;
import com.aireview.review.validation.UserName;
import com.aireview.review.validation.UserValidationGroups;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @UserName
    private String name;

    @Column(name = "nickname", columnDefinition = "varchar(20)", nullable = false)
    @Nickname
    private String nickname;

    @Column(name = "password", columnDefinition = "varchar(256)")
    @Password(groups = {UserValidationGroups.USER.class})
    private String password;

    @Column(name = "email", columnDefinition = "varchar(255)", nullable = false, unique = true)
    @Email(regexp = "[\\w~\\-.+]+@[\\w~\\-]+(\\.[\\w~\\-]+)+", message = "{Email}")
    private String email;

    @Column(name = "oauth_provider", columnDefinition = "varchar(20)")
    @Enumerated(EnumType.STRING)
    @NotNull(groups = {UserValidationGroups.OAuthUser.class})
    private OAuthProvider oauthProvider;

    @Column(name = "oauth_user_id", columnDefinition = "varchar(255)")
    @NotBlank(groups = {UserValidationGroups.OAuthUser.class})
    private String oauthUserId;

    @Column(name = "has_subscribed", columnDefinition = "tinyint(1)", nullable = false)
    @NotNull
    private boolean hasSubscribed;

    @Column(name = "status", columnDefinition = "enum('ENABLED','BLOCKED','DELETED')", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    public enum Status {
        ENABLED, BLOCKED, DELETED;
    }

    public static User of(String name, String nickname, String email, String password) {
        return new User(null, name, nickname, password, email, null, null, false, Status.ENABLED);
    }

    public static User oauthUserOf(String name, String nickname, String email, OAuthProvider oauthProvider, String oAuthUserId) {
        return new User(null, name, nickname, null, email, oauthProvider, oAuthUserId, false, Status.ENABLED);
    }

    public User(
            Long id,
            String name,
            String nickname,
            String password,
            String email,
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
