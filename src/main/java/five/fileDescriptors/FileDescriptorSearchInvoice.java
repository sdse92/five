package five.fileDescriptors;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class FileDescriptorSearchInvoice {

    private List<String> id;
    private String name;
    private List<String> fileType;
    private String courseName;
    private String storeLocation;
    private String downloadUrl;
    private long size;
    private Instant uploadDateFrom;
    private Instant uploadDateTo;
    private Integer pageSize;
    private Integer pageIndex;

}
