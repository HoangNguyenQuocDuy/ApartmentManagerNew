package hnqd.aparmentmanager.authservice.controller;

import hnqd.aparmentmanager.authservice.dto.request.UserRequest;
import hnqd.aparmentmanager.authservice.dto.response.ResponseObject;
import hnqd.aparmentmanager.authservice.dto.response.UserResponse;
import hnqd.aparmentmanager.authservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @PostMapping("/")
    public ResponseEntity<ResponseObject> createUser(@RequestBody UserRequest u) {
        log.info("#UserController.createUser - Start - username: {}, firstname: {}, lastname: {}, phone: {}," +
                        "email: {}, status: {}", u.getUsername(), u.getFirstname(), u.getLastname(),
                u.getPhone(), u.getEmail(), u.getStatus());
        try {
            UserResponse userSave = userService.createUser(u);
            log.info("#UserController.createUser - save user successfully: {}", userSave.getId());
            return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(
                    new ResponseObject("OK", "Create user successfully!", userSave)
            );
        } catch (Exception e) {
            log.info("#UserController.createUser - Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<ResponseObject> updateUser(@PathVariable("userId") Integer userId,
                                                     @RequestPart("file") @Nullable MultipartFile file,
                                                     @RequestParam @Nullable Map<String, String> params){
        log.info("#UserController.updateUser - Start");
        try {
            UserResponse userSave = userService.updateUser(userId, file, params);

            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Update user successfully!", userSave)
            );
        } catch (Exception e) {
            log.info("#UserController.updateUser - Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getListUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id, desc") String sort,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all
    ) {
        try {
            log.info("#UserController.getListUser - Start - page: {}, size: {}, sort: {}, filter: {}, search: {}",
                    page, size, sort, filter, search);
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get list user successfully!",
                            userService.getListUser(page, size, sort, filter, search, all)
                    )
            );
        } catch (Exception e) {
            log.info("#UserController.getListUser - Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseObject> getUserById(@PathVariable("userId") int userId) {
        try {
            log.info("#UserController.getUserById - Start - userId: {}", userId);
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get user by Id successfully!", userService.getUserById(userId))
            );
        } catch (Exception e) {
            log.info("#UserController.getUserById - Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseObject> deleteUser(@PathVariable("userId") Integer userId) {
        try {
            log.info("#UserController.deleteUser - Start - userId: {}", userId);
            userService.deleteUserById(userId);
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Delete this user successfully!", "")
            );
        } catch (Exception e) {
            log.info("#UserController.deleteUser - Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }
}
