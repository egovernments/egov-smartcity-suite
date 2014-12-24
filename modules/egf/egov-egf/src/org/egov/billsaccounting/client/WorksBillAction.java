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

public class WorksBillAction extends DispatchAction {
	private UserService userService;
	public final static Logger LOGGER = Logger.getLogger(WorksBillAction.class);
	public final static String LoginUserId = "com.egov.user.LoginUserId";
	public final String TARGETSUCESS = "success";
	public final String TARGETERROR = "error";
	public final String MODESTRING = "mode";
	public final String FORMNAME = "WorksBillForm";
	public final String NETPAYLIST = "netPayList";
	public final String DEPTLIST = "deptList";
	public final String TDSLIST = "tdsList";
	public final String ALERTMESSAGE = "alertMessage";
	public final String MODIFYMODE = "modify";

	public ActionForward beforeCreate(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		String target = TARGETSUCESS;

		try {
			WorksBillForm wbForm = (WorksBillForm) form;
			wbForm.reset(mapping, req);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date billdate = new Date();
			String billDatestr = sdf.format(billdate);
			LOGGER.info(billDatestr);
			wbForm.setBillDate(billDatestr);
			req.setAttribute(FORMNAME, wbForm);
			int userId = ((Integer) req.getSession().getAttribute(LoginUserId))
					.intValue();
			wbForm.setUserId(userId);

			User user = (User) userService.getUserByID(userId);
			String username = user.getUserName();
			LOGGER.info(username);
			wbForm.setUserName(username);

			WorksBillDelegate wbDelegate = new WorksBillDelegate();
			List deptList = wbDelegate.getAllActiveDepartments();
			List netPayList = wbDelegate.getNetPayList();
			req.setAttribute(NETPAYLIST, netPayList);
			req.setAttribute(DEPTLIST, deptList);
			req.setAttribute(MODESTRING, "create");
		} catch (EGOVRuntimeException e) {
			target = TARGETERROR;
			LOGGER.error("Exception in getTdsAndotherdtls !!!");
			HibernateUtil.rollbackTransaction();
			throw e;
		}

		return mapping.findForward(target);
	}

	public ActionForward getTdsAndotherdtls(ActionMapping mapping,
			ActionForm form, HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		String target = "";
		try {
			WorksBillForm wbForm = (WorksBillForm) form;
			// get Contractor or Supplier name and tds List
			WorksBillDelegate wbDelegate = new WorksBillDelegate();
			wbForm = wbDelegate.getTds(wbForm);
			req.setAttribute(TDSLIST, wbForm.getTdsList());
			req.setAttribute(FORMNAME, wbForm);
			List netPayList = wbDelegate.getNetPayList();
			req.setAttribute(NETPAYLIST, netPayList);
			List deptList = wbDelegate.getAllActiveDepartments();
			req.setAttribute(DEPTLIST, deptList);
			req.setAttribute(MODESTRING, req.getParameter(MODESTRING));
			target = TARGETSUCESS;
		} catch (Exception e) {
			target = TARGETERROR;
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
			LOGGER.info("Inside search");
			req.setAttribute(MODESTRING, "create");
			WorksBillForm wbForm = (WorksBillForm) form;
			// String buttonType=(String)req.getParameter("buttonType");
			String buttonType = wbForm.getButtonType();
			req.setAttribute("buttonType", buttonType);
			WorksBillDelegate wbDelegate = new WorksBillDelegate();
			deptList = wbDelegate.getAllActiveDepartments();
			netPayList = wbDelegate.getNetPayList();
			int userId = ((Integer) req.getSession().getAttribute(LoginUserId))
					.intValue();
			wbForm.setUserId(userId);
			LOGGER.info("CodeLists" + wbForm.codeList);
			int result = wbDelegate.postInEgBillRegister(wbForm);
			if (result == 1) {
				alertMessage = "Bill cannot be Created Bill Amount Exceeds The Total WorkOrder Amount ";
				billCreation = "failed";
			} else {
				alertMessage = "Bill " + wbForm.getBillNo()
						+ "  Succesfully created";
				billCreation = TARGETSUCESS;
			}

			req.setAttribute("billCreation", billCreation);
			req.setAttribute(ALERTMESSAGE, alertMessage);
			req.setAttribute(NETPAYLIST, netPayList);

			req.setAttribute(DEPTLIST, deptList);
			target = TARGETSUCESS;
		}
		/*
		 * catch(RBACException e) { target=TARGETSUCESS;
		 * alertMessage=e.getMessage(); req.setAttribute(DEPTLIST, deptList);
		 * req.setAttribute(ALERTMESSAGE,alertMessage);
		 * LOGGER.error("Error While Creating the Bill");
		 * 
		 * HibernateUtil.rollbackTransaction(); }
		 */
		catch (Exception ex) {
			target = TARGETSUCESS;
			alertMessage = ex.getMessage();
			req.setAttribute(DEPTLIST, deptList);
			req.setAttribute(ALERTMESSAGE, alertMessage);
			LOGGER.error("Error While Creating the Bill");

			HibernateUtil.rollbackTransaction();
		}
		return mapping.findForward(target);
	}

