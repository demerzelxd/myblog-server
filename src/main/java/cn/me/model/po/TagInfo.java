package cn.me.model.po;

import lombok.Data;

import java.util.List;

@Data
public class TagInfo
{
	private String tagName;

	List<TagItemInfo> tagItemInfoList;
}
