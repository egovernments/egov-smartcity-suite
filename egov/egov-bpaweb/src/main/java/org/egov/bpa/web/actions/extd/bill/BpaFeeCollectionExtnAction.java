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
package org.egov.bpa.web.actions.extd.bill;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.services.extd.bill.BpaBillExtnServiceImpl;
import org.egov.bpa.services.extd.bill.BpaBillableExtn;
import org.egov.bpa.services.extd.register.RegisterBpaExtnService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;

/**
 * 
 * @author Pradeep Kumar
 *
 */
@Transactional(readOnly = true)
@SuppressWarnings("serial")
@ParentPackage("egov")
public class BpaFeeCollectionExtnAction  extends BaseFormAction{
	
	private static final Logger LOGGER					= Logger.getLogger(BpaFeeCollectionExtnAction.class);

	private RegistrationExtn registration=null;
	private String collectXML;
	private RegisterBpaExtnService registerBpaExtnService;
	private BpaBillExtnServiceImpl bpaBillExtnServiceImpl;
	private BpaBillableExtn bpaBillableExtn;
	private Long registrationId;
	@Override
	public Object getModel() {
		return registration;
	}
	
	public Long getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(Long registrationId) {
		this.registrationId = registrationId;
	}

	public void prepare() {
		LOGGER.info("Inside Prepare method");
	}
	
	@SuppressWarnings("deprecation")
	@SkipValidation
	@Action(value = "/bpaFeeCollectionExtn-create", results = { @Result(name = "viewCollectFee",type = "dispatcher") })
	public String viewUnitCollectTax() {
		
		if(registrationId!=null)
		{
		 registration=registerBpaExtnService.getRegistrationById(registrationId);
		bpaBillableExtn.setRegistration(registration);
		collectXML = URLEncoder.encode(bpaBillExtnServiceImpl.getBillXML(bpaBillableExtn));
		}
		return "viewCollectFee";

	}
	
	public String getCollectXML() {
		return collectXML;
	}

	@SuppressWarnings("deprecation")
	public void setCollectXML(String collectXML) {
		this.collectXML = java.net.URLDecoder.decode(collectXML);
	}

	public void setRegisterBpaExtnService(RegisterBpaExtnService registerBpaService) {
		this.registerBpaExtnService = registerBpaService;
	}

	public void setBpaBillExtnServiceImpl(BpaBillExtnServiceImpl bpaBillServiceImpl) {
		this.bpaBillExtnServiceImpl = bpaBillServiceImpl;
	}

	public void setBpaBillableExtn(BpaBillableExtn bpaBillable) {
		this.bpaBillableExtn = bpaBillable;
	}


}
