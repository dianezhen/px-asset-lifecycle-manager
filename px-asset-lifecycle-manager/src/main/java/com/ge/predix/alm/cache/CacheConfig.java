package com.ge.predix.alm.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {
	
	@Value("${redis.Host}")
	private String redisHost;
	
	@Value("${redis.Port}")
	private int redisPort;

	@Value("${redis.Pass}")
	private String redisPass;
	
	@Value("${redis.cacheExpirationtime}")
	private int cacheExpirationtime;
	
	public String getRedisHost() {
		return redisHost;
	}

	public void setRedisHost(String redisHost) {
		this.redisHost = redisHost;
	}

	public int getRedisPort() {
		return redisPort;
	}

	public void setRedisPort(int redisPort) {
		this.redisPort = redisPort;
	}

	public String getRedisPass() {
		return redisPass;
	}

	public void setRedisPass(String redisPass) {
		this.redisPass = redisPass;
	}

	
	public int getCacheExpirationtime() {
		return cacheExpirationtime;
	}

	public void setCacheExpirationtime(int cacheExpirationtime) {
		this.cacheExpirationtime = cacheExpirationtime;
	}

	@Bean
	public JedisConnectionFactory redisConnectionFactory() {
		JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();

		// Defaults
		redisConnectionFactory.setHostName(redisHost);
		redisConnectionFactory.setPort(redisPort);
		redisConnectionFactory.setPassword(redisPass);
		return redisConnectionFactory;
	}

	@Bean
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory cf) {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
		redisTemplate.setConnectionFactory(cf);
		return redisTemplate;
	}

	@Bean
	public CacheManager cacheManager(RedisTemplate redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);

		// Number of seconds before expiration. Defaults to unlimited (0)
		cacheManager.setDefaultExpiration(cacheExpirationtime);
		return cacheManager;
	}

	@Bean
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object o, Method method, Object... objects) {
				// This will generate a unique key of the class name, the method
				// name,
				// and all method parameters appended.
				StringBuilder sb = new StringBuilder();
				sb.append(o.getClass().getName());
				sb.append(method.getName());
				for (Object obj : objects) {
					sb.append(obj.toString());
				}
				return sb.toString();
			}
		};
	}
}