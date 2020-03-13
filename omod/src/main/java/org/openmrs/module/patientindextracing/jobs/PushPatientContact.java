/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.jobs;

import org.openmrs.module.patientindextracing.service.PatientContacts;
import org.openmrs.scheduler.tasks.AbstractTask;

/**
 * @author MORRISON.I
 */
public class PushPatientContact extends AbstractTask {
	
	PatientContacts pcService = new PatientContacts();
	
	@Override
	public void execute() {
		//  List<PatientContactsModel> allContacts = new ArrayList<>();
		// allContacts = pcService.pullAllPatientContacts();
		
		pcService.pushContactsToWeb();
		
	}
}
