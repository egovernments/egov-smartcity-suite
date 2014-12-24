package org.egov.payroll.client.payslip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.lib.admbndry.CityWebsite;
import org.egov.lib.admbndry.CityWebsiteDAO;
import org.egov.lib.rjbac.dept.Department;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.services.payslip.PayRollService;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.model.Assignment;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class PayslipInfoAction extends Action {
	private static final Logger LOGGER = Logger.getLogger(PayslipInfoAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		try {
			PayRollService payRollService = PayrollManagersUtill.getPayRollService();
			String payslipId = request.getParameter("payslipId");
			EmpPayroll payslip = payRollService.getPayslipById(Long.parseLong(payslipId));
			Assignment ass= payslip.getEmpAssignment();
			Department dep=ass.getDeptId();
			DesignationMaster des= ass.getDesigId();
		     HibernateTemplate htm = new HibernateTemplate();
			htm.initialize(des);
			htm.initialize(dep);
			String nam= dep.getDeptName();
			String cityurl=(String)request.getSession().getAttribute("cityurl");
			CityWebsite cityWebsite=null;
			if(cityurl!=null)
			{
				cityWebsite = new CityWebsiteDAO().getCityWebSiteByURL(cityurl);
				request.getSession().setAttribute("cityLogo",cityWebsite.getLogo());
			}
			LOGGER.info("sssssssssssssssssoooooooooooooooumendesignation----"+ass+"dep"+dep+"nam"+nam);
			request.getSession().setAttribute("depart", dep);
			request.getSession().setAttribute("desig", des);
			request.getSession().setAttribute("paySlip", payslip);	
			return mapping.findForward("success");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return mapping.findForward("error");
		}
	}
}
