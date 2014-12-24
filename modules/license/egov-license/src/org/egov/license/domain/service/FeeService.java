/*
 * @(#)FeeService.java 3.0, 29 Jul, 2013 1:24:26 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.services.PersistenceService;
import org.egov.license.domain.entity.FeeMatrix;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.entity.LicenseAppType;
import org.egov.license.domain.entity.NatureOfBusiness;
import org.egov.license.domain.entity.SubCategory;
import org.egov.license.utils.Constants;

public class FeeService extends PersistenceService<FeeMatrix, Long> {
	public static final String FEE_BY_NAME_TYPE_NATURE = "FEE_BY_NAME_TYPE_NATURE";
	public static final String CNC = "CNC";
	public static final String PFA = "PFA";
	public static final Logger LOGGER = Logger.getLogger(FeeService.class);
	public static final String HOM = "HOM";

	@SuppressWarnings("unchecked")
	public List<FeeMatrix> getFeeList(final SubCategory tradeName, final LicenseAppType appType, final NatureOfBusiness natureOfBusiness) {
		LOGGER.debug("tradeName:::" + tradeName);

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
	public BigDecimal calculateFee(final License license, final SubCategory tradeName, final LicenseAppType appType, final NatureOfBusiness natureOfBusiness, final BigDecimal otherCharges, final BigDecimal deduction) {
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

	public BigDecimal calculateFeeForExisting(final License license, final SubCategory tradeName, final LicenseAppType appType, final NatureOfBusiness natureOfBusiness, final BigDecimal otherCharges, final BigDecimal deduction) {
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

	public void setFeeType(final List<FeeMatrix> feeList, final License license) {
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
