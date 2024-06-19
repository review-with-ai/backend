package com.aireview.review.model;

import com.aireview.review.domains.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserMeResponse {
    private Long id;
    private String name;
    private String nickname;
    private String email;
    private String status;
    private boolean hasSubscribed;

    public static UserMeResponse from(User user) {
        return new UserMeResponse(
                user.getId(),
                user.getName(),
                user.getNickname(),
                user.getEmail(),
                user.getStatus().name(),
                user.hasSubscribed()
        );
    }

    public UserMeResponse(Long id, String name, String nickname, String email, String status, boolean hasSubscribed) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.status = status;
        this.hasSubscribed = hasSubscribed;
    }
}
