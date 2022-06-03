package five.courses;

import five.utility.ValidationHelper;
import five.utility.exception.OperationException;
import five.utility.exception.OperationExceptionBuilder;
import five.utility.exception.OperationExceptionType;
import lombok.Data;

import java.util.List;
import java.util.Set;

import static five.utility.exception.OperationExceptionType.ERR_MISS_MANDATORY_FIELD;

@Data
public class CourseCreateInvoice implements ValidationHelper {

    private String name;
    private String description;
    private String filesDirectory;
    private String courseAdministratorId;
    private Set<String> members;
    private List<String> fileIds;

    @Override
    public void validateFields() {
        if (name == null) {
            throw  buildException(ERR_MISS_MANDATORY_FIELD, "Course 'name' is mandatory field", null);
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
