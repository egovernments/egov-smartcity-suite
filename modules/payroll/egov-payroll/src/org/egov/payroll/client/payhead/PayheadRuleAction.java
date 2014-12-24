package org.egov.payroll.client.payhead;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.payroll.model.PayheadRule;
import org.egov.payroll.model.SalaryCodes;
import org.egov.payroll.services.payhead.PayheadService;

public class PayheadRuleAction extends DispatchAction {

	private static final Logger LOGGER = Logger
			.getLogger(PayheadRuleAction.class);
	private PayheadService payheadService;
	private PersistenceService actionService;

	public ActionForward beforePayheadRule(ActionMapping actionMapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			List<Action> wfActionList = null;
			PayheadRuleForm payheadRuleForm = (PayheadRuleForm) form;
			List<SalaryCodes> salarycodeList = payheadService
					.getAllSalaryCodes();
			payheadRuleForm.setSalarycodeList(salarycodeList);
			Collections.sort(salarycodeList);// This to sort the list.
			wfActionList = actionService.findAllByNamedQuery("BY_TYPE",
					"PayHeadRule");
			request.getSession().setAttribute("salarycodeList", salarycodeList);
			request.getSession().setAttribute("wfActionList", wfActionList);
		} catch (Exception e) {
			LOGGER.error("Exception = " + e);
		}

