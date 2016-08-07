/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.workflow.matrix.service;

import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infra.workflow.matrix.entity.WorkFlowAdditionalRule;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrixDetails;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Designation;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class WorkFlowMatrixService extends PersistenceService<WorkFlowMatrix, Long> {

	private PersistenceService persistenceService;
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowMatrixService.class);
	public static final String OBJECTTYPE = "ObjectType";
	public static final String ADDITIONALRULE = "AdditionalRule";
	public static final String FROMDATE = "FromDate";
	public static final String TODATE = "ToDate";
	public static final String FROMAMOUNT = "FromAmount";
	public static final String TOAMOUNT = "ToAmount";
	public static final String DEPARTMENTS = "Departments";
	public static final String MODIFYDATE = "ModifyDate";
	public static final String DEFAULT = "DEFAULT";
	public static final String REJECTED = "Rejected";
	private Boolean isworkFlowCorrect;

	public WorkFlowMatrixService() {
		super(WorkFlowMatrix.class);
	}

	public WorkFlowMatrixService(Class<WorkFlowMatrix> type) {
		super(type);
	}

	public Boolean getIsworkFlowCorrect() {
		return this.isworkFlowCorrect;
	}

	public void setIsworkFlowCorrect(final Boolean isworkFlowCorrect) {
		this.isworkFlowCorrect = isworkFlowCorrect;
	}

	public List<Department> getdepartmentList() {

		return this.persistenceService.findAllBy("from Department order by deptName asc");
	}

	public List<WorkflowTypes> getobjectTypeList() {

		return this.persistenceService.findAllBy("from org.egov.infstr.models.WorkflowTypes order by type asc");
	}

	public WorkflowTypes getobjectTypebyId(final Long objectTypeId) {

		return (WorkflowTypes) this.persistenceService.find("from org.egov.infstr.models.WorkflowTypes where id=? order by type asc", objectTypeId);
	}

	public WorkflowTypes getobjectTypebyName(final String objectTypeName) {

		return (WorkflowTypes) this.persistenceService.find("from org.egov.infstr.models.WorkflowTypes where type=? order by type asc", objectTypeName);
	}

	public List getAdditionalRulesforObject(final Long objectTypeid) {
		final Criteria crit = getSession().createCriteria(WorkFlowAdditionalRule.class);
		crit.add(Restrictions.eq("objecttypeid.id", objectTypeid));
		return crit.list();
	}

	public List<Designation> getdesignationList() {

		return this.persistenceService.findAllBy("from Designation order by name asc");
	}

	/**
	 * This method returns a map consisting the state ,status from egw_status and buttons defined in the additionalrule table for a given objecttype
	 * @return
	 */
	public HashMap<String, List> getDetailsforObject(final Long workFlowObjectId) {

		LOGGER.info("getDetailsforObject Method is called");

		final List<String> stateList = new ArrayList();
		final List<String> statusList = new ArrayList();
		final List<String> buttonList = new ArrayList();
		final List<String> objactionList = new ArrayList();
		final HashMap detailMap = new HashMap();

		final Criteria stateCrit = getSession().createCriteria(WorkFlowAdditionalRule.class);
		stateCrit.add(Restrictions.eq("objecttypeid.id", workFlowObjectId));
		List<WorkFlowAdditionalRule> workFlowAdditionalList = new ArrayList();
		workFlowAdditionalList = stateCrit.list();
		for (final WorkFlowAdditionalRule wfAdditionalrule : workFlowAdditionalList) {
			if (wfAdditionalrule != null && wfAdditionalrule.getStates() != null && wfAdditionalrule.getStates() != "") {
				final StringTokenizer strngtkn = new StringTokenizer(wfAdditionalrule.getStates(), ",");
				while (strngtkn.hasMoreTokens()) {
					final String statetkn = strngtkn.nextToken();
					if (!stateList.contains(statetkn)) {
						stateList.add(statetkn);
					}
				}
			}
			if (wfAdditionalrule != null && wfAdditionalrule.getStatus() != null && wfAdditionalrule.getStatus() != "") {
				final StringTokenizer strngtkn = new StringTokenizer(wfAdditionalrule.getStatus(), ",");
				while (strngtkn.hasMoreTokens()) {
					final String moduleType = strngtkn.nextToken();
					final List<EgwStatus> statusLists = this.persistenceService.findAllBy("from EgwStatus where moduletype=? order by code asc", moduleType);
					for (final EgwStatus status : statusLists) {
						if (status != null && status.getCode() != null && (!statusList.contains(status.getCode()))) {
							statusList.add(status.getCode());
						}
					}
				}
			}
			if (wfAdditionalrule != null && wfAdditionalrule.getButtons() != null && wfAdditionalrule.getButtons() != "") {
				final StringTokenizer btntkn = new StringTokenizer(wfAdditionalrule.getButtons(), ",");
				while (btntkn.hasMoreTokens()) {
					final String buttontkn = btntkn.nextToken();
					if (!buttonList.contains(buttontkn)) {
						buttonList.add(buttontkn);
					}
				}
			}

			if (wfAdditionalrule != null && wfAdditionalrule.getWorkFlowActions() != null && wfAdditionalrule.getWorkFlowActions() != "") {
				final StringTokenizer acttkn = new StringTokenizer(wfAdditionalrule.getWorkFlowActions(), ",");
				while (acttkn.hasMoreTokens()) {
					final String objactiontkn = acttkn.nextToken();
					if (!objactionList.contains(objactiontkn)) {
						objactionList.add(objactiontkn);
					}
				}
			}
		}
		if (stateList.isEmpty()) {
			stateList.add("Created");
			stateList.add("Checked");
			stateList.add("Approved");
			stateList.add("Inspected");
			stateList.add("Submitted");
			stateList.add("ReSubmitted");
			stateList.add("Rejected");
		}
		if (statusList.isEmpty()) {
			statusList.add("Created");
			statusList.add("Approved");
			statusList.add("Rejected");
			statusList.add("Submitted");
			statusList.add("Closed");
		}
		if (buttonList.isEmpty()) {

			buttonList.add("Forward");
			buttonList.add("Approve");
			buttonList.add("Reject");
			buttonList.add("Close");
		}

		if (objactionList.isEmpty()) {

			objactionList.add("Pending Check");
			objactionList.add("Pending Approval");
			objactionList.add("Pending Close");
			objactionList.add("END");

		}
		detailMap.put("StateList", stateList);
		detailMap.put("StatusList", statusList);
		detailMap.put("ButtonsList", buttonList);
		detailMap.put("ActionsList", objactionList);

		LOGGER.info("getDetailsforObject Method is ended");
		return detailMap;
	}

	/**
	 * This method saves the workflow matrix details for every department selected
	 * @return
	 */
	public void save(final List<WorkFlowMatrix> actualWorkFlowMatrixDetails, final String[] departments) {

		LOGGER.info("save Method is called");

		for (final String dept : departments) {
			for (final WorkFlowMatrix workFlowMatrix : actualWorkFlowMatrixDetails) {
				final WorkFlowMatrix wfObj = workFlowMatrix.clone();
				if (dept.equals(DEFAULT)) {
					wfObj.setDepartment("ANY");
				} else {
					wfObj.setDepartment(dept);
				}
				getSession().save(wfObj);
			}
		}
		getSession().flush();
		LOGGER.info("save Method is ended");
	}

	/**
	 * This method gets the workflowdetails grouped by objecttype,additionalrule,from and todate,from qty and toqty
	 * @return
	 */
	public List<WorkFlowMatrixDetails> getWorkFlowMatrixObjectForView(final HashMap workFlowObjectMap) {

		LOGGER.info("save Method is called");
		final Criteria workFlowCrit = getSession().createCriteria(WorkFlowMatrix.class);
		if (workFlowObjectMap.get(OBJECTTYPE) != null && workFlowObjectMap.get(OBJECTTYPE) != "-1") {
			workFlowCrit.add(Restrictions.eq("objectType", getobjectTypebyId((Long) workFlowObjectMap.get(OBJECTTYPE)).getType()));
		}

		if (workFlowObjectMap.get(DEPARTMENTS) != null && (workFlowObjectMap.get(DEPARTMENTS)) != "-1") {
			final String[] department = (String[]) workFlowObjectMap.get(DEPARTMENTS);
			workFlowCrit.add(Restrictions.eq("department", department[0]));
		}
		if (workFlowObjectMap.get(ADDITIONALRULE) != null && !workFlowObjectMap.get(ADDITIONALRULE).equals("-1")) {
			workFlowCrit.add(Restrictions.eq("additionalRule", workFlowObjectMap.get(ADDITIONALRULE)));
		}

		addProjectionsforCriteria(workFlowCrit);

		workFlowCrit.addOrder(Order.asc("fromDate"));
		workFlowCrit.addOrder(Order.asc("fromQty"));
		workFlowCrit.addOrder(Order.asc("additionalRule"));

		workFlowCrit.setResultTransformer(Transformers.aliasToBean(WorkFlowMatrixDetails.class));
		final List workFlowList = workFlowCrit.list();

		if (workFlowList.size() != 0) {
			final List<WorkFlowMatrixDetails> actList = checkWithOtherParams(workFlowList, workFlowObjectMap);
			return prepareWorkFlowResult(actList);
		} else {
			return null;
		}
	}

	private void addProjectionsforCriteria(final Criteria workFlowCrit) {
		workFlowCrit.setProjection(Projections.projectionList().add(Projections.groupProperty("objectType").as("objectTypeAlias")).add(Projections.groupProperty("department").as("departmentAlias"))
				.add(Projections.groupProperty("additionalRule").as("additionalRuleAlias")).add(Projections.groupProperty("fromQty").as("fromQtyAlias")).add(Projections.groupProperty("toQty").as("toQtyAlias"))
				.add(Projections.groupProperty("fromDate").as("fromDateAlias")).add(Projections.groupProperty("toDate").as("toDateAlias")));
	}

	/**
	 * This method is used to prepare the workflow details that has to be shown in the view
	 * @return
	 */
	private List<WorkFlowMatrixDetails> prepareWorkFlowResult(final List<WorkFlowMatrixDetails> matrixdetList) {
		LOGGER.info("prepareWorkFlowResult Method is called");
		for (final WorkFlowMatrixDetails det : matrixdetList) {
			det.setObjectTypeDisplay(getobjectTypebyName(det.getObjectType()).getDisplayName());
			final List<WorkFlowMatrix> workFlowdet = getMatrixdetails(det);
			List<WorkFlowMatrixDetails> detailsList = new LinkedList();
			detailsList = prepareWorkFlowMatrixDetailsList(sortListbyActions(workFlowdet), detailsList, Boolean.TRUE);
			// }
			det.setMatrixdetails(detailsList);
		}
		LOGGER.info("prepareWorkFlowResult Method is ended");
		return matrixdetList;
	}

	public List prepareWorkFlowMatrixDetailsList(final List<WorkFlowMatrix> workFlowdet, final List<WorkFlowMatrixDetails> detailsList, final Boolean isReject) {
		for (final WorkFlowMatrix wfMatrixObj : (workFlowdet)) {
			final WorkFlowMatrixDetails details = new WorkFlowMatrixDetails();
			// if(!wfMatrixObj.getCurrentState().equalsIgnoreCase("Rejected")){
			if (isReject) {
				details.setAction(wfMatrixObj.getNextAction());
				details.setState(wfMatrixObj.getNextState());
				details.setStatus(wfMatrixObj.getNextStatus());
				String[] buttonarr = { "" };
				if (wfMatrixObj.getValidActions() != null) {
					buttonarr = wfMatrixObj.getValidActions().split(",");
				}
				details.setButtons(buttonarr);
				String[] desnarr = { "" };
				if (wfMatrixObj.getNextDesignation() != null) {
					desnarr = wfMatrixObj.getNextDesignation().split(",");
				}
				details.setDesignation(desnarr);
				details.setStatus(wfMatrixObj.getNextStatus());
			} else {

				details.setRejectAction(wfMatrixObj.getNextAction());
				details.setRejectState(wfMatrixObj.getNextState());
				details.setRejectStatus(wfMatrixObj.getNextStatus());
				String[] buttonarr = { "" };
				if (wfMatrixObj.getValidActions() != null) {
					buttonarr = wfMatrixObj.getValidActions().split(",");
				}
				details.setRejectButtons(buttonarr);
				String[] desnarr = { "" };
				if (wfMatrixObj.getNextDesignation() != null) {
					desnarr = wfMatrixObj.getNextDesignation().split(",");
				}
				details.setRejectDesignation(desnarr);
				details.setRejectStatus(wfMatrixObj.getNextStatus());

			}
			detailsList.add(details);
		}
		return detailsList;
	}

	/**
	 * This method is used to sort the list of workflow details in the order of approval from new to end then reject then matrix details after reject
	 * @return
	 */
	private List sortListbyActions(final List<WorkFlowMatrix> workFlowdet) {
		LOGGER.info("sortListbyActions Method is called");
		setIsworkFlowCorrect(Boolean.TRUE);
		/*
		 * for(WorkFlowMatrix wfMatrix:workFlowdet){ System.out.println("Inputids  "+wfMatrix.getId()); }
		 */
		final LinkedList unsortedList = new LinkedList(workFlowdet);
		WorkFlowMatrix rejectedMatrix = null;
		// int workFlowdetinitialsize=workFlowdet.size()-1;
		final List<WorkFlowMatrix> workflowSortedList = new LinkedList<WorkFlowMatrix>();
		final List<WorkFlowMatrix> rejectedRelatedList = new LinkedList<WorkFlowMatrix>();
		final Iterator<WorkFlowMatrix> workFlowdetiterator = workFlowdet.iterator();
		while (workFlowdetiterator.hasNext()) {
			final WorkFlowMatrix wfMatrix = workFlowdetiterator.next();
			if (wfMatrix.getPendingActions() == null && wfMatrix.getCurrentState().equalsIgnoreCase("NEW")) {
				workflowSortedList.add(wfMatrix);
				workFlowdetiterator.remove();
			}

		}

		for (; (workflowSortedList.size() > 0 && !workflowSortedList.get(workflowSortedList.size() - 1).getNextAction().equalsIgnoreCase("END"));) {
			final int size = workFlowdet.size();
			final String sortedwfMatrixnextAction = workflowSortedList.get(workflowSortedList.size() - 1).getNextAction();
			final String sortedwfMatrixnextState = workflowSortedList.get(workflowSortedList.size() - 1).getNextState();
			int count = 0;
			for (final WorkFlowMatrix wfMatrix : workFlowdet) {

				if (sortedwfMatrixnextAction.equalsIgnoreCase(wfMatrix.getPendingActions()) && sortedwfMatrixnextState.equalsIgnoreCase(wfMatrix.getCurrentState())) {
					// System.out.println("added"+wfMatrix.getId() );
					workflowSortedList.add(wfMatrix);

				} else {
					count++;
					if (count >= size) {
						setIsworkFlowCorrect(Boolean.FALSE);
						break;
					}
				}
			}

			if (this.isworkFlowCorrect) {
				for (int j = 0; j < workFlowdet.size(); j++) {
					final Long actwfMatrixnextAction = workFlowdet.get(j).getId();
					for (final WorkFlowMatrix wfMatrix : workflowSortedList) {
						if (actwfMatrixnextAction.equals(wfMatrix.getId())) {
							// System.out.println("removed"+wfMatrix.getId() );
							workFlowdet.remove(j);
						}
					}
				}
			}
			if (!this.isworkFlowCorrect) {
				break;
			}
		}

		if (this.isworkFlowCorrect) {

			final Iterator<WorkFlowMatrix> rejectworkFlowdetiterator = workFlowdet.iterator();
			while (rejectworkFlowdetiterator.hasNext()) {
				final WorkFlowMatrix wfMatrix = rejectworkFlowdetiterator.next();
				if (wfMatrix.getCurrentState().equalsIgnoreCase(REJECTED)) {
					rejectedMatrix = wfMatrix;
					for (final WorkFlowMatrix wfmatrix : workFlowdet) {
						if (wfMatrix.getNextAction().equalsIgnoreCase(wfmatrix.getPendingActions()) && wfMatrix.getNextState().equalsIgnoreCase(wfmatrix.getCurrentState())) {
							rejectedRelatedList.add(wfmatrix);
						}
					}
					rejectworkFlowdetiterator.remove();
					/*
					 * if(rejectedMatrix!=null&&!workflowSortedList.contains(rejectedMatrix)) workflowSortedList.add(rejectedMatrix);
					 */}
			}

			if (rejectedMatrix != null) {
				workflowSortedList.add(rejectedMatrix);
			}

			for (final WorkFlowMatrix wfMatrix : rejectedRelatedList) {
				// System.out.println("RejectedInputids  "+wfMatrix.getId());
				if (wfMatrix != null && !workflowSortedList.contains(wfMatrix)) {
					workflowSortedList.add(wfMatrix);
				}
			}

			// sortedList.addAll(rejectedRelatedList);
			LOGGER.info("sortListbyActions Method is ended");
			/*
			 * for(WorkFlowMatrix wfMatrix:workflowSortedList){ System.out.println("sortedInputids  "+wfMatrix.getId()); }
			 */

			return workflowSortedList;
		} else {
			return unsortedList;
		}
	}

	/**
	 * This method is used to get the list of matrix details for each record got from the grouped details from workflowmatrix table
	 * @return
	 */
	private List<WorkFlowMatrix> getMatrixdetails(final WorkFlowMatrixDetails det) {
		LOGGER.info("getMatrixdetails Method is called");
		final Criteria workFlowCrit = getSession().createCriteria(WorkFlowMatrix.class);
		if (det.getObjectType() != null) {
			workFlowCrit.add(Restrictions.eq("objectType", det.getObjectType()));
		}
		if (det.getAdditionalRule() != null) {
			workFlowCrit.add(Restrictions.eq("additionalRule", det.getAdditionalRule()));
		} else {
			workFlowCrit.add(Restrictions.isNull("additionalRule"));
		}
		if (det.getDepartment() != null) {
			workFlowCrit.add(Restrictions.eq("department", det.getDepartment()));
		}
		if (det.getFromDate() != null) {
			workFlowCrit.add(Restrictions.eq("fromDate", det.getFromDate()));
		}
		if (det.getToDate() != null) {
			workFlowCrit.add(Restrictions.eq("toDate", det.getToDate()));
		} else {
			workFlowCrit.add(Restrictions.isNull("toDate"));
		}
		if (det.getFromQty() != null) {
			workFlowCrit.add(Restrictions.eq("fromQty", det.getFromQty()));
		} else {
			workFlowCrit.add(Restrictions.isNull("fromQty"));
		}
		if (det.getToQty() != null) {
			workFlowCrit.add(Restrictions.eq("toQty", det.getToQty()));
		} else {
			workFlowCrit.add(Restrictions.isNull("toQty"));
		}
		LOGGER.info("getMatrixdetails Method is ended");
		return workFlowCrit.list();
	}

	/**
	 * This method takes the workflowmatrix details grouped by objecttype,from and todate,from and to quantity,department and checks if there are any record that matched with the select criteria entered in the searchscreen like from and todate and from and to quantity
	 * @return
	 */
	private List<WorkFlowMatrixDetails> checkWithOtherParams(final List<WorkFlowMatrixDetails> workFlowList, final HashMap workFlowObjectMap) {

		LOGGER.info("checkWithOtherParams Method is called");
		final List<WorkFlowMatrixDetails> tempList1 = new ArrayList();
		Date fromdate = null;
		Date todate = null;
		BigDecimal fromqty = null;
		BigDecimal toqty = null;
		if (workFlowObjectMap.get(FROMDATE) != null && workFlowObjectMap.get(FROMDATE) != "") {
			fromdate = (Date) workFlowObjectMap.get(FROMDATE);
		}
		if (workFlowObjectMap.get(TODATE) != null && workFlowObjectMap.get(TODATE) != "") {
			todate = (Date) workFlowObjectMap.get(TODATE);
		}
		if (workFlowObjectMap.get(FROMAMOUNT) != null && workFlowObjectMap.get(FROMAMOUNT) != "") {
			fromqty = (BigDecimal) workFlowObjectMap.get(FROMAMOUNT);
		}
		if (workFlowObjectMap.get(TOAMOUNT) != null && workFlowObjectMap.get(TOAMOUNT) != "") {
			toqty = (BigDecimal) workFlowObjectMap.get(TOAMOUNT);
		}

		if (fromdate != null && todate == null) {
			for (final WorkFlowMatrixDetails matrixdet : workFlowList) {
				if (matrixdet.getToDate() == null && matrixdet.getFromDate() != null && !fromdate.before(matrixdet.getFromDate())) {
					tempList1.add(matrixdet);
					continue;
				}
				if (fromdate.before(matrixdet.getFromDate()) || fromdate.equals(matrixdet.getFromDate())) {
					tempList1.add(matrixdet);
					continue;
				}
			}
		}

		if (fromdate != null && todate != null) {
			for (final WorkFlowMatrixDetails matrixdet : workFlowList) {
				if ((fromdate.before(matrixdet.getFromDate()) || fromdate.equals(matrixdet.getFromDate())) && (todate.after(matrixdet.getToDate()) || todate.equals(matrixdet.getToDate()))) {
					tempList1.add(matrixdet);
				}
			}
		}

		if (fromdate == null && todate == null) {
			tempList1.addAll(workFlowList);
		}

		if (fromqty != null && toqty == null) {

			final Iterator<WorkFlowMatrixDetails> matrixiterator = tempList1.iterator();
			while (matrixiterator.hasNext()) {
				final WorkFlowMatrixDetails matrixdet = matrixiterator.next();
				if (matrixdet.getFromQty() == null || (matrixdet.getFromQty() != null && fromqty.compareTo(matrixdet.getFromQty()) > 0)) {
					matrixiterator.remove();
				}
			}
		} else if (fromqty != null && toqty != null) {
			final Iterator<WorkFlowMatrixDetails> matrixiterator = tempList1.iterator();
			while (matrixiterator.hasNext()) {
				final WorkFlowMatrixDetails matrixdet = matrixiterator.next();
				if (matrixdet.getFromQty() == null || matrixdet.getToQty() == null) {
					matrixiterator.remove();
				} else {

					if ((matrixdet.getToQty() != null && matrixdet.getToQty() != null && (fromqty.doubleValue() >= (matrixdet.getFromQty().doubleValue()) && (toqty.doubleValue() <= (matrixdet.getToQty().doubleValue()))))) {
						;
					} else {
						matrixiterator.remove();
					}
				}
			}
		}

		else if (fromqty == null && toqty != null) {

			final Iterator<WorkFlowMatrixDetails> matrixiterator = tempList1.iterator();
			while (matrixiterator.hasNext()) {
				final WorkFlowMatrixDetails matrixdet = matrixiterator.next();

				if (matrixdet.getFromQty() == null) {
					matrixiterator.remove();
				}
				if (matrixdet.getToQty() != null) {
					if (toqty.compareTo(matrixdet.getToQty()) < 0) {
						matrixiterator.remove();
					}
				} else {
					if (toqty.compareTo(matrixdet.getFromQty()) < 0) {
						matrixiterator.remove();
					}
				}
			}
		}
		LOGGER.info("checkWithOtherParams Method is ended");
		if (fromqty == null && toqty == null && fromdate == null && todate == null) {
			return workFlowList;
		} else {
			return tempList1;
		}

	}

	/**
	 * This method checks if there are any workflow matrix details for the parameters from,todate,from and to quantity,objecttype,department
	 * @return
	 */
	public List checkIfMatrixExists(final HashMap workflowheaderparams) {
		LOGGER.info("checkIfMatrixExists Method is called");

		final StringBuffer dateQryStr = new StringBuffer();
		final StringBuffer qntyQryStr = new StringBuffer();
		prepareQuery(workflowheaderparams, dateQryStr);
		prepareQuery(workflowheaderparams, qntyQryStr);

		dateQryStr.append(" having  fromDate <= :fromdate  and toDate is null ");
		dateQryStr.append(" or fromDate <= :fromdate  and toDate>= :fromdate   ");
		if (workflowheaderparams.get(TODATE) != null && !workflowheaderparams.get(TODATE).equals("")) {
			dateQryStr.append(" or fromDate <= :todate  and toDate>= :todate   ");
		}

		final Query datequery = getSession().createQuery(new String(dateQryStr));
		addParameter(workflowheaderparams, datequery);
		datequery.setParameter("fromdate", workflowheaderparams.get(FROMDATE));
		if (workflowheaderparams.get(TODATE) != null && !workflowheaderparams.get(TODATE).equals("")) {
			datequery.setParameter("todate", workflowheaderparams.get(TODATE));
		}
		final List dateList = datequery.list();

		if (workflowheaderparams.get(FROMAMOUNT) != null && !workflowheaderparams.get(FROMAMOUNT).equals("")) {
			qntyQryStr.append(" having  fromQty <= :fromamount  and toQty is null ");
			qntyQryStr.append(" or fromQty <= :fromamount  and toQty>= :fromamount   ");
		}

		if (workflowheaderparams.get(TOAMOUNT) != null && !workflowheaderparams.get(TOAMOUNT).equals("")) {
			qntyQryStr.append(" or fromQty <= :toamount  and toQty>= :toamount   ");

		}

		final Query qntyquery = getSession().createQuery(new String(qntyQryStr));
		addParameter(workflowheaderparams, qntyquery);
		if (workflowheaderparams.get(FROMAMOUNT) != null && !workflowheaderparams.get(FROMAMOUNT).equals("")) {
			qntyquery.setParameter("fromamount", workflowheaderparams.get(FROMAMOUNT));
		}
		if (workflowheaderparams.get(TOAMOUNT) != null && !workflowheaderparams.get(TOAMOUNT).equals("")) {
			qntyquery.setParameter("toamount", workflowheaderparams.get(TOAMOUNT));
		}

		final List qntyList = qntyquery.list();
		dateList.retainAll(qntyList);
		LOGGER.info("checkIfMatrixExists Method is ended");
		if (dateList.size() > 0) {
			return dateList;
		} else {
			return null;
		}

	}

	/**
	 * This method is used to prepare query that is used in checkIfMatrixExists method
	 * @return
	 */
	private void prepareQuery(final HashMap workflowheaderparams, final StringBuffer QryStr) {
		LOGGER.info("prepareQuery Method is called");
		QryStr.append("select id from  WorkFlowMatrix  where objectType = :objecttype and department IN (:departments) ");

		if (workflowheaderparams.get(ADDITIONALRULE) != null && !workflowheaderparams.get(ADDITIONALRULE).equals("-1")) {
			QryStr.append("and additionalRule = :additionalrule");
		}
		QryStr.append(" group by id,objectType,department,fromDate,toDate,fromQty,toQty ");

		if (workflowheaderparams.get(ADDITIONALRULE) != null && !workflowheaderparams.get(ADDITIONALRULE).equals("-1")) {
			QryStr.append(",additionalRule ");
		}
		LOGGER.info("prepareQuery Method is ended");
	}

	private void addParameter(final HashMap workflowheaderparams, final Query datequery) {
		datequery.setParameter("objecttype", getobjectTypebyId(((Long) workflowheaderparams.get(OBJECTTYPE))).getType());
		if (workflowheaderparams.get(ADDITIONALRULE) != null && !workflowheaderparams.get(ADDITIONALRULE).equals("-1")) {
			datequery.setParameter("additionalrule", workflowheaderparams.get(ADDITIONALRULE));
		}
		datequery.setParameterList("departments", (String[]) workflowheaderparams.get(DEPARTMENTS));
	}

	/**
	 * This method is used to delete the workflow matrix details
	 */
	public void deleteWorkFlowforObject(final HashMap workflowsearchparams) {

		final Criteria workFlowCrit = getCriteriaForDeleteorModify(workflowsearchparams);
		for (final WorkFlowMatrix matrix : (List<WorkFlowMatrix>) workFlowCrit.list()) {
			getSession().delete(matrix);
		}
		getSession().flush();

	}

	/**
	 * This method is used to get the workflow matrix details
	 */
	public List getWorkFlowforObjectforModify(final HashMap workflowsearchparams) {

		final Criteria workFlowCrit = getCriteriaForDeleteorModify(workflowsearchparams);
		final List<WorkFlowMatrix> matrixList = workFlowCrit.list();
		return sortListbyActions(matrixList);

	}

	/**
	 * This method is used to get the criteria that can be used for modifying or deleting the workflow matrix details based on the search parameters
	 */
	private Criteria getCriteriaForDeleteorModify(final HashMap workflowsearchparams) {
		LOGGER.info("getCriteriaForDeleteorModify Method is called");
		final Criteria workFlowCrit = getSession().createCriteria(WorkFlowMatrix.class);
		if (workflowsearchparams.get(OBJECTTYPE) != null && !workflowsearchparams.get(OBJECTTYPE).equals("")) {
			workFlowCrit.add(Restrictions.eq("objectType", workflowsearchparams.get(OBJECTTYPE)));
		}
		if (workflowsearchparams.get(ADDITIONALRULE) != null && !workflowsearchparams.get(ADDITIONALRULE).equals("") && !workflowsearchparams.get(ADDITIONALRULE).equals("-1")) {
			workFlowCrit.add(Restrictions.eq("additionalRule", workflowsearchparams.get(ADDITIONALRULE)));
		}
		if (workflowsearchparams.get(DEPARTMENTS) != null && !workflowsearchparams.get(DEPARTMENTS).equals("")) {
			final String[] department = (String[]) workflowsearchparams.get(DEPARTMENTS);
			workFlowCrit.add(Restrictions.eq("department", department[0]));
		}
		if (workflowsearchparams.get(FROMDATE) != null && !workflowsearchparams.get(FROMDATE).equals("")) {
			workFlowCrit.add(Restrictions.eq("fromDate", workflowsearchparams.get(FROMDATE)));
		}
		if (workflowsearchparams.get(TODATE) != null && !workflowsearchparams.get(TODATE).equals("")) {
			workFlowCrit.add(Restrictions.eq("toDate", workflowsearchparams.get(TODATE)));
		}
		if (workflowsearchparams.get(FROMAMOUNT) != null && !workflowsearchparams.get(FROMAMOUNT).equals("")) {
			workFlowCrit.add(Restrictions.eq("fromQty", workflowsearchparams.get(FROMAMOUNT)));
		}
		if (workflowsearchparams.get(TOAMOUNT) != null && !workflowsearchparams.get(TOAMOUNT).equals("")) {
			workFlowCrit.add(Restrictions.eq("toQty", workflowsearchparams.get(TOAMOUNT)));
		}
		LOGGER.info("getCriteriaForDeleteorModify Method is ended");
		return workFlowCrit;
	}

	/**
	 * This method is used to update the existing workflow matrix details for which modification has been done,only the todate is set
	 * @return
	 */
	public Boolean updateWorkFlowforObject(final HashMap workflowparams) {

		LOGGER.info("updateWorkFlowforObject Method is called");
		final Criteria workFlowCrit = getCriteriaForDeleteorModify(workflowparams);
		for (final WorkFlowMatrix matrix : (List<WorkFlowMatrix>) workFlowCrit.list()) {

			matrix.setToDate(matrix.getToDate() != null ? matrix.getToDate() : workflowparams.get(MODIFYDATE) != null ? (Date) workflowparams.get(MODIFYDATE) : new Date());

			if (matrix.getFromDate().equals(matrix.getToDate())) {
				return false;
			}
			getSession().update(matrix);
		}

		getSession().flush();
		LOGGER.info("updateWorkFlowforObject Method is ended");
		return true;
	}

	/**
	 * This method is used to check if any matrix exists for the ones which are being created for the dates lesser than the current date
	 * @return
	 */
	public Date checkLegacyMatrix(final HashMap workflowheaderparams) {

		LOGGER.info("checkLegacyMatrix Method is called");
		final Criteria workFlowCrit = getSession().createCriteria(WorkFlowMatrix.class);
		if (workflowheaderparams.get(OBJECTTYPE) != null && workflowheaderparams.get(OBJECTTYPE) != "") {
			workFlowCrit.add(Restrictions.eq("objectType", getobjectTypebyId((Long) workflowheaderparams.get(OBJECTTYPE)).getType()));
		}
		if (workflowheaderparams.get(ADDITIONALRULE) != null && !workflowheaderparams.get(ADDITIONALRULE).equals("-1")) {
			workFlowCrit.add(Restrictions.eq("additionalRule", workflowheaderparams.get(ADDITIONALRULE)));
		}
		if (workflowheaderparams.get(DEPARTMENTS) != null && workflowheaderparams.get(DEPARTMENTS) != "") {
			final String[] department = (String[]) workflowheaderparams.get(DEPARTMENTS);
			workFlowCrit.add(Restrictions.eq("department", department[0]));
		}
		if (workflowheaderparams.get(FROMDATE) != null && workflowheaderparams.get(FROMDATE) != "") {
			workFlowCrit.add(Restrictions.ge("fromDate", workflowheaderparams.get(FROMDATE)));
		}
		if (workflowheaderparams.get(FROMAMOUNT) != null && workflowheaderparams.get(FROMAMOUNT) != "") {
			workFlowCrit.add(Restrictions.eq("fromQty", workflowheaderparams.get(FROMAMOUNT)));
		}
		if (workflowheaderparams.get(TOAMOUNT) != null && workflowheaderparams.get(TOAMOUNT) != "") {
			workFlowCrit.add(Restrictions.eq("toQty", workflowheaderparams.get(TOAMOUNT)));
		}
		addProjectionsforCriteria(workFlowCrit);
		workFlowCrit.addOrder(Order.asc("toDate"));
		workFlowCrit.setResultTransformer(Transformers.aliasToBean(WorkFlowMatrixDetails.class));
		final List<WorkFlowMatrixDetails> matrixList = workFlowCrit.list();
		LOGGER.info("checkLegacyMatrix Method is ended");
		if (matrixList.size() > 0) {

			return matrixList.get(0).getFromDateAlias();
		} else {
			return null;
		}

	}

	public WorkFlowMatrix getWorkFlowObjectbyId(final Long matrixid) {

		return (WorkFlowMatrix) getSession().get(WorkFlowMatrix.class, matrixid);

	}

	public PersistenceService getPersistenceService() {
		return this.persistenceService;
	}

	public void setPersistenceService(final PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

}
