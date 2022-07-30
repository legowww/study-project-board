package com.study.projectboard.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
}) //빠르게 서칭하기 위한 인덱싱 설정
@NoArgsConstructor(access = AccessLevel.PROTECTED) //하이버네이트 구현체만 접근
@EntityListeners(AuditingEntityListener.class) //Auditing 을 사용할 것이다.
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //MySql 방식
    private Long id;

    //not null
    @Setter @Column(nullable = false) private String title; //제목
    @Setter @Column(nullable = false, length = 10000) private String content; //본문

    //null, @Column 기본값인 nullable = true 이므로 생략가능하다.
    @Setter private String hashtag; // 해시태그

    @ToString.Exclude //순환 참조 방지, ArticleComment 에서 ToString 이 article 필드를 보고 현재 클래스 이동. Exclude 보고 재참조 안함.
    @OrderBy("id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleCommentSet = new LinkedHashSet<>();

    //메타데이터
    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt; //생성일시
    @CreatedBy @Column(nullable = false, length = 100) private String createdBy; //생성자, 현재 기본값 defaultName
    @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt; //수정일시
    @LastModifiedBy @Column(nullable = false, length = 100) private String modifiedBy; //수정자

    //private 로 막아버리고 팩터리 메서드 방식 사용
    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }
    //가이드 방식을 하는 팩터리 메서드
    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }

    //동등성 동일성 검사 EqualsAndHashCode..
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        //id != null: 영속성 전의 개체는 id가 null 일 수 있다.
        //영속화되지 않은 방금 만든 엔티티 -> 다른 값으로 인식한다. 동등성 검사를 탈락한다.
        return id != null && id.equals(article.id); //id 가 not null 가정
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


