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
package org.egov.infra.web.controller.admin.masters.boundary;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.web.support.json.adapter.BoundaryAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
public class PageViewBoundaryController {

    private BoundaryService boundaryService;

    @Autowired
    public PageViewBoundaryController(BoundaryService boundaryService) {
        this.boundaryService = boundaryService;
    }
    
    @RequestMapping(value = "/list-boundaries", method = RequestMethod.GET)
    public @ResponseBody void springPaginationDataTables(HttpServletRequest request, HttpServletResponse response) throws IOException {

        int pageStart = Integer.valueOf(request.getParameter("start"));
        int pageSize = Integer.valueOf(request.getParameter("length"));
        Long boundaryTypeId = Long.valueOf(request.getParameter("boundaryTypeId"));        
        int pageNumber = pageStart / pageSize + 1;
        
        Page<Boundary> pageOfBoundaries = boundaryService.getPageOfBoundaries(pageNumber, pageSize, boundaryTypeId);
        final List<Boundary> boundaryList = pageOfBoundaries.getContent();
        final StringBuilder complaintTypeJSONData = new StringBuilder();
        complaintTypeJSONData.append("{\"draw\": ").append("0");
        complaintTypeJSONData.append(",\"recordsTotal\":").append(pageOfBoundaries.getTotalElements());
        complaintTypeJSONData.append(",\"totalDisplayRecords\":").append(pageSize);
        complaintTypeJSONData.append(",\"recordsFiltered\":").append(pageOfBoundaries.getTotalElements());
        complaintTypeJSONData.append(",\"data\":").append(toJSON(boundaryList)).append("}");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(complaintTypeJSONData, response.getWriter());
    }
    
    private String toJSON(final Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.registerTypeAdapter(Boundary.class, new BoundaryAdapter()).create();
        String json = gson.toJson(object);
        return json;
    }
}
