package bg.softuni.needadrink.service.impl;


import bg.softuni.needadrink.domain.entities.ArticleEntity;
import bg.softuni.needadrink.domain.models.service.ArticleServiceModel;
import bg.softuni.needadrink.error.ArticleNotFoundException;
import bg.softuni.needadrink.repositiry.ArticleRepository;
import bg.softuni.needadrink.service.LogService;
import bg.softuni.needadrink.util.ValidatorUtil;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class ArticleEntityServiceImplTest {

    private ArticleEntity testArticleEntity1, testArticleEntity2;

    private ArticleServiceImpl serviceToTest;

    @Mock
    ArticleRepository mockArticleRepository;

    @Mock
    LogService mockLogService;

    @Mock
    ValidatorUtil validatorUtil;


    @BeforeEach
    public void init() {


        testArticleEntity1 = new ArticleEntity();
        testArticleEntity1
                .setTitle("article 1")
                .setCoverImgUrl("image 1")
                .setContent("content 1")
                .setDescription("description 1")
                .setAddedOn(LocalDate.of(2020, 1, 8));
        testArticleEntity1.setId("A");

        testArticleEntity2 = new ArticleEntity()
                .setTitle("article 2")
                .setCoverImgUrl("image 2")
                .setContent("content 2")
                .setDescription("description 2")
                .setAddedOn(LocalDate.of(2021, 1, 8));
        testArticleEntity2.setId("B");

        serviceToTest = new ArticleServiceImpl(
                mockArticleRepository,
                new ModelMapper(),
                new Gson(),
                validatorUtil,
                mockLogService);
    }

    @AfterEach
    public void reset() {
        Mockito.reset(mockArticleRepository);
    }

    @Test
    public void testGetAllArticles() {

        when(mockArticleRepository.findAllOrderByDate()).thenReturn(List.of(testArticleEntity1, testArticleEntity2));

        List<ArticleServiceModel> allModels = serviceToTest.getAllArticles();

        Assertions.assertEquals(2, allModels.size());

        ArticleServiceModel model1 = allModels.get(0);
        ArticleServiceModel model2 = allModels.get(1);

        // verify model 1
        Assertions.assertEquals(testArticleEntity1.getTitle(), model1.getTitle());
        Assertions.assertEquals(testArticleEntity1.getCoverImgUrl(), model1.getCoverImgUrl());
        Assertions.assertEquals(testArticleEntity1.getContent(), model1.getContent());
        Assertions.assertEquals(testArticleEntity1.getDescription(), model1.getDescription());
        Assertions.assertEquals(testArticleEntity1.getAddedOn(), model1.getAddedOn());

        // verify model 2
        Assertions.assertEquals(testArticleEntity2.getTitle(), model2.getTitle());
        Assertions.assertEquals(testArticleEntity2.getCoverImgUrl(), model2.getCoverImgUrl());
        Assertions.assertEquals(testArticleEntity2.getContent(), model2.getContent());
        Assertions.assertEquals(testArticleEntity2.getDescription(), model2.getDescription());
        Assertions.assertEquals(testArticleEntity2.getAddedOn(), model2.getAddedOn());

    }

    @Test
    void testTitleExists() {
        when(mockArticleRepository.findByTitle("article 1")).thenReturn(Optional.of(testArticleEntity1));

        Assertions.assertTrue(serviceToTest.titleExists("article 1"));
    }

    @Test
    void testFindArticleByIdThrowsExceptionWhenArticleDoesNotExist() {
        Assertions.assertThrows(
                ArticleNotFoundException.class, () -> {
                    serviceToTest.findArticleById("article_id_Does_not_exists");
                }
        );
    }

    @Test
    void testFindArticleById() {

        Mockito.when(mockArticleRepository.findById("A")).thenReturn(Optional.of((testArticleEntity1)));

        ArticleServiceModel model1 = serviceToTest.findArticleById("A");

        Assertions.assertEquals(testArticleEntity1.getId(), model1.getId());
        Assertions.assertEquals(testArticleEntity1.getTitle(), model1.getTitle());
        Assertions.assertEquals(testArticleEntity1.getCoverImgUrl(), model1.getCoverImgUrl());
        Assertions.assertEquals(testArticleEntity1.getContent(), model1.getContent());
        Assertions.assertEquals(testArticleEntity1.getDescription(), model1.getDescription());
        Assertions.assertEquals(testArticleEntity1.getAddedOn(), model1.getAddedOn());
    }

    @Test
    void testFindArticleByIdThrowException() {

        Assertions.assertThrows(ArticleNotFoundException.class, () -> serviceToTest.findArticleById("C"));
    }

    @Test
    void testGetArticleEntity() {

        Mockito.when(mockArticleRepository.findById("A")).thenReturn(Optional.of((testArticleEntity1)));

        ArticleEntity model1 = serviceToTest.getArticleEntity("A");

        Assertions.assertEquals(testArticleEntity1.getId(), model1.getId());
        Assertions.assertEquals(testArticleEntity1.getTitle(), model1.getTitle());
        Assertions.assertEquals(testArticleEntity1.getCoverImgUrl(), model1.getCoverImgUrl());
        Assertions.assertEquals(testArticleEntity1.getContent(), model1.getContent());
        Assertions.assertEquals(testArticleEntity1.getDescription(), model1.getDescription());
        Assertions.assertEquals(testArticleEntity1.getAddedOn(), model1.getAddedOn());
    }

    @Test
    void testGetArticleEntityThrowException() {

        Assertions.assertThrows(ArticleNotFoundException.class, () -> serviceToTest.getArticleEntity("C"));
    }

    @Test
    void testEditArticle() {
        ArticleServiceModel articleServiceModel = new ArticleServiceModel()
                .setTitle("new_article_1")
                .setCoverImgUrl("new_image_1")
                .setContent("new_content_1")
                .setDescription("new_description_1")
                .setAddedOn(LocalDate.of(2020, 1, 8));

        Mockito.when(mockArticleRepository.findById("A")).
                thenReturn(Optional.of(testArticleEntity1));

        ArticleServiceModel articleServiceModel1 = serviceToTest.editArticle("A", articleServiceModel);

        Assertions.assertEquals(articleServiceModel.getCoverImgUrl(), articleServiceModel1.getCoverImgUrl());
        Assertions.assertEquals(articleServiceModel.getContent(), articleServiceModel1.getContent());
        Assertions.assertEquals(articleServiceModel.getDescription(), articleServiceModel1.getDescription());
        Assertions.assertEquals(articleServiceModel.getAddedOn(), articleServiceModel1.getAddedOn());

    }

    @Test
    void testDeleteByIdWorks() {

        Mockito.when(mockArticleRepository.findById("A")).
                thenReturn(Optional.of(testArticleEntity1));
        serviceToTest.deleteById("A");
    }
}