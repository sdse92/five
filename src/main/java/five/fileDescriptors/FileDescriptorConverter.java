package five.fileDescriptors;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

@Mapper(componentModel = "spring")
@Service
public interface FileDescriptorConverter {

    FileDescriptor toDto(FileDescriptorDocument fileDescriptorDocument);
}
