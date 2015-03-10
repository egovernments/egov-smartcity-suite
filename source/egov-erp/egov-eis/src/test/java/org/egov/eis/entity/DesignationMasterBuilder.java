/**
 * 
 */
package org.egov.eis.entity;

import java.lang.reflect.Field;

import org.egov.commons.CChartOfAccounts;
import org.egov.pims.commons.DesignationMaster;

/**
 * @author Vaibhav.K
 *
 */
public class DesignationMasterBuilder {

	private final DesignationMaster designationMaster;
	
	public DesignationMasterBuilder(){
		designationMaster = new DesignationMaster();
	}
	
	public DesignationMaster build(){
		return designationMaster;
	}
	
	public DesignationMasterBuilder withName(String designationName){
		designationMaster.setDesignationName(designationName);
		return this;
	}
	
	public DesignationMasterBuilder withDescription(String designationDescription){
		designationMaster.setDesignationDescription(designationDescription);
		return this;
	}
	
	public DesignationMasterBuilder withChartOfAccounts(CChartOfAccounts glcode){
		designationMaster.setChartOfAccounts(glcode);
		return this;
	}
	
	public DesignationMasterBuilder withId(Integer designationId){
		 try {
	            Field idField  = designationMaster.getClass().getSuperclass().getDeclaredField("designationId");
	            idField.setAccessible(true);
	            idField.set(designationMaster, designationId);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	        return this;
	    }
}
