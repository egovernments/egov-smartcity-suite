package org.egov.ptis.actions.reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.http.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.validator.annotations.Validations;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Validations
public class AjaxDCBReportAction extends BaseFormAction {
    private String mode; 
    private Long boundaryId;
    public static final String ZONEWISE = "zone";
    public static final String WARDWISE = "ward";
    public static final String BLOCKWISE = "block";
    public static final String PROPERTY = "property";
    
    @Override
    public Object getModel() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @SuppressWarnings("unchecked")
    @Action(value = "/ajaxDCBReport-getBoundaryWiseDCBList")
    public void getBoundaryWiseDCBList(){
        List<DCBReportResult> resultList = new  ArrayList<DCBReportResult>();
        String result = null;
        SQLQuery query = prepareQuery();
        resultList = query.list();
        // for converting resultList to JSON objects. 
        // Write back the JSON Response.
        result = new StringBuilder("{ \"data\":").append(toJSON(resultList)).append("}").toString();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            IOUtils.write(result, response.getWriter());
        } catch (IOException e) {
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
     *   Final Query Form : select boundary,arrear,current from 
     *                          (select boundary,arrear,0 as collection group by boundary 
     *                           union 
     *                           select boundary,0 as arrear, collection group by boundary) group by boundary
     * @return
     */
    public SQLQuery prepareQuery() {
        StringBuffer queryStr = new StringBuffer("");
        StringBuffer unionQueryStr = new StringBuffer("");
        String arrear_innerCommonQry0 = "", arrear_innerCommonQry1 = "", current_innerCommonQry0 = "", current_innerCommonQry1 = "";
        String finalCommonQry = "", finalSelectQry = "", finalGrpQry = "",finalWhereQry = "",finalFrmQry = "";
        String innerSelectQry0 = "", innerSelectQry1 = "",arrearGroupBy = "", whereQry="",collGroupBy = "",orderBy = "";
        Long param = null;
              
        if(boundaryId!=-1 && boundaryId != null){
            param=boundaryId;
        }
        // To retreive Arrear Demand and Collection Details
        arrear_innerCommonQry0="idc.* from egpt_mv_inst_dem_coll idc, egpt_mv_propertyinfo pi,  eg_installment_master im "
                + "where idc.id_basic_property=pi.basicpropertyid and im.id=idc.id_installment "
                + "and im.start_date not between (select STARTINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE) "
                + "and  (select ENDINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE)";
        
        arrear_innerCommonQry1="sum(GeneralTax) as arrearGT, sum(LibCessTax) as arrearLC, sum(EduCessTax) as arrearEC,"
                + "sum(UnauthPenaltyTax) as arrearUPT,sum(PenaltyFinesTax) as arrearPFT,sum(SewTax) as arrearST,"
                + "sum(VacantLandTax) as arrearVLT,sum(PubSerChrgTax) as arrearPSCT,sum(GeneralTaxColl) as arrearGTColl, "
                + "sum(LibCessTaxColl) as arrearLCColl, sum(EduCessTaxColl) as arrearECColl,sum(UnauthPenaltyTaxColl) as arrearUPTColl,"
                + "sum(PenaltyFinesTaxColl) as arrearPFTColl,sum(SewTaxColl) as arrearSTColl,"
                + "sum(VacantLandTaxColl) as arrearVLTColl,sum(PubSerChrgTaxColl) as arrearPSCTColl,"
                + "0 as curGT, 0 as curLC, 0 as curEC,0 as curUPT,0 as curPFT,0 as curST,"
                + "0 as curVLT,0 as curPSCT,0 as curGTColl,0 as curLCColl,0 as curECColl,0 as curUPTColl,"
                + "0 as curPFTColl,0 as curSTColl, 0 as curVLTColl,0 as curPSCTColl from (";
        
        // To retreive Current Demand and Collection Details
        current_innerCommonQry0="idc.* from egpt_mv_inst_dem_coll idc, egpt_mv_propertyinfo pi,  eg_installment_master im "
                + "where idc.id_basic_property=pi.basicpropertyid and im.id=idc.id_installment "
                + "and im.start_date between (select STARTINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE) "
                + "and  (select ENDINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE)";
        
        current_innerCommonQry1="0 as arrearGT, 0 as arrearLC, 0 as arrearEC,0 as arrearUPT,0 as arrearPFT,0 as arrearST,"
                + "0 as arrearVLT,0 as arrearPSCT,0 as arrearGTColl,0 as arrearLCColl,0 as arrearECColl,0 as arrearUPTColl,"
                + "0 as arrearPFTColl,0 as arrearSTColl, 0 as arrearVLTColl,0 as arrearPSCTColl,"
                + "sum(GeneralTax) as curGT, sum(LibCessTax) as curLC, sum(EduCessTax) as curEC,"
                + "sum(UnauthPenaltyTax) as curUPT,sum(PenaltyFinesTax) as curPFT,sum(SewTax) as curST,"
                + "sum(VacantLandTax) as curVLT,sum(PubSerChrgTax) as curPSCT,sum(GeneralTaxColl) as curGTColl, "
                + "sum(LibCessTaxColl) as curLCColl, sum(EduCessTaxColl) as curECColl,sum(UnauthPenaltyTaxColl) as curUPTColl,"
                + "sum(PenaltyFinesTaxColl) as curPFTColl,sum(SewTaxColl) as curSTColl,"
                + "sum(VacantLandTaxColl) as curVLTColl,sum(PubSerChrgTaxColl) as curPSCTColl from (";
        
        // Final query that retreives both Arrear and Current details from the other two inner queries
        finalCommonQry = "cast(sum(arrearGT) AS numeric) as \"dmnd_arrearPT\", cast(sum(arrearLC)  AS numeric) as \"dmnd_arrearLC\", cast(sum(arrearEC) AS numeric) as \"dmnd_arrearEC\","
                + "cast(sum(arrearUPT) AS numeric) as \"dmnd_arrearUPT\",cast(sum(arrearPFT) AS numeric) as \"dmnd_arrearPFT\",cast(sum(arrearST) AS numeric) as \"dmnd_arrearST\","
                + "cast(sum(arrearVLT) AS numeric) as \"dmnd_arrearVLT\",cast(sum(arrearPSCT) AS numeric) as \"dmnd_arrearPSCT\",cast(SUM(arrearGTColl) AS numeric)  AS \"clctn_arrearPT\", "
                + "cast(sum(arrearLCColl) AS numeric) as \"clctn_arrearLC\", cast(sum(arrearECColl) AS numeric) as \"clctn_arrearEC\",cast(sum(arrearUPTColl) AS numeric) as \"clctn_arrearUPT\","
                + "cast(sum(arrearPFTColl) AS numeric) as \"clctn_arrearPFT\",cast(sum(arrearSTColl) AS numeric) as \"clctn_arrearST\","
                + "cast(sum(arrearVLTColl) AS numeric) as \"clctn_arrearVLT\",cast(sum(arrearPSCTColl) AS numeric) as \"clctn_arrearPSCT\","
                + "cast(sum(curGT) AS numeric) as \"dmnd_currentPT\", cast(sum(curLC) AS numeric) as \"dmnd_currentLC\", cast(sum(curEC) AS numeric) as \"dmnd_currentEC\","
                + "cast(sum(curUPT) AS numeric) as \"dmnd_currentUPT\",cast(sum(curUPT) AS numeric) as \"dmnd_currentPFT\",cast(sum(curST) AS numeric) as \"dmnd_currentST\","
                + "cast(sum(curVLT) AS numeric) as \"dmnd_currentVLT\",CAST(sum(curPSCT) AS numeric) as \"dmnd_currentPSCT\",CAST(sum(curGTColl) AS numeric) as \"clctn_currentPT\", "
                + "cast(sum(curLCColl) AS numeric) as \"clctn_currentLC\", cast(sum(curECColl) AS numeric) as \"clctn_currentEC\",cast(sum(curUPTColl) AS numeric) as \"clctn_currentUPT\","
                + "cast(sum(curPFTColl) AS numeric) as \"clctn_currentPFT\",cast(sum(curSTColl) AS numeric) as \"clctn_currentST\","
                + "cast(sum(curVLTColl) AS numeric) as \"clctn_currentVLT\",cast(sum(curPSCTColl) AS numeric) as \"clctn_currentPSCT\" from ("; 
        
        //Conditions to Retrieve data based on selected boundary types
        if(!mode.equalsIgnoreCase(PROPERTY)){
            finalSelectQry="select cast(id as integer) as \"boundaryId\",boundary.name as \"boundaryName\", ";
            finalGrpQry=" group by boundary.id,boundary.name order by boundary.name";
            finalFrmQry=" )as dcbinfo,eg_boundary boundary ";
        }
        if(mode.equalsIgnoreCase(ZONEWISE)){
            innerSelectQry0="select distinct pi.zoneid as zone,";
            innerSelectQry1="select zone as zone,";
            arrearGroupBy = ") as arrear  group by zone ";
            collGroupBy = ") as collection  group by zone "; 
            if(param!=0)
                whereQry=" and pi.zoneid = "+param;
            finalWhereQry=" where dcbinfo.zone=boundary.id ";
        } else if (mode.equalsIgnoreCase(WARDWISE)) {
            innerSelectQry0="select distinct pi.wardid as ward,";
            innerSelectQry1="select ward as ward,";
            arrearGroupBy = ") as arrear group by ward ";
            collGroupBy = ") as collection  group by ward ";
            whereQry=" and pi.zoneid = "+param;
            finalWhereQry=" where dcbinfo.ward=boundary.id ";
        } else if(mode.equalsIgnoreCase(BLOCKWISE)){
            innerSelectQry0="select distinct pi.blockid as block,";   
            innerSelectQry1="select block as block,";
            arrearGroupBy = ") as arrear group by block ";
            collGroupBy = ") as collection  group by block ";
            whereQry=" and pi.wardid = "+param;
            finalWhereQry=" where dcbinfo.block=boundary.id "; 
        } else if(mode.equalsIgnoreCase(PROPERTY)){
            innerSelectQry0="select distinct pi.upicno as upicno,";   
            innerSelectQry1="select upicno as upicno,";
            arrearGroupBy = ") as arrear group by upicno ";
            collGroupBy = ") as collection  group by upicno ";  
            whereQry=" and pi.blockid = "+param;
            finalSelectQry="select COALESCE(upicno,null,'',upicno) as \"assessmentNo\", "; 
            finalFrmQry=" )as dcbinfo ";
            finalWhereQry="";
            finalGrpQry=" group by dcbinfo.upicno order by dcbinfo.upicno ";   
        }   
        //  Arrear Demand query union Current Demand query
        unionQueryStr
            .append(innerSelectQry1)
            .append(arrear_innerCommonQry1)
            .append(innerSelectQry0)
            .append(arrear_innerCommonQry0)
            .append(whereQry)
            .append(arrearGroupBy)
            .append(" union ")
            .append(innerSelectQry1)
            .append(current_innerCommonQry1)
            .append(innerSelectQry0)
            .append(current_innerCommonQry0)
            .append(whereQry)
            .append(collGroupBy);
        // Final Query : Retrieves arrear and current for the selected boundary.
        queryStr
            .append(finalSelectQry)
            .append(finalCommonQry)
            .append(unionQueryStr)
            .append(finalFrmQry)
            .append(finalWhereQry)
            .append(finalGrpQry);
        
        SQLQuery query = persistenceService.getSession().createSQLQuery(queryStr.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(DCBReportResult.class));
        return query; 
    }
    
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Long getBoundaryId() {
        return boundaryId;
    }

    public void setBoundaryId(Long boundaryId) {
        this.boundaryId = boundaryId;
    }
}