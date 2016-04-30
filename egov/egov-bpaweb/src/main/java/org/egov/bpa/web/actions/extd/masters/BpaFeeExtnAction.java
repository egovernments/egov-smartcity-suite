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
package org.egov.bpa.web.actions.extd.masters;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.masters.BpaFeeDetailExtn;
import org.egov.bpa.models.extd.masters.BpaFeeExtn;
import org.egov.bpa.models.extd.masters.ServiceTypeExtn;
import org.egov.bpa.services.extd.masters.BpaFeeExtnService;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.Fund;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Transactional(readOnly = true)
@Namespace("/masters")
@ParentPackage("egov")
@SuppressWarnings("serial")
public class BpaFeeExtnAction extends BaseFormAction
{   	
	
	
	public static final String LIST = "list";
	public static final String VIEW = "view";
	BpaFeeExtn bpafeeExtn=new BpaFeeExtn();
	private BpaFeeExtnService bpaFeeExtnService;
    private String feecode;
	private Long idTemp;
	private String mode;
	private Long id;
	private Long bpafeeId;
	private List<BpaFeeDetailExtn> feedetailsList = new ArrayList<BpaFeeDetailExtn>(0);
	private BigDecimal fixedPrice;
	public BpaFeeExtnAction()
	{    
		addRelatedEntity("modifiedBy", User.class);
		addRelatedEntity("createdBy",User.class);
		addRelatedEntity("serviceType",ServiceTypeExtn.class);
		addRelatedEntity("fund", Fund.class);
		addRelatedEntity("function",CFunction.class);
		addRelatedEntity("glcode", CChartOfAccounts.class);
		
	}
	
	
	public BpaFeeExtn getModel() {
	
		return bpafeeExtn;
	}
	@SkipValidation	
	@Action(value = "/bpaFeeExtn-newform", results = { @Result(name = NEW,type = "dispatcher") })
	public String newform()
	{
		
		feedetailsList.add(new BpaFeeDetailExtn());
		return NEW;
	}
	
	@SkipValidation	
	@Action(value = "/bpaFeeExtn-search", results = { @Result(name = LIST,type = "dispatcher") })
	public String search()
	{ 
		return LIST;
	}
	
	
		@ValidationErrorPage("search")	
		@SkipValidation	
		@Action(value = "/bpaFeeExtn-modify", results = { @Result(name = NEW,type = "dispatcher") })
		public String modify()
		{
			buildFeeDetail();
			setMode(EDIT);
			return NEW;
		}
		
		@ValidationErrorPage("search")
		@SkipValidation	
		@Action(value = "/bpaFeeExtn-view", results = { @Result(name = NEW,type = "dispatcher") })
		public String view()
		{
			buildFeeDetail();
			setMode(VIEW);
			return NEW;
		}
			
		public BpaFeeExtn buildFeeDetail()
		{
			
			
			feedetailsList.clear();
			bpafeeExtn=bpaFeeExtnService.getFeeById(getIdTemp());
			feedetailsList = new ArrayList<BpaFeeDetailExtn>(bpafeeExtn.getFeedetailsesList());
			 if(feedetailsList.isEmpty()){
		    	feedetailsList.add(new BpaFeeDetailExtn());
		    }
			 
			 /*
			  * If the fees type is admission fee, and Is fixed amount flag is true, then set the value into local variable "Fixed Price".
			  */
			if(bpafeeExtn!=null && bpafeeExtn.getFeeType()!=null && bpafeeExtn.getFeeType().equalsIgnoreCase("AdmissionFee") && bpafeeExtn.getIsFixedAmount())
			{
				if(!feedetailsList.isEmpty())
				setFixedPrice(feedetailsList.get(0).getAmount());
				
			}
			 
		   return bpafeeExtn;
		}
		
		
		public Boolean validateCombination()
		{
			boolean isCodeAlreadyExist=Boolean.FALSE;
			BpaFeeExtn relation= null;
			if(bpafeeExtn.getId()==null){
			
			
			if(bpafeeExtn.getFeeType()!=null && bpafeeExtn.getServiceType().getId()!=null && bpafeeExtn.getFeeDescription()!=null)
				{
				isCodeAlreadyExist=bpaFeeExtnService.checkcombination(bpafeeExtn.getFeeType(),bpafeeExtn.getServiceType().getId(),bpafeeExtn.getFeeDescription());
	          
			}
			return isCodeAlreadyExist;
			}
			else
			{
				relation=(BpaFeeExtn)persistenceService.find("from org.egov.bpa.models.extd.masters.BpaFeeExtn where id=?" ,id);
			if(!	relation.getFeeType().equals(bpafeeExtn.getFeeType()) || ! relation.getServiceType().getId().equals(bpafeeExtn.getServiceType().getId()) || ! relation.getFeeDescription().equals(bpafeeExtn.getFeeDescription()))
			{
				return validateCombinationmodify();
			}
			}
			return isCodeAlreadyExist;
		}
		
