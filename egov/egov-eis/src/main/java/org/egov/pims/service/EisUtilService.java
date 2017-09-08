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
package org.egov.pims.service;

import org.apache.log4j.Logger;
import org.egov.commons.exception.NoSuchObjectException;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.EmployeeView;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.pims.dao.PersonalInformationDAO;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("eisService")
@Transactional(readOnly = true)
public class EisUtilService implements EISServeable {
    private static final Logger LOGGER = Logger.getLogger(EisUtilService.class);
    private final String EMPVIEWDEPTIDSLOGGEDINUSER = "EMPVIEW-DEPTIDS-LOGGEDINUSER";

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private PersonalInformationDAO personalInformationDAO;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    public List<Position> getPositionsForUser(Long user, Date date) {

        List<Position> positionList = new ArrayList<Position>();
        try {
            String mainStr = "";

            mainStr = "select distinct(a.position) from Assignment a where a.employee.id =?";

            if (date == null) {
                date = new Date();
            }

            mainStr += " and ((a.toDate is null and a.fromDate<= ?) or (a.fromDate <= ? and a.toDate >= ?))";
            positionList = (List) persistenceService.findAllBy(mainStr, user, date, date, date);

        } catch (Exception e) {
            LOGGER.error("Exception while getting the getPositionsForUser=" + e.getMessage());
            throw new ApplicationRuntimeException(e.getMessage(), e);

        }
        return positionList;

    }

    public Position getPrimaryPositionForUser(Long userId, Date date) {

        Position position = null;

        try {
            String mainStr = "";

            mainStr = "select a.position from Assignment a where a.primary=true";

            if (userId != null && userId != 0) {
                mainStr += " and a.oldEmployee.userMaster.id =?";

            }

            if (date == null) {
                date = new Date();
            }

            mainStr += " and ((a.toDate is null and a.fromDate<= ?) or (a.fromDate <= ? and a.toDate >= ?))";
            position = (Position) persistenceService.find(mainStr, userId, date, date, date);

        } catch (Exception e) {
            LOGGER.error("Exception while getting the getPrimaryPositionForUser=" + e.getMessage());
            throw new ApplicationRuntimeException(e.getMessage(), e);

        }
        return position;

    }

    public User getUserForPosition(Long positionId, Date date) {
        User user = null;
        try {
            String mainStr = "";

            mainStr = "select emp.userMaster from EmployeeView emp where emp.position.id = ?";

            if (date == null) {
                date = new Date();
            }

            mainStr += " and ((emp.toDate is null and emp.fromDate<= ?) or (emp.fromDate <= ? and emp.toDate >= ?))";
            user = (User) persistenceService.find(mainStr, positionId, date, date, date);
        } catch (Exception e) {
            LOGGER.error("Exception while getting the getUserForPosition=" + e.getMessage());
            throw new ApplicationRuntimeException(e.getMessage(), e);

        }
        return user;

    }

