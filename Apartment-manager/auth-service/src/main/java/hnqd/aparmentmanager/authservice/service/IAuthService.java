package hnqd.aparmentmanager.authservice.service;

import hnqd.aparmentmanager.authservice.dto.request.AuthRequest;
import hnqd.aparmentmanager.authservice.dto.response.AuthResponse;
import hnqd.aparmentmanager.common.exceptions.CommonException;
import jakarta.servlet.http.HttpServletResponse;

public interface IAuthService {
    AuthResponse authenticate(AuthRequest request, HttpServletResponse response)
            throws CommonException.NotFoundException, CommonException.WrongPasswordException;
}
