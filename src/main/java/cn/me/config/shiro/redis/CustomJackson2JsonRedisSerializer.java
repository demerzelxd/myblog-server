package cn.me.config.shiro.redis;

import cn.me.utils.SerializationUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.crazycake.shiro.exception.SerializationException;
import org.crazycake.shiro.serializer.RedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 自定义Jackson2JsonRedisSerializer对redis中的值进行序列化
 * @param <T>
 */
public class CustomJackson2JsonRedisSerializer<T> implements RedisSerializer<T>
{
	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	private final JavaType javaType;

	private ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Creates a new {@link CustomJackson2JsonRedisSerializer} for the given target {@link Class}.
	 *
	 * @param type
	 */
	public CustomJackson2JsonRedisSerializer(Class<T> type) {
		this.javaType = getJavaType(type);
	}

	/**
	 * Creates a new {@link CustomJackson2JsonRedisSerializer} for the given target {@link JavaType}.
	 *
	 * @param javaType
	 */
	public CustomJackson2JsonRedisSerializer(JavaType javaType) {
		this.javaType = javaType;
	}

	@SuppressWarnings("unchecked")
	public T deserialize(@Nullable byte[] bytes) throws SerializationException
	{

		if (SerializationUtils.isEmpty(bytes)) {
			return null;
		}
		try {
			return (T) this.objectMapper.readValue(bytes, 0, bytes.length, javaType);
		} catch (Exception ex) {
			throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
		}
	}

	@Override
	public byte[] serialize(@Nullable Object t) throws SerializationException {

		if (t == null) {
			return SerializationUtils.EMPTY_ARRAY;
		}
		try {
			return this.objectMapper.writeValueAsBytes(t);
		} catch (Exception ex) {
			throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
		}
	}

	/**
	 * Sets the {@code ObjectMapper} for this view. If not set, a default {@link ObjectMapper#ObjectMapper() ObjectMapper}
	 * is used.
	 * <p>
	 * Setting a custom-configured {@code ObjectMapper} is one way to take further control of the JSON serialization
	 * process. For example, an extended {@link SerializerFactory} can be configured that provides custom serializers for
	 * specific types. The other option for refining the serialization process is to use Jackson's provided annotations on
	 * the types to be serialized, in which case a custom-configured ObjectMapper is unnecessary.
	 */
	public void setObjectMapper(ObjectMapper objectMapper) {

		Assert.notNull(objectMapper, "'objectMapper' must not be null");
		this.objectMapper = objectMapper;
	}

	/**
	 * Returns the Jackson {@link JavaType} for the specific class.
	 * <p>
	 * Default implementation returns {@link TypeFactory#constructType(java.lang.reflect.Type)}, but this can be
	 * overridden in subclasses, to allow for custom generic collection handling. For instance:
	 *
	 * <pre class="code">
	 * protected JavaType getJavaType(Class&lt;?&gt; clazz) {
	 * 	if (List.class.isAssignableFrom(clazz)) {
	 * 		return TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, MyBean.class);
	 * 	} else {
	 * 		return super.getJavaType(clazz);
	 * 	}
	 * }
	 * </pre>
	 *
	 * @param clazz the class to return the java type for
	 * @return the java type
	 */
	protected JavaType getJavaType(Class<?> clazz) {
		return TypeFactory.defaultInstance().constructType(clazz);
	}
}
