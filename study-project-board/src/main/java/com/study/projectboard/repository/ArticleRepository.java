package com.study.projectboard.repository;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.study.projectboard.domain.Article;
import com.study.projectboard.domain.QArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//QuerydslPredicateExecutor<T>: Article 에 대한 기본 검색기능 추가 -> 부분검색이 불가능하다는 단점
//QuerydslBinderCustomizer<QT>: 부분검색 구현하자. customize 오버라이딩
@RepositoryRestResource
public interface ArticleRepository extends JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>,
        QuerydslBinderCustomizer<QArticle>
{
    //인터페이스지만 default 붙여서 구현하자.
    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.hashtag, root.createdAt, root.createdBy, root.content);

        //부분검색 설정
        //IgnoreCase -> 대소문자 구분X
        //StringExpression::likeIgnoreCase -> like '${v}' -> 내가 %를 넣어줘야하는 번거로움 존재
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // like '%${v}$%'
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // 문자열이 아니고 그냥 정확한 검색 사용 그러나 시분초를 정확하게 넣어야해서 좋은 방법은 아니다.
    }
}

