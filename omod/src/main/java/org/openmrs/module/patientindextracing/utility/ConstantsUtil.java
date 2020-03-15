/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.utility;

/**
 * @author MORRISON.I
 */
public class ConstantsUtil {
	
	public static final int PEPFAR_IDENTIFIER_INDEX = 4;
	
	public static final int HOSPITAL_IDENTIFIER_INDEX = 5;
	
	public static final int OPENMRS_IDENTIFIER_INDEX = 3;
	
	public static final int HTS_IDENTIFIER_INDEX = 8;
	
	public static final int PMTCT_IDENTIFIER_INDEX = 6;
	
	public static final int EXPOSE_INFANT_IDENTIFIER_INDEX = 7;
	
	public static final int PEP_ED_IDENTIFIER_INDEX = 9;
	
	public static final String COMMUNITY_TESTER_TABLE = "community_testers";
	
	public static final String PATIENT_CONTACT_TABLE = "patient_contacts";
	
	public static final String AUTH_TABLE = "user_auth";
	
	public static final String TOKEN_TABLE = "token_store";
	
	public static final String DEFAULT_TOKEN_ID = "web_token";
	
	public static final int ADMISSION_ENCOUNTER_TYPE = 2;
	
	public static final String TRACE_PENDING_STATUS = "Pending";
	
	public static final String TRACE_COMPLETED_STATUS = "Completed";
	
	public static final String DEFAULT_OBS_CHANGE_STATUS = "Insertion";
	
	public static final String DEFAULT_YES_CONCEPT_ID = "1065";
	
	public static final String DEFAULT_NO_CONCEPT_ID = "1066";
	
	public static final String DEFAULT_RECENT_TERM_CONCEPT_ID = "165852";
	
	public static final String DEFAULT_LONG_TERM_CONCEPT_ID = "165851";
	
	public static final String ADMISSION_FORM_UUID = "35cc381b-3b8f-45fa-b9b1-f638d51ad774";
	
	public static final String RISK_STRATIFICATION_PED_FORM_UUID = "a3524d1a-40cd-4bcd-b21c-82fbb0eab3db";
	
	public static final String RISK_STRATIFICATION_ADULT_FORM_UUID = "c50f3dda-754b-4fc5-9af9-0df15527b9e9";
	
	public static final String SUCCESS_MSG_CREATE_CONTACT = "Pulled successfully";
	
	public static final String DEFAULT_PASSWORD = "123456";
	
	public static final String TOKEN_CACHE_KEY = "token-cache";
	
	public static final String MALE_STRING = "Male";
	
	public static final String FEMALE_STRING = "Female";
	
	//Endpoints
	
	public static final String BASE_URL_LOGIN = "REMOVED_FOR_SECURITY";
	
	public static final String BASE_URL = "REMOVED_FOR_SECURITY";
	
	public static final String GET_COMMUNITY_TESTER = "REMOVED_FOR_SECURITY";
	
	public static final String GET_CLIENT = "REMOVED_FOR_SECURITY";
	
	public static final String POST_CONTACT = "REMOVED_FOR_SECURITY";
	
	public static final String CHECK_CONTACT_TRACE_STATUS = "REMOVED_FOR_SECURITY";
	
	public static final String POST_LOCK_CLIENT = "REMOVED_FOR_SECURITY";
	
	public static final String GET_USER_TOKEN = "REMOVED_FOR_SECURITY";
	
	public static final String POST_CHANGE_PASSWORD = "REMOVED_FOR_SECURITY";
	
}
