package org.egov.pims.web.actions.nomineeCertReq;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.model.EisRelationType;
import org.egov.pims.model.EligCertType;
import org.egov.pims.model.NomCertReqd;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.validator.annotations.Validation;
 

/**
 * @author Jagadeesan *
 */
@ParentPackage("egov")
@Validation()
public class NomineeCertReqAction extends BaseFormAction{
	
	private static final long serialVersionUID = 1L;
	public static final  String VIEW="view";
	private NomCertReqd nomineeCertReq = new NomCertReqd();
	protected transient PersistenceService<EisRelationType,Long> relationService;
	protected transient PersistenceService<EligCertType,Long> certService;
	protected transient PersistenceService<NomCertReqd, Long> nomineeCertReqService;
	private List<NomCertReqd> nomineeCertReqList;
	private String mode="";
	private Integer certTypeIds[]=new Integer[0];
	private Integer certTypeIdsToAdd[]=new Integer[0];
	
	@Override
	public Object getModel() {
		return nomineeCertReq;
	}
	
	public NomineeCertReqAction() {
		addRelatedEntity("relationType", EisRelationType.class);
		addRelatedEntity("certType", EligCertType.class);
	}
	
	public String execute()
	{
		return list();
	}
	
	public void prepare()
	{
		super.prepare(); 
		addDropdownData("eisRelationTypeList",relationService.findAll());
		addDropdownData("eligCertTypeList", certService.findAll());
	}
	
	@SkipValidation
	public String list() {
		addDropdownData("eligCertTypeList", certService.findAll());
		return INDEX;
	}
	
	@SkipValidation
	public String loadToCreate()  //This called when load the page to create nominee certification required master
	{
		return NEW;
	}

	@SkipValidation
	public String loadToViewOrEdit()  //This called when load the page to nominee certification required master
	{
		List<EligCertType> certReqListForLeftSelect=new ArrayList<EligCertType>();
		List<EligCertType> certReqListForRightSelect=new ArrayList<EligCertType>();
		
		if(nomineeCertReq.getRelationType()==null)
		{
			addFieldError("relationType",getMessage("NomineeCertReq.RelationType.errMsg"));
			return list(); 
		}
		
		nomineeCertReqList = nomineeCertReqService.findAllBy("from NomCertReqd where relationType.id=? ", nomineeCertReq.getRelationType().getId());
		
		if(nomineeCertReqList!=null && !nomineeCertReqList.isEmpty())
		{
			int i=0;
			certTypeIds = new Integer[nomineeCertReqList.size()];
			for(NomCertReqd nomCertReqdItr :nomineeCertReqList) 
			{
				certTypeIds[i++]= Integer.valueOf(nomCertReqdItr.getEligCertType().getId().toString());
				certReqListForRightSelect.add(nomCertReqdItr.getEligCertType());
			}
			
			addDropdownData("certReqListForRightSelect",certReqListForRightSelect);
			
			if(("View").equals(getMode().toString()))
			{
				return VIEW;
			}
			else if(("Edit").equals(getMode().toString()))
			{
				certReqListForLeftSelect = certService.findAll();
				certReqListForLeftSelect.removeAll(certReqListForRightSelect);
				addDropdownData("certReqListForLeftSelect",certReqListForLeftSelect);
				return EDIT;
			}
		}
		else if(nomineeCertReqList.isEmpty())
		{
			if(("View").equals(getMode().toString())){
				/*if(getActionMessages().isEmpty())//when deleting all certificate type from modify mode.here will excute
				{*/
					addActionError(getMessage("NomineeCertReq.View.NoRecord.Msg")+" - "+nomineeCertReq.getRelationType().getNomineeType());
				/*}*/
			}
			else if(("Edit").equals(getMode().toString())){
				addActionError(getMessage("NomineeCertReq.Edit.NoRecord.Msg")+" - "+nomineeCertReq.getRelationType().getNomineeType());
			}
			return list(); 
		}

		return EDIT;
	}
	
	
	
	public List<NomCertReqd> getNomineeCertReqList() {
		return nomineeCertReqList;
	}

	public void setNomineeCertReqList(List<NomCertReqd> nomineeCertReqList) {
		this.nomineeCertReqList = nomineeCertReqList;
	}

