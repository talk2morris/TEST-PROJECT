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
public class ClientScore implements Serializable {
	
	private Integer hiv_risk_score;
	
	private Integer clinical_tb_score;
	
	private Integer sti_score;
	
	public Integer getHiv_risk_score() {
		return hiv_risk_score;
	}
	
	public void setHiv_risk_score(Integer hiv_risk_score) {
		this.hiv_risk_score = hiv_risk_score;
	}
	
	public Integer getClinical_tb_score() {
		return clinical_tb_score;
	}
	
	public void setClinical_tb_score(Integer clinical_tb_score) {
		this.clinical_tb_score = clinical_tb_score;
	}
	
	public Integer getSti_score() {
		return sti_score;
	}
	
	public void setSti_score(Integer sti_score) {
		this.sti_score = sti_score;
	}
	
}
