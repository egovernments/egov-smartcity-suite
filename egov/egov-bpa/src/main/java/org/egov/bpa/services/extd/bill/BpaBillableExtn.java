/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.bpa.services.extd.bill;

import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.BpaAddressExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.model.AbstractBillable;
import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.egov.bpa.constants.BpaConstants.BPAMODULENAME;

@SuppressWarnings("unchecked")
public class BpaBillableExtn  extends AbstractBillable  {

	
	public static final String AUTO = "AUTO";
	public static final String WARD = "Ward";
	public static final String ADDRESSTYPEASOWNER = "OWNER";
	private PersistenceService persistenceService;
	private RegistrationExtn registration;
	@Autowired
	@Qualifier(value = "moduleDAO")
	private ModuleService moduleDao;
	private BpaCommonExtnService bpaCommonExtnService;	
	@Autowired
	@Qualifier(value = "egBillDAO")
	private EgBillDao egBillDao;
	@Override
	public String getBillPayee() {
		StringBuffer billPayee = new StringBuffer();
		if(registration!=null && registration.getOwner()!=null )
		{
			billPayee.append(((registration.getOwner().getName()==null)?"":""+registration.getOwner().getName()));
			billPayee.append(((registration.getOwner().getName()==null)?"":" "+registration.getOwner().getName()));	
			billPayee.append(((registration.getOwner().getName()==null)?"":" "+registration.getOwner().getName()));
		}
		return (billPayee!=null?billPayee.toString():" ");
	}

	@Override
	public String getBillAddress() {
		StringBuffer billingAddress= new StringBuffer();
		if(registration!=null && registration.getBpaAddressSet()!=null && registration.getBpaAddressSet().size()>0)
		{
			for(BpaAddressExtn bpaAddress: registration.getBpaAddressSet())
			{
				if(bpaAddress.getAddressTypeMaster()!=null && bpaAddress.getAddressTypeMaster().equals(ADDRESSTYPEASOWNER))
				{
					billingAddress.append(((bpaAddress.getStreetAddress1()==null)?"":""+bpaAddress.getStreetAddress1()));
					billingAddress.append(((bpaAddress.getStreetAddress2()==null)?" ":","+bpaAddress.getStreetAddress2()));
					billingAddress.append(bpaAddress.getVillageName()!=null&&bpaAddress.getVillageName().getName()!=null?","+bpaAddress.getVillageName().getName():" ");
					//billingAddress.append(((bpaAddress.getVillageName().getName()==null)?" ":","+bpaAddress.getVillageName().getName()));
					billingAddress.append(((bpaAddress.getCityTown()==null)?" ":","+bpaAddress.getCityTown()));
					billingAddress.append(((bpaAddress.getIndianState()==null)?"":","+bpaAddress.getIndianState()));
					billingAddress.append(((bpaAddress.getPincode()==null)?"":","+bpaAddress.getPincode()));
			
				}
			}
		}
		
		return billingAddress.toString();
	}

	@Override
	public EgDemand getCurrentDemand() {
		
		return (registration!=null?registration.getEgDemand():null);
	}

	@Override
	public List<EgDemand> getAllDemands() {
		List<EgDemand> demands = new ArrayList<EgDemand>();
		demands.add(getCurrentDemand());
		return demands;
	}

	@Override
	public EgBillType getBillType() {
		
		  
		     return egBillDao.getBillTypeByCode(AUTO);
	}

	@Override
	public Date getBillLastDueDate() {
		 return (DateUtils.today());
	}

	@Override
	public Long getBoundaryNum() {
		if(registration!=null && registration.getAdminboundaryid()!=null)
		{
			if(registration.getAdminboundaryid().getBoundaryType().getName().equals("Zone"))
				return 	registration.getAdminboundaryid().getParent().getId();
			else
				return registration.getAdminboundaryid().getId();
		} 
		return null;
	}

	@Override
	public String getBoundaryType() {
		return ADDRESSTYPEASOWNER;
	}

	@Override
	public String getDepartmentCode() {
		String deptCode=(bpaCommonExtnService.getAppconfigValueResult(BpaConstants.BPAMODULENAME, BpaConstants.DEPARTMENT_CODE, null));
		if(deptCode!=null && !deptCode.equals(""))
	          return (deptCode);
	    else return null;   
	}

