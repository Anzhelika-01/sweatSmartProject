package bg.softuni.sweatsmartproject.web;

import org.springframework.web.servlet.ModelAndView;

public abstract class BaseController {
    public ModelAndView view(String viewName, ModelAndView mav){
        mav.setViewName(viewName);

        return mav;
    }

    public ModelAndView view(String viewName){
        return this.view(viewName, new ModelAndView());
    }

    public ModelAndView redirect(String url){
        return this.view("redirect:" + url);
    }
}