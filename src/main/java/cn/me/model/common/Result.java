package cn.me.model.common;

import cn.hutool.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Result<T> implements Serializable
{
    /**
     * 请求情况
     */
    private Boolean success;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 状态码
     * 400 Client Error
     * 401 Unauthorized
     * 403 Forbidden
     * 404 Not Found
     * 500 System Error
     */
    private Integer code;

    /**
     * 反馈消息
     */
    private String msg;

    /**
     * 反馈数据
     */
    private T data;

    private Result(Boolean success, Integer code, String msg, T data)
    {
        this.success = success;
        this.timestamp = System.currentTimeMillis();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> success(T data, String msg)
    {
        return new Result<>(true, HttpStatus.HTTP_OK, msg, data);
    }

    public static <T> Result<T> success(T data)
    {
        return success(data, "OK");
    }

    public static <T> Result<T> success()
    {
        return success(null);
    }

    public static <T> Result<T> error(Integer code, String msg)
    {
        return new Result<>(false, code, msg, null);
    }

    public static <T> Result<T> error(String msg)
    {
        return error(HttpStatus.HTTP_BAD_REQUEST, msg);
    }

    public static <T> Result<T> error()
    {
        return error(HttpStatus.HTTP_BAD_REQUEST, null);
    }
}
