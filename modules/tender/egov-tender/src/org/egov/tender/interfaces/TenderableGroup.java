package org.egov.tender.interfaces;

import java.math.BigDecimal;
import java.util.Set;

import org.egov.tender.TenderableGroupType;

/**	TenderableGroup represent unit detail in each module - that is a grouping of Tenderable entities.
 *  Eg: Estimate/Indent Rate Contract in works and indent in stores.	  
 * 	Estimate and indent  objects should implement TenderableGroup interface.
 */
public interface TenderableGroup {

	/**
	 *  Return Object number. For instance: Estimate number, indent number or shop number.
	 */
	public String getNumber();
	
	/**
	 *  Return Object description. For instance: Estimate description, Indent description.
	 */
	public String getDescription();
	
	/**
	 *  Return Object Total estimated cost. Eg: This is total estimate amount.
	 */
	public BigDecimal getEstimatedCost();
	/**
	 * Use {@link TenderableGroupType} enum type to select type of tender group for respective module.
	 */
	public TenderableGroupType getTenderableGroupType();

	/**
	 * Returns the associated set of entities to be tendered out. A tenderable group can only be associated
	 * with one type of Tenderable entity.
	 * {@link Tenderable} represents entity in each module.
	 * Eg: Activity in works, items in stores and Shop in landestate module.  
	 * @return Set of Tenderable 
	 */	
	public Set<? extends Tenderable> getEntities();
}
