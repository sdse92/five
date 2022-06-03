package five.files;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "fileDescriptor")
@Data
@RequiredArgsConstructor
public class FileDescriptorDocument {

    @Id
    private String id;
    private String name;
    private String fileType;
    private String storeLocation;
    private String downloadUrl;
    private long size;
    private Instant uploadDate;

}
