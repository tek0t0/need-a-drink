package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.ArticleEntity;
import bg.softuni.needadrink.domain.models.binding.ArticleAddBindingModel;
import bg.softuni.needadrink.domain.models.service.ArticleServiceModel;
import bg.softuni.needadrink.domain.models.service.LogServiceModel;
import bg.softuni.needadrink.error.ArticleNotFoundException;
import bg.softuni.needadrink.service.LogService;
import bg.softuni.needadrink.util.Constants;
import bg.softuni.needadrink.repositiry.ArticleRepository;
import bg.softuni.needadrink.service.ArticleService;
import bg.softuni.needadrink.util.ValidatorUtil;
import com.google.gson.Gson;
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
    @Value("classpath:init/articles.json")
    Resource articlesFile;
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidatorUtil validatorUtil;
    private final LogService logService;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository,
                              ModelMapper modelMapper,
                              Gson gson,
                              ValidatorUtil validatorUtil,
                              LogService logService) {
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validatorUtil = validatorUtil;
        this.logService = logService;
    }

    @Override
    public void initArticles() throws IOException {

        if (articleRepository.count() == 0) {
            String content;

            content = String.join("", Files.readAllLines(Path.of(articlesFile.getURI())));
            ArticleAddBindingModel[] articleAddBindingModels = this.gson.fromJson(content, ArticleAddBindingModel[].class);
            for (ArticleAddBindingModel articleAddBindingModel : articleAddBindingModels) {

                if (this.validatorUtil.isValid(articleAddBindingModel)) {
                    ArticleEntity articleEntity = this.modelMapper.map(articleAddBindingModel, ArticleEntity.class);
                    articleEntity.setAddedOn(LocalDate.now());
                    this.articleRepository.saveAndFlush(articleEntity);
                } else {
                    LogServiceModel logServiceModel = new LogServiceModel();
                    logServiceModel.setUsername("ADMIN");
                    logServiceModel.setDescription("Failed to add article.");

                    this.logService.seedLogInDB(logServiceModel);
                }

            }
        }

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

        ArticleEntity articleEntity = new ArticleEntity();

        articleEntity
                .setTitle(articleServiceModel.getTitle())
                .setDescription(articleServiceModel.getDescription())
                .setContent(articleServiceModel.getContent())
                .setCoverImgUrl(articleServiceModel.getCoverImgUrl())
                .setAddedOn(LocalDate.now());

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername("ADMIN");
        logServiceModel.setDescription("Article added");

        this.logService.seedLogInDB(logServiceModel);

        articleRepository.saveAndFlush(articleEntity);
    }

    @Override
    public ArticleServiceModel findArticleById(String id) {
        return this.articleRepository.findById(id)
                .map(a -> modelMapper.map(a, ArticleServiceModel.class))
                .orElseThrow(() -> new ArticleNotFoundException(Constants.ARTICLE_ID_NOT_FOUND));


    }

    @Override
    public ArticleServiceModel editArticle(String id, ArticleServiceModel articleServiceModel) {
        ArticleEntity articleEntity = getArticleEntity(id);
        articleEntity
                .setTitle(articleServiceModel.getTitle())
                .setCoverImgUrl(articleServiceModel.getCoverImgUrl())
                .setDescription(articleServiceModel.getDescription())
                .setContent(articleServiceModel.getContent());

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername("ADMIN");
        logServiceModel.setDescription("Article edited");

        this.logService.seedLogInDB(logServiceModel);

        this.articleRepository.saveAndFlush(articleEntity);

        return this.modelMapper.map(articleEntity, ArticleServiceModel.class);
    }

    @Override
    public void deleteById(String id) {
        ArticleEntity articleEntity = getArticleEntity(id);

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername("ADMIN");
        logServiceModel.setDescription("Article deleted");

        this.logService.seedLogInDB(logServiceModel);

        this.articleRepository.delete(articleEntity);
    }

    public ArticleEntity getArticleEntity(String id) {
        return this.articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(Constants.ARTICLE_ID_NOT_FOUND));
    }


}
