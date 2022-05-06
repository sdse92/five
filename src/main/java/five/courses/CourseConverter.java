package five.courses;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

@Mapper(componentModel = "spring")
@Service
public interface CourseConverter {

    Course toDto(CourseDocument courseDocument);
}
