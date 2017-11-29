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

package org.egov.wtms.integration;

import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.wtms.ConsumerConsumption;
import org.egov.ptis.wtms.PropertyWiseConsumptions;
import org.egov.ptis.wtms.WaterChargesIntegrationService;
import org.egov.wtms.application.entity.NonMeteredConnBillDetails;
import org.egov.wtms.application.entity.WaterConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDetailService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.application.service.WaterConnectionService;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class WaterChargesIntegrationServiceImpl implements WaterChargesIntegrationService {

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;
    @Autowired
    private WaterConnectionService waterConnectionService;
    @Autowired
    private ConnectionDetailService connectionDetailService;
    @Autowired
    private InstallmentHibDao installmentDao;

    @Override
    public PropertyWiseConsumptions getPropertyWiseConsumptionsForWaterCharges(final String propertyId) {
        final PropertyWiseConsumptions propertyWiseConsumptions = new PropertyWiseConsumptions();
        propertyWiseConsumptions.setPropertyID(propertyId);
        BigDecimal currentTotal = BigDecimal.ZERO;
        BigDecimal arrearTotal = BigDecimal.ZERO;
        Installment arrInstal = null;
        final Installment currentInstallment = connectionDetailService
                .getCurrentInstallment(WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE, null, new Date());
        final List<WaterConnection> waterConnections = waterConnectionService.findByPropertyIdentifier(propertyId);
        final List<ConsumerConsumption> consumerConsumptions = new ArrayList<ConsumerConsumption>();
        for (final WaterConnection waterConnection : waterConnections) {
            final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                    .findByConnection(waterConnection);
            if (waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.ACTIVE))
                if (ConnectionType.NON_METERED.equals(waterConnectionDetails.getConnectionType())) {
                    final ConsumerConsumption consumerConsumption = new ConsumerConsumption();
                    consumerConsumption.setHscno(waterConnectionDetails.getConnection().getConsumerCode());
                    final Map<String, BigDecimal> resultmap = connectionDetailService
                            .getDemandCollMapForPtisIntegration(waterConnectionDetails,
                                    WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE, null);
                    if (null != resultmap && !resultmap.isEmpty()) {
                        final BigDecimal arrInstallment = resultmap.get(WaterTaxConstants.ARR_INSTALFROM_STR);
                        if (null != arrInstallment && arrInstallment != BigDecimal.ZERO)
                            arrInstal = installmentDao.findById(new Integer(arrInstallment.toString()), false);
                        consumerConsumption.setCurrentDue(resultmap.get(WaterTaxConstants.CURR_DUE));
                        consumerConsumption.setArrearDue(resultmap.get(WaterTaxConstants.ARR_DUE));
                        if (null != arrInstal) {
                            consumerConsumption.setArrearFromDate(new DateTime(arrInstal.getFromDate()));
                            consumerConsumption
                                    .setArrearToDate(new DateTime(currentInstallment.getFromDate()).minusDays(1));
                        }
                        consumerConsumption.setCurrentFromDate(new DateTime(currentInstallment.getFromDate()));
                        consumerConsumption.setCurentToDate(new DateTime(currentInstallment.getToDate()));
                        consumerConsumptions.add(consumerConsumption);
                        currentTotal = currentTotal.add(consumerConsumption.getCurrentDue());
                        arrearTotal = arrearTotal.add(consumerConsumption.getArrearDue());
                    }
                }

        }
        propertyWiseConsumptions.setCurrentTotal(currentTotal);
        propertyWiseConsumptions.setArrearTotal(arrearTotal);
        propertyWiseConsumptions.setConsumerConsumptions(consumerConsumptions);

        return propertyWiseConsumptions;
    }

    @Override
    public boolean updateBillNo(final String propertyId, final String billNumber) {
        final List<WaterConnection> waterConnections = waterConnectionService.findByPropertyIdentifier(propertyId);
        HashSet<NonMeteredConnBillDetails> nonMeteredConnBillDetails = null;
        for (final WaterConnection waterConnection : waterConnections) {
            final NonMeteredConnBillDetails nonMeteredConnBillDetail = new NonMeteredConnBillDetails();
            Installment installment = null;
            final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                    .getActiveNonHistoryConnectionDetailsByConnection(waterConnection);
            if (ConnectionType.NON_METERED.equals(waterConnectionDetails.getConnectionType())) {
                final Map<String, BigDecimal> resultmap = connectionDetailService.getDemandCollMapForBill(
                        waterConnectionDetails, WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE, null);
                if (null != resultmap && !resultmap.isEmpty()) {
                    nonMeteredConnBillDetails = new HashSet<NonMeteredConnBillDetails>();
                    final BigDecimal install = resultmap.get("inst");
                    installment = installmentDao.findById(install.intValue(), false);
                    nonMeteredConnBillDetail.setBillNo(billNumber);
                    nonMeteredConnBillDetail.setWaterConnectionDetails(
                            waterConnectionDetailsService.findBy(resultmap.get("wcdid").longValue()));
                    nonMeteredConnBillDetail.setInstallment(installment);
                    nonMeteredConnBillDetails.add(nonMeteredConnBillDetail);
                    waterConnectionDetails.setNonmeteredBillDetails(nonMeteredConnBillDetails);
                    waterConnectionDetailsService.save(waterConnectionDetails);
                }
            }
        }
        return true;
    }

    @Override
    public void updateConsumerIndex(final AssessmentDetails assessmentDetails) {
        final List<WaterConnection> waterConnections = waterConnectionService
                .findByPropertyIdentifier(assessmentDetails.getPropertyID());
        for (final WaterConnection waterConnection : waterConnections)
            if (waterConnection.getConsumerCode() != null) {
                final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                        .findByConnection(waterConnection);
                if (waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.INPROGRESS)
                        || waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.ACTIVE))
                    waterConnectionDetailsService.createWaterChargeIndex(waterConnectionDetails, assessmentDetails,
                            waterConnectionDetailsService.getTotalAmount(waterConnectionDetails));
            }

    }

}