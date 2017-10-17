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
package org.egov.ptis.actions.edit;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.client.util.PropertyTaxUtil.isNull;
import static org.egov.ptis.client.util.PropertyTaxUtil.isZero;
import static org.egov.ptis.constants.PropertyTaxConstants.BUILTUP_PROPERTY_DMDRSN_CODE_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_FIRST_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_SECOND_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_VACANT_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMAND_REASON_ORDER_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMAND_RSNS_LIST;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_BASICPROPERTY_BY_UPICNO;
import static org.egov.ptis.constants.PropertyTaxConstants.VACANT_PROPERTY_DMDRSN_CODE_MAP;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.dcb.bean.DCBDisplayInfo;
import org.egov.demand.dao.EgDemandDetailsDao;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.bean.DemandDetail;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.entity.demand.PTDemandCalculations;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.DemandAudit;
import org.egov.ptis.domain.entity.property.DemandAuditDetails;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.service.DemandAuditService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * <p>
 * Action to edit the installment wise Taxes
 * </p>
 * Audit log format for this action, demand/collection edits
 * 
 * <pre>
 * Remarks : &ltremarks&gt 
 * Demand-
 * Installment1 : Tax Name | Old Tax | New Tax
 * Installment2 : Tax Name | Old Tax | New Tax 
 *     ... ... 
 * Installmentn : Tax Name | Old Tax | New Tax
 * Collection -
 * Installment1 : Tax Name | Old Tax | New Tax 
 * Installment2 : Tax Name | Old Tax | New Tax 
 *     ... ... 
 * Installmentn : Tax Name | Old Tax | New Tax
 * </pre>
 * 
 * @author nayeem
 */
@SuppressWarnings("serial")
@ParentPackage("egov")
@Namespace("/edit")
@ResultPath("/WEB-INF/jsp/")
@Results({ @Result(name = AddDemandAction.RESULT_NEW, location = "edit/addDemand-addForm.jsp"),
        @Result(name = AddDemandAction.RESULT_ERROR, location = "edit/addDemand-error.jsp"),
        @Result(name = AddDemandAction.RESULT_ACK, location = "edit/addDemand-ack.jsp") })
public class AddDemandAction extends BaseFormAction {

    private static final Logger LOGGER = Logger.getLogger(AddDemandAction.class);
    protected static final String RESULT_NEW = "addForm";
    protected static final String RESULT_ERROR = "error";
    protected static final String RESULT_ACK = "ack";
    private static final String MSG_ERROR_NOT_MIGRATED_PROPERTY = " This is not a migrated property ";

    private static final String QUERY_DEMAND_DETAILS = "SELECT dd FROM Ptdemand ptd "
            + "LEFT JOIN ptd.egDemandDetails dd " + "LEFT JOIN ptd.egptProperty p " + "LEFT JOIN  p.basicProperty bp "
            + "WHERE bp = ? " + "AND bp.active = true " + "AND p.status = 'A' ";

    private static final String queryInstallmentPTDemand = "select ptd from Ptdemand ptd inner join fetch ptd.egDemandDetails dd "
            + "inner join fetch dd.egDemandReason dr inner join fetch dr.egDemandReasonMaster drm "
            + "inner join fetch ptd.egptProperty p inner join fetch p.basicProperty bp "
            + "where bp.active = true and (p.status = 'A' or p.status = 'I' or p.status = 'W') "
            + "and bp = ? and ptd.egInstallmentMaster = ? ";

    private static final String QUERY_NONZERO_DEMAND_DETAILS = QUERY_DEMAND_DETAILS + "AND dd.amount >= 0 ";

    private static final String queryInstallmentDemandDetails = QUERY_NONZERO_DEMAND_DETAILS
            + " AND ptd.egInstallmentMaster = ? ";

   
    private static final String ADD_DEMAND = "Add Demand";
    private static final String ADD_TYPE_POSTFIX = "-";

    private String propertyId;
    private String ownerName;
    private String propertyAddress;
    private String remarks;
    private String errorMessage;

