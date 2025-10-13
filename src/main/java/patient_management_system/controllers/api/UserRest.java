package patient_management_system.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.User;
import patient_management_system.service.UserService;

@Tag(name = "User Management")
@RestController
@RequestMapping("/api/users")
public class UserRest {

    private final UserService userService;

    @Autowired
    public UserRest(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "This endpoint is used to fetch all users from the db")
    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(){
        return userService.findAll();
    }

    @Operation(summary = "This endpoint is used to fetch a user record by id")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> findById(@PathVariable String id){
        return userService.findById(id);
    }

    @Operation(summary = "This endpoint is used to add new user record")
    @PostMapping
    public ResponseEntity<ResponseDTO> addUser(@RequestBody @Valid User user){
        return userService.addUser(user);
    }

    @PutMapping()
    public ResponseEntity<ResponseDTO> updatedById(@RequestBody User user){
        return userService.updateById(user);
    }

    @Operation(summary = "This endpoint is used to delete a user record by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteById(@PathVariable String id){
        return userService.deleteById(id);
    }
}
