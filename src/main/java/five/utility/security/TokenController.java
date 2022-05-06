package five.utility.security;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static five.utility.security.AuthenticationManager.getAuthentication;

@RestController
@RequestMapping("/security/tokens/")
@Api(tags = "Sign in")
public class TokenController {

    private final TokenFacade tokenFacade;
    private final AuthenticationManager authenticationManager;

    public TokenController(TokenFacade tokenFacade, AuthenticationManager authenticationManager) {
        this.tokenFacade = tokenFacade;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    @ApiOperation(value = "Create new token by checking account log/password or using voucher",
            notes = "Token need to be attached to header to every authorized requests")
    public SecurityToken createToken(@RequestBody AuthenticationInvoice invoice) {
        UserAuthentication authentication = authenticationManager.authenticateByPassword(invoice);
        return tokenFacade.createForCurrentAccount(authentication);

    }

    @GetMapping("validator")
    @ApiOperation("Check if token is valid and not expired")
    public boolean isValid() {
        return getAuthentication() != null;
    }

    @GetMapping("current")
    @ApiOperation(value = "Retrieve current token", authorizations = @Authorization(value = "bearer"))
    @PreAuthorize("authentication.authenticated")
    public SecurityToken getCurrent() {
        return tokenFacade.retrieveCurrent(getAuthentication());
    }

    @DeleteMapping("current")
    @PreAuthorize("authentication.authenticated")
    @ApiOperation(value = "Delete current token", authorizations = @Authorization(value = "bearer"))
    public void deleteCurrent() {
        tokenFacade.deleteCurrent(getAuthentication());
    }

}
