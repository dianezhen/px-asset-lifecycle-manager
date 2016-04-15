package com.ge.predix.alm.services;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/api/adm/v1")
public class AssetDataManagerController extends BaseController {

	private static final Logger log = Logger
			.getLogger(AssetDataManagerController.class);

	@Autowired(required = true)
	private AssetDataManagerService assetDataManagerService;

	@RequestMapping(value = "/createAssets", method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(HttpStatus.OK)
	// 200
	public @ResponseBody boolean createAssets(HttpServletRequest request,
			@RequestParam(value = "domain", required = true) String domain,
			@RequestBody String jsonAsset) {
		boolean response = false;
		response = assetDataManagerService.createAssets(domain, jsonAsset);
		return response;
	}

	@RequestMapping(value = "/updateAsset", method = RequestMethod.PUT, consumes = "application/json")
	@ResponseStatus(HttpStatus.OK)
	// 200
	public @ResponseBody boolean updateAsset(HttpServletRequest request,
			@RequestParam(value = "domain", required = true) String domain,
			@RequestParam(value = "assetURI", required = true) String assetID,
			@RequestBody String jsonAsset) {
		boolean response = false;
		response = assetDataManagerService.modifyAsset(domain, assetID,
				jsonAsset);
		return response;
	}

	@RequestMapping(value = "/deleteAsset", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	// 200
	public @ResponseBody boolean deleteAsset(HttpServletRequest request,
			@RequestParam(value = "domain", required = true) String domain,
			@RequestParam(value = "assetURI", required = true) String assetID) {
		boolean response = false;
		response = assetDataManagerService.deleteAsset(domain, assetID);
		return response;
	}

	@RequestMapping(value = "/viewAssets", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	// 200
	//@Cacheable("viewAssets")
	public String viewAssets(
			HttpServletRequest request,
			@RequestParam(value = "domain", required = true) String domain,
			@RequestParam(value = "searchpath", required = false) String searchpath)
			throws ResourceNotFoundException {
		log.info("Inside View Asset..");
		String response = null;
		response = assetDataManagerService.viewAsset(domain, searchpath);
		if (response == null) {
			try {
				throw new ResourceNotFoundException("Asset Data not found.");
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
			}
		}
		return response;

	}
}