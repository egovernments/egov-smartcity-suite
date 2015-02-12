package org.egov.pgr.web.controller;

import java.util.List;

import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.pgr.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;  

@Controller
public class AjaxController {

	private CommonService commonService;

	@Autowired
	public AjaxController(CommonService commonService) {
		this.commonService = commonService;
	}

	@RequestMapping(value="/ajax-getWards/{id}",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<BoundaryImpl> getWardsForZone(@PathVariable Integer id)
	{
		return commonService.getWards(id);   
	}


}
