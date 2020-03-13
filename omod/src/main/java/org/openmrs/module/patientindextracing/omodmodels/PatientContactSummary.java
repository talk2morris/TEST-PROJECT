/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.omodmodels;

/**
 * @author MORRISON.I
 */
public class PatientContactSummary {
	
	public int totalPatient;
	
	public int totalPatientContacts;
	
	public int totalContactTraced;
	
	public int getTotalPatient() {
		return totalPatient;
	}
	
	public void setTotalPatient(int totalPatient) {
		this.totalPatient = totalPatient;
	}
	
	public int getTotalPatientContacts() {
		return totalPatientContacts;
	}
	
	public void setTotalPatientContacts(int totalPatientContacts) {
		this.totalPatientContacts = totalPatientContacts;
	}
	
	public int getTotalContactTraced() {
		return totalContactTraced;
	}
	
	public void setTotalContactTraced(int totalContactTraced) {
		this.totalContactTraced = totalContactTraced;
	}
	
}
