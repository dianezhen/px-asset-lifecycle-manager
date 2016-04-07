package com.ge.predix.alm.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.ge.predix.alm.services.AssetDataManagerController;

import org.springframework.cloud.service.ServiceInfo;
import org.springframework.core.env.Environment;

@Configuration
class CloudConfig extends AbstractCloudConfig {
	
	private static final Logger log = Logger.getLogger(CloudConfig.class);

	@Autowired
	private Environment env;
	
	private String redisservice;

	@Value("${redis.cacheExpirationtime}")
	private int cacheExpirationtime;

	private String postgresservice;
	
	private String blobstoreService;
	
	void setEnvironment(Environment env) {
		System.out.println("*** SETTING ENVIRONMENT");
		setRedisservice(env.getProperty("redis_service"));
		setPostgresService(env.getProperty("postgres_service"));
		setBlobstoreService(env.getProperty("blobstore_service"));
	}

	public String getRedisservice() {
		return redisservice;
	}

	public void setRedisservice(String redisservice) {
		this.redisservice = redisservice;
	}

	public String getPostgresService() {
		return postgresservice;
	}

	public void setPostgresService(String postgresservice) {
		log.info("setting postgres service name " + postgresservice);
		System.out.println("*****  SETTING POSTGRES SERVICE NAME");
		this.postgresservice = postgresservice;
	}

	@Bean
	public DataSource postgresDataSource() {
		String postgresService = env.getProperty("postgres_service");
		System.out.println("*****  GETTING DATA SOURCE FOR " + postgresService);
		return connectionFactory().dataSource(postgresService);
	}

	// Connect to the 'redis-service' Redis service
	@Bean
	public RedisConnectionFactory redisFactory() {
		String redisService = env.getProperty("redis_service");
		return connectionFactory().redisConnectionFactory(redisService);
	}

	public String getBlobstoreService() {
		return blobstoreService;
	}

	public void setBlobstoreService(String blobstoreService) {
		this.blobstoreService = blobstoreService;
	}
	
	@Bean(name="blobstoreServiceInfo")
	public ServiceInfo getBlobstoreSerivceInfo() {
		String blobstoreService = env.getProperty("blobstore_service");
		return cloud().getServiceInfo(blobstoreService);
	}
	// (More beans to obtain service connectors)
}