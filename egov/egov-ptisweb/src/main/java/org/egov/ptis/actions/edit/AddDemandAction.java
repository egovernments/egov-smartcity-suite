/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.ptis.actions.edit;

import static org.egov.ptis.constants.PropertyTaxConstants.BUILTUP_PROPERTY_DMDRSN_CODE_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_FIRST_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_SECOND_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_VACANT_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMAND_REASON_ORDER_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMAND_RSNS_LIST;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.VACANT_PROPERTY_DMDRSN_CODE_MAP;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
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

    private static final String INSTALLMENT2 = "installment";
    /**
     *
     */
    private static final long serialVersionUID = -1690171613818493667L;
    private static final String BASIC_PROPERTY = "basicProperty";
    private static final Logger LOGGER = Logger.getLogger(AddDemandAction.class);
    protected static final String RESULT_NEW = "addForm";
    protected static final String RESULT_ERROR = "error";
    protected static final String RESULT_ACK = "ack";
    private static final String MSG_ERROR_NOT_MIGRATED_PROPERTY = " This is not a migrated property ";

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
    private transient BasicPropertyDAO basicPropertyDAO;
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
    @PersistenceContext
    private EntityManager entityManager;

    DemandAudit demandAudit = new DemandAudit();

    private List<EgDemandDetails> demandDetails = new ArrayList<>();
    private List<DemandDetail> demandDetailBeanList = new ArrayList<>();
    private List<Installment> allInstallments = new ArrayList<>();
    private final Set<Installment> propertyInstallments = new TreeSet<>();
    private Map<Installment, Map<String, Boolean>> collectionDetails = new HashMap<>();
    private Map<String, String> demandReasonMap = new HashMap<>();

    @Override
    public Object getModel() {
        return demandDetailBeanList;
    }

    @Override
    @SkipValidation
    public void prepare() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into prepare");
        basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(propertyId);
        if (null != basicProperty.getActiveProperty())
            if (basicProperty.getActiveProperty().getPropertyDetail().getPropertyTypeMaster().getCode()
                    .equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
                demandReasonMap = VACANT_PROPERTY_DMDRSN_CODE_MAP;
            else
                demandReasonMap = BUILTUP_PROPERTY_DMDRSN_CODE_MAP;

        for (final DemandDetail dd : demandDetailBeanList)
            if (dd.getInstallment() != null && dd.getInstallment().getId() != null
                    && !dd.getInstallment().getId().equals(-1)) {
                dd.setInstallment(installmentDAO.findById(dd.getInstallment().getId(), false));
                if (!dd.getIsNew())
                    propertyInstallments.add(dd.getInstallment());
            }

        final DateFormat dateFormat = new SimpleDateFormat(PropertyTaxConstants.DATE_FORMAT_DDMMYYY);
        try {
            allInstallments = propertyTaxUtil
                    .getInstallmentListByStartDateToCurrFinYearDesc(dateFormat.parse("01/04/1963"));
        } catch (final ParseException e) {
            throw new ApplicationRuntimeException("Error while getting all installments from start date", e);
        }

        allInstallments.removeAll(propertyInstallments);

        addDropdownData("allInstallments", allInstallments);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exiting from prepare");
    }

    @Override
    public void validate() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into validate");
        final Set<Installment> newInstallments = new TreeSet<>();
        final Set<String> installmentsChqPenalty = new TreeSet<>();
        final Set<String> instDmdRsnMaster = new HashSet<>();
        List<String> instString;
        final Set<String> actAmtInstallments = new TreeSet<>();
        List<String> errorParams = null;

        for (final DemandDetail dd : demandDetailBeanList)
            if (dd.getIsNew() != null && dd.getIsNew()) {
                instString = new ArrayList<>();
                instString.add(dd.getReasonMaster());
                if ((PropertyTaxConstants.NON_VACANT_TAX_DEMAND_REASONS.contains(dd.getReasonMaster())
                        || dd.getReasonMaster().equalsIgnoreCase(DEMANDRSN_STR_VACANT_TAX))
                        && (dd.getInstallment().getId() == null
                                || dd.getInstallment().getId().equals(-1)))
                    addActionError(getText("error.editDemand.selectInstallment"));

                if (null != dd.getInstallment().getId() && !dd.getInstallment().getId().equals(-1)) {
                    if (null == dd.getActualAmount())
                        addActionError(getText("error.editDemand.actualAmount", instString));
                    if (null != dd.getActualAmount() && null != dd.getActualCollection() &&
                            dd.getActualAmount().intValue() < dd.getActualCollection().intValue())
                        addActionError(getText("error.collection.greaterThan.actualAmount"));
                }

                if (dd.getActualAmount() == null) {
                    if (dd.getActualCollection() != null)
                        actAmtInstallments.add(dd.getInstallment().getDescription());
                } else if (PropertyTaxConstants.NON_VACANT_TAX_DEMAND_REASONS.contains(dd.getReasonMaster())
                        || dd.getReasonMaster().equalsIgnoreCase(DEMANDRSN_STR_VACANT_TAX))
                    if (dd.getInstallment().getId() == null || dd.getInstallment().getId().equals(-1))
                        addActionError(getText("error.editDemand.selectInstallment"));
                    else {
                        newInstallments.add(dd.getInstallment());
                        final String instRsn = dd.getInstallment().toString().concat(ADD_TYPE_POSTFIX)
                                .concat(dd.getReasonMaster());
                        if (!instDmdRsnMaster.add(instRsn)) {
                            instString.add(dd.getInstallment().toString());
                            addActionError(getText("error.editDemand.duplicateInstallment", instString));
                        }
                    }
            } else {
                newInstallments.add(dd.getInstallment());

                if (null != dd.getRevisedAmount() && dd.getRevisedAmount().compareTo(BigDecimal.ZERO) == 0
                        && dd.getActualCollection().compareTo(BigDecimal.ZERO) > 0 && dd.getRevisedCollection() == null) {
                    errorParams = new ArrayList<>();
                    errorParams.add(dd.getReasonMaster());
                    errorParams.add(dd.getInstallment().getDescription());
                    addActionError(getText("error.editDemand.collectionForUpdatedDemand", errorParams));
                }
                if (null != dd.getRevisedAmount() && null != dd.getActualCollection()
                        && dd.getRevisedAmount().intValue() < dd.getActualCollection().intValue())
                    addActionError(getText("error.collection.greaterThan.revisedAmount"));
                if (null != dd.getRevisedAmount() && null != dd.getRevisedCollection()
                        && dd.getRevisedAmount().intValue() < dd.getRevisedCollection().intValue())
                    addActionError(getText("error.revisedCollecion.greaterThan.revisedAmount"));
            }

        if (!actAmtInstallments.isEmpty()) {
            final String inst = actAmtInstallments.toString().replace('[', ' ').replace(']', ' ');
            final List<String> instStrings = new ArrayList<>();
            instStrings.add(inst);
            addActionError(getText("error.editDemand.actualAmount", instStrings));
        }

        List<Installment> installmentsInOrder = null;
        if (!newInstallments.isEmpty()) {
            installmentsInOrder = propertyTaxUtil.getInstallmentListByStartDateToCurrFinYearDesc(
                    new ArrayList<Installment>(newInstallments).get(0).getFromDate());

            if (newInstallments.size() != installmentsInOrder.size())
                addActionError(getText("error.editDemand.badInstallmentSelection"));

            final Date currDate = new Date();
            final Map<String, Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(currDate);
            if (!DateUtils.compareDates(currDate, currYearInstMap.get(CURRENTYEAR_SECOND_HALF).getFromDate())) {
                if (newInstallments.contains(currYearInstMap.get(CURRENTYEAR_FIRST_HALF))
                        && !newInstallments.contains(currYearInstMap.get(CURRENTYEAR_SECOND_HALF))
                        || !newInstallments.contains(currYearInstMap.get(CURRENTYEAR_FIRST_HALF))
                                && newInstallments.contains(currYearInstMap.get(CURRENTYEAR_SECOND_HALF)))
                    addActionError(getText("error.currentyearinstallments"));
            } else if (!newInstallments.contains(currYearInstMap.get(CURRENTYEAR_SECOND_HALF)))
                addActionError(getText("error.currentInst"));

        }

        if (!installmentsChqPenalty.isEmpty()) {
            final String inst = installmentsChqPenalty.toString().replace('[', ' ').replace(']', ' ');
            final List<String> instStrings = new ArrayList<>();
            instStrings.add(inst);
            addActionError(getText("error.editDemand.chqBouncePenaltyIsZero", instStrings));
        }

        if (StringUtils.isBlank(remarks))
            addActionError(getText("mandatory.editDmdCollRemarks"));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exiting from validate");
    }

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
        } else if (basicProperty != null){
            ownerName = basicProperty.getFullOwnerName();
            propertyAddress = basicProperty.getAddress().toString();
            final Query qry = entityManager.createNamedQuery("QUERY_DEMAND_DETAILS_FOR_CURRINST");
            qry.setParameter(BASIC_PROPERTY, basicProperty);
            qry.setParameter(INSTALLMENT2, propertyTaxCommonUtils.getCurrentInstallment());
            demandDetails = qry.getResultList();
            if (!demandDetails.isEmpty())
                Collections.sort(demandDetails, (o1, o2) -> o2.getEgDemandReason().getEgInstallmentMaster()
                        .compareTo(o1.getEgDemandReason().getEgInstallmentMaster()));

            final PropertyTaxBillable billable = new PropertyTaxBillable();
            billable.setBasicProperty(basicProperty);
            final Map<Installment, List<String>> installmentDemandReason = new HashMap<>();

            if (!demandDetails.isEmpty())
                for (final EgDemandDetails demandDetail : demandDetails) {
                    final Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();
                    final String reasonMaster = demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster();
                    propertyInstallments.add(installment);
                    if (installmentDemandReason.get(installment) == null) {
                        final List<String> rsns = new ArrayList<>();
                        rsns.add(reasonMaster);
                        installmentDemandReason.put(installment, rsns);
                    } else
                        installmentDemandReason.get(installment).add(reasonMaster);

                    final DemandDetail dmdDtl = createDemandDetailBean(installment, reasonMaster, propertyTaxCommonUtils.getTotalDemandVariationAmount(demandDetail),
                            demandDetail.getAmtCollected(), false);
                    demandDetailBeanList.add(dmdDtl);
                }
            else
                for (final Map.Entry<String, String> entry : demandReasonMap.entrySet()) {
                    final DemandDetail dmdDtl = createDemandDetailBean(null, entry.getKey(), null, null, true);
                    demandDetailBeanList.add(dmdDtl);
                }
            resultPage = RESULT_NEW;
        }

        allInstallments.removeAll(propertyInstallments);
        addDropdownData("allInstallments", allInstallments);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exiting from newAddForm");

        return resultPage;
    }

    private DemandDetail createDemandDetailBean(final Installment installment, final String reasonMaster, final BigDecimal amount,
            final BigDecimal amountCollected, final Boolean isNew) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into createDemandDetailBean");
            LOGGER.debug("createDemandDetailBean - installment=" + installment + ", reasonMaster=" + reasonMaster
                    + ", amount=" + amount + ", amountCollected=" + amountCollected);
        }

        final DemandDetail demandDetail = new DemandDetail();
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
     * To set DcbDispInfo with ReasonCategoryCodes as Tax and Penalty. Here reasonMasterCodes could also be set to DcbDispInfo.
     *
     * @return DCBDisplayInfo
     */

    @SkipValidation
    private void prepareDisplayInfo() {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into method prepareDisplayInfo");
        dcbDispInfo = new DCBDisplayInfo();
        dcbDispInfo.setReasonCategoryCodes(Collections.<String> emptyList());
        final List<String> reasonList = new ArrayList<>();
        reasonList.addAll(DEMAND_REASON_ORDER_MAP.keySet());
        dcbDispInfo.setReasonMasterCodes(reasonList);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("DCB Display Info : " + dcbDispInfo);
            LOGGER.debug("Number of Demand Reasons : " + reasonList.size());
            LOGGER.debug("Exit from method prepareDisplayInfo");
        }
    }

    @SuppressWarnings("unchecked")
    @ValidationErrorPage(value = RESULT_NEW)
    @Action(value = "/addDemand-update")
    public String update() {
        if (LOGGER.isDebugEnabled())
            LOGGER.info("Entered into update, basicProperty=" + basicProperty);
        final Query qry = entityManager.createNamedQuery("QUERY_DEMAND_DET_NON_ZERODEMAND");
        qry.setParameter(BASIC_PROPERTY, basicProperty);
        final List<EgDemandDetails> demandDetailsFromDB = qry.getResultList();

        final Installment currentInstallment = propertyTaxCommonUtils.getCurrentInstallment();
        final Map<Installment, List<EgDemandDetails>> demandDetails = new TreeMap<>();

        final Query qry1 = entityManager.createNamedQuery("QUERY_DEMAND_DET_WITH_ZERODEMAND");
        qry1.setParameter(BASIC_PROPERTY, basicProperty);
        final List<EgDemandDetails> dmdDtlsWithZeroAmt = qry1.getResultList();
        final Set<Installment> zeroInstallments = new TreeSet<>();

        BigDecimal totalDmd = BigDecimal.ZERO;
        EgDemandDetails egDemandDtls = null;

        demandAudit.setBasicproperty(basicProperty.getUpicNo());
        demandAudit.setTransaction(ADD_DEMAND);
        demandAudit.setRemarks(remarks);
        demandAudit.setLastModifiedDate(new Date());

        for (final DemandDetail dmdDetail : demandDetailBeanList)
            if (dmdDetail.getIsNew() != null && dmdDetail.getIsNew() && dmdDetail.getActualAmount() != null) {
                final EgDemandReason egDmdRsn = propertyTaxUtil.getDemandReasonByCodeAndInstallment(
                        demandReasonMap.get(dmdDetail.getReasonMaster()), dmdDetail.getInstallment());

                /**
                 * Checking whether already EgDemandDetails exists for this, if yes updating the same. this may be when taxes
                 * updated to 0 and then later adding the installment taxes.
                 */

                for (final EgDemandDetails details : dmdDtlsWithZeroAmt)
                    if (details.getEgDemandReason().equals(egDmdRsn)) {
                        zeroInstallments.add(details.getEgDemandReason().getEgInstallmentMaster());
                        details.setAmount(dmdDetail.getActualAmount());
                        details.setAmtCollected(dmdDetail.getActualCollection() == null ? BigDecimal.ZERO
                                : dmdDetail.getActualCollection());
                        egDemandDtls = details;

                    } else if (dmdDetail.getActualAmount().compareTo(BigDecimal.ZERO) != 0 && dmdDetail.getIsNew()) {
                        egDemandDtls = propService.createDemandDetails(dmdDetail.getActualAmount(),
                                dmdDetail.getActualCollection(), egDmdRsn, dmdDetail.getInstallment());
                        totalDmd = totalDmd.add(propertyTaxCommonUtils.getTotalDemandVariationAmount(egDemandDtls));
                    }

                if (dmdDtlsWithZeroAmt.isEmpty() && dmdDetail.getActualAmount().compareTo(BigDecimal.ZERO) != 0
                        && dmdDetail.getIsNew()) {
                    egDemandDtls = propService.createDemandDetails(dmdDetail.getActualAmount(),
                            dmdDetail.getActualCollection(), egDmdRsn, dmdDetail.getInstallment());
                    totalDmd = totalDmd.add(propertyTaxCommonUtils.getTotalDemandVariationAmount(egDemandDtls));
                }
                logAudit(dmdDetail);
                final List<EgDemandDetails> dmdDtl = new ArrayList<>();
                if (demandDetails.get(dmdDetail.getInstallment()) == null) {

                    dmdDtl.add(egDemandDtls);
                    demandDetails.put(dmdDetail.getInstallment(), dmdDtl);
                } else
                    demandDetails.get(dmdDetail.getInstallment()).add(egDemandDtls);
            }

        for (final EgDemandDetails ddFromDB : demandDetailsFromDB)
            for (final DemandDetail dmdDetail : demandDetailBeanList)
                if (dmdDetail.getIsNew() != null && !dmdDetail.getIsNew()) {
                    Boolean isUpdateAmount = false;
                    Boolean isUpdateCollection = false;

                    if (dmdDetail.getRevisedAmount() != null
                            && dmdDetail.getInstallment().equals(ddFromDB.getEgDemandReason().getEgInstallmentMaster())
                            && ddFromDB.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                    .equalsIgnoreCase(demandReasonMap.get(dmdDetail.getReasonMaster())))
                        isUpdateAmount = true;

                    if (dmdDetail.getRevisedCollection() != null
                            && ddFromDB.getEgDemand().getEgInstallmentMaster().equals(currentInstallment)
                            && ddFromDB.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                    .equalsIgnoreCase(demandReasonMap.get(dmdDetail.getReasonMaster()))) {

                        final Installment inst = installmentDAO.findById(dmdDetail.getInstallment().getId(),
                                false);

                        if (ddFromDB.getEgDemandReason().getEgInstallmentMaster().equals(inst))
                            isUpdateCollection = true;
                    }

                    if (isUpdateAmount)
                        ddFromDB.setAmount(
                                dmdDetail.getRevisedAmount() != null ? dmdDetail.getRevisedAmount() : BigDecimal.ZERO);

                    if (isUpdateCollection)
                        ddFromDB.setAmtCollected(dmdDetail.getRevisedCollection() != null
                                ? dmdDetail.getRevisedCollection() : BigDecimal.ZERO);

                    if (isUpdateAmount || isUpdateCollection) {
                        ddFromDB.setModifiedDate(new Date());
                        logAudit(dmdDetail);
                        demandDetailsDao.update(ddFromDB);

                        break;
                    }
                }
        if (!demandAudit.getDemandAuditDetails().isEmpty())
            demandAuditService.saveDetails(demandAudit);
        final Query query = entityManager.createNamedQuery("QUERY_DEMAND_DETAILS_FOR_CURRINST");
        query.setParameter(BASIC_PROPERTY, basicProperty);
        query.setParameter(INSTALLMENT2, propertyTaxCommonUtils.getCurrentInstallment());
        final List<EgDemandDetails> currentInstdemandDetailsFromDB = query.getResultList();

        final Map<Installment, Set<EgDemandDetails>> demandDetailsSetByInstallment = getEgDemandDetailsSetByInstallment(
                currentInstdemandDetailsFromDB);
        final List<Installment> installments = new ArrayList<>(demandDetailsSetByInstallment.keySet());
        Collections.sort(installments);

        for (final Installment inst : installments) {
            final Map<String, BigDecimal> dmdRsnAmt = new LinkedHashMap<>();
            for (final String rsn : DEMAND_RSNS_LIST) {
                final EgDemandDetails newDmndDtls = propService
                        .getEgDemandDetailsForReason(demandDetailsSetByInstallment.get(inst), rsn);
                if (newDmndDtls != null && newDmndDtls.getAmtCollected() != null) {
                    final BigDecimal extraCollAmt = newDmndDtls.getAmtCollected().subtract(propertyTaxCommonUtils.getTotalDemandVariationAmount(newDmndDtls));
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
        final Set<EgDemandDetails> demandDetailsToBeSaved = new HashSet<>();
        for (final Map.Entry<Installment, List<EgDemandDetails>> entry : demandDetails.entrySet())
            if (!entry.getValue().get(0).getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()
                    .equalsIgnoreCase(DEMANDRSN_STR_CHQ_BOUNCE_PENALTY))
                demandDetailsToBeSaved.addAll(new HashSet<EgDemandDetails>(entry.getValue()));
        final Query query1 = entityManager.createNamedQuery("QUERY_CURRENT_PTDEMAND");
        query1.setParameter(BASIC_PROPERTY, basicProperty);
        query1.setParameter(INSTALLMENT2, propertyTaxCommonUtils.getCurrentInstallment());
        final List<Ptdemand> currPtdemand = query1.getResultList();
        if (currPtdemand.isEmpty()) {
            final Ptdemand ptDemand = new Ptdemand();
            final PTDemandCalculations ptDmdCalc = new PTDemandCalculations();

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
            final Ptdemand ptdemand = currPtdemand.get(0);
            ptdemand.setBaseDemand(ptdemand.getBaseDemand() != null ? ptdemand.getBaseDemand().add(totalDmd) : totalDmd);
            ptdemand.getEgDemandDetails().addAll(demandDetailsToBeSaved);
            getPersistenceService().applyAuditing(ptdemand.getDmdCalculations());
            basicProperty.getProperty().getPtDemandSet().add(ptdemand);
        }

        propertyImplService.update(basicProperty.getProperty());

        LOGGER.info("Exiting from update");
        return RESULT_ACK;
    }

    public Map<Installment, Set<EgDemandDetails>> getEgDemandDetailsSetByInstallment(final List<EgDemandDetails> demandDtls) {
        final Map<Installment, Set<EgDemandDetails>> newEgDemandDetailsSetByInstallment = new HashMap<>();

        for (final EgDemandDetails dd : demandDtls) {

            if (dd.getAmtCollected() == null)
                dd.setAmtCollected(BigDecimal.ZERO);

            if (newEgDemandDetailsSetByInstallment.get(dd.getEgDemandReason().getEgInstallmentMaster()) == null) {
                final Set<EgDemandDetails> ddSet = new HashSet<>();
                ddSet.add(dd);
                newEgDemandDetailsSetByInstallment.put(dd.getEgDemandReason().getEgInstallmentMaster(), ddSet);
            } else
                newEgDemandDetailsSetByInstallment.get(dd.getEgDemandReason().getEgInstallmentMaster()).add(dd);
        }
        return newEgDemandDetailsSetByInstallment;
    }

    private void logAudit(final DemandDetail dmdDetail) {

        final DemandAuditDetails dmdAdtDtls = new DemandAuditDetails();
        dmdAdtDtls.setYear(dmdDetail.getInstallment().toString());
        dmdAdtDtls.setAction(dmdDetail.getIsNew() ? "Add" : "Edit");
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

    public void setPropertyId(final String propertyId) {
        this.propertyId = propertyId;
    }

    public List<EgDemandDetails> getDemandDetails() {
        return demandDetails;
    }

    public void setDemandDetails(final List<EgDemandDetails> demandDetails) {
        this.demandDetails = demandDetails;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(final String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public PropertyTaxUtil getPropertyTaxUtil() {
        return propertyTaxUtil;
    }

    public void setPropertyTaxUtil(final PropertyTaxUtil propertyTaxUtil) {
        this.propertyTaxUtil = propertyTaxUtil;
    }

    public PropertyService getPropService() {
        return propService;
    }

    public void setPropService(final PropertyService propService) {
        this.propService = propService;
    }

    public Map<Installment, Map<String, Boolean>> getCollectionDetails() {
        return collectionDetails;
    }

    public void setCollectionDetails(final Map<Installment, Map<String, Boolean>> collectionDetails) {
        this.collectionDetails = collectionDetails;
    }

    public List<DemandDetail> getDemandDetailBeanList() {
        return demandDetailBeanList;
    }

    public void setDemandDetailBeanList(final List<DemandDetail> demandDetailBeanList) {
        this.demandDetailBeanList = demandDetailBeanList;
    }

    public List<Installment> getAllInstallments() {
        return allInstallments;
    }

    public void setAllInstallments(final List<Installment> allInstallments) {
        this.allInstallments = allInstallments;
    }

    public Map<String, String> getDemandReasonMap() {
        return demandReasonMap;
    }

    public void setDemandReasonMap(final Map<String, String> demandReasonMap) {
        this.demandReasonMap = demandReasonMap;
    }

}
