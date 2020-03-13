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
public class PedRiskStratification {
	
	private Integer rst_ped_forced_sex;
	
	private Integer rst_ped_poor_health_in_last_six_months;
	
	private Integer rst_ped_mother_positive_or_member_deceased;
	
	private Integer rst_ped_skin_problems_in_last_six_months;
	
	private Integer rst_ped_living_with_tb_diagnosed_person_or_tb_symptoms;
	
	private Integer rst_ped_ever_had_anal_or_vaginal_sex_without_condom;
	
	private Integer rst_ped_child_ever_tested_for_hiv;
	
	//  private Integer rst_ped_hiv_risk_score;
	
	public Integer getRst_ped_poor_health_in_last_six_months() {
		return rst_ped_poor_health_in_last_six_months;
	}
	
	public void setRst_ped_poor_health_in_last_six_months(Integer rst_ped_poor_health_in_last_six_months) {
		this.rst_ped_poor_health_in_last_six_months = rst_ped_poor_health_in_last_six_months;
	}
	
	public Integer getRst_ped_forced_sex() {
		return rst_ped_forced_sex;
	}
	
	public void setRst_ped_forced_sex(Integer rst_ped_forced_sex) {
		this.rst_ped_forced_sex = rst_ped_forced_sex;
	}
	
	public Integer getRst_ped_mother_positive_or_member_deceased() {
		return rst_ped_mother_positive_or_member_deceased;
	}
	
	public void setRst_ped_mother_positive_or_member_deceased(Integer rst_ped_mother_positive_or_member_deceased) {
		this.rst_ped_mother_positive_or_member_deceased = rst_ped_mother_positive_or_member_deceased;
	}
	
	public Integer getRst_ped_skin_problems_in_last_six_months() {
		return rst_ped_skin_problems_in_last_six_months;
	}
	
	public void setRst_ped_skin_problems_in_last_six_months(Integer rst_ped_skin_problems_in_last_six_months) {
		this.rst_ped_skin_problems_in_last_six_months = rst_ped_skin_problems_in_last_six_months;
	}
	
	public Integer getRst_ped_living_with_tb_diagnosed_person_or_tb_symptoms() {
		return rst_ped_living_with_tb_diagnosed_person_or_tb_symptoms;
	}
	
	public void setRst_ped_living_with_tb_diagnosed_person_or_tb_symptoms(
	        Integer rst_ped_living_with_tb_diagnosed_person_or_tb_symptoms) {
		this.rst_ped_living_with_tb_diagnosed_person_or_tb_symptoms = rst_ped_living_with_tb_diagnosed_person_or_tb_symptoms;
	}
	
	public Integer getRst_ped_ever_had_anal_or_vaginal_sex_without_condom() {
		return rst_ped_ever_had_anal_or_vaginal_sex_without_condom;
	}
	
	public void setRst_ped_ever_had_anal_or_vaginal_sex_without_condom(
	        Integer rst_ped_ever_had_anal_or_vaginal_sex_without_condom) {
		this.rst_ped_ever_had_anal_or_vaginal_sex_without_condom = rst_ped_ever_had_anal_or_vaginal_sex_without_condom;
	}
	
	public Integer getRst_ped_child_ever_tested_for_hiv() {
		return rst_ped_child_ever_tested_for_hiv;
	}
	
	public void setRst_ped_child_ever_tested_for_hiv(Integer rst_ped_child_ever_tested_for_hiv) {
		this.rst_ped_child_ever_tested_for_hiv = rst_ped_child_ever_tested_for_hiv;
	}
	
}
