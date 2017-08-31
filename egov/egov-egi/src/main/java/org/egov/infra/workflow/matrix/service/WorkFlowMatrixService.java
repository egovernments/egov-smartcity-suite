/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.workflow.matrix.service;

import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infra.workflow.matrix.entity.WorkFlowAdditionalRule;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrixDetails;
import org.egov.infra.workflow.matrix.repository.WorkflowMatrixRepository;
import org.egov.infra.workflow.service.WorkflowTypeService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Designation;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@Transactional(readOnly = true)
public class WorkFlowMatrixService {

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
    public static final String TO_DATE = "toDate";
    public static final String TO_QTY = "toQty";
    public static final String FROM_QTY = "fromQty";
    public static final String FROM_DATE = "fromDate";
    public static final String ADDITIONAL_RULE = "additionalRule";
    public static final String DEPARTMENT = "department";
    public static final String OBJECT_TYPE = "objectType";

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    @Qualifier("entityQueryService")
    private PersistenceService entityQueryService;

    @Autowired
    private WorkflowTypeService workflowTypeService;

    @Autowired
    private WorkflowMatrixRepository workflowMatrixRepository;


    public List<Department> getdepartmentList() {
        return departmentService.getAllDepartments();
    }

    public List<WorkflowTypes> getobjectTypeList() {
        return workflowTypeService.getAllWorkflowTypes();
    }

    public WorkflowTypes getobjectTypebyId(final Long objectTypeId) {
        return workflowTypeService.getWorkflowTypeById(objectTypeId);
    }

    public WorkflowTypes getobjectTypebyName(final String objectTypeName) {
        return workflowTypeService.getWorkflowTypeByType(objectTypeName);
    }

    public List getAdditionalRulesforObject(final Long objectTypeid) {
        final Criteria crit = entityQueryService.getSession().createCriteria(WorkFlowAdditionalRule.class);
        crit.add(Restrictions.eq("objecttypeid.id", objectTypeid));
        return crit.list();
    }

    public List<Designation> getdesignationList() {
        return this.entityQueryService.findAllBy("from Designation order by name asc");
    }

