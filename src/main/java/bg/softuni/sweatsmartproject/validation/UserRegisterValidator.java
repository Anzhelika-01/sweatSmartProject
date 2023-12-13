package bg.softuni.sweatsmartproject.validation;

import bg.softuni.sweatsmartproject.domain.dto.wrapper.UserRegisterForm;
import bg.softuni.sweatsmartproject.repository.UserRepo;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@bg.softuni.sweatsmartproject.validation.Validator
public class UserRegisterValidator implements Validator {
    private final UserRepo userRepo;

    public UserRegisterValidator(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRegisterForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRegisterForm userRegister = (UserRegisterForm) target;

        if (this.userRepo.findUserByUsername(userRegister.getUsername()).isPresent()) {
            errors.rejectValue(
                    "username",
                    "Username exists",
                    "Username exists"
            );
        }

    }
}