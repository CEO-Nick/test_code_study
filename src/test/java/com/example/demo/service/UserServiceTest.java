package com.example.demo.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import com.example.demo.exception.CertificationCodeNotMatchedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
    @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    // JavaMailSender라는 bean을 Mocking해주는 Annotation
    @MockBean
    private JavaMailSender mailSender;

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        String email = "kok202@naver.com";

        // when
        UserEntity result = userService.getByEmail(email);

        // then
        assertThat(result.getNickname()).isEqualTo("kok202");
    }

    @Test
    void getByEmail은_PENDING_상태인_유저를_찾아올_수_없다() {
        // given
        String email = "kok303@naver.com";

        // when & then
        assertThatThrownBy(() -> userService.getByEmail(email))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById는_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        long id = 1L;

        // when
        UserEntity result = userService.getById(id);

        // then
        assertThat(result.getNickname()).isEqualTo("kok202");
    }

    @Test
    void getById는_PENDING_상태인_유저를_찾아올_수_없다() {
        // given
        long id = 2L;

        // when & then
        assertThatThrownBy(() -> userService.getById(id))
            .isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    void userCreateDto_를_이용하여_유저를_생성할_수_있다() {
        // given
        UserCreateDto userCreateDto = UserCreateDto.builder()
            .email("kok202@kakao.com")
            .address("Gimpo")
            .nickname("kok202-k")
            .build();
        // mailSender Mock -> SimpleMailMessage를 사용하는 send 메서드가 호출되도 아무것도 하지마라
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // when
        UserEntity result = userService.create(userCreateDto);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
//        assertThat(result.getCertificationCode()).isEqualTo("T.T");
    }

    @Test
    void userUpdateDto_를_이용하여_유저를_수정할_수_있다() {
        // given
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
            .address("South Korea")
            .nickname("kok202-update")
            .build();

        // when
        userService.update(1, userUpdateDto);

        // then
        UserEntity result = userService.getById(1);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getAddress()).isEqualTo("South Korea");
        assertThat(result.getNickname()).isEqualTo("kok202-update");
    }

    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변경된다() {
        userService.login(1L);

        UserEntity result = userService.getById(1L);
        assertThat(result.getLastLoginAt()).isGreaterThan(0L);
//        assertThat(result.getLastLoginAt()).isEqualTo("T.T");
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_상태로_변경할_수_있다() {
        // given & when
        userService.verifyEmail(2L, "aaaaaaaa-aaaaa-aaaaa-aaa-aaa-aaaaa");

        // then
        UserEntity result = userService.getById(2L);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {
        // given & when & then
        assertThatThrownBy(() -> userService.verifyEmail(2L, "wrong-certification-code"))
            .isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}