	public ActionForward beforeViewModify(ActionMapping mapping,
			ActionForm form, HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		String target = TARGETSUCESS;

		try {
			LOGGER.info("Inside BeforeModify");
			String mode = MODIFYMODE;
			mode = req.getParameter(MODESTRING);

			req.setAttribute(MODESTRING, mode);
			WorksBillForm wbForm = (WorksBillForm) form;
			// billNumber=1040&showMode=modify&expType=Works
			String billRegId = req.getParameter("billId");
			LOGGER.info("billRegId" + billRegId);
			wbForm.setBillId(billRegId);
			WorksBillDelegate wbDelegate = new WorksBillDelegate();
			int userId = ((Integer) req.getSession().getAttribute(LoginUserId))
					.intValue();
			wbForm.setUserId(userId);
			LOGGER.info("CodeLists" + wbForm.codeList);
			wbForm = wbDelegate.getEgBillRegister(wbForm);
			req.setAttribute(FORMNAME, wbForm);

			req.setAttribute(TDSLIST, wbForm.getTdsList());

			List netPayList = wbDelegate.getNetPayList();
			req.setAttribute(NETPAYLIST, netPayList);
			List deptList = wbDelegate.getAllActiveDepartments();
			req.setAttribute(DEPTLIST, deptList);

			target = TARGETSUCESS;

		} catch (Exception ex) {
			target = TARGETERROR;
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
		try {
			LOGGER.info("Inside Modify");
			String mode = MODIFYMODE;
			mode = req.getParameter(MODESTRING);
			req.setAttribute(MODESTRING, mode);
			int userId = ((Integer) req.getSession().getAttribute(LoginUserId))
					.intValue();

			WorksBillForm wbForm = (WorksBillForm) form;
			wbForm.setUserId(userId);
			WorksBillDelegate delgate = new WorksBillDelegate();
			int result = delgate.modify(wbForm);
			target = TARGETSUCESS;
			if (result == 1) {
				alertMessage = "Bill cannot be Created Bill Amount Exceeds The Total WorkOrder Amount ";
				billCreation = "failed";
			} else {
				alertMessage = " Bill  " + wbForm.getBillNo()
						+ "  Succesfully Modified";
				billCreation = TARGETSUCESS;
				// HibernateUtil.commitTransaction();
			}
			req.setAttribute("billCreation", billCreation);
			req.setAttribute(ALERTMESSAGE, alertMessage);
			req.setAttribute(TDSLIST, wbForm.getTdsList());
			req.setAttribute(FORMNAME, wbForm);
			List deptList = delgate.getAllActiveDepartments();
			List netPayList = delgate.getNetPayList();
			req.setAttribute(NETPAYLIST, netPayList);
			req.setAttribute(DEPTLIST, deptList);
			req.setAttribute(MODESTRING, MODIFYMODE);
			String buttonType = wbForm.getButtonType();
			req.setAttribute("buttonType", buttonType);

		} catch (Exception e) {
			target = TARGETERROR;
			alertMessage = "Exception while Modifying the  Bill :bill modification  failed";
			LOGGER.error("Error While Modifying the Bill");

			HibernateUtil.rollbackTransaction();
			// throw new EGOVRuntimeException(e.getMessage(),e);

		}
		LOGGER.info("just before forward");
		return mapping.findForward(target);

	}

	public ActionForward approve(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		String target = "";

		try {
			LOGGER.info("Inside Modify");
			String mode = MODIFYMODE;
			mode = req.getParameter(MODESTRING);
			req.setAttribute(MODESTRING, mode);
			int userId = ((Integer) req.getSession().getAttribute(LoginUserId))
					.intValue();

			WorksBillForm wbForm = (WorksBillForm) form;
			wbForm.setUserId(userId);
			WorksBillDelegate delgate = new WorksBillDelegate();
			// delgate.modify(wbForm);
			LOGGER.info("BIll Modified Succesfully");
			LOGGER.info("Before Vopucher Creation");
			List netPayList = delgate.getNetPayList();
			delgate.gnerateVoucher(Integer.parseInt(wbForm.getBillId()),
					userId, wbForm.getVoucherHeader_narration(), null);

			target = TARGETSUCESS;
			String alertMessage = "Bill Succesfully Modified And Approved VoucherNumber ";
			req.setAttribute(ALERTMESSAGE, alertMessage);
			req.setAttribute(TDSLIST, wbForm.getTdsList());
			req.setAttribute(FORMNAME, wbForm);
			List deptList = delgate.getAllActiveDepartments();

			req.setAttribute(NETPAYLIST, netPayList);
			req.setAttribute(DEPTLIST, deptList);
			req.setAttribute("buttonType", "close");
			req.setAttribute(MODESTRING, "approve");
		} catch (NumberFormatException e) {
			target = TARGETERROR;
			LOGGER.error("Error While Modifying the Bill");
			HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage());
		} catch (EGOVRuntimeException e) {
			target = TARGETERROR;
			LOGGER.error("Error While Modifying the Bill");

			HibernateUtil.rollbackTransaction();
			throw e;
		} catch (SQLException e) {
			target = TARGETERROR;
			LOGGER.error("Error While Modifying the Bill");
			HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage());
		}

		return mapping.findForward(target);

	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
