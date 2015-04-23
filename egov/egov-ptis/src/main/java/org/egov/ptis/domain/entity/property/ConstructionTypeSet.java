/*
 * ConstructionTypeSet.java Created on Oct 20, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.entity.property;

import java.util.Set;

/**
 * ConstructionTypeSet is a generic interface for all implementations.
 * <p>
 * ConstructionTypeSet encapsulates a set of ConstructionType objects, uses
 * StructuralClassification. The Floor and Property aggregate objects are linked
 * to the ConstructionTypeSet Objects.
 * </p>
 * 
 * @author Gayathri Joshi
 * @version 2.00
 * @see org.egov.ptis.domain.entity.property.StructureClassification
 * @see org.egov.ptis.domain.entity.property.ConstructionType
 * @since 2.00
 */
public interface ConstructionTypeSet {

	/**
	 * This method returns StructureClassification object.
	 * 
	 * @return StructureClassification object
	 */
	public StructureClassification getStructureClassification();

	/**
	 * This method returns a Set of ConstructionType objects.
	 * 
	 * @return Set of ConstructionType objects
	 */
	public Set getConstructionTypes();

	/**
	 * This method sets a Set of ConstructionType objects to ConstructionTypeSet
	 * .
	 * 
	 * @param Set
	 *            of ConstructionType objects
	 */
	public void setConstructionTypes(Set constrTypes);

	/**
	 * This method adds ConstructionType object.
	 * 
	 * @param ConstructionType
	 *            object
	 */
	public void addConstructionType(ConstructionType constructionType);

	/**
	 * This method removes ConstructionType object.
	 * 
	 * @param ConstructionType
	 *            object
	 */
	public void removeConstructionType(ConstructionType constructionType);

}
