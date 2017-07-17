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

package org.egov.ptis.actions.reports;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.opensymphony.xwork2.validator.annotations.Validations;
import org.apache.commons.io.IOUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.ptis.domain.service.report.ReportService;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Validations
public class AjaxDCBReportAction extends BaseFormAction {
    /**
     *
     */
    private static final long serialVersionUID = -5523504056467935435L;
    private String mode;
    private Long boundaryId;
    public static final String ZONEWISE = "zone";
    public static final String WARDWISE = "ward";
    public static final String BLOCKWISE = "block";
    public static final String PROPERTY = "property";
    @Autowired
    private ReportService reportService;
    private String propTypes;
    private Boolean courtCase;

    @Override
    public Object getModel() {

        return null;
    }

    /**
     * Invoked from an ajax call to show boundary wise Demand, Collection and Balance
     */
    @SuppressWarnings("unchecked")
    @Action(value = "/ajaxDCBReport-getBoundaryWiseDCBList")
    public void getBoundaryWiseDCBList() throws IOException {
        List<DCBReportResult> resultList = new ArrayList<DCBReportResult>();
        final SQLQuery query = prepareQuery();
        resultList = query.list();
        // for converting resultList to JSON objects.
        // Write back the JSON Response.
        String result = new StringBuilder("{ \"data\":").append(toJSON(resultList, DCBReportResult.class,
                DCBReportHelperAdaptor.class)).append("}").toString();
        final HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(result, response.getWriter());
    }

    /**
     * @ Description - Returns query that retrieves zone/ward/block/propertywise Arrear, Current Demand and Collection Details
     * @return
     */
    public SQLQuery prepareQuery() {
        //To conver multi selected propertyTypes values(json stringify) into list
        List<String> propertyType=new Gson().fromJson(propTypes, new TypeToken<ArrayList<String>>() { 
        }.getType());
        final SQLQuery query = reportService.prepareQueryForDCBReport(boundaryId, mode,courtCase,propertyType);
        query.setResultTransformer(new AliasToBeanResultTransformer(DCBReportResult.class));
        return query; 
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public Long getBoundaryId() {
        return boundaryId;
    }

    public void setBoundaryId(final Long boundaryId) {
        this.boundaryId = boundaryId;
    }

    public Boolean getCourtCase() {
        return courtCase;
    }

    public void setCourtCase(Boolean courtCase) { 
        this.courtCase = courtCase;
    }

    public void setPropTypes(String propTypes) {
        this.propTypes = propTypes;
    }
}