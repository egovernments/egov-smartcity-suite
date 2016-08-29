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

package org.egov.stms.transactions.service;

import static org.egov.stms.utils.constants.SewerageTaxConstants.ARREARSEWERAGETAX;
import static org.egov.stms.utils.constants.SewerageTaxConstants.BOUNDARYTYPE_WARD;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_ADVANCE_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_SEWERAGETAX_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.HIERARCHYTYPE_REVENUE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgdmCollectedReceipt;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.stms.masters.pojo.DCBReportWardwiseResult;
import org.egov.stms.masters.pojo.SewerageRateDCBResult;
import org.egov.stms.masters.pojo.SewerageRateResultComparatorByInstallment;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class SewerageDCBReporService {
    
    @Autowired
    private BoundaryService boundaryService;
    
    @Autowired
    SewerageThirdPartyServices sewerageThirdPartyServices;
    
    @Autowired
    private BoundaryTypeService boundaryTypeService;
    
    
    public List<SewerageRateDCBResult> getSewerageRateDCBReport(final SewerageApplicationDetails sewerageApplicationDetails) {
        List<SewerageRateDCBResult> rateResultList = new ArrayList<SewerageRateDCBResult>();
        Map<String, Map<Date, BigDecimal>> receiptMap = null;
        Map<Date, BigDecimal> receiptDtlMap  = null;
        final HashMap<String, SewerageRateDCBResult> sewerageReportMap = new HashMap<String, SewerageRateDCBResult>();
        if (sewerageApplicationDetails.getConnection() != null ) {
           
            SewerageRateDCBResult dcbResult = new SewerageRateDCBResult();
            for (EgDemandDetails demandDtl : sewerageApplicationDetails.getCurrentDemand().getEgDemandDetails()) {
                final SewerageRateDCBResult rateResult = sewerageReportMap
                        .get(demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription());
                if (rateResult == null) {
                    // TODO: Handle Penalty cases in future.

                    dcbResult = new SewerageRateDCBResult();
                    dcbResult.setInstallmentYearDescription(
                            demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription());
                    dcbResult.setInstallmentYearId(demandDtl.getEgDemandReason().getEgInstallmentMaster().getId());
                    if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(ARREARSEWERAGETAX)) {
                        dcbResult.setArrearAmount(demandDtl.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
                        dcbResult.setCollectedArrearAmount(demandDtl.getAmtCollected().setScale(2, BigDecimal.ROUND_HALF_UP));
                    } 
                    else if(demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode().equalsIgnoreCase(FEES_SEWERAGETAX_CODE)){
                        dcbResult.setDemandAmount(dcbResult.getDemandAmount().add(demandDtl.getAmount()));
                        dcbResult.setCollectedDemandAmount(dcbResult.getCollectedDemandAmount().add(demandDtl.getAmtCollected()));
                    }
                    else if(demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode().equalsIgnoreCase(FEES_ADVANCE_CODE)) {
                        dcbResult.setAdvanceAmount(demandDtl.getAmtCollected().setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    sewerageReportMap.put(demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription(), dcbResult);
                } else {

                    dcbResult = sewerageReportMap.get(demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription());

                    if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(ARREARSEWERAGETAX)) {
                        dcbResult.setArrearAmount(
                                dcbResult.getArrearAmount().add(demandDtl.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)));
                        dcbResult.setCollectedArrearAmount(dcbResult.getCollectedArrearAmount()
                                .add(demandDtl.getAmtCollected().setScale(2, BigDecimal.ROUND_HALF_UP)));
                    } else if(demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode().equalsIgnoreCase(FEES_SEWERAGETAX_CODE)) {
                        dcbResult.setDemandAmount(
                                dcbResult.getDemandAmount().add(demandDtl.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)));
                        dcbResult.setCollectedDemandAmount(dcbResult.getCollectedDemandAmount()
                                .add(demandDtl.getAmtCollected().setScale(2, BigDecimal.ROUND_HALF_UP)));
                    }
                    else if(demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode().equalsIgnoreCase(FEES_ADVANCE_CODE)){
                        dcbResult.setAdvanceAmount(dcbResult.getAdvanceAmount().add(demandDtl.getAmtCollected().setScale(2, BigDecimal.ROUND_HALF_UP)));
                    }
                    sewerageReportMap.put(demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription(), dcbResult);
                }
            }
     
            receiptMap = new TreeMap<String, Map<Date, BigDecimal>>();
           for(SewerageApplicationDetails detail : sewerageApplicationDetails.getConnection().getApplicationDetails()){
            for (EgDemandDetails demandDetail : detail.getCurrentDemand().getEgDemandDetails()) {
                receiptDtlMap = new HashMap<Date, BigDecimal>();

                for (EgdmCollectedReceipt receipt : demandDetail.getEgdmCollectedReceipts()) {
                    receiptDtlMap.put(receipt.getReceiptDate(), receipt.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
                    receiptMap.put(receipt.getReceiptNumber(), receiptDtlMap);
                }

            }
           }
            
            dcbResult.setReceipts(receiptMap);
            dcbResult.setApplicationNumber(sewerageApplicationDetails.getApplicationNumber());
            
            if (sewerageReportMap.size() > 0) {
                sewerageReportMap.forEach((key, value) -> {
                    rateResultList.add(value);
                });

            }

        }
        Collections.sort(rateResultList, new SewerageRateResultComparatorByInstallment());
        return rateResultList;
       
    }

    public List<DCBReportWardwiseResult> getSewerageRateDCBWardwiseReport(
            final Map<String, List<SewerageApplicationDetails>> applicationDtlMap, final String propertyType) {
        List<DCBReportWardwiseResult> dcbReportList = new ArrayList<DCBReportWardwiseResult>();

        Map<String, DCBReportWardwiseResult> dcbReportMap = new HashMap<String, DCBReportWardwiseResult>();
        Map<String, DCBReportWardwiseResult> dcbMap = new HashMap<String, DCBReportWardwiseResult>();
        DCBReportWardwiseResult dcbResult = new DCBReportWardwiseResult();

        for (Map.Entry<String, List<SewerageApplicationDetails>> entry : applicationDtlMap.entrySet()) {
            Boundary boundary = null;
            BoundaryType boundaryType = null;
            List<Boundary> boundaryList = new ArrayList<Boundary>();
            dcbResult = new DCBReportWardwiseResult();
            for (SewerageApplicationDetails appDetails : entry.getValue()) {
                if (appDetails != null) {
                    for (EgDemandDetails demandDetails : appDetails.getCurrentDemand().getEgDemandDetails()) {
                        dcbResult.setNoofassessments(entry.getValue().size());
                        dcbResult.setRevenueWard(entry.getKey());
                        if (null != propertyType) {
                            dcbResult.setPropertyType(propertyType);
                        }
                        boundaryType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyTypeName(BOUNDARYTYPE_WARD, HIERARCHYTYPE_REVENUE);
                        boundaryList.addAll(boundaryService.getBondariesByNameAndTypeOrderByBoundaryNumAsc(entry.getKey(), boundaryType.getId()));
                        if(!boundaryList.isEmpty())
                        boundary = boundaryList.get(0);
                        if(boundary!=null)                        
                        dcbResult.setWardId(boundary.getId());
                        final DCBReportWardwiseResult rateResult = dcbReportMap.get(entry.getKey());
                        if (rateResult == null) {
                            dcbResult.setInstallmentYearDescription(demandDetails.getEgDemandReason().getEgInstallmentMaster().getDescription());
                            buildArrearAndCurrentDemandTax(dcbResult, demandDetails);

                        } else {
                            dcbResult = dcbReportMap.get(entry.getKey());

                            buildArrearAndCurrentDemandTax(dcbResult, demandDetails);

                        }
                        dcbResult.setTotal_demand(dcbResult.getArr_demand().add(dcbResult.getCurr_demand()));
                        dcbResult.setTotal_collection(dcbResult.getArr_collection().add(dcbResult.getCurr_collection()));
                        dcbResult.setTotal_balance(dcbResult.getArr_balance().add(dcbResult.getCurr_balance()));

                        dcbReportMap.put(entry.getKey(), dcbResult);
                    }
                }
            }
            dcbMap.put(entry.getKey(), dcbResult);

        }
        if (dcbMap.size() > 0) {
            dcbMap.forEach((key, value) -> {
                dcbReportList.add(value);
            });
        }

        return dcbReportList;
    }

    private void buildArrearAndCurrentDemandTax(DCBReportWardwiseResult dcbResult, EgDemandDetails demandDetails) {
        if (demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode()
                .equalsIgnoreCase(ARREARSEWERAGETAX)) {
            dcbResult.setArr_demand(dcbResult.getArr_demand()
                    .add(demandDetails.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)));
            dcbResult.setArr_collection(dcbResult.getArr_collection()
                    .add(demandDetails.getAmtCollected().setScale(2, BigDecimal.ROUND_HALF_UP)));
            dcbResult.setArr_balance(dcbResult.getArr_balance()
                    .add(demandDetails.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).subtract(demandDetails.getAmtCollected().setScale(2, BigDecimal.ROUND_HALF_UP))));
        } else if(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode().equalsIgnoreCase(FEES_SEWERAGETAX_CODE)) {
            dcbResult.setCurr_demand(dcbResult.getCurr_demand().add(demandDetails.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)));
            dcbResult.setCurr_collection(dcbResult.getCurr_collection()
                    .add(demandDetails.getAmtCollected().setScale(2, BigDecimal.ROUND_HALF_UP)));
            dcbResult.setCurr_balance(dcbResult.getCurr_balance()
                    .add(demandDetails.getAmount().subtract(demandDetails.getAmtCollected()).setScale(2, BigDecimal.ROUND_HALF_UP)));
        }
    }

    public List<DCBReportWardwiseResult> getSewerageDCBWardConnections(
            Map<String, List<SewerageApplicationDetails>> applicationDtlMap, final String propertyType,
            final HttpServletRequest request) {

        List<DCBReportWardwiseResult> dcbReportList = new ArrayList<DCBReportWardwiseResult>();
        Map<String, DCBReportWardwiseResult> dcbReportMap = new HashMap<String, DCBReportWardwiseResult>();
        DCBReportWardwiseResult dcbResult = new DCBReportWardwiseResult();

        for (Map.Entry<String, List<SewerageApplicationDetails>> entry : applicationDtlMap.entrySet()) {

            List<SewerageApplicationDetails> applicationList = new ArrayList<SewerageApplicationDetails>();
            if (entry.getValue() != null)
                applicationList.addAll(entry.getValue());

            for (SewerageApplicationDetails detail : applicationList) {
                dcbResult = new DCBReportWardwiseResult();
                if (detail != null) {
                    dcbResult.setShscnumber(detail.getConnection().getShscNumber());
                    dcbResult.setOwnerName(detail.getOwnerName());
                    for (EgDemandDetails demandDetails : detail.getCurrentDemand().getEgDemandDetails()) {
                        dcbResult.setApplicationNumber(entry.getKey());
                        if (propertyType != null) {
                            dcbResult.setPropertyType(propertyType);
                        }
                        final DCBReportWardwiseResult rateResult = dcbReportMap.get(entry.getKey());
                        if (rateResult == null) {
                            dcbResult.setInstallmentYearDescription(
                                    demandDetails.getEgDemandReason().getEgInstallmentMaster().getDescription());
                            buildArrearAndCurrentDemandTax(dcbResult, demandDetails);
                            dcbReportMap.put(entry.getKey(), dcbResult);
                        } else {
                            dcbResult = dcbReportMap.get(entry.getKey());

                            if (demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                    .equalsIgnoreCase(ARREARSEWERAGETAX)) {
                                dcbResult.setArr_demand(dcbResult.getArr_demand()
                                        .add(demandDetails.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)));
                                dcbResult.setArr_collection(dcbResult.getArr_collection()
                                        .add(demandDetails.getAmtCollected()).setScale(2, BigDecimal.ROUND_HALF_UP));
                                dcbResult.setArr_balance(dcbResult.getArr_balance()
                                        .add(demandDetails.getAmount().subtract(demandDetails.getAmtCollected()).setScale(2, BigDecimal.ROUND_HALF_UP)));
                            } else if(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                    .equalsIgnoreCase(FEES_SEWERAGETAX_CODE)){
                                dcbResult.setCurr_demand(dcbResult.getCurr_demand()
                                        .add(demandDetails.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)));
                                dcbResult.setCurr_collection(dcbResult.getCurr_collection()
                                        .add(demandDetails.getAmtCollected().setScale(2, BigDecimal.ROUND_HALF_UP)));
                                dcbResult.setCurr_balance(dcbResult.getCurr_balance()
                                        .add(demandDetails.getAmount().subtract(demandDetails.getAmtCollected()).setScale(2, BigDecimal.ROUND_HALF_UP)));

                                dcbResult.setTotal_demand(dcbResult.getArr_demand().add(dcbResult.getCurr_demand()));
                                dcbResult.setTotal_collection(dcbResult.getArr_collection().add(dcbResult.getCurr_collection()));
                                dcbResult.setTotal_balance(dcbResult.getArr_balance().add(dcbResult.getCurr_balance()));
                            }

                            dcbReportMap.put(entry.getKey(), dcbResult);
                        }
                    }
                }
            }

        }
        if (dcbReportMap.size() > 0) {
            dcbReportMap.forEach((key, value) -> {
                dcbReportList.add(value);
            });
        }

        return dcbReportList;
    }

}
