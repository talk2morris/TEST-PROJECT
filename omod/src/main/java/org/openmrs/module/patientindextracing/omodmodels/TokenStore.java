/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.omodmodels;

import java.util.Date;

/**
 * @author MORRISON.I
 */
public class TokenStore {
	
	private String token_id;
	
	private String uuid;
	
	private String token;
	
	private Date date_created;
	
	private Date date_modified;
	
	public String getToken_id() {
		return token_id;
	}
	
	public void setToken_id(String token_id) {
		this.token_id = token_id;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public Date getDate_created() {
		return date_created;
	}
	
	public void setDate_created(Date date_created) {
		this.date_created = date_created;
	}
	
	public Date getDate_modified() {
		return date_modified;
	}
	
	public void setDate_modified(Date date_modified) {
		this.date_modified = date_modified;
	}
	
}
