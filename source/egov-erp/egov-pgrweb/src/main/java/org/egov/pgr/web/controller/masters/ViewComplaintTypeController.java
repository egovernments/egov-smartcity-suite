package org.egov.pgr.web.controller.masters;

import java.util.List;

import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintTypeService;
import org.elasticsearch.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/view-complaintType/{pageNumber}")
public class ViewComplaintTypeController {

	private ComplaintTypeService complaintTypeService; 

	@Autowired
	public ViewComplaintTypeController(ComplaintTypeService complaintTypeService) {
		this.complaintTypeService = complaintTypeService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String complaintTypeViewForm(@PathVariable Integer pageNumber,Model model) {
		Page<ComplaintType> page = complaintTypeService.getListOfComplaintTypes(pageNumber);
		List<ComplaintType> compTypeList = Lists.newArrayList(page.iterator());
	    model.addAttribute("complaintTypePage", page);
	    model.addAttribute("list",compTypeList);
		return "view-complaintType";
	}

}
