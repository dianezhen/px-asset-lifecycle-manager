package com.ge.predix.alm.services;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import com.ge.predix.alm.dto.Domain;
import com.ge.predix.alm.repository.JpaDomainRepository;

import java.util.regex.Pattern;

@RestController
@RequestMapping(value="/domain")
public class SchemaController {
	
	private static final Logger log = Logger.getLogger(SchemaController.class);
	
	private static final Pattern NOT_ALPHA_NUMERIC_PATTERN = Pattern.compile("[^a-zA-Z0-9]");
	
	@Autowired
	private JpaDomainRepository jpaDomainRepository;
	//private BlobStoreRepository blobRepo;F
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public Iterable<Domain> domains() {
		return jpaDomainRepository.findAll();
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST,  headers = {"content-type=application/x-www-form-urlencoded"})
	public String add(@RequestParam("domainName") String domainName, 
			         @RequestParam("schema") String schema) 
	    throws Exception {
		
		//validate domain is alphanumeric
		if (NOT_ALPHA_NUMERIC_PATTERN.matcher(domainName).find()) 
			throw new Exception("Domain must be an alphanumeric with no spaces");
		
		//validate domain is unique
		if (jpaDomainRepository.exists(domainName)) {
			throw new Exception("New Domain must be unique"); //need to be specific here
		}
		
		//store record into postgres
		Domain domain = new Domain(domainName, schema);
		try {
			jpaDomainRepository.save(domain);
		}
		catch (Exception e) {
			log.error(e);
			throw e;
		}
		return domainName + " created.";
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT,  headers = {"content-type=application/x-www-form-urlencoded"})
	public String update(@RequestParam("domainName") String domainName, 
			          @RequestParam("schema") String schema) 
	    throws Exception {
		//validate domain exusts
		if (!jpaDomainRepository.exists(domainName)) {
			throw new Exception("Domain " + domainName + " does not exist"); //need to be specific here
		}
		
		//store record into postgres
		Domain domain = new Domain(domainName, schema);
		try {
			jpaDomainRepository.save(domain);
		}
		catch (Exception e) {
			log.error(e);
			throw e;
		}
		return domainName + " updated.";
	}

	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Domain getById(@PathVariable String id) {
		return jpaDomainRepository.findOne(id);
	}
}