    public List<EmployeeView> getEmployeeInfoList(HashMap paramMap) {
        List<EmployeeView> empInfoList = new ArrayList<EmployeeView>();
        Integer departmentId = paramMap.get("departmentId") != null ? Integer.parseInt((String) paramMap
                .get("departmentId")) : null;
        Integer designationId = paramMap.get("designationId") != null ? Integer.parseInt((String) paramMap
                .get("designationId")) : null;
        Integer functionaryId = paramMap.get("functionaryId") != null ? Integer.parseInt((String) paramMap
                .get("functionaryId")) : null;
        String code = (String) paramMap.get("code");
        String name = (String) paramMap.get("name");
        Integer status = paramMap.get("status") != null ? Integer.parseInt((String) paramMap.get("status")) : null;
        Integer empType = paramMap.get("empType") != null ? Integer.parseInt((String) paramMap.get("empType")) : null;
        String searchAll = (String) paramMap.get("searchAll");
        Long boundaryId = paramMap.get("boundaryId") != null ? Long.valueOf((String) paramMap.get("boundaryId")) : null;
        Long userId = paramMap.get("userId") != null ? Long.valueOf((String) paramMap.get("userId")) : null;
        List<String> roleList = paramMap.get("roleList") != null ? (List<String>) paramMap.get("roleList")
                : new ArrayList<String>();
        List<User> userList = new ArrayList<User>();

        if (boundaryId != null && boundaryId != 0) {
            Boundary boundary = null;

            boundary = boundaryService.getBoundaryById(boundaryId);
            // Exclude get users list if boundary is city level
            if (boundary.getParent() != null)
                try {
                    // userList = getListOfUsersByBoundaryId(boundaryId);
                    userList = getListOfUsersForGivenBoundaryId(boundaryId);
                } catch (NoSuchObjectException e) {

                    LOGGER.error(e);
                }

        }

        // Checking for boundary and role to get the list of users(It's taking
        // 2/3 sec more time than writing qry)
        /*
         * if(boundaryId!=null && boundaryId!=0) { List<User> tempUserList = new
         * ArrayList<User>(); try{ tempUserList =
         * getListOfUsersByBoundaryId(boundaryId); }catch (NoSuchObjectException
         * e){ LOGGER.error(e); } if(!tempUserList.isEmpty()){
         * if(!roleList.isEmpty()){ for(User userObj :
         * getListOfUsersByRole(roleList)){ if(tempUserList.contains(userObj)){
         * userList.add(userObj); } } } } } else if(!roleList.isEmpty()){
         * userList = getListOfUsersByRole(roleList); }
         */

        if ("Y".equalsIgnoreCase(searchAll)) {
            empInfoList = persistenceService.findAllBy("from EmployeeView");
        } else {
            String mainStr = "from EmployeeView ev where";
            if (code != null && !code.equals("")) {
                mainStr += " upper(trim(ev.employeeCode)) = :employeeCode and ";
            }
            if (departmentId != null && departmentId.intValue() != 0) {
                mainStr += " ev.department.id= :deptId and ";
            }
            if (designationId != null && designationId.intValue() != 0) {
                mainStr += " ev.designation.id = :designationId and ";
            }
            if (functionaryId != null && functionaryId.intValue() != 0) {
                mainStr += " ev.functionary.id = :functionaryId and ";
            }

            if (empType != null && empType.intValue() != 0) {
                mainStr += " ev.employeeType.id=:employeeType and";
            }

            // Adding Boundary
            if (boundaryId != null && boundaryId.intValue() != 0) {
                if (!userList.isEmpty()) {
                    mainStr += " ev.userMaster in(:userObjList) and ";
                }
            }
            if (userId != null && userId.intValue() != 0) {
                mainStr += " ev.userMaster.id =:userId and ";
            }
            if (!roleList.isEmpty()) {
                mainStr += "ev.userMaster.id in(select userRole.user.id from UserRole userRole where "
                        + "((userRole.fromDate <= current_date and userRole.toDate >= current_date) or "
                        + "(userRole.fromDate <= current_date and userRole.toDate is null)) and "
                        + "userRole.role.roleName in(:roleList) ) and";
            }
            if (name != null && !name.equals("")) {
                mainStr += " trim(upper(ev.employeeName))  like '%" + name.trim().toUpperCase() + "%' and ";
            }
            if (status != null && status.intValue() != 0) {
                mainStr += " ev.employeeStatus.id = :employeeStatus and ";
            }
            if (status != null && status.intValue() != 0 && designationId != null && designationId.intValue() == 0) {
                mainStr += " ((ev.toDate is null and ev.fromDate <= current_date ) OR (ev.fromDate <= current_date AND ev.toDate > current_date)) and ev.employeeStatus.id = :employeeStatus ";
            } else if (status != null && status.intValue() == 0 && designationId != null
                    && designationId.intValue() != 0) {
                mainStr += " ((ev.toDate is null and ev.fromDate <= current_date ) OR (ev.fromDate <= current_date AND ev.toDate > current_date)) ";
            }
            // Inspite of SearchAll is true or false, if employee code is
            // entered, search for all active and inactive employees
            else if (code != null && !code.equals("")) {
                mainStr += "  ((ev.toDate IS NULL AND ev.fromDate <= current_date) OR (ev.fromDate <= current_date AND ev.toDate > current_date) "
                        + " OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   "
                        + " WHERE evn.id = ev.id AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND "
                        + " ((evn2.toDate  IS NULL AND evn2.fromDate <= current_date) OR (evn2.fromDate <= current_date AND evn2.toDate > current_date)) )))) ";
            } else if ((status != null && status.intValue() != 0)
                    || (designationId != null && designationId.intValue() == 0)) {
                mainStr += "  ((ev.toDate IS NULL AND ev.fromDate <= current_date) OR (ev.fromDate <= current_date AND ev.toDate > current_date) "
                        + " OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   "
                        + " WHERE evn.id = ev.id AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND "
                        + " ((evn2.toDate  IS NULL AND evn2.fromDate <= current_date) OR (evn2.fromDate <= current_date AND evn2.toDate > current_date)) )))) ";
            } else {
                mainStr += " ((ev.toDate is null and ev.fromDate <= current_date ) OR (ev.fromDate <= current_date AND ev.toDate > current_date)) ";
            }
            mainStr += " and ev.userActive='1' "; // getting only active employees
            // for any kind of search
            Query qry = null;
            qry = persistenceService.getSession().createQuery(mainStr);
            LOGGER.info("qryqryqryqry" + qry.toString());
            if (code != null && !code.equals("")) {
                qry.setString("employeeCode", code);
            }
            if (departmentId != null && departmentId.intValue() != 0) {
                qry.setInteger("deptId", departmentId);
            }
            if (designationId != null && designationId.intValue() != 0) {
                qry.setInteger("designationId", designationId);
            }
            if (functionaryId != null && functionaryId.intValue() != 0) {
                qry.setInteger("functionaryId", functionaryId);
            }
            if (status != null && status.intValue() != 0) {
                qry.setInteger("employeeStatus", status);
            }
            if (boundaryId != null && boundaryId.intValue() != 0) {
                if (!userList.isEmpty()) {
                    qry.setParameterList("userObjList", userList);
                }
            }
            if (userId != null && userId.intValue() != 0) {
                qry.setLong("userId", userId);
            }
            if (!roleList.isEmpty()) {
                qry.setParameterList("roleList", roleList);
            }
            if (empType != null && empType.intValue() != 0) {
                qry.setInteger("employeeType", empType.intValue());
            }
            empInfoList = (List) qry.list();
        }
        return empInfoList;
    }

