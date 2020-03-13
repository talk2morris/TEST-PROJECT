/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.omodmodels;

import java.util.Date;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author MORRISON.I
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenModel {
	
	@JsonProperty("Token")
	private String Token;
	
	private Date date_created;
	
	public String getToken() {
		return Token;
	}
	
	public void setToken(String token) {
		this.Token = token;
	}
	
	public Date getDate_created() {
		return date_created;
	}
	
	public void setDate_created(Date date_created) {
		this.date_created = date_created;
	}
	
}
