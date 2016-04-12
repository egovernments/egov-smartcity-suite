/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.ptis.actions.reports;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_OF_PROPERTY_FOR_DEFAULTERS_REPORT;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Installment;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.ptis.bean.DefaultersInfo;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.InstDmdCollMaterializeView;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Namespace("/reports")
@Results({ @Result(name = DefaultersReportAction.RESULT_SEARCH, location = "reports/defaultersReport-search.jsp") })
public class DefaultersReportAction extends BaseFormAction {

    private static final Logger LOGGER = Logger.getLogger(DefaultersReportAction.class);
    public static final String RESULT_SEARCH = "search";
    private Long wardId;
    private String fromDemand;
    private String toDemand;
    private Integer limit;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    public PropertyTaxUtil propertyTaxUtil;
    private Map<String, String> ownerShipMap;
    private String ownerShipType;

    @Override
    public Object getModel() {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void prepare() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into prepare method");
        super.prepare();
        final List<Boundary> wardList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Ward",
                REVENUE_HIERARCHY_TYPE);
        addDropdownData("wardList", wardList);
        addDropdownData("limitList", buildLimitList());
        setOwnerShipMap(OWNERSHIP_OF_PROPERTY_FOR_DEFAULTERS_REPORT);
    }

    @SkipValidation
    @Action(value = "/defaultersReport-search")
    public String search() {
        return RESULT_SEARCH;
    }

    private List<Integer> buildLimitList() {
        List<Integer> limitList = new ArrayList<Integer>();
        limitList.add(10);
        limitList.add(50);
        limitList.add(100);
        limitList.add(500);
        limitList.add(1000);
        return limitList;
    }

    /**
     * Invoked by Defaulters Report. Shows list of defaulters
     */
    @SuppressWarnings("unchecked")
    @Action(value = "/defaultersReport-getDefaultersList")
    public void getDefaultersList() {
        List<DefaultersInfo> resultList = new ArrayList<DefaultersInfo>();
        String result = null;
        final Query query = propertyTaxUtil.prepareQueryforDefaultersReport(wardId, fromDemand, toDemand, limit,ownerShipType);
        resultList = prepareOutput( (List<PropertyMaterlizeView>)query.list());
        // for converting resultList to JSON objects.
        // Write back the JSON Response.
        result = new StringBuilder("{ \"data\":").append(toJSON(resultList)).append("}").toString();
        final HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            IOUtils.write(result, response.getWriter());
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param object
     * @return
     */
    private Object toJSON(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(DefaultersInfo.class, new DefaultersReportHelperAdaptor())
                .create();
        final String json = gson.toJson(object);
        return json;
    }

    /**
     * @param propertyViewList
     * @return List of Defaulters
     */
    private List<DefaultersInfo> prepareOutput(final List<PropertyMaterlizeView> propertyViewList) {
        List<DefaultersInfo> defaultersList = new ArrayList<DefaultersInfo>();
        DefaultersInfo defaultersInfo = null;
        BigDecimal totalDue = BigDecimal.ZERO;
        int count = 0;
        Installment curInstallment = propertyTaxUtil.getCurrentInstallment();
        for (final PropertyMaterlizeView propView : propertyViewList) {
            defaultersInfo = new DefaultersInfo();
            totalDue = BigDecimal.ZERO;
            count++;
            defaultersInfo.setSlNo(count);
            defaultersInfo.setAssessmentNo(propView.getPropertyId());
            defaultersInfo.setOwnerName(propView.getOwnerName().contains(",") ? propView.getOwnerName().replace(",",
                    " & ") : propView.getOwnerName());
            defaultersInfo.setWardName(propView.getWard().getName());
            defaultersInfo.setHouseNo(propView.getHouseNo());
            defaultersInfo.setLocality(propView.getLocality().getName());
            defaultersInfo.setMobileNumber((StringUtils.isNotBlank(propView.getMobileNumber()) ? propView
                    .getMobileNumber() : "NA"));
            defaultersInfo.setArrearsDue(propView.getAggrArrDmd().subtract(propView.getAggrArrColl()));
            defaultersInfo.setCurrentDue((propView.getAggrCurrFirstHalfDmd().add(propView.getAggrCurrSecondHalfDmd())).subtract((propView.getAggrCurrFirstHalfColl().add(propView.getAggrCurrSecondHalfColl()))));
            defaultersInfo
                    .setAggrArrearPenalyDue((propView.getAggrArrearPenaly() != null ? propView.getAggrArrearPenaly() : ZERO)
                            .subtract(propView.getAggrArrearPenalyColl() != null ? propView.getAggrArrearPenalyColl() : ZERO));
            defaultersInfo.setAggrCurrPenalyDue((propView.getAggrCurrFirstHalfPenaly() != null ? propView.getAggrCurrFirstHalfPenaly() : ZERO)
                    .subtract((propView.getAggrCurrFirstHalfPenalyColl() != null ? propView.getAggrCurrFirstHalfPenalyColl() : ZERO)));
            totalDue = defaultersInfo.getArrearsDue().add(defaultersInfo.getCurrentDue())
                    .add(defaultersInfo.getAggrArrearPenalyDue()).add(defaultersInfo.getAggrCurrPenalyDue());
            defaultersInfo.setTotalDue(totalDue);
            if(propView.getInstDmdColl().size()!=0 && !propView.getInstDmdColl().isEmpty()){
                defaultersInfo.setArrearsFrmInstallment(propView.getInstDmdColl().iterator().next().getInstallment().getDescription());
                final Iterator itr = propView.getInstDmdColl().iterator();
                InstDmdCollMaterializeView idc = new InstDmdCollMaterializeView();
                InstDmdCollMaterializeView lastElement = new InstDmdCollMaterializeView(); 
                while(itr.hasNext()) {
                    idc =(InstDmdCollMaterializeView) itr.next();
                    if(!idc.getInstallment().equals(curInstallment))
                        lastElement = idc;
                }
                if(lastElement!=null && lastElement.getInstallment()!=null)  
                    defaultersInfo.setArrearsToInstallment(lastElement.getInstallment().getDescription());
            }
           defaultersList.add(defaultersInfo);   
        } 

        return defaultersList;

    }

    public Long getWardId() {
        return wardId;
    }

    public void setWardId(Long wardId) {
        this.wardId = wardId;
    }

    public String getFromDemand() {
        return fromDemand;
    }

    public void setFromDemand(String fromDemand) {
        this.fromDemand = fromDemand;
    }

    public String getToDemand() {
        return toDemand;
    }

    public void setToDemand(String toDemand) {
        this.toDemand = toDemand;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Map<String, String> getOwnerShipMap() {
        return ownerShipMap;
    }

    public void setOwnerShipMap(Map<String, String> ownerShipMap) {
        this.ownerShipMap = ownerShipMap;
    }

    public String getOwnerShipType() {
        return ownerShipType;
    }

    public void setOwnerShipType(String ownerShipType) {
        this.ownerShipType = ownerShipType;
    }

}
