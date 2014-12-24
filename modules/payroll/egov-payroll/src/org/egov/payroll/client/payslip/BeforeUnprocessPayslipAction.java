package org.egov.payroll.client.payslip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.infstr.services.EISServeable;

public class BeforeUnprocessPayslipAction extends Action {
	

		private EISServeable eisService;
		public ActionForward execute(ActionMapping actionMapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response)throws Exception
		{
		
			String target="success";
			request.getSession().setAttribute("deptList", getEisService().getDeptsForUser());		
			return actionMapping.findForward(target);
		}
		public EISServeable getEisService() {
			return eisService;
		}
		public void setEisService(EISServeable eisService) {
			this.eisService = eisService;
		}

	
}
