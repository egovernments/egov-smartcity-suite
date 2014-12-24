package org.egov.pims.web.actions.empMaster;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.model.EligCertType;
import org.egov.pims.model.NomCertReqd;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.validator.annotations.Validation;
@ParentPackage("egov")
@Validation()
public class CertificationTypeMasterAction extends BaseFormAction 
{
	EligCertType certificationMstr = new EligCertType();
	private PersistenceService<EligCertType, Long> certService ;
	private String modeType;
	@Override
	public Object getModel() 
	{
	
		return certificationMstr;
	}
	@SkipValidation
	public String certificateCreate()
	{
		return NEW;
	}
	
	public CertificationTypeMasterAction()
	{
		
	}
	
	public String create()
	{
		
		if(certificationMstr.getId()==null)
		{
			certService.create(certificationMstr);
			addActionMessage(getMessage("CertificationTypeMaster.Create.Msg"));
			certificationMstr=new EligCertType();
		}
		return NEW;
	}
	
	public String update()
	{
		certService.update(certificationMstr);
		addActionMessage(getMessage("CertificationTypeMaster.Update.Msg"));
		certificationMstr=new EligCertType();
		return NEW;
	}
	
	public String remove()
	{
		NomCertReqd nomCertReqd=(NomCertReqd)getPersistenceService().find("from NomCertReqd where eligCertType.id=?",certificationMstr.getId());
		if(nomCertReqd!=null)
			addActionError(getMessage("CertificationTypeMaster.CantDelete.Msg"));
		else
		{
		certService.delete(certificationMstr);
		addActionMessage(getMessage("CertificationTypeMaster.Delete.Msg"));
		certificationMstr=new EligCertType();
		}
		return NEW;
	}
	public String view()
	{
		setModeType("view");
		addDropdownData("EligCertTypeList", getPersistenceService().findAllBy("from EligCertType cert order by cert.type"));
		return INDEX;
	}
	public String modify()
	{
		setModeType("modify");
		addDropdownData("EligCertTypeList", getPersistenceService().findAllBy("from EligCertType cert order by cert.type"));
		return INDEX;
	}
	
	public String delete()
	{
		setModeType("delete");
		addDropdownData("EligCertTypeList", getPersistenceService().findAllBy("from EligCertType cert order by cert.type"));
		return INDEX;
	}
	
	public String search()
	{
		certificationMstr = (EligCertType)persistenceService.find("from org.egov.pims.model.EligCertType where id=?",certificationMstr.getId());
		return NEW;
	}
	
	protected String getMessage(final String key) {
		return getText(key);
	}
	
	public void setCertService(PersistenceService<EligCertType, Long> certService) {
		this.certService = certService;
	}
	public EligCertType getCertificationMstr() {
		return certificationMstr;
	}
	public void setCertificationMstr(EligCertType certificationMstr) {
		this.certificationMstr = certificationMstr;
	}
	public String getModeType() {
		return modeType;
	}
	public void setModeType(String modeType) {
		this.modeType = modeType;
	}
	
}
