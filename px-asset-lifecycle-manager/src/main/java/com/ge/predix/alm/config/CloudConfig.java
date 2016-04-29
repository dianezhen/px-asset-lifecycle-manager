package com.ge.predix.alm.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.ge.predix.alm.cloud.AssetServiceInfo;
import com.ge.predix.alm.cloud.UaaServiceInfo;

import org.springframework.core.env.Environment;

@Configuration
class CloudConfig extends AbstractCloudConfig {
	
	private static final Logger log = Logger.getLogger(CloudConfig.class);

	@Autowired
	private Environment env;
	
	private String redisservice;

	private String postgresservice;
	

	private String uaaservice;
	
	void setEnvironment(Environment env) {
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
	

	@Bean(name="assetServiceInfo")
	public AssetServiceInfo getAssetSerivceInfo() {
		String assetService = env.getProperty("asset_service");
		return (AssetServiceInfo)cloud().getServiceInfo(assetService);
	}
	
	@Bean(name="uaaServiceInfo")
	public UaaServiceInfo getUaaSerivceInfo() {
		String uaaservice = env.getProperty("uaa_service");
		UaaServiceInfo userSvcInfo = (UaaServiceInfo)cloud().getServiceInfo(uaaservice);
		userSvcInfo.setClientID(env.getProperty("uaa_clientid"));
		userSvcInfo.setClientSecret(env.getProperty("uaa_clientsecret"));
		userSvcInfo.setGrantType(env.getProperty("uaa_granttype"));
		return userSvcInfo;
	}
	
	@Bean
	public Properties applicationProp() {
		Properties resp = this.cloud().getCloudProperties();

		for (Object akey : resp.keySet()) {
			log.info("Key = " + akey.toString() + " & Value = " + resp.get(akey).toString());
		}
		return resp;
	}

} 