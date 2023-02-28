package top.fixyou.base.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 26018
 * @description
 * @date 2023/02/27
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult<T> {
    Integer code;
    String message;
    T data;

    public static <T> ResponseResult<T> ok(T data) {
        return new ResponseResult<>(200, "请求成功", data);
    }
    public static <T> ResponseResult<T> ok() {
        return new ResponseResult<>(200, "请求成功", null);
    }

    public static ResponseResult<Void> fail() {
        return new ResponseResult<>(500, null, null);
    }

    public static ResponseResult<Void> fail(String message) {
        return new ResponseResult<>(500, message, null);
    }
    public static <T> ResponseResult<T> fail(String message,T data) {
        return new ResponseResult<>(500, message, data);
    }

    public static <T> ResponseResult<T> fail(Integer code,String message,T data) {
        return new ResponseResult<>(code, message, data);
    }

}
