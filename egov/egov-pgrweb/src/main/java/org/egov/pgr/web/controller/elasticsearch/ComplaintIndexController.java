package org.egov.pgr.web.controller.elasticsearch;

import java.util.HashMap;
import java.util.List;

import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.elasticsearch.ComplaintDashBoardRequest;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.elasticsearch.ComplaintIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value ="/complaint/aggregate")
public class ComplaintIndexController {
	
	@Autowired
	private ComplaintIndexService ComplaintIndexService;
	
	@RequestMapping(value = "/grievance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public HashMap<String, Object> getDashBoardResponse(@RequestBody  ComplaintDashBoardRequest complaintRequest){
		 return  ComplaintIndexService.getGrievanceReport(complaintRequest);
	}
}
