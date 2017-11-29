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
package org.egov.lcms.web.controller.ajax;

import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.lcms.masters.entity.AdvocateMaster;
import org.egov.lcms.masters.entity.CourtMaster;
import org.egov.lcms.masters.entity.PetitionTypeMaster;
import org.egov.lcms.masters.service.AdvocateMasterService;
import org.egov.lcms.masters.service.CourtMasterService;
import org.egov.lcms.masters.service.CourtTypeMasterService;
import org.egov.lcms.masters.service.PetitionTypeMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/legalcase/")
public class AjaxLegalCaseController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private AdvocateMasterService advocateMasterService;

    @Autowired
    private PetitionTypeMasterService petitiontypeMasterService;

    @Autowired
    private CourtTypeMasterService courtTypeMasterService;

    @Autowired
    private CourtMasterService courtMasterService;

    @RequestMapping(value = "ajax/departments", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Department> getAllDepartmentsByNameLike(
            @ModelAttribute("legalcase") @RequestParam final String departmentName) {
        return departmentService.getAllDepartmentsByNameLike(departmentName);

    }

    @RequestMapping(value = "ajax/advocateSearch", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AdvocateMaster> getAllAdvocatesByNameLike(
            @ModelAttribute("legalcase") @RequestParam final String advocateName,
            @RequestParam final Boolean isSeniorAdvocate) {

        return advocateMasterService.getAllAdvocatesByNameLikeAndIsSeniorAdvocate(advocateName.toUpperCase(),
                isSeniorAdvocate);
    }

    /*
     * @RequestMapping(value = "ajax/positions", method = GET, produces =
     * MediaType.APPLICATION_JSON_VALUE) public @ResponseBody Map<Long, String>
     * getAllPositionsByDeptAndNameLike(
     * @ModelAttribute("legalcase") @RequestParam final String departmentName,
     * @RequestParam final String positionName) { final Map<Long, String>
     * positionEmployeeMap = new HashMap<Long, String>(); String posEmpName;
     * final Department deptObj =
     * departmentService.getDepartmentByName(departmentName); final
     * List<Assignment> assignList = assignmentService
     * .getAllPositionsByDepartmentAndPositionNameForGivenRange(deptObj.getId(),
     * positionName.toUpperCase()); if (!assignList.isEmpty()) for (final
     * Assignment assign : assignList) { posEmpName =
     * assign.getPosition().getName().concat("@").concat(assign.getEmployee().
     * getUsername()); positionEmployeeMap.put(assign.getEmployee().getId(),
     * posEmpName); } return positionEmployeeMap; }
     */

    @RequestMapping(value = "/ajax-petitionTypeByCourtType", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<PetitionTypeMaster> getAllPetitionTypesByCountType(@RequestParam final Long courtType) {
        List<PetitionTypeMaster> petitionTypeList = new ArrayList<PetitionTypeMaster>(0);
        if (courtType != null) {
            petitionTypeList = petitiontypeMasterService
                    .findActivePetitionByCourtType(courtTypeMasterService.findOne(courtType));
            petitionTypeList.forEach(petitionType -> petitionType.toString());
        }
        return petitionTypeList;
    }

    @RequestMapping(value = "/ajax-courtNameByCourtType", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<CourtMaster> getAllCourtNamesByCountType(@RequestParam final Long courtType) {
        List<CourtMaster> courtNameList = new ArrayList<CourtMaster>(0);
        if (courtType != null) {
            courtNameList = courtMasterService.findActiveCourtByCourtType(courtTypeMasterService.findOne(courtType));
            courtNameList.forEach(petitionType -> petitionType.toString());
        }
        return courtNameList;
    }

}
