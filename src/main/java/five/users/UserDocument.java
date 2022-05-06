package five.users;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "user")
@Data
@RequiredArgsConstructor
public class UserDocument {

    @Id
    private String id;
    private String name;
    private String passwordHash;
    private String phone;
    private String email;
    private Set<UserRole> roles;
    private Set<String> groups;

}
