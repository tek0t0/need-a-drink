package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.Article;
import bg.softuni.needadrink.domain.models.service.ArticleServiceModel;
import bg.softuni.needadrink.repositiry.ArticleRepository;
import bg.softuni.needadrink.service.ArticleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;

    public ArticleServiceImpl(ArticleRepository articleRepository, ModelMapper modelMapper) {
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void initArticles() {
        Article article1 = new Article();
        article1
                .setAddedOn(LocalDate.now())
                .setTitle("Whats new in mixology")
                .setDescription("5 cocktail trends to watch in 2020")
                .setContent("Lorem ipsum dolor sit amet consectetur adipisicing elit. Magnam eveniet adipisci" +
                        " molestias porro cumque quaerat veritatis voluptas accusantium ipsum soluta, " +
                        "officia vero quidem, natus consequatur quae quia dolorem! Natus, accusantium.")
                .setCoverImgUrl("https://cdn.vox-cdn.com/thumbor/QMfCpaGj-WwLgeLin_b8hCMEL8M=/22x0:912x668/1400x1400/filters:focal(22x0:912x668):format(jpeg)/cdn.vox-cdn.com/uploads/chorus_image/image/45710634/unnamed-1.0.0.jpg");

        Article article2 = new Article();
        article2
                .setAddedOn(LocalDate.now())
                .setTitle("Whats new in mixology2")
                .setDescription("5 cocktail trends to watch in 2020")
                .setContent("Lorem ipsum dolor sit amet consectetur adipisicing elit. Magnam eveniet adipisci" +
                        " molestias porro cumque quaerat veritatis voluptas accusantium ipsum soluta, " +
                        "officia vero quidem, natus consequatur quae quia dolorem! Natus, accusantium.")
                .setCoverImgUrl("https://cdn.vox-cdn.com/thumbor/QMfCpaGj-WwLgeLin_b8hCMEL8M=/22x0:912x668/1400x1400/filters:focal(22x0:912x668):format(jpeg)/cdn.vox-cdn.com/uploads/chorus_image/image/45710634/unnamed-1.0.0.jpg");

        Article article3 = new Article();
        article3
                .setAddedOn(LocalDate.now())
                .setTitle("Whats new in mixology3")
                .setDescription("5 cocktail trends to watch in 2020")
                .setContent("Lorem ipsum dolor sit amet consectetur adipisicing elit. Magnam eveniet adipisci" +
                        " molestias porro cumque quaerat veritatis voluptas accusantium ipsum soluta, " +
                        "officia vero quidem, natus consequatur quae quia dolorem! Natus, accusantium.")
                .setCoverImgUrl("https://cdn.vox-cdn.com/thumbor/QMfCpaGj-WwLgeLin_b8hCMEL8M=/22x0:912x668/1400x1400/filters:focal(22x0:912x668):format(jpeg)/cdn.vox-cdn.com/uploads/chorus_image/image/45710634/unnamed-1.0.0.jpg");

        if(articleRepository.count() == 0) {
            articleRepository.saveAndFlush(article1);
            articleRepository.saveAndFlush(article2);
            articleRepository.saveAndFlush(article3);
        }
    }

    @Override
    public List<ArticleServiceModel> findLatestArticles() {
        return articleRepository
                .findLatestArticles()
                .stream()
                .map(a->modelMapper.map(a, ArticleServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleServiceModel> getAllArticles() {
        return articleRepository
                .findAll()
                .stream()
                .map(a->modelMapper.map(a, ArticleServiceModel.class))
                .collect(Collectors.toList());
    }
}
