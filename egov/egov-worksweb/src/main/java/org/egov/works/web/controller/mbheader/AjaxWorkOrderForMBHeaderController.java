package org.egov.works.web.controller.mbheader;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.works.contractorbill.service.ContractorBillRegisterService;
import org.egov.works.letterofacceptance.entity.SearchRequestLetterOfAcceptance;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.web.adaptor.SearchWorkOrderForMBHeaderJsonAdaptor;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.service.WorkOrderEstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@RestController
@RequestMapping(value = "/workorder")
public class AjaxWorkOrderForMBHeaderController {

    @Autowired
    private SearchWorkOrderForMBHeaderJsonAdaptor searchWorkOrderForMBHeaderJsonAdaptor;
    
    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;

    @RequestMapping(value = "/ajax-search", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchWorkOrders(
            @ModelAttribute final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance) {
        final List<WorkOrderEstimate> searchMilestoneList = workOrderEstimateService
                .searchWorkOrderToCreateMBHeader(searchRequestLetterOfAcceptance);
        final String result = new StringBuilder("{ \"data\":").append(searchWorkOrder(searchMilestoneList)).append("}")
                .toString();
        return result;
    }

    public Object searchWorkOrder(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(WorkOrderEstimate.class, searchWorkOrderForMBHeaderJsonAdaptor)
                .create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/validate/{workOrderId}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String validateWorkOrder(@PathVariable Long workOrderId, final HttpServletRequest request,
            final HttpServletResponse response) {
        final JsonObject jsonObject = new JsonObject();
        return null;
    }

    protected void sendAJAXResponse(final String msg, final HttpServletResponse response) {
        try {
            final Writer httpResponseWriter = response.getWriter();
            IOUtils.write(msg, httpResponseWriter);
            IOUtils.closeQuietly(httpResponseWriter);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
