package five.files;

import lombok.Data;

import java.time.Instant;

@Data
public class FileDescriptor {

    private String id;
    private String name;
    private String fileType;
    private String storeLocation;
    private String downloadUrl;
    private long size;
    private Instant uploadDate;

}
