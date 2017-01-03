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

package org.egov.eis.web.controller.masters.position;

import org.apache.commons.io.IOUtils;
import org.egov.eis.service.DeptDesigService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.pims.commons.DeptDesig;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/position")
public class SearchPositionController {

    private final DepartmentService departmentService;
    private final DesignationService designationService;
    private final DeptDesigService deptDesigService;
    private final PositionMasterService positionMasterService;
    private static final String WARNING = "warning";
    private static final String POSITION_NOT_PRESENT = "There is no position added for the selected department and designation.";

    @Autowired
    private SearchPositionController(final PositionMasterService positionMasterService,
            final DepartmentService departmentService, final DesignationService designationMasterService,
            final DeptDesigService deptDesigService) {
        this.departmentService = departmentService;
        designationService = designationMasterService;
        this.deptDesigService = deptDesigService;
        this.positionMasterService = positionMasterService;
    }

    @ModelAttribute
    public DeptDesig deptDesig() {
        return new DeptDesig();
    }

    @ModelAttribute("departments")
    public List<Department> departments() {
        return departmentService.getAllDepartments();
    }

    @ModelAttribute("designations")
    public List<Designation> designations() {
        return designationService.getAllDesignationsSortByNameAsc();
    }

    @RequestMapping(value = "search", method = GET)
    public String search(final Model model) {
        model.addAttribute("mode", "new");
        return "position-search";
    }

    @RequestMapping(value = "position-getTotalPositionCount", method = RequestMethod.GET)
    public @ResponseBody String searchSanctionedAndOutSourcePositions(@RequestParam final String departmentId,
            @RequestParam final String designationId) {
        Long deptid = Long.valueOf(0), desigid = Long.valueOf(0);
        Integer outsourcedPost = 0, sanctionedPost = 0;

        if (departmentId != null && !"".equals(departmentId))
            deptid = Long.valueOf(departmentId);
        if (designationId != null && !"".equals(designationId))
            desigid = Long.valueOf(designationId);

        outsourcedPost = positionMasterService.getTotalOutSourcedPosts(deptid, desigid);
        sanctionedPost = positionMasterService.getTotalSanctionedPosts(deptid, desigid);

        return outsourcedPost + "/" + sanctionedPost;
    }

    @RequestMapping(value = "position-update", method = RequestMethod.GET)
    public @ResponseBody String changePosition(@RequestParam final String desigName,
            @RequestParam final String positionName, @RequestParam final String deptName,
            @RequestParam final String isoutsourced, @RequestParam final String positionId) {

        if (positionId != null) {
            final Position positionObj = positionMasterService.getPositionById(Long.valueOf(positionId));
            if (positionObj != null && !positionObj.getName().equalsIgnoreCase(positionName)) {
                final List<Position> positionList = positionMasterService.findByNameContainingIgnoreCase(positionName);
                if (positionList != null && positionList.size() > 0)
                    return "POSITIONNAMEALREADYEXIST";
                // return "NOCHANGESINEXISTINGNAME";
            }

            positionObj.setName(positionName);

            if (isoutsourced != null && isoutsourced.equalsIgnoreCase("TRUE")) {
                // Current position outsource is true.
                if (!positionObj.getIsPostOutsourced()) {
                    positionObj.setIsPostOutsourced(true);
                    positionObj.getDeptDesig()
                            .setOutsourcedPosts(positionObj.getDeptDesig().getOutsourcedPosts() != null
                                    ? positionObj.getDeptDesig().getOutsourcedPosts() + 1 : 1);
                }
            } else // If outsourced is false.
                if (positionObj.getIsPostOutsourced()) {
                positionObj.setIsPostOutsourced(false);
                positionObj.getDeptDesig().setOutsourcedPosts(positionObj.getDeptDesig().getOutsourcedPosts() != null
                        ? positionObj.getDeptDesig().getOutsourcedPosts() - 1 : 0);
            }

            positionMasterService.updatePosition(positionObj);
            return "SUCCESS";
        }
        if (positionName == null)
            return "POSITIONNAMEISNULL";
        return "SUCCESS";
    }

    @RequestMapping(value = "resultList-update", method = RequestMethod.GET)
    public @ResponseBody void springPaginationDataTablesUpdate(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        Long departmentId = Long.valueOf(0), designationId = Long.valueOf(0);

        if (request.getParameter("departmentId") != null && !"".equals(request.getParameter("departmentId")))
            departmentId = Long.valueOf(request.getParameter("departmentId"));
        if (request.getParameter("designationId") != null && !"".equals(request.getParameter("designationId")))
            designationId = Long.valueOf(request.getParameter("designationId"));

        final String complaintRouterJSONData = commonSearchResult(departmentId, designationId);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(complaintRouterJSONData, response.getWriter());
    }

    public String commonSearchResult(final Long departmentId, final Long designationId) {

        final List<Position> positionList = positionMasterService.getPageOfPositions(departmentId, designationId);
        final StringBuilder PositionJSONData = new StringBuilder("{\"data\":").append(toJSON(positionList, Position.class, PositionAdaptor.class)).append("}");
        return PositionJSONData.toString();
    }

    @RequestMapping(value = "search", method = RequestMethod.POST)
    public String searchPosition(@Valid @ModelAttribute final DeptDesig deptDesig, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final Model model) {
        if (errors.hasErrors())
            return "position-search";

        final DeptDesig departmentDesignation = deptDesigService
                .findByDepartmentAndDesignation(deptDesig.getDepartment().getId(), deptDesig.getDesignation().getId());

        if (departmentDesignation == null) {
            model.addAttribute(WARNING, POSITION_NOT_PRESENT);
            model.addAttribute("deptDesig", new DeptDesig());
            model.addAttribute("mode", "error");
            return "position-search";
        }

        final List<Position> positionList = positionMasterService
                .getAllPositionsByDeptDesigId(departmentDesignation.getId());
        model.addAttribute("deptDesig", departmentDesignation);
        model.addAttribute("positions", positionList);

        return "position-search";
    }
}
