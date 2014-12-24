package org.egov.pims.web.actions.empMaster;

import java.sql.BatchUpdateException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.model.EisRelationType;
import org.egov.pims.model.EligCertType;
import org.egov.web.actions.BaseFormAction;

public class RelationTypeMasterAction extends BaseFormAction 
{
	EisRelationType relationMstr = new EisRelationType();
	private PersistenceService<EisRelationType, Integer> relationService ;
	private String modeType;
	@Override
	public Object getModel() 
	{
		return relationMstr;
	}
	@SkipValidation
	public String relationCreate()
	{
		return NEW;
	}
	public RelationTypeMasterAction()
	{
		
	}
	public String create()
	{
		if(relationMstr.getId()==null)
		{
			
			getRelationService().create(relationMstr);
			addActionMessage(getMessage("RelationTypeMaster.Create.Msg"));
			relationMstr=new EisRelationType();
		}
		return NEW;
	}
	
	public String update()
	{
		relationService.update(relationMstr);
		addActionMessage(getMessage("RelationTypeMaster.Update.Msg"));
		relationMstr=new EisRelationType();
		return NEW;
	}
	
	public String remove()
	{
		try
		{
			relationService.delete(relationMstr);
			//FIXME:should be removed once NomineeMaster is done
			HibernateUtil.getCurrentSession().flush();
		}
		catch(Exception e)
		{
			addActionError("Relation Type Is already in Use!!");
			return NEW;
		}
		addActionMessage(getMessage("RelationTypeMaster.Delete.Msg"));
		relationMstr=new EisRelationType();
		return NEW;
	}
	public String view()
	{
		setModeType("view");
		addDropdownData("EisRelationTypeList", getPersistenceService().findAllBy("from EisRelationType rel order by rel.nomineeType"));
		return INDEX;
	}
	public String modify()
	{
		setModeType("modify");
		addDropdownData("EisRelationTypeList", getPersistenceService().findAllBy("from EisRelationType rel order by rel.nomineeType"));
		return INDEX;
	}
	
	public String delete()
	{
		setModeType("delete");
		addDropdownData("EisRelationTypeList", getPersistenceService().findAllBy("from EisRelationType rel order by rel.nomineeType"));
		return INDEX;
	}
	public String search()
	{
		relationMstr = (EisRelationType)persistenceService.find("from org.egov.pims.model.EisRelationType where id=?",relationMstr.getId());
		return NEW;
	}
	protected String getMessage(final String key) {
		return getText(key);
	}
	public void setRelationService(
			PersistenceService<EisRelationType, Integer> relationService) {
		
		this.relationService = relationService;
	}
	
	public void setRelationMstr(EisRelationType relationMstr) {
		this.relationMstr = relationMstr;
	}
	public PersistenceService<EisRelationType, Integer> getRelationService() {
		return relationService;
	}
	public String getModeType() {
		return modeType;
	}
	public void setModeType(String modeType) {
		this.modeType = modeType;
	}
}
