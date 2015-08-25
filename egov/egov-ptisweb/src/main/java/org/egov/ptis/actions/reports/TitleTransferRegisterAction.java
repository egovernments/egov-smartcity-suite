package org.egov.ptis.actions.reports;

import static org.egov.ptis.constants.PropertyTaxConstants.ADMIN_HIERARCHY_TYPE;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.ptis.bean.PropertyWiseArrearInfo;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.validator.annotations.Validations;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Validations
@Results({ @Result(name = TitleTransferRegisterAction.SEARCH, location = "titleTransferRegister-search.jsp")})
public class TitleTransferRegisterAction extends BaseFormAction {
    
    private final Logger LOGGER = Logger.getLogger(getClass());
    public static final String SEARCH = "search";
    private Long zoneId;
    private Long wardId;
    private Long areaId;
    private String fromDate;
    private String toDate;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    public FinancialYearDAO financialYearDAO;
    private String finYearStartDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public Object getModel() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public void prepare() {
            LOGGER.debug("Entered into prepare method");
            super.prepare();
            List<Boundary> zoneList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Zone",ADMIN_HIERARCHY_TYPE);
            addDropdownData("zoneList", zoneList);
            LOGGER.debug("Zone id : " + zoneId + ", " + "Ward id : " + wardId);
            prepareWardDropDownData(zoneId != null, wardId != null);
            if (wardId == null || wardId.equals(-1)) {
                    addDropdownData("blockList", Collections.EMPTY_LIST);
            }
            prepareBlockDropDownData(wardId != null, areaId != null);
            LOGGER.debug("Exit from prepare method");
            CFinancialYear finyear=financialYearDAO.getFinancialYearByDate(new Date());
            if(finyear!=null)
                finYearStartDate=sdf.format(finyear.getStartingDate());
    }

    @SuppressWarnings("unchecked")
    private void prepareWardDropDownData(boolean zoneExists, boolean wardExists) {
            LOGGER.debug("Entered into prepareWardDropDownData method");
            LOGGER.debug("Zone Exists ? : " + zoneExists + ", " + "Ward Exists ? : " + wardExists);
            if (zoneExists && wardExists) {
                    List<Boundary> wardList = new ArrayList<Boundary>();
                    wardList = boundaryService.getActiveChildBoundariesByBoundaryId(getZoneId());
                    addDropdownData("wardList", wardList);
            } else {
                    addDropdownData("wardList", Collections.EMPTY_LIST);
            }
            LOGGER.debug("Exit from prepareWardDropDownData method");
    }
    
    @SuppressWarnings("unchecked")
    private void prepareBlockDropDownData(boolean wardExists, boolean blockExists) {
            LOGGER.debug("Entered into prepareBlockDropDownData method");
            LOGGER.debug("Ward Exists ? : " + wardExists + ", " + "Block Exists ? : " + blockExists);
            if (wardExists && blockExists) {
                    List<Boundary> blockList = new ArrayList<Boundary>();
                    blockList = boundaryService.getActiveChildBoundariesByBoundaryId(getWardId());
                    addDropdownData("blockList", blockList);
            } else {
                    addDropdownData("blockList", Collections.EMPTY_LIST);
            }
            LOGGER.debug("Exit from prepareWardDropDownData method");
    }

    @SkipValidation
    @Action(value = "/reports/titleTransferRegister-search")
    public String search() {
        fromDate=finYearStartDate;
        toDate=sdf.format(new Date());
        return SEARCH; 
    }
    
