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
package org.egov.pims.service;

import org.apache.log4j.Logger;
import org.egov.commons.exception.NoSuchObjectException;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.EmployeeView;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.workflow.service.OwnerGroupService;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("eisService")
@Transactional(readOnly = true)
public class EisUtilService implements OwnerGroupService<Position> {
    private static final Logger LOGGER = Logger.getLogger(EisUtilService.class);

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private PersonalInformationDAO personalInformationDAO;


    public List<Position> getOwnerGroupsByUserId(Long userId) {
        return this.getPositionsForUser(userId, new Date());
    }

    public List<Position> getPositionsForUser(Long user, Date date) {

        List<Position> positionList;
        try {
            String mainStr = "select distinct(a.position) from Assignment a where a.employee.id =?";
            Date givenDate = date == null ? new Date() : date;

            mainStr += " and ((a.toDate is null and a.fromDate<= ?) or (a.fromDate <= ? and a.toDate >= ?))";
            positionList = (List) persistenceService.findAllBy(mainStr, user, givenDate, givenDate, givenDate);

        } catch (Exception e) {
            LOGGER.error("Exception while getting the getPositionsForUser=" + e.getMessage());
            throw new ApplicationRuntimeException(e.getMessage(), e);

        }
        return positionList;

    }

    public Position getPrimaryPositionForUser(Long userId, Date date) {

        Position position;

        try {
            String mainStr = "select a.position from Assignment a where a.primary=true";

            if (userId != null && userId != 0) {
                mainStr += " and a.oldEmployee.userMaster.id =?";

            }

            Date givenDate = date == null ? new Date() : date;

            mainStr += " and ((a.toDate is null and a.fromDate<= ?) or (a.fromDate <= ? and a.toDate >= ?))";
            position = (Position) persistenceService.find(mainStr, userId, givenDate, givenDate, givenDate);

        } catch (Exception e) {
            LOGGER.error("Exception while getting the getPrimaryPositionForUser=" + e.getMessage());
            throw new ApplicationRuntimeException(e.getMessage(), e);

        }
        return position;

    }

    public User getUserForPosition(Long positionId, Date date) {
        User user;
        try {
            String mainStr = "select emp.userMaster from EmployeeView emp where emp.position.id = ?";

            Date givenDate = date == null ? new Date() : date;

            mainStr += " and ((emp.toDate is null and emp.fromDate<= ?) or (emp.fromDate <= ? and emp.toDate >= ?))";
            user = (User) persistenceService.find(mainStr, positionId, givenDate, givenDate, givenDate);
        } catch (Exception e) {
            LOGGER.error("Exception while getting the getUserForPosition=" + e.getMessage());
            throw new ApplicationRuntimeException(e.getMessage(), e);

        }
        return user;

    }

    public List<EmployeeView> getEmployeeInfoList(Map paramMap) {
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
                : new ArrayList<>();
        List<User> userList = new ArrayList<>();
        List<EmployeeView> empInfoList;
        if (boundaryId != null && boundaryId != 0) {
            Boundary boundary = boundaryService.getBoundaryById(boundaryId);
            // Exclude get users list if boundary is city level
            if (boundary.getParent() != null)
                try {
                    userList = getListOfUsersForGivenBoundaryId(boundaryId);
                } catch (NoSuchObjectException e) {

                    LOGGER.error(e);
                }

        }

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

            if (boundaryId != null && boundaryId.intValue() != 0 && !userList.isEmpty()) {
                mainStr += " ev.userMaster in(:userObjList) and ";
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
            if (boundaryId != null && boundaryId.intValue() != 0 && !userList.isEmpty()) {
                qry.setParameterList("userObjList", userList);
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

    public List getListOfUsersForGivenBoundaryId(Long boundaryId) throws NoSuchObjectException {
        return personalInformationDAO.getListOfUsersForGivenBoundaryId(boundaryId);
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
     * @deprecated
     */
    @Deprecated
    public List<Designation> getAllDesignationByDept(Integer departmentId, Date givenDate) {
        Date userGivenDate = givenDate == null ? new Date() : givenDate;
        Long deptId = departmentId.longValue();
        Criteria criteria = persistenceService
                .getSession()
                .createCriteria(Assignment.class, "assign")
                .createAlias("assign.department", "department")
                .add(Restrictions.eq("department.id", deptId))
                .add(Restrictions.and(Restrictions.le("assign.fromDate", userGivenDate),
                        Restrictions.ge("assign.toDate", userGivenDate)));

        ProjectionList projections = Projections.projectionList().add(Projections.property("assign.designation"));
        criteria.setProjection(projections);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        return (List<Designation>) criteria.list();

    }

    /*
     * Gets all the DO users for the following params
     *      (non-Javadoc)
     * @see org.egov.infstr.services.EISServeable#getListOfDrawingOfficers(java.util.List, java.lang.String, java.lang.String)
     */
    public List<HashMap> getListOfDrawingOfficers(List<Long> desigList, Date assignDate, String codeOrName) {
        ArrayList results = new ArrayList();
        Query query = getQueryForDrawingOfficer(desigList, null, assignDate == null ? new Date() : assignDate, codeOrName);
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
        StringBuilder qry = new StringBuilder().append("select distinct eee.id as empid,eee.name as empname,eee.code as empcode,")
                .append(" do.id as doid,do.name as doname,do.code as docode from eg_eis_employeeinfo eee")
                .append(" inner join eg_position pos on pos.id = eee.pos_id")
                .append(" inner join eg_drawingofficer do on do.id = pos.id_drawing_officer ")
                .append(" where eee.isactive=1 and pos.id_drawing_officer is not null ")
                .append(" and :enteredDate between eee.from_date and eee.to_date ");

        if ((null != desigList && !desigList.isEmpty())) {
            qry.append(" and eee.designationid in (:desList) ");
        }
        if (null != codeOrName && !codeOrName.isEmpty()) {
            qry.append(" and (lower(do.name) like lower(:enteredString) or lower(do.code) like lower(:enteredString) ")
                    .append(" or lower(eee.name) like lower(:enteredString) or lower(eee.code) like lower(:enteredString)) ");
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