    private BasicProperty basicProperty;
    private PropertyService propService;
    private DCBDisplayInfo dcbDispInfo;

    @Autowired
    private InstallmentHibDao installmentDAO;
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    @Autowired
    private DemandAuditService demandAuditService;
    @Autowired
    @Qualifier("egDemandDetailsDAO")
    private EgDemandDetailsDao demandDetailsDao;
    @Autowired
    @Qualifier("propertyImplService")
    private PersistenceService propertyImplService;


    DemandAudit demandAudit = new DemandAudit();

    private List<EgDemandDetails> demandDetails = new ArrayList<EgDemandDetails>();
    private List<DemandDetail> demandDetailBeanList = new ArrayList<DemandDetail>();
    private List<Installment> allInstallments = new ArrayList<Installment>();
    private Set<Installment> propertyInstallments = new TreeSet<Installment>();
    private Map<Installment, Map<String, Boolean>> collectionDetails = new HashMap<Installment, Map<String, Boolean>>();
    private Map<String, String> demandReasonMap = new HashMap<String, String>();

    @Override
    public Object getModel() {
        return demandDetailBeanList;
    }

    @Override
    @SuppressWarnings("unchecked")
    @SkipValidation
    public void prepare() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into prepare");

        basicProperty = (BasicProperty) getPersistenceService().findByNamedQuery(QUERY_BASICPROPERTY_BY_UPICNO,
                propertyId);
        if (null != basicProperty.getActiveProperty())
            if (basicProperty.getActiveProperty().getPropertyDetail().getPropertyTypeMaster().getCode()
                    .equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
                demandReasonMap = VACANT_PROPERTY_DMDRSN_CODE_MAP;
            } else
                demandReasonMap = BUILTUP_PROPERTY_DMDRSN_CODE_MAP;

        for (DemandDetail dd : demandDetailBeanList) {

            if (dd.getInstallment() != null && dd.getInstallment().getId() != null
                    && !dd.getInstallment().getId().equals(-1)) {
                dd.setInstallment((Installment) installmentDAO.findById(dd.getInstallment().getId(), false));
                if (!dd.getIsNew()) {
                    propertyInstallments.add(dd.getInstallment());
                }
            }
        }

        DateFormat dateFormat = new SimpleDateFormat(PropertyTaxConstants.DATE_FORMAT_DDMMYYY);
        try {
            allInstallments = propertyTaxUtil
                    .getInstallmentListByStartDateToCurrFinYearDesc(dateFormat.parse("01/04/1963"));
        } catch (ParseException e) {
            throw new ApplicationRuntimeException("Error while getting all installments from start date", e);
        }

        allInstallments.removeAll(propertyInstallments);

