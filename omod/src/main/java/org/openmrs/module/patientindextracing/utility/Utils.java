package org.openmrs.module.patientindextracing.utility;

import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.codehaus.jackson.map.ObjectMapper;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientindextracing.omodmodels.DBConnection;
import org.openmrs.module.patientindextracing.omodmodels.Version;

import org.openmrs.util.OpenmrsUtil;

public class Utils {
	
	public final static int HIV_Enrollment_Encounter_Type_Id = 14;
	
	public final static int Pharmacy_Encounter_Type_Id = 13;
	
	public final static int Laboratory_Encounter_Type_Id = 11;
	
	public final static int Care_card_Encounter_Type_Id = 12;
	
	public final static int Adult_Ped_Initial_Encounter_Type_Id = 8;
	
	public final static int Client_Tracking_And_Termination_Encounter_Type_Id = 15;
	
	public final static int Client_Intake_Form_Encounter_Type_Id = 20;
	
	public final static int Patient_PEPFAR_Id = 3;
	
	public final static int Patient_Hospital_Id = 3;
	
	public final static int Patient_RNLSerial_No = 3;
	
	public final static int Reason_For_Termination = 165470;
	
	public final static int Antenatal_Registration_Encounter_Type_Id = 10;
	
	public final static int Delivery_Encounter_Type_Id = 16;
	
	public final static int Child_Birth_Detail_Encounter_Type_Id = 9;
	
	public final static int Child_Followup_Encounter_Type_Id = 18;
	
	public final static int Partner_register_Encounter_Id = 19;//Check this data from the database when there is record
	
	public final static int Admission_Simple_Client_intake = 2;
	
	public final static int Risk_Stratification_PED_Encounter = 33;
	
	public final static int Risk_Stratification_ADULT_Encounter = 34;
	
	public static String getMacAddress() {
		
		// temp for NELSON
		return "ISMAILTEST";
		//		InetAddress ip;
		//		try {
		//			
		//			ip = InetAddress.getLocalHost();
		//			//System.out.println("Current IP address : " + ip.getHostAddress());
		//			
		//			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
		//			
		//			byte[] mac = network.getHardwareAddress();
		//			
		//			//System.out.print("Current MAC address : ");
		//			StringBuilder sb = new StringBuilder();
		//			for (int i = 0; i < mac.length; i++) {
		//				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
		//			}
		//			
		//			//System.out.println(sb.toString());
		//			return sb.toString();
		//			
		//		}
		//		catch (UnknownHostException e) {
		//			
		//			e.printStackTrace();
		//			
		//		}
		//		catch (SocketException e) {
		//			
		//			e.printStackTrace();
		//			
		//		}
		//		
		//		return null;
		
	}
	
	public static String getLoggedInUser() {
		return Context.getAuthenticatedUser().toString();
	}
	
	public static String getFacilityName() {
		return Context.getAdministrationService().getGlobalProperty("Facility_Name");
	}
	
	public static String getFacilityLocalId() {
		return Context.getAdministrationService().getGlobalProperty("facility_local_id");
	}
	
	public static String getNigeriaQualId() {
		return Context.getAdministrationService().getGlobalProperty("nigeria_qual_id");
	}
	
	public static String getFacilityDATIMId() {
		return Context.getAdministrationService().getGlobalProperty("facility_datim_code");
	}
	
	public static String getIPFullName() {
		return Context.getAdministrationService().getGlobalProperty("partner_full_name");
	}
	
	public static String getIPShortName() {
		return Context.getAdministrationService().getGlobalProperty("partner_short_name");
	}
	
	//date is always saved as yyyy-MM-dd
	public static Date getLastNDRDate() {
		String lastRunDateString = Context.getAdministrationService().getGlobalProperty("ndr_last_run_date");
		if (String.valueOf(lastRunDateString).isEmpty()) {
			return null;
		} else {
			try {
				return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(lastRunDateString);
			}
			catch (Exception e) {
				System.out.println("Last Date was not in the correct format");
				return null;
			}
		}
	}
	
	public static String getBiometricServer() {
		return Context.getAdministrationService().getGlobalProperty("biometric_server");
	}
	
	public static Version getVersionInfo() throws URISyntaxException, IOException {
		
		File f = new File(Utils.class.getClassLoader().getResource("version.json").getFile());
		
		ObjectMapper mapper = new ObjectMapper();
		Version versionModel = mapper.readValue(f, Version.class);
		
		return versionModel;
	}
	
	public static Version getNmrsVersion() {
		
		URL resource = Utils.class.getClassLoader().getResource("version.json");
		try {
			File filePath = Paths.get(resource.toURI()).toFile();
			ObjectMapper mapper = new ObjectMapper();
			Version versionModel = mapper.readValue(filePath, Version.class);
			return versionModel;
		}
		catch (Exception e) {
			
		}
		
		return null;
	}
	
	public static void updateLast_NDR_Run_Date(Date date) {
		String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
		Context.getAdministrationService().updateGlobalProperty("ndr_last_run_date", dateString);
	}
	
	public static String getVisitId(Patient pts, Encounter enc) {
		return enc.getEncounterId().toString(); //getVisit().getVisitId().toString();
		/*String dateString = new SimpleDateFormat("dd-MM-yyyy").format(enc.getEncounterDatetime());
		return pts.getPatientIdentifier(3).getIdentifier() + "-" + dateString;*/
	}
	
