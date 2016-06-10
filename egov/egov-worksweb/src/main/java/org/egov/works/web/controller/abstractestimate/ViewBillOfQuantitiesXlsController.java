package org.egov.works.web.controller.abstractestimate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.service.EstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/abstractestimate")
public class ViewBillOfQuantitiesXlsController {

    public static final String BOQ = "Bill Of Qunatities";
    @Autowired
    private EstimateService estimateService;

    @Autowired
    private ReportService reportService;

    @RequestMapping(value="/viewBillOfQuantitiesXls/{abstractEstimateId}",method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> viewBillOfQuantities(@PathVariable final Long abstractEstimateId,
            final Model model) {
        final AbstractEstimate estimate = estimateService.getAbstractEstimateById(abstractEstimateId);
        final ReportRequest reportRequest = new ReportRequest("BillOfQuantities", estimate.getSORActivities(),
                createHeaderParams(estimate, BOQ));
        reportRequest.setReportFormat(FileFormat.XLS);
        final ReportOutput reportOutput = reportService.createReport(reportRequest);
        final HttpHeaders headers = new HttpHeaders();
        headers.add("content-disposition", "no-cache;filename=AbstractEstimate-BillOfQuantites.xls");
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

    private Map createHeaderParams(final AbstractEstimate estimate, final String type) {
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        if (type.equalsIgnoreCase(BOQ)) {
            reportParams.put("workName", estimate.getName());
            reportParams.put("deptName", estimate.getExecutingDepartment().getName());
            reportParams.put("estimateNo", estimate.getEstimateNumber());
            reportParams.put("activitySize",
                    estimate.getSORActivities() == null ? 0 : estimate.getSORActivities().size());
            reportParams.put("NonSOR_Activities", estimate.getNonSORActivities());
            reportParams.put("grandTotalAmt", BigDecimal.valueOf(estimate.getWorkValue()));
            reportParams.put("estimateDate",
                    estimate.getEstimateDate() != null ? formatter.format(estimate.getEstimateDate()) : "");
        }
        return reportParams;
    }
}
