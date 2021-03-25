package bg.softuni.needadrink.web.controllers;


import bg.softuni.needadrink.domain.models.service.IngredientServiceModel;
import bg.softuni.needadrink.service.IngredientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/all")
    public String getAllIngredients(Model model) {
        List<IngredientServiceModel> allIngredients = ingredientService.getAllIngredients();
        model.addAttribute("ingredients", allIngredients);

        return "ingredient/all-ingredients";
    }
}
