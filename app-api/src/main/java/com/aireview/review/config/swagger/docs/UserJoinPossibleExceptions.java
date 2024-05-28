package com.aireview.review.config.swagger.docs;

import com.aireview.review.common.exception.AiReviewException;
import com.aireview.review.config.swagger.ExplainError;
import com.aireview.review.domains.user.exception.DuplicateEmailException;
import org.springframework.stereotype.Component;

@Component
public class UserJoinPossibleExceptions implements PossibleExceptions{

    @ExplainError("이미 가입된 이메일일때")
    public AiReviewException 이메일_중복 = DuplicateEmailException.INSTANCE;
}
