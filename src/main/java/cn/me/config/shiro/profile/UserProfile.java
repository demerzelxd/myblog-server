package cn.me.config.shiro.profile;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装了用户可以公开的principal属性
 */
@Data
public class UserProfile implements Serializable
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
}
