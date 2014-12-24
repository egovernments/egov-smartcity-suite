/*
 * @(#)LicenseDemand.java 3.0, 29 Jul, 2013 1:24:27 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.demand.model.EgReasonCategory;
import org.egov.infstr.commons.Module;
import org.egov.license.utils.Constants;

public class LicenseDemand extends EgDemand implements Serializable {
	private static final long serialVersionUID = 1L;
	private transient static final Logger LOGGER = Logger.getLogger(LicenseDemand.class);
	private License license;
	private final DemandGenericDao dmdGenDao = new DemandGenericHibDao();
	private Installment egInstallmentMaster;
	private Date renewalDate;
	private char isLateRenewal;

	public License getLicense() {
		return this.license;
	}

	public LicenseDemand createDemand(final List<FeeMatrix> feeMatrixList, final NatureOfBusiness nature, final LicenseAppType applType, final Installment installment, final License license, final Set<EgDemandReasonMaster> egDemandReasonMasters,
			final BigDecimal totalAmount, final Module module) {
		LOGGER.debug("Initializing Demand...");
		generateDemand(feeMatrixList, installment, license, totalAmount, module);
		LOGGER.debug("Initializing Demand completed.");
		return this;
	}

	private BigDecimal generateDemand(final List<FeeMatrix> feeMatrixList, final Installment installment, final License license, BigDecimal totalAmount, final Module module) {
		setIsHistory("N");
		setEgInstallmentMaster(installment);
		setCreateTimestamp(new Date());
		setLicense(license);
		setIsLateRenewal('0');
		LOGGER.debug("calculating FEE          ...............................................");
		Set<EgDemandDetails> demandDetails = null;
		if (getEgDemandDetails().isEmpty() || getEgDemandDetails() == null) {
			demandDetails = new LinkedHashSet<EgDemandDetails>();
		} else {
			demandDetails = getEgDemandDetails();
		}
		for (final FeeMatrix fm : feeMatrixList) {
			final EgDemandReasonMaster reasonMaster = fm.getDemandReasonMaster();
			final EgDemandReason reason = this.dmdGenDao.getDmdReasonByDmdReasonMsterInstallAndMod(reasonMaster, installment, module);
			LOGGER.info("Reson for Reason Master" + ":master:" + reasonMaster.getReasonMaster() + "Reason:" + reason);
			if (fm.getFeeType().getName().contains("Late")) {
				continue;
			} else {
				if (license.getFeeTypeStr().equalsIgnoreCase("PFA") && fm.getFeeType().getName().equalsIgnoreCase("CNC")) {
					final InstallmentDao installmentDao = CommonsDaoFactory.getDAOFactory().getInstallmentDao();
					final List<Installment> effectiveInstallment = installmentDao.getEffectiveInstallmentsforModuleandDate(installment.getFromDate(), 12 * 5, module);
					for (final Installment inst : effectiveInstallment) {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("REASON MASTER::::" + reasonMaster.getReasonMaster() + "::" + inst + "::" + fm.getAmount() + ":::" + module.getModuleName());
						}
						final EgDemandReason demandReason = this.dmdGenDao.getDmdReasonByDmdReasonMsterInstallAndMod(reasonMaster, inst, module);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug(reason + "::" + inst + "::" + fm.getAmount());
						}
						demandDetails.add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), demandReason, BigDecimal.ZERO));
						totalAmount = totalAmount.add(fm.getAmount());
					}
				} else {
					if (module.getModuleName().equals(Constants.PWDLICENSE_MODULENAME) || module.getModuleName().equals(Constants.ELECTRICALLICENSE_MODULENAME)) {
						if (license.getFeeExemption() != null && license.getFeeExemption().equals("YES") && !license.getTradeName().getLicenseType().getName().equalsIgnoreCase((Constants.LICENSE_APP_TYPE_RENEW))) {
							totalAmount = totalAmount.add(getDemandDetailsForFeeExemption(demandDetails, fm, reason, totalAmount, license));
						} else {
							if (license.getOldLicenseNumber() != null) {
								// For Enter Pwd License generated full demand
								demandDetails.add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO));
								totalAmount = totalAmount.add(fm.getAmount());
							} else {
								totalAmount = getDemandDetails(demandDetails, fm, reason, totalAmount, license);

								if (!totalAmount.equals(BigDecimal.ZERO)
										&& license.getState() != null
										&& (license.getState().getValue().equals(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE + Constants.WORKFLOW_REG_FEE_STATE_COLLECTED) || license.getState().getValue()
												.equals(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE + Constants.WORKFLOW_REG_FEE_STATE_COLLECTED))) {
									for (final EgDemandDetails demDetails : demandDetails) {

										totalAmount = totalAmount.add(demDetails.getAmount()).subtract(demDetails.getAmtCollected());
									}
								}
							}
						}
					} else {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug(reason + "::" + fm.getAmount());
						}
						demandDetails.add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO));
						totalAmount = totalAmount.add(fm.getAmount());
					}
				}
			}
		}

		if (license.getState() == null || (license.getState() != null && license.getState().getValue().equals(Constants.WORKFLOW_STATE_TYPE_END))) {
			if (license.getDeduction() != null && license.getDeduction().compareTo(BigDecimal.ZERO) != 0) {
				final EgReasonCategory reasonCategory = this.dmdGenDao.getReasonCategoryByCode(Constants.DEMANDRSN_REBATE);
				final List<EgDemandReasonMaster> demandReasonMasterByCategory = this.dmdGenDao.getDemandReasonMasterByCategoryAndModule(reasonCategory, module);
				for (final EgDemandReasonMaster demandReasonMaster : demandReasonMasterByCategory) {
					final EgDemandReason reason = this.dmdGenDao.getDmdReasonByDmdReasonMsterInstallAndMod(demandReasonMaster, installment, module);
					final List<EgDemandReason> egDemandReason = this.dmdGenDao.getDemandReasonByInstallmentAndModule(installment, module);
					for (final EgDemandReason egDemReason : egDemandReason) {
						if (egDemReason.getId().equals(reason.getEgDemandReason().getId())) {
							for (final EgDemandDetails demDetails : demandDetails) {
								if (demDetails.getEgDemandReason().getId().equals(reason.getEgDemandReason().getId())) {
									demDetails.setAmtRebate(license.getDeduction());
								}
								setAmtRebate(license.getDeduction());
							}
						}
					}
				}
				totalAmount = totalAmount.subtract(license.getDeduction());
			}

			if (license.getOtherCharges() != null && license.getOtherCharges().compareTo(BigDecimal.ZERO) != 0) {
				final EgDemandReasonMaster demandReasonMasterByCode = this.dmdGenDao.getDemandReasonMasterByCode("Other Charges", module);
				final EgDemandReason reason = this.dmdGenDao.getDmdReasonByDmdReasonMsterInstallAndMod(demandReasonMasterByCode, installment, module);
				demandDetails.add(EgDemandDetails.fromReasonAndAmounts(license.getOtherCharges() != null ? license.getOtherCharges() : BigDecimal.ZERO, reason, BigDecimal.ZERO));
				totalAmount = totalAmount.add(license.getOtherCharges());
			}

			if (license.getSwmFee() != null && license.getSwmFee().compareTo(BigDecimal.ZERO) != 0) {
				final EgDemandReasonMaster demandReasonMasterByCode = this.dmdGenDao.getDemandReasonMasterByCode("SWM", module);
				final EgDemandReason reason = this.dmdGenDao.getDmdReasonByDmdReasonMsterInstallAndMod(demandReasonMasterByCode, installment, module);
				demandDetails.add(EgDemandDetails.fromReasonAndAmounts(license.getSwmFee() != null ? license.getSwmFee() : BigDecimal.ZERO, reason, BigDecimal.ZERO));
				totalAmount = totalAmount.add(license.getSwmFee());
			}
		}
		setEgDemandDetails(demandDetails);
		setBaseDemand(totalAmount);
		return totalAmount;
	}

	/**
	 * To generate Demand Details for Registration Fee and CNC Fee based on the State of the license, if the license is created for the first time then only Registration Fee demand is generated and if in the next state after Registration Fee Collected, then CNC demand is generated.
	 * @param demandDetails
	 * @param fm
	 * @param reason
	 * @param totalAmount
	 * @param license2
	 * @return
	 */
	private BigDecimal getDemandDetails(final Set<EgDemandDetails> demandDetails, final FeeMatrix fm, final EgDemandReason reason, BigDecimal totalAmount, final License license2) {

		if (license2.getState() == null && fm.getDemandReasonMaster().getCode().equals(Constants.DEMAND_REASON_REGN_FEE)) {
			demandDetails.add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO));
			totalAmount = totalAmount.add(fm.getAmount());
		} else if (license2.getState() != null && license2.getState().getValue().equals(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE + Constants.WORKFLOW_REG_FEE_STATE_COLLECTED) && fm.getDemandReasonMaster().getCode().equals(Constants.CNC)) {
			demandDetails.add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO));
			totalAmount = totalAmount.add(fm.getAmount());
		} else if (license2.getState() != null && license2.getState().getValue().equals(Constants.WORKFLOW_STATE_TYPE_END) && fm.getDemandReasonMaster().getCode().equals(Constants.DEMAND_REASON_REGN_FEE)) {
			demandDetails.add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO));
			totalAmount = totalAmount.add(fm.getAmount());
		} else if (license2.getState() != null && license2.getState().getValue().equals(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE + Constants.WORKFLOW_REG_FEE_STATE_COLLECTED) && fm.getDemandReasonMaster().getCode().equals(Constants.CNC)) {
			demandDetails.add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO));
			totalAmount = totalAmount.add(fm.getAmount());
		}
		return totalAmount;
	}

	/**
	 * To generate Demand Details for Fee Exemption, if Fee Exemption is yes then Amt Rebate is set same as Registration Fee
	 * @param demandDetails
	 * @param fm
	 * @param reason
	 * @param totalAmount
	 * @param license2
	 * @return
	 */
	private BigDecimal getDemandDetailsForFeeExemption(final Set<EgDemandDetails> demandDetails, final FeeMatrix fm, final EgDemandReason reason, BigDecimal totalAmount, final License license2) {

		final EgDemandDetails egDemandDetails = EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO);
		// If FeeExempted then set Amount Rebate equal to Registration Fee Demand
		if (license2.getFeeExemption() != null && license2.getFeeExemption().equals("YES") && fm.getDemandReasonMaster().getCode().equals(Constants.DEMAND_REASON_REGN_FEE)) {
			final BigDecimal amtRebate = egDemandDetails.getAmount();
			egDemandDetails.setAmtRebate(amtRebate);
			setAmtRebate(amtRebate);
		}
		totalAmount = egDemandDetails.getAmount();
		demandDetails.add(egDemandDetails);
		return totalAmount;
	}

	private BigDecimal generateLatePenalty(BigDecimal totalAmount, final Set<EgDemandDetails> demandDetails, final FeeMatrix fm, final EgDemandReason reason, final boolean isExpired, final int noOfMonths) {
		if (fm.getFeeType().getName().contains("Late")) {
			if (isExpired == true && noOfMonths - Constants.GRACEPERIOD >= 1) {
				final BigDecimal calculatedLateFee = fm.getAmount().multiply(BigDecimal.valueOf(noOfMonths));
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("calculatedLateFee::" + calculatedLateFee);
					demandDetails.add(EgDemandDetails.fromReasonAndAmounts(calculatedLateFee, reason, BigDecimal.ZERO));
					totalAmount = totalAmount.add(calculatedLateFee);
				}
			}
		}
		return totalAmount;
	}

	public LicenseDemand renewDemand(final List<FeeMatrix> feeMatrixList, final NatureOfBusiness nature, final LicenseAppType applType, final Installment installment, final License license, final Set<EgDemandReasonMaster> egDemandReasonMasters,
			BigDecimal totalAmount, final Module module, final Date renewalDate, final List<EgDemandDetails> oldDetails) {
		LOGGER.debug("Initializing Demand...");
		setIsHistory("N");
		setEgInstallmentMaster(installment);
		setCreateTimestamp(new Date());
		setLicense(license);
		setRenewalDate(new Date());
		final String dateDiffToExpiryDate = license.getDateDiffToExpiryDate(renewalDate);
		final String[] split = dateDiffToExpiryDate.split("/");

		final boolean isExpired = split[0].equalsIgnoreCase("false") ? false : true;
		final int noOfMonths = Integer.parseInt(split[1]);
		final Set<EgDemandDetails> demandDetails = new LinkedHashSet<EgDemandDetails>();
		for (final FeeMatrix fm : feeMatrixList) {
			if (fm.getFeeType().getName().contains("Late")) {
				if (isExpired == true && noOfMonths - Constants.GRACEPERIOD >= 1) {
					final EgDemandReasonMaster reasonMaster = fm.getDemandReasonMaster();
					final EgDemandReason reason = this.dmdGenDao.getDmdReasonByDmdReasonMsterInstallAndMod(reasonMaster, installment, module);
					totalAmount = generateLatePenalty(totalAmount, demandDetails, fm, reason, isExpired, noOfMonths);
				}
			}

		}
		totalAmount = totalAmount.add(generateDemand(feeMatrixList, installment, license, totalAmount, module));
		demandDetails.addAll(getEgDemandDetails());
		if (isExpired == true && noOfMonths - Constants.GRACEPERIOD >= 1) {
			setIsLateRenewal((char) (noOfMonths - Constants.GRACEPERIOD));
		}
		if (license.getState() != null && !license.getState().getValue().equals(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE + Constants.WORKFLOW_REG_FEE_STATE_COLLECTED)) {
			demandDetails.addAll(oldDetails);
		}
		// setEgDemandDetails(demandDetails);
		for (final EgDemandDetails details : getEgDemandDetails()) {
			if (details.getEgDemandReason() == null) {
				LOGGER.info("Reason not Found for " + details.getAmount() + " ");
			}
		}
		setIsLateRenewal(("" + noOfMonths).charAt(0));
		setBaseDemand(totalAmount);
		LOGGER.debug("Initializing Demand completed.");
		return this;
	}

	public void setLicense(final License license) {
		this.license = license;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		if (this.license == null) {
			str.append("license=null");
		} else {
			str.append(this.license.getApplicationNumber());
		}
		str.append(super.toString());
		return str.toString();
	}

	public void getDemandForInstallment(final Installment installment) {
	}

	public void updateCollectedForExisting() {
		for (final EgDemandDetails dd : this.getEgDemandDetails()) {
			dd.setAmtCollected(dd.getAmount());
		}
	}

	@Override
	public Installment getEgInstallmentMaster() {
		return this.egInstallmentMaster;
	}

	@Override
	public void setEgInstallmentMaster(final Installment egInstallmentMaster) {
		this.egInstallmentMaster = egInstallmentMaster;
	}

	public Date getRenewalDate() {
		return this.renewalDate;
	}

	public void setRenewalDate(final Date renewalDate) {
		this.renewalDate = renewalDate;
	}

	public char getIsLateRenewal() {
		return this.isLateRenewal;
	}

	public void setIsLateRenewal(final char isLateRenewal) {
		this.isLateRenewal = isLateRenewal;
	}
}