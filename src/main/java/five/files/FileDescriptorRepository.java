package five.files;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDescriptorRepository extends MongoRepository<FileDescriptorDocument, String> {
    FileDescriptorDocument findByName(String name);
}
