package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.model.UserStatus;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource("classpath:test-application.properties")
@Sql("/sql/user-repository-test-data.sql")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다() {
        // given
        // when
        Optional<UserEntity> findUser = userRepository.findByIdAndStatus(1,
            UserStatus.ACTIVE);

        // then
        assertThat(findUser.isPresent()).isTrue();
    }

    @Test
    void findByIdAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
        // given
        // when
        Optional<UserEntity> findUser = userRepository.findByIdAndStatus(2L,
            UserStatus.ACTIVE);

        // then
        assertThat(findUser.isEmpty()).isTrue();
    }

    @Test
    void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다() {
        // given
        // when
        Optional<UserEntity> findUser = userRepository.findByEmailAndStatus("kok202@naver.com",
            UserStatus.ACTIVE);

        // then
        assertThat(findUser.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
        // given
        // when
        Optional<UserEntity> findUser = userRepository.findByEmailAndStatus("test222@test.com",
            UserStatus.ACTIVE);

        // then
        assertThat(findUser.isEmpty()).isTrue();
    }
}