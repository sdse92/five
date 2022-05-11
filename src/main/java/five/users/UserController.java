package five.users;

import five.utility.SearchResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserManager userService;

    public UserController(UserManager userService) {
        this.userService = userService;
    }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'MODERATOR')")
    public User createUser(@RequestBody
                                   UserCreateInvoice invoice) {
        return userService.create(invoice);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'MODERATOR', 'USER')")
    public SearchResult<User> gerAllUsers() {
        return userService.getAll();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'MODERATOR', 'USER')")
    public User updateUser(@PathVariable
                           String id,
                           @RequestBody
                                   UserUpdateInvoice invoice) {
        return userService.update(id, invoice);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'MODERATOR', 'USER')")
    public User gerUser(@PathVariable
                            String id) {
        return userService.get(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'MODERATOR')")
    public User deleteUser(@PathVariable
                            String id) {
        return userService.delete(id);
    }
}
