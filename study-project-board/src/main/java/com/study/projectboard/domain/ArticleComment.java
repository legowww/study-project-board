package com.study.projectboard.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
//빠르게 서칭하기 위한 인덱싱 설정
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleComment extends AuditingFields{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    //@ManyToOne(optional = false): 필수 값, @JoinColumn 없어도 ManyToOne 이 조인 기능을 해준다.
    @Setter @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn private Article article; //게시글 (ID)
    @Setter @Column(nullable = false, length = 500) private String content; //본문

    private ArticleComment(Article article, String content) {
        this.article = article;
        this.content = content;
    }
    public static ArticleComment of(Article article, String content) {
        return new ArticleComment(article, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
