/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.page.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientindextracing.omodmodels.PatientContactsModel;
import org.openmrs.module.patientindextracing.service.CommunityTesters;
import org.openmrs.module.patientindextracing.service.PatientContacts;
import org.openmrs.module.patientindextracing.utility.ConstantsUtil;
import org.openmrs.module.patientindextracing.utility.Utils;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author MORRISON.I
 */
public class ContactPageController {
	
	public String post(HttpServletRequest request, @RequestParam("index_patient_id") String index_patient_id,
	        @RequestParam("relationship") String relationship, @RequestParam("age") String age,
	        @RequestParam("sex") String sex, @RequestParam("preferred_testing_location") String preferred_testing_location,
	        @RequestParam("state") String state, @RequestParam("lga") String lga, @RequestParam("town") String town,
	        @RequestParam("village") String village, @RequestParam("physically_abused") String physically_abused,
	        @RequestParam("forced_sexually") String forced_sexually,
	        @RequestParam("fear_their_partner") String fear_their_partner,
	        @RequestParam("notification_method") String notification_method,
	        @RequestParam(value = "more_information", required = false) String more_information,
	        @RequestParam(value = "assign_contact_to_cec", required = false) String assign_contact_to_cec,
	        @RequestParam(value = "community_tester_guid", required = false) String community_tester_guid,
	        @RequestParam(value = "community_tester_name", required = false) String community_tester_name,
	        @RequestParam("firstname") String firstname, @RequestParam("lastname") String lastname, UiUtils ui,
	        PageModel pageModel, @RequestParam("phone_number") String phone_number)
	//@RequestParam("user") User user
	{
		PatientContacts pcService = new PatientContacts();
		
		String responseMsg = "";
		String redirectMsg = "";
		try {
			
			PatientContactsModel newcontact = new PatientContactsModel();
			newcontact.setAge(Integer.parseInt(age));
			newcontact.setAssign_contact_to_cec(resolveYesNo(assign_contact_to_cec));
			
			if (assign_contact_to_cec.equals("Yes")) {
				newcontact.setCommunity_tester_name(community_tester_name);
				newcontact.setCommunity_tester_guid(community_tester_guid);
			}
			
			newcontact.setCountry("Nigeria");
			newcontact.setDate_created(new Date());
			newcontact.setFear_their_partner(fear_their_partner);
			newcontact.setForced_sexually(forced_sexually);
			
			//get patient id from uuid
			Integer patientId = Context.getPatientService().getPatientByUuid(index_patient_id).getId();
			
			newcontact.setIndex_patient_id(patientId);
			newcontact.setLga(lga);
			newcontact.setMore_information(more_information);
			newcontact.setNotification_method(notification_method);
			newcontact.setPhysically_abused(physically_abused);
			newcontact.setPreferred_testing_location(preferred_testing_location);
			newcontact.setRelationship(relationship);
			newcontact.setSex(sex);
			newcontact.setState(state);
			newcontact.setTown(town);
			newcontact.setUuid(UUID.randomUUID().toString());
			newcontact.setVillage(village);
			newcontact.setTrace_status(ConstantsUtil.TRACE_PENDING_STATUS);
			newcontact.setDatim_code(Utils.getFacilityDATIMId());
			newcontact.setCreated_by(Context.getAuthenticatedUser().toString());
			newcontact.setPhone_number(phone_number);
			//newcontact.setCreated_by(user.getUsername());
			newcontact.setCode(index_patient_id);
			newcontact.setFirstname(firstname);
			newcontact.setLastname(lastname);
			
			int rr = pcService.createContacts(newcontact);
			if (rr == 1) {
				//patient created successfully				
				InfoErrorMessageUtil.flashInfoMessage(request.getSession(), "Contact created successfully");
				
			} else {
				//redirectMsg = "redirect:" + ui.pageLink("patientindextracing", "Contact");
				InfoErrorMessageUtil.flashErrorMessage(request.getSession(), "Error occurred, try again");
				
			}
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			//	redirectMsg = "redirect:" + ui.pageLink("patientindextracing", "Contact");
			InfoErrorMessageUtil.flashErrorMessage(request.getSession(), "Error occurred, try again");
			
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("patientId", index_patient_id);
		
		return "redirect:" + ui.pageLink("patientindextracing", "Contact", params);
	}
	
	public String getIndexClientContacts(@RequestParam("indexClientId") int indexClientId) {
		try {
			PatientContacts pcService = new PatientContacts();
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(pcService.getAllPatientContactsByIndexId(indexClientId));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	private int resolveYesNo(String strValue) {
		if (strValue.equals("Yes")) {
			return 1;
		}
		return 0;
	}
	
	public void get(PageModel pageModel) {
		try {
			CommunityTesters ctService = new CommunityTesters();
			
			//PatientContacts pcService = new PatientContacts();
			//pageModel.clear();
			pageModel.put("testers", ctService.getAllCommunityTesters());
			pageModel.put("successs", "Contact created successfully");
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
}
