package five.courses;

import five.users.User;
import five.users.UserManager;
import five.utility.SearchResult;
import five.utility.exception.OperationException;
import five.utility.exception.OperationExceptionBuilder;
import five.utility.exception.OperationExceptionType;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static five.utility.exception.OperationExceptionType.ERR_COURSE_NOT_EXIST;

@Service
@Log4j2
public class CourseManager {

    private final CourseConverter courseConverter;
    private final CourseRepository courseRepository;
    private final UserManager userManager;

    public CourseManager(CourseConverter courseConverter,
                         CourseRepository courseRepository,
                         UserManager userManager) {
        this.courseConverter = courseConverter;
        this.courseRepository = courseRepository;
        this.userManager = userManager;
    }

    public Course create(CourseCreateInvoice createInvoice) {
       createInvoice.validate();
       CourseDocument course = prepareNewCourse(createInvoice);
       courseRepository.save(course);
       log.info("Created course: " + course);
       return courseConverter.toDto(course);
    }

    public Course update(String id, CourseUpdateInvoice invoice) {
        CourseDocument course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            throw buildException(ERR_COURSE_NOT_EXIST, "Can't find course with id: " + id, id);
        }
        CourseDocument updatedCourse = prepareUpdatedCourse(course, invoice);
        courseRepository.save(updatedCourse);
        log.info("Updated user: " + updatedCourse);
        return courseConverter.toDto(updatedCourse);
    }

    public Course get(String id) {
        CourseDocument course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            throw buildException(ERR_COURSE_NOT_EXIST, "Can't find course with id: " + id, id);
        }
        return courseConverter.toDto(course);
    }

    public SearchResult<Course> getAll() {
        List<Course> courseItems = courseRepository.findAll()
                .stream().map(courseConverter::toDto)
                .collect(Collectors.toList());
        return SearchResult.<Course>builder()
                .items(courseItems)
                .total((long) courseItems.size())
                .build();
    }

    public Course delete(String id) {
        CourseDocument course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            throw buildException(ERR_COURSE_NOT_EXIST, "Can't find course with id: " + id, id);
        }
        courseRepository.delete(course);
        log.info("Deleted course: " + course);
        return courseConverter.toDto(course);
    }

    public List<Course> getByUser(String userId) {
        User user = userManager.get(userId);
        List<Course> courses = new ArrayList<>();
        if (user != null && user.getGroups() != null && !user.getGroups().isEmpty()) {
            for ( String groupId: user.getGroups()) {
                CourseDocument courseDocument = courseRepository.findById(groupId).orElse(null);
                if (courseDocument != null) {
                    courses.add(courseConverter.toDto(courseDocument));
                }
            }
        }
        return courses;
    }

    private CourseDocument prepareNewCourse(CourseCreateInvoice invoice) {
        CourseDocument newCourse = new CourseDocument();
        newCourse.setName(invoice.getName());
        newCourse.setDescription(invoice.getDescription());
        newCourse.setFilesDirectory(invoice.getFilesDirectory());
        newCourse.setCourseAdministratorId(invoice.getCourseAdministratorId());
        newCourse.setMembers(invoice.getMembers());
        newCourse.setFileIds(invoice.getFileIds());
        return newCourse;
    }

    private CourseDocument prepareUpdatedCourse(CourseDocument course ,CourseUpdateInvoice invoice) {
        if (invoice.getName() != null) {
            course.setName(invoice.getName());
        }
        if (invoice.getDescription() != null) {
            course.setDescription(invoice.getDescription());
        }
        if (invoice.getFilesDirectory() != null) {
            course.setFilesDirectory(invoice.getFilesDirectory());
        }
        if (invoice.getCourseAdministratorId() != null) {
            course.setCourseAdministratorId(invoice.getCourseAdministratorId());
        }
        if (invoice.getMembers() != null && !invoice.getMembers().isEmpty()) {
            course.setMembers(invoice.getMembers());
        }
        if (invoice.getFileIds() != null && !invoice.getFileIds().isEmpty()) {
            course.setFileIds(invoice.getFileIds());
        }
        return course;
    }

    private OperationException buildException(OperationExceptionType exceptionType, String description, String attach) {
        return OperationExceptionBuilder.operationException()
                .textcode(exceptionType)
                .description(description)
                .attachment(attach)
                .build();
    }
}
