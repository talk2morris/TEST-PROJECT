/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.omodmodels;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author MORRISON.I
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangePasswordModel {
	
	private String datimCode;
	
	private String new_password;
	
	private String old_password;
	
	public String getOld_password() {
		return old_password;
	}
	
	public void setOld_password(String old_password) {
		this.old_password = old_password;
	}
	
	public String getDatimCode() {
		return datimCode;
	}
	
	public void setDatimCode(String datimCode) {
		this.datimCode = datimCode;
	}
	
	public String getNew_password() {
		return new_password;
	}
	
	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}
	
}
