package org.egov.works.web.controller.mb;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.infra.exception.ApplicationException;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.web.adaptor.MeasurementBookJsonAdaptor;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.service.WorkOrderEstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
@RequestMapping(value = "/measurementbook")
public class CreateMBController {

    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;

    @Autowired
    private MeasurementBookJsonAdaptor measurementBookJsonAdaptor;

    @Autowired
    private MBHeaderService mbHeaderService;

    @Autowired
    private ResourceBundleMessageSource messageSource;

    public WorkOrderEstimate getWorkOrderEstimate(final Long workOrderId) {
        final WorkOrderEstimate workOrderEstimate = workOrderEstimateService.getWorkOrderEstimateById(workOrderId);
        return workOrderEstimate;
    }

    @RequestMapping(value = "/create/{workOrderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String createMeasurementBook(@PathVariable final Long workOrderId, final HttpServletRequest request,
            final HttpServletResponse response) {
        final WorkOrderEstimate workOrderEstimate = getWorkOrderEstimate(workOrderId);
        final String result = new StringBuilder().append(toSearchMilestoneTemplateJson(workOrderEstimate)).toString();
        return result;
    }

    public Object toSearchMilestoneTemplateJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(WorkOrderEstimate.class, measurementBookJsonAdaptor).create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/create/{workOrderId}", method = RequestMethod.POST)
    public @ResponseBody String create(@ModelAttribute("milestone") final MBHeader mbHeader,
            final Model model, final BindingResult errors, final HttpServletRequest request, final BindingResult resultBinder,
            final HttpServletResponse response)
            throws ApplicationException, IOException {

        final JsonObject jsonObject = new JsonObject();
        validateMBHeader(mbHeader, jsonObject);

        if (jsonObject.toString().length() > 2) {
            sendAJAXResponse(jsonObject.toString(), response);
            return "";
        }

        final MBHeader updatedMBHeader = mbHeaderService.update(mbHeader);
        return messageSource.getMessage("msg.mbheader.created",
                new String[] { "", "", updatedMBHeader.getMbRefNo() },
                null);
    }

    private void validateMBHeader(final MBHeader mbHeader, final JsonObject jsonObject) {
        // TODO Auto-generated method stub

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
