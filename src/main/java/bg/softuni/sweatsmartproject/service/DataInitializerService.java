package bg.softuni.sweatsmartproject.service;

import bg.softuni.sweatsmartproject.domain.entity.User;
import bg.softuni.sweatsmartproject.repository.UserRepo;
import bg.softuni.sweatsmartproject.repository.UserRoleRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DataInitializerService {

    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;

    private final UserRoleRepo userRoleRepo;

    private final UserRepo userRepo;

    @Autowired
    public DataInitializerService(UserRoleService userRoleService, PasswordEncoder passwordEncoder, UserRoleRepo userRoleRepo, UserRepo userRepo) {

        this.userRoleService = userRoleService;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepo = userRoleRepo;
        this.userRepo = userRepo;
    }

    @PostConstruct
    public void postConstruct() {
        dbInit();
    }

    public void dbInit() {
        if (!isDbInit()) {
            this.userRoleService.dbInit();
            initAdmin();
        }
    }
    public void initAdmin() {
        final User user = new User().builder().
                email("kotevaanzhelika@gmail.com")
                .username("akoteva")
                .role(userRoleRepo.findAll())
                .password(passwordEncoder.encode("1234"))
                .build();

        this.userRepo.saveAndFlush(user);
    }
    public boolean isDbInit() {
        return this.userRepo.count() > 0;
    }
}