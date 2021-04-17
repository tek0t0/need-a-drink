package bg.softuni.needadrink.web;

import bg.softuni.needadrink.domain.entities.*;
import bg.softuni.needadrink.repositiry.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;


public class TestData {
    private String testArticleId;
    private String testCommentId;
    private String testUserId;
    private String testCocktailId;
    private String testIngredientId;


    private UserRepository userRepository;
    private LogRepository logRepository;
    private ArticleRepository articleRepository;
    private CommentRepository commentRepository;
    private CocktailRepository cocktailRepository;
    private IngredientRepository ingredientRepository;
    private UserRoleRepository userRoleRepository;

    public TestData(UserRepository userRepository,
                    LogRepository logRepository,
                    ArticleRepository articleRepository,
                    CommentRepository commentRepository,
                    CocktailRepository cocktailRepository,
                    IngredientRepository ingredientRepository,
                    UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.logRepository = logRepository;
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
        this.cocktailRepository = cocktailRepository;
        this.ingredientRepository = ingredientRepository;
        this.userRoleRepository = userRoleRepository;
    }

    public void init() {
        cleanUp();

        UserEntity userEntity = new UserEntity();
        userEntity
                .setEmail("test@test.bg")
                .setPassword("xyz")
                .setFullName("petar petrov")
                .setFavoriteCocktails(new ArrayList<>())
                .setBirthDate(LocalDate.now())
                .setImgUrl("testUrl.png");
        userEntity = userRepository.save(userEntity);

        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity
                .setTitle("testTitle")
                .setContent("testContent")
                .setDescription("testDescription")
                .setCoverImgUrl("imgUrl.png")
                .setAddedOn(LocalDate.now())
                .setComments(new ArrayList<>());
        articleEntity = articleRepository.save(articleEntity);

        IngredientEntity ingredientEntity = new IngredientEntity();
        ingredientEntity
                .setName("testNama")
                .setDescription("testDescription")
                .setImgUrl("testImgUrl.png")
                .setUsedIn(new ArrayList<>());

        ingredientEntity = ingredientRepository.save(ingredientEntity);

        CocktailEntity cocktailEntity = new CocktailEntity();
        cocktailEntity
                .setName("testName")
                .setDescription("testDescription")
                .setImgUrl("testImgUrl.png")
                .setVideoUrl("videoUrl")
                .setIngredients(new HashSet<>())
                .setPreparation("testPreparation");

        cocktailEntity = cocktailRepository.save(cocktailEntity);

        CommentEntity commentEntity = new CommentEntity();
        commentEntity
                .setAddedOn(LocalDateTime.now())
                .setAuthor(userEntity)
                .setContent("testContent");
        commentEntity = commentRepository.save(commentEntity);

        testArticleId = articleEntity.getId();
        testUserId = userEntity.getId();
        testCocktailId = cocktailEntity.getId();
        testIngredientId = ingredientEntity.getId();
        testCommentId = commentEntity.getId();

    }

    void cleanUp() {
        logRepository.deleteAll();
        articleRepository.deleteAll();
        cocktailRepository.deleteAll();
        userRepository.deleteAll();
        commentRepository.deleteAll();
        ingredientRepository.deleteAll();
    }

    public String getTestArticleId() {
        return testArticleId;
    }

    public String getTestCommentId() {
        return testCommentId;
    }

    public String getTestUserId() {
        return testUserId;
    }

    public String getTestCocktailId() {
        return testCocktailId;
    }

    public String getTestIngredientId() {
        return testIngredientId;
    }
}
