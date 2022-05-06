package five.users;

import lombok.Data;

import java.util.Set;

@Data
public class User {

    private String id;
    private String name;
    private String phone;
    private String email;
    private Set<UserRole> roles;
    private Set<String> groups;

}
