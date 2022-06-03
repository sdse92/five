package five.courses;

import five.utility.SearchResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseManager courseService;

    public CourseController(CourseManager courseManager) { this.courseService = courseManager; }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'MODERATOR')")
    public Course createCourse(@RequestBody CourseCreateInvoice createInvoice) {
        return courseService.create(createInvoice);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'MODERATOR', 'USER')")
    public SearchResult<Course> gerAllCourses() {
        return courseService.getAll();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'MODERATOR', 'USER')")
    public Course updateCourse(@PathVariable
                                   String id,
                                @RequestBody
                                   CourseUpdateInvoice invoice) {
        return courseService.update(id, invoice);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'MODERATOR', 'USER')")
    public Course gerCourse(@PathVariable
                                String id) {
        return courseService.get(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'MODERATOR')")
    public Course deleteCourse(@PathVariable
                                   String id) {
        return courseService.delete(id);
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'MODERATOR', 'USER')")
    public List<Course> gerCoursesByUser(@PathVariable
                                            String id) {
        return courseService.getByUser(id);
    }
}