	/**
	 * To create the nominee certification required master
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public String create()  throws IOException,ServletException
	{
		for(int i=0;i<certTypeIds.length;i++)
		{
			NomCertReqd tmpNomineeCertReq = new NomCertReqd();
			EligCertType tmpEligCertType = (EligCertType)certService.findById(Long.valueOf(certTypeIds[i]), false);
			tmpNomineeCertReq.setRelationType(nomineeCertReq.getRelationType());
			tmpNomineeCertReq.setEligCertType(tmpEligCertType);
			nomineeCertReqService.create(tmpNomineeCertReq);
		}
		
		if(getActionErrors().isEmpty() && getFieldErrors().isEmpty())
		{
			addActionMessage(getMessage("NomineeCertReq.Create.Success.Msg"));
		}
		//nomineeCertReqService.create(nomineeCertReq);
		
		
		setMode("View");
		return loadToViewOrEdit();
	}
	
	/**
	 * To edit the nominee certification required master
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public String edit()  throws IOException,ServletException
	{
		//Atleast one record will be there.
		nomineeCertReqList = nomineeCertReqService.findAllBy("from NomCertReqd where relationType.id=? ", nomineeCertReq.getRelationType().getId());
		
		if(nomineeCertReqList!=null && !nomineeCertReqList.isEmpty()){
			
			boolean recordToDelete=true;
			
			if(certTypeIds.length>0){
				certTypeIdsToAdd = new Integer[certTypeIds.length];
				certTypeIdsToAdd = certTypeIds;
			}

			for(NomCertReqd nomCertReqdItr :nomineeCertReqList) 
			{
				if(certTypeIds.length>0){
					for(int j=0;j<certTypeIdsToAdd.length;j++)
					{
						if(certTypeIdsToAdd[j]!=null && certTypeIdsToAdd[j].intValue()==Integer.valueOf(nomCertReqdItr.getEligCertType().getId().toString()).intValue())
						{
							recordToDelete =false;
							certTypeIdsToAdd[j]=null;
							break;
						}
					}
				}
				
				//Record to delete
				if(recordToDelete)
				{
					nomineeCertReqService.delete(nomCertReqdItr);
				}
			}
			
			//Records to add
			if(certTypeIds.length>0){
				for(int k=0;k<certTypeIdsToAdd.length;k++)
				{
					if(certTypeIdsToAdd[k]!=null)
					{
						NomCertReqd tmpNomineeCertReq = new NomCertReqd();
						EligCertType tmpEligCertType = (EligCertType)certService.findById(Long.valueOf(certTypeIdsToAdd[k]), false);
						tmpNomineeCertReq.setRelationType(nomineeCertReq.getRelationType());
						tmpNomineeCertReq.setEligCertType(tmpEligCertType);
						nomineeCertReqService.create(tmpNomineeCertReq);
					}
				}
			}
			
		}
		
		if(getActionErrors().isEmpty() && getFieldErrors().isEmpty())
		{
			addActionMessage(getMessage("NomineeCertReq.Edit.Success.Msg"));
		}
		
		if(certTypeIds.length==0)//It means, trying to delete all certificate type for nominee.so forwarding to create page.
		{
			nomineeCertReq.setRelationType(null);
			return loadToCreate();
			
		}
		
		setMode("View");
		return loadToViewOrEdit();
		
	}
	 
	public void validate()
	{
		if(nomineeCertReq.getRelationType()==null){
			addFieldError("relationType", getMessage("NomineeCertReq.RelationType.errMsg"));
		}
		if(certTypeIds.length==0 && !("Edit").equals(getMode().toString()))
		{
			addFieldError("certTypeIds", getMessage("NomineeCertReq.CertType.errMsg"));
		}
		
		if(("Create").equals(getMode().toString()) && nomineeCertReq.getRelationType()!=null &&  certTypeIds.length>0)
		{
			nomineeCertReqList = nomineeCertReqService.findAllBy("from NomCertReqd where relationType.id=? ", nomineeCertReq.getRelationType().getId());
			
			if(!nomineeCertReqList.isEmpty())
			{
				addActionError(getMessage("NomineeCertReq.Create.RecordExist.Msg")+" - "+nomineeCertReq.getRelationType().getNomineeType());
				nomineeCertReq.setRelationType(null);
			}
		}
		
	}
	
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public Integer[] getCertTypeIds() {
		return certTypeIds;
	}

	public void setCertTypeIds(Integer[] certTypeIds) {
		this.certTypeIds = certTypeIds;
	}
	
	public PersistenceService<NomCertReqd, Long> getNomineeCertReqService() {
		return nomineeCertReqService;
	}

	public void setRelationService(
			PersistenceService<EisRelationType, Long> relationService) {
		this.relationService = relationService;
	}

	public void setCertService(PersistenceService<EligCertType, Long> certService) {
		this.certService = certService;
	}

	public void setNomineeCertReq(NomCertReqd nomineeCertReq) {
		this.nomineeCertReq = nomineeCertReq;
	}

	public void setNomineeCertReqService(
			PersistenceService<NomCertReqd, Long> nomineeCertReqService) {
		this.nomineeCertReqService = nomineeCertReqService;
	}

	protected String getMessage(final String key) {
		return getText(key);
	}
}