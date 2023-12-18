package bg.softuni.sweatsmartproject.web;

import bg.softuni.sweatsmartproject.domain.dto.wrapper.UserRegisterForm;
import bg.softuni.sweatsmartproject.service.UserService;
import bg.softuni.sweatsmartproject.validation.UserRegisterValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController extends BaseController {
    private final UserService userService;
    private final UserRegisterValidator userRegisterValidator;
    private final SecurityContextRepository securityContextRepository;
    public static final String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult.";


    @Autowired
    public UserController(UserService userService, UserRegisterValidator userRegisterValidator, SecurityContextRepository securityContextRepository) {
        this.userService = userService;
        this.userRegisterValidator = userRegisterValidator;
        this.securityContextRepository = securityContextRepository;
    }

    @GetMapping("/register")
    public ModelAndView getRegister() {
        return super.view("register");
    }

    @PostMapping("/register")
    public ModelAndView postRegister(@Valid @ModelAttribute(name = "registerForm") UserRegisterForm registerForm,
                                     BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest request,
                                     HttpServletResponse response) {

        this.userRegisterValidator.validate(registerForm, bindingResult);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("registerForm", registerForm)
                    .addFlashAttribute(BINDING_RESULT_PATH + "registerForm", bindingResult);

            return super.view("register");
        }

        this.userService.registerUser(registerForm, successfulAuth -> {
            final SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();

            final SecurityContext context = strategy.createEmptyContext();
            context.setAuthentication(successfulAuth);

            strategy.setContext(context);

            securityContextRepository.saveContext(context, request, response);
        });

        return super.redirect("login");
    }

    @GetMapping("/login")
    public ModelAndView getLogin() {
        return super.view("login");
    }


    @PostMapping("/login-error")
    public ModelAndView onFailedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
        redirectAttributes.addFlashAttribute("bad_credentials", true);

        return super.redirect("login");
    }

    @ModelAttribute(name = "registerForm")
    public UserRegisterForm getRegisterForm() {
        return new UserRegisterForm();
    }
}