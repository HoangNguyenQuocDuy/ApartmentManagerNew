package hnqd.aparmentmanager.authservice.service.impl;

import hnqd.aparmentmanager.authservice.dto.request.AuthRequest;
import hnqd.aparmentmanager.authservice.dto.response.AuthResponse;
import hnqd.aparmentmanager.authservice.dto.response.UserResponse;
import hnqd.aparmentmanager.authservice.entity.User;
import hnqd.aparmentmanager.authservice.repository.IUserRepo;
import hnqd.aparmentmanager.authservice.service.IAuthService;
import hnqd.aparmentmanager.common.exceptions.CommonException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final IUserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final JWTServiceImpl jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public AuthResponse authenticate(AuthRequest request, HttpServletResponse response)
            throws CommonException.NotFoundException, CommonException.WrongPasswordException {
        User user = userRepo.findByUsername(request.getUsername()).orElseThrow(
                () -> new CommonException.NotFoundException("User not found!")
        );

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CommonException.WrongPasswordException("Invalid password!");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        String jwtAccessToken = jwtService.generateToken(user);

        response.setHeader("Authentication", "Bearer " + jwtAccessToken);

        return AuthResponse.builder()
                .accessToken(jwtAccessToken)
                .userResponse(
                        UserResponse.builder()
                                .id(user.getId())
                                .username(user.getUsername())
                                .email(user.getEmail())
                                .phone(user.getPhone())
                                .firstName(user.getFirstname())
                                .lastName(user.getLastname())
                                .status(user.getStatus())
                                .roleName(user.getRoleName())
                                .build()
                )
                .build();
    }
}
