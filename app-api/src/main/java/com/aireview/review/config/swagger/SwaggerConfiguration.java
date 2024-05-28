package com.aireview.review.config.swagger;

import com.aireview.review.authentication.jwt.JwtConfig;
import com.aireview.review.common.exception.AiReviewException;
import com.aireview.review.common.exception.ErrorCode;
import com.aireview.review.config.swagger.docs.PossibleExceptions;
import com.aireview.review.login.exception.LoginErrorCode;
import com.aireview.review.login.usernamepassword.UsernamePasswordLoginRequest;
import com.aireview.review.model.ErrorResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;


@Configuration
@RequiredArgsConstructor
public class SwaggerConfiguration {

    private static final String BEARER_TOKEN_PREFIX = "Bearer";
    private static final String BEARER_FORMAT = "JWT";

    private final JwtConfig jwtConfig;

    private final ApplicationContext applicationContext;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ai review 프로젝트 REST API 명세서")
                        .version("v1"))
                .components(new Components().addSchemas("ErrorResponse", errorResponseSchema()));
    }

    private Schema<ErrorResponse> errorResponseSchema() {
        return new Schema<ErrorResponse>()
                .description("에러 발생 응답")
                .type("object")
                .addProperty("code", new Schema().type("string").description("에러 코드"))
                .addProperty("reason", new Schema().type("string").description("에러 원인 메시지"))
                .addProperty("timestamp", new Schema().type("string").description("에러 발생 시각"))
                .addProperty("path", new Schema().type("string").description("에러 발생 경로"));
    }


    @Bean
    public SecurityScheme securityScheme() {
        return new SecurityScheme()
                .name(jwtConfig.getHeader())
                .type(SecurityScheme.Type.HTTP)
                .scheme(BEARER_TOKEN_PREFIX)
                .bearerFormat(BEARER_FORMAT);
    }

    /**
     * 필터에서 처리되는 요청 Open API 추가
     **/

    @Bean
    public OpenApiCustomizer addLoginOpenAPI() {
        return openApi -> {
            openApi
                    .addTagsItem(new Tag().name("Authentication").description("로그인"))
                    .path("/login", loginPathItem())
                    .path("/oauth2/authorization/{providerId}", oauthLoginPathItem());
        };
    }


    private PathItem loginPathItem() {
        Schema<UsernamePasswordLoginRequest> requestSchema = new Schema<>()
                .description("Email/PW 로그인 요청")
                .type("object")
                .addProperty("email", new Schema().type("string").description("이메일").example("hong24@gmail.com"))
                .addProperty("password", new Schema().type("string").description("패스워드").example("hongpass24"));

        Map<String, LoginErrorCode> possibleLoginErrorCodes = new HashMap<>();
        possibleLoginErrorCodes.put("이메일_오류", LoginErrorCode.WRONG_EMAIL);
        possibleLoginErrorCodes.put("비밀번호_오류", LoginErrorCode.WRONG_PASSWORD);
        possibleLoginErrorCodes.put("요청_형식_오류", LoginErrorCode.WRONG_LOGIN_REQUEST_FORMAT);
        possibleLoginErrorCodes.put("기타_오류", LoginErrorCode.LOGIN_FAIL);

        ApiResponses apiResponses = loginResponses(possibleLoginErrorCodes);

        return new PathItem().post(new Operation()
                .tags(List.of("Authentication"))
                .summary("Email/PW 로그인")
                .description("일반 회원가입 유저의 로그인")
                .requestBody(new RequestBody().content(new Content()
                        .addMediaType("application/json", new MediaType().schema(requestSchema))))
                .responses(apiResponses));
    }

    private ApiResponses loginResponses(Map<String, LoginErrorCode> possibleLoginErrorCodes) {
        ApiResponses responses = new ApiResponses()
                .addApiResponse("200", new ApiResponse().content(new Content()
                        .addMediaType("application/json",
                                new MediaType().schema(new Schema<>().$ref("#/components/schemas/LoginResponse")))));

        Map<Integer, List<ExampleHolder>> statusWithExampleHolder = possibleLoginErrorCodes
                .entrySet().stream()
                .map(entry -> new ExampleHolder(
                        new Example().value(new ErrorResponse(entry.getValue(), "로그인 url")),
                        entry.getKey(),
                        entry.getValue().getStatus()))
                .collect(groupingBy(ExampleHolder::getCode));
        addExamplesToResponses(responses, statusWithExampleHolder);
        return responses;
    }

    private PathItem oauthLoginPathItem() {
        Parameter parameter = new Parameter()
                .name("providerId")
                .description("OAuth 인증 서비스 ID")
                .required(true)
                .in("path")
                .schema(new Schema().type("string")._enum(List.of("naver", "kakao")));

        Map<String, LoginErrorCode> possibleLoginErrorCodes = new HashMap<>();
        possibleLoginErrorCodes.put("OAUTH_인증_오류", LoginErrorCode.OAUTH_FAIL);


        ApiResponses apiResponses = loginResponses(possibleLoginErrorCodes);


        return new PathItem().get(new Operation()
                .tags(List.of("Authentication"))
                .summary("OAuth 회원 가입 및 로그인 - 카카오, 네이버")
                .description("첫 인증 시 회원가입. 이후 로그인")
                .addParametersItem(parameter)
                .responses(apiResponses));
    }

    /**
     * ApiExceptionExample 어노테이션 읽어서 예외 응답 Example 추가
     **/
    @Bean
    public OperationCustomizer customize() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            ApiExceptionExample exceptionsExample =
                    handlerMethod.getMethodAnnotation(ApiExceptionExample.class);

            if (exceptionsExample != null) {
                generateExceptionResponseExample(operation, exceptionsExample.value());
            }
            return operation;
        };
    }

    private void generateExceptionResponseExample(Operation operation, Class<? extends PossibleExceptions> possibleExceptionDocs) {
        ApiResponses responses = operation.getResponses();
        PossibleExceptions bean = applicationContext.getBean(possibleExceptionDocs);
        Field[] fields = bean.getClass().getDeclaredFields();
        Map<Integer, List<ExampleHolder>> statusWithExamples = Arrays.stream(fields)
                .filter(field -> field.getType().equals(AiReviewException.class))
                .map(field -> {
                    try {
                        AiReviewException e = (AiReviewException) field.get(bean);
                        ErrorCode errorCode = e.getErrorCode();
                        ExplainError explainError = field.getAnnotation(ExplainError.class);
                        String description = explainError != null ? explainError.value() : "";
                        return new ExampleHolder(getExample(description, errorCode), field.getName(), errorCode.getStatus());
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(groupingBy(ExampleHolder::getCode));
        addExamplesToResponses(responses, statusWithExamples);
    }

    private Example getExample(String description, ErrorCode errorCode) {
        return new Example()
                .description(description)
                .value(new ErrorResponse(errorCode, "요청 url path"));
    }

    private void addExamplesToResponses(ApiResponses responses, Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach(
                (status, exampleHolders) -> {
                    ApiResponse response = new ApiResponse();
                    Content content = new Content();
                    MediaType mediaType = new MediaType();
                    exampleHolders.forEach(
                            exampleHolder -> {
                                mediaType.addExamples(exampleHolder.getName(), exampleHolder.getExample());
                            }
                    );
                    mediaType.schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"));
                    content.addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, mediaType);
                    response.setContent(content);
                    responses.addApiResponse(status.toString(), response);
                }
        );
    }
}
