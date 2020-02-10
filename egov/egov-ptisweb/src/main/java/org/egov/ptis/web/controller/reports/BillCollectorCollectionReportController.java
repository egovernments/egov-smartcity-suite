package org.egov.ptis.web.controller.reports;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.egov.commons.CFinancialYear;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.ptis.actions.reports.BillCollectorCollectionAdaptor;
import org.egov.ptis.domain.entity.property.CollectionSummary;
import org.egov.ptis.domain.entity.property.view.BillCollectorCollectionRequest;
import org.egov.ptis.domain.service.report.BillCollectorCollectionReportService;
import org.egov.ptis.domain.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/report/billcollectorcollection")
public class BillCollectorCollectionReportController {

    @Autowired
    private BillCollectorCollectionReportService bcCollectionReportService;

    @Autowired
    private ReportService reportService;

    @ModelAttribute("year")
    public List<CFinancialYear> currentFinancialYear() {
        return bcCollectionReportService.getFinancialYears();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String searchForm(final Model model) {
        model.addAttribute("billCollectorCollection", new BillCollectorCollectionRequest());
        model.addAttribute("currDate", new Date());
        return "bccollection-view";
    }

    @ModelAttribute("billCollectors")
    public Set<User> loadCollectionOperators() {
        return reportService.getCollectionOperators();
    }

    @GetMapping(value = "/result", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchBaseRegister(final BillCollectorCollectionRequest billCollectorCollectionRequest, final Model model,
            final HttpServletRequest request) {
        Page<CollectionSummary> coll = bcCollectionReportService.pagedBCCOllectionRecords(billCollectorCollectionRequest);
        return new DataTable<>(coll,
                billCollectorCollectionRequest.draw()).toJson(BillCollectorCollectionAdaptor.class);
    }

}
