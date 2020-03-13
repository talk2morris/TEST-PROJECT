/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.omodmodels;

import java.util.List;
import java.util.Map;

/**
 * @author MORRISON.I
 */
public class YieldPerSex {
	
	private List<Object> maleList;
	
	private List<Object> femaleList;
	
	public List<Object> getMaleList() {
		return maleList;
	}
	
	public void setMaleList(List<Object> maleList) {
		this.maleList = maleList;
	}
	
	public List<Object> getFemaleList() {
		return femaleList;
	}
	
	public void setFemaleList(List<Object> femaleList) {
		this.femaleList = femaleList;
	}
	
}
