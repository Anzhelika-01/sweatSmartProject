package bg.softuni.sweatsmartproject.scheduleJobs;

import bg.softuni.sweatsmartproject.domain.entity.User;
import bg.softuni.sweatsmartproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableAsync
public class DailyJobScheduler {

    private final UserService userService;
    private final JavaMailSender mailSender;

    @Autowired
    public DailyJobScheduler(UserService userService, JavaMailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }

    @Async
    @Scheduled(cron = "0 0 0 * * *")
    public void runDailyJob() {
        try {
            List<User> users = userService.getAllUsers();
            for (User user : users) {
                sendEmail(user.getEmail());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendEmail(String to) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Daily Update");
            message.setText("Don't forget to track your daily caloric intake!");
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}