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
public class TbScreening implements Serializable {
	
	private Integer tb_screening_current_cough;
	
	private Integer tb_screening_weight_loss;
	
	private Integer tb_screening_fever;
	
	private Integer tb_screening_night_sweats;
	
	private Integer tb_contact_with_tb_patient;
	
	public Integer getTb_screening_current_cough() {
		return tb_screening_current_cough;
	}
	
	public void setTb_screening_current_cough(Integer tb_screening_current_cough) {
		this.tb_screening_current_cough = tb_screening_current_cough;
	}
	
	public Integer getTb_screening_weight_loss() {
		return tb_screening_weight_loss;
	}
	
	public void setTb_screening_weight_loss(Integer tb_screening_weight_loss) {
		this.tb_screening_weight_loss = tb_screening_weight_loss;
	}
	
	public Integer getTb_screening_fever() {
		return tb_screening_fever;
	}
	
	public void setTb_screening_fever(Integer tb_screening_fever) {
		this.tb_screening_fever = tb_screening_fever;
	}
	
	public Integer getTb_screening_night_sweats() {
		return tb_screening_night_sweats;
	}
	
	public void setTb_screening_night_sweats(Integer tb_screening_night_sweats) {
		this.tb_screening_night_sweats = tb_screening_night_sweats;
	}
	
	public Integer getTb_contact_with_tb_patient() {
		return tb_contact_with_tb_patient;
	}
	
	public void setTb_contact_with_tb_patient(Integer tb_contact_with_tb_patient) {
		this.tb_contact_with_tb_patient = tb_contact_with_tb_patient;
	}
	
}
