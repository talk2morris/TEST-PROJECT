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
public class RiskStratificationNew {
	
	public PedRiskStratification pedRiskStratification;
	
	public AdultRiskStratification adultRiskStratification;
	
	public RiskScore riskScore;
	
	public PedRiskStratification getPedRiskStratification() {
		return pedRiskStratification;
	}
	
	public void setPedRiskStratification(PedRiskStratification pedRiskStratification) {
		this.pedRiskStratification = pedRiskStratification;
	}
	
	public AdultRiskStratification getAdultRiskStratification() {
		return adultRiskStratification;
	}
	
	public void setAdultRiskStratification(AdultRiskStratification adultRiskStratification) {
		this.adultRiskStratification = adultRiskStratification;
	}
	
	public RiskScore getRiskScore() {
		return riskScore;
	}
	
	public void setRiskScore(RiskScore riskScore) {
		this.riskScore = riskScore;
	}
	
}
