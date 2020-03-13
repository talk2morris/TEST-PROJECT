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
public class RiskScore {
	
	public Integer rst_adult_hiv_risk_score;
	
	public Integer rst_ped_hiv_risk_score;
	
	public Integer getRst_adult_hiv_risk_score() {
		return rst_adult_hiv_risk_score;
	}
	
	public void setRst_adult_hiv_risk_score(Integer rst_adult_hiv_risk_score) {
		this.rst_adult_hiv_risk_score = rst_adult_hiv_risk_score;
	}
	
	public Integer getRst_ped_hiv_risk_score() {
		return rst_ped_hiv_risk_score;
	}
	
	public void setRst_ped_hiv_risk_score(Integer rst_ped_hiv_risk_score) {
		this.rst_ped_hiv_risk_score = rst_ped_hiv_risk_score;
	}
	
}
