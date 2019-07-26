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
package org.egov.ptis.domain.service.courtverdict;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_SECOND_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.COURTCASE;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.ReceiptAmountInfo;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.demand.integration.TaxCollection;
import org.egov.demand.model.DemandDetailVariation;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.bean.demand.DemandDetail;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.CourtVerdict;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.repository.master.structureclassification.StructureClassificationRepository;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
import org.egov.ptis.master.service.PropertyUsageService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CourtVerdictDCBService extends TaxCollection {

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    StructureClassificationRepository structureDAO;
    @Autowired
    PropertyUsageService propertyUsageService;
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    private PersistenceService<T, Serializable> persistenceService;
    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<PropertyImpl> propertyWorkflowService;
    @Autowired
    private InstallmentHibDao installmentDao;
    @Autowired
    private PtDemandDao ptDemandDAO;
    @Autowired
    private CourtVerdictService courtVerdictService;
    @Autowired
    private PTBillServiceImpl ptBillServiceImpl;
    @Autowired
    private ModuleService moduleDao;

    private static final Logger LOGGER = Logger.getLogger(CourtVerdictService.class);

    public PropertyImpl modifyDemand(PropertyImpl newProperty, PropertyImpl oldProperty) {
        PropertyImpl modProperty = null;

        try {
            modProperty = (PropertyImpl) propertyService.modifyDemand(newProperty, oldProperty);
        } catch (final TaxCalculatorExeption e) {

            LOGGER.error("forward : There are no Unit rates defined for chosen combinations", e);
            return newProperty;
        }
        return modProperty;
    }

    public void updateDemandDetails(CourtVerdict courtVerdict) {

        Set<EgDemandDetails> demandDetails = propertyService.getCurrrentDemand(courtVerdict.getProperty()).getEgDemandDetails();
        for (final EgDemandDetails dmdDetails : demandDetails)
            for (final DemandDetail dmdDetailBean : courtVerdict.getDemandDetailBeanList()) {
                Boolean isUpdateAmount = Boolean.FALSE;
                Boolean isUpdateCollection = Boolean.FALSE;
                dmdDetailBean.setInstallment(installmentDao.findById(dmdDetailBean.getInstallment().getId(), false));
                if (dmdDetailBean.getRevisedAmount() != null
                        && dmdDetailBean.getInstallment()
                                .equals(dmdDetails.getEgDemandReason().getEgInstallmentMaster())
                        && dmdDetails.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()
                                .equalsIgnoreCase(dmdDetailBean.getReasonMaster()))
                    isUpdateAmount = true;

                if (dmdDetailBean.getRevisedCollection() != null
                        && dmdDetails.getEgDemand().getEgInstallmentMaster()
                                .equals(propertyTaxCommonUtils.getCurrentInstallment())
                        && dmdDetails.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()
                                .equalsIgnoreCase(dmdDetailBean.getReasonMaster())
                        && dmdDetails.getEgDemandReason().getEgInstallmentMaster()
                                .equals(dmdDetailBean.getInstallment()))
                    isUpdateCollection = true;

                if (isUpdateAmount) {
                    DemandDetailVariation dmdVar = persistDemandDetailVariation(dmdDetails, dmdDetailBean.getRevisedAmount(),
                            COURTCASE);
                    Set<DemandDetailVariation> variationSet = new HashSet<>();
                    variationSet.add(dmdVar);
                    dmdDetails.setDemandDetailVariation(variationSet);
                }
                if (isUpdateCollection)
                    dmdDetails.setAmtCollected(
                            dmdDetailBean.getRevisedCollection() != null ? dmdDetailBean.getRevisedCollection()
                                    : BigDecimal.ZERO);

                if (isUpdateAmount || isUpdateCollection) {
                    dmdDetails.setModifiedDate(new Date());
                    break;
                }
            }
        if (courtVerdict.getState() != null) {
            for (Ptdemand ptdemand : courtVerdict.getProperty().getPtDemandSet()) {

                ptdemand.setEgDemandDetails(demandDetails);
            }
        } else {
            List<Ptdemand> currPtdemand = courtVerdictService.getCurrPtDemand(courtVerdict);
            if (currPtdemand != null) {
                final Ptdemand ptdemand = (Ptdemand) currPtdemand.get(0).clone();
                ptdemand.setBaseDemand(getTotalDemand(demandDetails));
                ptdemand.setEgDemandDetails(demandDetails);
                ptdemand.setEgptProperty(courtVerdict.getProperty());
                ptdemand.getDmdCalculations().setCreatedDate(new Date());
                persistenceService.applyAuditing(ptdemand.getDmdCalculations());
                courtVerdict.getProperty().getPtDemandSet().clear();
                courtVerdict.getProperty().getPtDemandSet().add(ptdemand);
            }
        }
    }

    protected Module module() {
        return moduleDao.getModuleByName(PTMODULENAME);
    }

    private BigDecimal getTotalDemand(Set<EgDemandDetails> dmndDetails) {
        BigDecimal totalDmd = BigDecimal.ZERO;
        for (EgDemandDetails newDemandDetails : dmndDetails) {
            totalDmd = totalDmd.add(newDemandDetails.getAmount());
        }
        return totalDmd;
    }

    public Map<String, String> validateDemand(List<DemandDetail> demandDetailBeanList) {

        HashMap<String, String> errors = new HashMap<>();

        for (final DemandDetail dd : demandDetailBeanList) {
            dd.setInstallment(installmentDao.findById(dd.getInstallment().getId(), false));
            if (dd.getRevisedCollection() != null && dd.getRevisedAmount() != null) {
                if (dd.getRevisedCollection().compareTo(dd.getActualAmount().subtract(dd.getRevisedAmount())) > 0) {
                    errors.put("revisedCollection",
                            "revised.collection.greater");
                }
                if (dd.getRevisedAmount().compareTo(dd.getActualAmount()) > 0)
                    errors.put("revisedDemand", "reviseddmd.gt.actualdmd");
            }
            if (dd.getRevisedCollection() == null && dd.getRevisedAmount() != null)
                errors.put("revisedCollAmt", "mandatory.revised.collection");
        }
        return errors;
    }

    public void addDemandDetails(CourtVerdict courtVerdict) {

        List<DemandDetail> demandDetailList = getDemandDetails(courtVerdict);
        courtVerdict.setDemandDetailBeanList(demandDetailList);

    }

    private List<DemandDetail> setDemandBeanList(List<EgDemandDetails> newDmndDetails, List<EgDemandDetails> oldDmndDetails) {

        List<DemandDetail> demandDetailList = new ArrayList<>();

        int i = 0;
        for (final EgDemandDetails demandDetail : newDmndDetails) {
            for (final EgDemandDetails oldDemandDetail : oldDmndDetails) {
                if (oldDemandDetail.getEgDemandReason().getEgInstallmentMaster()
                        .equals(demandDetail.getEgDemandReason().getEgInstallmentMaster())
                        && oldDemandDetail.getEgDemandReason().getEgDemandReasonMaster()
                                .equals(demandDetail.getEgDemandReason().getEgDemandReasonMaster())) {
                    final Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();
                    final String reasonMaster = demandDetail.getEgDemandReason().getEgDemandReasonMaster()
                            .getReasonMaster();
                    BigDecimal revisedAmount = BigDecimal.ZERO;
                    for (DemandDetailVariation demandDetailVariation : demandDetail.getDemandDetailVariation()) {
                        if (demandDetailVariation.getDemandDetail().getId().equals(demandDetail.getId())
                                && demandDetailVariation.getDramount().compareTo(BigDecimal.ZERO) >= 0) {
                            revisedAmount = demandDetailVariation.getDramount();
                            break;
                        }
                    }

                    final BigDecimal revisedCollection = demandDetail.getAmtCollected();
                    final DemandDetail dmdDtl = createDemandDetailBean(installment, reasonMaster, oldDemandDetail.getAmount(),
                            revisedAmount,
                            oldDemandDetail.getAmtCollected(), revisedCollection);
                    demandDetailList.add(i, dmdDtl);

                    break;
                }
            }
            i++;
        }
        return demandDetailList;
    }

    private DemandDetail createDemandDetailBean(final Installment installment, final String reasonMaster,
            final BigDecimal amount, final BigDecimal revisedAmount, final BigDecimal amountCollected,
            final BigDecimal revisedCollection) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into createDemandDetailBean");
            LOGGER.debug("createDemandDetailBean - installment=" + installment + ", reasonMaster=" + reasonMaster
                    + ", amount=" + amount + ", amountCollected=" + amountCollected);
        }

        final DemandDetail demandDetail = new DemandDetail();
        demandDetail.setInstallment(installment);
        demandDetail.setReasonMaster(reasonMaster);
        demandDetail.setActualAmount(amount);
        demandDetail.setRevisedAmount(revisedAmount);
        demandDetail.setActualCollection(amountCollected);
        demandDetail.setRevisedCollection(revisedCollection);
        demandDetail.setIsCollectionEditable(true);
        return demandDetail;
    }

    public CourtVerdict updateDemand(CourtVerdict courtVerdict) {

        List<DemandDetail> demandDetailList = getDemandDetails(courtVerdict);
        BigDecimal totalCollectionAmt = BigDecimal.ZERO;
        for (DemandDetail demandDetail : demandDetailList) {
            if (demandDetail.getActualCollection().compareTo(demandDetail.getRevisedCollection()) >= 0)
                totalCollectionAmt = totalCollectionAmt.add(demandDetail.getActualCollection());
            else
                totalCollectionAmt = totalCollectionAmt.add(demandDetail.getRevisedCollection());
        }

        Ptdemand ptDemandNew = propertyService.getCurrrentDemand(courtVerdict.getProperty());

        if (ptDemandNew.getEgDemandDetails() != null) {
            for (EgDemandDetails egDemandDetails : ptDemandNew.getEgDemandDetails()) {

                totalCollectionAmt = updateCollection(totalCollectionAmt, egDemandDetails);
            }

            if (totalCollectionAmt.compareTo(BigDecimal.ZERO) > 0) {
                final Installment currSecondHalf = propertyTaxUtil.getInstallmentsForCurrYear(new Date())
                        .get(CURRENTYEAR_SECOND_HALF);
                final EgDemandDetails advanceDemandDetails = ptBillServiceImpl.getDemandDetail(ptDemandNew, currSecondHalf,
                        DEMANDRSN_CODE_ADVANCE);
                if (advanceDemandDetails == null) {
                    final EgDemandDetails dmdDetails = ptBillServiceImpl.insertDemandDetails(DEMANDRSN_CODE_ADVANCE,
                            totalCollectionAmt, currSecondHalf);
                    ptDemandNew.getEgDemandDetails().add(dmdDetails);
                } else
                    advanceDemandDetails.getAmtCollected().add(totalCollectionAmt);
            }
        }

        return courtVerdict;
    }

    private BigDecimal updateCollection(BigDecimal totalColl, EgDemandDetails newDemandDetail) {
        BigDecimal remaining = totalColl;
        if (newDemandDetail != null) {
            newDemandDetail.setAmtCollected(ZERO);
            if (remaining.compareTo(BigDecimal.ZERO) > 0) {
                if (remaining.compareTo(newDemandDetail.getAmount()) <= 0) {
                    newDemandDetail.setAmtCollected(remaining);
                    newDemandDetail.setModifiedDate(new Date());
                    remaining = BigDecimal.ZERO;
                } else {
                    newDemandDetail.setAmtCollected(newDemandDetail.getAmount());
                    newDemandDetail.setModifiedDate(new Date());
                    remaining = remaining.subtract(newDemandDetail.getAmount());
                }
            }
        }
        return remaining;
    }

    public List<DemandDetail> getDemandDetails(CourtVerdict courtVerdict) {
        Set<EgDemandDetails> newDemandDetails = (ptDemandDAO.getNonHistoryCurrDmdForProperty(courtVerdict.getProperty()))
                .getEgDemandDetails();
        Set<EgDemandDetails> oldDemandDetails = (ptDemandDAO
                .getNonHistoryCurrDmdForProperty(courtVerdict.getBasicProperty().getProperty()))
                        .getEgDemandDetails();
        List<EgDemandDetails> newDmndDetails = new ArrayList<>(newDemandDetails);
        List<EgDemandDetails> oldDmndDetails = new ArrayList<>(oldDemandDetails);

        if (!newDmndDetails.isEmpty())
            newDmndDetails = sortDemandDetails(newDmndDetails);

        if (!oldDmndDetails.isEmpty())
            oldDmndDetails = sortDemandDetails(oldDmndDetails);

        return setDemandBeanList(newDmndDetails, oldDmndDetails);
    }

    public List<DemandDetail> setDemandBeanList(List<EgDemandDetails> demandDetails) {

        List<DemandDetail> demandDetailList = new ArrayList<>();

        for (final EgDemandDetails demandDetail : demandDetails) {
            final Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();
            final String reasonMaster = demandDetail.getEgDemandReason().getEgDemandReasonMaster()
                    .getReasonMaster();
            final DemandDetail dmdDtl = createDemandDetailBean(installment, reasonMaster, demandDetail.getAmount(),
                    demandDetail.getAmtCollected());
            demandDetailList.add(dmdDtl);
        }
        return demandDetailList;
    }

    private DemandDetail createDemandDetailBean(final Installment installment, final String reasonMaster,
            final BigDecimal amount, final BigDecimal amountCollected) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into createDemandDetailBean");
            LOGGER.debug("createDemandDetailBean - installment=" + installment + ", reasonMaster=" + reasonMaster
                    + ", amount=" + amount + ", amountCollected=" + amountCollected);
        }

        final DemandDetail demandDetail = new DemandDetail();
        demandDetail.setInstallment(installment);
        demandDetail.setReasonMaster(reasonMaster);
        demandDetail.setActualAmount(amount);
        demandDetail.setActualCollection(amountCollected);
        demandDetail.setIsCollectionEditable(true);
        return demandDetail;
    }

    public List<EgDemandDetails> sortDemandDetails(List<EgDemandDetails> demandDetails) {
        Collections.sort(demandDetails, new Comparator<EgDemandDetails>() {

            @Override
            public int compare(EgDemandDetails dmdDtl1, EgDemandDetails dmdDtl2) {
                return dmdDtl1.getEgDemandReason().getEgInstallmentMaster()
                        .compareTo(dmdDtl2.getEgDemandReason().getEgInstallmentMaster());
            }

        }.thenComparing(new Comparator<EgDemandDetails>() {

            @Override
            public int compare(EgDemandDetails dmdDtl1, EgDemandDetails dmdDtl2) {
                return dmdDtl1.getEgDemandReason().getEgDemandReasonMaster().getOrderId()
                        .compareTo(dmdDtl2.getEgDemandReason().getEgDemandReasonMaster().getOrderId());
            }
        }));
        return demandDetails;
    }

    @Override
    public List<ReceiptDetail> reconstructReceiptDetail(String billReferenceNumber, BigDecimal actualAmountPaid,
            List<ReceiptDetail> receiptDetailList) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String constructAdditionalInfoForReceipt(BillReceiptInfo billReceiptInfo) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ReceiptAmountInfo receiptAmountBifurcation(BillReceiptInfo billReceiptInfo) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateDemandDetails(BillReceiptInfo bri) {
        // TODO Auto-generated method stub

    }
}
