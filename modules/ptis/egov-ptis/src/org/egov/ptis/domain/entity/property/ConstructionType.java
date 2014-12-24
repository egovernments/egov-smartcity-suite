/*
 * ConstructionType.java Created on Oct 20, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.entity.property;

import java.io.Serializable;
import java.util.Date;

import org.egov.lib.rjbac.user.User;

/**
 * <p>
 * This is an Interface which describes the various types of construction A
 * building is constructed of Floor, Wall, Roof, Wood etc. Each of them
 * represents a ConstructionType.
 * </p>
 * 
 * @author Gayathri Joshi
 * @version 2.00
 * @see org.egov.ptis.domain.entity.property.ConstructionTypeImpl
 * @since 2.00
 */
public interface ConstructionType extends Serializable {

	public Long getId();

	public void setId(Long id);

	public User getCreatedBy();

	public void setCreatedBy(User createdBy);

	public Date getCreatedDate();

	public void setCreatedDate(Date createdDate);

	public User getModifiedBy();

	public void setModifiedBy(User modifiedBy);

	public Date getModifiedDate();

	public void setModifiedDate(Date modifiedDate);

	public String getType();

	public String getCode();

	public String getName();

	public void setType(String type);

	public void setCode(String code);

	public void setName(String name);

}
