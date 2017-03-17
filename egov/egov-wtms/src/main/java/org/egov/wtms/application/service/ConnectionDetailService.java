/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */
package org.egov.wtms.application.service;

import static org.egov.wtms.utils.constants.WaterTaxConstants.INPROGRESS;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.demand.model.EgDemand;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.wtms.application.entity.WaterConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.application.repository.WaterConnectionRepository;
import org.egov.wtms.application.rest.WaterChargesDetails;
import org.egov.wtms.application.rest.WaterTaxDue;
import org.egov.wtms.masters.entity.ApplicationType;
import org.egov.wtms.masters.entity.WaterTaxDetailRequest;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ConnectionDetailService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private InstallmentHibDao installmentDao;

    @Autowired
    private WaterConnectionDetailsRepository waterConnectionDetailsRepository;
    @Autowired
    private WaterConnectionRepository waterConnectionRepository;

    @Autowired
    private ConnectionDemandService connectionDemandService;

    @Autowired
    private WaterConnectionService waterConnectionService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public WaterTaxDue getDueDetailsByConsumerCode(final String consumerCode) {
        final WaterTaxDue waterTaxDue = new WaterTaxDue();
        final List<String> consumerCodes = new ArrayList<>();
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByApplicationNumberOrConsumerCode(consumerCode);
        if (null != waterConnectionDetails) {
            getDueInfo(waterConnectionDetails);
            consumerCodes.add(waterConnectionDetails.getConnection().getConsumerCode());
            waterTaxDue.setConsumerCode(consumerCodes);
            waterTaxDue.setPropertyID(waterConnectionDetails.getConnection().getPropertyIdentifier());
            waterTaxDue.setConnectionCount(consumerCodes.size());
            waterTaxDue.setIsSuccess(true);
        } else {
            waterTaxDue.setIsSuccess(false);
            waterTaxDue.setConsumerCode(Collections.emptyList());
            waterTaxDue.setConnectionCount(0);
            waterTaxDue.setErrorCode(WaterTaxConstants.CONSUMERCODE_NOT_EXIST_ERR_CODE);
            waterTaxDue.setErrorMessage(WaterTaxConstants.WTAXDETAILS_CONSUMER_CODE_NOT_EXIST_ERR_MSG_PREFIX
                    + consumerCode + WaterTaxConstants.WTAXDETAILS_NOT_EXIST_ERR_MSG_SUFFIX);
        }
        return waterTaxDue;
    }

    public WaterTaxDue getDueDetailsByPropertyId(final String propertyIdentifier) {
        BigDecimal arrDmd = new BigDecimal(0);
        BigDecimal arrColl = new BigDecimal(0);
        BigDecimal currDmd = new BigDecimal(0);
        BigDecimal currColl = new BigDecimal(0);
        BigDecimal totalDue = new BigDecimal(0);
        WaterTaxDue waterTaxDue;
        final List<WaterConnection> waterConnections = waterConnectionService
                .findByPropertyIdentifier(propertyIdentifier);
        if (waterConnections.isEmpty()) {
            waterTaxDue = new WaterTaxDue();
            waterTaxDue.setConsumerCode(Collections.emptyList());
            waterTaxDue.setConnectionCount(0);
            waterTaxDue.setIsSuccess(false);
            waterTaxDue.setErrorCode(WaterTaxConstants.PROPERTYID_NOT_EXIST_ERR_CODE);
            waterTaxDue.setErrorMessage(WaterTaxConstants.WTAXDETAILS_PROPERTYID_NOT_EXIST_ERR_MSG_PREFIX
                    + propertyIdentifier + WaterTaxConstants.WTAXDETAILS_NOT_EXIST_ERR_MSG_SUFFIX);
        } else {
            waterTaxDue = new WaterTaxDue();
            final List<String> consumerCodes = new ArrayList<>();
            for (final WaterConnection connection : waterConnections)
                if (connection.getConsumerCode() != null) {
                    final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                            .findByConsumerCodeAndConnectionStatus(connection.getConsumerCode(),
                                    ConnectionStatus.ACTIVE);
                    if (waterConnectionDetails != null) {
                        waterTaxDue = getDueInfo(waterConnectionDetails);
                        waterTaxDue.setPropertyID(propertyIdentifier);
                        consumerCodes.add(connection.getConsumerCode());
                        arrDmd = arrDmd.add(waterTaxDue.getArrearDemand());
                        arrColl = arrColl.add(waterTaxDue.getArrearCollection());
                        currDmd = currDmd.add(waterTaxDue.getCurrentDemand());
                        currColl = currColl.add(waterTaxDue.getCurrentCollection());
                        totalDue = totalDue.add(waterTaxDue.getTotalTaxDue());
                    }
                }
            waterTaxDue.setArrearDemand(arrDmd);
            waterTaxDue.setArrearCollection(arrColl);
            waterTaxDue.setCurrentDemand(currDmd);
            waterTaxDue.setCurrentCollection(currColl);
            waterTaxDue.setTotalTaxDue(totalDue);
            waterTaxDue.setConsumerCode(consumerCodes);
            waterTaxDue.setConnectionCount(waterConnections.size());
            waterTaxDue.setIsSuccess(true);
        }
            List<WaterConnectionDetails> connectionDetailsList = waterConnectionDetailsService.getAllConnectionDetailsExceptInactiveStatusByPropertyID(propertyIdentifier);
            for(WaterConnectionDetails connectionDetails : connectionDetailsList){
                if(INPROGRESS.equals(connectionDetails.getConnectionStatus().toString())){
                    waterTaxDue.setIsInWorkFlow(true);
                }
            }
        
        return waterTaxDue;
    }

    @Transactional
    public String updateWaterConnectionDetails(final WaterTaxDetailRequest waterTaxDetailRequest) {
        final List<WaterConnection> waterConnections = waterConnectionService
                .findByPropertyIdentifier(waterTaxDetailRequest.getAssessmentNumber());
        final WaterConnection waterConnection = waterConnectionService
                .findParentWaterConnection(waterTaxDetailRequest.getAssessmentNumber());
        List<WaterConnectionDetails> waterConnectionDetailslist;
        Boolean parentConnection = false;
        WaterConnectionDetails waterConnectionDetailsRetainer = null;
        WaterConnectionDetails waterConnectionDetails;
        final ApplicationType additionAppType = applicationTypeService.findByCode(WaterTaxConstants.ADDNLCONNECTION);
        if (waterConnections.isEmpty()) {
            for (final String childAssessmentNumber : waterTaxDetailRequest.getChildAssessmentNumber())
                if (waterConnectionDetailsRetainer == null && parentConnection == false) {
                    waterConnectionDetailsRetainer = waterConnectionDetailsService
                            .getPrimaryConnectionDetailsByPropertyAssessmentNumbers(
                                    waterTaxDetailRequest.getChildAssessmentNumber());
                    final WaterConnection connectiontemp = waterConnectionDetailsRetainer.getConnection();
                    connectiontemp.setOldPropertyIdentifier(childAssessmentNumber);
                    connectiontemp.setPropertyIdentifier(waterTaxDetailRequest.getAssessmentNumber());
                    waterConnectionRepository.save(connectiontemp);
                    parentConnection = Boolean.TRUE;
                } else {
                    waterConnectionDetailslist = waterConnectionDetailsService
                            .getAllConnectionDetailsExceptInactiveStatusByPropertyID(childAssessmentNumber);

                    for (final WaterConnectionDetails waterConnectionDetailObj : waterConnectionDetailslist)
                        if (waterConnectionDetailObj != null && waterConnectionDetailObj.getApplicationType()
                                .getCode().equals(WaterTaxConstants.NEWCONNECTION)) {
                            final WaterConnection connectiontemp = waterConnectionDetailObj.getConnection();
                            connectiontemp.setOldPropertyIdentifier(childAssessmentNumber);
                            connectiontemp.setPropertyIdentifier(waterTaxDetailRequest.getAssessmentNumber());
                            connectiontemp.setParentConnection(waterConnectionDetailsRetainer.getConnection());
                            waterConnectionDetailObj.setApplicationType(additionAppType);
                            waterConnectionDetailObj.setConnection(connectiontemp);
                            waterConnectionDetailsRepository
                                    .save(waterConnectionDetailObj);
                        } else {
                            final WaterConnection connectiontemp = waterConnectionDetailObj.getConnection();
                            connectiontemp.setOldPropertyIdentifier(connectiontemp.getPropertyIdentifier());
                            connectiontemp.setPropertyIdentifier(waterTaxDetailRequest.getAssessmentNumber());
                            connectiontemp.setParentConnection(waterConnectionDetailsRetainer.getConnection());
                            waterConnectionRepository.save(connectiontemp);
                        }

                }
        } else if (waterConnection != null) {
            final WaterConnectionDetails waterConnectionDetailsRetainerObj = waterConnectionDetailsService
                    .findParentConnectionDetailsByConsumerCodeAndConnectionStatus(waterConnection.getConsumerCode(),
                            ConnectionStatus.ACTIVE);
            if (waterConnectionDetailsRetainerObj != null && !waterTaxDetailRequest.getChildAssessmentNumber().isEmpty())
                for (final String childAssessmentNumber : waterTaxDetailRequest.getChildAssessmentNumber()) {
                    waterConnectionDetails = waterConnectionDetailsService
                            .getPrimaryConnectionDetailsByPropertyIdentifier(childAssessmentNumber);
                    if (waterConnectionDetails != null) {
                        final WaterConnection connectiontemp = waterConnectionDetails.getConnection();
                        connectiontemp.setOldPropertyIdentifier(connectiontemp.getPropertyIdentifier());
                        connectiontemp.setPropertyIdentifier(waterTaxDetailRequest.getAssessmentNumber());
                        connectiontemp.setParentConnection(waterConnectionDetailsRetainerObj.getConnection());
                        waterConnectionDetails.setApplicationType(additionAppType);
                        waterConnectionDetails.setConnection(connectiontemp);
                        waterConnectionDetailsRepository
                                .save(waterConnectionDetails);
                    } else {
                        waterConnectionDetailslist = waterConnectionDetailsService
                                .getChildConnectionDetailsByPropertyID(childAssessmentNumber);
                        for (final WaterConnectionDetails tempconn : waterConnectionDetailslist) {
                            final WaterConnection connectiontemp = tempconn.getConnection();
                            connectiontemp.setOldPropertyIdentifier(connectiontemp.getPropertyIdentifier());
                            connectiontemp.setPropertyIdentifier(childAssessmentNumber);
                            waterConnectionRepository.save(connectiontemp);
                        }
                    }
                }
        }
        return waterTaxDetailRequest.getAssessmentNumber();
    }

    public List<WaterChargesDetails> getWaterTaxDetailsByPropertyId(final String propertyIdentifier, final String ulbCode) {
        final List<WaterConnection> waterConnections = waterConnectionService
                .findByPropertyIdentifier(propertyIdentifier);
        final List<WaterChargesDetails> waterChargesDetailsList = new ArrayList<>();
        if (waterConnections.isEmpty())
            return waterChargesDetailsList;
        else {
            WaterChargesDetails waterChargesDetails = new WaterChargesDetails();
            for (final WaterConnection connection : waterConnections)
                if (connection.getConsumerCode() != null) {
                    WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                            .findByConsumerCodeAndConnectionStatus(connection.getConsumerCode(),
                                    ConnectionStatus.ACTIVE);
                    if (waterConnectionDetails != null)
                        waterChargesDetails = getWatertaxDetails(waterConnectionDetails, connection.getConsumerCode(),
                                propertyIdentifier, ulbCode);
                    else {
                        waterConnectionDetails = waterConnectionDetailsService.findByConsumerCodeAndConnectionStatus(
                                connection.getConsumerCode(), ConnectionStatus.INACTIVE);
                        if (waterConnectionDetails != null)
                            waterChargesDetails = getWatertaxDetails(waterConnectionDetails,
                                    connection.getConsumerCode(), propertyIdentifier, ulbCode);
                    }
                    waterChargesDetailsList.add(waterChargesDetails);
                }
            return waterChargesDetailsList;
        }
    }

    public WaterChargesDetails getWatertaxDetails(final WaterConnectionDetails waterConnectionDetails,
            final String consumerCode, final String propertyIdentifier, final String ulbCode) {
        final WaterChargesDetails waterChargesDetails = new WaterChargesDetails();
        waterChargesDetails.setTotalTaxDue(getDueInfo(waterConnectionDetails).getTotalTaxDue());
        waterChargesDetails.setConnectionType(waterConnectionDetails.getConnectionType().name());
        waterChargesDetails.setConsumerCode(consumerCode);
        waterChargesDetails.setPropertyID(propertyIdentifier);
        waterChargesDetails.setConnectionStatus(waterConnectionDetails.getConnectionStatus().name());
        waterChargesDetails.setNoOfPerson(waterConnectionDetails.getNumberOfPerson());
        waterChargesDetails.setPipesize(waterConnectionDetails.getPipeSize().getCode());
        waterChargesDetails.setWaterSource(waterConnectionDetails.getWaterSource().getDescription());
        waterChargesDetails.setUlbCode(ulbCode);
        waterChargesDetails.setWaterSupplyType(
                waterConnectionDetails.getWaterSupply() != null ? waterConnectionDetails.getWaterSupply().getDescription() : "");
        waterChargesDetails.setCategory(waterConnectionDetails.getCategory().getDescription());
        waterChargesDetails.setSumpCapacity(waterConnectionDetails.getSumpCapacity());
        waterChargesDetails.setUsageType(waterConnectionDetails.getUsageType().getDescription());
        waterChargesDetails.setPropertytype(waterConnectionDetails.getPropertyType().getName());
        waterChargesDetails.setConnectionStatus(waterConnectionDetails.getConnectionStatus().toString());
        if (waterConnectionDetails.getApplicationType() != null
                && waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.NEWCONNECTION))
            waterChargesDetails.setIsPrimaryConnection(true);
        else
            waterChargesDetails.setIsPrimaryConnection(false);
        return waterChargesDetails;
    }

    private WaterTaxDue getDueInfo(final WaterConnectionDetails waterConnectionDetails) {
        final Map<String, BigDecimal> resultmap = getDemandCollMap(waterConnectionDetails);
        final WaterTaxDue waterTaxDue = new WaterTaxDue();
        if (null != resultmap && !resultmap.isEmpty()) {
            final BigDecimal currDmd = resultmap.get(WaterTaxConstants.CURR_DMD_STR);
            waterTaxDue.setCurrentDemand(currDmd);
            final BigDecimal arrDmd = resultmap.get(WaterTaxConstants.ARR_DMD_STR);
            waterTaxDue.setArrearDemand(arrDmd);
            final BigDecimal currCollection = resultmap.get(WaterTaxConstants.CURR_COLL_STR);
            waterTaxDue.setCurrentCollection(currCollection);
            final BigDecimal arrCollection = resultmap.get(WaterTaxConstants.ARR_COLL_STR);
            waterTaxDue.setArrearCollection(arrCollection);
            // Calculating tax dues
            final BigDecimal taxDue = currDmd.add(arrDmd).subtract(currCollection).subtract(arrCollection);
            waterTaxDue.setTotalTaxDue(taxDue);
        }
        return waterTaxDue;
    }

    public Map<String, BigDecimal> getDemandCollMap(final WaterConnectionDetails waterConnectionDetails) {
        final EgDemand currDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        Installment installment = null;
        List<Object> dmdCollList = new ArrayList<>(0);
        Installment currFirstHalf = null;
        Installment currSecondHalf = null;
        Integer instId = null;
        BigDecimal currDmd = BigDecimal.ZERO;
        BigDecimal arrDmd = BigDecimal.ZERO;
        BigDecimal currCollection = BigDecimal.ZERO;
        BigDecimal arrCollection = BigDecimal.ZERO;
        final Map<String, BigDecimal> retMap = new HashMap<>(0);
        if (currDemand != null)
            dmdCollList = connectionDemandService.getDmdCollAmtInstallmentWise(currDemand);
        currFirstHalf = propertyTaxUtil.getInstallmentsForCurrYear(new Date())
                .get(PropertyTaxConstants.CURRENTYEAR_FIRST_HALF);
        currSecondHalf = propertyTaxUtil.getInstallmentsForCurrYear(new Date())
                .get(PropertyTaxConstants.CURRENTYEAR_SECOND_HALF);
        for (final Object object : dmdCollList) {
            final Object[] listObj = (Object[]) object;
            instId = Integer.valueOf(listObj[1].toString());
            installment = installmentDao.findById(instId, false);
            if (currFirstHalf.equals(installment) || currSecondHalf.equals(installment)) {
                if (listObj[3] != null && new BigDecimal((Double) listObj[3]).compareTo(BigDecimal.ZERO) == 1)
                    currCollection = currCollection.add(new BigDecimal((Double) listObj[3]));
                currDmd = currDmd.add(new BigDecimal((Double) listObj[2]));
            } else if (listObj[2] != null) {
                arrDmd = arrDmd.add(new BigDecimal((Double) listObj[2]));
                if (new BigDecimal((Double) listObj[3]).compareTo(BigDecimal.ZERO) == 1)
                    arrCollection = arrCollection.add(new BigDecimal((Double) listObj[3]));
            }
        }
        retMap.put(WaterTaxConstants.CURR_DMD_STR, currDmd);
        retMap.put(WaterTaxConstants.ARR_DMD_STR, arrDmd);
        retMap.put(WaterTaxConstants.CURR_COLL_STR, currCollection);
        retMap.put(WaterTaxConstants.ARR_COLL_STR, arrCollection);
        return retMap;
    }

    public Installment getCurrentInstallment(final String moduleName, final String installmentType, final Date date) {
        return connectionDemandService.getCurrentInstallment(moduleName, installmentType, date);
    }

    public Map<String, BigDecimal> getDemandCollMapForPtisIntegration(
            final WaterConnectionDetails waterConnectionDetails, final String moduleName,
            final String installmentType) {
        final EgDemand currDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        Installment installment = null;
        List<Object> dmdCollList = new ArrayList<>(0);
        Installment currInst = null;
        Integer instId = null;
        BigDecimal curDue = BigDecimal.ZERO;
        BigDecimal arrDue = BigDecimal.ZERO;

        BigDecimal arrearInstallmentfrom = BigDecimal.ZERO;
        final Map<String, BigDecimal> retMap = new HashMap<>(0);
        if (currDemand != null)
            dmdCollList = connectionDemandService.getDmdCollAmtInstallmentWiseWithIsDmdTrue(currDemand);
        currInst = getCurrentInstallment(moduleName, null, new Date());
        for (final Object object : dmdCollList) {
            final Object[] listObj = (Object[]) object;
            instId = Integer.valueOf(listObj[2].toString());
            installment = installmentDao.findById(instId, false);
            if (currInst.equals(installment))
                curDue = new BigDecimal(listObj[6].toString());
            else {
                arrDue = new BigDecimal(listObj[6].toString());
                if (arrDue.signum() > 0)
                    if (BigDecimal.ZERO == arrearInstallmentfrom || null == arrearInstallmentfrom)
                        arrearInstallmentfrom = BigDecimal.valueOf(instId);

            }
        }
        retMap.put(WaterTaxConstants.ARR_DUE, arrDue);
        retMap.put(WaterTaxConstants.CURR_DUE, curDue);
        retMap.put(WaterTaxConstants.ARR_INSTALFROM_STR, arrearInstallmentfrom);
        return retMap;
    }

    public Map<String, BigDecimal> getDemandCollMapForBill(final WaterConnectionDetails waterConnectionDetails,
            final String moduleName, final String installmentType) {
        final EgDemand currDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        List<Object> dmdCollList = new ArrayList<>(0);
        Integer instId = null;
        Double balance = null;
        Integer val = null;
        final Map<String, BigDecimal> retMap = new HashMap<>(0);
        if (currDemand != null)
            dmdCollList = connectionDemandService.getDmdCollAmtInstallmentWiseWithIsDmdTrue(currDemand);
        for (final Object object : dmdCollList) {
            final Object[] listObj = (Object[]) object;
            balance = (Double) listObj[6];
            if (BigDecimal.valueOf(balance).signum() > 0) {
                val = Integer.valueOf(listObj[0].toString());
                instId = Integer.valueOf(listObj[2].toString());
                retMap.put("wcdid", BigDecimal.valueOf(val));
                retMap.put("inst", BigDecimal.valueOf(instId));
            }
        }
        return retMap;
    }
}
