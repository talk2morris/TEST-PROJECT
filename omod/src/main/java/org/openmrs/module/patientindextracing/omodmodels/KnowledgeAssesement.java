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
public class KnowledgeAssesement implements Serializable {
	
	private Integer knowledge_assessment_previously_tested_negative;
	
	private Integer knowledge_assessment_informed_on_preventing_transmission_method;
	
	private Integer knowledge_assessment_informed_about_risk_factors;
	
	private Integer knowledge_assessment_informed_about_possible_test_results;
	
	private Integer knowledge_assessment_informed_consent_for_testing_given;
	
	private Integer knowledge_assessment_pregnant;
	
	private Integer knowledge_assessment_informed_about_transmission_routes;
	
	public Integer getKnowledge_assessment_previously_tested_negative() {
		return knowledge_assessment_previously_tested_negative;
	}
	
	public void setKnowledge_assessment_previously_tested_negative(Integer knowledge_assessment_previously_tested_negative) {
		this.knowledge_assessment_previously_tested_negative = knowledge_assessment_previously_tested_negative;
	}
	
	public Integer getKnowledge_assessment_informed_on_preventing_transmission_method() {
		return knowledge_assessment_informed_on_preventing_transmission_method;
	}
	
	public void setKnowledge_assessment_informed_on_preventing_transmission_method(
	        Integer knowledge_assessment_informed_on_preventing_transmission_method) {
		this.knowledge_assessment_informed_on_preventing_transmission_method = knowledge_assessment_informed_on_preventing_transmission_method;
	}
	
	public Integer getKnowledge_assessment_informed_about_risk_factors() {
		return knowledge_assessment_informed_about_risk_factors;
	}
	
	public void setKnowledge_assessment_informed_about_risk_factors(Integer knowledge_assessment_informed_about_risk_factors) {
		this.knowledge_assessment_informed_about_risk_factors = knowledge_assessment_informed_about_risk_factors;
	}
	
	public Integer getKnowledge_assessment_informed_about_possible_test_results() {
		return knowledge_assessment_informed_about_possible_test_results;
	}
	
	public void setKnowledge_assessment_informed_about_possible_test_results(
	        Integer knowledge_assessment_informed_about_possible_test_results) {
		this.knowledge_assessment_informed_about_possible_test_results = knowledge_assessment_informed_about_possible_test_results;
	}
	
	public Integer getKnowledge_assessment_informed_consent_for_testing_given() {
		return knowledge_assessment_informed_consent_for_testing_given;
	}
	
	public void setKnowledge_assessment_informed_consent_for_testing_given(
	        Integer knowledge_assessment_informed_consent_for_testing_given) {
		this.knowledge_assessment_informed_consent_for_testing_given = knowledge_assessment_informed_consent_for_testing_given;
	}
	
	public Integer getKnowledge_assessment_pregnant() {
		return knowledge_assessment_pregnant;
	}
	
	public void setKnowledge_assessment_pregnant(Integer knowledge_assessment_pregnant) {
		this.knowledge_assessment_pregnant = knowledge_assessment_pregnant;
	}
	
	public Integer getKnowledge_assessment_informed_about_transmission_routes() {
		return knowledge_assessment_informed_about_transmission_routes;
	}
	
	public void setKnowledge_assessment_informed_about_transmission_routes(
	        Integer knowledge_assessment_informed_about_transmission_routes) {
		this.knowledge_assessment_informed_about_transmission_routes = knowledge_assessment_informed_about_transmission_routes;
	}
	
}
