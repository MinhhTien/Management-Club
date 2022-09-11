package fcode.backend.management.service;

import fcode.backend.management.model.dto.ArticleDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.ArticleRepository;
import fcode.backend.management.repository.entity.Article;

import fcode.backend.management.service.constant.ServiceMessage;
import fcode.backend.management.service.constant.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ModelMapper modelMapper;

    private static final Logger logger = LogManager.getLogger(ArticleService.class);
    private static final String CREATE_ARTICLE_MESSAGE = "Create article: ";
    private static final String UPDATE_ARTICLE_MESSAGE = "Update article: ";
    private static final String GET_ARTICLE_MESSAGE = "Get article: ";
    private static final String DELETE_ARTICLE_MESSAGE = "Delete article: ";
    private static final String APPROVE_ARTICLE = "Approve article: ";
    private static final String DISAPPROVE_ARTICLE = "Disapprove article: ";

    public Response<Void> createArticle(ArticleDTO articleDTO) {
        logger.info("{}{}", CREATE_ARTICLE_MESSAGE, articleDTO);
        if (articleDTO == null) {
            logger.warn("{}{}", CREATE_ARTICLE_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Article article = modelMapper.map(articleDTO, Article.class);
        article.setId(null);
        article.setStatus(Status.PROCESSING);
        logger.info("{}{}", CREATE_ARTICLE_MESSAGE, article);
        articleRepository.save(article);
        logger.info("Create article successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> approveArticle(Integer id) {
        logger.info("{}{}", APPROVE_ARTICLE, id);

        Article articleEntity = articleRepository.findArticleByIdAndStatus(id, Status.PROCESSING);
        if (articleEntity == null) {
            logger.warn("{}{}", APPROVE_ARTICLE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        articleEntity.setStatus(Status.ACTIVE);
        articleRepository.save(articleEntity);
        logger.info("Approve article successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> disapproveArticle(Integer id) {
        logger.info("{}{}", DISAPPROVE_ARTICLE, id);

        Article articleEntity = articleRepository.findArticleByIdAndStatus(id, Status.PROCESSING);
        if (articleEntity == null) {
            logger.warn("{}{}", APPROVE_ARTICLE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        articleEntity.setStatus(Status.INACTIVE);
        articleRepository.save(articleEntity);
        logger.info("Disapprove article successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
    @Transactional
    public Response<Set<ArticleDTO>> getAllArticles() {
        logger.info("{}{}", GET_ARTICLE_MESSAGE, "All article");

        Set<ArticleDTO> articleDTOSet = articleRepository.findArticleByStatus(Status.ACTIVE).stream().map(map -> modelMapper.map(map, ArticleDTO.class)).collect(Collectors.toSet());
        logger.info("Get all articles successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), articleDTOSet);
    }

    public Response<ArticleDTO> getArticlesById(Integer id) {
        logger.info("{}{}", GET_ARTICLE_MESSAGE, id);
        if (id == null) {
            logger.warn("{}{}", GET_ARTICLE_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Article article = articleRepository.findArticleByIdAndStatus(id, Status.ACTIVE);
        if (article == null) {
            logger.warn("{}{}", GET_ARTICLE_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        logger.info("Get question successfully.");
        ArticleDTO articleDTO = modelMapper.map(article, ArticleDTO.class);
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), articleDTO);
    }


}
