/*
 * DemandDao.java Created on Nov 19, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.demand;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.egov.infstr.commons.Module;
import org.egov.infstr.dao.GenericDAO;
import org.egov.lib.admbndry.Boundary;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;

/**
 * TODO Brief Description of the puprpose of the class/interface
 * 
 * @author Administrator
 * @version 1.00
 * @see
 * @see
 * @since 1.00
 */
public interface PtDemandDao extends GenericDAO {
	/**
	 * This method called getCurrentDemandforProperty gets Total Current Demand
	 * Amount .
	 * 
	 * <p>
	 * This method returns Total Current Demand for given property.
	 * </p>
	 * 
	 * @param org
	 *            .egov.ptis.property.model.Property property
	 * 
	 * @return a BigDecimal.
	 * 
	 * 
	 */
	public BigDecimal getCurrentDemandforProperty(Property property);

	/**
	 * This method called WhetherBillExistsForProperty gets Character .
	 * 
	 * <p>
	 * This method returns Character for given Property , billnum and Module.
	 * </p>
	 * 
	 * @param org
	 *            .egov.ptis.property.model.Property property
	 * 
	 * @param java
	 *            .lang.Integer billnum
	 * 
	 * @param org
	 *            .egov.infstr.commons.Module module
	 * 
	 * @return Character of 'Y' or 'N'.
	 * 
	 * 
	 */
	public Character whetherBillExistsForProperty(Property property, String billnum, Module module);

	/**
	 * This method called getNonHistoryDemandForProperty gets EgptPtdemand
	 * Object which is NonHistory.
	 * 
	 * <p>
	 * This method returns EgptPtdemand Object for given property .
	 * </p>
	 * 
	 * @param org
	 *            .egov.ptis.property.model.Property property
	 * 
	 * @return EgptPtdemand Object.
	 * 
	 * 
	 */
	public Ptdemand getNonHistoryDemandForProperty(Property property);

	/**
	 * This method called getDmdDetailsByPropertyIdBoundary gets DemandDetails
	 * List .
	 * 
	 * <p>
	 * This method returns DemandDetails List for given propertyId &
	 * Boundary(Optional) .
	 * </p>
	 * 
	 * @param String
	 *            propertyId
	 * 
	 * @param org
	 *            .egov.lib.admbndry.Boundary divBoundary
	 * 
	 * @return DemandDetails List.
	 * 
	 * 
	 */

	public List getDmdDetailsByPropertyIdBoundary(BasicProperty basicProperty, Boundary divBoundary);

	/**
	 * This method called getAllDemands gets Map<EgDemandReason,Amount> .
	 * 
	 * <p>
	 * This method returns Map of DemandReason Objects and DemandDetails amount
	 * for given BasicProperty & divBoundary .
	 * </p>
	 * 
	 * @param org
	 *            .egov.ptis.property.model.BasicProperty BasicProperty
	 * 
	 * @param org
	 *            .egov.lib.admbndry.Boundary divBoundary
	 * 
	 * @return Map<EgDemandReason,Amount>.
	 * 
	 * 
	 */
	public Map getAllDemands(BasicProperty basicProperty, Boundary divBoundary);

	/**
	 * Gets the current 1) demand amount, 2) collected amount, 3) rebate amount
	 * for the demand associated with the given bill ID.
	 */
	public List<BigDecimal> getCurrentAmountsFromBill(Long billID);

	/**
	 * This method returns Map contains Demand and Collection for current and
	 * arrears years individually.<br>
	 * The return map will contain four key-value pairs described below.<br>
	 * CURR_DMD - Current Year Demand.<br>
	 * ARR_DMD - Current Year Collection.<br>
	 * CURR_COLL - Total Arrears years Demand.<br>
	 * ARR_COLL - Total Arrears years Collection.<br>
	 * 
	 * @param property
	 *            - Current installment non history default property.
	 * @return Map.
	 **/
	public Map<String, BigDecimal> getDemandCollMap(Property property);

	public Ptdemand getNonHistoryCurrDmdForProperty(Property property);
}
