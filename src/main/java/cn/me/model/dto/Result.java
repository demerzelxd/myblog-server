package cn.me.model.dto;

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

    public static <T> Result<T> success(T data)
    {
        return new Result<>(true, 200, "OK", data);
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
        return error(400, msg);
    }

    public static <T> Result<T> error()
    {
        return error(400, null);
    }
}
