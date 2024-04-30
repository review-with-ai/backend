package com.aireview.review.controller;

import com.aireview.review.config.security.SecurityConfiguration;
import com.aireview.review.model.LoginRequest;
import com.aireview.review.model.LoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.runtime.ObjectMethods;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles(value = "local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest {
    // TODO: 4/29/24 join 테스트 필요
    @Autowired
    private WebApplicationContext applicationContext;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("로그인 성공 테스트 (아이디, 비밀번호가 올바른 경우)")
    void loginSuccessTest() throws Exception {
        String username = "soo@email.com";
        String password = "soopassword123";
        ResultActions result = mockMvc.perform(
                post("/api/v1/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest(username, password)))
        );

        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 실패 테스트 (아이디 누락)")
    void loginFailureTest_whenPrincipalNotExist() throws Exception {
        String username = "";
        String password = "soopassword123";
        ResultActions result = mockMvc.perform(
                post("/api/v1/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest(username, password)))
        );

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldError").exists())
                .andExpect(jsonPath("$.fieldError.username").exists());
    }

    @Test
    @DisplayName("로그인 실패 테스트 (존재하지 않는 아이디의 경우)")
    void loginFailureTest_whenUsernameNotFound() throws Exception {
        String username = "soo@gmail.com";
        String password = "soopassword123";
        ResultActions result = mockMvc.perform(
                post("/api/v1/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest(username, password)))
        );

        result.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @DisplayName("로그인 실패 테스트 (비밀번호가 올바르지 않은 경우)")
    void loginFailureTest_whenBadCredential() throws Exception {
        String username = "soo@gmail.com";
        String password = "soopassword123";
        ResultActions result = mockMvc.perform(
                post("/api/v1/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest(username, password)))
        );

        result.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist());

    }
}