    public List<Position> getUniquePositionList(HashMap<String, String> paramMap, Date date) {
        List<Position> positionList = new ArrayList<Position>();
        Integer departmentId = paramMap.get("departmentId") != null ? Integer.parseInt(paramMap.get("departmentId"))
                : null;
        Integer designationId = paramMap.get("designationId") != null ? Integer.parseInt(paramMap.get("designationId"))
                : null;
        Integer functionaryId = paramMap.get("functionaryId") != null ? Integer.parseInt(paramMap.get("functionaryId"))
                : null;
        Integer functionId = paramMap.get("functionId") != null ? Integer.parseInt(paramMap.get("functionId")) : null;
        Integer fundId = paramMap.get("fundId") != null ? Integer.parseInt(paramMap.get("fundId")) : null;
        if (date == null) {
            date = new Date();
        }

        String qryString = "select distinct(ass.position) from Assignment ass where ";
        if (departmentId != null && departmentId.intValue() != 0) {
            qryString += " ass.deptId.id= :deptId and ";
        }
        if (designationId != null && designationId.intValue() != 0) {
            qryString += " ass.desigId.designationId = :designationId and ";
        }
        if (functionaryId != null && functionaryId.intValue() != 0) {
            qryString += " ass.functionary.id = :functionaryId and ";
        }
        if (functionId != null && functionId.intValue() != 0) {
            qryString += " ass.functionId.id = :functionId and ";
        }
        if (fundId != null && fundId.intValue() != 0) {
            qryString += " ass.fundId.id = :fundId and ";
        }
        qryString += " ass.id in (select asprd.id from Assignment asprd where asprd.fromDate <= :date and asprd.toDate >= :date)";

        Query qry = null;
        LOGGER.info("qryqryqryqryString--------" + qryString);
        qry = persistenceService.getSession().createQuery(qryString);
        LOGGER.info("qryqryqryqry----" + qry.toString());
        if (departmentId != null && departmentId.intValue() != 0) {
            qry.setInteger("deptId", departmentId);
        }
        if (designationId != null && designationId.intValue() != 0) {
            qry.setInteger("designationId", designationId);
        }
        if (functionaryId != null && functionaryId.intValue() != 0) {
            qry.setInteger("functionaryId", functionaryId);
        }
        if (functionId != null && functionId.intValue() != 0) {
            qry.setInteger("functionId", functionId);
        }
        if (fundId != null && fundId.intValue() != 0) {
            qry.setInteger("fundId", fundId);
        }
        qry.setDate("date", date);
        positionList = (List) qry.list();
        return positionList;
    }

