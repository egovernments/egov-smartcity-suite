/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.works.services;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.commons.dao.AccountdetailkeyHibernateDAO;
import org.egov.commons.dao.AccountdetailtypeHibernateDAO;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EmployeeService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.DeptDesig;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.works.utils.WorksConstants;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WorksService {
    private static final Logger logger = Logger.getLogger(WorksService.class);
    @Autowired
    private AppConfigValueService appConfigValuesService;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;
    private PersistenceService persistenceService;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private AccountdetailtypeHibernateDAO accountdetailtypeHibernateDAO;
    @Autowired
    private AccountdetailkeyHibernateDAO accountdetailkeyHibernateDAO;

    /**
     * This method will return the value in AppConfigValue table for the given module and key.
     *
     * @param moduleName
     * @param key
     * @return
     */
    public List<AppConfigValues> getAppConfigValue(final String moduleName, final String key) {
        return appConfigValuesService.getConfigValuesByModuleAndKey(moduleName, key);
    }

    public List<String> getNatureOfWorkAppConfigValues(final String moduleName, final String key) {
        final List<AppConfigValues> appValuesList = appConfigValuesService
                .getConfigValuesByModuleAndKey(moduleName, key);
        final List<String> natureOfWorksList = new ArrayList<String>();
        if (appValuesList != null && !appValuesList.isEmpty())
            for (final AppConfigValues appValue : appValuesList)
                natureOfWorksList.add(appValue.getValue());
        return natureOfWorksList;
    }

    public String getWorksConfigValue(final String key) {
        final List<AppConfigValues> configList = getAppConfigValue(WorksConstants.WORKS_MODULE_NAME, key);
        if (!configList.isEmpty())
            return configList.get(0).getValue();
        return null;
    }

    public Accountdetailtype getAccountdetailtypeByName(final String name) {
        return accountdetailtypeHibernateDAO.getAccountdetailtypeByName(name);
    }

    public Double getConfigval() {
        final String configVal = getWorksConfigValue("MAXEXTRALINEITEMPERCENTAGE");
        Double extraPercentage = null;
        if (StringUtils.isNotBlank(configVal))
            extraPercentage = Double.valueOf(configVal);
        else
            extraPercentage = Double.valueOf("1");
        return extraPercentage;
    }

    /*
     * returns employee name and designation
     * @ return String
     * @ abstractEstimate, employeeService
     */
    public String getEmpNameDesignation(final Position position, final Date date) {
        String empName = "";
        String designationName = "";
        final DeptDesig deptDesig = position.getDeptDesig();
        final Designation designationMaster = deptDesig.getDesignation();
        designationName = designationMaster.getName();
        final Employee employee = assignmentService.getPrimaryAssignmentForPositionAndDate(position.getId(), date).getEmployee();

        if (employee != null && employee.getName() != null)
            empName = employee.getName();

        return empName + "@" + designationName;
    }

    /*
     * public String getEmpNameDesignation(Position position){ String empName=""; String designationName="";
     * //abstractEstimate.getState().getOwner() DeptDesig deptDesig= position.getDeptDesigId(); DesignationMaster
     * designationMaster=deptDesig.getDesigId(); designationName=designationMaster.getDesignationName(); PersonalInformation
     * personalInformation=null; try { personalInformation=employeeService .getEmpForPositionAndDate(position.getEfferctiveDate(),
     * position.getId()); } catch (Exception e) { logger.debug("exception "+e); } if(personalInformation!=null &&
     * personalInformation.getEmployeeName()!=null) empName=personalInformation.getEmployeeName(); return
     * empName+"@"+designationName; }
     */

    /**
     * if the bigdecimal obj1 is greater than or egual to obj2 then it returns false
     *
     * @param obj1
     * @param obj2
     * @return
     */
    public boolean checkBigDecimalValue(final BigDecimal obj1, final BigDecimal obj2) {
        if (obj1 == null)
            return true;
        if (obj2 == null)
            return true;
        if (obj1.compareTo(obj2) == -1)
            return false;
        if (obj1.compareTo(obj2) == 0)
            return false;
        return true;
    }

    /**
     * @return list of egwstatus objects
     */
    public List<EgwStatus> getStatusesByParams(final String objStatus, final String objSetStatus,
            final String objLastStatus, final String objType) {
        final List<String> statList = new ArrayList<String>();
        if (StringUtils.isNotBlank(objStatus))
            statList.add(objStatus);
        if (StringUtils.isNotBlank(objSetStatus) && StringUtils.isNotBlank(objLastStatus)) {
            final List<String> statusList = Arrays.asList(objSetStatus.split(","));
            for (final String stat : statusList)
                if (stat.equals(objLastStatus)) {
                    statList.add(stat);
                    break;
                } else
                    statList.add(stat);
        }
        return egwStatusHibernateDAO.getStatusListByModuleAndCodeList(objType, statList);
    }

    public void createAccountDetailKey(final Long id, final String type) {
        final Accountdetailtype accountdetailtype = getAccountdetailtypeByName(type);
        final Accountdetailkey adk = new Accountdetailkey();
        adk.setGroupid(1);
        adk.setDetailkey(id.intValue());
        adk.setDetailname(accountdetailtype.getAttributename());
        adk.setAccountdetailtype(accountdetailtype);
        accountdetailkeyHibernateDAO.create(adk);
    }

    public List getWorksRoles() {
        final String configVal = getWorksConfigValue("WORKS_ROLES");
        final List rolesList = new ArrayList();
        if (StringUtils.isNotBlank(configVal)) {
            final String[] configVals = configVal.split(",");
            for (final String configVal2 : configVals)
                rolesList.add(configVal2);
        }
        return rolesList;
    }

    public List<String> getTendertypeList() {
        final String tenderConfigValues = getWorksConfigValue(WorksConstants.TENDER_TYPE);
        return Arrays.asList(tenderConfigValues.split(","));
    }

    public boolean validateWorkflowForUser(final StateAware wfObj, final User user) {

        boolean validateUser = true;
        List<Assignment> assignmentList = null;
        final List<Position> positionList = new ArrayList<Position>();
        if (user != null && wfObj.getCurrentState() != null
                && !wfObj.getCurrentState().getValue().equals(WorksConstants.END)) {
            assignmentList = assignmentService.findByEmployeeAndGivenDate(user.getId(), new Date());
            for (final Assignment assignment : assignmentList)
                positionList.add(assignment.getPosition());

            if (!positionList.isEmpty() && positionList.contains(wfObj.getCurrentState().getOwnerPosition()))
                validateUser = false;
        }

        return validateUser;
    }

    public Long getCurrentLoggedInUserId() {
        return ApplicationThreadLocals.getUserId();
    }

    public User getCurrentLoggedInUser() {
        return (User) persistenceService.getSession().load(User.class, ApplicationThreadLocals.getUserId());
    }

    public Map<String, Integer> getExceptionSOR() {
        final List<AppConfigValues> appConfigList = getAppConfigValue(WorksConstants.WORKS_MODULE_NAME, "EXCEPTIONALSOR");
        final Map<String, Integer> resultMap = new HashMap<String, Integer>();
        for (final AppConfigValues configValue : appConfigList) {
            final String value[] = configValue.getValue().split(",");
            resultMap.put(value[0], Integer.valueOf(value[1]));
        }
        return resultMap;
    }

    /**
     * NOTE --- THIS API IS USED ONLY FOR WORK PROGRESS ABSTRACT REPORT BY DEPARTMENT IN WORKS MODULE
     *
     * @description -This method returns the total payment amount and payment count made till date for the Project code Ids
     * present in the temporary table WorkProgressProjectCode for a particular uuid
     * @param uuid - Only project codes ids associated with this uuid are considered
     * @return - List of Maps of department name, total amount of approved payments and count made for all the bills made against
     * project codes
     * @return - Null is returned in the case of no data
     * @throws ApplicationException - If anyone of the parameters is null or the ProjectCode list passed is empty.
     */
    public List<Map<String, Object>> getWorkProgressTotalPayments(final String uuid) throws ApplicationException {
        Map<String, Object> result = null;
        List<Object[]> objForExpense;
        final List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        final String payQuery = " select dept.dept_name,nvl(sum(bp.debitamount),0),count(payvh.id) FROM "
                + " eg_billregister br,eg_billdetails bd, eg_billpayeedetails bp,voucherheader vh,eg_billregistermis ms, voucherheader payvh, miscbilldetail misc, eg_department dept  "
                + " WHERE br.id=bd.billid and bd.id=bp.BILLDETAILID and vh.id=ms.VOUCHERHEADERID and ms.BILLID=br.id and misc.billvhid=vh.id and misc.payvhid=payvh.id and bp.pc_department=dept.id_dept  and payvh.status="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " and br.STATUSID in(select id from egw_status where lower(code)='approved' and "
                + " moduletype in('SALBILL','EXPENSEBILL','SBILL','CONTRACTORBILL','CBILL')) "
                + " and bd.DEBITAMOUNT>0  and vh.STATUS="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " and bp.ACCOUNTDETAILTYPEID= (SELECT id FROM accountdetailtype "
                + " WHERE name ='PROJECTCODE' AND description='PROJECTCODE' ) and (bp.ACCOUNTDETAILKEYID in(SELECT PC_ID FROM EGW_WORKPROGRESS_PROJECT_CODE WHERE UUID like '"
                + uuid + "')) group by dept.dept_name ";

        objForExpense = persistenceService.getSession().createSQLQuery(payQuery).list();
        if (objForExpense != null && objForExpense.size() != 0) {
            for (Integer i = 0; i < objForExpense.size(); i++) {
                result = new HashMap<String, Object>();
                result.put("deptName", objForExpense.get(i)[0].toString());
                result.put("amount", objForExpense.get(i)[1].toString());
                result.put("count", objForExpense.get(i)[2].toString());
                resultList.add(result);
            }
            return resultList;
        } else
            return null;
    }

    /**
     * NOTE --- THIS API IS USED ONLY FOR WORK PROGRESS ABSTRACT REPORT BY DEPARTMENT IN WORKS MODULE
     *
     * @description -This method returns the total approved voucher count created till date for the Project code Ids present in
     * the temporary table WorkProgressProjectCode for a particular uuid
     * @param uuid - Only project codes ids associated with this uuid are considered
     * @return - List of Maps of depart name, total approved voucher count
     * @return - Null is returned in the case of no data
     * @throws ApplicationException - If anyone of the parameters is null or the ProjectCode list passed is empty.
     */
    public List<Map<String, Object>> getVoucherCounts(final String uuid) throws ApplicationException {
        List<Object[]> queryResult;
        final List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> resultMap = null;
        final String countQry = " select dept.dept_name, count(distinct(vh.id))  FROM "
                + "eg_billregister br,eg_billdetails bd, eg_billpayeedetails bp,voucherheader vh,eg_billregistermis ms, eg_department dept "
                + "WHERE br.id=bd.billid and bd.id=bp.BILLDETAILID and vh.id=ms.VOUCHERHEADERID and ms.BILLID=br.id  and bp.pc_department=dept.id_dept and vh.name='Contractor Journal'  "
                + "and br.STATUSID in(select id from egw_status where lower(code)='approved' and "
                + "moduletype in('CONTRACTORBILL')) "
                + "and bd.DEBITAMOUNT>0  and vh.STATUS="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " and bp.ACCOUNTDETAILTYPEID= (SELECT id FROM accountdetailtype "
                + "WHERE name ='PROJECTCODE' AND description='PROJECTCODE' ) and bp.ACCOUNTDETAILKEYID in(SELECT PC_ID FROM EGW_WORKPROGRESS_PROJECT_CODE WHERE UUID like '"
                + uuid + "')  group by dept.dept_name ";

        queryResult = persistenceService.getSession().createSQLQuery(countQry).list();
        if (queryResult != null && queryResult.size() != 0) {
            for (Integer i = 0; i < queryResult.size(); i++) {
                resultMap = new HashMap<String, Object>();
                resultMap.put("deptName", queryResult.get(i)[0].toString());
                resultMap.put("count", new Integer(queryResult.get(i)[1].toString()));
                resultList.add(resultMap);
            }
            return resultList;
        } else
            return null;
    }

    /**
     * NOTE --- THIS API IS USED ONLY FOR WORK PROGRESS ABSTRACT REPORT BY DEPARTMENT IN WORKS MODULE
     *
     * @description -This method returns the approved CJV count and sum of CJVs amount for the approved CJVs made till date for a
     * list of Project codes for which there is a final bill created for it. Project code Ids present in the temporary table
     * WorkProgressProjectCode for a particular uuid are only considered NOTE --- ASSUMPTION IS THERE WILL BE ONLY 1 FINAL BILL
     * FOR AN ESTIMATE
     * @param uuid - Only project codes ids associated with this uuid are considered
     * @return - List of Map of Maps. The outer map's key is the department name Inner map's keys "amount" and "count" represent
     * sum of CJVs amount and approved CJV count
     * @return - Null is returned in the case of no data
     * @throws ApplicationException - If anyone of the parameters is null or the ProjectCode list passed is empty.
     */
    public List<Map<String, Map<String, BigDecimal>>> getTotalCJVCountAndAmounts(final String uuid)
            throws ApplicationException {

        Map<String, Map<String, BigDecimal>> resultMap = null;
        Map<String, BigDecimal> simpleMap = null;
        List<Object[]> payQueryResult;
        List<Object[]> countQueryResult;
        final List<Map<String, Map<String, BigDecimal>>> resultList = new ArrayList<Map<String, Map<String, BigDecimal>>>();

        final String payQuery = " select dept.dept_name , nvl(sum(bp.debitamount),0)  FROM "
                + " eg_billregister br,eg_billdetails bd, eg_billpayeedetails bp,voucherheader vh,eg_billregistermis ms , eg_department dept "
                + " WHERE br.id=bd.billid and bd.id=bp.BILLDETAILID and vh.id=ms.VOUCHERHEADERID and ms.BILLID=br.id and bp.pc_department=dept.id_dept  and vh.name='Contractor Journal'  "
                + " and br.STATUSID in(select id from egw_status where lower(code)='approved' and "
                + " moduletype in('CONTRACTORBILL')) "
                + " and bd.DEBITAMOUNT>0  and vh.STATUS="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " and bp.ACCOUNTDETAILTYPEID= (SELECT id FROM accountdetailtype "
                + " WHERE name ='PROJECTCODE' AND description='PROJECTCODE' ) and (bp.ACCOUNTDETAILKEYID in(SELECT PC_ID FROM EGW_WORKPROGRESS_PROJECT_CODE WHERE UUID like '"
                + uuid
                + "'))"
                + " and bp.accountdetailkeyid in ( select bp1.ACCOUNTDETAILKEYID FROM eg_billregister br1, "
                + " eg_billdetails bd1, eg_billpayeedetails bp1 ,voucherheader vh1,eg_billregistermis ms1  WHERE br1.id =bd1.billid AND bd1.id =bp1.BILLDETAILID  "
                + "  and vh1.id=ms1.VOUCHERHEADERID and ms1.BILLID=br1.id and vh1.name='Contractor Journal' and vh1.STATUS="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " AND br1.STATUSID IN (SELECT id FROM egw_status WHERE lower(code)='approved' AND moduletype  IN('CONTRACTORBILL')) "
                + " and br1.billtype in ('FinalBill', 'Final Bill') and bp1.ACCOUNTDETAILKEYID = bp.ACCOUNTDETAILKEYID "
                + " and bp1.ACCOUNTDETAILTYPEID=bp.ACCOUNTDETAILTYPEID ) group by dept.dept_name  order by dept.dept_name  ";

        final String countQuery = " select dept.dept_name , count(vh.id) FROM "
                + " eg_billregister br,eg_billdetails bd, eg_billpayeedetails bp,voucherheader vh,eg_billregistermis ms, eg_department dept  "
                + " WHERE br.id=bd.billid and bd.id=bp.BILLDETAILID and vh.id=ms.VOUCHERHEADERID and ms.BILLID=br.id and bp.pc_department=dept.id_dept  and vh.name='Contractor Journal' "
                + " and br.billtype in ('FinalBill', 'Final Bill') "
                + " and br.STATUSID in(select id from egw_status where lower(code)='approved' and "
                + " moduletype in('CONTRACTORBILL')) "
                + " and bd.DEBITAMOUNT>0  and vh.STATUS="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " and bp.ACCOUNTDETAILTYPEID= (SELECT id FROM accountdetailtype "
                + " WHERE name ='PROJECTCODE' AND description='PROJECTCODE' ) and (bp.ACCOUNTDETAILKEYID in(SELECT PC_ID FROM EGW_WORKPROGRESS_PROJECT_CODE WHERE UUID like '"
                + uuid + "'))  group by dept.dept_name order by dept.dept_name ";

        payQueryResult = persistenceService.getSession().createSQLQuery(payQuery).list();
        countQueryResult = persistenceService.getSession().createSQLQuery(countQuery).list();
        final List<String> deptNameList = new ArrayList<String>();
        if (payQueryResult != null && payQueryResult.size() > 0)
            for (Integer i = 0; i < payQueryResult.size(); i++)
                deptNameList.add(payQueryResult.get(i)[0].toString());
        if (countQueryResult != null && countQueryResult.size() > 0)
            for (Integer i = 0; i < countQueryResult.size(); i++)
                deptNameList.add(countQueryResult.get(i)[0].toString());
        if (deptNameList == null || !(deptNameList.size() > 0))
            return null;
        // To remove duplicates
        final HashSet<String> tempSet = new HashSet<String>();
        tempSet.addAll(deptNameList);
        deptNameList.clear();
        deptNameList.addAll(tempSet);
        final BigDecimal[] payArray = new BigDecimal[deptNameList.size()];
        final BigDecimal[] countArray = new BigDecimal[deptNameList.size()];
        for (Integer i = 0; i < deptNameList.size(); i++) {
            payArray[i] = BigDecimal.ZERO;
            countArray[i] = BigDecimal.ZERO;
        }
        String deptName = null;
        Integer index = null;
        Integer i = null;
        for (i = 0; i < payQueryResult.size(); i++) {
            deptName = payQueryResult.get(i)[0].toString();
            index = deptNameList.indexOf(deptName);
            payArray[index] = new BigDecimal(payQueryResult.get(i)[1].toString());
        }
        for (i = 0; i < countQueryResult.size(); i++) {
            deptName = countQueryResult.get(i)[0].toString();
            index = deptNameList.indexOf(deptName);
            countArray[index] = new BigDecimal(countQueryResult.get(i)[1].toString());
        }
        for (i = 0; i < deptNameList.size(); i++) {
            deptName = deptNameList.get(i);
            simpleMap = new HashMap<String, BigDecimal>();
            simpleMap.put("amount", payArray[i]);
            simpleMap.put("count", countArray[i]);
            resultMap = new HashMap<String, Map<String, BigDecimal>>();
            resultMap.put(deptName, simpleMap);
            resultList.add(resultMap);
        }
        return resultList;
    }

    /**
     * NOTE --- THIS API IS USED ONLY FOR WORK PROGRESS ABSTRACT REPORT BY DEPARTMENT IN WORKS MODULE
     *
     * @description -This method returns the total payment amount and payment count made till date for the Project code Ids
     * present in the temporary table WorkProgressProjectCode for a particular uuid
     * @param uuid - Only project codes ids associated with this uuid are considered
     * @return - List of Maps of department name, total amount of approved payments and count made for all the bills made against
     * project codes
     * @return - Null is returned in the case of no data
     * @throws ApplicationException - If anyone of the parameters is null or the ProjectCode list passed is empty.
     */
    public List<Map<String, Object>> getWorkProgressTotal(final String uuid) throws ApplicationException {
        Map<String, Object> result = null;
        List<Object[]> objForExpense;
        final List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        final String payQuery = " select dept.dept_name,nvl(sum(bp.debitamount),0),count(payvh.id) FROM "
                + " eg_billregister br,eg_billdetails bd, eg_billpayeedetails bp,voucherheader vh,eg_billregistermis ms, voucherheader payvh, miscbilldetail misc, eg_department dept  "
                + " WHERE br.id=bd.billid and bd.id=bp.BILLDETAILID and vh.id=ms.VOUCHERHEADERID and ms.BILLID=br.id and misc.billvhid=vh.id and misc.payvhid=payvh.id and bp.pc_department=dept.id_dept  and payvh.status="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " and br.STATUSID in(select id from egw_status where lower(code)='approved' and "
                + " moduletype in('SALBILL','EXPENSEBILL','SBILL','CONTRACTORBILL','CBILL')) "
                + " and bd.DEBITAMOUNT>0  and vh.STATUS="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " and bp.ACCOUNTDETAILTYPEID= (SELECT id FROM accountdetailtype "
                + " WHERE name ='PROJECTCODE' AND description='PROJECTCODE' ) and (bp.ACCOUNTDETAILKEYID in(SELECT PC_ID FROM EGW_WORKPROGRESS_PROJECT_CODE WHERE UUID like '"
                + uuid + "')) group by dept.dept_name ";

        objForExpense = persistenceService.getSession().createSQLQuery(payQuery).list();
        if (objForExpense != null && objForExpense.size() != 0) {
            for (Integer i = 0; i < objForExpense.size(); i++) {
                result = new HashMap<String, Object>();
                result.put("deptName", objForExpense.get(i)[0].toString());
                result.put("amount", objForExpense.get(i)[1].toString());
                result.put("count", objForExpense.get(i)[2].toString());
                resultList.add(result);
            }
            return resultList;
        } else
            return null;
    }

    /**
     * NOTE --- THIS API IS USED ONLY FOR WORK PROGRESS ABSTRACT REPORT 2 BY DEPARTMENT IN WORKS MODULE
     *
     * @description -This method returns the total payment amount and payment count made for given start date and end date for the
     * Project code Ids present in the temporary table WorkProgressProjectCode for a particular uuid This will also return the
     * number of cpncurrence payment given and the total amount for which concurrence is given
     * @param uuid - Only project codes ids associated with this uuid are considered
     * @return - List of Maps of department name, total amount of approved payments and count made for all the bills made against
     * project codes
     * @return - Null is returned in the case of no data
     * @throws ApplicationException - If anyone of the parameters is null or the ProjectCode list passed is empty.
     */
    public List<Map<String, Object>> getWorkProgressAbstractReport2TotalPayments(final String uuid,
            final Date fromDate, final Date toDate) throws ApplicationException {
        Map<String, Object> result = null;
        List<Object[]> objForExpense;
        final List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        final String payQuery = "select \"Department\" as \"DEP\",sum(\"Approved Payment Amount\")as \"APP_PAY\",sum(\"No of Approved Payment\") as \"APP_PAY_COUNT\","
                + " sum(\"Concurrence Payment Amount\")as \"CON_AMT\",sum(\"No of Concurrence Given\")as \"CON_PAY_COUNT\" FROM("
                + " select dept.dept_name as \"Department\",nvl(sum(bp.debitamount),0) \"Approved Payment Amount\",count(distinct payvh.id) \"No of Approved Payment\", 0 \"Concurrence Payment Amount\", 0 \"No of Concurrence Given\" "
                + " FROM eg_billregister br,eg_billdetails bd, eg_billpayeedetails bp,voucherheader vh,eg_billregistermis ms, voucherheader payvh, miscbilldetail misc, eg_department dept  "
                + " WHERE br.id=bd.billid and bd.id=bp.BILLDETAILID and vh.id=ms.VOUCHERHEADERID and ms.BILLID=br.id and misc.billvhid=vh.id and misc.payvhid=payvh.id and bp.pc_department=dept.id_dept  and payvh.status="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " and br.STATUSID in(select id from egw_status where lower(code)='approved' and "
                + " moduletype in('EXPENSEBILL','CONTRACTORBILL','CBILL')) "
                + " and br.BILLDATE between '"
                + dateFormatter.format(fromDate)
                + "' and '"
                + dateFormatter.format(toDate)
                + "' "
                + " and bd.DEBITAMOUNT>0  and vh.STATUS="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " and bp.ACCOUNTDETAILTYPEID= (SELECT id FROM accountdetailtype "
                + " WHERE name ='PROJECTCODE' AND description='PROJECTCODE' ) and (bp.ACCOUNTDETAILKEYID in(SELECT PC_ID FROM EGW_WORKPROGRESS_PROJECT_CODE WHERE UUID like '"
                + uuid
                + "')) group by dept.dept_name "
                + " UNION ALL "
                + " select dept.dept_name as \"Department\",0 \"Approved Payment Amount\",0 \"No of Approved Payment\" "
                + ",nvl(sum(bp.debitamount),0) \"Concurrence Payment Amount\",count(distinct payvh.id) \"No of Concurrence Given\" "
                + " FROM eg_billregister br,eg_billdetails bd, eg_billpayeedetails bp,voucherheader vh,eg_billregistermis ms,"
                + " voucherheader payvh, miscbilldetail misc, eg_department dept,paymentheader ph   "
                + " WHERE br.id=bd.billid and bd.id=bp.BILLDETAILID and vh.id=ms.VOUCHERHEADERID and ms.BILLID=br.id and misc.billvhid=vh.id and misc.payvhid=payvh.id and bp.pc_department=dept.id_dept "
                + " and br.STATUSID in(select id from egw_status where lower(code)='approved' and "
                + " moduletype in('EXPENSEBILL','CONTRACTORBILL','CBILL')) "
                + " and br.BILLDATE between '"
                + dateFormatter.format(fromDate)
                + "' and '"
                + dateFormatter.format(toDate)
                + "' "
                + " and bd.DEBITAMOUNT>0  and ph.VOUCHERHEADERID=payvh.id and (payvh.status="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " or (payvh.status="
                + FinancialConstants.PREAPPROVEDVOUCHERSTATUS
                + " and ph.CONCURRENCEDATE is not null))"
                + " and bp.ACCOUNTDETAILTYPEID= (SELECT id FROM accountdetailtype "
                + " WHERE name ='PROJECTCODE' AND description='PROJECTCODE' ) and (bp.ACCOUNTDETAILKEYID in(SELECT PC_ID FROM EGW_WORKPROGRESS_PROJECT_CODE WHERE UUID like '"
                + uuid + "')) group by dept.dept_name " + " )group by \"Department\"";

        logger.debug("Payment query inside getWorkProgressAbstractReport2TotalPayments :" + payQuery);

        objForExpense = persistenceService.getSession().createSQLQuery(payQuery).list();
        if (objForExpense != null && objForExpense.size() != 0) {
            for (Integer i = 0; i < objForExpense.size(); i++) {
                result = new HashMap<String, Object>();
                result.put("deptName", objForExpense.get(i)[0].toString());
                result.put("amount", objForExpense.get(i)[1].toString());
                result.put("count", objForExpense.get(i)[2].toString());
                result.put("concAmount", objForExpense.get(i)[3].toString());
                result.put("concCount", objForExpense.get(i)[4].toString());
                resultList.add(result);
            }
            return resultList;
        } else
            return null;
    }

    /**
     * @description - Similar to getWorkProgressAbstractReport2TotalPaymentsAPI except that concurrence payments are not
     * considered Payments only for the project code id that is passed to the API are considered
     * @param Project code id for which the payment amount should be considered
     * @return - Total amount of approved payments
     * @return - Null is returned in the case of no data
     * @throws ApplicationException - If the parameter is null
     */
    public BigDecimal getTotalPaymentForProjectCode(final Long projcodeId) throws ApplicationException {
        List<Object> objForExpense;
        final String payQuery = " select nvl(sum(nvl(bp.debitamount,0)),0)  "
                + " FROM eg_billregister br,eg_billdetails bd, eg_billpayeedetails bp,voucherheader vh,eg_billregistermis ms, "
                + " voucherheader payvh, miscbilldetail misc, eg_department dept  "
                + " WHERE br.id=bd.billid and bd.id=bp.BILLDETAILID and vh.id=ms.VOUCHERHEADERID and ms.BILLID=br.id and misc.billvhid=vh.id "
                + " and misc.payvhid=payvh.id and bp.pc_department=dept.id_dept  and payvh.status="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " and br.STATUSID in(select id from egw_status where lower(code)='approved' and "
                + " moduletype in('EXPENSEBILL','CONTRACTORBILL','CBILL')) " + " and bd.DEBITAMOUNT>0  and vh.STATUS="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " and bp.ACCOUNTDETAILTYPEID= (SELECT id FROM accountdetailtype "
                + " WHERE name ='PROJECTCODE' AND description='PROJECTCODE' ) and (bp.ACCOUNTDETAILKEYID in('"
                + projcodeId + "'))  ";

        logger.debug("Payment query inside getTotalPaymentForProjectCode :" + payQuery);

        objForExpense = persistenceService.getSession().createSQLQuery(payQuery).list();
        if (objForExpense != null && objForExpense.size() != 0 && objForExpense.get(0) != null
                && !objForExpense.get(0).toString().equalsIgnoreCase("0")) {
            final BigDecimal result = new BigDecimal(objForExpense.get(0).toString());
            return result;
        } else
            return null;
    }

    /**
     * NOTE --- THIS API IS USED ONLY FOR WORK PROGRESS ABSTRACT REPORT 2 BY DEPARTMENT IN WORKS MODULE
     *
     * @description -This method returns the total approved voucher(CJV) count and amount for given start date and end date for
     * the Project code Ids present in the temporary table WorkProgressProjectCode for a particular uuid
     * @param uuid - Only project codes ids associated with this uuid are considered
     * @return - List of Maps of depart name, total approved voucher count
     * @return - Null is returned in the case of no data
     * @throws ApplicationException - If anyone of the parameters is null or the ProjectCode list passed is empty.
     */
    public List<Map<String, Object>> getWorkProgressAbstractReport2VoucherCounts(final String uuid,
            final Date fromDate, final Date toDate) throws ApplicationException {
        List<Object[]> queryResult;
        final List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> resultMap = null;
        final String countQry = " select dept.dept_name, count(distinct(vh.id)), nvl(sum(bp.debitamount),0)  FROM "
                + "eg_billregister br,eg_billdetails bd, eg_billpayeedetails bp,voucherheader vh,eg_billregistermis ms, eg_department dept "
                + "WHERE br.id=bd.billid and bd.id=bp.BILLDETAILID and vh.id=ms.VOUCHERHEADERID and ms.BILLID=br.id  and bp.pc_department=dept.id_dept and vh.name='Contractor Journal'  "
                + "and br.STATUSID in(select id from egw_status where lower(code)='approved' and "
                + "moduletype in('CONTRACTORBILL')) "
                + " and br.BILLDATE between '"
                + dateFormatter.format(fromDate)
                + "' and '"
                + dateFormatter.format(toDate)
                + "' "
                + "and bd.DEBITAMOUNT>0  and vh.STATUS="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " and bp.ACCOUNTDETAILTYPEID= (SELECT id FROM accountdetailtype "
                + "WHERE name ='PROJECTCODE' AND description='PROJECTCODE' ) and bp.ACCOUNTDETAILKEYID in(SELECT PC_ID FROM EGW_WORKPROGRESS_PROJECT_CODE WHERE UUID like '"
                + uuid + "')  group by dept.dept_name ";

        queryResult = persistenceService.getSession().createSQLQuery(countQry).list();
        if (queryResult != null && queryResult.size() != 0) {
            for (Integer i = 0; i < queryResult.size(); i++) {
                resultMap = new HashMap<String, Object>();
                resultMap.put("deptName", queryResult.get(i)[0].toString());
                resultMap.put("count", new Integer(queryResult.get(i)[1].toString()));
                resultMap.put("amount", queryResult.get(i)[2].toString());
                resultList.add(resultMap);
            }
            return resultList;
        } else
            return null;
    }

    /**
     * NOTE --- THIS API IS USED ONLY FOR WORK PROGRESS ABSTRACT REPORT BY DEPARTMENT IN WORKS MODULE
     *
     * @description -This method returns the total payment amount and payment count made till date for the Project code Ids
     * present in the temporary table WorkProgProjCodeSpillOver for a particular uuid
     * @param uuid - Only project codes ids associated with this uuid are considered
     * @return - List of Maps of department name, total amount of approved payments and count made for all the bills made against
     * project codes
     * @return - Null is returned in the case of no data
     * @throws ApplicationException - If anyone of the parameters is null or the ProjectCode list passed is empty.
     */
    public List<Map<String, Object>> getWorkProgSpillOverTotalPayments(final String uuid, final Date fromDate,
            final Date toDate) throws ApplicationException {
        Map<String, Object> result = null;
        List<Object[]> objForExpense;
        final List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        final String payQuery = " select \"Dept\",sum(\"Approved_Pay\") as \"APP_PAY\",sum(\"No_Approved_Pay\") as \"APP_PAY_COUNT\","
                + " sum(\"Concurrence_Pay_Amount\")as \"CON_AMT\",sum(\"No_Concurrence_Pay\")as \"CON_PAY_COUNT\" FROM("
                + " select dept.dept_name as \"Dept\",nvl(sum(bp.debitamount),0) as \"Approved_Pay\",count(distinct payvh.id) \"No_Approved_Pay\""
                + " ,0 \"Concurrence_Pay_Amount\",0 \"No_Concurrence_Pay\""
                + " FROM eg_billregister br,eg_billdetails bd, eg_billpayeedetails bp,voucherheader vh,eg_billregistermis ms, "
                + " voucherheader payvh, miscbilldetail misc, eg_department dept  "
                + " WHERE br.id=bd.billid and bd.id=bp.BILLDETAILID and vh.id=ms.VOUCHERHEADERID and ms.BILLID=br.id "
                + " and misc.billvhid=vh.id and misc.payvhid=payvh.id and bp.pc_department=dept.id_dept  "
                + " and payvh.status="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " and br.STATUSID in(select id from egw_status where lower(code)='approved' and "
                + " moduletype in('EXPENSEBILL','CONTRACTORBILL','CBILL')) "
                + " and bd.DEBITAMOUNT>0  and vh.STATUS="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " and br.BILLDATE between '"
                + dateFormatter.format(fromDate)
                + "' and '"
                + dateFormatter.format(toDate)
                + "' "
                + " and bp.ACCOUNTDETAILTYPEID= (SELECT id FROM accountdetailtype "
                + " WHERE name ='PROJECTCODE' AND description='PROJECTCODE' ) "
                + " and (bp.ACCOUNTDETAILKEYID in(SELECT PC_ID FROM EGW_WRKPROG_PROJCODE_SPILLOVER WHERE UUID like '"
                + uuid
                + "')) "
                + " group by dept.dept_name "
                + " UNION ALL "
                + "select dept.dept_name as \"Dept\",0 as \"Approved_Pay\",0 \"No_Approved_Pay\" "
                + ",nvl(sum(bp.debitamount),0) \"Concurrence_Payment_Amount\",count(distinct payvh.id) \"No_Concurrence_Given\""
                + " FROM eg_billregister br,eg_billdetails bd, eg_billpayeedetails bp,voucherheader vh,eg_billregistermis ms, "
                + " voucherheader payvh, miscbilldetail misc, eg_department dept,paymentheader ph  "
                + " WHERE br.id=bd.billid and bd.id=bp.BILLDETAILID and vh.id=ms.VOUCHERHEADERID and ms.BILLID=br.id "
                + " and misc.billvhid=vh.id and misc.payvhid=payvh.id and bp.pc_department=dept.id_dept  "
                + " and payvh.status="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " and br.STATUSID in(select id from egw_status where lower(code)='approved' and "
                + " moduletype in('EXPENSEBILL','CONTRACTORBILL','CBILL')) "
                + " and bd.DEBITAMOUNT>0  "
                + " and ph.VOUCHERHEADERID=payvh.id and (payvh.status="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " or (payvh.status="
                + FinancialConstants.PREAPPROVEDVOUCHERSTATUS
                + " and ph.CONCURRENCEDATE is not null))"
                + " and br.BILLDATE between '"
                + dateFormatter.format(fromDate)
                + "' and '"
                + dateFormatter.format(toDate)
                + "' "
                + " and bp.ACCOUNTDETAILTYPEID= (SELECT id FROM accountdetailtype "
                + " WHERE name ='PROJECTCODE' AND description='PROJECTCODE' ) "
                + " and (bp.ACCOUNTDETAILKEYID in(SELECT PC_ID FROM EGW_WRKPROG_PROJCODE_SPILLOVER WHERE UUID like '"
                + uuid + "')) " + "group by dept.dept_name " + " )group by \"Dept\"";

        logger.debug("Payment query inside getWorkProgSpillOverTotalPayments :" + payQuery);
        objForExpense = persistenceService.getSession().createSQLQuery(payQuery).list();
        if (objForExpense != null && objForExpense.size() != 0) {
            for (Integer i = 0; i < objForExpense.size(); i++) {
                result = new HashMap<String, Object>();
                result.put("deptName", objForExpense.get(i)[0].toString());
                result.put("amount", objForExpense.get(i)[1].toString());
                result.put("count", objForExpense.get(i)[2].toString());
                result.put("concAmount", objForExpense.get(i)[3].toString());
                result.put("concCount", objForExpense.get(i)[4].toString());

                resultList.add(result);
            }
            return resultList;
        } else
            return null;
    }

    /**
     * NOTE --- THIS API IS USED ONLY FOR WORK PROGRESS ABSTRACT REPORT BY DEPARTMENT IN WORKS MODULE
     *
     * @description -This method returns the total approved voucher count for spill over created before from date for the Project
     * code Ids present in the temporary table WorkProgProjCodeSpillOver for a particular uuid
     * @param uuid - Only project codes ids associated with this uuid are considered
     * @return - List of Maps of depart name, total approved voucher count
     * @return - Null is returned in the case of no data
     * @throws ApplicationException - If anyone of the parameters is null or the ProjectCode list passed is empty.
     */
    public List<Map<String, Object>> getVoucherCountsForSpillOver(final String uuid, final Date fromDate,
            final Date toDate) throws ApplicationException {
        List<Object[]> queryResult;
        final List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> resultMap = null;

        final String countQry = " select dept.dept_name, count(distinct(vh.id)), nvl(sum(bp.debitamount),0) FROM "
                + "eg_billregister br,eg_billdetails bd, eg_billpayeedetails bp,voucherheader vh,eg_billregistermis ms, eg_department dept "
                + "WHERE br.id=bd.billid and bd.id=bp.BILLDETAILID and vh.id=ms.VOUCHERHEADERID and ms.BILLID=br.id  and bp.pc_department=dept.id_dept and vh.name='Contractor Journal'  "
                + "and br.STATUSID in(select id from egw_status where lower(code)='approved' and "
                + "moduletype in('CONTRACTORBILL')) " + "and bd.DEBITAMOUNT>0  and vh.STATUS="
                + FinancialConstants.CREATEDVOUCHERSTATUS + " and br.BILLDATE between '"
                + dateFormatter.format(fromDate) + "' and '" + dateFormatter.format(toDate) + "' "
                + " and bp.ACCOUNTDETAILTYPEID= (SELECT id FROM accountdetailtype "
                + "WHERE name ='PROJECTCODE' AND description='PROJECTCODE' ) "
                + " and bp.ACCOUNTDETAILKEYID in(SELECT PC_ID FROM EGW_WRKPROG_PROJCODE_SPILLOVER WHERE UUID like '"
                + uuid + "')  " + "group by dept.dept_name ";

        queryResult = persistenceService.getSession().createSQLQuery(countQry).list();
        if (queryResult != null && queryResult.size() != 0) {
            for (Integer i = 0; i < queryResult.size(); i++) {
                resultMap = new HashMap<String, Object>();
                resultMap.put("deptName", queryResult.get(i)[0].toString());
                resultMap.put("count", new Integer(queryResult.get(i)[1].toString()));
                resultMap.put("amount", queryResult.get(i)[2].toString());
                resultList.add(resultMap);
            }
            return resultList;
        } else
            return null;
    }

    public Fund getCapitalFund() {
        Fund fund = null;
        fund = (Fund) persistenceService.find("from Fund where name like '%" + WorksConstants.CAPITAL_FUND + "'");

        return fund;
    }

    public List<Map<String, Object>> getBudgetDetailsForFinYear(final String uuid, final Date fromDate,
            final Date toDate) {
        List<Object[]> queryResult;
        final List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> resultMap = null;

        final String query = "select dp.DEPT_NAME, sum(bp.DEBITAMOUNT), "
                + " sum(budd.APPROVEDAMOUNT)+ (decode(sum(bapp.addition_amount-bapp.deduction_amount),null,0,sum(bapp.addition_amount-bapp.deduction_amount)) ) "
                + " from eg_billregister br, eg_billdetails bd, eg_billpayeedetails bp, eg_billregistermis bm, eg_department dp, "
                + " egf_budgetdetail budd left outer join EGF_BUDGET_REAPPROPRIATION bapp on budd.id=bapp.budgetdetail "
                + " where bp.BILLDETAILID=bd.id and bd.BILLID=br.id and br.BILLDATE between '"
                + dateFormatter.format(fromDate)
                + "' and '"
                + dateFormatter.format(toDate)
                + "' "
                + " and bp.ACCOUNTDETAILTYPEID= (SELECT id FROM accountdetailtype WHERE name ='PROJECTCODE' AND description='PROJECTCODE')  "
                + " and bm.BILLID=br.id and br.STATUSID in (select id from egw_status where lower(code)='approved' and moduletype in('CONTRACTORBILL'))"
                + " and dp.ID_DEPT=bm.DEPARTMENTID "
                + " and budd.EXECUTING_DEPARTMENT=dp.ID_DEPT and bd.FUNCTIONID=budd.FUNCTION and bm.FUNDID=budd.FUND "
                + " and budd.BUDGET in(select id from egf_budget where financialyearid=(select id from financialyear "
                + " where startingDate >='" + dateFormatter.format(fromDate) + "' and endingDate <='"
                + dateFormatter.format(toDate) + "') and isBERE='BE') "
                + " and bp.ACCOUNTDETAILKEYID in(SELECT PC_ID FROM EGW_WRKPROG_PROJCODE_SPILLOVER WHERE UUID like '"
                + uuid + "')  " + "group by dp.DEPT_NAME order by dp.DEPT_NAME";

        queryResult = persistenceService.getSession().createSQLQuery(query).list();
        if (queryResult != null && queryResult.size() != 0) {
            for (Integer i = 0; i < queryResult.size(); i++) {
                resultMap = new HashMap<String, Object>();
                resultMap.put("deptName", queryResult.get(i)[0].toString());
                resultMap.put("BudgetAmount", new BigDecimal(queryResult.get(i)[1].toString()));
                resultMap.put("BudgetAvailable", new BigDecimal(queryResult.get(i)[2].toString()));
                resultList.add(resultMap);
            }
            return resultList;
        } else
            return null;
    }

    public List<Map<String, Object>> getSpillOverWorkValue(final String uuid, final Date fromDate, final Date toDate) {
        List<Object[]> queryResult1;
        List<Object[]> queryResult2;
        final List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> resultMap = null;
        BigDecimal spillOverValue = BigDecimal.ZERO;

        final String query1 = "select deptName, nvl(sum(estimate),0)+nvl(sum(ohvalue),0), count(distinct estId) "
                + " from( select dp.dept_name as deptName, ae.id as estId, (ae.value) as estimate, sum(oh.value) as ohvalue "
                + " from egw_abstractestimate ae LEFT OUTER JOIN egw_overheadvalues oh ON ae.id=oh.abstractestimate_id, "
                + " eg_department dp WHERE ae.status_id = (select id from egw_status where lower(code)='admin_sanctioned' and moduletype in('AbstractEstimate')) "
                + " and ae.approveddate<'"
                + dateFormatter.format(fromDate)
                + "' "
                + " and ae.PARENTID is null "
                + " and dp.id_dept=ae.executingdepartment "
                + " and EXISTS (SELECT EST_ID FROM EGW_WRKPROG_PROJCODE_SPILLOVER WHERE EST_ID=ae.id and UUID like '"
                + uuid
                + "' "
                + " and NOT EXISTS(select pyd.accountdetailkeyid from eg_billregister egbr, eg_billpayeedetails pyd, eg_billdetails ebd "
                + " where egbr.id=ebd.billid and pyd.billdetailid=ebd.id and lower(egbr.billtype)='final bill' "
                + " and egbr.statusid = (select id from egw_status where lower(code)='approved' and moduletype in('CONTRACTORBILL')) "
                + " and egbr.EXPENDITURETYPE='Works' and egbr.BILLDATE<'"
                + dateFormatter.format(fromDate)
                + "' and pyd.accountdetailkeyid=PC_ID "
                + " and pyd.accountdetailtypeid=(SELECT id FROM accountdetailtype WHERE name ='PROJECTCODE' AND description='PROJECTCODE'))"
                + " ) " + " group by dp.dept_name,ae.id,ae.value ) " + " group by deptname order by deptname";

        final String query2 = "select dp.dept_name, nvl(sum(bpd.DEBITAMOUNT),0) FROM egw_abstractestimate ae ,"
                + " egw_financialdetail fd, eg_department dp,eg_billregister br,eg_billdetails bd, eg_billpayeedetails bpd, "
                + " paymentheader ph1,miscbilldetail md,eg_billregistermis bmis,voucherheader pvh "
                + " WHERE ae.status_id = (select id from egw_status where lower(code)='admin_sanctioned' and moduletype in('AbstractEstimate')) "
                + " and ae.approveddate<'"
                + dateFormatter.format(fromDate)
                + "' "
                + " and bd.BILLID=br.id and bd.id=bpd.BILLDETAILID and bpd.ACCOUNTDETAILKEYID=ae.PROJECTCODE_ID and EXISTS "
                + " (SELECT PC_ID FROM EGW_WRKPROG_PROJCODE_SPILLOVER WHERE UUID like '"
                + uuid
                + "' AND PC_ID=bpd.ACCOUNTDETAILKEYID "
                + " and NOT EXISTS(select pyd.accountdetailkeyid from eg_billregister egbr, eg_billpayeedetails pyd, eg_billdetails ebd "
                + " where egbr.id=ebd.billid and pyd.billdetailid=ebd.id and lower(egbr.billtype)='final bill' "
                + " and egbr.statusid = (select id from egw_status where lower(code)='approved' and moduletype in('CONTRACTORBILL')) "
                + " and egbr.EXPENDITURETYPE='Works' and egbr.BILLDATE<'"
                + dateFormatter.format(fromDate)
                + "' and pyd.accountdetailkeyid=PC_ID "
                + " and pyd.accountdetailtypeid=(SELECT id FROM accountdetailtype WHERE name ='PROJECTCODE' AND description='PROJECTCODE'))"
                + ")"
                + " and bpd.ACCOUNTDETAILTYPEID=(SELECT id FROM accountdetailtype WHERE name ='PROJECTCODE' AND description='PROJECTCODE') "
                + " and bpd.DEBITAMOUNT>0 and EXISTS (select id from egw_status where lower(code)='approved' and moduletype in('CONTRACTORBILL','EXPENSEBILL','SBILL','SALBILL') and id=br.STATUSID) "
                + " and fd.ABSTRACTESTIMATE_ID=ae.id and ae.PARENTID is null "
                + " and bpd.pc_department=dp.id_dept "
                + " and bmis.billid=br.id and md.PAYVHID=ph1.voucherheaderid and md.BILLVHID=bmis.voucherheaderid and pvh.id=ph1.voucherheaderid "
                + " and pvh.status=0 "
                + " and pvh.VOUCHERDATE < '"
                + dateFormatter.format(fromDate)
                + "'"
                + " group by dp.dept_name order by dp.dept_name ";

        queryResult1 = persistenceService.getSession().createSQLQuery(query1).list();
        queryResult2 = persistenceService.getSession().createSQLQuery(query2).list();

        if (queryResult1 != null && queryResult1.size() != 0) {
            for (Integer i = 0; i < queryResult1.size(); i++) {
                spillOverValue = BigDecimal.ZERO;
                if (queryResult2 != null && queryResult2.size() != 0) {
                    for (Integer j = 0; j < queryResult2.size(); j++)
                        if (queryResult1.get(i)[0].toString().equals(queryResult2.get(j)[0].toString()))
                            spillOverValue = new BigDecimal(queryResult1.get(i)[1].toString()).subtract(new BigDecimal(
                                    queryResult2.get(j)[1].toString()));
                } else
                    spillOverValue = new BigDecimal(queryResult1.get(i)[1].toString());

                resultMap = new HashMap<String, Object>();
                resultMap.put("deptName", queryResult1.get(i)[0].toString());
                resultMap.put("spillOverEstimateCount", new Integer(queryResult1.get(i)[2].toString()));
                resultMap.put("spillOverWorkValue", spillOverValue);
                resultList.add(resultMap);
            }
            return resultList;
        } else
            return null;
    }

    /**
     * @description -This method returns the total payment amount made as on a particular date for a list of ProjectCode ids that
     * is passed.
     * @param entityList - Object list containing ProjectCode ids.
     * @param asOnDate - The payments are considered from the beginning to asOnDate (excluding asOnDate)
     * @return - Total amount as BigDecimal
     * @throws ApplicationException - If anyone of the parameters is null or the ProjectCode ids list passed is empty.
     */
    public BigDecimal getPaymentInfoforProjectCode(final List<Object> projectCodeIdList, final Date asOnDate)
            throws ApplicationException {
        if (projectCodeIdList == null || projectCodeIdList.size() == 0)
            throw new ApplicationException("ProjectCode Id list is null or empty");
        if (asOnDate == null)
            throw new ApplicationException("asOnDate is null");
        final String strAsOnDate = Constants.DDMMYYYYFORMAT1.format(asOnDate);
        final String strProjectCodeIds = getInSubQuery(projectCodeIdList, " bpd.ACCOUNTDETAILKEYID ", false);
        final String query = " SELECT NVL(SUM(bpd.DEBITAMOUNT),0) " + " FROM eg_billregister br, "
                + "   eg_billdetails bd, " + "   eg_billpayeedetails bpd, " + "   paymentheader ph1, "
                + "   miscbilldetail md, " + "   eg_billregistermis bmis, " + "   voucherheader pvh "
                + " WHERE bd.BILLID =br.id " + " AND bd.id =bpd.BILLDETAILID "
                + strProjectCodeIds
                + " AND bpd.ACCOUNTDETAILKEYID NOT IN "
                + "     (SELECT pyd.accountdetailkeyid "
                + "     FROM eg_billregister egbr, "
                + "       eg_billpayeedetails pyd, "
                + "       eg_billdetails ebd "
                + "     WHERE egbr.id           =ebd.billid "
                + "     AND pyd.billdetailid    =ebd.id "
                + "     AND lower(egbr.billtype)='final bill' "
                + "     AND egbr.statusid      IN "
                + "       (SELECT id "
                + "       FROM egw_status "
                + "       WHERE lower(code)='approved' "
                + "       AND moduletype  IN('CONTRACTORBILL') "
                + "       ) "
                + "     AND egbr.EXPENDITURETYPE   ='Works' "
                + "     AND egbr.BILLDATE          <'"
                + strAsOnDate
                + "' "
                + "     AND pyd.accountdetailkeyid =bpd.ACCOUNTDETAILKEYID "
                + "     AND pyd.accountdetailtypeid= "
                + "       (SELECT id "
                + "       FROM accountdetailtype "
                + "       WHERE name     ='PROJECTCODE' "
                + "       AND description='PROJECTCODE' "
                + "       ) "
                + "     ) "
                + " AND bpd.ACCOUNTDETAILTYPEID= "
                + "   (SELECT id "
                + "   FROM accountdetailtype "
                + "   WHERE name     ='PROJECTCODE' "
                + "   AND description='PROJECTCODE' "
                + "   ) "
                + " AND bpd.DEBITAMOUNT>0 "
                + " AND br.STATUSID   IN "
                + "   (SELECT id "
                + "   FROM egw_status "
                + "   WHERE lower(code)='approved' "
                + "   AND moduletype  IN('CONTRACTORBILL','EXPENSEBILL','SBILL','SALBILL') "
                + "   ) "
                + " AND bmis.billid           =br.id "
                + " AND md.PAYVHID            =ph1.voucherheaderid "
                + " AND md.BILLVHID           =bmis.voucherheaderid "
                + " AND pvh.id                =ph1.voucherheaderid "
                + " AND pvh.status            =0 "
                + " AND pvh.VOUCHERDATE       < '" + strAsOnDate + "' ";
        final List<BigDecimal> paymtAmtArray = persistenceService.getSession().createSQLQuery(query).list();
        return paymtAmtArray == null ? null : paymtAmtArray.get(0);
    }

    /**
     * Similar to getPaymentInfoforProjectCode() but difference is the sub query used to generate the list of project code ids is
     * passed as parameter instead
     *
     * @param projectCodeIdStr
     * @param asOnDate
     * @return
     * @throws ApplicationException
     */
    public BigDecimal getPaymentInfoforProjectCodeSubQuery(final String projectCodeIdStr, final Date asOnDate)
            throws ApplicationException {
        if (projectCodeIdStr == null)
            throw new ApplicationException("ProjectCode Id Str is null ");
        if (asOnDate == null)
            throw new ApplicationException("asOnDate is null");
        final String strAsOnDate = Constants.DDMMYYYYFORMAT1.format(asOnDate);
        final String query = " SELECT NVL(SUM(bpd.DEBITAMOUNT),0) " + " FROM eg_billregister br, "
                + "   eg_billdetails bd, " + "   eg_billpayeedetails bpd, " + "   paymentheader ph1, "
                + "   miscbilldetail md, " + "   eg_billregistermis bmis, " + "   voucherheader pvh "
                + " WHERE bd.BILLID =br.id " + " AND bd.id =bpd.BILLDETAILID " + " AND bpd.ACCOUNTDETAILKEYID  IN ("
                + projectCodeIdStr
                + ")"
                + " AND bpd.ACCOUNTDETAILKEYID NOT IN "
                + "     (SELECT pyd.accountdetailkeyid "
                + "     FROM eg_billregister egbr, "
                + "       eg_billpayeedetails pyd, "
                + "       eg_billdetails ebd "
                + "     WHERE egbr.id           =ebd.billid "
                + "     AND pyd.billdetailid    =ebd.id "
                + "     AND lower(egbr.billtype)='final bill' "
                + "     AND egbr.statusid      IN "
                + "       (SELECT id "
                + "       FROM egw_status "
                + "       WHERE lower(code)='approved' "
                + "       AND moduletype  IN('CONTRACTORBILL') "
                + "       ) "
                + "     AND egbr.EXPENDITURETYPE   ='Works' "
                + "     AND egbr.BILLDATE          <'"
                + strAsOnDate
                + "' "
                + "     AND pyd.accountdetailkeyid =bpd.ACCOUNTDETAILKEYID "
                + "     AND pyd.accountdetailtypeid= "
                + "       (SELECT id "
                + "       FROM accountdetailtype "
                + "       WHERE name     ='PROJECTCODE' "
                + "       AND description='PROJECTCODE' "
                + "       ) "
                + "     ) "
                + " AND bpd.ACCOUNTDETAILTYPEID= "
                + "   (SELECT id "
                + "   FROM accountdetailtype "
                + "   WHERE name     ='PROJECTCODE' "
                + "   AND description='PROJECTCODE' "
                + "   ) "
                + " AND bpd.DEBITAMOUNT>0 "
                + " AND br.STATUSID   IN "
                + "   (SELECT id "
                + "   FROM egw_status "
                + "   WHERE lower(code)='approved' "
                + "   AND moduletype  IN('CONTRACTORBILL','EXPENSEBILL','SBILL','SALBILL') "
                + "   ) "
                + " AND bmis.billid           =br.id "
                + " AND md.PAYVHID            =ph1.voucherheaderid "
                + " AND md.BILLVHID           =bmis.voucherheaderid "
                + " AND pvh.id                =ph1.voucherheaderid "
                + " AND pvh.status            =0 "
                + " AND pvh.VOUCHERDATE       < '" + strAsOnDate + "' ";
        final List<BigDecimal> paymtAmtArray = persistenceService.getSession().createSQLQuery(query).list();
        return paymtAmtArray == null ? null : paymtAmtArray.get(0);
    }

    /*
     * This will split up the list passed into sublists of 1000 The returned string will be in the format and ( param in(1...1000)
     * or param in (1001...2000)) etc The purpose of this is for the in clause in queries
     */
    public String getInSubQuery(final List<Object> idList, final String param, final boolean isApostropheRequired) {
        final StringBuffer inClause = new StringBuffer("");
        final String apostropheOrNot = isApostropheRequired ? "'" : "";
        if (idList != null && idList.size() > 0 && param != null) {
            final int size = idList.size();
            inClause.append(" and (" + param + " in ( ");
            for (int i = 0; i < size; i++) {
                if (i % 1000 == 0 && i != 0)
                    inClause.append(") or " + param + " in (").append(
                            apostropheOrNot + idList.get(i).toString() + apostropheOrNot);
                else
                    inClause.append(apostropheOrNot + idList.get(i).toString() + apostropheOrNot);
                if (i == size - 1)
                    inClause.append(")) ");
                else if (i % 1000 != 999)
                    inClause.append(",");
            }
        }
        return inClause.toString();
    }

    public List<Department> getAllDeptmentsForLoggedInUser() {
        // load the primary and secondary assignment departments of the logged in user
        final List<Assignment> assignmentsList = assignmentService
                .getAllActiveEmployeeAssignmentsByEmpId(getCurrentLoggedInUserId());
        employeeService.getEmployeeById(getCurrentLoggedInUserId());
        final List<Department> departmentList = new ArrayList<Department>();
        if (assignmentsList != null)
            for (final Assignment assignment : assignmentsList)
                if (assignment.getPrimary())
                    departmentList.add(0, assignment.getDepartment());
                else
                    departmentList.add(assignment.getDepartment());
        return departmentList;
    }

    public Department getDepartmentByName(final String deptName) {
        Department dept = null;
        dept = (Department) persistenceService.find("from Department where name=?", deptName);

        return dept;
    }

    public Map<String, String> getBoundaryDepartment() {
        final List<AppConfigValues> appConfigList = getAppConfigValue(WorksConstants.WORKS_MODULE_NAME,
                "REGION_BOUNDARY_DEPARTMENT_NAME");
        final Map<String, String> resultMap = new HashMap<String, String>();
        for (final AppConfigValues configValue : appConfigList) {
            final String value[] = configValue.getValue().split(",");
            resultMap.put(value[0], value[1]);
        }
        return resultMap;
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    /**
     * @return all financial years after 2010-11
     */
    public List<CFinancialYear> getAllFinancialYearsForWorks() {
        final Query query = persistenceService.getSession().createQuery(
                "from CFinancialYear where trunc(startingDate)>='01-Apr-2010' order by id desc");
        return query.list();
    }

    /**
     * NOTE --- THIS API IS USED ONLY FOR Works Report 2 dashboard BY DEPARTMENT IN WORKS MODULE
     *
     * @description -This method returns the approved CJV count and sum of CJVs amount for the approved CJVs made till date for a
     * list of Spill over Project codes for which there is a final bill created for it in current year. Project code Ids present
     * in the temporary table WorkProgProjCodeSpillOver for a particular uuid are only considered NOTE --- ASSUMPTION IS THERE
     * WILL BE ONLY 1 FINAL BILL FOR AN ESTIMATE
     * @param uuid - Only spill over project codes ids associated with this uuid are considered
     * @return - List of Map of Maps. The outer map's key is the department name Inner map's keys "amount" and "count" represent
     * sum of CJVs amount and approved Final CJV count
     * @return - Null is returned in the case of no data
     * @throws ApplicationException - If anyone of the parameters is null or the ProjectCode list passed is empty.
     */
    public List<Map<String, Map<String, BigDecimal>>> getCJVCountAndAmountsForSpillOver(final String uuid,
            final Date fromDate, final Date toDate) throws ApplicationException {

        Map<String, Map<String, BigDecimal>> resultMap = null;
        Map<String, BigDecimal> simpleMap = null;
        List<Object[]> payQueryResult;
        List<Object[]> countQueryResult;
        final List<Map<String, Map<String, BigDecimal>>> resultList = new ArrayList<Map<String, Map<String, BigDecimal>>>();

        final String payQuery = " select dept.dept_name , nvl(sum(bp.debitamount),0)  FROM "
                + " eg_billregister br,eg_billdetails bd, eg_billpayeedetails bp,voucherheader vh,eg_billregistermis ms , eg_department dept "
                + " WHERE br.id=bd.billid and bd.id=bp.BILLDETAILID and vh.id=ms.VOUCHERHEADERID and ms.BILLID=br.id and bp.pc_department=dept.id_dept  and vh.name='Contractor Journal'  "
                + " and br.STATUSID in(select id from egw_status where lower(code)='approved' and "
                + " moduletype in('CONTRACTORBILL')) "
                + " and br.billdate between '"
                + dateFormatter.format(fromDate)
                + "' and '"
                + dateFormatter.format(toDate)
                + "'"
                + " and bd.DEBITAMOUNT>0  and vh.STATUS="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " and bp.ACCOUNTDETAILTYPEID= (SELECT id FROM accountdetailtype "
                + " WHERE name ='PROJECTCODE' AND description='PROJECTCODE' ) and (bp.ACCOUNTDETAILKEYID in(SELECT PC_ID FROM EGW_WRKPROG_PROJCODE_SPILLOVER WHERE UUID like '"
                + uuid
                + "'))"
                + " and bp.accountdetailkeyid in ( select bp1.ACCOUNTDETAILKEYID FROM eg_billregister br1, "
                + " eg_billdetails bd1, eg_billpayeedetails bp1 ,voucherheader vh1,eg_billregistermis ms1  WHERE br1.id =bd1.billid AND bd1.id =bp1.BILLDETAILID  "
                + "  and vh1.id=ms1.VOUCHERHEADERID and ms1.BILLID=br1.id and vh1.name='Contractor Journal' and vh1.STATUS="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " AND br1.STATUSID IN (SELECT id FROM egw_status WHERE lower(code)='approved' AND moduletype  IN('CONTRACTORBILL')) "
                + " and br1.billtype in ('FinalBill', 'Final Bill') and bp1.ACCOUNTDETAILKEYID = bp.ACCOUNTDETAILKEYID "
                + " and bp1.ACCOUNTDETAILTYPEID=bp.ACCOUNTDETAILTYPEID ) group by dept.dept_name  order by dept.dept_name  ";

        final String countQuery = " select dept.dept_name , count(vh.id) FROM "
                + " eg_billregister br,eg_billdetails bd, eg_billpayeedetails bp,voucherheader vh,eg_billregistermis ms, eg_department dept  "
                + " WHERE br.id=bd.billid and bd.id=bp.BILLDETAILID and vh.id=ms.VOUCHERHEADERID and ms.BILLID=br.id and bp.pc_department=dept.id_dept  and vh.name='Contractor Journal' "
                + " and br.billtype in ('FinalBill', 'Final Bill') "
                + " and br.STATUSID in(select id from egw_status where lower(code)='approved' and "
                + " moduletype in('CONTRACTORBILL')) "
                + " and br.billdate between '"
                + dateFormatter.format(fromDate)
                + "' and '"
                + dateFormatter.format(toDate)
                + "'"
                + " and bd.DEBITAMOUNT>0  and vh.STATUS="
                + FinancialConstants.CREATEDVOUCHERSTATUS
                + " and bp.ACCOUNTDETAILTYPEID= (SELECT id FROM accountdetailtype "
                + " WHERE name ='PROJECTCODE' AND description='PROJECTCODE' ) and (bp.ACCOUNTDETAILKEYID in(SELECT PC_ID FROM EGW_WRKPROG_PROJCODE_SPILLOVER WHERE UUID like '"
                + uuid + "'))  group by dept.dept_name order by dept.dept_name ";

        payQueryResult = persistenceService.getSession().createSQLQuery(payQuery).list();
        countQueryResult = persistenceService.getSession().createSQLQuery(countQuery).list();
        final List<String> deptNameList = new ArrayList<String>();
        if (payQueryResult != null && payQueryResult.size() > 0)
            for (Integer i = 0; i < payQueryResult.size(); i++)
                deptNameList.add(payQueryResult.get(i)[0].toString());
        if (countQueryResult != null && countQueryResult.size() > 0)
            for (Integer i = 0; i < countQueryResult.size(); i++)
                deptNameList.add(countQueryResult.get(i)[0].toString());
        if (deptNameList == null || !(deptNameList.size() > 0))
            return null;
        // To remove duplicates
        final HashSet<String> tempSet = new HashSet<String>();
        tempSet.addAll(deptNameList);
        deptNameList.clear();
        deptNameList.addAll(tempSet);
        final BigDecimal[] payArray = new BigDecimal[deptNameList.size()];
        final BigDecimal[] countArray = new BigDecimal[deptNameList.size()];
        for (Integer i = 0; i < deptNameList.size(); i++) {
            payArray[i] = BigDecimal.ZERO;
            countArray[i] = BigDecimal.ZERO;
        }
        String deptName = null;
        Integer index = null;
        Integer i = null;
        for (i = 0; i < payQueryResult.size(); i++) {
            deptName = payQueryResult.get(i)[0].toString();
            index = deptNameList.indexOf(deptName);
            payArray[index] = new BigDecimal(payQueryResult.get(i)[1].toString());
        }
        for (i = 0; i < countQueryResult.size(); i++) {
            deptName = countQueryResult.get(i)[0].toString();
            index = deptNameList.indexOf(deptName);
            countArray[index] = new BigDecimal(countQueryResult.get(i)[1].toString());
        }
        for (i = 0; i < deptNameList.size(); i++) {
            deptName = deptNameList.get(i);
            simpleMap = new HashMap<String, BigDecimal>();
            simpleMap.put("amount", payArray[i]);
            simpleMap.put("count", countArray[i]);
            resultMap = new HashMap<String, Map<String, BigDecimal>>();
            resultMap.put(deptName, simpleMap);
            resultList.add(resultMap);
        }
        return resultList;
    }

    public String toCurrency(final double money) {
        final double rounded = Math.round(money * 100) / 100.0;
        final DecimalFormat formatter = new DecimalFormat("0.00");
        formatter.setDecimalSeparatorAlwaysShown(true);
        return formatter.format(rounded);
    }

    @SuppressWarnings("unchecked")
    public Collection<String> getStatusNameDetails(final String[] statusNames) {
        return CollectionUtils.select(Arrays.asList(statusNames), statusName -> (String) statusName != null);
    }

    @SuppressWarnings("unchecked")
    public Collection<Date> getStatusDateDetails(final Date[] statusDates) {
        return CollectionUtils.select(Arrays.asList(statusDates), statusDate -> (Date) statusDate != null);
    }

}