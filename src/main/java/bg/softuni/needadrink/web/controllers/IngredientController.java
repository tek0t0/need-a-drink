package bg.softuni.needadrink.web.controllers;


import bg.softuni.needadrink.domain.models.binding.IngredientBindingModel;
import bg.softuni.needadrink.domain.models.service.IngredientServiceModel;
import bg.softuni.needadrink.service.IngredientService;
import bg.softuni.needadrink.web.anotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;
    private final ModelMapper modelMapper;

    public IngredientController(IngredientService ingredientService, ModelMapper modelMapper) {
        this.ingredientService = ingredientService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    @PageTitle("All Ingredients")
    public String getAllIngredients(Model model) {
        List<IngredientServiceModel> allIngredients = ingredientService.getAllIngredients();
        model.addAttribute("ingredients", allIngredients);

        return "ingredient/all-ingredients";
    }

    @GetMapping("/add")
    @PageTitle("Add Ingredient")
    public String add(Model model) {
        List<IngredientServiceModel> allIngredients = ingredientService.getAllIngredients();
        if (!model.containsAttribute("ingredientBindingModel")) {
            model.addAttribute("ingredientBindingModel", new IngredientBindingModel());
            model.addAttribute("nameExists", false);

        }


        model.addAttribute("ingredients", allIngredients);

        return "ingredient/add-ingredient";
    }

    @PostMapping("/add")
    public String addConfirm(@Valid IngredientBindingModel ingredientBindingModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        List<IngredientServiceModel> allIngredients = ingredientService.getAllIngredients();
        redirectAttributes.addFlashAttribute("allIngredients", allIngredients);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("ingredientBindingModel", ingredientBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.ingredientBindingModel", bindingResult);

            return "redirect:/ingredients/add";
        }

        if (ingredientService.nameExists(ingredientBindingModel.getName())) {
            redirectAttributes.addFlashAttribute("ingredientBindingModel", ingredientBindingModel);
            redirectAttributes.addFlashAttribute("nameExists", true);

            return "redirect:/ingredients/add";
        }

        IngredientServiceModel ingredientServiceModel = modelMapper.map(ingredientBindingModel, IngredientServiceModel.class);
        ingredientService.addIngredient(ingredientServiceModel);
        return "redirect:/ingredients/all";
    }

    @GetMapping("/edit/{id}")
    @PageTitle("Edit Ingredient")
    public String ingredientEdit(@PathVariable String id, Model model) {
        model.addAttribute("ingredientBindingModel", this.ingredientService.findIngredientById(id));
        model.addAttribute("ingredientId", id);
        model.addAttribute("nameExists", false);

        return "ingredient/edit-ingredient";
    }

    @PostMapping("/edit/{id}")
    public String ingredientEditConfirm(
            @PathVariable String id,
            @Valid IngredientBindingModel ingredientBindingModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("ingredientBindingModel", ingredientBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.ingredientBindingModel", bindingResult);

            return "ingredient/edit-ingredient";
        }

        if (ingredientService.newNameExists(modelMapper.map(ingredientBindingModel, IngredientServiceModel.class))) {
            redirectAttributes.addFlashAttribute("ingredientBindingModel", ingredientBindingModel);
            redirectAttributes.addFlashAttribute("nameExists", true);

            return "ingredient/edit-ingredient";
        }

        IngredientServiceModel ingredientServiceModel = modelMapper.map(ingredientBindingModel, IngredientServiceModel.class);
        this.ingredientService.editIngredient(ingredientServiceModel);
        return "redirect:/ingredients/all";
    }

    @GetMapping("/delete/{id}")
    @PageTitle("Delete Ingredient")
    public String deleteIngredient(@PathVariable String id, Model model) {
        model.addAttribute("ingredient", this.ingredientService.findIngredientById(id));
        return "ingredient/delete-ingredient";
    }

    @PostMapping("/delete/{id}")
    public String deleteIngredientConfirm(@PathVariable String id) {
        this.ingredientService.deleteIngredient(id);
          return "redirect:/ingredients/all";
    }
}
