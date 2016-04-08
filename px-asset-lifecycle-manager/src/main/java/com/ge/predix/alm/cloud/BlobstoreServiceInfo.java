package com.ge.predix.alm.cloud;

import org.springframework.cloud.service.UriBasedServiceInfo;

public class BlobstoreServiceInfo extends UriBasedServiceInfo {
	
	private String accessKey;
	private String accessSecret;
	
	public BlobstoreServiceInfo(String id, String url) {
		super(id, url);
	}
	
	public void setAccessKey(String key) {
		this.accessKey = accessKey;
	}
	
	public String getAccessKey() {
		return accessKey;
	}
	
	public void setAccessSecret(String secret) {
		this.accessSecret = secret;
	}
	
	public String getAccessSecret() {
		return accessSecret;
	}
	
}
