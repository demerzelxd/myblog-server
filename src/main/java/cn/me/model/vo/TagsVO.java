package cn.me.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TagsVO
{
	/**
	 * 标签主键ID
	 */
	private Integer id;

	/**
	 * 标签名
	 */
	private String tagName;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private LocalDateTime createTime;
}
