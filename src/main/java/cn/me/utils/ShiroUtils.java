package cn.me.utils;

import cn.me.config.shiro.profile.UserProfile;
import org.apache.shiro.SecurityUtils;

/**
 * shiro工具类
 */
public class ShiroUtils
{
	/**
	 * 获取shiro当前用户的principal
	 */
	public static UserProfile getProfile()
	{
		return (UserProfile) SecurityUtils.getSubject().getPrincipal();
	}
}
