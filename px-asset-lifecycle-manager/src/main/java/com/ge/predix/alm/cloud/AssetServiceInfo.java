package com.ge.predix.alm.cloud;

import org.springframework.cloud.service.UriBasedServiceInfo;

public class AssetServiceInfo extends UriBasedServiceInfo {
	private String instanceId;
	private String zoneHeaderName;
	private String zoneId;
	private String zoneScope;
	
	public AssetServiceInfo(String id, String url) {
		super(id, url);
	}
	
	public void setInstanceId(String id) {
		this.instanceId = id;
	}
	
	public String getInstanceId() {
		return instanceId;
	}
	
	public void setZoneHeaderName(String name) {
		this.zoneHeaderName = name;
	}
	
	public String getZoneHeaderName() {
		return zoneHeaderName;
	}
	
	public void setZoneId(String id) {
		this.zoneId = id;
	}
	
	public String getZoneId() {
		return zoneId;
	}
	
	public void setZoneScope(String scope) {
		this.zoneScope = scope;
	}
	
	public String getZoneScope() {
		return zoneScope;
	}
}