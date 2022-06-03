package five.courses;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class Course {

    private String id;
    private String name;
    private String description;
    private String filesDirectory;
    private String courseAdministratorId;
    private Set<String> members;
    private List<String> fileIds;

}
