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

import java.util.List;

import org.egov.infra.scheduler.quartz.AbstractQuartzJob;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.client.adapter.TaxCalculatorAdapter;
import org.egov.ptis.client.service.NonResidentialUnitTaxCalculator;
import org.egov.ptis.client.service.ResidentialUnitTaxCalculator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.MigratedPropertyFloor;
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
