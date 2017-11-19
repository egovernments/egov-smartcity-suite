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
/**
 * 
 */
package org.egov.ptis.actions.recovery;

import org.apache.log4j.Logger;
import org.egov.commons.EgwStatus;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.actions.common.PropertyTaxBaseAction;
import org.egov.ptis.actions.view.ViewPropertyAction;
import org.egov.ptis.client.util.FinancialUtil;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyStatus;
import org.egov.ptis.domain.entity.recovery.Recovery;
import org.egov.ptis.domain.entity.recovery.WarrantFee;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class BaseRecoveryAction extends PropertyTaxBaseAction {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(BaseRecoveryAction.class);
	public static final SimpleDateFormat DDMMYYYYFORMATS = new SimpleDateFormat("dd/MM/yyyy", new Locale("en", "IN"));
	protected ViewPropertyAction viewPropertyAction = new ViewPropertyAction();
	protected Map<String, Object> viewMap;
	protected String ownerName;
	protected String propertyAddress;
	protected EisCommonService eisCommonService;
	protected ReportService reportService;
	protected String reportId;
	protected ModuleService moduleService;
	protected PropertyTaxNumberGenerator propertyTaxNumberGenerator;
	protected PersistenceService<BasicProperty, Long> basicPropertyService;
	protected FinancialUtil financialUtil;
	protected NoticeService noticeService;
	private InstallmentDao instalDao;
	private ModuleService moduleDao;
	@Autowired
	private EgBillDao egBillDAO;
	@Autowired
	private PropertyTaxCommonUtils propertyTaxCommonUtils;

	@Autowired
	private ReportViewerUtil reportViewerUtil;

	@Override
	public StateAware getModel() {
		return null;
	}

	protected BasicProperty getPropertyView(String propertyId) {
		LOGGER.debug("BaseRecoveryAction | getPropertyView | Start");
		viewPropertyAction.setPropertyId(propertyId);
		viewPropertyAction.setPropertyTaxUtil(new PropertyTaxUtil());
		viewPropertyAction.setSession(this.getSession());
		viewPropertyAction.setPersistenceService(persistenceService);
		viewPropertyAction.viewForm();
		viewMap = viewPropertyAction.getViewMap();
		LOGGER.debug("BaseRecoveryAction | getPropertyView | End");
		return viewPropertyAction.getBasicProperty();
	}

	protected void validateStartRecovery(Recovery recovery) {
		if (getCurrentDate().after(recovery.getIntimationNotice().getPaymentDueDate())) {

			throw new ValidationException(Arrays.asList(new ValidationError("paymentDueDate",
					getText("paymentDueDate.greaterThan.currentDate"))));
		}

	}

	protected void validateWarrantNotice(Recovery recovery) {

		if (recovery.getIntimationNotice().getPaymentDueDate()
				.after(recovery.getWarrantNotice().getWarrantReturnByDate())) {

			throw new ValidationException(Arrays.asList(new ValidationError("warrantReturnByDate",
					getText("warrantReturnByDate.greaterThan.paymentDueDate"))));
		}

	}

	protected void validateCeaseNotice(Recovery recovery) {

		if (recovery.getWarrantNotice().getWarrantReturnByDate().after(recovery.getCeaseNotice().getExecutionDate())) {

			throw new ValidationException(Arrays.asList(new ValidationError("executionDate",
					getText("executionDate.greaterThan.warrantReturnByDate"))));
		}

	}

	protected String getNextState(String currectState) {

		if (currectState.equalsIgnoreCase(PropertyTaxConstants.RECOVERY_NOTICE155CREATED)) {
			return "Generate Notice 155";
		} else if (currectState.equalsIgnoreCase(PropertyTaxConstants.RECOVERY_NOTICE155GENERATED)) {
			return "Create Warrant";
		} else if (currectState.equalsIgnoreCase(PropertyTaxConstants.RECOVERY_WARRANTPREPARED)) {
			return "Generate Warrant Application Pending";
		} else if (currectState.equalsIgnoreCase(PropertyTaxConstants.RECOVERY_WARRANTAPPROVED)) {
			return "Create Notice 156";
		} else if (currectState.equalsIgnoreCase(PropertyTaxConstants.RECOVERY_WARRANTNOTICECREATED)) {
			return "Issue Notice 156";
		} else if (currectState.equalsIgnoreCase(PropertyTaxConstants.RECOVERY_WARRANTNOTICEISSUED)) {
			return "Create Notice 159";
		} else if (currectState.equalsIgnoreCase(PropertyTaxConstants.RECOVERY_CEASENOTICECREATED)) {
			return "Issue Notice 159";
		} else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	protected EgBill getBil(String consumerId) {

		Module module = moduleDao.getModuleByName(PropertyTaxConstants.PTMODULENAME);
		Installment finYear = instalDao.getInsatllmentByModuleForGivenDate(module, new Date());
		// get the latest bill
		StringBuffer query = new StringBuffer(100);
		query.append(
				"select id from eg_bill where issue_date = ( select issue_date from (select issue_date from eg_bill where")
				.append(" consumer_id='" + consumerId + "' and issue_date >=to_date('"
						+ DDMMYYYYFORMATS.format(finYear.getFromDate()) + "','dd/MM/yyyy')")
				.append("  order by issue_date desc)  group by rownum,issue_date having rownum <=1)");
		LOGGER.debug("BaseRecoveryAction | getBilAmount | query >> " + query.toString());
		List list = persistenceService.getSession().createSQLQuery(query.toString()).list();
		if (list != null && list.size() > 0) {
			EgBill bill = (EgBill) persistenceService.find(" from EgBill where id=" + list.get(0));
			return bill;
		} else {
			return null;
		}
	}

	public Long getDemandReason(String demandMaster) {
		StringBuffer query = new StringBuffer();
		query.append(" from EgDemandReason where egDemandReasonMaster.reasonMaster='" + demandMaster + "'").append(
				" and egInstallmentMaster.id='" + propertyTaxCommonUtils.getCurrentInstallment().getId() + "'");
		EgDemandReason demandReason = (EgDemandReason) persistenceService.find(query.toString());

		return demandReason.getId();
	}

	protected Map<String, Object> getNotice156Param(Recovery recovery) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("currentDate", DateUtils.getFormattedDate(new Date(), "dd/MM/yyyy"));
		params.put("ownerFatherName", "-");
		params.put("taxName", "Property Tax");
		BigDecimal totalWarrantFees = BigDecimal.ZERO;
		for (WarrantFee warrantFee : recovery.getWarrant().getWarrantFees()) {
			totalWarrantFees = totalWarrantFees.add(warrantFee.getAmount());
		}
		params.put("totalWarrantFees", totalWarrantFees.setScale(2).toString());
		params.put("warrantReturnByDate",
				DateUtils.convertToWords(recovery.getWarrantNotice().getWarrantReturnByDate()));
		return params;
	}

	protected Map<String, Object> getNotice159Param(Recovery recovery) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fatherName", "-");
		params.put("noticeDate", DDMMYYYYFORMATS.format(new Date()));
		BigDecimal totalWarrantFees = BigDecimal.ZERO;
		for (WarrantFee warrantFee : recovery.getWarrant().getWarrantFees()) {
			totalWarrantFees = totalWarrantFees.add(warrantFee.getAmount());
		}
		params.put("totalWarrantFees", totalWarrantFees.setScale(2).toString());

		return params;
	}

	@SuppressWarnings("unchecked")
	protected void updateDemand(Recovery recovery) {
		StringBuffer consumerId = new StringBuffer();
		consumerId.append(recovery.getBasicProperty().getUpicNo()).append("(Zone:")
				.append(recovery.getBasicProperty().getPropertyID().getZone().getBoundaryNum()).append(" Ward:")
				.append(recovery.getBasicProperty().getPropertyID().getWard().getBoundaryNum()).append(")");

		EgBill currentBill = getBil(consumerId.toString());
		EgDemand currentDemand = currentBill.getEgDemand();
		Set<EgDemandDetails> demandDetails = currentDemand.getEgDemandDetails();
		EgDemandDetails demandDetail = new EgDemandDetails();
		BigDecimal totalDemandAmt = currentDemand.getBaseDemand();
		for (WarrantFee warrantFee : recovery.getWarrant().getWarrantFees()) {
			totalDemandAmt = totalDemandAmt.add(warrantFee.getAmount());
			demandDetail = new EgDemandDetails();
			demandDetail.setAmount(warrantFee.getAmount());
			demandDetail.setAmtCollected(BigDecimal.ZERO);
			demandDetail.setAmtRebate(BigDecimal.ZERO);
			demandDetail.setEgDemandReason(warrantFee.getDemandReason());
			demandDetail.setCreateDate(new Date());
			demandDetail.setModifiedDate(new Date());
			demandDetails.add(demandDetail);
		}
		currentDemand.setBaseDemand(totalDemandAmt);
		currentDemand.setEgDemandDetails(demandDetails);
		currentBill.setTotalAmount(totalDemandAmt);
		currentBill.setEgDemand(currentDemand);
		egBillDAO.update(currentBill);

		Map<Installment, Map<String, BigDecimal>> amounts = new HashMap<Installment, Map<String, BigDecimal>>();
		Map<String, BigDecimal> voucherDemandMap = new HashMap<String, BigDecimal>(); // Map
																						// for
																						// create
																						// voucher
		voucherDemandMap.put(PropertyTaxConstants.DEMANDRSN_CODE_RECOVERY_FEE, totalDemandAmt);
		amounts.put(currentDemand.getEgInstallmentMaster(), voucherDemandMap);

		financialUtil.createVoucher(recovery.getBasicProperty().getUpicNo(), amounts, "Recovery Fees");

	}

	protected EgwStatus getEgwStatusForModuleAndCode(String moduleName, String statusCode) {

		EgwStatus status = (EgwStatus) persistenceService.findByNamedQuery(
				PropertyTaxConstants.QUERY_STATUS_BY_MODULE_AND_CODE, moduleName, statusCode);
		return status;
	}

	protected PropertyStatus getPropStatusByStatusCode(String statusCode) {

		PropertyStatus status = (PropertyStatus) persistenceService.findByNamedQuery(
				PropertyTaxConstants.QUERY_PROP_STATUS_BY_STATUSCODE, statusCode);
		return status;
	}

	/**
	 * 
	 * @param state
	 *            - The state of the work flow object
	 * @return TRUE - if logged in user is the authorised user to view the inbox
	 *         item. FALSE - if logged in user is not the authenticated to view
	 *         the inbox item.
	 */
	public Boolean authenticateInboxItemRqst(State state) {
		if (null == state || null == state.getOwnerUser())
			return Boolean.FALSE;
		// FIX ME
		// User authorisedUser =
		// (User)eisCommonService.getUserforPosition(state.getOwnerUser());
		// User loggedInUser =(User) new
		// UserDAO().getUserByID(Integer.valueOf(ApplicationThreadLocals.getUserId()));
		User authorisedUser = null;
		User loggedInUser = null;
		return authorisedUser.equals(loggedInUser);

	}

	protected String addingReportToSession(ReportOutput reportOutput) {
		return reportViewerUtil.addReportToTempCache(reportOutput);
	}

	public Map<String, Object> getViewMap() {
		return viewMap;
	}

	public void setViewPropertyAction(ViewPropertyAction viewPropertyAction) {
		this.viewPropertyAction = viewPropertyAction;
	}

	public void setViewMap(Map<String, Object> viewMap) {
		this.viewMap = viewMap;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public String getReportId() {
		return reportId;
	}

	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}

	public void setPropertyTaxNumberGenerator(PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
		this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
	}

	public void setbasicPropertyService(PersistenceService<BasicProperty, Long> basicPropertyService) {
		this.basicPropertyService = basicPropertyService;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public String getPropertyAddress() {
		return propertyAddress;
	}

	public void setOwnerName(Property property) {
		this.ownerName = property.getBasicProperty().getFullOwnerName();
	}

	public void setPropertyAddress(Address address) {
		this.propertyAddress = address.toString();
	}

	public Date getCurrentDate() {

		return new Date();
	}

	public void setFinancialUtil(FinancialUtil financialUtil) {
		this.financialUtil = financialUtil;
	}

	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}

}
