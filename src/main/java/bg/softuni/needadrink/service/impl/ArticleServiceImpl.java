package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.Article;
import bg.softuni.needadrink.domain.models.binding.ArticleAddBindingModel;
import bg.softuni.needadrink.domain.models.service.ArticleServiceModel;
import bg.softuni.needadrink.domain.models.service.LogServiceModel;
import bg.softuni.needadrink.error.ArticleNotFoundException;
import bg.softuni.needadrink.service.CloudinaryService;
import bg.softuni.needadrink.service.LogService;
import bg.softuni.needadrink.util.Constants;
import bg.softuni.needadrink.repositiry.ArticleRepository;
import bg.softuni.needadrink.service.ArticleService;
import bg.softuni.needadrink.util.ValidatorUtil;
import com.google.gson.Gson;
import com.jayway.jsonpath.InvalidJsonException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Value("classpath:init/articles.json") Resource articlesFile;
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidatorUtil validatorUtil;
    private final LogService logService;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository,
                              ModelMapper modelMapper,
                              Gson gson,
                              ValidatorUtil validatorUtil,
                              LogService logService, CloudinaryService cloudinaryService) {
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validatorUtil = validatorUtil;
        this.logService = logService;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public void initArticles() {

        String content;
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
        } catch (IOException ex) {
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
            article.setCoverImgUrl(Constants.DEFAULT_ARTICLE_IMAGE_URL);
        }

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername("ADMIN");
        logServiceModel.setDescription("Article added");
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);

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
            article.setCoverImgUrl(Constants.DEFAULT_ARTICLE_IMAGE_URL);
        }

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername("ADMIN");
        logServiceModel.setDescription("Article edited");
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);

        return this.modelMapper.map(this.articleRepository.saveAndFlush(article), ArticleServiceModel.class);
    }

    @Override
    public void deleteById(String id) {
        Article article = this.articleRepository.findById(id)
                .orElseThrow(()->new ArticleNotFoundException(Constants.ARTICLE_ID_NOT_FOUND));

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername("ADMIN");
        logServiceModel.setDescription("Article deleted");
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);

        this.articleRepository.delete(article);
    }





}
