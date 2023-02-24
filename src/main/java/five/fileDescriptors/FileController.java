package five.fileDescriptors;


import five.utility.SearchResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/file")
@Log4j2
public class FileController {

    private final FileManager fileManager;

    public FileController(FileManager fileManager) { this.fileManager = fileManager; }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'MODERATOR')")
    public FileDescriptor saveFile( @RequestParam("file") MultipartFile file,
                                    @RequestParam("courseDescriptor") String courseDescriptor) {
        return fileManager.saveFile(file, courseDescriptor);
    }

    @PostMapping("/multipleFiles")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'MODERATOR')")
    public List<FileDescriptor> saveFiles(@RequestParam("files") MultipartFile[] files,
                                         @RequestParam("courseDescriptor") String courseDescriptor) {
        return Arrays.asList(files)
                .stream()
                .map(file -> saveFile(file, courseDescriptor))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'MODERATOR', 'USER')")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileManager.loadFileAsResource(fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/filtered")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'MODERATOR', 'USER')")
    public SearchResult<FileDescriptor> getDescriptorsByFilter(@RequestBody FileDescriptorSearchInvoice descriptorSearchInvoice) {
        return fileManager.retrieveByFilter(descriptorSearchInvoice);
    }

}
