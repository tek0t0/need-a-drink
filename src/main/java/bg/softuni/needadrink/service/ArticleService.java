package bg.softuni.needadrink.service;

import bg.softuni.needadrink.domain.models.service.ArticleServiceModel;

import java.io.IOException;
import java.util.List;

public interface ArticleService {
    void initArticles() throws IOException;

    List<ArticleServiceModel> getAllArticles();

    boolean titleExists(String title);

    void addArticle(ArticleServiceModel articleServiceModel);


    ArticleServiceModel findArticleById(String id);

    ArticleServiceModel editArticle(String id, ArticleServiceModel map);

    void deleteById(String id);
}
