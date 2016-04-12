/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.collection.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.AccountPayeeDetail;
import org.egov.collection.entity.Challan;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.entity.ReceiptMisc;
import org.egov.collection.entity.ReceiptVoucher;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.egov.collection.integration.pgi.PaymentResponse;
import org.egov.collection.integration.services.BillingIntegrationService;
import org.egov.collection.utils.CollectionsNumberGenerator;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.collection.utils.FinancialsUtil;
import org.egov.commons.Bankaccount;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.eis.entity.Employee;
import org.egov.eis.entity.Jurisdiction;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EmployeeService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.search.elastic.entity.CollectionIndex;
import org.egov.infra.search.elastic.service.CollectionIndexService;
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
    SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    private DesignationService designationService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DepartmentService departmentService;

    private ChallanService challanService;

    @Autowired
    private CollectionIndexService collectionIndexService;
    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    /**
     * @param statusCode Status code of receipts to be fetched. If null or ALL, then receipts with all statuses are fetched
     * @param userName User name of the user who has created the receipts. If null or ALL, then receipts of all users are fetched
     * @param counterId Counter id on which the receipts were created. If negative, then receipts from all counters are fetched
     * @param serviceCode Service code for which the receipts were created. If null or ALL, then receipts of all billing services
     * are fetched
     * @return List of all receipts created by given user from given counter id and having given status
     */
    public List<ReceiptHeader> findAllByPositionAndInboxItemDetails(final Long positionId, final String groupingCriteria) {
        final StringBuilder query = new StringBuilder("from org.egov.collection.entity.ReceiptHeader where 1=1");
        String wfAction = null, serviceCode = null, userName = null, receiptDate = null, receiptType = null;
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
        final boolean allPositions = positionId == null || positionId.equals(CollectionConstants.ALL);
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

        if (!allPositions)
            query.append(" and state.ownerPosition.id = :positionId");
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

        if (!allPositions)
            listQuery.setLong("positionId", positionId);
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
     * This method returns the internal reference numbers corresponding to the instrument type.
     *
     * @param entity an instance of <code>ReceiptHeader</code>
     * @return a <code>List</code> of strings , each representing the internal reference numbers for each instrument type for the
     * given receipt
     */
    public List<String> generateInternalReferenceNo(final ReceiptHeader entity) {
        final CFinancialYear financialYear = collectionsUtil.getFinancialYearforDate(entity.getCreatedDate());
        final CFinancialYear currentFinancialYear = collectionsUtil.getFinancialYearforDate(new Date());

        return collectionsNumberGenerator.generateInternalReferenceNumber(entity, financialYear, currentFinancialYear);
    }

    /**
     * This method is called for voucher creation into the financial system. For each receipt created in the collections module, a
     * voucher is created.
     *
     * @param receiptHeader Receipt header for which the pre-approval voucher is to be created
     * @param receiptBulkUpload
     * @return The created voucher
     */

    protected CVoucherHeader createVoucher(final ReceiptHeader receiptHeader) {
        final HashMap<String, Object> headerdetails = new HashMap<String, Object>(0);
        final List<HashMap<String, Object>> accountCodeList = new ArrayList<HashMap<String, Object>>(0);
        final List<HashMap<String, Object>> subledgerList = new ArrayList<HashMap<String, Object>>(0);
        String fundCode = null, fundsourceCode = null, departmentCode = null;
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
                    || instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_BANK)) {
                headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERNAME);
                headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE);
            } else if (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
                    || instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_DD)
                    || instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_CARD)
                    || instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_ONLINE))
                if (collectionsUtil.getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                        CollectionConstants.APPCONFIG_VALUE_RECEIPTVOUCHERTYPEFORCHEQUEDDCARD).equals(
                        CollectionConstants.FINANCIAL_JOURNALVOUCHER_VOUCHERTYPE)) {
                    headerdetails.put(VoucherConstant.VOUCHERNAME,
                            CollectionConstants.FINANCIAL_JOURNALVOUCHER_VOUCHERNAME);
                    headerdetails.put(VoucherConstant.VOUCHERTYPE,
                            CollectionConstants.FINANCIAL_JOURNALVOUCHER_VOUCHERTYPE);
                } else {
                    headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERNAME);
                    headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE);
                }
        headerdetails.put(VoucherConstant.DESCRIPTION, CollectionConstants.FINANCIAL_VOUCHERDESCRIPTION);
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            String dateString = "";
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

        if (receiptHeader.getVoucherNum() != null && !receiptHeader.getVoucherNum().equals(""))
            headerdetails.put(VoucherConstant.VOUCHERNUMBER, receiptHeader.getVoucherNum());

        headerdetails.put(VoucherConstant.FUNDCODE, fundCode);
        headerdetails.put(VoucherConstant.DEPARTMENTCODE, departmentCode);
        headerdetails.put(VoucherConstant.FUNDSOURCECODE, fundsourceCode);
        headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);
        headerdetails.put(VoucherConstant.SOURCEPATH,
                CollectionConstants.RECEIPT_VIEW_SOURCEPATH + receiptHeader.getId());

        Set<ReceiptDetail> receiptDetailSet = new LinkedHashSet<ReceiptDetail>(0);

        /**
         * Aggregate Amount in case of bill based receipt for account codes appearing more than once in receipt details
         */
        if (receiptHeader.getReceipttype() == 'B')
            receiptDetailSet = aggregateDuplicateReceiptDetailObject(new ArrayList<ReceiptDetail>(
                    receiptHeader.getReceiptDetails()));
        else
            receiptDetailSet = receiptHeader.getReceiptDetails();

        for (final ReceiptDetail receiptDetail : receiptDetailSet)
            if (receiptDetail.getCramount().compareTo(BigDecimal.ZERO) != 0
                    || receiptDetail.getDramount().compareTo(BigDecimal.ZERO) != 0) {

                final HashMap<String, Object> accountcodedetailsHashMap = new HashMap<String, Object>(0);
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

                        final HashMap<String, Object> subledgerdetailsHashMap = new HashMap<String, Object>(0);
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
     * Creates voucher for given receipt header and maps it with the same. Also updates the instrument voucher mapping in
     * financials.
     *
     * @param receiptHeader Receipt header for which voucher is to be created
     * @return The created voucher header
     */

    public CVoucherHeader createVoucherForReceipt(final ReceiptHeader receiptHeader) throws ApplicationRuntimeException {
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
     * Starts workflow for given set of receipt headers. Internally performs the following: 1. Start workflow 2. Transition
     * workflow state with action "Create Receipt" 3. Create vouchers (if required) 4. If vouchers created, transition workflow
     * state with action "Create Voucher"
     *
     * @param receiptHeaders set of receipt headers on which workflow is to be started
     * @param receiptBulkUpload
     */
    public void startWorkflow(final ReceiptHeader receiptHeader) throws ApplicationRuntimeException {
        final Boolean createVoucherForBillingService = collectionsUtil.checkVoucherCreation(receiptHeader);
        Position position = null;

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
     * Method to find all the Cash,Cheque and DD type instruments with status as :new and
     *
     * @return List of HashMap
     */
    public List<HashMap<String, Object>> findAllRemitanceDetails(final String boundaryIdList) {
        final List<HashMap<String, Object>> paramList = new ArrayList<HashMap<String, Object>>();
        // TODO: Fix the sum(ih.instrumentamount) the amount is wrong because of
        // the ujl.boundary in (" + boundaryIdList + ")"
        final String queryBuilder = "SELECT sum(ih.instrumentamount) as INSTRUMENTMAOUNT,to_char(ch.RECEIPTDATE, 'DD-MM-YYYY') AS RECEIPTDATE,"
                + "sd.NAME as SERVICENAME,it.TYPE as INSTRUMENTTYPE,fnd.name AS FUNDNAME,dpt.name AS DEPARTMENTNAME,"
                + "fnd.code AS FUNDCODE,dpt.code AS DEPARTMENTCODE from EGCL_COLLECTIONHEADER ch,"
                + "EGF_INSTRUMENTHEADER ih,EGCL_COLLECTIONINSTRUMENT ci,EGCL_SERVICEDETAILS sd,"
                + "EGF_INSTRUMENTTYPE it,EGCL_COLLECTIONMIS cm,FUND fnd,EG_DEPARTMENT dpt";

        final String whereClauseBeforInstumentType = " where ch.id=cm.collectionheader AND "
                + "fnd.id=cm.fund AND dpt.id=cm.department and ci.INSTRUMENTHEADER=ih.ID and "
                + "ch.SERVICEDETAILS=sd.ID and ch.ID=ci.COLLECTIONHEADER and ih.INSTRUMENTTYPE=it.ID and ";

        final String whereClause = " AND ih.ID_STATUS=(select id from egw_status where moduletype='"
                + CollectionConstants.MODULE_NAME_INSTRUMENTHEADER + "' " + "and description='"
                + CollectionConstants.INSTRUMENT_NEW_STATUS
                + "') and ih.ISPAYCHEQUE='0' and ch.STATUS=(select id from egw_status where " + "moduletype='"
                + CollectionConstants.MODULE_NAME_RECEIPTHEADER + "' and code='"
                + CollectionConstants.RECEIPT_STATUS_CODE_APPROVED + "') ";

        final String groupByClause = " group by to_char(ch.RECEIPTDATE, 'DD-MM-YYYY'),sd.NAME,it.TYPE,fnd.name,dpt.name,fnd.code,dpt.code";
        final String orderBy = " order by RECEIPTDATE";

        /**
         * Query to get the collection of the instrument types Cash,Cheque,DD & Card for bank remittance
         */
        final StringBuilder queryStringForCashChequeDDCard = new StringBuilder(queryBuilder + ",egeis_jurisdiction ujl"
                + whereClauseBeforInstumentType + "it.TYPE in ('" + CollectionConstants.INSTRUMENTTYPE_CASH + "','"
                + CollectionConstants.INSTRUMENTTYPE_CHEQUE + "'," + "'" + CollectionConstants.INSTRUMENTTYPE_DD
                + "','" + CollectionConstants.INSTRUMENTTYPE_CARD + "') " + whereClause
                + "AND ch.CREATEDBY=ujl.employee and ujl.boundary in (" + boundaryIdList + ")" + groupByClause);

        /**
         * If the department of login user is AccountCell .i.e., Department Code-'A',then this user will be able to remit online
         * transaction as well. All the online receipts created by 'citizen' user will be remitted by Account Cell user.
         */
        final User citizenUser = collectionsUtil.getUserByUserName(CollectionConstants.CITIZEN_USER_NAME);

        if (boundaryIdList != null && citizenUser != null) {
            final String queryStringForOnline = " union " + queryBuilder + whereClauseBeforInstumentType + "it.TYPE='"
                    + CollectionConstants.INSTRUMENTTYPE_ONLINE + "'" + whereClause + "AND ch.CREATEDBY="
                    + citizenUser.getId() + groupByClause;

            queryStringForCashChequeDDCard.append(queryStringForOnline);
        }

        final Query query = getSession().createSQLQuery(queryStringForCashChequeDDCard.toString() + orderBy);

        final List<Object[]> queryResults = query.list();

        for (int i = 0; i < queryResults.size(); i++) {
            final Object[] arrayObjectInitialIndex = queryResults.get(i);
            HashMap<String, Object> objHashMap = new HashMap<String, Object>(0);

            if (i == 0) {
                objHashMap.put(CollectionConstants.BANKREMITTANCE_RECEIPTDATE, arrayObjectInitialIndex[1]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICENAME, arrayObjectInitialIndex[2]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDNAME, arrayObjectInitialIndex[4]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTNAME, arrayObjectInitialIndex[5]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDCODE, arrayObjectInitialIndex[6]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTCODE, arrayObjectInitialIndex[7]);

                if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CASH)) {
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT,
                            arrayObjectInitialIndex[0]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
                }
                if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
                        || arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT,
                            arrayObjectInitialIndex[0]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
                }
                if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CARD)) {
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT,
                            arrayObjectInitialIndex[0]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
                }
                if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_ONLINE)) {
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT,
                            arrayObjectInitialIndex[0]);
                }
            } else {
                final int checknew = checkIfMapObjectExist(paramList, arrayObjectInitialIndex);
                if (checknew == -1) {
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_RECEIPTDATE, arrayObjectInitialIndex[1]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICENAME, arrayObjectInitialIndex[2]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDNAME, arrayObjectInitialIndex[4]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTNAME, arrayObjectInitialIndex[5]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDCODE, arrayObjectInitialIndex[6]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTCODE, arrayObjectInitialIndex[7]);

                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CASH)) {
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT,
                                arrayObjectInitialIndex[0]);
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
                    }
                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
                            || arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT,
                                arrayObjectInitialIndex[0]);
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
                    }
                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CARD)) {
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT,
                                arrayObjectInitialIndex[0]);
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
                    }
                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_ONLINE)) {
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT,
                                arrayObjectInitialIndex[0]);
                    }
                } else {
                    objHashMap = paramList.get(checknew);

                    paramList.remove(checknew);

                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CASH))
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT,
                                arrayObjectInitialIndex[0]);
                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
                            || arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
                        BigDecimal existingAmount = BigDecimal.ZERO;
                        if (objHashMap.get(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT) != "")
                            existingAmount = new BigDecimal(objHashMap.get(
                                    CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT).toString());
                        existingAmount = existingAmount.add(new BigDecimal(arrayObjectInitialIndex[0].toString()));
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, existingAmount);
                    }
                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CARD))
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT,
                                arrayObjectInitialIndex[0]);
                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_ONLINE))
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT,
                                arrayObjectInitialIndex[0]);
                }
            }
            if (objHashMap.get(CollectionConstants.BANKREMITTANCE_RECEIPTDATE) != null
                    && objHashMap.get(CollectionConstants.BANKREMITTANCE_SERVICENAME) != null)
                paramList.add(objHashMap);
        }
        return paramList;
    }

    /**
     * Method to find all the Cash,Cheque and DD type instruments with status as :new and
     *
     * @return List of HashMap
     */
    public List<HashMap<String, Object>> findAllRemittanceDetailsForServiceAndFund(final String boundaryIdList,
            final String serviceCodes, final String fundCodes) {
        final List<HashMap<String, Object>> paramList = new ArrayList<HashMap<String, Object>>();
        // TODO: Fix the sum(ih.instrumentamount) the amount is wrong because of
        // the ujl.boundary in (" + boundaryIdList + ")"
        final String queryBuilder = "SELECT sum(ih.instrumentamount) as INSTRUMENTMAOUNT,to_char(ch.RECEIPTDATE, 'DD-MM-YYYY') AS RECEIPTDATE,"
                + "sd.NAME as SERVICENAME,it.TYPE as INSTRUMENTTYPE,fnd.name AS FUNDNAME,dpt.name AS DEPARTMENTNAME,"
                + "fnd.code AS FUNDCODE,dpt.code AS DEPARTMENTCODE from EGCL_COLLECTIONHEADER ch,"
                + "EGF_INSTRUMENTHEADER ih,EGCL_COLLECTIONINSTRUMENT ci,EGCL_SERVICEDETAILS sd,"
                + "EGF_INSTRUMENTTYPE it,EGCL_COLLECTIONMIS cm,FUND fnd,EG_DEPARTMENT dpt";

        final String whereClauseBeforInstumentType = " where ch.id=cm.collectionheader AND "
                + "fnd.id=cm.fund AND dpt.id=cm.department and ci.INSTRUMENTHEADER=ih.ID and "
                + "ch.SERVICEDETAILS=sd.ID and ch.ID=ci.COLLECTIONHEADER and ih.INSTRUMENTTYPE=it.ID and ";

        final String whereClauseForServiceAndFund = " sd.code in (" + serviceCodes + ")" + " and fnd.code in ("
                + fundCodes + ")" + " and ";

        final String whereClause = " AND ih.ID_STATUS=(select id from egw_status where moduletype='"
                + CollectionConstants.MODULE_NAME_INSTRUMENTHEADER + "' " + "and description='"
                + CollectionConstants.INSTRUMENT_NEW_STATUS
                + "') and ih.ISPAYCHEQUE='0' and ch.STATUS=(select id from egw_status where " + "moduletype='"
                + CollectionConstants.MODULE_NAME_RECEIPTHEADER + "' and code='"
                + CollectionConstants.RECEIPT_STATUS_CODE_APPROVED + "') ";

        final String groupByClause = " group by to_char(ch.RECEIPTDATE, 'DD-MM-YYYY'),sd.NAME,it.TYPE,fnd.name,dpt.name,fnd.code,dpt.code";
        final String orderBy = " order by RECEIPTDATE";

        /**
         * Query to get the collection of the instrument types Cash,Cheque,DD & Card for bank remittance
         */
        final StringBuilder queryStringForCashChequeDDCard = new StringBuilder(queryBuilder + ",egeis_jurisdiction ujl"
                + whereClauseBeforInstumentType + whereClauseForServiceAndFund + "it.TYPE in ('"
                + CollectionConstants.INSTRUMENTTYPE_CASH + "','" + CollectionConstants.INSTRUMENTTYPE_CHEQUE + "',"
                + "'" + CollectionConstants.INSTRUMENTTYPE_DD + "','" + CollectionConstants.INSTRUMENTTYPE_CARD + "') "
                + whereClause + "AND ch.CREATEDBY=ujl.employee and ujl.boundary in (" + boundaryIdList + ")"
                + groupByClause);

        /**
         * If the department of login user is AccountCell .i.e., Department Code-'A',then this user will be able to remit online
         * transaction as well. All the online receipts created by 'citizen' user will be remitted by Account Cell user.
         */
        final User citizenUser = collectionsUtil.getUserByUserName(CollectionConstants.CITIZEN_USER_NAME);

        if (boundaryIdList != null && citizenUser != null) {
            final String queryStringForOnline = " union " + queryBuilder + whereClauseBeforInstumentType
                    + whereClauseForServiceAndFund + "it.TYPE='" + CollectionConstants.INSTRUMENTTYPE_ONLINE + "'"
                    + whereClause + "AND ch.CREATEDBY=" + citizenUser.getId() + groupByClause;

            queryStringForCashChequeDDCard.append(queryStringForOnline);
        }

        final Query query = getSession().createSQLQuery(queryStringForCashChequeDDCard.toString() + orderBy);

        final List<Object[]> queryResults = query.list();

        for (int i = 0; i < queryResults.size(); i++) {
            final Object[] arrayObjectInitialIndex = queryResults.get(i);
            HashMap<String, Object> objHashMap = new HashMap<String, Object>(0);

            if (i == 0) {
                objHashMap.put(CollectionConstants.BANKREMITTANCE_RECEIPTDATE, arrayObjectInitialIndex[1]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICENAME, arrayObjectInitialIndex[2]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDNAME, arrayObjectInitialIndex[4]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTNAME, arrayObjectInitialIndex[5]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDCODE, arrayObjectInitialIndex[6]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTCODE, arrayObjectInitialIndex[7]);

                if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CASH)) {
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT,
                            arrayObjectInitialIndex[0]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
                }
                if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
                        || arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT,
                            arrayObjectInitialIndex[0]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
                }
                if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CARD)) {
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT,
                            arrayObjectInitialIndex[0]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
                }
                if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_ONLINE)) {
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT,
                            arrayObjectInitialIndex[0]);
                }
            } else {
                final int checknew = checkIfMapObjectExist(paramList, arrayObjectInitialIndex);
                if (checknew == -1) {
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_RECEIPTDATE, arrayObjectInitialIndex[1]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICENAME, arrayObjectInitialIndex[2]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDNAME, arrayObjectInitialIndex[4]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTNAME, arrayObjectInitialIndex[5]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDCODE, arrayObjectInitialIndex[6]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTCODE, arrayObjectInitialIndex[7]);

                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CASH)) {
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT,
                                arrayObjectInitialIndex[0]);
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
                    }
                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
                            || arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT,
                                arrayObjectInitialIndex[0]);
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
                    }
                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CARD)) {
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT,
                                arrayObjectInitialIndex[0]);
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
                    }
                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_ONLINE)) {
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT,
                                arrayObjectInitialIndex[0]);
                    }
                } else {
                    objHashMap = paramList.get(checknew);

                    paramList.remove(checknew);

                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CASH))
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT,
                                arrayObjectInitialIndex[0]);
                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
                            || arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
                        BigDecimal existingAmount = BigDecimal.ZERO;
                        if (objHashMap.get(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT) != "")
                            existingAmount = new BigDecimal(objHashMap.get(
                                    CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT).toString());
                        existingAmount = existingAmount.add(new BigDecimal(arrayObjectInitialIndex[0].toString()));
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, existingAmount);
                    }
                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CARD))
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT,
                                arrayObjectInitialIndex[0]);
                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_ONLINE))
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT,
                                arrayObjectInitialIndex[0]);
                }
            }
            if (objHashMap.get(CollectionConstants.BANKREMITTANCE_RECEIPTDATE) != null
                    && objHashMap.get(CollectionConstants.BANKREMITTANCE_SERVICENAME) != null)
                paramList.add(objHashMap);
        }
        return paramList;
    }

    /**
     * Method to check if the given HashMap already exists in the List of HashMap
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

    /**
     * Create Contra Vouchers for String array passed of serviceName, totalCashAmount, totalChequeAmount, totalCardAmount and
     * totalOnlineAcount
     *
     * @param serviceName
     * @param totalCashAmount
     * @param totalChequeAmount
     * @return List of Contra Vouchers Created
     */
    @Transactional
    public List createBankRemittance(final String[] serviceNameArr, final String[] totalCashAmount,
            final String[] totalChequeAmount, final String[] totalCardAmount, final String[] totalOnlineAmount,
            final String[] receiptDateArray, final String[] fundCodeArray, final String[] departmentCodeArray,
            final Integer accountNumberId, final Integer positionUser, final String[] receiptNumberArray) {
        final List<CVoucherHeader> newContraVoucherList = new ArrayList<CVoucherHeader>(0);
        final List<ReceiptHeader> bankRemittanceList = new ArrayList<ReceiptHeader>(0);
        final SimpleDateFormat dateFomatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final Map<String, Object> instrumentDepositeMap = financialsUtil.prepareForUpdateInstrumentDepositSQL();
        final String instrumentGlCodeQueryString = "SELECT COA.GLCODE FROM CHARTOFACCOUNTS COA,EGF_INSTRUMENTACCOUNTCODES IAC,EGF_INSTRUMENTTYPE IT "
                + "WHERE IT.ID=IAC.TYPEID AND IAC.GLCODEID=COA.ID AND IT.TYPE=";
        final String receiptInstrumentQueryString = "select DISTINCT (instruments) from org.egov.collection.entity.ReceiptHeader receipt "
                + "join receipt.receiptInstrument as instruments where ";
        final String serviceNameCondition = "receipt.service.name=? ";
        final String receiptDateCondition = "and to_char(receipt.receiptdate,'dd-MM-yyyy')=? ";
        final String instrumentStatusCondition = "and instruments.statusId.id=? ";
        final String instrumentTypeCondition = "and instruments.instrumentType.type = ? ";
        final String receiptFundCondition = "and receipt.receiptMisc.fund.code = ? ";
        final String receiptDepartmentCondition = "and receipt.receiptMisc.department.code = ? ";

        final String cashInHandQueryString = instrumentGlCodeQueryString + "'"
                + CollectionConstants.INSTRUMENTTYPE_CASH + "'";
        final String chequeInHandQueryString = instrumentGlCodeQueryString + "'"
                + CollectionConstants.INSTRUMENTTYPE_CHEQUE + "'";
        final String cardPaymentQueryString = instrumentGlCodeQueryString + "'"
                + CollectionConstants.INSTRUMENTTYPE_CARD + "'";
        final String onlinePaymentQueryString = instrumentGlCodeQueryString + "'"
                + CollectionConstants.INSTRUMENTTYPE_ONLINE + "'";

        final Query cashInHand = getSession().createSQLQuery(cashInHandQueryString);
        final Query chequeInHand = getSession().createSQLQuery(chequeInHandQueryString);
        final Query cardPaymentAccount = getSession().createSQLQuery(cardPaymentQueryString);
        final Query onlinePaymentAccount = getSession().createSQLQuery(onlinePaymentQueryString);

        String cashInHandGLCode = null, chequeInHandGlcode = null, cardPaymentGlCode = null, onlinePaymentGlCode = null;

        final String voucherWorkflowMsg = "Voucher Workflow Started";

        final String createVoucher = collectionsUtil.getAppConfigValue(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.APPCONFIG_VALUE_CREATEVOUCHER_FOR_REMITTANCE);
        final String functionCode = collectionsUtil.getAppConfigValue(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.APPCONFIG_VALUE_COLLECTION_BANKREMITTANCE_FUNCTIONCODE);

        if (!cashInHand.list().isEmpty())
            cashInHandGLCode = cashInHand.list().get(0).toString();
        if (!chequeInHand.list().isEmpty())
            chequeInHandGlcode = chequeInHand.list().get(0).toString();
        if (!cardPaymentAccount.list().isEmpty())
            cardPaymentGlCode = cardPaymentAccount.list().get(0).toString();
        if (!onlinePaymentAccount.list().isEmpty())
            onlinePaymentGlCode = onlinePaymentAccount.list().get(0).toString();

        final EgwStatus status = collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_INSTRUMENTHEADER, CollectionConstants.INSTRUMENT_NEW_STATUS);

        /**
         * Get the AppConfig parameter defined for the Remittance voucher type in case of instrument type Cheque,DD & Card
         */

        Boolean voucherTypeForChequeDDCard = false;
        Boolean useReceiptDateAsContraVoucherDate = false;

        if (collectionsUtil.getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.APPCONFIG_VALUE_REMITTANCEVOUCHERTYPEFORCHEQUEDDCARD).equals(
                CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE))
            voucherTypeForChequeDDCard = true;

        if (collectionsUtil.getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.APPCONFIG_VALUE_USERECEIPTDATEFORCONTRA).equals(CollectionConstants.YES))
            useReceiptDateAsContraVoucherDate = true;

        final EgwStatus instrumentStatusDeposited = collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_INSTRUMENTHEADER, CollectionConstants.INSTRUMENT_DEPOSITED_STATUS);

        for (int i = 0; i < serviceNameArr.length; i++) {
            final String serviceName = serviceNameArr[i].trim();
            Date voucherDate;
            try {
                voucherDate = sdf.parse(sdf.format(new Date()));
            } catch (ParseException e) {
               
                LOGGER.debug("Exception in parsing date  " + receiptDateArray[i] + " - " + e.getMessage());
                throw new ApplicationRuntimeException("Exception while parsing date", e);
            }
            

            if (useReceiptDateAsContraVoucherDate)
                try {
                    voucherDate = dateFomatter.parse(receiptDateArray[i]);
                } catch (final ParseException exp) {
                    LOGGER.debug("Exception in parsing date  " + receiptDateArray[i] + " - " + exp.getMessage());
                    throw new ApplicationRuntimeException("Exception while parsing date", exp);
                }

            if (serviceName != null && serviceName.length() > 0) {
                final Bankaccount depositedBankAccount = (Bankaccount) persistenceService.find(
                        "from Bankaccount where id=?", Long.valueOf(accountNumberId.longValue()));
                final ServiceDetails serviceDetails = (ServiceDetails) persistenceService.findByNamedQuery(
                        CollectionConstants.QUERY_SERVICE_BY_NAME, serviceName);

                final String serviceGlCode = depositedBankAccount.getChartofaccounts().getGlcode();
                final List<HashMap<String, Object>> subledgerList = new ArrayList<HashMap<String, Object>>();

                // If Cash Amount is present
                if (totalCashAmount[i].trim() != null && totalCashAmount[i].trim().length() > 0
                        && cashInHandGLCode != null) {
                    final StringBuilder cashQueryBuilder = new StringBuilder(receiptInstrumentQueryString);
                    cashQueryBuilder.append(serviceNameCondition);
                    cashQueryBuilder.append(receiptDateCondition);
                    cashQueryBuilder.append(instrumentStatusCondition);
                    cashQueryBuilder.append(instrumentTypeCondition);
                    cashQueryBuilder.append(receiptFundCondition);
                    cashQueryBuilder.append(receiptDepartmentCondition);

                    final Object arguments[] = new Object[6];

                    arguments[0] = serviceName;
                    arguments[1] = receiptDateArray[i];
                    arguments[2] = status.getId();
                    arguments[3] = CollectionConstants.INSTRUMENTTYPE_CASH;
                    arguments[4] = fundCodeArray[i];
                    arguments[5] = departmentCodeArray[i];

                    final List<InstrumentHeader> instrumentHeaderListCash = persistenceService.findAllBy(
                            cashQueryBuilder.toString(), arguments);

                    if (CollectionConstants.YES.equalsIgnoreCase(createVoucher) && serviceDetails.getVoucherCreation()) {
                        final HashMap<String, Object> headerdetails = new HashMap<String, Object>(0);

                        headerdetails.put(VoucherConstant.VOUCHERNAME,
                                CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
                        headerdetails.put(VoucherConstant.VOUCHERTYPE,
                                CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
                        headerdetails
                                .put(VoucherConstant.DESCRIPTION, CollectionConstants.FINANCIAL_VOUCHERDESCRIPTION);
                        headerdetails.put(VoucherConstant.VOUCHERDATE, voucherDate);
                        headerdetails.put(VoucherConstant.FUNDCODE, fundCodeArray[i]);
                        headerdetails.put(VoucherConstant.DEPARTMENTCODE, CollectionConstants.DEPT_CODE_FOR_ACCOUNTS);
                        headerdetails.put(VoucherConstant.FUNDSOURCECODE, serviceDetails.getFundSource() == null ? null
                                : serviceDetails.getFundSource().getCode());
                        headerdetails.put(VoucherConstant.FUNCTIONARYCODE,
                                serviceDetails.getFunctionary() == null ? null : serviceDetails.getFunctionary()
                                        .getCode());
                        headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);
                        headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);

                        final List<HashMap<String, Object>> accountCodeCashList = new ArrayList<HashMap<String, Object>>(
                                0);
                        final HashMap<String, Object> accountcodedetailsCreditCashHashMap = new HashMap<String, Object>(
                                0);
                        accountcodedetailsCreditCashHashMap.put(VoucherConstant.GLCODE, cashInHandGLCode);
                        accountcodedetailsCreditCashHashMap.put(VoucherConstant.FUNCTIONCODE, functionCode);
                        accountcodedetailsCreditCashHashMap.put(VoucherConstant.CREDITAMOUNT, totalCashAmount[i]);
                        accountcodedetailsCreditCashHashMap.put(VoucherConstant.DEBITAMOUNT, 0);

                        accountCodeCashList.add(accountcodedetailsCreditCashHashMap);

                        final HashMap<String, Object> accountcodedetailsDebitHashMap = new HashMap<String, Object>(0);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.GLCODE, serviceGlCode);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.FUNCTIONCODE, functionCode);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.CREDITAMOUNT, 0);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.DEBITAMOUNT, totalCashAmount[i]);
                        accountCodeCashList.add(accountcodedetailsDebitHashMap);

                        final CVoucherHeader voucherHeaderCash = financialsUtil.createRemittanceVoucher(headerdetails,
                                accountCodeCashList, subledgerList);

                        newContraVoucherList.add(voucherHeaderCash);
                        createVoucherForCashRemittance(instrumentDepositeMap, voucherWorkflowMsg, voucherDate,
                                depositedBankAccount, serviceGlCode, instrumentHeaderListCash, voucherHeaderCash);
                    } else
                        financialsUtil.updateInstrumentHeader(instrumentHeaderListCash, instrumentStatusDeposited,
                                depositedBankAccount);

                    for (final InstrumentHeader instHead : instrumentHeaderListCash) {
                        final List<ReceiptHeader> receiptHeaders = findAllByNamedQuery(
                                CollectionConstants.QUERY_RECEIPTS_BY_INSTRUMENTHEADER_AND_SERVICECODE,
                                instHead.getId(), serviceDetails.getCode());
                        bankRemittanceList.addAll(receiptHeaders);
                    }

                }
                // If Cheque Amount is present
                if (totalChequeAmount[i].trim() != null && totalChequeAmount[i].trim().length() > 0
                        && chequeInHandGlcode != null) {
                    final StringBuilder chequeQueryBuilder = new StringBuilder(receiptInstrumentQueryString);
                    chequeQueryBuilder.append(serviceNameCondition);
                    chequeQueryBuilder.append(receiptDateCondition);
                    chequeQueryBuilder.append(instrumentStatusCondition);
                    chequeQueryBuilder.append("and instruments.instrumentType.type in ( ?, ?)");
                    chequeQueryBuilder
                            .append("and receipt.status.id=(select id from org.egov.commons.EgwStatus where moduletype=? and code=?) ");
                    chequeQueryBuilder.append(receiptFundCondition);
                    chequeQueryBuilder.append(receiptDepartmentCondition);

                    final Object arguments[] = new Object[9];

                    arguments[0] = serviceName;
                    arguments[1] = receiptDateArray[i];
                    arguments[2] = status.getId();
                    arguments[3] = CollectionConstants.INSTRUMENTTYPE_CHEQUE;
                    arguments[4] = CollectionConstants.INSTRUMENTTYPE_DD;
                    arguments[5] = CollectionConstants.MODULE_NAME_RECEIPTHEADER;
                    arguments[6] = CollectionConstants.RECEIPT_STATUS_CODE_APPROVED;
                    arguments[7] = fundCodeArray[i];
                    arguments[8] = departmentCodeArray[i];

                    final List<InstrumentHeader> instrumentHeaderListCheque = persistenceService.findAllBy(
                            chequeQueryBuilder.toString(), arguments);
                    if (CollectionConstants.YES.equalsIgnoreCase(createVoucher) && serviceDetails.getVoucherCreation()) {
                        final HashMap<String, Object> headerdetails = new HashMap<String, Object>(0);
                        final List<HashMap<String, Object>> accountCodeChequeList = new ArrayList<HashMap<String, Object>>(
                                0);
                        final HashMap<String, Object> accountcodedetailsCreditChequeHashMap = new HashMap<String, Object>(
                                0);

                        if (voucherTypeForChequeDDCard) {
                            headerdetails.put(VoucherConstant.VOUCHERNAME,
                                    CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERNAME);
                            headerdetails.put(VoucherConstant.VOUCHERTYPE,
                                    CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE);

                        } else {
                            headerdetails.put(VoucherConstant.VOUCHERNAME,
                                    CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
                            headerdetails.put(VoucherConstant.VOUCHERTYPE,
                                    CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
                        }
                        headerdetails.put(VoucherConstant.VOUCHERNAME,
                                CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
                        headerdetails.put(VoucherConstant.VOUCHERTYPE,
                                CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
                        headerdetails
                                .put(VoucherConstant.DESCRIPTION, CollectionConstants.FINANCIAL_VOUCHERDESCRIPTION);
                        headerdetails.put(VoucherConstant.VOUCHERDATE, voucherDate);
                        headerdetails.put(VoucherConstant.FUNDCODE, fundCodeArray[i]);
                        headerdetails.put(VoucherConstant.DEPARTMENTCODE, CollectionConstants.DEPT_CODE_FOR_ACCOUNTS);
                        headerdetails.put(VoucherConstant.FUNDSOURCECODE, serviceDetails.getFundSource() == null ? null
                                : serviceDetails.getFundSource().getCode());
                        headerdetails.put(VoucherConstant.FUNCTIONARYCODE,
                                serviceDetails.getFunctionary() == null ? null : serviceDetails.getFunctionary()
                                        .getCode());
                        headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);

                        accountcodedetailsCreditChequeHashMap.put(VoucherConstant.GLCODE, chequeInHandGlcode);
                        accountcodedetailsCreditChequeHashMap.put(VoucherConstant.FUNCTIONCODE, functionCode);
                        accountcodedetailsCreditChequeHashMap.put(VoucherConstant.CREDITAMOUNT, totalChequeAmount[i]);
                        accountcodedetailsCreditChequeHashMap.put(VoucherConstant.DEBITAMOUNT, 0);

                        accountCodeChequeList.add(accountcodedetailsCreditChequeHashMap);

                        final HashMap<String, Object> accountcodedetailsDebitHashMap = new HashMap<String, Object>(0);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.GLCODE, serviceGlCode);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.FUNCTIONCODE, functionCode);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.CREDITAMOUNT, 0);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.DEBITAMOUNT, totalChequeAmount[i]);
                        accountCodeChequeList.add(accountcodedetailsDebitHashMap);

                        final CVoucherHeader voucherHeaderCheque = financialsUtil.createRemittanceVoucher(
                                headerdetails, accountCodeChequeList, subledgerList);

                        newContraVoucherList.add(voucherHeaderCheque);
                        createVoucherForChequeCardRemittance(instrumentDepositeMap, voucherWorkflowMsg,
                                voucherTypeForChequeDDCard, voucherDate, depositedBankAccount, serviceGlCode,
                                instrumentHeaderListCheque, voucherHeaderCheque);
                    } else
                        financialsUtil.updateInstrumentHeader(instrumentHeaderListCheque, instrumentStatusDeposited,
                                depositedBankAccount);

                    for (final InstrumentHeader instHead : instrumentHeaderListCheque) {
                        final List<ReceiptHeader> receiptHeaders = findAllByNamedQuery(
                                CollectionConstants.QUERY_RECEIPTS_BY_INSTRUMENTHEADER_AND_SERVICECODE,
                                instHead.getId(), serviceDetails.getCode());
                        bankRemittanceList.addAll(receiptHeaders);
                    }
                }
                // If card amount is present
                if (totalCardAmount[i].trim() != null && totalCardAmount[i].trim().length() > 0
                        && cardPaymentGlCode != null) {
                    final StringBuilder onlineQueryBuilder = new StringBuilder(receiptInstrumentQueryString);
                    onlineQueryBuilder.append(serviceNameCondition);
                    onlineQueryBuilder.append(receiptDateCondition);
                    onlineQueryBuilder.append(instrumentStatusCondition);
                    onlineQueryBuilder.append(instrumentTypeCondition);
                    onlineQueryBuilder.append(receiptFundCondition);
                    onlineQueryBuilder.append(receiptDepartmentCondition);

                    final Object arguments[] = new Object[6];

                    arguments[0] = serviceName;
                    arguments[1] = receiptDateArray[i];
                    arguments[2] = status.getId();
                    arguments[3] = CollectionConstants.INSTRUMENTTYPE_CARD;
                    arguments[4] = fundCodeArray[i];
                    arguments[5] = departmentCodeArray[i];

                    final List<InstrumentHeader> instrumentHeaderListOnline = persistenceService.findAllBy(
                            onlineQueryBuilder.toString(), arguments);

                    if (CollectionConstants.YES.equalsIgnoreCase(createVoucher) && serviceDetails.getVoucherCreation()) {
                        final HashMap<String, Object> headerdetails = new HashMap<String, Object>(0);

                        if (voucherTypeForChequeDDCard) {
                            headerdetails.put(VoucherConstant.VOUCHERNAME,
                                    CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERNAME);
                            headerdetails.put(VoucherConstant.VOUCHERTYPE,
                                    CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE);
                        } else {

                            headerdetails.put(VoucherConstant.VOUCHERNAME,
                                    CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
                            headerdetails.put(VoucherConstant.VOUCHERTYPE,
                                    CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
                        }
                        headerdetails.put(VoucherConstant.VOUCHERNAME,
                                CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
                        headerdetails.put(VoucherConstant.VOUCHERTYPE,
                                CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
                        headerdetails
                                .put(VoucherConstant.DESCRIPTION, CollectionConstants.FINANCIAL_VOUCHERDESCRIPTION);
                        headerdetails.put(VoucherConstant.VOUCHERDATE, voucherDate);
                        headerdetails.put(VoucherConstant.FUNDCODE, fundCodeArray[i]);
                        headerdetails.put(VoucherConstant.DEPARTMENTCODE, CollectionConstants.DEPT_CODE_FOR_ACCOUNTS);
                        headerdetails.put(VoucherConstant.FUNDSOURCECODE, serviceDetails.getFundSource() == null ? null
                                : serviceDetails.getFundSource().getCode());
                        headerdetails.put(VoucherConstant.FUNCTIONARYCODE,
                                serviceDetails.getFunctionary() == null ? null : serviceDetails.getFunctionary()
                                        .getCode());
                        headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);

                        final List<HashMap<String, Object>> accountCodeOnlineList = new ArrayList<HashMap<String, Object>>();
                        final HashMap<String, Object> accountcodedetailsCreditOnlineHashMap = new HashMap<String, Object>();

                        accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.GLCODE, cardPaymentGlCode);
                        accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.FUNCTIONCODE, functionCode);
                        accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.CREDITAMOUNT, totalCardAmount[i]);
                        accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.DEBITAMOUNT, 0);

                        accountCodeOnlineList.add(accountcodedetailsCreditOnlineHashMap);
                        final HashMap<String, Object> accountcodedetailsDebitHashMap = new HashMap<String, Object>();
                        accountcodedetailsDebitHashMap.put(VoucherConstant.GLCODE, serviceGlCode);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.FUNCTIONCODE, functionCode);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.CREDITAMOUNT, 0);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.DEBITAMOUNT, totalCardAmount[i]);
                        accountCodeOnlineList.add(accountcodedetailsDebitHashMap);

                        final CVoucherHeader voucherHeaderCard = financialsUtil.createRemittanceVoucher(headerdetails,
                                accountCodeOnlineList, subledgerList);

                        newContraVoucherList.add(voucherHeaderCard);
                        createVoucherForChequeCardRemittance(instrumentDepositeMap, voucherWorkflowMsg,
                                voucherTypeForChequeDDCard, voucherDate, depositedBankAccount, serviceGlCode,
                                instrumentHeaderListOnline, voucherHeaderCard);
                    } else
                        financialsUtil.updateInstrumentHeader(instrumentHeaderListOnline, instrumentStatusDeposited,
                                depositedBankAccount);

                    for (final InstrumentHeader instHead : instrumentHeaderListOnline) {
                        final List<ReceiptHeader> receiptHeaders = findAllByNamedQuery(
                                CollectionConstants.QUERY_RECEIPTS_BY_INSTRUMENTHEADER_AND_SERVICECODE,
                                instHead.getId(), serviceDetails.getCode());
                        bankRemittanceList.addAll(receiptHeaders);
                    }

                }
                // If online amount is present
                if (totalOnlineAmount[i].trim() != null && totalOnlineAmount[i].trim().length() > 0
                        && onlinePaymentGlCode != null) {
                    final StringBuilder onlineQueryBuilder = new StringBuilder(receiptInstrumentQueryString);
                    onlineQueryBuilder.append(serviceNameCondition);
                    onlineQueryBuilder.append(receiptDateCondition);
                    onlineQueryBuilder.append(instrumentStatusCondition);
                    onlineQueryBuilder.append(instrumentTypeCondition);
                    onlineQueryBuilder.append(receiptFundCondition);
                    onlineQueryBuilder.append(receiptDepartmentCondition);

                    final Object arguments[] = new Object[6];

                    arguments[0] = serviceName;
                    arguments[1] = receiptDateArray[i];
                    arguments[2] = status.getId();
                    arguments[3] = CollectionConstants.INSTRUMENTTYPE_ONLINE;
                    arguments[4] = fundCodeArray[i];
                    arguments[5] = departmentCodeArray[i];

                    final List<InstrumentHeader> instrumentHeaderListOnline = persistenceService.findAllBy(
                            onlineQueryBuilder.toString(), arguments);

                    if (CollectionConstants.YES.equalsIgnoreCase(createVoucher) && serviceDetails.getVoucherCreation()) {
                        final HashMap<String, Object> headerdetails = new HashMap<String, Object>(0);

                        if (voucherTypeForChequeDDCard) {
                            headerdetails.put(VoucherConstant.VOUCHERNAME,
                                    CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERNAME);
                            headerdetails.put(VoucherConstant.VOUCHERTYPE,
                                    CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE);
                        } else {

                            headerdetails.put(VoucherConstant.VOUCHERNAME,
                                    CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
                            headerdetails.put(VoucherConstant.VOUCHERTYPE,
                                    CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
                        }
                        headerdetails.put(VoucherConstant.VOUCHERNAME,
                                CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
                        headerdetails.put(VoucherConstant.VOUCHERTYPE,
                                CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
                        headerdetails
                                .put(VoucherConstant.DESCRIPTION, CollectionConstants.FINANCIAL_VOUCHERDESCRIPTION);
                        headerdetails.put(VoucherConstant.VOUCHERDATE, voucherDate);
                        headerdetails.put(VoucherConstant.FUNDCODE, fundCodeArray[i]);
                        headerdetails.put(VoucherConstant.DEPARTMENTCODE, CollectionConstants.DEPT_CODE_FOR_ACCOUNTS);
                        headerdetails.put(VoucherConstant.FUNDSOURCECODE, serviceDetails.getFundSource() == null ? null
                                : serviceDetails.getFundSource().getCode());
                        headerdetails.put(VoucherConstant.FUNCTIONARYCODE,
                                serviceDetails.getFunctionary() == null ? null : serviceDetails.getFunctionary()
                                        .getCode());
                        headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);

                        final List<HashMap<String, Object>> accountCodeOnlineList = new ArrayList<HashMap<String, Object>>(
                                0);
                        final HashMap<String, Object> accountcodedetailsCreditOnlineHashMap = new HashMap<String, Object>(
                                0);

                        accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.GLCODE, onlinePaymentGlCode);
                        accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.FUNCTIONCODE, functionCode);
                        accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.CREDITAMOUNT, totalOnlineAmount[i]);
                        accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.DEBITAMOUNT, 0);

                        accountCodeOnlineList.add(accountcodedetailsCreditOnlineHashMap);
                        final HashMap<String, Object> accountcodedetailsDebitHashMap = new HashMap<String, Object>(0);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.GLCODE, serviceGlCode);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.FUNCTIONCODE, functionCode);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.CREDITAMOUNT, 0);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.DEBITAMOUNT, totalOnlineAmount[i]);
                        accountCodeOnlineList.add(accountcodedetailsDebitHashMap);

                        final CVoucherHeader voucherHeaderCard = financialsUtil.createRemittanceVoucher(headerdetails,
                                accountCodeOnlineList, subledgerList);
                        newContraVoucherList.add(voucherHeaderCard);

                        createVoucherForChequeCardRemittance(instrumentDepositeMap, voucherWorkflowMsg,
                                voucherTypeForChequeDDCard, voucherDate, depositedBankAccount, serviceGlCode,
                                instrumentHeaderListOnline, voucherHeaderCard);
                    } else
                        financialsUtil.updateInstrumentHeader(instrumentHeaderListOnline, instrumentStatusDeposited,
                                depositedBankAccount);

                    for (final InstrumentHeader instHead : instrumentHeaderListOnline) {
                        final List<ReceiptHeader> receiptHeaders = findAllByNamedQuery(
                                CollectionConstants.QUERY_RECEIPTS_BY_INSTRUMENTHEADER_AND_SERVICECODE,
                                instHead.getId(), serviceDetails.getCode());
                        bankRemittanceList.addAll(receiptHeaders);
                    }

                }
            }
        }
        for (final ReceiptHeader receiptHeader : bankRemittanceList) {
            final EgwStatus statusRemitted = collectionsUtil.getStatusForModuleAndCode(
                    CollectionConstants.MODULE_NAME_RECEIPTHEADER, CollectionConstants.RECEIPT_STATUS_CODE_REMITTED);
            receiptHeader.setStatus(statusRemitted);
            getSession().flush();
            super.update(receiptHeader);
        }

        return bankRemittanceList;
    }

    @Transactional
    public void createVoucherForChequeCardRemittance(final Map<String, Object> instrumentDepositeMap,
            final String voucherWorkflowMsg, final Boolean voucherTypeForChequeDDCard, final Date voucherDate,
            final Bankaccount depositedBankAccount, final String serviceGlCode,
            final List<InstrumentHeader> instrumentHeaderListCheque, final CVoucherHeader voucherHeaderCheque) {
        final EgwStatus instrumentStatusDeposited = collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_INSTRUMENTHEADER, CollectionConstants.INSTRUMENT_DEPOSITED_STATUS);
        for (final InstrumentHeader instrumentHeader : instrumentHeaderListCheque) {
            final InstrumentHeader instrumentHeaderObj = financialsUtil.updateInstrumentHeaderStatus(instrumentHeader,
                    instrumentStatusDeposited, depositedBankAccount);
            if (voucherHeaderCheque.getId() != null && serviceGlCode != null) {
                final Map<String, Object> chequeMap = constructInstrumentMap(instrumentDepositeMap,
                        depositedBankAccount, instrumentHeaderObj, voucherHeaderCheque, voucherDate);
                if (voucherTypeForChequeDDCard)
                    financialsUtil.updateCheque_DD_Card_Deposit_Receipt(voucherHeaderCheque.getId(), serviceGlCode,
                            instrumentHeaderObj, chequeMap);
                else
                    financialsUtil.updateCheque_DD_Card_Deposit(voucherHeaderCheque.getId(), serviceGlCode,
                            instrumentHeaderObj, chequeMap);
            }
        }
    }

    @Transactional
    public void createVoucherForCashRemittance(final Map<String, Object> instrumentDepositeMap,
            final String voucherWorkflowMsg, final Date voucherDate, final Bankaccount depositedBankAccount,
            final String serviceGlCode, final List<InstrumentHeader> instrumentHeaderListCash,
            final CVoucherHeader voucherHeaderCash) {
        final EgwStatus instrumentStatusDeposited = collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_INSTRUMENTHEADER, CollectionConstants.INSTRUMENT_DEPOSITED_STATUS);
        for (final InstrumentHeader instrumentHeader : instrumentHeaderListCash)
            if (voucherHeaderCash.getId() != null && serviceGlCode != null) {
                final Map<String, Object> cashMap = constructInstrumentMap(instrumentDepositeMap, depositedBankAccount,
                        instrumentHeader, voucherHeaderCash, voucherDate);
                final InstrumentHeader instrumentHeaderObj = financialsUtil.updateInstrumentHeaderStatus(
                        instrumentHeader, instrumentStatusDeposited, depositedBankAccount);
                financialsUtil
                        .updateCashDeposit(voucherHeaderCash.getId(), serviceGlCode, instrumentHeaderObj, cashMap);
            }
    }

    /**
     * For Bill Based Receipt, aggregate the amount for same account heads
     *
     * @param receiptDetailSetParam
     * @return Set of Receipt Detail after Aggregating Amounts
     */
    public Set<ReceiptDetail> aggregateDuplicateReceiptDetailObject(final List<ReceiptDetail> receiptDetailSetParam) {
        final List<ReceiptDetail> newReceiptDetailList = new ArrayList<ReceiptDetail>(0);

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
        return new HashSet<ReceiptDetail>(newReceiptDetailList);
    }

    /**
     * API to check if the given receipt detail object already exists in the list passed passed as parameter
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
     * @param receiptHeaders Set of receipt headers to be transitioned
     * @param actionName Action name for the transition
     * @param comment Comment for the transition
     */
    public void endReceiptWorkFlowOnCancellation(final ReceiptHeader receiptHeaderToBeCancelled) {
        // End work-flow for the cancelled receipt
        Position position = null;
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
     * This method persists the given <code>ReceiptPayeeDetails</code> entity. The receipt number for all of the receipts is
     * generated, if not already present. If the receipt is associated with a challan, and the challan number is not present, the
     * challan number is generated and set into it.
     */
    @Override
    @Transactional
    public ReceiptHeader persist(final ReceiptHeader receiptHeader) throws ApplicationRuntimeException {
        if (receiptHeader.getReceipttype() != CollectionConstants.RECEIPT_TYPE_CHALLAN
                && (!CollectionConstants.RECEIPT_STATUS_CODE_PENDING.equals(receiptHeader.getStatus().getCode()) || !CollectionConstants.RECEIPT_STATUS_CODE_FAILED
                        .equals(receiptHeader.getStatus().getCode())) && receiptHeader.getReceiptnumber() == null)
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
                updateBillingSystemWithReceiptInfo(receiptHeader, null);
        }
        // For bill based collection, push data to index only upon successful
        // update to billing system
        if (!receiptHeader.getService().getServiceType().equalsIgnoreCase(CollectionConstants.SERVICE_TYPE_BILLING)
                && !CollectionConstants.RECEIPT_STATUS_CODE_FAILED.equals(receiptHeader.getStatus().getCode())
                && !CollectionConstants.RECEIPT_STATUS_CODE_PENDING.equals(receiptHeader.getStatus().getCode()))
            updateCollectionIndex(receiptHeader);

        return super.persist(receiptHeader);
    }

    /**
     * This method persists the given <code>ReceiptPayeeDetails</code> entity. If the receipt number for all of the receipts is
     * generated, if not already present.
     */

    @Transactional
    public ReceiptHeader persistChallan(final ReceiptHeader receiptHeader, final Position position,
            final String actionName, final String approvalRemarks) throws ApplicationRuntimeException {
        final Integer validUpto = Integer.valueOf(collectionsUtil.getAppConfigValue(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.APPCONFIG_VALUE_CHALLANVALIDUPTO));

        final Challan challan = receiptHeader.getChallan();
        /*
         * challan.setValidUpto(eisService.getPriorOrAfterWorkingDate(challan. getChallanDate(), validUpto, DATE_ORDER.AFTER));
         */
        final Calendar c = new GregorianCalendar();
        c.add(Calendar.DATE, validUpto);
        challan.setValidUpto(c.getTime());

        /* if (challan.getCreatedDate() == null) */
        /* challan.setCreatedDate(new Date()); */

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
     * This method persists the given set of <code>ReceiptPayeeDetails</code> instances with receipt number as Pending
     *
     * @param entity a set of <code>ReceiptPayeeDetails</code> instances to be persisted
     * @return the list of persisted <code>ReceiptPayeeDetails</code> instances
     */
    @Transactional
    public ReceiptHeader persistReceiptsObject(final ReceiptHeader receiptHeader) {
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
     * This method looks up the bean to communicate with the billing system and updates the billing system.
     */

    @Transactional
    public Boolean updateBillingSystem(final ServiceDetails serviceDetails, final Set<BillReceiptInfo> billReceipts,
            BillingIntegrationService billingService) throws ApplicationRuntimeException {
        if (!serviceDetails.getServiceType().equals(CollectionConstants.SERVICE_TYPE_BILLING))
            return true;
        else if (billingService == null && serviceDetails.getServiceType().equals(CollectionConstants.SERVICE_TYPE_BILLING))
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
     * This method is called for voucher reversal in case of intra-day receipt cancellation.
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
     * @ Create instrument voucher list from receiptpayeelist and pass it to financialsutil
     *
     * @param receiptPayeeDetails
     * @return void
     */

    public void updateInstrument(final ReceiptHeader receiptHeader) {
        final List<Map<String, Object>> instrumentVoucherList = new ArrayList<Map<String, Object>>(0);
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

    public void performWorkflow(final String actionName, final ReceiptHeader receiptHeader, final String remarks)
            throws ApplicationRuntimeException {
        try {
            Position operatorPosition;
            Employee employee = null;
            final Boolean isEmployee = collectionsUtil.isEmployee(receiptHeader.getCreatedBy());
            if (!isEmployee) {
                employee = employeeService.getEmployeeById(collectionsUtil.getLoggedInUser().getId());
                operatorPosition = collectionsUtil.getPositionByDeptDesgAndBoundary(receiptHeader.getReceiptMisc()
                        .getBoundary());
            } else {
                operatorPosition = collectionsUtil.getPositionOfUser(receiptHeader.getCreatedBy());
                employee = employeeService.getEmployeeById(receiptHeader.getCreatedBy().getId());
            }
            final Department department = departmentService.getDepartmentByName(collectionsUtil.getAppConfigValue(
                    CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                    CollectionConstants.COLLECTION_DEPARTMENTFORWORKFLOWAPPROVER));
            final Designation designation = designationService.getDesignationByName(collectionsUtil.getAppConfigValue(
                    CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                    CollectionConstants.COLLECTION_DESIGNATIONFORAPPROVER));
            Boundary boundary = null;
            for (final Jurisdiction jur : employee.getJurisdictions())
                boundary = jur.getBoundary();
            final List<Employee> emp = employeeService.findByDepartmentDesignationAndBoundary(department.getId(),
                    designation.getId(), boundary.getId());
            if (emp.isEmpty())
                throw new ValidationException(Arrays.asList(new ValidationError("Manager does not exists",
                        "submitcollections.validation.error.manager.notexists")));
            final Position approverPosition = collectionsUtil.getPositionOfUser(emp.get(0));
            if (actionName.equals(CollectionConstants.WF_ACTION_SUBMIT))
                perform(receiptHeader, CollectionConstants.WF_ACTION_APPROVE,
                        CollectionConstants.RECEIPT_STATUS_CODE_SUBMITTED, CollectionConstants.WF_ACTION_APPROVE,
                        approverPosition, remarks);
            else if (actionName.equals(CollectionConstants.WF_ACTION_APPROVE))
                perform(receiptHeader, CollectionConstants.WF_STATE_APPROVED,
                        CollectionConstants.RECEIPT_STATUS_CODE_APPROVED, "", approverPosition, remarks);
            else if (actionName.equals(CollectionConstants.WF_ACTION_REJECT))
                perform(receiptHeader, CollectionConstants.WF_STATE_REJECTED,
                        CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED, CollectionConstants.WF_ACTION_SUBMIT,
                        operatorPosition, remarks);
        } catch (final ValidationException e) {
            LOGGER.error(e.getErrors());
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {
            final String errorMsg = "Receipt Service Exception while workflow transition!";
            LOGGER.error(errorMsg, e);
            throw new ApplicationRuntimeException(e.getMessage());
        }
    }

    @Transactional
    public void perform(final ReceiptHeader receiptHeader, final String wfState, final String newStatusCode,
            final String nextAction, final Position ownerPosition, final String remarks)
            throws ApplicationRuntimeException {
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
        getSession().flush();
        super.persist(receiptHeader);
        updateCollectionIndex(receiptHeader);
    }

    public Set<InstrumentHeader> createOnlineInstrument(final Date transactionDate, final String transactionId,
            final BigDecimal transactionAmt) {
        final InstrumentHeader onlineInstrumentHeader = new InstrumentHeader();
        Set<InstrumentHeader> instrumentHeaderSet = new HashSet<InstrumentHeader>(0);
        onlineInstrumentHeader.setInstrumentType(financialsUtil
                .getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_ONLINE));
        onlineInstrumentHeader.setTransactionDate(transactionDate);
        onlineInstrumentHeader.setIsPayCheque(CollectionConstants.ZERO_INT);
        onlineInstrumentHeader.setTransactionNumber(transactionId);
        onlineInstrumentHeader.setInstrumentAmount(transactionAmt);

        final List<InstrumentHeader> instHeaderList = new ArrayList<InstrumentHeader>();
        instHeaderList.add(onlineInstrumentHeader);
        instrumentHeaderSet = new HashSet<InstrumentHeader>(createInstrument(instHeaderList));
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

        if (bankAccId != null && bankAccId != 0)
            sb.append(" AND ih.bankaccountid=" + bankAccId + "");
        if ((bankAccId == null || bankAccId == 0) && bankId != null && bankId != 0)
            sb.append(" AND ih.bankid=" + bankAccId + "");
        if (!"".equals(chequeDDNo) && chequeDDNo != null)
            sb.append(" AND ih.instrumentnumber=trim('" + chequeDDNo + "') ");
        if (!"".equals(chqueDDDate) && chqueDDDate != null)
            sb.append(" AND ih.instrumentdate >= '" + chqueDDDate + "' ");

        return sb.toString();
    }

    /**
     * This method performs the following for receipts to be newly created:
     * <ol>
     * <li>The user instrument header details, and actual amount paid by user is captured.</li>
     * <li>A debit receipt detail account head is created for the total credit collected.</li>
     * <li>Vouchers are created</li>
     * </ol>
     * <p>
     * The receipts are persisted and work flow is started for these persisted receipts where in the receipt state is set to NEW
     * The billing system is updated about the persisted receipts. These include details of both newly created as well as
     * cancelled receipts. If the instrument list and voucher list are not empty, the .... is updated The receipt ids of the newly
     * created receipts are collectively populated to be shown on the print screen
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
            if (receiptHeader.getService().getServiceType().equalsIgnoreCase(CollectionConstants.SERVICE_TYPE_BILLING)) {

                updateBillingSystemWithReceiptInfo(receiptHeader, null);
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

    /**
     * This method sets the status of the receipt to be cancelled as CANCELLED and persists it A new receipt header object is
     * populated with the data contained in the cancelled receipt. This instance is added to the model.
     *
     * @param receiptHeaderToBeCancelled
     */

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

    /**
     * Updates the billing system with receipt information
     *
     * @param receiptHeader
     */
    @Transactional
    public void updateBillingSystemWithReceiptInfo(final ReceiptHeader receiptHeader,
            final BillingIntegrationService billingService) throws ApplicationRuntimeException {

        /**
         * for each receipt created, send the details back to the billing system
         */
        LOGGER.info("$$$$$$ Update Billing system for Service Code :"
                + receiptHeader.getService().getCode()
                + (receiptHeader.getConsumerCode() != null ? " and consumer code: " + receiptHeader.getConsumerCode()
                        : ""));
        final Set<BillReceiptInfo> billReceipts = new HashSet<BillReceiptInfo>(0);
        billReceipts.add(new BillReceiptInfoImpl(receiptHeader, chartOfAccountsHibernateDAO));

        if (updateBillingSystem(receiptHeader.getService(), billReceipts, billingService)) {
            receiptHeader.setIsReconciled(true);
            // the receipts should be persisted again
            super.persist(receiptHeader);
            updateCollectionIndex(receiptHeader);
            getSession().flush();
        }
        LOGGER.info("$$$$$$ Billing system updated for Service Code :"
                + receiptHeader.getService().getCode()
                + (receiptHeader.getConsumerCode() != null ? " and consumer code: " + receiptHeader.getConsumerCode()
                        : ""));
    }

    @Transactional
    public void updateCollectionIndex(final ReceiptHeader receiptHeader) {
        final CollectionIndex collectionIndex = collectionsUtil.constructCollectionIndex(receiptHeader);
        collectionIndexService.pushCollectionIndex(collectionIndex);
    }

    /**
     * @param receipts - list of receipts which have to be processed as successful payments. For payments created as a response
     * from bill desk, size of the array will be 1.
     */
    public ReceiptHeader createOnlineSuccessPayment(final ReceiptHeader receiptHeader, final Date transactionDate,
            final String transactionId, final BigDecimal transactionAmt, final String authStatusCode,
            final String remarks, final BillingIntegrationService billingService) {
        final EgwStatus receiptStatus = collectionsUtil
                .getReceiptStatusForCode(CollectionConstants.RECEIPT_STATUS_CODE_APPROVED);
        receiptHeader.setStatus(receiptStatus);

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
        getSession().flush();
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
            throw new ApplicationRuntimeException("Failed to create voucher in Financials");
        }
        updateBillingSystemWithReceiptInfo(receiptHeader, billingService);
        return receiptHeader;
    }

    @Transactional
    public void persistFieldReceipt(final ReceiptHeader receiptHeader, final List<InstrumentHeader> instrumentHeaderList) {
        final Set<InstrumentHeader> instHeaderSet = new HashSet(createInstrument(instrumentHeaderList));
        receiptHeader.setReceiptInstrument(instHeaderSet);
        persist(receiptHeader);
        LOGGER.info("Receipt Created with receipt number: " + receiptHeader.getReceiptnumber());
        updateFinancialAndBillingSystem(receiptHeader, null);
        LOGGER.info("Billing system updated with receipt info");
    }

    @Transactional
    public void updateDishonoredInstrumentStatus(final ReceiptHeader receiptHeader,
            final InstrumentHeader instrumentHeader, final EgwStatus receiptStatus, final boolean isReconciled) {
        financialsUtil.updateInstrumentHeader(instrumentHeader);
        // update receipts - set status to INSTR_BOUNCED and recon flag to false
        updateReceiptHeaderStatus(receiptHeader, receiptStatus, false);
        LOGGER.debug("Updated receipt status to " + receiptStatus.getCode() + " set reconcilation to false");

        updateBillingSystemWithReceiptInfo(receiptHeader, null);
    }

    /**
     * This method updates the status and reconciliation flag for the given receipt
     *
     * @param receiptHeader <code>ReceiptHeader</code> objects whose status and reconciliation flag have to be modified
     * @param status a <code>EgwStatus</code> instance representing the state to which the receipt has to be updated with
     * @param isReconciled a <code>Boolean</code> flag indicating the value for the reconciliation status
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
            final PaymentResponse paymentResponse, final BillingIntegrationService billingService,
            final List<ReceiptDetail> reconstructedList, final ReceiptDetail debitAccountDetail) {

        if (reconstructedList != null) {
            onlinePaymentReceiptHeader.getReceiptDetails().clear();
            persistReceiptsObject(onlinePaymentReceiptHeader);
            LOGGER.debug("Reconstructed receiptDetailList : " + reconstructedList.toString());
            for (final ReceiptDetail receiptDetail : reconstructedList) {
                receiptDetail.setReceiptHeader(onlinePaymentReceiptHeader);
                onlinePaymentReceiptHeader.addReceiptDetail(receiptDetail);
            }
            onlinePaymentReceiptHeader.addReceiptDetail(debitAccountDetail);

        }

        return createOnlineSuccessPayment(onlinePaymentReceiptHeader, paymentResponse.getTxnDate(),
                paymentResponse.getTxnReferenceNo(), paymentResponse.getTxnAmount(), paymentResponse.getAuthStatus(),
                null, billingService);

    }

}
