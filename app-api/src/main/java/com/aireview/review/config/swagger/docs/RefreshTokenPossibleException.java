package com.aireview.review.config.swagger.docs;

import com.aireview.review.authentication.jwt.exception.RefreshTokenExpiredException;
import com.aireview.review.authentication.jwt.exception.RefreshTokenNotIssuedByApp;
import com.aireview.review.authentication.jwt.exception.RefreshTokenNotValidException;
import com.aireview.review.common.exception.AiReviewException;
import com.aireview.review.config.swagger.ExplainError;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenPossibleException implements PossibleExceptions {

    @ExplainError("리프레시 토큰이 만료됨")
    public final AiReviewException 리프레시_토큰_만료 = RefreshTokenExpiredException.INSTANCE;

    @ExplainError("유저에게 발급된 리프레시 토큰이 없거나 발급된 리프레시 토큰과 다른 토큰일 경우")
    public final AiReviewException 유효하지_않은_토큰 = RefreshTokenNotValidException.INSTANCE;

    @ExplainError("앱에서 발급된 토큰이 아님. ex - jwt가 아니거나 signature가 다르거나 등등")
    public final AiReviewException 앱에서_발급된_토큰이_아님 = RefreshTokenNotIssuedByApp.INSTANCE;
}
