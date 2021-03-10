package cn.me.model.po;

import lombok.Data;

import java.util.List;

@Data
public class ArchiveInfo
{
	/**
	 * 年
	 */
	private String year;

	/**
	 * 归档单位
	 */
	private List<ArchiveItemInfo> archiveItemInfoList;
}
