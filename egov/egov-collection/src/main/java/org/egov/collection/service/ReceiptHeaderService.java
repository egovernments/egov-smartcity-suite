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
package org.egov.collection.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.AccountPayeeDetail;
import org.egov.collection.entity.Challan;
import org.egov.collection.entity.CollectionIndex;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.entity.ReceiptMisc;
import org.egov.collection.entity.ReceiptVoucher;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.egov.collection.integration.services.BillingIntegrationService;
import org.egov.collection.utils.CollectionsNumberGenerator;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.collection.utils.FinancialsUtil;
import org.egov.collection.utils.es.CollectionIndexUtils;
import org.egov.commons.Bankaccount;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.entity.Jurisdiction;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EmployeeService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.engine.ReportConstants;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentType;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provides services related to receipt header
 */
@Transactional(readOnly = true)
public class ReceiptHeaderService extends PersistenceService<ReceiptHeader, Long> {

    private static final Logger LOGGER = Logger.getLogger(ReceiptHeaderService.class);
    private CollectionsUtil collectionsUtil;
    private CollectionsNumberGenerator collectionsNumberGenerator;
    private FinancialsUtil financialsUtil;
    private PersistenceService persistenceService;

    @Autowired
    private DesignationService designationService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private AssignmentService assignmentService;

    private ChallanService challanService;

    @Autowired
    private CollectionIndexService collectionIndexService;

    @Autowired
    private CollectionIndexUtils collectionIndexUtils;

    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    public ReceiptHeaderService() {
        super(ReceiptHeader.class);
    }

    public ReceiptHeaderService(final Class<ReceiptHeader> type) {
        super(type);
    }

    /**
     * @param statusCode
     *            Status code of receipts to be fetched. If null or ALL, then
     *            receipts with all statuses are fetched
     * @param userName
     *            User name of the user who has created the receipts. If null or
     *            ALL, then receipts of all users are fetched
     * @param counterId
     *            Counter id on which the receipts were created. If negative,
     *            then receipts from all counters are fetched
     * @param serviceCode
     *            Service code for which the receipts were created. If null or
     *            ALL, then receipts of all billing services are fetched
     * @return List of all receipts created by given user from given counter id
     *         and having given status
     */
    public List<ReceiptHeader> findAllByPositionAndInboxItemDetails(final List<Long> positionIds,
            final String groupingCriteria) {
        final StringBuilder query = new StringBuilder(
                "from org.egov.collection.entity.ReceiptHeader where 1=1 and state.value != 'END' and state.status != 2");
        String wfAction = null;
        String serviceCode = null;
        String userName = null;
        String receiptDate = null;
        String receiptType = null;
        Integer counterId = null;
        final String params[] = groupingCriteria.split(CollectionConstants.SEPARATOR_HYPHEN, -1);
        if (params.length == 6) {
            wfAction = params[0];
            serviceCode = params[1];
            userName = params[2];
            counterId = Integer.valueOf(params[4]);
            receiptDate = params[3];
            receiptType = params[5];
        }
        final boolean allCounters = counterId == null || counterId < 0;
        // final boolean allPositions = positionIds == null ||
        // positionIds.equals(CollectionConstants.ALL);
        final boolean allServices = serviceCode == null || serviceCode.equals(CollectionConstants.ALL);
        final boolean allWfAction = wfAction == null || wfAction.equals(CollectionConstants.ALL);
        final boolean allUserName = userName == null || userName.equals(CollectionConstants.ALL);
        final boolean allDate = receiptDate == null || receiptDate.equals(CollectionConstants.ALL);
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date rcptDate = null;
        try {
            rcptDate = formatter.parse(receiptDate);
        } catch (final ParseException e) {
            LOGGER.error("Exception while parsing ReceiptDate" + e.getMessage());
        }

        // if (!allPositions)
        query.append(" and state.ownerPosition.id in :positionIds");
        if (!allCounters)
            query.append(" and location.id = :counterId");
        if (!allServices && receiptType.equals(CollectionConstants.SERVICE_TYPE_BILLING))
            query.append(" and service.code = :serviceCode");
        if (!allWfAction)
            query.append(" and state.nextAction = :wfAction");
        if (!allUserName)
            query.append(" and createdBy.username = :userName");
        if (!allDate)
            query.append(" and (cast(receiptDate as date)) = :rcptDate");
        if (receiptType.equals(CollectionConstants.SERVICE_TYPE_BILLING))
            query.append(" and receipttype = :receiptType");
        else
            query.append(" and receipttype in ('A', 'C')");
        query.append(" order by receiptdate  desc");
        final Query listQuery = getSession().createQuery(query.toString());

        // if (!allPositions)
        listQuery.setParameterList("positionIds", positionIds);
        if (!allCounters)
            listQuery.setInteger("counterId", counterId);
        if (!allServices && receiptType.equals(CollectionConstants.SERVICE_TYPE_BILLING))
            listQuery.setString("serviceCode", serviceCode);
        if (!allWfAction)
            listQuery.setString("wfAction", wfAction);
        if (!allUserName)
            listQuery.setString("userName", userName);
        if (!allDate)
            listQuery.setDate("rcptDate", rcptDate);
        if (receiptType.equals(CollectionConstants.SERVICE_TYPE_BILLING))
            listQuery.setCharacter("receiptType", receiptType.charAt(0));
        return listQuery.list();
    }

    /**
     * This method is called for voucher creation into the financial system. For
     * each receipt created in the collections module, a voucher is created.
     *
     * @param receiptHeader
     *            Receipt header for which the pre-approval voucher is to be
     *            created
     * @param receiptBulkUpload
     * @return The created voucher
     */

