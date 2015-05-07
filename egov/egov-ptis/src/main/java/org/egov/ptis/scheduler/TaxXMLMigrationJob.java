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
package org.egov.ptis.scheduler;

import static org.egov.ptis.constants.PropertyTaxConstants.APPCONFIG_KEY_WARDSFOR_TAXXMLMIGRTN;

import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.infstr.scheduler.quartz.AbstractQuartzJob;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.client.service.TaxXMLToDBCoverterService;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.quartz.StatefulJob;
import org.springframework.beans.factory.annotation.Autowired;

public class TaxXMLMigrationJob extends AbstractQuartzJob implements StatefulJob {

	private static final Logger LOGGER = Logger.getLogger(TaxXMLMigrationJob.class);

	private PropertyTaxUtil propertyTaxUtil;
	private PersistenceService basicPrpertyService;
	private Integer propCount;
	private Integer modulo;
	private PropertyTaxNumberGenerator ptNumberGenerator;
	@Autowired
	private AppConfigValuesDAO appConfigValuesDao;

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

		List<AppConfigValues> appConfigValues = appConfigValuesDao
				.getConfigValuesByModuleAndKey(PropertyTaxConstants.PTMODULENAME, APPCONFIG_KEY_WARDSFOR_TAXXMLMIGRTN);

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
