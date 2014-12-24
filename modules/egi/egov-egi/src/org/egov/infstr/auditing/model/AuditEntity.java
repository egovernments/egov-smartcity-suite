/*
 * @(#)AuditEntity.java 3.0, 21 Jun, 2013 5:58:40 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.auditing.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Enum for all auditable entities.
 * The AuditEntity enum should prefix with its respective AuditModule enum.
 * <pre>
 * eg: 
 * for AuditModule PROPERTYTAX
 * the AuditEntity should be entered like PROPERTYTAX_XYZENTITY
 * </pre> 
 **/
public enum AuditEntity {
	//All WORKS module auditable entities
	WORKS_SOR("SOR Master"),
	WORKS_ESTIMATE("Estimate"),
	
	//All PROPERTYTAX auditable entities
	PROPERTYTAX_PROPERTY("Property"),
		
	//All COLLECTION module auditable entities
	COLLECTIONS_RECEIPTHEADER("Receipt"),
	COLLECTIONS_CHALLAN("Challan"),
	
	//All DMS module auditable entities
	DMS_INBOUNDFILE("Inbound File"),
	DMS_OUTBOUNDFILE("Outbound File"),
	DMS_INTERNALFILE("Internal File"),
	DMS_NOTIFICATIONGRP("Notification Group Master"),
	DMS_FILENOTIFICATION("File Notification"),
	
	//All INFRA module auditable entities
	INFRA_USER("User Master"),
	
	//All Stores module auditable entities
	STORES_MATERIALTYPE("Material Type"),
	STORES_ITEM("Item Master"),	
	STORES_IMI("Modify Indent"),
	STORES_UNITOFMEASURE("Unit Of Measure"),
	STORES_USERSTORE("UserStore Mapping"),
	
	//All LandEstate module auditable entities
	LANDESTATE_HOARDINGSIZE("Hoarding Size"),
	LANDESTATE_LANDLEASETRANSFER("Land Lease Transfer"),
	LANDESTATE_SHOPPINGCOMPLEX("Shopping Complex"),
			
	//All LCMS module auditable entities
	LCMS_CASEREG("Case Registration"),
	LCMS_CONSICSNAMEND("Consecutuion Amendment"),
	LCMS_CMPLNC("Compliance"),
	LCMS_ISSUE("Issue"),
	LCMS_APPRANC("Appearance"),
	LCMS_REPLY("Reply"),
	LCMS_HERNG("Hearing"),
	LCMS_PRNCMNTOFJUDGMNT("Pronouncement of Judgement"),
	LCMS_CLSDFORJUDGMNT("Closed for Judgement"),
	LCMS_ORDR("Order"),
	LCMS_CLSCASE("Close Case"),
	LCMS_JUDGMNT("Judgement"),
	LCMS_CASETYPMSTR("Case Type Master"),
	LCMS_COURTMSTR("Court Master"),
	LCMS_COURTTYPMSTR("Court Type Master"),
	LCMS_GOVDEPTMSTR("Government Department Master"),
	LCMS_INTRMORDRMSTR("Interim Order Master"),
	LCMS_JUDGMNTTYPMSTR("Judgement Type Master"),
	LCMS_SUBJCTMATRMSTR("Subject Matter Master"),
	LCMS_STNDNGCOUNCLMSTR("Standing Counsel Master"),
	
	//All EIS module auditable entities
	EIS_EMPLOYEE("Employee Master"),
	
	//All Payroll module auditable entities
	PAYROLL_EMPPAYMASTER("Employee Payroll Master"),
	PAYROLL_PAYHEADMASTER("Payhead Master"),
	
	//All SWM module auditable entites 
	SWM_VEHICLE("Vehicle Details"),
	SWM_GARBAGECOLLECTION("Garbage Collection Details"),
	
	//All Financials module auditable entites 
	FINANCIALS_CHARTOFACCOUNTS("Account code"),
	FINANCIALS_BILLREGISTER("BillRegister"),
	
	//All License module auditable entities
	ECL_LIC("Electrical License"),
	HPL_LIC("Hospital License"),
	HWKL_LIC("Hawker License"),
	PWDL_LIC("PWD License"),
	TL_LIC("Trade License"),
	VETL_LIC("Veterinary License"),
	WWL_LIC("WaterWorks License"),
	
	//All Pension module auditable entities
	PENSION_PCH_PENSIONER("Pension Change History for Pensioner"),
	PENSION_PCH_FAMILYPENSIONER("Pension Change History for Family pensioner");

	String entityName;
	private AuditEntity(String entityName) {
		this.entityName = entityName;
	}
	
	public String getEntityName() {
		return this.entityName;
	}
	
	public static AuditEntity getAuditEntityByName(String entityName){
		for(AuditEntity eachValue : values()){
			if( eachValue.entityName.equals(entityName)){
				return eachValue;	    	  
			}
		}
		return null;
	}
	
	public static Set<String> getAuditEntityNamesByModule(String auditModuleEnumName){
		Set<String> auditEntities = new HashSet<String>();
		for(AuditEntity eachValue : values()){
			if( eachValue.name().startsWith(auditModuleEnumName)){
				auditEntities.add(eachValue.entityName);	    	  
			}
		}
		return auditEntities;
	}
}
