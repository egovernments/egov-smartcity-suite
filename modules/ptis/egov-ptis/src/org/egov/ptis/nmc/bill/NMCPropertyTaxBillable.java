package org.egov.ptis.nmc.bill;

import static org.egov.demand.interfaces.LatePayPenaltyCalculator.LPPenaltyCalcType.SIMPLE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.ARR_LP_DATE_BREAKUP;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.ARR_LP_DATE_CONSTANT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEFAULT_FUNCTIONARY_CODE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEFAULT_FUND_CODE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEFAULT_FUND_SRC_CODE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_PENALTY_FINES;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEPT_CODE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.LP_PERCENTAGE_CONSTANT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PENALTY_WATERTAX_EFFECTIVE_DATE;

import groovy.mock.interceptor.Demand;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Installment;
import org.egov.demand.dao.DCBHibernateDaoFactory;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infstr.utils.EgovUtils;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.model.PropertyInstTaxBean;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.ptis.service.collection.PropertyTaxCollection;

public class NMCPropertyTaxBillable extends PropertyTaxBillable {

	private PropertyTaxUtil ptUtils = new PropertyTaxUtil();
	private static final Logger LOGGER = Logger.getLogger(NMCPropertyTaxBillable.class);

	/**
	 * Boolean value to indicate if apportioning has to be handled by
	 * Collections or the billing system (PTIS). False indicates that
	 * apportioning will be taken care of by Collections
	 */
	private Boolean isCallbackForApportion = Boolean.TRUE;
	private LPPenaltyCalcType penaltyCalcType = SIMPLE;
	private String referenceNumber;
	private EgBillType billType;
	private Boolean levyPenalty;
	private Map<Installment, PropertyInstTaxBean> instTaxBean = new HashMap<Installment, PropertyInstTaxBean>();
	
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
	public String getDepartmentCode() {
		return DEPT_CODE_TAX;
	}

