package com.ge.predix.alm.services;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ge.predix.alm.dto.Domain;
//import com.ge.predix.alm.dto.Schema;
//import com.ge.predix.alm.repository.BlobStoreRepository;
import com.ge.predix.alm.repository.JpaDomainRepository;

import java.util.regex.Pattern;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;

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
	
	/*
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public String add(@RequestParam("domain") String domainName, 
					  @RequestParam("schema") String schema) throws Exception {
		//validate domain is alphanumeric
		System.out.println("SAVING NEW DOMAIN : " + domainName + " SCHEMA " + schema);
		if (NOT_ALPHA_NUMERIC_PATTERN.matcher(domainName).find()) 
			throw new Exception("Domain must be an alphanumeric with no spaces");
		
		//validate domain is unique
		try {
			if (jpaDomainRepository.exists(domainName)) {
				throw new Exception("New Domain must be unique"); //need to be specific here
			}
		}
		catch (Exception e) {
			log.error(e);
			throw e;
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
		return domainName + "created.";
	}
	*/
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST,  headers = {"content-type=application/x-www-form-urlencoded"})
	public String add(@RequestParam("domainName") String domainName, 
			         @RequestParam("schema") String schema) 
	    throws Exception {
		
		//validate domain is alphanumeric
		System.out.println("SAVING NEW DOMAIN : " + domainName + " SCHEMA " + schema);
		if (NOT_ALPHA_NUMERIC_PATTERN.matcher(domainName).find()) 
			throw new Exception("Domain must be an alphanumeric with no spaces");
		
		//validate domain is unique
		try {
			if (jpaDomainRepository.exists(domainName)) {
				throw new Exception("New Domain must be unique"); //need to be specific here
			}
		}
		catch (Exception e) {
			System.out.println("TABLE DOESN'T EXIST YET");
			//log.error(e);
			//e.printStackTrace();
			//throw e;
		}
		
		//store record into postgres
		Domain domain = new Domain(domainName, schema);
		try {
			jpaDomainRepository.save(domain);
		}
		catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return domainName + "created.";
	}
	

	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Domain getById(@PathVariable String id) {
		return jpaDomainRepository.findOne(id);
	}

	/*
	@ResponseBody
	@RequestMapping(value = "/{id}/schema", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> getSchemaById(@PathVariable String id)
		throws IOException
	{
		Domain domain = domainRepo.findOne(id);
		String fileUri = domain.getSchemaUri();
		Schema schema = blobRepo.getSchema(fileUri);
		return ResponseEntity.ok().contentLength(schema.getContentLength()).
				contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(new InputStreamResource(schema.getInputStream()));
				
	}
	*/
}
