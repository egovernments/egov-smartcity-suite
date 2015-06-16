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
package org.egov.pgr.web.controller.masters.escalation;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.pgr.entity.ComplaintRouter;
import org.egov.pgr.entity.ComplaintRouterAdaptor;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintRouterService;
import org.egov.pgr.service.ComplaintTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping(value = "/escalation")
public class SearchEscalationController {
	public static final String CONTENTTYPE_JSON = "application/json";

	protected final ComplaintTypeService complaintTypeService;
	 private final BoundaryTypeService boundaryTypeService;
	 private final PositionMasterService  positionMasterService;
	 private final ComplaintRouterService complaintRouterService;
	
	 
	 @ModelAttribute
	public ComplaintRouter complaintRouter() {
		return new ComplaintRouter();
	}
 
	@ModelAttribute("complaintTypes")
	public List<ComplaintType> complaintTypes() {
		return complaintTypeService.findAll();
	}

	@ModelAttribute("boundaryTypes")
	public List<BoundaryType> boundaryTypes() {
		return boundaryTypeService
				.getBoundaryTypeByHierarchyTypeName("ADMINISTRATION");
	}

	@Autowired
	public SearchEscalationController(
			final ComplaintTypeService complaintTypeService,PositionMasterService positionMasterService,
			BoundaryTypeService boundaryTypeService,ComplaintRouterService complaintRouterService) {
		this.complaintTypeService = complaintTypeService;
		this.positionMasterService=positionMasterService;
		this.boundaryTypeService = boundaryTypeService;
		this.complaintRouterService=complaintRouterService;
	}

	@RequestMapping(value = "/search-view", method = RequestMethod.POST)
	public String searchEscalationTimeForm(
			@ModelAttribute ComplaintRouter complaintRouter, final Model model) {	
		return "escalation-searchView";
	}

	@RequestMapping(value = "/search-view", method = GET)
	public String searchForm(@ModelAttribute ComplaintRouter complaintRouter,
			final Model model) {
	
		return "escalation-searchView";
	}
	
	 @ExceptionHandler(Exception.class)
	@RequestMapping(value = "resultList-update", method = RequestMethod.GET)
    public @ResponseBody void springPaginationDataTablesUpdate(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
		 Long complaintTypeId = Long.valueOf(0),positionId=  Long.valueOf(0),boundaryTypeId=  Long.valueOf(0),boundaryId=  Long.valueOf(0);
    //    final int pageStart = Integer.valueOf(request.getParameter("start"));
      //  final int pageSize = Integer.valueOf(request.getParameter("length"));
        if(request.getParameter("complaintTypeId")!=null && !"".equals(request.getParameter("complaintTypeId")))
          complaintTypeId = Long.valueOf(request.getParameter("complaintTypeId"));
        if(request.getParameter("positionId")!=null && !"".equals(request.getParameter("positionId"))) 
        	positionId = Long.valueOf(request.getParameter("positionId"));
        if(request.getParameter("boundaryId")!=null && !"".equals(request.getParameter("boundaryId"))) 
        	boundaryId = Long.valueOf(request.getParameter("boundaryId"));
        if(request.getParameter("boundaryTypeId")!=null && !"".equals(request.getParameter("boundaryTypeId"))) 
        	boundaryTypeId = Long.valueOf(request.getParameter("boundaryTypeId"));
      
 //      final int pageNumber = pageStart / pageSize + 1;
        //pageNumber, pageSize,
        final String escalationTimeRouterJSONData = commonSearchResult( complaintTypeId,boundaryId,boundaryTypeId,
        		positionId);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(escalationTimeRouterJSONData, response.getWriter());
    }
//final Integer pageNumber, final Integer pageSize,
 public String commonSearchResult( final Long complaintTypeId,final Long boundaryId,final Long boundaryTypeId,
            final Long positionId) {
		 
        final List<ComplaintRouter> pageOfEscalation = complaintRouterService.getPageOfRouters(//pageNumber, pageSize,
        		complaintTypeId, boundaryTypeId,boundaryId,positionId);
        return new StringBuilder("{ \"data\":").append(toJSON(pageOfEscalation)).append("}").toString();
    }

    private String toJSON(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(ComplaintRouter.class, new ComplaintRouterAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }
 
    
    

}
