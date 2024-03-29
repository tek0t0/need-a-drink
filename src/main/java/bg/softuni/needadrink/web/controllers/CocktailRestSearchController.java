package bg.softuni.needadrink.web.controllers;

import bg.softuni.needadrink.domain.models.service.CocktailServiceModel;
import bg.softuni.needadrink.domain.models.views.CocktailSearchViewModel;
import bg.softuni.needadrink.service.CocktailService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cocktails")
public class CocktailRestSearchController {
    private final CocktailService cocktailService;
    private final ModelMapper modelMapper;

    public CocktailRestSearchController(CocktailService cocktailService, ModelMapper modelMapper) {
        this.cocktailService = cocktailService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/search")
    public ResponseEntity<List<CocktailServiceModel>> findAll() {
        List<CocktailServiceModel> cocktailsList = cocktailService.getAllCocktails()
                .stream()
                .map(c -> modelMapper.map(c, CocktailServiceModel.class))
                .collect(Collectors.toList());

        return ResponseEntity
                .ok()
                .body(cocktailsList);

    }
}
