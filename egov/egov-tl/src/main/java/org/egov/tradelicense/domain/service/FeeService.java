package org.egov.tradelicense.domain.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.services.PersistenceService;
import org.egov.tradelicense.domain.entity.FeeMatrix;
import org.egov.tradelicense.domain.entity.License;
import org.egov.tradelicense.domain.entity.LicenseAppType;
import org.egov.tradelicense.domain.entity.NatureOfBusiness;
import org.egov.tradelicense.domain.entity.SubCategory;
import org.egov.tradelicense.utils.Constants;

public class FeeService extends PersistenceService<FeeMatrix, Long> {
	public static final String FEE_BY_NAME_TYPE_NATURE = "FEE_BY_NAME_TYPE_NATURE";
	public static final String CNC = "CNC";
	public static final String PFA = "PFA";
	public static final Logger LOGGER = Logger.getLogger(FeeService.class);
	public static final String HOM = "HOM";

	@SuppressWarnings("unchecked")
	public List<FeeMatrix> getFeeList(SubCategory tradeName, LicenseAppType appType, NatureOfBusiness natureOfBusiness) {
		LOGGER.debug("tradeName:::"+tradeName);
				
		return this.findAllByNamedQuery(FEE_BY_NAME_TYPE_NATURE, tradeName.getId(), appType.getId());
	}

