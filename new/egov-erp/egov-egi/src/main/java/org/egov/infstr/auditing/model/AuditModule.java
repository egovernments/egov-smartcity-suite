/*
 * @(#)AuditModule.java 3.0, 21 Jun, 2013 5:58:26 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.auditing.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *Enum for holding all auditable Modules.
 **/
public enum AuditModule {
	
	PROPERTYTAX("Property Tax"),	
	WORKS("Works"),
	COLLECTIONS("Collections"),
	STORES("Stores"),
	DMS("Document Management System"),
	INFRA("Administration"),
	LANDESTATE("Land And Estate"),
	LCMS("Legal Case Management System"),
	PAYROLL("Payroll"),
	FINANCIALS("Financials"),
	EIS("Employee Information System"),
	SWM("Solid Waste Management"),
	ECL("Electrical Contractor License"),
	HPL("Hospital License"),
	HWKL("Hawker License"),
	PWDL("PWD Contractor License"),
	TL("Trade License"),
	VETL("Veterinary Contractor License"),
	WWL("WaterWorks License"),
	PENSION("Pension");

	
	public String moduleName;	
	private final Set<String> entityNames = new HashSet<String>();
	
	AuditModule(String moduleName){
		this.moduleName = moduleName;
		entityNames.addAll(AuditEntity.getAuditEntityNamesByModule(this.name()));
	}
	
	public String getModuleName() {
		return moduleName;
	}

	public Set<String> getAuditEntities(){
		return entityNames;
	}
	
	public static List<String> getAuditModules() {
		List<String> modules = new ArrayList<String>();
		for(AuditModule module : values()) {
			modules.add(module.moduleName);
		}
		return modules;
	}
	
	public static AuditModule getAuditModuleByName(String moduleName){
		for(AuditModule eachValue : values()){
			if( eachValue.moduleName.equals(moduleName)){
				return eachValue;	    	  
			}
		}
		return null;
	}
	
}
