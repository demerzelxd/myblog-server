package cn.me.config.redis;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.crazycake.shiro.exception.SerializationException;
import org.crazycake.shiro.serializer.RedisSerializer;
import org.springframework.util.Assert;

/**
 * 自定义Jackson2JsonRedisSerializer对redis中的值进行序列化
 * @param <T>
 */
public class CustomJackson2JsonRedisSerializer<T> implements RedisSerializer<T>
{
	private ObjectMapper objectMapper = new ObjectMapper();

	private final JavaType javaType;

	public CustomJackson2JsonRedisSerializer(Class<T> type)
	{
		this.javaType = getJavaType(type);
	}

	public CustomJackson2JsonRedisSerializer(JavaType javaType)
	{
		this.javaType = javaType;
	}

	@Override
	public byte[] serialize(T t) throws SerializationException
	{
		if (t == null)
		{
			return new byte[0];
		}
		try
		{
			return this.objectMapper.writeValueAsBytes(t);
		}
		catch (Exception ex)
		{
			throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(byte[] bytes) throws SerializationException
	{
		if (bytes == null || bytes.length == 0)
		{
			return null;
		}
		try
		{
			return (T) this.objectMapper.readValue(bytes, 0, bytes.length, javaType);
		}
		catch (Exception ex)
		{
			throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
		}
	}

	public void setObjectMapper(ObjectMapper objectMapper)
	{
		Assert.notNull(objectMapper, "'objectMapper' must not be null");
		this.objectMapper = objectMapper;
	}

	protected JavaType getJavaType(Class<?> clazz)
	{
		return TypeFactory.defaultInstance().constructType(clazz);
	}
}
