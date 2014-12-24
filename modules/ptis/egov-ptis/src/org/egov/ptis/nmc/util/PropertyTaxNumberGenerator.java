package org.egov.ptis.nmc.util;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.BILLGEN_SEQNAME_PREFIX;

import java.util.Calendar;
import java.util.Date;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Installment;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.commons.dao.InstallmentDao;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.utils.SequenceNumberGenerator;
import org.egov.infstr.utils.StringUtils;
import org.egov.lib.admbndry.Boundary;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.nmc.constants.NMCPTISConstants;

public class PropertyTaxNumberGenerator {
	private SequenceNumberGenerator sequenceNumberGenerator;

	public String generateNoticeNumber(String noticeType) {
		StringBuffer noticeNo = new StringBuffer();
		String objString = "";
		int year = Calendar.getInstance().get(Calendar.YEAR);
		if (NMCPTISConstants.NOTICE127.equalsIgnoreCase(noticeType)) {
			objString = NMCPTISConstants.NOTICE127_SEQ_STR;
			noticeNo.append(NMCPTISConstants.NOTICE127_NOTICENO_PREFIX);
		} else if (NMCPTISConstants.NOTICE134.equalsIgnoreCase(noticeType)) {
			objString = NMCPTISConstants.NOTICE134_SEQ_STR;
			noticeNo.append(NMCPTISConstants.NOTICE134_NOTICENO_PREFIX);
		} else if (NMCPTISConstants.NOTICE125.equalsIgnoreCase(noticeType)) {
			objString = NMCPTISConstants.NOTICE125_SEQ_STR;
			noticeNo.append(NMCPTISConstants.NOTICE125_NOTICENO_PREFIX);
		} else if (NMCPTISConstants.NOTICE_PRATIVRUTTA.equalsIgnoreCase(noticeType)) {
			objString = NMCPTISConstants.PRATIVRUTTA_SEQ_STR;
			noticeNo.append(NMCPTISConstants.PRATIVRUTTA_NOTICENO_PREFIX);
		}
		String index = sequenceNumberGenerator
				.getNextNumberWithFormat(objString.toUpperCase(), 8, '0', Long.valueOf(1)).getFormattedNumber();
		noticeNo.append(index);
		noticeNo.append("/");

		// might be required for 127,134 as well
		if (NMCPTISConstants.NOTICE_PRATIVRUTTA.equalsIgnoreCase(noticeType)) {
			noticeNo.append(year);
		}

		return noticeNo.toString();
	}

	public String generateBillNumber(String wardNo) {
		StringBuffer billNo = new StringBuffer();
		ModuleDao moduleDao = GenericDaoFactory.getDAOFactory().getModuleDao();
		Module module = moduleDao.getModuleByName(PropertyTaxConstants.PTMODULENAME);
		InstallmentDao instalDao = CommonsDaoFactory.getDAOFactory().getInstallmentDao();
		Installment finYear = instalDao.getInsatllmentByModuleForGivenDate(module, new Date());
		String index = sequenceNumberGenerator.getNextNumberWithFormat(BILLGEN_SEQNAME_PREFIX + wardNo, 7, '0',
				Long.valueOf(1)).getFormattedNumber();
		billNo.append(wardNo);
		billNo.append("/");
		billNo.append(index);
		billNo.append("/");
		billNo.append(finYear.getDescription());
		return billNo.toString();
	}

