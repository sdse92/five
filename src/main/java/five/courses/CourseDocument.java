package five.courses;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Document(collection = "course")
@Data
@RequiredArgsConstructor
public class CourseDocument {

    @Id
    private String id;
    private String name;
    private String description;
    private String courseAdministratorId;
    private Set<String> members;
    private List<String> fileIds;

}
