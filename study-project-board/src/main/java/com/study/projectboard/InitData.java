package com.study.projectboard;

import com.study.projectboard.domain.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitData {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.initData();
    }

    @Component
    @RequiredArgsConstructor
    static class InitService {

        //가짜(프록시) 엔티티 메니저를 제공, 호출하면 현재 트랜잭션에 맞는 엔티티 매니저 찾아서 연결시킴
        private final EntityManager em;

        @Transactional
        public void initData() {
            Article article = Article.of("new article", "new content", "#spring");
            article.setHashtag("#updateSpring");
            em.persist(article);
        }
    }
}
