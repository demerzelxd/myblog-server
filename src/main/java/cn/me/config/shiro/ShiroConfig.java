package cn.me.config.shiro;

import cn.me.config.redis.CustomJackson2JsonRedisSerializer;
import cn.me.config.shiro.filter.JwtFilter;
import cn.me.config.shiro.realms.CustomRealm;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig
{
	@Autowired
	private JwtFilter jwtFilter;

	/**
	 * 自定义sessionManager，使用redisSessionDAO生成并保存session
	 *
	 * @return
	 */
	@Bean
	public SessionManager sessionManager(RedisSessionDAO redisSessionDAO)
	{
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		// 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
		CustomJackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new CustomJackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper om = new ObjectMapper();
		// 指定要序列化的域，field，get和set,以及修饰符范围，ANY是都有包括private和public
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		// 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String, Integer等会抛出异常
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		// 序列化值时使用此序列化方法
		redisSessionDAO.setValueSerializer(jackson2JsonRedisSerializer);
		// 注入redisSessionDAO
		sessionManager.setSessionDAO(redisSessionDAO);
		return sessionManager;
	}

	/**
	 * 自定义SecurityManager 使用自定义sessionManager和redis缓存
	 *
	 * @param realm
	 * @param sessionManager
	 * @return
	 */
	@Bean
	public SessionsSecurityManager securityManager(CustomRealm realm,
	                                               SessionManager sessionManager,
	                                               RedisCacheManager redisCacheManager)
	{
		// 创建安全管理器对象，初始化设置为自定义realm，realm主要用于认证授权时候数据的提供
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(realm);
		// 注入sessionManager
		securityManager.setSessionManager(sessionManager);
		// 注入redisCacheManager
		securityManager.setCacheManager(redisCacheManager);
		return securityManager;
	}

	/**
	 * 过滤器链定义，定义不同请求路径经过的过滤器
	 *
	 * @return
	 */
	@Bean
	public ShiroFilterChainDefinition shiroFilterChainDefinition()
	{
		DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
		Map<String, String> filterMap = new LinkedHashMap<>();
		// 定义经过所有路径都需要先认证
		// filterMap.put("/**", "authc");

		// 定义经过所有路径都需要先经过jwtFilter
		filterMap.put("/**", "jwt");
		chainDefinition.addPathDefinitions(filterMap);
		return chainDefinition;
	}

	/**
	 * 创建ShiroFilter负责拦截请求
	 *
	 * @param securityManager
	 * @param shiroFilterChainDefinition
	 * @return
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
	                                                     ShiroFilterChainDefinition shiroFilterChainDefinition)
	{
		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
		// 给filter设置安全管理器
		shiroFilter.setSecurityManager(securityManager);

		// 给filter设置jwtFilter
		Map<String, Filter> filters = new HashMap<>();
		filters.put("jwt", jwtFilter);
		shiroFilter.setFilters(filters);

		// 给filter设置配置好的过滤器链
		Map<String, String> filterMap = shiroFilterChainDefinition.getFilterChainMap();
		shiroFilter.setFilterChainDefinitionMap(filterMap);
		return shiroFilter;
	}
}
