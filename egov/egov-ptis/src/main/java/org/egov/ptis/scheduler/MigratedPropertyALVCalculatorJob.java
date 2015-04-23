package org.egov.ptis.scheduler;

import java.util.List;

import org.egov.infstr.scheduler.quartz.AbstractQuartzJob;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.MigratedPropertyFloor;
import org.egov.ptis.nmc.adapter.TaxCalculatorAdapter;
import org.egov.ptis.nmc.service.NonResidentialUnitTaxCalculator;
import org.egov.ptis.nmc.service.ResidentialUnitTaxCalculator;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@SuppressWarnings("serial")
public class MigratedPropertyALVCalculatorJob extends AbstractQuartzJob implements StatefulJob {

	private static final Logger LOGGER = LoggerFactory.getLogger(MigratedPropertyALVCalculatorJob.class);

	private ResidentialUnitTaxCalculator residentialUnitTaxCalculator;
	private NonResidentialUnitTaxCalculator nonResidentialUnitTaxCalculator;
	private PersistenceService<MigratedPropertyFloor, Long> migratedPropFloorService;
	private PropertyTaxUtil propertyTaxUtil;
	
	@SuppressWarnings("unchecked")
	@Override
	public void executeJob() {
		LOGGER.debug("Entered into MigratedPropertyALVCalculatorJob.executeJob");
		Long currentTimeMillis = System.currentTimeMillis();

		List<BasicProperty> basicProperties = HibernateUtil
				.getCurrentSession()
				.createQuery(
						"select bp from PropertyImpl p LEFT JOIN p.basicProperty bp LEFT JOIN bp.propertyID pid " +
						"where p.status = 'A' " +
						"and bp.active = true " +
						"and bp.isMigrated = 'Y' " +
						"and pid.ward.id = 3555 " +
						"and bp.id not in (select psv.basicProperty from PropertyStatusValues psv " +
						"where psv.propertyStatus.statusCode='MODIFY') " +
						"and bp.upicNo not in (select propertyId from MigratedPropertyFloor)").setMaxResults(20)
				.list();

		for (BasicProperty basicProperty : basicProperties) {
			new TaxCalculatorAdapter(residentialUnitTaxCalculator, nonResidentialUnitTaxCalculator,
					migratedPropFloorService, propertyTaxUtil).calculateALV(basicProperty);
		}

		LOGGER.info("Calculated the ALV in " + ((System.currentTimeMillis() - currentTimeMillis) / 1000) + " sec(s)");
	}

	public void setResidentialUnitTaxCalculator(ResidentialUnitTaxCalculator residentialUnitTaxCalculator) {
		this.residentialUnitTaxCalculator = residentialUnitTaxCalculator;
	}

	public void setNonResidentialUnitTaxCalculator(NonResidentialUnitTaxCalculator nonResidentialUnitTaxCalculator) {
		this.nonResidentialUnitTaxCalculator = nonResidentialUnitTaxCalculator;
	}

	public void setMigratedPropFloorService(PersistenceService<MigratedPropertyFloor, Long> migratedPropFloorService) {
		this.migratedPropFloorService = migratedPropFloorService;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

}
