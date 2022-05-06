package five.utility.security;

import five.users.User;
import five.users.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class TokenManager {

    private UserManager userManager;
    private Map<String, SecurityToken> tokens;
    private Duration tokenExpirationPeriod;

    @Autowired
    public TokenManager(UserManager accountSystemManager) {
        this.userManager = accountSystemManager;
        this.tokens = new HashMap<>();
        this.tokenExpirationPeriod = Duration.ofHours(1);
    }


    public SecurityToken create(final String accountId) {
        return create(accountId, tokenExpirationPeriod);
    }

    public SecurityToken create(final String accountId, TemporalAmount expiration) {
        final SecurityToken token = createNewToken(accountId, expiration);
        tokens.put(token.getPayload(), token);
        return token;
    }

    public SecurityToken retrieveByPayload(final String payload) {
        final SecurityToken token = tokens.get(payload);
        if (token == null || token.isExpired()) {
            tokens.remove(payload);
            return null;
        }
        token.prolongate(tokenExpirationPeriod);
        return token;
    }

    public void deleteByPayload(final String payload) {
        tokens.remove(payload);
    }

    private SecurityToken createNewToken(final String accountId, TemporalAmount expiration) {
        User user = userManager.get(accountId);
        return SecurityToken.builder()
                .id(UUID.randomUUID().toString())
                .payload(UUID.randomUUID().toString())
                .userId(accountId)
                .roles(user.getRoles())
                .createdAt(Instant.now())
                .validUntil(Instant.now().plus(expiration))
                .build();
    }
}
