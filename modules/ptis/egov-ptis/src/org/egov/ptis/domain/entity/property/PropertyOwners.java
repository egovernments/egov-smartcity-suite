/*
 * PropertyOwners.java Created on Nov 26, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.entity.property;

import org.egov.exceptions.EGOVRuntimeException;

/**
 * This class defines Property Owners i.e A Property can have multiple Owners at
 * same point of time
 * 
 * @author Neetu
 * @version 1.00
 * @see
 * @see
 * @since 1.00
 */
public class PropertyOwners {

	private Integer property_Owner_Id = null;

	private Integer ownerId = null;

	private Integer Id_Property = null;
	private PropertySource source;

	/**
	 * @return Returns the id_Property.
	 */
	public Integer getId_Property() {
		return Id_Property;
	}

	/**
	 * @param id_Property
	 *            The id_Property to set.
	 */
	public void setId_Property(Integer id_Property) {
		Id_Property = id_Property;
	}

	/**
	 * @return Returns the ownerId.
	 */
	public Integer getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId
	 *            The ownerId to set.
	 */
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * @return Returns the property_Owner_Id.
	 */
	public Integer getProperty_Owner_Id() {
		return property_Owner_Id;
	}

	/**
	 * @param property_Owner_Id
	 *            The property_Owner_Id to set.
	 */
	public void setProperty_Owner_Id(Integer property_Owner_Id) {
		this.property_Owner_Id = property_Owner_Id;
	}

	/**
	 * @return Returns the source.
	 */
	public PropertySource getSource() {
		return source;
	}

	/**
	 * @param source
	 *            The source to set.
	 */
	public void setSource(PropertySource source) {
		this.source = source;
	}

	/**
	 * @return Returns if the given Object is equal to PropertyStatusValues
	 */
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (this == that)
			return true;

		if (that.getClass() != this.getClass())
			return false;
		final PropertyOwners propertyOwners = (PropertyOwners) that;

		if (this.getId_Property() != null && propertyOwners.getId_Property() != null) {
			if (getId_Property().equals(propertyOwners.getId_Property())) {
				return true;
			} else
				return false;
		} else if (this.getOwnerId() != null && propertyOwners.getOwnerId() != null) {
			if (getOwnerId().equals(propertyOwners.getOwnerId())) {
				return true;
			} else
				return false;
		}

		else if (this.getProperty_Owner_Id() != null && propertyOwners.getProperty_Owner_Id() != null) {
			if (getProperty_Owner_Id().equals(propertyOwners.getProperty_Owner_Id())) {
				return true;
			} else
				return false;
		} else
			return false;
	}

	/**
	 * @return Returns the hashCode
	 */
	public int hashCode() {
		int hashCode = 0;
		if (this.getId_Property() != null) {
			hashCode += this.getId_Property().hashCode();
		} else if (this.getOwnerId() != null) {
			hashCode += this.getOwnerId().hashCode();
		} else if (this.getProperty_Owner_Id() != null) {
			hashCode += this.getProperty_Owner_Id().hashCode();
		}

		return hashCode;
	}

	/**
	 * @return Returns the boolean after validating the current object
	 */
	public boolean validate() {
		if (getId_Property() == null)
			throw new EGOVRuntimeException(
					"In PropertyOwners Validate : 'ID_Property' Attribute is Not Set, Please Check !!");
		if (getOwnerId() == null)
			throw new EGOVRuntimeException(
					"In PropertyOwners Validate : 'Owner ID' Attribute is Not Set, Please Check !!");
		return true;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getProperty_Owner_Id()).append("|Source: ").append(getSource());

		return objStr.toString();
	}
}
