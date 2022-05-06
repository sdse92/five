package five.utility.security;

import lombok.Data;

@Data
public final class AuthenticationInvoice {

    private String name;
    private String password;
    private String passwordUpdate;
    private String tokenPayload;
    private String voucherPayload;

}
