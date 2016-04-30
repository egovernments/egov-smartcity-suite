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
package org.egov.infstr.services;

import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * For all EIS dependent Service
 **/
public interface EISServeable {

    public static enum DATE_ORDER {
        PRIOR, AFTER
    };

    /**
     * Use getPositionsForEmployee API in position master service
     * 
     * @param user
     *            id
     * @param forDate
     * @return List of Position
     **/
    @Deprecated
    List<Position> getPositionsForUser(Long userId, Date forDate);

    /**
     * Used get User for a Position Id for a particular Date.
     * 
     * @param position
     *            Id
     * @param forDate
     * @return User
     **/
    User getUserForPosition(Long positionId, Date forDate);

    /**
     * Used get Primary Position for a given User Id for a particular Date.
     * 
     * @param position
     *            Id
     * @param forDate
     * @return Position
     **/
    @Deprecated
    Position getPrimaryPositionForUser(Long userId, Date forDate);

    /**
     * This API is to get a list of employeeView from HashMap values that are
     * passed.
     * 
     * @param paramMap
     *            <p>
     *            HashMap<String, String> paramMap will have the data required
     *            for the search criteria for employeeView list
     *            <p>
     *            departmentId -This will be the id of the department in String
     *            <p>
     *            designationId -This will be the id of the designation in
     *            String
     *            <p>
     *            functionaryId -This will be the id of the functionary in
     *            String
     *            <p>
     *            code -This will be the code of the employee in String
     *            <p>
     *            name -This will be the name of the employee in String
     *            <p>
     *            status -This will be the id of employee status in String
     *            <p>
     *            empType -This will be the id of employee type in String
     *            <p>
     *            searchAll -This will be the "Y" if you want to get all
     *            employees / "N" if you don't want
     * @return list of employeeView *
     */
    public List<? extends Object> getEmployeeInfoList(HashMap<String, String> paramMap);

    /**
     * This API is to get a list of employeeView from HashMap values that are
     * passed.
     * 
     * @param paramMap
     *            <p>
     *            HashMap<String, String> paramMap will have the data required
     *            for the search criteria for position list
     *            <p>
     *            departmentId -This will be the id of the department in String
     *            <p>
     *            designationId -This will be the id of the designation in
     *            String
     *            <p>
     *            functionaryId -This will be the id of the functionary in
     *            String
     *            <p>
     *            functionId -This will be the id of the function in String
     *            <p>
     *            fundId -This will be the id of the fund in String
     *            <p>
     *            boundaryId -This will be the id of the boundary in String
     *            <p>
     *            date
     * @return list of unique position
     */
    public List<Position> getUniquePositionList(HashMap<String, String> paramMap, Date date);

    /**
     * Use the API getAllDesignationsByDepartment in DesignationService <br>
     * 
     */
    @Deprecated
    public List<Designation> getAllDesignationByDept(Integer departmentId, Date givenDate);

    /**
     * Get all users for the given department and designation id's for the given
     * date
     *
     * @param deptId
     *            the Department Id
     * @param desigId
     *            the Designation Id
     * @param date
     *            the java.util.Date
     * @return List of Users
     */
    public List<User> getUsersByDeptAndDesig(Integer deptId, Integer desigId, Date date);

    /**
     * Based on the ISFILTERBYDEPT flag api returns departmentlist
     * 
     * @return List of Department
     */
    public List<Department> getDeptsForUser();
}
