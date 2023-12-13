package bg.softuni.sweatsmartproject.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/about")
public class AboutController extends BaseController{

    @GetMapping
    public ModelAndView getAbout(){
        return super.view("about");
    }
}