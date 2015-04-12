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
        
        Page<Boundary> pageOfBoundaries = boundaryService.getListOfBoundariesAsPage(pageNumber, pageSize, boundaryTypeId);
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
