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
package org.egov.pims.commons.service;

import org.egov.infra.admin.master.entity.User;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;

import java.util.Date;
import java.util.List;



/**
 * An interface for all the common entities, which are likely to be used in more than one applications.
 * @author Venkatesh.M.J,Divya
 * @version 1.2
 * @since 1.2, Oct 25, 2005
*/
public interface EisCommonsService 
{  
   public abstract void updatePosition(Position position);
   public abstract Position getPositionById(Integer positionId);
   public abstract Position getPositionByUserId(Long userId);
   public abstract User getUserforPosition(Position pos);
   public abstract Position getPositionForUserByIdAndDate(Integer userId, Date assignDate);
   
 //Api for Auto Generate Employee code
	public abstract Boolean isEmployeeAutoGenerateCodeYesOrNo();
	//Api check's for employee code unique
	public abstract Boolean checkEmpCode(String empCode);
	//to get position by passing position name
	public abstract Position getPositionByName(String positionName);
	/**
	 * This method returns the current position id  of the user
	 * 
	 * @param user the user whose designation is needed.
	 * 
	 * 
	 * @return the position id as integer 
	 */
	public abstract Position getCurrentPositionByUser(User user);
	
	public abstract  User getUserForPosition(Integer posId, Date date);
	public abstract List<Designation> getDesigantionBasedOnFuncDept(Integer deptId,Integer functionaryId) throws Exception;
	/**
	  * Returning temporary  assigned employee object by pepartment,designation,functionary,date 
	  * @param deptId
	  * @param DesigId
	  * @param functionaryId
	  * @param onDate
	  * @return Employee
	  * @throws Exception 
	  */
	 public PersonalInformation getTempAssignedEmployeeByDeptDesigFunctionaryDate(Integer deptId, Integer desigId, Integer functionaryId, Date onDate) throws Exception;

}
