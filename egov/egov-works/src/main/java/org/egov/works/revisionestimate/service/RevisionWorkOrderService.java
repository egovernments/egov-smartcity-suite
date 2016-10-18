package org.egov.works.revisionestimate.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.egov.works.revisionestimate.entity.RevisionAbstractEstimate;
import org.egov.works.revisionestimate.entity.RevisionWorkOrder;
import org.egov.works.revisionestimate.repository.RevisionWorkOrderRepository;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.service.WorkOrderEstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RevisionWorkOrderService {

    @PersistenceContext
    private EntityManager entityManager;

    private final RevisionWorkOrderRepository revisionWorkOrderRepository;

    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;

    @Autowired
    private RevisionEstimateService revisionEstimateService;

    @Autowired
    public RevisionWorkOrderService(final RevisionWorkOrderRepository revisionWorkOrderRepository) {
        this.revisionWorkOrderRepository = revisionWorkOrderRepository;
    }

    @Transactional
    public RevisionWorkOrder create(final RevisionWorkOrder revisionWorkOrder) {
        return revisionWorkOrderRepository.save(revisionWorkOrder);
    }

    public RevisionWorkOrder getRevisionWorkOrderByParent(final Long id) {
        return revisionWorkOrderRepository.findByParent_Id(id);
    }

    public RevisionWorkOrder getRevisionWorkOrderById(final Long id) {
        return revisionWorkOrderRepository.findOne(id);
    }

    public String getRevisionEstimatesForWorkOrder(final Long workorderId) {
        final WorkOrderEstimate workOrderEstimate = workOrderEstimateService.getWorkOrderEstimateByWorkOrderId(workorderId);
        final List<RevisionAbstractEstimate> revisionEstimates = revisionEstimateService
                .findRevisionEstimatesByParentAndStatus(workOrderEstimate.getEstimate().getId());
        String revisionEstimateNumbers = StringUtils.EMPTY;
        for (final RevisionAbstractEstimate revisionAbstractEstimate : revisionEstimates)
            revisionEstimateNumbers += revisionAbstractEstimate.getEstimateNumber() + ",";

        if (revisionEstimateNumbers.endsWith(","))
            revisionEstimateNumbers = revisionEstimateNumbers.substring(0, revisionEstimateNumbers.length() - 2);

        return revisionEstimateNumbers;
    }

    public List<RevisionWorkOrder> findApprovedRevisionEstimatesByParent(final Long id) {
        return revisionWorkOrderRepository.findByParent_IdAndStatus(id,
                RevisionAbstractEstimate.RevisionEstimateStatus.APPROVED.toString());
    }
}