	public static Obs extractObs(int conceptID, List<Obs> obsList) {

        if (obsList == null) {
            return null;
        }
        return obsList.stream().filter(ele -> ele.getConcept().getConceptId() == conceptID).findFirst().orElse(null);
    }
	
	public static Obs extractObsByValues(int conceptID, List<Obs> obsList) {

        if (obsList == null) {
            return null;
        }
        return obsList.stream().filter(ele -> ele.getValueCoded().getId() == conceptID).findFirst().orElse(null);
    }
	
	public static Encounter getLastEncounter(Patient patient) {

        List<Encounter> hivEnrollmentEncounter = Context.getEncounterService()
                .getEncountersByPatient(patient);

        //sort the list by date
        hivEnrollmentEncounter.sort(Comparator.comparing(Encounter::getEncounterDatetime));
        int size = hivEnrollmentEncounter.size();
        return hivEnrollmentEncounter.get(size - 1);
    }
	
	public static List<Obs> getCareCardObs(Patient patient, Date endDate) {

        List<Encounter> hivEnrollmentEncounter = Context.getEncounterService()
                .getEncountersByPatient(patient).stream()
                .filter(x -> x.getEncounterDatetime().before(endDate) && x.getEncounterType().getEncounterTypeId() == Care_card_Encounter_Type_Id)
                .sorted(Comparator.comparing(Encounter::getEncounterDatetime))
                .collect(Collectors.toList());

        if (hivEnrollmentEncounter != null && hivEnrollmentEncounter.size() > 0) {
            int lastIndex = hivEnrollmentEncounter.size() - 1;
            return new ArrayList<>(hivEnrollmentEncounter.get(lastIndex).getAllObs(false));
        } else {
            return null;
        }
    }
	
	public static List<Obs> getHIVEnrollmentObs(Patient patient) {
        Optional<Encounter> hivEnrollmentEncounter = Context.getEncounterService()
                .getEncountersByPatient(patient).stream()
                .filter(x -> x.getEncounterType().getEncounterTypeId() == HIV_Enrollment_Encounter_Type_Id)
                .findAny();
        if (hivEnrollmentEncounter != null && hivEnrollmentEncounter.isPresent()) {
            return new ArrayList<>(hivEnrollmentEncounter.get().getAllObs(false));
        }
        return null;

    }
	
	public static XMLGregorianCalendar getXmlDate(Date date) throws DatatypeConfigurationException {
		XMLGregorianCalendar cal = null;
		if (date != null) {
			cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd").format(date));
		}
		return cal;
	}
	
	public static XMLGregorianCalendar getXmlDateTime(Date date) throws DatatypeConfigurationException {
		XMLGregorianCalendar cal = null;
		if (date != null) {
			cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
			    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date));
		}
		return cal;
	}
	
	public static String formatDate(Date date) {
		return formatDate(date, "dd/MM/yyyy");
	}
	
	public static String formatDate(Date date, String format) {
		String dateString = "";
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(format);
			dateString = df.format(date);
		}
		return dateString;
	}
	
	public static String getPatientPEPFARId(Patient patient) {
		
		PatientIdentifier patientId = patient.getPatientIdentifier(Patient_PEPFAR_Id);
		
		if (patientId != null) {
			return patientId.getIdentifier();
		} else {
			return patient.getPatientIdentifier(4).getIdentifier();
		}
	}
	
	public static String getPatientHospitalNo(Patient patient) {
		
		PatientIdentifier patientId = patient.getPatientIdentifier(ConstantsUtil.HOSPITAL_IDENTIFIER_INDEX);
		
		if (patientId != null) {
			return patientId.getIdentifier();
		} else {
			return "";
		}
	}
	
	public static String getPatientRNLSerialNo(Patient patient) {
		
		PatientIdentifier patientId = patient.getPatientIdentifier(Patient_RNLSerial_No);
		if (patientId != null) {
			return patientId.getIdentifier();
		} else {
			return "";
		}
	}
	
	/*public String ensureTodayDownloadFolderExist(String parentFolder, HttpServletRequest request) {
		//create today's folder
		String dateString = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		String todayFolders = parentFolder + "/" + dateString;
		File dir = new File(todayFolders);
		Boolean b = dir.mkdir();
		System.out.println("creating folder : " + todayFolders + "was successful : " + b);
		return todayFolders;
	}*/
	public static void ShowSystemProps() {
		System.getProperties().list(System.out);
	}
	
	public static DBConnection getNmrsConnectionDetails() {
		
		DBConnection result = new DBConnection();
		
		try {
			
			//            // throw new FileNotFoundException("property file '" + appDirectory + "' not found in the classpath");
			//starts here
			Properties props = new Properties();
			props = OpenmrsUtil.getRuntimeProperties("openmrs");
			if (props == null) {
				props = OpenmrsUtil.getRuntimeProperties("openmrs-standalone");
				
			}
			
			result.setUsername(props.getProperty("connection.username"));
			result.setPassword(props.getProperty("connection.password"));
			result.setUrl(props.getProperty("connection.url"));
			
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			//LoggerUtils.write(Utils.class.getName(), ex.getMessage(), LogFormat.FATAL, LogLevel.live);
		}
		
		return result;
		
	}
	
	public static String isSurgeSite() {
		String isSurge = "";
		try {
			isSurge = Context.getAdministrationService().getGlobalProperty("surge_site");
		}
		catch (Exception e) {
			isSurge = "";
		}
		return isSurge;
	}
}