		if ("modify".equals(request.getParameter("mode"))) {
			return actionMapping.findForward("modifyPayheadRule");
		} else {
			return actionMapping.findForward("beforePayheadRule");
		}

	}

	public ActionForward createPayheadRule(ActionMapping actionMapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Locale loc = getLocale(request);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", loc);
		PayheadRuleForm payheadRuleForm = (PayheadRuleForm) form;
		SalaryCodes salCode = null;
		int len = payheadRuleForm.getDescription().length;
		String[] payRuleId = payheadRuleForm.getPayRuleId();
		for (int i = 0; i < len; i++) {
			if ("0".equals(payRuleId[i])) {
				PayheadRule payheadRule = new PayheadRule();
				// String[] desc=payheadRuleForm.getDescription();
				salCode = payheadService.getSalaryCodeByHead(payheadRuleForm
						.getSalarycode());
				payheadRule.setSalarycode(salCode);
				payheadRule.setDescription(payheadRuleForm.getDescription()[i]);
				if (payheadRuleForm.getPayheadRuleScript()[i] != null
						&& !("").equals(payheadRuleForm.getPayheadRuleScript()[i]
								.trim())) {
					String query = " from org.egov.infstr.workflow.Action act where act.id = ? ";
					org.egov.infstr.workflow.Action ruleScript = (org.egov.infstr.workflow.Action) actionService
							.find(query, Long.parseLong(payheadRuleForm
									.getPayheadRuleScript()[i]));
					payheadRule.setRuleScript(ruleScript);
				}/*
				 * else{ payheadRule.setRuleScript(null); }
				 */

				// DateTime effectiveFrom = new
				// DateTime(payheadRule.getEffectiveFrom());
				Date effectiveFrom = sdf.parse(payheadRuleForm
						.getEffectiveFrom()[i]);

				payheadRule.setEffectiveFrom(effectiveFrom);
				payheadService.savePayheadRule(payheadRule);
			} else {
				PayheadRule payheadRule = payheadService
						.getPayheadRuleById(Integer.parseInt(payRuleId[i]));
				salCode = payheadService.getSalaryCodeByHead(payheadRuleForm
						.getSalarycode());
				payheadRule.setSalarycode(salCode);

				payheadRule.setDescription(payheadRuleForm.getDescription()[i]);
				if (payheadRuleForm.getPayheadRuleScript()[i] != null
						&& !("").equals(payheadRuleForm.getPayheadRuleScript()[i]
								.trim())) {
					String query = " from org.egov.infstr.workflow.Action act where act.id = ? ";
					org.egov.infstr.workflow.Action ruleScript = (org.egov.infstr.workflow.Action) actionService
							.find(query, Long.parseLong(payheadRuleForm
									.getPayheadRuleScript()[i]));
					payheadRule.setRuleScript(ruleScript);
				}/*
				 * else{ payheadRule.setRuleScript(null); }
				 */

				// DateTime effectiveFrom = new
				// DateTime(payheadRule.getEffectiveFrom());
				Date effectiveFrom = sdf.parse(payheadRuleForm
						.getEffectiveFrom()[i]);

				payheadRule.setEffectiveFrom(effectiveFrom);
				payheadService.updatePayheadRule(payheadRule);
			}
		}

		Set delPayRule = (Set) request.getSession().getAttribute("delPayRule");
		if (delPayRule != null) {
			for (Iterator it = delPayRule.iterator(); it.hasNext();) {
				Integer id = (Integer) it.next();
				PayheadRule prule = payheadService.getPayheadRuleById(id);
				payheadService.deletePayrule(prule);
			}
		}
		request.getSession().removeAttribute("delPayRule");
		request.getSession().setAttribute("payheadRuleId", salCode.getId());

		return actionMapping.findForward("createdPayheadRule");

	}

	public ActionForward viewPayheadRule(ActionMapping actionMapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		PayheadRuleForm payheadRuleForm = (PayheadRuleForm) form;
		Integer payheadRuleId = null;
		// payheadRuleId=Integer.parseInt(request.getParameter("id"));
		// String mode = request.getParameter("mode");
		String head = request.getParameter("head");
		if (head == null) {
			payheadRuleId = (Integer) request.getSession().getAttribute(
					"payheadRuleId");
		} else {
			SalaryCodes salCode = payheadService.getSalaryCodeByHead(head);
			payheadRuleId = salCode.getId();
		}

		List<PayheadRule> ruleList = payheadService
				.getAllPayheadRulesBySalCode(payheadRuleId);
		// payheadRuleForm.setDescription(payheadRule.getDescription());
		// payheadRuleForm.setEffectiveFrom(payheadRule.getEffectiveFrom().toString());

		List<Action> wfActionList = null;
		wfActionList = actionService.findAllByNamedQuery("BY_TYPE",
				"PayHeadRule");

		// payheadRuleForm.setSalarycode(payheadRule.getSalarycode().getHead());
		// payheadRuleForm.setPayheadRule(payheadRule);
		List<SalaryCodes> salarycodeList = payheadService.getAllSalaryCodes();
		payheadRuleForm.setSalarycodeList(salarycodeList);
		Collections.sort(salarycodeList);
		request.getSession().setAttribute("salarycodeList", salarycodeList);
		request.getSession().setAttribute("wfActionList", wfActionList);
		request.getSession().setAttribute("ruleList", ruleList);
		request.getSession().setAttribute("id", payheadRuleId);

		return actionMapping.findForward("viewPayheadRule");

	}

	/*
	 * public ActionForward viewPayheadRule(ActionMapping
	 * actionMapping,ActionForm form,HttpServletRequest request,
	 * HttpServletResponse response)throws Exception { PayheadRuleForm
	 * payheadRuleForm = (PayheadRuleForm)form; Integer payheadRuleId = null;
	 * //payheadRuleId=Integer.parseInt(request.getParameter("id")); //String
	 * mode = request.getParameter("mode"); if (payheadRuleForm == null ||
	 * payheadRuleForm.getPayheadRuleId() == null) { payheadRuleId =
	 * (Integer)request.getSession().getAttribute("payheadRuleId"); } else {
	 * payheadRuleId = Integer.parseInt(payheadRuleForm.getPayheadRuleId()); }
	 * PayheadRule payheadRule =
	 * payheadManager.getPayheadRuleById(payheadRuleId);
	 * payheadRuleForm.setDescription(payheadRule.getDescription());
	 * payheadRuleForm
	 * .setEffectiveFrom(payheadRule.getEffectiveFrom().toString());
	 * 
	 * List<Action> wfActionList = null; wfActionList =
	 * actionService.findAllByNamedQuery("BY_TYPE", "PayHeadRule");
	 * 
	 * payheadRuleForm.setSalarycode(payheadRule.getSalarycode().getHead());
	 * payheadRuleForm.setPayheadRule(payheadRule); List<SalaryCodes>
	 * salarycodeList = payheadManager.getAllSalaryCodes();
	 * payheadRuleForm.setSalarycodeList(salarycodeList);
	 * Collections.sort(salarycodeList);
	 * request.getSession().setAttribute("salarycodeList", salarycodeList);
	 * request.getSession().setAttribute("wfActionList", wfActionList);
	 * request.getSession().setAttribute("payheadRule", payheadRule);
	 * 
	 * return actionMapping.findForward("viewPayheadRule");
	 * 
	 * }
	 */

	public ActionForward beforModifyPayheadRule(ActionMapping actionMapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			PayheadRuleForm payheadRuleForm = (PayheadRuleForm) form;
			String head = request.getParameter("head");
			SalaryCodes salCode = payheadService.getSalaryCodeByHead(head);
			/*
			 * if (payheadRuleForm == null || payheadRuleForm.getPayheadRuleId()
			 * == null) { payheadRuleId =
			 * (Integer)request.getSession().getAttribute("payheadRuleId"); }
			 * else { payheadRuleId =
			 * Integer.parseInt(payheadRuleForm.getPayheadRuleId()); }
			 */
			List<PayheadRule> ruleList = payheadService
					.getAllPayheadRulesBySalCode(salCode.getId());

			List<Action> wfActionList = null;
			wfActionList = actionService.findAllByNamedQuery("BY_TYPE",
					"PayHeadRule");

			// payheadRuleForm.setSalarycode(payheadRule.getSalarycode().getHead());
			// payheadRuleForm.setPayheadRule(payheadRule);
			List<SalaryCodes> salarycodeList = payheadService
					.getAllSalaryCodes();
			payheadRuleForm.setSalarycodeList(salarycodeList);
			Collections.sort(salarycodeList);
			request.getSession().setAttribute("salarycodeList", salarycodeList);
			request.getSession().setAttribute("wfActionList", wfActionList);
			request.getSession().setAttribute("id", salCode.getHead());

			request.getSession().setAttribute("ruleList", ruleList);

		} catch (Exception e) {
			LOGGER.error("Exception = " + e);
		}

		return actionMapping.findForward("createmodifyPayheadRule");
	}

	public PersistenceService getActionService() {
		return actionService;
	}

	public void setActionService(PersistenceService actionService) {
		this.actionService = actionService;
	}

	public PayheadService getPayheadService() {
		return payheadService;
	}

	public void setPayheadService(PayheadService payheadService) {
		this.payheadService = payheadService;
	}

}
