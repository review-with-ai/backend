package com.aireview.review.model;

import com.aireview.review.validation.Nickname;
import com.aireview.review.validation.Password;
import com.aireview.review.validation.UserName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "일반 회원가입 요청")
public class JoinRequest {

    @UserName
    @Schema(description = "사용자 이름은 3글자이상 50글자 이하의 한글만 가능합니다.(공백 불가능)", example = "홍길동")
    private String name;

    @Nickname
    @Schema(description = "닉네임은 5글자 이상 20글자 이하여야 합니다.(영문자, 한글, 숫자, 특수문자 가능).", example = "홍길동_24")
    private String nickname;

    @Email(regexp = "[\\w~\\-.+]+@[\\w~\\-]+(\\.[\\w~\\-]+)+", message = "{Email}")
    @Schema(description = "유효한 이메일 형식이어야합니다.(@ . 포함)", example = "hong24@gmail.com", type = "string")
    private String email;

    @Password
    @Schema(description = "패스워드는 8글자 20글자 이하여야하고 반드시 한개 이상의 영문자 및 숫자를 포함해야합니다.(특수문자 불가능)", example = "hongpass24")
    private String password;

    public JoinRequest(String name, String nickname, String email, String password) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }
}
