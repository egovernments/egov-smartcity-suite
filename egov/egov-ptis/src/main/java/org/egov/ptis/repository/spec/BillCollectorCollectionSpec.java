package org.egov.ptis.repository.spec;

import java.util.Calendar;

import javax.persistence.criteria.Predicate;

import org.egov.ptis.domain.entity.property.CollectionSummary;
import org.egov.ptis.domain.entity.property.view.BillCollectorCollectionRequest;
import org.springframework.data.jpa.domain.Specification;

public class BillCollectorCollectionSpec {

    public static Specification<CollectionSummary> bcCollectionSpecification(
            final BillCollectorCollectionRequest billCollectorCollectionRequest) {
        return (root, query, builder) -> {
            Calendar cal = Calendar.getInstance();

            final Predicate predicate = builder.conjunction();
            if (billCollectorCollectionRequest.getFromDate() != null) {
                predicate.getExpressions()
                        .add(builder.greaterThanOrEqualTo(root.get("receiptDate"), billCollectorCollectionRequest.getFromDate()));
            }
            if (billCollectorCollectionRequest.getToDate() != null) {
                cal.setTime(billCollectorCollectionRequest.getToDate());
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 999);
                predicate.getExpressions()
                        .add(builder.lessThanOrEqualTo(root.get("receiptDate"), cal.getTime()));
            }
            if (billCollectorCollectionRequest.getUserId() != null && !billCollectorCollectionRequest.getUserId().equals("0"))
                predicate.getExpressions()
                        .add(builder.equal(root.get("user"), Integer.valueOf(billCollectorCollectionRequest.getUserId())));
            predicate.getExpressions()
                    .add(builder.equal(root.get("paidAt"), "SYSTEM"));
            predicate.getExpressions()
                    .add(builder.notEqual(root.get("paymentMode"), "online"));
            predicate.getExpressions()
                    .add(builder.notEqual(root.get("status").get("code"), "CANCELLED"));
            return predicate;
        };
    }

}
