package cn.me.exception;

import cn.me.model.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 全局异常处理，RestControllerAdvice捕获所有的Controller异常
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler
{
	/**
	 * 处理shiro异常
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(value = ShiroException.class)
	public Result handleException(ShiroException e)
	{
		log.error("shiro运行异常：--------------- {}", e.getMessage());
		// Spring框架的HttpStatus是枚举，Hutool的HttpStatus是int
		return Result.error(cn.hutool.http.HttpStatus.HTTP_UNAUTHORIZED, e.getMessage());
	}

	/**
	 * 处理Controller用@Validated修饰的传入参数格式异常
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public Result handleException(MethodArgumentNotValidException e)
	{
		log.error("传入参数格式异常：--------------- {}", e.getMessage());
		List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
		StringBuilder sb = new StringBuilder();
		allErrors.forEach(objectError -> sb.append(objectError.getDefaultMessage()).append("；"));
		// 去掉右边多余的分号；
		return Result.error(StringUtils.stripEnd(sb.toString(), "；"));
	}

	/**
	 * 处理Assert异常
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = IllegalArgumentException.class)
	public Result handleException(IllegalArgumentException e)
	{
		log.error("Assert异常：--------------- {}", e.getMessage());
		return Result.error(e.getMessage());
	}

	/**
	 * 处理所有运行时异常，范围大的异常在下面
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = RuntimeException.class)
	public Result handleException(RuntimeException e)
	{
		log.error("运行时异常：--------------- {}", e.getMessage());
		return Result.error(e.getMessage());
	}
}
