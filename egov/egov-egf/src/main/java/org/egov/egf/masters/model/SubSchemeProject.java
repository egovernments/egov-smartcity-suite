/**
 * 
 */
package org.egov.egf.masters.model;

import org.egov.commons.SubScheme;
import org.egov.commons.utils.EntityType;
import org.egov.infstr.models.BaseModel;

/**
 * @author mani
 *
 */
public class SubSchemeProject extends BaseModel {
	private static final long	serialVersionUID	= 122001167280016076L;
	private SubScheme subScheme;
	//private ProjectCode projectCode;
	private Long projectCode;
	public SubScheme getSubScheme() {
		return subScheme;
	}
	public void setSubScheme(SubScheme subScheme) {
		this.subScheme = subScheme;
	}
	public Long getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(Long projectCode) {
		this.projectCode = projectCode;
	}
		
	
	
}
