package org.egov.ptis.nmc.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.UnitCalculationDetail;
import org.egov.ptis.nmc.service.TaxXMLToDBCoverterService;
import org.egov.ptis.nmc.util.PropertyTaxUtil;

/**
 * The <code> TaxXmlToDbConverterAdapter </code> exposes methods which can be used to convert
 * Tax xml to Db
 *
 * @author nayeem
 *
 */
public class TaxXmlToDbConverterAdapter {

	private static final Logger LOGGER = Logger.getLogger(TaxXMLToDBCoverterService.class);


	private TaxXMLToDBCoverterService taxXmlToDbConverterService;


	public TaxXmlToDbConverterAdapter() { }

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

		Date modelPropOccupancyDate = taxXmlToDbConverterService.getPropertyTaxUtil().getPropertyOccupancyDate(
				propertyModel);
	
		if (oldProperty != null) {
			unitCalculationDetails.addAll(new HashSet<UnitCalculationDetail>(getUnitCalcDetailsClone(
					modelPropOccupancyDate, oldProperty)));
		}

		taxXmlToDbConverterService.getActivePropTaxCalc(modelPropOccupancyDate, propertyModel);

		taxXmlToDbConverterService.initCurrentUnitSlabs();

		Map<Date, Property> propertyByOccupancy = new HashMap<Date, Property>();
		propertyByOccupancy.put(modelPropOccupancyDate, propertyModel);

		taxXmlToDbConverterService.preparePropertyTypesByOccupancy(propertyByOccupancy);

		taxXmlToDbConverterService.setUnitIdentifierPrefix(taxXmlToDbConverterService.generateUnitIdentifierPrefix());

		unitCalculationDetails.addAll(taxXmlToDbConverterService.getTheRowsForChange(propertyModel));

		Property property = taxXmlToDbConverterService.addUnitCalculationDetails(unitCalculationDetails,
				propertyModel);

		LOGGER.debug("Exiting from convertXmlToDB");
		return property;
	}

	/**
	 * Gives the Copy of the UnitCalculationDetail before the Occupancy Date occupancyDate
	 *
	 * @param occupancyDate
	 * @param activeProperty
	 * @return List of UnitCalculationDetail
	 */
	@SuppressWarnings("unchecked")
	private List<UnitCalculationDetail> getUnitCalcDetailsClone(Date occupancyDate, Property oldProperty) {
		LOGGER.debug("Entered into getUnitCalcDetailsClone, occupancyDate=" + occupancyDate);

		List<UnitCalculationDetail> unitCalcDetailClones = new ArrayList<UnitCalculationDetail>();
		BasicProperty basicProperty = taxXmlToDbConverterService.getBasicProperty();

		Property latestProperty = PropertyTaxUtil.getLatestProperty(basicProperty, PropertyTaxConstants.STATUS_ISHISTORY);
		
		if (latestProperty == null) {
			latestProperty = oldProperty;
		}
		
		if (latestProperty != null) {
			String hqlUnitCalc = "from UnitCalculationDetail where property = ? "
					+ "and (fromDate is null or fromDate < ?) " + "and occupancyDate < ? "
					+ "and guidValEffectiveDate < ? ";

			List<UnitCalculationDetail> unitCalcDetails = HibernateUtil.getCurrentSession().createQuery(hqlUnitCalc)
					.setEntity(0, latestProperty)
					.setDate(1, occupancyDate)
					.setDate(2, occupancyDate)
					.setDate(3, occupancyDate).list();

			if (unitCalcDetails.isEmpty()) {
				LOGGER.debug("Existing Unit Calculations are not there before occupancyDate=" + occupancyDate);
			}

			for (UnitCalculationDetail unitCalcDetail : unitCalcDetails) {
				unitCalcDetailClones.add(new UnitCalculationDetail(unitCalcDetail));
			}
		}
		
		LOGGER.debug("Exiting from getUnitCalcDetailsClone");
		return unitCalcDetailClones;
	}

}
