/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.infra.persistence.validator;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.validator.annotation.CompareDates;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.util.Date;

public class CompareDatesValidator implements ConstraintValidator<CompareDates, Date> {

    private CompareDates compareDates;

    @Override
    public void initialize(final CompareDates compareDates) {
        this.compareDates = compareDates;
    }

    @Override
    public boolean isValid(final Date date, final ConstraintValidatorContext arg1) {
        if (compareDates.fromDate() == null || compareDates.toDate() == null || compareDates.dateFormat() == null)
            return false;

        return dateValidation(date, compareDates.fromDate(), compareDates.toDate());
    }

    private boolean dateValidation(final Date date, final String field1, final String field2) {
        final Date fromDate = getValue(date, field1);
        final Date toDate = getValue(date, field2);
        if (fromDate == null || toDate == null)
            return false;

        return fromDate.before(toDate);
    }

    private Date getValue(final Date target, final String field) {
        try {
            final BeanInfo info = java.beans.Introspector.getBeanInfo(target.getClass());
            final PropertyDescriptor[] props = info.getPropertyDescriptors();
            for (final PropertyDescriptor propertyDescriptor : props)
                if (propertyDescriptor.getName().equals(field))
                    return (Date) propertyDescriptor.getReadMethod().invoke(target);
            return null;
        } catch (final Exception e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }
}
