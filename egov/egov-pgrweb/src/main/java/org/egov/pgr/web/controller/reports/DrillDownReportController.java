package org.egov.pgr.web.controller.reports;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.pgr.service.reports.DrillDownReportService;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping(value = "/report")
public class DrillDownReportController {

    @Autowired
    private DrillDownReportService drillDownReportService;

    @Autowired
    public DrillDownReportController(final DrillDownReportService drillDownReportService) {
        this.drillDownReportService = drillDownReportService;
    }

    @ModelAttribute
    public void getReportHelper(final Model model) {
        ReportHelper reportHealperObj = new ReportHelper();
        model.addAttribute("reportHelper", reportHealperObj);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/drillDownReportByBoundary")
    public String searchAgeingReportByBoundaryForm(final Model model) {
        model.addAttribute("mode", "ByBoundary");
        return "drillDown-search";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/drillDownReportByDept")
    public String searchAgeingReportByDepartmentForm(final Model model) {
        model.addAttribute("mode", "ByDepartment");
        return "drillDown-search";
    }

    @ExceptionHandler(Exception.class)
    @RequestMapping(value = "/drillDown/resultList-update", method = RequestMethod.GET)
    public @ResponseBody void springPaginationDataTablesUpdate(@RequestParam String mode,
            @RequestParam String complaintDateType, @RequestParam DateTime fromDate, @RequestParam DateTime toDate,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        SQLQuery drillDownreportQuery = drillDownReportService.getDrillDownReportQuery(fromDate, toDate,
                complaintDateType, mode);
        drillDownreportQuery.setResultTransformer(Transformers.aliasToBean(DrillDownReportResult.class));
        List<AgeingReportResult> drillDownresult = drillDownreportQuery.list();

        String result = new StringBuilder("{ \"data\":").append(toJSON(drillDownresult)).append("}").toString();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(result, response.getWriter());

    }

    private Object toJSON(final Object object) {

        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(DrillDownReportResult.class,
                new DrillDownReportHelperAdaptor()).create();
        final String json = gson.toJson(object);
        return json;

    }

}