    protected CVoucherHeader createVoucher(final ReceiptHeader receiptHeader) {
        final HashMap<String, Object> headerdetails = new HashMap<>(0);
        final List<HashMap<String, Object>> accountCodeList = new ArrayList<>(0);
        final List<HashMap<String, Object>> subledgerList = new ArrayList<>(0);
        String fundCode = null;
        String fundsourceCode = null;
        String departmentCode = null;
        Boolean isVoucherApproved = Boolean.FALSE;

        if (receiptHeader.getService().getIsVoucherApproved() != null)
            isVoucherApproved = receiptHeader.getService().getIsVoucherApproved();

        final ReceiptMisc receiptMisc = receiptHeader.getReceiptMisc();
        if (receiptMisc.getFund() != null)
            fundCode = receiptMisc.getFund().getCode();
        if (receiptMisc.getFundsource() != null)
            fundsourceCode = receiptMisc.getFundsource().getCode();
        if (receiptMisc.getDepartment() != null)
            departmentCode = receiptMisc.getDepartment().getCode();

        for (final InstrumentHeader instrumentHeader : receiptHeader.getReceiptInstrument())
            if (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_CASH)
                    || instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
                    || instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_DD)
                    || instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_ONLINE)) {
                headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERNAME);
                headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE);
            } else if (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_BANK)) {
                headerdetails
                .put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
                headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
            }
        headerdetails.put(VoucherConstant.DESCRIPTION, CollectionConstants.FINANCIAL_VOUCHERDESCRIPTION);
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            String dateString;
            if (receiptHeader.getVoucherDate() == null) {
                dateString = format.format(new Date());
                headerdetails.put(VoucherConstant.VOUCHERDATE, format.parse(dateString));

            } else {
                dateString = format.format(receiptHeader.getVoucherDate());
                headerdetails.put(VoucherConstant.VOUCHERDATE, format.parse(dateString));
            }
        } catch (final ParseException e) {
            LOGGER.error("Exception while voucher date", e);
            throw new ApplicationRuntimeException(e.getMessage());
        }

        if (receiptHeader.getVoucherNum() != null && !receiptHeader.getVoucherNum().isEmpty())
            headerdetails.put(VoucherConstant.VOUCHERNUMBER, receiptHeader.getVoucherNum());

        headerdetails.put(VoucherConstant.FUNDCODE, fundCode);
        headerdetails.put(VoucherConstant.DEPARTMENTCODE, departmentCode);
        headerdetails.put(VoucherConstant.FUNDSOURCECODE, fundsourceCode);
        headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);
        headerdetails.put(VoucherConstant.SOURCEPATH,
                CollectionConstants.RECEIPT_VIEW_SOURCEPATH + receiptHeader.getId());

        Set<ReceiptDetail> receiptDetailSet;

        /**
         * Aggregate Amount in case of bill based receipt for account codes
         * appearing more than once in receipt details
         */
        if (receiptHeader.getReceipttype() == 'B')
            receiptDetailSet = aggregateDuplicateReceiptDetailObject(new ArrayList<ReceiptDetail>(
                    receiptHeader.getReceiptDetails()));
        else
            receiptDetailSet = receiptHeader.getReceiptDetails();

        for (final ReceiptDetail receiptDetail : receiptDetailSet)
            if (receiptDetail.getCramount().compareTo(BigDecimal.ZERO) != 0
            || receiptDetail.getDramount().compareTo(BigDecimal.ZERO) != 0) {

                final HashMap<String, Object> accountcodedetailsHashMap = new HashMap<>(0);
                accountcodedetailsHashMap.put(VoucherConstant.GLCODE, receiptDetail.getAccounthead().getGlcode());

                accountcodedetailsHashMap.put(VoucherConstant.DEBITAMOUNT,
                        receiptDetail.getDramount().compareTo(BigDecimal.ZERO) == 0 ? 0 : receiptDetail.getDramount());
                accountcodedetailsHashMap.put(VoucherConstant.CREDITAMOUNT,
                        receiptDetail.getCramount().compareTo(BigDecimal.ZERO) == 0 ? 0 : receiptDetail.getCramount());
                if (receiptDetail.getFunction() != null)
                    accountcodedetailsHashMap.put(VoucherConstant.FUNCTIONCODE, receiptDetail.getFunction().getCode());
                accountCodeList.add(accountcodedetailsHashMap);

                for (final AccountPayeeDetail accpayeeDetail : receiptDetail.getAccountPayeeDetails())
                    if (accpayeeDetail.getAmount().compareTo(BigDecimal.ZERO) != 0) {

                        final HashMap<String, Object> subledgerdetailsHashMap = new HashMap<>(0);
                        subledgerdetailsHashMap.put(VoucherConstant.GLCODE, accpayeeDetail.getReceiptDetail()
                                .getAccounthead().getGlcode());
                        subledgerdetailsHashMap.put(VoucherConstant.DETAILTYPEID, accpayeeDetail.getAccountDetailType()
                                .getId());
                        subledgerdetailsHashMap.put(VoucherConstant.DETAILKEYID, accpayeeDetail.getAccountDetailKey()
                                .getDetailkey());
                        if (accpayeeDetail.getReceiptDetail().getCramount().compareTo(BigDecimal.ZERO) != 0)
                            subledgerdetailsHashMap.put(VoucherConstant.CREDITAMOUNT, accpayeeDetail.getAmount()
                                    .compareTo(BigDecimal.ZERO) == 0 ? 0 : accpayeeDetail.getAmount());
                        else if (accpayeeDetail.getReceiptDetail().getDramount().compareTo(BigDecimal.ZERO) != 0)
                            subledgerdetailsHashMap.put(VoucherConstant.DEBITAMOUNT, accpayeeDetail.getAmount()
                                    .compareTo(BigDecimal.ZERO) == 0 ? 0 : accpayeeDetail.getAmount());
                        subledgerList.add(subledgerdetailsHashMap);

                    }

            }

        return financialsUtil.createVoucher(headerdetails, accountCodeList, subledgerList, isVoucherApproved);
    }

    /**
     * Creates voucher for given receipt header and maps it with the same. Also
     * updates the instrument voucher mapping in financials.
     *
     * @param receiptHeader
     *            Receipt header for which voucher is to be created
     * @return The created voucher header
     */

    public CVoucherHeader createVoucherForReceipt(final ReceiptHeader receiptHeader) {
        CVoucherHeader voucherheader = null;

        // Additional check for challan Based Receipt, if the receipt cancelled
        // before remittance
        // then need to check the instrument status of that receipt in order to
        // create voucher
        // as the challan has a 'PENDING' receipt object associated with it
        boolean isParentReceiptInstrumentDeposited = false;

        if (receiptHeader.getReceiptHeader() != null)
            for (final InstrumentHeader instrumentHeader : receiptHeader.getReceiptHeader().getReceiptInstrument())
                if (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_CASH)) {
                    if (instrumentHeader.getStatusId().getDescription()
                            .equals(CollectionConstants.INSTRUMENT_RECONCILED_STATUS)) {
                        isParentReceiptInstrumentDeposited = true;
                        break;
                    }
                } else if (instrumentHeader.getStatusId().getDescription()
                        .equals(CollectionConstants.INSTRUMENT_DEPOSITED_STATUS)) {
                    isParentReceiptInstrumentDeposited = true;
                    break;
                }

        if (receiptHeader.getReceiptHeader() == null || receiptHeader.getReceiptHeader() != null
                && !isParentReceiptInstrumentDeposited) {
            voucherheader = createVoucher(receiptHeader);
            if (voucherheader != null) {
                final ReceiptVoucher receiptVoucher = new ReceiptVoucher();
                receiptVoucher.setVoucherheader(voucherheader);
                receiptVoucher.setReceiptHeader(receiptHeader);
                receiptHeader.addReceiptVoucher(receiptVoucher);
            }
        }

        updateInstrument(receiptHeader);
        LOGGER.debug("Created voucher for receipt : " + receiptHeader.getReceiptnumber());

        return voucherheader;
    }

    /**
     * Starts workflow for given set of receipt headers. Internally performs the
     * following: 1. Start workflow 2. Transition workflow state with action
     * "Create Receipt" 3. Create vouchers (if required) 4. If vouchers created,
     * transition workflow state with action "Create Voucher"
     *
     * @param receiptHeaders
     *            set of receipt headers on which workflow is to be started
     * @param receiptBulkUpload
     */
    public void startWorkflow(final ReceiptHeader receiptHeader) {
        final Boolean createVoucherForBillingService = collectionsUtil.checkVoucherCreation(receiptHeader);
        Position position;
        if (!collectionsUtil.isEmployee(receiptHeader.getCreatedBy()))
            position = collectionsUtil.getPositionByDeptDesgAndBoundary(receiptHeader.getReceiptMisc().getBoundary());
        else
            position = collectionsUtil.getPositionOfUser(receiptHeader.getCreatedBy());
        if (receiptHeader.getState() == null && !createVoucherForBillingService)
            receiptHeader
            .transition()
            .start()
            .withSenderName(
                    receiptHeader.getCreatedBy().getUsername() + "::" + receiptHeader.getCreatedBy().getName())
                    .withComments(CollectionConstants.WF_STATE_RECEIPT_CREATED)
                    .withStateValue(CollectionConstants.WF_STATE_RECEIPT_CREATED).withOwner(position)
                    .withDateInfo(new Date()).withNextAction(CollectionConstants.WF_ACTION_SUBMIT);
        else if (createVoucherForBillingService) {
            createVoucherForReceipt(receiptHeader);
            receiptHeader
            .transition()
            .start()
            .withSenderName(
                    receiptHeader.getCreatedBy().getUsername() + "::" + receiptHeader.getCreatedBy().getName())
                    .withComments("Receipt voucher created")
                    .withStateValue(CollectionConstants.WF_ACTION_CREATE_VOUCHER).withOwner(position)
                    .withDateInfo(new Date()).withNextAction(CollectionConstants.WF_ACTION_SUBMIT);
        }

        if (receiptHeader.getReceiptMisc().getDepositedInBank() != null)
            receiptHeader
            .transition(true)
            .withSenderName(
                    receiptHeader.getCreatedBy().getUsername() + "::" + receiptHeader.getCreatedBy().getName())
                    .withComments("Receipts Submitted for Approval")
                    .withStateValue(CollectionConstants.WF_ACTION_CREATE_VOUCHER).withOwner(position)
                    .withDateInfo(new Date()).withNextAction(CollectionConstants.WF_ACTION_SUBMIT);

        LOGGER.debug("Workflow state transition complete");
    }

    /**
     * Method to check if the given HashMap already exists in the List of
     * HashMap
     *
     * @param queryResults
     * @param objHashMap
     * @param m
     * @return index of objHashMap in the queryResults
     */
    public int checkIfMapObjectExist(final List<HashMap<String, Object>> paramList,
            final Object[] arrayObjectInitialIndexTemp) {
        int check = -1;
        for (int m = 0; m < paramList.size(); m++) {
            final HashMap<String, Object> objHashMapTemp = paramList.get(m);

            if (arrayObjectInitialIndexTemp[1] != null && arrayObjectInitialIndexTemp[2] != null)
                if (arrayObjectInitialIndexTemp[1].equals(objHashMapTemp
                        .get(CollectionConstants.BANKREMITTANCE_RECEIPTDATE))
                        && arrayObjectInitialIndexTemp[2].equals(objHashMapTemp
                                .get(CollectionConstants.BANKREMITTANCE_SERVICENAME))
                                && arrayObjectInitialIndexTemp[6].equals(objHashMapTemp
                                        .get(CollectionConstants.BANKREMITTANCE_FUNDCODE))
                                        && arrayObjectInitialIndexTemp[7].equals(objHashMapTemp
                                                .get(CollectionConstants.BANKREMITTANCE_DEPARTMENTCODE))) {
                    check = m;
                    break;
                } else
                    continue;

        }
        return check;
    }

    @Transactional
    public void updateChequeCardRemittance(final Map<String, Object> instrumentDepositeMap,
            final String voucherWorkflowMsg, final Boolean voucherTypeForChequeDDCard, final Date voucherDate,
            final Bankaccount depositedBankAccount, final String serviceGlCode,
            final List<InstrumentHeader> instrumentHeaderListCheque, final CVoucherHeader voucherHeaderCheque) {
        final EgwStatus instrumentStatusDeposited = collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_INSTRUMENTHEADER, CollectionConstants.INSTRUMENT_DEPOSITED_STATUS);
        int counter = 1;
        for (InstrumentHeader instrumentHeader : instrumentHeaderListCheque) {
            instrumentHeader = financialsUtil.updateInstrumentHeaderStatus(instrumentHeader, instrumentStatusDeposited,
                    depositedBankAccount);
            if (voucherHeaderCheque.getId() != null && serviceGlCode != null) {
                final Map<String, Object> chequeMap = constructInstrumentMap(instrumentDepositeMap,
                        depositedBankAccount, instrumentHeader, voucherHeaderCheque, voucherDate);
                if (voucherTypeForChequeDDCard)
                    financialsUtil.updateCheque_DD_Card_Deposit_Receipt(chequeMap);
                else
                    financialsUtil.updateCheque_DD_Card_Deposit(chequeMap);
            }
            if (counter % 20 == 0) {
                getSession().flush();
                getSession().clear();
            }
            counter++;
        }
    }

    @Transactional
    public void updateCashRemittance(final Map<String, Object> instrumentDepositeMap, final String voucherWorkflowMsg,
            final Date voucherDate, final Bankaccount depositedBankAccount, final String serviceGlCode,
            final List<InstrumentHeader> instrumentHeaderListCash, final CVoucherHeader voucherHeaderCash) {
        int counter = 1;
        for (final InstrumentHeader instrumentHeader : instrumentHeaderListCash) {
            if (voucherHeaderCash.getId() != null && serviceGlCode != null) {
                final Map<String, Object> cashMap = constructInstrumentMap(instrumentDepositeMap, depositedBankAccount,
                        instrumentHeader, voucherHeaderCash, voucherDate);
                financialsUtil.updateCashDeposit(cashMap);
            }
            if (counter % 20 == 0) {
                getSession().flush();
                getSession().clear();
            }
            counter++;
        }
    }

    /**
     * For Bill Based Receipt, aggregate the amount for same account heads
     *
     * @param receiptDetailSetParam
     * @return Set of Receipt Detail after Aggregating Amounts
     */
    public Set<ReceiptDetail> aggregateDuplicateReceiptDetailObject(final List<ReceiptDetail> receiptDetailSetParam) {
        final List<ReceiptDetail> newReceiptDetailList = new ArrayList<>(0);

        int counter = 0;

        for (final ReceiptDetail receiptDetailObj : receiptDetailSetParam) {
            if (counter == 0)
                newReceiptDetailList.add(receiptDetailObj);
            else {
                final int checknew = checkIfReceiptDetailObjectExist(newReceiptDetailList, receiptDetailObj);
                if (checknew == -1)
                    newReceiptDetailList.add(receiptDetailObj);
                else {
                    final ReceiptDetail receiptDetail = new ReceiptDetail();

                    final ReceiptDetail newReceiptDetailObj = newReceiptDetailList.get(checknew);
                    newReceiptDetailList.remove(checknew);

                    receiptDetail.setAccounthead(newReceiptDetailObj.getAccounthead());
                    receiptDetail.setAccountPayeeDetails(newReceiptDetailObj.getAccountPayeeDetails());
                    receiptDetail.setCramount(newReceiptDetailObj.getCramount().add(receiptDetailObj.getCramount()));
                    receiptDetail.setCramountToBePaid(newReceiptDetailObj.getCramountToBePaid());
                    receiptDetail.setDescription(newReceiptDetailObj.getDescription());
                    receiptDetail.setDramount(newReceiptDetailObj.getDramount().add(receiptDetailObj.getDramount()));
                    receiptDetail.setFinancialYear(newReceiptDetailObj.getFinancialYear());
                    receiptDetail.setFunction(newReceiptDetailObj.getFunction());
                    receiptDetail.setOrdernumber(newReceiptDetailObj.getOrdernumber());

                    newReceiptDetailList.add(receiptDetail);
                }
            }
            counter++;
        }
        return new HashSet<>(newReceiptDetailList);
    }

    /**
     * API to check if the given receipt detail object already exists in the
     * list passed passed as parameter
     *
     * @param newReceiptDetailSet
     * @param receiptDetailObj
     * @return
     */
    public int checkIfReceiptDetailObjectExist(final List<ReceiptDetail> newReceiptDetailSet,
            final ReceiptDetail receiptDetailObj) {
        int check = -1;

        for (int m = 0; m < newReceiptDetailSet.size(); m++) {

            final ReceiptDetail receiptDetail = newReceiptDetailSet.get(m);

            if (receiptDetailObj.getAccounthead().getId().equals(receiptDetail.getAccounthead().getId())) {
                check = m;
                break;
            } else
                continue;
        }
        return check;
    }

    /**
     * End Work-flow of the given cancelled receipt
     *
     * @param receiptHeaders
     *            Set of receipt headers to be transitioned
     * @param actionName
     *            Action name for the transition
     * @param comment
     *            Comment for the transition
     */
    public void endReceiptWorkFlowOnCancellation(final ReceiptHeader receiptHeaderToBeCancelled) {
        // End work-flow for the cancelled receipt
        Position position;
        if (!collectionsUtil.isEmployee(receiptHeaderToBeCancelled.getCreatedBy()))
            position = collectionsUtil.getPositionByDeptDesgAndBoundary(receiptHeaderToBeCancelled.getReceiptMisc()
                    .getBoundary());
        else
            position = collectionsUtil.getPositionOfUser(receiptHeaderToBeCancelled.getCreatedBy());

        if (position != null)
            receiptHeaderToBeCancelled
            .transition(true)
            .end()
            .withSenderName(
                    receiptHeaderToBeCancelled.getCreatedBy().getUsername() + "::"
                            + receiptHeaderToBeCancelled.getCreatedBy().getName())
                            .withComments("Receipt Cancelled - Workflow ends").withStateValue(CollectionConstants.WF_STATE_END)
                            .withOwner(position).withDateInfo(new Date());
    }

    /**
     * This method persists the given <code>ReceiptPayeeDetails</code> entity.
     * The receipt number for all of the receipts is generated, if not already
     * present. If the receipt is associated with a challan, and the challan
     * number is not present, the challan number is generated and set into it.
     */
    @Override
    @Transactional
    public ReceiptHeader persist(final ReceiptHeader receiptHeader) throws ApplicationRuntimeException {
        if (receiptHeader.getReceipttype() != CollectionConstants.RECEIPT_TYPE_CHALLAN
                && !CollectionConstants.RECEIPT_STATUS_CODE_PENDING.equals(receiptHeader.getStatus().getCode())
                && !CollectionConstants.RECEIPT_STATUS_CODE_FAILED.equals(receiptHeader.getStatus().getCode())
                && receiptHeader.getReceiptnumber() == null)
            setReceiptNumber(receiptHeader);

        if (receiptHeader.getChallan() != null) {
            final Challan challan = receiptHeader.getChallan();
            if (challan.getChallanNumber() == null)
                setChallanNumber(challan);

            receiptHeader.setChallan(challan);
            LOGGER.info("Persisted challan with challan number " + challan.getChallanNumber());
        }

        // Update Billing System regarding cancellation of the existing
        if (CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED.equals(receiptHeader.getStatus().getCode())) {
            if (receiptHeader.getState() != null
                    && !receiptHeader.getState().getValue().equals(CollectionConstants.WF_STATE_END))
                endReceiptWorkFlowOnCancellation(receiptHeader);
            if (receiptHeader.getReceipttype() == CollectionConstants.RECEIPT_TYPE_BILL)
                updateBillingSystemWithReceiptInfo(receiptHeader, null, null);
        }
        return super.persist(receiptHeader);
    }

    /**
     * This method persists the given <code>ReceiptPayeeDetails</code> entity.
     * If the receipt number for all of the receipts is generated, if not
     * already present.
     */

    @Transactional
    public ReceiptHeader persistChallan(final ReceiptHeader receiptHeader, final Position position,
            final String actionName, final String approvalRemarks) {
        final Integer validUpto = Integer.valueOf(collectionsUtil.getAppConfigValue(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.APPCONFIG_VALUE_CHALLANVALIDUPTO));
        final Challan challan = receiptHeader.getChallan();
        DateTime date = new DateTime(challan.getChallanDate());
        date = date.plusDays(validUpto);
        challan.setValidUpto(date.toDate());
        if (challan.getChallanNumber() == null)
            setChallanNumber(challan);

        challan.setReceiptHeader(receiptHeader);
        receiptHeader.setChallan(challan);
        super.persist(receiptHeader);
        LOGGER.info("Persisting challan with challan number " + challan.getChallanNumber());
        challanService.workflowtransition(receiptHeader.getChallan(), position, actionName, approvalRemarks);
        return receiptHeader;
    }

    /**
     * This method persists the given set of <code>ReceiptPayeeDetails</code>
     * instances with receipt number as Pending
     *
     * @param entity
     *            a set of <code>ReceiptPayeeDetails</code> instances to be
     *            persisted
     * @return the list of persisted <code>ReceiptPayeeDetails</code> instances
     */
    @Transactional
    public ReceiptHeader persistReceiptObject(final ReceiptHeader receiptHeader) {
        return super.persist(receiptHeader);
    }

    public void setReceiptNumber(final ReceiptHeader entity) {
        entity.setReceiptnumber(collectionsNumberGenerator.generateReceiptNumber(entity));
    }

    private void setChallanNumber(final Challan challan) {
        final CFinancialYear financialYear = collectionsUtil.getFinancialYearforDate(new Date());
        challan.setChallanNumber(collectionsNumberGenerator.generateChallanNumber(challan, financialYear));
    }

    public void setCollectionsNumberGenerator(final CollectionsNumberGenerator collectionsNumberGenerator) {
        this.collectionsNumberGenerator = collectionsNumberGenerator;
    }

    private BillingIntegrationService getBillingServiceBean(final String serviceCode) {
        return (BillingIntegrationService) collectionsUtil.getBean(serviceCode
                + CollectionConstants.COLLECTIONS_INTERFACE_SUFFIX);
    }

    /**
     * This method looks up the bean to communicate with the billing system and
     * updates the billing system.
     */

    @Transactional
    public Boolean updateBillingSystem(final ServiceDetails serviceDetails, final Set<BillReceiptInfo> billReceipts,
            BillingIntegrationService billingService) {
        if (!serviceDetails.getServiceType().equals(CollectionConstants.SERVICE_TYPE_BILLING))
            return true;
        else if (billingService == null
                && serviceDetails.getServiceType().equals(CollectionConstants.SERVICE_TYPE_BILLING))
            billingService = getBillingServiceBean(serviceDetails.getCode());

        if (billingService == null && serviceDetails.getServiceType().equals(CollectionConstants.SERVICE_TYPE_BILLING))
            return false;
        else
            try {
                billingService.updateReceiptDetails(billReceipts);
                return true;
            } catch (final Exception e) {
                final String errMsg = "Exception while updating billing system [" + serviceDetails.getCode()
                        + "] with receipt details!";
                LOGGER.error(errMsg, e);
                throw new ApplicationRuntimeException(errMsg, e);
            }
    }

    public String getAdditionalInfoForReceipt(final String serviceCode, final BillReceiptInfo billReceipt) {
        final BillingIntegrationService billingService = getBillingServiceBean(serviceCode);
        if (billingService == null)
            throw new ApplicationRuntimeException("Unable to load bean for billing system: " + serviceCode);
        else
            try {
                return billingService.constructAdditionalInfoForReceipt(billReceipt);
            } catch (final Exception e) {
                final String errMsg = "Exception while constructing additional info for receipt [" + serviceCode + "]!";
                LOGGER.error(errMsg, e);
                throw new ApplicationRuntimeException(errMsg, e);
            }
    }

    /**
     * This method is called for voucher reversal in case of intra-day receipt
     * cancellation.
     */

    public void createReversalVoucher(final ReceiptVoucher receiptVoucher, final String instrumentType) {
        final List<HashMap<String, Object>> reversalVoucherInfoList = new ArrayList<HashMap<String, Object>>(0);
        final HashMap<String, Object> reversalVoucherInfo = new HashMap<String, Object>(0);

        if (receiptVoucher.getVoucherheader() != null) {
            reversalVoucherInfo.put(CollectionConstants.FINANCIALS_VOUCHERREVERSAL_ORIGINALVOUCHERID, receiptVoucher
                    .getVoucherheader().getId());
            reversalVoucherInfo.put(CollectionConstants.FINANCIALS_VOUCHERREVERSAL_DATE, new Date());

            if (receiptVoucher.getVoucherheader().getType()
                    .equals(CollectionConstants.FINANCIAL_JOURNALVOUCHER_VOUCHERTYPE)) {

                reversalVoucherInfo.put(CollectionConstants.FINANCIALS_VOUCHERREVERSAL_TYPE,
                        CollectionConstants.FINANCIAL_JOURNALVOUCHER_VOUCHERTYPE);
                reversalVoucherInfo.put(CollectionConstants.FINANCIALS_VOUCHERREVERSAL_NAME,
                        CollectionConstants.FINANCIAL_JOURNALVOUCHER_VOUCHERNAME);
            } else if (receiptVoucher.getVoucherheader().getType()
                    .equals(CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE)) {
                reversalVoucherInfo.put(CollectionConstants.FINANCIALS_VOUCHERREVERSAL_TYPE,
                        CollectionConstants.FINANCIAL_PAYMENTVOUCHER_VOUCHERTYPE);
                reversalVoucherInfo.put(CollectionConstants.FINANCIALS_VOUCHERREVERSAL_NAME,
                        CollectionConstants.FINANCIAL_PAYMENTVOUCHER_VOUCHERNAME);
            }
        }

        reversalVoucherInfoList.add(reversalVoucherInfo);

        try {
            financialsUtil.getReversalVoucher(reversalVoucherInfoList);
        } catch (final Exception exp) {
            final String errorMsg = "Receipt Service Exception while creating reversal voucher!";
            LOGGER.error(errorMsg, exp);
            throw new ApplicationRuntimeException(errorMsg, exp);
        }
    }

    /**
     * @param receiptPayeeDetails
     * @return void @ Create instrument voucher list from receiptpayeelist and
     *         pass it to financialsutil
     */

    public void updateInstrument(final ReceiptHeader receiptHeader) {
        final List<Map<String, Object>> instrumentVoucherList = new ArrayList<Map<String, Object>>(0);
        if (receiptHeader.getReceiptVoucher() != null && !receiptHeader.getReceiptVoucher().isEmpty()) {
            final CVoucherHeader voucherHeader = receiptHeader.getReceiptVoucher().iterator().next().getVoucherheader();
            if (voucherHeader != null && receiptHeader.getReceiptInstrument() != null) {
                for (final InstrumentHeader instrumentHeader : receiptHeader.getReceiptInstrument()) {
                    final Map<String, Object> iVoucherMap = new HashMap<String, Object>(0);
                    iVoucherMap.put(CollectionConstants.FINANCIAL_INSTRUMENTSERVICE_INSTRUMENTHEADEROBJECT,
                            instrumentHeader);
                    iVoucherMap.put(CollectionConstants.FINANCIAL_INSTRUMENTSERVICE_VOUCHERHEADEROBJECT, voucherHeader);
                    instrumentVoucherList.add(iVoucherMap);
                }
                financialsUtil.updateInstrumentVoucher(instrumentVoucherList);
            }
        }
    }

    public List<InstrumentHeader> createInstrument(final List<InstrumentHeader> instrumentHeaderList) {
        final List<Map<String, Object>> instrumentHeaderMapList = new ArrayList<Map<String, Object>>(0);
        if (instrumentHeaderList != null)
            for (final InstrumentHeader instrumentHeader : instrumentHeaderList) {
                final Map<String, Object> instrumentHeaderMap = new HashMap<String, Object>(0);
                instrumentHeaderMap.put(CollectionConstants.MAP_KEY_INSTRSERVICE_INSTRUMENTNUMBER,
                        instrumentHeader.getInstrumentNumber());
                instrumentHeaderMap.put(CollectionConstants.MAP_KEY_INSTRSERVICE_INSTRUMENTDATE,
                        instrumentHeader.getInstrumentDate());
                instrumentHeaderMap.put(CollectionConstants.MAP_KEY_INSTRSERVICE_INSTRUMENTAMOUNT,
                        instrumentHeader.getInstrumentAmount());
                instrumentHeaderMap.put(CollectionConstants.MAP_KEY_INSTRSERVICE_INSTRUMENTTYPE, instrumentHeader
                        .getInstrumentType().getType());
                instrumentHeaderMap.put(CollectionConstants.MAP_KEY_INSTRSERVICE_ISPAYCHEQUE,
                        instrumentHeader.getIsPayCheque());
                if (instrumentHeader.getBankId() != null)
                    instrumentHeaderMap.put(CollectionConstants.MAP_KEY_INSTRSERVICE_BANKCODE, instrumentHeader
                            .getBankId().getCode());
                instrumentHeaderMap.put(CollectionConstants.MAP_KEY_INSTRSERVICE_BANKBRANCHNAME,
                        instrumentHeader.getBankBranchName());
                instrumentHeaderMap.put(CollectionConstants.MAP_KEY_INSTRSERVICE_TRANSACTIONNUMBER,
                        instrumentHeader.getTransactionNumber());
                instrumentHeaderMap.put(CollectionConstants.MAP_KEY_INSTRSERVICE_TRANSACTIONDATE,
                        instrumentHeader.getTransactionDate());
                if (instrumentHeader.getBankAccountId() != null)
                    instrumentHeaderMap.put(CollectionConstants.MAP_KEY_INSTRSERVICE_BANKACCOUNTID, instrumentHeader
                            .getBankAccountId().getId());
                instrumentHeaderMapList.add(instrumentHeaderMap);
                // should add bankaccount for bank : key = Bank account id;
                // value = instrumentHeader.getBankAccount.getId()
            }
        return financialsUtil.createInstrument(instrumentHeaderMapList);
    }

    private Map<String, Object> constructInstrumentMap(final Map<String, Object> instrumentDepositeMap,
            final Bankaccount bankaccount, final InstrumentHeader instrumentHeader, final CVoucherHeader voucherHeader,
            final Date voucherDate) {
        final InstrumentType instrumentType = (InstrumentType) persistenceService.find(
                "select it from InstrumentType it,InstrumentHeader ih where " + "ih.instrumentType=it.id and ih.id=?",
                instrumentHeader.getId());
        instrumentDepositeMap.put("instrumentheader", instrumentHeader.getId());
        instrumentDepositeMap.put("bankaccountid", bankaccount.getId());
        instrumentDepositeMap.put("instrumentamount", instrumentHeader.getInstrumentAmount());
        instrumentDepositeMap.put("instrumenttype", instrumentType.getType());
        instrumentDepositeMap.put("depositdate", voucherDate);
        instrumentDepositeMap.put("createdby", voucherHeader.getCreatedBy().getId());
        instrumentDepositeMap.put("ispaycheque", instrumentHeader.getIsPayCheque());
        instrumentDepositeMap.put("payinid", voucherHeader.getId());
        return instrumentDepositeMap;
    }

    @Transactional
    public void performWorkflow(final String actionName, final ReceiptHeader receiptHeader, final String remarks) {
        try {
            Position operatorPosition;
            Employee employee = employeeService.getEmployeeById(receiptHeader.getCreatedBy().getId());

            final Department department = departmentService.getDepartmentByName(collectionsUtil.getAppConfigValue(
                    CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                    CollectionConstants.COLLECTION_DEPARTMENTFORWORKFLOWAPPROVER));
            final Designation designation = collectionsUtil.getDesignationForApprover();
            final Boolean isEmployee = collectionsUtil.isEmployee(receiptHeader.getCreatedBy());
            if (!isEmployee)
                employee = employeeService.getEmployeeById(collectionsUtil.getLoggedInUser().getId());
            Boundary boundary = null;
            for (final Jurisdiction jur : employee.getJurisdictions())
                boundary = jur.getBoundary();
            final List<Employee> emp = employeeService.findByDepartmentDesignationAndBoundary(department.getId(),
                    designation.getId(), boundary.getId());
            if (emp.isEmpty())
                throw new ValidationException(Arrays.asList(new ValidationError("Manager does not exists",
                        "submitcollections.validation.error.manager.notexists")));
            Position approverPosition = null;
            final List<Assignment> assignments = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(emp.get(0)
                    .getId());
            for (final Assignment assign : assignments)
                if (assign.getDesignation().equals(designation))
                    approverPosition = assign.getPosition();
            if (actionName.equals(CollectionConstants.WF_ACTION_SUBMIT))
                perform(receiptHeader, CollectionConstants.WF_ACTION_APPROVE,
                        CollectionConstants.RECEIPT_STATUS_CODE_SUBMITTED, CollectionConstants.WF_ACTION_APPROVE,
                        approverPosition, remarks);
            else if (actionName.equals(CollectionConstants.WF_ACTION_APPROVE))
                perform(receiptHeader, CollectionConstants.WF_STATE_APPROVED,
                        CollectionConstants.RECEIPT_STATUS_CODE_APPROVED, "", approverPosition, remarks);
            else if (actionName.equals(CollectionConstants.WF_ACTION_REJECT)) {
                if (!isEmployee)
                    operatorPosition = collectionsUtil.getPositionByDeptDesgAndBoundary(receiptHeader.getReceiptMisc()
                            .getBoundary());
                else
                    operatorPosition = collectionsUtil.getPositionOfUser(receiptHeader.getCreatedBy());
                perform(receiptHeader, CollectionConstants.WF_STATE_REJECTED,
                        CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED, CollectionConstants.WF_ACTION_SUBMIT,
                        operatorPosition, remarks);
            }
        } catch (final ValidationException e) {
            final List<ValidationError> errors = new ArrayList<>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            LOGGER.error(errors, e);
            throw new ValidationException(errors);
        } catch (final Exception e) {
            final String errorMsg = "Receipt Service Exception while workflow transition!";
            LOGGER.error(errorMsg, e);
            throw new ApplicationRuntimeException(e.getMessage());
        }
    }

    @Transactional
    public void performWorkflowForAllReceipts(final String actionName, final List<ReceiptHeader> receiptHeaders,
            final String remarks) {
        try {
            final Position approverPosition = getApproverPosition(receiptHeaders.get(0));
            for (final ReceiptHeader receiptHeader : receiptHeaders)
                if (actionName.equals(CollectionConstants.WF_ACTION_SUBMIT))
                    perform(receiptHeader, CollectionConstants.WF_ACTION_APPROVE,
                            CollectionConstants.RECEIPT_STATUS_CODE_SUBMITTED, CollectionConstants.WF_ACTION_APPROVE,
                            approverPosition, remarks);
                else if (actionName.equals(CollectionConstants.WF_ACTION_APPROVE))
                    perform(receiptHeader, CollectionConstants.WF_STATE_APPROVED,
                            CollectionConstants.RECEIPT_STATUS_CODE_APPROVED, "", approverPosition, remarks);
                else if (actionName.equals(CollectionConstants.WF_ACTION_REJECT)) {
                    final Position operatorPosition = getOperatorPosition(receiptHeaders.get(0));
                    perform(receiptHeader, CollectionConstants.WF_STATE_REJECTED,
                            CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED,
                            CollectionConstants.WF_ACTION_SUBMIT, operatorPosition, remarks);
                }
        } catch (final ValidationException e) {
            final List<ValidationError> errors = new ArrayList<>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            LOGGER.error(errors, e);
            throw new ValidationException(errors);
        } catch (final Exception e) {
            final String errorMsg = "Receipt Service Exception while workflow transition!";
            LOGGER.error(errorMsg, e);
            throw new ApplicationRuntimeException(e.getMessage());
        }
    }

    public Position getApproverPosition(final ReceiptHeader receiptHeader) {
        Employee employee;
        final Boolean isEmployee = collectionsUtil.isEmployee(receiptHeader.getCreatedBy());
        if (!isEmployee)
            employee = employeeService.getEmployeeById(collectionsUtil.getLoggedInUser().getId());
        else
            employee = employeeService.getEmployeeById(receiptHeader.getCreatedBy().getId());
        final Department department = departmentService.getDepartmentByName(collectionsUtil.getAppConfigValue(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.COLLECTION_DEPARTMENTFORWORKFLOWAPPROVER));
        final Designation designation = collectionsUtil.getDesignationForApprover();
        Boundary boundary = null;
        for (final Jurisdiction jur : employee.getJurisdictions())
            boundary = jur.getBoundary();
        final List<Employee> emp = employeeService.findByDepartmentDesignationAndBoundary(department.getId(),
                designation.getId(), boundary.getId());
        if (emp.isEmpty())
            throw new ValidationException(Arrays.asList(new ValidationError("Manager does not exists",
                    "submitcollections.validation.error.manager.notexists")));
        Position approverPosition = null;
        final List<Assignment> assignments = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(emp.get(0)
                .getId());
        for (final Assignment assign : assignments)
            if (assign.getDesignation().equals(designation))
                approverPosition = assign.getPosition();
        return approverPosition;
    }

    public Position getOperatorPosition(final ReceiptHeader receiptHeader) {
        Position operatorPosition;
        final Boolean isEmployee = collectionsUtil.isEmployee(receiptHeader.getCreatedBy());
        if (!isEmployee)
            operatorPosition = collectionsUtil.getPositionByDeptDesgAndBoundary(receiptHeader.getReceiptMisc()
                    .getBoundary());
        else
            operatorPosition = collectionsUtil.getPositionOfUser(receiptHeader.getCreatedBy());
        return operatorPosition;
    }

    @Transactional
    public void perform(final ReceiptHeader receiptHeader, final String wfState, final String newStatusCode,
            final String nextAction, final Position ownerPosition, final String remarks) {
        receiptHeader.setStatus(collectionsUtil.getReceiptStatusForCode(newStatusCode));

        if (receiptHeader.getStatus().getCode().equals(CollectionConstants.RECEIPT_STATUS_CODE_APPROVED))
            // Receipt approved. end workflow for this receipt.
            receiptHeader
            .transition()
            .end()
            .withSenderName(
                    receiptHeader.getCreatedBy().getUsername() + "::" + receiptHeader.getCreatedBy().getName())
                    .withComments("Receipt Approved - Workflow ends").withStateValue(CollectionConstants.WF_STATE_END)
                    .withOwner(ownerPosition).withDateInfo(new Date());
        else
            receiptHeader
            .transition()
            .withSenderName(
                    receiptHeader.getCreatedBy().getUsername() + "::" + receiptHeader.getCreatedBy().getName())
                    .withComments(remarks).withStateValue(wfState).withOwner(ownerPosition).withDateInfo(new Date())
                    .withNextAction(nextAction);
        super.persist(receiptHeader);

        updateCollectionIndexAndPushMail(receiptHeader);
    }

    public Set<InstrumentHeader> createOnlineInstrument(final Date transactionDate, final String transactionId,
            final BigDecimal transactionAmt) {
        final InstrumentHeader onlineInstrumentHeader = new InstrumentHeader();
        Set<InstrumentHeader> instrumentHeaderSet;
        onlineInstrumentHeader.setInstrumentType(financialsUtil
                .getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_ONLINE));
        onlineInstrumentHeader.setTransactionDate(transactionDate);
        onlineInstrumentHeader.setIsPayCheque(CollectionConstants.ZERO_INT);
        onlineInstrumentHeader.setTransactionNumber(transactionId);
        onlineInstrumentHeader.setInstrumentAmount(transactionAmt);

        final List<InstrumentHeader> instHeaderList = new ArrayList<>();
        instHeaderList.add(onlineInstrumentHeader);
        instrumentHeaderSet = new HashSet<>(createInstrument(instHeaderList));
        return instrumentHeaderSet;
    }

    public String getReceiptHeaderforDishonor(final Long mode, final Long bankAccId, final Long bankId,
            final String chequeDDNo, final String chqueDDDate) {
        final StringBuilder sb = new StringBuilder();
        sb.append("FROM egcl_collectionheader rpt,egcl_collectioninstrument ci,egf_instrumentheader ih,egw_status status,bank b,"
                + "bankbranch bb,bankaccount ba WHERE rpt.id = ci.collectionheader AND ci.instrumentheader = ih.id AND status.id = ih.id_status "
                + "AND b.id = bb.bankid AND bb.id = ba.branchid AND ba.id = ih.bankaccountid AND ih.instrumenttype = '"
                + mode
                + "' AND ((ih.ispaycheque ='0' AND status.moduletype ='"
                + CollectionConstants.MODULE_NAME_INSTRUMENTHEADER
                + "'"
                + "AND status.description = '"
                + CollectionConstants.INSTRUMENT_DEPOSITED_STATUS + "'))");

        if (bankAccId != null && bankAccId != -1)
            sb.append(" AND ih.bankaccountid=" + bankAccId + "");
        if ((bankAccId == null || bankAccId == -1) && bankId != null && bankId != 0)
            sb.append(" AND ih.bankid=" + bankId + "");
        if (!"".equals(chequeDDNo) && chequeDDNo != null)
            sb.append(" AND ih.instrumentnumber=trim('" + chequeDDNo + "') ");
        if (!"".equals(chqueDDDate) && chqueDDDate != null)
            sb.append(" AND ih.instrumentdate = '" + chqueDDDate + "' ");

        return sb.toString();
    }

    /**
     * This method performs the following for receipts to be newly created:
     * <ol>
     * <li>The user instrument header details, and actual amount paid by user is
     * captured.</li>
     * <li>A debit receipt detail account head is created for the total credit
     * collected.</li>
     * <li>Vouchers are created</li>
     * </ol>
     * <p>
     * The receipts are persisted and work flow is started for these persisted
     * receipts where in the receipt state is set to NEW The billing system is
     * updated about the persisted receipts. These include details of both newly
     * created as well as cancelled receipts. If the instrument list and voucher
     * list are not empty, the .... is updated The receipt ids of the newly
     * created receipts are collectively populated to be shown on the print
     * screen
     */
    @Transactional
    public void populateAndPersistReceipts(final ReceiptHeader receiptHeader,
            final List<InstrumentHeader> receiptInstrList) {
        try {
            persist(receiptHeader);
            LOGGER.info("Persisted receipts");
            // Start work flow for newly created receipt.This might internally
            // create voucher also, based on configuration.
            startWorkflow(receiptHeader);
            LOGGER.info("Workflow started for newly created receipts");
            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            final Date cutOffDate = getDataEntryCutOffDate(sdf);
            if (!receiptHeader.getService().getServiceType().equalsIgnoreCase(CollectionConstants.SERVICE_TYPE_BILLING))
                if (cutOffDate != null && receiptHeader.getReceiptdate().before(cutOffDate))
                    performWorkflow(CollectionConstants.WF_ACTION_APPROVE, receiptHeader,
                            "Legacy data Receipt Approval based on cutoff date");
                else
                    updateCollectionIndexAndPushMail(receiptHeader);
            if (receiptHeader.getService().getServiceType().equalsIgnoreCase(CollectionConstants.SERVICE_TYPE_BILLING)) {

                updateBillingSystemWithReceiptInfo(receiptHeader, null, null);
                LOGGER.info("Updated billing system ");
            }

        } catch (final HibernateException e) {
            LOGGER.error("Receipt Service HException while persisting ReceiptHeader", e);
            throw new ApplicationRuntimeException("Receipt Service Exception while persisting ReceiptHeader : ", e);
        } catch (final ApplicationRuntimeException e) {
            LOGGER.error("Receipt Service AException while persisting ReceiptHeader!", e);
            throw new ApplicationRuntimeException("Receipt Service Exception while persisting ReceiptHeader : ", e);
        }
    }// end of method

    public Date getDataEntryCutOffDate(final SimpleDateFormat sdf) {
        Date cutOffDate = null;
        try {
            cutOffDate = sdf.parse(collectionsUtil.getAppConfigValue(
                    CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                    CollectionConstants.APPCONFIG_VALUE_COLLECTIONDATAENTRYCUTOFFDATE));
        } catch (final ParseException e) {
            LOGGER.error("Error parsing Cut Off Date" + e.getMessage());
        }
        return cutOffDate;
    }

    /**
     * Updates the billing system with receipt information
     *
     * @param receiptHeader
     */
    @Transactional
    public void updateBillingSystemWithReceiptInfo(final ReceiptHeader receiptHeader,
            final BillingIntegrationService billingService, final InstrumentHeader bouncedInstrumentInfo) {

        /**
         * for each receipt created, send the details back to the billing system
         */
        LOGGER.info("$$$$$$ Update Billing system for Service Code :"
                + receiptHeader.getService().getCode()
                + (receiptHeader.getConsumerCode() != null ? " and consumer code: " + receiptHeader.getConsumerCode()
                        : ""));
        final Set<BillReceiptInfo> billReceipts = new HashSet<>(0);
        billReceipts.add(new BillReceiptInfoImpl(receiptHeader, chartOfAccountsHibernateDAO, persistenceService,
                bouncedInstrumentInfo));

        if (updateBillingSystem(receiptHeader.getService(), billReceipts, billingService)) {
            receiptHeader.setIsReconciled(true);
            // the receipts should be persisted again
            super.persist(receiptHeader);
            updateCollectionIndexAndPushMail(receiptHeader);
        }
        LOGGER.info("$$$$$$ Billing system updated for Service Code :"
                + receiptHeader.getService().getCode()
                + (receiptHeader.getConsumerCode() != null ? " and consumer code: " + receiptHeader.getConsumerCode()
                        : ""));
    }

    @Transactional
    public void updateCollectionIndexAndPushMail(final ReceiptHeader receiptHeader) {
        if (receiptHeader.getPayeeEmail() != null
                && !receiptHeader.getPayeeEmail().isEmpty()
                && (receiptHeader.getCollectiontype().equals(CollectionConstants.COLLECTION_TYPE_ONLINECOLLECTION)
                        && receiptHeader.getStatus().getCode().equals(CollectionConstants.RECEIPT_STATUS_CODE_APPROVED) || !receiptHeader
                        .getCollectiontype().equals(CollectionConstants.COLLECTION_TYPE_ONLINECOLLECTION)
                        && receiptHeader.getStatus().getCode()
                        .equals(CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED)))
            pushMail(receiptHeader);
        CollectionIndex collectionIndexObj = collectionIndexUtils.findByReceiptNumber(receiptHeader.getReceiptnumber());
        if (collectionIndexObj != null) {
            collectionIndexObj.setStatus(receiptHeader.getStatus().getDescription());
            collectionIndexService.persistCollectionIndex(collectionIndexObj);

        } else {
            collectionIndexObj = collectionsUtil.constructCollectionIndex(receiptHeader);
            collectionIndexService.pushCollectionIndex(collectionIndexObj);
        }
    }

    private void pushMail(final ReceiptHeader receiptHeader) {
        collectionsUtil.emailReceiptAsAttachment(receiptHeader,
                collectionsUtil.createReport(getReportRequest(receiptHeader)).getReportOutputData());
    }

    public ReportRequest getReportRequest(final ReceiptHeader receiptHeader) {
        String additionalMessage;
        final List<BillReceiptInfo> receiptList = new ArrayList<>(0);
        final Map<String, Object> reportParams = new HashMap<>(0);
        final String serviceCode = receiptHeader.getService().getCode();

        reportParams.put(CollectionConstants.REPORT_PARAM_COLLECTIONS_UTIL, collectionsUtil);
        final String templateName = collectionsUtil.getReceiptTemplateName(receiptHeader.getReceipttype(), serviceCode);

        if (receiptHeader.getReceipttype() == CollectionConstants.RECEIPT_TYPE_BILL) {
            additionalMessage = getAdditionalInfoForReceipt(serviceCode, new BillReceiptInfoImpl(receiptHeader,
                    chartOfAccountsHibernateDAO, persistenceService, null));
            if (additionalMessage != null)
                receiptList.add(new BillReceiptInfoImpl(receiptHeader, additionalMessage, chartOfAccountsHibernateDAO,
                        persistenceService));
            else
                receiptList.add(new BillReceiptInfoImpl(receiptHeader, chartOfAccountsHibernateDAO, persistenceService,
                        null));
        }
        final ReportRequest reportInput = new ReportRequest(templateName, receiptList, reportParams);
        reportInput.setReportFormat(ReportConstants.FileFormat.PDF);
        reportInput.setPrintDialogOnOpenReport(false);
        return reportInput;
    }

    /**
     * @param receipts
     *            - list of receipts which have to be processed as successful
     *            payments. For payments created as a response from bill desk,
     *            size of the array will be 1.
     */
    @Transactional
    public ReceiptHeader createOnlineSuccessPayment(final ReceiptHeader receiptHeader, final Date transactionDate,
            final String transactionId, final BigDecimal transactionAmt, final String authStatusCode,
            final String remarks, final BillingIntegrationService billingService) {
        receiptHeader.setStatus(collectionsUtil
                .getReceiptStatusForCode(CollectionConstants.RECEIPT_STATUS_CODE_APPROVED));

        receiptHeader.setReceiptInstrument(createOnlineInstrument(transactionDate, transactionId, transactionAmt));
        receiptHeader.setIsReconciled(Boolean.FALSE);
        receiptHeader.getOnlinePayment().setAuthorisationStatusCode(authStatusCode);
        receiptHeader.getOnlinePayment().setTransactionNumber(transactionId);
        receiptHeader.getOnlinePayment().setTransactionAmount(transactionAmt);
        receiptHeader.getOnlinePayment().setTransactionDate(transactionDate);
        receiptHeader.getOnlinePayment().setRemarks(remarks);

        // set online payment status as SUCCESS
        receiptHeader.getOnlinePayment().setStatus(
                collectionsUtil.getStatusForModuleAndCode(CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
                        CollectionConstants.ONLINEPAYMENT_STATUS_CODE_SUCCESS));
        persist(receiptHeader);
        LOGGER.debug("Persisted receipt after receiving success message from the payment gateway");
        return updateFinancialAndBillingSystem(receiptHeader, billingService);
    }

    @Transactional
    public ReceiptHeader updateFinancialAndBillingSystem(final ReceiptHeader receiptHeader,
            final BillingIntegrationService billingService) {
        try {
            final Boolean createVoucherForBillingService = collectionsUtil.checkVoucherCreation(receiptHeader);
            if (createVoucherForBillingService) {
                createVoucherForReceipt(receiptHeader);
                LOGGER.debug("Updated financial systems and created voucher.");
            }

        } catch (final ApplicationRuntimeException ex) {
            LOGGER.error("Receipt Service Exception while updateFinancialAndBillingSystem", ex);
            throw new ApplicationRuntimeException("Failed to create voucher in Financials");
        }
        updateBillingSystemWithReceiptInfo(receiptHeader, billingService, null);
        return receiptHeader;
    }

    @Transactional
    public void persistFieldReceipt(final ReceiptHeader receiptHeader, final List<InstrumentHeader> instrumentHeaderList) {
        final Set<InstrumentHeader> instHeaderSet = new HashSet<>(createInstrument(instrumentHeaderList));
        receiptHeader.setReceiptInstrument(instHeaderSet);
        persist(receiptHeader);
        LOGGER.info("Receipt Created with receipt number: " + receiptHeader.getReceiptnumber());
        updateFinancialAndBillingSystem(receiptHeader, null);
        LOGGER.info("Billing system updated with receipt info");
    }

    @Transactional
    public void updateDishonoredInstrumentStatus(final ReceiptHeader receiptHeader,
            final InstrumentHeader bounceInstrumentInfo, final EgwStatus receiptStatus, final boolean isReconciled) {
        financialsUtil.updateInstrumentHeader(bounceInstrumentInfo);
        // update receipts - set status to INSTR_BOUNCED and recon flag to false
        updateReceiptHeaderStatus(receiptHeader, receiptStatus, false);
        LOGGER.debug("Updated receipt status to " + receiptStatus.getCode() + " set reconcilation to false");

        updateBillingSystemWithReceiptInfo(receiptHeader, null, bounceInstrumentInfo);
    }

    /**
     * This method updates the status and reconciliation flag for the given
     * receipt
     *
     * @param receiptHeader
     *            <code>ReceiptHeader</code> objects whose status and
     *            reconciliation flag have to be modified
     * @param status
     *            a <code>EgwStatus</code> instance representing the state to
     *            which the receipt has to be updated with
     * @param isReconciled
     *            a <code>Boolean</code> flag indicating the value for the
     *            reconciliation status
     */
    @Transactional
    public void updateReceiptHeaderStatus(final ReceiptHeader receiptHeader, final EgwStatus status,
            final boolean isReconciled) {
        if (status != null)
            receiptHeader.setStatus(status);
        receiptHeader.setIsReconciled(isReconciled);
        update(receiptHeader);
    }

    @Transactional
    public ReceiptHeader reconcileOnlineSuccessPayment(final ReceiptHeader onlinePaymentReceiptHeader,
            final Date txnDate, final String txnRefNo, final BigDecimal txnAmount, final String txnAuthStatus,
            final List<ReceiptDetail> reconstructedList, final ReceiptDetail debitAccountDetail) {
        final BillingIntegrationService billingService = (BillingIntegrationService) collectionsUtil
                .getBean(onlinePaymentReceiptHeader.getService().getCode()
                        + CollectionConstants.COLLECTIONS_INTERFACE_SUFFIX);
        if (reconstructedList != null) {
            onlinePaymentReceiptHeader.getReceiptDetails().clear();
            persistReceiptObject(onlinePaymentReceiptHeader);
            LOGGER.debug("Reconstructed receiptDetailList : " + reconstructedList.toString());
            for (final ReceiptDetail receiptDetail : reconstructedList) {
                receiptDetail.setReceiptHeader(onlinePaymentReceiptHeader);
                onlinePaymentReceiptHeader.addReceiptDetail(receiptDetail);
            }
            onlinePaymentReceiptHeader.addReceiptDetail(debitAccountDetail);

        }

        return createOnlineSuccessPayment(onlinePaymentReceiptHeader, txnDate, txnRefNo, txnAmount, txnAuthStatus,
                null, billingService);

    }

    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    public void setFinancialsUtil(final FinancialsUtil financialsUtil) {
        this.financialsUtil = financialsUtil;
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void setChallanService(final ChallanService challanService) {
        this.challanService = challanService;
    }

}