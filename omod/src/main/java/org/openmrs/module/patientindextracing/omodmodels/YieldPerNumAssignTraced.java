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
public class YieldPerNumAssignTraced {
	
	private List<Long> assignYieldBySex;
	
	private List<Long> unassignYieldBySex;
	
	private List<Long> tracedYieldBySex;
	
	private List<Long> untracedYieldBySex;
	
	public List<Long> getAssignYieldBySex() {
		return assignYieldBySex;
	}
	
	public void setAssignYieldBySex(List<Long> assignYieldBySex) {
		this.assignYieldBySex = assignYieldBySex;
	}
	
	public List<Long> getUnassignYieldBySex() {
		return unassignYieldBySex;
	}
	
	public void setUnassignYieldBySex(List<Long> unassignYieldBySex) {
		this.unassignYieldBySex = unassignYieldBySex;
	}
	
	public List<Long> getTracedYieldBySex() {
		return tracedYieldBySex;
	}
	
	public void setTracedYieldBySex(List<Long> tracedYieldBySex) {
		this.tracedYieldBySex = tracedYieldBySex;
	}
	
	public List<Long> getUntracedYieldBySex() {
		return untracedYieldBySex;
	}
	
	public void setUntracedYieldBySex(List<Long> untracedYieldBySex) {
		this.untracedYieldBySex = untracedYieldBySex;
	}
	
}
