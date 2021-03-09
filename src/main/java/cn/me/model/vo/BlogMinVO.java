package cn.me.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 不返回博客内容
 */
@Data
public class BlogMinVO
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
	private String title;

	/**
	 * 博客描述
	 */
	private String description;

	/**
	 * 状态：0-停用，1-正常
	 */
	private String status;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private LocalDateTime createTime;

	/**
	 * 标签名
	 */
	private String tagName;
}
