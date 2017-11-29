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
package org.egov.billsaccounting.services;


import org.apache.log4j.Logger;
import org.egov.commons.CVoucherHeader;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.voucher.PreApprovedVoucher;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Manikanta
 *
 */

@Transactional(readOnly = true)
public class BillsAccountingService {

    private final static Logger LOGGER = Logger.getLogger(BillsAccountingService.class);

    private static final String MISSINGMSG = "is not defined in AppConfig values cannot proceed creating voucher";

   
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private CreateVoucher createVoucher;
    /**
     * API to create voucher in pre approved status
     * @param billId
     * @return
     */
    @Transactional
    public long createPreApprovedVoucherFromBill(final int billId, final String voucherNumber, final Date voucherDate)
            throws ApplicationRuntimeException, ValidationException
    {
        String voucherStatus = null;
        long vh = -1;
        try {
            final List vStatusList = appConfigValuesService.getConfigValuesByModuleAndKey("EGF", "PREAPPROVEDVOUCHERSTATUS");

            if (!vStatusList.isEmpty() && vStatusList.size() == 1)
            {
                final AppConfigValues appVal = (AppConfigValues) vStatusList.get(0);
                voucherStatus = appVal.getValue();
            } else
                throw new ApplicationRuntimeException("PREAPPROVEDVOUCHERSTATUS" + MISSINGMSG);
           vh = createVoucher.createVoucherFromBill(billId, voucherStatus, voucherNumber, voucherDate);
        } catch (final ValidationException e) {
            LOGGER.error(e.getErrors());
            throw new ValidationException(e.getErrors());
        } catch (final Exception e)
        {
            LOGGER.error(e.getMessage());
            throw new ApplicationRuntimeException(e.getMessage());

        }
        return vh;

    }

    /**
     * API to create voucher in pre approved status
     * @param billId
     * @return
     */
    @Transactional
    public long createPreApprovedVoucherFromBillForPJV(final int billId, final List<PreApprovedVoucher> voucherdetailList,
            final List<PreApprovedVoucher> subLedgerList) throws ApplicationRuntimeException
    {
        String voucherStatus = null;
        long vh = -1;
        try {
            final List vStatusList = appConfigValuesService.getConfigValuesByModuleAndKey("EGF", "PREAPPROVEDVOUCHERSTATUS");

            if (!vStatusList.isEmpty() && vStatusList.size() == 1)
            {
                final AppConfigValues appVal = (AppConfigValues) vStatusList.get(0);
                voucherStatus = appVal.getValue();
            } else
                throw new ApplicationRuntimeException("PREAPPROVEDVOUCHERSTATUS" + MISSINGMSG);
            vh = createVoucher.createVoucherFromBillForPJV(billId, voucherStatus, voucherdetailList, subLedgerList);
        } catch (final Exception e)
        {
            LOGGER.error(e.getMessage());
            throw new ApplicationRuntimeException(e.getMessage());

        }
        return vh;

    }

    /**
     * API to Change the status of preapproved voucher
     * @param vouhcerheaderid
     * @return
     */
    @Transactional
    public void createVoucherfromPreApprovedVoucher(final long vouhcerheaderid) throws ApplicationRuntimeException
    {
        String voucherStatus = null;

        try {
            final List vStatusList = appConfigValuesService.getConfigValuesByModuleAndKey("EGF", "APPROVEDVOUCHERSTATUS");
            if (!vStatusList.isEmpty() && vStatusList.size() == 1)
            {
                final AppConfigValues appVal = (AppConfigValues) vStatusList.get(0);
                voucherStatus = appVal.getValue();
            } else
                throw new ApplicationRuntimeException("APPROVEDVOUCHERSTATUS" + MISSINGMSG);

            createVoucher.createVoucherFromPreApprovedVoucher(vouhcerheaderid, voucherStatus);

        } catch (final ApplicationRuntimeException e) {

            LOGGER.error(e.getMessage());
            throw new ApplicationRuntimeException(e.getMessage());
        }

    }

    /**
     * Api to create voucher from bill with normal flow
     * @param billId
     * @return
     */
    @Transactional
    public long createVoucherFromBill(final int billId) throws ApplicationRuntimeException
    {
        try {
            String voucherStatus = null;
            final List vStatusList = appConfigValuesService.getConfigValuesByModuleAndKey("EGF", "DEFAULTVOUCHERCREATIONSTATUS");
            if (!vStatusList.isEmpty() && vStatusList.size() == 1)
            {
                final AppConfigValues appVal = (AppConfigValues) vStatusList.get(0);
                voucherStatus = appVal.getValue();
            } else
                throw new ApplicationRuntimeException("DEFAULTVOUCHERCREATIONSTATUS" + MISSINGMSG);
             final long vh = createVoucher.createVoucherFromBill(billId, voucherStatus, null, null);
            return vh;
        } catch (final Exception e)
        {
            LOGGER.error(e.getMessage());
            throw new ApplicationRuntimeException(e.getMessage());
        }

    }

    @Transactional
    public void updatePJV(final CVoucherHeader vh, final List<PreApprovedVoucher> detailList,
            final List<PreApprovedVoucher> subledgerlist)
            throws ApplicationRuntimeException
    {
        createVoucher.updatePJV(vh, detailList, subledgerlist);
    }

    /**
     * To get the PJV number for the bill number
     * @param billNumber
     * @return
     */
    @Transactional
    public CVoucherHeader getPJVNumberForBill(final String billNumber) throws ApplicationException
    {
        try
        {
            final Session session = persistenceService.getSession();
            final Query query = session
                    .createQuery("select br.egBillregistermis.voucherHeader from EgBillregister br where br.billnumber=:billNumber");
            query.setString("billNumber", billNumber);
            if (null == query.uniqueResult())
                throw new ApplicationException("PJV is not created for this bill number [" + billNumber + "]");

            return (CVoucherHeader) query.uniqueResult();
        } catch (final Exception e)
        {
            throw new ApplicationException(e.getMessage());
        }
    }
}