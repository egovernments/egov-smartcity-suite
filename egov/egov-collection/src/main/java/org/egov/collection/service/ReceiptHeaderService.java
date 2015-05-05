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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import org.egov.collection.integration.services.BillingIntegrationService;
import org.egov.collection.utils.CollectionsNumberGenerator;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.collection.utils.FinancialsUtil;
import org.egov.commons.Bankaccount;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsServiceImpl;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.contra.ContraJournalVoucher;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentType;
import org.egov.pims.commons.Position;
import org.hibernate.Query;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provides services related to receipt header
 */
@Transactional(readOnly=true)
public class ReceiptHeaderService extends PersistenceService<ReceiptHeader, Long> {

	private static final Logger LOGGER = Logger.getLogger(ReceiptHeaderService.class);
	/*private final ReceiptHeaderRepository receiptHeaderRepository;

	@Autowired
	public ReceiptHeaderService(final ReceiptHeaderRepository receiptHeaderRepository) {
		this.receiptHeaderRepository = receiptHeaderRepository;
	}*/
	
/*	@PersistenceContext
    private EntityManager entityManager;

    public Session getSession() {
        return entityManager.unwrap(Session.class);
    }*/

	@Autowired
	private CollectionsUtil collectionsUtil;
	private CollectionsNumberGenerator collectionsNumberGenerator;
	private FinancialsUtil financialsUtil;
	private PersistenceService persistenceService;

	private CommonsServiceImpl commonsServiceImpl;

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
	public List<ReceiptHeader> findAllByStatusUserCounterService(String statusCode, String userName, Integer counterId, String serviceCode) {
		StringBuilder query = new StringBuilder("select receipt from org.egov.collection.entity.ReceiptHeader receipt where 1=1");

		boolean allStatuses = (statusCode == null || statusCode.equals(CollectionConstants.ALL));
		boolean allCounters = (counterId == null || counterId < 0);
		boolean allUsers = (userName == null || userName.equals(CollectionConstants.ALL));
		boolean allServices = (serviceCode == null || serviceCode.equals(CollectionConstants.ALL));

		int argCount = 0;
		argCount += allStatuses ? 0 : 1;
		argCount += allCounters ? 0 : 1;
		argCount += allUsers ? 0 : 1;
		argCount += allServices ? 0 : 1;
		Object arguments[] = new Object[argCount];

		argCount = 0;
		if (!allStatuses) {
			query.append(" and receipt.status.code = ?");
			arguments[argCount++] = statusCode;
		}

		if (!allUsers) {
			query.append(" and receipt.createdBy.userName = ?");
			arguments[argCount++] = userName;
		}

		if (!allCounters) {
			query.append(" and receipt.location.id = ?");
			arguments[argCount++] = counterId;
		}

		if (!allServices) {
			query.append(" and receipt.service.code = ?");
			arguments[argCount++] = serviceCode;
		}

		query.append(" order by receipt.createdDate desc");

		return this.findAllBy(query.toString(), arguments);
	}

