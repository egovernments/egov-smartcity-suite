/*******************************************************************************
n * eGov suite of products aim to improve the internal efficiency,transparency, 
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
package org.egov.ptis.actions.edit;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.client.util.PropertyTaxUtil.isNull;
import static org.egov.ptis.client.util.PropertyTaxUtil.isZero;
import static org.egov.ptis.constants.PropertyTaxConstants.AUDITDATA_STRING_SEP;
import static org.egov.ptis.constants.PropertyTaxConstants.BUILTUP_PROPERTY_DMDRSN_CODE_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMAND_REASON_ORDER_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMAND_RSNS_LIST;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_BASICPROPERTY_BY_UPICNO;
import static org.egov.ptis.constants.PropertyTaxConstants.VACANT_PROPERTY_DMDRSN_CODE_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_VACANT_TAX; 
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_SECOND_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_FIRST_HALF;

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
import org.egov.commons.dao.InstallmentDao;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.dcb.bean.DCBDisplayInfo;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.ptis.bean.DemandDetail;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.entity.demand.PTDemandCalculations;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.service.property.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;

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
@Results({ @Result(name = EditDemandAction.RESULT_NEW, location = "edit/editDemand-editForm.jsp"),
        @Result(name = EditDemandAction.RESULT_ERROR, location = "edit/editDemand-error.jsp"),
        @Result(name = EditDemandAction.RESULT_ACK, location = "edit/editDemand-ack.jsp") })
public class EditDemandAction extends BaseFormAction {

    private static final Logger LOGGER = Logger.getLogger(EditDemandAction.class);
    protected static final String RESULT_NEW = "editForm";
    protected static final String RESULT_ERROR = "error";
    protected static final String RESULT_ACK = "ack";
    private static final String MSG_ERROR_NOT_MIGRATED_PROPERTY = " This is not a migrated property ";
    private static final String MSG_ERROR_ALL_FIELDS_BLANK = " You have not entered any value ";
    private static final String MSG_ERROR_EDITDEMAND_NOTALLOWED = " You cannot edit the demands. ";
    private static final String STRING_REMARKS = "Remarks";
    private static final String STRING_VALUE_SEP = "|";
    private static final String STRING_KEY_SEP = ":";

    private static final String QUERY_DEMAND_DETAILS = "SELECT dd FROM Ptdemand ptd "
            + "LEFT JOIN ptd.egDemandDetails dd " + "LEFT JOIN ptd.egptProperty p " + "LEFT JOIN  p.basicProperty bp "
            + "WHERE bp = ? " + "AND bp.active = true " + "AND p.status = 'A' ";

    private static final String QUERY_NONZERO_DEMAND_DETAILS = QUERY_DEMAND_DETAILS /*
                                                                                     * +
                                                                                     * "AND dd.amount > 0 "
                                                                                     */;

    private static final String queryInstallmentDemandDetails = QUERY_NONZERO_DEMAND_DETAILS
    /* + " AND ptd.egInstallmentMaster = ? " */;

    private static final String EDIT_TYPE_DEMAND = "Demand";
    private static final String EDIT_TYPE_COLLECTION = "Collection";
    private static final String EDIT_TYPE_ADD_INSTALLMENT = "Add Installment";
    private static final String EDIT_TYPE_POSTFIX = "-";

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
                    .getInstallmentListByStartDateToCurrFinYear(dateFormat.parse("01/04/1963"));
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
                            String instRsn = dd.getInstallment().toString().concat(EDIT_TYPE_POSTFIX)
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
            installmentsInOrder = propertyTaxUtil
                    .getInstallmentListByStartDateToCurrFinYear((new ArrayList<Installment>(newInstallments).get(0))
                            .getFromDate());

            if (newInstallments.size() != installmentsInOrder.size()) {
                addActionError(getText("error.editDemand.badInstallmentSelection"));
            }

            Map<String, Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
            if ((newInstallments.contains(currYearInstMap.get(CURRENTYEAR_FIRST_HALF)) && !newInstallments.contains(currYearInstMap.get(CURRENTYEAR_SECOND_HALF))) ||
                    (!newInstallments.contains(currYearInstMap.get(CURRENTYEAR_FIRST_HALF)) && newInstallments.contains(currYearInstMap.get(CURRENTYEAR_SECOND_HALF)))) {
                addActionError(getText("error.currentyearinstallments"));
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
    @Action(value = "/editDemand-newEditForm")
    public String newEditForm() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into newEditForm");
        String resultPage = "";

        if (basicProperty != null && !basicProperty.getSource().equals(PropertyTaxConstants.SOURCEOFDATA_DATAENTRY)
                && !basicProperty.getSource().equals(PropertyTaxConstants.SOURCEOFDATA_MIGRATION)) {
            setErrorMessage(MSG_ERROR_NOT_MIGRATED_PROPERTY);
            resultPage = RESULT_ERROR;
        } else {
            ownerName = basicProperty.getFullOwnerName();
            propertyAddress = basicProperty.getAddress().toString();
            demandDetails = getPersistenceService().findAllBy(queryInstallmentDemandDetails, basicProperty/* , */
            /* propertyTaxUtil.getCurrentInstallment() */);
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

            Installment currentInstallment = propertyTaxUtil.getCurrentInstallment();
            resultPage = RESULT_NEW;
        }

        allInstallments.removeAll(propertyInstallments);
        addDropdownData("allInstallments", allInstallments);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exiting from newEditForm");

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
            LOGGER.debug("createDemandDetailBean - demandDetail= " + demandDetail
                    + "\nExiting from createDemandDetailBean");
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
    @Action(value = "/editDemand-update")
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

        if (isAllRevisedTaxesBlank && isAllRevisedCollectionsBlank && isAllNewActTaxesBlank && isAllNewRvsdTaxesBlank) {
            errorMessage = MSG_ERROR_ALL_FIELDS_BLANK;
            return RESULT_ERROR;
        }

        StringBuilder edits = new StringBuilder(256);
        edits.append(STRING_REMARKS).append(STRING_KEY_SEP).append(remarks);

        Map<Installment, String> installmentTaxEdits = new TreeMap<Installment, String>();
        Map<Installment, String> installmentCollectionEdits = new TreeMap<Installment, String>();

        List<EgDemandDetails> demandDetailsFromDB = getPersistenceService().findAllBy(QUERY_NONZERO_DEMAND_DETAILS,
                basicProperty);
        Installment currentInstallment = propertyTaxUtil.getCurrentInstallment();
        Map<Installment, List<EgDemandDetails>> demandDetails = new TreeMap<Installment, List<EgDemandDetails>>();
        Map<Installment, BigDecimal> baseDemands = new TreeMap<Installment, BigDecimal>();

        String queryZeroDemandDetails = QUERY_DEMAND_DETAILS + " AND dd.amount = 0";

        List<EgDemandDetails> dmdDtlsWithZeroAmt = getPersistenceService().findAllBy(queryZeroDemandDetails,
                basicProperty);
        Set<Installment> zeroInstallments = new TreeSet<Installment>();

        for (DemandDetail dmdDetail : demandDetailBeanList) {
            if ((dmdDetail.getIsNew() != null && dmdDetail.getIsNew()) && dmdDetail.getActualAmount() != null) {
                EgDemandReason egDmdRsn = propertyTaxUtil.getDemandReasonByCodeAndInstallment(
                        demandReasonMap.get(dmdDetail.getReasonMaster()), dmdDetail.getInstallment());
                // PropertyService.createDemandDetails()

                /**
                 * Checking whether already EgDemandDetails exists for this, if
                 * yes updating the same. this may be when taxes updated to 0
                 * and then later adding the installment taxes.
                 */

                EgDemandDetails egDemandDtls = null;

                for (EgDemandDetails details : dmdDtlsWithZeroAmt) {
                    if (details.getEgDemandReason().equals(egDmdRsn)) {
                        zeroInstallments.add(details.getEgDemandReason().getEgInstallmentMaster());
                        details.setAmount(dmdDetail.getActualAmount());
                        details.setAmtCollected((dmdDetail.getActualCollection() == null) ? BigDecimal.ZERO : dmdDetail
                                .getActualCollection());
                        egDemandDtls = details;

                    } else {
                        egDemandDtls = propService.createDemandDetails(dmdDetail.getActualAmount(),
                                dmdDetail.getActualCollection(), egDmdRsn, dmdDetail.getInstallment());
                    }
                }

                if (dmdDtlsWithZeroAmt.isEmpty()) {
                    egDemandDtls = propService.createDemandDetails(dmdDetail.getActualAmount(),
                            dmdDetail.getActualCollection(), egDmdRsn, dmdDetail.getInstallment());
                }

                if (demandDetails.get(dmdDetail.getInstallment()) == null) {
                    List<EgDemandDetails> dmdDtl = new ArrayList<EgDemandDetails>();
                    dmdDtl.add(egDemandDtls);
                    demandDetails.put(dmdDetail.getInstallment(), dmdDtl);
                } else {
                    demandDetails.get(dmdDetail.getInstallment()).add(egDemandDtls);
                }

                if (baseDemands.get(dmdDetail.getInstallment()) == null) {
                    baseDemands.put(dmdDetail.getInstallment(), dmdDetail.getActualAmount());
                } else {
                    baseDemands.put(dmdDetail.getInstallment(),
                            baseDemands.get(dmdDetail.getInstallment()).add(dmdDetail.getActualAmount()));
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
                        buildAuditLog(installmentTaxEdits, ddFromDB.getEgDemandReason().getEgInstallmentMaster(),
                                ddFromDB.getEgDemandReason().getEgDemandReasonMaster().getCode(), ddFromDB.getAmount(),
                                dmdDetail.getRevisedAmount());
                    }

                    if (dmdDetail.getRevisedCollection() != null
                            && ddFromDB.getEgDemand().getEgInstallmentMaster().equals(currentInstallment)
                            && ddFromDB.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                    .equalsIgnoreCase(demandReasonMap.get(dmdDetail.getReasonMaster()))) {

                        Installment inst = (Installment) installmentDAO.findById(dmdDetail.getInstallment().getId(),
                                false);

                        if (ddFromDB.getEgDemandReason().getEgInstallmentMaster().equals(inst)) {
                            isUpdateCollection = true;

                            buildAuditLog(
                                    installmentCollectionEdits,
                                    inst,
                                    ddFromDB.getEgDemandReason().getEgDemandReasonMaster().getCode(),
                                    (ddFromDB.getAmtCollected() == null) ? BigDecimal.ZERO : ddFromDB.getAmtCollected(),
                                    dmdDetail.getRevisedCollection());
                        }
                    }

                    if (isUpdateAmount) {
                        ddFromDB.setAmount(dmdDetail.getRevisedAmount() != null ? dmdDetail.getRevisedAmount()
                                : BigDecimal.ZERO);
                    }

                    if (isUpdateCollection) {
                        ddFromDB.setAmtCollected(dmdDetail.getRevisedCollection() != null ? dmdDetail
                                .getRevisedCollection() : BigDecimal.ZERO);
                    }

                    if (isUpdateAmount || isUpdateCollection) {
                        ddFromDB.setModifiedDate(new Date());
                        getPersistenceService().setType(EgDemandDetails.class);
                        getPersistenceService().update(ddFromDB);
                        break;
                    }
                }
            }
        }

        List<EgDemandDetails> currentInstdemandDetailsFromDB = getPersistenceService().findAllBy(
                queryInstallmentDemandDetails, basicProperty/*
                                                             * ,
                                                             * propertyTaxUtil.
                                                             * getCurrentInstallment
                                                             * ()
                                                             */);

        EgDemand currentPtdemand = null;
        if (!currentInstdemandDetailsFromDB.isEmpty())
            currentPtdemand = currentInstdemandDetailsFromDB.get(0).getEgDemand();

        Map<Installment, Set<EgDemandDetails>> demandDetailsSetByInstallment = getEgDemandDetailsSetByInstallment(currentInstdemandDetailsFromDB);
        List<Installment> installments = new ArrayList<Installment>(demandDetailsSetByInstallment.keySet());
        Collections.sort(installments);

        for (Installment inst : installments) {
            Map<String, BigDecimal> dmdRsnAmt = new LinkedHashMap<String, BigDecimal>();
            for (String rsn : DEMAND_RSNS_LIST) {
                EgDemandDetails newDmndDtls = propService.getEgDemandDetailsForReason(
                        demandDetailsSetByInstallment.get(inst), rsn);
                if (newDmndDtls != null && newDmndDtls.getAmtCollected() != null) {
                    BigDecimal extraCollAmt = newDmndDtls.getAmtCollected().subtract(newDmndDtls.getAmount());
                    // If there is extraColl then add to map
                    if (extraCollAmt.compareTo(BigDecimal.ZERO) > 0) {
                        dmdRsnAmt
                                .put(newDmndDtls.getEgDemandReason().getEgDemandReasonMaster().getCode(), extraCollAmt);
                        newDmndDtls.setAmtCollected(newDmndDtls.getAmtCollected().subtract(extraCollAmt));
                        newDmndDtls.setModifiedDate(new Date());
                        getPersistenceService().setType(EgDemandDetails.class);
                        getPersistenceService().update(newDmndDtls);
                    }
                }
            }
            propService.getExcessCollAmtMap().put(inst, dmdRsnAmt);
        }

        LOGGER.info("Excess Collection - " + propService.getExcessCollAmtMap());

        /*
         * propService.adjustExcessCollectionAmount(installments,
         * demandDetailsSetByInstallment, (Ptdemand) currentPtdemand);
         */

        edits.append(AUDITDATA_STRING_SEP).append(EDIT_TYPE_DEMAND).append(EDIT_TYPE_POSTFIX);

        for (Map.Entry<Installment, String> instTaxEdit : installmentTaxEdits.entrySet()) {
            edits.append(AUDITDATA_STRING_SEP).append(instTaxEdit.getKey()).append(STRING_KEY_SEP)
                    .append(instTaxEdit.getValue());
        }

        if (!installmentCollectionEdits.isEmpty()) {
            edits.append(AUDITDATA_STRING_SEP).append(EDIT_TYPE_COLLECTION).append(EDIT_TYPE_POSTFIX);
        }

        for (Map.Entry<Installment, String> instCollectionEdit : installmentCollectionEdits.entrySet()) {
            edits.append(AUDITDATA_STRING_SEP).append(instCollectionEdit.getKey()).append(STRING_KEY_SEP)
                    .append(instCollectionEdit.getValue());
        }

        Map<Installment, BigDecimal> baseDemand = new HashMap<Installment, BigDecimal>();
        for (EgDemandDetails dd : demandDetailsFromDB) {
            if (baseDemand.get(dd.getEgDemand().getEgInstallmentMaster()) == null) {
                baseDemand.put(dd.getEgDemand().getEgInstallmentMaster(), dd.getAmount());
            } else {
                baseDemand.put(dd.getEgDemand().getEgInstallmentMaster(),
                        baseDemand.get(dd.getEgDemand().getEgInstallmentMaster()).add(dd.getAmount()));
            }
        }

        for (Ptdemand ptdemand : basicProperty.getProperty().getPtDemandSet()) {

            ptdemand.setBaseDemand(baseDemand.get(ptdemand.getEgInstallmentMaster()));

            if (ptdemand.getEgInstallmentMaster().equals(currentInstallment)) {
                for (Map.Entry<Installment, List<EgDemandDetails>> entry : demandDetails.entrySet()) {

                    if (entry.getKey().equals(currentInstallment)) {
                        ptdemand.getEgDemandDetails().addAll(entry.getValue());
                    } else {
                        if (!zeroInstallments.contains(entry.getKey())) {
                            for (EgDemandDetails dd : entry.getValue()) {
                                ptdemand.getEgDemandDetails().add((EgDemandDetails) dd.clone());
                            }
                        }
                    }
                    ptdemand.setBaseDemand(ptdemand.getBaseDemand().add(baseDemands.get(entry.getKey())));
                }
            } else {
                List<EgDemandDetails> dmdDetails = demandDetails.get(ptdemand.getEgInstallmentMaster());
                if (dmdDetails != null
                        && dmdDetails.get(0).getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()
                                .equalsIgnoreCase(DEMANDRSN_STR_CHQ_BOUNCE_PENALTY)) {
                    if (!zeroInstallments.contains(ptdemand.getEgInstallmentMaster())) {
                        ptdemand.getEgDemandDetails().addAll(dmdDetails);
                        ptdemand.setBaseDemand(ptdemand.getBaseDemand().add(
                                baseDemands.get(ptdemand.getEgInstallmentMaster())));
                    }
                }
            }

            getPersistenceService().setType(Ptdemand.class);
            getPersistenceService().update(ptdemand);
        }

        Map<Installment, String> addedInstallments = new TreeMap<Installment, String>();

        for (Map.Entry<Installment, List<EgDemandDetails>> entry : demandDetails.entrySet()) {
            if (!entry.getValue().get(0).getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()
                    .equalsIgnoreCase(DEMANDRSN_STR_CHQ_BOUNCE_PENALTY)) {

                for (EgDemandDetails dmdDetails : entry.getValue()) {

                    buildAuditLog(addedInstallments, dmdDetails.getEgDemandReason().getEgInstallmentMaster(),
                            dmdDetails.getEgDemandReason().getEgDemandReasonMaster().getCode(), dmdDetails.getAmount(),
                            BigDecimal.ZERO);

                }

                // If the newly added installment Ptdemand is not present then
                // creating it,
                // else updating the existing installment Ptdemand
                if (!zeroInstallments.contains(entry.getKey())) {
                    Ptdemand ptDemand = new Ptdemand();
                    PTDemandCalculations ptDmdCalc = new PTDemandCalculations();

                    ptDemand.setEgInstallmentMaster(entry.getKey());
                    ptDemand.setEgDemandDetails(new HashSet<EgDemandDetails>(entry.getValue()));
                    ptDemand.setBaseDemand(baseDemands.get(entry.getKey()));
                    ptDemand.setCreateDate(new Date());
                    ptDemand.setModifiedDate(new Date());
                    ptDemand.setIsHistory("N");
                    ptDemand.setEgptProperty((PropertyImpl) basicProperty.getProperty());

                    ptDmdCalc.setPtDemand(ptDemand);

                    ptDemand.setDmdCalculations(ptDmdCalc);
                    getPersistenceService().applyAuditing(ptDmdCalc);
                    basicProperty.getProperty().getPtDemandSet().add(ptDemand);
                }

                getPersistenceService().setType(PropertyImpl.class);
                getPersistenceService().update(basicProperty.getProperty());
            }
        }

        if (!addedInstallments.isEmpty()) {
            edits.append(AUDITDATA_STRING_SEP).append(EDIT_TYPE_ADD_INSTALLMENT).append(EDIT_TYPE_POSTFIX);
        }

        for (Map.Entry<Installment, String> inst : addedInstallments.entrySet()) {
            edits.append(AUDITDATA_STRING_SEP).append(inst.getKey()).append(STRING_KEY_SEP).append(inst.getValue());
        }
        // Auditing is removed
        // propertyTaxUtil.generateAuditEvent(EDIT_DEMAND_AUDIT_ACTION,
        // basicProperty, edits.toString(), null);
        LOGGER.info("Exiting from update");
        return RESULT_ACK;
    }

    public void buildAuditLog(Map<Installment, String> installmentEdits, Installment installment, String reasonCode,
            BigDecimal actualAmount, BigDecimal revisedAmount) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into buildAuditLog");

        // amountEdits = TAX_NAME | OLD_TAX | NEW_TAX |
        String amountEdits = reasonCode.concat(STRING_VALUE_SEP).concat(actualAmount.toString())
                .concat(STRING_VALUE_SEP).concat(revisedAmount.toString()).concat(STRING_VALUE_SEP);

        if (installmentEdits.get(installment) == null
                || !installmentEdits.get(installment).equalsIgnoreCase(amountEdits)) {
            // logic to add the Installment only once
            // if its there just get the 'amountEdits' and append the new
            // 'amountEdits'
            if (installmentEdits.get(installment) == null) {
                installmentEdits.put(installment, amountEdits);
            } else {
                if (!installmentEdits.get(installment).contains(reasonCode)) {
                    installmentEdits.put(installment, installmentEdits.get(installment).concat(amountEdits));
                }
            }
        }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exiting from buildAuditLog");
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
