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

package org.egov.adtax.service.collection;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.adtax.entity.Advertisement;
import org.egov.adtax.entity.AdvertisementAdditionalTaxRate;
import org.egov.adtax.service.AdvertisementAdditinalTaxRateService;
import org.egov.adtax.service.AdvertisementDemandService;
import org.egov.adtax.service.penalty.AdvertisementPenaltyCalculator;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.collection.integration.models.BillAccountDetails.PURPOSE;
import org.egov.collection.integration.models.BillInfo.COLLECTIONTYPE;
import org.egov.collection.integration.models.BillInfoImpl;
import org.egov.commons.Installment;
import org.egov.demand.interfaces.BillServiceInterface;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.utils.DatabaseSequenceProvider;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdvertisementBillServiceImpl extends BillServiceInterface {
    private static final String ADVERTISEMENT_BILLNUMBER = "SEQ_advertisementbill_NUMBER";

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private AppConfigValueService appConfigValuesService;
    @Autowired
    private AdvertisementAdditinalTaxRateService advertisementAdditinalTaxRateService;
    @Autowired
    private AdvertisementDemandService advertisementDemandService;
    @Autowired
    private AdvertisementPenaltyCalculator advertisementPenaltyCalculator;
    @Autowired
    private AdvertisementBillable advertisementBillable;
    @Autowired
    private DatabaseSequenceProvider databaseSequenceProvider;
    @Autowired
    private AdtaxExternalService adtaxExternalService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public List<EgBillDetails> getBilldetails(final Billable billObj) {
        final List<EgBillDetails> billDetailList = new ArrayList<>();
        int orderNo = 1;
        final AdvertisementBillable advBillable = (AdvertisementBillable) billObj;
        final EgDemand dmd = advBillable.getCurrentDemand();
        final List<EgDemandDetails> details = new ArrayList<>(dmd.getEgDemandDetails());
        BigDecimal totalTaxableAmount = BigDecimal.ZERO;
        if (!details.isEmpty())
            Collections.sort(details, (c1, c2) -> c1.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()
                    .compareTo(c2.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()));

        final Map<String, BigDecimal> additionalTaxes = new HashMap<>();
        final List<AdvertisementAdditionalTaxRate> additionalTaxRates = advertisementAdditinalTaxRateService
                .getAllActiveAdditinalTaxRates();

        for (final AdvertisementAdditionalTaxRate taxRates : additionalTaxRates)
            additionalTaxes.put(taxRates.getReasonCode(), taxRates.getPercentage());

        for (final EgDemandDetails demandDetail : details)
            if (demandDetail.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal creaditAmt = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());

                // If Amount- collected amount greather than zero, then send
                // these demand details to collection.
                if (creaditAmt.compareTo(BigDecimal.ZERO) > 0
                        && (!AdvertisementTaxConstants.DEMANDREASON_PENALTY.equalsIgnoreCase(demandDetail
                                .getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()) ||
                                !additionalTaxes.containsKey(demandDetail
                                        .getEgDemandReason().getEgDemandReasonMaster().getCode()))) {
                    // TODO: CHECK WHETHER PENALTY,ENCROCHAMENT FEE AND ARRREARS ALSO NEED TO CONSIDER ?
                    totalTaxableAmount = totalTaxableAmount.add(creaditAmt);

                    final EgBillDetails billdetail = createBillDetailObject(orderNo, BigDecimal.ZERO, creaditAmt,
                            demandDetail.getEgDemandReason().getGlcodeId().getGlcode(), getReceiptDetailDescription(
                                    demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster(),
                                    demandDetail.getEgDemandReason().getEgInstallmentMaster()));
                    orderNo++;
                    billDetailList.add(billdetail);

                }
            }

        final Map<Installment, BigDecimal> penaltyReasons = advertisementPenaltyCalculator
                .getPenaltyOnAdditionalTaxByInstallment(advBillable.getAdvertisement().getActiveAdvertisementPermit());

        if (penaltyReasons != null && penaltyReasons.size() > 0) {
            for (final Map.Entry<Installment, BigDecimal> penaltyReason : penaltyReasons.entrySet()) {
                BigDecimal penaltyAmount = penaltyReason.getValue();

                if (penaltyAmount.compareTo(BigDecimal.ZERO) > 0)
                    orderNo = prepareBillDetails(billDetailList, orderNo, dmd, penaltyAmount, penaltyReason.getKey(),
                            AdvertisementTaxConstants.DEMANDREASON_PENALTY);
            }
        }

        // TODO: GET TOTAL AMOUNT FOR WHICH TAX AND CESS APPLICABLE. IF AMOUNT GREATER THAN ZERO, THEN ONLY CALL BELOW ONE.
        if (serviceTaxAndCessCalculationRequired()) {
            final Installment currentInstallment = advertisementDemandService.getCurrentInstallment();
            // TODO: GET CURRENT INSTALLMENT

            for (final AdvertisementAdditionalTaxRate taxRates : additionalTaxRates)
                if (taxRates.getPercentage().compareTo(BigDecimal.ZERO) > 0)
                    orderNo = prepareBillDetails(billDetailList, orderNo, dmd,
                            totalTaxableAmount
                                    .multiply(taxRates.getPercentage())
                                    .divide(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_HALF_UP),
                            currentInstallment, taxRates.getReasonCode());
        }

        // TODO: IF LIST SIZE IS ZERO THEN RETURN NULL OR THROW EXCEPTION.
        return billDetailList;
    }

    private int prepareBillDetails(final List<EgBillDetails> billDetailList, int orderNo, final EgDemand dmd,
            final BigDecimal amount, final Installment installment, final String demandReasonCode) {
        final EgDemandReason pendingTaxReason = advertisementDemandService
                .getDemandReasonByCodeAndInstallment(demandReasonCode,
                        installment);
        if (pendingTaxReason != null) {
            final List<EgDemandDetails> demandDetail = advertisementDemandService.getDemandDetailByPassingDemandDemandReason(dmd,
                    pendingTaxReason);

            if (demandDetail != null && !demandDetail.isEmpty()) {
                final EgDemandDetails existingDemandDetail = demandDetail.get(0);
                final BigDecimal creaditAmt = existingDemandDetail.getAmount().subtract(
                        existingDemandDetail.getAmtCollected());
                final EgBillDetails billdetail = createBillDetailObject(
                        orderNo,
                        BigDecimal.ZERO,
                        creaditAmt.add(amount),
                        existingDemandDetail.getEgDemandReason().getGlcodeId().getGlcode(),
                        getReceiptDetailDescription(existingDemandDetail.getEgDemandReason()
                                .getEgDemandReasonMaster().getReasonMaster(),
                                existingDemandDetail.getEgDemandReason().getEgInstallmentMaster()));
                orderNo++;
                billDetailList.add(billdetail);

            } else {
                // Mean, with specific demand reason, there is no
                // entry present in current demand
                final EgBillDetails billdetail = createBillDetailObject(
                        orderNo,
                        BigDecimal.ZERO,
                        amount, pendingTaxReason.getGlcodeId().getGlcode(),
                        getReceiptDetailDescription(pendingTaxReason.getEgDemandReasonMaster()
                                .getReasonMaster(),
                                pendingTaxReason.getEgInstallmentMaster()));
                orderNo++;
                billDetailList.add(billdetail);

            }
        }
        return orderNo;
    }

    private String getReceiptDetailDescription(final String reasonType, final Installment instlment) {
        List<AdvertisementAdditionalTaxRate> additionalTaxRates = advertisementAdditinalTaxRateService
                .getAllActiveAdditinalTaxRates();
        List<String> taxTypeList = new ArrayList<>();
        for (AdvertisementAdditionalTaxRate advertisementAdditionalTaxRate : additionalTaxRates) {
            if (advertisementAdditionalTaxRate != null)
                taxTypeList.add(advertisementAdditionalTaxRate.getTaxType());
        }
        if (!taxTypeList.isEmpty() && taxTypeList.contains(reasonType)) {
            return reasonType + " " + AdvertisementTaxConstants.COLL_RECEIPTDETAIL_DESC_PREFIX;
        } else {
            return reasonType + " " + AdvertisementTaxConstants.COLL_RECEIPTDETAIL_DESC_PREFIX
                    + (instlment != null ? " " + instlment.getDescription() : "");
        }

    }

    private EgBillDetails createBillDetailObject(final int orderNo, final BigDecimal debitAmount,
            final BigDecimal creditAmount, final String glCodeForDemandDetail, final String description) {

        final EgBillDetails billdetail = new EgBillDetails();
        billdetail.setFunctionCode(AdvertisementTaxConstants.ADVERTISEMENT_FUCNTION_CODE);
        billdetail.setOrderNo(orderNo);
        billdetail.setCreateDate(new Date());
        billdetail.setModifiedDate(new Date());
        billdetail.setCrAmount(creditAmount);
        billdetail.setDrAmount(debitAmount);
        billdetail.setGlcode(glCodeForDemandDetail);
        billdetail.setDescription(description);
        billdetail.setAdditionalFlag(1);
        billdetail.setPurpose(PURPOSE.OTHERS.toString());
        return billdetail;
    }

    @Override
    public void cancelBill() {

    }

    @Override
    @Transactional
    public String getBillXML(final Billable billObj) {
        String collectXML;
        try {
            collectXML = URLEncoder.encode(super.getBillXML(billObj), "UTF-8");
        } catch (final UnsupportedEncodingException e) {

            throw new RuntimeException(e.getMessage());
        }
        return collectXML;
    }

    private Boolean serviceTaxAndCessCalculationRequired() {
        final AppConfigValues isServiceTaxAndCessCollectionRequired = appConfigValuesService.getConfigValuesByModuleAndKey(
                AdvertisementTaxConstants.MODULE_NAME, AdvertisementTaxConstants.SERVICETAXANDCESSCOLLECTIONREQUIRED).get(0);
        return isServiceTaxAndCessCollectionRequired != null
                && "YES".equalsIgnoreCase(isServiceTaxAndCessCollectionRequired.getValue()) ? true : false;
    }

    /**
     * API to return BillInfoImpl, used in tax payment through Mobile App
     *
     * @param mobileAdvertisementTaxDetails
     * @return
     */
    public BillInfoImpl getBillInfo(final BigDecimal amountToBePaid, final Advertisement advertisement) {
        advertisementBillable.setAdvertisement(advertisement);
        ApplicationThreadLocals.setUserId(2L);
        advertisementBillable.setReferenceNumber(AdvertisementTaxConstants.SERVICE_CODE.concat(String.format(
                "%s%06d", "", databaseSequenceProvider.getNextSequence(ADVERTISEMENT_BILLNUMBER))));
        final EgBill egBill = generateBill(advertisementBillable);
        return prepareBillForCollection(amountToBePaid, egBill, null);
    }

    @Transactional
    public BillInfoImpl prepareBillForCollection(final BigDecimal amountToBePaid, final EgBill bill,
            final String source) {
        return adtaxExternalService.prepareBillInfo(amountToBePaid, COLLECTIONTYPE.O, bill,
                source);
    }
}
