package cn.me.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 返回给前端的数据
 */
@Data
public class UserVO implements Serializable
{
	/**
	 * 主键ID
	 */
	private Integer id;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 头像URL
	 */
	private String avatar;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 状态：0-停用，1-正常
	 */
	private String status;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private LocalDateTime createTime;

	/**
	 * 上次登录时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private LocalDateTime lastLogin;
}
