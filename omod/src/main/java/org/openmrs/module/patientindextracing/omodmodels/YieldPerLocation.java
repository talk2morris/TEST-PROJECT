/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.omodmodels;

import java.util.List;
import java.util.Set;

/**
 * @author MORRISON.I
 */
public class YieldPerLocation {
	
	private Set<String> locations;
	
	private List<Long> yieldSum;
	
	public Set<String> getLocations() {
		return locations;
	}
	
	public void setLocations(Set<String> locations) {
		this.locations = locations;
	}
	
	public List<Long> getYieldSum() {
		return yieldSum;
	}
	
	public void setYieldSum(List<Long> yieldSum) {
		this.yieldSum = yieldSum;
	}
	
}
