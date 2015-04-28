/*******************************************************************************
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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.domain.bill;

import static org.egov.demand.interfaces.LatePayPenaltyCalculator.LPPenaltyCalcType.SIMPLE;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_LP_DATE_BREAKUP;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_LP_DATE_CONSTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.DEFAULT_FUNCTIONARY_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.DEFAULT_FUND_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.DEFAULT_FUND_SRC_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_WATER_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_PENALTY_FINES;
import static org.egov.ptis.constants.PropertyTaxConstants.LP_PERCENTAGE_CONSTANT;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.egov.commons.Installment;
import org.egov.demand.dao.DCBDaoFactory;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.dao.EgDemandDao;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.interfaces.LatePayPenaltyCalculator;
import org.egov.demand.model.AbstractBillable;
import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.utils.EgovUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.client.model.PropertyInstTaxBean;
import org.egov.ptis.client.service.Amount;
import org.egov.ptis.client.service.PenaltyCalculationService;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.PropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.service.collection.PropertyTaxCollection;
import org.egov.ptis.utils.PTISCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author satyam
 * 
 */
public class PropertyTaxBillable extends AbstractBillable implements Billable,
		LatePayPenaltyCalculator {

	private BasicProperty basicProperty;
	private Long userId;
	EgBillType egBillType;
	PTISCacheManager ptcm = new PTISCacheManager();
	@Autowired
	@Qualifier(value = "demandDAO")
	private EgDemandDao demandDao;
	@Autowired
	@Qualifier(value = "moduleDAO")
	private ModuleDao moduleDao;
	@Autowired
	@Qualifier(value = "propertyDAO")
	private PropertyDAO propertyDao;
	@Autowired
	@Qualifier(value = "ptDemandDAO")
	private PtDemandDao ptDemandDao;
	@Autowired
	@Qualifier(value = "egBillDAO")
	private EgBillDao egBillDao;
	@Autowired
	@Qualifier(value = "demandGenericDAO")
	private DemandGenericDao demandGenericDao;
	@Autowired
	private UserService userService;

	private Boolean isCallbackForApportion = Boolean.TRUE;
	private static final BigDecimal VALUE_HUNDRED = new BigDecimal(100);
	private LPPenaltyCalcType penaltyCalcType = SIMPLE;
	private PropertyTaxUtil ptUtils = new PropertyTaxUtil();
	private String referenceNumber;
	private EgBillType billType;
	private Boolean levyPenalty;
	private Map<Installment, PropertyInstTaxBean> instTaxBean = new HashMap<Installment, PropertyInstTaxBean>();
	private String collType;
	private String pgType;
	private Map<Installment, EgDemandDetails> installmentWisePenaltyDemandDetail = new TreeMap<Installment, EgDemandDetails>();
	private Boolean isMiscellaneous = Boolean.FALSE;
	private BigDecimal mutationFee;

	@Override
	public Boolean getOverrideAccountHeadsAllowed() {
		Boolean retVal = Boolean.FALSE;
		return retVal;
	}

	@Override
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public BasicProperty getBasicProperty() {
		return basicProperty;
	}

	public void setBasicProperty(BasicProperty basicProperty) {
		this.basicProperty = basicProperty;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.egov.demand.interfaces.Billable#getBillAddres()
	 */
	@Override
	public String getBillAddress() {

		return ptcm.buildAddressByImplemetation(getBasicProperty().getAddress());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.egov.demand.interfaces.Billable#getBillDemand()
	 */
	@Override
	public EgDemand getCurrentDemand() {
		BasicProperty bp = null;
		try {
			bp = getBasicProperty();
		} catch (Exception e) {
			throw new EGOVRuntimeException("Property does not exist" + e);
		}
		return ptDemandDao.getNonHistoryCurrDmdForProperty(bp.getProperty());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.egov.demand.interfaces.Billable#getBillLastDueDate()
	 */
	@Override
	public Date getBillLastDueDate() {
		Date Billlastdate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(Billlastdate);
		cal.get(Calendar.MONTH + 1);
		Billlastdate = cal.getTime();
		return (Billlastdate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.egov.demand.interfaces.Billable#getBillPayee()
	 */
	@Override
	public String getBillPayee() {
		return (ptcm.buildOwnerFullName(getBasicProperty().getProperty().getPropertyOwnerSet()))
				.trim();
	}

	@Override
	public String getBoundaryType() {
		return "Ward";
	}

	@Override
	public Long getBoundaryNum() {
		return getBasicProperty().getBoundary().getId();
	}

	@Override
	public Module getModule() {
		return moduleDao.getModuleByName(PropertyTaxConstants.PTMODULENAME);

	}

	@Override
	public String getCollModesNotAllowed() {
		String modesNotAllowed = "";
		BigDecimal chqBouncepenalty = BigDecimal.ZERO;
		EgDemand currDemand = getCurrentDemand();
		if (currDemand != null && currDemand.getMinAmtPayable() != null
				&& currDemand.getMinAmtPayable().compareTo(BigDecimal.ZERO) > 0) {
			chqBouncepenalty = getCurrentDemand().getMinAmtPayable();
		}
		if (getUserId() != null && !getUserId().equals("")) {
			String loginUser = userService.getUserById(getUserId()).getName();
			if (loginUser.equals(PropertyTaxConstants.CITIZENUSER)) {
				// New Modes for the Client are to be added i.e BlackBerry
				// payment etc.
				modesNotAllowed = "cash,cheque";
			} else if (!loginUser.equals(PropertyTaxConstants.CITIZENUSER)
					&& chqBouncepenalty.compareTo(BigDecimal.ZERO) > 0) {
				modesNotAllowed = "cheque";
			}
		}
		return modesNotAllowed;
	}

	@Override
	public String getDepartmentCode() {
		return "R";

	}

	@Override
	public Date getIssueDate() {
		return new Date();
	}

	@Override
	public Date getLastDate() {
		return getBillLastDueDate();
	}

	@Override
	public String getServiceCode() {
		return "PT";
	}

	@Override
	public BigDecimal getTotalAmount() {
		EgDemand currentDemand = getCurrentDemand();
		List instVsAmt = propertyDao.getDmdCollAmtInstWise(currentDemand);
		BigDecimal balance = BigDecimal.ZERO;
		for (Object object : instVsAmt) {
			Object[] ddObject = (Object[]) object;
			BigDecimal dmdAmt = (BigDecimal) ddObject[1];
			BigDecimal collAmt = BigDecimal.ZERO;
			if (ddObject[2] != null) {
				collAmt = (BigDecimal) ddObject[2];
			}
			balance = balance.add(dmdAmt.subtract(collAmt));
			BigDecimal penaltyAmount = demandGenericDao.getBalanceByDmdMasterCode(currentDemand,
					PropertyTaxConstants.PENALTY_DMD_RSN_CODE, getModule());
			if (penaltyAmount != null && penaltyAmount.compareTo(BigDecimal.ZERO) > 0) {
				balance = balance.add(penaltyAmount);
			}
		}
		return balance;
	}

	@Override
	public String getDescription() {
		return "Property Tax Bill Number: " + getBasicProperty().getUpicNo();
	}

	/**
	 * Method Overridden to get all the Demands (including all the history and
	 * non history) for a basicproperty .
	 * 
	 * @return java.util.List<EgDemand>
	 * 
	 */

	@Override
	public List<EgDemand> getAllDemands() {
		List<EgDemand> demands = null;
		List demandIds = propertyDao.getAllDemands(getBasicProperty());
		if (demandIds != null && !demandIds.isEmpty()) {
			demands = new ArrayList<EgDemand>();
			Iterator iter = demandIds.iterator();
			while (iter.hasNext()) {
				demands.add((EgDemand) demandDao.findById(Long.valueOf(iter.next().toString()),
						false));
			}
		}
		return demands;
	}

	@Override
	public BigDecimal getFunctionaryCode() {
		return new BigDecimal(DEFAULT_FUNCTIONARY_CODE);
	}

	@Override
	public String getFundCode() {
		return DEFAULT_FUND_CODE;
	}

	@Override
	public String getFundSourceCode() {
		return DEFAULT_FUND_SRC_CODE;
	}

	@Override
	public Boolean getPartPaymentAllowed() {
		return true;
	}

	@Override
	public String getDisplayMessage() {
		return "Property Tax Collection";
	}

	@Override
	public Boolean isCallbackForApportion() {
		return isCallbackForApportion;
	}

	@Override
	public void setCallbackForApportion(Boolean b) {
		isCallbackForApportion = b;
	}

	@Override
	public BigDecimal calcPanalty(Date latestCollReceiptDate, Date fromDate, BigDecimal amount) {
		BigDecimal penalty = BigDecimal.ZERO;
		Calendar fromDateCalendar = Calendar.getInstance();
		fromDateCalendar.setTime(fromDate);
		Calendar arrearsPenaltyApplicableDate = Calendar.getInstance();
		arrearsPenaltyApplicableDate.set(Calendar.DAY_OF_MONTH, 1);
		arrearsPenaltyApplicableDate.set(Calendar.MONTH, Calendar.JANUARY);
		arrearsPenaltyApplicableDate.set(Calendar.HOUR_OF_DAY, 00);
		arrearsPenaltyApplicableDate.set(Calendar.MINUTE, 00);
		arrearsPenaltyApplicableDate.set(Calendar.SECOND, 00);

		Calendar latestCollRcptCalendar = Calendar.getInstance();
		if (latestCollReceiptDate != null) {
			latestCollRcptCalendar.setTime(latestCollReceiptDate);
		}

		DateFormat dateFormat = new SimpleDateFormat(PropertyTaxConstants.DATE_FORMAT_DDMMYYY);
		Date arrearlpDate = null;
		Date arrearlpDateBreakup = null;
		Date frmDate = null;
		try {
			arrearlpDate = dateFormat.parse(ARR_LP_DATE_CONSTANT);
			arrearlpDateBreakup = dateFormat.parse(ARR_LP_DATE_BREAKUP);
			frmDate = dateFormat.parse(dateFormat.format(fromDate));
		} catch (ParseException e) {
			throw new EGOVRuntimeException("Exception occured in calcPanalty", e);
		}

		if (getLPPenaltyCalcType().equals(LPPenaltyCalcType.SIMPLE)) {
			int noOfMonths = 0;
			Date penaltyFromDate = null;

			if (latestCollReceiptDate != null && latestCollReceiptDate.after(frmDate)
					&& latestCollReceiptDate.after(arrearlpDateBreakup)) {

				latestCollRcptCalendar.add(Calendar.MONTH, 1);
				arrearsPenaltyApplicableDate.setTime(latestCollRcptCalendar.getTime());
				penaltyFromDate = arrearsPenaltyApplicableDate.getTime();

			} else if (frmDate.after(arrearlpDateBreakup) || frmDate.equals(arrearlpDateBreakup)) {
				arrearsPenaltyApplicableDate.set(Calendar.YEAR,
						(fromDateCalendar.get(Calendar.YEAR) + 1));
				penaltyFromDate = arrearsPenaltyApplicableDate.getTime();
			} else {
				penaltyFromDate = arrearlpDate;
			}

			noOfMonths = PropertyTaxUtil.getMonthsBetweenDates(penaltyFromDate, new Date());

			penalty = amount.multiply(LP_PERCENTAGE_CONSTANT).divide(BigDecimal.valueOf(100))
					.multiply(BigDecimal.valueOf(noOfMonths));

		}
		return EgovUtils.roundOff(penalty);
	}

	public BigDecimal calcCurrPenalty(Date latestCollRcptDate, BigDecimal amount) {
		BigDecimal penalty = BigDecimal.ZERO;
		Calendar today = Calendar.getInstance();

		Calendar currentPenaltyApplicableDate = Calendar.getInstance();
		currentPenaltyApplicableDate.set(Calendar.DAY_OF_MONTH, 1);
		currentPenaltyApplicableDate.set(Calendar.MONTH, Calendar.JANUARY);
		currentPenaltyApplicableDate.set(Calendar.HOUR_OF_DAY, 00);
		currentPenaltyApplicableDate.set(Calendar.MINUTE, 00);
		currentPenaltyApplicableDate.set(Calendar.SECOND, 00);

		Calendar latestCollRcptCalendar = Calendar.getInstance();
		if (latestCollRcptDate != null) {
			latestCollRcptCalendar.setTime(latestCollRcptDate);
		}

		if (today.get(Calendar.MONTH) > 2 && today.get(Calendar.MONTH) <= 11) {
			currentPenaltyApplicableDate.set(Calendar.YEAR, today.get(Calendar.YEAR) + 1);
		} else {
			currentPenaltyApplicableDate.set(Calendar.YEAR, today.get(Calendar.YEAR));
		}

		if (today.getTime().after(currentPenaltyApplicableDate.getTime())
				|| today.getTime().equals(currentPenaltyApplicableDate.getTime())) {
			BigDecimal balanceTax = amount;
			int noOfMonths = 0;
			if (latestCollRcptDate != null && latestCollRcptCalendar.get(Calendar.MONTH) >= 0
					&& latestCollRcptCalendar.get(Calendar.MONTH) < 3
					&& today.get(Calendar.YEAR) == latestCollRcptCalendar.get(Calendar.YEAR)) {
				currentPenaltyApplicableDate.set(Calendar.MONTH,
						latestCollRcptCalendar.get(Calendar.MONTH) + 1);
				noOfMonths = PropertyTaxUtil.getMonthsBetweenDates(
						currentPenaltyApplicableDate.getTime(), new Date());
			} else {
				noOfMonths = PropertyTaxUtil.getMonthsBetweenDates(
						currentPenaltyApplicableDate.getTime(), new Date());
			}
			penalty = balanceTax.multiply(LP_PERCENTAGE_CONSTANT).divide(VALUE_HUNDRED)
					.multiply(new BigDecimal(noOfMonths));
		}

		return EgovUtils.roundOff(penalty);
	}

	@Override
	public BigDecimal calcLPPenaltyForPeriod(Date fromDate, Date toDate, BigDecimal amount) {
		return null;
	}

	@Override
	public LPPenaltyCalcType getLPPenaltyCalcType() {
		return penaltyCalcType;
	}

	@Override
	public BigDecimal getLPPPercentage() {
		return new BigDecimal(ptUtils.getAppConfigValue("LATE_PAYPENALTY_PERC",
				PropertyTaxConstants.PTMODULENAME));
	}

	@Override
	public void setPenaltyCalcType(LPPenaltyCalcType penaltyType) {
		this.penaltyCalcType = penaltyType;
	}

	@Override
	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	@Override
	public EgBillType getBillType() {
		if (billType == null) {
			EgBillDao egBillDao = DCBDaoFactory.getDaoFactory().getEgBillDao();
			if (getUserId() != null && !getUserId().equals("")) {
				String loginUser = userService.getUserById(getUserId()).getName();
				if (loginUser.equals(PropertyTaxConstants.CITIZENUSER)) {
					billType = egBillDao.getBillTypeByCode("ONLINE");
				} else if (!loginUser.equals(PropertyTaxConstants.CITIZENUSER)) {
					billType = egBillDao.getBillTypeByCode("AUTO");
				}
			}
		}
		return billType;
	}

	public void setBillType(EgBillType billType) {
		this.billType = billType;
	}

	@Override
	public String getPropertyId() {
		StringBuilder consumerCode = new StringBuilder();
		consumerCode.append(getBasicProperty().getUpicNo());
		if (getBasicProperty().getPropertyID() != null) {
			consumerCode.append("(Zone:");
			if (getBasicProperty().getPropertyID().getZone() != null) {
				consumerCode.append(getBasicProperty().getPropertyID().getZone().getBoundaryNum());
			}
			consumerCode.append(" Ward:");
			if (getBasicProperty().getPropertyID().getWard() != null) {
				consumerCode.append(getBasicProperty().getPropertyID().getWard().getBoundaryNum())
						.append(")");
			}
		}
		return consumerCode.toString();
	}

	public Boolean getLevyPenalty() {
		return levyPenalty;
	}

	public void setLevyPenalty(Boolean levyPenalty) {
		this.levyPenalty = levyPenalty;
	}

	public Map<Installment, PropertyInstTaxBean> getInstTaxBean() {
		return instTaxBean;
	}

	public void setInstTaxBean(Map<Installment, PropertyInstTaxBean> instTaxBean) {
		this.instTaxBean = instTaxBean;
	}

	public void setCollectionType(String collType) {
		this.collType = collType;
	}

	public String getCollectionType() {
		return collType;
	}

	public void setPaymentGatewayType(String pgType) {
		this.pgType = pgType;
	}

	public String getPaymentGatewayType() {
		return pgType;
	}

	private Map<Installment, EgDemandDetails> getInstallmentWisePenaltyDemandDetails(
			Property property, Installment currentInstallment) {
		Map<Installment, EgDemandDetails> installmentWisePenaltyDemandDetails = new TreeMap<Installment, EgDemandDetails>();

		String query = "select ptd from Ptdemand ptd " + "inner join fetch ptd.egDemandDetails dd "
				+ "inner join fetch dd.egDemandReason dr "
				+ "inner join fetch dr.egDemandReasonMaster drm "
				+ "inner join fetch ptd.egptProperty p " + "inner join fetch p.basicProperty bp "
				+ "where bp.active = true " + "and (p.status = 'A' or p.status = 'I') "
				+ "and p = :property " + "and ptd.egInstallmentMaster = :installment "
				+ "and drm.code = :penaltyReasonCode";

		List list = HibernateUtil.getCurrentSession().createQuery(query)
				.setEntity("property", property).setEntity("installment", currentInstallment)
				.setString("penaltyReasonCode", DEMANDRSN_CODE_PENALTY_FINES).list();

		Ptdemand ptDemand = null;

		if (!list.isEmpty()) {
			ptDemand = (Ptdemand) list.get(0);
			for (EgDemandDetails dmdDet : ptDemand.getEgDemandDetails()) {
				if (dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode()
						.equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES)) {
					installmentWisePenaltyDemandDetails.put(dmdDet.getEgDemandReason()
							.getEgInstallmentMaster(), dmdDet);
				}
			}
		}

		return installmentWisePenaltyDemandDetails;
	}

	public Map<Installment, PropertyInstTaxBean> getCalculatedPenalty() throws ValidationException {
		Map<Installment, BigDecimal> waterTaxForInst = new HashMap<Installment, BigDecimal>();
		Map<Installment, BigDecimal> waterTaxCollForInst = new HashMap<Installment, BigDecimal>();
		Map<Installment, PropertyInstTaxBean> propertyInstPenMap = new HashMap<Installment, PropertyInstTaxBean>();
		DateFormat dateFormat = new SimpleDateFormat(PropertyTaxConstants.DATE_FORMAT_DDMMYYY);
		PropertyTaxCollection propTaxColection = new PropertyTaxCollection();
		Date waterTaxEffectiveDate = null;
		EgDemandDetails penaltyDmdDtls = null;
		Installment installment = null;
		PropertyInstTaxBean propInstTaxBean = null;
		BigDecimal taxAmount = BigDecimal.ZERO;
		BigDecimal collAmtForPenaltyCalc = BigDecimal.ZERO;
		BigDecimal actualCollectionAmount = BigDecimal.ZERO;
		BigDecimal balance = BigDecimal.ZERO;
		BigDecimal lpAmt = BigDecimal.ZERO;
		BigDecimal installmentWaterTax = BigDecimal.ZERO;
		Amount waterTaxPenalty = new Amount(BigDecimal.ZERO);
		BigDecimal waterTaxCollection = BigDecimal.ZERO;

		Date fromDateForWaterTaxPenaltyCalc = null;

		boolean thereIsBalance = false;
		boolean levyNewPenaltyRules = false;
		Property property = null;

		if (getLevyPenalty()) {
			EgDemand currentDemand = getCurrentDemand();
			Installment currentInstall = currentDemand.getEgInstallmentMaster();
			property = getBasicProperty().getProperty();

			Map<String, Map<Installment, BigDecimal>> installmentWiseDemandAndCollection = ptUtils
					.prepareReasonWiseDenandAndCollection(property, currentInstall);

			installmentWisePenaltyDemandDetail = getInstallmentWisePenaltyDemandDetails(property,
					currentInstall);

			Map<Installment, BigDecimal> instWiseDmdMap = installmentWiseDemandAndCollection
					.get("DEMAND");
			Map<Installment, BigDecimal> instWiseAmtCollMap = installmentWiseDemandAndCollection
					.get("COLLECTION");

			Date earliestModificationDate = null;
			BasicProperty basicProperty = ((Ptdemand) currentDemand).getEgptProperty()
					.getBasicProperty();
			boolean isMigrated = basicProperty.getIsMigrated().equals(
					PropertyTaxConstants.STATUS_MIGRATED);

			waterTaxEffectiveDate = PropertyTaxUtil.getWaterTaxEffectiveDateForPenalty();

			String propertyId = getPropertyId();
			Map<String, Date> latestCollRcptDateMap = ptUtils
					.getLatestCollRcptDateForProp(propertyId);

			for (EgDemandDetails dd : currentDemand.getEgDemandDetails()) {
				if (DEMANDRSN_CODE_GENERAL_WATER_TAX.equalsIgnoreCase(dd.getEgDemandReason()
						.getEgDemandReasonMaster().getCode())) {
					waterTaxForInst.put(dd.getEgDemandReason().getEgInstallmentMaster(),
							dd.getAmount());
					waterTaxCollForInst
							.put(dd.getEgDemandReason().getEgInstallmentMaster(),
									(dd.getAmtCollected() == null) ? BigDecimal.ZERO : dd
											.getAmtCollected());
				}
			}

			if (isMigrated) {

				earliestModificationDate = ptUtils.getEarliestModificationDate(basicProperty
						.getUpicNo());

				for (Map.Entry<Installment, BigDecimal> mapEntry : instWiseDmdMap.entrySet()) {

					installment = mapEntry.getKey();
					waterTaxPenalty.setAmount(BigDecimal.ZERO);

					// Applying the old penalty rules for the installments
					// before the reassessed(modified) date
					if (earliestModificationDate == null
							|| (installment.getFromDate().before(earliestModificationDate) && installment
									.getToDate().before(earliestModificationDate))) {

						taxAmount = mapEntry.getValue();
						collAmtForPenaltyCalc = instWiseAmtCollMap.get(mapEntry.getKey());
						actualCollectionAmount = instWiseAmtCollMap.get(mapEntry.getKey());
						balance = taxAmount.subtract(collAmtForPenaltyCalc);

						thereIsBalance = balance.compareTo(BigDecimal.ZERO) == 1;

						/*
						 * penalty will be calculated only when there is +ve tax
						 * balance
						 */
						if (thereIsBalance) {

							if (!installmentWisePenaltyDemandDetail.isEmpty()) {
								penaltyDmdDtls = installmentWisePenaltyDemandDetail.get(mapEntry
										.getKey());
							}

							propInstTaxBean = new PropertyInstTaxBean();
							BigDecimal taxForPenaltyCalc = BigDecimal.ZERO;
							Date latestCollRcptDate = null;
							taxForPenaltyCalc = balance;
							installmentWaterTax = BigDecimal.ZERO;

							if (!waterTaxForInst.isEmpty()) {
								if (installment.getFromDate().before(waterTaxEffectiveDate)
										&& !ptUtils.between(waterTaxEffectiveDate,
												installment.getFromDate(), installment.getToDate())) {

									installmentWaterTax = waterTaxForInst.get(installment);
									if (installmentWaterTax != null
											&& installmentWaterTax.compareTo(BigDecimal.ZERO) > 0) {
										// calculate the penalty on water tax
										// separately from Jan 2012,
										taxForPenaltyCalc = taxForPenaltyCalc
												.subtract(installmentWaterTax);
										Date date = latestCollRcptDateMap.get(mapEntry.getKey()
												.getDescription());

										fromDateForWaterTaxPenaltyCalc = date != null
												&& date.after(waterTaxEffectiveDate) ? date
												: waterTaxEffectiveDate;

										waterTaxCollection = waterTaxCollForInst.get(installment);

										// taking the balance water tax
										if (waterTaxCollection != null
												&& waterTaxCollection.compareTo(BigDecimal.ZERO) > 0) {
											installmentWaterTax = installmentWaterTax
													.subtract(waterTaxCollection);
										}

										waterTaxPenalty.setAmount(new PenaltyCalculationService()
												.calculatePenalty(fromDateForWaterTaxPenaltyCalc,
														installmentWaterTax));
										thereIsBalance = taxForPenaltyCalc
												.compareTo(BigDecimal.ZERO) == 1;
									}
								}
							}

							BigDecimal penAmt = BigDecimal.ZERO;
							lpAmt = BigDecimal.ZERO;

							if (penaltyDmdDtls != null) {
								if (thereIsBalance) { // calculate the penalty
														// if
														// there is a tax
														// balance

									latestCollRcptDate = latestCollRcptDateMap.get(mapEntry
											.getKey().getDescription());
									Calendar todayCalendar = Calendar.getInstance();
									todayCalendar.setTime(new Date());
									int currMonth = todayCalendar.get(Calendar.MONTH);
									int currYear = todayCalendar.get(Calendar.YEAR);
									Calendar latestCollRcptCalendar = Calendar.getInstance();

									BigDecimal balancePenalty = BigDecimal.ZERO;

									if (penaltyDmdDtls.getAmtCollected().compareTo(BigDecimal.ZERO) > 0) {
										balancePenalty = penaltyDmdDtls.getAmount().subtract(
												penaltyDmdDtls.getAmtCollected());
									}

									if (latestCollRcptDate != null) {
										latestCollRcptCalendar.setTime(latestCollRcptDate);
									}

									if (!installment.equals(currentInstall)) {

										if (latestCollRcptDate != null) {

											if (latestCollRcptCalendar.get(Calendar.MONTH) == currMonth
													&& latestCollRcptCalendar.get(Calendar.YEAR) == currYear) {

												penAmt = balancePenalty;

											} else {

												penAmt = calcPanalty(latestCollRcptDate,
														installment.getFromDate(),
														taxForPenaltyCalc);

												penAmt = penAmt.add(balancePenalty);
											}

											lpAmt = penAmt;

										} else if (collAmtForPenaltyCalc.equals(BigDecimal.ZERO)) {
											lpAmt = calcPanalty(null, installment.getFromDate(),
													taxForPenaltyCalc);
										}
									} else {
										if (latestCollRcptDate != null
												&& latestCollRcptCalendar.get(Calendar.MONTH) == currMonth) {

											penAmt = balancePenalty;

										} else {
											if (collAmtForPenaltyCalc.equals(BigDecimal.ZERO)) {
												penAmt = calcCurrPenalty(null, taxForPenaltyCalc);
											} else {
												penAmt = calcCurrPenalty(latestCollRcptDate,
														taxForPenaltyCalc);
												penAmt = penAmt.add(balancePenalty);
											}
										}

										lpAmt = penAmt;
									}
								} else {
									lpAmt = penaltyDmdDtls.getAmount();
								}
							} else {
								if (!installment.equals(currentInstall)) {
									lpAmt = calcPanalty(latestCollRcptDate,
											installment.getFromDate(), taxForPenaltyCalc);
								} else {
									lpAmt = calcCurrPenalty(latestCollRcptDate, taxForPenaltyCalc);
								}
							}

							if (waterTaxPenalty.isGreaterThanZero()) {
								lpAmt = lpAmt.add(waterTaxPenalty.getAmount());
							}

							propInstTaxBean = createInstallmentTaxPenlatyBean(penaltyDmdDtls,
									installment, taxAmount, actualCollectionAmount, lpAmt,
									currentInstall);

							propertyInstPenMap.put(mapEntry.getKey(), propInstTaxBean);
						}
					} else {
						levyNewPenaltyRules = true;
						break;
					}
				}

				if (levyNewPenaltyRules) {
					calculateAndPreparePenaltyBeans(waterTaxForInst, waterTaxCollForInst,
							propertyInstPenMap, instWiseDmdMap, instWiseAmtCollMap, currentInstall,
							basicProperty, latestCollRcptDateMap);
				}
			} else {

				calculateAndPreparePenaltyBeans(waterTaxForInst, waterTaxCollForInst,
						propertyInstPenMap, instWiseDmdMap, instWiseAmtCollMap, currentInstall,
						basicProperty, latestCollRcptDateMap);
			}
		}
		return propertyInstPenMap;
	}

	/**
	 * @param waterTaxForInst
	 * @param waterTaxCollForInst
	 * @param propertyInstPenMap
	 * @param instWiseDmdMap
	 * @param instWiseAmtCollMap
	 * @param currentInstall
	 * @param basicProperty
	 */
	private void calculateAndPreparePenaltyBeans(Map<Installment, BigDecimal> waterTaxForInst,
			Map<Installment, BigDecimal> waterTaxCollForInst,
			Map<Installment, PropertyInstTaxBean> propertyInstPenMap,
			Map<Installment, BigDecimal> instWiseDmdMap,
			Map<Installment, BigDecimal> instWiseAmtCollMap, Installment currentInstall,
			BasicProperty basicProperty, Map<String, Date> installmentAndLatestCollDate)
			throws ValidationException {

		PropertyInstTaxBean propInstTaxBean;
		PenaltyCalculationService penaltyCalculationService = new PenaltyCalculationService(
				basicProperty, instWiseDmdMap, instWiseAmtCollMap, installmentAndLatestCollDate,
				installmentWisePenaltyDemandDetail);

		Map<Installment, BigDecimal> installmentWisePenalty = penaltyCalculationService
				.getInstallmentWisePenalty();

		Installment inst = null;
		EgDemandDetails penaltyDemandDetails = null;

		if (installmentWisePenalty != null && !installmentWisePenalty.isEmpty()) {
			for (Map.Entry<Installment, BigDecimal> mapEntry : installmentWisePenalty.entrySet()) {

				inst = mapEntry.getKey();

				if (!installmentWisePenaltyDemandDetail.isEmpty()) {
					penaltyDemandDetails = installmentWisePenaltyDemandDetail.get(inst);
				}

				propInstTaxBean = createInstallmentTaxPenlatyBean(penaltyDemandDetails, inst,
						instWiseDmdMap.get(inst), instWiseAmtCollMap.get(inst),
						mapEntry.getValue(), currentInstall);

				propertyInstPenMap.put(inst, propInstTaxBean);
			}
		}
	}

	/**
	 * @param propTaxColection
	 * @param penaltyDmdDtls
	 * @param installment
	 * @param propInstTaxBean
	 * @param taxAmount
	 * @param actualCollectionAmount
	 * @param lpAmt
	 * @param currentInstall
	 */
	private PropertyInstTaxBean createInstallmentTaxPenlatyBean(EgDemandDetails penaltyDmdDtls,
			Installment installment, BigDecimal taxAmount, BigDecimal actualCollectionAmount,
			BigDecimal lpAmt, Installment currentInstall) {

		PropertyInstTaxBean propInstTaxBean = new PropertyInstTaxBean();

		propInstTaxBean.setInstallment(installment);
		propInstTaxBean.setInstallmentId(installment.getId());
		propInstTaxBean.setInstallmentStr(installment.getDescription());
		propInstTaxBean.setInstTaxAmt(taxAmount);
		propInstTaxBean.setInstPenaltyAmt(lpAmt.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO
				: lpAmt);
		propInstTaxBean.setInstCollAmt(actualCollectionAmount);
		propInstTaxBean.setInstBalanceAmt(taxAmount.subtract(actualCollectionAmount));

		if (penaltyDmdDtls != null) {
			propInstTaxBean.setInstPenaltyCollAmt(penaltyDmdDtls.getAmtCollected());
		}

		if (!installment.equals(currentInstall)) {
			propInstTaxBean.setInstRebateAmt(new BigDecimal("0"));
		} else {
			propInstTaxBean.setInstRebateAmt(new PropertyTaxCollection().calcEarlyPayRebate(
					taxAmount,
					getTaxforReason(getBasicProperty().getProperty(),
							PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_TAX),
					actualCollectionAmount));
		}

		return propInstTaxBean;
	}

	/**
	 * Returns true if the penalty is partially collected else false
	 *
	 * @param penaltyDmdDtls
	 * @return true if partial collection else false
	 */
	private boolean isPenaltyPartiallyPaid(EgDemandDetails penaltyDmdDtls) {
		return penaltyDmdDtls.getAmount().compareTo(penaltyDmdDtls.getAmtCollected()) > 0;
	}

	private BigDecimal getTaxforReason(Property property, String demandReason) {
		BigDecimal reasonTax = BigDecimal.ZERO;
		Installment inst = PropertyTaxUtil.getCurrentInstallment();
		PtDemandDao ptDemandDao = PropertyDAOFactory.getDAOFactory().getPtDemandDao();
		EgDemand egDemand = ptDemandDao.getNonHistoryCurrDmdForProperty(property);
		for (EgDemandDetails dmdDet : egDemand.getEgDemandDetails()) {
			if (dmdDet.getEgDemandReason().getEgInstallmentMaster().equals(inst)
					&& dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode()
							.equals(demandReason)) {
				reasonTax = dmdDet.getAmount();
				break;
			}
		}
		return reasonTax;
	}

	/**
	 * Gives the Rebate applicable for advance payment
	 * 
	 * @param currentTax
	 * @return applicable rebate amount
	 */
	public BigDecimal calculateAdvanceRebate(BigDecimal currentTax) {
		return currentTax.multiply(PropertyTaxConstants.ADVANCE_REBATE_PERCENTAGE).divide(
				VALUE_HUNDRED);
	}

	public Boolean isMiscellaneous() {
		return isMiscellaneous;
	}

	public void setIsMiscellaneous(Boolean isMiscellaneous) {
		this.isMiscellaneous = isMiscellaneous;
	}

	public BigDecimal getMutationFee() {
		return mutationFee;
	}

	public void setMutationFee(BigDecimal mutationFee) {
		this.mutationFee = mutationFee;
	}
}
