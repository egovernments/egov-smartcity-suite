package org.egov.billsaccounting.client;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.RBACException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;

public class SupplierBillAction extends DispatchAction {
	public final static Logger LOGGER = Logger.getLogger(WorksBillAction.class);
	private UserService userService;
	private static final String error = "error";
	private static final String success = "success";

	public ActionForward beforeCreate(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		String target = success;
		String beforeCreateExpMsg = "Exception in beforeCreate ...";

		try {
			SupplierBillForm sbForm = (SupplierBillForm) form;
			sbForm.reset(mapping, req);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date billdate = new Date();
			String billDatestr = sdf.format(billdate);
			LOGGER.debug(billDatestr);
			sbForm.setBillDate(billDatestr);
			req.setAttribute("SupplierBillForm", sbForm);
			int userId = ((Integer) req.getSession().getAttribute(
					"com.egov.user.LoginUserId")).intValue();
			sbForm.setUserId(userId);

			User user = (User) userService.getUserByID(userId);
			String username = user.getUserName();
			LOGGER.debug(username);
			sbForm.setUserName(username);

			SupplierBillDelegate sbDelegate = new SupplierBillDelegate();
			List deptList = sbDelegate.getAllActiveDepartments();
			List netPayList = sbDelegate.getNetPayList();
			req.setAttribute("netPayList", netPayList);
			req.setAttribute("deptList", deptList);
			req.setAttribute("mode", "create");
		} catch (EGOVRuntimeException e) {
			target = error;
			LOGGER.error(beforeCreateExpMsg + "" + e.getMessage());
			HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage());
		}
		return mapping.findForward(target);
	}

	public ActionForward getTdsAndotherdtls(ActionMapping mapping,
			ActionForm form, HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		String target = "";
		try {
			SupplierBillForm sbForm = (SupplierBillForm) form;
			// get Contractor or Supplier name and tds List
			SupplierBillDelegate sbDelegate = new SupplierBillDelegate();
			sbForm = sbDelegate.getTds(sbForm);
			req.setAttribute("tdsList", sbForm.getTdsList());
			req.setAttribute("SupplierBillForm", sbForm);
			List netPayList = sbDelegate.getNetPayList();
			req.setAttribute("netPayList", netPayList);
			List deptList = sbDelegate.getAllActiveDepartments();
			req.setAttribute("deptList", deptList);
			req.setAttribute("mode", req.getParameter("mode"));
			target = success;
		} catch (Exception e) {
			target = "error";
			LOGGER.error("Exception in getTdsAndotherdtls !!!");
			HibernateUtil.rollbackTransaction();
		}
		return mapping.findForward(target);
	}

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		String target = "";
		String alertMessage = null;
		String billCreation = null;
		List deptList = null;
		List netPayList = null;
		try {
			LOGGER.debug("Inside search");
			req.setAttribute("mode", "create");
			SupplierBillForm sbForm = (SupplierBillForm) form;
			// String buttonType=(String)req.getParameter("buttonType");
			String buttonType = sbForm.getButtonType();
			req.setAttribute("buttonType", buttonType);
			SupplierBillDelegate sbDelegate = new SupplierBillDelegate();
			deptList = sbDelegate.getAllActiveDepartments();
			netPayList = sbDelegate.getNetPayList();
			int userId = ((Integer) req.getSession().getAttribute(
					"com.egov.user.LoginUserId")).intValue();
			sbForm.setUserId(userId);
			LOGGER.debug("CodeLists" + sbForm.codeList);
			int result = sbDelegate.postInEgBillRegister(sbForm);
			if (result == 1) {
				alertMessage = "Bill cannot be Created Bill Amount Exceeds The Total WorkOrder Amount ";
				billCreation = "failed";
			} else {
				alertMessage = "Bill " + sbForm.getBillNo()
						+ "  Succesfully created";
				billCreation = "success";
			}

			req.setAttribute("billCreation", billCreation);
			req.setAttribute("alertMessage", alertMessage);
			req.setAttribute("netPayList", netPayList);

			req.setAttribute("deptList", deptList);
			target = "success";
		} catch (EGOVRuntimeException e) {
			target = success;
			alertMessage = e.getMessage();
			req.setAttribute("deptList", deptList);
			req.setAttribute("alertMessage", alertMessage);
			LOGGER.error("Error While Creating the Bill");

			HibernateUtil.rollbackTransaction();
		} catch (Exception e) {
			target = success;
			alertMessage = e.getMessage();
			req.setAttribute("deptList", deptList);
			req.setAttribute("alertMessage", alertMessage);
			LOGGER.error("Error While Creating the Bill");

			HibernateUtil.rollbackTransaction();
		}

		return mapping.findForward(target);
	}

	public ActionForward beforeViewModify(ActionMapping mapping,
			ActionForm form, HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		String target = "success";

		try {
			LOGGER.debug("Inside BeforeModify");
			String mode = "modify";
			mode = req.getParameter("mode");

			req.setAttribute("mode", mode);
			SupplierBillForm sbForm = (SupplierBillForm) form;
			// billNumber=1040&showMode=modify&expType=Works
			String billRegId = req.getParameter("billId");
			LOGGER.debug("billRegId" + billRegId);
			sbForm.setBillId(billRegId);
			SupplierBillDelegate sbDelegate = new SupplierBillDelegate();
			int userId = ((Integer) req.getSession().getAttribute(
					"com.egov.user.LoginUserId")).intValue();
			sbForm.setUserId(userId);
			LOGGER.debug("CodeLists" + sbForm.codeList);
			sbForm = sbDelegate.getEgBillRegister(sbForm);
			req.setAttribute("SupplierBillForm", sbForm);

			req.setAttribute("tdsList", sbForm.getTdsList());

			List netPayList = sbDelegate.getNetPayList();
			req.setAttribute("netPayList", netPayList);
			List deptList = sbDelegate.getAllActiveDepartments();
			req.setAttribute("deptList", deptList);

			target = "success";

		} catch (Exception ex) {
			target = "error";
			LOGGER.error("Exception Encountered!!!" + ex.getMessage());
			HibernateUtil.rollbackTransaction();
			// throw new EGOVRuntimeException(ex.getMessage(),ex);

		}
		return mapping.findForward(target);
	}

	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		String target = "";
		String alertMessage = null;
		String billCreation = null;
		String modifyExpMsg = "Exception while modifying ...";
		try {
			LOGGER.debug("Inside BeforeModify");
			String mode = "modify";
			mode = req.getParameter("mode");
			req.setAttribute("mode", mode);
			int userId = ((Integer) req.getSession().getAttribute(
					"com.egov.user.LoginUserId")).intValue();

			SupplierBillForm sbForm = (SupplierBillForm) form;
			sbForm.setUserId(userId);
			SupplierBillDelegate delgate = new SupplierBillDelegate();
			int result = delgate.modify(sbForm);
			target = "success";
			if (result == 1) {
				alertMessage = "Bill cannot be Created Bill Amount Exceeds The Total WorkOrder Amount ";
				billCreation = "failed";
			} else {
				alertMessage = " Bill  " + sbForm.getBillNo()
						+ "  Succesfully Modified";
				billCreation = "success";
				// HibernateUtil.commitTransaction();
			}
			req.setAttribute("billCreation", billCreation);
			req.setAttribute("alertMessage", alertMessage);
			req.setAttribute("tdsList", sbForm.getTdsList());
			req.setAttribute("SupplierBillForm", sbForm);
			List deptList = delgate.getAllActiveDepartments();
			List netPayList = delgate.getNetPayList();
			req.setAttribute("netPayList", netPayList);
			req.setAttribute("deptList", deptList);
			req.setAttribute("mode", "modify");
			String buttonType = sbForm.getButtonType();
			req.setAttribute("buttonType", buttonType);

		} catch (Exception e) {
			target = "error";
			alertMessage = "Exception while Modifying the  Bill... bill modification  failed";
			LOGGER.error(modifyExpMsg);
			HibernateUtil.rollbackTransaction();
			// throw new EGOVRuntimeException(e.getMessage(),e);

		}
		LOGGER.debug("forwarding to " + target + "...");
		return mapping.findForward(target);

	}

	public ActionForward approve(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		String target = "";
		String approveExpMsg = "Exception in approve...";

		try {
			LOGGER.debug("Inside Approve");
			String mode = "modify";
			mode = req.getParameter("mode");
			req.setAttribute("mode", mode);
			int userId = ((Integer) req.getSession().getAttribute(
					"com.egov.user.LoginUserId")).intValue();
			SupplierBillForm sbForm = (SupplierBillForm) form;
			sbForm.setUserId(userId);
			SupplierBillDelegate delgate = new SupplierBillDelegate();
			// delgate.modify(sbForm);
			LOGGER.debug("Bill Modified Succesfully.");

			LOGGER.debug("Before Voucher Creation...");

			delgate.gnerateVoucher(Integer.parseInt(sbForm.getBillId()),
					userId, sbForm.getVoucherHeader_narration(), null);

			target = "success";
			String alertMessage = "Bill Succesfully Modified And Approved VoucherNumber ";
			req.setAttribute("alertMessage", alertMessage);
			req.setAttribute("tdsList", sbForm.getTdsList());
			req.setAttribute("SupplierBillForm", sbForm);
			List deptList = delgate.getAllActiveDepartments();
			List netPayList = delgate.getNetPayList();
			req.setAttribute("netPayList", netPayList);
			req.setAttribute("deptList", deptList);
			req.setAttribute("buttonType", "close");
			req.setAttribute("mode", "approve");
		} catch (NumberFormatException e1) {
			target = "error";
			LOGGER.error(approveExpMsg + "" + e1.getMessage());
			HibernateUtil.rollbackTransaction();
			HibernateUtil.rollbackTransaction();
		} catch (EGOVRuntimeException e1) {
			target = "error";
			LOGGER.error(approveExpMsg + "" + e1.getMessage());
			HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e1.getMessage());
		} catch (SQLException e1) {
			target = "error";
			LOGGER.error(approveExpMsg + "" + e1.getMessage());
			HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e1.getMessage());

		}

		LOGGER.debug("  forwarding to target ....");
		return mapping.findForward(target);

	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
