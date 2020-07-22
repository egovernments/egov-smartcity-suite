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
package org.egov.services.report;

import java.util.List;

import org.egov.commons.CVoucherHeader;
import org.egov.deduction.model.EgRemittance;
import org.egov.egf.model.BillRegisterReportBean;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.bills.Miscbilldetail;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.model.payment.Paymentheader;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 
 * @author subhash
 *
 */
@SuppressWarnings("deprecation")
@Service
public class BillRegisterReportService {

    @SuppressWarnings({ "rawtypes" })
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @SuppressWarnings({ "unchecked" })
    @ReadOnly
    public List<Miscbilldetail> getMiscBillDetailsByBillVoucher(final BillRegisterReportBean billRegReport) {

        return persistenceService.getSession()
                .createQuery(" from Miscbilldetail mis where mis.billnumber=:billNumber " +
                        " and mis.billVoucherHeader.voucherNumber=:voucherNumber")
                .setParameter("billNumber", billRegReport.getBillNumber())
                .setParameter("voucherNumber", billRegReport.getVoucherNumber()).list();
    }

    @ReadOnly
    public Paymentheader getPaymentHeaderByPaymentVoucher(final CVoucherHeader cVoucherHeader) {
        return (Paymentheader) persistenceService.getSession()
                .createQuery("from Paymentheader where voucherheader=:voucherHeader")
                .setParameter("voucherHeader", cVoucherHeader).uniqueResult();
    }

    @SuppressWarnings({ "unchecked" })
    @ReadOnly
    public List<InstrumentVoucher> getInstrumentVouchersByVoucherHeader(final Long voucherHeader,
            List<Integer> cancelledChequeStatus) {
        return persistenceService.getSession().createQuery(
                "from InstrumentVoucher iv where iv.voucherHeaderId.id=:vhId and" +
                        " iv.instrumentHeaderId.statusId.id not in(:cancelledChequeList)")
                .setLong("vhId", voucherHeader)
                .setParameterList("cancelledChequeList", cancelledChequeStatus).list();
    }

    @SuppressWarnings({ "unchecked" })
    @ReadOnly
    public List<EgRemittance> getRemittancePaymentByVoucher(final String voucherNumber) {
        return persistenceService.getSession()
                .createQuery("select distinct rm from EgRemittance rm join rm.egRemittanceDetail rdtl "
                        + "where rdtl.egRemittanceGldtl.generalledgerdetail.generalLedgerId.voucherHeaderId.voucherNumber =:voucherNumber "
                        + "and rdtl.egRemittanceGldtl.generalledgerdetail.generalLedgerId.voucherHeaderId.status!=:voucherStatus "
                        + "and rm.voucherheader.status!=:voucherStatus order by rm.voucherheader.id")
                .setParameter("voucherNumber", voucherNumber)
                .setParameter("voucherStatus", FinancialConstants.CANCELLEDVOUCHERSTATUS)
                .list();
    }

}
