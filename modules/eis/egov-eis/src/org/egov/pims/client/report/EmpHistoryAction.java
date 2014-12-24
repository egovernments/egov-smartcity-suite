package org.egov.pims.client.report;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.commons.EgwStatus;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.pims.client.common.BaseSearchAction;
import org.egov.pims.reports.services.EisReportService;

public class EmpHistoryAction extends BaseSearchAction {

	@Override
	public ActionForward search(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		HttpSession  session=request.getSession();
		super.search(mapping, form, request, response); 
		//EmpHistoryForm empHistoryForm=(EmpHistoryForm)form;
		ArrayList designationMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-DesignationMaster");
		List<EgwStatus> statusMasterList=(ArrayList<EgwStatus>)EgovMasterDataCaching.getInstance().get("egEmp-EgwStatus");
		ArrayList employeeStatusMasterList	=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-EmployeeStatusMaster");
		session.setAttribute("designationMasterList", designationMasterList);
		session.setAttribute("statusMasterList", statusMasterList);
		session.setAttribute("employeeStatusMasterList"	,employeeStatusMasterList);
		return mapping.findForward("success");
	}
	
	public ActionForward historyReportDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		EmpHistoryForm empHistoryForm=(EmpHistoryForm)form;
		Map finParamsMap=new LinkedHashMap<String , Integer>();
		Integer designationId=Integer.valueOf(empHistoryForm.getDesignationId());
		Integer statusId=Integer.valueOf(empHistoryForm.getStatus());
		Integer emptypeId=Integer.valueOf(empHistoryForm.getEmpType());
		if(empHistoryForm.getDepartmentId()!=null && !empHistoryForm.getDepartmentId().isEmpty())
		finParamsMap.put("departmentId", Integer.valueOf(empHistoryForm.getDepartmentId()));
		
		if(empHistoryForm.getFunctionaryId()!=null && !empHistoryForm.getFunctionaryId().isEmpty())
			finParamsMap.put("functionaryId", Integer.valueOf(empHistoryForm.getFunctionaryId()));
		
		if(empHistoryForm.getFunctionId()!=null && !empHistoryForm.getFunctionId().isEmpty())
			finParamsMap.put("functionId", Integer.valueOf(empHistoryForm.getFunctionId()));
		
		List empList=new EisReportService().
		getEmpHistory( designationId,  empHistoryForm.getCode(),  empHistoryForm.getName(), statusId,emptypeId,  finParamsMap);
		request.setAttribute("empList", empList);
		return mapping.findForward("success");
	}
}
