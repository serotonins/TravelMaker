package com.ssafy.gumibom.domain.user.sms;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class DefaultRes<T> {
    private int statusCode;
    private String responseMessage;
    private T data;

    public DefaultRes(int statusCode, String responseMessage, T data) {
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
        this.data = data;
    }

    public static <T> DefaultRes<T> res(int statusCode, String responseMessage) {
        return new DefaultRes<>(statusCode, responseMessage, null);
    }

    public static <T> DefaultRes<T> res(int statusCode, String responseMessage, T data) {
        return new DefaultRes<>(statusCode, responseMessage, data);
    }

    // Getters and Setters
}