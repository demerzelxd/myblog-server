package cn.me.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户注册接收前端数据
 */
@Data
public class UserLogonDTO implements Serializable
{
	/**
	 * 用户名
	 */
	@NotBlank(message = "用户名不能为空")
	@Length(min = 4, max = 64, message = "用户名长度应在4到64个字符")
	private String username;

	/**
	 * 密码
	 */
	@NotBlank(message = "密码不能为空")
	@Length(min = 4, max = 64, message = "密码长度应在4到64个字符")
	private String password;

	/**
	 * 邮箱
	 */
	@NotBlank(message = "邮箱不能为空")
	@Email(message = "邮箱格式不正确")
	private String email;
}
