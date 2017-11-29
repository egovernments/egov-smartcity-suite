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

package org.egov.infra.persistence.validator;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.validator.annotation.UniqueDateOverlap;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

import static org.egov.infra.utils.DateUtils.endOfDay;
import static org.egov.infra.utils.DateUtils.startOfDay;

public class UniqueDateOverlapValidator implements ConstraintValidator<UniqueDateOverlap, Object> {

    private UniqueDateOverlap uniqueDateOverlap;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(final UniqueDateOverlap uniqueDateOverlap) {
        this.uniqueDateOverlap = uniqueDateOverlap;
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            boolean isValid = checkUnique(object);
            if (!isValid)
                context.buildConstraintViolationWithTemplate(uniqueDateOverlap.message()).
                        addPropertyNode(uniqueDateOverlap.fromField()).addConstraintViolation();
            return isValid;
        } catch (final IllegalAccessException e) {
            throw new ApplicationRuntimeException("Error while validating unique key with date overlapping", e);
        }

    }

    private boolean checkUnique(Object object) throws IllegalAccessException {
        Number id = (Number) FieldUtils.readField(object, uniqueDateOverlap.id(), true);
        Criteria uniqueDateOverlapChecker = entityManager.unwrap(Session.class).createCriteria(object.getClass()).setReadOnly(true);
        Conjunction uniqueCheck = Restrictions.conjunction();
        for (String fieldName : uniqueDateOverlap.uniqueFields()) {
            Object fieldValue = FieldUtils.readField(object, fieldName, true);
            if (fieldValue instanceof String)
                uniqueCheck.add(Restrictions.eq(fieldName, fieldValue).ignoreCase());
            else
                uniqueCheck.add(Restrictions.eq(fieldName, fieldValue));
        }
        Date fromDate = startOfDay((Date) FieldUtils.readField(object, uniqueDateOverlap.fromField(), true));
        Date toDate = endOfDay((Date) FieldUtils.readField(object, uniqueDateOverlap.toField(), true));
        Conjunction checkFromDate = Restrictions.conjunction();
        checkFromDate.add(Restrictions.le(uniqueDateOverlap.fromField(), fromDate));
        checkFromDate.add(Restrictions.ge(uniqueDateOverlap.toField(), fromDate));
        Conjunction checkToDate = Restrictions.conjunction();
        checkToDate.add(Restrictions.le(uniqueDateOverlap.fromField(), toDate));
        checkToDate.add(Restrictions.ge(uniqueDateOverlap.toField(), toDate));
        Conjunction checkFromAndToDate = Restrictions.conjunction();
        checkFromAndToDate.add(Restrictions.ge(uniqueDateOverlap.fromField(), fromDate));
        checkFromAndToDate.add(Restrictions.le(uniqueDateOverlap.toField(), toDate));
        Disjunction dateRangeChecker = Restrictions.disjunction();
        dateRangeChecker.add(checkFromDate).add(checkToDate).add(checkFromAndToDate);
        uniqueCheck.add(dateRangeChecker);
        if (id != null)
            uniqueCheck.add(Restrictions.ne(uniqueDateOverlap.id(), id));
        return uniqueDateOverlapChecker.add(uniqueCheck).setProjection(Projections.id()).setMaxResults(1).uniqueResult() == null;
    }
}
