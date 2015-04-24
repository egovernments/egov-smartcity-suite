package org.egov.ptis.nmc.bill;

import static org.egov.demand.interfaces.LatePayPenaltyCalculator.LPPenaltyCalcType.SIMPLE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.ARR_LP_DATE_BREAKUP;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.ARR_LP_DATE_CONSTANT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEFAULT_FUNCTIONARY_CODE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEFAULT_FUND_CODE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEFAULT_FUND_SRC_CODE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_WATER_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_PENALTY_FINES;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEPT_CODE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.LP_PERCENTAGE_CONSTANT;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.demand.dao.DCBDaoFactory;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infstr.ValidationException;
import org.egov.infstr.utils.EgovUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.model.PropertyInstTaxBean;
import org.egov.ptis.nmc.service.Amount;
import org.egov.ptis.nmc.service.PenaltyCalculationService;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.ptis.service.collection.PropertyTaxCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class NMCPropertyTaxBillable extends PropertyTaxBillable {

	private PropertyTaxUtil ptUtils = new PropertyTaxUtil();
	private static final Logger LOGGER = Logger
			.getLogger(NMCPropertyTaxBillable.class);
	private static final BigDecimal VALUE_HUNDRED = new BigDecimal(100);

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
	private String collType;
	private String pgType;
	private Map<Installment, PropertyInstTaxBean> instTaxBean = new HashMap<Installment, PropertyInstTaxBean>();
	private Boolean isMiscellaneous = Boolean.FALSE;
	private BigDecimal mutationFee;
	private Map<Installment, EgDemandDetails> installmentWisePenaltyDemandDetail = new TreeMap<Installment, EgDemandDetails>();
	@Autowired
	private UserService userService;

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
	public BigDecimal calcPanalty(Date latestCollReceiptDate, Date fromDate,
			BigDecimal amount) {
		LOGGER.info("Enter into calcPanalty - fromDate: " + fromDate
				+ ", amount: " + amount);
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

		DateFormat dateFormat = new SimpleDateFormat(
				PropertyTaxConstants.DATE_FORMAT_DDMMYYY);
		Date arrearlpDate = null;
		Date arrearlpDateBreakup = null;
		Date frmDate = null;
		try {
			arrearlpDate = dateFormat.parse(ARR_LP_DATE_CONSTANT);
			arrearlpDateBreakup = dateFormat.parse(ARR_LP_DATE_BREAKUP);
			frmDate = dateFormat.parse(dateFormat.format(fromDate));
		} catch (ParseException e) {
			LOGGER.error(
					"Error while parsing Arrear Late Payment penalty dates", e);
		}

		if (getLPPenaltyCalcType().equals(LPPenaltyCalcType.SIMPLE)) {
			int noOfMonths = 0;
			Date penaltyFromDate = null;

			if (latestCollReceiptDate != null
					&& latestCollReceiptDate.after(frmDate)
					&& latestCollReceiptDate.after(arrearlpDateBreakup)) {

				latestCollRcptCalendar.add(Calendar.MONTH, 1);
				arrearsPenaltyApplicableDate.setTime(latestCollRcptCalendar
						.getTime());
				penaltyFromDate = arrearsPenaltyApplicableDate.getTime();

			} else if (frmDate.after(arrearlpDateBreakup)
					|| frmDate.equals(arrearlpDateBreakup)) {
				arrearsPenaltyApplicableDate.set(Calendar.YEAR,
						(fromDateCalendar.get(Calendar.YEAR) + 1));
				penaltyFromDate = arrearsPenaltyApplicableDate.getTime();
			} else {
				penaltyFromDate = arrearlpDate;
			}

			LOGGER.info("calcPanalty - penaltyFromDate: " + penaltyFromDate);

			noOfMonths = PropertyTaxUtil.getMonthsBetweenDates(penaltyFromDate,
					new Date());

			penalty = amount.multiply(LP_PERCENTAGE_CONSTANT)
					.divide(BigDecimal.valueOf(100))
					.multiply(BigDecimal.valueOf(noOfMonths));

			LOGGER.info("calcPanalty - noOfMonths: " + noOfMonths
					+ ", penalty: " + penalty);
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
			currentPenaltyApplicableDate.set(Calendar.YEAR,
					today.get(Calendar.YEAR) + 1);
		} else {
			currentPenaltyApplicableDate.set(Calendar.YEAR,
					today.get(Calendar.YEAR));
		}

		if (today.getTime().after(currentPenaltyApplicableDate.getTime())
				|| today.getTime().equals(
						currentPenaltyApplicableDate.getTime())) {
			BigDecimal balanceTax = amount;
			int noOfMonths = 0;
			if (latestCollRcptDate != null
					&& latestCollRcptCalendar.get(Calendar.MONTH) >= 0
					&& latestCollRcptCalendar.get(Calendar.MONTH) < 3
					&& today.get(Calendar.YEAR) == latestCollRcptCalendar
							.get(Calendar.YEAR)) {
				currentPenaltyApplicableDate.set(Calendar.MONTH,
						latestCollRcptCalendar.get(Calendar.MONTH) + 1);
				noOfMonths = PropertyTaxUtil.getMonthsBetweenDates(
						currentPenaltyApplicableDate.getTime(), new Date());
			} else {
				noOfMonths = PropertyTaxUtil.getMonthsBetweenDates(
						currentPenaltyApplicableDate.getTime(), new Date());
			}
			penalty = balanceTax.multiply(LP_PERCENTAGE_CONSTANT)
					.divide(VALUE_HUNDRED).multiply(new BigDecimal(noOfMonths));
		}

		return EgovUtils.roundOff(penalty);
	}

	@Override
	public BigDecimal calcLPPenaltyForPeriod(Date fromDate, Date toDate,
			BigDecimal amount) {
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
			EgBillDao egBillDao = DCBDaoFactory.getDaoFactory()
					.getEgBillDao();
			if (getUserId() != null && !getUserId().equals("")) {
				String loginUser = userService.getUserById(getUserId())
						.getName();
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
				consumerCode.append(getBasicProperty().getPropertyID()
						.getZone().getBoundaryNum());
			}
			consumerCode.append(" Ward:");
			if (getBasicProperty().getPropertyID().getWard() != null) {
				consumerCode.append(
						getBasicProperty().getPropertyID().getWard()
								.getBoundaryNum()).append(")");
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

		String query = "select ptd from Ptdemand ptd "
				+ "inner join fetch ptd.egDemandDetails dd "
				+ "inner join fetch dd.egDemandReason dr "
				+ "inner join fetch dr.egDemandReasonMaster drm "
				+ "inner join fetch ptd.egptProperty p "
				+ "inner join fetch p.basicProperty bp "
				+ "where bp.active = true "
				+ "and (p.status = 'A' or p.status = 'I') "
				+ "and p = :property "
				+ "and ptd.egInstallmentMaster = :installment "
				+ "and drm.code = :penaltyReasonCode";

		List list = HibernateUtil.getCurrentSession().createQuery(query)
				.setEntity("property", property)
				.setEntity("installment", currentInstallment)
				.setString("penaltyReasonCode", DEMANDRSN_CODE_PENALTY_FINES)
				.list();

		Ptdemand ptDemand = null;

		if (list.isEmpty()) {
			LOGGER.debug("No penalty demand details..");
		} else {
			ptDemand = (Ptdemand) list.get(0);
			for (EgDemandDetails dmdDet : ptDemand.getEgDemandDetails()) {
				if (dmdDet.getEgDemandReason().getEgDemandReasonMaster()
						.getCode()
						.equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES)) {
					installmentWisePenaltyDemandDetails.put(dmdDet
							.getEgDemandReason().getEgInstallmentMaster(),
							dmdDet);
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
			
			installmentWisePenaltyDemandDetail = getInstallmentWisePenaltyDemandDetails(property, currentInstall);
			
			Map<Installment, BigDecimal> instWiseDmdMap = installmentWiseDemandAndCollection.get("DEMAND");
			Map<Installment, BigDecimal> instWiseAmtCollMap = installmentWiseDemandAndCollection.get("COLLECTION");
			
			Date earliestModificationDate = null;
			BasicProperty basicProperty = ((Ptdemand) currentDemand).getEgptProperty().getBasicProperty();
			boolean isMigrated = basicProperty.getIsMigrated().equals(NMCPTISConstants.STATUS_MIGRATED); 
			
			waterTaxEffectiveDate = PropertyTaxUtil.getWaterTaxEffectiveDateForPenalty();
			
			String propertyId = getPropertyId();
			Map<String, Date> latestCollRcptDateMap = ptUtils.getLatestCollRcptDateForProp(propertyId);

			for (EgDemandDetails dd : currentDemand.getEgDemandDetails()) {
				if (DEMANDRSN_CODE_GENERAL_WATER_TAX.equalsIgnoreCase(dd.getEgDemandReason().getEgDemandReasonMaster()
						.getCode())) {
					waterTaxForInst.put(dd.getEgDemandReason().getEgInstallmentMaster(), dd.getAmount());
					waterTaxCollForInst.put(dd.getEgDemandReason().getEgInstallmentMaster(),
							(dd.getAmtCollected() == null) ? BigDecimal.ZERO : dd.getAmtCollected());
				}
			}

			if (isMigrated) {
				
				earliestModificationDate = ptUtils.getEarliestModificationDate(basicProperty.getUpicNo());
				
				for (Map.Entry<Installment, BigDecimal> mapEntry : instWiseDmdMap.entrySet()) {

					installment = mapEntry.getKey();
					waterTaxPenalty.setAmount(BigDecimal.ZERO);

					// Applying the old penalty rules for the installments
					// before the reassessed(modified) date
					if (earliestModificationDate == null
							|| (installment.getFromDate().before(earliestModificationDate) && installment.getToDate()
									.before(earliestModificationDate))) {

						taxAmount = mapEntry.getValue();
						collAmtForPenaltyCalc = instWiseAmtCollMap.get(mapEntry.getKey());
						actualCollectionAmount = instWiseAmtCollMap.get(mapEntry.getKey());
						balance = taxAmount.subtract(collAmtForPenaltyCalc);

						thereIsBalance = balance.compareTo(BigDecimal.ZERO) == 1;

						LOGGER.debug("getCalculatedPenalty - Installment=" + mapEntry.getKey() + ", thereIsBalance="
								+ thereIsBalance);
						/*
						 * penalty will be calculated only when there is +ve tax
						 * balance
						 */
						if (thereIsBalance) {

							if (!installmentWisePenaltyDemandDetail.isEmpty()) {
								penaltyDmdDtls = installmentWisePenaltyDemandDetail.get(mapEntry.getKey());
							}

							propInstTaxBean = new PropertyInstTaxBean();
							BigDecimal taxForPenaltyCalc = BigDecimal.ZERO;
							Date latestCollRcptDate = null;
							taxForPenaltyCalc = balance;
							installmentWaterTax = BigDecimal.ZERO;

							if (!waterTaxForInst.isEmpty()) {
								if (installment.getFromDate().before(waterTaxEffectiveDate)
										&& !ptUtils.between(waterTaxEffectiveDate, installment.getFromDate(),
												installment.getToDate())) {
									
									installmentWaterTax = waterTaxForInst.get(installment);
									LOGGER.info("prepareDmdAndBillDetails - Water Tax: " + installmentWaterTax);
									
									if (installmentWaterTax != null && installmentWaterTax.compareTo(BigDecimal.ZERO) > 0) {
										//calculate the penalty on water tax separately from Jan 2012, 
										taxForPenaltyCalc = taxForPenaltyCalc.subtract(installmentWaterTax);
										Date date = latestCollRcptDateMap.get(mapEntry.getKey().getDescription());
										
										fromDateForWaterTaxPenaltyCalc = date != null && date.after(waterTaxEffectiveDate) ? date : waterTaxEffectiveDate;
										
										waterTaxCollection = waterTaxCollForInst.get(installment);
										
										// taking the balance water tax 
										if (waterTaxCollection != null && waterTaxCollection.compareTo(BigDecimal.ZERO) > 0) {
											installmentWaterTax = installmentWaterTax.subtract(waterTaxCollection);
										}
										
										waterTaxPenalty.setAmount(new PenaltyCalculationService().calculatePenalty(fromDateForWaterTaxPenaltyCalc, installmentWaterTax));
										thereIsBalance = taxForPenaltyCalc.compareTo(BigDecimal.ZERO) == 1;
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

									latestCollRcptDate = latestCollRcptDateMap.get(mapEntry.getKey().getDescription());
									Calendar todayCalendar = Calendar.getInstance();
									todayCalendar.setTime(new Date());
									int currMonth = todayCalendar.get(Calendar.MONTH);
									int currYear = todayCalendar.get(Calendar.YEAR);
									Calendar latestCollRcptCalendar = Calendar.getInstance();
									
									BigDecimal balancePenalty = BigDecimal.ZERO;
											
									if (penaltyDmdDtls.getAmtCollected().compareTo(BigDecimal.ZERO) > 0) {
										balancePenalty = penaltyDmdDtls.getAmount().subtract(penaltyDmdDtls.getAmtCollected());
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

												penAmt = calcPanalty(latestCollRcptDate, installment.getFromDate(),
														taxForPenaltyCalc);

												penAmt = penAmt.add(balancePenalty);
											}

											lpAmt = penAmt;

										} else if (collAmtForPenaltyCalc.equals(BigDecimal.ZERO)) {
											lpAmt = calcPanalty(null, installment.getFromDate(), taxForPenaltyCalc);
										}
									} else {
										if (latestCollRcptDate != null
												&& latestCollRcptCalendar.get(Calendar.MONTH) == currMonth) {

											penAmt = balancePenalty;

										} else {
											if (collAmtForPenaltyCalc.equals(BigDecimal.ZERO)) {
												penAmt = calcCurrPenalty(null, taxForPenaltyCalc);
											} else {
												penAmt = calcCurrPenalty(latestCollRcptDate, taxForPenaltyCalc);
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
									lpAmt = calcPanalty(latestCollRcptDate, installment.getFromDate(),
											taxForPenaltyCalc);
								} else {
									lpAmt = calcCurrPenalty(latestCollRcptDate, taxForPenaltyCalc);
								}
							}

							if (waterTaxPenalty.isGreaterThanZero()) {
								lpAmt = lpAmt.add(waterTaxPenalty.getAmount());
							}
							
							propInstTaxBean = createInstallmentTaxPenlatyBean(penaltyDmdDtls, installment, taxAmount,
									actualCollectionAmount, lpAmt, currentInstall);

							propertyInstPenMap.put(mapEntry.getKey(), propInstTaxBean);
						}
					} else {
						levyNewPenaltyRules = true;
						break;
					}
				}
				
				if (levyNewPenaltyRules) {
					calculateAndPreparePenaltyBeans(waterTaxForInst, waterTaxCollForInst, propertyInstPenMap,
							instWiseDmdMap, instWiseAmtCollMap, currentInstall, basicProperty, latestCollRcptDateMap);
				}
			} else {
				
				calculateAndPreparePenaltyBeans(waterTaxForInst, waterTaxCollForInst, propertyInstPenMap,
						instWiseDmdMap, instWiseAmtCollMap, currentInstall, basicProperty, latestCollRcptDateMap);
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
	private void calculateAndPreparePenaltyBeans(
			Map<Installment, BigDecimal> waterTaxForInst,
			Map<Installment, BigDecimal> waterTaxCollForInst,
			Map<Installment, PropertyInstTaxBean> propertyInstPenMap,
			Map<Installment, BigDecimal> instWiseDmdMap,
			Map<Installment, BigDecimal> instWiseAmtCollMap,
			Installment currentInstall, BasicProperty basicProperty,
			Map<String, Date> installmentAndLatestCollDate)
			throws ValidationException {

		PropertyInstTaxBean propInstTaxBean;
		PenaltyCalculationService penaltyCalculationService = new PenaltyCalculationService(
				basicProperty, instWiseDmdMap, instWiseAmtCollMap,
				installmentAndLatestCollDate,
				installmentWisePenaltyDemandDetail);

		Map<Installment, BigDecimal> installmentWisePenalty = penaltyCalculationService
				.getInstallmentWisePenalty();

		Installment inst = null;
		EgDemandDetails penaltyDemandDetails = null;

		if (installmentWisePenalty != null && !installmentWisePenalty.isEmpty()) {
			for (Map.Entry<Installment, BigDecimal> mapEntry : installmentWisePenalty
					.entrySet()) {

				inst = mapEntry.getKey();

				if (!installmentWisePenaltyDemandDetail.isEmpty()) {
					penaltyDemandDetails = installmentWisePenaltyDemandDetail
							.get(inst);
				}

				propInstTaxBean = createInstallmentTaxPenlatyBean(
						penaltyDemandDetails, inst, instWiseDmdMap.get(inst),
						instWiseAmtCollMap.get(inst), mapEntry.getValue(),
						currentInstall);

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
	private PropertyInstTaxBean createInstallmentTaxPenlatyBean(
			EgDemandDetails penaltyDmdDtls, Installment installment,
			BigDecimal taxAmount, BigDecimal actualCollectionAmount,
			BigDecimal lpAmt, Installment currentInstall) {

		PropertyInstTaxBean propInstTaxBean = new PropertyInstTaxBean();

		propInstTaxBean.setInstallment(installment);
		propInstTaxBean.setInstallmentId(installment.getId());
		propInstTaxBean.setInstallmentStr(installment.getDescription());
		propInstTaxBean.setInstTaxAmt(taxAmount);
		propInstTaxBean
				.setInstPenaltyAmt(lpAmt.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO
						: lpAmt);
		propInstTaxBean.setInstCollAmt(actualCollectionAmount);
		propInstTaxBean.setInstBalanceAmt(taxAmount
				.subtract(actualCollectionAmount));

		if (penaltyDmdDtls != null) {
			propInstTaxBean.setInstPenaltyCollAmt(penaltyDmdDtls
					.getAmtCollected());
		}

		if (!installment.equals(currentInstall)) {
			propInstTaxBean.setInstRebateAmt(new BigDecimal("0"));
		} else {
			propInstTaxBean
					.setInstRebateAmt(new PropertyTaxCollection()
							.calcEarlyPayRebate(
									taxAmount,
									getTaxforReason(
											getBasicProperty().getProperty(),
											NMCPTISConstants.DEMANDRSN_CODE_GENERAL_TAX),
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
		return penaltyDmdDtls.getAmount().compareTo(
				penaltyDmdDtls.getAmtCollected()) > 0;
	}

	private BigDecimal getTaxforReason(Property property, String demandReason) {
		BigDecimal reasonTax = BigDecimal.ZERO;
		Installment inst = PropertyTaxUtil.getCurrentInstallment();
		PtDemandDao ptDemandDao = PropertyDAOFactory.getDAOFactory()
				.getPtDemandDao();
		EgDemand egDemand = ptDemandDao
				.getNonHistoryCurrDmdForProperty(property);
		for (EgDemandDetails dmdDet : egDemand.getEgDemandDetails()) {
			if (dmdDet.getEgDemandReason().getEgInstallmentMaster()
					.equals(inst)
					&& dmdDet.getEgDemandReason().getEgDemandReasonMaster()
							.getCode().equals(demandReason)) {
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
		return currentTax.multiply(NMCPTISConstants.ADVANCE_REBATE_PERCENTAGE)
				.divide(VALUE_HUNDRED);
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