	/**
	 * @param tradeName -- is mandatory
	 * @param appType --is mandatory
	 * @param tradeNatureId
	 * @param otherCharges --will be added to sum
	 * @param deduction -- will be subtracted from sum
	 * @return calculatedFee This api will also set the LicenseNumber prefix when new class is added needs to update the api to set corresponding prefix(fee type) eg: TradeLicense has PFA or CNC HospitalLicense has HOM only put loggers
	 */
	public BigDecimal calculateFee(License license, SubCategory tradeName, LicenseAppType appType, NatureOfBusiness natureOfBusiness, BigDecimal otherCharges, BigDecimal deduction) {
		BigDecimal totalFee = BigDecimal.ZERO;
		boolean isPFA = false;
		final List<FeeMatrix> feeList = this.getFeeList(tradeName, appType, natureOfBusiness);
		totalFee = totalFee.add(otherCharges != null ? otherCharges : BigDecimal.ZERO);
		totalFee = totalFee.subtract(deduction != null ? deduction : BigDecimal.ZERO);
		if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.TRADELICENSE)) {
			for (final FeeMatrix fee : feeList) {
				if (fee.getFeeType().getName().equalsIgnoreCase(PFA)) {
					isPFA = true;
				}
				totalFee = totalFee.add(fee.getAmount());
			}
			if (isPFA) {
				license.setFeeTypeStr(PFA);
			} else {
				license.setFeeTypeStr(CNC);
			}
		} else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.HOSPITALLICENSE)) {
			for (final FeeMatrix fee : feeList) {
				totalFee = totalFee.add(fee.getAmount());
			}
			license.setFeeTypeStr(HOM);
		} else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.HAWKERLICENSE)) {
			for (final FeeMatrix fee : feeList) {
				if (fee.getFeeType().getName().equalsIgnoreCase(PFA)) {
					isPFA = true;
				}
				totalFee = totalFee.add(fee.getAmount());
			}
			if (isPFA) {
				license.setFeeTypeStr(PFA);
			} else {
				license.setFeeTypeStr(CNC);
			}
		} else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.WATERWORKSLICENSE)) {
			for (final FeeMatrix fee : feeList) {
				if (fee.getFeeType().getName().equalsIgnoreCase(PFA)) {
					isPFA = true;
				}
				totalFee = totalFee.add(fee.getAmount());
			}
			if (isPFA) {
				license.setFeeTypeStr(PFA);
			} else {
				license.setFeeTypeStr(CNC);
			}
		} else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.PWDCONTRACTORLICENSE)) {
			for (final FeeMatrix fee : feeList) {
				
				totalFee = totalFee.add(fee.getAmount());
			}
				license.setFeeTypeStr(CNC);
			} else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.VETERINARYLICENSE)) {
			for (final FeeMatrix fee : feeList) {

				totalFee = totalFee.add(fee.getAmount());
			}
			license.setFeeTypeStr(CNC);
		} else {
			LOGGER.warn("Fee Type prefix is not set");
		}
		return totalFee;
	}

	public BigDecimal calculateFeeForExisting(License license, SubCategory tradeName, LicenseAppType appType, NatureOfBusiness natureOfBusiness, BigDecimal otherCharges, BigDecimal deduction) {
		BigDecimal totalFee = BigDecimal.ZERO;
		boolean isPFA = false;
		
		final List<FeeMatrix> feeList = this.getFeeList(tradeName, appType, natureOfBusiness);
		totalFee = totalFee.add(otherCharges != null ? otherCharges : BigDecimal.ZERO);
		totalFee = totalFee.subtract(deduction != null ? deduction : BigDecimal.ZERO);
		if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.TRADELICENSE)) {
			for (final FeeMatrix fee : feeList) {
				if (fee.getFeeType().getName().equalsIgnoreCase(PFA)) {
					isPFA = true;
				}
				totalFee = totalFee.add(fee.getAmount());
			}
			if (isPFA) {
				license.setFeeTypeStr(PFA);
			} else {
				license.setFeeTypeStr(CNC);
			}
		} else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.HOSPITALLICENSE)) {
			for (final FeeMatrix fee : feeList) {
				totalFee = totalFee.add(fee.getAmount());
			}
			license.setFeeTypeStr(HOM);
		} else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.HAWKERLICENSE)) {
			for (final FeeMatrix fee : feeList) {
				if (fee.getFeeType().getName().equalsIgnoreCase(PFA)) {
					isPFA = true;
				}
				totalFee = totalFee.add(fee.getAmount());
			}
			if (isPFA) {
				license.setFeeTypeStr(PFA);
			} else {
				license.setFeeTypeStr(CNC);
			}
		} else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.WATERWORKSLICENSE)) {
			for (final FeeMatrix fee : feeList) {
				if (fee.getFeeType().getName().equalsIgnoreCase(PFA)) {
					isPFA = true;
				}
				totalFee = totalFee.add(fee.getAmount());
			}
			if (isPFA) {
				license.setFeeTypeStr(PFA);
			} else {
				license.setFeeTypeStr(CNC);
			}
		} else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.PWDCONTRACTORLICENSE)) {
			for (final FeeMatrix fee : feeList) {
				
				totalFee = totalFee.add(fee.getAmount());
			}
				license.setFeeTypeStr(CNC);
			} else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.VETERINARYLICENSE)) {
			for (final FeeMatrix fee : feeList) {

				totalFee = totalFee.add(fee.getAmount());
			}

			license.setFeeTypeStr(CNC);
		} else {
			LOGGER.warn("Fee Type prefix is not set");
		}
		return totalFee;
	}
	
	public void setFeeType(List<FeeMatrix> feeList, License license) {
		boolean isPFA = false;

		if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.TRADELICENSE)) {
			for (final FeeMatrix fee : feeList) {
				if (fee.getFeeType().getName().equalsIgnoreCase(PFA)) {
					isPFA = true;
				}
			}
			if (isPFA) {
				license.setFeeTypeStr(PFA);
			} else {
				license.setFeeTypeStr(CNC);
			}
		} else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.HOSPITALLICENSE)) {
			license.setFeeTypeStr(HOM);
		} else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.HAWKERLICENSE)) {
			for (final FeeMatrix fee : feeList) {
				if (fee.getFeeType().getName().equalsIgnoreCase(PFA)) {
					isPFA = true;
				}
			}
			if (isPFA) {
				license.setFeeTypeStr(PFA);
			} else {
				license.setFeeTypeStr(CNC);
			}
		} else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.WATERWORKSLICENSE)) {
			for (final FeeMatrix fee : feeList) {
				if (fee.getFeeType().getName().equalsIgnoreCase(PFA)) {
					isPFA = true;
				}
			}
			if (isPFA) {
				license.setFeeTypeStr(PFA);
			} else {
				license.setFeeTypeStr(CNC);
			}
		} else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.PWDCONTRACTORLICENSE)) {

			license.setFeeTypeStr(CNC);
		} else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.VETERINARYLICENSE)) {

			license.setFeeTypeStr(CNC);
		} else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.ELECTRICALCONTRACTORLICENSE)) {

			license.setFeeTypeStr(CNC);
		} else {
			LOGGER.warn("Fee Type prefix is not set");
		}
	}
}
