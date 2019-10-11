/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.wtms.service;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ZERO;
import static org.egov.infra.utils.DateUtils.toYearFormat;
import static org.egov.wtms.masters.entity.enums.ConnectionStatus.ACTIVE;
import static org.egov.wtms.masters.entity.enums.ConnectionType.METERED;
import static org.egov.wtms.masters.entity.enums.ConnectionType.NON_METERED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.BILLTYPE_AUTO;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CATEGORY_BPL;
import static org.egov.wtms.utils.constants.WaterTaxConstants.DEMANDRSN_CODE_ADVANCE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ESTIMATIONCHARGES_SERVICE_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.METERED_CHARGES_REASON_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MODULE_NAME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MONTHLY;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NO_OF_INSTALLMENTS;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PROPERTY_MODULE_NAME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REGULARIZE_CONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERTAXREASONCODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERTAX_CONNECTION_CHARGE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERTAX_DONATION_CHARGE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATER_MATERIAL_CHARGES_REASON_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.YEARLY;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.application.service.WaterDemandConnectionService;
import org.egov.wtms.application.service.collection.ConnectionBillService;
import org.egov.wtms.application.service.collection.WaterConnectionBillable;
import org.egov.wtms.autonumber.BillReferenceNumberGenerator;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WaterEstimationChargesPaymentService {

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private ConnectionBillService connectionBillService;

    @Autowired
    @Qualifier("waterConnectionBillable")
    private WaterConnectionBillable waterConnectionBillable;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private ConnectionDemandService connectionDemandService;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;
    
    @Autowired
    private WaterDemandConnectionService waterDemandConnectionService;

	public BigDecimal getEstimationDueAmount(WaterConnectionDetails waterConnectionDetails) {
		BigDecimal balance = BigDecimal.ZERO;
		Map<String, BigDecimal> amountsMap = getEstimationDueDetails(waterConnectionDetails);
		if (!amountsMap.isEmpty())
			balance = amountsMap.get("balance");

		return balance;
	}

	public Map<String, BigDecimal> getEstimationDueDetails(WaterConnectionDetails waterConnectionDetails) {
		Map<String, BigDecimal> amountsMap = new HashMap<>();
		EgDemand currentDemand = waterDemandConnectionService.getCurrentDemand(waterConnectionDetails).getDemand();
		BigDecimal estimationAmount = ZERO;
		List<String> demandCodes = Arrays.asList(METERED_CHARGES_REASON_CODE, WATERTAXREASONCODE,
				DEMANDRSN_CODE_ADVANCE, WATERTAX_CONNECTION_CHARGE);
		if (currentDemand != null) {
			BigDecimal materialCharges = BigDecimal.ZERO;
			for (EgDemandDetails demandDetails : currentDemand.getEgDemandDetails()) {
				if (!demandCodes.contains(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode())) {
					if (WATER_MATERIAL_CHARGES_REASON_CODE
							.equalsIgnoreCase(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode()))
						materialCharges = demandDetails.getAmount();
					estimationAmount = estimationAmount.add(demandDetails.getAmount()
							.subtract(demandDetails.getAmtRebate().add(demandDetails.getAmtCollected())));
				}
			}
			amountsMap.put("balance", estimationAmount);
			amountsMap.put("materialCharges", materialCharges);
		}
		return amountsMap;
	}

    @Transactional
    public String generateBill(String applicationNumber) {

        String currentInstallmentYear = StringUtils.EMPTY;
        WaterConnectionDetails connectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCode(applicationNumber);

        if (ConnectionStatus.INPROGRESS.equals(connectionDetails.getConnectionStatus()))
            currentInstallmentYear = toYearFormat(connectionDemandService.getCurrentInstallment(
                    MODULE_NAME, YEARLY, new Date()).getInstallmentYear());
        else if (ACTIVE.equals(connectionDetails.getConnectionStatus()) && NON_METERED.equals(connectionDetails.getConnectionType()))
            currentInstallmentYear = toYearFormat(connectionDemandService.getCurrentInstallment(
                    PROPERTY_MODULE_NAME, null, new Date()).getInstallmentYear());
        else if (ACTIVE.equals(connectionDetails.getConnectionStatus()) && METERED.equals(connectionDetails.getConnectionType()))
            currentInstallmentYear = toYearFormat(connectionDemandService.getCurrentInstallment(
                    MODULE_NAME, MONTHLY, new Date()).getInstallmentYear());
        AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                connectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);

        waterConnectionBillable.setWaterConnectionDetails(connectionDetails);
        waterConnectionBillable.setAssessmentDetails(assessmentDetails);
        waterConnectionBillable.setUserId(ApplicationThreadLocals.getUserId());

        BigDecimal noOfInstallment = BigDecimal.valueOf(NO_OF_INSTALLMENTS);
        if (waterConnectionBillable.getPartPaymentAllowed() && NON_METERED.equals(connectionDetails.getConnectionType())) {
            BigDecimal estimationAmount = getEstimationAmount(connectionDetails);
            BigDecimal estimationDueAmount = getEstimationDueAmount(connectionDetails);
            BigDecimal estimationInstAmount = estimationAmount.divide(noOfInstallment, ROUND_HALF_UP).setScale(0, ROUND_HALF_UP);
            if (REGULARIZE_CONNECTION.equals(connectionDetails.getApplicationType().getCode()))
                waterConnectionBillable.getCurrentDemand().setMinAmtPayable(estimationAmount);
            else if (CATEGORY_BPL.equalsIgnoreCase(connectionDetails.getCategory().getName()))
                waterConnectionBillable.getCurrentDemand().setMinAmtPayable(setBPLMinimumAmount(connectionDetails));
            else if (estimationDueAmount.compareTo(estimationInstAmount) > 0)
                waterConnectionBillable.getCurrentDemand().setMinAmtPayable(estimationInstAmount);
            else
                waterConnectionBillable.getCurrentDemand().setMinAmtPayable(estimationDueAmount);
        }

        BillReferenceNumberGenerator billRefeNumber = beanResolver.getAutoNumberServiceFor(BillReferenceNumberGenerator.class);
        waterConnectionBillable.setReferenceNumber(billRefeNumber.generateBillNumber(currentInstallmentYear));
        waterConnectionBillable.setBillType(connectionDemandService.getBillTypeByCode(BILLTYPE_AUTO));
        waterConnectionBillable.setServiceCode(ESTIMATIONCHARGES_SERVICE_CODE);

        return connectionBillService.getBillXML(waterConnectionBillable);
    }

    public BigDecimal getEstimationAmount(WaterConnectionDetails waterConnectionDetails) {
        EgDemand currentDemand = waterDemandConnectionService.getCurrentDemand(waterConnectionDetails).getDemand();
        BigDecimal estimationAmount = BigDecimal.ZERO;
        List<String> demandCodes = Arrays.asList(METERED_CHARGES_REASON_CODE, WATERTAXREASONCODE,
                DEMANDRSN_CODE_ADVANCE, WATERTAX_CONNECTION_CHARGE);
        if (currentDemand != null)
            for (EgDemandDetails demandDetails : currentDemand.getEgDemandDetails()) {
                if (!demandCodes.contains(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode())) {
                    estimationAmount = estimationAmount.add(demandDetails.getAmount());
                }
            }
        return estimationAmount;
    }
    
    public BigDecimal setBPLMinimumAmount(WaterConnectionDetails waterConnectionDetails) {
        EgDemand currentDemand = waterDemandConnectionService.getCurrentDemand(waterConnectionDetails).getDemand();
        if(currentDemand!=null)
            for(EgDemandDetails demandDetail: currentDemand.getEgDemandDetails()) {
                if(WATERTAX_DONATION_CHARGE.equalsIgnoreCase(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    return demandDetail.getAmount();
            }
        return ZERO;
    }
    
    public BigDecimal getCollectedEstimationCharges(WaterConnectionDetails waterConnectionDetails) {
        EgDemand currentDemand = waterDemandConnectionService.getCurrentDemand(waterConnectionDetails).getDemand();
        BigDecimal collectedEstimationCharges = ZERO;
        List<String> demandCodes = Arrays.asList(METERED_CHARGES_REASON_CODE, WATERTAXREASONCODE,
                DEMANDRSN_CODE_ADVANCE, WATERTAX_CONNECTION_CHARGE);
        if (currentDemand != null)
            for (EgDemandDetails demandDetails : currentDemand.getEgDemandDetails()) {
                if (!demandCodes.contains(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode())) {
                	collectedEstimationCharges = collectedEstimationCharges.add(demandDetails.getAmtCollected());
                }
            }
        return collectedEstimationCharges;
    }
}
