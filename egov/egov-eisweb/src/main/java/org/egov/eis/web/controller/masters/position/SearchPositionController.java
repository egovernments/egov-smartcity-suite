package org.egov.eis.web.controller.masters.position;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.egov.eis.service.DesignationService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/position")
public class SearchPositionController {

	private DepartmentService departmentService;
	private DesignationService designationService;
	
	@Autowired
	private SearchPositionController(DepartmentService departmentService,DesignationService designationMasterService)
	{
		this.departmentService=departmentService;
		this.designationService=designationMasterService;
	}
	
	 @RequestMapping(value = "search", method = GET)
	    public String search(@ModelAttribute final Position position) {
	        return "position-search";
	    }

}
