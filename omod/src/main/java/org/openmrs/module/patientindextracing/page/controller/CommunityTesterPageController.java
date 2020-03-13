/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.page.controller;

import org.openmrs.module.patientindextracing.omodmodels.TesterModel;
import org.openmrs.module.patientindextracing.service.CommunityTesters;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author MORRISON.I
 */
public class CommunityTesterPageController {
	
	//create community testers
	public String post(@RequestParam("newtester") TesterModel newtester) {
		CommunityTesters ctService = new CommunityTesters();
		
		if (newtester != null) {
			ctService.createTesters(newtester);
			return "Community Tester created successfully!";
		} else {
			return "Invalid object in request";
		}
	}
	
	//get all community testers
	public void get(PageModel pageModel) {
		//ObjectMapper mapper = new ObjectMapper();
		CommunityTesters ctService = new CommunityTesters();
		try {
			pageModel.put("testers", ctService.getAllCommunityTesters());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	//pull community testers from the web.
	public String reloadTester() {
		CommunityTesters ctService = new CommunityTesters();
		try {
			ctService.refreshCommunityTesterList();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return "The data was refreshed successfully";
		
	}
	
}
