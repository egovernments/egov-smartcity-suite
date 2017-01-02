package org.egov.ptis.web.controller.reports;

import static org.egov.infra.utils.JsonUtils.toJSON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.actions.reports.DCBReportHelperAdaptor;
import org.egov.ptis.actions.reports.DCBReportResult;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterHibernateDAO;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.service.report.ReportService;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/report/dcbReportVLT")
public class DCBReportVLTController {

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private PropertyTypeMasterHibernateDAO propertyTypeMasterDAO;

    @ModelAttribute("wards")
    public Map<Long, String> wardBoundaries() {
        final List<Boundary> wardList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                PropertyTaxConstants.WARD,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
        Map<Long, String> wardBndryMap = new HashMap<Long, String>();
        wardBndryMap = CommonServices.getFormattedBndryMap(wardList);
        wardBndryMap.put(0l, "All");
        return wardBndryMap;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String searchDcbForm(final Model model) {
        model.addAttribute("DCBVLTReport", new DCBReportResult());
        model.addAttribute("mode", "ward");
        return "dcbVLT-form";
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void dcbReportSearchResult(@RequestParam final String boundaryId,
            @RequestParam final String mode,
            @RequestParam final String courtCase, final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        final PropertyTypeMaster propertyTypeMaster = propertyTypeMasterDAO
                .getPropertyTypeMasterByCode(PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND);
        final List<String> propertyType = new ArrayList<>();
        propertyType.add(propertyTypeMaster.getId().toString());
        final SQLQuery query = reportService.prepareQueryForDCBReport(Long.valueOf(boundaryId), mode, Boolean.valueOf(courtCase),
                propertyType);
        query.setResultTransformer(new AliasToBeanResultTransformer(DCBReportResult.class));
        final List<DCBReportResult> resultList = query.list();
        final String result = new StringBuilder("{ \"data\":").append(toJSON(resultList, DCBReportResult.class,
                DCBReportHelperAdaptor.class)).append("}").toString();
        IOUtils.write(result, response.getWriter());
    }
}
