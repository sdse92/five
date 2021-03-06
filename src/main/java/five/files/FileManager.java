package five.files;

import five.courses.CourseDocument;
import five.courses.CourseRepository;
import five.utility.exception.OperationException;
import five.utility.exception.OperationExceptionBuilder;
import five.utility.exception.OperationExceptionType;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

import static five.utility.exception.OperationExceptionType.*;

@Service
@Log4j2
public class FileManager {

    private final FileDescriptorConverter fileDescriptorConverter;
    private final FileDescriptorRepository fileDescriptorRepository;
    private final CourseRepository courseRepository;

    public FileManager(FileDescriptorConverter fileDescriptorConverter, FileDescriptorRepository fileDescriptorRepository, CourseRepository courseRepository) {
        this.fileDescriptorConverter = fileDescriptorConverter;
        this.fileDescriptorRepository = fileDescriptorRepository;
        this.courseRepository = courseRepository;
    }

    public FileDescriptor saveFile(MultipartFile file, String courseDescriptor) {
        CourseDocument course = courseRepository.findByName(courseDescriptor);
        if (course == null || course.getFilesDirectory() == null) {
            throw buildException(ERR_COURSE_UPLOAD_DIRECTORY,
                    "Course with name " + courseDescriptor + " not exist or 'filesDirectory' not exist",
                    courseDescriptor);
        }
        File uploadedFile = new File(course.getFilesDirectory() + "/" + file.getOriginalFilename());
        if (uploadedFile.exists()) {
            throw buildException(ERR_EXIST_FILENAME,
                    "File with this name " + file.getOriginalFilename() + " already exist",
                    file.getOriginalFilename());
        }

        FileDescriptorDocument uploadedObject = buildFileDescriptor(file, course.getFilesDirectory());
        uploadFile(file, uploadedFile);
        fileDescriptorRepository.save(uploadedObject);
        log.info("Object uploaded: " + uploadedObject);
        return fileDescriptorConverter.toDto(uploadedObject);
    }

    public Resource loadFileAsResource(String fileName) {
        FileDescriptorDocument fileDescriptor = fileDescriptorRepository.findByName(fileName);
        Resource resource = null;
        try {
            Path filePath = Paths.get(fileDescriptor.getStoreLocation());
            resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                fileDescriptorNotFound(fileName);
            }
        } catch (MalformedURLException ex) {
            fileDescriptorNotFound(fileName);
        }
        return resource;
    }

    private FileDescriptorDocument buildFileDescriptor(MultipartFile file, String course) {
        FileDescriptorDocument uploadedObject = new FileDescriptorDocument();
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/file/downloadFile/")
                .toUriString();
        uploadedObject.setName(file.getOriginalFilename());
        uploadedObject.setFileType(file.getContentType());
        uploadedObject.setStoreLocation(course + "/" + file.getOriginalFilename());
        try {
            uploadedObject.setDownloadUrl(String.valueOf(new URL(fileDownloadUri + file.getOriginalFilename())));
        } catch (MalformedURLException e) {
            throw buildException(ERR_URL_ENCODE,
                    "Can't encode url " + fileDownloadUri + file.getOriginalFilename(),
                    e.getMessage());
        }
        uploadedObject.setSize(file.getSize());
        uploadedObject.setUploadDate(Instant.now());
        return uploadedObject;
    }

    private void uploadFile(MultipartFile file, File uploadedFile) {
        try (FileOutputStream fos = new FileOutputStream(uploadedFile)) {
            byte[] fileBytes = file.getBytes();
            fos.write(fileBytes);
        } catch (IOException e) {
            throw buildException(ERR_UPLOAD_FILE,
                    "Can't upload file " + file.getOriginalFilename(),
                    file.getOriginalFilename());
        }
    }

    private void fileDescriptorNotFound(String fileName){
        throw buildException(ERR_FILE_DESCRIPTOR_NOT_EXIST,
                "File Descriptor with File name " + fileName + " not exist",
                fileName);
    }

    private OperationException buildException(OperationExceptionType exceptionType, String description, String attach) {
        return OperationExceptionBuilder.operationException()
                .textcode(exceptionType)
                .description(description)
                .attachment(attach)
                .build();
    }
}
