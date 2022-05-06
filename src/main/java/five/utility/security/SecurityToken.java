package five.utility.security;

import five.users.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

@Data
@Builder
public class SecurityToken {

    private final String id;
    private final String userId;
    private final String payload;
    private final Instant createdAt;
    private final Set<UserRole> roles;
    private Instant validUntil;

    public boolean isExpired() {
        return validUntil.isBefore(Instant.now());
    }

    public void prolongate(final Duration duration) {
        validUntil = validUntil.plus(duration);
    }
}