    public List getListOfUsersByBoundaryId(Long boundaryId) throws NoSuchObjectException {
        //List listOfUserByBoundary = personalInformationDAO.getListOfUsersByBoundaryId(boundaryId);
        return null;
    }

    public List getListOfUsersForGivenBoundaryId(Long boundaryId) throws NoSuchObjectException {
        List listOfUserByBoundary = personalInformationDAO.getListOfUsersForGivenBoundaryId(boundaryId);
        return listOfUserByBoundary;
    }

    /**
     * Use the API getAllDesignationsByDepartment in DesignationService.java
     * <p>
     * return all distinct Designations to which employees are assigned in the
     * given department for given date. This list includes primary as well as
     * secondary assignments. If there is No Designation for the given
     * department then returns the empty list
     *
     * @param departmentId
     * @param givenDate
     * @return DesignationMaster List
     */
    @Deprecated
    public List<Designation> getAllDesignationByDept(Integer departmentId, Date givenDate) {
        List<Designation> designationMstrObj = new ArrayList<Designation>();
        Date userGivenDate = givenDate == null ? new Date() : givenDate;
        Long deptId = departmentId.longValue();
        Criteria criteria = persistenceService
                .getSession()
                .createCriteria(Assignment.class, "assign")
                .createAlias("assign.department", "department")
                .add(Restrictions.eq("department.id", deptId.longValue()))
                .add(Restrictions.and(Restrictions.le("assign.fromDate", userGivenDate),
                        Restrictions.ge("assign.toDate", userGivenDate)));

        ProjectionList projections = Projections.projectionList().add(Projections.property("assign.designation"));
        criteria.setProjection(projections);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        designationMstrObj = (List<Designation>) criteria.list();

        return designationMstrObj;

    }

    /**
     * Get all users for the given department and designation id's for the given
     * date
     *
     * @param deptId  the Department Id
     * @param desigId the Designation Id
     * @param date    the java.util.Date
     * @return List of Users
     */
    public List<User> getUsersByDeptAndDesig(Integer deptId, Integer desigId, Date date) {
        date = date == null ? new Date() : date;
        Long dept = deptId.longValue();
        Long desig = desigId.longValue();
        Criteria criteria = persistenceService.getSession().createCriteria(EmployeeView.class, "view")
                .add(Restrictions.eq("view.department.id", dept)).add(Restrictions.eq("view.designation.id", desig))
                .add(Restrictions.le("view.fromDate", date))
                .add(Restrictions.or(Restrictions.ge("view.toDate", date), Restrictions.isNull("view.toDate")))
                .add(Restrictions.isNotNull("view.userName"));
        ProjectionList projections = Projections.projectionList().add(Projections.property("view.employee"));
        criteria.setProjection(projections);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return (List<User>) criteria.list();
    }

    /**
     * @param empId
     * @param fromDate
     * @param toDate
     * @return
     */
    public List<Assignment> getPrimartAssignmentForGivenDateRange(Integer empId, Date fromDate, Date toDate) {
        List<Assignment> assignmentObj = new ArrayList<Assignment>();
        Criteria criteria = persistenceService
                .getSession()
                .createCriteria(EmployeeView.class, "emp")
                .add(Restrictions.eq("emp.primary", true))
                .add(Restrictions.eq("emp.id", empId))
                .add(Restrictions.and(Restrictions.le("emp.fromDate", fromDate),
                        Restrictions.ge("emp.toDate", fromDate)));

        if (toDate != null) {
            criteria.add(Restrictions.and(Restrictions.le("emp.fromDate", toDate),
                    Restrictions.ge("emp.toDate", toDate)));
        }

        ProjectionList projections = Projections.projectionList().add(Projections.property("emp.assignment"));
        criteria.setProjection(projections);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        assignmentObj = (List<Assignment>) criteria.list();
        return assignmentObj;
    }

