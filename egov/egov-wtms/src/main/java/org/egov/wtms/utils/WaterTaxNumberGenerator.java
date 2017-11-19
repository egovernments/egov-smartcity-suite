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
package org.egov.wtms.utils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.wtms.application.service.WaterConnectionService;
import org.egov.wtms.autonumber.ConsumerNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WaterTaxNumberGenerator {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private WaterConnectionService waterConnectionService;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    @Transactional
    public String getNextConsumerNumber() {
        Boolean cosumerCodeExists = true;
        String consumerCode = null;
        final ConsumerNumberGenerator consumerGen = beanResolver.getAutoNumberServiceFor(ConsumerNumberGenerator.class);

        while (cosumerCodeExists) {
            consumerCode = consumerGen.generateConsumerNumber();
            if (waterConnectionService.findByConsumerCode(consumerCode) != null) {
                cosumerCodeExists = true;
                continue;
            } else {
                cosumerCodeExists = false;
                break;
            }

        }
        return consumerCode;
    }

    /*
     * @Transactional public String generateConsumerNumber() { try { final
     * String sequenceName = CONSUMER_NUMBER_SEQ_PREFIX; Serializable
     * sequenceNumber; try { sequenceNumber =
     * sequenceNumberGenerator.getNextSequence(sequenceName); } catch (final
     * SQLGrammarException e) { sequenceNumber =
     * dbSequenceGenerator.createAndGetNextSequence(sequenceName); } return
     * String.format("%s%06d", waterTaxUtils.getCityCode(), sequenceNumber); }
     * catch (final SQLException e) { throw new ApplicationRuntimeException(
     * "Error occurred while generating Consumer Number", e); } }
     */
    /*
     * @Transactional public String generateWorkOrderNumber() { try { final
     * String sequenceName = WORKORDER_NUMBER_SEQ_PREFIX; Serializable
     * sequenceNumber; try { sequenceNumber =
     * sequenceNumberGenerator.getNextSequence(sequenceName); } catch (final
     * SQLGrammarException e) { sequenceNumber =
     * dbSequenceGenerator.createAndGetNextSequence(sequenceName); } return
     * String.format("%s%06d", "", sequenceNumber); } catch (final SQLException
     * e) { throw new ApplicationRuntimeException(
     * "Error occurred while generating Consumer Number", e); } }
     */

    /*
     * @Transactional public String generateMeterDemandNoticeNumber() { try {
     * final String sequenceName = METERDEMANDNOTICE_NUMBER_SEQ_PREFIX;
     * Serializable sequenceNumber; try { sequenceNumber =
     * sequenceNumberGenerator.getNextSequence(sequenceName); } catch (final
     * SQLGrammarException e) { sequenceNumber =
     * dbSequenceGenerator.createAndGetNextSequence(sequenceName); } return
     * String.format("%s%06d", "", sequenceNumber); } catch (final SQLException
     * e) { throw new ApplicationRuntimeException(
     * "Error occurred while generating meter Generate Number", e); } }
     */

    /*
     * public String generateBillNumber(final String installmentYear) { try {
     * final String sequenceName = WaterTaxConstants.WATER_CONN_BILLNO_SEQ +
     * installmentYear; Serializable sequenceNumber; try { sequenceNumber =
     * sequenceNumberGenerator.getNextSequence(sequenceName); } catch (final
     * SQLGrammarException e) { sequenceNumber =
     * dbSequenceGenerator.createAndGetNextSequence(sequenceName); } return
     * String.format("%s%06d", "", sequenceNumber); } catch (final SQLException
     * e) { throw new ApplicationRuntimeException(
     * "Error occurred while generating water connection charges bill Number ",
     * e); } }
     */
}