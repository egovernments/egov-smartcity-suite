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
import org.egov.infra.persistence.validator.annotation.CompositeUnique;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CompositeUniqueCheckValidator implements ConstraintValidator<CompositeUnique, Object> {

    private CompositeUnique unique;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(CompositeUnique unique) {
        this.unique = unique;
    }

    @Override
    public boolean isValid(Object arg0, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Number id = (Number) FieldUtils.readField(arg0, unique.id(), true);
            boolean isValid = checkCompositeUniqueKey(arg0, id);
            if (!isValid && unique.enableDfltMsg())
                for (String fieldName : unique.fields())
                    constraintValidatorContext.buildConstraintViolationWithTemplate(unique.message()).addPropertyNode(fieldName)
                            .addConstraintViolation();
            return isValid;
        } catch (IllegalAccessException e) {
            throw new ApplicationRuntimeException("Error while validating composite unique key", e);
        }

    }

    private boolean checkCompositeUniqueKey(Object arg0, Number id) throws IllegalAccessException {
        Criteria criteria = entityManager.unwrap(Session.class)
                .createCriteria(unique.isSuperclass() ? arg0.getClass().getSuperclass() : arg0.getClass())
                .setFlushMode(FlushMode.MANUAL)
                .setReadOnly(true);
        Conjunction conjunction = Restrictions.conjunction();
        for (String fieldName : unique.fields()) {
            Object fieldValue = FieldUtils.readField(arg0, fieldName, true);
            if (unique.checkForNull() && fieldValue == null)
                conjunction.add(Restrictions.isNull(fieldName));
            else if (fieldValue instanceof String)
                conjunction.add(Restrictions.eq(fieldName, fieldValue).ignoreCase());
            else
                conjunction.add(Restrictions.eq(fieldName, fieldValue));
        }
        if (id != null)
            conjunction.add(Restrictions.ne(unique.id(), id));
        return criteria.add(conjunction)
                .setProjection(Projections.id())
                .setMaxResults(1).uniqueResult() == null;
    }

}
