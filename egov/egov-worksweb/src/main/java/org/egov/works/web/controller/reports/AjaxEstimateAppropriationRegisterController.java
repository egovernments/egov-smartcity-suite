package org.egov.works.web.controller.reports;

import java.util.List;

import org.egov.works.lineestimate.entity.LineEstimateAppropriation;
import org.egov.works.reports.entity.EstimateAppropriationRegisterSearchRequest;
import org.egov.works.reports.service.EstimateAppropriationRegisterService;
import org.egov.works.web.adaptor.EstimateAppropriationRegisterJSONAdaptor;
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
@RequestMapping("/reports")
public class AjaxEstimateAppropriationRegisterController {
    
    @Autowired
    private EstimateAppropriationRegisterService estimateAppropriationRegisterService;
    
    @Autowired
    private EstimateAppropriationRegisterJSONAdaptor estimateAppropriationRegisterJSONAdaptor;
    
    @RequestMapping(value = "/ajax-estimateappropriationregister", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String showSearchEstimateAppropriationRegister(final Model model,
            @ModelAttribute final EstimateAppropriationRegisterSearchRequest estimateAppropriationRegisterSearchRequest) {
        
        final List<LineEstimateAppropriation> lineEstimateAppropriations = estimateAppropriationRegisterService
                .searchEstimateAppropriationRegister(estimateAppropriationRegisterSearchRequest);
        final String result = new StringBuilder("{ \"data\":").append(toSearchEstimateAppropriationRegisterJson(lineEstimateAppropriations))
                .append("}").toString();
        return result;
    }

    public Object toSearchEstimateAppropriationRegisterJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(LineEstimateAppropriation.class, estimateAppropriationRegisterJSONAdaptor).create();
        final String json = gson.toJson(object);
        return json;
    }

}
