/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.jobs;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.openmrs.module.patientindextracing.service.CacheHelper;
import org.openmrs.module.patientindextracing.service.TokenStorage;
import org.openmrs.scheduler.tasks.AbstractTask;

/**
 * @author MORRISON.I
 */
public class TokenCacheUpdater extends AbstractTask {
	
	//CacheHelper cacheHelper = new CacheHelper();
	TokenStorage tokenRefresher = new TokenStorage();
	
	@Override
	public void execute() {
		try {
			tokenRefresher.refreshTokenCache();
		}
		catch (UnirestException ex) {
			System.err.println(ex.getMessage());
		}
		
	}
	
}
