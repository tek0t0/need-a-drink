package bg.softuni.needadrink.service;

import bg.softuni.needadrink.domain.models.service.ArticleServiceModel;

import java.util.List;

public interface ArticleService {
    void initArticles();

    List<ArticleServiceModel> findLatestArticles();
}
