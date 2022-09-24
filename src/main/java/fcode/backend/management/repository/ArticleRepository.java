package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Article;
import fcode.backend.management.service.constant.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Article findArticleByIdAndStatus(Integer id, Status status);
    Set<Article> findArticleByStatus(Status status);
    Article findArticleByIdAndStatusIsNot(Integer id, Status status);
    Set<Article> findArticleByMemberIdAndStatus(Integer memberId, Status status);
}
