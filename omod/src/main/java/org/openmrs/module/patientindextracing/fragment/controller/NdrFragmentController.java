package org.openmrs.module.patientindextracing.fragment.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientindextracing.omodmodels.ChangePasswordModel;
import org.openmrs.module.patientindextracing.omodmodels.Client;
import org.openmrs.module.patientindextracing.omodmodels.PatientContactSummary;
import org.openmrs.module.patientindextracing.omodmodels.TesterModel;
import org.openmrs.module.patientindextracing.omodmodels.UserModel;
import org.openmrs.module.patientindextracing.omodmodels.YieldPerLocation;
import org.openmrs.module.patientindextracing.omodmodels.YieldPerNumAssignTraced;
import org.openmrs.module.patientindextracing.omodmodels.YieldPerSex;
import org.openmrs.module.patientindextracing.service.CacheHelper;
import org.openmrs.module.patientindextracing.service.CommunityTesters;
import org.openmrs.module.patientindextracing.service.ContactVisualization;
import org.openmrs.module.patientindextracing.service.PatientContacts;
import org.openmrs.module.patientindextracing.service.SystemUser;
import org.openmrs.module.patientindextracing.service.TokenStorage;
import org.openmrs.module.patientindextracing.utility.ConstantsUtil;
import org.openmrs.module.patientindextracing.utility.Utils;
import org.openmrs.notification.Alert;
import org.openmrs.notification.Message;
import org.springframework.web.bind.annotation.RequestParam;

public class NdrFragmentController {
	
	public void controller() {
	}
	
	public String visualizeYieldPerAssign(@RequestParam(value = "patientUUID", required = true) String patientUUID) {
		ContactVisualization visualization = new ContactVisualization(patientUUID);
		ObjectMapper mapper = new ObjectMapper();
		YieldPerNumAssignTraced yieldPerNumAssignTraced = new YieldPerNumAssignTraced();
		yieldPerNumAssignTraced = visualization.getYieldPerNumAssignTraced();
		
		try {
			return mapper.writeValueAsString(yieldPerNumAssignTraced);
		}
		catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		
		return null;
	}
	
	public String visualizeYieldPerSex(@RequestParam(value = "patientUUID", required = true) String patientUUID) {
		ContactVisualization visualization = new ContactVisualization(patientUUID);
		ObjectMapper mapper = new ObjectMapper();
		YieldPerSex yieldPerSex = visualization.getYieldPerSex();
		try {
			return mapper.writeValueAsString(yieldPerSex);
		}
		catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		
		return null;
		
	}
	
	public String visualizeYieldPerLocation(@RequestParam(value = "patientUUID", required = true) String patientUUID) {
		ContactVisualization visualization = new ContactVisualization(patientUUID);
		ObjectMapper mapper = new ObjectMapper();
		YieldPerLocation yieldPerLocation = visualization.getYieldPerLocation();
		
		try {
			return mapper.writeValueAsString(yieldPerLocation);
		}
		catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		
		return null;
		
	}
	
	public String visualizeAllYieldPerSex() {
		ContactVisualization visualization = new ContactVisualization();
		ObjectMapper mapper = new ObjectMapper();
		YieldPerSex yieldPerSex = visualization.getYieldPerSex();
		try {
			return mapper.writeValueAsString(yieldPerSex);
		}
		catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		
		return null;
		
	}
	
	public String visualizeAllYieldPerLocation() {
		ContactVisualization visualization = new ContactVisualization();
		ObjectMapper mapper = new ObjectMapper();
		YieldPerLocation yieldPerLocation = visualization.getYieldPerLocation();
		
		try {
			return mapper.writeValueAsString(yieldPerLocation);
		}
		catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		
		return null;
		
	}
	
