package five.courses;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class CourseUpdateInvoice {

    private String name;
    private String description;
    private String courseAdministratorId;
    private Set<String> members;
    private List<String> fileIds;
}
