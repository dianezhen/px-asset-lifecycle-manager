package com.ge.predix.alm.services;

import java.nio.charset.Charset;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ge.predix.alm.cloud.UaaServiceInfo;

@Component
public class UaaTokenManager  {
	private static final Logger log = Logger.getLogger(UaaTokenManager.class);

	@Value("${oauth.clientID}")
	private String clientID;

	@Value("${oauth.clientSecret}")
	private String clientSecret;

	@Value("${oauth.grantType}")
	private String grantType;

	@Autowired
	private UaaServiceInfo uaaServiceInfo;


	public String getUAAToken() {
		log.info("Inside Getting a new Token");

		String uaaToken = "";

		RestTemplate template = new RestTemplate();
		// set headers & req
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		String auth = clientID + ":" + clientSecret;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset
				.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		headers.set("Authorization", authHeader);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
				uaaServiceInfo.getUri() + "/oauth/token").queryParam(
				"grant_type", grantType);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		// Get the response as string
		ResponseEntity<String> response = template.exchange(builder.build()
				.encode().toUri(), HttpMethod.POST, entity, String.class);
		if (response.getStatusCode() != HttpStatus.OK
				&& response.getStatusCode() != HttpStatus.NO_CONTENT) {
			log.error("Error calling UAA Service. " + response.getStatusCode()
					+ " - " + response.getBody());
		} else {
			log.info("Got token from UAA." + response.getStatusCode() + " - "
					+ response.getBody());
			try {
				JSONParser jsonParser = new JSONParser();
				JSONObject fullObject = (JSONObject) jsonParser.parse(response
						.getBody());
				uaaToken = (String) fullObject.get("access_token");
			} catch (ParseException ex) {
				ex.printStackTrace();
				log.error("UAA Token is not JSON.");
			} catch (NullPointerException ex) {
				log.error("UAA Token is NULL.");
				ex.printStackTrace();
			}  
			log.info("Access Token = " + uaaToken);
		}
		log.info("Out of Getting a new Token");
		return uaaToken;
	}
}
