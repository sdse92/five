package five.utility.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenFacade{

    private TokenManager tokenManager;

    @Autowired
    public TokenFacade(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public SecurityToken createForCurrentAccount(UserAuthentication authentication) {
        return tokenManager.create(authentication.getUserId());
    }

    public SecurityToken retrieveCurrent(UserAuthentication authentication) {
        return tokenManager.retrieveByPayload(authentication.getTokenPayload());
    }

    public void deleteCurrent(UserAuthentication authentication) {
        tokenManager.deleteByPayload(authentication.getTokenPayload());
    }


}
