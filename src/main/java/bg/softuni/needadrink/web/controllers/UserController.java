package bg.softuni.needadrink.web.controllers;

import bg.softuni.needadrink.domain.models.binding.UserEditBindingModel;
import bg.softuni.needadrink.domain.models.binding.UserRegisterBindingModel;
import bg.softuni.needadrink.domain.models.service.UserRegisterServiceModel;
import bg.softuni.needadrink.domain.models.service.UserServiceModel;
import bg.softuni.needadrink.domain.models.views.UserProfileViewModel;
import bg.softuni.needadrink.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;

@Controller
@RequestMapping("/users")
public class UserController {

    private final ModelMapper modelMapper;
    private final UserService userService;

    public UserController(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/register")
    private String register(Model model) {
        if (!model.containsAttribute("userRegisterBindingModel")) {
            model.addAttribute("userRegisterBindingModel", new UserRegisterBindingModel());
            model.addAttribute("emailExistsError", false);
            model.addAttribute("invalidBirthDate", false);
        }
        return "user/register";
    }

    @PostMapping("register")
    private String registerConfirm(
            @Valid UserRegisterBindingModel userRegisterBindingModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        LocalDate today = LocalDate.now();
        LocalDate birthDate = userRegisterBindingModel.getBirthDate();

        if (Period.between(birthDate, today).getYears() < 18) {
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("invalidBirthDate", true);
            return "redirect:/users/register";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel", bindingResult);
            return "redirect:/users/register";
        }

        if (userService.emailExists(userRegisterBindingModel.getEmail())) {
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("emailExistsError", true);
            return "redirect:/users/register";
        }

        UserRegisterServiceModel registerServiceModel = modelMapper.map(userRegisterBindingModel, UserRegisterServiceModel.class);
        userService.registerAndLoginUser(registerServiceModel);


        return "redirect:/home";
    }

    @PostMapping("/login-error")
    public String failedLogin(@ModelAttribute("email")
                                      String email,
                              RedirectAttributes attributes) {
        attributes.addFlashAttribute("bad_credentials", true);
        attributes.addFlashAttribute("email", email);

        return "redirect:/users/login";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        model
                .addAttribute("userProfileViewModel", this.modelMapper
                        .map(this.userService.findUserByEmail(principal.getName()), UserProfileViewModel.class));
        return "user/profile";
    }

    @GetMapping("/edit")
    public String editProfile(Principal principal, Model model) {
        model.addAttribute("userEditBindingModel", this.modelMapper
                        .map(this.userService.findUserByEmail(principal.getName()), UserEditBindingModel.class));
        model.addAttribute("confirmPassMissMatch", false);
        return "user/profile-edit";
    }

    @PostMapping("/edit")
    public String editProfileConfirm(@ModelAttribute UserEditBindingModel model,
                                     @Valid UserEditBindingModel userEditBindingModel,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userEditBindingModel", userEditBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userEditBindingModel", bindingResult);
            return "user/profile-edit";
        }

        if (!model.getPassword().equals(model.getConfirmPassword())) {
//            redirectAttributes.addFlashAttribute("userEditBindingModel", userEditBindingModel);
//            redirectAttributes.addFlashAttribute("confirmPassMissMatch", true);
            //TODO:check why cant set redirect attributes to model?
            return "user/profile-edit";
        }

        this.userService.editUserProfile(this.modelMapper.map(model, UserServiceModel.class), model.getOldPassword());

        return "redirect:/users/profile";
    }


}
