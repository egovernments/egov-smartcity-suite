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

package org.egov.wtms.web.controller.reports;

import static org.egov.ptis.constants.PropertyTaxConstants.ADMIN_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.reports.entity.DCBReportView;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping(value = "/reports")
public class DCBReportController {
    
    public static final String ZONEWISE = "zone";
    public static final String WARDWISE = "ward";
    public static final String BLOCKWISE = "block";
    public static final String PROPERTY = "property";
    
    @PersistenceContext
    EntityManager entityManager;
    
    @Autowired(required = true)
    protected WaterConnectionDetailsService waterConnectionDetailsService;
    @Autowired
    private BoundaryService boundaryService;
    
    
    @ModelAttribute("dCBReportResult")
    public DCBReportResult dCBReportResultModel() {
        return new DCBReportResult();
    }
    
    @Autowired
    public @ModelAttribute("connectionTypes") Map<String, String> connectionTypes() {
        return waterConnectionDetailsService.getConnectionTypesMap();
    }
    @ModelAttribute("zones")
    public List<Boundary> zones() {
        return  boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ZONE,ADMIN_HIERARCHY_TYPE);
    }
    @RequestMapping(value="/dCBReport",method = RequestMethod.GET)
    public String search(final Model model) {
        model.addAttribute("mode", "zone");
        return "dCBReport-search";
    }

    @RequestMapping(value="/dCBReportList",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody void search(@ModelAttribute DCBReportResult dCBReportResult,final HttpServletRequest request,final HttpServletResponse response) throws IOException  {
        List<DCBReportResult> resultList = new  ArrayList<DCBReportResult>();
        Long zoneid = null ;
        String zones = "",mode = "",connectionType = "";
        if (request.getParameter("zoneid") != null && !"".equals(request.getParameter("zoneid")) && !"-1".equals(request.getParameter("zoneid")))
            zoneid = Long.valueOf(request.getParameter("zoneid"));
        if (request.getParameter("zones") != null && !"".equals(request.getParameter("zones")))
            zones = request.getParameter("zones");
        if (request.getParameter("connectionType") != null && !"".equals(request.getParameter("connectionType")))
            connectionType = request.getParameter("connectionType");
        if (request.getParameter("mode") != null && !"".equals(request.getParameter("mode")))
            mode = request.getParameter("mode");
        if(zoneid==null){
            zones = zones;
        }else{
            zones = zoneid.toString();
        }
        SQLQuery query = prepareQuery(zones,connectionType,mode);
        resultList = query.list();
        String result = null;
        result = new StringBuilder("{ \"data\":").append(toJSON(resultList)).append("}").toString();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(result, response.getWriter());
    }
    private SQLQuery prepareQuery(String paramList,String connectionType,String mode){
        String query ,whereQry = "",selectQry = "",groupByQry = "";
        query=" sum(arr_demand) as arr_demand, sum(curr_demand) as curr_demand, sum(arr_coll) as arr_coll,"
                + "sum(curr_coll) as curr_coll,sum(arr_balance) as arr_balance,sum(curr_balance) as curr_balance  from egwtr_mv_dcb_view dcbinfo,eg_boundary boundary ";
        //Conditions to Retrieve data based on selected boundary types
        if(!mode.equalsIgnoreCase(PROPERTY)){
            selectQry="select cast(dcbinfo.zoneid as integer) as \"zoneid\",boundary.name as \"boundaryName\", ";
            groupByQry=" group by dcbinfo.zoneid,boundary.name order by boundary.name";
        }
        if(mode.equalsIgnoreCase(ZONEWISE)){
            selectQry="select distinct dcbinfo.zoneid as zone,";
            groupByQry = " group by dcbinfo.zoneid "; 
            whereQry=" where dcbinfo.zoneid=boundary.id ";
            if(paramList!=null)
                whereQry=" and dcbinfo.zoneid in ("+paramList+")";
        } else if (mode.equalsIgnoreCase(WARDWISE)) {
            selectQry="select distinct dcbinfo.wardid as ward,";
            groupByQry = " group by dcbinfo.wardid ";
            whereQry=" where dcbinfo.wardid=boundary.id ";
            whereQry=" and dcbinfo.zoneid in ("+paramList+")";
        } else if(mode.equalsIgnoreCase(BLOCKWISE)){
            selectQry="select distinct dcbinfo.block as block,";   
            groupByQry = " group by dcbinfo.block ";
            whereQry=" where dcbinfo.block=boundary.id ";
            whereQry=" and dcbinfo.wardid in ("+paramList+")";
        } else if(mode.equalsIgnoreCase(PROPERTY)){
            selectQry="select distinct dcbinfo.uscno as uscno,";   
            groupByQry = "group by dcbinfo.uscno ";  
            whereQry=" where dcbinfo.block in ("+paramList+")";
        }   
        if(!connectionType.equalsIgnoreCase("")){
            whereQry = " and dcbinfo.connectiontype = '"+connectionType+"'";
        }
        query = selectQry + query + whereQry+ groupByQry ;
        SQLQuery finalQuery = entityManager.unwrap(Session.class).createSQLQuery(query.toString());
        finalQuery.setResultTransformer(new AliasToBeanResultTransformer(DCBReportResult.class));
        return finalQuery;
    }
    private Object toJSON(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(DCBReportResult.class,
                new DCBReportHelperAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }
    

}
