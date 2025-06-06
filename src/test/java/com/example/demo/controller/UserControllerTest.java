package com.example.demo.controller;

import static com.example.demo.model.UserStatus.ACTIVE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
    @Sql(value = "/sql/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 사용자는_특정_유저의_정보를_개인정보는_소거된채_전달_받을_수_있다() throws Exception {
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("kok202@naver.com"))
            .andExpect(jsonPath("$.nickname").value("kok202"))
            .andExpect(jsonPath("$.address").doesNotExist())
            .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_api_호출할_경우_404_응답을_받는다() throws Exception {
        mockMvc.perform(get("/api/users/11231232112312"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Users에서 ID 11231232112312를 찾을 수 없습니다."));
    }

    @Test
    void 사용자는_인증코드로_계정을_활성화_시킬_수_있다() throws Exception {
        mockMvc.perform(get("/api/users/2/verify")
                .queryParam("certificationCode", "aaaaaaaa-aaaaa-aaaaa-aaa-aaa-aaaaa"))
            .andExpect(status().isFound());

        UserEntity userEntity = userRepository.findById(2L).get();
        assertThat(userEntity.getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    void 사용자는_인증코드가_일치하지_않을_경우_권한_없음_에러를_내려준다() throws Exception {
        mockMvc.perform(get("/api/users/2/verify")
                .queryParam("certificationCode", "bbbbbb"))
            .andExpect(status().isForbidden());
    }

    @Test
    void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() throws Exception {
        mockMvc.perform(get("/api/users/me")
            .header("EMAIL", "kok202@naver.com"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("kok202@naver.com"))
            .andExpect(jsonPath("$.nickname").value("kok202"))
            .andExpect(jsonPath("$.address").value("Seoul"))
            .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void 사용자는_내_정보를_수정할_수_있다() throws Exception {
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
            .nickname("kok202-updated")
            .address("Busan")
            .build();

        mockMvc.perform(put("/api/users/me")
            .header("EMAIL", "kok202@naver.com")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userUpdateDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("kok202@naver.com"))
            .andExpect(jsonPath("$.nickname").value("kok202-updated"))
            .andExpect(jsonPath("$.address").value("Busan"))
            .andExpect(jsonPath("$.status").value("ACTIVE"));
    }
}