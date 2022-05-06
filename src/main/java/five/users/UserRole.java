package five.users;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

    MEMBER, MODERATOR, ADMINISTRATOR;

    @Override
    public String getAuthority() { return name(); }
}
