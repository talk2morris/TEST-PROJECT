/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientindextracing.omodmodels.PatientContactSummary;
import org.openmrs.module.patientindextracing.omodmodels.PatientContactsModel;
import org.openmrs.module.patientindextracing.omodmodels.YieldPerLocation;
import org.openmrs.module.patientindextracing.omodmodels.YieldPerNumAssignTraced;
import org.openmrs.module.patientindextracing.omodmodels.YieldPerSex;
import org.openmrs.module.patientindextracing.utility.ConstantsUtil;

/**
 * @author MORRISON.I
 */
public class ContactVisualization {

    private PatientContacts patientContacts;
    private List<PatientContactsModel> patientContactsModels = new ArrayList<>();

    public ContactVisualization(String patientUuid) {
        patientContacts = new PatientContacts();
        Integer patientId = Context.getPatientService().getPatientByUuid(patientUuid).getId();
        if (patientId != null) {
            patientContactsModels = patientContacts.getAllPatientContactsByIndexId(patientId);
        }

    }
    
    
    public ContactVisualization(){
         patientContacts = new PatientContacts();
         patientContactsModels = patientContacts.pullAllPatientContacts();
    }
    
    
    public PatientContactSummary doPatientSummaryCount(){
        int patientCount = Context.getPatientService().getAllPatients().size();
        
        int contactCount = patientContactsModels.size();
        int totalContactTraced = patientContactsModels.stream().filter(a -> a.getTrace_status().equals(ConstantsUtil.TRACE_COMPLETED_STATUS))
                .collect(Collectors.toList()).size();
        
        PatientContactSummary patientContactSummary = new PatientContactSummary();
        patientContactSummary.setTotalContactTraced(totalContactTraced);
        patientContactSummary.setTotalPatient(patientCount);
        patientContactSummary.setTotalPatientContacts(contactCount);
        
        return patientContactSummary;
        
    }
    
    

    public YieldPerLocation getYieldPerLocation() {

        Set<String> distinctLocations = new HashSet<>();
        List<Long> locationSum = new ArrayList<>();

        distinctLocations = patientContactsModels.stream()
                .map(PatientContactsModel::getLga)
                .distinct().collect(Collectors.toSet());

        for (String a : distinctLocations) {
            locationSum.add(getLocationCount(a));
        }

        YieldPerLocation yieldPerLocation = new YieldPerLocation();
        yieldPerLocation.setLocations(distinctLocations);
        yieldPerLocation.setYieldSum(locationSum);

        return yieldPerLocation;

    }

    public YieldPerSex getYieldPerSex() {

        Set<String> distinctSex = new HashSet<>();
        YieldPerSex yieldPerSex = new YieldPerSex();
        
      //  Set<YieldPerSex> yieldPerSexsSet = new HashSet<>();

        distinctSex = patientContactsModels.stream()
                .map(PatientContactsModel::getSex)
                .distinct().collect(Collectors.toSet());

        List<Object> maleList = new ArrayList<>();
        List<Object> femalList = new ArrayList<>();

        for (String a : distinctSex) {
            // YieldPerSex each = new YieldPerSex();
            // Map<String,Long> mapp = new HashMap<>();
            //  mapp.put(a, getSexCount(a));
            if (a.equals(ConstantsUtil.FEMALE_STRING)) {
                femalList.add(a);
                femalList.add(getSexCount(a));
            } else if (a.equals(ConstantsUtil.MALE_STRING)) {
                maleList.add(a);
                maleList.add(getSexCount(a));
            }

            // each.setSexYield(mapp);
            //  yieldPerSexsSet.add(each);
        }
        //set zero
        if(maleList.isEmpty()){
        maleList.add(ConstantsUtil.MALE_STRING);
        maleList.add(0);
        }else if(femalList.isEmpty()){
          femalList.add(ConstantsUtil.FEMALE_STRING);
        maleList.add(0);
        }
        
        yieldPerSex.setFemaleList(femalList);
        yieldPerSex.setMaleList(maleList);

        return yieldPerSex;

    }

    
    public YieldPerNumAssignTraced getYieldPerNumAssignTraced() {

        List<Long> assignYield = new ArrayList<>();
       List<Long> unAssignYield = new ArrayList<>();
       List<Long> tracedYield = new ArrayList<>();
       List<Long> unTracedYield = new ArrayList<>();

        Long maleAssigned = patientContactsModels.stream().filter(a
                -> a.getSex().equalsIgnoreCase(ConstantsUtil.MALE_STRING) && a.getAssign_contact_to_cec() == 1
        ).count();

        Long femaleAssigned = patientContactsModels.stream().filter(a
                -> a.getSex().equalsIgnoreCase(ConstantsUtil.FEMALE_STRING) && a.getAssign_contact_to_cec() == 1
        ).count();

        Long maleUnassigned = patientContactsModels.stream().filter(a
                -> a.getSex().equalsIgnoreCase(ConstantsUtil.MALE_STRING) && a.getAssign_contact_to_cec() == 0
        ).count();

        Long femaleUnassigned = patientContactsModels.stream().filter(a
                -> a.getSex().equalsIgnoreCase(ConstantsUtil.FEMALE_STRING) && a.getAssign_contact_to_cec() == 0
        ).count();

        Long maleTraced = patientContactsModels.stream().filter(a
                -> a.getSex().equalsIgnoreCase(ConstantsUtil.MALE_STRING) && a.getTrace_status().equals(ConstantsUtil.TRACE_COMPLETED_STATUS)
        ).count();

        Long femaleTraced = patientContactsModels.stream().filter(a
                -> a.getSex().equalsIgnoreCase(ConstantsUtil.FEMALE_STRING) && a.getTrace_status().equals(ConstantsUtil.TRACE_COMPLETED_STATUS)
        ).count();

        Long maleUnTraced = patientContactsModels.stream().filter(a
                -> a.getSex().equalsIgnoreCase(ConstantsUtil.MALE_STRING) && a.getTrace_status().equals(ConstantsUtil.TRACE_PENDING_STATUS)
        ).count();

        Long femaleUnTraced = patientContactsModels.stream().filter(a
                -> a.getSex().equalsIgnoreCase(ConstantsUtil.FEMALE_STRING) && a.getTrace_status().equals(ConstantsUtil.TRACE_PENDING_STATUS)
        ).count();

        assignYield.add(maleAssigned);
        assignYield.add(femaleAssigned);
        
        unAssignYield.add(maleUnassigned);
        unAssignYield.add(femaleUnassigned);
        
        tracedYield.add(maleTraced);
        tracedYield.add(femaleTraced);
        
        unTracedYield.add(maleUnTraced);
        unTracedYield.add(femaleUnTraced);

        YieldPerNumAssignTraced yieldPerNumAssignTraced = new YieldPerNumAssignTraced();
        yieldPerNumAssignTraced.setAssignYieldBySex(assignYield);
        yieldPerNumAssignTraced.setTracedYieldBySex(tracedYield);
        yieldPerNumAssignTraced.setUnassignYieldBySex(unAssignYield);
        yieldPerNumAssignTraced.setUntracedYieldBySex(unTracedYield);

        return yieldPerNumAssignTraced;

    }

    private Long getLocationCount(String location) {
        return patientContactsModels.stream().filter(a -> a.getLga().equals(location)).count();
    }

    private Long getSexCount(String sex) {
        return patientContactsModels.stream().filter(a -> a.getSex().equals(sex)).count();
    }

}
