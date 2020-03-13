/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.dbmanager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.openmrs.module.patientindextracing.omodmodels.DBConnection;
import org.openmrs.module.patientindextracing.omodmodels.PatientContactsModel;
import org.openmrs.module.patientindextracing.omodmodels.TesterModel;
import org.openmrs.module.patientindextracing.omodmodels.TokenStore;
import org.openmrs.module.patientindextracing.omodmodels.UserModel;
import org.openmrs.module.patientindextracing.utility.ConstantsUtil;
import org.openmrs.module.patientindextracing.utility.Utils;

/**
 * @author MORRISON.I
 */
public class NdrDBManager {
	
	Connection conn = null;
	
	PreparedStatement pStatement = null;
	
	private ResultSet resultSet = null;
	
	public NdrDBManager() {
		
	}
	
	public void openConnection() throws SQLException {
		DBConnection openmrsConn = Utils.getNmrsConnectionDetails();
		
		conn = DriverManager.getConnection(openmrsConn.getUrl(), openmrsConn.getUsername(), openmrsConn.getPassword());
	}
	
	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
			if (pStatement != null) {
				pStatement.close();
			}
		}
		catch (Exception ex) {
			
		}
	}
	
	public int createCommunityTester(TesterModel model) throws SQLException {
		pStatement = conn.prepareStatement("insert into " + ConstantsUtil.COMMUNITY_TESTER_TABLE
		        + "(username, email, phone_number, assign_facilityId, facility_name, facility_code, state, lga, "
		        + "lga_code, date_created, created_by,community_tester_guid)Values(?,?,?,?,?,?,?,?,?,NOW(),?,?)");
		pStatement.setString(1, model.getUsername());
		pStatement.setString(2, model.getEmail());
		pStatement.setString(3, model.getPhone_number());
		pStatement.setString(4, model.getAssign_facilityId());
		pStatement.setString(5, model.getFacility_name());
		pStatement.setString(6, model.getFacility_code());
		pStatement.setString(7, model.getState());
		pStatement.setString(8, model.getLga());
		pStatement.setString(9, model.getLga_code());
		// pStatement.setDate(10, (Date) model.getDate_created());
		pStatement.setString(10, model.getCreated_by());
		pStatement.setString(11, model.getCommunity_tester_guid());
		
		return pStatement.executeUpdate();
	}
	
	public int updateCommunityTester(TesterModel model) throws SQLException {
		
		pStatement = conn.prepareStatement("insert into " + ConstantsUtil.COMMUNITY_TESTER_TABLE
		        + "(username, email, phone_number, assign_facilityId, facility_name, facility_code, state, lga, "
		        + "lga_code, date_created, created_by,community_tester_guid)Values(?,?,?,?,?,?,?,?,?,NOW(),?,?) "
		        + "ON DUPLICATE KEY UPDATE email = ?,phone_number = ?, "
		        + "assign_facilityId = ?, facility_name = ?, facility_code = ?, state = ?," + " lga=?,lga_code=?  ");
		
		pStatement.setString(1, model.getUsername());
		pStatement.setString(2, model.getEmail());
		pStatement.setString(3, model.getPhone_number());
		pStatement.setString(4, model.getAssign_facilityId());
		pStatement.setString(5, model.getFacility_name());
		pStatement.setString(6, model.getFacility_code());
		pStatement.setString(7, model.getState());
		pStatement.setString(8, model.getLga());
		pStatement.setString(9, model.getLga_code());
		// pStatement.setDate(10, (Date) model.getDate_created());
		pStatement.setString(10, model.getCreated_by());
		pStatement.setString(11, model.getCommunity_tester_guid());
		
		//update fields
		pStatement.setString(12, model.getEmail());
		pStatement.setString(13, model.getPhone_number());
		pStatement.setString(14, model.getAssign_facilityId());
		pStatement.setString(15, model.getFacility_name());
		pStatement.setString(16, model.getFacility_code());
		pStatement.setString(17, model.getState());
		pStatement.setString(18, model.getLga());
		pStatement.setString(19, model.getLga_code());
		
		return pStatement.executeUpdate();
		
	}
	
	public List<TesterModel> getCommunityTesters() throws SQLException {
		
		pStatement = conn.prepareStatement("select * from " + ConstantsUtil.COMMUNITY_TESTER_TABLE);
		resultSet = pStatement.executeQuery();
		
		return convertResultsetToCommunityTesters(resultSet);
		
	}
	
	public void deleteAllCommunityTesters() throws SQLException {
		pStatement = conn.prepareStatement("DELETE FROM community_testers");
		pStatement.execute();
	}
	
	public List<String> getAssignedDeletedTesters() throws SQLException {
        pStatement = conn.prepareCall("select community_tester_guid from " + ConstantsUtil.PATIENT_CONTACT_TABLE + " where "
                + "community_tester_guid not in (select community_tester_guid from " + ConstantsUtil.COMMUNITY_TESTER_TABLE + ")");

        ResultSet rs = pStatement.executeQuery();
        List<String> testersUUID = new ArrayList<>();

        while (rs.next()) {
            testersUUID.add(rs.getString("community_tester_guid"));
        }

        return testersUUID;
    }
	
	public int removeContactAssignment(String testersId) throws SQLException {
		pStatement = conn
		        .prepareCall("update patient_contacts set assign_contact_to_cec =null, "
		                + "community_tester_guid = null, community_tester_name = null, community_tester_guid = null where community_tester_guid = ? and trace_status = ? ");
		
		//	String testeridStr = prepareString(testersId);
		pStatement.setString(1, testersId);
		pStatement.setString(2, ConstantsUtil.TRACE_PENDING_STATUS);
		
		return pStatement.executeUpdate();
	}
	
	private static String prepareString(List<String> lists) {
		StringBuilder sb = new StringBuilder();
		
		int b = 0;
		sb.append("(");
		for (String a : lists) {
			b++;
			sb.append("'");
			sb.append(a);
			sb.append("'");
			if ((lists.size()) != b) {
				sb.append(",");
			}
			
		}
		sb.append(")");
		return sb.toString();
		
	}
	
	private List<TesterModel> convertResultsetToCommunityTesters(ResultSet resultSet) throws SQLException {
        List<TesterModel> testers = new ArrayList<>();

        while (resultSet.next()) {
            TesterModel model = new TesterModel();

            model.setAssign_facilityId(resultSet.getString("assign_facilityId"));
            model.setCreated_by(resultSet.getString("created_by"));
            model.setDate_created(resultSet.getDate("date_created"));
            model.setDate_modified(resultSet.getDate("date_modified"));
            model.setEmail(resultSet.getString("email"));
            model.setFacility_code(resultSet.getString("facility_code"));
            model.setFacility_name(resultSet.getString("facility_name"));
            model.setId(resultSet.getInt("id"));
            model.setLga(resultSet.getString("lga"));
            model.setLga_code(resultSet.getString("lga_code"));
            model.setModified_by(resultSet.getString("modified_by"));
            model.setPhone_number(resultSet.getString("phone_number"));
            model.setState(resultSet.getString("state"));
            model.setUsername(resultSet.getString("username"));
            model.setCommunity_tester_guid(resultSet.getString("community_tester_guid"));

            testers.add(model);
        }

        return testers;

    }
	
	public int createPatientContacts(PatientContactsModel model) throws SQLException {
		
		pStatement = conn
		        .prepareStatement("insert into "
		                + ConstantsUtil.PATIENT_CONTACT_TABLE
		                + "(uuid, index_patient_id, relationship, age, sex, "
		                + "preferred_testing_location, state, lga, town, "
		                + "village, physically_abused, forced_sexually, fear_their_partner, notification_method,"
		                + " more_information, assign_contact_to_cec, community_tester_name, "
		                + "created_by,code,datim_code,community_tester_guid,trace_status,country,firstname,lastname,phone_number,date_created)"
		                + "Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())");
		
		pStatement.setString(1, UUID.randomUUID().toString());
		pStatement.setInt(2, model.getIndex_patient_id());
		pStatement.setString(3, model.getRelationship());
		pStatement.setInt(4, model.getAge());
		pStatement.setString(5, model.getSex());
		pStatement.setString(6, model.getPreferred_testing_location());
		pStatement.setString(7, model.getState());
		pStatement.setString(8, model.getLga());
		pStatement.setString(9, model.getTown());
		pStatement.setString(10, model.getVillage());
		pStatement.setString(11, model.getPhysically_abused());
		pStatement.setString(12, model.getForced_sexually());
		pStatement.setString(13, model.getFear_their_partner());
		pStatement.setString(14, model.getNotification_method());
		pStatement.setString(15, model.getMore_information());
		pStatement.setInt(16, model.getAssign_contact_to_cec());
		pStatement.setString(17, model.getCommunity_tester_name());
		pStatement.setString(18, model.getCreated_by());
		pStatement.setString(19, model.getCode());
		pStatement.setString(20, model.getDatim_code());
		pStatement.setString(21, model.getCommunity_tester_guid());
		pStatement.setString(22, model.getTrace_status());
		pStatement.setString(23, model.getCountry());
		pStatement.setString(24, model.getFirstname());
		pStatement.setString(25, model.getLastname());
		pStatement.setString(26, model.getPhone_number());
		
		return pStatement.executeUpdate();
	}
	
	public List<PatientContactsModel> getPatientContactsByIndex(int indexClientId) throws SQLException {
		
		pStatement = conn.prepareStatement("select * from " + ConstantsUtil.PATIENT_CONTACT_TABLE
		        + " where index_patient_id = ? ");
		pStatement.setInt(1, indexClientId);
		ResultSet resultSet = pStatement.executeQuery();
		
		return convertResultsetsToPatientContacts(resultSet);
		
	}
	
	private List<PatientContactsModel> convertResultsetsToPatientContacts(ResultSet resultset) throws SQLException {

        List<PatientContactsModel> response = new ArrayList<>();

        while (resultset.next()) {

            PatientContactsModel pModel = new PatientContactsModel();
            pModel.setAge(resultset.getInt("age"));
            pModel.setAssign_contact_to_cec(resultset.getInt("assign_contact_to_cec"));
            pModel.setCommunity_tester_name(resultset.getString("community_tester_name"));
            pModel.setCountry(resultset.getString("country"));
            pModel.setCreated_by(resultset.getString("created_by"));
            pModel.setDate_created(resultset.getDate("date_created"));
            pModel.setDate_modified(resultset.getDate("date_modified"));
            pModel.setFear_their_partner(resultset.getString("fear_their_partner"));
            pModel.setForced_sexually(resultset.getString("forced_sexually"));
            pModel.setId(resultset.getInt("id"));
            pModel.setIndex_patient_id(resultset.getInt("index_patient_id"));
            pModel.setLga(resultset.getString("lga"));
            pModel.setModified_by(resultset.getString("modified_by"));
            pModel.setMore_information(resultset.getString("more_information"));
            pModel.setNotification_method(resultset.getString("notification_method"));
            pModel.setPhysically_abused(resultset.getString("physically_abused"));
            pModel.setPreferred_testing_location(resultset.getString("preferred_testing_location"));
            pModel.setRelationship(resultset.getString("relationship"));
            pModel.setSex(resultset.getString("sex"));
            pModel.setState(resultset.getString("state"));
            pModel.setTown(resultset.getString("town"));
            pModel.setUuid(resultset.getString("uuid"));
            pModel.setVillage(resultset.getString("village"));
            pModel.setCode(resultset.getString("code"));
            pModel.setDatim_code(resultset.getString("datim_code"));
            pModel.setCommunity_tester_guid(resultset.getString("community_tester_guid"));
            pModel.setTrace_status(resultset.getString("trace_status"));
            pModel.setCountry(resultset.getString("country"));
            pModel.setFirstname(resultset.getString("firstname"));
            pModel.setLastname(resultset.getString("lastname"));
            pModel.setPhone_number(resultset.getString("phone_number"));

            response.add(pModel);

        }

        return response;

    }
	
	public void assignContacts(int contactId, TesterModel testerModel, String modifier) throws SQLException {
		String sql_txt = "update "
		        + ConstantsUtil.PATIENT_CONTACT_TABLE
		        + " set community_tester_name = ?,community_tester_guid = ?, assign_contact_to_cec = ?,date_modified = ?, modified_by = ? where id = ? ";
		pStatement = conn.prepareStatement(sql_txt);
		pStatement.setString(1, testerModel.getUsername());
		pStatement.setString(2, testerModel.getCommunity_tester_guid());
		pStatement.setInt(3, 1);
		pStatement.setDate(4, new Date(new java.util.Date().getTime()));
		pStatement.setString(5, modifier);
		pStatement.setInt(6, contactId);
		
		pStatement.executeUpdate();
	}
	
	public List<PatientContactsModel> getAllPatientContacts() throws SQLException {
		
		pStatement = conn.prepareStatement("select * from " + ConstantsUtil.PATIENT_CONTACT_TABLE);
		ResultSet resultSet = pStatement.executeQuery();
		
		return convertResultsetsToPatientContacts(resultSet);
	}
	
	public void updatePatientContactTracedStatus(String contactUUID) throws SQLException {
		
		String sql_txt = "update " + ConstantsUtil.PATIENT_CONTACT_TABLE + " set trace_status = ? where uuid = ?";
		pStatement = conn.prepareStatement(sql_txt);
		pStatement.setString(1, ConstantsUtil.TRACE_COMPLETED_STATUS);
		pStatement.setString(2, contactUUID);
		pStatement.executeUpdate();
	}
	
	public int createSystemUser(UserModel user) throws SQLException {
		String sql_txt = "INSERT INTO " + ConstantsUtil.AUTH_TABLE
		        + "(uuid, username, password, date_created, date_modified) " + "VALUES(?, ?, ?, NOW(), ?);";
		pStatement = conn.prepareStatement(sql_txt);
		pStatement.setString(1, UUID.randomUUID().toString());
		pStatement.setString(2, user.getUsername());
		pStatement.setString(3, user.getPassword());
		pStatement.setDate(4, (Date) user.getDate_modified());
		
		return pStatement.executeUpdate();
	}
	
	public UserModel getSystemUser() throws SQLException {
		String sql_txt = "SELECT id, uuid, username, password, date_created, date_modified FROM " + ConstantsUtil.AUTH_TABLE;
		pStatement = conn.prepareStatement(sql_txt);
		ResultSet rs = pStatement.executeQuery();
		
		if (rs.next()) {
			UserModel userModel = new UserModel();
			userModel.setId(rs.getInt("id"));
			userModel.setPassword(rs.getString("password"));
			userModel.setUsername(rs.getString("username"));
			userModel.setUuid(rs.getString("uuid"));
			userModel.setDate_created(rs.getDate("date_created"));
			userModel.setDate_modified(rs.getDate("date_modified"));
			
			return userModel;
		}
		
		return null;
	}
	
	public int editSystemUser(int id, UserModel user) throws SQLException {
		String sql_txt = "UPDATE " + ConstantsUtil.AUTH_TABLE + " SET username=?, password=?, "
		        + "date_modified=? WHERE id=?;";
		pStatement = conn.prepareStatement(sql_txt);
		pStatement.setString(1, user.getUsername());
		pStatement.setString(2, user.getPassword());
		pStatement.setDate(3, new Date(new java.util.Date().getTime()));
		pStatement.setInt(4, id);
		
		return pStatement.executeUpdate();
	}
	
	public int createTokenStore(TokenStore tokenStore) throws SQLException {
		System.out.println("About to write token to DB");
		pStatement = conn.prepareStatement("insert into " + ConstantsUtil.TOKEN_TABLE
		        + "(token_id, uuid, token, date_created,date_modified)Values(?,?,?,NOW(),NULL) "
		        + "ON DUPLICATE KEY UPDATE uuid = ?, token = ?, date_modified = NOW() ");
		
		pStatement.setString(1, ConstantsUtil.DEFAULT_TOKEN_ID);
		pStatement.setString(2, tokenStore.getUuid());
		pStatement.setString(3, tokenStore.getToken());
		pStatement.setString(4, tokenStore.getUuid());
		pStatement.setString(5, tokenStore.getToken());
		// pStatement.setDate(6, (Date) tokenStore.getDate_modified());
		
		int response = pStatement.executeUpdate();
		
		System.out.println("Finished writing token to DB");
		System.out.println("response is: " + response);
		
		return response;
		
	}
	
	public TokenStore getTokenStore() throws SQLException {
		String sql_txt = "SELECT token_id, uuid, token, date_created,date_modified FROM " + ConstantsUtil.TOKEN_TABLE;
		pStatement = conn.prepareStatement(sql_txt);
		ResultSet rs = pStatement.executeQuery();
		
		TokenStore store = null;
		
		while (rs.next()) {
			store = new TokenStore();
			store.setDate_created(rs.getDate("date_created"));
			store.setDate_modified(rs.getDate("date_modified"));
			store.setToken(rs.getString("token"));
			store.setToken_id(rs.getString("token_id"));
			store.setUuid(rs.getString("uuid"));
		}
		
		return store;
		
	}
	
}
