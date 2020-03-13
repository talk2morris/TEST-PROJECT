/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.RequestBodyEntity;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterRole;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientindextracing.dbmanager.NdrDBManager;
import org.openmrs.module.patientindextracing.omodmodels.AdultRiskStratification;
import org.openmrs.module.patientindextracing.omodmodels.CheckContactResponse;
import org.openmrs.module.patientindextracing.omodmodels.Client;
import org.openmrs.module.patientindextracing.omodmodels.ConceptIDMappings;
import org.openmrs.module.patientindextracing.omodmodels.ConceptValuesMappingUtil;
import org.openmrs.module.patientindextracing.omodmodels.ContactRequestModel;
import org.openmrs.module.patientindextracing.omodmodels.KnowledgeAssesement;
import org.openmrs.module.patientindextracing.omodmodels.PatientContactsModel;
import org.openmrs.module.patientindextracing.omodmodels.PedRiskStratification;
import org.openmrs.module.patientindextracing.omodmodels.PostTestCouncel;
import org.openmrs.module.patientindextracing.omodmodels.PreTestCouncel;
import org.openmrs.module.patientindextracing.omodmodels.RiskAssessment;
import org.openmrs.module.patientindextracing.omodmodels.STIScreening;
import org.openmrs.module.patientindextracing.omodmodels.TbScreening;
import org.openmrs.module.patientindextracing.omodmodels.TesterModel;
import org.openmrs.module.patientindextracing.utility.ConstantsUtil;
import org.openmrs.module.patientindextracing.utility.GeneralMapper;
import org.openmrs.module.patientindextracing.utility.Utils;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author MORRISON.I
 */
public class PatientContacts {
	
	private Concept concept_YES = null;
	
	private Concept concept_NO = null;
	
	private Concept concept_Long_Term = null;
	
	private Concept concept_Recent_Term = null;
	
	private TokenStorage tokenStorage;
	
	public PatientContacts() {
		concept_NO = Context.getConceptService().getConcept(ConstantsUtil.DEFAULT_NO_CONCEPT_ID);
		concept_YES = Context.getConceptService().getConcept(ConstantsUtil.DEFAULT_YES_CONCEPT_ID);
		concept_Long_Term = Context.getConceptService().getConcept(ConstantsUtil.DEFAULT_LONG_TERM_CONCEPT_ID);
		concept_Recent_Term = Context.getConceptService().getConcept(ConstantsUtil.DEFAULT_RECENT_TERM_CONCEPT_ID);
		tokenStorage = new TokenStorage();
	}
	
	NdrDBManager dbManager = null;
	
	public void updateContactStatus(String contactUUID, String datimCode) {
		
		try {
			dbManager = new NdrDBManager();
			dbManager.openConnection();
			dbManager.updatePatientContactTracedStatus(contactUUID);
			dbManager.closeConnection();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public CheckContactResponse checkContactStatusOnline(String facilityId, String conctactUuid) throws UnirestException {

        Unirest.setObjectMapper(GeneralMapper.getCustomObjectMapper());

        System.out.println("Fetching contact status using " + facilityId + " and " + conctactUuid);

        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("Authorization", "Bearer " + tokenStorage.getTokenCache());

        HttpResponse<CheckContactResponse> payloads = Unirest
                .get(ConstantsUtil.BASE_URL + ConstantsUtil.CHECK_CONTACT_TRACE_STATUS)
                .headers(headers)
                .queryString("facilityId", facilityId).queryString("contactUuid", conctactUuid)
                .asObject(CheckContactResponse.class);

        if (payloads.getStatus() == 200) {
            System.out.println("Got contact status: " + payloads.getBody().getTrace_status());
            return payloads.getBody();
        }

        return null;

    }
	
	public String lockClientViaWeb(String clientIdentifier) throws UnirestException {

        Unirest.setObjectMapper(GeneralMapper.getCustomObjectMapper());

        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("Authorization", "Bearer " + tokenStorage.getTokenCache());

        JSONObject jb = new JSONObject();
        jb.put("clientIdentifier", clientIdentifier);

        HttpResponse<String> payloads = Unirest.post(ConstantsUtil.BASE_URL + ConstantsUtil.POST_LOCK_CLIENT)
                .headers(headers)
                .body(jb)
                .asString();

        if (payloads.getStatus() == 200 || payloads.getStatus() == 201) {
            return payloads.getBody();
        }

        return null;

    }
	
	public int createContacts(PatientContactsModel pContacts) {
		int response = 0;
		try {
			dbManager = new NdrDBManager();
			dbManager.openConnection();
			response = dbManager.createPatientContacts(pContacts);
			dbManager.closeConnection();
			
		}
		catch (SQLException ex) {
			Logger.getLogger(PatientContacts.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		}
		
		return response;
	}
	
	public List<PatientContactsModel> getAllPatientContactsByIndexId(int indexClientId) {
        List<PatientContactsModel> response = new ArrayList<>();
        try {
            dbManager = new NdrDBManager();
            dbManager.openConnection();
            response = dbManager.getPatientContactsByIndex(indexClientId);
            dbManager.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(PatientContacts.class.getName()).log(Level.SEVERE, null, ex);
        }

        return response;
    }
	
	public Client pullPatientByReferralCode(String indexClientIndentifier) throws UnirestException {
        Unirest.setObjectMapper(GeneralMapper.getCustomObjectMapper());

        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("Authorization", "Bearer " + tokenStorage.getTokenCache());

        HttpResponse<Client> payloads = Unirest.get(ConstantsUtil.BASE_URL + ConstantsUtil.GET_CLIENT)
                .headers(headers)
                .queryString("ClientIdentifier", indexClientIndentifier)
                .asObject(Client.class);

        if (payloads != null) {
            return payloads.getBody();
        }

        return null;

    }
	
	public List<PatientContactsModel> pullAllPatientContacts() {
        List<PatientContactsModel> response = new ArrayList<>();
        try {
            dbManager = new NdrDBManager();
            dbManager.openConnection();
            response = dbManager.getAllPatientContacts();
            dbManager.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(PatientContacts.class.getName()).log(Level.SEVERE, null, ex);
        }

        return response;

    }
	
	public void pushContactsToWeb() {
        List<PatientContactsModel> contacts = pullAllPatientContacts();

        Unirest.setObjectMapper(GeneralMapper.getCustomObjectMapper());

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + tokenStorage.getTokenCache());

        for (PatientContactsModel each : contacts) {
            try {
                Patient pt = Context.getPatientService().getPatient(each.getIndex_patient_id());
                ContactRequestModel requestModel = GeneralMapper.mapToContactRequestModel(each);
                requestModel = GeneralMapper.mapPatientToContactRequest(pt, requestModel);

                System.out.println("Datimcode is: " + requestModel.getDatim_code());

                RequestBodyEntity response = Unirest.post(ConstantsUtil.BASE_URL + ConstantsUtil.POST_CONTACT)
                        .headers(headers)
                        .body(requestModel);

                if (response != null) {
                    System.out.println("Sent contact" + (each.getId()) + " for patient: " + each.getFirstname());
                } else {
                    System.err.println("Error occurred, sending contact with ID: " + each.getId());
                }
                System.out.println(response.getBody());
                System.out.println(response.asJson().getHeaders());
                System.out.println(response.asJson().getStatus());
                System.out.println(response.asJson().getStatusText());
                System.out.println(response.asJson().getBody());

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
	
	public String assignTesterToContact(int contactId, TesterModel tester, String modifier) {
		String responseMsg = "";
		if (tester != null) {
			try {
				dbManager = new NdrDBManager();
				dbManager.openConnection();
				dbManager.assignContacts(contactId, tester, modifier);
				dbManager.closeConnection();
				responseMsg = "Contact successfully assigned to community tester";
				
				return responseMsg;
				
			}
			catch (Exception ex) {
				Logger.getLogger(PatientContacts.class.getName()).log(Level.SEVERE, null, ex);
				responseMsg = "Error, occurred";
			}
		} else {
			responseMsg = "Error occurred, check that community tester exists";
		}
		
		return responseMsg;
	}
	
	public String createPatientData(Client client, String indexClientIndentifier) {
        Patient p = new Patient(GeneralMapper.ClientToPerson(client));
        p.setDateCreated(new Date());
        p.setCreator(Context.getAuthenticatedUser());

        Set<PatientIdentifier> pids = new TreeSet<>();
        PatientIdentifier pid_1 = new PatientIdentifier();
        // TODO: Check if patient already exists
        pid_1.setIdentifier(indexClientIndentifier);
        //  OPENMRS Identifier type
        pid_1.setIdentifierType(new PatientIdentifierType(ConstantsUtil.OPENMRS_IDENTIFIER_INDEX));
        pid_1.setPatient(p);

        pid_1.setLocation(Context.getLocationService().getDefaultLocation());
        pid_1.setPatient(p);
        pid_1.setUuid(UUID.randomUUID().toString());
        pid_1.setPreferred(Boolean.TRUE);
        pids.add(pid_1);

        p.setIdentifiers(pids);
        p.setUuid(UUID.randomUUID().toString());

        try {
            Context.getPatientService().savePatient(p);

        } catch (Exception ex) {
            //return to caller here
            return "Error occurred! check if patient already exists.";

        }

        String riskResponse = null;
        String htsResponse = null;

        try {
            if (client.getRiskStratificationNew() != null && client.getRiskStratificationNew().getAdultRiskStratification() != null) {
                riskResponse = createPatientAdultRiskStrat(client, p);
            } else if (client.getRiskStratificationNew() != null && client.getRiskStratificationNew().getPedRiskStratification() != null) {
                riskResponse = createPatientPedRiskStrat(client, p);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            //    if (client.getStoppedAtPreTest() == 0) {

            htsResponse = createPatientAndFillHTS(client, p);
            //   }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return resolveResponseMessage(riskResponse, htsResponse);

    }
	
	private String resolveResponseMessage(String riskResponse, String htsResponse) {
		if (riskResponse == null && htsResponse == null) {
			return "Error occurred, could not perform operation";
		}
		
		if (riskResponse == null && htsResponse != null) {
			return "Not all Operation completed, Risk Stratification could not process";
		}
		
		if (riskResponse != null && htsResponse == null) {
			return "Not all Opeartion completed, Hts data could not process";
		}
		
		return ConstantsUtil.SUCCESS_MSG_CREATE_CONTACT;
		
	}
	
	@Transactional
    public String createPatientAndFillHTS(Client client, Patient p) {

        //  Patient p = Context.getPatientService().getPatient(10812);
        Encounter clientEncounter = new Encounter();
        Visit clientVisit = new Visit(p, Context.getVisitService().getAllVisitTypes().get(0), new Date());

        clientEncounter.setVisit(clientVisit);
        clientEncounter.setLocation(Context.getUserContext().getLocation());
        clientEncounter.setPatient(p);
        clientEncounter.setEncounterDatetime(new Date());
        clientEncounter.setForm(Context.getFormService().getFormByUuid(ConstantsUtil.ADMISSION_FORM_UUID));
        EncounterRole er = Context.getEncounterService().getEncounterRoleByUuid(EncounterRole.UNKNOWN_ENCOUNTER_ROLE_UUID);

        //UUID for super user
        clientEncounter.setProvider(er, Context.getProviderService().getProviderByUuid("f9badd80-ab76-11e2-9e96-0800200c9a66"));
        clientEncounter.setEncounterType(Context.getEncounterService().getEncounterType(Utils.Admission_Simple_Client_intake));

        
        
        //setting
        Set<Obs> allObs = new HashSet<>();
        Concept testingSetting = resolveHivTestingSetting(client.getTestingPoint());
        Obs settingObs = new Obs();
        settingObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.HTS_Client_Intake_SETTING_ConceptID));
        if (testingSetting != null) {
            settingObs.setValueCoded(testingSetting);
        } else {
            //surge setting is default to community
            settingObs.setValueCoded(Context.getConceptService().getConcept(ConceptValuesMappingUtil.SETTING_COMMUNITY.getKey()));
        }

        settingObs.setUuid(UUID.randomUUID().toString());
        settingObs.setPerson(p);
        settingObs.setEncounter(clientEncounter);
        //TODO: use date from request
        settingObs.setObsDatetime(new Date());

        
        //first time question
        Obs firstTimeObs = new Obs();
        firstTimeObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.HTS_CLient_Intake_FIRST_TIME_ConceptID));
        firstTimeObs.setValueCoded(Context.getConceptService().getConcept(1065));
        firstTimeObs.setPerson(p);
        firstTimeObs.setUuid(UUID.randomUUID().toString());
        firstTimeObs.setObsDatetime(new Date());
        firstTimeObs.setEncounter(clientEncounter);
        // Context.getObsService().saveObs(settingObs, ConstantsUtil.DEFAULT_OBS_CHANGE_STATUS);

        Concept rSessionConcept = resolveSessionConcept(client.getSessionType());

        if (rSessionConcept != null) {
            Obs typeOfSession = new Obs();

            typeOfSession.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.HTS_Client_Intake_SESSION_TYPE_ConceptID));
            typeOfSession.setValueCoded(rSessionConcept);
            typeOfSession.setPerson(p);
            typeOfSession.setUuid(UUID.randomUUID().toString());
            typeOfSession.setObsDatetime(new Date());
            typeOfSession.setEncounter(clientEncounter);
            //  Context.getObsService().saveObs(typeOfSession, ConstantsUtil.DEFAULT_OBS_CHANGE_STATUS);
            allObs.add(typeOfSession);
        }

        Obs refferedFromObs = new Obs();

        refferedFromObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.HTS_Client_Intake_REFFERRED_FROM));
        //hard code refferred from
        refferedFromObs.setValueCoded(Context.getConceptService().getConcept(978));
        refferedFromObs.setPerson(p);
        refferedFromObs.setUuid(UUID.randomUUID().toString());
        refferedFromObs.setObsDatetime(new Date());
        refferedFromObs.setEncounter(clientEncounter);
        //  Context.getObsService().saveObs(refferedFromObs, ConstantsUtil.DEFAULT_OBS_CHANGE_STATUS);

