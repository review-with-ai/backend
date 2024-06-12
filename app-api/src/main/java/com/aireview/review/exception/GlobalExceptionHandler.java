package com.aireview.review.exception;

import com.aireview.review.common.exception.AiReviewException;
import com.aireview.review.common.exception.ErrorCode;
import com.aireview.review.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({AiReviewException.class})
    public ResponseEntity<ErrorResponse> refreshTokenException(AiReviewException ex, HttpServletRequest request) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(errorCode, request.getRequestURL().toString());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> methodArgumentNotValid(
            MethodArgumentNotValidException exception,
            BindingResult bindingResult,
            HttpServletRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();

        bindingResult.getFieldErrors()
                .forEach(fieldError -> fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage()));

        ErrorCode errorCode = GlobalErrorCode.REQUEST_NOT_VALID;

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.getCode(), fieldErrors.toString(), request.getRequestURL().toString()));
    }

    /*
      @Valid를 통해 검증되는 경우를 제외하고 메소드 매개변수에서 발생하는 검증 오류 ex) request param, validator를 통한 검증
    */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> constraintViolation(
            ConstraintViolationException e,
            HttpServletRequest request) {

        Map<String, String> bindingResult = new HashMap<>();

        e.getConstraintViolations()
                .forEach(
                        constraintViolation -> {
                            List<String> propertyPath =
                                    List.of(constraintViolation.getPropertyPath().toString().split("\\."));
                            String path = propertyPath.get(propertyPath.size() - 1);
                            bindingResult.put(path, constraintViolation.getMessage());
                        }
                );


        ErrorCode errorCode = GlobalErrorCode.REQUEST_NOT_VALID;

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.getCode(), bindingResult.toString(), request.getRequestURL().toString()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> runtimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