    @SuppressWarnings("unchecked")
    @Action(value = "/titleTransferRegister-getPropertyList")
    public void getPropertyList(){
        List<TitleTransferReportResult> resultList = new  ArrayList<TitleTransferReportResult>();
        String result = null;
        Query query = prepareQuery();
        resultList = prepareOutput(query.list());
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
        final Gson gson = gsonBuilder.registerTypeAdapter(TitleTransferReportResult.class,
                new TitleTransferReportHelperAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }
    
    
    private Query prepareQuery() {
        StringBuffer query = new StringBuffer(300);
        PropertyMutation pm=new PropertyMutation();
        String boundaryCond="";
        String boundaryWhrCond="";
       
        if (zoneId != null && zoneId!=-1) {
            boundaryCond=" and pi.zone.id= "+zoneId;
        }
        if(wardId != null && wardId!=-1){
            boundaryCond=boundaryCond+" and pi.ward.id= "+wardId;
        }
        if(areaId != null && areaId!=-1){
            boundaryCond=boundaryCond+" and pi.area.id= "+areaId;
        }
        if(boundaryCond!=""){
            boundaryWhrCond=",PropertyID pi where pm.basicProperty.id=pi.basicProperty.id "
                    + " and pm.state.value='"+PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVED+"' ";
        } else{
            boundaryWhrCond=" where pm.state.value='"+PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVED+"' ";
        }
        // Query that retrieves all the properties that has Transfer of owners details.
        query.append("select pm from PropertyMutation pm").append(boundaryWhrCond).append(boundaryCond);
        if(fromDate != null && !fromDate.isEmpty()){
            if(toDate!=null && !toDate.isEmpty())
                query.append(" and (cast(pm.createdDate as date)) between to_date('"+fromDate+"', 'DD/MM/YYYY') and to_date('"+toDate+"','DD/MM/YYYY') ");
            else
                query.append(" and (cast(pm.createdDate as date)) between to_date('"+fromDate+"', 'DD/MM/YYYY') and to_date('"+sdf.format(new Date())+"','DD/MM/YYYY')  "); 
        } 
        
        query.append(" order by pm.basicProperty.id,pm.mutationDate ");
        Query qry = getPersistenceService().getSession().createQuery(query.toString()); 
        return qry;
    }
    
    /**
     * @ Description : gets the property tax arrear amount for a property
     * @param basicPropId
     * @param finyear
     * @return
     */
    public BigDecimal getPropertyTaxDetails(Long basicPropId,CFinancialYear finyear) {
        List<Object> list = new ArrayList<Object>();
        
        String selectQuery = " select sum(amount) as amount from ("
                        + " select distinct inst.description,dd.amount as amount from egpt_basic_property bp, egpt_property prop, "
                        + " egpt_ptdemand ptd, eg_demand d, "
                        + " eg_demand_details dd, eg_demand_reason dr, eg_demand_reason_master drm, eg_installment_master inst "
                        + " where bp.id = prop.id_basic_property and prop.status = 'A' "
                        + " and prop.id = ptd.id_property and ptd.id_demand = d.id " + " and d.id = dd.id_demand "
                        + " and dd.id_demand_reason = dr.id and drm.id = dr.id_demand_reason_master "
                        + " and dr.id_installment = inst.id and bp.id =:basicPropId "
                        + " and inst.start_date between '"+finyear.getStartingDate()+"' and '"+finyear.getEndingDate()+"'"
                        + " and drm.code = '"+PropertyTaxConstants.GEN_TAX+"') as genTaxDtls";
        Query qry = getPersistenceService().getSession().createSQLQuery(selectQuery).setLong("basicPropId", basicPropId);
        list = qry.list();
        return (BigDecimal) list.get(0);
    }
 
    
    
    /**
     * @param propertyMutationList
     * @return
     */
    private List<TitleTransferReportResult> prepareOutput(List<PropertyMutation> propertyMutationList){
        List<TitleTransferReportResult> ttrFinalList = new LinkedList<TitleTransferReportResult>();
        CFinancialYear finyear=financialYearDAO.getFinancialYearByDate(new Date());
        List basicPropList = new ArrayList();
        TitleTransferReportResult ttrInfoTotal = null;
        TitleTransferReportResult titleTransferReportInfo = new TitleTransferReportResult();
        
        for (PropertyMutation propMutation : propertyMutationList) {
            // initially the block is executed
            if (basicPropList.size() == 0) {
                titleTransferReportInfo = PreparePropertyWiseInfo(propMutation,finyear);
                basicPropList.add(propMutation.getBasicProperty().getId());
                ttrInfoTotal=titleTransferReportInfo;
                ttrFinalList.add(ttrInfoTotal);
            }
            // executed for duplic basic property in list basicPropList
            else if (basicPropList.contains(propMutation.getBasicProperty().getId())) {
                titleTransferReportInfo = addPropertyWiseInfo(propMutation,finyear);
                ttrInfoTotal = titleTransferReportInfo;
                ttrFinalList.add(ttrInfoTotal);
            }
         // executed while adding unique basic property in list  basicPropList
            else if (!basicPropList.contains(propMutation.getBasicProperty().getId())) {
                titleTransferReportInfo = PreparePropertyWiseInfo(propMutation,finyear);
                basicPropList.add(propMutation.getBasicProperty().getId());
                ttrInfoTotal=titleTransferReportInfo;
                ttrFinalList.add(ttrInfoTotal);
            }
        }
        return ttrFinalList;
    }
    
    /**
     * @param propertyMutation
     * @param finyear
     * @return
     */
    private TitleTransferReportResult PreparePropertyWiseInfo(PropertyMutation propertyMutation,CFinancialYear finyear){
        TitleTransferReportResult ttrObj = new TitleTransferReportResult();
        String ownerName="";
        ttrObj.setAssessmentNo(propertyMutation.getBasicProperty().getUpicNo());
        ttrObj.setOwnerName(propertyMutation.getBasicProperty().getFullOwnerName());
        ttrObj.setDoorNo(propertyMutation.getBasicProperty().getAddress().getHouseNoBldgApt());
        ttrObj.setLocation(propertyMutation.getBasicProperty().getPropertyID().getLocality().getName());
        ttrObj.setPropertyTax(getPropertyTaxDetails(propertyMutation.getBasicProperty().getId(),finyear).toString());
        if(propertyMutation.getTransferorInfos()!=null && propertyMutation.getTransferorInfos().size()>0){
            for(User usr:propertyMutation.getTransferorInfos()){
                ownerName=ownerName+usr.getName()+",";
            }
            ttrObj.setOldTitle(ownerName.substring(0,ownerName.length()-1)); 
        }
        
        if(propertyMutation.getTransfereeInfos()!=null && propertyMutation.getTransfereeInfos().size()>0){
            ownerName="";
            for(User usr:propertyMutation.getTransfereeInfos()){
                ownerName=ownerName+usr.getName()+",";
            }
            ttrObj.setChangedTitle(ownerName.substring(0,ownerName.length()-1)); 
        }
        ttrObj.setDateOfTransfer(sdf.format(propertyMutation.getLastModifiedDate()));
        ttrObj.setCommissionerOrder("APPROVED");
        
        return ttrObj;
    }
    
    /**
     * @param propertyMutation
     * @param finyear
     * @return
     */
    private TitleTransferReportResult addPropertyWiseInfo(PropertyMutation propertyMutation,CFinancialYear finyear) {
        TitleTransferReportResult ttrObj = new TitleTransferReportResult();
        String ownerName="";
        ttrObj.setAssessmentNo("");
        ttrObj.setOwnerName("");
        ttrObj.setDoorNo("");
        ttrObj.setLocation("");
        ttrObj.setPropertyTax("");
        if(propertyMutation.getTransferorInfos()!=null && propertyMutation.getTransferorInfos().size()>0){
            for(User usr:propertyMutation.getTransferorInfos()){
                ownerName=ownerName+usr.getName()+",";
            }
            ttrObj.setOldTitle(ownerName.substring(0,ownerName.length()-1)); 
        }
        
        if(propertyMutation.getTransfereeInfos()!=null && propertyMutation.getTransfereeInfos().size()>0){
            ownerName="";
            for(User usr:propertyMutation.getTransfereeInfos()){
                ownerName=ownerName+usr.getName()+",";
            }
            ttrObj.setChangedTitle(ownerName.substring(0,ownerName.length()-1)); 
        }
        
       ttrObj.setDateOfTransfer(sdf.format(propertyMutation.getLastModifiedDate()));
       ttrObj.setCommissionerOrder("APPROVED");
        return ttrObj;
}
    
   
    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public Long getWardId() {
        return wardId;
    }

    public void setWardId(Long wardId) {
        this.wardId = wardId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFinYearStartDate() {
        return finYearStartDate;
    }

    public void setFinYearStartDate(String finYearStartDate) {
        this.finYearStartDate = finYearStartDate;
    }

}
