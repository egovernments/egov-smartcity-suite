package org.egov.adtax.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.adtax.entity.Hoarding;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.admin.master.service.ModuleService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import utils.AdvertisementTaxConstants;

@Service
@Transactional(readOnly = true)
public class AdvertisementDemandService {

    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    private ModuleService moduleService;

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public EgDemand createDemand(Hoarding hoarding) {

        EgDemand demand = null;
        Set<EgDemandDetails> demandDetailSet = new HashSet<EgDemandDetails>();
        Installment installment = getCurrentInstallment();
        BigDecimal totalDemandAmount = BigDecimal.ZERO;
        if (hoarding != null && hoarding.getDemandId() == null) {
            if (hoarding.getTaxAmount() != null) {
                demandDetailSet.add(createDemandDetail(
                        BigDecimal.valueOf(hoarding.getTaxAmount()),
                        getDemandReasonByCodeAndInstallment(AdvertisementTaxConstants.DEMANDREASON_ADVERTISEMENTTAX,
                                installment)));
                totalDemandAmount.add(BigDecimal.valueOf(hoarding.getTaxAmount()));
            }
            if (hoarding.getEncroachmentFee() != null) {
                demandDetailSet.add(createDemandDetail(
                        BigDecimal.valueOf(hoarding.getEncroachmentFee()),
                        getDemandReasonByCodeAndInstallment(AdvertisementTaxConstants.DEMANDREASON_ENCROCHMENTFEE,
                                installment)));
                totalDemandAmount.add(BigDecimal.valueOf(hoarding.getEncroachmentFee()));
            }
            demand = createDemand(demandDetailSet, installment, totalDemandAmount);
        }

        return demand;
    }

    public EgDemandReason getDemandReasonByCodeAndInstallment(final String demandReason, final Installment installment) {
        final Query demandQuery = getCurrentSession().getNamedQuery("DEMANDREASONBY_CODE_AND_INSTALLMENTID");
        demandQuery.setParameter(0, demandReason);
        demandQuery.setParameter(1, installment.getId());
        final EgDemandReason demandReasonObj = (EgDemandReason) demandQuery.uniqueResult();
        return demandReasonObj;
    }

    private EgDemand createDemand(Set<EgDemandDetails> demandDetailSet, Installment installment,
            BigDecimal totalDemandAmount) {
        final EgDemand egDemand = new EgDemand();
        egDemand.setEgInstallmentMaster(installment);
        egDemand.getEgDemandDetails().addAll(demandDetailSet);
        egDemand.setIsHistory("N");
        egDemand.setCreateDate(new Date());
        egDemand.setBaseDemand(totalDemandAmount);
        egDemand.setModifiedDate(new Date());
        return egDemand;
    }

    public EgDemand updateDemand(Hoarding hoarding) {
        // Double taxAmont, Double encroachmentFee
        return null;
    }

    protected Installment getCurrentInstallment() {
        return installmentDao.getInsatllmentByModuleForGivenDateAndInstallmentType(
                moduleService.getModuleByName(AdvertisementTaxConstants.MODULE_NAME), new Date(),
                AdvertisementTaxConstants.YEARLY);

    }

    protected EgDemandDetails createDemandDetail(BigDecimal amount, EgDemandReason demandReason) {
        EgDemandDetails dmdDet = new EgDemandDetails();
        dmdDet.setAmount(amount);
        dmdDet.setAmtCollected(BigDecimal.ZERO);
        dmdDet.setAmtRebate(BigDecimal.ZERO);
        dmdDet.setEgDemandReason(demandReason);
        dmdDet.setCreateDate(new Date());
        dmdDet.setModifiedDate(new Date());
        return dmdDet;
    }

}
