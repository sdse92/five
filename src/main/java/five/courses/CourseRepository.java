package five.courses;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends MongoRepository<CourseDocument, String> {

    CourseDocument findByName(String name);


}
