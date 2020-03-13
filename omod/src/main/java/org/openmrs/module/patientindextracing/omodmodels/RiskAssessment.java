/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.omodmodels;

import java.io.Serializable;

/**
 * @author MORRISON.I
 */
public class RiskAssessment implements Serializable {
	
	private Integer risk_assessment_ever_had_sexual_intercourse;
	
	private Integer risk_assessment_blood_transfussion_in_last_3_month;
	
	private Integer risk_assessment_unprotected_sex_with_casual_partner_in_last_3_months;
	
	private Integer risk_assessment_unprotected_sex_with_regular_partner_in_last_3_months;
	
	private Integer risk_assessment_sti_in_last_3_months;
	
	private Integer risk_assessment_more_than_1_sex_partner_in_last_3_months;
	
	public Integer getRisk_assessment_ever_had_sexual_intercourse() {
		return risk_assessment_ever_had_sexual_intercourse;
	}
	
	public void setRisk_assessment_ever_had_sexual_intercourse(Integer risk_assessment_ever_had_sexual_intercourse) {
		this.risk_assessment_ever_had_sexual_intercourse = risk_assessment_ever_had_sexual_intercourse;
	}
	
	public Integer getRisk_assessment_blood_transfussion_in_last_3_month() {
		return risk_assessment_blood_transfussion_in_last_3_month;
	}
	
	public void setRisk_assessment_blood_transfussion_in_last_3_month(
	        Integer risk_assessment_blood_transfussion_in_last_3_month) {
		this.risk_assessment_blood_transfussion_in_last_3_month = risk_assessment_blood_transfussion_in_last_3_month;
	}
	
	public Integer getRisk_assessment_unprotected_sex_with_casual_partner_in_last_3_months() {
		return risk_assessment_unprotected_sex_with_casual_partner_in_last_3_months;
	}
	
	public void setRisk_assessment_unprotected_sex_with_casual_partner_in_last_3_months(
	        Integer risk_assessment_unprotected_sex_with_casual_partner_in_last_3_months) {
		this.risk_assessment_unprotected_sex_with_casual_partner_in_last_3_months = risk_assessment_unprotected_sex_with_casual_partner_in_last_3_months;
	}
	
	public Integer getRisk_assessment_unprotected_sex_with_regular_partner_in_last_3_months() {
		return risk_assessment_unprotected_sex_with_regular_partner_in_last_3_months;
	}
	
	public void setRisk_assessment_unprotected_sex_with_regular_partner_in_last_3_months(
	        Integer risk_assessment_unprotected_sex_with_regular_partner_in_last_3_months) {
		this.risk_assessment_unprotected_sex_with_regular_partner_in_last_3_months = risk_assessment_unprotected_sex_with_regular_partner_in_last_3_months;
	}
	
	public Integer getRisk_assessment_sti_in_last_3_months() {
		return risk_assessment_sti_in_last_3_months;
	}
	
	public void setRisk_assessment_sti_in_last_3_months(Integer risk_assessment_sti_in_last_3_months) {
		this.risk_assessment_sti_in_last_3_months = risk_assessment_sti_in_last_3_months;
	}
	
	public Integer getRisk_assessment_more_than_1_sex_partner_in_last_3_months() {
		return risk_assessment_more_than_1_sex_partner_in_last_3_months;
	}
	
	public void setRisk_assessment_more_than_1_sex_partner_in_last_3_months(
	        Integer risk_assessment_more_than_1_sex_partner_in_last_3_months) {
		this.risk_assessment_more_than_1_sex_partner_in_last_3_months = risk_assessment_more_than_1_sex_partner_in_last_3_months;
	}
	
}
