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
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.persistence.validator.annotation.CompositeUnique;
import org.hibernate.Query;
import org.hibernate.Session;

public class CompositeUniqueCheckValidator implements ConstraintValidator<CompositeUnique, Object> {

    private CompositeUnique unique;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(final CompositeUnique unique) {
        this.unique = unique;
    }

    @Override
    public boolean isValid(final Object arg0, final ConstraintValidatorContext constraintValidatorContext) {
        if (unique.compositefields() == null || unique.compositefields().length == 0)
            return true;

        String fieldStr = "";
        String columnNameStr = "";
        /*
         * combine all Column names and Fileds and convert into String and Pass
         * to Query for Unique Check validation....
         */
        for (int i = 0; i < unique.compositefields().length; i++) {
            if (fieldStr.isEmpty())
                fieldStr = unique.compositefields()[i];
            else
                fieldStr = fieldStr + "," + unique.compositefields()[i];
            if (columnNameStr.isEmpty())
                columnNameStr = unique.compositecolumnName()[i];
            else
                columnNameStr = columnNameStr + "||''||" + unique.compositecolumnName()[i];
        }
        if (!isCompositeUnique(arg0, fieldStr, columnNameStr)) {
            final List<String> items = Arrays.asList(fieldStr.split(","));
            String concatenatedFieldValuesForErrorMesssages = new String();
            if (unique.enableDfltMsg())
                /*
                 * this Loop for passing all fields which n all allowed for
                 * Composite Unique Check validation and Show Error Messages for
                 * all fields in UI.
                 */
                for (final String asStringObj : items) {
                    constraintValidatorContext.buildConstraintViolationWithTemplate(unique.message()).addPropertyNode(asStringObj)
                            .addConstraintViolation();
                    concatenatedFieldValuesForErrorMesssages = concatenatedFieldValuesForErrorMesssages
                            .concat(constraintValidatorContext.getDefaultConstraintMessageTemplate());

                }
            return false;
        }
        return true;

    }

    private boolean isCompositeUnique(final Object arg0, final String field, final String columnName) {
        final Object value = getValue(arg0, field);
        if (value == null)
            return true;
        final Long id = getId(arg0);
        final Session currentSession = entityManager.unwrap(Session.class);
        boolean isValid = true;
        if (id == null) {
            final Query q = currentSession
                    .createSQLQuery("select * from " + unique.tableName() + " where upper(" + columnName + ")=:value");
            q.setString("value", value.toString().toUpperCase());
            if (!q.list().isEmpty())
                isValid = false;
        } else {
            final Query q = currentSession.createSQLQuery(
                    "select * from " + unique.tableName() + " where upper(" + columnName + ")=:value and id!=:id");
            q.setString("value", value.toString().toUpperCase());
            q.setLong("id", id);
            if (!q.list().isEmpty())
                isValid = false;
        }
        return isValid;
    }

    private Long getId(final Object arg0) {
        return (Long) getIdValue(arg0, unique.id());
    }

    private Object getValue(final Object target, final String field) {
        String concatenatedFieldValues = new String();
        try {
            final List<String> items = Arrays.asList(field.split(","));
            final BeanInfo info = java.beans.Introspector.getBeanInfo(target.getClass());
            final PropertyDescriptor[] props = info.getPropertyDescriptors();
            for (final String strlist : items)
                for (final PropertyDescriptor propertyDescriptor : props)
                    if (!propertyDescriptor.getName().equals("new"))
                        if (Class.forName(propertyDescriptor.getPropertyType().getName()).getName().equals("java.lang.String")) {
                            /*
                             * this If for Only String Fields
                             */
                            if (propertyDescriptor.getName().equals(strlist))

                                concatenatedFieldValues = concatenatedFieldValues
                                        .concat(propertyDescriptor.getReadMethod().invoke(target).toString());
                        } else /*
                                * This Loop will enter only Field is not String
                                * Type.ex:class,List ex: In AppConfig have
                                * requirement like Unique Combination of
                                * (key_Name and module_id From(id of Eg_module))
                                * so here in Else part Module composite Object
                                * to getId() method and returns id value..
                                */
                            if (propertyDescriptor.getName().equals(strlist)) {
                            /*
                             * if this
                             * "propertyDescriptor.getReadMethod().invoke(target)"
                             * returns any composite Object which is der in Ur
                             * Object then Pass That composite Object to getId()
                             * methos and Get just id of thet Composite Object.
                             */
                            final Long id = getId(propertyDescriptor.getReadMethod().invoke(target));
                            concatenatedFieldValues = concatenatedFieldValues.concat(id.toString());
                        }
            return concatenatedFieldValues;
        } catch (final Exception e) {
            throw new EGOVRuntimeException(e.getMessage(), e);
        }
    }

    private Object getIdValue(final Object target, final String field) {
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
