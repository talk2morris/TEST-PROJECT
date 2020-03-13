/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.service;

// import com.github.benmanes.caffeine.cache.Cache;
// import com.github.benmanes.caffeine.cache.Caffeine;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.openmrs.module.patientindextracing.omodmodels.TokenModel;
import org.openmrs.module.patientindextracing.omodmodels.UserModel;
import org.openmrs.module.patientindextracing.omodmodels.UserRequest;
import org.openmrs.module.patientindextracing.utility.ConstantsUtil;
import org.openmrs.module.patientindextracing.utility.GeneralMapper;
import org.openmrs.module.patientindextracing.utility.Utils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Configuration;

/**
 * @author MORRISON.I
 */

@Configuration
public class CacheHelper {
	
	//private Caffeine cacheManager;
	
	// private Cache<String, TokenModel> tokenCache;
	
	//	public CacheHelper() {
	//		cacheManager = Caffeine.newBuilder();
	//		tokenCache = cacheManager.expireAfterWrite(60, TimeUnit.MINUTES).build();
	//		
	//	}
	
	//	public void refreshTokenCache() throws UnirestException {
	//		System.out.println("About to refresh token cache");
	//		
	//		//Note: Endpoint sees phone number as passowrd and username as datim code
	//		Unirest.setObjectMapper(GeneralMapper.getCustomObjectMapper());
	//		SystemUser systemUser = new SystemUser();
	//		UserModel oldUser = systemUser.getSystemUser();
	//		
	//		UserRequest userRequest = new UserRequest();
	//		userRequest.setDatimCode(Utils.getFacilityDATIMId());
	//		userRequest.setIsnmrs("True");
	//		userRequest.setMacAddress(Utils.getMacAddress());
	//		userRequest.setUserName(Utils.getFacilityDATIMId());
	//		
	//		System.out.println("Using Datim code: " + userRequest.getDatimCode());
	//		
	//		if (oldUser != null) {
	//			String plainPassword = new String(Base64.getDecoder().decode(oldUser.getPassword()));
	//			userRequest.setPhoneNumber(plainPassword);
	//		} else {
	//			userRequest.setPhoneNumber(ConstantsUtil.DEFAULT_PASSWORD);
	//		}
	//		
	//		HttpResponse<TokenModel> response = Unirest.post(ConstantsUtil.BASE_URL_LOGIN + ConstantsUtil.GET_USER_TOKEN)
	//		        .header("Content-Type", "application/json").body(userRequest).asObject(TokenModel.class);
	//		
	//		System.out.println("Response status is: " + response.getStatus());
	//		System.out.println(response.getBody());
	//		if (response.getStatus() == 200) {
	//			System.out.println("Got token from Web");
	//			TokenModel result = response.getBody();
	//			result.setDate_created(new Date());
	//			System.out.println(result.getToken());
	//			tokenCache.put(ConstantsUtil.TOKEN_CACHE_KEY, result);
	//		}
	//		
	//		System.out.println("Finished refreshing token!");
	//	}
	//	
	//	public String getToken() {
	//		TokenModel model = tokenCache.getIfPresent(ConstantsUtil.TOKEN_CACHE_KEY);
	//		if (model != null) {
	//			return model.getToken();
	//		}
	//		
	//		return "";
	//		
	//	}
	//	
}
