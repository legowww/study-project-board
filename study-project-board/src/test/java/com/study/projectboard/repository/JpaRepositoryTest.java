package com.study.projectboard.repository;

import com.study.projectboard.config.JpaConfig;
import com.study.projectboard.domain.Article;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;


@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class) //JpaConfig 는 내가 만든 파일
@DataJpaTest //@Entity 어노테이션이 적용된 클래스를 스캔하여 스프링 데이터 JPA 저장소를 구성합니다.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //@DataJpaTest 는 기본으로 임베디드 디비 사용하는데 이를 막고 내가 설정한 디비 사용
class JpaRepositoryTest {

    @Autowired private ArticleRepository articleRepository;
    @Autowired private ArticleCommentRepository articleCommentRepository;



    @DisplayName("select 테스트")
    @Test
    public void jpaTest() throws Exception {
        //given

        //when
        List<Article> articles = articleRepository.findAll();

        //then
        org.assertj.core.api.Assertions.assertThat(articles).isNotNull().hasSize(0);
    }

    @DisplayName("insert 테스트")
    @Test
    public void insertTest() throws Exception {
        //given
        long previousCount = articleRepository.count();
        Article article = Article.of("new article", "new content", "#spring");

        //when
        Article savedArticle = articleRepository.save(article);

        //then
        Assertions.assertEquals(previousCount + 1, articleRepository.count());
    }

    @DisplayName("update 테스트")
    @Test
    public void updateTest() throws Exception {
        //given
        Article article = Article.of("new article", "new content", "#spring");
        Article savedArticle = articleRepository.save(article);

        //when
        Article findArticle = articleRepository.findById(1L).orElseThrow();
        findArticle.setHashtag("#fixed hashtag");
        articleRepository.flush();

        //then
        Article findArticle2 = articleRepository.findById(1L).orElseThrow();
        System.out.println("findArticle2 = " + findArticle2);
    }


}