package com.ge.predix.alm.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AssetDataManagerServiceImpl implements AssetDataManagerService {

	private static final Logger log = Logger
			.getLogger(AssetDataManagerServiceImpl.class);
	@Value("${asset.ZoneId}")
	private String assetZoneId;

	@Value("${asset.Url}")
	private String assetUrl;

	private RestTemplate almRestTemplate;

	@Override
	public boolean createAssets(String domain, String jsonAsset) {
		log.info("Inside creating new Asset");
		RestTemplate template = new RestTemplate();
		// set headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Predix-Zone-Id", assetZoneId);
		HttpEntity<String> entity = new HttpEntity<String>(
				jsonAsset.toString(), headers);

		// Get the response as string
		ResponseEntity<String> response = template.exchange(assetUrl + "/"
				+ domain, HttpMethod.POST, entity, String.class);
		if (response.getStatusCode() != HttpStatus.OK
				|| response.getStatusCode() != HttpStatus.NO_CONTENT) {
			log.error("Error storing Asset Data in Asset Service. "
					+ response.getStatusCode() + " - " + response.getBody());
			return false;
		} else {
			log.info("Asset data stored successfully in Asset Service");
			return true;
		}
	}

	@Override
	public boolean modifyAsset(String domain, String assetID, String jsonAsset) {
		log.info("Inside Modify an Asset");
		RestTemplate template = new RestTemplate();
		// set headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Predix-Zone-Id", assetZoneId);
		HttpEntity<String> entity = new HttpEntity<String>(
				jsonAsset.toString(), headers);

		// Get the response as string
		ResponseEntity<String> response = template.exchange(assetUrl + "/"
				+ domain + "/" + assetID, HttpMethod.PUT, entity, String.class);
		if (response.getStatusCode() != HttpStatus.OK
				|| response.getStatusCode() != HttpStatus.NO_CONTENT) {
			log.error("Error Updating Asset Data in Asset Service. "
					+ response.getStatusCode() + " - " + response.getBody());
			return false;
		} else {
			log.info("Asset data updated successfully in Asset Service");
			return true;
		}
	}

	@Override
	public boolean deleteAsset(String domain, String assetID) {
		log.info("Inside Delete an Asset");
		RestTemplate template = new RestTemplate();
		// set headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Predix-Zone-Id", assetZoneId);
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		// Get the response as string
		ResponseEntity<String> response = template.exchange(assetUrl + "/"
				+ domain + "/" + assetID, HttpMethod.DELETE, entity,
				String.class);
		if (response.getStatusCode() != HttpStatus.OK
				|| response.getStatusCode() != HttpStatus.NO_CONTENT) {
			log.error("Error Updating Asset Data in Asset Service. "
					+ response.getStatusCode() + " - " + response.getBody());
			return false;
		} else {
			log.info("Asset data updated successfully in Asset Service");
			return true;
		}
	}

	@Override
	public String viewAsset(String domain, String searchpath) {
		log.info("Inside View Asset Service call with Domain =" + domain
				+ " searchpath = " + searchpath);
		Object req = new Object();
		String resp = null;
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		headers.set("Predix-Zone-Id", assetZoneId);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
				assetUrl + "/" + domain).queryParam("requestData", req);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		almRestTemplate = new RestTemplate();
		ResponseEntity<String> response = almRestTemplate
				.exchange(builder.build().encode().toUri(), HttpMethod.GET,
						entity, String.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			log.info("Service Response: " + response.getBody());
			resp = response.getBody();
		}
		return resp;
	}
}