	@Override
	public Boolean getOverrideAccountHeadsAllowed() {
		return false;
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
	public BigDecimal calcPanalty(Date fromDate, BigDecimal amount) {
		LOGGER.info("Enter into calcPanalty - fromDate: " + fromDate + ", amount: " + amount);
		BigDecimal penalty = BigDecimal.ZERO;
		Calendar fromDateCalendar = Calendar.getInstance();
		fromDateCalendar.setTime(fromDate);
		Calendar arrearsPenaltyApplicableDate = Calendar.getInstance();
		arrearsPenaltyApplicableDate.set(Calendar.DAY_OF_MONTH, 1);
		arrearsPenaltyApplicableDate.set(Calendar.MONTH, Calendar.JANUARY);
		arrearsPenaltyApplicableDate.set(Calendar.HOUR_OF_DAY, 00);
		arrearsPenaltyApplicableDate.set(Calendar.MINUTE, 00);
		arrearsPenaltyApplicableDate.set(Calendar.SECOND, 00);

		DateFormat dateFormat = new SimpleDateFormat(NMCPTISConstants.DATE_FORMAT_DDMMYYY);
		Date arrearlpDate = null;
		Date arrearlpDateBreakup = null;
		Date frmDate = null;
		try {
			arrearlpDate = dateFormat.parse(ARR_LP_DATE_CONSTANT);
			arrearlpDateBreakup = dateFormat.parse(ARR_LP_DATE_BREAKUP);
			frmDate = dateFormat.parse(dateFormat.format(fromDate));
		} catch (ParseException e) {
			LOGGER.error("Error while parsing Arrear Late Payment penalty dates", e);
		}

		if (getLPPenaltyCalcType().equals(LPPenaltyCalcType.SIMPLE)) {
			int noOfMonths = 0;

			if (frmDate.after(arrearlpDateBreakup) || frmDate.equals(arrearlpDateBreakup)) {
				arrearsPenaltyApplicableDate.set(Calendar.YEAR, (fromDateCalendar.get(Calendar.YEAR) + 1));
				LOGGER.info("calcPanalty - arrearsPenaltyApplicableDate: " + arrearsPenaltyApplicableDate.getTime());
				noOfMonths = PropertyTaxUtil.getMonthsBetweenDates(arrearsPenaltyApplicableDate.getTime(), new Date());
			} else {
				noOfMonths = PropertyTaxUtil.getMonthsBetweenDates(arrearlpDate, new Date());
			}

			penalty = amount.multiply(LP_PERCENTAGE_CONSTANT).divide(BigDecimal.valueOf(100))
					.multiply(BigDecimal.valueOf(noOfMonths));
			LOGGER.info("calcPanalty - noOfMonths: " + noOfMonths + ", penalty: " + penalty);
		}
		return EgovUtils.roundOff(penalty);
	}

	public BigDecimal calcCurrPenalty(BigDecimal amount, BigDecimal currCollection, BigDecimal penDetailAmount) {
		BigDecimal penalty = BigDecimal.ZERO;
		Calendar today = Calendar.getInstance();

		Calendar currentPenaltyApplicableDate = Calendar.getInstance();
		currentPenaltyApplicableDate.set(Calendar.DAY_OF_MONTH, 1);
		currentPenaltyApplicableDate.set(Calendar.MONTH, Calendar.JANUARY);
		currentPenaltyApplicableDate.set(Calendar.HOUR_OF_DAY, 00);
		currentPenaltyApplicableDate.set(Calendar.MINUTE, 00);
		currentPenaltyApplicableDate.set(Calendar.SECOND, 00);

		if (today.get(Calendar.MONTH) > 2 && today.get(Calendar.MONTH) <= 11) {
			currentPenaltyApplicableDate.set(Calendar.YEAR, today.get(Calendar.YEAR) + 1);
		} else {
			currentPenaltyApplicableDate.set(Calendar.YEAR, today.get(Calendar.YEAR));
		}

		if (today.getTime().after(currentPenaltyApplicableDate.getTime())
				|| today.getTime().equals(currentPenaltyApplicableDate.getTime())) {
			BigDecimal balanceTax = amount.subtract(currCollection);
			int noOfMonths = PropertyTaxUtil.getMonthsBetweenDates(currentPenaltyApplicableDate.getTime(), new Date());
			penalty = balanceTax.multiply(LP_PERCENTAGE_CONSTANT).divide(new BigDecimal(100))
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
		return new BigDecimal(ptUtils.getAppConfigValue("LATE_PAYPENALTY_PERC", PropertyTaxConstants.PTMODULENAME));
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
			EgBillDao egBillDao = DCBHibernateDaoFactory.getDaoFactory().getEgBillDao();
			UserDAO userDao = new UserDAO();
			if (getUserId() != null && !getUserId().equals("")) {
				String loginUser = userDao.getUserByID(Integer.valueOf(getUserId().toString())).getUserName();
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
				consumerCode.append(getBasicProperty().getPropertyID().getWard().getBoundaryNum()).append(")");
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

	public Map<Installment, PropertyInstTaxBean> getCalculatedPenalty() {
		Map<Installment, BigDecimal> waterTaxForInstallment = new HashMap<Installment, BigDecimal>();
		Map<Installment, BigDecimal> waterTaxCollForInst = new HashMap<Installment, BigDecimal>();
		Map<Installment, PropertyInstTaxBean> propertyInstPenMap = new HashMap<Installment, PropertyInstTaxBean>();
		DateFormat dateFormat = new SimpleDateFormat(NMCPTISConstants.DATE_FORMAT_DDMMYYY);
		PropertyTaxCollection propTaxColection = new PropertyTaxCollection();
		EgDemand currentDemand = getCurrentDemand();
		Date waterTaxEffectiveDate = null;
		EgDemandDetails penaltyDmdDtls = null;
		Installment installment = null;
		PropertyInstTaxBean propInstTaxBean = null;
		BigDecimal taxAmount = BigDecimal.ZERO;
		BigDecimal collectionAmt = BigDecimal.ZERO;
		BigDecimal balance = BigDecimal.ZERO;
		BigDecimal lpAmt = BigDecimal.ZERO;
		if (getLevyPenalty()) {
			Map<Installment, BigDecimal> instWiseDmdMap = ptUtils.prepareRsnWiseDemandForProp(getBasicProperty()
					.getProperty());
			Map<Installment, BigDecimal> instWiseAmtCollMap = ptUtils.prepareRsnWiseCollForProp(getBasicProperty()
					.getProperty());
			Installment currentInstall = currentDemand.getEgInstallmentMaster();
			try {
				waterTaxEffectiveDate = dateFormat.parse(PENALTY_WATERTAX_EFFECTIVE_DATE);
			} catch (ParseException pe) {
				throw new EGOVRuntimeException("Error while parsing Water Tax Effective Date for Penalty Calculation",
						pe);
			}
			for (EgDemandDetails dd : currentDemand.getEgDemandDetails()) {
				if (NMCPTISConstants.DEMANDRSN_CODE_GENERAL_WATER_TAX.equalsIgnoreCase(dd.getEgDemandReason()
						.getEgDemandReasonMaster().getCode())) {
					waterTaxForInstallment.put(dd.getEgDemandReason().getEgInstallmentMaster(), dd.getAmount());
					waterTaxCollForInst.put(dd.getEgDemandReason().getEgInstallmentMaster(),
							(dd.getAmtCollected() == null) ? BigDecimal.ZERO : dd.getAmtCollected());
				}
			}

			for (Map.Entry<Installment, BigDecimal> mapEntry : instWiseDmdMap.entrySet()) {
				taxAmount = mapEntry.getValue();
				collectionAmt = instWiseAmtCollMap.get(mapEntry.getKey());
				balance = taxAmount.subtract(collectionAmt);
				if (balance.compareTo(BigDecimal.ZERO) == 1) {
					// penalty will be calculated only when there is +ve tax balance
					installment = mapEntry.getKey();
					penaltyDmdDtls = propTaxColection.getDemandDetail(currentDemand, mapEntry.getKey(),
							NMCPTISConstants.DEMANDRSN_CODE_PENALTY_FINES);
					propInstTaxBean = new PropertyInstTaxBean();
					BigDecimal taxForPenaltyCalc = BigDecimal.ZERO;
					taxForPenaltyCalc = taxAmount;
					if (!waterTaxForInstallment.isEmpty()) {
						if (installment.getFromDate().before(waterTaxEffectiveDate)
								&& !ptUtils.between(waterTaxEffectiveDate, installment.getFromDate(),
										installment.getToDate())) {
							LOGGER.info("prepareDmdAndBillDetails - Not considering the Water Tax for penalty calc");
							// Subtract the Water Tax from total tax payble
							// for
							// the
							// installment
							LOGGER.info("prepareDmdAndBillDetails - Water Tax: "
									+ waterTaxForInstallment.get(installment));
							if (waterTaxForInstallment.get(installment) != null) {
								taxForPenaltyCalc = taxForPenaltyCalc.subtract(waterTaxForInstallment.get(installment));
								collectionAmt = collectionAmt.subtract(waterTaxCollForInst.get(installment));
							}
						}
					}
					if (penaltyDmdDtls != null) {
						Calendar penDmdDetUpdTime = Calendar.getInstance();
						penDmdDetUpdTime.setTime(penaltyDmdDtls.getLastUpdatedTimeStamp());
						Calendar todayCalendar = Calendar.getInstance();
						todayCalendar.setTime(new Date());
						int latestPenPaidMonth = penDmdDetUpdTime.get(Calendar.MONTH);
						int currMonth = todayCalendar.get(Calendar.MONTH);
						BigDecimal penAmt = BigDecimal.ZERO;

						if (!installment.equals(currentInstall)) {
							if (latestPenPaidMonth == currMonth) {
								penAmt = penaltyDmdDtls.getAmount();
							} else {
								penAmt = calcPanalty(installment.getFromDate(), taxForPenaltyCalc);
							}
							lpAmt = penAmt.subtract(penaltyDmdDtls.getAmtCollected());
							penaltyDmdDtls.setAmount(penAmt);
						} else {
							if (latestPenPaidMonth == currMonth) {
								penAmt = penaltyDmdDtls.getAmount();
							} else {
								penAmt = calcCurrPenalty(taxForPenaltyCalc, collectionAmt, penaltyDmdDtls.getAmount());
							}
							lpAmt = penAmt.subtract(penaltyDmdDtls.getAmtCollected());
						}
					} else {
						if (!installment.equals(currentInstall)) {
							lpAmt = calcPanalty(installment.getFromDate(), taxForPenaltyCalc);
						} else {
							lpAmt = calcCurrPenalty(taxForPenaltyCalc, collectionAmt, null);
						}
					}
					propInstTaxBean.setInstallment(installment);
					propInstTaxBean.setInstallmentId(installment.getId());
					propInstTaxBean.setInstallmentStr(installment.getDescription());
					propInstTaxBean.setInstTaxAmt(taxAmount);
					propInstTaxBean.setInstPenaltyAmt(lpAmt);
					propInstTaxBean.setInstCollAmt(collectionAmt);
					propertyInstPenMap.put(mapEntry.getKey(), propInstTaxBean);
				}
			}
		}
		return propertyInstPenMap;
	}
}
