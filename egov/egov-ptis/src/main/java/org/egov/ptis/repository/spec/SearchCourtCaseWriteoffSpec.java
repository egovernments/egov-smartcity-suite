package org.egov.ptis.repository.spec;

import javax.persistence.criteria.Predicate;

import org.egov.ptis.domain.entity.property.CourtVerdict;
import org.egov.ptis.domain.entity.property.WriteOff;
import org.egov.ptis.domain.entity.property.view.SearchCourtCaseWriteoffRequest;
import org.springframework.data.jpa.domain.Specification;

public class SearchCourtCaseWriteoffSpec {

    private SearchCourtCaseWriteoffSpec() {

    }

    public static Specification<WriteOff> writeoffSpecification(
            final SearchCourtCaseWriteoffRequest searchCourtCaseWriteoffRequest) {
        return (root, query, builder) -> {
            final Predicate predicate = builder.conjunction();
            if (searchCourtCaseWriteoffRequest.getConsumerCode() != null)
                predicate.getExpressions()
                        .add(builder.equal(root.get("basicProperty").get("upicNo"),
                                searchCourtCaseWriteoffRequest.getConsumerCode()));
            if (searchCourtCaseWriteoffRequest.getApplicationNumber() != null)
                predicate.getExpressions()
                        .add(builder.equal(root.get("applicationNumber"), searchCourtCaseWriteoffRequest.getApplicationNumber()));
            if (searchCourtCaseWriteoffRequest.getApplicationStatus() != null
                    && !searchCourtCaseWriteoffRequest.getApplicationStatus().equalsIgnoreCase("All"))
                if (searchCourtCaseWriteoffRequest.getApplicationStatus().equalsIgnoreCase("Open"))
                    predicate.getExpressions()
                            .add(builder.equal(root.get("property").get("status"), 'W'));
                else
                    predicate.getExpressions()
                            .add(builder.notEqual(root.get("property").get("status"), 'W'));
            if (searchCourtCaseWriteoffRequest.getFromDate() != null && searchCourtCaseWriteoffRequest.getToDate() != null) {
                predicate.getExpressions()
                        .add(builder.greaterThanOrEqualTo(root.get("createdDate"), searchCourtCaseWriteoffRequest.getFromDate()));
                predicate.getExpressions()
                        .add(builder.lessThanOrEqualTo(root.get("createdDate"), searchCourtCaseWriteoffRequest.getToDate()));
            }
            predicate.getExpressions()
                    .add(builder.equal(root.get("property").get("propertyModifyReason"), "WRITE_OFF"));
            return predicate;
        };
    }

    public static Specification<CourtVerdict> courtVerdictSpecification(
            final SearchCourtCaseWriteoffRequest searchCourtCaseWriteoffRequest) {
        return (root, query, builder) -> {
            final Predicate predicate = builder.conjunction();
            if (searchCourtCaseWriteoffRequest.getConsumerCode() != null)
                predicate.getExpressions()
                        .add(builder.equal(root.get("basicProperty").get("upicNo"),
                                searchCourtCaseWriteoffRequest.getConsumerCode()));
            if (searchCourtCaseWriteoffRequest.getApplicationNumber() != null)
                predicate.getExpressions()
                        .add(builder.equal(root.get("applicationNumber"), searchCourtCaseWriteoffRequest.getApplicationNumber()));
            if (searchCourtCaseWriteoffRequest.getApplicationStatus() != null
                    && !searchCourtCaseWriteoffRequest.getApplicationStatus().equalsIgnoreCase("All"))
                if (searchCourtCaseWriteoffRequest.getApplicationStatus().equalsIgnoreCase("Open"))
                    predicate.getExpressions()
                            .add(builder.equal(root.get("property").get("status"), 'W'));
                else
                    predicate.getExpressions()
                            .add(builder.notEqual(root.get("property").get("status"), 'W'));
            if (searchCourtCaseWriteoffRequest.getFromDate() != null && searchCourtCaseWriteoffRequest.getToDate() != null) {
                predicate.getExpressions()
                        .add(builder.greaterThanOrEqualTo(root.get("createdDate"), searchCourtCaseWriteoffRequest.getFromDate()));
                predicate.getExpressions()
                        .add(builder.lessThanOrEqualTo(root.get("createdDate"), searchCourtCaseWriteoffRequest.getToDate()));
            }
            predicate.getExpressions()
                    .add(builder.equal(root.get("property").get("propertyModifyReason"), "COURTVERDICT"));
            return predicate;
        };
    }
}