		public boolean validateCombinationmodify()
		{

			boolean isCodeAlreadyExist=Boolean.FALSE;
			if(bpafeeExtn.getFeeType()!=null && bpafeeExtn.getServiceType().getId()!=null && bpafeeExtn.getFeeDescription()!=null)
			{
			isCodeAlreadyExist=bpaFeeExtnService.checkcombination(bpafeeExtn.getFeeType(),bpafeeExtn.getServiceType().getId(),bpafeeExtn.getFeeDescription());
          
			}
			if(isCodeAlreadyExist)
			{
				addFieldError("Descriptionexist",getMessage("bpafee.description.exist"));
			}
			return isCodeAlreadyExist;
		}
		
	    @ValidationErrorPage("search")
	    public void validate()
		{
		
	    	List<String> temp=new ArrayList<String>();
			temp.add(null);
		    feedetailsList.removeAll(temp);
		    if(validateCombination() && bpafeeExtn.getId()==null){
		    	
		    	addFieldError("Descriptionexist", getMessage("bpafee.description.exist"));
		}
	    
		if(bpafeeExtn.getFeeCode()==null || "".equals(bpafeeExtn.getFeeCode())){
			addFieldError("CODE", getMessage("bpafee.feescode.required"));
		}
		else if(bpafeeExtn.getFeeCode()!=null && !"".equals(bpafeeExtn.getFeeCode())){
			/*
			 * Check Fee code is unique in the system.
			 */
			boolean isCodeAlreadyExist=bpaFeeExtnService.checkCode(bpafeeExtn.getFeeCode(),bpafeeExtn.getId());
			if(isCodeAlreadyExist)
				addFieldError("codeAlreadyExist", getMessage("bpafee.code.exists"));
		 }
		if(bpafeeExtn.getFeeDescription()==null || "".equals(bpafeeExtn.getFeeDescription())){
			addFieldError("FeesDescription", getMessage("bpafee.description.required"));
		}
		
		if(bpafeeExtn.getFeeType()==null || bpafeeExtn.getFeeType().equals("-1")){
			addFieldError("FeesType", getMessage("bpafee.Feestype.required"));
		}
		if(bpafeeExtn.getServiceType()==null ){
			addFieldError("registration.serviceType", getMessage("bpafee.serviceType.required"));
		}
		else if(bpafeeExtn.getServiceType().getId()==null || bpafeeExtn.getServiceType().getId()==-1){
			addFieldError("registration.serviceType", getMessage("bpafee.serviceType.required"));
		}
		
		/*
		 * Check Fee Descriptin, Fee type and Service type is unique in the system.
		 */
	
		if(bpafeeExtn.getFeeDescription()!=null && !"".equals(bpafeeExtn.getFeeDescription())
				&& bpafeeExtn.getServiceType()!=null && bpafeeExtn.getServiceType().getId()!=-1 ){
			
		boolean isDescAlreadyExist=bpaFeeExtnService.checkFeeDescriptionByFeeAndServiceType(bpafeeExtn.getFeeDescription(),bpafeeExtn.getServiceType(),bpafeeExtn.getId());
		  if(isDescAlreadyExist)
			addFieldError("descAlreadyExist", getMessage("bpafee.desc.exists"));
		}
		
		if(bpafeeExtn.getFund()==null || "".equals(bpafeeExtn.getFund())){
			addFieldError("FeesFund", getMessage("bpafee.Fund.required"));
		}
		if(bpafeeExtn.getFunction()==null || "".equals(bpafeeExtn.getFunction())){
			addFieldError("FeesFunction", getMessage("bpafee.Function.required"));
		} 
		if(bpafeeExtn.getGlcode()==null ){
			addFieldError("BudgetHead", getMessage("bpafee.Glcode.required"));
		}
		else if(bpafeeExtn.getGlcode().getId()==null || bpafeeExtn.getGlcode().getId()==-1 ){
			addFieldError("BudgetHead", getMessage("bpafee.Glcode.required"));
		}
		
		 if(bpafeeExtn.getFeeType().contentEquals("AdmissionFee")){
			int i=1;
			
			if(bpafeeExtn.getIsFixedAmount()!=null && bpafeeExtn.getIsFixedAmount())
					{
					
						if(fixedPrice==null || "".equals(fixedPrice))
						{
							addFieldError("FixedAmountRequired", getMessage("bpafee.FixedAmountRequired"));
						}
							
					}else if(bpafeeExtn.getIsFixedAmount()!=null && !bpafeeExtn.getIsFixedAmount()) {
						
						for(BpaFeeDetailExtn unitDetail: feedetailsList) {
							if(unitDetail!=null){
								if(unitDetail.getAmount()==null || "".equals(unitDetail.getAmount()))
										addFieldError("Fees.detail",getMessage("bpafee.ammount.required")+" "+i);
								if(unitDetail.getFromAreasqmt()==null || "".equals(unitDetail.getFromAreasqmt()))
									addFieldError("Fees.detailFRm",getMessage("bpafee.fromsqft.required")+" "+i);
								if(unitDetail.getToAreasqmt()==null || "".equals(unitDetail.getToAreasqmt()))
									addFieldError("Fees.detailTo",getMessage("bpafee.tosqft.required")+" "+i);
								i++;
							
												}
								}
						
					}else
					{
						addFieldError("FixedAmountRequire", getMessage("bpafee.IsFixedAmount"));
					}
			}
	}

	
       private String getMessage(String key) {
	     // TODO Auto-generated method stub
	      return getText(key);
      }

