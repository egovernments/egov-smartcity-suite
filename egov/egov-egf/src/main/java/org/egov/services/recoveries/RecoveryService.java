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
package org.egov.services.recoveries;

import org.apache.log4j.Logger;
import org.egov.commons.EgPartytype;
import org.egov.commons.EgwTypeOfWork;
import org.egov.dao.recoveries.EgDeductionDetailsHibernateDAO;
import org.egov.dao.recoveries.TdsHibernateDAO;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.recoveries.EgDeductionDetails;
import org.egov.model.recoveries.Recovery;
import org.egov.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service("recoveryPersistenceService")
@Transactional(readOnly = true)
public class RecoveryService extends PersistenceService<Recovery, Long> {

    private static final Logger LOGGER = Logger.getLogger(RecoveryService.class);

    @Autowired
    private EgDeductionDetailsHibernateDAO egDeductionDetHibernateDao;
    @Autowired
    private TdsHibernateDAO tdsHibernateDAO;

    public RecoveryService() {
        super(Recovery.class);
    }

    public RecoveryService(Class<Recovery> type) {
        super(type);
    }
    
    public Recovery getTdsById(final Long tdsId)
    {
        return tdsHibernateDAO.findById(tdsId, false);
    }

    public List<Recovery> findByEstDate(final String estimateDate) throws ApplicationRuntimeException
    {
        try {
            return tdsHibernateDAO.findByEstDate(estimateDate);
        } catch (final Exception e) {
            //
            // EgovUtils.rollBackTransaction();
            throw new ApplicationRuntimeException("Exception in searching Tds by estimate Date" + e.getMessage(), e);
        }
    }

    public Recovery getTdsByType(final String type)
    {
        return tdsHibernateDAO.getTdsByType(type);
    }

    public List<Recovery> getAllTdsByPartyType(final String partyType) {
        return tdsHibernateDAO.getAllTdsByPartyType(partyType);
    }

    public List getAllTds()
    {
        return tdsHibernateDAO.getAllTds();
    }

    public void createTds(final Recovery tds)
    {
        tdsHibernateDAO.create(tds);
    }

    public void updateTds(final Recovery tds)
    {
        tdsHibernateDAO.update(tds);
    }

    @Override
    public Recovery findById(final Long id)
    {
        return tdsHibernateDAO.findById(id, false);
    }

    public EgDeductionDetails getEgDeductionDetailsById(final Integer deductionId)
    {
        return (EgDeductionDetails) egDeductionDetHibernateDao.findById(deductionId, false);
    }

    public void createEgDeductionDetails(final EgDeductionDetails egDeductionDetails)
    {
        egDeductionDetHibernateDao.create(egDeductionDetails);
    }

    public void updateEgDeductionDetails(final EgDeductionDetails egDeductionDetails)
    {
        egDeductionDetHibernateDao.update(egDeductionDetails);
    }

    public void deleteEgDeductionDetails(final EgDeductionDetails egDeductionDetails)
    {
        try
        {
            egDeductionDetHibernateDao.delete(egDeductionDetails);
        } catch (final Exception e)
        {
            // EgovUtils.rollBackTransaction();
            throw new ApplicationRuntimeException("Exception in Deleting EgDeductionDetails." + e.getMessage(), e);
        }
    }

    public List<EgDeductionDetails> findByTds(final Recovery tds)
    {
        return egDeductionDetHibernateDao.findByTds(tds);
    }

    public List<Recovery> getAllActiveTds()
    {
        return tdsHibernateDAO.getAllActiveTds();
    }

    public List<Recovery> getAllActiveAutoRemitTds()
    {
        return tdsHibernateDAO.getAllActiveAutoRemitTds();
    }

    public List<Recovery> getActiveTdsFilterBy(final String estimateDate, final BigDecimal estCost,
            final EgPartytype egPartytype,
            final EgwTypeOfWork egwTypeOfWork, final EgwTypeOfWork egwSubTypeOfWork)
            {
        return tdsHibernateDAO.getActiveTdsFilterBy(estimateDate, estCost, egPartytype, egwTypeOfWork, egwSubTypeOfWork);
            }

    public List<EgDeductionDetails> getEgDeductionDetailsFilterBy(final Recovery tds, final BigDecimal amount, final String date,
            final EgwTypeOfWork egwTypeOfWork, final EgwTypeOfWork egwSubTypeOfWork)
            {
        return egDeductionDetHibernateDao.getEgDeductionDetailsFilterBy(tds, amount, date, egwTypeOfWork, egwSubTypeOfWork);
            }

    public List<Recovery> recoveryForPartyContractor(final Date asOndate) throws ValidationException
    {
        return tdsHibernateDAO.recoveryForPartyContractor(asOndate);
    }

