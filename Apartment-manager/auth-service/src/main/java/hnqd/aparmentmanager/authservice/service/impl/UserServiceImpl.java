package hnqd.aparmentmanager.authservice.service.impl;

import hnqd.aparmentmanager.authservice.client.IChatRoomService;
import hnqd.aparmentmanager.authservice.dto.request.UserRequest;
import hnqd.aparmentmanager.authservice.dto.response.UserResponse;
import hnqd.aparmentmanager.authservice.entity.User;
import hnqd.aparmentmanager.authservice.repository.IUserRepo;
import hnqd.aparmentmanager.authservice.service.IUserService;
import hnqd.aparmentmanager.common.Enum.EEmailType;
import hnqd.aparmentmanager.common.dto.response.ListResponse;
import hnqd.aparmentmanager.common.dto.response.RestResponse;
import hnqd.aparmentmanager.common.exceptions.CommonException;
import hnqd.aparmentmanager.common.utils.UploadImage;
import io.github.perplexhub.rsql.RSQLJPASupport;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private static SecureRandom random = new SecureRandom();
    private final IUserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RabbitTemplate rabbitTemplate;
    private final ModelMapper modelMapper;
    private final UploadImage uploadImage;
    private final IChatRoomService chatRoomService;

    @Override
    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(
                () -> new CommonException.NotFoundException("User not found with username: " + username)
        );
    }

    @Override
    @Transactional
    public UserResponse createUser(UserRequest userReq) {
        if (userRepo.existsByUsername(userReq.getUsername())) {
            throw new CommonException.DuplicationError("User with username has exists");
        }
        if (userRepo.existsByEmailAndStatus(userReq.getEmail(), "Active")) {
            throw new CommonException.DuplicationError("User with email has exists");
        }
        if (userRepo.existsByEmailAndStatus(userReq.getEmail(), "New")) {
            throw new CommonException.DuplicationError("User with email has exists");
        }

        User user = new User();
        String password = generateRandomString(6);

        user.setStatus("New");
        user.setPhone(userReq.getPhone());
        user.setEmail(userReq.getEmail());
        user.setFirstname(userReq.getFirstname());
        user.setLastname(userReq.getLastname());
        user.setUsername(userReq.getUsername());
        user.setRoleName("ROLE_RESIDENT");
        user.setPassword(passwordEncoder.encode(password));
        int userId = userRepo.saveAndFlush(user).getId();

        Map<String, String> otpMessage = new HashMap<>();
        otpMessage.put("otp", password);
        otpMessage.put("email", user.getEmail());
        otpMessage.put("username", user.getUsername());
        otpMessage.put("mailType", EEmailType.WELCOME.name());
        otpMessage.put("subject", "WELCOME TO QUOC DUY APARTMENT");
        log.info("Start send to notification's queue ->>>>>>");
        rabbitTemplate.convertAndSend(
                "notificationExchange",
                "rSc7D1FNUS",
                otpMessage,
                message -> {
                    message.getMessageProperties().setCorrelationId(UUID.randomUUID().toString());
                    return message;
                }
        );

        User userFind = userRepo.findById(userId).orElseThrow(
                () -> new CommonException.NotFoundException("User not found with username: " + user.getUsername())
        );

        return modelMapper.map(userFind, UserResponse.class);
    }

    @Override
    public Page<UserResponse> getUsers(Map<String, String> params) {
        int page = Integer.parseInt(params.get("page"));
        int size = Integer.parseInt(params.get("size"));
        Pageable pageable = PageRequest.of(page, size);

        Page<User> userFindPage = null;
        if (params.get("roleName") != null && !params.get("roleName").isEmpty()) {
            userFindPage = userRepo.findAllByRoleName(params.get("roleName"), pageable);
            List<UserResponse> userResponses = userFindPage.stream().map(
                    u -> modelMapper.map(u, UserResponse.class)
            ).collect(Collectors.toList());

            return new PageImpl<>(userResponses, pageable, userFindPage.getTotalElements());
        }

        userFindPage = userRepo.findAll(pageable);
        List<UserResponse> userResponses = userFindPage.stream().map(
                u -> modelMapper.map(u, UserResponse.class)
        ).collect(Collectors.toList());

        return new PageImpl<>(userResponses, pageable, userFindPage.getTotalElements());
    }

    @Transactional
    @Override
    public UserResponse updateUser(Integer userId, MultipartFile file, Map<String, String> params) throws IOException {
        String fN = params.getOrDefault("firstname", "");
        String lN = params.getOrDefault("lastname", "");
        String phone = params.getOrDefault("phone", "");
        String email = params.getOrDefault("email", "");
        String status = params.getOrDefault("status", "");

        UserRequest userReq = UserRequest.builder()
                .file(file)
                .password(params.get("password"))
//                .roomId(Integer.valueOf(params.getOrDefault("roomId", "0")))
                .build();

        if (!fN.equals("")) {
            userReq.setFirstname(fN);
        }
        if (!lN.equals("")) {
            userReq.setLastname(lN);
        }
        if (!phone.equals("")) {
            userReq.setPhone(phone);
        }
        if (!status.equals("")) {
            userReq.setStatus(status);
        }

        User storedUser = userRepo.findById(userId).orElseThrow(
                () -> new CommonException.NotFoundException("User not found with ID: " + userId)
        );
        String userStoredStatus = storedUser.getStatus();

        if (userReq.getPassword() != null && !userReq.getPassword().isEmpty()) {
            String storedPassword = storedUser.getPassword();

            User user = userRepo.findByRoleName("ROLE_ADMIN");

//            ChatRoomRequestDto chatRoomRequestDto = new ChatRoomRequestDto();
//            Set<Integer> userIds = new HashSet<>();
//            userIds.add(user.getId());
//            userIds.add(userId);
//            chatRoomRequestDto.setUsersIds(userIds);
//
//            chatRoomService.createRoom(chatRoomRequestDto);

            if (!passwordEncoder.matches(storedPassword, userReq.getPassword())) {
                storedUser.setPassword(passwordEncoder.encode(userReq.getPassword()));
            } else {
                throw new CommonException.DuplicatePasswordException("Duplicate password!");
            }
        }

        if (userReq.getFile() != null && !userReq.getFile().isEmpty()) {
            storedUser.setAvatar(uploadImage.uploadToCloudinary(userReq.getFile()));
        }
        if (userReq.getStatus() != null && !userReq.getStatus().isEmpty()) {
            storedUser.setStatus(userReq.getStatus());
        }
        if (!fN.equals("")) {
            storedUser.setFirstname(fN);
        }
        if (!lN.equals("")) {
            storedUser.setLastname(lN);
        }
        if (!phone.equals("")) {
            storedUser.setPhone(phone);
        }
        User user = userRepo.save(storedUser);

        if (userStoredStatus.equals("New")) {
            chatRoomService.createOrGetChatRoomWithAdmin(userId);
            chatRoomService.createOrGetChatRoomWithCommonGroup(userId);
        }

        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public UserResponse getUserById(int id) throws CommonException.NotFoundException {
        User user = userRepo.findById(id).orElseThrow(
                () -> new CommonException.NotFoundException("User not found with ID: " + id)
        );
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public void deleteUserById(int id) {
        User storedUser = userRepo.findById(id).orElseThrow(
                () -> new CommonException.NotFoundException("User not found with ID: " + id)
        );

//        storedUser.getLikedPosts().clear();
        userRepo.delete(storedUser);
    }

    @Override
    public void forgotPassword(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(
                () -> new CommonException.NotFoundException("User not found with email: " + email)
        );

        if (user == null) {
            throw new RuntimeException("User with Email: " + email + " not found!");
        }
        String verificationCode = generateVerificationCode();
        user.setResetPasswordCode(verificationCode);
        userRepo.save(user);
//        emailSender.sendEmail(
//                email,
//                "YOUR VERIFICATION CODE FROM QD APARTMENT",
//                "Your verification code is: " + verificationCode
//        );

        Map<String, String> otpMessage = new HashMap<>();
        otpMessage.put("otp", verificationCode);
        otpMessage.put("email", user.getEmail());
        otpMessage.put("username", user.getUsername());
        otpMessage.put("mailType", EEmailType.RESET_PASSWORD.getName());
        otpMessage.put("subject", "RESET PASSWORD REQUEST CODE FROM QUOC DUY APARTMENT");
        rabbitTemplate.convertAndSend(
                "notificationExchange",
                "rSc7D1FNUS",
                otpMessage,
                message -> {
                    message.getMessageProperties().setCorrelationId(UUID.randomUUID().toString());
                    return message;
                }
        );
    }

    @Override
    public void resetPassword(String email, String verificationCode, String newPassword) {
        User user = userRepo.findByEmail(email).orElseThrow(
                () -> new CommonException.NotFoundException("User not found with email: " + email)
        );

        if (user == null) {
            throw new RuntimeException("User with Email: " + email + " not found!");
        }

        if (!user.getResetPasswordCode().equals(verificationCode)) {
            throw new IllegalArgumentException("Invalid verification code");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordCode(null);
        userRepo.save(user);
    }

    @Override
    public RestResponse<ListResponse<UserResponse>> getListUser(int page, int size,
                                                                String sort, String filter,
                                                                String search, boolean all) {
        Specification<User> sortable = RSQLJPASupport.toSort(sort);
        Specification<User> filterable = RSQLJPASupport.toSpecification(filter);

        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page, size);

        Page<User> resultPage = userRepo.findAll(sortable.and(filterable), pageable);

        Page<UserResponse> responsePage = resultPage.map(user -> modelMapper.map(user, UserResponse.class));

        return RestResponse.ok(ListResponse.of(responsePage));
    }

    private String generateRandomString(int length) {
        StringBuilder randomString = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            randomString.append(CHARACTERS.charAt(randomIndex));
        }

        return randomString.toString();
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
