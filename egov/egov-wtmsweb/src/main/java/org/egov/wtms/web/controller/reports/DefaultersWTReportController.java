
package org.egov.wtms.web.controller.reports;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.apache.commons.io.IOUtils;
import org.egov.commons.EgwStatus;
import org.egov.commons.Installment;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.DailyWTCollectionReport;
import org.egov.wtms.application.service.DefaultersReport;
import org.egov.wtms.application.service.DefaultersWTReportService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.utils.DemandComparatorByInstallmentOrder;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.hibernate.SQLQuery;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/report/defaultersWTReport/search")

public class DefaultersWTReportController {
	
	@Autowired
    private BoundaryService boundaryService;
	
	@Autowired
    private DefaultersWTReportService defaultersWTReportService;
	

    @Autowired
    public WaterConnectionDetailsService waterConnectionDetailsService;
    
    @Autowired
    public ConnectionDemandService connectionDemandService;
    
    @Autowired
    private DemandGenericDao demandGenericDao;

	
	@RequestMapping(method = GET)
    public String search(final Model model) {
		 model.addAttribute("currentDate", new Date());
        return "defaultersWTReport-search";
    }
	
	@ModelAttribute
    public DefaultersReport reportModel() {
        return new DefaultersReport();
    }

	 public @ModelAttribute("revenueWards") List<Boundary> revenueWardList() {
	        return  boundaryService
	                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WaterTaxConstants.REVENUE_WARD, REVENUE_HIERARCHY_TYPE);
	    }
	 
	 
	 public @ModelAttribute("topDefaultersList") List<Integer> defaultersList() {
		 List<Integer> topdefaultersList = new ArrayList<Integer>();
		 topdefaultersList.add(10);
		 topdefaultersList.add(50);
		 topdefaultersList.add(100);
		 topdefaultersList.add(500);
		 topdefaultersList.add(1000);
	        return topdefaultersList;
	    }
	 
	 @RequestMapping(value = "/result", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	    public @ResponseBody void searchResult(final HttpServletRequest request, final HttpServletResponse response)
	                    throws IOException, ParseException {
	        String ward = "";
	        String topDefaulters= "";
	        String fromAmount= "";
	        String toAmount= "";
	        
		 String ss ="";
	        if (null != request.getParameter("ward"))
	        	ward = request.getParameter("ward");
	        if (null != request.getParameter("topDefaulters"))
	        	topDefaulters = request.getParameter("topDefaulters");
	        if (null != request.getParameter("fromAmount"))
	        	fromAmount = request.getParameter("fromAmount");
	        if (null != request.getParameter("toAmount"))
	        	toAmount = request.getParameter("toAmount");
		 
		 
		 List<DefaultersReport> defaultersreportlist = new ArrayList<DefaultersReport>();
	        final SQLQuery query = defaultersWTReportService.getDefaultersReportDetails(fromAmount, toAmount, ward, topDefaulters);
	        defaultersreportlist = query.list();
	        String result = null;
	        for(DefaultersReport dd:defaultersreportlist){
	        	dd.setDuePeriodFrom(getDuePeriodFrom(dd.getHscNo()));
	        }
	        result = new StringBuilder("{ \"data\":").append(toJSON(defaultersreportlist)).append("}").toString();
	        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	        IOUtils.write(result, response.getWriter());
	    }
	 public String getDuePeriodFrom(String consumerCode)
	    {
	    	WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCode(consumerCode);
	    	final DemandComparatorByInstallmentOrder demandComparatorByOrderId = new DemandComparatorByInstallmentOrder();
	    	Set<EgDemandDetails> egdemandtemplist = new HashSet<EgDemandDetails>();
	        final Set<EgDemandDetails> demnadDetList =waterConnectionDetails.getDemand().getEgDemandDetails();
	        for (final EgDemandDetails egDemandTemp : demnadDetList) {
	        	if (!egDemandTemp.getAmount().equals(egDemandTemp.getAmtCollected())){
	        		egdemandtemplist.addAll(egDemandTemp.getEgDemand().getEgDemandDetails());
	        	}
	        }
	        List<EgDemandDetails> egdemandlist = new ArrayList<EgDemandDetails>(egdemandtemplist);
	        Collections.sort(egdemandlist, demandComparatorByOrderId);
	    	return egdemandlist.get(0).getEgDemandReason().getEgInstallmentMaster().getDescription();
	    }

	 
	 private Object toJSON(final Object object) {
	        final GsonBuilder gsonBuilder = new GsonBuilder();
	        final Gson gson = gsonBuilder.registerTypeAdapter(DailyWTCollectionReport.class,
	                new DefaultersReportAdaptor()).create();
	        final String json = gson.toJson(object);
	        return json;
	    }
}