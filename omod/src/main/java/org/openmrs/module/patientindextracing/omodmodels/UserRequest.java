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
public class UserRequest {
	
	private String userName;
	
	private String phoneNumber;
	
	private String macAddress;
	
	private String datimCode;
	
	private String Isnmrs;
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getMacAddress() {
		return macAddress;
	}
	
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
	public String getDatimCode() {
		return datimCode;
	}
	
	public void setDatimCode(String datimCode) {
		this.datimCode = datimCode;
	}
	
	public String getIsnmrs() {
		return Isnmrs;
	}
	
	public void setIsnmrs(String Isnmrs) {
		this.Isnmrs = Isnmrs;
	}
	
}
