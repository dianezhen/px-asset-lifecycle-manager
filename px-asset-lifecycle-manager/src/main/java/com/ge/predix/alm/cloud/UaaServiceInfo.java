package com.ge.predix.alm.cloud;

import org.springframework.cloud.service.UriBasedServiceInfo;

public class UaaServiceInfo extends UriBasedServiceInfo {

	public UaaServiceInfo(String id, String url) {
		super(id, url);
	}
}