        Concept rMaritalConceptVal = resolveMaritalStatus(client.getMaritalStatus());
        if (rMaritalConceptVal != null) {
            Obs maritalObs = new Obs();
            maritalObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.HTS_Client_Intake_MARITAL_STATUS_ConceptID));
            maritalObs.setValueCoded(rMaritalConceptVal);
            maritalObs.setPerson(p);
            maritalObs.setUuid(UUID.randomUUID().toString());
            maritalObs.setObsDatetime(new Date());
            maritalObs.setEncounter(clientEncounter);
            //  Context.getObsService().saveObs(maritalObs, ConstantsUtil.DEFAULT_OBS_CHANGE_STATUS);
            allObs.add(maritalObs);
        }

        Concept rClientIndex = resolveClientIdentifiedIndex(client.getIndexClient());
        if (rClientIndex != null) {
            Obs clientIndexObs = new Obs();
            clientIndexObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.HTS_Client_Intake_Client_IDENTIFIED_INDEX));
            clientIndexObs.setValueCoded(rClientIndex);
            clientIndexObs.setPerson(p);
            clientIndexObs.setObsDatetime(new Date());
            clientIndexObs.setUuid(UUID.randomUUID().toString());
            clientIndexObs.setEncounter(clientEncounter);
            //  Context.getObsService().saveObs(clientIndexObs, ConstantsUtil.DEFAULT_OBS_CHANGE_STATUS);

            allObs.add(clientIndexObs);

            if (client.getIndexClient().equals("Yes")) {

                Concept rIndexType = resolveIndexType(client.getIndexType());
                if (rIndexType != null) {
                    Obs indexTypeObs = new Obs();
                    indexTypeObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.HTS_Client_Intake_INDEX_TYPE));
                    indexTypeObs.setValueCoded(rIndexType);
                    indexTypeObs.setPerson(p);
                    indexTypeObs.setObsDatetime(new Date());
                    indexTypeObs.setUuid(UUID.randomUUID().toString());
                    indexTypeObs.setEncounter(clientEncounter);
                    //   Context.getObsService().saveObs(indexTypeObs, ConstantsUtil.DEFAULT_OBS_CHANGE_STATUS);

                    allObs.add(indexTypeObs);
                }

                if (client.getIndexClientId() != null) {
                    Obs indexClientIDObs = new Obs();
                    indexClientIDObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.HTS_Client_Intake_INDEX_CLIENT_ID));
                    indexClientIDObs.setValueText(client.getIndexClientId());
                    indexClientIDObs.setPerson(p);
                    indexClientIDObs.setObsDatetime(new Date());
                    indexClientIDObs.setUuid(UUID.randomUUID().toString());
                    indexClientIDObs.setEncounter(clientEncounter);
                    //  Context.getObsService().saveObs(indexClientIDObs, ConstantsUtil.DEFAULT_OBS_CHANGE_STATUS);

                    allObs.add(indexClientIDObs);

                }

            }

        }

        //HIV Result
        Concept prevTestValueConcept = resolveHivTestResult(client.getPreviousResult());
        if (prevTestValueConcept != null) {
            Obs prevTestObs = new Obs();
            prevTestObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.SCREENING_TEST_RESULT));
            prevTestObs.setValueCoded(prevTestValueConcept);
            prevTestObs.setPerson(p);
            prevTestObs.setObsDatetime(new Date());
            prevTestObs.setUuid(UUID.randomUUID().toString());
            prevTestObs.setEncounter(clientEncounter);

            allObs.add(prevTestObs);

        }

        Concept currentHivResult = resolveHivFinalTestResult(client.getCurrentResult());
        if (currentHivResult != null) {
            Obs currentObs = new Obs();
            currentObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.FINAL_RESULT));
            currentObs.setValueCoded(currentHivResult);
            currentObs.setPerson(p);
            currentObs.setObsDatetime(new Date());
            currentObs.setUuid(UUID.randomUUID().toString());
            currentObs.setEncounter(clientEncounter);

            allObs.add(currentObs);
        }

        if (client.getHivTestDate() != null) {
            Obs hivTestDateObs = new Obs();
            hivTestDateObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.SCREENING_TEST_RESULT_DATE));
            hivTestDateObs.setValueDatetime(client.getHivTestDate());
            hivTestDateObs.setPerson(p);
            hivTestDateObs.setObsDatetime(new Date());
            hivTestDateObs.setUuid(UUID.randomUUID().toString());

            allObs.add(hivTestDateObs);

        }

        //recency Testing
        if (client.getPreviouslyTested().equalsIgnoreCase(ConceptValuesMappingUtil.HIV_FINAL_TEST_RESULT_POSITIVE.getValue())) {
            if (client.getHivRecencyTestType() != null) {
                Obs recencyTypeObs = new Obs();
                recencyTypeObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.HIV_RECENCY_ASSAY_CONCEPT));
                recencyTypeObs.setValueCoded(resolveTerm(client.getHivRecencyTestType()));
                recencyTypeObs.setPerson(p);
                recencyTypeObs.setObsDatetime(new Date());
                recencyTypeObs.setUuid(UUID.randomUUID().toString());

                allObs.add(recencyTypeObs);
            }

            if (client.getHivRecencyTestDate() != null) {
                Obs recencyTestDateObs = new Obs();
                recencyTestDateObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.HIV_RECENCY_TEST_DATE_CONCEPT));
                recencyTestDateObs.setValueDatetime(client.getHivTestDate());
                recencyTestDateObs.setPerson(p);
                recencyTestDateObs.setObsDatetime(new Date());
                recencyTestDateObs.setUuid(UUID.randomUUID().toString());

                allObs.add(recencyTestDateObs);
            }

            if (client.getFinalRecencyTestResult() != null) {
                Obs finalRecencyResult = new Obs();
                finalRecencyResult.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.FINAL_HIV_RECENCY_INFECTION_TESTING_ALGORITHM_RESULT_CONCEPT));
                finalRecencyResult.setValueCoded(resolveTerm(client.getFinalRecencyTestResult()));
                finalRecencyResult.setPerson(p);
                finalRecencyResult.setObsDatetime(new Date());
                finalRecencyResult.setUuid(UUID.randomUUID().toString());

                allObs.add(finalRecencyResult);

            }

            //TODO: Use default ASANTE Value for test name
        }

        if (client.getPreTestCouncel() != null) {
            //change to value coded
            //Knowledge assessment section
            //previously tested HIV
            if (client.getPreTestCouncel().getKnowledge_assessment() != null) {
                Obs prevTestHIV = new Obs();
                prevTestHIV.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Previously_Tested_Hiv_Negative_Concept_Id));
                if (client.getPreTestCouncel() != null && client.getPreTestCouncel().getKnowledge_assessment().getKnowledge_assessment_previously_tested_negative() != null) {
                    prevTestHIV.setValueCoded(resolveIntegerConcept(client.getPreTestCouncel().getKnowledge_assessment().getKnowledge_assessment_previously_tested_negative()));
                    prevTestHIV.setPerson(p);
                    prevTestHIV.setUuid(UUID.randomUUID().toString());
                    prevTestHIV.setObsDatetime(new Date());
                    prevTestHIV.setEncounter(clientEncounter);
                    //   Context.getObsService().saveObs(prevTestHIV, ConstantsUtil.DEFAULT_OBS_CHANGE_STATUS);
                    allObs.add(prevTestHIV);
                }

                //client pregnant
                Obs clientPregnantObs = new Obs();
                clientPregnantObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Client_Pregnant_Concept_Id));
                if (client.getPreTestCouncel() != null && client.getPreTestCouncel().getKnowledge_assessment().getKnowledge_assessment_pregnant() != null) {
                    clientPregnantObs.setValueBoolean(resolveBoolean(client.getPreTestCouncel().getKnowledge_assessment().getKnowledge_assessment_pregnant()));
                    clientPregnantObs.setPerson(p);
                    clientPregnantObs.setUuid(UUID.randomUUID().toString());
                    clientPregnantObs.setObsDatetime(new Date());
                    clientPregnantObs.setEncounter(clientEncounter);
                    //  Context.getObsService().saveObs(clientPregnantObs, ConstantsUtil.DEFAULT_OBS_CHANGE_STATUS);
                    allObs.add(clientPregnantObs);
                }

                //hiv transmission routes
                Obs hivTransRoutes = new Obs();
                hivTransRoutes.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Client_Informed_About_Transsmission_Concept_Id));
                if (client.getPreTestCouncel() != null && client.getPreTestCouncel().getKnowledge_assessment().getKnowledge_assessment_informed_about_transmission_routes() != null) {
                    hivTransRoutes.setValueCoded(resolveBooleanConcept(resolveBoolean(client.getPreTestCouncel().getKnowledge_assessment().getKnowledge_assessment_informed_about_transmission_routes())));
                    hivTransRoutes.setPerson(p);
                    hivTransRoutes.setUuid(UUID.randomUUID().toString());
                    hivTransRoutes.setObsDatetime(new Date());
                    hivTransRoutes.setEncounter(clientEncounter);
                    //  Context.getObsService().saveObs(hivTransRoutes, ConstantsUtil.DEFAULT_OBS_CHANGE_STATUS);
                    allObs.add(hivTransRoutes);
                }

                //about risk factor of transmission
                Obs riskFactorObs = new Obs();
                riskFactorObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Client_Informed_About_Risk_Factors_Concept_Id));
                if (client.getPreTestCouncel() != null && client.getPreTestCouncel().getKnowledge_assessment().getKnowledge_assessment_informed_about_risk_factors() != null) {
                    riskFactorObs.setValueCoded(resolveBooleanConcept(resolveBoolean(client.getPreTestCouncel().getKnowledge_assessment().getKnowledge_assessment_informed_about_risk_factors())));
                    riskFactorObs.setPerson(p);
                    riskFactorObs.setUuid(UUID.randomUUID().toString());
                    riskFactorObs.setObsDatetime(new Date());
                    riskFactorObs.setEncounter(clientEncounter);
                    //   Context.getObsService().saveObs(riskFactorObs, ConstantsUtil.DEFAULT_OBS_CHANGE_STATUS);
                    allObs.add(riskFactorObs);
                }

                //preventing hiv transmission
                Obs prenHIVtransObs = new Obs();
                prenHIVtransObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Client_Informed_On_Prevention_Concept_Id));
                if (client.getPreTestCouncel() != null && client.getPreTestCouncel().getKnowledge_assessment().getKnowledge_assessment_informed_on_preventing_transmission_method() != null) {
                    prenHIVtransObs.setValueCoded(resolveBooleanConcept(resolveBoolean(client.getPreTestCouncel().getKnowledge_assessment().getKnowledge_assessment_informed_on_preventing_transmission_method())));
                    prenHIVtransObs.setPerson(p);
                    prenHIVtransObs.setUuid(UUID.randomUUID().toString());
                    prenHIVtransObs.setObsDatetime(new Date());
                    prenHIVtransObs.setEncounter(clientEncounter);
                    //  Context.getObsService().saveObs(prenHIVtransObs, ConstantsUtil.DEFAULT_OBS_CHANGE_STATUS);
                    allObs.add(prenHIVtransObs);
                }

                //informed about possible test result  
                Obs aboutPossibleTestResult = new Obs();
                aboutPossibleTestResult.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Client_Informed_About_Possible_Concept_Id));
                if (client.getPreTestCouncel() != null && client.getPreTestCouncel().getKnowledge_assessment().getKnowledge_assessment_informed_about_possible_test_results() != null) {
                    aboutPossibleTestResult.setValueCoded(resolveBooleanConcept(resolveBoolean(client.getPreTestCouncel().getKnowledge_assessment().getKnowledge_assessment_informed_about_possible_test_results())));
                    aboutPossibleTestResult.setPerson(p);
                    aboutPossibleTestResult.setUuid(UUID.randomUUID().toString());
                    aboutPossibleTestResult.setObsDatetime(new Date());
                    aboutPossibleTestResult.setEncounter(clientEncounter);
                    //  Context.getObsService().saveObs(aboutPossibleTestResult, ConstantsUtil.DEFAULT_OBS_CHANGE_STATUS);
                    allObs.add(aboutPossibleTestResult);
                }

                //consent for hiv given
                Obs consentForHIV = new Obs();
                consentForHIV.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Informed_Consent_For_Hiv_Testing_Concept_Id));
                if (client.getPreTestCouncel() != null && client.getPreTestCouncel().getKnowledge_assessment().getKnowledge_assessment_informed_consent_for_testing_given() != null) {
                    consentForHIV.setValueBoolean(resolveBoolean(client.getPreTestCouncel().getKnowledge_assessment().getKnowledge_assessment_informed_consent_for_testing_given()));
                    consentForHIV.setPerson(p);
                    consentForHIV.setUuid(UUID.randomUUID().toString());
                    consentForHIV.setObsDatetime(new Date());
                    consentForHIV.setEncounter(clientEncounter);
                    //    Context.getObsService().saveObs(consentForHIV, ConstantsUtil.DEFAULT_OBS_CHANGE_STATUS);
                    allObs.add(consentForHIV);
                }
            }

            //risk assessment
            if (client.getPreTestCouncel().getRisk_assessment() != null) {
                Obs everHadSexIntercoruse = new Obs();
                everHadSexIntercoruse.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Ever_Had_Sexual_Intercourse_Concept_Id));
                if (client.getPreTestCouncel().getRisk_assessment().getRisk_assessment_ever_had_sexual_intercourse() != null) {
                    everHadSexIntercoruse.setValueCoded(resolveIntegerConcept(client.getPreTestCouncel().getRisk_assessment().getRisk_assessment_ever_had_sexual_intercourse()));
                    everHadSexIntercoruse.setPerson(p);
                    everHadSexIntercoruse.setUuid(UUID.randomUUID().toString());
                    everHadSexIntercoruse.setObsDatetime(new Date());
                    everHadSexIntercoruse.setEncounter(clientEncounter);

                    allObs.add(everHadSexIntercoruse);
                }

                Obs riskBloodTrans = new Obs();
                riskBloodTrans.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Blood_Transfussion_In_Last_3_Months));
                if (client.getPreTestCouncel().getRisk_assessment().getRisk_assessment_blood_transfussion_in_last_3_month() != null) {
                    riskBloodTrans.setValueCoded(resolveIntegerConcept(client.getPreTestCouncel().getRisk_assessment().getRisk_assessment_blood_transfussion_in_last_3_month()));
                    riskBloodTrans.setPerson(p);
                    riskBloodTrans.setUuid(UUID.randomUUID().toString());
                    riskBloodTrans.setObsDatetime(new Date());
                    riskBloodTrans.setEncounter(clientEncounter);

                    allObs.add(riskBloodTrans);

                }

                Obs unprotectedSexCasual = new Obs();
                unprotectedSexCasual.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Unprotected_Sex_With_Casual_Partner_In_Last_3_Months));
                if (client.getPreTestCouncel().getRisk_assessment().getRisk_assessment_unprotected_sex_with_casual_partner_in_last_3_months() != null) {
                    unprotectedSexCasual.setValueCoded(resolveIntegerConcept(client.getPreTestCouncel().getRisk_assessment().getRisk_assessment_unprotected_sex_with_casual_partner_in_last_3_months()));
                    unprotectedSexCasual.setPerson(p);
                    unprotectedSexCasual.setUuid(UUID.randomUUID().toString());
                    unprotectedSexCasual.setObsDatetime(new Date());
                    unprotectedSexCasual.setEncounter(clientEncounter);

                    allObs.add(unprotectedSexCasual);
                }

                Obs unprotectedSexRegular = new Obs();
                unprotectedSexRegular.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Unprotected_Sex_With_Regular_Partner_In_Last_3_Months));
                if (client.getPreTestCouncel().getRisk_assessment().getRisk_assessment_unprotected_sex_with_regular_partner_in_last_3_months() != null) {
                    unprotectedSexRegular.setValueCoded(resolveIntegerConcept(client.getPreTestCouncel().getRisk_assessment().getRisk_assessment_unprotected_sex_with_regular_partner_in_last_3_months()));
                    unprotectedSexRegular.setPerson(p);
                    unprotectedSexRegular.setUuid(UUID.randomUUID().toString());
                    unprotectedSexRegular.setObsDatetime(new Date());
                    unprotectedSexRegular.setEncounter(clientEncounter);
                    allObs.add(unprotectedSexRegular);
                }

                Obs stilastMonths = new Obs();
                stilastMonths.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Sti_In_Last_3_Months));
                if (client.getPreTestCouncel().getRisk_assessment().getRisk_assessment_sti_in_last_3_months() != null) {
                    stilastMonths.setValueCoded(resolveIntegerConcept(client.getPreTestCouncel().getRisk_assessment().getRisk_assessment_sti_in_last_3_months()));
                    stilastMonths.setPerson(p);
                    stilastMonths.setUuid(UUID.randomUUID().toString());
                    stilastMonths.setObsDatetime(new Date());
                    stilastMonths.setEncounter(clientEncounter);
                    allObs.add(stilastMonths);
                }

                Obs moreThanOneSexPartner = new Obs();
                moreThanOneSexPartner.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.More_Than_1_Sex_Partner));
                if (client.getPreTestCouncel().getRisk_assessment().getRisk_assessment_more_than_1_sex_partner_in_last_3_months() != null) {
                    moreThanOneSexPartner.setValueCoded(resolveIntegerConcept(client.getPreTestCouncel().getRisk_assessment().getRisk_assessment_more_than_1_sex_partner_in_last_3_months()));
                    moreThanOneSexPartner.setPerson(p);
                    moreThanOneSexPartner.setUuid(UUID.randomUUID().toString());
                    moreThanOneSexPartner.setObsDatetime(new Date());
                    moreThanOneSexPartner.setEncounter(clientEncounter);
                    allObs.add(moreThanOneSexPartner);
                }
            }

            //TB Screening
            if (client.getPreTestCouncel().getTb_screening() != null) {
                Obs currentCough = new Obs();
                currentCough.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Current_Cough));
                if (client.getPreTestCouncel().getTb_screening().getTb_screening_current_cough() != null) {
                    currentCough.setValueCoded(resolveIntegerConcept(client.getPreTestCouncel().getTb_screening().getTb_screening_current_cough()));
                    currentCough.setPerson(p);
                    currentCough.setUuid(UUID.randomUUID().toString());
                    currentCough.setObsDatetime(new Date());
                    currentCough.setEncounter(clientEncounter);
                    allObs.add(currentCough);

                }

                Obs weightLoss = new Obs();
                weightLoss.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Weight_loss));
                if (client.getPreTestCouncel().getTb_screening().getTb_screening_weight_loss() != null) {
                    weightLoss.setValueCoded(resolveIntegerConcept(client.getPreTestCouncel().getTb_screening().getTb_screening_weight_loss()));
                    weightLoss.setPerson(p);
                    weightLoss.setUuid(UUID.randomUUID().toString());
                    weightLoss.setObsDatetime(new Date());
                    weightLoss.setEncounter(clientEncounter);
                    allObs.add(weightLoss);
                }

                Obs feverObs = new Obs();
                feverObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Fever));
                if (client.getPreTestCouncel().getTb_screening().getTb_screening_fever() != null) {
                    feverObs.setValueCoded(resolveIntegerConcept(client.getPreTestCouncel().getTb_screening().getTb_screening_fever()));
                    feverObs.setPerson(p);
                    feverObs.setUuid(UUID.randomUUID().toString());
                    feverObs.setObsDatetime(new Date());
                    feverObs.setEncounter(clientEncounter);
                    allObs.add(feverObs);
                }

                Obs nightSweats = new Obs();
                nightSweats.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Night_sweats));
                if (client.getPreTestCouncel().getTb_screening().getTb_screening_night_sweats() != null) {
                    nightSweats.setValueCoded(resolveIntegerConcept(client.getPreTestCouncel().getTb_screening().getTb_screening_night_sweats()));
                    nightSweats.setPerson(p);
                    nightSweats.setUuid(UUID.randomUUID().toString());
                    nightSweats.setObsDatetime(new Date());
                    nightSweats.setEncounter(clientEncounter);
                    allObs.add(nightSweats);
                }

                //contact with TB+ patient
                Obs contactWithTB = new Obs();
                contactWithTB.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.CONTAT_WITH_TB_PATIENT));
                if (client.getPreTestCouncel().getTb_screening().getTb_contact_with_tb_patient() != null) {
                    contactWithTB.setValueCoded(resolveIntegerConcept(client.getPreTestCouncel().getTb_screening().getTb_contact_with_tb_patient()));
                    contactWithTB.setPerson(p);
                    contactWithTB.setUuid(UUID.randomUUID().toString());
                    contactWithTB.setObsDatetime(new Date());
                    contactWithTB.setEncounter(clientEncounter);
                    allObs.add(contactWithTB);
                }
            }

            //syndromic STI Screening
            if (client.getPreTestCouncel().getSti_screening() != null) {
                Obs vaginalDischargeBurning = new Obs();
                vaginalDischargeBurning.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Complaints_of_vaginal_discharge_or_burning_when_urinating));
                if (client.getPreTestCouncel().getSti_screening().getSti_screening_complain_of_vaginal_discharge() != null) {
                    vaginalDischargeBurning.setValueCoded(resolveIntegerConcept(client.getPreTestCouncel().getSti_screening().getSti_screening_complain_of_vaginal_discharge()));
                    vaginalDischargeBurning.setPerson(p);
                    vaginalDischargeBurning.setUuid(UUID.randomUUID().toString());
                    vaginalDischargeBurning.setObsDatetime(new Date());
                    vaginalDischargeBurning.setEncounter(clientEncounter);
                    allObs.add(vaginalDischargeBurning);
                }

                Obs lowerAbdominal = new Obs();
                lowerAbdominal.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Complaints_of_lower_abdominal_pains_with_or_without_vaginal_discharge));
                if (client.getPreTestCouncel().getSti_screening().getSti_screening_complain_of_lower_abominal_pain() != null) {
                    lowerAbdominal.setValueCoded(resolveIntegerConcept(client.getPreTestCouncel().getSti_screening().getSti_screening_complain_of_lower_abominal_pain()));
                    lowerAbdominal.setPerson(p);
                    lowerAbdominal.setUuid(UUID.randomUUID().toString());
                    lowerAbdominal.setObsDatetime(new Date());
                    lowerAbdominal.setEncounter(clientEncounter);
                    allObs.add(lowerAbdominal);
                }

                Obs urethralDischarge = new Obs();
                urethralDischarge.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Complaints_of_urethral_discharge_or_burning_when_urinating));
                if (client.getPreTestCouncel().getSti_screening().getSti_screening_complain_of_urethral_discharge() != null) {
                    urethralDischarge.setValueCoded(resolveIntegerConcept(client.getPreTestCouncel().getSti_screening().getSti_screening_complain_of_urethral_discharge()));
                    urethralDischarge.setPerson(p);
                    urethralDischarge.setUuid(UUID.randomUUID().toString());
                    urethralDischarge.setObsDatetime(new Date());
                    urethralDischarge.setEncounter(clientEncounter);
                    allObs.add(urethralDischarge);
                }

                Obs scroltalSwelling = new Obs();
                scroltalSwelling.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Complaints_of_scrotal_swelling_and_pain));
                if (client.getPreTestCouncel().getSti_screening().getSti_screening_complain_of_scrotal_swelling() != null) {
                    scroltalSwelling.setValueCoded(resolveIntegerConcept(client.getPreTestCouncel().getSti_screening().getSti_screening_complain_of_scrotal_swelling()));
                    scroltalSwelling.setPerson(p);
                    scroltalSwelling.setUuid(UUID.randomUUID().toString());
                    scroltalSwelling.setObsDatetime(new Date());
                    scroltalSwelling.setEncounter(clientEncounter);
                    allObs.add(scroltalSwelling);
                }

                Obs genitalScore = new Obs();
                genitalScore.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Complaints_of_genital_sore));
                if (client.getPreTestCouncel().getSti_screening().getSti_screening_complain_of_genital_sore() != null) {
                    genitalScore.setValueCoded(resolveIntegerConcept((client.getPreTestCouncel().getSti_screening().getSti_screening_complain_of_genital_sore())));
                    genitalScore.setPerson(p);
                    genitalScore.setUuid(UUID.randomUUID().toString());
                    genitalScore.setObsDatetime(new Date());
                    genitalScore.setEncounter(clientEncounter);
                    allObs.add(genitalScore);

                }

            }

        }

        //Post test councelling
        if (client.getPostTestCouncel() != null) {
            Obs testedBefore = new Obs();
            testedBefore.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Have_you_been_tested_for_HIV_before_within_this_year));
            if (client.getPostTestCouncel().getPostTestedBefore() != null) {
                testedBefore.setValueCoded(resolvePreviouslyTested(client.getPostTestCouncel().getPostTestedBefore()));
                testedBefore.setPerson(p);
                testedBefore.setUuid(UUID.randomUUID().toString());
                testedBefore.setObsDatetime(new Date());
                testedBefore.setEncounter(clientEncounter);
                allObs.add(testedBefore);
            }

            Obs requestAndResultSign = new Obs();
            requestAndResultSign.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.HIV_Request_and_Result_form_signed_by_tester));
            if (client.getPostTestCouncel().getRequest_and_result_form_signed_by_testers() != null) {
                requestAndResultSign.setValueCoded(resolveIntegerConcept(client.getPostTestCouncel().getRequest_and_result_form_signed_by_testers()));
                requestAndResultSign.setPerson(p);
                requestAndResultSign.setUuid(UUID.randomUUID().toString());
                requestAndResultSign.setObsDatetime(new Date());
                requestAndResultSign.setEncounter(clientEncounter);
                allObs.add(requestAndResultSign);
            }

            Obs requestAndResultIntake = new Obs();
            requestAndResultIntake.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.HIV_Request_and_Result_form_filled_with_CT_Intake_Form));
            if (client.getPostTestCouncel().getRequest_and_result_form_filled_with_ct_intake_form() != null) {
                requestAndResultIntake.setValueCoded(resolveIntegerConcept(client.getPostTestCouncel().getRequest_and_result_form_filled_with_ct_intake_form()));
                requestAndResultIntake.setPerson(p);
                requestAndResultIntake.setUuid(UUID.randomUUID().toString());
                requestAndResultIntake.setObsDatetime(new Date());
                requestAndResultIntake.setEncounter(clientEncounter);
                allObs.add(requestAndResultIntake);
            }

            Obs clientReceivedResult = new Obs();
            clientReceivedResult.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Client_received_HIV_test_result));
            if (client.getPostTestCouncel().getClient_received_hiv_result() != null) {
                clientReceivedResult.setValueCoded(resolveIntegerConcept(client.getPostTestCouncel().getClient_received_hiv_result()));
                clientReceivedResult.setPerson(p);
                clientReceivedResult.setUuid(UUID.randomUUID().toString());
                clientReceivedResult.setObsDatetime(new Date());
                clientReceivedResult.setEncounter(clientEncounter);
                allObs.add(clientReceivedResult);

            }

            Obs postTestCouncelling = new Obs();
            postTestCouncelling.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Post_test_counseling_done));
            if (client.getPostTestCouncel().getPost_test_councelling_done() != null) {
                postTestCouncelling.setValueCoded(resolveIntegerConcept(client.getPostTestCouncel().getPost_test_councelling_done()));
                postTestCouncelling.setPerson(p);
                postTestCouncelling.setUuid(UUID.randomUUID().toString());
                postTestCouncelling.setObsDatetime(new Date());
                postTestCouncelling.setEncounter(clientEncounter);
                allObs.add(postTestCouncelling);
            }

            Obs riskReductionDev = new Obs();
            riskReductionDev.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Risk_reduction_plan_developed));
            if (client.getPostTestCouncel().getRisk_reduction_plan_developed() != null) {
                riskReductionDev.setValueCoded(resolveIntegerConcept(client.getPostTestCouncel().getRisk_reduction_plan_developed()));
                riskReductionDev.setPerson(p);
                riskReductionDev.setUuid(UUID.randomUUID().toString());
                riskReductionDev.setObsDatetime(new Date());
                riskReductionDev.setEncounter(clientEncounter);
                allObs.add(riskReductionDev);
            }

            Obs postTestDisclosure = new Obs();
            postTestDisclosure.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Post_test_disclosure_plan_developed));
            if (client.getPostTestCouncel().getPost_test_disclosure_plan_developed() != null) {
                postTestDisclosure.setValueCoded(resolveIntegerConcept(client.getPostTestCouncel().getPost_test_disclosure_plan_developed()));
                postTestDisclosure.setPerson(p);
                postTestDisclosure.setUuid(UUID.randomUUID().toString());
                postTestDisclosure.setObsDatetime(new Date());
                postTestDisclosure.setEncounter(clientEncounter);
                allObs.add(postTestDisclosure);
            }

            Obs willBringPartner = new Obs();
            willBringPartner.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Will_bring_partner_for_HIV_testing));
            if (client.getPostTestCouncel().getWill_bring_partners_for_testing() != null) {
                willBringPartner.setValueCoded(resolveIntegerConcept(client.getPostTestCouncel().getWill_bring_partners_for_testing()));
                willBringPartner.setPerson(p);
                willBringPartner.setUuid(UUID.randomUUID().toString());
                willBringPartner.setObsDatetime(new Date());
                willBringPartner.setEncounter(clientEncounter);
                allObs.add(willBringPartner);
            }

            Obs willOwnChildren = new Obs();
            willOwnChildren.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Will_bring_own_children_Less_5_years_for_HIV_testing));
            if (client.getPostTestCouncel().getWill_bring_own_children_less_than_5_years_old_for_testing() != null) {
                willOwnChildren.setValueCoded(resolveIntegerConcept(client.getPostTestCouncel().getWill_bring_own_children_less_than_5_years_old_for_testing()));
                willBringPartner.setPerson(p);
                willBringPartner.setUuid(UUID.randomUUID().toString());
                willBringPartner.setObsDatetime(new Date());
                willBringPartner.setEncounter(clientEncounter);
                allObs.add(willBringPartner);

            }

            Obs providedWithInfo = new Obs();
            providedWithInfo.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Provided_with_information_on_FP_and_dual_contraception));
            if (client.getPostTestCouncel().getProvided_with_information_on_fp_and_dual_contraception() != null) {
                providedWithInfo.setValueCoded(resolveIntegerConcept(client.getPostTestCouncel().getProvided_with_information_on_fp_and_dual_contraception()));
                providedWithInfo.setPerson(p);
                providedWithInfo.setUuid(UUID.randomUUID().toString());
                providedWithInfo.setObsDatetime(new Date());
                providedWithInfo.setEncounter(clientEncounter);
                allObs.add(providedWithInfo);
            }

            Obs clientPartnerFP = new Obs();
            clientPartnerFP.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Client_Partner_use_FP_methods));
            if (client.getPostTestCouncel().getClient_or_partner_use_fp_methods() != null) {
                clientPartnerFP.setValueCoded(resolveIntegerConcept(client.getPostTestCouncel().getClient_or_partner_use_fp_methods()));
                clientPartnerFP.setPerson(p);
                clientPartnerFP.setUuid(UUID.randomUUID().toString());
                clientPartnerFP.setObsDatetime(new Date());
                clientPartnerFP.setEncounter(clientEncounter);
                allObs.add(clientPartnerFP);
            }

            Obs clientPartnerFPCondoms = new Obs();
            clientPartnerFPCondoms.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Client_Partner_use_condoms_as_FP_method));
            if (client.getPostTestCouncel().getClient_or_partner_use_condom_as_one_fp_method() != null) {
                clientPartnerFPCondoms.setValueCoded(resolveIntegerConcept(client.getPostTestCouncel().getClient_or_partner_use_condom_as_one_fp_method()));
                clientPartnerFPCondoms.setPerson(p);
                clientPartnerFPCondoms.setUuid(UUID.randomUUID().toString());
                clientPartnerFPCondoms.setObsDatetime(new Date());
                clientPartnerFPCondoms.setEncounter(clientEncounter);
                allObs.add(clientPartnerFPCondoms);
            }

            Obs correctCondomsUse = new Obs();
            correctCondomsUse.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Correct_condom_use_demonstrated));
            if (client.getPostTestCouncel().getCorrect_condom_use_demonstrated() != null) {
                correctCondomsUse.setValueCoded(resolveIntegerConcept(client.getPostTestCouncel().getCorrect_condom_use_demonstrated()));
                correctCondomsUse.setPerson(p);
                correctCondomsUse.setUuid(UUID.randomUUID().toString());
                correctCondomsUse.setObsDatetime(new Date());
                correctCondomsUse.setEncounter(clientEncounter);
                allObs.add(correctCondomsUse);
            }

            Obs condomsProvidedClient = new Obs();
            condomsProvidedClient.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Condoms_provided_to_client));
            if (client.getPostTestCouncel().getCondoms_provided() != null) {
                condomsProvidedClient.setValueCoded(resolveIntegerConcept(client.getPostTestCouncel().getCondoms_provided()));
                condomsProvidedClient.setPerson(p);
                condomsProvidedClient.setUuid(UUID.randomUUID().toString());
                condomsProvidedClient.setObsDatetime(new Date());
                condomsProvidedClient.setEncounter(clientEncounter);
                allObs.add(condomsProvidedClient);
            }

            Obs clientRefferedOtherService = new Obs();
            clientRefferedOtherService.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.Client_referred_to_other_services));
            if (client.getPostTestCouncel().getClient_referred_to_other_services() != null) {
                clientRefferedOtherService.setValueCoded(resolveIntegerConcept(client.getPostTestCouncel().getClient_referred_to_other_services()));
                clientRefferedOtherService.setPerson(p);
                clientRefferedOtherService.setUuid(UUID.randomUUID().toString());
                clientRefferedOtherService.setObsDatetime(new Date());
                clientRefferedOtherService.setEncounter(clientEncounter);
                allObs.add(clientRefferedOtherService);
            }
        }

        //load all obs
        allObs.add(settingObs);
        allObs.add(firstTimeObs);
        allObs.add(refferedFromObs);

        clientEncounter.setObs(allObs);

        Context.getEncounterService().saveEncounter(clientEncounter);

        return ConstantsUtil.SUCCESS_MSG_CREATE_CONTACT;

    }
	
	public String createPatientAdultRiskStrat(Client client, Patient p) {

        Encounter clientEncounter = new Encounter();
        Visit clientVisit = new Visit(p, Context.getVisitService().getAllVisitTypes().get(0), new Date());

        clientEncounter.setVisit(clientVisit);
        clientEncounter.setLocation(Context.getUserContext().getLocation());
        clientEncounter.setPatient(p);
        clientEncounter.setEncounterDatetime(new Date());

        clientEncounter.setForm(Context.getFormService().getFormByUuid(ConstantsUtil.RISK_STRATIFICATION_ADULT_FORM_UUID));
        EncounterRole er = Context.getEncounterService().getEncounterRoleByUuid(EncounterRole.UNKNOWN_ENCOUNTER_ROLE_UUID);

        //UUID for super user
        clientEncounter.setProvider(er, Context.getProviderService().getProviderByUuid("f9badd80-ab76-11e2-9e96-0800200c9a66"));
        clientEncounter.setEncounterType(Context.getEncounterService().getEncounterType(Utils.Risk_Stratification_ADULT_Encounter));

        Set<Obs> allObs = new HashSet<>();

        AdultRiskStratification adultRiskStratification = client.getRiskStratificationNew().getAdultRiskStratification();

        Concept adultEverHadAnal = resolveIntegerConcept(adultRiskStratification.getRst_adult_ever_had_anal_or_vaginal_sex_without_condom());
        if (adultEverHadAnal != null) {
            Obs adultEverHadAnalObs = new Obs();
            adultEverHadAnalObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.RST_ADULT_EVER_HAD_ANAL_OR_VAGINAL_SEX_WITHOUT_CONDOM));
            adultEverHadAnalObs.setValueCoded(adultEverHadAnal);
            adultEverHadAnalObs.setPerson(p);
            adultEverHadAnalObs.setUuid(UUID.randomUUID().toString());
            adultEverHadAnalObs.setObsDatetime(new Date());
            adultEverHadAnalObs.setEncounter(clientEncounter);
            allObs.add(adultEverHadAnalObs);
        }

        Concept adultTBSymtoms = resolveIntegerConcept(adultRiskStratification.getRst_adult_have_tb_symptoms());
        if (adultTBSymtoms != null) {
            Obs adultTBSymtomsObs = new Obs();
            adultTBSymtomsObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.RST_ADULT_HAVE_TB_SYMPTOMS));
            adultTBSymtomsObs.setValueCoded(adultTBSymtoms);
            adultTBSymtomsObs.setPerson(p);
            adultTBSymtomsObs.setUuid(UUID.randomUUID().toString());
            adultTBSymtomsObs.setObsDatetime(new Date());
            adultTBSymtomsObs.setEncounter(clientEncounter);
            allObs.add(adultTBSymtomsObs);
        }

        Concept adultEverHadBloodTRans = resolveIntegerConcept(adultRiskStratification.getRst_adult_ever_had_blood_transfusion());
        if (adultEverHadBloodTRans != null) {
            Obs adultEverHadBloodTransObs = new Obs();
            adultEverHadBloodTransObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.RST_ADULT_EVER_HAD_BLOOD_TRANSFUSION));
            adultEverHadBloodTransObs.setValueCoded(Context.getConceptService().getConcept(ConceptIDMappings.RST_ADULT_EVER_HAD_BLOOD_TRANSFUSION));
            adultEverHadBloodTransObs.setPerson(p);
            adultEverHadBloodTransObs.setUuid(UUID.randomUUID().toString());
            adultEverHadBloodTransObs.setObsDatetime(new Date());
            adultEverHadBloodTransObs.setEncounter(clientEncounter);
            allObs.add(adultEverHadBloodTransObs);
        }

        Concept adultForceSex = resolveIntegerConcept(adultRiskStratification.getRst_adult_forced_sex());
        if (adultForceSex != null) {
            Obs adultForceSExObs = new Obs();
            adultForceSExObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.RST_ADULT_FORCED_SEX));
            adultForceSExObs.setValueCoded(adultForceSex);
            adultForceSExObs.setPerson(p);
            adultForceSExObs.setUuid(UUID.randomUUID().toString());
            adultForceSExObs.setObsDatetime(new Date());
            adultForceSExObs.setEncounter(clientEncounter);
            allObs.add(adultForceSExObs);
        }

        Concept adultMonetizeSex = resolveIntegerConcept(adultRiskStratification.getRst_adult_have_paid_for_or_sold_sex());
        if (adultMonetizeSex != null) {
            Obs adultMonetizeSexObs = new Obs();
            adultMonetizeSexObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.RST_ADULT_HAVE_PAID_FOR_OR_SOLD_SEX));
            adultMonetizeSexObs.setValueCoded(adultMonetizeSex);
            adultMonetizeSexObs.setPerson(p);
            adultMonetizeSexObs.setUuid(UUID.randomUUID().toString());
            adultMonetizeSexObs.setObsDatetime(new Date());
            adultMonetizeSexObs.setEncounter(clientEncounter);
            allObs.add(adultMonetizeSexObs);
        }

        Concept adultSharedSharpObjs = resolveIntegerConcept(adultRiskStratification.getRst_adult_have_shared_sharp_objects());
        if (adultSharedSharpObjs != null) {
            Obs adultSharedSharpObs = new Obs();
            adultSharedSharpObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.RST_ADULT_HAVE_SHARED_SHARP_OBJECTS));
            adultSharedSharpObs.setValueCoded(adultSharedSharpObjs);
            adultSharedSharpObs.setPerson(p);
            adultSharedSharpObs.setUuid(UUID.randomUUID().toString());
            adultSharedSharpObs.setObsDatetime(new Date());
            adultSharedSharpObs.setEncounter(clientEncounter);
            allObs.add(adultSharedSharpObs);
        }

        Concept adultSharedSTISymtoms = resolveIntegerConcept(adultRiskStratification.getRst_adult_have_sti_symptoms());
        if (adultSharedSTISymtoms != null) {
            Obs adultSharedSTISymtomsObs = new Obs();
            adultSharedSTISymtomsObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.RST_ADULT_HAVE_STI_SYMPTOMS));
            adultSharedSTISymtomsObs.setValueCoded(adultSharedSTISymtoms);
            adultSharedSTISymtomsObs.setPerson(p);
            adultSharedSTISymtomsObs.setUuid(UUID.randomUUID().toString());
            adultSharedSTISymtomsObs.setObsDatetime(new Date());
            adultSharedSTISymtomsObs.setEncounter(clientEncounter);
            allObs.add(adultSharedSTISymtomsObs);
        }

        Concept adultPhysicianRequest = resolveIntegerConcept(adultRiskStratification.getRst_adult_test_based_on_physician_request());
        if (adultPhysicianRequest != null) {
            Obs adultPhysicianRequestObs = new Obs();
            adultPhysicianRequestObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.RST_ADULT_TEST_BASED_ON_PHYSICIAN_REQUEST));
            adultPhysicianRequestObs.setValueCoded(adultPhysicianRequest);
            adultPhysicianRequestObs.setPerson(p);
            adultPhysicianRequestObs.setUuid(UUID.randomUUID().toString());
            adultPhysicianRequestObs.setObsDatetime(new Date());
            adultPhysicianRequestObs.setEncounter(clientEncounter);
            allObs.add(adultPhysicianRequestObs);
        }

        clientEncounter.setObs(allObs);

        Context.getEncounterService().saveEncounter(clientEncounter);

        return ConstantsUtil.SUCCESS_MSG_CREATE_CONTACT;

    }
	
	public String createPatientPedRiskStrat(Client client, Patient p) {

        Encounter clientEncounter = new Encounter();
        Visit clientVisit = new Visit(p, Context.getVisitService().getAllVisitTypes().get(0), new Date());

        clientEncounter.setVisit(clientVisit);
        clientEncounter.setLocation(Context.getUserContext().getLocation());
        clientEncounter.setPatient(p);
        clientEncounter.setEncounterDatetime(new Date());

        clientEncounter.setForm(Context.getFormService().getFormByUuid(ConstantsUtil.RISK_STRATIFICATION_PED_FORM_UUID));
        EncounterRole er = Context.getEncounterService().getEncounterRoleByUuid(EncounterRole.UNKNOWN_ENCOUNTER_ROLE_UUID);

        //UUID for super user
        clientEncounter.setProvider(er, Context.getProviderService().getProviderByUuid("f9badd80-ab76-11e2-9e96-0800200c9a66"));
        clientEncounter.setEncounterType(Context.getEncounterService().getEncounterType(Utils.Risk_Stratification_PED_Encounter));

        Set<Obs> allObs = new HashSet<>();

        //Risk Stratification    
        //Pediatrics
        PedRiskStratification pedRiskStrat = client.getRiskStratificationNew().getPedRiskStratification();

        Concept pedChildEverTested = resolveIntegerConcept(pedRiskStrat.getRst_ped_child_ever_tested_for_hiv());
        if (pedChildEverTested != null) {
            Obs pedChildTestObs = new Obs();
            pedChildTestObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.RST_PED_CHILD_EVER_TESTED_FOR_HIV));
            pedChildTestObs.setValueCoded(pedChildEverTested);
            pedChildTestObs.setPerson(p);
            pedChildTestObs.setUuid(UUID.randomUUID().toString());
            pedChildTestObs.setObsDatetime(new Date());
            pedChildTestObs.setEncounter(clientEncounter);

            allObs.add(pedChildTestObs);
        }

        Concept pedHadAnal = resolveIntegerConcept(pedRiskStrat.getRst_ped_ever_had_anal_or_vaginal_sex_without_condom());
        if (pedHadAnal != null) {
            Obs pedHadAnalObs = new Obs();
            pedHadAnalObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.RST_PED_EVER_HAD_ANAL_OR_VAGINAL_SEX_WITHOUT_CONDOM));
            pedHadAnalObs.setValueCoded(pedHadAnal);
            pedHadAnalObs.setPerson(p);
            pedHadAnalObs.setUuid(UUID.randomUUID().toString());
            pedHadAnalObs.setObsDatetime(new Date());
            pedHadAnalObs.setEncounter(clientEncounter);
            allObs.add(pedHadAnalObs);
        }

        Concept pedForceSex = resolveIntegerConcept(pedRiskStrat.getRst_ped_forced_sex());
        if (pedForceSex != null) {
            Obs pedForceSexObs = new Obs();
            pedForceSexObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.RST_PED_FORCED_SEX));
            pedForceSexObs.setValueCoded(pedForceSex);
            pedForceSexObs.setPerson(p);
            pedForceSexObs.setUuid(UUID.randomUUID().toString());
            pedForceSexObs.setObsDatetime(new Date());
            pedForceSexObs.setEncounter(clientEncounter);
            allObs.add(pedForceSexObs);
        }

        Concept pedLiveWithTB = resolveIntegerConcept(pedRiskStrat.getRst_ped_living_with_tb_diagnosed_person_or_tb_symptoms());
        if (pedLiveWithTB != null) {
            Obs pedLiveWithTBObs = new Obs();
            pedLiveWithTBObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.RST_PED_LIVING_WITH_TB_DIAGNOSED_PERSON_OR_TB_SYMPTOMS));
            pedLiveWithTBObs.setValueCoded(pedLiveWithTB);
            pedLiveWithTBObs.setPerson(p);
            pedLiveWithTBObs.setUuid(UUID.randomUUID().toString());
            pedLiveWithTBObs.setObsDatetime(new Date());
            pedLiveWithTBObs.setEncounter(clientEncounter);
            allObs.add(pedLiveWithTBObs);
        }

        Concept pedMotherDeceased = resolveIntegerConcept(pedRiskStrat.getRst_ped_mother_positive_or_member_deceased());
        if (pedMotherDeceased != null) {
            Obs pedMotherDeceasedObs = new Obs();
            pedMotherDeceasedObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.RST_PED_MOTHER_POSITIVE_OR_MEMBER_DECEASED));
            pedMotherDeceasedObs.setValueCoded(pedMotherDeceased);
            pedMotherDeceasedObs.setPerson(p);
            pedMotherDeceasedObs.setUuid(UUID.randomUUID().toString());
            pedMotherDeceasedObs.setObsDatetime(new Date());
            pedMotherDeceasedObs.setEncounter(clientEncounter);
            allObs.add(pedMotherDeceasedObs);
        }

        Concept pedPoorHealth = resolveIntegerConcept(pedRiskStrat.getRst_ped_poor_health_in_last_six_months());
        if (pedPoorHealth != null) {
            Obs pedPoorHealthObs = new Obs();
            pedPoorHealthObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.RST_PED_POOR_HEALTH_IN_LAST_SIX_MONTHS));
            pedPoorHealthObs.setValueCoded(pedPoorHealth);
            pedPoorHealthObs.setPerson(p);
            pedPoorHealthObs.setUuid(UUID.randomUUID().toString());
            pedPoorHealthObs.setObsDatetime(new Date());
            pedPoorHealthObs.setEncounter(clientEncounter);
            allObs.add(pedPoorHealthObs);

        }

        Concept pedSkinProbs = resolveIntegerConcept(pedRiskStrat.getRst_ped_skin_problems_in_last_six_months());
        if (pedSkinProbs != null) {
            Obs pedSkinProbsObs = new Obs();
            pedSkinProbsObs.setConcept(Context.getConceptService().getConcept(ConceptIDMappings.RST_PED_SKIN_PROBLEMS_IN_LAST_SIX_MONTHS));
            pedSkinProbsObs.setValueCoded(pedSkinProbs);
            pedSkinProbsObs.setPerson(p);
            pedSkinProbsObs.setUuid(UUID.randomUUID().toString());
            pedSkinProbsObs.setObsDatetime(new Date());
            pedSkinProbsObs.setEncounter(clientEncounter);
            allObs.add(pedSkinProbsObs);
        }

        clientEncounter.setObs(allObs);

        Context.getEncounterService().saveEncounter(clientEncounter);

        return ConstantsUtil.SUCCESS_MSG_CREATE_CONTACT;

    }
	
	public Client createDummyClient() {
		Client c = new Client();
		c.setClientIdentifier("YU65MN5");
		c.setAge("20");
		c.setAcceptedPartnerNotification("Yes");
		c.setAddress("78 Sirakoro street");
		c.setClientLga("Bwari");
		c.setClientState("FCT");
		c.setClientVillage("kubwa village");
		c.setFirstName("Kelly");
		c.setHospitalNumber("H7629K9");
		c.setIndexClient("No");
		c.setMaritalStatus("Divorced");
		c.setPhoneNumber("08023987620");
		c.setSex("M");
		c.setSessionType("Individual");
		c.setSurname("Rowland");
		
		PreTestCouncel preTest = new PreTestCouncel();
		
		KnowledgeAssesement knowA = new KnowledgeAssesement();
		knowA.setKnowledge_assessment_informed_about_possible_test_results(1);
		knowA.setKnowledge_assessment_informed_about_risk_factors(1);
		knowA.setKnowledge_assessment_informed_about_transmission_routes(1);
		knowA.setKnowledge_assessment_informed_consent_for_testing_given(1);
		knowA.setKnowledge_assessment_informed_on_preventing_transmission_method(0);
		knowA.setKnowledge_assessment_pregnant(1);
		
		RiskAssessment riskA = new RiskAssessment();
		riskA.setRisk_assessment_blood_transfussion_in_last_3_month(1);
		riskA.setRisk_assessment_ever_had_sexual_intercourse(0);
		riskA.setRisk_assessment_more_than_1_sex_partner_in_last_3_months(1);
		riskA.setRisk_assessment_sti_in_last_3_months(1);
		riskA.setRisk_assessment_unprotected_sex_with_casual_partner_in_last_3_months(1);
		riskA.setRisk_assessment_unprotected_sex_with_casual_partner_in_last_3_months(1);
		riskA.setRisk_assessment_unprotected_sex_with_regular_partner_in_last_3_months(1);
		
		preTest.setRisk_assessment(riskA);
		
		STIScreening sTIScreening = new STIScreening();
		sTIScreening.setSti_screening_complain_of_genital_sore(1);
		sTIScreening.setSti_screening_complain_of_lower_abominal_pain(0);
		sTIScreening.setSti_screening_complain_of_scrotal_swelling(1);
		sTIScreening.setSti_screening_complain_of_urethral_discharge(0);
		sTIScreening.setSti_screening_complain_of_vaginal_discharge(1);
		
		preTest.setSti_screening(sTIScreening);
		
		TbScreening tbScreening = new TbScreening();
		tbScreening.setTb_screening_current_cough(1);
		tbScreening.setTb_screening_fever(0);
		tbScreening.setTb_screening_night_sweats(0);
		tbScreening.setTb_screening_weight_loss(1);
		
		preTest.setTb_screening(tbScreening);
		
		preTest.setKnowledge_assessment(knowA);
		c.setPreTestCouncel(preTest);
		
		PostTestCouncel postTest = new PostTestCouncel();
		postTest.setClient_or_partner_use_condom_as_one_fp_method(1);
		postTest.setClient_or_partner_use_fp_methods(0);
		postTest.setClient_received_hiv_result(0);
		postTest.setClient_referred_to_other_services(1);
		postTest.setCondoms_provided(1);
		postTest.setCorrect_condom_use_demonstrated(1);
		postTest.setPostTestedBefore(ConceptValuesMappingUtil.PREV_TESTED_NOT_PREVIOUSLY_TESTED.getValue());
		postTest.setPost_test_councelling_done(0);
		postTest.setPost_test_disclosure_plan_developed(1);
		postTest.setProvided_with_information_on_fp_and_dual_contraception(1);
		postTest.setRequest_and_result_form_filled_with_ct_intake_form(0);
		postTest.setRequest_and_result_form_signed_by_testers(1);
		postTest.setRisk_reduction_plan_developed(1);
		postTest.setWill_bring_own_children_less_than_5_years_old_for_testing(1);
		postTest.setWill_bring_partners_for_testing(1);
		
		c.setPostTestCouncel(postTest);
		
		return c;
		
	}
	
	private Concept resolveTerm(String selectedValue) {
		if (selectedValue == null) {
			return null;
		}
		
		if (selectedValue.equalsIgnoreCase("Recent")) {
			return concept_Recent_Term;
		} else if (selectedValue.equalsIgnoreCase("Long Term")) {
			return concept_Long_Term;
		}
		
		return null;
		
	}
	
	private Boolean resolveBoolean(Integer intValue) {
		if (intValue == 1) {
			return Boolean.TRUE;
		} else if (intValue == 0) {
			return Boolean.FALSE;
		}
		
		return null;
	}
	
	private Concept resolveBooleanConcept(Boolean booleanVal) {
		if (booleanVal == null) {
			return null;
		}
		
		if (booleanVal == true) {
			return concept_YES;
		} else if (booleanVal == false) {
			return concept_NO;
		}
		
		return null;
	}
	
	private Concept resolveIntegerConcept(Integer integerVal) {
		if (integerVal == null) {
			return null;
		}
		
		if (integerVal == 1) {
			return concept_YES;
		} else if (integerVal == 0) {
			return concept_NO;
		}
		
		return null;
	}
	
	private Concept resolveHivTestResult(String selectedValue) {
		if (selectedValue == null) {
			return null;
		}
		
		if (selectedValue.equals(ConceptValuesMappingUtil.HIV_TEST_RESULT_DEFAULT.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.HIV_TEST_RESULT_DEFAULT.getKey());
		} else if (selectedValue.equals(ConceptValuesMappingUtil.HIV_TEST_RESULT_NEGATIVE.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.HIV_TEST_RESULT_NEGATIVE.getKey());
		} else if (selectedValue.equals(ConceptValuesMappingUtil.HIV_TEST_RESULT_POSITIVE.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.HIV_TEST_RESULT_POSITIVE.getKey());
		}
		
		return null;
		
	}
	
	private Concept resolveHivFinalTestResult(String selectedValue) {
		if (selectedValue == null) {
			return null;
		}
		
		if (selectedValue.equals(ConceptValuesMappingUtil.HIV_FINAL_TEST_RESULT_NEGATIVE.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.HIV_FINAL_TEST_RESULT_NEGATIVE.getKey());
		} else if (selectedValue.equals(ConceptValuesMappingUtil.HIV_FINAL_TEST_RESULT_POSITIVE.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.HIV_FINAL_TEST_RESULT_POSITIVE.getKey());
		}
		
		return null;
		
	}
	
	private Concept resolveHivTestingSetting(String selectedValue) {
		
		if (selectedValue == null) {
			return null;
		}
		if (selectedValue.equalsIgnoreCase(ConceptValuesMappingUtil.SETTING_COMMUNITY.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.SETTING_COMMUNITY.getKey());
		} else if (selectedValue.equalsIgnoreCase(ConceptValuesMappingUtil.SETTING_FP.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.SETTING_FP.getKey());
		} else if (selectedValue.equalsIgnoreCase(ConceptValuesMappingUtil.SETTING_OPD.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.SETTING_OPD.getKey());
		} else if (selectedValue.equalsIgnoreCase(ConceptValuesMappingUtil.SETTING_OTHERS.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.SETTING_OTHERS.getKey());
		} else if (selectedValue.equalsIgnoreCase(ConceptValuesMappingUtil.SETTING_OUTREACH.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.SETTING_OUTREACH.getKey());
		} else if (selectedValue.equalsIgnoreCase(ConceptValuesMappingUtil.SETTING_STANDALONE.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.SETTING_STANDALONE.getKey());
		} else if (selectedValue.equalsIgnoreCase(ConceptValuesMappingUtil.SETTING_STI.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.SETTING_STI.getKey());
		} else if (selectedValue.equalsIgnoreCase(ConceptValuesMappingUtil.SETTING_TB.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.SETTING_TB.getKey());
		} else if (selectedValue.equalsIgnoreCase(ConceptValuesMappingUtil.SETTING_VCT.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.SETTING_VCT.getKey());
		} else if (selectedValue.equalsIgnoreCase(ConceptValuesMappingUtil.SETTING_WARD.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.SETTING_WARD.getKey());
		}
		
		return null;
		
	}
	
	private Concept resolveSessionConcept(String selectedValue) {
		
		if (selectedValue == null) {
			return null;
		}
		
		if (selectedValue.equals(ConceptValuesMappingUtil.SESSION_INDIVIDUAL.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.SESSION_INDIVIDUAL.getKey());
		} else if (selectedValue.equals(ConceptValuesMappingUtil.SESSION_COUPLE.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.SESSION_COUPLE.getKey());
		} else if (selectedValue.equals(ConceptValuesMappingUtil.SESSION_PREVIOUSLY_SELF_TESTED.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.SESSION_PREVIOUSLY_SELF_TESTED.getKey());
		}
		return null;
	}
	
	private Concept resolveMaritalStatus(String selectedValue) {
		
		if (selectedValue == null) {
			return null;
		}
		
		if (selectedValue.equals(ConceptValuesMappingUtil.MARITAL_STATUS_DIVORCED.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.MARITAL_STATUS_DIVORCED.getKey());
		} else if (selectedValue.equals(ConceptValuesMappingUtil.MARITAL_STATUS_LIVING_PARTNER.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.MARITAL_STATUS_LIVING_PARTNER.getKey());
		} else if (selectedValue.equals(ConceptValuesMappingUtil.MARITAL_STATUS_MARRIED.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.MARITAL_STATUS_MARRIED.getKey());
		} else if (selectedValue.equals(ConceptValuesMappingUtil.MARITAL_STATUS_NEVER_MARRIED.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.MARITAL_STATUS_NEVER_MARRIED.getKey());
		} else if (selectedValue.equals(ConceptValuesMappingUtil.MARITAL_STATUS_SEPARATED.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.MARITAL_STATUS_SEPARATED.getKey());
		} else if (selectedValue.equals(ConceptValuesMappingUtil.MARITAL_STATUS_SEPARATED.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.MARITAL_STATUS_SEPARATED.getKey());
		} else if (selectedValue.equals(ConceptValuesMappingUtil.MARITAL_STATUS_WIDOWED.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.MARITAL_STATUS_WIDOWED.getKey());
		}
		
		return null;
		
	}
	
	private Concept resolveClientIdentifiedIndex(String selectedValue) {
		
		if (selectedValue == null) {
			return null;
		}
		
		if (selectedValue.equals(ConceptValuesMappingUtil.INDEX_CLIENT_IDENTIFIED_YES.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.INDEX_CLIENT_IDENTIFIED_YES.getKey());
		} else if (selectedValue.equals(ConceptValuesMappingUtil.INDEX_CLIENT_IDENTIFIED_NO.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.INDEX_CLIENT_IDENTIFIED_NO.getKey());
		}
		
		return null;
		
	}
	
	private Concept resolveIndexType(String selectedValue) {
		if (selectedValue == null) {
			return null;
		}
		
		if (selectedValue.equals(ConceptValuesMappingUtil.INDEX_TYPE_BIOLOGICAL.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.INDEX_TYPE_BIOLOGICAL.getKey());
		} else if (selectedValue.equals(ConceptValuesMappingUtil.INDEX_TYPE_SEXUAL.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.INDEX_TYPE_SEXUAL.getKey());
		} else if (selectedValue.equals(ConceptValuesMappingUtil.INDEX_TYPE_SOCIAL.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.INDEX_TYPE_SOCIAL.getKey());
		}
		
		return null;
		
	}
	
	private Concept resolvePreviouslyTested(String selectedValue) {
		if (selectedValue == null) {
			return null;
		}
		
		if (selectedValue.equals(ConceptValuesMappingUtil.PREV_TESTED_NOT_PREVIOUSLY_TESTED.getValue())) {
			return Context.getConceptService().getConcept(
			    ConceptValuesMappingUtil.PREV_TESTED_NOT_PREVIOUSLY_TESTED.getKey());
		} else if (selectedValue.equals(ConceptValuesMappingUtil.PREV_TESTED_PREV_NEGATIVE.getValue())) {
			return Context.getConceptService().getConcept(ConceptValuesMappingUtil.PREV_TESTED_PREV_NEGATIVE.getKey());
		} else if (selectedValue.equals(ConceptValuesMappingUtil.PREV_TESTED_PREV_POSITIVE_IN_CARE.getValue())) {
			return Context.getConceptService().getConcept(
			    ConceptValuesMappingUtil.PREV_TESTED_PREV_POSITIVE_IN_CARE.getKey());
		} else if (selectedValue.equals(ConceptValuesMappingUtil.PREV_TESTED_PREV_POSITIVE_IN_NOT_CARE.getValue())) {
			return Context.getConceptService().getConcept(
			    ConceptValuesMappingUtil.PREV_TESTED_PREV_POSITIVE_IN_NOT_CARE.getKey());
		}
		
		return null;
		
	}
	
}