        addDropdownData("allInstallments", allInstallments);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exiting from prepare");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void validate() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into validate");

        // Set<String> instDemandRsn = new LinkedHashSet<String>();
        Set<Installment> newInstallments = new TreeSet<Installment>();
        Set<String> installmentsChqPenalty = new TreeSet<String>();
        Set<String> instDmdRsnMaster = new HashSet<String>();
        List<String> instString;
        Set<String> actAmtInstallments = new TreeSet<String>();
        List<String> errorParams = null;

        for (DemandDetail dd : demandDetailBeanList) {

            if (dd.getIsNew() != null && dd.getIsNew()) {
                instString = new ArrayList<String>();
                instString.add(dd.getReasonMaster());
                if (dd.getReasonMaster().equalsIgnoreCase(DEMANDRSN_STR_GENERAL_TAX)
                        || dd.getReasonMaster().equalsIgnoreCase(DEMANDRSN_STR_VACANT_TAX)) {
                    if (dd.getInstallment().getId() == null || dd.getInstallment().getId().equals(-1)) {
                        addActionError(getText("error.editDemand.selectInstallment"));
                    }
                }

                if (null != dd.getInstallment().getId() && !dd.getInstallment().getId().equals(-1)) {
                    if (null == dd.getActualAmount()) {
                        addActionError(getText("error.editDemand.actualAmount", instString));
                    }
                    if (null != dd.getActualAmount() && null != dd.getActualCollection()) {
                        if (dd.getActualAmount().intValue() < dd.getActualCollection().intValue()) {
                            addActionError(getText("error.collection.greaterThan.actualAmount"));
                        }
                    }
                }

                if (dd.getActualAmount() == null) {
                    if (dd.getActualCollection() != null) {
                        actAmtInstallments.add(dd.getInstallment().getDescription());
                    }
                } else {
                    if (dd.getReasonMaster().equalsIgnoreCase(DEMANDRSN_STR_GENERAL_TAX)
                            || dd.getReasonMaster().equalsIgnoreCase(DEMANDRSN_STR_VACANT_TAX)) {
                        if (dd.getInstallment().getId() == null || dd.getInstallment().getId().equals(-1)) {
                            addActionError(getText("error.editDemand.selectInstallment"));
                        } else {
                            newInstallments.add(dd.getInstallment());
                            String instRsn = dd.getInstallment().toString().concat(ADD_TYPE_POSTFIX)
                                    .concat(dd.getReasonMaster());
                            if (instDmdRsnMaster.add(instRsn) == false) {
                                instString.add(dd.getInstallment().toString());
                                addActionError(getText("error.editDemand.duplicateInstallment", instString));
                            }
                        }
                    }
                }
            } else {
                newInstallments.add(dd.getInstallment());

                if (null != dd.getRevisedAmount() && isZero(dd.getRevisedAmount())) {
                    if (dd.getActualCollection().compareTo(BigDecimal.ZERO) > 0 && isNull(dd.getRevisedCollection())) {
                        errorParams = new ArrayList<String>();
                        errorParams.add(dd.getReasonMaster());
                        errorParams.add(dd.getInstallment().getDescription());
                        addActionError(getText("error.editDemand.collectionForUpdatedDemand", errorParams));
                    }
                }
                if (null != dd.getRevisedAmount() && null != dd.getActualCollection()) {
                    if (dd.getRevisedAmount().intValue() < dd.getActualCollection().intValue()) {
                        addActionError(getText("error.collection.greaterThan.revisedAmount"));
                    }
                }
                if (null != dd.getRevisedAmount() && null != dd.getRevisedCollection()) {
                    if (dd.getRevisedAmount().intValue() < dd.getRevisedCollection().intValue()) {
                        addActionError(getText("error.revisedCollecion.greaterThan.revisedAmount"));
                    }
                }
            }

        }

        if (actAmtInstallments.size() > 0) {
            final String inst = actAmtInstallments.toString().replace('[', ' ').replace(']', ' ');
            List<String> instStrings = new ArrayList<String>() {
                {
                    add(inst);
                }
            };
            addActionError(getText("error.editDemand.actualAmount", instStrings));
        }

        List<Installment> installmentsInOrder = null;
        if (!newInstallments.isEmpty()) {
            installmentsInOrder = propertyTaxUtil.getInstallmentListByStartDateToCurrFinYearDesc(
                    (new ArrayList<Installment>(newInstallments).get(0)).getFromDate());

            if (newInstallments.size() != installmentsInOrder.size()) {
                addActionError(getText("error.editDemand.badInstallmentSelection"));
            }

            Date currDate = new Date();
            Map<String, Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(currDate);
            if (!DateUtils.compareDates(currDate, currYearInstMap.get(CURRENTYEAR_SECOND_HALF).getFromDate())) {
                if ((newInstallments.contains(currYearInstMap.get(CURRENTYEAR_FIRST_HALF))
                        && !newInstallments.contains(currYearInstMap.get(CURRENTYEAR_SECOND_HALF)))
                        || (!newInstallments.contains(currYearInstMap.get(CURRENTYEAR_FIRST_HALF))
                                && newInstallments.contains(currYearInstMap.get(CURRENTYEAR_SECOND_HALF)))) {
                    addActionError(getText("error.currentyearinstallments"));
                }
            } else if (!newInstallments.contains(currYearInstMap.get(CURRENTYEAR_SECOND_HALF))) {
                addActionError(getText("error.currentInst"));
            }

        }

        if (installmentsChqPenalty.size() > 0) {
            String inst = installmentsChqPenalty.toString().replace('[', ' ').replace(']', ' ');
            List<String> instStrings = new ArrayList<String>();
            instStrings.add(inst);
            addActionError(getText("error.editDemand.chqBouncePenaltyIsZero", instStrings));
        }

        if (StringUtils.isBlank(remarks)) {
            addActionError(getText("mandatory.editDmdCollRemarks"));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exiting from validate");
    }

    @SuppressWarnings("unchecked")
    @SkipValidation
    @Action(value = "/addDemand-newAddForm")
    public String newAddForm() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into newAddForm");
        String resultPage = "";

        if (basicProperty != null && !basicProperty.getSource().equals(PropertyTaxConstants.SOURCEOFDATA_DATAENTRY)
                && !basicProperty.getSource().equals(PropertyTaxConstants.SOURCEOFDATA_MIGRATION)) {
            setErrorMessage(MSG_ERROR_NOT_MIGRATED_PROPERTY);
            resultPage = RESULT_ERROR;
        } else {
            ownerName = basicProperty.getFullOwnerName();
            propertyAddress = basicProperty.getAddress().toString();
            demandDetails = getPersistenceService().findAllBy(queryInstallmentDemandDetails, basicProperty,
                    propertyTaxCommonUtils.getCurrentInstallment());
            if (!demandDetails.isEmpty()) {
                Collections.sort(demandDetails, new Comparator<EgDemandDetails>() {
                    @Override
                    public int compare(EgDemandDetails o1, EgDemandDetails o2) {
                        return o1.getEgDemandReason().getEgInstallmentMaster()
                                .compareTo(o2.getEgDemandReason().getEgInstallmentMaster());
                    }
                });
            }

            PropertyTaxBillable billable = new PropertyTaxBillable();
            billable.setBasicProperty(basicProperty);
            Boolean isInstallmentExists = false;
            Map<Installment, List<String>> installmentDemandReason = new HashMap<Installment, List<String>>();

            if (!demandDetails.isEmpty()) {
                for (EgDemandDetails demandDetail : demandDetails) {
                    Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();
                    String reasonMaster = demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster();
                    propertyInstallments.add(installment);
                    if (installmentDemandReason.get(installment) == null) {
                        List<String> rsns = new ArrayList<String>();
                        rsns.add(reasonMaster);
                        installmentDemandReason.put(installment, rsns);
                    } else {
                        installmentDemandReason.get(installment).add(reasonMaster);
                    }

                    DemandDetail dmdDtl = createDemandDetailBean(installment, reasonMaster, demandDetail.getAmount(),
                            demandDetail.getAmtCollected(), false);
                    demandDetailBeanList.add(dmdDtl);
                }
            } else {
                for (Map.Entry<String, String> entry : demandReasonMap.entrySet()) {
                    DemandDetail dmdDtl = createDemandDetailBean(null, entry.getKey(), null, null, true);
                    demandDetailBeanList.add(dmdDtl);
                }
            }

            Installment currentInstallment = propertyTaxCommonUtils.getCurrentInstallment();
            resultPage = RESULT_NEW;
        }

        allInstallments.removeAll(propertyInstallments);
        addDropdownData("allInstallments", allInstallments);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exiting from newAddForm");

        return resultPage;
    }

    private DemandDetail createDemandDetailBean(Installment installment, String reasonMaster, BigDecimal amount,
            BigDecimal amountCollected, Boolean isNew) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into createDemandDetailBean");
            LOGGER.debug("createDemandDetailBean - installment=" + installment + ", reasonMaster=" + reasonMaster
                    + ", amount=" + amount + ", amountCollected=" + amountCollected);
        }

        DemandDetail demandDetail = new DemandDetail();
        demandDetail.setInstallment(installment);
        demandDetail.setReasonMaster(reasonMaster);
        demandDetail.setActualAmount(amount);
        demandDetail.setActualCollection(amountCollected);
        demandDetail.setIsCollectionEditable(true);
        demandDetail.setIsNew(isNew);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug(
                    "createDemandDetailBean - demandDetail= " + demandDetail + "\nExiting from createDemandDetailBean");
        return demandDetail;
    }

    /**
     * To set DcbDispInfo with ReasonCategoryCodes as Tax and Penalty. Here
     * reasonMasterCodes could also be set to DcbDispInfo.
     * 
     * @return DCBDisplayInfo
     */

    @SkipValidation
    private void prepareDisplayInfo() {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into method prepareDisplayInfo");
        dcbDispInfo = new DCBDisplayInfo();
        dcbDispInfo.setReasonCategoryCodes(Collections.<String> emptyList());
        List<String> reasonList = new ArrayList<String>();
        reasonList.addAll(DEMAND_REASON_ORDER_MAP.keySet());
        dcbDispInfo.setReasonMasterCodes(reasonList);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("DCB Display Info : " + dcbDispInfo);
            LOGGER.debug("Number of Demand Reasons : " + (reasonList != null ? reasonList.size() : ZERO));
            LOGGER.debug("Exit from method prepareDisplayInfo");
        }
    }

    @SuppressWarnings("unchecked")
    @ValidationErrorPage(value = RESULT_NEW)
    @Action(value = "/addDemand-update")
    public String update() {
        if (LOGGER.isDebugEnabled())
            LOGGER.info("Entered into update, basicProperty=" + basicProperty);

        boolean isAllRevisedTaxesBlank = true;
        boolean isAllRevisedCollectionsBlank = true;
        boolean isAllNewActTaxesBlank = true;
        boolean isAllNewRvsdTaxesBlank = true;

        for (DemandDetail dmdDetail : demandDetailBeanList) {

            if (dmdDetail.getIsNew()) {
                if (dmdDetail.getActualAmount() != null) {
                    isAllNewActTaxesBlank = false;
                    break;
                }

                if (dmdDetail.getActualCollection() != null) {
                    isAllNewRvsdTaxesBlank = false;
                    break;
                }
            } else {
                if (dmdDetail.getRevisedAmount() != null) {
                    isAllRevisedTaxesBlank = false;
                    break;
                }

                if (dmdDetail.getRevisedCollection() != null) {
                    isAllRevisedCollectionsBlank = false;
                    break;
                }
            }
        }

        List<EgDemandDetails> demandDetailsFromDB = getPersistenceService().findAllBy(QUERY_NONZERO_DEMAND_DETAILS,
                basicProperty);
        Installment currentInstallment = propertyTaxCommonUtils.getCurrentInstallment();
        Map<Installment, List<EgDemandDetails>> demandDetails = new TreeMap<Installment, List<EgDemandDetails>>();

        String queryZeroDemandDetails = QUERY_DEMAND_DETAILS + " AND dd.amount = 0";

        List<EgDemandDetails> dmdDtlsWithZeroAmt = getPersistenceService().findAllBy(queryZeroDemandDetails,
                basicProperty);
        Set<Installment> zeroInstallments = new TreeSet<Installment>();

        BigDecimal totalDmd = BigDecimal.ZERO;
        EgDemandDetails egDemandDtls = null;

        demandAudit.setBasicproperty(basicProperty.getUpicNo());
        demandAudit.setTransaction(ADD_DEMAND);
        demandAudit.setRemarks(remarks);
        demandAudit.setLastModifiedDate(new Date());

        for (DemandDetail dmdDetail : demandDetailBeanList) {
            if ((dmdDetail.getIsNew() != null && dmdDetail.getIsNew()) && dmdDetail.getActualAmount() != null) {
                EgDemandReason egDmdRsn = propertyTaxUtil.getDemandReasonByCodeAndInstallment(
                        demandReasonMap.get(dmdDetail.getReasonMaster()), dmdDetail.getInstallment());

                /**
                 * Checking whether already EgDemandDetails exists for this, if
                 * yes updating the same. this may be when taxes updated to 0
                 * and then later adding the installment taxes.
                 */

                for (EgDemandDetails details : dmdDtlsWithZeroAmt) {
                    if (details.getEgDemandReason().equals(egDmdRsn)) {
                        zeroInstallments.add(details.getEgDemandReason().getEgInstallmentMaster());
                        details.setAmount(dmdDetail.getActualAmount());
                        details.setAmtCollected((dmdDetail.getActualCollection() == null) ? BigDecimal.ZERO
                                : dmdDetail.getActualCollection());
                        egDemandDtls = details;

                    } else {
                        if(dmdDetail.getActualAmount().compareTo(BigDecimal.ZERO)!=0 && dmdDetail.getIsNew()){
                            egDemandDtls = propService.createDemandDetails(dmdDetail.getActualAmount(),
                                    dmdDetail.getActualCollection(), egDmdRsn, dmdDetail.getInstallment());
                            totalDmd = totalDmd.add(egDemandDtls.getAmount());
                        }
                        
                    }
                }

                if (dmdDtlsWithZeroAmt.isEmpty()) {
                    if(dmdDetail.getActualAmount().compareTo(BigDecimal.ZERO)!=0 && dmdDetail.getIsNew()){
                        egDemandDtls = propService.createDemandDetails(dmdDetail.getActualAmount(),
                                dmdDetail.getActualCollection(), egDmdRsn, dmdDetail.getInstallment());
                        totalDmd = totalDmd.add(egDemandDtls.getAmount());
                    }
                    
                    

                }
                logAudit(dmdDetail);
                List<EgDemandDetails> dmdDtl = new ArrayList<EgDemandDetails>();
                if (demandDetails.get(dmdDetail.getInstallment()) == null) {

                    dmdDtl.add(egDemandDtls);
                    demandDetails.put(dmdDetail.getInstallment(), dmdDtl);
                } else {
                    demandDetails.get(dmdDetail.getInstallment()).add(egDemandDtls);
                }

            }

        }
        
        
        for (EgDemandDetails ddFromDB : demandDetailsFromDB) {

            for (DemandDetail dmdDetail : demandDetailBeanList) {

                if (dmdDetail.getIsNew() != null && !dmdDetail.getIsNew()) {
                    Boolean isUpdateAmount = false;
                    Boolean isUpdateCollection = false;

                    if (dmdDetail.getRevisedAmount() != null
                            && dmdDetail.getInstallment().equals(ddFromDB.getEgDemandReason().getEgInstallmentMaster())
                            && ddFromDB.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                    .equalsIgnoreCase(demandReasonMap.get(dmdDetail.getReasonMaster()))) {

                        isUpdateAmount = true;

                    }

                    if (dmdDetail.getRevisedCollection() != null
                            && ddFromDB.getEgDemand().getEgInstallmentMaster().equals(currentInstallment)
                            && ddFromDB.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                    .equalsIgnoreCase(demandReasonMap.get(dmdDetail.getReasonMaster()))) {

                        Installment inst = (Installment) installmentDAO.findById(dmdDetail.getInstallment().getId(),
                                false);

                        if (ddFromDB.getEgDemandReason().getEgInstallmentMaster().equals(inst)) {
                            isUpdateCollection = true;

                        }
                    }

                    if (isUpdateAmount) {
                        ddFromDB.setAmount(
                                dmdDetail.getRevisedAmount() != null ? dmdDetail.getRevisedAmount() : BigDecimal.ZERO);
                    }

                    if (isUpdateCollection) {
                        ddFromDB.setAmtCollected(dmdDetail.getRevisedCollection() != null
                                ? dmdDetail.getRevisedCollection() : BigDecimal.ZERO);

                    }

                    if (isUpdateAmount || isUpdateCollection) {
                        ddFromDB.setModifiedDate(new Date());
                        logAudit(dmdDetail);
                        demandDetailsDao.update(ddFromDB);

                        break;
                    }
                }

            }

        }
        if (demandAudit.getDemandAuditDetails() != null && demandAudit.getDemandAuditDetails().size() > 0)
            demandAuditService.saveDetails(demandAudit);

        List<EgDemandDetails> currentInstdemandDetailsFromDB = getPersistenceService().findAllBy(
                queryInstallmentDemandDetails, basicProperty, propertyTaxCommonUtils.getCurrentInstallment());

        EgDemand currentPtdemand = null;
        if (!currentInstdemandDetailsFromDB.isEmpty())
            currentPtdemand = currentInstdemandDetailsFromDB.get(0).getEgDemand();

        Map<Installment, Set<EgDemandDetails>> demandDetailsSetByInstallment = getEgDemandDetailsSetByInstallment(
                currentInstdemandDetailsFromDB);
        List<Installment> installments = new ArrayList<Installment>(demandDetailsSetByInstallment.keySet());
        Collections.sort(installments);

        for (Installment inst : installments) {
            Map<String, BigDecimal> dmdRsnAmt = new LinkedHashMap<String, BigDecimal>();
            for (String rsn : DEMAND_RSNS_LIST) {
                EgDemandDetails newDmndDtls = propService
                        .getEgDemandDetailsForReason(demandDetailsSetByInstallment.get(inst), rsn);
                if (newDmndDtls != null && newDmndDtls.getAmtCollected() != null) {
                    BigDecimal extraCollAmt = newDmndDtls.getAmtCollected().subtract(newDmndDtls.getAmount());
                    // If there is extraColl then add to map
                    if (extraCollAmt.compareTo(BigDecimal.ZERO) > 0) {
                        dmdRsnAmt.put(newDmndDtls.getEgDemandReason().getEgDemandReasonMaster().getCode(),
                                extraCollAmt);
                        newDmndDtls.setAmtCollected(newDmndDtls.getAmtCollected().subtract(extraCollAmt));
                        newDmndDtls.setModifiedDate(new Date());
                        demandDetailsDao.update(newDmndDtls);
                    }
                }
            }
            propService.getExcessCollAmtMap().put(inst, dmdRsnAmt);
        }

        LOGGER.info("Excess Collection - " + propService.getExcessCollAmtMap());

        Map<Installment, String> addedInstallments = new TreeMap<Installment, String>();

        Set<EgDemandDetails> demandDetailsToBeSaved = new HashSet<EgDemandDetails>();
        for (Map.Entry<Installment, List<EgDemandDetails>> entry : demandDetails.entrySet()) {
            if (!entry.getValue().get(0).getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()
                    .equalsIgnoreCase(DEMANDRSN_STR_CHQ_BOUNCE_PENALTY)) {

                demandDetailsToBeSaved.addAll(new HashSet<EgDemandDetails>(entry.getValue()));
            }

        }
        List<Ptdemand> currPtdemand = getPersistenceService().findAllBy(queryInstallmentPTDemand, basicProperty,
                propertyTaxCommonUtils.getCurrentInstallment());

        if (currPtdemand != null && currPtdemand.isEmpty()) {
            Ptdemand ptDemand = new Ptdemand();
            PTDemandCalculations ptDmdCalc = new PTDemandCalculations();

            ptDemand.setEgInstallmentMaster(propertyTaxCommonUtils.getCurrentInstallment());
            ptDemand.setEgDemandDetails(demandDetailsToBeSaved);
            ptDemand.setBaseDemand(totalDmd);
            ptDemand.setCreateDate(new Date());
            ptDemand.setModifiedDate(new Date());
            ptDemand.setIsHistory("N");
            ptDemand.setEgptProperty((PropertyImpl) basicProperty.getProperty());
            ptDmdCalc.setPtDemand(ptDemand);
            ptDemand.setDmdCalculations(ptDmdCalc);
            getPersistenceService().applyAuditing(ptDmdCalc);
            basicProperty.getProperty().getPtDemandSet().add(ptDemand);

        } else {
            Ptdemand ptdemand = currPtdemand.get(0);
            ptdemand.setBaseDemand(ptdemand.getBaseDemand() != null ? ptdemand.getBaseDemand().add(totalDmd) : totalDmd);
            ptdemand.getEgDemandDetails().addAll(demandDetailsToBeSaved);
            getPersistenceService().applyAuditing(ptdemand.getDmdCalculations());
            basicProperty.getProperty().getPtDemandSet().add(ptdemand);
        }

        propertyImplService.update(basicProperty.getProperty());

        LOGGER.info("Exiting from update");
        return RESULT_ACK;
    }

    public Map<Installment, Set<EgDemandDetails>> getEgDemandDetailsSetByInstallment(List<EgDemandDetails> demandDtls) {
        Map<Installment, Set<EgDemandDetails>> newEgDemandDetailsSetByInstallment = new HashMap<Installment, Set<EgDemandDetails>>();

        for (EgDemandDetails dd : demandDtls) {

            if (dd.getAmtCollected() == null) {
                dd.setAmtCollected(ZERO);
            }

            if (newEgDemandDetailsSetByInstallment.get(dd.getEgDemandReason().getEgInstallmentMaster()) == null) {
                Set<EgDemandDetails> ddSet = new HashSet<EgDemandDetails>();
                ddSet.add(dd);
                newEgDemandDetailsSetByInstallment.put(dd.getEgDemandReason().getEgInstallmentMaster(), ddSet);
            } else {
                newEgDemandDetailsSetByInstallment.get(dd.getEgDemandReason().getEgInstallmentMaster()).add(dd);
            }
        }
        return newEgDemandDetailsSetByInstallment;
    }

    private void logAudit(DemandDetail dmdDetail) {

        DemandAuditDetails dmdAdtDtls = new DemandAuditDetails();
        dmdAdtDtls.setYear(dmdDetail.getInstallment().toString());
        dmdAdtDtls.setAction(dmdDetail.getIsNew() == true ? "Add" : "Edit");
        dmdAdtDtls.setTaxType(dmdDetail.getReasonMaster());
        dmdAdtDtls.setActualDmd(dmdDetail.getActualAmount() != null ? dmdDetail.getActualAmount() : BigDecimal.ZERO);
        dmdAdtDtls
                .setModifiedDmd(dmdDetail.getRevisedAmount() != null ? dmdDetail.getRevisedAmount() : BigDecimal.ZERO);
        dmdAdtDtls.setActualColl(
                dmdDetail.getActualCollection() != null ? dmdDetail.getActualCollection() : BigDecimal.ZERO);
        dmdAdtDtls.setModifiedColl(
                dmdDetail.getRevisedCollection() != null ? dmdDetail.getRevisedCollection() : BigDecimal.ZERO);
        dmdAdtDtls.setDemandAudit(demandAudit);
        demandAudit.getDemandAuditDetails().add(dmdAdtDtls);

    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public List<EgDemandDetails> getDemandDetails() {
        return demandDetails;
    }

    public void setDemandDetails(List<EgDemandDetails> demandDetails) {
        this.demandDetails = demandDetails;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public PropertyTaxUtil getPropertyTaxUtil() {
        return propertyTaxUtil;
    }

    public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
        this.propertyTaxUtil = propertyTaxUtil;
    }

    public PropertyService getPropService() {
        return propService;
    }

    public void setPropService(PropertyService propService) {
        this.propService = propService;
    }

    public Map<Installment, Map<String, Boolean>> getCollectionDetails() {
        return collectionDetails;
    }

    public void setCollectionDetails(Map<Installment, Map<String, Boolean>> collectionDetails) {
        this.collectionDetails = collectionDetails;
    }

    public List<DemandDetail> getDemandDetailBeanList() {
        return demandDetailBeanList;
    }

    public void setDemandDetailBeanList(List<DemandDetail> demandDetailBeanList) {
        this.demandDetailBeanList = demandDetailBeanList;
    }

    public List<Installment> getAllInstallments() {
        return allInstallments;
    }

    public void setAllInstallments(List<Installment> allInstallments) {
        this.allInstallments = allInstallments;
    }

    public Map<String, String> getDemandReasonMap() {
        return demandReasonMap;
    }

    public void setDemandReasonMap(Map<String, String> demandReasonMap) {
        this.demandReasonMap = demandReasonMap;
    }

}
