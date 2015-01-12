/*
 * @(#)BaseModel.java 3.0, 17 Jun, 2013 2:45:06 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.models;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.annotation.Introspection;
import org.egov.lib.rjbac.user.User;
import org.hibernate.search.annotations.DocumentId;

public class BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;
	/*
	 * Base class cannot be indexed only subclasses can be indexed for Lucene refer: http://opensource.atlassian.com/projects/hibernate/browse/HSEARCH-333
	 */
	@DocumentId
	protected Long id;
	protected User createdBy;
	protected Date createdDate;
	protected User modifiedBy;
	protected Date modifiedDate;

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public User getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ValidationError> validate() {
		return new ArrayList<ValidationError>();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{ ");
		sb.append("class : ").append(this.getClass().getSimpleName()).append(", ");
		try {
			Field[] fields = this.getClass().getDeclaredFields();
			for (Field field : fields) {
				try {
					if (field.isAnnotationPresent(Introspection.class)) {
						if (!field.isAccessible()) {
							field.setAccessible(true);
						}
						Object val = field.get(this);
						if (val != null) {
							String fieldName = field.getAnnotation(Introspection.class).value();
							sb.append((fieldName.trim().equals("") ? field.getName() : fieldName)).append(" : ").append(val).append(", ");
						}
					}
				} catch (Exception e) {
					throw new EGOVRuntimeException("Internal Server Error ", e);
				}

			}
		} catch (Exception e) {
			sb.append(e.toString());
		}
		sb.append(" }");
		sb.deleteCharAt(sb.lastIndexOf(","));
		return sb.toString();
	}

}
