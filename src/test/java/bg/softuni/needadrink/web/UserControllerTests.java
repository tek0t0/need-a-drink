package bg.softuni.needadrink.web;

import bg.softuni.needadrink.domain.entities.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTests extends BaseTest {

    private static final String USERS_CONTROLLER_PREFIX = "/users";
    private static final String FAV_ICON_EXTENSION = "?favicon=%2Fimages%2Ffav_icon.png";

    @Test
    @Transactional
    void login() throws Exception {
        this.mockMvc.perform(get(USERS_CONTROLLER_PREFIX + "/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"));
    }

    @Test
    @Transactional
    void register() throws Exception {
        this.mockMvc.perform(get(USERS_CONTROLLER_PREFIX + "/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/register"))
                .andExpect(model().attributeExists("userRegisterBindingModel"))
                .andExpect(model().attributeExists("emailExistsError"))
                .andExpect(model().attributeExists("invalidBirthDate"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void registerRedirectsAndDoesNOtAddUserToTheRepoWhenThereAreMistakesInBinding() throws Exception {
        long count = this.userRepository.count();
        this.mockMvc.perform(post(USERS_CONTROLLER_PREFIX + "/register")

                .param("email", "1")
                .param("fullName", "1")
                .param("password", "1")
                .param("birthDate", "2000-01-01")
                .param("imgUrl", "1")
                .with(csrf()))
                .andExpect(flash().attributeExists("userRegisterBindingModel"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.userRegisterBindingModel"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(USERS_CONTROLLER_PREFIX + "/register" + FAV_ICON_EXTENSION));
        Assertions.assertEquals(count, this.userRepository.count());
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void registerRedirectsAndDoesNOtAddUserToTheRepoWhenBirthdateInvalid() throws Exception {
        long count = this.userRepository.count();
        this.mockMvc.perform(post(USERS_CONTROLLER_PREFIX + "/register")

                .param("email", "sadasd@asa.bg")
                .param("fullName", "ASDASDASDASD")
                .param("password", "asdasdasdas")
                .param("birthDate", "2015-01-01")
                .param("imgUrl", "asdasd.png")
                .with(csrf()))
                .andExpect(flash().attributeExists("userRegisterBindingModel"))
                .andExpect(flash().attributeExists("invalidBirthDate"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(USERS_CONTROLLER_PREFIX + "/register" + FAV_ICON_EXTENSION));
        Assertions.assertEquals(count, this.userRepository.count());
    }


    @Test
    @Transactional
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void registerRedirectsAndDoesNOtAddUserToTheRepoWhenEmailExists() throws Exception {
        long count = this.userRepository.count();
        this.mockMvc.perform(post(USERS_CONTROLLER_PREFIX + "/register")

                .param("email", "test@test.bg")
                .param("fullName", "ASDASDASDASD")
                .param("password", "asdasdasdas")
                .param("confirmPassword", "asdasdasdas")
                .param("birthDate", "2000-01-01")
                .param("imgUrl", "asdasd.png")
                .with(csrf()))
                .andExpect(flash().attributeExists("userRegisterBindingModel"))
                .andExpect(flash().attributeExists("emailExistsError"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(USERS_CONTROLLER_PREFIX + "/register" + FAV_ICON_EXTENSION));
        Assertions.assertEquals(count, this.userRepository.count());
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void registerRedirects() throws Exception {
        long count = this.userRepository.count();
        this.mockMvc.perform(post(USERS_CONTROLLER_PREFIX + "/register")

                .param("email", "newTest@test.bg")
                .param("fullName", "ASDASDASDASD")
                .param("password", "asdasdasdas")
                .param("confirmPassword", "asdasdasdas")
                .param("birthDate", "2000-01-01")
                .param("imgUrl", "asdasd.png")
                .with(csrf()))

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home" + FAV_ICON_EXTENSION));
        Assertions.assertEquals(count + 1, this.userRepository.count());
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void failedLogin() throws Exception {
        this.mockMvc.perform(post(USERS_CONTROLLER_PREFIX + "/login-error")

                .param("email", "test@test.bg")
                .param("password", "asdasdasdas")
                .with(csrf()))
                .andExpect(flash().attributeExists("bad_credentials"))
                .andExpect(flash().attributeExists("email"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(USERS_CONTROLLER_PREFIX + "/login" + FAV_ICON_EXTENSION));
    }

    @Test
    @Transactional
    @WithMockUser(username = "test@test.bg", roles = {"ADMIN", "USER"})
    void editProfile() throws Exception {
        this.mockMvc.perform(get(USERS_CONTROLLER_PREFIX + "/edit"))
                .andExpect(model().attributeExists("userEditBindingModel"))
                .andExpect(model().attributeExists("passMissMatch"))
                .andExpect(view().name("user/profile-edit"));

    }

    @Test
    @Transactional
    @WithMockUser(username = "test@test.bg", roles = {"ADMIN", "USER"})
    void editProfileRedirectsWhenThereAreMistakesInBinding() throws Exception {
        this.mockMvc.perform(post(USERS_CONTROLLER_PREFIX + "/edit")
                .param("fullName", "ASD")
                .param("oldPassword", "xyz")
                .param("birthDate", "2015-01-01")
                .param("imgUrl", "newImg.png")
                .with(csrf()))
                .andExpect(model().attributeExists("userEditBindingModel"))
                .andExpect(model().attributeExists("org.springframework.validation.BindingResult.userEditBindingModel"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/profile-edit"));

    }

    @Test
    @Transactional
    @WithMockUser(username = "test@test.bg", roles = {"ADMIN", "USER"}, password = "12345")
    void editProfileRedirectsWhenPasswordMissMatch() throws Exception {
        this.mockMvc.perform(post(USERS_CONTROLLER_PREFIX + "/edit")
                .param("email", "test@test.bg")
                .param("fullName", "ASDasdasd")
                .param("oldPassword", "xyzasa")
                .param("birthDate", "2000-01-01")
                .param("imgUrl", "newImg.png")
                .with(csrf()))
                .andExpect(flash().attributeExists("userEditBindingModel"))
                .andExpect(flash().attributeExists("passMissMatch"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(USERS_CONTROLLER_PREFIX + "/edit" + FAV_ICON_EXTENSION));

    }

//    @Test
//    @Transactional
//    @WithMockUser(username = "test@test.bg", roles = {"ADMIN", "USER"})
//    void editProfileRedirectTrue() throws Exception {
//        long count = this.userRepository.count();
//
//        MockMultipartFile file = new MockMultipartFile("file", "hello.png", MediaType.ALL_VALUE, "Hello, World!".getBytes()
//        );
//        UserEntity entity = this.userRepository.findAll().get(0);
//        this.mockMvc.perform(post(USERS_CONTROLLER_PREFIX + "/edit")
//                .param("email", "test@test.bg")
//                .param("fullName", "ASDasdasd")
//                .param("oldPassword","12345")
//                .param("password", "")
//                .param("confirmPassword", "")
//                .param("birthDate", "2000-01-01")
//                .param("imgUrl","file","png")
//                .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl(USERS_CONTROLLER_PREFIX + "/edit" + FAV_ICON_EXTENSION));
//        Assertions.assertEquals(count , this.userRepository.count());
//        UserEntity model = this.userRepository.findAll().get(0);
//        Assertions.assertEquals(model.getFullName(), "ASDasdasd");
//    }

}
