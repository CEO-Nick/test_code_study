package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.model.dto.UserCreateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserCreateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private JavaMailSender mailSender;

    @Test
    void 사용자는_회원_가입을_할_수_있고_회원가입된_사용자는_PENDING_상태이다() throws Exception {
        UserCreateDto userCreateDto = UserCreateDto.builder()
            .email("kok202@kakao.com")
            .nickname("kok202")
            .address("Pangyo")
            .build();
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userCreateDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.email").value("kok202@kakao.com"))
            .andExpect(jsonPath("$.nickname").value("kok202"))
            .andExpect(jsonPath("$.address").doesNotExist())
            .andExpect(jsonPath("$.status").value("PENDING"));
    }


}