	@Override
	public BigDecimal getFunctionaryCode() {
		//TODO: GET THIS INFO FROM FEE MASTER
		return BigDecimal.ZERO;
	}

	@Override
	public String getFundCode() {
		 String fundCode=(bpaCommonExtnService.getAppconfigValueResult(BpaConstants.BPAMODULENAME, BpaConstants.FUND_CODE, null));
	        if(fundCode!=null && !fundCode.equals(""))
	          return (fundCode);
	        else return null;
	}

	@Override
	public String getFundSourceCode() {
		//TODO: GET THIS INFO FROM FEE MASTER
		return "";
	}

	@Override
	public Date getIssueDate() {
		 return new Date();
	}

	@Override
	public Date getLastDate() {
		return getBillLastDueDate();
	}

	@Override
	public Module getModule() {
		
		Module module = moduleDao.getModuleByName(BPAMODULENAME);
		 return  module;
     }

	@Override
	public Boolean getOverrideAccountHeadsAllowed() {
		return Boolean.FALSE;
	}

	@Override
	public Boolean getPartPaymentAllowed() {
		return Boolean.FALSE;
	}

	@Override
	public String getServiceCode() {
		//TODO: CHECK AGAIN
		return BpaConstants.EXTD_SERVICE_CODE;
	}

	@Override
	public BigDecimal getTotalAmount() {
		BigDecimal balance = BigDecimal.ZERO;
			
		if(registration!=null && registration.getEgDemand()!=null)
		{
			for (EgDemandDetails det : registration.getEgDemand().getEgDemandDetails()) {
				BigDecimal dmdAmt = det.getAmount();
				BigDecimal collAmt=det.getAmtCollected();
				balance = balance.add(dmdAmt.subtract(collAmt));
					}
				}
		return balance;
	}

	@Override
	public Long getUserId() {
		
		return (EgovThreadLocals.getUserId()==null?null:Long.valueOf(EgovThreadLocals.getUserId()));
	}

	@Override
	public String getDescription() {
		StringBuffer description = new StringBuffer();
		
		if(registration!=null && registration.getPlanSubmissionNum()!=null)
		{
			description.append(BpaConstants.FEECOLLECTIONMESSAGE);
			description.append((registration.getPlanSubmissionNum()!=null?registration.getPlanSubmissionNum():""));
		}
		return description.toString();
	}

	@Override
	public String getDisplayMessage() {
		 return BpaConstants.RENT_COLLECTION;
	}

	@Override
	public String getCollModesNotAllowed() {
		
		return "cheque,cash,bankchallan";
	}

	@Override
	public String getPropertyId() {

		if(registration!=null)
		{
			return registration.getId().toString(); //TODO: CHECK THIS.
		}
		return null;
	}

	@Override
	public Boolean isCallbackForApportion() {
		   return Boolean.FALSE; //TODO: CHECK THIS
	}

	@Override
	public void setCallbackForApportion(Boolean b) {
		 throw new IllegalArgumentException("Apportioning is always TRUE and shouldn't be changed");
				
	}

/*	@Override
	public void setCollectionType(String collType) {
		// TODO Auto-generated method stub
		//CHECK THIS
	}

	@Override
	public String getCollectionType() {
		return DemandConstants.COLLECTIONTYPE_COUNTER;
	}

	@Override
	public void setPaymentGatewayType(String pgType) {
		// TODO Auto-generated method stub
		
	}
//TODO PHIOnix removing all cos no API in AbstractBillable
	@Override
	public String getPaymentGatewayType() {
		// TODO Auto-generated method stub
		return null;
	}*/

	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public BpaCommonExtnService getBpaCommonExtnService() {
		return bpaCommonExtnService;
	}

	public void setBpaCommonExtnService(BpaCommonExtnService bpaCommonService) {
		this.bpaCommonExtnService = bpaCommonService;
	}

	public RegistrationExtn getRegistration() {
		return registration;
	}

	public void setRegistration(RegistrationExtn registration) {
		this.registration = registration;
	}

}
