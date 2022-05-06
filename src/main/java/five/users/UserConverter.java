package five.users;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

@Mapper(componentModel = "spring")
@Service
public interface UserConverter {

    User toDto(UserDocument userDocument);
}
