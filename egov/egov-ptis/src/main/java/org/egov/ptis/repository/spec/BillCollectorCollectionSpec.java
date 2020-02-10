package org.egov.ptis.repository.spec;

import javax.persistence.criteria.Predicate;

import org.egov.ptis.domain.entity.property.CollectionSummary;
import org.egov.ptis.domain.entity.property.view.BillCollectorCollectionRequest;
import org.springframework.data.jpa.domain.Specification;

public class BillCollectorCollectionSpec {

    private BillCollectorCollectionSpec() {

    }

    public static Specification<CollectionSummary> bcCollectionSpecification(
            final BillCollectorCollectionRequest billCollectorCollectionRequest) {
        return (root, query, builder) -> {
            final Predicate predicate = builder.conjunction();
            if (billCollectorCollectionRequest.getFromDate() != null)
                predicate.getExpressions()
                        .add(builder.greaterThanOrEqualTo(root.get("receiptDate"), billCollectorCollectionRequest.getFromDate()));
            if (billCollectorCollectionRequest.getToDate() != null)
                predicate.getExpressions()
                        .add(builder.lessThanOrEqualTo(root.get("receiptDate"), billCollectorCollectionRequest.getToDate()));
            if (billCollectorCollectionRequest.getUserId() != null && !billCollectorCollectionRequest.getUserId().equals("0"))
                predicate.getExpressions()
                        .add(builder.equal(root.get("user"), Integer.valueOf(billCollectorCollectionRequest.getUserId())));
            predicate.getExpressions()
                    .add(builder.equal(root.get("paidAt"), "SYSTEM"));
            predicate.getExpressions()
                    .add(builder.notEqual(root.get("paymentMode"), "online"));

            return predicate;
        };
    }
}
