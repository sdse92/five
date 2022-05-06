package five.users;

import five.utility.exception.OperationException;
import five.utility.exception.OperationExceptionBuilder;
import five.utility.exception.OperationExceptionType;
import five.utility.ValidationHelper;
import lombok.Data;

import java.util.Set;

import static five.utility.exception.OperationExceptionType.*;

@Data
public class UserCreateInvoice implements ValidationHelper {

    private String name;
    private String password;
    private String phone;
    private String email;
    private Set<UserRole> roles;
    private Set<String> groups;


    @Override
    public void validateFields() {
        if (name == null) {
            throw  buildException(ERR_MISS_MANDATORY_FIELD, "Users 'name' is mandatory field", null);
        }

        if (password == null) {
            throw  buildException(ERR_MISS_MANDATORY_FIELD, "Users 'password' is mandatory field", null);
        }

        if (phone != null && !validatePhone(phone)) {
            throw  buildException(ERR_ILLEGAL_PHONE, "Users 'phone' doesn't match format +7(999)1112233", phone);
        }

        if (email != null && !validateEmail(email)) {
            throw  buildException(ERR_ILLEGAL_EMAIL, "Users 'email' doesn't match format user@user.com", email);
        }

        if (roles == null || roles.isEmpty() || !validateRoles(roles)) {
            throw  buildException(ERR_NO_ROLES, "Users 'roles' should contain at least one valid role", null);
        }
    }

    private OperationException buildException(OperationExceptionType exceptionType, String description, String attach) {
        return OperationExceptionBuilder.operationException()
                .textcode(exceptionType)
                .description(description)
                .attachment(attach)
                .build();
    }
}
