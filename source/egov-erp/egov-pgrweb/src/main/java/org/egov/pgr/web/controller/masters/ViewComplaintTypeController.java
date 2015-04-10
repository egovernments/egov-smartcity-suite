package org.egov.pgr.web.controller.masters;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.ComplaintTypeAdaptor;
import org.egov.pgr.service.ComplaintTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/view-complaintType")
public class ViewComplaintTypeController extends MultiActionController {

    private ComplaintTypeService complaintTypeService;
    public static final String CONTENTTYPE_JSON = "application/json";

    @Autowired
    public ViewComplaintTypeController(ComplaintTypeService complaintTypeService) {
        this.complaintTypeService = complaintTypeService;
    }

    @RequestMapping(value = "form", method = RequestMethod.GET)
    public String complaintTypeViewForm(@ModelAttribute ComplaintType complaintType, Model model) {
        return "view-complaintType";

    }

    public String toJSON(final Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.registerTypeAdapter(ComplaintType.class, new ComplaintTypeAdaptor()).create();
        String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "result", method = RequestMethod.GET)
    public @ResponseBody void springPaginationDataTables(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int pageStart = Integer.valueOf(request.getParameter("start"));
        int pageSize = Integer.valueOf(request.getParameter("length"));
        int pageNumber = pageStart / pageSize + 1;
        List<ComplaintType> totalRecords = complaintTypeService.findAll();

        if (pageSize == -1) {
            pageSize = totalRecords.size();
        }

        final List<ComplaintType> complaintTypeList = complaintTypeService.getListOfComplaintTypes(pageNumber, pageSize).getContent();
        final StringBuilder complaintTypeJSONData = new StringBuilder();
        complaintTypeJSONData.append("{\"draw\": ").append("0");
        complaintTypeJSONData.append(",\"recordsTotal\":").append(totalRecords.size());
        complaintTypeJSONData.append(",\"totalDisplayRecords\":").append(complaintTypeList.size());
        complaintTypeJSONData.append(",\"recordsFiltered\":").append(totalRecords.size());
        complaintTypeJSONData.append(",\"data\":").append(toJSON(complaintTypeList)).append("}");
        response.setContentType(CONTENTTYPE_JSON);
        IOUtils.write(complaintTypeJSONData, response.getWriter());
    }

}
