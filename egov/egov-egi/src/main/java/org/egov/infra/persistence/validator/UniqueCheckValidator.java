/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infra.persistence.validator;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.Query;
import org.hibernate.Session;

public class UniqueCheckValidator implements ConstraintValidator<Unique, Object> {

    private Unique unique;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(final Unique unique) {
        this.unique = unique;
    }

    @Override
    public boolean isValid(final Object arg0, final ConstraintValidatorContext constraintValidatorContext) {
        if (unique.fields() == null || unique.fields().length == 0)
            return true;

        for (int i = 0; i < unique.fields().length; i++)
            if (!isUnique(arg0, unique.fields()[i], unique.columnName()[i])) {
                return false;
            }
        return true;
    }

    private boolean isUnique(final Object arg0, final String field, final String columnName) {
        final Object value = getValue(arg0, field);
        if (value == null)
            return true;
        final Long id = getId(arg0);
        final Session currentSession = entityManager.unwrap(Session.class);
        boolean isValid = true;
        if (id == null) {
            final Query q = currentSession.createSQLQuery("select * from " + unique.tableName() + " where upper("
                    + columnName + ")=:value");
            q.setString("value", value.toString().trim().toUpperCase());
            if (!q.list().isEmpty())
                isValid = false;
        } else {
            final Query q = currentSession.createSQLQuery("select * from " + unique.tableName() + " where upper("
                    + columnName + ")=:value and id!=:id");
            q.setString("value", value.toString().trim().toUpperCase());
            q.setLong("id", id);
            if (!q.list().isEmpty())
                isValid = false;
        }
        return isValid;
    }

    private Long getId(final Object arg0) {
        return (Long) getValue(arg0, unique.id());
    }

    private Object getValue(final Object target, final String field) {
        try {
            final BeanInfo info = java.beans.Introspector.getBeanInfo(target.getClass());
            final PropertyDescriptor[] props = info.getPropertyDescriptors();
            for (final PropertyDescriptor propertyDescriptor : props)
                if (propertyDescriptor.getName().equals(field))
                    return propertyDescriptor.getReadMethod().invoke(target);
            return null;
        } catch (final Exception e) {
            throw new EGOVRuntimeException(e.getMessage(), e);
        }
    }

}
