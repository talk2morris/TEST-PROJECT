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
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import org.openmrs.module.patientindextracing.dbmanager.NdrDBManager;
import org.openmrs.module.patientindextracing.omodmodels.TokenModel;
import org.openmrs.module.patientindextracing.omodmodels.TokenStore;
import org.openmrs.module.patientindextracing.omodmodels.UserModel;
import org.openmrs.module.patientindextracing.omodmodels.UserRequest;
import org.openmrs.module.patientindextracing.utility.ConstantsUtil;
import org.openmrs.module.patientindextracing.utility.GeneralMapper;
import org.openmrs.module.patientindextracing.utility.Utils;

/**
 * @author MORRISON.I
 */
public class TokenStorage {
	
	NdrDBManager dbManageer = null;
	
	public int createOrUpdateToken(TokenStore store) {
		int res = 0;
		if (store != null) {
			try {
				dbManageer = new NdrDBManager();
				dbManageer.openConnection();
				res = dbManageer.createTokenStore(store);
				dbManageer.closeConnection();
			}
			catch (SQLException ex) {
				System.err.println(ex.getMessage());
			}
		}
		
		return res;
	}
	
	public TokenStore getToken() {
		
		TokenStore store = null;
		try {
			dbManageer = new NdrDBManager();
			dbManageer.openConnection();
			store = dbManageer.getTokenStore();
			dbManageer.closeConnection();
		}
		catch (SQLException ex) {
			
		}
		
		return store;
		
	}
	
	public void refreshTokenCache() throws UnirestException {
		System.out.println("About to refresh token cache");
		
		//Note: Endpoint sees phone number as passowrd and username as datim code
		Unirest.setObjectMapper(GeneralMapper.getCustomObjectMapper());
		SystemUser systemUser = new SystemUser();
		UserModel oldUser = systemUser.getSystemUser();
		
		UserRequest userRequest = new UserRequest();
		userRequest.setDatimCode(Utils.getFacilityDATIMId());
		userRequest.setIsnmrs("True");
		userRequest.setMacAddress(Utils.getMacAddress());
		userRequest.setUserName(Utils.getFacilityDATIMId());
		
		System.out.println("Using Datim code: " + userRequest.getDatimCode());
		
		if (oldUser != null) {
			String plainPassword = new String(Base64.getDecoder().decode(oldUser.getPassword()));
			userRequest.setPhoneNumber(plainPassword);
		} else {
			userRequest.setPhoneNumber(ConstantsUtil.DEFAULT_PASSWORD);
		}
		
		HttpResponse<TokenModel> response = Unirest.post(ConstantsUtil.BASE_URL_LOGIN + ConstantsUtil.GET_USER_TOKEN)
		        .header("Content-Type", "application/json").body(userRequest).asObject(TokenModel.class);
		
		System.out.println("Response status is: " + response.getStatus());
		System.out.println(response.getBody());
		if (response.getStatus() == 200) {
			System.out.println("Got token from Web");
			TokenModel result = response.getBody();
			result.setDate_created(new Date());
			System.out.println(result.getToken());
			
			TokenStore store = new TokenStore();
			store.setDate_created(new Date());
			store.setDate_modified(new Date());
			store.setToken(result.getToken());
			store.setUuid(UUID.randomUUID().toString());
			
			createOrUpdateToken(store);
			
		}
		
		System.out.println("Finished refreshing token!");
	}
	
	public String getTokenCache() {
		TokenStore store = getToken();
		if (store != null) {
			
			return store.getToken();
		}
		
		return "";
	}
	
}
