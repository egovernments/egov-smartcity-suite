/*
 * AbstractProperty.java Created on Oct 21, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.entity.property;

/**
 * The Abstract Class for the Property
 * 
 * @author Neetu
 * @version 2.00
 * @see org.egov.ptis.property.model.PropertyDetail,
 *      org.egov.ptis.property.model.PropertyImpl
 */
public abstract class AbstractProperty extends PropertyImpl implements PropertyDetail {
	public PropertyImpl propertyImpl;
}
