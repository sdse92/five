package five.users;

import five.utility.SearchResult;
import five.utility.exception.OperationException;
import five.utility.exception.OperationExceptionBuilder;
import five.utility.exception.OperationExceptionType;
import five.utility.security.UserAuthentication;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static five.utility.exception.OperationExceptionType.ERR_ACC_NOT_EXIST;
import static five.utility.exception.OperationExceptionType.ERR_NOT_AUTHORIZED;

@Service
@Log4j2
public class UserManager {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final UserPasswordHashEngine hashEngine;

    public UserManager(UserRepository userRepository, UserConverter userConverter, UserPasswordHashEngine hashEngine) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.hashEngine = hashEngine;
    }

    public User create(UserCreateInvoice invoice) {
        invoice.validate();
        UserDocument newUser = prepareUser(invoice);
        userRepository.save(newUser);
        log.info("Created user: " + newUser);
        return userConverter.toDto(newUser);
    }

    public User update(String id, UserUpdateInvoice invoice) {
        validateUserOperation(id);
        invoice.validate();
        UserDocument user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw buildException(ERR_ACC_NOT_EXIST, "Can't find user with id: " + id, id);
        }
        UserDocument updatedUser = prepareUpdatedUser(user, invoice);
        userRepository.save(updatedUser);
        log.info("Updated user: " + updatedUser);
        return userConverter.toDto(updatedUser);
    }

    public User get(String id) {
        UserDocument user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw buildException(ERR_ACC_NOT_EXIST, "Can't find user with id: " + id, id);
        }
        return userConverter.toDto(user);
    }

    public SearchResult<User> getAll() {
        List<User> userItems = userRepository.findAll()
                .stream().map(userConverter::toDto)
                .collect(Collectors.toList());
        return SearchResult.<User>builder()
                .items(userItems)
                .total((long) userItems.size())
                .build();
    }

    public User delete(String id) {
        UserDocument user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw buildException(ERR_ACC_NOT_EXIST, "Can't find user with id: " + id, id);
        }
        userRepository.delete(user);
        log.info("Deleted user: " + user);
        return userConverter.toDto(user);
    }

    private UserDocument prepareUser(UserCreateInvoice invoice) {
        UserDocument user = new UserDocument();
        user.setName(invoice.getName());
        user.setPasswordHash(hashEngine.hash(invoice.getPassword()));
        user.setPhone(invoice.getPhone());
        user.setEmail(invoice.getEmail());
        user.setRoles(invoice.getRoles());
        user.setGroups(invoice.getGroups());
        return user;
    }

    private UserDocument prepareUpdatedUser(UserDocument user, UserUpdateInvoice invoice) {
        if (invoice.getName() != null) {
            user.setName(invoice.getName());
        }
        if (invoice.getPassword() != null) {
            user.setPasswordHash(hashEngine.hash(invoice.getPassword()));
        }
        if (invoice.getPhone() != null) {
            user.setPhone(invoice.getPhone());
        }
        if (invoice.getEmail() != null) {
            user.setEmail(invoice.getEmail());
        }
        if (invoice.getRoles() != null && !invoice.getRoles().isEmpty()) {
            user.setRoles(invoice.getRoles());
        }
        if (invoice .getGroups() != null && !invoice.getGroups().isEmpty()) {
            user.setGroups(invoice.getGroups());
        }
        return user;
    }

    private void validateUserOperation(String id) {
        UserAuthentication authentication = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().contains(UserRole.ADMINISTRATOR) ||
            !authentication.getUserId().equals(id)) {
            throw buildException(ERR_NOT_AUTHORIZED, "You have no rights to change user id:" + id, id);
        }
    }

    private OperationException buildException(OperationExceptionType exceptionType, String description, String attach) {
        return OperationExceptionBuilder.operationException()
                .textcode(exceptionType)
                .description(description)
                .attachment(attach)
                .build();
    }
}
