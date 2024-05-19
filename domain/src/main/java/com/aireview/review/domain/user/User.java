package com.aireview.review.domain.user;

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
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nickname;
    private String password;
    private String email;
    private String oauthProvider;
    private String oauthUserId;
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        ENABLED, DISABLED;
    }

    public User(String name, String nickname, String email, String password) {
        this(null, name, nickname, password, email, null, null, Status.ENABLED);
    }

    public User(String name, String nickname, String email, String oauthProvider, String oAuthUserId) {
        this(null, name, nickname, null, email, oauthProvider, oAuthUserId, Status.ENABLED);
    }

    public User(Long id, String name, String nickname, String password, String email, String oauthProvider, String oAuthUserId, Status status) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.oauthProvider = oauthProvider;
        this.oauthUserId = oAuthUserId;
        this.status = status;
    }
}
