package com.ge.predix.alm.dto;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name= "DOMAIN")
public class Domain {
	@Id
	@Column(name="domain_name", unique=true)
	private String domainName;
	
	@Column(name="schema_uri")
	private String schemaUri;
	
	public Domain() {}
	
	public Domain(String name, String schemaUri) {
		System.out.println("SAVING DOMAIN ******   DOMAIN NAME = " + name);
		this.domainName = name;
		this.schemaUri = schemaUri;
	}
	
	public String getDomainName() {
		return domainName;
	}
	
	public void setDomainName(String name) {
		this.domainName = name;
	}

	public String getSchemaUri() {
		return schemaUri;
	}
	
	public void setSchemaUri(String schemaUri) {
		this.schemaUri = schemaUri;
	}
}
