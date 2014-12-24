/*
 * Created on Dec 4, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.pims.model;

/**
 * @author deepak
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TechnicalGradesMaster implements java.io.Serializable
{
	public Integer id;
			public String gradeName;
			public java.util.Date fromDate;
			public java.util.Date toDate;
			public SkillMaster skillMaster;
			public java.util.Date getFromDate() {
				return fromDate;
			}
			public void setFromDate(java.util.Date fromDate) {
				this.fromDate = fromDate;
			}
			public java.util.Date getToDate() {
				return toDate;
			}
			public void setToDate(java.util.Date toDate) {
				this.toDate = toDate;
			}
			public Integer getId() {
				return id;
			}
			/**
			 * @param id The id to set.
			 */
			public void setId(Integer id) {
				this.id = id;
			}
			/**
			 * @return Returns the name.
			 */
			public String getGradeName() {
				return gradeName;
			}
			/**
			 * @param name The name to set.
			 */
			public void setGradeName(String gradeName) {
				this.gradeName = gradeName;
			}
			public SkillMaster getSkillMaster() {
				return skillMaster;
			}
			public void setSkillMaster(SkillMaster skillMaster) {
				this.skillMaster = skillMaster;
			}
       

}
