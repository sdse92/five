package five.utility;

import five.users.UserRole;

import java.util.Set;
import java.util.regex.Pattern;

public interface ValidationHelper {

    default void validate() { validateFields(); }

    default boolean validateEmail(String email) {
        final Pattern emailPattern =
                Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$");
        return emailPattern.matcher(email).find();
    }

    default boolean validatePhone(String phone) {
        final Pattern phonePattern =
                Pattern.compile("^[+]+[0-9]{1}+[(]+[0-9]{3}+[)]+[0-9]{7}$");
        return phonePattern.matcher(phone).find();
    }

    default boolean validateRoles(Set<UserRole> roles) {
        for (UserRole role : UserRole.values()) {
            if (roles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    void validateFields();
}
