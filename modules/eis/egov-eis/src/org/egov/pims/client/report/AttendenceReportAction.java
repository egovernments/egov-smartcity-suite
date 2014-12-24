package org.egov.pims.client.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFinancialYear;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.pims.client.common.BaseSearchAction;
import org.egov.pims.utils.EisManagersUtill;

public class AttendenceReportAction extends BaseSearchAction {
	@Override
	public ActionForward search(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		super.search(mapping, form, request, response); 
		HttpSession  session=request.getSession();
		ArrayList designationMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-DesignationMaster");
		session.setAttribute("designationMasterList", designationMasterList);
		List fYMasterList=EisManagersUtill.getCommonsService().getAllFinancialYearList();
		
		session.setAttribute("finMap",getFinMap(fYMasterList));
			
		
		return mapping.findForward("success");
	}

	/**
	 * @param fYMasterList
	 */
	private Map getFinMap(List fYMasterList) {
		Map<Long,String> finMap = new TreeMap<Long,String>();
		try {
			for(Iterator iter = fYMasterList.iterator();iter.hasNext();)
			{
				CFinancialYear cFinancialYear = (CFinancialYear)iter.next();
				finMap.put(cFinancialYear.getId(), cFinancialYear.getFinYearRange());
			}
		} catch (Exception e) {
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
		return finMap;
	}
}
