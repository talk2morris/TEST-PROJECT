/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.omodmodels;

/**
 * @author MORRISON.I
 */
public class CheckContactResponse {
	
	private String uuid;
	
	private String facility_datim_code;
	
	private String trace_status;
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getFacility_datim_code() {
		return facility_datim_code;
	}
	
	public void setFacility_datim_code(String facility_datim_code) {
		this.facility_datim_code = facility_datim_code;
	}
	
	public String getTrace_status() {
		return trace_status;
	}
	
	public void setTrace_status(String trace_status) {
		this.trace_status = trace_status;
	}
	
}
