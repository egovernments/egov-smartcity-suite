package org.egov.ptis.web.controller.reports;

import java.util.Date;
import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.transactions.AssessmentTransactions;
import org.egov.ptis.domain.service.report.DemandRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/report")
public class DemandRegisterController {
    
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    public PropertyTaxUtil propertyTaxUtil;
    @Autowired
    private FinancialYearDAO financialYearDAO;
    @Autowired
    private DemandRegisterService transactionsService;
    
    @ModelAttribute("wards")
    public List<Boundary> wardBoundaries() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(PropertyTaxConstants.WARD,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }
    
    @ModelAttribute("financialYears")
    public List<CFinancialYear> getFinancialYears(){
       
        return financialYearDAO.getAllPriorFinancialYears(new Date());
    }
    
    @RequestMapping(value = "/arrdmdrgstr-vlt/form", method = RequestMethod.GET)
    public String searchVLTArrearDemandRegisterForm(final Model model) {
        model.addAttribute("currDate", new Date());
        model.addAttribute("mode", PropertyTaxConstants.CATEGORY_TYPE_VACANTLAND_TAX);
        return "arrdmdrgstr-vlt-form";
    }

    @RequestMapping(value = "/arrdmdrgstr-pt/form", method = RequestMethod.GET)
    public String searchPTArrearDemandRegisterForm(final Model model) {
        model.addAttribute("currDate", new Date());
        model.addAttribute("mode", PropertyTaxConstants.CATEGORY_TYPE_PROPERTY_TAX);
        model.addAttribute("ADRReport", new AssessmentTransactions());
        return "arrdmdrgstr-pt-form";
    }
    
    @ResponseBody
    @RequestMapping(value = "/arrdmdrgstr/result", method = RequestMethod.GET)
    public ResponseEntity<byte[]> generateNotice(final Model model, @RequestParam final Long wardId, @RequestParam final Long financialYearId ) {
        final CFinancialYear financialYear = financialYearDAO.getFinancialYearById(financialYearId);
        ReportOutput reportOutput = transactionsService.generateDemandRegisterReport(PropertyTaxConstants.ADR_REPORT, wardId, financialYear.getStartingDate());
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=ArrearDemandRegister_"+financialYear.getFinYearRange()+".pdf");
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

}
