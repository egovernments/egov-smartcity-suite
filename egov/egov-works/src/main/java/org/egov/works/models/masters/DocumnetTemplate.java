package org.egov.works.models.masters;

import org.egov.infstr.models.BaseModel;

/**
 * This class represents document tamplate object.
 * @author prashant.gaurav
 *
 */
public class DocumnetTemplate extends BaseModel{
	private String departmentName;
	private String template;
	
	
	/**
	 * Default constructor
	 */
	public DocumnetTemplate() {	}
	
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	
	
}
