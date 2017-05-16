package org.egov.portal.repository.specs;

import javax.persistence.criteria.Predicate;

import org.egov.portal.entity.PortalServiceType;
import org.springframework.data.jpa.domain.Specification;

public class SearchPortalServiceTypeSpec {

    private SearchPortalServiceTypeSpec() {

    }

    public static Specification<PortalServiceType> searchPortalServiceType(final Long module, final String name) {
        return (root, query, builder) -> {
            final Predicate predicate = builder.conjunction();
            if (module != null)
                predicate.getExpressions()
                        .add(builder.equal(root.get("module"), module));
            if (name != null && !"select".equalsIgnoreCase(name))
                predicate.getExpressions().add(builder.equal(root.get("name"), name));
            return predicate;
        };
    }
}
