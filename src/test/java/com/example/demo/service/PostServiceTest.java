package com.example.demo.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.dto.PostCreateDto;
import com.example.demo.model.dto.PostUpdateDto;
import com.example.demo.repository.PostEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
    @Sql(value = "/sql/post-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class PostServiceTest {

    @Autowired
    private PostService postService;


    @Test
    void getById_로_존재하는_게시글을_찾아올_수_있다() {
        PostEntity result = postService.getById(1);

        assertThat(result.getContent()).isEqualTo("test post 1");
        assertThat(result.getWriter().getEmail()).isEqualTo("kok202@naver.com");
    }

    @Test
    void getById_로_존재하지_않는_게시글을_찾아오면_예외가_발생한다() {
        assertThatThrownBy(() -> postService.getById(3))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void postCreateDto_를_이용하여_게시물을_생성할_수_있다() {
        // given
        PostCreateDto postCreateDto = PostCreateDto.builder()
            .writerId(1L)
            .content("test post 3")
            .build();

        // when
        PostEntity result = postService.create(postCreateDto);

        // then
        assertThat(result.getContent()).isEqualTo("test post 3");
        assertThat(result.getWriter().getId()).isEqualTo(1L);
    }

    @Test
    void postUpdateDto_를_이용하여_게시물을_수정할_수_있다() {
        // given
        PostUpdateDto postUpdateDto = PostUpdateDto.builder()
            .content("test post 1 updated")
            .build();

        // when
        postService.update(1, postUpdateDto);

        // then
        PostEntity result = postService.getById(1);
        assertThat(result.getContent()).isEqualTo("test post 1 updated");
//        assertThat(result.getModifiedAt()).isEqualTo("T.T");
    }

}