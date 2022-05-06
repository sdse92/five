package five.utility.security;

import five.users.UserRole;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.Authentication;

import java.util.Collection;

@Data
@Builder
public final class UserAuthentication implements Authentication {

    private final String userId;
    private final String userName;
    private final String generatedTokenPayload;
    private final boolean passwordUpdated;
    private final String tokenPayload;
    private final Collection<UserRole> authorities;

    private final String name;
    private final transient Object credentials;
    private final transient Object details;
    private final transient Object principal;
    private boolean authenticated;
}