    /**
     * This method returns a map consisting the state ,status from egw_status and buttons defined in the additionalrule table for a given objecttype
     *
     * @return
     */
    public Map<String, List> getDetailsforObject(final Long workFlowObjectId) {
        final List<String> stateList = new ArrayList();
        final List<String> statusList = new ArrayList();
        final List<String> buttonList = new ArrayList();
        final List<String> objactionList = new ArrayList();
        final HashMap detailMap = new HashMap();

        final Criteria stateCrit = entityQueryService.getSession().createCriteria(WorkFlowAdditionalRule.class);
        stateCrit.add(Restrictions.eq("objecttypeid.id", workFlowObjectId));
        List<WorkFlowAdditionalRule> workFlowAdditionalList = stateCrit.list();
        for (final WorkFlowAdditionalRule wfAdditionalrule : workFlowAdditionalList) {
            if (wfAdditionalrule != null && isNotBlank(wfAdditionalrule.getStates())) {
                final StringTokenizer strngtkn = new StringTokenizer(wfAdditionalrule.getStates(), ",");
                while (strngtkn.hasMoreTokens()) {
                    final String statetkn = strngtkn.nextToken();
                    if (!stateList.contains(statetkn)) {
                        stateList.add(statetkn);
                    }
                }
            }
            if (wfAdditionalrule != null && isNotBlank(wfAdditionalrule.getStatus())) {
                final StringTokenizer strngtkn = new StringTokenizer(wfAdditionalrule.getStatus(), ",");
                while (strngtkn.hasMoreTokens()) {
                    final String moduleType = strngtkn.nextToken();
                    final List<EgwStatus> statusLists = this.entityQueryService.findAllBy("from EgwStatus where moduletype=? order by code asc", moduleType);
                    for (final EgwStatus status : statusLists) {
                        if (status != null && status.getCode() != null && (!statusList.contains(status.getCode()))) {
                            statusList.add(status.getCode());
                        }
                    }
                }
            }
            if (wfAdditionalrule != null && isNotBlank(wfAdditionalrule.getButtons())) {
                final StringTokenizer btntkn = new StringTokenizer(wfAdditionalrule.getButtons(), ",");
                while (btntkn.hasMoreTokens()) {
                    final String buttontkn = btntkn.nextToken();
                    if (!buttonList.contains(buttontkn)) {
                        buttonList.add(buttontkn);
                    }
                }
            }

            if (wfAdditionalrule != null && isNotBlank(wfAdditionalrule.getWorkFlowActions())) {
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
            stateList.add(REJECTED);
        }
        if (statusList.isEmpty()) {
            statusList.add("Created");
            statusList.add("Approved");
            statusList.add(REJECTED);
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

        return detailMap;
    }

    /**
     * This method saves the workflow matrix details for every department selected
     *
     * @return
     */
    @Transactional
    public void save(final List<WorkFlowMatrix> actualWorkFlowMatrixDetails, final String[] departments) {

        for (final String dept : departments) {
            for (final WorkFlowMatrix workFlowMatrix : actualWorkFlowMatrixDetails) {
                final WorkFlowMatrix wfObj = workFlowMatrix.clone();
                if (DEFAULT.equals(dept)) {
                    wfObj.setDepartment("ANY");
                } else {
                    wfObj.setDepartment(dept);
                }
                workflowMatrixRepository.save(wfObj);
            }
        }
    }

    /**
     * This method gets the workflowdetails grouped by objecttype,additionalrule,from and todate,from qty and toqty
     *
     * @return
     */
    public List<WorkFlowMatrixDetails> getWorkFlowMatrixObjectForView(final Map workFlowObjectMap) {
        final Criteria workFlowCrit = entityQueryService.getSession().createCriteria(WorkFlowMatrix.class);
        if (workFlowObjectMap.get(OBJECTTYPE) != null && !workFlowObjectMap.get(OBJECTTYPE).equals("-1")) {
            workFlowCrit.add(Restrictions.eq(OBJECT_TYPE, getobjectTypebyId((Long) workFlowObjectMap.get(OBJECTTYPE)).getType()));
        }

        if (workFlowObjectMap.get(DEPARTMENTS) != null && !workFlowObjectMap.get(DEPARTMENTS).equals("-1")) {
            final String[] department = (String[]) workFlowObjectMap.get(DEPARTMENTS);
            workFlowCrit.add(Restrictions.eq(DEPARTMENT, department[0]));
        }
        if (workFlowObjectMap.get(ADDITIONALRULE) != null && !workFlowObjectMap.get(ADDITIONALRULE).equals("-1")) {
            workFlowCrit.add(Restrictions.eq(ADDITIONAL_RULE, workFlowObjectMap.get(ADDITIONALRULE)));
        }

        addProjectionsforCriteria(workFlowCrit);

        workFlowCrit.addOrder(Order.asc(FROM_DATE));
        workFlowCrit.addOrder(Order.asc(FROM_QTY));
        workFlowCrit.addOrder(Order.asc(ADDITIONAL_RULE));

        workFlowCrit.setResultTransformer(Transformers.aliasToBean(WorkFlowMatrixDetails.class));
        final List workFlowList = workFlowCrit.list();

        if (!workFlowList.isEmpty()) {
            final List<WorkFlowMatrixDetails> actList = checkWithOtherParams(workFlowList, workFlowObjectMap);
            return prepareWorkFlowResult(actList);
        } else {
            return Collections.emptyList();
        }
    }

    private void addProjectionsforCriteria(final Criteria workFlowCrit) {
        workFlowCrit.setProjection(Projections.projectionList().add(Projections.groupProperty(OBJECT_TYPE).as("objectTypeAlias")).add(Projections.groupProperty(DEPARTMENT).as("departmentAlias"))
                .add(Projections.groupProperty(ADDITIONAL_RULE).as("additionalRuleAlias")).add(Projections.groupProperty(FROM_QTY).as("fromQtyAlias")).add(Projections.groupProperty(TO_QTY).as("toQtyAlias"))
                .add(Projections.groupProperty(FROM_DATE).as("fromDateAlias")).add(Projections.groupProperty(TO_DATE).as("toDateAlias")));
    }

    /**
     * This method is used to prepare the workflow details that has to be shown in the view
     *
     * @return
     */
    private List<WorkFlowMatrixDetails> prepareWorkFlowResult(final List<WorkFlowMatrixDetails> matrixdetList) {
        for (final WorkFlowMatrixDetails det : matrixdetList) {
            det.setObjectTypeDisplay(getobjectTypebyName(det.getObjectType()).getDisplayName());
            final List<WorkFlowMatrix> workFlowdet = getMatrixdetails(det);
            List<WorkFlowMatrixDetails> detailsList = new LinkedList();
            detailsList = prepareWorkFlowMatrixDetailsList(sortListbyActions(workFlowdet), detailsList, Boolean.TRUE);
            det.setMatrixdetails(detailsList);
        }
        return matrixdetList;
    }

    public List prepareWorkFlowMatrixDetailsList(final List<WorkFlowMatrix> workFlowdet, final List<WorkFlowMatrixDetails> detailsList, final Boolean isReject) {
        for (final WorkFlowMatrix wfMatrixObj : workFlowdet) {
            final WorkFlowMatrixDetails details = new WorkFlowMatrixDetails();
            if (isReject) {
                details.setAction(wfMatrixObj.getNextAction());
                details.setState(wfMatrixObj.getNextState());
                details.setStatus(wfMatrixObj.getNextStatus());
                String[] buttonarr = {""};
                if (wfMatrixObj.getValidActions() != null) {
                    buttonarr = wfMatrixObj.getValidActions().split(",");
                }
                details.setButtons(buttonarr);
                String[] desnarr = {""};
                if (wfMatrixObj.getNextDesignation() != null) {
                    desnarr = wfMatrixObj.getNextDesignation().split(",");
                }
                details.setDesignation(desnarr);
                details.setStatus(wfMatrixObj.getNextStatus());
            } else {

                details.setRejectAction(wfMatrixObj.getNextAction());
                details.setRejectState(wfMatrixObj.getNextState());
                details.setRejectStatus(wfMatrixObj.getNextStatus());
                String[] buttonarr = {""};
                if (wfMatrixObj.getValidActions() != null) {
                    buttonarr = wfMatrixObj.getValidActions().split(",");
                }
                details.setRejectButtons(buttonarr);
                String[] desnarr = {""};
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
     *
     * @return
     */
    private List sortListbyActions(final List<WorkFlowMatrix> workFlowdet) {
        final LinkedList unsortedList = new LinkedList(workFlowdet);
        WorkFlowMatrix rejectedMatrix = null;
        final List<WorkFlowMatrix> workflowSortedList = new LinkedList<>();
        final List<WorkFlowMatrix> rejectedRelatedList = new LinkedList<>();
        final Iterator<WorkFlowMatrix> workFlowdetiterator = workFlowdet.iterator();
        while (workFlowdetiterator.hasNext()) {
            final WorkFlowMatrix wfMatrix = workFlowdetiterator.next();
            if (wfMatrix.getPendingActions() == null && "NEW".equalsIgnoreCase(wfMatrix.getCurrentState())) {
                workflowSortedList.add(wfMatrix);
                workFlowdetiterator.remove();
            }

        }

        Boolean isworkFlowCorrect = Boolean.TRUE;
        while (!workflowSortedList.isEmpty() && !"END".equalsIgnoreCase(workflowSortedList.get(workflowSortedList.size() - 1).getNextAction())) {
            final int size = workFlowdet.size();
            final String sortedwfMatrixnextAction = workflowSortedList.get(workflowSortedList.size() - 1).getNextAction();
            final String sortedwfMatrixnextState = workflowSortedList.get(workflowSortedList.size() - 1).getNextState();
            int count = 0;
            for (final WorkFlowMatrix wfMatrix : workFlowdet) {

                if (sortedwfMatrixnextAction.equalsIgnoreCase(wfMatrix.getPendingActions()) && sortedwfMatrixnextState.equalsIgnoreCase(wfMatrix.getCurrentState())) {
                    workflowSortedList.add(wfMatrix);
                } else {
                    count++;
                    if (count >= size) {
                        isworkFlowCorrect = Boolean.FALSE;
                        break;
                    }
                }
            }

            if (isworkFlowCorrect) {
                for (int j = 0; j < workFlowdet.size(); j++) {
                    final Long actwfMatrixnextAction = workFlowdet.get(j).getId();
                    for (final WorkFlowMatrix wfMatrix : workflowSortedList) {
                        if (actwfMatrixnextAction.equals(wfMatrix.getId())) {
                            workFlowdet.remove(j);
                        }
                    }
                }
            } else {
                break;
            }
        }

        if (isworkFlowCorrect) {

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
                }
            }

            if (rejectedMatrix != null) {
                workflowSortedList.add(rejectedMatrix);
            }

            for (final WorkFlowMatrix wfMatrix : rejectedRelatedList) {
                if (wfMatrix != null && !workflowSortedList.contains(wfMatrix)) {
                    workflowSortedList.add(wfMatrix);
                }
            }

            return workflowSortedList;
        } else {
            return unsortedList;
        }
    }

    /**
     * This method is used to get the list of matrix details for each record got from the grouped details from workflowmatrix table
     *
     * @return
     */
    private List<WorkFlowMatrix> getMatrixdetails(final WorkFlowMatrixDetails det) {
        final Criteria workFlowCrit = entityQueryService.getSession().createCriteria(WorkFlowMatrix.class);
        if (det.getObjectType() != null) {
            workFlowCrit.add(Restrictions.eq(OBJECT_TYPE, det.getObjectType()));
        }
        if (det.getAdditionalRule() != null) {
            workFlowCrit.add(Restrictions.eq(ADDITIONAL_RULE, det.getAdditionalRule()));
        } else {
            workFlowCrit.add(Restrictions.isNull(ADDITIONAL_RULE));
        }
        if (det.getDepartment() != null) {
            workFlowCrit.add(Restrictions.eq(DEPARTMENT, det.getDepartment()));
        }
        if (det.getFromDate() != null) {
            workFlowCrit.add(Restrictions.eq(FROM_DATE, det.getFromDate()));
        }
        if (det.getToDate() != null) {
            workFlowCrit.add(Restrictions.eq(TO_DATE, det.getToDate()));
        } else {
            workFlowCrit.add(Restrictions.isNull(TO_DATE));
        }
        if (det.getFromQty() != null) {
            workFlowCrit.add(Restrictions.eq(FROM_QTY, det.getFromQty()));
        } else {
            workFlowCrit.add(Restrictions.isNull(FROM_QTY));
        }
        if (det.getToQty() != null) {
            workFlowCrit.add(Restrictions.eq(TO_QTY, det.getToQty()));
        } else {
            workFlowCrit.add(Restrictions.isNull(TO_QTY));
        }
        return workFlowCrit.list();
    }

    /**
     * This method takes the workflowmatrix details grouped by objecttype,from and todate,from and to quantity,department and checks if there are any record that matched with the select criteria entered in the searchscreen like from and todate and from and to quantity
     *
     * @return
     */
    private List<WorkFlowMatrixDetails> checkWithOtherParams(final List<WorkFlowMatrixDetails> workFlowList, final Map workFlowObjectMap) {
        final List<WorkFlowMatrixDetails> tempList1 = new ArrayList();
        Date fromdate = null;
        Date todate = null;
        BigDecimal fromqty = null;
        BigDecimal toqty = null;
        if (workFlowObjectMap.get(FROMDATE) != null && workFlowObjectMap.get(FROMDATE) instanceof Date) {
            fromdate = (Date) workFlowObjectMap.get(FROMDATE);
        }
        if (workFlowObjectMap.get(TODATE) != null && workFlowObjectMap.get(TODATE) instanceof Date) {
            todate = (Date) workFlowObjectMap.get(TODATE);
        }
        if (workFlowObjectMap.get(FROMAMOUNT) != null && workFlowObjectMap.get(FROMAMOUNT) instanceof BigDecimal) {
            fromqty = (BigDecimal) workFlowObjectMap.get(FROMAMOUNT);
        }
        if (workFlowObjectMap.get(TOAMOUNT) != null && workFlowObjectMap.get(TOAMOUNT) instanceof BigDecimal) {
            toqty = (BigDecimal) workFlowObjectMap.get(TOAMOUNT);
        }

        if (fromdate != null && todate == null) {
            for (final WorkFlowMatrixDetails matrixdet : workFlowList) {
                if (checkFromDateIsNotBeforeMatrixDate(fromdate, matrixdet)
                        || (fromdate.before(matrixdet.getFromDate()) || fromdate.equals(matrixdet.getFromDate()))) {
                    tempList1.add(matrixdet);
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

                    if (matrixdet.getToQty() != null && (fromqty.doubleValue() >= (matrixdet.getFromQty().doubleValue()) && (toqty.doubleValue() <= (matrixdet.getToQty().doubleValue())))) {
                        //Do nothing ?
                    } else {
                        matrixiterator.remove();
                    }
                }
            }
        } else if (fromqty == null && toqty != null) {

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
        if (fromqty == null && toqty == null && fromdate == null && todate == null) {
            return workFlowList;
        } else {
            return tempList1;
        }

    }

    private boolean checkFromDateIsNotBeforeMatrixDate(final Date fromdate, final WorkFlowMatrixDetails matrixdet) {
        return matrixdet.getToDate() == null && matrixdet.getFromDate() != null && !fromdate.before(matrixdet.getFromDate());
    }

    /**
     * This method checks if there are any workflow matrix details for the parameters from,todate,from and to quantity,objecttype,department
     *
     * @return
     */
    public List checkIfMatrixExists(final Map workflowheaderparams) {

        final StringBuilder dateQryStr = new StringBuilder();
        final StringBuilder qntyQryStr = new StringBuilder();
        prepareQuery(workflowheaderparams, dateQryStr);
        prepareQuery(workflowheaderparams, qntyQryStr);

        dateQryStr.append(" having  fromDate <= :fromdate  and toDate is null ");
        dateQryStr.append(" or fromDate <= :fromdate  and toDate>= :fromdate   ");
        if (isNotBlank((String) workflowheaderparams.get(TODATE))) {
            dateQryStr.append(" or fromDate <= :todate  and toDate>= :todate   ");
        }

        final Query datequery = entityQueryService.getSession().createQuery(dateQryStr.toString());
        addParameter(workflowheaderparams, datequery);
        datequery.setParameter("fromdate", workflowheaderparams.get(FROMDATE));
        if (isNotBlank((String) workflowheaderparams.get(TODATE))) {
            datequery.setParameter("todate", workflowheaderparams.get(TODATE));
        }
        if (isNotBlank((String) workflowheaderparams.get(FROMAMOUNT))) {
            qntyQryStr.append(" having  fromQty <= :fromamount  and toQty is null ");
            qntyQryStr.append(" or fromQty <= :fromamount  and toQty>= :fromamount   ");
        }

        if (isNotBlank((String) workflowheaderparams.get(TOAMOUNT))) {
            qntyQryStr.append(" or fromQty <= :toamount  and toQty>= :toamount   ");

        }

        final Query qntyquery = entityQueryService.getSession().createQuery(qntyQryStr.toString());
        addParameter(workflowheaderparams, qntyquery);
        if (isNotBlank((String) workflowheaderparams.get(FROMAMOUNT))) {
            qntyquery.setParameter("fromamount", workflowheaderparams.get(FROMAMOUNT));
        }
        if (isNotBlank((String) workflowheaderparams.get(TOAMOUNT))) {
            qntyquery.setParameter("toamount", workflowheaderparams.get(TOAMOUNT));
        }

        return qntyquery.list();
    }

    /**
     * This method is used to prepare query that is used in checkIfMatrixExists method
     *
     * @return
     */
    private void prepareQuery(final Map workflowheaderparams, final StringBuilder qryStr) {
        qryStr.append("select id from  WorkFlowMatrix  where objectType = :objecttype and department IN (:departments) ");

        if (!"-1".equals(workflowheaderparams.get(ADDITIONALRULE))) {
            qryStr.append("and additionalRule = :additionalrule");
        }
        qryStr.append(" group by id,objectType,department,fromDate,toDate,fromQty,toQty ");

        if (!"-1".equals(workflowheaderparams.get(ADDITIONALRULE))) {
            qryStr.append(",additionalRule ");
        }
    }

    private void addParameter(final Map workflowheaderparams, final Query datequery) {
        datequery.setParameter(OBJECT_TYPE, getobjectTypebyId((Long) workflowheaderparams.get(OBJECTTYPE)).getType());
        if (!"-1".equals(workflowheaderparams.get(ADDITIONALRULE))) {
            datequery.setParameter("additionalrule", workflowheaderparams.get(ADDITIONALRULE));
        }
        datequery.setParameterList("departments", (String[]) workflowheaderparams.get(DEPARTMENTS));
    }

    /**
     * This method is used to delete the workflow matrix details
     */
    @Transactional
    public void deleteWorkFlowforObject(final Map workflowsearchparams) {

        final Criteria workFlowCrit = getCriteriaForDeleteorModify(workflowsearchparams);
        for (final WorkFlowMatrix matrix : (List<WorkFlowMatrix>) workFlowCrit.list()) {
            workflowMatrixRepository.delete(matrix);
        }

    }

    /**
     * This method is used to get the workflow matrix details
     */
    public List getWorkFlowforObjectforModify(final Map workflowsearchparams) {

        final Criteria workFlowCrit = getCriteriaForDeleteorModify(workflowsearchparams);
        final List<WorkFlowMatrix> matrixList = workFlowCrit.list();
        return sortListbyActions(matrixList);

    }

    /**
     * This method is used to get the criteria that can be used for modifying or deleting the workflow matrix details based on the search parameters
     */
    private Criteria getCriteriaForDeleteorModify(final Map workflowsearchparams) {
        final Criteria workFlowCrit = entityQueryService.getSession().createCriteria(WorkFlowMatrix.class);
        if (isNotBlank((String) workflowsearchparams.get(OBJECTTYPE))) {
            workFlowCrit.add(Restrictions.eq(OBJECT_TYPE, workflowsearchparams.get(OBJECTTYPE)));
        }
        if (isNotBlank((String) workflowsearchparams.get(ADDITIONALRULE)) && !"-1".equals(workflowsearchparams.get(ADDITIONALRULE))) {
            workFlowCrit.add(Restrictions.eq(ADDITIONAL_RULE, workflowsearchparams.get(ADDITIONALRULE)));
        }
        if (isNotBlank((String) workflowsearchparams.get(DEPARTMENTS))) {
            final String[] department = (String[]) workflowsearchparams.get(DEPARTMENTS);
            workFlowCrit.add(Restrictions.eq(DEPARTMENT, department[0]));
        }
        if (isNotBlank((String) workflowsearchparams.get(FROMDATE))) {
            workFlowCrit.add(Restrictions.eq(FROM_DATE, workflowsearchparams.get(FROMDATE)));
        }
        if (isNotBlank((String) workflowsearchparams.get(TODATE))) {
            workFlowCrit.add(Restrictions.eq(TO_DATE, workflowsearchparams.get(TODATE)));
        }
        if (isNotBlank((String) workflowsearchparams.get(FROMAMOUNT))) {
            workFlowCrit.add(Restrictions.eq(FROM_QTY, workflowsearchparams.get(FROMAMOUNT)));
        }
        if (isNotBlank((String) workflowsearchparams.get(TOAMOUNT))) {
            workFlowCrit.add(Restrictions.eq(TO_QTY, workflowsearchparams.get(TOAMOUNT)));
        }
        return workFlowCrit;
    }

    /**
     * This method is used to update the existing workflow matrix details for which modification has been done,only the todate is set
     *
     * @return
     */
    @Transactional
    public Boolean updateWorkFlowforObject(final Map workflowparams) {
        final Criteria workFlowCrit = getCriteriaForDeleteorModify(workflowparams);
        for (final WorkFlowMatrix matrix : (List<WorkFlowMatrix>) workFlowCrit.list()) {

            Date toDate = matrix.getToDate() == null ? (Date) workflowparams.get(MODIFYDATE) : matrix.getToDate();
            matrix.setToDate(toDate == null ? new Date() : toDate);

            if (matrix.getFromDate().equals(matrix.getToDate())) {
                return false;
            }
            workflowMatrixRepository.save(matrix);
        }
        return true;
    }

    /**
     * This method is used to check if any matrix exists for the ones which are being created for the dates lesser than the current date
     *
     * @return
     */
    public Date checkLegacyMatrix(final Map workflowheaderparams) {
        final Criteria workFlowCrit = entityQueryService.getSession().createCriteria(WorkFlowMatrix.class);
        if (isNotBlank((String) workflowheaderparams.get(OBJECTTYPE))) {
            workFlowCrit.add(Restrictions.eq(OBJECT_TYPE, getobjectTypebyId((Long) workflowheaderparams.get(OBJECTTYPE)).getType()));
        }
        if (!"-1".equals(workflowheaderparams.get(ADDITIONALRULE))) {
            workFlowCrit.add(Restrictions.eq(ADDITIONAL_RULE, workflowheaderparams.get(ADDITIONALRULE)));
        }
        if (!"-1".equals(workflowheaderparams.get(DEPARTMENTS))) {
            final String[] department = (String[]) workflowheaderparams.get(DEPARTMENTS);
            workFlowCrit.add(Restrictions.eq(DEPARTMENT, department[0]));
        }
        if (isNotBlank((String) workflowheaderparams.get(FROMDATE))) {
            workFlowCrit.add(Restrictions.ge(FROM_DATE, workflowheaderparams.get(FROMDATE)));
        }
        if (isNotBlank((String) workflowheaderparams.get(FROMAMOUNT))) {
            workFlowCrit.add(Restrictions.eq(FROM_QTY, workflowheaderparams.get(FROMAMOUNT)));
        }
        if (isNotBlank((String) workflowheaderparams.get(TOAMOUNT))) {
            workFlowCrit.add(Restrictions.eq(TO_QTY, workflowheaderparams.get(TOAMOUNT)));
        }
        addProjectionsforCriteria(workFlowCrit);
        workFlowCrit.addOrder(Order.asc(TO_DATE));
        workFlowCrit.setResultTransformer(Transformers.aliasToBean(WorkFlowMatrixDetails.class));
        final List<WorkFlowMatrixDetails> matrixList = workFlowCrit.list();
        if (!matrixList.isEmpty()) {
            return matrixList.get(0).getFromDateAlias();
        } else {
            return null;
        }

    }

    public WorkFlowMatrix getWorkFlowObjectbyId(final Long matrixid) {
        return workflowMatrixRepository.findOne(matrixid);
    }

}
