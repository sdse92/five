package five.courses;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class CourseSearchInvoice {

    private String id;
    private String name;
    private String description;
    private String filesDirectory;
    private String courseAdministratorId;
    private Set<String> members;
    private List<String> fileIds;
    private Integer pageSize;
    private Integer pageIndex;

}
