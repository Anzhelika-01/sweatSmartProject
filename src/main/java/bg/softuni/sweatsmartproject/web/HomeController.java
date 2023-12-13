package bg.softuni.sweatsmartproject.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController extends BaseController{

    @GetMapping
    public ModelAndView getHome(){
        return super.view("index");
    }

    @GetMapping("index")
    public ModelAndView getIndex(){
        return super.view("index");
    }
}