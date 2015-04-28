/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.client.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.client.service.TaxXMLToDBCoverterService;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.UnitCalculationDetail;

/**
 * The <code> TaxXmlToDbConverterAdapter </code> exposes methods which can be
 * used to convert Tax xml to Db
 *
 * @author nayeem
 *
 */
public class TaxXmlToDbConverterAdapter {

	private static final Logger LOGGER = Logger.getLogger(TaxXMLToDBCoverterService.class);

	private TaxXMLToDBCoverterService taxXmlToDbConverterService;

	public TaxXmlToDbConverterAdapter() {
	}

	public TaxXmlToDbConverterAdapter(TaxXMLToDBCoverterService taxXmlToDbConverterService) {
		this.taxXmlToDbConverterService = taxXmlToDbConverterService;
	}

	/**
	 * Converts the Xml to DB for Notice generation
	 *
	 * @param propertyModel
	 * @return Property the PropertyImpl
	 * @throws Exception
	 */
	public Property convertXmlToDB(Property propertyModel, Property oldProperty) throws Exception {
		LOGGER.debug("Entered into convertXmlToDB");
		Set<UnitCalculationDetail> unitCalculationDetails = new HashSet<UnitCalculationDetail>();

		Date modelPropOccupancyDate = taxXmlToDbConverterService.getPropertyTaxUtil()
				.getPropertyOccupancyDate(propertyModel);

		if (oldProperty != null) {
			unitCalculationDetails.addAll(new HashSet<UnitCalculationDetail>(
					getUnitCalcDetailsClone(modelPropOccupancyDate, oldProperty)));
		}

		taxXmlToDbConverterService.getActivePropTaxCalc(modelPropOccupancyDate, propertyModel);

		taxXmlToDbConverterService.initCurrentUnitSlabs();

		Map<Date, Property> propertyByOccupancy = new HashMap<Date, Property>();
		propertyByOccupancy.put(modelPropOccupancyDate, propertyModel);

		taxXmlToDbConverterService.preparePropertyTypesByOccupancy(propertyByOccupancy);

		taxXmlToDbConverterService.setUnitIdentifierPrefix(taxXmlToDbConverterService
				.generateUnitIdentifierPrefix());

		unitCalculationDetails
				.addAll(taxXmlToDbConverterService.getTheRowsForChange(propertyModel));

		Property property = taxXmlToDbConverterService.addUnitCalculationDetails(
				unitCalculationDetails, propertyModel);

		LOGGER.debug("Exiting from convertXmlToDB");
		return property;
	}

	/**
	 * Gives the Copy of the UnitCalculationDetail before the Occupancy Date
	 * occupancyDate
	 *
	 * @param occupancyDate
	 * @param activeProperty
	 * @return List of UnitCalculationDetail
	 */
	@SuppressWarnings("unchecked")
	private List<UnitCalculationDetail> getUnitCalcDetailsClone(Date occupancyDate,
			Property oldProperty) {
		LOGGER.debug("Entered into getUnitCalcDetailsClone, occupancyDate=" + occupancyDate);

		List<UnitCalculationDetail> unitCalcDetailClones = new ArrayList<UnitCalculationDetail>();
		BasicProperty basicProperty = taxXmlToDbConverterService.getBasicProperty();

		Property latestProperty = PropertyTaxUtil.getLatestProperty(basicProperty,
				PropertyTaxConstants.STATUS_ISHISTORY);

		if (latestProperty == null) {
			latestProperty = oldProperty;
		}

		if (latestProperty != null) {
			String hqlUnitCalc = "from UnitCalculationDetail where property = ? "
					+ "and (fromDate is null or fromDate < ?) " + "and occupancyDate < ? "
					+ "and guidValEffectiveDate < ? ";

			List<UnitCalculationDetail> unitCalcDetails = HibernateUtil.getCurrentSession()
					.createQuery(hqlUnitCalc).setEntity(0, latestProperty)
					.setDate(1, occupancyDate).setDate(2, occupancyDate).setDate(3, occupancyDate)
					.list();

			if (unitCalcDetails.isEmpty()) {
				LOGGER.debug("Existing Unit Calculations are not there before occupancyDate="
						+ occupancyDate);
			}

			for (UnitCalculationDetail unitCalcDetail : unitCalcDetails) {
				unitCalcDetailClones.add(new UnitCalculationDetail(unitCalcDetail));
			}
		}

		LOGGER.debug("Exiting from getUnitCalcDetailsClone");
		return unitCalcDetailClones;
	}

}
