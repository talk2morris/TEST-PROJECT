/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.jobs;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.openmrs.module.patientindextracing.omodmodels.CheckContactResponse;
import org.openmrs.module.patientindextracing.omodmodels.PatientContactsModel;
import org.openmrs.module.patientindextracing.service.PatientContacts;
import org.openmrs.module.patientindextracing.utility.ConstantsUtil;
import org.openmrs.module.patientindextracing.utility.Utils;
import org.openmrs.scheduler.tasks.AbstractTask;

/**
 * @author MORRISON.I
 */
public class CheckPatientTraceStatus extends AbstractTask {
	
	PatientContacts pcService = new PatientContacts();
	
	@Override
    public void execute() {
        System.out.println("Checking for pending contacts status!");
        String datimCode = Utils.getFacilityDATIMId();
        List<PatientContactsModel> allContacts = new ArrayList<>();
        allContacts = pcService.pullAllPatientContacts();
        
        allContacts.stream().filter(comp -> comp.getTrace_status().equalsIgnoreCase(ConstantsUtil.TRACE_PENDING_STATUS))
                .collect(Collectors.toList()).forEach(each -> {
       System.out.println("Gotten some list, processing the status");             
            try {
                CheckContactResponse contact = pcService.checkContactStatusOnline(datimCode,each.getUuid());
                if (contact != null && contact.getTrace_status().equals(ConstantsUtil.TRACE_COMPLETED_STATUS)) {
                    pcService.updateContactStatus(each.getUuid(), datimCode);
                    System.out.println("Updated status for contact with ID: "+contact.getUuid()); 
                }
                
            } catch (UnirestException ex) {                
                System.err.println(ex.getMessage());
            }
        });
    }
}
