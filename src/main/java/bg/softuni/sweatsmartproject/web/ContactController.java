package bg.softuni.sweatsmartproject.web;

import bg.softuni.sweatsmartproject.domain.dto.model.MessageModel;
import bg.softuni.sweatsmartproject.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/contact")
public class ContactController extends BaseController{

    private final MessageService messageService;

    @Autowired
    public ContactController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ModelAndView getContact(){
        return super.view("contact");
    }

    @PostMapping
    public ModelAndView postMessage(@Valid @ModelAttribute(name = "messageModel") MessageModel messageModel) {

        messageService.addMessage(messageModel);

        String successMessage = "Your message has been sent successfully, and you will receive a response very soon.";

        ModelAndView modelAndView = new ModelAndView("contact");
        modelAndView.addObject("successMessage", successMessage);

        return modelAndView;
    }

    @ModelAttribute(name = "messageModel")
    public MessageModel getMessage(){
        return new MessageModel();
    }
}