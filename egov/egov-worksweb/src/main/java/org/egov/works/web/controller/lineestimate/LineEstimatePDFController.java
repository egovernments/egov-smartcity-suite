package org.egov.works.web.controller.lineestimate;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportConstants;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.web.utils.WebUtils;
import org.egov.infstr.utils.NumberUtil;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.LineEstimateAppropriation;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.entity.enums.WorkCategory;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/lineestimate")
public class LineEstimatePDFController {
    
    @Autowired
    private ReportService reportService;
    
    @Autowired
    private WorksUtils worksUtils;
    
    @Autowired
    private LineEstimateService lineEstimateService;
    
    public static final String LINEESTIMATEPDF = "lineEstimatePDF";
    private final Map<String, Object> reportParams = new HashMap<String, Object>();
    private ReportRequest reportInput = null;
    private ReportOutput reportOutput = null;
 
   
    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;
    

    @RequestMapping(value = "/lineEstimatePDF/{lineEstimateId}", method = RequestMethod.GET)
     public @ResponseBody ResponseEntity<byte[]> generateLineEstimatePDF(final HttpServletRequest request,@PathVariable("lineEstimateId") final Long id,
            final HttpSession session) throws IOException{
                 final LineEstimate lineEstimate = lineEstimateService.getLineEstimateById(id);
                 return generateReport(lineEstimate, request, session);
          }


    private ResponseEntity<byte[]> generateReport(LineEstimate lineEstimate, HttpServletRequest request,HttpSession session) {
        if ( lineEstimate !=null ) {
            
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            
            final String url = WebUtils.extractRequestDomainURL(request, false);
            reportParams.put("cityLogo", url.concat(ReportConstants.IMAGE_CONTEXT_PATH)
            .concat((String) request.getSession().getAttribute("citylogo")));
            
            final String cityName = request.getSession().getAttribute("citymunicipalityname").toString();
            reportParams.put("cityName", cityName);
            reportParams.put("proNo", (lineEstimate.getAdminSanctionNumber() != null ? lineEstimate.getAdminSanctionNumber() : ""));
            reportParams.put("sub", lineEstimate.getSubject());
            reportParams.put("ref", lineEstimate.getReference());
            reportParams.put("dated", (lineEstimate.getAdminSanctionDate() != null ? formatter.format(lineEstimate.getAdminSanctionDate()) : ""));
            reportParams.put("scheme", (lineEstimate.getScheme() != null ? lineEstimate.getScheme().getName() : ""));
            reportParams.put("function", (lineEstimate.getFunction() != null ? lineEstimate.getFunction().getName() : ""));
            reportParams.put("account", (lineEstimate.getBudgetHead() != null ? lineEstimate.getBudgetHead().getName() : "" ));
            //reportParams.put("lineEstimateDetails",lineEstimate.getLineEstimateDetails() );
            reportParams.put("modeOfAllotment", lineEstimate.getModeOfAllotment().toString());
            if(lineEstimate.getWorkCategory().toString().equals(WorkCategory.NON_SLUM_WORK.toString()))
                reportParams.put("typeOfSlum", "No");
            else {
                reportParams.put("typeOfSlum", "Yes - " + lineEstimate.getTypeOfSlum().toString().replace("_", " ") + " Slum - " + lineEstimate.getBeneficiary().toString());
            }
            reportParams.put("present", lineEstimate.getAdminSanctionBy() != null ? lineEstimate.getAdminSanctionBy().getName() : "");
            String zonalCommissioner = worksUtils.getUserDesignation(lineEstimate.getAdminSanctionBy());
            reportParams.put("zonalCommissioner", zonalCommissioner);
            reportParams.put("zonalCommissionerCapital", zonalCommissioner != null ? zonalCommissioner.toUpperCase() : "");
            reportParams.put("beneficiary", lineEstimate.getBeneficiary() != null ? lineEstimate.getBeneficiary().toString() : "");
            
            String basNos = "";
            BigDecimal totalAmount = BigDecimal.ZERO;
            
            for(LineEstimateDetails led : lineEstimate.getLineEstimateDetails()) {
                List<LineEstimateAppropriation> leas = led.getLineEstimateAppropriations();
                for(LineEstimateAppropriation lea : leas) {
                    basNos += lea.getBudgetUsage().getAppropriationnumber() + ", ";
                }
                totalAmount = totalAmount.add(led.getEstimateAmount());
            }
            if(basNos.endsWith(", "))
                basNos = basNos.substring(0, basNos.length() - 2);
            
            reportParams.put("basNos", basNos);
            reportParams.put("totalAmount", totalAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN));
            reportParams.put("totalAmountWords", NumberUtil.amountInWords(totalAmount));
            
            reportInput = new ReportRequest(LINEESTIMATEPDF,lineEstimate.getLineEstimateDetails(), reportParams );
           
        }
        
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=LineEstimate.pdf");
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
        
 }
    
}