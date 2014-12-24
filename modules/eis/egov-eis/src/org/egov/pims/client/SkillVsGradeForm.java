/*
 *
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.client;
import org.apache.struts.action.ActionForm;
public class SkillVsGradeForm extends ActionForm
{

	private String[] gradeValue ;
	private String[] gradeId;
	private String skillValue ="";
	private String skillId ="";
	public String fromDate;
	public String toDate;
	public String[] fromDateGrd;
	public String[] toDateGrd;
	
	/**
	 * public String fromDate;
	public String toDate;
	
	 * @return Returns the gradeValue.
	 */
	public String[] getGradeValue() {
		return gradeValue;
	}
	/**
	 * @param gradeValue The gradeValue to set.
	 */
	public void setGradeValue(String[] gradeValue) {
		this.gradeValue = gradeValue;
	}
	/**
	 * @return Returns the skillValue.
	 */
	public String getSkillValue() {
		return skillValue;
	}
	/**
	 * @param skillValue The skillValue to set.
	 */
	public void setSkillValue(String skillValue) {
		this.skillValue = skillValue;
	}
	/**
	 * @return Returns the gradeId.
	 */
	public String[] getGradeId() {
		if(gradeId!=null)
		{
			int size= gradeId.length;
			int gradeLengeh =gradeId.length;
			if(gradeLengeh!=0)
			{
				for(int i=0;i<gradeLengeh;i++)
				{
					if(gradeId[i]==null||gradeId.equals(""))
					{
						gradeId[i]= "0";
					}	
				}
			}
			
		}
		
		
		return gradeId;
	}
	/**
	 * @param gradeId The gradeId to set.
	 */
	public void setGradeId(String[] gradeId) {
		this.gradeId = gradeId;
	}
	/**
	 * @return Returns the skillId.
	 */
	public String getSkillId() {
		return skillId;
	}
	/**
	 * @param skillId The skillId to set.
	 */
	public void setSkillId(String skillId) {
		this.skillId = skillId;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String[] getFromDateGrd() {
		return fromDateGrd;
	}
	public void setFromDateGrd(String[] fromDateGrd) {
		this.fromDateGrd = fromDateGrd;
	}
	public String[] getToDateGrd() {
		return toDateGrd;
	}
	public void setToDateGrd(String[] toDateGrd) {
		this.toDateGrd = toDateGrd;
	}
 }