package org.egov.pgr.web.controller.reports;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.pgr.service.reports.AgeingReportService;
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
public class AgeingReportController {

    @Autowired
    private AgeingReportService ageingReportService;
    
   
    @Autowired
    public AgeingReportController(final AgeingReportService ageingReportService) {
        this.ageingReportService=ageingReportService;
    }
    @ModelAttribute
    public void getReportHelper( final Model model) {
         ReportHelper reportHealperObj= new ReportHelper();
       
        Map<String,String> status = new LinkedHashMap<String,String>();
        status.put("Completed", "Completed");
        status.put("Pending", "Pending");
        model.addAttribute("status", status);
        
/*        Map<String,String>  groupBy= new LinkedHashMap<String,String>();   
        groupBy.put("Department", "Department");
        groupBy.put("Zone", "Zone");
        model.addAttribute("groupByList", groupBy);
        reportHealperObj.setGroupBy("Department");*/
        model.addAttribute("reportHelper", reportHealperObj);
        
    }
    @RequestMapping(method = RequestMethod.GET, value = "/ageingReportByBoundary")
    public String searchAgeingReportByBoundaryForm( final Model model) {
        model.addAttribute("mode", "ByBoundary");
        return "ageing-search"; 
    }
    @RequestMapping(method = RequestMethod.GET, value = "/ageingReportByDept")
    public String searchAgeingReportByDepartmentForm( final Model model) {
        model.addAttribute("mode", "ByDepartment");
        return "ageing-search";
    }
     
    @ExceptionHandler(Exception.class)
    @RequestMapping(value = "/ageing/resultList-update", method = RequestMethod.GET) 
    public @ResponseBody void springPaginationDataTablesUpdate(@RequestParam String mode,@RequestParam String complaintDateType,@RequestParam DateTime fromDate ,@RequestParam String status,@RequestParam DateTime toDate, final HttpServletRequest request,
 final HttpServletResponse response)
            throws IOException {
        
//TODO: IF COMPLETED STATUS, THEN USE EG_WF_state createddate.
//TODO: if zone or department is null, then add it under not stated case.
//TODO: boundary.. they will add at complaint level.
  
        
        SQLQuery ageingreportQuery = ageingReportService.getageingReport(fromDate, toDate, status,complaintDateType);
        ageingreportQuery.setResultTransformer(Transformers.aliasToBean(AgeingReportResult.class));
        List<AgeingReportResult> ageingresult=  ageingreportQuery.list();
        
        String result = new StringBuilder("{ \"data\":").append(toJSON(ageingresult)).append("}").toString();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(result, response.getWriter());

    }
    private Object toJSON(final Object object) {

        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(AgeingReportResult.class, new AgeingReportHelperAdaptor())
                .create();
        final String json = gson.toJson(object);
        return json;
    
    }
}
