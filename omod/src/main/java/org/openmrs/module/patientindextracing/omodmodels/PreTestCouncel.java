/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.omodmodels;

/**
 * @author MORRISON.I
 */
public class PreTestCouncel {
	
	private KnowledgeAssesement knowledge_assessment;
	
	private RiskAssessment risk_assessment;
	
	private TbScreening tb_screening;
	
	private STIScreening sti_screening;
	
	private ClientScore client_score;
	
	public KnowledgeAssesement getKnowledge_assessment() {
		return knowledge_assessment;
	}
	
	public void setKnowledge_assessment(KnowledgeAssesement knowledge_assessment) {
		this.knowledge_assessment = knowledge_assessment;
	}
	
	public RiskAssessment getRisk_assessment() {
		return risk_assessment;
	}
	
	public void setRisk_assessment(RiskAssessment risk_assessment) {
		this.risk_assessment = risk_assessment;
	}
	
	public TbScreening getTb_screening() {
		return tb_screening;
	}
	
	public void setTb_screening(TbScreening tb_screening) {
		this.tb_screening = tb_screening;
	}
	
	public STIScreening getSti_screening() {
		return sti_screening;
	}
	
	public void setSti_screening(STIScreening sti_screening) {
		this.sti_screening = sti_screening;
	}
	
	public ClientScore getClient_score() {
		return client_score;
	}
	
	public void setClient_score(ClientScore client_score) {
		this.client_score = client_score;
	}
	
}
