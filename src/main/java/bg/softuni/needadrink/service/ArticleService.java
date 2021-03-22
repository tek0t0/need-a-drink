package bg.softuni.needadrink.service;

import bg.softuni.needadrink.domain.models.service.ArticleServiceModel;

import java.util.List;

public interface ArticleService {
    void initArticles();

    List<ArticleServiceModel> findLatestArticles();

    List<ArticleServiceModel> getAllArticles();

    boolean titleExists(String title);

    void addArticle(ArticleServiceModel articleServiceModel);


    ArticleServiceModel findArticleById(String id);

    ArticleServiceModel editArticle(String id, ArticleServiceModel map);
}
