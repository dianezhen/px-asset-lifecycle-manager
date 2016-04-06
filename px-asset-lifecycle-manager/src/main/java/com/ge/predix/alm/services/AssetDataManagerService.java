package com.ge.predix.alm.services;

import java.util.List;

public interface AssetDataManagerService {
	
	void createAssets();
	void modifyAsset();
	void deleteAsset();
	List<Object> viewAsset();
}
