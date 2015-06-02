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
package org.egov.ptis.client.util;

import static org.egov.ptis.constants.PropertyTaxConstants.BILLGEN_SEQNAME_PREFIX;
import static org.egov.ptis.constants.PropertyTaxConstants.MANUAL_BILLGEN_SEQNAME_PREFIX;
import static org.egov.ptis.constants.PropertyTaxConstants.UNIT_IDENTIFIER_SEQ_STR;

import java.util.Calendar;
import java.util.Date;

import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infstr.utils.SequenceNumberGenerator;
import org.egov.infstr.utils.StringUtils;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class PropertyTaxNumberGenerator {
	private SequenceNumberGenerator sequenceNumberGenerator;
	@Autowired
	@Qualifier(value = "moduleDAO")
	private ModuleService moduleDao;
	@Autowired
	private InstallmentDao installmentDao;

	public String generateNoticeNumber(String noticeType) {
		StringBuffer noticeNo = new StringBuffer();
		String objString = "";
		int year = Calendar.getInstance().get(Calendar.YEAR);
		if (PropertyTaxConstants.NOTICE127.equalsIgnoreCase(noticeType)) {
			objString = PropertyTaxConstants.NOTICE127_SEQ_STR;
			noticeNo.append(PropertyTaxConstants.NOTICE127_NOTICENO_PREFIX);
		} else if (PropertyTaxConstants.NOTICE134.equalsIgnoreCase(noticeType)) {
			objString = PropertyTaxConstants.NOTICE134_SEQ_STR;
			noticeNo.append(PropertyTaxConstants.NOTICE134_NOTICENO_PREFIX);
		} else if (PropertyTaxConstants.NOTICE125.equalsIgnoreCase(noticeType)) {
			objString = PropertyTaxConstants.NOTICE125_SEQ_STR;
			noticeNo.append(PropertyTaxConstants.NOTICE125_NOTICENO_PREFIX);
		} else if (PropertyTaxConstants.NOTICE_PRATIVRUTTA
				.equalsIgnoreCase(noticeType)) {
			objString = PropertyTaxConstants.PRATIVRUTTA_SEQ_STR;
			noticeNo.append(PropertyTaxConstants.PRATIVRUTTA_NOTICENO_PREFIX);
		}
		String index = sequenceNumberGenerator.getNextNumberWithFormat(
				objString.toUpperCase(), 8, '0', Long.valueOf(1))
				.getFormattedNumber();
		noticeNo.append(index);
		noticeNo.append("/");

		// might be required for 127,134 as well
		if (PropertyTaxConstants.NOTICE_PRATIVRUTTA.equalsIgnoreCase(noticeType)) {
			noticeNo.append(year);
		}

		return noticeNo.toString();
	}

	public String generateBillNumber(String wardNo) {
		StringBuffer billNo = new StringBuffer();
		Module module = moduleDao
				.getModuleByName(PropertyTaxConstants.PTMODULENAME);
		Installment finYear = installmentDao
				.getInsatllmentByModuleForGivenDate(module, new Date());
		String index = sequenceNumberGenerator.getNextNumberWithFormat(
				BILLGEN_SEQNAME_PREFIX + wardNo, 7, '0', Long.valueOf(1))
				.getFormattedNumber();
		billNo.append(wardNo);
		billNo.append("/");
		billNo.append(index);
		billNo.append("/");
		billNo.append(finYear.getDescription());
		return billNo.toString();
	}

	public String generateManualBillNumber(PropertyID propertyID) {
		StringBuilder billNo = new StringBuilder();

		String zoneNo = propertyID.getZone().getBoundaryNum().toString();
		String wardNo = propertyID.getWard().getBoundaryNum().toString();

		zoneNo = org.apache.commons.lang.StringUtils.leftPad(zoneNo, 2, '0');
		wardNo = org.apache.commons.lang.StringUtils.leftPad(wardNo, 3, '0');

		String index = sequenceNumberGenerator
				.getNextNumberWithFormat(
						MANUAL_BILLGEN_SEQNAME_PREFIX + wardNo, 6, '0',
						Long.valueOf(1)).getFormattedNumber();

		billNo.append(zoneNo).append("/").append(wardNo).append("/")
				.append(index);
		return billNo.toString();
	}

	public String generateRecoveryNotice(String noticeType) {
		StringBuffer noticeNo = new StringBuffer();
		String objString = "";
		if (PropertyTaxConstants.NOTICE155_SEQ_STR.equalsIgnoreCase(noticeType)) {
			objString = PropertyTaxConstants.NOTICE155_SEQ_STR;
			noticeNo.append(PropertyTaxConstants.NOTICE155_NOTICENO_PREFIX);
		} else if (PropertyTaxConstants.NOTICE156_SEQ_STR
				.equalsIgnoreCase(noticeType)) {
			objString = PropertyTaxConstants.NOTICE156_SEQ_STR;
			noticeNo.append(PropertyTaxConstants.NOTICE156_NOTICENO_PREFIX);
		} else if (PropertyTaxConstants.NOTICE159_SEQ_STR
				.equalsIgnoreCase(noticeType)) {
			objString = PropertyTaxConstants.NOTICE159_SEQ_STR;
			noticeNo.append(PropertyTaxConstants.NOTICE159_NOTICENO_PREFIX);
		} else if (PropertyTaxConstants.WARRANT_APPLICATION
				.equalsIgnoreCase(noticeType)) {
			String index = sequenceNumberGenerator.getNextNumberWithFormat(
					PropertyTaxConstants.WARRANT_APPLICATION.toUpperCase(), 5, '0',
					Long.valueOf(1)).getFormattedNumber();
			noticeNo.append(index);
			return noticeNo.toString();
		}
		String index = sequenceNumberGenerator.getNextNumberWithFormat(
				objString.toUpperCase(), 5, '0', Long.valueOf(1))
				.getFormattedNumber();
		noticeNo.append(index);
		noticeNo.append("/");
		Module module = moduleDao
				.getModuleByName(PropertyTaxConstants.PTMODULENAME);
		Installment finYear = installmentDao
				.getInsatllmentByModuleForGivenDate(module, new Date());
		noticeNo.append(finYear.getDescription());
		return noticeNo.toString();
	}

	public String generateIndexNumber(String wardNum) {

		StringBuffer indexNum = new StringBuffer();
		try {
			wardNum = org.apache.commons.lang.StringUtils.leftPad(wardNum, 3, "0");
			indexNum.append(wardNum);
			Long index = sequenceNumberGenerator.getNextNumber(
					"IDIV-" + wardNum).getNumber();
			indexNum.append(org.apache.commons.lang.StringUtils.leftPad(index.toString(), 6, "0"));
		} catch (Exception e) {
			throw new EGOVRuntimeException("Exception : " + e.getMessage(), e);
		}

		return indexNum.toString();
	}

	public String getObjectionNumber() {
		String type = PropertyTaxConstants.OBJECTION_SEQ_STR;
		return type
				+ "/"
				+ org.apache.commons.lang.StringUtils.leftPad(
						sequenceNumberGenerator.getNextNumber(type, 1)
								.getFormattedNumber(), 8, "0");
	}

	public String getHearingNumber(Boundary zoneBoundary) {
		StringBuffer hearingNum = new StringBuffer();
		String type = PropertyTaxConstants.HEARINGNO_SEQ_STR;
		return hearingNum
				.append(org.apache.commons.lang.StringUtils.leftPad(sequenceNumberGenerator
						.getNextNumber(type, 1).getFormattedNumber(), 3, "0"))
				.append("/")
				.append(type)
				.append("/")
				.append(zoneBoundary != null ? zoneBoundary.getName() : "")
				.append("-")
				.append(zoneBoundary != null ? zoneBoundary.getBoundaryNum()
						.toString() : "").toString();

	}

	public String generateNameTransApplNo(Boundary wardBndry) {
		StringBuffer applNum = new StringBuffer();
		applNum.append(wardBndry.getBoundaryNum().toString());
		Long index = sequenceNumberGenerator.getNextNumber(
				PropertyTaxConstants.MUTATIONAPPLNO_SEQ_STR).getNumber();
		applNum.append(org.apache.commons.lang.StringUtils.leftPad(index.toString(), 6, "0"));
		return applNum.toString();
	}

	public String generateMemoNumber() {

		String memoNumber = "";
		String type = PropertyTaxConstants.MEMONO_SEQ_STR;
		return memoNumber
				+ (StringUtils.leftPad(
						sequenceNumberGenerator.getNextNumber(type, 1)
								.getFormattedNumber(), 5, "0"));

	}

	public String getRejectionLetterSerialNum() {
		String type = PropertyTaxConstants.REJECTION_SEQ_STR;
		return sequenceNumberGenerator.getNextNumber(type, 1)
				.getFormattedNumber();
	}

	public String generateUnitIdentifierPrefix() {
		return sequenceNumberGenerator
				.getNextNumber(UNIT_IDENTIFIER_SEQ_STR, 1).getFormattedNumber();
	}

	public void setSequenceNumberGenerator(
			SequenceNumberGenerator sequenceNumberGenerator) {
		this.sequenceNumberGenerator = sequenceNumberGenerator;
	}

}
