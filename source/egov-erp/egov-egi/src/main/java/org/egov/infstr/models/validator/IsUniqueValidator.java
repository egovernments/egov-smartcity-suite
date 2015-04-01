/*
 * @(#)IsUniqueValidator.java 3.0, 17 Jun, 2013 2:44:10 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.models.validator;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;

public class IsUniqueValidator implements ConstraintValidator<Unique, Object> {

	private Unique unique;

	@Override
	public void initialize(final Unique unique) {
		this.unique = unique;
	}

	@Override
	public boolean isValid(Object arg0, ConstraintValidatorContext constraintValidatorContext) {
		if (this.unique.fields() == null || this.unique.fields().length == 0) {
			return true;
		}

                for (int i = 0; i < this.unique.fields().length; i++) {
                    if (!this.isUnique(arg0, this.unique.fields()[i], this.unique.columnName()[i])) {
                        constraintValidatorContext
                                .buildConstraintViolationWithTemplate(this.unique.message())
                                .addNode(this.unique.fields()[i]).addConstraintViolation();
                        return false;
                    }
                }
		return true;
	}

	private boolean isUnique(final Object arg0, final String field, final String columnName) {
		final Object value = this.getValue(arg0, field);
		if (value == null) {
			return true;
		}
		final Long id = this.getId(arg0);
		final Session currentSession = HibernateUtil.getCurrentSession();
		final FlushMode currentFlushMode = currentSession.getFlushMode();
		currentSession.setFlushMode(FlushMode.MANUAL);
		boolean isValid = true;
		if (id == null) {
			final Query q = currentSession.createSQLQuery("select * from " + this.unique.tableName() + " where upper(" + columnName + ")=:value");
			q.setString("value", value.toString().trim().toUpperCase());
			if (!q.list().isEmpty()) {
				isValid = false;
			}
		} else {
			final Query q = currentSession.createSQLQuery("select * from " + this.unique.tableName() + " where upper(" + columnName + ")=:value and id!=:id");
			q.setString("value", value.toString().trim().toUpperCase());
			q.setLong("id", id);
			if (!q.list().isEmpty()) {
				isValid = false;
			}
		}
		currentSession.setFlushMode(currentFlushMode);
		return isValid;
	}

	private Long getId(final Object arg0) {
		return (Long) this.getValue(arg0, this.unique.id());
	}

	private Object getValue(final Object target, final String field) {
		try {
			final BeanInfo info = java.beans.Introspector.getBeanInfo(target.getClass());
			final PropertyDescriptor[] props = info.getPropertyDescriptors();
			for (final PropertyDescriptor propertyDescriptor : props) {
				if (propertyDescriptor.getName().equals(field)) {
					return propertyDescriptor.getReadMethod().invoke(target);
				}
			}
			return null;
		} catch (final Exception e) {
			throw new EGOVRuntimeException(e.getMessage(), e);
		}
	}

}
