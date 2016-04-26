package com.ge.predix.alm.cloud.cloudfoundry;

import java.util.Map;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;

import com.ge.predix.alm.cloud.UaaServiceInfo;

public class UaaServiceInfoCreator extends
		CloudFoundryServiceInfoCreator<UaaServiceInfo> {
	public UaaServiceInfoCreator() {
		super(new Tags("uaa"));
	}

	@Override
	public UaaServiceInfo createServiceInfo(Map<String, Object> serviceData) {
		String id = (String) serviceData.get("name");

		Map<String, Object> credentials = getCredentials(serviceData);
		String uri = getUriFromCredentials(credentials);

		UaaServiceInfo info = new UaaServiceInfo(id, uri);

		return info;
	}
}
