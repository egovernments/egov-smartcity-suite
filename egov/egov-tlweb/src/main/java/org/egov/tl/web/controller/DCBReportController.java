/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.tl.web.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.tl.utils.Constants;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/tlreports")
public class DCBReportController {

    public static final String ZONEWISE = "zone";
    public static final String WARDWISE = "ward";
    public static final String BLOCKWISE = "block";
    public static final String LOCALITYWISE = "locality";
    public static final String PROPERTY = "property";

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private BoundaryService boundaryService;
    private final DCBReportResult dCBReportResult = new DCBReportResult();

    @ModelAttribute("dCBReportResult")
    public DCBReportResult dCBReportResultModel() {
        return dCBReportResult;
    }

   

    @ModelAttribute("zones")
    public List<Boundary> zones() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(Constants.ZONE,
                Constants.REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute("wards")
    public List<Boundary> wards() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(Constants.DIVISION,
                Constants.REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute("blocks")
    public List<Boundary> blocks() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(Constants.BLOCK,
                Constants.REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute("localitys")
    public List<Boundary> localitys() {
        return boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(Constants.LOCALITY, Constants.LOCATION_HIERARCHY_TYPE);
    }

    @RequestMapping(value = "/dCBReport/zoneWise", method = RequestMethod.GET)
    public String zoneWisesearch(final Model model) {
        model.addAttribute("mode", "zone");
        model.addAttribute("reportType", "zoneWise");
        return "dCBReport-search";
    }

    @RequestMapping(value = "/dCBReport/wardWise", method = RequestMethod.GET)
    public String wardWisesearch(final Model model) {
        model.addAttribute("mode", "ward");
        model.addAttribute("reportType", "wardWise");
        return "dCBReport-search";
    }

    @RequestMapping(value = "/dCBReport/blockWise", method = RequestMethod.GET)
    public String blockWisesearch(final Model model) {
        model.addAttribute("mode", "block");
        model.addAttribute("reportType", "blockWise");
        return "dCBReport-search";
    }

    @RequestMapping(value = "/dCBReport/localityWise", method = RequestMethod.GET)
    public String localityWisesearch(final Model model) {
        model.addAttribute("mode","locality");
        model.addAttribute("reportType", "locality");
        return "dCBReport-search";
    }
    @RequestMapping(value = "/dCBReport/licenseNumberWise", method = RequestMethod.GET)
    public String licenseNumberWisesearch(final Model model) {
        model.addAttribute("mode",PROPERTY);
        model.addAttribute("reportType", PROPERTY);
        return "dCBReport-search";
    }
    @RequestMapping(value = "/dCBReportList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void search(final HttpServletRequest request, final HttpServletResponse response,
            final Model model) throws IOException {
        List<DCBReportResult> resultList = new ArrayList<DCBReportResult>();
        String licensenumber = "", mode = "", reportType = "";
        String[] boundaryId = null;
        final StringBuilder zones = new StringBuilder();
        if (request.getParameter("boundaryId[]") != null && !"".equals(request.getParameter("boundaryId[]")))
            boundaryId = request.getParameterValues("boundaryId[]");
        else if (request.getParameter("boundaryId") != null && !"".equals(request.getParameter("boundaryId")))
            boundaryId = request.getParameterValues("boundaryId");
        if (boundaryId != null)
            for (final String n : boundaryId) {
                if (zones.length() > 0)
                    zones.append(',');
                zones.append(n);
            }
        if (request.getParameter("mode") != null && !"".equals(request.getParameter("mode")))
            mode = request.getParameter("mode");
        if (request.getParameter("reportType") != null && !"".equals(request.getParameter("reportType")))
            reportType = request.getParameter("reportType");
        if (request.getParameter("licensenumber") != null && !"".equals(request.getParameter("licensenumber")))
            licensenumber = request.getParameter("licensenumber");
        final SQLQuery query = prepareQuery(zones.toString(), licensenumber, mode, reportType);
        resultList = query.list();
        String result = null;
        result = new StringBuilder("{ \"data\":").append(toJSON(resultList)).append("}").toString();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(result, response.getWriter());
    }

    private SQLQuery prepareQuery(final String paramList, final String licensenumber,  String mode,
            final String reportType) {
        StringBuilder query = new StringBuilder();
        final StringBuilder selectQry1 = new StringBuilder();
        final StringBuilder selectQry2 = new StringBuilder();
        StringBuilder fromQry = new StringBuilder();
        StringBuilder whereQry = new StringBuilder();
        final StringBuilder groupByQry = new StringBuilder();
       selectQry2
        .append("  cast(SUM(arr_demand) as bigint) AS arr_demand,cast(SUM(curr_demand) as bigint) AS curr_demand,cast(SUM(arr_coll) as bigint) AS arr_coll,cast(SUM(curr_coll) as bigint) AS curr_coll,"
                + "cast(SUM(arr_balance) as bigint) AS arr_balance,cast(SUM(curr_balance) as bigint) AS curr_balance ");
        fromQry = new StringBuilder(" from egtl_mv_dcb_view dcbinfo,eg_boundary boundary ");
       
        if (mode.equalsIgnoreCase(PROPERTY)) {
            selectQry1
            .append("select distinct dcbinfo.licenseNumber as licenseNumber ,cast(dcbinfo.licenseId as integer) as licenseid,dcbinfo.username as \"username\", ");
            fromQry = new StringBuilder(" from egtl_mv_dcb_view dcbinfo ");
            if(licensenumber !=null && !"".equals(licensenumber)){
               
            whereQry = whereQry.append(" where  dcbinfo.licenseNumber = '" + licensenumber.toUpperCase() + "'");
            }
            groupByQry.append("group by dcbinfo.licenseNumber,dcbinfo.licenseId,dcbinfo.username ");
            if(licensenumber !=null && !"".equals(licensenumber)){
                whereQry.append(" and ");
            }else{
                whereQry.append(" where ");
            }
            whereQry.append(" dcbinfo.licenseNumber is not null  ");
            if (paramList != null && !paramList.equalsIgnoreCase("") && reportType.equalsIgnoreCase("localityWise"))
                whereQry = whereQry.append(" and dcbinfo.locality in (" + paramList + ")");
          
        }
        
        query = selectQry1.append(selectQry2).append(fromQry).append(whereQry).append(groupByQry);
        final SQLQuery finalQuery = entityManager.unwrap(Session.class).createSQLQuery(query.toString());
        finalQuery.setResultTransformer(new AliasToBeanResultTransformer(DCBReportResult.class));
        return finalQuery;
    }

    private Object toJSON(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(DCBReportResult.class, new DCBReportHelperAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }

}
