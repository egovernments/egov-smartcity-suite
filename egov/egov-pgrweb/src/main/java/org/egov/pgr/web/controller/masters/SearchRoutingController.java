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

        final Long boundaryTypeId = Long.valueOf(request.getParameter("boundaryTypeId"));
        final Long complaintTypeId = Long.valueOf(request.getParameter("complaintTypeId"));
        final Long boundaryId = Long.valueOf(request.getParameter("boundaryId"));

        final String complaintRouterJSONData = commonSearchResult(boundaryTypeId, complaintTypeId, boundaryId);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(complaintRouterJSONData, response.getWriter());
    }

    @RequestMapping(value = "/resultList-view", method = RequestMethod.GET)
    public @ResponseBody void springPaginationDataTablesView(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {

        final Long boundaryTypeId = Long.valueOf(request.getParameter("boundaryTypeId"));
        final Long complaintTypeId = Long.valueOf(request.getParameter("complaintTypeId"));
        final Long boundaryId = Long.valueOf(request.getParameter("boundaryId"));

        final String complaintRouterJSONData = commonSearchResult(boundaryTypeId, complaintTypeId, boundaryId);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(complaintRouterJSONData, response.getWriter());
    }

    public String commonSearchResult(final Long boundaryTypeId, final Long complaintTypeId, final Long boundaryId) {
        final List<ComplaintRouter> pageOfRouters = complaintRouterService.getPageOfRouters(boundaryTypeId,
                complaintTypeId, boundaryId);
        return new StringBuilder("{ \"data\":").append(toJSON(pageOfRouters)).append("}").toString();
    }

    private String toJSON(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(ComplaintRouter.class, new ComplaintRouterAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }
}
