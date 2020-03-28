package org.egov.ptis.repository.spec;

import java.util.Calendar;

import javax.persistence.criteria.Predicate;

import org.egov.ptis.domain.entity.property.view.BillCollectorCollectionRequest;
import org.egov.ptis.domain.entity.property.view.CollectionIndexInfo;
import org.springframework.data.jpa.domain.Specification;

public class BillCollectorCollectionBookSpec {

    public static Specification<CollectionIndexInfo> bcCollectionSpecification(
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
            if (billCollectorCollectionRequest.getUserId() != null && !billCollectorCollectionRequest.getUserId().equals("-1"))
                predicate.getExpressions()
                        .add(builder.equal(root.get("createdBy"), Integer.valueOf(billCollectorCollectionRequest.getUserId())));
            predicate.getExpressions()
                    .add(builder.equal(root.get("channel"), "SYSTEM"));
            predicate.getExpressions()
                    .add(builder.notEqual(root.get("paymentMode"), "online"));
            return predicate;
        };
    }

}
