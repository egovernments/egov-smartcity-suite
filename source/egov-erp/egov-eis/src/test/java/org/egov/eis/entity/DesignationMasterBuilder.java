/**
 * 
 */
package org.egov.eis.entity;

import org.egov.commons.CChartOfAccounts;
import org.egov.pims.commons.DesignationMaster;

/**
 * @author Vaibhav.K
 *
 */
public class DesignationMasterBuilder {

	private final DesignationMaster designationMaster;
	
	private static int count=0;
	
	public DesignationMasterBuilder(){
		designationMaster = new DesignationMaster();
		count++;
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
		 designationMaster.setDesignationId(designationId);
	        return this;
	    }
	
	public DesignationMasterBuilder withDefaults()
	{
	    if(null==designationMaster.getDesignationId())
	    {
	        withId(count);
	    }
	    if(null==designationMaster.getDesignationName())
	    {
	        withName("test-designation-"+count);
	    }
//	    if(null==designationMaster.getChartOfAccounts())
//            {
//                withChartOfAccounts(new CChar);
//            }
	    
	    return this;
	}
	
	
	public DesignationMasterBuilder withDbDefaults()
        {
          
            if(null==designationMaster.getDesignationName())
            {
                withName("test-designation-"+count);
            }
            return this;
        }
	
}