	public String generateRecoveryNotice(String noticeType) {
		StringBuffer noticeNo = new StringBuffer();
		String objString = "";
		if (NMCPTISConstants.NOTICE155_SEQ_STR.equalsIgnoreCase(noticeType)) {
			objString = NMCPTISConstants.NOTICE155_SEQ_STR;
			noticeNo.append(NMCPTISConstants.NOTICE155_NOTICENO_PREFIX);
		} else if (NMCPTISConstants.NOTICE156_SEQ_STR.equalsIgnoreCase(noticeType)) {
			objString = NMCPTISConstants.NOTICE156_SEQ_STR;
			noticeNo.append(NMCPTISConstants.NOTICE156_NOTICENO_PREFIX);
		} else if (NMCPTISConstants.NOTICE159_SEQ_STR.equalsIgnoreCase(noticeType)) {
			objString = NMCPTISConstants.NOTICE159_SEQ_STR;
			noticeNo.append(NMCPTISConstants.NOTICE159_NOTICENO_PREFIX);
		} else if (NMCPTISConstants.WARRANT_APPLICATION.equalsIgnoreCase(noticeType)) {
			String index = sequenceNumberGenerator.getNextNumberWithFormat(
					NMCPTISConstants.WARRANT_APPLICATION.toUpperCase(), 5, '0', Long.valueOf(1)).getFormattedNumber();
			noticeNo.append(index);
			return noticeNo.toString();
		}
		String index = sequenceNumberGenerator
				.getNextNumberWithFormat(objString.toUpperCase(), 5, '0', Long.valueOf(1)).getFormattedNumber();
		noticeNo.append(index);
		noticeNo.append("/");
		ModuleDao moduleDao = GenericDaoFactory.getDAOFactory().getModuleDao();
		Module module = moduleDao.getModuleByName(PropertyTaxConstants.PTMODULENAME);
		InstallmentDao instalDao = CommonsDaoFactory.getDAOFactory().getInstallmentDao();
		Installment finYear = instalDao.getInsatllmentByModuleForGivenDate(module, new Date());
		noticeNo.append(finYear.getDescription());
		return noticeNo.toString();
	}

	public String generateIndexNumber(String wardNum) {

		StringBuffer indexNum = new StringBuffer();
		try {
			wardNum = StringUtils.leftPad(wardNum, 3, "0");
			indexNum.append(wardNum);
			Long index = sequenceNumberGenerator.getNextNumber("IDIV-" + wardNum).getNumber();
			indexNum.append(StringUtils.leftPad(index.toString(), 6, "0"));
		} catch (Exception e) {
			throw new EGOVRuntimeException("Exception : " + e.getMessage(), e);
		}

		return indexNum.toString();
	}

	public String getObjectionNumber() {
		String type = NMCPTISConstants.OBJECTION_SEQ_STR;
		return type + "/"
				+ StringUtils.leftPad(sequenceNumberGenerator.getNextNumber(type, 1).getFormattedNumber(), 8, "0");
	}

	public String getHearingNumber(Boundary zoneBoundary) {
		StringBuffer hearingNum = new StringBuffer();
		String type = NMCPTISConstants.HEARINGNO_SEQ_STR;
		return hearingNum
				.append(StringUtils
						.leftPad(sequenceNumberGenerator.getNextNumber(type, 1).getFormattedNumber(), 3, "0"))
				.append("/").append(type).append("/").append(zoneBoundary != null ? zoneBoundary.getName() : "")
				.append("-").append(zoneBoundary != null ? zoneBoundary.getBoundaryNum().toString() : "").toString();

	}

	public String generateNameTransApplNo(Boundary wardBndry) {
		StringBuffer applNum = new StringBuffer();
		applNum.append(wardBndry.getBoundaryNum().toString());
		Long index = sequenceNumberGenerator.getNextNumber(NMCPTISConstants.MUTATIONAPPLNO_SEQ_STR).getNumber();
		applNum.append(StringUtils.leftPad(index.toString(), 6, "0"));
		return applNum.toString();
	}

	public String generateMemoNumber() {

		String memoNumber = "";
		String type = NMCPTISConstants.MEMONO_SEQ_STR;
		return memoNumber
				+ (StringUtils.leftPad(sequenceNumberGenerator.getNextNumber(type, 1).getFormattedNumber(), 5, "0"));

	}

	public String getRejectionLetterSerialNum() {
		String type = NMCPTISConstants.REJECTION_SEQ_STR;
		return sequenceNumberGenerator.getNextNumber(type, 1).getFormattedNumber();
	}

	public void setSequenceNumberGenerator(SequenceNumberGenerator sequenceNumberGenerator) {
		this.sequenceNumberGenerator = sequenceNumberGenerator;
	}

}
