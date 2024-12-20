package hnqd.aparmentmanager.common.exceptions;

import hnqd.aparmentmanager.common.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleIoException(IOException ex) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(CommonException.NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(CommonException.NotFoundException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(CommonException.WrongPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleWrongPasswordException(CommonException.NotFoundException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CommonException.DuplicatePasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDuplicatePasswordException(CommonException.DuplicatePasswordException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CommonException.LockerIsUnOccupiedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicatePasswordException(CommonException.LockerIsUnOccupiedException ex) {
        return new ErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(CommonException.RequestBodyInvalid.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRequestBodyInvalidException(CommonException.RequestBodyInvalid ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CommonException.RoomHasNotUsed.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleRoomHasNotUsedException(CommonException.RoomHasNotUsed ex) {
        return new ErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(CommonException.PermissionDenied.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handlePermissionDeniedException(CommonException.PermissionDenied ex) {
        return new ErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(CommonException.DueDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDueDateException(CommonException.DueDateException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CommonException.DuplicationError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDuplicationException(CommonException.DuplicationError ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
