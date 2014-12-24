package org.egov.ptis.scheduler;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.APPCONFIG_KEY_BULKBILL_WARD;

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.client.filter.SetDomainJndiHibFactNames;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.service.bill.BillService;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 
 * @author nayeem
 *
 */
public class BulkBillGenerationJob extends QuartzJobBean {
		
	private static final Logger LOGGER = Logger.getLogger(BulkBillGenerationJob.class);
    private static final String SUPER_USER_NAME = "egovernments";
	
	private String city;
	private String userName;
	private Integer billsCount;
		
	private UserService userService;	
	private BillService billService;
	
	public BulkBillGenerationJob () {}
	
	@Override
	protected void executeInternal(JobExecutionContext context) {
			
		LOGGER.debug("Entered into executeInternal");
		
		setThreadLocals(context);
		
		Long currentTime = System.currentTimeMillis();
		
		Query query = getQuery();				
		
		@SuppressWarnings("unchecked")
		List<String> indexNumbers = query.list();
		
		LOGGER.debug("executeInternal - indexNumbers : " + indexNumbers);
		Long timeTaken = currentTime - System.currentTimeMillis();
		LOGGER.debug("executeInternal took " + (timeTaken/1000) + " secs for BasicProperty selection");
		LOGGER.debug("executeInternal - BasicProperties = " + indexNumbers.size());
		LOGGER.info("executeInternal - Generating bills.....");
		
		currentTime = System.currentTimeMillis();
		int noOfBillsGenerated = 0;
		
		for (String indexNumber : indexNumbers) {
			BasicProperty basicProperty = null;
			try {
				HibernateUtil.beginTransaction();
				basicProperty = (BasicProperty) HibernateUtil.getCurrentSession()
						.createQuery("from BasicPropertyImpl bp WHERE bp.upicNo = ?")
						.setString(0, indexNumber)
						.uniqueResult();
				billService.getPropertyTaxUtil().makeTheEgBillAsHistory(basicProperty);
				billService.generateBill(basicProperty, Integer.valueOf(EGOVThreadLocals.getUserId()));
				HibernateUtil.commitTransaction();
				noOfBillsGenerated++;
			} catch (Exception e) {
				HibernateUtil.rollbackTransaction();
				String msg = " Error while generating Demand bill via BulkBillGeneration Job ";
				String propertyType = " for " + (basicProperty.getIsMigrated().equals('Y') 
											? " migrated property "
											: " non-migrated property "
										);
				LOGGER.info(noOfBillsGenerated + " Bill(s) generated...");
				LOGGER.error(msg + propertyType + basicProperty.getUpicNo(), e);				
			} finally {
				LOGGER.debug("executeInternal - Closing single Hibernate Session ");
				HibernateUtil.closeSession();
			}
		}
		
		timeTaken = currentTime - System.currentTimeMillis();
		
		LOGGER.info("executeInternal - " + indexNumbers.size() + " Bill(s) generated in " + (timeTaken/1000) + " (secs)");
		
		LOGGER.debug("Exiting from executeJob");
	}

	private Query getQuery() {
		
		StringBuilder queryString = new StringBuilder(200);
		Session currentSession = HibernateUtil.getCurrentSession();

		Installment currentInstallment = PropertyTaxUtil.getCurrentInstallment();
		Module ptModule = GenericDaoFactory.getDAOFactory().getModuleDao()
				.getModuleByName(NMCPTISConstants.PTMODULENAME);
		
		// for Bulk bill generation for particular ward, getting the ward number from 
		// AppConfigValues, if available then by ward else irrespective of ward
		List<AppConfigValues> appConfigValues = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO()
				.getConfigValuesByModuleAndKey(NMCPTISConstants.PTMODULENAME, APPCONFIG_KEY_BULKBILL_WARD);

		queryString = queryString.append("SELECT bp.upicNo ")
				.append("FROM BasicPropertyImpl bp ")
				.append("WHERE bp.active = true ")
				.append("AND bp.upicNo IS NOT NULL ");

		if (!appConfigValues.isEmpty()) {
			if (StringUtils.isNotBlank(appConfigValues.get(0).getValue())) {
				queryString = queryString.append(" AND bp.propertyID.ward.boundaryNum = ").append(
						new BigInteger(appConfigValues.get(0).getValue()));
			}
		}
		
		queryString = queryString.append("AND bp NOT IN ( ")
				.append("SELECT bp ")
				.append("FROM BasicPropertyImpl bp, EgBill b ")
				.append("WHERE bp.active = true ")
				.append("AND bp.isMigrated = 'N' ")
				.append("AND bp.upicNo = substring(b.consumerId, 1, 9) ")
				.append("AND b.module = :ptModule ")
				.append("AND b.egBillType = :billType ")
				.append("AND b.is_History = :isHistory ")
				.append("AND b.is_Cancelled = :isCancelled ")
				.append("AND (b.issueDate NOT BETWEEN :fromDate AND :toDate)) ");

		Query query = currentSession
				.createQuery(queryString.toString())
				.setEntity("ptModule", ptModule)
				.setEntity("billType",
						billService.getPropertyTaxUtil().getBillTypeByCode(NMCPTISConstants.BILLTYPE_MANUAL))
				.setCharacter("isHistory", 'N').setCharacter("isCancelled", 'N')
				.setDate("fromDate", currentInstallment.getFromDate())
				.setDate("toDate", currentInstallment.getToDate());

		query.setMaxResults(billsCount);
		
		return query;
	}
	
	private void setThreadLocals(JobExecutionContext context) { 
		LOGGER.debug("Entered into setThreadLocals");
		
		String jndiName = EGovConfig.getProperty(city, "", "JNDIURL");
		String hibFactName = EGovConfig.getProperty(city, "", "HibernateFactory");

		LOGGER.debug("Setting thread locals: [city=" + city + "][jndiName=" + jndiName + "][hibFactName=" + hibFactName + "]");
		SetDomainJndiHibFactNames.setThreadLocals(city, jndiName, hibFactName);

		User user = null;
		
		if (userName == null) {
			user = userService.getUserByUserName(SUPER_USER_NAME);
		} else {
			user = userService.getUserByUserName(userName);
		}
		
		EGOVThreadLocals.setUserId(user.getId().toString());
		
		LOGGER.debug("Exiting from setThreadLocals");
	}

	public void setBillService(BillService billService) {
		this.billService = billService;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}	

	public Integer getBillsCount() {
		return billsCount;
	}

	public void setBillsCount(Integer billsCount) {
		this.billsCount = billsCount;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public BillService getBillService() {
		return billService;
	}
	
	
}