package cn.me.model.po;

import lombok.Data;

@Data
public class ArchiveItemInfo
{
	/**
	 * 博客主键ID
	 */
	private Integer id;

	/**
	 * 博客标题
	 */
	private String title;

	/**
	 * 月日
	 */
	private String md;
}
