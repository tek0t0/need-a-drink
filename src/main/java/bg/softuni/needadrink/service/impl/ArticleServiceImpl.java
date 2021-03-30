package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.Article;
import bg.softuni.needadrink.domain.entities.Ingredient;
import bg.softuni.needadrink.domain.models.binding.ArticleAddBindingModel;
import bg.softuni.needadrink.domain.models.binding.IngredientBindingModel;
import bg.softuni.needadrink.domain.models.service.ArticleServiceModel;
import bg.softuni.needadrink.error.ArticleNotFoundException;
import bg.softuni.needadrink.util.Constants;
import bg.softuni.needadrink.repositiry.ArticleRepository;
import bg.softuni.needadrink.service.ArticleService;
import bg.softuni.needadrink.util.ValidatorUtil;
import com.google.gson.Gson;
import com.jayway.jsonpath.InvalidJsonException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Value("classpath:init/articles.json") Resource articlesFile;
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidatorUtil validatorUtil;

    public ArticleServiceImpl(ArticleRepository articleRepository, ModelMapper modelMapper, Gson gson, ValidatorUtil validatorUtil) {
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public void initArticles() {

        String content = null;
        try {
            content = String.join("", Files.readAllLines(Path.of(articlesFile.getURI())));
            ArticleAddBindingModel[] articleAddBindingModels = this.gson.fromJson(content, ArticleAddBindingModel[].class);
            for (ArticleAddBindingModel bindingModel : articleAddBindingModels) {

                if(this.validatorUtil.isValid(bindingModel)){
                    Article article = this.modelMapper.map(bindingModel, Article.class);
                    article.setAddedOn(LocalDate.now());
                    this.articleRepository.saveAndFlush(article);
                } else {
                    throw new InvalidJsonException(Constants.INVALID_JSON_DATA_ERROR);
                }

            }
        } catch (IOException ignored) {
            //TODO
        }
    }

    @Override
    public List<ArticleServiceModel> findLatestArticles() {
        return articleRepository
                .findLatestArticles()
                .stream()
                .map(a -> modelMapper.map(a, ArticleServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleServiceModel> getAllArticles() {
        return articleRepository
                .findAllOrderByDate()
                .stream()
                .map(a -> modelMapper.map(a, ArticleServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean titleExists(String title) {
        return articleRepository.findByTitle(title).isPresent();
    }

    @Override
    public void addArticle(ArticleServiceModel articleServiceModel) {
        Article article = modelMapper.map(articleServiceModel, Article.class);
        article.setAddedOn(LocalDate.now());
        if (article.getCoverImgUrl() == null) {
            article.setCoverImgUrl("https://cdn.vox-cdn.com/thumbor/QMfCpaGj-WwLgeLin_b8hCMEL8M=/22x0:912x668/1400x1400/filters:focal(22x0:912x668):format(jpeg)/cdn.vox-cdn.com/uploads/chorus_image/image/45710634/unnamed-1.0.0.jpg");
        }

        articleRepository.saveAndFlush(article);
    }

    @Override
    public ArticleServiceModel findArticleById(String id) {
        return this.articleRepository.findById(id)
                .map(a -> modelMapper.map(a, ArticleServiceModel.class))
                .orElseThrow(() -> new ArticleNotFoundException(Constants.ARTICLE_ID_NOT_FOUND));


    }

    @Override
    public ArticleServiceModel editArticle(String id, ArticleServiceModel articleServiceModel) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(Constants.ARTICLE_ID_NOT_FOUND));
        article
                .setTitle(articleServiceModel.getTitle())
                .setDescription(articleServiceModel.getDescription())
                .setContent(articleServiceModel.getContent());

        if(articleServiceModel.getCoverImgUrl() == null){
            article.setCoverImgUrl("https://cdn.vox-cdn.com/thumbor/QMfCpaGj-WwLgeLin_b8hCMEL8M=/22x0:912x668/1400x1400/filters:focal(22x0:912x668):format(jpeg)/cdn.vox-cdn.com/uploads/chorus_image/image/45710634/unnamed-1.0.0.jpg");
        }

        return this.modelMapper.map(this.articleRepository.saveAndFlush(article), ArticleServiceModel.class);
    }

    @Override
    public void deleteById(String id) {
        Article article = this.articleRepository.findById(id).orElseThrow(()->new ArticleNotFoundException(Constants.ARTICLE_ID_NOT_FOUND));

        this.articleRepository.delete(article);
    }


    //TODO: Logger for all the methods


}
