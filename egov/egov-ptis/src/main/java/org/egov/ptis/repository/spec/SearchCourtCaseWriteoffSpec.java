package org.egov.ptis.repository.spec;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.criteria.Predicate;

import org.egov.ptis.domain.entity.property.CourtVerdict;
import org.egov.ptis.domain.entity.property.WriteOff;
import org.egov.ptis.domain.entity.property.view.SearchCourtCaseWriteoffRequest;
import org.springframework.data.jpa.domain.Specification;

public class SearchCourtCaseWriteoffSpec {

    private static final String PROPERTY = "property";
    private static final String STATUS = "status";
    private static final String CREATED_DATE = "createdDate";
    private static final String STATE = "state";
    private static final String VALUE = "value";
    private static final String CLOSED = "Closed";

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
                if (searchCourtCaseWriteoffRequest.getApplicationStatus().equalsIgnoreCase("Open")) {
                    predicate.getExpressions()
                            .add(builder.notEqual(root.get(STATE).get(VALUE), CLOSED));
                } else {
                    predicate.getExpressions()
                            .add(builder.notEqual(root.get(PROPERTY).get(STATUS), 'W'));
                    predicate.getExpressions()
                            .add(builder.equal(root.get(STATE).get(VALUE), CLOSED));
                }
            if (searchCourtCaseWriteoffRequest.getFromDate() != null && searchCourtCaseWriteoffRequest.getToDate() != null) {
                predicate.getExpressions()
                        .add(builder.greaterThanOrEqualTo(root.get(CREATED_DATE), searchCourtCaseWriteoffRequest.getFromDate()));
                predicate.getExpressions()
                        .add(builder.lessThanOrEqualTo(root.get(CREATED_DATE), getDate(Calendar.getInstance())));
            }
            predicate.getExpressions()
                    .add(builder.equal(root.get(PROPERTY).get("propertyModifyReason"), "WRITE_OFF"));
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
                if (searchCourtCaseWriteoffRequest.getApplicationStatus().equalsIgnoreCase("Open")) {
                    predicate.getExpressions()
                            .add(builder.notEqual(root.get(STATE).get(VALUE), CLOSED));
                } else {
                    predicate.getExpressions()
                            .add(builder.notEqual(root.get(PROPERTY).get(STATUS), 'W'));
                    predicate.getExpressions()
                            .add(builder.equal(root.get(STATE).get(VALUE), CLOSED));
                }
            if (searchCourtCaseWriteoffRequest.getFromDate() != null && searchCourtCaseWriteoffRequest.getToDate() != null) {
                predicate.getExpressions()
                        .add(builder.greaterThanOrEqualTo(root.get(CREATED_DATE), searchCourtCaseWriteoffRequest.getFromDate()));
                predicate.getExpressions()
                        .add(builder.lessThanOrEqualTo(root.get(CREATED_DATE), getDate(Calendar.getInstance())));
            }
            predicate.getExpressions()
                    .add(builder.equal(root.get(PROPERTY).get("propertyModifyReason"), "COURTVERDICT"));
            return predicate;
        };
    }

    public static Date getDate(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
}
