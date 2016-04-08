package com.ge.predix.alm.dto;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@Table(name= "DOMAIN")
public class Domain {
	@Id
	@Column(name="domain_name", unique=true)
	private String domainName;
	
	@Column(name="schema", columnDefinition="TEXT NOT NULL")
	private String schema;
	
	public Domain() {}
	
	public Domain(String name, String schema) {
		System.out.println("SAVING DOMAIN ******   DOMAIN NAME = " + name);
		this.domainName = name;
		this.schema = schema;
	}
	
	public String getDomainName() {
		return domainName;
	}
	
	public void setDomainName(String name) {
		this.domainName = name;
	}

	public String getSchema() {
		return schema;
	}
	
	public void setSchema(String schema) {
		this.schema = schema;
	}
}
