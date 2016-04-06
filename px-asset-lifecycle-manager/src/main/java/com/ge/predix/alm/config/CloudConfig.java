package com.ge.predix.alm.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
class CloudConfig extends AbstractCloudConfig {

	@Value("${redis.service}")
	private String redisservice;

	@Value("${redis.cacheExpirationtime}")
	private int cacheExpirationtime;

	@Value("${postgres.service}")
	private String postgresservice;

	public String getRedisservice() {
		return redisservice;
	}

	public void setRedisservice(String redisservice) {
		this.redisservice = redisservice;
	}

	public String getPostgresservice() {
		return postgresservice;
	}

	public void setPostgresservice(String postgresservice) {
		this.postgresservice = postgresservice;
	}

	@Bean
	public DataSource postgresDataSource() {
		return connectionFactory().dataSource(postgresservice);
	}

	// Connect to the 'redis-service' Redis service
	@Bean
	public RedisConnectionFactory redisFactory() {
		return connectionFactory().redisConnectionFactory(redisservice);
	}

	// (More beans to obtain service connectors)
}