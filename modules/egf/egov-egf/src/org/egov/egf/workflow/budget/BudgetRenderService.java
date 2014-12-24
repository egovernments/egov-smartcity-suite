package org.egov.egf.workflow.budget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationException;
import org.egov.infstr.beanfactory.ApplicationContextBeanProvider;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.inbox.DefaultWorkflowTypeService;
import org.egov.infstr.workflow.inbox.WorkflowTypeService;
import org.egov.model.budget.Budget;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.services.budget.BudgetService;
import org.egov.utils.Constants;
import org.egov.web.utils.ERPWebApplicationContext;

/**
 * @author eGov
 */
public class BudgetRenderService implements WorkflowTypeService<Budget> {

	private static final Logger LOGGER = Logger
			.getLogger(BudgetRenderService.class);
	private transient DefaultWorkflowTypeService<Budget> dftWFTypeService;
	private EmployeeService employeeService;

	private GenericHibernateDaoFactory genericDao;
	private ApplicationContextBeanProvider beanProvider;

	public BudgetRenderService(PersistenceService persistenceService,
			Class<? extends Budget> workflowType) {
		dftWFTypeService = new DefaultWorkflowTypeService<Budget>(
				persistenceService);
		dftWFTypeService.setWorkflowType(workflowType);
	}

	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}

	public List<Budget> getAssignedWorkflowItems(Integer owner, Integer userId,
			String order) {
		return groupBudgetWFItems(dftWFTypeService.getAssignedWorkflowItems(
				owner, userId, order));
	}

	public List<Budget> getDraftWorkflowItems(Integer owner, Integer userId,
			String order) {
		return dftWFTypeService.getDraftWorkflowItems(owner, userId, order);
	}

	public List<Budget> getFilteredWorkflowItems(Integer owner, Integer userId,
			Integer sender, Date fromDate, Date toDate) {
		return groupBudgetWFItems(dftWFTypeService.getFilteredWorkflowItems(
				owner, userId, sender, fromDate, toDate));
	}

	public List<Budget> getWorkflowItems(final Map<String, Object> criteria) {
		return groupBudgetWFItems(dftWFTypeService.getWorkflowItems(criteria));
	}

	@Override
	public List<Budget> getWorkflowItems(String myLinkId) {
		return new ArrayList<Budget>();
	}

	private List<Budget> groupBudgetWFItems(List<Budget> wfItemList)
			throws EGOVRuntimeException {
		List<Budget> filteredBudgetItems = new ArrayList<Budget>();
		PersonalInformation emp = employeeService.getEmpForUserId(Integer
				.valueOf(EGOVThreadLocals.getUserId()));
		Assignment empAssignment = employeeService.getAssignmentByEmpAndDate(
				new Date(), emp.getIdPersonalInformation());
		DesignationMaster designation = empAssignment.getDesigId();
		List<AppConfigValues> list = genericDao.getAppConfigValuesDAO()
				.getConfigValuesByModuleAndKey(Constants.EGF,
						"budget_toplevel_approver_designation");
		if (list.isEmpty())
			throw new ValidationException("",
					"budget_toplevel_approver_designation is not defined in AppConfig");

		List<AppConfigValues> list2 = genericDao.getAppConfigValuesDAO()
				.getConfigValuesByModuleAndKey(Constants.EGF,
						"budget_secondlevel_approver_designation");
		BudgetService budgetService = (BudgetService) beanProvider
				.getBean("budgetService",
						ERPWebApplicationContext.ContextName.EGF, true);
		if (list2.isEmpty())
			throw new ValidationException("",
					"budget_secondlevel_approver_designation is not defined in AppConfig");

		if (designation.getDesignationName().equalsIgnoreCase(
				((AppConfigValues) list.get(0)).getValue())) {
			for (Budget budget : wfItemList) {
				if (budget.getIsPrimaryBudget() && budget.getParent() == null) {
					filteredBudgetItems.add(budget);
				}
			}
		} else if (designation.getDesignationName().equalsIgnoreCase(
				((AppConfigValues) list2.get(0)).getValue())) {
			for (Budget budget : wfItemList) {
				if (budget.getParent() != null && budget.getIsPrimaryBudget()
						&& !budgetService.isLeaf(budget)) {
					filteredBudgetItems.add(budget);
				}
			}
		} else {
			for (Budget budget : wfItemList) {
				if (budget.getParent() != null && budget.getIsPrimaryBudget()
						&& budgetService.isLeaf(budget)) {
					filteredBudgetItems.add(budget);
				}
			}
		}
		return filteredBudgetItems;
	}

	public void setBeanProvider(ApplicationContextBeanProvider beanProvider) {
		this.beanProvider = beanProvider;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

}