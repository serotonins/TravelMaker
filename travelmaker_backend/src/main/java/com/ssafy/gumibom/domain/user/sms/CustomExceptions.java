package com.ssafy.gumibom.domain.user.sms;

public class CustomExceptions {
    public static class Exception extends RuntimeException {
        private final String message;

        public Exception(String message) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }

    // SMS 인증번호 불일치 예외
    public static class SmsCertificationNumberMismatchException extends RuntimeException {
        public SmsCertificationNumberMismatchException(String message) {
            super(message);
        }
    }

}
