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

import static org.egov.ptis.constants.PropertyTaxConstants.APPCONFIG_KEY_BULKBILL_WARD;
import static org.egov.ptis.constants.PropertyTaxConstants.QUARTZ_BULKBILL_JOBS;

import java.util.List;

import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.scheduler.quartz.AbstractQuartzJob;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.service.bill.BillService;
import org.hibernate.Query;
import org.quartz.StatefulJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author nayeem
 *
 */
public class BulkBillGenerationJob extends AbstractQuartzJob implements
		StatefulJob {

	private static final Logger LOGGER = Logger
			.getLogger(BulkBillGenerationJob.class);

	private Integer billsCount;
	private Integer modulo;
	private UserManager userMgr;
	private BillService billService;
	protected PersistenceService persistenceService;
	@Autowired
	@Qualifier(value = "moduleDAO")
	private ModuleDao moduleDao;
	@Autowired
	private AppConfigValuesDAO appConfigValuesDao;

	public BulkBillGenerationJob() {
		LOGGER.info("BulkBillGenerationJob instantiated.........." + this);
	}

	@Override
	public void executeJob() {

		LOGGER.debug("Entered into executeJob" + modulo);

		Long currentTime = System.currentTimeMillis();

		Query query = getQuery();

		@SuppressWarnings("unchecked")
		List<String> indexNumbers = query.list();

		LOGGER.info("executeJob" + modulo + " - got " + indexNumbers
				+ "indexNumbers for bill generation");
		Long timeTaken = currentTime - System.currentTimeMillis();
		LOGGER.debug("executeJob" + modulo + " took " + (timeTaken / 1000)
				+ " secs for BasicProperty selection");
		LOGGER.debug("executeJob" + modulo + " - BasicProperties = "
				+ indexNumbers.size());
		LOGGER.info("executeJob" + modulo + " - Generating bills.....");

		currentTime = System.currentTimeMillis();
		int noOfBillsGenerated = 0;

		for (String indexNumber : indexNumbers) {
			BasicProperty basicProperty = null;
			try {
				basicProperty = (BasicProperty) getPersistenceService()
						.getSession()
						.createQuery(
								"from BasicPropertyImpl bp WHERE bp.upicNo = ?")
						.setString(0, indexNumber).uniqueResult();
				billService.generateBill(basicProperty,EgovThreadLocals.getUserId().intValue());
				basicProperty.setIsBillCreated('Y');
				noOfBillsGenerated++;
			} catch (Exception e) {
				basicProperty.setIsBillCreated('F');
				basicProperty.setBillCrtError(e.getMessage());
				String msg = " Error while generating Demand bill via BulkBillGeneration Job "
						+ modulo.toString();
				String propertyType = " for "
						+ (basicProperty.getIsMigrated().equals('Y') ? " migrated property "
								: " non-migrated property ");
				LOGGER.error(msg + propertyType + basicProperty.getUpicNo(), e);
			}
		}

		timeTaken = currentTime - System.currentTimeMillis();

		LOGGER.info("executeJob" + modulo + " - " + noOfBillsGenerated + "/"
				+ indexNumbers.size() + " Bill(s) generated in "
				+ (timeTaken / 1000) + " (secs)");

		LOGGER.debug("Exiting from executeJob" + modulo);
	}

	private Query getQuery() {

		StringBuilder queryString = new StringBuilder(200);
		StringBuilder wardAndPartNumbers = new StringBuilder();
		Installment currentInstallment = PropertyTaxUtil
				.getCurrentInstallment();
		Module ptModule = moduleDao
				.getModuleByName(PropertyTaxConstants.PTMODULENAME);

		// for Bulk bill generation for particular ward, getting the ward number
		// from
		// AppConfigValues, if available then by ward else irrespective of ward
		List<AppConfigValues> appConfigValues = appConfigValuesDao
				.getConfigValuesByModuleAndKey(PropertyTaxConstants.PTMODULENAME,
						APPCONFIG_KEY_BULKBILL_WARD);

		queryString = queryString
				.append("select bp.upicNo ")
				.append("from BasicPropertyImpl bp ")
				.append("where bp.active = true ")
				.append("and bp.upicNo IS not NULL ")
				.append("and (bp.isBillCreated is NULL or bp.isBillCreated='N') ")
				.append("and MOD(bp.id, ").append(QUARTZ_BULKBILL_JOBS)
				.append(") = :modulo ");

		if (!appConfigValues.isEmpty()) {
			wardAndPartNumbers.append("(");
			int count = 1;
			for (AppConfigValues appConfig : appConfigValues) {
				if (count == appConfigValues.size()) {
					wardAndPartNumbers.append("'").append(appConfig.getValue())
							.append("')");
				} else {
					wardAndPartNumbers.append("'").append(appConfig.getValue())
							.append("', ");
				}
				count++;
			}

			queryString.append(" AND ")
					.append("bp.propertyID.ward.boundaryNum||'-'||bp.partNo")
					.append(" IN ").append(wardAndPartNumbers.toString());
		}

		queryString = queryString
				.append(" AND bp NOT IN (SELECT bp FROM BasicPropertyImpl bp, EgBill b ")
				.append("WHERE bp.active = true ")
				.append("AND bp.upicNo = substring(b.consumerId, 1, instr(b.consumerId, '(')-1) ")
				.append("AND b.module = :ptModule ")
				.append("AND b.egBillType = :billType ")
				.append("AND b.is_History = 'N' ")
				.append("AND b.is_Cancelled = 'N' ")
				.append("AND (b.issueDate BETWEEN :fromDate AND :toDate)) ");

		Query query = getPersistenceService()
				.getSession()
				.createQuery(queryString.toString())
				.setInteger("modulo", modulo)
				.setEntity("ptModule", ptModule)
				.setEntity(
						"billType",
						billService.getPropertyTaxUtil().getBillTypeByCode(
								PropertyTaxConstants.BILLTYPE_MANUAL))
				.setDate("fromDate", currentInstallment.getFromDate())
				.setDate("toDate", currentInstallment.getToDate());

		query.setMaxResults(billsCount);

		return query;
	}

	public void setBillService(BillService billService) {
		this.billService = billService;
	}

	public Integer getBillsCount() {
		return billsCount;
	}

	public void setBillsCount(Integer billsCount) {
		this.billsCount = billsCount;
	}

	public Integer getModulo() {
		return modulo;
	}

	public void setModulo(Integer modulo) {
		this.modulo = modulo;
	}

	public UserManager getUserMgr() {
		return userMgr;
	}

	public void setUserMgr(UserManager userMgr) {
		this.userMgr = userMgr;
	}

	public BillService getBillService() {
		return billService;
	}

	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
}
