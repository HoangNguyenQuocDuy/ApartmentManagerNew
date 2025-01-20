package hnqd.aparmentmanager.authservice.service;

import hnqd.aparmentmanager.authservice.dto.request.UserRequest;
import hnqd.aparmentmanager.authservice.dto.response.UserResponse;
import hnqd.aparmentmanager.authservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface IUserService {
    User getUserByUsername(String username);
    UserResponse createUser(UserRequest userReq) throws IOException;
    Page<UserResponse> getUsers(Map<String, String> params);
    UserResponse updateUser(Integer userId, MultipartFile file, Map<String, String> userRequest) throws IOException;
    UserResponse getUserById(int id);
    void deleteUserById(int id);
    void forgotPassword(String email);
    void resetPassword(String email, String verificationCode, String newPassword);
}
