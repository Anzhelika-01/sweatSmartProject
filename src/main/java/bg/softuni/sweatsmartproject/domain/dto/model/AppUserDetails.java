package bg.softuni.sweatsmartproject.domain.dto.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Setter
@Getter
public class AppUserDetails extends User {
    private String email;

    public AppUserDetails(String username, String password,
                          Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public AppUserDetails setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getEmail() {
        return this.email;
    }
}