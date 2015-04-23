package org.egov.ptis.scheduler;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.APPCONFIG_KEY_WARDSFOR_TAXXMLMIGRTN;

import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.scheduler.quartz.AbstractQuartzJob;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.service.TaxXMLToDBCoverterService;
import org.egov.ptis.nmc.util.PropertyTaxNumberGenerator;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.quartz.StatefulJob;

public class TaxXMLMigrationJob extends AbstractQuartzJob implements StatefulJob {

	private static final Logger LOGGER = Logger.getLogger(TaxXMLMigrationJob.class);

	private PropertyTaxUtil propertyTaxUtil;
	private PersistenceService basicPrpertyService;
	private Integer propCount;
	private Integer modulo;
	private PropertyTaxNumberGenerator ptNumberGenerator;

	@SuppressWarnings("unchecked")
	@Override
	public void executeJob() {
		LOGGER.debug("Entered into TaxXMLMigrationJob.executeJob");
		List<BasicProperty> basicProperties = basicPrpertyService.getSession().createQuery(getQueryString())
				.setInteger("modulo", modulo).setMaxResults(propCount).list();

		Long currentTimeMillis = System.currentTimeMillis();

		LOGGER.info("job"+modulo+" tax XML migration is started ");

		for (BasicProperty basicProperty : basicProperties) {

			TaxXMLToDBCoverterService.createConverter(basicProperty, propertyTaxUtil, basicPrpertyService, ptNumberGenerator)
					.migrateTaxXML();
		}

		LOGGER.info("XML migration job"+modulo+" is completed in " + ((System.currentTimeMillis() - currentTimeMillis) / 1000)
				+ " sec(s)");
		LOGGER.debug("Exiting from TaxXMLMigrationJob.executeJob");
	}

	private String getQueryString() {
		LOGGER.debug("Entered into getQueryString");

		StringBuilder queryBuilder = new StringBuilder()
				.append("SELECT distinct bp FROM Ptdemand ptd ")
				.append("LEFT JOIN ptd.egptProperty p ")
				.append("LEFT JOIN p.basicProperty bp ")
				.append("WHERE ptd.dmdCalculations.taxInfo IS NOT NULL ")
				.append("AND p.status = 'A' ")
				.append("AND bp.active = true ")
				.append("AND bp.isTaxXMLMigrated <> 'F' ")
				.append("AND bp.isTaxXMLMigrated = 'N' ")
				.append("and MOD(bp.id, 2) = :modulo "); // TODO [CODE REVIEW] check the SQL that gets executed.
		// should be EAGERLY fetched, not lazily fetched else we'll have low performance for the job

		String wards = getWards();

		if (wards != null) {
				queryBuilder.append("AND bp.propertyID.ward.boundaryNum in ").append(wards);
		} else {
			LOGGER.debug("No ward is configured");
		}

		LOGGER.debug("Exiting from getQueryString");
		return queryBuilder.toString();
	}

	private String getWards() {

		LOGGER.debug("Entered into getWards" );

		List<AppConfigValues> appConfigValues = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO()
				.getConfigValuesByModuleAndKey(NMCPTISConstants.PTMODULENAME, APPCONFIG_KEY_WARDSFOR_TAXXMLMIGRTN);

		StringBuilder wardBuilder = new StringBuilder();

		if (!appConfigValues.isEmpty()) {
			wardBuilder.append("(");
			int count = 1;

			for (AppConfigValues appConfig : appConfigValues) {
				if (count == appConfigValues.size()) {
					wardBuilder.append("'").append(appConfig.getValue().trim()).append("')");
				} else {
					wardBuilder.append("'").append(appConfig.getValue().trim()).append("',");
				}
				count++;
			}

			String wards = wardBuilder.toString();
			LOGGER.debug("getWards - wards=" + wards);

			return wards;
		}
		LOGGER.debug("getWards - returning with null value");
		return null;
	}

	public void setBasicPrpertyService(PersistenceService basicPrpertyService) {
		this.basicPrpertyService = basicPrpertyService;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

	public Integer getPropCount() {
		return propCount;
	}

	public void setPropCount(Integer propCount) {
		this.propCount = propCount;
	}

	public Integer getModulo() {
		return modulo;
	}

	public void setModulo(Integer modulo) {
		this.modulo = modulo;
	}

	public void setPtNumberGenerator(PropertyTaxNumberGenerator ptNumberGenerator) {
		this.ptNumberGenerator = ptNumberGenerator;
	}

}