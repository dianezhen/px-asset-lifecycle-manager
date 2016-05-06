package com.ge.predix.alm.cloud;

import org.springframework.cloud.service.UriBasedServiceInfo;

public class UaaServiceInfo extends UriBasedServiceInfo {

	private String clientID;
	private String clientSecret;
	private String grantType;

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public UaaServiceInfo(String id, String url) {
		super(id, url);
	}
}
