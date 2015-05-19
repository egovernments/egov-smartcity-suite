package org.egov.pgr.web.controller.masters;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.pgr.entity.ComplaintRouter;
import org.egov.pgr.entity.ComplaintRouterAdaptor;
import org.egov.pgr.service.ComplaintRouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping(value = "/router")
public class SearchRoutingController {
    public static final String CONTENTTYPE_JSON = "application/json";

    protected BoundaryTypeService boundaryTypeService;

    protected ComplaintRouterService complaintRouterService;

    @Autowired
    public SearchRoutingController(final BoundaryTypeService boundaryTypeService,
            final ComplaintRouterService complaintRouterService) {
        this.boundaryTypeService = boundaryTypeService;
        this.complaintRouterService = complaintRouterService;
    }

    @ModelAttribute
    public ComplaintRouter complaintRouter() {
        return new ComplaintRouter();
    }

    @ModelAttribute("boundaryTypes")
    public List<BoundaryType> boundaryTypes() {
        return boundaryTypeService.getBoundaryTypeByHierarchyTypeName("ADMINISTRATION");
    }

    @RequestMapping(value = "/search-update", method = GET)
    public String searchRouterUpdateForm(final Model model) {
        model.addAttribute("boundaryTypes", boundaryTypeService.getBoundaryTypeByHierarchyTypeName("ADMINISTRATION"));
        return "router-searchUpdate";
    }

    @RequestMapping(value = "/search-view", method = GET)
    public String searchRouterViewForm(final Model model) {
        model.addAttribute("boundaryTypes", boundaryTypeService.getBoundaryTypeByHierarchyTypeName("ADMINISTRATION"));
        return "router-searchView";
    }

    @RequestMapping(value = "/resultList-update", method = RequestMethod.GET)
    public @ResponseBody void springPaginationDataTablesUpdate(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {

        final int pageStart = Integer.valueOf(request.getParameter("start"));
        final int pageSize = Integer.valueOf(request.getParameter("length"));
        final Long boundaryTypeId = Long.valueOf(request.getParameter("boundaryTypeId"));
        final Long complaintTypeId = Long.valueOf(request.getParameter("complaintTypeId"));
        final Long boundaryId = Long.valueOf(request.getParameter("boundaryId"));

        final int pageNumber = pageStart / pageSize + 1;
        final String complaintRouterJSONData = commonSearchResult(pageNumber, pageSize, boundaryTypeId,
                complaintTypeId, boundaryId);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(complaintRouterJSONData, response.getWriter());
    }

    @RequestMapping(value = "/resultList-view", method = RequestMethod.GET)
    public @ResponseBody void springPaginationDataTablesView(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {

        final int pageStart = Integer.valueOf(request.getParameter("start"));
        final int pageSize = Integer.valueOf(request.getParameter("length"));
        final Long boundaryTypeId = Long.valueOf(request.getParameter("boundaryTypeId"));
        final Long complaintTypeId = Long.valueOf(request.getParameter("complaintTypeId"));
        final Long boundaryId = Long.valueOf(request.getParameter("boundaryId"));

        final int pageNumber = pageStart / pageSize + 1;
        final String complaintRouterJSONData = commonSearchResult(pageNumber, pageSize, boundaryTypeId,
                complaintTypeId, boundaryId);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(complaintRouterJSONData, response.getWriter());
    }

    public String commonSearchResult(final Integer pageNumber, final Integer pageSize, final Long boundaryTypeId,
            final Long complaintTypeId, final Long boundaryId) {
        final Page<ComplaintRouter> pageOfRouters = complaintRouterService.getPageOfRouters(pageNumber, pageSize,
                boundaryTypeId, complaintTypeId, boundaryId);
        final List<ComplaintRouter> routerList = pageOfRouters.getContent();
        final StringBuilder complaintRouterJSONData = new StringBuilder();
        complaintRouterJSONData.append("{\"draw\": ").append("0");
        complaintRouterJSONData.append(",\"recordsTotal\":").append(pageOfRouters.getTotalElements());
        complaintRouterJSONData.append(",\"totalDisplayRecords\":").append(pageSize);
        complaintRouterJSONData.append(",\"recordsFiltered\":").append(pageOfRouters.getTotalElements());
        complaintRouterJSONData.append(",\"data\":").append(toJSON(routerList)).append("}");
        return complaintRouterJSONData.toString();
    }

    private String toJSON(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(ComplaintRouter.class, new ComplaintRouterAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }
}
