package org.egov.pims.client.common;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.utils.EgovMasterDataCaching;

public class BaseSearchAction extends DispatchAction {
	private EISServeable eisService;
 
public ActionForward search(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)throws Exception
{
	HttpSession session=req.getSession();
	  List<String> headerFields = new ArrayList<String>(); 
	 List<String> mandatoryFields = new ArrayList<String>();
	EgovMasterDataCaching egovCaching=null;
	List<AppConfigValues> appConfigList = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EIS", "EISSEARCHATTRIBUTES");
	
	for (AppConfigValues appConfig : appConfigList) {
		
			String value = appConfig.getValue();
			String header=value.substring(0, value.indexOf('|'));
			headerFields.add(header);
			String mandate = value.substring(value.indexOf('|')+1);
			if(mandate.equalsIgnoreCase("M")){
				mandatoryFields.add(header);
			}
		
	} 
	if(!headerFields.isEmpty())
	{
		egovCaching=EgovMasterDataCaching.getInstance();
	}
	if(headerFields.contains("department")){
		session.setAttribute("deptList",getEisService().getDeptsForUser());
	}
	if(headerFields.contains("functionary")){
		session.setAttribute("functionaryList", egovCaching.get("egi-functionary"));
	}
	if(headerFields.contains("function")){
		session.setAttribute("functionList", egovCaching.get("egi-function"));
	}
	session.setAttribute("headerFields",headerFields);
	session.setAttribute("mandatoryFields",mandatoryFields);
	return mapping.findForward("success");
}
public EISServeable getEisService() {
	return eisService;
}

public void setEisService(EISServeable eisService) {
	this.eisService = eisService;
}

}
