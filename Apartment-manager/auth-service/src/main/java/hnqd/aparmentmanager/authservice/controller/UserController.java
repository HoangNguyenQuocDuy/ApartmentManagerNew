package hnqd.aparmentmanager.authservice.controller;

import hnqd.aparmentmanager.authservice.dto.request.UserRequest;
import hnqd.aparmentmanager.authservice.dto.response.ResponseObject;
import hnqd.aparmentmanager.authservice.dto.response.UserResponse;
import hnqd.aparmentmanager.authservice.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("/")
    public ResponseEntity<ResponseObject> createUser(@ModelAttribute UserRequest u) {
        try {
            UserResponse userSave = userService.createUser(u);
            return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(
                    new ResponseObject("OK", "Create user successfully!", userSave)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ResponseObject> updateUser(@PathVariable("userId") Integer userId,
                                                     @RequestPart("file")MultipartFile file,
                                                     @RequestParam Map<String, String> params){
        try {
            UserResponse userSave = userService.updateUser(userId, file, params);

            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Update user successfully!", userSave)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getListUser(@RequestParam Map<String, String> params) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get list user successfully!", userService.getUsers(params))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseObject> getUserById(@PathVariable int userId) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get user by Id successfully!", userService.getUserById(userId))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseObject> deleteUser(@PathVariable("userId") Integer userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Delete this user successfully!", "")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }
}
