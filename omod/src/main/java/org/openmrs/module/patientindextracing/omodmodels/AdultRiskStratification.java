/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.omodmodels;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author MORRISON.I
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdultRiskStratification {
	
	//  private Integer rst_adult_hiv_risk_score;
	
	private Integer rst_adult_have_tb_symptoms;
	
	private Integer rst_adult_have_shared_sharp_objects;
	
	private Integer rst_adult_have_paid_for_or_sold_sex;
	
	private Integer rst_adult_ever_had_blood_transfusion;
	
	private Integer rst_adult_test_based_on_physician_request;
	
	private Integer rst_adult_ever_had_anal_or_vaginal_sex_without_condom;
	
	private Integer rst_adult_have_sti_symptoms;
	
	private Integer rst_adult_forced_sex;
	
	public Integer getRst_adult_have_tb_symptoms() {
		return rst_adult_have_tb_symptoms;
	}
	
	public void setRst_adult_have_tb_symptoms(Integer rst_adult_have_tb_symptoms) {
		this.rst_adult_have_tb_symptoms = rst_adult_have_tb_symptoms;
	}
	
	public Integer getRst_adult_have_shared_sharp_objects() {
		return rst_adult_have_shared_sharp_objects;
	}
	
	public void setRst_adult_have_shared_sharp_objects(Integer rst_adult_have_shared_sharp_objects) {
		this.rst_adult_have_shared_sharp_objects = rst_adult_have_shared_sharp_objects;
	}
	
	public Integer getRst_adult_have_paid_for_or_sold_sex() {
		return rst_adult_have_paid_for_or_sold_sex;
	}
	
	public void setRst_adult_have_paid_for_or_sold_sex(Integer rst_adult_have_paid_for_or_sold_sex) {
		this.rst_adult_have_paid_for_or_sold_sex = rst_adult_have_paid_for_or_sold_sex;
	}
	
	public Integer getRst_adult_ever_had_blood_transfusion() {
		return rst_adult_ever_had_blood_transfusion;
	}
	
	public void setRst_adult_ever_had_blood_transfusion(Integer rst_adult_ever_had_blood_transfusion) {
		this.rst_adult_ever_had_blood_transfusion = rst_adult_ever_had_blood_transfusion;
	}
	
	public Integer getRst_adult_test_based_on_physician_request() {
		return rst_adult_test_based_on_physician_request;
	}
	
	public void setRst_adult_test_based_on_physician_request(Integer rst_adult_test_based_on_physician_request) {
		this.rst_adult_test_based_on_physician_request = rst_adult_test_based_on_physician_request;
	}
	
	public Integer getRst_adult_ever_had_anal_or_vaginal_sex_without_condom() {
		return rst_adult_ever_had_anal_or_vaginal_sex_without_condom;
	}
	
	public void setRst_adult_ever_had_anal_or_vaginal_sex_without_condom(
	        Integer rst_adult_ever_had_anal_or_vaginal_sex_without_condom) {
		this.rst_adult_ever_had_anal_or_vaginal_sex_without_condom = rst_adult_ever_had_anal_or_vaginal_sex_without_condom;
	}
	
	public Integer getRst_adult_have_sti_symptoms() {
		return rst_adult_have_sti_symptoms;
	}
	
	public void setRst_adult_have_sti_symptoms(Integer rst_adult_have_sti_symptoms) {
		this.rst_adult_have_sti_symptoms = rst_adult_have_sti_symptoms;
	}
	
	public Integer getRst_adult_forced_sex() {
		return rst_adult_forced_sex;
	}
	
	public void setRst_adult_forced_sex(Integer rst_adult_forced_sex) {
		this.rst_adult_forced_sex = rst_adult_forced_sex;
	}
	
}
