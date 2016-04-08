package com.ge.predix.alm.cloud.cloudfoundry;

import com.ge.predix.alm.cloud.AssetServiceInfo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;

import java.util.Map;

public class AssetServiceInfoCreator extends CloudFoundryServiceInfoCreator<AssetServiceInfo> {
	
	@Value("${vcap.zoneHeaderName}")
	private String zoneHeaderName;
	
	@Value("${vcap.zoneIdName}")
	private String zoneIdName;
	
	@Value("${vcap.zoneScopeName}")
	private String zoneScopeName;
	
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
      
        //insert the predix asset variables
        Map<String, String> zoneInfo = (Map<String, String>)credentials.get("zone");
        
        
    	info.setZoneHeaderName(zoneInfo.get(zoneHeaderName)); 
    	info.setZoneId(zoneInfo.get(zoneIdName));
    	info.setZoneScope(zoneInfo.get(zoneScopeName)); 
    	
        return info;
	}
}
