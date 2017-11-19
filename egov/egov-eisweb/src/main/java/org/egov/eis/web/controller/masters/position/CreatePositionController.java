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

package org.egov.eis.web.controller.masters.position;

import org.egov.eis.service.DeptDesigService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.pims.commons.DeptDesig;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/position/create")
public class CreatePositionController {

	private DepartmentService departmentService;
	private DesignationService designationService;
	private PositionMasterService positionMasterService;
	private DeptDesigService deptDesigService;
	private static final String SUCCESS_MESSAGE = "Successfully created Position !";
	private static final String WARNING = "warning";
	private static final String POSITION_ALREADY_PRESENT = "Position name already exist. Please change position name.";
	
	@Autowired
	private CreatePositionController(DeptDesigService deptDesigService,DepartmentService departmentService,DesignationService designationMasterService,PositionMasterService positionMasterService)
	{
		this.deptDesigService=deptDesigService;
		this.departmentService=departmentService;
		this.designationService=designationMasterService;
		this.positionMasterService=positionMasterService;
	}
	
	 @ModelAttribute("departments")
	    public List<Department> departments() {
	        return departmentService.getAllDepartments();
	    }

	 @ModelAttribute("designations")	
	 public List<Designation> designations()
	 {
		 return designationService.getAllDesignationsSortByNameAsc();
	 }
	 
	 @RequestMapping(method = RequestMethod.GET)
	    public String createPositionForm(@ModelAttribute final Position position) {
	        return "position-form";
	    }
	 
	 
	 @RequestMapping(method = RequestMethod.POST)
	public String createPosition(
			@Valid @ModelAttribute final Position position,final BindingResult errors, final RedirectAttributes redirectAttrs,
			Model model) {
		 
		if (errors.hasErrors())
			return "position-form";
     
		/*
		 * Check if position name already present in the system. Position name
		 * must be unique in the sytem.
		 */

		if (!positionMasterService.validatePosition(position)) {
			model.addAttribute(WARNING, POSITION_ALREADY_PRESENT);
			return "position-form";
		}
		/*
		 * If designation and department object already present in DeptDesig
		 * table, then increase the sanctioned post count by 1. Increase
		 * Postoutsourced by 1, if position type is outsourced from UI.
		 */
		if (position != null && position.getDeptDesig() != null	&& position.getDeptDesig().getDepartment() != null
				&& position.getDeptDesig().getDesignation() != null) {

			DeptDesig departmentDesignation = deptDesigService.findByDepartmentAndDesignation(position.getDeptDesig()
							.getDepartment().getId(), position.getDeptDesig().getDesignation().getId());

			if (departmentDesignation != null) {
					departmentDesignation.setSanctionedPosts((departmentDesignation.getSanctionedPosts()!=null?departmentDesignation.getSanctionedPosts() + 1:1));
						
					if (position.isPostOutsourced())
							departmentDesignation.setOutsourcedPosts((departmentDesignation.getOutsourcedPosts()!=null?departmentDesignation.getOutsourcedPosts()+1:1));
				
						position.setDeptDesig(departmentDesignation);
			} else {
						position.getDeptDesig().setSanctionedPosts(Integer.valueOf(1));
							if (position.isPostOutsourced()) 
								position.getDeptDesig().setOutsourcedPosts(Integer.valueOf(1));
							else
								position.getDeptDesig().setOutsourcedPosts(Integer.valueOf(0));
			}

		}
		positionMasterService.createPosition(position);
		model.addAttribute("mode", "saved");
		model.addAttribute(WARNING, SUCCESS_MESSAGE);
		return "position-form";
	    } 
}