    public void setEgDeductionDetHibernateDao(
            final EgDeductionDetailsHibernateDAO egDeductionDetHibernateDao) {
        this.egDeductionDetHibernateDao = egDeductionDetHibernateDao;
    }

    public void setTdsHibernateDAO(final TdsHibernateDAO tdsHibernateDAO) {
        this.tdsHibernateDAO = tdsHibernateDAO;
    }

    public EgPartytype getPartytypeByCode(final String code) {
        return tdsHibernateDAO.getPartytypeByCode(code);
    }

    public EgwTypeOfWork getTypeOfWorkByCode(final String code) {
        return tdsHibernateDAO.getTypeOfWorkByCode(code);
    }

    public EgPartytype getSubPartytypeByCode(final String code) {
        return tdsHibernateDAO.getSubPartytypeByCode(code);
    }

    public Recovery getTdsByTypeAndPartyType(final String type, final EgPartytype egPartytype) {
        return tdsHibernateDAO.getTdsByTypeAndPartyType(type, egPartytype);
    }

    public BigDecimal getDeductionAmount(final String recoveryCode, final String partyType, final String subPartyType,
            final String docType,
            final BigDecimal grossAmount, final Date asOnDate) throws Exception {

        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Constants.LOCALE);
        BigDecimal incomeTax = new BigDecimal(0);
        BigDecimal surcharge = new BigDecimal(0);
        BigDecimal education = new BigDecimal(0);
        BigDecimal total = new BigDecimal(0);
        BigDecimal deductionAmt = new BigDecimal(0);
        EgDeductionDetails egDeductionDetails = null;

        if (null == recoveryCode || recoveryCode.trim().equals("")) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Recovery Code is missing");
            throw new ValidationException(EMPTY, "Recovery Code is missing");
        }

        if (null == partyType || partyType.trim().equals("")) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Party Type is missing");
            throw new ValidationException(EMPTY, "Party Type is missing");
        }

        if (null == grossAmount) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Gross Amount is missing");
            throw new ValidationException(EMPTY, "Gross Amount is missing");
        }

        if (null == asOnDate) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("AsOnDate is missing");
            throw new ValidationException(EMPTY, "AsOnDate is missing");
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getDeductionAmount() -> recoveryCode :" + recoveryCode + " | partyType :" + partyType
                    + " | grossAmount :" + grossAmount + " | asOnDate :" + dateFormatter.format(asOnDate)
                    + " | docType :" + docType);

        EgwTypeOfWork egwTypeOfWork = null;
        EgPartytype egSubPartytype = null;

        final EgPartytype egPartytype = getPartytypeByCode(partyType);
        final Recovery recovery = getTdsByTypeAndPartyType(recoveryCode, egPartytype);

        if (recovery == null)
            throw new ValidationException(EMPTY, "Recovery with " + recoveryCode + " code  and " + egPartytype
                    + " party type is invalid.");
        if (recovery.getRecoveryMode() == 'M')
            return BigDecimal.valueOf(-1);

        if (null != docType)
            egwTypeOfWork = getTypeOfWorkByCode(docType);

        if (null != subPartyType)
            egSubPartytype = getSubPartytypeByCode(subPartyType);

        try {
            egDeductionDetails = egDeductionDetHibernateDao.findEgDeductionDetailsForDeduAmt(recovery,
                    egPartytype, egSubPartytype, egwTypeOfWork, asOnDate);
        } catch (final Exception e) {
            LOGGER.error("Exception in egDeductionDetails fetching :" + e);
            throw new ValidationException(EMPTY, "Error while fetching the date for this " + recoveryCode
                    + " code for this " + dateFormatter.format(asOnDate) + " date. " + e.getMessage());
        }

        if (null == egDeductionDetails)
            throw new ValidationException(EMPTY, "There is no data for this " + recoveryCode + " code for this "
                    + dateFormatter.format(asOnDate) + " date.");

        if (null != recovery.getCalculationType() && recovery.getCalculationType().equalsIgnoreCase("flat")) {
            if (null != egDeductionDetails.getFlatAmount())
                deductionAmt = egDeductionDetails.getFlatAmount();
        } else {
            if (null != egDeductionDetails.getIncometax())
                incomeTax = egDeductionDetails.getIncometax();
            if (null != egDeductionDetails.getSurcharge())
                surcharge = egDeductionDetails.getSurcharge();
            if (null != egDeductionDetails.getEducation())
                education = egDeductionDetails.getEducation();
            total = incomeTax.add(surcharge).add(education);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("total IT/SC/EC " + total);
            deductionAmt = grossAmount.multiply(total.divide(new BigDecimal(100)));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("deductionAmt :" + deductionAmt);
        return deductionAmt = deductionAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
