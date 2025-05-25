package hnqd.aparmentmanager.authservice.controller;

import hnqd.aparmentmanager.authservice.dto.request.AuthRequest;
import hnqd.aparmentmanager.authservice.dto.response.ResponseObject;
import hnqd.aparmentmanager.authservice.service.IUserService;
import hnqd.aparmentmanager.authservice.service.impl.AuthServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthServiceImpl authService;
    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {

        try {
            return ResponseEntity.status(200).body(
                    new ResponseObject("OK", "Login successful!", authService.authenticate(authRequest, response))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_EXPECTATION_FAILED).body(
                    new ResponseObject("FAILED", "Login ERROR!", e.getMessage())
            );
        }
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        try {
            userService.forgotPassword(body.get("email"));
            return ResponseEntity.status(HttpStatus.SC_OK).body(
                    new ResponseObject("OK", "Verify token has been sent", "")
            );
        } catch(Exception e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.EXPECTATION_FAILED).body(
                    new ResponseObject("FAILED", "Failed when execute forgot password!", e.getMessage())
            );
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> data) {
        try {
            String email = data.get("email");
            String verificationCode = data.get("verificationCode");
            String newPassword = data.get("newPassword");

            userService.resetPassword(email, verificationCode, newPassword);

            return ResponseEntity.status(HttpStatus.SC_OK).body("Change password successful");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(
                    ex.getMessage());
        }
    }
}
