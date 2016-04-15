package com.ge.predix.alm.services;

public interface AssetDataManagerService {

	boolean createAssets(String domain,String jsonAsset);

	boolean modifyAsset(String domain, String assetID, String jsonAsset);

	boolean deleteAsset(String domain, String assetID);

	String viewAsset(String domain, String serachpath);
}
