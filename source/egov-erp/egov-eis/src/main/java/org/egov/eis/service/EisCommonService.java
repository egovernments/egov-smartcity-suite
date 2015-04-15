/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.eis.service;

import java.util.Date;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vaibhav.K
 *
 */
@Service
@Transactional(readOnly = true)
public class EisCommonService {
    
    @Autowired
    private PositionHierarchyService positionHierarchyService;
    
    @Autowired
    private AssignmentService assignmentService;
    
    @Autowired
    private UserService userService;
    
    public Position getSuperiorPositionByObjectTypeAndPositionFrom(final Integer objectId,final Long posId) {
        return positionHierarchyService.getPositionHierarchyByPosAndObjectType(posId,objectId).getToPosition();
    }
    
    public Position getSuperiorPositionByObjectAndObjectSubTypeAndPositionFrom(final Integer objectId,final String objectSubType,final Long posId) {
        return positionHierarchyService.getPosHirByPosAndObjectTypeAndObjectSubType(posId, objectId, objectSubType).getToPosition();
    }
    
    public User getUserForPosition(final Long posId,final Date givenDate) {
        try{
            return assignmentService.getAssignmentForPosition(posId,givenDate).getEmployee().getUserMaster();
        }
        catch(NullPointerException e) {
            throw new EGOVRuntimeException("User Not Found");
        }
        catch(Exception e) {
            throw new EGOVRuntimeException(e.getMessage());
        }
    }

    public DesignationMaster getEmployeeDesignation(final Long posId) {
        return assignmentService.getPrimaryAssignmentForPositon(posId).getDesigId();
    }
    
    public Department getDepartmentForUser(final Long userId) {
    	return assignmentService.getPrimaryAssignmentForUser(userId).getDeptId();
    }
    
}