	public String visualizeAllYieldPerAssign() {
		ContactVisualization visualization = new ContactVisualization();
		ObjectMapper mapper = new ObjectMapper();
		YieldPerNumAssignTraced yieldPerNumAssignTraced = new YieldPerNumAssignTraced();
		yieldPerNumAssignTraced = visualization.getYieldPerNumAssignTraced();
		
		try {
			return mapper.writeValueAsString(yieldPerNumAssignTraced);
		}
		catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		
		return null;
	}
	
	public String getContactSummary() {
		ContactVisualization visualization = new ContactVisualization();
		ObjectMapper mapper = new ObjectMapper();
		PatientContactSummary patientContactSummary = new PatientContactSummary();
		patientContactSummary = visualization.doPatientSummaryCount();
		
		try {
			return mapper.writeValueAsString(patientContactSummary);
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return null;
		
	}
	
	public String retrieveSystemUserDetails() {
		SystemUser userService = new SystemUser();
		ObjectMapper mapper = new ObjectMapper();
		
		UserModel userModel = null;
		
		try {
			
			userModel = userService.getSystemUser();
			if (userModel != null) {
				userModel.setMac(Utils.getMacAddress());
				userModel.setPassword("");
			} else {
				userModel = new UserModel();
				userModel.setUsername(Utils.getFacilityDATIMId());
				userModel.setMac(Utils.getMacAddress());
				userModel.setPasswordExist(Boolean.FALSE);
				// userModel.set
			}
			return mapper.writeValueAsString(userModel);
		}
		catch (IOException ex) {
			Logger.getLogger(NdrFragmentController.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return null;
	}
	
	public String saveSystemUser(@RequestParam(value = "userModel", required = true) String userModelString) {
		SystemUser userService = new SystemUser();
		ObjectMapper mapper = new ObjectMapper();
		String plainPassword = null;
		
		UserModel userModel = new UserModel();
		try {
			userModel = mapper.readValue(userModelString, UserModel.class);
		}
		catch (IOException ex) {
			Logger.getLogger(NdrFragmentController.class.getName()).log(Level.SEVERE, null, ex);
		}
		//CacheHelper cacheHelper = new CacheHelper();
		TokenStorage tokenStorage = new TokenStorage();
		
		//userModel.setDate_created(new Date());
		//userModel.setMac(Utils.getMacAddress());
		userModel.setPassword(Base64.getEncoder().encodeToString(userModel.getPassword().getBytes()));
		userModel.setOldPassword(Base64.getEncoder().encodeToString(userModel.getOldPassword().getBytes()));
		UserModel findUser = userService.getSystemUser();
		
		ChangePasswordModel changePasswordModel = new ChangePasswordModel();
		changePasswordModel.setDatimCode(userModel.getUsername());
		
		int res = 0;
		if (findUser != null) {
			plainPassword = new String(Base64.getDecoder().decode(userModel.getPassword()));
			changePasswordModel.setNew_password(plainPassword);
			if (findUser.getPassword().equals(userModel.getOldPassword())) {
				try {
					String tempOldPass = new String(Base64.getDecoder().decode(findUser.getPassword()));
					changePasswordModel.setOld_password(tempOldPass);
					
					boolean result = userService.changeUserPasswordViaWeb(changePasswordModel);
					if (result) {
						//userModel.setDate_modified(new Date());
						res = userService.editSystemUser(findUser.getId(), userModel);
					}
					tokenStorage.refreshTokenCache();
				}
				catch (UnirestException ex) {
					Logger.getLogger(NdrFragmentController.class.getName()).log(Level.SEVERE, null, ex);
				}
				
			} else {
				return "Error occurred, password mismatch!";
			}
			
		} else {
			try {
				plainPassword = new String(Base64.getDecoder().decode(userModel.getPassword()));
				changePasswordModel.setNew_password(plainPassword);
				changePasswordModel.setOld_password(ConstantsUtil.DEFAULT_PASSWORD);
				boolean result = userService.changeUserPasswordViaWeb(changePasswordModel);
				if (result) {
					res = userService.createSystemUser(userModel);
				}
				tokenStorage.refreshTokenCache();
			}
			catch (UnirestException ex) {
				Logger.getLogger(NdrFragmentController.class.getName()).log(Level.SEVERE, null, ex);
			}
			
		}
		if (res == 1) {
			return "Password saved successfully!";
		} else {
			return "Error occurred.";
		}
	}
	
	//get host for openmrs instance
	public String retrieveBiometricServer(String msg) {
		System.out.println("This is from the UI: " + msg);
		return Utils.getBiometricServer();
	}
	
	// pull community testers from the web.
	public String reloadTester() {
		
		CommunityTesters ctService = new CommunityTesters();
		
		try {
			ctService.refreshCommunityTesterList();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return "Error occurred, operation aborted!";
		}
		
		return "The data was refreshed successfully";
	}
	
	public String getIndexContacts(@RequestParam(value = "indexClientId", required = true) String indexClientId) {
		
		PatientContacts pcService = new PatientContacts();
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			Integer patientId = Context.getPatientService().getPatientByUuid(indexClientId).getId();
			return mapper.writeValueAsString(pcService.getAllPatientContactsByIndexId(patientId));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
		
	}
	
	public String retrieveClient(@RequestParam("clientData") String clientData) {
		PatientContacts pcService = new PatientContacts();
		ObjectMapper mapper = new ObjectMapper();
		
		if (clientData != null) {
			
			try {
				
				Client mappedClient = mapper.readValue(clientData, Client.class);
				//  Client client = new Client();
				
				String response = pcService.createPatientData(mappedClient, mappedClient.getClientIdentifier());
				if (response.equals(ConstantsUtil.SUCCESS_MSG_CREATE_CONTACT)) {
					//lock client from pulling twice
					try {
						pcService.lockClientViaWeb(mappedClient.getClientIdentifier());
					}
					catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
					
				}
				return response;
				
			}
			catch (Exception ex) {
				return "Error occurred, could not perform operation";
			}
		}
		
		return null;
	}
	
	public String retrieveClientOld() {
		Alert al = new Alert("Thsi is a sample alert", Context.getAuthenticatedUser());
		// al.setText("This is a custom alert");
		al.setUuid(UUID.randomUUID().toString());
		Context.getAlertService().saveAlert(al);
		
		return "sucess";
	}
	
	public Client retrieveClientByID(@RequestParam(value = "referralCode", required = true) String referralCode) {
		
		PatientContacts pcService = new PatientContacts();
		Client clientFromWeb = new Client();
		try {
			clientFromWeb = pcService.pullPatientByReferralCode(referralCode);
			
		}
		catch (Exception ex) {
			
		}
		//	Client dummyClientData = pcService.createDummyClient();
		
		return clientFromWeb;
		
	}
	
	public String reassignContact(@RequestParam(value = "contactId", required = true) int contactId,
	        @RequestParam("tester") String tester) {
		
		ObjectMapper mapper = new ObjectMapper();
		TesterModel testerModel = null;
		
		try {
			testerModel = mapper.readValue(tester, TesterModel.class);
		}
		catch (IOException ex) {
			Logger.getLogger(NdrFragmentController.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		PatientContacts pcService = new PatientContacts();
		String response = pcService.assignTesterToContact(contactId, testerModel, Context.getAuthenticatedUser().toString());
		return response;
		
	}
	
	public String pushContact() {
		
		PatientContacts pcService = new PatientContacts();
		pcService.pushContactsToWeb();
		return "Finish push";
	}
	
	//get all community testers
	public String getAllTesters() {
        //ObjectMapper mapper = new ObjectMapper();
        CommunityTesters ctService = new CommunityTesters();

        List<TesterModel> communityTesterses = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        String response = "";

        try {
            communityTesterses = ctService.getAllCommunityTesters();
            response = mapper.writeValueAsString(communityTesterses);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return response;
    }
}
