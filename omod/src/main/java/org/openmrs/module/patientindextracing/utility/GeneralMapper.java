/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.modelmapper.ModelMapper;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.PersonName;
import org.openmrs.module.patientindextracing.omodmodels.Client;
import org.openmrs.module.patientindextracing.omodmodels.CommunityTestersPayload;
import org.openmrs.module.patientindextracing.omodmodels.ContactRequestModel;
import org.openmrs.module.patientindextracing.omodmodels.PatientContactsModel;
import org.openmrs.module.patientindextracing.omodmodels.TesterModel;

/**
 * @author MORRISON.I
 */
public class GeneralMapper {
	
	public static ModelMapper mapper = new ModelMapper();
	
	public static ContactRequestModel mapToContactRequestModel(PatientContactsModel patientContactsModel) {
		return mapper.map(patientContactsModel, ContactRequestModel.class);
	}
	
	public static ContactRequestModel mapPatientToContactRequest(Patient p, ContactRequestModel contactRequestModel) {
		
		contactRequestModel.setIndex_age(String.valueOf(p.getAge()));
		try {
			contactRequestModel.setIndex_client_indentifier(p.getPatientIdentifier(ConstantsUtil.OPENMRS_IDENTIFIER_INDEX)
			        .getIdentifier());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		contactRequestModel.setIndex_firstname(p.getGivenName());
		contactRequestModel.setIndex_resident_address(p.getPersonAddress().getAddress1());
		contactRequestModel.setIndex_sex(p.getPerson().getGender());
		contactRequestModel.setIndex_surname(p.getFamilyName());
		
		return contactRequestModel;
	}
	
	public static TesterModel mapPayloadToCommunityTesters(CommunityTestersPayload payload) {
		
		TesterModel testers = new TesterModel();
		testers.setAssign_facilityId(payload.AssignedFacilityId);
		testers.setEmail(payload.getEmail());
		testers.setFacility_code(payload.getFacilityCode());
		testers.setFacility_name(payload.getFacilityName());
		testers.setLga(payload.getLga());
		testers.setLga_code(payload.getLgaCode());
		//testers.setPhone_number(payload.getPhoneNumber());
		testers.setState(payload.getState());
		testers.setUsername(payload.getUsername());
		testers.setCommunity_tester_guid(payload.getCommunity_tester_guid());
		
		return testers;
		
	}
	
	public static List<TesterModel> mapPayloadToCommunityTestersList(List<CommunityTestersPayload> payloads) {
        List<TesterModel> response = new ArrayList<>();
        payloads.stream().forEach(a -> {
            response.add(mapPayloadToCommunityTesters(a));
        });
        return response;
    }
	
	public static ObjectMapper getCustomObjectMapper() {
		return new ObjectMapper() {
			
			org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
			
			public String writeValue(Object value) {
				try {
					return mapper.writeValueAsString(value);
				}
				catch (Exception ex) {
					Logger.getLogger(GeneralMapper.class.getName()).log(Level.SEVERE, null, ex);
				}
				return null;
			}
			
			public <T> T readValue(String value, Class<T> valueType) {
				try {
					return mapper.readValue(value, valueType);
				}
				catch (IOException ex) {
					Logger.getLogger(GeneralMapper.class.getName()).log(Level.SEVERE, null, ex);
				}
				return null;
			}
		};
	}
	
	public static Person ClientToPerson(Client client) {
        Person p = new Person();
        PersonAddress pd = new PersonAddress();
        pd.setCityVillage(client.getClientVillage());
        //client has no country
        pd.setCountry("Nigeria");
        pd.setCountyDistrict(client.getClientLga());
        pd.setStateProvince(client.getClientState());
        pd.setPreferred(Boolean.TRUE);
        pd.setUuid(UUID.randomUUID().toString());
        //  pd.setPerson(p);

        Set<PersonAddress> addr = new TreeSet<>();
        addr.add(pd);

        Set<PersonName> pNameSet = new TreeSet<>();
        PersonName pName = new PersonName();
        pName.setGivenName(client.getFirstName());
        pName.setFamilyName(client.getSurname());
        pName.setUuid(UUID.randomUUID().toString());
        pNameSet.add(pName);

        p.setNames(pNameSet);
        p.setAddresses(addr);
        p.setBirthdateEstimated(true);
        p.setBirthdateFromAge(Integer.parseInt(client.getAge()), new Date());
        p.setGender(client.getSex());
        p.setUuid(UUID.randomUUID().toString());

        //TODO
        return p;

    }
}
