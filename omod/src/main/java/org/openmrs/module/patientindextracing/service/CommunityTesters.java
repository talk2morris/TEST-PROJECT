/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientindextracing.dbmanager.NdrDBManager;
import org.openmrs.module.patientindextracing.omodmodels.CommunityTestersPayload;
import org.openmrs.module.patientindextracing.omodmodels.PatientContactsModel;
import org.openmrs.module.patientindextracing.omodmodels.TesterModel;
import org.openmrs.module.patientindextracing.utility.ConstantsUtil;
import org.openmrs.module.patientindextracing.utility.GeneralMapper;
import org.openmrs.module.patientindextracing.utility.Utils;

/**
 * @author MORRISON.I
 */
public class CommunityTesters {
	
	NdrDBManager dbManageer = null;
	
	//private CacheHelper cacheHelper;
	private TokenStorage tokenRefresher;
	
	public CommunityTesters() {
		//cacheHelper = new CacheHelper();
		tokenRefresher = new TokenStorage();
	}
	
	public void createTesters(TesterModel cTesterModel) {
		
		if (cTesterModel != null) {
			try {
				dbManageer = new NdrDBManager();
				dbManageer.openConnection();
				cTesterModel.setCreated_by("Admin");
				dbManageer.createCommunityTester(cTesterModel);
				dbManageer.closeConnection();
			}
			catch (SQLException ex) {
				
			}
		}
	}
	
	public void createBulkTesters(List<TesterModel> cTesterModels, boolean deleteExisting) throws SQLException {
		
		dbManageer = new NdrDBManager();
		dbManageer.openConnection();
		if (deleteExisting) {
			dbManageer.deleteAllCommunityTesters();
		}
		for (TesterModel a : cTesterModels) {
			a.setCreated_by("Admin");
			dbManageer.createCommunityTester(a);
		}
		
		dbManageer.closeConnection();
		
	}
	
	public void createOrUpdateBulkTesters(List<TesterModel> cTesterModels, boolean deleteExisting) throws SQLException {

        dbManageer = new NdrDBManager();
        dbManageer.openConnection();
        if (deleteExisting) {
            dbManageer.deleteAllCommunityTesters();
        }

        for (TesterModel a : cTesterModels) {
            a.setCreated_by("Admin");
            dbManageer.createCommunityTester(a);
            //dbManageer.updateCommunityTester(a);
        }

        List<String> testersList = dbManageer.getAssignedDeletedTesters();
        testersList.stream().forEach(a -> {
            try {
                dbManageer.removeContactAssignment(a);
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        });

        dbManageer.closeConnection();
    }
	
	public List<TesterModel> getAllCommunityTesters() {
        List<TesterModel> response = new ArrayList<>();
        try {
            dbManageer = new NdrDBManager();
            dbManageer.openConnection();
            response = dbManageer.getCommunityTesters();
            dbManageer.closeConnection();
            return response;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //return empty list
        return response;
    }
	
	public List<PatientContactsModel> getAssignedContacts(int testerId) {
		return null;
	}
	
	private List<CommunityTestersPayload> loadTestersFromWeb(String facilityId) throws UnirestException {
        //move this to proper location
        Unirest.setObjectMapper(GeneralMapper.getCustomObjectMapper());
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("Authorization", "Bearer " + tokenRefresher.getTokenCache());

        HttpResponse<CommunityTestersPayload[]> payloads = Unirest
                .get(ConstantsUtil.BASE_URL + ConstantsUtil.GET_COMMUNITY_TESTER)
                .headers(headers)
                .queryString("datimCode", facilityId).asObject(CommunityTestersPayload[].class);

        if (payloads != null && (payloads.getStatus() == 200 || payloads.getStatus() == 201)) {
            return Arrays.asList(payloads.getBody());
        }

        return Collections.emptyList();
    }
	
	public void refreshCommunityTesterList() throws UnirestException, SQLException {
		
		//String duummyFacility = "mgic";
		String currentFacilityId = Utils.getFacilityDATIMId();
		
		List<CommunityTestersPayload> payload = loadTestersFromWeb(currentFacilityId);
		if (!payload.isEmpty()) {
			
			//delete existing and insert.
			//createBulkTesters(GeneralMapper.mapPayloadToCommunityTestersList(payload), true);
			createOrUpdateBulkTesters(GeneralMapper.mapPayloadToCommunityTestersList(payload), true);
		}
		
	}
	
}
