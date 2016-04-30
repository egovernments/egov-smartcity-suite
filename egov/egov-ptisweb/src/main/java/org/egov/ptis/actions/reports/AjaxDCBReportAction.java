package org.egov.ptis.actions.reports;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.validator.annotations.Validations;
import org.apache.commons.io.IOUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private PropertyTaxUtil propertyTaxUtil;
    private String propTypes;
    private Boolean courtCase;

    @Override
    public Object getModel() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Invoked from an ajax call to show boundary wise Demand, Collection and Balance
     */
    @SuppressWarnings("unchecked")
    @Action(value = "/ajaxDCBReport-getBoundaryWiseDCBList")
    public void getBoundaryWiseDCBList() {
        List<DCBReportResult> resultList = new ArrayList<DCBReportResult>();
        String result = null;
        final SQLQuery query = prepareQuery();
        resultList = query.list();
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
        final Gson gson = gsonBuilder.registerTypeAdapter(DCBReportResult.class,
                new DCBReportHelperAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }

    /**
     * @ Description - Returns query that retrieves zone/ward/block/propertywise Arrear, Current Demand and Collection Details
     * @return
     */
    public SQLQuery prepareQuery() {
        //To conver multi selected propertyTypes values(json stringify) into list
        List<String> propertyType=new Gson().fromJson(propTypes, new TypeToken<ArrayList<String>>() { 
        }.getType());
        final SQLQuery query = propertyTaxUtil.prepareQueryForDCBReport(boundaryId, mode,courtCase,propertyType);
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