package com.ge.predix.alm.cloud.cloudfoundry;

import com.ge.predix.alm.cloud.BlobstoreServiceInfo;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;

import java.util.Map;

public class BlobstoreServiceInfoCreator extends CloudFoundryServiceInfoCreator<BlobstoreServiceInfo> {
	
	public BlobstoreServiceInfoCreator() {
		super(new Tags("blobstore", "blob-store"));
	}

	@Override
	public BlobstoreServiceInfo createServiceInfo(Map<String, Object> serviceData) {
        String id = (String) serviceData.get("name");

        Map<String, Object> credentials = getCredentials(serviceData);
        String uri = getUriFromCredentials(credentials);
        
        String accessKey = getStringFromCredentials(credentials, "access_key_id");
        String accessSecret = getStringFromCredentials(credentials, "secret_access_key");
        
        BlobstoreServiceInfo info = new BlobstoreServiceInfo(id, uri);
        info.setAccessKey(accessKey);
        info.setAccessSecret(accessSecret);

        return new BlobstoreServiceInfo(id, uri);
	}
}
