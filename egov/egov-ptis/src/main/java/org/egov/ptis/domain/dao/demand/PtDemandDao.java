/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.ptis.domain.dao.demand;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Module;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PtDemandDao {
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
	
	public Map<String, BigDecimal> getDemandCollMapIncludingPenaltyAndAdvance(final Property property);
	
	public Map<String, BigDecimal> getDemandIncludingPenaltyCollMap(Property property); 
	
	public Map<String, BigDecimal> getPenaltyDemandCollMap(Property property);
	
	public Ptdemand getNonHistoryCurrDmdForProperty(Property property);

	public Ptdemand findById(Integer id, boolean lock);

	public List<Ptdemand> findAll();

	public Ptdemand create(Ptdemand ptdemand);

	public void delete(Ptdemand ptdemand);

	public Ptdemand update(Ptdemand ptdemand);
	
	public List<Object> getPropertyTaxDetails(String assessmentNo);
	public Set<String> getDemandYears(String assessmentNo);
	public List<Object> getTaxDetailsForWaterConnection(String assessmentNo,String connectionType) ;
}
