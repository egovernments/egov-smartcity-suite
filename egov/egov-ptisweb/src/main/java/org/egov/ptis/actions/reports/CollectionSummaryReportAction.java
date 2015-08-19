package org.egov.ptis.actions.reports;

import static java.math.BigDecimal.ZERO;
import static org.egov.infra.web.struts.actions.BaseFormAction.VIEW;
import static org.egov.ptis.constants.PropertyTaxConstants.ADMIN_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.COLL_MODES_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCATION_HIERARCHY_TYPE;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.web.utils.EgovPaginatedList;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.infstr.utils.NumberUtil;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.CollectionSummary;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.validator.annotations.Validations;
@SuppressWarnings("serial")
@ParentPackage("egov")
@Validations
@Results({ @Result(name = VIEW, location = "collectionSummaryReport-view.jsp")})
public class CollectionSummaryReportAction extends SearchFormAction {
        private final Logger LOGGER = Logger.getLogger(getClass());
        private String mode;
        private Map<Long, String> zoneBndryMap;
        private Map<Long, String> wardBndryMap;
        private Map<Long, String> blockBndryMap;
        private Map<Long, String> localityBndryMap;
        private Map<Character, String> collectionModesMap;
        private String fromDate;
        private String toDate;
        private String boundaryId;
        private String collMode;
        private String transMode;
        @Autowired
        public PropertyTaxUtil propertyTaxUtil;
        @Autowired
        public FinancialYearDAO financialYearDAO;
        private List<Map<String, Object>> resultList;
        private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        public static final String ZONEWISE = "zoneWise";
        public static final String WARDWISE = "wardWise";
        public static final String BLOCKWISE = "blockWise";
        public static final String LOCALITYWISE = "localityWise";
        private Boolean srchFlag = Boolean.FALSE;
        private String dateSelected;
        private static final String CURR_DATE = "currentDate";
        @Autowired
        private BoundaryService boundaryService;
        private String finYearStartDate;
        private EgovPaginatedList paginatedList;
        
