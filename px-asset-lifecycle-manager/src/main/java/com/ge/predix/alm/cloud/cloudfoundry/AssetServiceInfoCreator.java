package com.ge.predix.alm.cloud.cloudfoundry;

import com.ge.predix.alm.cloud.AssetServiceInfo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;

import java.util.Map;
import java.util.Set;

public class AssetServiceInfoCreator extends
		CloudFoundryServiceInfoCreator<AssetServiceInfo> {
	private static final Logger log = Logger
			.getLogger(AssetServiceInfoCreator.class);

	public AssetServiceInfoCreator() {
		super(new Tags("asset"));
	}

	@Override
	public AssetServiceInfo createServiceInfo(Map<String, Object> serviceData) {
		String id = (String) serviceData.get("name");

		Map<String, Object> credentials = getCredentials(serviceData);
		String uri = getUriFromCredentials(credentials);

		AssetServiceInfo info = new AssetServiceInfo(id, uri);

		info.setInstanceId(getStringFromCredentials(credentials, "instanceId"));
		log.info(" instance ID = " + info.getInstanceId());

		// insert the predix asset variables
		Map<String, Object> zoneInfo = (Map<String, Object>) credentials
				.get("zone");

		if (zoneInfo != null) {
			log.info("zoneInfo = " + zoneInfo.size());
			Set<String> keys = zoneInfo.keySet();
			for (String key : keys) {
				log.info("zoneInfo for key " + key + " = "
						+ (String) zoneInfo.get(key));
				switch(key) {
				case "http-header-name":
					info.setZoneHeaderName((String) zoneInfo.get(key));
				case "http-header-value":
					info.setZoneId((String) zoneInfo.get(key));
				case "oauth-scope":
					info.setZoneScope((String) zoneInfo.get(key));
				}
			}
		}
		return info;
	}
}
