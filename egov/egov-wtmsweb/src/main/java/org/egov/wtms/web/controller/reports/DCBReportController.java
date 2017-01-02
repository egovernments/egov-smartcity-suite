/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.wtms.web.controller.reports;

import org.apache.commons.io.IOUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.reports.entity.DCBReportHelperAdaptor;
import org.egov.wtms.reports.entity.DCBReportResult;
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
import java.util.Map;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.egov.ptis.constants.PropertyTaxConstants.BLOCK;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCALITY;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCATION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;

@Controller
@RequestMapping(value = "/reports")
public class DCBReportController {

    public static final String ZONEWISE = "zone";
    public static final String WARDWISE = "ward";
    public static final String BLOCKWISE = "block";
    public static final String LOCALITYWISE = "locality";
    public static final String PROPERTY = "property";

    @PersistenceContext
    EntityManager entityManager;

    @Autowired(required = true)
    protected WaterConnectionDetailsService waterConnectionDetailsService;
    @Autowired
    private BoundaryService boundaryService;
    private final DCBReportResult dCBReportResult = new DCBReportResult();

    @ModelAttribute("dCBReportResult")
    public DCBReportResult dCBReportResultModel() {
        return dCBReportResult;
    }

    public @ModelAttribute("connectionTypes") Map<String, String> connectionTypes() {
        return waterConnectionDetailsService.getConnectionTypesMap();
    }

    @ModelAttribute("zones")
    public List<Boundary> zones() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ZONE,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute("wards")
    public List<Boundary> wards() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute("blocks")
    public List<Boundary> blocks() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(BLOCK,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute("localitys")
    public List<Boundary> localitys() {
        return boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(LOCALITY, LOCATION_HIERARCHY_TYPE);
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
        model.addAttribute("mode", "locality");
        model.addAttribute("reportType", "localityWise");
        return "dCBReport-search";
    }

    @RequestMapping(value = "/dCBReportList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void search(final HttpServletRequest request, final HttpServletResponse response,
            final Model model) throws IOException {
        List<DCBReportResult> resultList = new ArrayList<DCBReportResult>();
        String connectionType = "", mode = "", reportType = "";
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
        if (request.getParameter("connectionType") != null && !"".equals(request.getParameter("connectionType")))
            connectionType = request.getParameter("connectionType");
        if (request.getParameter("mode") != null && !"".equals(request.getParameter("mode")))
            mode = request.getParameter("mode");
        if (request.getParameter("reportType") != null && !"".equals(request.getParameter("reportType")))
            reportType = request.getParameter("reportType");
        final SQLQuery query = prepareQuery(zones.toString(), connectionType, mode, reportType);
        resultList = query.list();
        String result = null;
        result = new StringBuilder("{ \"data\":").append(toJSON(resultList, DCBReportResult.class, DCBReportHelperAdaptor.class)).append("}").toString();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(result, response.getWriter());    
    }

    private SQLQuery prepareQuery(final String paramList, final String connectionType, final String mode,
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
        fromQry = new StringBuilder(" from egwtr_mv_dcb_view dcbinfo,eg_boundary boundary ");
        if (mode.equalsIgnoreCase(ZONEWISE)) {
            selectQry1
            .append("select  distinct cast(dcbinfo.zoneid as integer) as \"zoneid\",boundary.name as \"boundaryName\", count(hscno) as countofconsumerno,");
            groupByQry.append(" group by dcbinfo.zoneid,boundary.name order by boundary.name");
            whereQry.append(" where dcbinfo.zoneid=boundary.id ");
            if (paramList != null && !paramList.equalsIgnoreCase(""))
                whereQry = whereQry.append(" and dcbinfo.zoneid in (" + paramList + ")");
        } else if (mode.equalsIgnoreCase(WARDWISE)) {
            selectQry1
            .append("select distinct cast(dcbinfo.wardid as integer) as \"wardid\",boundary.name as \"boundaryName\",count(hscno) as countofconsumerno, ");
            groupByQry.append(" group by dcbinfo.wardid,boundary.name order by boundary.name");
            whereQry.append(" where dcbinfo.wardid=boundary.id ");
            if (paramList != null && !paramList.equalsIgnoreCase("") && reportType.equalsIgnoreCase("wardWise"))
                whereQry = whereQry.append(" and dcbinfo.wardid in (" + paramList + ")");
            if (paramList != null && !paramList.equalsIgnoreCase("") && !reportType.equalsIgnoreCase("wardWise"))
                whereQry = whereQry.append(" and dcbinfo.zoneid in (" + paramList + ")");
        } else if (mode.equalsIgnoreCase(BLOCKWISE)) {
            selectQry1
            .append("select  distinct cast(dcbinfo.block as integer) as \"wardid\",boundary.name as \"boundaryName\", count(hscno) as countofconsumerno,");
            groupByQry.append(" group by dcbinfo.block,boundary.name order by boundary.name");
            whereQry.append(" where dcbinfo.block=boundary.id ");
            if (paramList != null && !paramList.equalsIgnoreCase("") && reportType.equalsIgnoreCase("blockWise"))
                whereQry = whereQry.append(" and dcbinfo.block in (" + paramList + ")");
            if (paramList != null && !paramList.equalsIgnoreCase("") && !reportType.equalsIgnoreCase("blockWise"))
                whereQry = whereQry.append(" and dcbinfo.wardid in (" + paramList + ")");
        } else if (mode.equalsIgnoreCase(LOCALITYWISE)) {
            selectQry1
            .append("select  distinct cast(dcbinfo.locality as integer) as \"locality\",boundary.name as \"boundaryName\",dcbinfo.username as \"username\", count(hscno) as countofconsumerno, ");
            groupByQry.append(" group by dcbinfo.locality,boundary.name,dcbinfo.username order by boundary.name");
            whereQry.append(" where dcbinfo.locality=boundary.id and dcbinfo.locality in (" + paramList + ")");
        } else if (mode.equalsIgnoreCase(PROPERTY)) {
            selectQry1
            .append("select distinct dcbinfo.hscno as hscno,dcbinfo.propertyid as \"propertyid\" ,dcbinfo.username as \"username\", ");
            fromQry = new StringBuilder(" from egwtr_mv_dcb_view dcbinfo ");
            groupByQry.append("group by dcbinfo.hscno,dcbinfo.propertyid,dcbinfo.username ");
            whereQry.append(" where dcbinfo.hscno is not null  ");
            if (paramList != null && !paramList.equalsIgnoreCase("") && reportType.equalsIgnoreCase("localityWise"))
                whereQry = whereQry.append(" and dcbinfo.locality in (" + paramList + ")");
            else
                whereQry = whereQry.append(" and dcbinfo.block in (" + paramList + ")");
        }
        if (!connectionType.equalsIgnoreCase(""))
            whereQry.append(" and dcbinfo.connectiontype = '" + connectionType + "'");
        whereQry.append(" and dcbinfo.connectionstatus = 'ACTIVE'");
        query = selectQry1.append(selectQry2).append(fromQry).append(whereQry).append(groupByQry);
        final SQLQuery finalQuery = entityManager.unwrap(Session.class).createSQLQuery(query.toString());
        finalQuery.setResultTransformer(new AliasToBeanResultTransformer(DCBReportResult.class));
        return finalQuery;
    }
}
