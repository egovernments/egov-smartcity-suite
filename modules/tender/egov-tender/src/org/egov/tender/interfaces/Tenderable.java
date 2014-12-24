package org.egov.tender.interfaces;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.infstr.commonMasters.EgUom;
import org.egov.tender.TenderableType;

/**
 * Tenderable represents entity that will be tendered out in each module.
 * Eg: Activity in works, items in stores and Shop in landestate module. 
 * Activity, items and shop objects should implement this interface. 
 */

public interface Tenderable {
	
	/**
	 * Use {@link TenderableType} enum type  to select tenderable type for each module.
	 */
	public TenderableType getTenderableType();
	
	/**
	 *  Return Number/code of each entity.
	 * 	@Return String 
	 */
	public String getNumber();
	
	/**
	 *  Returns the entity name.
	 * 	@Return String 
	 */
	public String getName();
	
	/**
	 * Returns Description of entity.
	 * @Return String 
	 */
	public String getDescription();
	
	/**
	 * Quantity requested for each entity. 
	 * @Return BigDecimal 
	 */
	public BigDecimal getRequestedQty();
	
	/**
	 * Expected Value of requested quantity. 
	 * @Return BigDecimal 
	 */
	public BigDecimal getRequestedValue();
	
	/**
	 * Expected date to finish work/supply of material 
	 * @Return Date 
	 */
	public Date getRequestedByDate();
	
	/**
	 * Unit of measurement used to measure quantity.
	 * @Return EgUom Object.
	 */
	public EgUom getRequestedUOM();
	
}
