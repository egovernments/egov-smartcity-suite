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
package org.egov.works.services;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.AccountdetailkeyHibernateDAO;
import org.egov.commons.dao.AccountdetailtypeHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EmployeeService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.DeptDesig;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;

public class WorksService {
    private static final Logger logger = Logger.getLogger(WorksService.class);
    @Autowired
    private AppConfigValueService appConfigValuesService;

    private PersistenceService persistenceService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private AccountdetailtypeHibernateDAO accountdetailtypeHibernateDAO;
    @Autowired
    private AccountdetailkeyHibernateDAO accountdetailkeyHibernateDAO;
    @Autowired
    private FinancialYearHibernateDAO financialYearHibernateDAO;

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

    public String getWorksConfigValue(final String key) {
        final List<AppConfigValues> configList = getAppConfigValue(WorksConstants.WORKS_MODULE_NAME, key);
        if (!configList.isEmpty())
            return configList.get(0).getValue();
        return null;
    }

    public Accountdetailtype getAccountdetailtypeByName(final String name) {
        return accountdetailtypeHibernateDAO.getAccountdetailtypeByName(name);
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
        return persistenceService.getSession().load(User.class, ApplicationThreadLocals.getUserId());
    }

    public Map<String, Integer> getExceptionSOR() {
        final List<AppConfigValues> appConfigList = getAppConfigValue(WorksConstants.WORKS_MODULE_NAME, "EXCEPTIONALUOMS");
        final Map<String, Integer> resultMap = new HashMap<String, Integer>();
        for (final AppConfigValues configValue : appConfigList) {
            final String value[] = configValue.getValue().split(",");
            resultMap.put(value[0], Integer.valueOf(value[1]));
        }
        return resultMap;
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

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
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

    public Assignment getLatestAssignmentForCurrentLoginUser() {
        final Long currentLoginUserId = getCurrentLoggedInUserId();
        Assignment assignment = null;
        if (currentLoginUserId != null)
            assignment = assignmentService.getPrimaryAssignmentForEmployee(currentLoginUserId);
        return assignment;
    }

    public CFinancialYear getFinancialYearByDate(final Date asOnDate) {
        return financialYearHibernateDAO.getFinYearByDate(asOnDate);
    }

}