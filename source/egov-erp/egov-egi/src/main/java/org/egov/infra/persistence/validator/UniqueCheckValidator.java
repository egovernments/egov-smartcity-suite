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
                constraintValidatorContext.buildConstraintViolationWithTemplate(unique.message())
                        .addPropertyNode(unique.fields()[i]).addConstraintViolation();
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
