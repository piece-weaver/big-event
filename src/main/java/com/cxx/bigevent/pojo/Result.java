package com.cxx.bigevent.pojo;


import com.cxx.bigevent.exception.ErrorCode;
import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public Result() {
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <E> Result<E> success(E data) {
        return new Result<E>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), data);
    }

    public static <E> Result<E> success() {
        return new Result<E>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), null);
    }

    public static <E> Result<E> error(String message) {
        return new Result<E>(ErrorCode.ERROR.getCode(), message, null);
    }

    public static <E> Result<E> error(Integer code, String message) {
        return new Result<E>(code, message, null);
    }

    public static <E> Result<E> error(ErrorCode errorCode) {
        return new Result<E>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static <E> Result<E> error(ErrorCode errorCode, String message) {
        return new Result<E>(errorCode.getCode(), message, null);
    }
}
