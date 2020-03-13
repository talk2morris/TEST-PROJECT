/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.service;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.RequestBodyEntity;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.openmrs.module.patientindextracing.dbmanager.NdrDBManager;
import org.openmrs.module.patientindextracing.omodmodels.ChangePasswordModel;
import org.openmrs.module.patientindextracing.omodmodels.UserModel;
import org.openmrs.module.patientindextracing.utility.ConstantsUtil;
import org.openmrs.module.patientindextracing.utility.GeneralMapper;

/**
 * @author MORRISON.I
 */
public class SystemUser {
	
	private NdrDBManager dbManageer;
	
	private String loggedUser;
	
	private TokenStorage tokenStorage = null;
	
	public SystemUser() {
		tokenStorage = new TokenStorage();
		// loggedUser = Utils.getLoggedInUser();
	}
	
	public int createSystemUser(UserModel model) {
		
		int response = 0;
		if (model != null) {
			try {
				dbManageer = new NdrDBManager();
				dbManageer.openConnection();
				response = dbManageer.createSystemUser(model);
				dbManageer.closeConnection();
				
			}
			catch (SQLException ex) {
				System.out.println(ex.getMessage());
				return 0;
			}
		}
		return response;
	}
	
	public UserModel getSystemUser() {
		UserModel mm = null;
		try {
			
			dbManageer = new NdrDBManager();
			dbManageer.openConnection();
			mm = dbManageer.getSystemUser();
			if (Objects.nonNull(mm.getPassword())) {
				mm.setPasswordExist(Boolean.TRUE);
			} else {
				mm.setPasswordExist(Boolean.FALSE);
			}
			dbManageer.closeConnection();
			
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		return mm;
	}
	
	public int editSystemUser(int id, UserModel model) {
		int response = 0;
		try {
			dbManageer = new NdrDBManager();
			dbManageer.openConnection();
			response = dbManageer.editSystemUser(id, model);
			dbManageer.closeConnection();
			
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
			return 0;
		}
		return response;
	}
	
	public boolean changeUserPasswordViaWeb(ChangePasswordModel model) throws UnirestException{
            
            Unirest.setObjectMapper(GeneralMapper.getCustomObjectMapper());
                
                    Map<String,String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer "+tokenStorage.getTokenCache());
               
               System.out.println("about to change password");
               
               RequestBodyEntity response = Unirest.post(ConstantsUtil.BASE_URL + ConstantsUtil.POST_CHANGE_PASSWORD)
				        .headers(headers)
                                        .body(model);
               System.out.println("Endpoint returns "+response.asString().getBody());
               
               return response.asString().getStatus() == 200 || response.asString().getStatus() == 201;
               
            
        }
}