	/**
	 * This method returns the internal reference numbers corresponding to the
	 * instrument type.
	 * 
	 * @param entity
	 *            an instance of <code>ReceiptHeader</code>
	 * 
	 * @return a <code>List</code> of strings , each representing the internal
	 *         reference numbers for each instrument type for the given receipt
	 */
	public List<String> generateInternalReferenceNo(ReceiptHeader entity) {
		CFinancialYear financialYear = collectionsUtil.getFinancialYearforDate(entity.getCreatedDate().toDate());
		CFinancialYear currentFinancialYear = collectionsUtil.getFinancialYearforDate(new Date());

		return collectionsNumberGenerator.generateInternalReferenceNumber(entity, financialYear, currentFinancialYear);
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
	 * 
	 */
	@Transactional
	protected CVoucherHeader createVoucher(ReceiptHeader receiptHeader, Boolean receiptBulkUpload) {
		HashMap<String, Object> headerdetails = new HashMap<String, Object>();
		List<HashMap<String, Object>> accountCodeList = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> subledgerList = new ArrayList<HashMap<String, Object>>();
		String fundCode = null, fundsourceCode = null, departmentCode = null;
		Boolean isVoucherApproved = Boolean.FALSE;

		if (receiptHeader.getService().getIsVoucherApproved() != null) {
			isVoucherApproved = receiptHeader.getService().getIsVoucherApproved();
		}

		ReceiptMisc receiptMisc = receiptHeader.getReceiptMisc();
		if (receiptMisc.getFund() != null) {
			fundCode = receiptMisc.getFund().getCode();
		}
		if (receiptMisc.getFundsource() != null) {
			fundsourceCode = receiptMisc.getFundsource().getCode();
		}
		if (receiptMisc.getDepartment() != null) {
			departmentCode = receiptMisc.getDepartment().getCode();
		}

		for (InstrumentHeader instrumentHeader : receiptHeader.getReceiptInstrument()) {
			if (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_CASH)
					|| instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_BANK)) {
				headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERNAME);
				headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE);
			} else if ((instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE))
					|| (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_DD))
					|| (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_CARD))
					|| (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_ONLINE))) {
				if ((collectionsUtil.getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG, CollectionConstants.APPCONFIG_VALUE_RECEIPTVOUCHERTYPEFORCHEQUEDDCARD))
						.equals(CollectionConstants.FINANCIAL_JOURNALVOUCHER_VOUCHERTYPE)) {
					headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_JOURNALVOUCHER_VOUCHERNAME);
					headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_JOURNALVOUCHER_VOUCHERTYPE);
				} else {
					headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERNAME);
					headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE);
				}

			}
		}
		headerdetails.put(VoucherConstant.DESCRIPTION, CollectionConstants.FINANCIAL_VOUCHERDESCRIPTION);
		if (receiptHeader.getVoucherDate() == null) {
			headerdetails.put(VoucherConstant.VOUCHERDATE, new Date());
		} else {
			headerdetails.put(VoucherConstant.VOUCHERDATE, receiptHeader.getVoucherDate());
		}

		if (receiptHeader.getVoucherNum() != null && !receiptHeader.getVoucherNum().equals("")) {
			headerdetails.put(VoucherConstant.VOUCHERNUMBER, receiptHeader.getVoucherNum());
		}

		headerdetails.put(VoucherConstant.FUNDCODE, fundCode);
		headerdetails.put(VoucherConstant.DEPARTMENTCODE, departmentCode);
		headerdetails.put(VoucherConstant.FUNDSOURCECODE, fundsourceCode);
		headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);
		headerdetails.put(VoucherConstant.SOURCEPATH, CollectionConstants.RECEIPT_VIEW_SOURCEPATH + receiptHeader.getId());

		Set<ReceiptDetail> receiptDetailSet = new LinkedHashSet<ReceiptDetail>(0);

		/**
		 * Aggregate Amount in case of bill based receipt for account codes
		 * appearing more than once in receipt details
		 */
		if (receiptHeader.getReceipttype() == 'B') {

			receiptDetailSet = aggregateDuplicateReceiptDetailObject(new ArrayList<ReceiptDetail>(receiptHeader.getReceiptDetails()));
		} else {
			receiptDetailSet = receiptHeader.getReceiptDetails();
		}

		for (ReceiptDetail receiptDetail : receiptDetailSet) {
			if (receiptDetail.getCramount().compareTo(BigDecimal.ZERO) != 0 || receiptDetail.getDramount().compareTo(BigDecimal.ZERO) != 0) {

				HashMap<String, Object> accountcodedetailsHashMap = new HashMap<String, Object>();
				accountcodedetailsHashMap.put(VoucherConstant.GLCODE, receiptDetail.getAccounthead().getGlcode());

				accountcodedetailsHashMap.put(VoucherConstant.DEBITAMOUNT, receiptDetail.getDramount().compareTo(BigDecimal.ZERO) == 0 ? 0 : receiptDetail.getDramount());
				accountcodedetailsHashMap.put(VoucherConstant.CREDITAMOUNT, receiptDetail.getCramount().compareTo(BigDecimal.ZERO) == 0 ? 0 : receiptDetail.getCramount());
				if (receiptDetail.getFunction() != null)
					accountcodedetailsHashMap.put(VoucherConstant.FUNCTIONCODE, receiptDetail.getFunction().getCode());
				accountCodeList.add(accountcodedetailsHashMap);

				for (AccountPayeeDetail accpayeeDetail : receiptDetail.getAccountPayeeDetails()) {
					if (accpayeeDetail.getAmount().compareTo(BigDecimal.ZERO) != 0) {

						HashMap<String, Object> subledgerdetailsHashMap = new HashMap<String, Object>();
						subledgerdetailsHashMap.put(VoucherConstant.GLCODE, accpayeeDetail.getReceiptDetail().getAccounthead().getGlcode());
						subledgerdetailsHashMap.put(VoucherConstant.DETAILTYPEID, accpayeeDetail.getAccountDetailType().getId());
						subledgerdetailsHashMap.put(VoucherConstant.DETAILKEYID, accpayeeDetail.getAccountDetailKey().getDetailkey());
						if (accpayeeDetail.getReceiptDetail().getCramount().compareTo(BigDecimal.ZERO) != 0) {
							subledgerdetailsHashMap.put(VoucherConstant.CREDITAMOUNT, accpayeeDetail.getAmount().compareTo(BigDecimal.ZERO) == 0 ? 0 : accpayeeDetail.getAmount());
						} else if (accpayeeDetail.getReceiptDetail().getDramount().compareTo(BigDecimal.ZERO) != 0) {
							subledgerdetailsHashMap.put(VoucherConstant.DEBITAMOUNT, accpayeeDetail.getAmount().compareTo(BigDecimal.ZERO) == 0 ? 0 : accpayeeDetail.getAmount());
						}
						subledgerList.add(subledgerdetailsHashMap);

					}
				}

			}

		}

		return financialsUtil.createVoucher(headerdetails, accountCodeList, subledgerList, receiptBulkUpload, isVoucherApproved);
	}

	/**
	 * Creates voucher for given receipt header and maps it with the same.
	 * 
	 * @param receiptHeader
	 *            Receipt header for which voucher is to be created
	 * @return The created voucher header
	 */
	@Transactional
	public CVoucherHeader createVoucherForReceipt(ReceiptHeader receiptHeader, Boolean receiptBulkUpload) throws EGOVRuntimeException {
		CVoucherHeader voucherheader = null;

		// Additional check for challan Based Receipt, if the receipt cancelled
		// before remittance
		// then need to check the instrument status of that receipt in order to
		// create voucher
		// as the challan has a 'PENDING' receipt object associated with it
		boolean isParentReceiptInstrumentDeposited = false;

		if (receiptHeader.getReceiptHeader() != null) {
			for (InstrumentHeader instrumentHeader : receiptHeader.getReceiptHeader().getReceiptInstrument()) {

				if (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_CASH)) {
					if (instrumentHeader.getStatusId().getDescription().equals(CollectionConstants.INSTRUMENT_RECONCILED_STATUS)) {
						isParentReceiptInstrumentDeposited = true;
						break;
					}
				} else {
					if (instrumentHeader.getStatusId().getDescription().equals(CollectionConstants.INSTRUMENT_DEPOSITED_STATUS)) {
						isParentReceiptInstrumentDeposited = true;
						break;
					}
				}
			}
		}

		if (receiptHeader.getReceiptHeader() == null || (receiptHeader.getReceiptHeader() != null && !isParentReceiptInstrumentDeposited)) {
			voucherheader = createVoucher(receiptHeader, receiptBulkUpload);
			if (voucherheader != null) {
				ReceiptVoucher receiptVoucher = new ReceiptVoucher();
				receiptVoucher.setVoucherheader(voucherheader);
				receiptVoucher.setReceiptHeader(receiptHeader);
				receiptHeader.addReceiptVoucher(receiptVoucher);
			}
		}

		LOGGER.debug("Created voucher for receipt : " + receiptHeader.getReceiptnumber());

		return voucherheader;
	}

	/**
	 * Creates vouchers for given set of receipt headers
	 * 
	 * @param receiptHeaders
	 *            receipt headers for which vouchers are to be created
	 */
	@Transactional
	public void createVouchers(ReceiptHeader receiptHeader, Boolean receiptBulkUpload) throws EGOVRuntimeException {
			createVoucherForReceipt(receiptHeader, receiptBulkUpload);
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
	public void startWorkflow(ReceiptHeader receiptHeader, Boolean receiptBulkUpload) throws EGOVRuntimeException {
		Boolean createVoucherForBillingService = Boolean.TRUE;
		//for (ReceiptHeader receiptHeader : receiptHeaders) {
			createVoucherForBillingService = null == receiptHeader.getService().getVoucherCreation() ? Boolean.FALSE : receiptHeader.getService().getVoucherCreation();
			if (receiptHeader.getState() == null)
	               
			receiptHeader.transition(true).start().withSenderName(receiptHeader.getCreatedBy().getName()).withComments("Receipt created - work flow starts")
			 				.withStateValue(CollectionConstants.WF_STATE_NEW).withOwner(collectionsUtil.getPositionOfUser(receiptHeader.getCreatedBy())).withDateInfo(new Date());
		//}

		transition(receiptHeader, CollectionConstants.WF_ACTION_CREATE_RECEIPT, "Receipt created");

		LOGGER.debug("Workflow state transition complete");

		if (createVoucherForBillingService) {
			createVouchers(receiptHeader, receiptBulkUpload);
			transition(receiptHeader, CollectionConstants.WF_ACTION_CREATE_VOUCHER, "Receipt voucher created");
		}

		if (receiptBulkUpload) {
			//for (ReceiptHeader receiptHeader : receiptHeaders) {
				// transition the receipt header workflow to Approved state
				receiptHeader.transition(true).transition().withSenderName(receiptHeader.getCreatedBy().getName()).withComments("Approval of Data Migration Receipt Complete")
				.withStateValue(CollectionConstants.WF_ACTION_APPROVE).withOwner(collectionsUtil.getPositionOfUser(receiptHeader.getCreatedBy())).withDateInfo(new Date());
				// End the Receipt header workflow
				receiptHeader.transition(true).end().withSenderName(receiptHeader.getCreatedBy().getName()).withComments("Data Migration Receipt Approved - Workflow ends")
					.withStateValue(CollectionConstants.WF_STATE_END).withOwner(collectionsUtil.getPositionOfUser(receiptHeader.getCreatedBy())).withDateInfo(new Date());
			//}
		}
	}

	/**
	 * Transitions the given set of receipt headers with given action
	 * 
	 * @param receiptHeaders
	 *            Set of receipt headers to be transitioned
	 * @param actionName
	 *            Action name for the transition
	 * @param comment
	 *            Comment for the transition
	 */
	public void transition(ReceiptHeader receiptHeader, String actionName, String comment) {
			receiptHeader.transition(true).transition().withComments(comment).withStateValue(actionName);
	}

	/**
	 * Method to find all the Cash,Cheque and DD type instruments with status as
	 * :new and
	 * 
	 * @return List of HashMap
	 */
	public List<HashMap<String, Object>> findAllRemitanceDetails(String boundaryIdList) {
		List<HashMap<String, Object>> paramList = new ArrayList<HashMap<String, Object>>();

		String queryBuilder = "SELECT sum(ih.instrumentamount) as INSTRUMENTMAOUNT,vh.VOUCHERDATE AS VOUCHERDATE,"
				+ "sd.SERVICENAME as SERVICENAME,it.TYPE as INSTRUMENTTYPE,fnd.name AS FUNDNAME,dpt.dept_name AS DEPARTMENTNAME,"
				+ "fnd.code AS FUNDCODE,dpt.dept_code AS DEPARTMENTCODE from EGCL_COLLECTIONHEADER ch,VOUCHERHEADER vh,"
				+ "EGCL_COLLECTIONVOUCHER cv,EGF_INSTRUMENTHEADER ih,EGCL_COLLECTIONINSTRUMENT ci,EG_SERVICEDETAILS sd,"
				+ "EGF_INSTRUMENTTYPE it,EGCL_COLLECTIONMIS cm,FUND fnd,EG_DEPARTMENT dpt";

		String whereClauseBeforInstumentType = " where ch.id=cv.COLLECTIONHEADERID AND ch.id=cm.id_collectionheader AND "
				+ "fnd.id=cm.id_fund AND dpt.id_dept=cm.id_department and vh.id=cv.VOUCHERHEADERID and ci.INSTRUMENTMASTERID=ih.ID and "
				+ "ch.ID_SERVICE=sd.ID and ch.ID=ci.COLLECTIONHEADERID and ih.INSTRUMENTTYPE=it.ID and ";

		String whereClause = " AND ih.ID_STATUS=(select id from egw_status where moduletype='" + CollectionConstants.MODULE_NAME_INSTRUMENTHEADER + "' " + "and description='"
				+ CollectionConstants.INSTRUMENT_NEW_STATUS + "') and ih.ISPAYCHEQUE=0 and vh.MODULEID=(select id from eg_modules " + "where name='"
				+ CollectionConstants.MODULE_NAME_COLLECTIONS + "') and ch.ID_STATUS=(select id from egw_status where " + "moduletype='"
				+ CollectionConstants.MODULE_NAME_RECEIPTHEADER + "' and code='" + CollectionConstants.RECEIPT_STATUS_CODE_APPROVED + "') ";

		String groupByClause = " group by vh.VOUCHERDATE,sd.SERVICENAME,it.TYPE,fnd.name,dpt.dept_name,fnd.code,dpt.dept_code";
		String orderBy = " order by VOUCHERDATE";

		/**
		 * Query to get the collection of the instrument types Cash,Cheque,DD &
		 * Card for bank remittance
		 */
		StringBuilder queryStringForCashChequeDDCard = new StringBuilder(queryBuilder + ",EG_USER_JURLEVEL ujl,EG_USER_JURVALUES ujv" + whereClauseBeforInstumentType
				+ "it.TYPE in ('" + CollectionConstants.INSTRUMENTTYPE_CASH + "','" + CollectionConstants.INSTRUMENTTYPE_CHEQUE + "'," + "'"
				+ CollectionConstants.INSTRUMENTTYPE_DD + "','" + CollectionConstants.INSTRUMENTTYPE_CARD + "') " + whereClause
				+ "AND ch.CREATED_BY=ujl.ID_USER AND ujl.id_user_jurlevel= ujv.id_user_jurlevel and ujv.id_bndry in (" + boundaryIdList + ")" + groupByClause);

		/**
		 * If the department of login user is AccountCell .i.e., Department
		 * Code-'A',then this user will be able to remit online transaction as
		 * well. All the online receipts created by 'citizen' user will be
		 * remitted by Account Cell user.
		 */
		User citizenUser = collectionsUtil.getUserByUserName(CollectionConstants.CITIZEN_USER_NAME);

		if (boundaryIdList != null && citizenUser != null) {
			String queryStringForOnline = " union " + queryBuilder + whereClauseBeforInstumentType + "it.TYPE='" + CollectionConstants.INSTRUMENTTYPE_ONLINE + "'" + whereClause
					+ "AND ch.CREATED_BY=" + citizenUser.getId() + groupByClause;

			queryStringForCashChequeDDCard.append(queryStringForOnline);
		}

		Query query = this.getSession().createSQLQuery(queryStringForCashChequeDDCard.toString() + orderBy);

		List<Object[]> queryResults = query.list();

		for (int i = 0; i < queryResults.size(); i++) {
			Object[] arrayObjectInitialIndex = queryResults.get(i);
			HashMap<String, Object> objHashMap = new HashMap<String, Object>();

			if (i == 0) {
				objHashMap.put(CollectionConstants.BANKREMITTANCE_VOUCHERDATE, arrayObjectInitialIndex[1]);
				objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICENAME, arrayObjectInitialIndex[2]);
				objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDNAME, arrayObjectInitialIndex[4]);
				objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTNAME, arrayObjectInitialIndex[5]);
				objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDCODE, arrayObjectInitialIndex[6]);
				objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTCODE, arrayObjectInitialIndex[7]);

				if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CASH)) {
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, arrayObjectInitialIndex[0]);
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
				}
				if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE) || arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, arrayObjectInitialIndex[0]);
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
				}
				if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CARD)) {
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, arrayObjectInitialIndex[0]);
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
				}
				if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_ONLINE)) {
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, arrayObjectInitialIndex[0]);
				}
			} else {
				int checknew = checkIfMapObjectExist(paramList, arrayObjectInitialIndex);
				if (checknew == -1) {
					objHashMap.put(CollectionConstants.BANKREMITTANCE_VOUCHERDATE, arrayObjectInitialIndex[1]);
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICENAME, arrayObjectInitialIndex[2]);
					objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDNAME, arrayObjectInitialIndex[4]);
					objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTNAME, arrayObjectInitialIndex[5]);
					objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDCODE, arrayObjectInitialIndex[6]);
					objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTCODE, arrayObjectInitialIndex[7]);

					if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CASH)) {
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, arrayObjectInitialIndex[0]);
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
					}
					if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE) || arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, arrayObjectInitialIndex[0]);
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
					}
					if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CARD)) {
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, arrayObjectInitialIndex[0]);
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
					}
					if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_ONLINE)) {
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, arrayObjectInitialIndex[0]);
					}
				} else {
					objHashMap = paramList.get(checknew);

					paramList.remove(checknew);

					if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CASH)) {
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, arrayObjectInitialIndex[0]);
					}
					if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE) || arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
						BigDecimal existingAmount = BigDecimal.ZERO;
						if (objHashMap.get(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT) != "") {
							existingAmount = new BigDecimal(objHashMap.get(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT).toString());
						}
						existingAmount = existingAmount.add(new BigDecimal(arrayObjectInitialIndex[0].toString()));
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, existingAmount);
					}
					if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CARD)) {
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, arrayObjectInitialIndex[0]);
					}
					if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_ONLINE)) {
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, arrayObjectInitialIndex[0]);
					}
				}
			}
			if (objHashMap.get(CollectionConstants.BANKREMITTANCE_VOUCHERDATE) != null && objHashMap.get(CollectionConstants.BANKREMITTANCE_SERVICENAME) != null)
				paramList.add(objHashMap);
		}
		return paramList;
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
	public int checkIfMapObjectExist(List<HashMap<String, Object>> paramList, Object[] arrayObjectInitialIndexTemp) {
		int check = -1;
		for (int m = 0; m < paramList.size(); m++) {
			HashMap<String, Object> objHashMapTemp = paramList.get(m);

			if (arrayObjectInitialIndexTemp[1] != null && arrayObjectInitialIndexTemp[2] != null) {
				if (arrayObjectInitialIndexTemp[1].equals(objHashMapTemp.get(CollectionConstants.BANKREMITTANCE_VOUCHERDATE))
						&& arrayObjectInitialIndexTemp[2].equals(objHashMapTemp.get(CollectionConstants.BANKREMITTANCE_SERVICENAME))
						&& arrayObjectInitialIndexTemp[6].equals(objHashMapTemp.get(CollectionConstants.BANKREMITTANCE_FUNDCODE))
						&& arrayObjectInitialIndexTemp[7].equals(objHashMapTemp.get(CollectionConstants.BANKREMITTANCE_DEPARTMENTCODE))) {
					check = m;
					break;
				} else
					continue;
			}

		}
		return check;
	}

	/**
	 * Create Contra Vouchers for String array passed of serviceName,
	 * totalCashAmount, totalChequeAmount, totalCardAmount and totalOnlineAcount
	 * 
	 * @param serviceName
	 * @param totalCashAmount
	 * @param totalChequeAmount
	 * @return List of Contra Vouchers Created
	 */
	public List<CVoucherHeader> createBankRemittance(String[] serviceNameArr, String[] totalCashAmount, String[] totalChequeAmount, String[] totalCardAmount,
			String[] totalOnlineAmount, String[] receiptDateArray, String[] fundCodeArray, String[] departmentCodeArray, Integer accountNumberId, Integer positionUser) {
		List<CVoucherHeader> newContraVoucherList = new ArrayList<CVoucherHeader>();
		SimpleDateFormat dateFomatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Map<String, Object> instrumentDepositeMap = financialsUtil.prepareForUpdateInstrumentDepositSQL();
		String instrumentGlCodeQueryString = "SELECT COA.GLCODE FROM CHARTOFACCOUNTS COA,EGF_INSTRUMENTACCOUNTCODES IAC,EGF_INSTRUMENTTYPE IT "
				+ "WHERE IT.ID=IAC.TYPEID AND IAC.GLCODEID=COA.ID AND IT.TYPE=";
		String receiptInstrumentQueryString = "select DISTINCT (instruments) from org.egov.collection.entity.ReceiptHeader receipt "
				+ "join receipt.receiptInstrument as instruments where ";
		String serviceNameCondition = "receipt.service.serviceName=? ";
		String receiptDateCondition = "and to_char(receipt.createdDate,'yyyy-MM-dd')=? ";
		String instrumentStatusCondition = "and instruments.statusId.id=? ";
		String instrumentTypeCondition = "and instruments.instrumentType.type = ? ";
		String receiptFundCondition = "and receipt.receiptMisc.fund.code = ? ";
		String receiptDepartmentCondition = "and receipt.receiptMisc.department.deptCode = ? ";

		String cashInHandQueryString = instrumentGlCodeQueryString + "'" + CollectionConstants.INSTRUMENTTYPE_CASH + "'";
		String chequeInHandQueryString = instrumentGlCodeQueryString + "'" + CollectionConstants.INSTRUMENTTYPE_CHEQUE + "'";
		String cardPaymentQueryString = instrumentGlCodeQueryString + "'" + CollectionConstants.INSTRUMENTTYPE_CARD + "'";
		String onlinePaymentQueryString = instrumentGlCodeQueryString + "'" + CollectionConstants.INSTRUMENTTYPE_ONLINE + "'";

		Query cashInHand = this.getSession().createSQLQuery(cashInHandQueryString);
		Query chequeInHand = this.getSession().createSQLQuery(chequeInHandQueryString);
		Query cardPaymentAccount = this.getSession().createSQLQuery(cardPaymentQueryString);
		Query onlinePaymentAccount = this.getSession().createSQLQuery(onlinePaymentQueryString);

		String cashInHandGLCode = null, chequeInHandGlcode = null, cardPaymentGlCode = null, onlinePaymentGlCode = null;
		
		String voucherWorkflowMsg = "Voucher Workflow Started";

		if (!cashInHand.list().isEmpty()) {
			cashInHandGLCode = cashInHand.list().get(0).toString();
		}
		if (!chequeInHand.list().isEmpty()) {
			chequeInHandGlcode = chequeInHand.list().get(0).toString();
		}
		if (!cardPaymentAccount.list().isEmpty()) {
			cardPaymentGlCode = cardPaymentAccount.list().get(0).toString();
		}
		if (!onlinePaymentAccount.list().isEmpty()) {
			onlinePaymentGlCode = onlinePaymentAccount.list().get(0).toString();
		}

		EgwStatus status = commonsServiceImpl.getStatusByModuleAndCode(CollectionConstants.MODULE_NAME_INSTRUMENTHEADER, CollectionConstants.INSTRUMENT_NEW_STATUS);

		/**
		 * Get the AppConfig parameter defined for the Remittance voucher type
		 * in case of instrument type Cheque,DD & Card*/
		 

		Boolean voucherTypeForChequeDDCard = false;
		Boolean useReceiptDateAsContraVoucherDate = false;

		if ((collectionsUtil.getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG, CollectionConstants.APPCONFIG_VALUE_REMITTANCEVOUCHERTYPEFORCHEQUEDDCARD)
				.equals(CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE))) {
			voucherTypeForChequeDDCard = true;
		}

		if ((collectionsUtil.getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG, CollectionConstants.APPCONFIG_VALUE_USERECEIPTDATEFORCONTRA)
				.equals(CollectionConstants.YES))) {
			useReceiptDateAsContraVoucherDate = true;
		}

		for (int i = 0; i < serviceNameArr.length; i++) {
			String serviceName = serviceNameArr[i].trim();
			Date voucherDate = new Date();

			if (useReceiptDateAsContraVoucherDate) {
				try {
					voucherDate = dateFomatter.parse(receiptDateArray[i]);
				} catch (ParseException exp) {
					LOGGER.debug("Exception in parsing date  " + receiptDateArray[i] + " - " + exp.getMessage());
				}
			}

			if (serviceName != null && serviceName.length() > 0) {
				String serviceGLCodeQueryString = "select coa.glcode from BANKACCOUNT ba,CHARTOFACCOUNTS coa where " + "ba.GLCODEID=coa.ID and ba.ID=" + accountNumberId;
				ServiceDetails serviceDetails = (ServiceDetails) persistenceService.findByNamedQuery(CollectionConstants.QUERY_SERVICE_BY_NAME, serviceName);
				Bankaccount depositedBankAccount = null;
				Query serviceGLCodeQuery = this.getSession().createSQLQuery(serviceGLCodeQueryString);

				String serviceGlCode = serviceGLCodeQuery.list().get(0).toString();
				List<HashMap<String, Object>> subledgerList = new ArrayList<HashMap<String, Object>>();

				// If Cash Amount is present
				if (totalCashAmount[i].trim() != null && totalCashAmount[i].trim().length() > 0 && cashInHandGLCode != null) {
					StringBuilder cashQueryBuilder = new StringBuilder(receiptInstrumentQueryString);
					cashQueryBuilder.append(serviceNameCondition);
					cashQueryBuilder.append(receiptDateCondition);
					cashQueryBuilder.append(instrumentStatusCondition);
					cashQueryBuilder.append(instrumentTypeCondition);
					cashQueryBuilder.append(receiptFundCondition);
					cashQueryBuilder.append(receiptDepartmentCondition);

					Object arguments[] = new Object[6];

					arguments[0] = serviceName;
					arguments[1] = receiptDateArray[i];
					arguments[2] = status.getId();
					arguments[3] = CollectionConstants.INSTRUMENTTYPE_CASH;
					arguments[4] = fundCodeArray[i];
					arguments[5] = departmentCodeArray[i];

					List<InstrumentHeader> instrumentHeaderListCash = (List<InstrumentHeader>) persistenceService.findAllBy(cashQueryBuilder.toString(), arguments);

					HashMap<String, Object> headerdetails = new HashMap<String, Object>();

					headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
					headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
					headerdetails.put(VoucherConstant.DESCRIPTION, CollectionConstants.FINANCIAL_VOUCHERDESCRIPTION);
					headerdetails.put(VoucherConstant.VOUCHERDATE, voucherDate);
					headerdetails.put(VoucherConstant.FUNDCODE, fundCodeArray[i]);
					headerdetails.put(VoucherConstant.DEPARTMENTCODE, departmentCodeArray[i]);
					headerdetails.put(VoucherConstant.FUNDSOURCECODE, serviceDetails.getFundSource() == null ? null : serviceDetails.getFundSource().getCode());
					headerdetails.put(VoucherConstant.FUNCTIONARYCODE, serviceDetails.getFunctionary() == null ? null : serviceDetails.getFunctionary().getCode());
					headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);

					List<HashMap<String, Object>> accountCodeCashList = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> accountcodedetailsCreditCashHashMap = new HashMap<String, Object>();

					accountcodedetailsCreditCashHashMap.put(VoucherConstant.GLCODE, cashInHandGLCode);
					accountcodedetailsCreditCashHashMap.put(VoucherConstant.FUNCTIONCODE, null);
					accountcodedetailsCreditCashHashMap.put(VoucherConstant.CREDITAMOUNT, totalCashAmount[i]);
					accountcodedetailsCreditCashHashMap.put(VoucherConstant.DEBITAMOUNT, 0);

					accountCodeCashList.add(accountcodedetailsCreditCashHashMap);
					// TODO: Add debit account details
					{
						HashMap<String, Object> accountcodedetailsDebitHashMap = new HashMap<String, Object>();
						accountcodedetailsDebitHashMap.put(VoucherConstant.GLCODE, serviceGlCode);
						accountcodedetailsDebitHashMap.put(VoucherConstant.FUNCTIONCODE, null);
						accountcodedetailsDebitHashMap.put(VoucherConstant.CREDITAMOUNT, 0);
						accountcodedetailsDebitHashMap.put(VoucherConstant.DEBITAMOUNT, totalCashAmount[i]);
						accountCodeCashList.add(accountcodedetailsDebitHashMap);
					}

					CVoucherHeader voucherHeaderCash = new CVoucherHeader();
					try {
						voucherHeaderCash = financialsUtil.createPreApprovalVoucher(headerdetails, accountCodeCashList, subledgerList);
					} catch (Exception e) {
						LOGGER.error("Error in createBankRemittance createPreApprovalVoucher when cash amount>0");
					}
					newContraVoucherList.add(voucherHeaderCash);
					depositedBankAccount = (Bankaccount) persistenceService.find("from Bankaccount where chartofaccounts.glcode=?", serviceGlCode);
					for (InstrumentHeader instrumentHeader : instrumentHeaderListCash) {
						if (voucherHeaderCash.getId() != null && serviceGlCode != null) {
							Map<String, Object> cashMap = constructInstrumentMap(instrumentDepositeMap, depositedBankAccount, instrumentHeader, voucherHeaderCash, voucherDate);
							financialsUtil.updateCashDeposit(voucherHeaderCash.getId(), serviceGlCode, instrumentHeader,cashMap);
							ContraJournalVoucher contraJournalVoucher = (ContraJournalVoucher) persistenceService.findByNamedQuery(
									CollectionConstants.QUERY_GET_CONTRAVOUCHERBYVOUCHERHEADERID, voucherHeaderCash.getId(), instrumentHeader.getId());
							contraJournalVoucher.transition(true).start().withSenderName(contraJournalVoucher.getCreatedBy().getName()).withComments("Voucher Created").withOwner(collectionsUtil.getPositionOfUser(contraJournalVoucher.getCreatedBy()));
							contraJournalVoucher.transition(true).transition().withSenderName(contraJournalVoucher.getCreatedBy().getName()).withComments(voucherWorkflowMsg).withOwner(collectionsUtil.getPositionOfUser(contraJournalVoucher.getCreatedBy()));
						}
					}

				}
				// If Cheque Amount is present
				if (totalChequeAmount[i].trim() != null && totalChequeAmount[i].trim().length() > 0 && chequeInHandGlcode != null) {
					StringBuilder chequeQueryBuilder = new StringBuilder(receiptInstrumentQueryString);
					chequeQueryBuilder.append(serviceNameCondition);
					chequeQueryBuilder.append(receiptDateCondition);
					chequeQueryBuilder.append(instrumentStatusCondition);
					chequeQueryBuilder.append("and instruments.instrumentType.type in ( ?, ?)");
					chequeQueryBuilder.append("and receipt.status.id=(select id from org.egov.commons.EgwStatus where moduletype=? and code=?) ");
					chequeQueryBuilder.append(receiptFundCondition);
					chequeQueryBuilder.append(receiptDepartmentCondition);

					Object arguments[] = new Object[9];

					arguments[0] = serviceName;
					arguments[1] = receiptDateArray[i];
					arguments[2] = status.getId();
					arguments[3] = CollectionConstants.INSTRUMENTTYPE_CHEQUE;
					arguments[4] = CollectionConstants.INSTRUMENTTYPE_DD;
					arguments[5] = CollectionConstants.MODULE_NAME_RECEIPTHEADER;
					arguments[6] = CollectionConstants.RECEIPT_STATUS_CODE_APPROVED;
					arguments[7] = fundCodeArray[i];
					arguments[8] = departmentCodeArray[i];

					List<InstrumentHeader> instrumentHeaderListCheque = (List<InstrumentHeader>) persistenceService.findAllBy(chequeQueryBuilder.toString(), arguments);

					HashMap<String, Object> headerdetails = new HashMap<String, Object>();
					List<HashMap<String, Object>> accountCodeChequeList = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> accountcodedetailsCreditChequeHashMap = new HashMap<String, Object>();

					if (voucherTypeForChequeDDCard) {
						headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERNAME);
						headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE);

					} else {
						headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
						headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
					}
					headerdetails.put(VoucherConstant.DESCRIPTION, CollectionConstants.FINANCIAL_VOUCHERDESCRIPTION);
					headerdetails.put(VoucherConstant.VOUCHERDATE, voucherDate);
					headerdetails.put(VoucherConstant.FUNDCODE, fundCodeArray[i]);
					headerdetails.put(VoucherConstant.DEPARTMENTCODE, departmentCodeArray[i]);
					headerdetails.put(VoucherConstant.FUNDSOURCECODE, serviceDetails.getFundSource() == null ? null : serviceDetails.getFundSource().getCode());
					headerdetails.put(VoucherConstant.FUNCTIONARYCODE, serviceDetails.getFunctionary() == null ? null : serviceDetails.getFunctionary().getCode());
					headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);

					accountcodedetailsCreditChequeHashMap.put(VoucherConstant.GLCODE, chequeInHandGlcode);
					accountcodedetailsCreditChequeHashMap.put(VoucherConstant.FUNCTIONCODE, null);
					accountcodedetailsCreditChequeHashMap.put(VoucherConstant.CREDITAMOUNT, totalChequeAmount[i]);
					accountcodedetailsCreditChequeHashMap.put(VoucherConstant.DEBITAMOUNT, 0);

					accountCodeChequeList.add(accountcodedetailsCreditChequeHashMap);
					// TODO: Add debit account details
					{
						HashMap<String, Object> accountcodedetailsDebitHashMap = new HashMap<String, Object>();
						accountcodedetailsDebitHashMap.put(VoucherConstant.GLCODE, serviceGlCode);
						accountcodedetailsDebitHashMap.put(VoucherConstant.FUNCTIONCODE, null);
						accountcodedetailsDebitHashMap.put(VoucherConstant.CREDITAMOUNT, 0);
						accountcodedetailsDebitHashMap.put(VoucherConstant.DEBITAMOUNT, totalChequeAmount[i]);
						accountCodeChequeList.add(accountcodedetailsDebitHashMap);
					}

					CVoucherHeader voucherHeaderCheque = new CVoucherHeader();
					try {
						voucherHeaderCheque = financialsUtil.createPreApprovalVoucher(headerdetails, accountCodeChequeList, subledgerList);
					} catch (Exception e) {
						LOGGER.error("Error in createBankRemittance createPreApprovalVoucher when cheque amount>0");
					}
					newContraVoucherList.add(voucherHeaderCheque);
					depositedBankAccount = (Bankaccount) persistenceService.find("from Bankaccount where chartofaccounts.glcode=?", serviceGlCode);
					for (InstrumentHeader instrumentHeader : instrumentHeaderListCheque) {
						if (voucherHeaderCheque.getId() != null && serviceGlCode != null) {
							Map<String, Object> chequeMap = constructInstrumentMap(instrumentDepositeMap, depositedBankAccount, instrumentHeader, voucherHeaderCheque, voucherDate);
							if (voucherTypeForChequeDDCard) {
								financialsUtil.updateCheque_DD_Card_Deposit_Receipt(voucherHeaderCheque.getId(), serviceGlCode, instrumentHeader,chequeMap);
							} else {

								financialsUtil.updateCheque_DD_Card_Deposit(voucherHeaderCheque.getId(), serviceGlCode, instrumentHeader,chequeMap);
								ContraJournalVoucher contraJournalVoucher = (ContraJournalVoucher) persistenceService.findByNamedQuery(
										CollectionConstants.QUERY_GET_CONTRAVOUCHERBYVOUCHERHEADERID, voucherHeaderCheque.getId(), instrumentHeader.getId());
								contraJournalVoucher.transition(true).start().withSenderName(contraJournalVoucher.getCreatedBy().getName()).withComments(CollectionConstants.WF_STATE_NEW).withOwner(collectionsUtil.getPositionOfUser(contraJournalVoucher.getCreatedBy()));
								contraJournalVoucher.transition(true).transition().withSenderName(contraJournalVoucher.getCreatedBy().getName()).withComments(voucherWorkflowMsg).withOwner(collectionsUtil.getPositionOfUser(contraJournalVoucher.getCreatedBy()));
							}
						}

					}
				}
				// If card amount is present
				if (totalCardAmount[i].trim() != null && totalCardAmount[i].trim().length() > 0 && cardPaymentGlCode != null) {
					StringBuilder onlineQueryBuilder = new StringBuilder(receiptInstrumentQueryString);
					onlineQueryBuilder.append(serviceNameCondition);
					onlineQueryBuilder.append(receiptDateCondition);
					onlineQueryBuilder.append(instrumentStatusCondition);
					onlineQueryBuilder.append(instrumentTypeCondition);
					onlineQueryBuilder.append(receiptFundCondition);
					onlineQueryBuilder.append(receiptDepartmentCondition);

					Object arguments[] = new Object[6];

					arguments[0] = serviceName;
					arguments[1] = receiptDateArray[i];
					arguments[2] = status.getId();
					arguments[3] = CollectionConstants.INSTRUMENTTYPE_CARD;
					arguments[4] = fundCodeArray[i];
					arguments[5] = departmentCodeArray[i];

					List<InstrumentHeader> instrumentHeaderListOnline = (List<InstrumentHeader>) persistenceService.findAllBy(onlineQueryBuilder.toString(), arguments);

					HashMap<String, Object> headerdetails = new HashMap<String, Object>();

					if (voucherTypeForChequeDDCard) {
						headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERNAME);
						headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE);
					} else {

						headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
						headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
					}
					headerdetails.put(VoucherConstant.DESCRIPTION, CollectionConstants.FINANCIAL_VOUCHERDESCRIPTION);
					headerdetails.put(VoucherConstant.VOUCHERDATE, voucherDate);
					headerdetails.put(VoucherConstant.FUNDCODE, fundCodeArray[i]);
					headerdetails.put(VoucherConstant.DEPARTMENTCODE, departmentCodeArray[i]);
					headerdetails.put(VoucherConstant.FUNDSOURCECODE, serviceDetails.getFundSource() == null ? null : serviceDetails.getFundSource().getCode());
					headerdetails.put(VoucherConstant.FUNCTIONARYCODE, serviceDetails.getFunctionary() == null ? null : serviceDetails.getFunctionary().getCode());
					headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);

					List<HashMap<String, Object>> accountCodeOnlineList = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> accountcodedetailsCreditOnlineHashMap = new HashMap<String, Object>();

					accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.GLCODE, cardPaymentGlCode);
					accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.FUNCTIONCODE, null);
					accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.CREDITAMOUNT, totalCardAmount[i]);
					accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.DEBITAMOUNT, 0);

					accountCodeOnlineList.add(accountcodedetailsCreditOnlineHashMap);
					// TODO: Add debit account details
					{
						HashMap<String, Object> accountcodedetailsDebitHashMap = new HashMap<String, Object>();
						accountcodedetailsDebitHashMap.put(VoucherConstant.GLCODE, serviceGlCode);
						accountcodedetailsDebitHashMap.put(VoucherConstant.FUNCTIONCODE, null);
						accountcodedetailsDebitHashMap.put(VoucherConstant.CREDITAMOUNT, 0);
						accountcodedetailsDebitHashMap.put(VoucherConstant.DEBITAMOUNT, totalCardAmount[i]);
						accountCodeOnlineList.add(accountcodedetailsDebitHashMap);
					}

					CVoucherHeader voucherHeaderCard = new CVoucherHeader();
					try {
						voucherHeaderCard = financialsUtil.createPreApprovalVoucher(headerdetails, accountCodeOnlineList, subledgerList);
					} catch (Exception e) {
						LOGGER.error("Error in createBankRemittance createPreApprovalVoucher when online amount>0");
					}
					newContraVoucherList.add(voucherHeaderCard);
					depositedBankAccount = (Bankaccount) persistenceService.find("from Bankaccount where chartofaccounts.glcode=?", serviceGlCode);
					for (InstrumentHeader instrumentHeader : instrumentHeaderListOnline) {
						if (voucherHeaderCard.getId() != null && serviceGlCode != null) {
							Map<String, Object> cardMap = constructInstrumentMap(instrumentDepositeMap, depositedBankAccount, instrumentHeader, voucherHeaderCard, voucherDate);
							if (voucherTypeForChequeDDCard) {
								financialsUtil.updateCheque_DD_Card_Deposit_Receipt(voucherHeaderCard.getId(), serviceGlCode, instrumentHeader,cardMap);
							} else {
								financialsUtil.updateCheque_DD_Card_Deposit(voucherHeaderCard.getId(), serviceGlCode, instrumentHeader,cardMap);
								ContraJournalVoucher contraJournalVoucher = (ContraJournalVoucher) persistenceService.findByNamedQuery(
										CollectionConstants.QUERY_GET_CONTRAVOUCHERBYVOUCHERHEADERID, voucherHeaderCard.getId(), instrumentHeader.getId());
								contraJournalVoucher.transition(true).start().withSenderName(contraJournalVoucher.getCreatedBy().getName()).withComments(CollectionConstants.WF_STATE_NEW).withOwner(collectionsUtil.getPositionOfUser(contraJournalVoucher.getCreatedBy()));
								contraJournalVoucher.transition(true).transition().withSenderName(contraJournalVoucher.getCreatedBy().getName()).withComments(voucherWorkflowMsg).withOwner(collectionsUtil.getPositionOfUser(contraJournalVoucher.getCreatedBy()));
							}
						}
					}
				}
				// If online amount is present
				if (totalOnlineAmount[i].trim() != null && totalOnlineAmount[i].trim().length() > 0 && onlinePaymentGlCode != null) {
					StringBuilder onlineQueryBuilder = new StringBuilder(receiptInstrumentQueryString);
					onlineQueryBuilder.append(serviceNameCondition);
					onlineQueryBuilder.append(receiptDateCondition);
					onlineQueryBuilder.append(instrumentStatusCondition);
					onlineQueryBuilder.append(instrumentTypeCondition);
					onlineQueryBuilder.append(receiptFundCondition);
					onlineQueryBuilder.append(receiptDepartmentCondition);

					Object arguments[] = new Object[6];

					arguments[0] = serviceName;
					arguments[1] = receiptDateArray[i];
					arguments[2] = status.getId();
					arguments[3] = CollectionConstants.INSTRUMENTTYPE_ONLINE;
					arguments[4] = fundCodeArray[i];
					arguments[5] = departmentCodeArray[i];

					List<InstrumentHeader> instrumentHeaderListOnline = (List<InstrumentHeader>) persistenceService.findAllBy(onlineQueryBuilder.toString(), arguments);

					HashMap<String, Object> headerdetails = new HashMap<String, Object>();

					if (voucherTypeForChequeDDCard) {
						headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERNAME);
						headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE);
					} else {

						headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
						headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
					}
					headerdetails.put(VoucherConstant.DESCRIPTION, CollectionConstants.FINANCIAL_VOUCHERDESCRIPTION);
					headerdetails.put(VoucherConstant.VOUCHERDATE, voucherDate);
					headerdetails.put(VoucherConstant.FUNDCODE, fundCodeArray[i]);
					headerdetails.put(VoucherConstant.DEPARTMENTCODE, departmentCodeArray[i]);
					headerdetails.put(VoucherConstant.FUNDSOURCECODE, serviceDetails.getFundSource() == null ? null : serviceDetails.getFundSource().getCode());
					headerdetails.put(VoucherConstant.FUNCTIONARYCODE, serviceDetails.getFunctionary() == null ? null : serviceDetails.getFunctionary().getCode());
					headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);

					List<HashMap<String, Object>> accountCodeOnlineList = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> accountcodedetailsCreditOnlineHashMap = new HashMap<String, Object>();

					accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.GLCODE, onlinePaymentGlCode);
					accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.FUNCTIONCODE, null);
					accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.CREDITAMOUNT, totalOnlineAmount[i]);
					accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.DEBITAMOUNT, 0);

					accountCodeOnlineList.add(accountcodedetailsCreditOnlineHashMap);
					// TODO: Add debit account details
					{
						HashMap<String, Object> accountcodedetailsDebitHashMap = new HashMap<String, Object>();
						accountcodedetailsDebitHashMap.put(VoucherConstant.GLCODE, serviceGlCode);
						accountcodedetailsDebitHashMap.put(VoucherConstant.FUNCTIONCODE, null);
						accountcodedetailsDebitHashMap.put(VoucherConstant.CREDITAMOUNT, 0);
						accountcodedetailsDebitHashMap.put(VoucherConstant.DEBITAMOUNT, totalOnlineAmount[i]);
						accountCodeOnlineList.add(accountcodedetailsDebitHashMap);
					}

					CVoucherHeader voucherHeaderCard = new CVoucherHeader();
					try {
						voucherHeaderCard = financialsUtil.createPreApprovalVoucher(headerdetails, accountCodeOnlineList, subledgerList);
					} catch (Exception e) {
						LOGGER.error("Error in createBankRemittance createPreApprovalVoucher when online amount>0");
					}
					newContraVoucherList.add(voucherHeaderCard);
					depositedBankAccount = (Bankaccount) persistenceService.find("from Bankaccount where chartofaccounts.glcode=?", serviceGlCode);
					for (InstrumentHeader instrumentHeader : instrumentHeaderListOnline) {
						if (voucherHeaderCard.getId() != null && serviceGlCode != null) {
							Map<String, Object> cardMap = constructInstrumentMap(instrumentDepositeMap, depositedBankAccount, instrumentHeader, voucherHeaderCard, voucherDate);
							if (voucherTypeForChequeDDCard) {
								financialsUtil.updateCheque_DD_Card_Deposit_Receipt(voucherHeaderCard.getId(), serviceGlCode, instrumentHeader,cardMap);
							} else {
								financialsUtil.updateCheque_DD_Card_Deposit(voucherHeaderCard.getId(), serviceGlCode, instrumentHeader,cardMap);
								ContraJournalVoucher contraJournalVoucher = (ContraJournalVoucher) persistenceService.findByNamedQuery(
										CollectionConstants.QUERY_GET_CONTRAVOUCHERBYVOUCHERHEADERID, voucherHeaderCard.getId(), instrumentHeader.getId());
								contraJournalVoucher.transition(true).start().withSenderName(contraJournalVoucher.getCreatedBy().getName()).withComments(CollectionConstants.WF_STATE_NEW).withOwner(collectionsUtil.getPositionOfUser(contraJournalVoucher.getCreatedBy()));
								contraJournalVoucher.transition(true).transition().withSenderName(contraJournalVoucher.getCreatedBy().getName()).withComments(voucherWorkflowMsg).withOwner(collectionsUtil.getPositionOfUser(contraJournalVoucher.getCreatedBy()));
							}
						}
					}

				}
			}
		}
		return newContraVoucherList;
	}

	/**
	 * For Bill Based Receipt, aggregate the amount for same account heads
	 * 
	 * @param receiptDetailSetParam
	 * @return Set of Receipt Detail after Aggregating Amounts
	 */
	public Set<ReceiptDetail> aggregateDuplicateReceiptDetailObject(List<ReceiptDetail> receiptDetailSetParam) {
		List<ReceiptDetail> newReceiptDetailList = new ArrayList<ReceiptDetail>();

		int counter = 0;

		for (ReceiptDetail receiptDetailObj : receiptDetailSetParam) {
			if (counter == 0) {
				newReceiptDetailList.add(receiptDetailObj);
			} else {
				int checknew = checkIfReceiptDetailObjectExist(newReceiptDetailList, receiptDetailObj);
				if (checknew == -1) {
					newReceiptDetailList.add(receiptDetailObj);
				} else {
					ReceiptDetail receiptDetail = new ReceiptDetail();

					ReceiptDetail newReceiptDetailObj = newReceiptDetailList.get(checknew);
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
	 * API to check if the given receipt detail object already exists in the
	 * list passed passed as parameter
	 * 
	 * @param newReceiptDetailSet
	 * @param receiptDetailObj
	 * @return
	 */
	public int checkIfReceiptDetailObjectExist(List<ReceiptDetail> newReceiptDetailSet, ReceiptDetail receiptDetailObj) {
		int check = -1;

		for (int m = 0; m < newReceiptDetailSet.size(); m++) {

			ReceiptDetail receiptDetail = newReceiptDetailSet.get(m);

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
	public void endReceiptWorkFlowOnCancellation(ReceiptHeader receiptHeaderToBeCancelled) {
		// End work-flow for the cancelled receipt
		Position position = collectionsUtil.getPositionOfUser(receiptHeaderToBeCancelled.getCreatedBy());
		if (position != null) {
			receiptHeaderToBeCancelled.transition(true).end().withSenderName(receiptHeaderToBeCancelled.getCreatedBy().getName()).withComments("Receipt Cancelled - Workflow ends")
				.withStateValue(CollectionConstants.WF_STATE_END).withOwner(position).withDateInfo(new Date());
		}
	}
	
	/**
	 * This method persists the given <code>ReceiptPayeeDetails</code> entity. 
	 * 
	 * The receipt number for all of the receipts is generated, 
	 * if not already present.
	 * 
	 * If the receipt is associated with a challan, and the challan number is 
	 * not present, the challan number is generated and set into it.
	 */
	@Override
	@Transactional
	public ReceiptHeader persist(ReceiptHeader entity) {
		for (ReceiptHeader receiptHeader : entity.getReceiptHeaders()) {
			if (receiptHeader.getReceipttype()!=CollectionConstants.RECEIPT_TYPE_CHALLAN && 
					!CollectionConstants.RECEIPT_STATUS_CODE_PENDING.equals(
							receiptHeader.getStatus().getCode()) && receiptHeader.getReceiptnumber()==null){
				setReceiptNumber(receiptHeader);
			}
			
			if(receiptHeader.getChallan() != null){
				Challan challan = receiptHeader.getChallan();
				if (challan.getChallanNumber() == null) {
					setChallanNumber(challan);
				}
			
				receiptHeader.setChallan(challan);
				LOGGER.info("Persisted challan with challan number " + 
						challan.getChallanNumber());
			}
		}
		return super.persist(entity);
	}
	
	/**
	 * This method persists the given <code>ReceiptPayeeDetails</code> entity. If the receipt
	 * number for all of the receipts is generated, if not already present.
	 */
	@Transactional
	public ReceiptHeader persistChallan(ReceiptHeader entity) {
		for (ReceiptHeader receiptHeader : entity.getReceiptHeaders()) {
			Integer validUpto = Integer.valueOf(collectionsUtil.getAppConfigValue(
					CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
					CollectionConstants.APPCONFIG_VALUE_CHALLANVALIDUPTO));
			
			Challan challan = receiptHeader.getChallan();
			
			/*challan.setValidUpto(eisService.getPriorOrAfterWorkingDate(
					challan.getChallanDate(), validUpto, DATE_ORDER.AFTER));*/
		
			if(challan.getCreatedDate()==null)
				challan.setCreatedDate(new DateTime());
			
			if (challan.getChallanNumber() == null) {
				setChallanNumber(challan);
			}
			
			challan.setReceiptHeader(receiptHeader);
			receiptHeader.setChallan(challan);
			
			LOGGER.info("Persisting challan with challan number " + challan.getChallanNumber());
		}
		
		return super.persist(entity);
	}

	/**
	 * This method persists the given set of <code>ReceiptPayeeDetails</code>
	 * instances
	 * 
	 * @param entity
	 *            a set of <code>ReceiptPayeeDetails</code> instances to be
	 *            persisted
	 * 
	 * @return the list of persisted <code>ReceiptPayeeDetails</code> instances
	 */
	@Transactional
	public List<ReceiptHeader> persist(Set<ReceiptHeader> entity) {
		List<ReceiptHeader> saved = new ArrayList<ReceiptHeader>();
		Iterator iterator = entity.iterator();
		while (iterator.hasNext()) {
			ReceiptHeader rpd = (ReceiptHeader) iterator.next();
			saved.add(this.persist(rpd));
		}
		return saved;
	}
	
	/**
	 This method persists the given set of <code>ReceiptPayeeDetails</code> 
	 * instances with receipt number as Pending
	 * 
	 * @param entity
	 *            a set of <code>ReceiptPayeeDetails</code> instances to be
	 *            persisted
	 * 
	 * @return the list of persisted <code>ReceiptPayeeDetails</code> instances
	 * 
	 */
	@Transactional
	public List<ReceiptHeader> persistPendingReceipts(Set<ReceiptHeader> entity) {
		List<ReceiptHeader> saved = new ArrayList<ReceiptHeader>();
		Iterator iterator = entity.iterator();
		while (iterator.hasNext()) {
			ReceiptHeader rpd = (ReceiptHeader) iterator.next();
			//for (ReceiptHeader receiptHeader : rpd.getReceiptHeaders()) {
				//if (receiptHeader.getReceiptnumber() == null) {
				//	receiptHeader.setReceiptnumber(CollectionConstants.RECEIPTNUMBER_PENDING);
				//}
			//}	
			saved.add(super.persist(rpd));
		}
		return saved;
	}
	

	public void setReceiptNumber(ReceiptHeader entity) {
		entity.setReceiptnumber(collectionsNumberGenerator
				.generateReceiptNumber(entity));
	}
	
	private void setChallanNumber(Challan challan) {
		CFinancialYear financialYear = collectionsUtil.
			getFinancialYearforDate(challan.getCreatedDate().toDate());
		challan.setChallanNumber(collectionsNumberGenerator
			.generateChallanNumber(challan, financialYear));
	}

	public void setCollectionsNumberGenerator(
			CollectionsNumberGenerator collectionsNumberGenerator) {
		this.collectionsNumberGenerator = collectionsNumberGenerator;
	}

	private BillingIntegrationService getBillingServiceBean(String serviceCode) {
		return (BillingIntegrationService) collectionsUtil.getBean(serviceCode
						+ CollectionConstants.COLLECTIONS_INTERFACE_SUFFIX);
	}

	/**
	 * This method looks up the bean to communicate with the billing system and
	 * updates the billing system.
	 */
	@Transactional
	public Boolean updateBillingSystem(String serviceCode,
			Set<BillReceiptInfo> billReceipts) {
		BillingIntegrationService billingService = getBillingServiceBean(serviceCode);
		if (billingService == null) {
			return false;
		} else {
			try {
				 billingService.updateReceiptDetails(billReceipts);
				return true;
			} catch (Exception e) {
				String errMsg = "Exception while updating billing system ["
					+ serviceCode + "] with receipt details!";
				LOGGER.error(errMsg, e);
				throw new EGOVRuntimeException(errMsg, e);
			}
		}
	}

	/**
	 * This method is called for voucher reversal in case of intra-day receipt
	 * cancellation.
	 * 
	 */
	@Transactional
	public void createReversalVoucher(ReceiptVoucher receiptVoucher,
			String instrumentType) {
		List<HashMap<String, Object>> reversalVoucherInfoList = new ArrayList();
		HashMap<String, Object> reversalVoucherInfo = new HashMap();
		
		if(receiptVoucher.getVoucherheader()!=null)
		{	
			reversalVoucherInfo.put(CollectionConstants.FINANCIALS_VOUCHERREVERSAL_ORIGINALVOUCHERID, receiptVoucher
					.getVoucherheader().getId());
			reversalVoucherInfo.put(CollectionConstants.FINANCIALS_VOUCHERREVERSAL_DATE, new Date());
			
			if(receiptVoucher.getVoucherheader().getType().equals(CollectionConstants.FINANCIAL_JOURNALVOUCHER_VOUCHERTYPE)){
				
				reversalVoucherInfo.put(CollectionConstants.FINANCIALS_VOUCHERREVERSAL_TYPE,
						CollectionConstants.FINANCIAL_JOURNALVOUCHER_VOUCHERTYPE);
				reversalVoucherInfo.put(CollectionConstants.FINANCIALS_VOUCHERREVERSAL_NAME,
						CollectionConstants.FINANCIAL_JOURNALVOUCHER_VOUCHERNAME);
			}
			else if(receiptVoucher.getVoucherheader().getType().equals(CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE)){
				reversalVoucherInfo.put(CollectionConstants.FINANCIALS_VOUCHERREVERSAL_TYPE,
						CollectionConstants.FINANCIAL_PAYMENTVOUCHER_VOUCHERTYPE);
				reversalVoucherInfo.put(CollectionConstants.FINANCIALS_VOUCHERREVERSAL_NAME,
						CollectionConstants.FINANCIAL_PAYMENTVOUCHER_VOUCHERNAME);
			}
		}	
				
		reversalVoucherInfoList.add(reversalVoucherInfo);

		try {
			financialsUtil.getReversalVoucher(reversalVoucherInfoList);
		} catch (Exception e) {
			LOGGER.error("Receipt Service Exception while creating reversal voucher!",e);
			// TODO: Throw EGovRuntimeException ?
		}
	}

	/**
	 * @ Create instrument voucher list from receiptpayeelist and pass it to
	 * financialsutil
	 * 
	 * @param receiptPayeeDetails
	 * @return void
	 */
	@Transactional
	public void updateInstrument(List<CVoucherHeader> voucherHeaderList,
			List<InstrumentHeader> instrumentHeaderList) {
		List<Map<String, Object>> instrumentVoucherList = new ArrayList();
		if (voucherHeaderList != null && instrumentHeaderList != null) {
			for (CVoucherHeader voucherHeader : voucherHeaderList) {
				for (InstrumentHeader instrumentHeader : instrumentHeaderList) {
					Map<String, Object> iVoucherMap = new HashMap();
					iVoucherMap
							.put(
									CollectionConstants.FINANCIAL_INSTRUMENTSERVICE_INSTRUMENTHEADEROBJECT,
									instrumentHeader);
					iVoucherMap
							.put(
									CollectionConstants.FINANCIAL_INSTRUMENTSERVICE_VOUCHERHEADEROBJECT,
									voucherHeader);
					instrumentVoucherList.add(iVoucherMap);
				}
			}
			financialsUtil.updateInstrument(instrumentVoucherList);
		}
	}
	@Transactional
	public List<InstrumentHeader> createInstrument(
			List<InstrumentHeader> instrumentHeaderList) {
		List<Map<String, Object>> instrumentHeaderMapList = new ArrayList();
		if (instrumentHeaderList != null) {
			for (InstrumentHeader instrumentHeader : instrumentHeaderList) {
				Map<String, Object> instrumentHeaderMap = new HashMap();
				instrumentHeaderMap.put(
						CollectionConstants.MAP_KEY_INSTRSERVICE_INSTRUMENTNUMBER, 
						instrumentHeader.getInstrumentNumber());
				instrumentHeaderMap.put(
						CollectionConstants.MAP_KEY_INSTRSERVICE_INSTRUMENTDATE, 
						instrumentHeader.getInstrumentDate());
				instrumentHeaderMap.put(
						CollectionConstants.MAP_KEY_INSTRSERVICE_INSTRUMENTAMOUNT, 
						instrumentHeader.getInstrumentAmount());
				instrumentHeaderMap.put(
						CollectionConstants.MAP_KEY_INSTRSERVICE_INSTRUMENTTYPE, 
						instrumentHeader.getInstrumentType().getType());
				instrumentHeaderMap.put(
						CollectionConstants.MAP_KEY_INSTRSERVICE_ISPAYCHEQUE, 
						instrumentHeader.getIsPayCheque());
				if(instrumentHeader.getBankId()!=null)
					instrumentHeaderMap.put(
							CollectionConstants.MAP_KEY_INSTRSERVICE_BANKCODE, 
							instrumentHeader.getBankId().getCode());
				instrumentHeaderMap.put(
						CollectionConstants.MAP_KEY_INSTRSERVICE_BANKBRANCHNAME, 
						instrumentHeader.getBankBranchName());
				instrumentHeaderMap.put(
						CollectionConstants.MAP_KEY_INSTRSERVICE_TRANSACTIONNUMBER, 
						instrumentHeader.getTransactionNumber());
				instrumentHeaderMap.put(
						CollectionConstants.MAP_KEY_INSTRSERVICE_TRANSACTIONDATE, 
						instrumentHeader.getTransactionDate());
				if(instrumentHeader.getBankAccountId()!=null){
					instrumentHeaderMap.put(
							CollectionConstants.MAP_KEY_INSTRSERVICE_BANKACCOUNTID, 
							instrumentHeader.getBankAccountId().getId());
				}
				instrumentHeaderMapList.add(instrumentHeaderMap);
				// should add bankaccount for bank : key = Bank account id; value = instrumentHeader.getBankAccount.getId()
			}
		}
		return financialsUtil.createInstrument(instrumentHeaderMapList);
	}
	
	private Map<String, Object> constructInstrumentMap(Map<String, Object> instrumentDepositeMap, Bankaccount bankaccount, InstrumentHeader instrumentHeader,
			CVoucherHeader voucherHeader, Date voucherDate) {
		InstrumentType instrumentType = (InstrumentType) persistenceService.find("select it from InstrumentType it,InstrumentHeader ih where "
				+ "ih.instrumentType=it.id and ih.id=?", instrumentHeader.getId());
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

}
