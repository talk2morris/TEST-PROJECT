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
public class PostTestCouncel implements Serializable {
	
	private String PostTestedBefore;
	
	private Integer request_and_result_form_signed_by_testers;
	
	private Integer request_and_result_form_filled_with_ct_intake_form;
	
	private Integer client_received_hiv_result;
	
	private Integer post_test_councelling_done;
	
	private Integer client_or_partner_use_fp_methods;
	
	private Integer client_or_partner_use_condom_as_one_fp_method;
	
	private Integer risk_reduction_plan_developed;
	
	private Integer post_test_disclosure_plan_developed;
	
	private Integer will_bring_partners_for_testing;
	
	private Integer will_bring_own_children_less_than_5_years_old_for_testing;
	
	private Integer provided_with_information_on_fp_and_dual_contraception;
	
	private Integer correct_condom_use_demonstrated;
	
	private Integer condoms_provided;
	
	private Integer client_referred_to_other_services;
	
	public String getPostTestedBefore() {
		return PostTestedBefore;
	}
	
	public void setPostTestedBefore(String PostTestedBefore) {
		this.PostTestedBefore = PostTestedBefore;
	}
	
	public Integer getRequest_and_result_form_signed_by_testers() {
		return request_and_result_form_signed_by_testers;
	}
	
	public void setRequest_and_result_form_signed_by_testers(Integer request_and_result_form_signed_by_testers) {
		this.request_and_result_form_signed_by_testers = request_and_result_form_signed_by_testers;
	}
	
	public Integer getRequest_and_result_form_filled_with_ct_intake_form() {
		return request_and_result_form_filled_with_ct_intake_form;
	}
	
	public void setRequest_and_result_form_filled_with_ct_intake_form(
	        Integer request_and_result_form_filled_with_ct_intake_form) {
		this.request_and_result_form_filled_with_ct_intake_form = request_and_result_form_filled_with_ct_intake_form;
	}
	
	public Integer getClient_received_hiv_result() {
		return client_received_hiv_result;
	}
	
	public void setClient_received_hiv_result(Integer client_received_hiv_result) {
		this.client_received_hiv_result = client_received_hiv_result;
	}
	
	public Integer getPost_test_councelling_done() {
		return post_test_councelling_done;
	}
	
	public void setPost_test_councelling_done(Integer post_test_councelling_done) {
		this.post_test_councelling_done = post_test_councelling_done;
	}
	
	public Integer getClient_or_partner_use_fp_methods() {
		return client_or_partner_use_fp_methods;
	}
	
	public void setClient_or_partner_use_fp_methods(Integer client_or_partner_use_fp_methods) {
		this.client_or_partner_use_fp_methods = client_or_partner_use_fp_methods;
	}
	
	public Integer getClient_or_partner_use_condom_as_one_fp_method() {
		return client_or_partner_use_condom_as_one_fp_method;
	}
	
	public void setClient_or_partner_use_condom_as_one_fp_method(Integer client_or_partner_use_condom_as_one_fp_method) {
		this.client_or_partner_use_condom_as_one_fp_method = client_or_partner_use_condom_as_one_fp_method;
	}
	
	public Integer getRisk_reduction_plan_developed() {
		return risk_reduction_plan_developed;
	}
	
	public void setRisk_reduction_plan_developed(Integer risk_reduction_plan_developed) {
		this.risk_reduction_plan_developed = risk_reduction_plan_developed;
	}
	
	public Integer getPost_test_disclosure_plan_developed() {
		return post_test_disclosure_plan_developed;
	}
	
	public void setPost_test_disclosure_plan_developed(Integer post_test_disclosure_plan_developed) {
		this.post_test_disclosure_plan_developed = post_test_disclosure_plan_developed;
	}
	
	public Integer getWill_bring_partners_for_testing() {
		return will_bring_partners_for_testing;
	}
	
	public void setWill_bring_partners_for_testing(Integer will_bring_partners_for_testing) {
		this.will_bring_partners_for_testing = will_bring_partners_for_testing;
	}
	
	public Integer getWill_bring_own_children_less_than_5_years_old_for_testing() {
		return will_bring_own_children_less_than_5_years_old_for_testing;
	}
	
	public void setWill_bring_own_children_less_than_5_years_old_for_testing(
	        Integer will_bring_own_children_less_than_5_years_old_for_testing) {
		this.will_bring_own_children_less_than_5_years_old_for_testing = will_bring_own_children_less_than_5_years_old_for_testing;
	}
	
	public Integer getProvided_with_information_on_fp_and_dual_contraception() {
		return provided_with_information_on_fp_and_dual_contraception;
	}
	
	public void setProvided_with_information_on_fp_and_dual_contraception(
	        Integer provided_with_information_on_fp_and_dual_contraception) {
		this.provided_with_information_on_fp_and_dual_contraception = provided_with_information_on_fp_and_dual_contraception;
	}
	
	public Integer getCorrect_condom_use_demonstrated() {
		return correct_condom_use_demonstrated;
	}
	
	public void setCorrect_condom_use_demonstrated(Integer correct_condom_use_demonstrated) {
		this.correct_condom_use_demonstrated = correct_condom_use_demonstrated;
	}
	
	public Integer getCondoms_provided() {
		return condoms_provided;
	}
	
	public void setCondoms_provided(Integer condoms_provided) {
		this.condoms_provided = condoms_provided;
	}
	
	public Integer getClient_referred_to_other_services() {
		return client_referred_to_other_services;
	}
	
	public void setClient_referred_to_other_services(Integer client_referred_to_other_services) {
		this.client_referred_to_other_services = client_referred_to_other_services;
	}
	
}
