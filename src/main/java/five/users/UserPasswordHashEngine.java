package five.users;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class UserPasswordHashEngine{

    public String hash(final String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean match(final String password, final String passwordHash) {
        return BCrypt.checkpw(password, passwordHash);
    }
}