        BigDecimal taxAmount = ZERO,totTaxAmt = ZERO,arrearTaxAmount = ZERO,totArrearTaxAmt = ZERO,penaltyAmount = ZERO,totPenaltyAmt = ZERO;
        BigDecimal arrearPenaltyAmount = ZERO,totArrearPenaltyAmt = ZERO,libCessAmount = ZERO,totLibCessAmt = ZERO,arrearLibCessAmount = ZERO;
        BigDecimal totArrearLibCessAmt = ZERO,grandTotal = ZERO;
        Long prevZone = null,prevWard = null, prevBlock = null, prevLocality = null;
        
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
        @SuppressWarnings("unchecked")
        public void prepare() {
                LOGGER.debug("Entered into prepare method");
                setZoneBndryMap(CommonServices.getFormattedBndryMap(boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Zone",ADMIN_HIERARCHY_TYPE)));
                setWardBndryMap(CommonServices.getFormattedBndryMap(boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Ward",ADMIN_HIERARCHY_TYPE)));
                setBlockBndryMap(CommonServices.getFormattedBndryMap(boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Block",ADMIN_HIERARCHY_TYPE)));
                setLocalityBndryMap(CommonServices.getFormattedBndryMap(boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Locality",LOCATION_HIERARCHY_TYPE)));
                addDropdownData("instrumentTypeList", propertyTaxUtil.prepareInstrumentTypeList());  
                setCollectionModesMap(COLL_MODES_MAP);
                CFinancialYear finyear=financialYearDAO.getFinancialYearByDate(new Date());
                if(finyear!=null)
                    finYearStartDate=sdf.format(finyear.getStartingDate());
                LOGGER.debug("Exit from prepare method");
        }
	
	@SkipValidation
        @Action(value = "/reports/collectionSummaryReport-zoneWise")
        public String zoneWise() {
                fromDate=finYearStartDate;
                toDate=sdf.format(new Date());
	        setMode("zoneWise");
                return VIEW;
        }
	
	
	@SkipValidation
        @Action(value = "/reports/collectionSummaryReport-wardWise")
        public String wardWise() {
                fromDate=finYearStartDate;
                toDate=sdf.format(new Date());
                setMode("wardWise");
                return VIEW;
        }
	
	
	@SkipValidation
        @Action(value = "/reports/collectionSummaryReport-blockWise")
        public String blockWise() {
                fromDate=finYearStartDate;
                toDate=sdf.format(new Date());
                setMode("blockWise");
                return VIEW;
        }
	
	@SkipValidation
        @Action(value = "/reports/collectionSummaryReport-localityWise")
        public String localityWise() {
                fromDate=finYearStartDate;
                toDate=sdf.format(new Date());
                setMode("localityWise");
                return VIEW;
        }
	
	
	@Override
        public void validate() {
                LOGGER.debug("Entered into validate method");
                try {
                    Date date1 = sdf.parse(fromDate);
                    Date date2 = sdf.parse(toDate);
                    Date date3 = sdf.parse(finYearStartDate);
                    
                    if(date1.compareTo(date3)<0){
                        addActionError(getText("collectionRep.fromdate.validate",new String[] {finYearStartDate}));
                        srchFlag = Boolean.FALSE;
                    }
                    if(date1.compareTo(date2)>0){
                        addActionError(getText("collectionRep.todate.validate"));
                        srchFlag = Boolean.FALSE;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                LOGGER.debug("Exit from validate method");
        }
	
	@SuppressWarnings("unchecked")
        @ValidationErrorPage(value = "view")
        @Action(value = "/reports/collectionSummaryReport-list")
        public String list() throws ParseException {
            LOGGER.debug("Entered into list method");
            setPageSize(30);
            super.search();
            setResultList(prepareBndryWiseResultList());
            return VIEW; 
        }
	
	  @Override
	    public SearchQuery prepareQuery(String sortField, String sortOrder) {
	            String fromDateLocal = "";
	            String toDateLocal = "";
	            String srchQryStr="", countQry = "", baseQry = "", orderbyQry="";
                    List<Object> paramList = new ArrayList<Object>();
	            try {
	                    String currDate = sdf.format(new Date());
	                    srchFlag = Boolean.TRUE;
	                    if (currDate.equals((fromDate)) || currDate.equals(toDate)) {
	                            dateSelected = CURR_DATE;
	                    }
	                    if (ZONEWISE.equalsIgnoreCase(mode)) {
	                        baseQry = "select * from egpt_mv_c10_report c10, eg_boundary zoneBndry where ";
	                        countQry = "select count(*) from egpt_mv_c10_report c10, eg_boundary zoneBndry where ";
	                    } else if (WARDWISE.equalsIgnoreCase(mode)) {
	                        baseQry = "select * from egpt_mv_c10_report c10, eg_boundary wardBndry where ";
	                        countQry = "select count(*) from egpt_mv_c10_report c10, eg_boundary wardBndry where ";
	                    } else if (BLOCKWISE.equalsIgnoreCase(mode)) {
	                        baseQry = "select * from egpt_mv_c10_report c10, eg_boundary blockBndry where ";
	                        countQry = "select count(*) from egpt_mv_c10_report c10, eg_boundary blockBndry where ";
	                    } else if (LOCALITYWISE.equalsIgnoreCase(mode)) {
	                        baseQry = "select * from egpt_mv_c10_report c10, eg_boundary localityBndry where ";
	                        countQry = "select count(*) from egpt_mv_c10_report c10, eg_boundary localityBndry where ";
	                    }
	                    if (fromDate != null && !fromDate.equals("DD/MM/YYYY") && !fromDate.equals("")) {
	                            srchQryStr= "receipt_date >= to_date(?, 'DD/MM/YYYY') ";
	                            paramList.add(fromDate);
	                    }
	                    if (toDate != null && !toDate.equals("DD/MM/YYYY") && !toDate.equals("")) {
	                            srchQryStr=srchQryStr+"and receipt_date <= to_date(?, 'DD/MM/YYYY') ";
	                            paramList.add(toDate); 
	                    }
	                    if (collMode != null && !collMode.equals("") && !collMode.equals("-1")) {
	                            LOGGER.debug("Collection Mode = " + collMode);
	                            srchQryStr=srchQryStr+"and collectiontype = ? ";
	                            paramList.add(collMode);
	                    }
	                    if (transMode != null && !transMode.equals("") && !transMode.equals("-1")) {
	                            LOGGER.debug("Transaction Mode = " + transMode);
	                            srchQryStr=srchQryStr+"and payment_mode = ? ";
	                            paramList.add(transMode);
	                    }
	                    if (mode.equals(ZONEWISE)) {
	                            if (boundaryId != null && !boundaryId.equals("") && !boundaryId.equals("-1")) {
	                                LOGGER.debug("zoneNo = " + boundaryId);
	                                srchQryStr=srchQryStr+"and zoneid = ? ";
	                                paramList.add(Integer.parseInt(boundaryId));
	                            }
	                            srchQryStr=srchQryStr+"and zoneid = zoneBndry.id ";
	                            orderbyQry="order by zoneBndry.boundarynum";
	                    } else if (mode.equals(WARDWISE)) {
	                         if (boundaryId != null && !boundaryId.equals("") && !boundaryId.equals("-1")) {
	                                 LOGGER.debug("wardNo = " + boundaryId);
	                                 srchQryStr=srchQryStr+"and wardid = ? ";
	                                 paramList.add(Integer.parseInt(boundaryId));
	                         }
	                         srchQryStr=srchQryStr+"and wardid = wardBndry.id ";
	                         orderbyQry="order by wardBndry.boundarynum";
	                    } else if (mode.equals(BLOCKWISE)) {
	                        if (boundaryId != null && !boundaryId.equals("") && !boundaryId.equals("-1")) {
	                            LOGGER.debug("blockNo = " + boundaryId);
	                            srchQryStr=srchQryStr+"and areaid = ? ";
	                            paramList.add(Integer.parseInt(boundaryId));
	                        }
	                        srchQryStr=srchQryStr+"and areaid = blockBndry.id ";
	                        orderbyQry="order by blockBndry.boundarynum";
	                    } else if (mode.equals(LOCALITYWISE)) {
	                        if (boundaryId != null && !boundaryId.equals("") && !boundaryId.equals("-1")) {
	                            LOGGER.debug("localityNo = " + boundaryId);
	                            srchQryStr=srchQryStr+"and localityid = ? ";
	                            paramList.add(Integer.parseInt(boundaryId));
	                        }
	                        srchQryStr=srchQryStr+"and localityid = localityBndry.id ";
	                        orderbyQry="order by localityBndry.boundarynum";
	                    }
	                    countQry=countQry+srchQryStr;
	                    srchQryStr=baseQry+srchQryStr+orderbyQry;
	                    
	            } catch (Exception e) {
	                    e.printStackTrace();
	                    LOGGER.error("Error occured in Class : CollectionSummaryReportAction  Method : list", e);
	                    throw new EGOVRuntimeException("Error occured in Class : CollectionSummaryReportAction  Method : list "
	                                    + e.getMessage());
	            }
	            LOGGER.debug("Exit from list method");
	            return new SearchQuerySQL(srchQryStr,countQry, paramList);
	    }
	
	
	private List<Map<String, Object>> prepareBndryWiseResultList() throws ParseException {
	    LOGGER.debug("Entered into prepareResultList method");
            List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>(); 
            paginatedList = (EgovPaginatedList) searchResult;
            List<CollectionSummary> collSummaryList = new ArrayList<CollectionSummary>();
            
            try {
                final Iterator iter = paginatedList.getList().iterator();
                while (iter.hasNext()) {
                    Object[] object = (Object[]) iter.next();
                    CollectionSummary cs = new CollectionSummary();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                    cs.setReceiptNumber(object[0].toString());
                    cs.setReceiptDate(sdf1.parse(object[1].toString()));
                    cs.setPropertyId(object[2].toString());
                    cs.setZoneId(Long.valueOf(object[3].toString()));
                    cs.setWardId(Long.valueOf(object[4].toString()));
                    cs.setAreaId(Long.valueOf(object[5].toString()));
                    cs.setLocalityId(Long.valueOf(object[6].toString()));
                    cs.setPayeeName(object[8].toString());
                    cs.setCollectionType(object[9].toString().charAt(0));
                    cs.setPaymentMode(object[10].toString());
                    cs.setUserName(object[11].toString());
                    cs.setTaxColl(object[12]!= null?new BigDecimal(object[12].toString()):new BigDecimal(0));
                    cs.setPenaltyColl(object[13]!= null?new BigDecimal(object[13].toString()):new BigDecimal(0));
                    cs.setLibCessColl(object[14]!= null?new BigDecimal(object[14].toString()):new BigDecimal(0));
                    cs.setArrearTaxColl(object[15]!= null?new BigDecimal(object[15].toString()):new BigDecimal(0));
                    cs.setArrearPenaltyColl(object[16]!= null?new BigDecimal(object[16].toString()):new BigDecimal(0));
                    cs.setArrearLibCessColl(object[17]!= null?new BigDecimal(object[17].toString()):new BigDecimal(0));
                    collSummaryList.add(cs);
                }
            
        
                if (collSummaryList != null && !collSummaryList.isEmpty()) {
                        for (CollectionSummary collSummObj : collSummaryList) {
                                CollectionSummary collSummary =collSummObj;
                                if (prevZone == null && prevWard == null && prevBlock == null && prevLocality == null) {
                                        initializeReasonAmount(collSummary);
                                } else if ((prevZone != null && prevZone.equals(collSummary.getZoneId()))
                                                || (prevWard != null && prevWard.equals(collSummary.getWardId()))
                                                || (prevBlock != null && prevBlock.equals(collSummary.getAreaId()))
                                                || (prevLocality != null && prevLocality.equals(collSummary.getLocalityId()))) {
                                        if (taxAmount != null) {
                                                taxAmount = (collSummary.getTaxColl() != null) ? taxAmount.add(collSummary.getTaxColl())
                                                                : taxAmount;
                                        } else {
                                                taxAmount = collSummary.getTaxColl();
                                        }
                                        if (arrearTaxAmount != null) {
                                            arrearTaxAmount = (collSummary.getArrearTaxColl() != null) ? arrearTaxAmount.add(collSummary.getArrearTaxColl())
                                                            : arrearTaxAmount;
                                        } else {
                                            arrearTaxAmount = collSummary.getArrearTaxColl();
                                        }
                                        if (penaltyAmount != null) {
                                                penaltyAmount = (collSummary.getPenaltyColl() != null) ? penaltyAmount.add(collSummary
                                                                .getPenaltyColl()) : penaltyAmount;
                                        } else {
                                                penaltyAmount = collSummary.getPenaltyColl();
                                        }
                                        if (arrearPenaltyAmount != null) {
                                            arrearPenaltyAmount = (collSummary.getArrearPenaltyColl() != null) ? arrearPenaltyAmount.add(collSummary
                                                            .getArrearPenaltyColl()) : arrearPenaltyAmount;
                                        } else {
                                            arrearPenaltyAmount = collSummary.getArrearPenaltyColl();
                                        }
                                        if (libCessAmount != null) {
                                            libCessAmount = (collSummary.getLibCessColl() != null) ? libCessAmount.add(collSummary
                                                            .getLibCessColl()) : libCessAmount;
                                        } else {
                                            libCessAmount = collSummary.getLibCessColl();
                                        }
                                        if (arrearLibCessAmount != null) {
                                            arrearLibCessAmount = (collSummary.getArrearLibCessColl() != null) ? arrearLibCessAmount.add(collSummary
                                                            .getArrearLibCessColl()) : arrearLibCessAmount;
                                        } else {
                                            arrearLibCessAmount = collSummary.getArrearLibCessColl();
                                        }
                                } else {
                                        resList.add(getCalculatedResultMap());
                                        initializeReasonAmount(collSummary);
                                }
                        }
                        //Last Row
                        resList.add(getCalculatedResultMap());
                        Map<String, Object> totalsMap = new HashMap<String, Object>();
                        if (mode.equals(ZONEWISE)) {
                            totalsMap.put("Zone", "<b>Total</b>");
                        } else if (mode.equals(WARDWISE)) {
                            totalsMap.put("Ward", "<b>Total</b>");
                        } else if (mode.equals(BLOCKWISE)) {
                            totalsMap.put("Block", "<b>Total</b>");
                        } else if (mode.equals(LOCALITYWISE)) {
                            totalsMap.put("Locality", "<b>Total</b>");
                        }
                        totalsMap.put("ArrearTaxAmount", formatNumberWithoutFraction(totArrearTaxAmt));
                        totalsMap.put("ArrearLibraryCess", formatNumberWithoutFraction(totArrearLibCessAmt));
                        totalsMap.put("ArrearTotal", formatNumberWithoutFraction(totArrearTaxAmt.add(totArrearLibCessAmt)));
                        totalsMap.put("TaxAmount", formatNumberWithoutFraction(totTaxAmt));
                        totalsMap.put("LibraryCess", formatNumberWithoutFraction(totLibCessAmt));
                        totalsMap.put("CurrentTotal", formatNumberWithoutFraction(totTaxAmt.add(totLibCessAmt)));
                        totalsMap.put("Penalty", formatNumberWithoutFraction(totPenaltyAmt));
                        totalsMap.put("ArrearPenalty", formatNumberWithoutFraction(totArrearPenaltyAmt));
                        totalsMap.put("PenaltyTotal", formatNumberWithoutFraction(totPenaltyAmt.add(totArrearPenaltyAmt)));
                        totalsMap.put("Total", formatNumberWithoutFraction(totArrearTaxAmt.add(totArrearLibCessAmt).add(totTaxAmt)
                                .add(totLibCessAmt).add(totPenaltyAmt).add(totArrearPenaltyAmt)));
                        resList.add(totalsMap);
                }
            } catch (Exception e) {
                    LOGGER.error("Exception in prepareBndryWiseResultList method : " + e.getMessage());
                    throw new EGOVRuntimeException("Exception in prepareBndryWiseResultList method : ", e);
            }
            LOGGER.debug("Exit from prepareResultList method");
            return resList;
	}
	
	private void initializeReasonAmount(CollectionSummary collSummary){
	    if (mode.equals(ZONEWISE)) {
                prevZone = collSummary.getZoneId();
            } else if (mode.equals(WARDWISE)) {
                prevWard = collSummary.getWardId();
            } else if (mode.equals(BLOCKWISE)) {
                prevBlock = collSummary.getAreaId();
            } else if (mode.equals(LOCALITYWISE)) {
                prevLocality = collSummary.getLocalityId();
            }
            taxAmount = collSummary.getTaxColl();
            arrearTaxAmount = collSummary.getArrearTaxColl();
            penaltyAmount = collSummary.getPenaltyColl();
            arrearPenaltyAmount = collSummary.getArrearPenaltyColl();
            libCessAmount = collSummary.getLibCessColl();
            arrearLibCessAmount = collSummary.getArrearLibCessColl();
        }
        
        private Map<String, Object> getCalculatedResultMap(){
            Map<String, Object> resultMap = new HashMap<String, Object>();
            
            if (mode.equals(ZONEWISE)) {
                resultMap.put("Zone", boundaryService.getBoundaryById(prevZone).getName());
            } else if (mode.equals(WARDWISE)) {
                resultMap.put("Ward", boundaryService.getBoundaryById(prevWard).getName());
            } else if (mode.equals(BLOCKWISE)) {
                resultMap.put("Block", boundaryService.getBoundaryById(prevBlock).getName());
            } else if (mode.equals(LOCALITYWISE)) {
                resultMap.put("Locality", boundaryService.getBoundaryById(prevLocality).getName());
            }
            resultMap.put("ArrearTaxAmount", formatNumberWithoutFraction((arrearTaxAmount != null) ? arrearTaxAmount : ZERO));
            totArrearTaxAmt = (arrearTaxAmount != null) ? totArrearTaxAmt.add(arrearTaxAmount) : totArrearTaxAmt
                    .add(ZERO);
            
            resultMap.put("ArrearLibraryCess", formatNumberWithoutFraction((arrearLibCessAmount != null) ? arrearLibCessAmount : ZERO));
            totArrearLibCessAmt = (arrearLibCessAmount != null) ? totArrearLibCessAmt.add(arrearLibCessAmount) : totArrearLibCessAmt
                    .add(ZERO);
            resultMap.put("ArrearTotal", formatNumberWithoutFraction(totArrearTaxAmt.add(totArrearLibCessAmt)));
            
            resultMap.put("TaxAmount", formatNumberWithoutFraction(taxAmount));
            totTaxAmt = (taxAmount != null) ? totTaxAmt.add(taxAmount) : totTaxAmt.add(ZERO);
            
            resultMap.put("LibraryCess", formatNumberWithoutFraction((libCessAmount != null) ? libCessAmount : ZERO));
            totLibCessAmt = (libCessAmount != null) ? totLibCessAmt.add(libCessAmount) : totLibCessAmt
                    .add(ZERO);
            resultMap.put("CurrentTotal", formatNumberWithoutFraction(totTaxAmt.add(totLibCessAmt)));
            
            resultMap.put("Penalty", formatNumberWithoutFraction((penaltyAmount != null) ? penaltyAmount : ZERO));
            totPenaltyAmt = (penaltyAmount != null) ? totPenaltyAmt.add(penaltyAmount) : totPenaltyAmt
                            .add(ZERO);
            
            resultMap.put("ArrearPenalty", formatNumberWithoutFraction((arrearPenaltyAmount != null) ? arrearPenaltyAmount : ZERO));
            totArrearPenaltyAmt = (arrearPenaltyAmount != null) ? totArrearPenaltyAmt.add(arrearPenaltyAmount) : totArrearPenaltyAmt
                            .add(ZERO);
            resultMap.put("PenaltyTotal", formatNumberWithoutFraction(totPenaltyAmt.add(totArrearPenaltyAmt)));
            
            if(arrearTaxAmount != null){
                taxAmount= taxAmount.add(arrearTaxAmount);
            }
            if(penaltyAmount != null){
                taxAmount=taxAmount.add(penaltyAmount);
            }
            if(arrearPenaltyAmount != null){
                taxAmount= taxAmount.add(arrearPenaltyAmount);
            }
            if(libCessAmount != null){
                taxAmount=taxAmount.add(libCessAmount);
            }
            if(arrearLibCessAmount != null){
                taxAmount=taxAmount.add(arrearLibCessAmount);
            }
            resultMap.put("Total", formatNumberWithoutFraction(taxAmount));
            return resultMap;
        }
        
        private String formatNumberWithoutFraction(final BigDecimal number){
            return NumberUtil.formatNumber(number,0,true);
        }    

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Map<Character, String> getCollectionModesMap() {
        return collectionModesMap;
    }

    public void setCollectionModesMap(Map<Character, String> collectionModesMap) {
        this.collectionModesMap = collectionModesMap;
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

    public String getCollMode() {
        return collMode;
    }

    public void setCollMode(String collMode) {
        this.collMode = collMode;
    }

    public String getTransMode() {
        return transMode;
    }

    public void setTransMode(String transMode) {
        this.transMode = transMode;
    }

    public List<Map<String, Object>> getResultList() {
        return resultList;
    }

    public void setResultList(List<Map<String, Object>> resultList) {
        this.resultList = resultList;
    }

    public Boolean getSrchFlag() {
        return srchFlag;
    }

    public void setSrchFlag(Boolean srchFlag) {
        this.srchFlag = srchFlag;
    }

    public String getDateSelected() {
        return dateSelected;
    }

    public void setDateSelected(String dateSelected) {
        this.dateSelected = dateSelected;
    }

    public String getBoundaryId() {
        return boundaryId;
    }

    public void setBoundaryId(String boundaryId) {
        this.boundaryId = boundaryId;
    }

    public Map<Long, String> getWardBndryMap() {
        return wardBndryMap;
    }

    public void setWardBndryMap(Map<Long, String> wardBndryMap) {
        this.wardBndryMap = wardBndryMap;
    }

    public Map<Long, String> getBlockBndryMap() {
        return blockBndryMap;
    }

    public void setBlockBndryMap(Map<Long, String> blockBndryMap) {
        this.blockBndryMap = blockBndryMap;
    }

    public Map<Long, String> getLocalityBndryMap() {
        return localityBndryMap;
    }

    public void setLocalityBndryMap(Map<Long, String> localityBndryMap) {
        this.localityBndryMap = localityBndryMap;
    }

    public Map<Long, String> getZoneBndryMap() {
        return zoneBndryMap;
    }

    public void setZoneBndryMap(Map<Long, String> zoneBndryMap) {
        this.zoneBndryMap = zoneBndryMap;
    }

    public EgovPaginatedList getPaginatedList() {
        return paginatedList;
    }

    public void setPaginatedList(EgovPaginatedList paginatedList) {
        this.paginatedList = paginatedList;
    }
}
