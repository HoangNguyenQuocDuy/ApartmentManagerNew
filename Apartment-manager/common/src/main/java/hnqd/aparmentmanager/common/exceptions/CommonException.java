package hnqd.aparmentmanager.common.exceptions;

public class CommonException{
    public static class NotFoundException extends BaseException {
        public NotFoundException(String msg) {
            super(msg);
        }
    }

    public static class WrongPasswordException extends BaseException {
        public WrongPasswordException(String msg) {
            super(msg);
        }
    }

    public static class DuplicatePasswordException extends BaseException {
        public DuplicatePasswordException(String msg) {
            super(msg);
        }
    }

    public static class LockerIsUnOccupiedException extends BaseException {
        public LockerIsUnOccupiedException(String msg) {
            super(msg);
        }
    }

    public static class RequestBodyInvalid  extends BaseException {
        public RequestBodyInvalid(String msg) {
            super(msg);
        }
    }

    public static class RoomHasNotUsed  extends BaseException {
        public RoomHasNotUsed(String msg) {
            super(msg);
        }
    }

    public static class PermissionDenied  extends BaseException {
        public PermissionDenied(String msg) {
            super(msg);
        }
    }

    public static class DueDateException extends BaseException {
        public DueDateException(String message) {
            super(message);
        }
    }

    public static class DuplicationError extends BaseException {
        public DuplicationError(String message) {
            super(message);
        }
    }

    public static class TokenExpired extends BaseException {
        public TokenExpired(String message) {
            super(message);
        }
    }

    public static class UnknownValuesException extends BaseException {
        public UnknownValuesException(String message) {
            super(message);
        }
    }
}
