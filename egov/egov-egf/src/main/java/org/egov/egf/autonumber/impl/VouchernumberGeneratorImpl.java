package org.egov.egf.autonumber.impl;

import java.io.Serializable;

import org.egov.commons.CFinancialYear;
import org.egov.commons.CFiscalPeriod;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.dao.FiscalPeriodHibernateDAO;
import org.egov.egf.autonumber.VouchernumberGenerator;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.utils.ApplicationSequenceNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class VouchernumberGeneratorImpl implements VouchernumberGenerator {

	@Autowired
	private FiscalPeriodHibernateDAO fiscalPeriodHibernateDAO;
	@Autowired
	private ApplicationSequenceNumberGenerator applicationSequenceNumberGenerator;
	/**
	 * 
	 * Format fundcode/vouchertype/seqnumber/month/financialyear but 
	 * sequence is running number for a year
	 *
	 */
	public String getNextNumber(CVoucherHeader vh) 
	{
		String voucherNumber="";

		String sequenceName="";

		final CFiscalPeriod fiscalPeriod = fiscalPeriodHibernateDAO.getFiscalPeriodByDate(vh.getVoucherDate());
		if(fiscalPeriod==null)
			throw new ApplicationRuntimeException("Fiscal period is not defined for the voucher date");
		sequenceName="sq_"+vh.getFundId().getIdentifier()+"_"+vh.getVoucherNumberPrefix()+"_"+fiscalPeriod.getName();  
		Serializable nextSequence = applicationSequenceNumberGenerator.getNextSequence(sequenceName);

		voucherNumber=	String.format("%s/%s/%08d/%02d/%s", vh.getFundId().getIdentifier(), vh.getVoucherNumberPrefix(),
				nextSequence,vh.getVoucherDate().getMonth()+1,fiscalPeriod.getcFinancialYear().getFinYearRange());

		return voucherNumber;
	}
}
