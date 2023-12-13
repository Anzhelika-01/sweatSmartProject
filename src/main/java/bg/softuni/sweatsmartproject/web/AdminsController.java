package bg.softuni.sweatsmartproject.web;

import bg.softuni.sweatsmartproject.domain.dto.model.UserModel;
import bg.softuni.sweatsmartproject.domain.dto.view.UserInfoWithoutPassDto;
import bg.softuni.sweatsmartproject.service.UserRoleService;
import bg.softuni.sweatsmartproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admins")
public class AdminsController extends BaseController {

    private final UserService userService;

    private final UserRoleService userRoleService;

    @Autowired
    public AdminsController(UserService userService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @GetMapping
    public ModelAndView getAdmins(Model model) {

        final List<UserInfoWithoutPassDto> users = this.userService.getAllUsersWithInfo();

        model.addAttribute("users", users);

        return super.view("admins");
    }

    @PostMapping("/change-username")
    public ModelAndView postChangeUsername(@RequestParam UUID userId,
                                           @RequestParam String username, RedirectAttributes redirectAttributes) {
        final UserModel userToChange = this.userService.findById(userId);

        if (userToChange != null) {
            boolean isChanged = this.userService.changeUsername(username, userToChange);

            if (!isChanged) {
                redirectAttributes.addFlashAttribute("bad_credentials", true);
            }
        } else {
            redirectAttributes.addFlashAttribute("user_not_found", true);
        }

        return super.redirect("/admins");
    }

    @PostMapping("/add-role")
    public ModelAndView addRole(@RequestParam UUID userId, @RequestParam String role) {
        final UserModel userModel = this.userService.findById(userId);
        this.userService.changeRoles(userModel, role, true);

        return super.redirect("/admins");
    }

    @PostMapping("/delete-role")
    public ModelAndView deleteRole(@RequestParam UUID userId, @RequestParam String role) {
        final UserModel userModel = this.userService.findById(userId);
        this.userService.changeRoles(userModel, role, false);

        return super.redirect("/admins");
    }
}