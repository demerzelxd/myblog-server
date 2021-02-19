package cn.me.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
// 在这边写MapperScan就不需要在启动类写了
@MapperScan("cn.me.mapper")
public class MybatisPlusConfig
{
	//向核心容器注入分页拦截器
	@Bean
	public PaginationInterceptor paginationInterceptor()
	{
		return new PaginationInterceptor();
	}
}