    /**
     * Based on the ISFILTERBYDEPT flag api returns departmentlist if
     * ISFILTERBYDEPT is YES then return Department list for the login user,if
     * the user is hod of depts those depts includes in the list if
     * ISFILTERBYDEPT is NO then returns all the departments
     */
    public List<Department> getDeptsForUser() {
        String filterByDept = appConfigValuesService.getAppConfigValue("EIS-PAYROLL", "FILTERBYDEPT", "NO");
        List<Department> deptlist = null;
        if (filterByDept != null && filterByDept.toUpperCase().equals("YES")) {
            List<BigDecimal> deptList = persistenceService.findPageByNamedQuery("EMPVIEW-DEPTIDS-LOGGEDINUSER", 0,
                    null, ApplicationThreadLocals.getUserId().intValue(), ApplicationThreadLocals.getUserId().intValue())
                    .getList();
            if (deptList.isEmpty())
                return Collections.emptyList();
            else {
                List<Integer> deptListInt = new ArrayList<Integer>();
                for (BigDecimal deptId : deptList) {
                    if (deptId != null) {
                        deptListInt.add(deptId.intValue());
                    }
                }
                deptlist = persistenceService.getSession().createCriteria(Department.class)
                        .add(Restrictions.in("id", deptListInt)).list();
            }
        } else {
            deptlist = persistenceService.getSession().createCriteria(Department.class).list();
        }
        return deptlist;
    }

    public boolean isValidWorkflowUser(Position owner) {
        boolean validuser = false;
        List<Position> positions = getPositionsForUser(Long.valueOf(ApplicationThreadLocals.getUserId()), new Date());
        if (positions.contains(owner)) {
            validuser = true;
        }
        return validuser;

    }

    /*
     * Gets all the DO users for the following params
     *      (non-Javadoc)
     * @see org.egov.infstr.services.EISServeable#getListOfDrawingOfficers(java.util.List, java.lang.String, java.lang.String)
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<HashMap> getListOfDrawingOfficers(List<Long> desigList, Date assignDate, String codeOrName) {
        ArrayList results = new ArrayList();
        if (null == assignDate)
            assignDate = new Date();
        Query query = getQueryForDrawingOfficer(desigList, null, assignDate, codeOrName);
        List<Object[]> tmpList = (List<Object[]>) query.list();
        int i = 0;
        for (Object[] objArray : tmpList) {
            Map temp = new HashMap();
            temp.put("empid", objArray[0]);
            temp.put("empname", objArray[1]);
            temp.put("empcode", objArray[2]);
            temp.put("doid", objArray[3]);
            temp.put("doname", objArray[4]);
            temp.put("docode", objArray[5]);
            results.add(i, temp);
            i++;
        }

        return results;
    }

    private Query getQueryForDrawingOfficer(List<Long> desigList, Integer doId, Date assignDate, String codeOrName) {
        StringBuffer qry = new StringBuffer("select distinct eee.id as empid,eee.name as empname,eee.code as empcode," +
                " do.id as doid,do.name as doname,do.code as docode from eg_eis_employeeinfo eee" +
                " inner join eg_position pos on pos.id = eee.pos_id" +
                " inner join eg_drawingofficer do on do.id = pos.id_drawing_officer " +
                " where eee.isactive=1 and pos.id_drawing_officer is not null " +
                " and :enteredDate between eee.from_date and eee.to_date ");

        if ((null != desigList && !desigList.isEmpty())) {
            qry.append(" and eee.designationid in (:desList) ");
        }
        if (null != codeOrName && !codeOrName.isEmpty()) {
            qry.append(" and (lower(do.name) like lower(:enteredString) or lower(do.code) like lower(:enteredString) " +
                    "        or lower(eee.name) like lower(:enteredString) or lower(eee.code) like lower(:enteredString)) ");
        }
        if (null != doId) {
            qry.append(" and do.id=:doId ");
        }
        qry.append(" order by eee.name ");
        Query query = persistenceService.getSession().createSQLQuery(qry.toString());
        query.setDate("enteredDate", assignDate);
        if (null != desigList && !desigList.isEmpty()) {
            query.setParameterList("desList", desigList);
        }
        if (null != doId) {
            query.setInteger("doId", doId);
        }
        if (null != codeOrName && !codeOrName.isEmpty()) {
            query.setString("enteredString", "%" + codeOrName + "%");
        }

        return query;
    }

}
