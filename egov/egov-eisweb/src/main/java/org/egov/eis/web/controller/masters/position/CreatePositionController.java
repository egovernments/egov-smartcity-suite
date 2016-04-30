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
