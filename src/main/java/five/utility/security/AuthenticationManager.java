package five.utility.security;

import five.utility.exception.OperationException;
import five.utility.exception.OperationExceptionBuilder;
import five.users.UserDocument;
import five.users.UserManager;
import five.users.UserPasswordHashEngine;
import five.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static five.utility.exception.OperationExceptionType.*;

@Component
public class AuthenticationManager {

    private UserPasswordHashEngine userPasswordHashEngine;
    private UserRepository userRepository;
    private UserManager userManager;
    private TokenManager tokenManager;

    @Autowired
    public AuthenticationManager(UserPasswordHashEngine userPasswordHashEngine,
                                 UserRepository userRepository,
                                 UserManager userManager,
                                 TokenManager tokenManager) {
        this.userPasswordHashEngine = userPasswordHashEngine;
        this.userRepository = userRepository;
        this.userManager = userManager;
        this.tokenManager = tokenManager;
    }

    public static UserAuthentication getAuthentication() {
        return (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    public static OperationException notAuthenticated(Object attach) {
        return OperationExceptionBuilder.operationException()
                .textcode(ERR_NOT_AUTHORIZED)
                .description("Not authenticated")
                .attachment(attach)
                .build();
    }

    public UserAuthentication authenticateByPassword(final AuthenticationInvoice invoice) {
        final String name = invoice.getName();
        final UserDocument userDocument = userRepository.findByName(name);
        if (userDocument == null) {
            throw accountIsMissingOrPasswordIsIncorrect(invoice.getName());
        }
        final String password = invoice.getPassword();
        if (!userPasswordHashEngine.match(password, userDocument.getPasswordHash())) {
            throw accountIsMissingOrPasswordIsIncorrect(invoice.getName());
        }

        return UserAuthentication.builder()
                .userId(userDocument.getId())
                .userName(userDocument.getName())
                .build();
    }

    public UserAuthentication authenticateByToken(final String tokenPayload) {
        final SecurityToken token = tokenManager.retrieveByPayload(tokenPayload);
        if (token == null) {
            throw tokenIsExpiredOrMissing(tokenPayload);
        }
        final UserDocument userDocument = userRepository.findById(token.getUserId()).orElse(null);
        if (userDocument == null) {
            throw accountIsMissing(token.getUserId());
        }
        return UserAuthentication.builder()
                .userId(userDocument.getId())
                .userName(userDocument.getName())
                .tokenPayload(token.getPayload())
                .authorities(userDocument.getRoles())
                .authenticated(true)
                .build();
    }

    private OperationException accountIsMissingOrPasswordIsIncorrect(String attach) {
        return OperationExceptionBuilder.operationException()
                .textcode(ERR_ACC_OR_PASS)
                .description("Account is missing or password is incorrect")
                .attachment(attach)
                .build();
    }

    private OperationException tokenIsExpiredOrMissing(String token) {
        return OperationExceptionBuilder.operationException()
                .textcode(ERR_ACC_TOKEN)
                .description("Security token is expired or does not exist")
                .attachment(token)
                .build();
    }

    private OperationException accountIsMissing(String accountId) {
        return OperationExceptionBuilder.operationException()
                .textcode(ERR_INTERNAL)
                .description("Account is missing")
                .attachment(accountId)
                .build();
    }
}
