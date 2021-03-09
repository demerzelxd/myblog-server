package cn.me.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class BlogDTO implements Serializable
{
	/**
	 * 博客主键ID
	 */
	private Integer id;

	/**
	 * 用户ID
	 */
	private Integer userId;

	/**
	 * 博客标题
	 */
	@NotBlank(message = "博客标题不能为空")
	private String title;

	/**
	 * 博客描述
	 */
	private String description;

	/**
	 * 博客内容
	 */
	private String content;

	/**
	 * 标签名
	 */
	@Length(max = 64, message = "标签名长度应不超过64个字符")
	private String tagName;
}
