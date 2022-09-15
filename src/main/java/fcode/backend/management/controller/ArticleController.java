package fcode.backend.management.controller;

import fcode.backend.management.model.dto.ArticleDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    ArticleService articleService;
    @PostMapping
    Response<Void> createArticle(@RequestBody ArticleDTO articleDTO) {
        return articleService.createArticle(articleDTO);
    }
    @GetMapping("/{id}")
    Response<ArticleDTO> getArticleById(@PathVariable Integer id) {
        return articleService.getArticleById(id);
    }
    @GetMapping("/processing")
    Response<Set<ArticleDTO>> getProcessingArticle() {
        return articleService.getProcessingArticles();
    }
    @GetMapping("/all")
    Response<Set<ArticleDTO>> getAllArticle() {
        return articleService.getAllArticles();
    }
    @GetMapping("/inactive")
    Response<Set<ArticleDTO>> getInactiveArticle() {
        return articleService.getInactiveArticles();
    }
    @PutMapping("/approve/{id}")
    Response<Void> approveArticle(@PathVariable Integer id) {
        return articleService.approveArticle(id);
    }
    @PutMapping("/disapprove/{id}")
    Response<Void> disapproveArticle(@PathVariable Integer id) {
        return articleService.disapproveArticle(id);
    }
    @PutMapping("/approve/all")
    Response<Void> approveAllArticles() {
        return articleService.approveAll();
    }
    @PutMapping("/disapprove/all")
    Response<Void> disapproveAllArticles() {
        return articleService.disapproveAll();
    }
    @PutMapping
    Response<Void> updateArticle(@RequestBody ArticleDTO articleDTO) {
        return articleService.updateArticle(articleDTO);
    }
    @DeleteMapping("/{id}")
    Response<Void> deleteArticle(@PathVariable Integer id) {
        return articleService.deleteArticleById(id);
    }
}