   	@Action(value = "/bpaFeeExtn-create", results = { @Result(name = NEW,type = "dispatcher") })
   	@Transactional
		public String create()
	    {   
			if(bpafeeExtn.getIsActive()==null && getMode().equals(EDIT))
				bpafeeExtn.setIsActive(false);
			else
				bpafeeExtn.setIsActive(true);
			
	    	bpafeeExtn=bpaFeeExtnService.save(bpafeeExtn,feedetailsList,fixedPrice,getMode());
			
			if(getMode().equals(EDIT))
				addActionMessage(bpafeeExtn.getFeeType()+  " With Fees Code "   +bpafeeExtn.getFeeCode()+   "  Updated Successfully..");
			else
				addActionMessage(bpafeeExtn.getFeeType()+"  With Fees Code "    +bpafeeExtn.getFeeCode()+ "    Created Successfully..");
			setMode(VIEW);
	         return NEW;
	    }
	
		public void prepare()
		{
	         super.prepare();
	      addDropdownData("feesTypeList", persistenceService.findAllBy("from BpaFeeExtn order by feeCode"));
	       addDropdownData("servicetypeList", persistenceService.findAllBy("from ServiceTypeExtn order by code"));
	     //  List<CFunction> funList=persistenceService.findAllBy("from CFunction order by id");
	       addDropdownData("functionList",persistenceService.findAllBy("from CFunction order by id"));
	       addDropdownData("fundList",persistenceService.findAllBy("from Fund order by name"));
	       addDropdownData("glcodeList",persistenceService.findAllBy("from CChartOfAccounts where classification=4 and type='I' order by name"));
			addDropdownData("feeGroupList", (Arrays.asList(BpaConstants.COCFEE,BpaConstants.CMDAFEE,BpaConstants.MWGWFFEE)));
		}
		@SkipValidation
		@Action(value = "/bpaFeeExtn-codeUniqueCheck", results = { @Result(name = "codeUniqueCheck",type = "dispatcher") })
		public String codeUniqueCheck(){
			return "codeUniqueCheck" ;
		} 	
		public boolean getCodeUniqueCheck() throws Exception
		{
			return bpaFeeExtnService.checkCode(feecode,idTemp);
		}
	    public String getFeecode() {
			return feecode;
		}
		public void setFeecode(String feecode) {
			this.feecode = feecode;
		}
		public Long getIdTemp() {
			return idTemp;
		}
		public void setIdTemp(Long idTemp) {
			this.idTemp = idTemp;
		}
	
		

	public String getMode() {
		return mode;
	}


	public void setMode(String mode) {
		this.mode = mode;
	}


	public Long getBpafeeId() {
		return bpafeeId;
	}


	public BpaFeeExtn getBpafeeExtn() {
		return bpafeeExtn;
	}


	public void setBpafeeExtn(BpaFeeExtn bpafeeExtn) {
		this.bpafeeExtn = bpafeeExtn;
	}


	public BpaFeeExtnService getBpaFeeExtnService() {
		return bpaFeeExtnService;
	}


	public void setBpaFeeExtnService(BpaFeeExtnService bpaFeeExtnService) {
		this.bpaFeeExtnService = bpaFeeExtnService;
	}


	public List<BpaFeeDetailExtn> getFeedetailsList() {
		return feedetailsList;
	}


	public void setFeedetailsList(List<BpaFeeDetailExtn> feedetailsList) {
		this.feedetailsList = feedetailsList;
	}


	public void setBpafeeId(Long bpafeeId) {
		this.bpafeeId = bpafeeId;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public BigDecimal getFixedPrice() {
		return fixedPrice;
	}


	public void setFixedPrice(BigDecimal fixedPrice) {
		this.fixedPrice = fixedPrice;
	}

	
   
}
