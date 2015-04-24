/**
 * 
 */
package org.egov.collection.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.collection.entity.Challan;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.web.constants.CollectionConstants;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infstr.beanfactory.ApplicationContextBeanProvider;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.commons.service.GenericCommonsService;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.security.terminal.model.Location;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsServiceImpl;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.SearchPositionService;
import org.egov.pims.utils.EisManagersUtill;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Utility methods for ERP collections module
 */
public class CollectionsUtil {
	/**
	 * Map to cache status codes and corresponding status objects
	 */
	private final Map<String, EgwStatus> statusMap = new HashMap<String, EgwStatus>();

	private PersistenceService persistenceService;
	@Autowired
	private EisCommonsServiceImpl eisCommonsServiceImpl;
	@Autowired
	private GenericCommonsService genericCommonsService;
	@Autowired
	private UserService userService;
	@Autowired
	private CommonsService commonsService;
	private PersistenceService<Script, Long> scriptService;
	private GenericHibernateDaoFactory genericDao;
	private EisUtilService eisService;
	private SearchPositionService searchPositionService;
	//private AuditEventService auditEventService;
	private DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");

	public void setBeanProvider(ApplicationContextBeanProvider beanProvider) {
		this.beanProvider = beanProvider;
	}

	private ApplicationContextBeanProvider beanProvider;

	private static final Logger LOGGER = Logger.getLogger(CollectionsUtil.class);

	/**
	 * 
	 * @param genericDao
	 *            the Generic Hibernate Dao Factory instance
	 */
	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}

	/**
	 * @param persistenceService
	 *            the genericPersistenceService to set
	 */
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	/**
	 * Returns the Status object for given status code for a receipt
	 * 
	 * @param statusCode
	 *            Status code for which status object is to be returned
	 * @return the Status object for given status code for a receipt
	 */
	public EgwStatus getReceiptStatusForCode(String statusCode) {
		EgwStatus status = (EgwStatus) statusMap.get(statusCode);

		synchronized (this) {
			if (status == null) {
				// Status not yet cached. Get it from DB and cache it
				status = (EgwStatus) persistenceService.findByNamedQuery(CollectionConstants.QUERY_STATUS_BY_MODULE_AND_CODE, CollectionConstants.MODULE_NAME_RECEIPTHEADER,
						statusCode);

				if (status != null) {
					statusMap.put(statusCode, status);
				}
			}
		}

		return status;
	}

	/**
	 * This method returns the <code>EgwStatus</code> for the given module type
	 * and status code
	 * 
	 * @param moduleName
	 *            Module name of the required status
	 * @param statusCode
	 *            Status code of the required status
	 * 
	 * @return the <code>EgwStatus</code> instance
	 */
	public EgwStatus getEgwStatusForModuleAndCode(String moduleName, String statusCode) {

		EgwStatus status = (EgwStatus) persistenceService.findByNamedQuery(CollectionConstants.QUERY_STATUS_BY_MODULE_AND_CODE, moduleName, statusCode);
		return status;
	}

	/**
	 * @param sessionMap
	 *            Map of session variables
	 * @return user name of currently logged in user
	 */
	public String getLoggedInUserName(Map<String, Object> sessionMap) {
		return (String) sessionMap.get(CollectionConstants.SESSION_VAR_LOGIN_USER_NAME);
	}

	/**
	 * This method returns the User instance associated with the logged in user
	 * 
	 * @param sessionMap
	 *            Map of session variables
	 * @return the logged in user
	 */
	public User getLoggedInUser(Map<String, Object> sessionMap) {
		return userService.getUserByUsername((String) sessionMap.get(CollectionConstants.SESSION_VAR_LOGIN_USER_NAME));
	}

	/**
	 * @param user
	 *            the user whose department is to be returned
	 * @return department of the given user
	 */
	public Department getDepartmentOfUser(User user) {
		PersonalInformation empForUserId = EisManagersUtill.getEmployeeService().getEmpForUserId(user.getId());
		Assignment assignmentByEmpAndDate = EisManagersUtill.getEmployeeService().getAssignmentByEmpAndDate(new Date(), empForUserId.getIdPersonalInformation());
		return assignmentByEmpAndDate.getDeptId();
	}

	/**
	 * @param sessionMap
	 *            map of session variables
	 * @return department of currently logged in user
	 */
	public Department getDepartmentOfLoggedInUser(Map<String, Object> sessionMap) {
		return getDepartmentOfUser(getLoggedInUser(sessionMap));
	}

	/**
	 * This method returns the User instance for the userName passed as
	 * parameter
	 * 
	 * @param userName
	 * @return User
	 */
	public User getUserByUserName(String userName) {
		return userService.getUserByUsername(userName);
	}

	/**
	 * @param sessionMap
	 *            Map of session variables
	 * @return Location object for given user
	 */
	public Location getLocationOfUser(Map<String, Object> sessionMap) {
		Location location = null;
		try {
			if (sessionMap.get(CollectionConstants.SESSION_VAR_LOGIN_USER_COUNTERID) != null) {
				location = (Location) persistenceService.findByNamedQuery(CollectionConstants.QUERY_GET_LOCATIONBYID,
						Integer.valueOf((String) sessionMap.get(CollectionConstants.SESSION_VAR_LOGIN_USER_COUNTERID)));
			} else {
				location = (Location) persistenceService.findByNamedQuery(CollectionConstants.QUERY_LOCATION_BY_USER,
						(String) sessionMap.get(CollectionConstants.SESSION_VAR_LOGIN_USER_NAME));
			}
			if(location == null){
				throw new EGOVRuntimeException("Unable to fetch the location of the logged in user [" + (String) sessionMap.get(CollectionConstants.SESSION_VAR_LOGIN_USER_NAME) + "]");
			}
		} catch (Exception exp) {
			String errorMsg = "Unable to fetch the location of the logged in user [" + (String) sessionMap.get(CollectionConstants.SESSION_VAR_LOGIN_USER_NAME) + "]";
			LOGGER.error(errorMsg, exp);
			throw new EGOVRuntimeException(errorMsg, exp);
		}
		return location;
	}

	/**
	 * @return list of all active counters
	 */
	public List getAllCounters() {
		return persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_ALLCOUNTERS);
	}

	/**
	 * @return list of all active counters
	 */
	public List getActiveCounters() {
		return persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_ACTIVE_COUNTERS);
	}

	/**
	 * @return List of all collection services (service type = B (Billing) or C
	 *         (Challan)
	 */
	public List getCollectionServiceList() {
		return persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_COLLECTION_SERVICS);
	}

	/**
	 * @return List of all collection services (service type = C)
	 */
	public List getChallanServiceList() {
		return persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_SERVICES_BY_TYPE, CollectionConstants.CHALLAN_SERVICE_TYPE);
	}

	/**
	 * @return List of all billing services
	 */
	public List getBillingServiceList() {
		return persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_SERVICES_BY_TYPE, CollectionConstants.SERVICE_TYPE_BILLING);
	}

	/**
	 * @return list of all users who have created at least one receipt
	 */
	public List getReceiptCreators() {
		return persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_CREATEDBYUSERS_OF_RECEIPTS);
	}

	/**
	 * 
	 * @return list of all zones that have receipts created
	 */
	public List getReceiptZoneList() {
		return persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_ZONE_OF_RECEIPTS);
	}

	/**
	 * This method returns the collection modes that are not allowed based on
	 * rules configured in the script
	 * 
	 * @param loggedInUser
	 *            a <code>User</code> entity representing the logged in user.
	 * 
	 * @return a <code>List</code> of <code>String</code> values representing
	 *         the mode of payments supported.
	 */
	public List<String> getCollectionModesNotAllowed(User loggedInUser) {
		List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", CollectionConstants.SCRIPT_PAYMENTMODESNOTALLOWED_RULES);
		return null;//(List<String>) scripts.get(0).eval(Script.createContext("loggedInUser", loggedInUser, "collUtil", this));
	}

	/**
	 * @param sessionMap
	 *            Map of session variables
	 * @return Position of logged in user
	 */
	public Position getPositionOfUser(User user) {
		return eisCommonsServiceImpl.getCurrentPositionByUser(user);
	}

	/**
	 * Gets position by given position name
	 * 
	 * @param positionName
	 *            Position name
	 * @return Position object for given position name
	 */
	public Position getPositionByName(String positionName) {
		return eisCommonsServiceImpl.getPositionByName(positionName);
	}

	/**
	 * This method retrieves the <code>CFinancialYear</code> for the given date.
	 * 
	 * @param date
	 *            an instance of <code>Date</code> for which the financial year
	 *            is to be retrieved.
	 * 
	 * @return an instance of <code></code> representing the financial year for
	 *         the given date
	 */
	public CFinancialYear getFinancialYearforDate(Date date) {
		return (CFinancialYear) persistenceService.getSession()
				.createQuery("from CFinancialYear cfinancialyear where ? between " + "cfinancialyear.startingDate and cfinancialyear.endingDate").setDate(0, date).list().get(0);
	}

	/**
	 * This method checks if the given challan is valid.
	 * 
	 * @param challan
	 *            the <code>Challan</code> instance whose validity has to be
	 *            checked
	 * 
	 * @return a boolean value - true indicating that the challan is valid and
	 *         false indicating that teh challan is not valid
	 */
	public boolean checkChallanValidity(Challan challan) {
		Calendar current = Calendar.getInstance();
		current.clear(Calendar.HOUR_OF_DAY);
		current.clear(Calendar.MINUTE);
		current.clear(Calendar.SECOND);
		current.clear(Calendar.MILLISECOND);

		Calendar validityStart = Calendar.getInstance();
		validityStart.setTime(challan.getChallanDate());
		validityStart.clear(Calendar.HOUR_OF_DAY);
		validityStart.clear(Calendar.MINUTE);
		validityStart.clear(Calendar.SECOND);
		validityStart.clear(Calendar.MILLISECOND);

		Calendar validityEnd = Calendar.getInstance();
		validityEnd.setTime(challan.getValidUpto());
		validityEnd.clear(Calendar.HOUR_OF_DAY);
		validityEnd.clear(Calendar.MINUTE);
		validityEnd.clear(Calendar.SECOND);
		validityEnd.clear(Calendar.MILLISECOND);

		if (validityStart.compareTo(current) <= 0 && validityEnd.compareTo(current) >= 0) {
			return true;
		}
		return false;
	}

	public void setScriptService(PersistenceService<Script, Long> scriptService) {
		this.scriptService = scriptService;
	}

	/**
	 * Fetches given bean from application context
	 * 
	 * @param beanName
	 *            name of bean to be fetched
	 * 
	 * @return given bean from application context
	 */
	public Object getBean(String beanName) {

		Object bean = null;
		try {
			bean = beanProvider.getBean(beanName);
			LOGGER.debug(" Got bean : " + beanName);
		} catch (BeansException e) {
			String errorMsg = "Could not locate bean [" + beanName + "]";
			LOGGER.error(errorMsg, e);
			throw new EGOVRuntimeException(errorMsg, e);
		}
		return bean;
	}

	/**
	 * This method returns the currently active config value for the given
	 * module name and key
	 * 
	 * @param moduleName
	 *            a <code>String<code> representing the module name
	 * 
	 * @param key
	 *            a <code>String</code> representing the key
	 * 
	 * @param defaultValue
	 *            Default value to be returned in case the key is not defined
	 * 
	 * @return <code>String</code> representing the configuration value
	 */
	public String getAppConfigValue(String moduleName, String key, String defaultValue) {
		AppConfigValues configVal = genericDao.getAppConfigValuesDAO().getAppConfigValueByDate(moduleName, key, new Date());
		return configVal == null ? defaultValue : configVal.getValue();
	}

	/**
	 * This method returns the config value for the given module name and key
	 * 
	 * @param moduleName
	 *            a <code>String<code> representing the module name
	 * 
	 * @param key
	 *            a <code>String</code> representing the key
	 * 
	 * @return <code>String</code> representing the configuration value
	 */
	public String getAppConfigValue(String moduleName, String key) {
		return genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(moduleName, key).get(0).getValue();
	}

	/**
	 * This method returns the list of config values for the given module name
	 * and key
	 * 
	 * @param moduleName
	 *            a <code>String<code> representing the module name
	 * 
	 * @param key
	 *            a <code>String</code> representing the key
	 * 
	 * @return <code>List<AppConfigValues></code> representing the list of
	 *         configuration values
	 */
	public List<AppConfigValues> getAppConfigValues(String moduleName, String key) {
		return genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(moduleName, key);
	}

	/**
	 * Gets position by given position id
	 * 
	 * @param positionId
	 *            Position Id
	 * @return Position object for given position id
	 */
	public Position getPositionById(Integer positionId) {
		return eisCommonsServiceImpl.getPositionById(positionId);
	}

	/**
	 * This method is invoked from the ReceiptHeader.workFlow script and returns
	 * the position for the employee id passed as parameter
	 * 
	 * @param employeeId
	 *            PersonalInformation Id
	 * @return Position object for Employee Id passed as parameter
	 */

	public Position getPositionforEmp(Integer employeeId) {
		return EisManagersUtill.getEmployeeService().getPositionforEmp(employeeId);
	}

	/**
	 * This method is invoked from the ReceiptHeader.workFlow script and returns
	 * Employee object for the given Department Id, Designation Id ,Boundary Id
	 * and FunctionaryId
	 * 
	 * @param deptId
	 *            Department Id
	 * @param designationId
	 *            Designation Id
	 * @param boundaryId
	 *            Boundary Id
	 * @param functionaryId
	 *            Functionary Id
	 * @return PersonalInformation
	 */

	public PersonalInformation getEmployeeByDepartmentDesignationBoundaryandFunctionary(Integer deptId, Integer designationId, Integer boundaryId, Integer functionaryId) {
		PersonalInformation personalInformation = null;
		try {
			personalInformation = EisManagersUtill.getEmployeeService().getEmployeeByFunctionary(deptId, designationId, Long.valueOf(boundaryId), functionaryId);
		} catch (Exception e) {
			String errorMsg = "Could not get PersonalInformation";
			LOGGER.error("Could not get PersonalInformation", e);
			throw new EGOVRuntimeException(errorMsg, e);
		}
		return personalInformation;
	}

	/**
	 * 
	 * @param sessionMap
	 * @return
	 */

	public List<Department> getAllNonPrimaryAssignmentsOfLoggedInUser(Map<String, Object> sessionMap) {
		return getAllNonPrimaryAssignmentsOfUser(getLoggedInUser(sessionMap));
	}

	/**
	 * @param user
	 *            the user whose non-primary department list is to be returned
	 * @return list of non-primary department of the given user
	 */
	public List<Department> getAllNonPrimaryAssignmentsOfUser(User user) {
		List<Department> departmentlist = new ArrayList<Department>();
		try {
			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("code", EisManagersUtill.getEmployeeService().getEmpForUserId(user.getId()).getCode());
			List<EmployeeView> employeeViewList = eisService.getEmployeeInfoList(paramMap);
			if (!employeeViewList.isEmpty()) {
				for (EmployeeView employeeView : employeeViewList) {
					if (employeeView.getAssignment().getIsPrimary() == 'N') {
						departmentlist.add(employeeView.getAssignment().getDeptId());
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Could not get list of assignments", e);
		}

		return departmentlist;
	}

	/**
	 * @param user
	 *            the user whose non-primary department is to be returned
	 * @return non-primary department of the given user. In case user has
	 *         multiple non-primary departments, the first one will be returned.
	 */
	public Department getNonPrimaryDeptOfUser(User user) {
		List<Department> nonPrimaryAssignments = getAllNonPrimaryAssignmentsOfUser(user);
		return (nonPrimaryAssignments.isEmpty()) ? null : nonPrimaryAssignments.get(0);
	}

	public List<DesignationMaster> getDesignationsAllowedForChallanApproval(Integer departmentId, ReceiptHeader receiptHeaderObj) {
		List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", CollectionConstants.QUERY_CHALLAN_WORKFLOWDESIGNATIONS);
		return null ;/* (List<DesignationMaster>) scripts.get(0).eval(
				Script.createContext("departmentId", departmentId, "collUtil", this, "receiptHeaderObj", receiptHeaderObj, "persistanceService", persistenceService));
*/
	}

	public List<Department> getDepartmentsAllowedForChallanApproval(User loggedInUser, ReceiptHeader receiptHeaderObj) {
		List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", CollectionConstants.QUERY_CHALLAN_WORKFLOWDEPARTMENTS);
		return null; /*(List<Department>) scripts.get(0).eval(
				Script.createContext("loggedInUser", loggedInUser, "collUtil", this, "receiptHeaderObj", receiptHeaderObj, "persistanceService", persistenceService));
*/
	}

	public List<Department> getDepartmentsAllowedForBankRemittanceApproval(User loggedInUser) {
		List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", CollectionConstants.QUERY_BANKREMITTANCE_WORKFLOWDEPARTMENTS);
		return  null;/*(List<Department>) scripts.get(0).eval(
				Script.createContext("loggedInUser", loggedInUser, "collUtil", this, "persistanceService", persistenceService, "contraJournalVoucherObj",
						new ContraJournalVoucher()));*/

	}

	public List<DesignationMaster> getDesignationsAllowedForBankRemittanceApproval(Integer departmentId) {
		List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", CollectionConstants.QUERY_BANKREMITTANCE_WORKFLOWDESIGNATIONS);
		return null;/*(List<DesignationMaster>) scripts.get(0).eval(
				Script.createContext("departmentId", departmentId, "collUtil", this, "persistanceService", persistenceService, "contraJournalVoucherObj",
						new ContraJournalVoucher()));*/

	}

	/**
	 * This method checks if the given glcode belongs to an account head
	 * representing an arrear account head (for Property Tax). The glcodes for
	 * such accounts are retrieved from App Config.
	 * 
	 * @param glcode
	 *            The Chart of Accounts Code
	 * 
	 * @param description
	 *            Description of the glcode
	 * 
	 * @returna a <code>Boolean</code> indicating if the glcode is arrear
	 *          account head
	 * 
	 */
	public boolean isPropertyTaxArrearAccountHead(String glcode, String description) {
		List<AppConfigValues> list = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(CollectionConstants.MODULE_NAME_PROPERTYTAX, "ISARREARACCOUNT");
		AppConfigValues penaltyGlCode = genericDao.getAppConfigValuesDAO().getAppConfigValueByDate(CollectionConstants.MODULE_NAME_PROPERTYTAX, "PTPENALTYGLCODE", new Date());
		boolean retValue = false;
		LOGGER.debug("isPropertyTaxArrearAccountHead glcode " + glcode + " description " + description);
		if (penaltyGlCode != null && penaltyGlCode.getValue().equals(glcode)) {
			Module module = genericCommonsService.getModuleByName(CollectionConstants.MODULE_NAME_PROPERTYTAX);
			String currInst = commonsService.getInsatllmentByModuleForGivenDate(module, new Date()).getDescription();
			if (currInst.equals(description.substring(16, description.length()))) {
				retValue = false;
			} else {
				retValue = true;
			}
		} else {
			ArrayList<String> accValues = new ArrayList<String>();
			for (AppConfigValues value : list) {
				accValues.add(value.getValue());
			}
			if (accValues.contains(glcode)) {
				retValue = true;
			} else {
				retValue = false;
			}
		}

		return retValue;
	}

	/**
	 * @param eisService
	 *            the eisService to set
	 */
	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

	/**
	 * @param searchPositionService
	 *            the searchPositionService to set
	 */
	public void setSearchPositionService(SearchPositionService searchPositionService) {
		this.searchPositionService = searchPositionService;
	}

	public List<EmployeeView> getPositionBySearchParameters(String beginsWith, Integer desId, Integer deptId, Integer jurdId, Integer roleId, Date userDate, Integer maxResults)
			throws NoSuchObjectException {

		return searchPositionService.getPositionBySearchParameters(beginsWith, desId, deptId, Long.valueOf(jurdId), roleId, userDate, maxResults);

	}

	public void auditEventForReceiptEntity(ReceiptHeader receiptHeader, String action) {/*
		StringBuffer details1 = new StringBuffer("Service Name: ").append(receiptHeader.getService().getServiceName());
		StringBuffer details2 = new StringBuffer("Status: ").append(receiptHeader.getStatus().getDescription()).append('\n').append("Amount: Rs.")
				.append(receiptHeader.getTotalAmount() != null ? receiptHeader.getTotalAmount() : "0");
		AuditEvent auditEvent = new AuditEvent(AuditModule.COLLECTIONS, AuditEntity.COLLECTIONS_RECEIPTHEADER, action, receiptHeader.getReceiptnumber(), details1.toString());
		auditEvent.setPkId(receiptHeader.getId());
		auditEvent.setDetails2(details2.toString());
		this.auditEventService.createAuditEvent(auditEvent, ReceiptHeader.class);
	*/}

	public void auditEventForChallanEntity(ReceiptHeader receiptHeader, String action) {/*
		Challan challan = receiptHeader.getChallan();
		if (challan != null) {
			StringBuffer details1 = new StringBuffer("Challan Number: ").append(challan.getChallanNumber());
			if (action != null && action.equals(WF_ACTION_NAME_NEW_CHALLAN)) {
				details1.append('\n').append("Challan date: ").append(dateFormatter.format(challan.getChallanDate())).append('\n').append("Valid upto date: ")
						.append(dateFormatter.format(challan.getValidUpto()));
			} else {
				details1.append('\n').append("Challan date: ").append(dateFormatter.format(challan.getChallanDate()));
			}

			StringBuffer details2 = new StringBuffer("Status: ").append(challan.getStatus().getDescription()).append('\n').append("Amount: Rs.")
					.append(receiptHeader.getTotalAmount());
			AuditEvent auditEvent = new AuditEvent(AuditModule.COLLECTIONS, AuditEntity.COLLECTIONS_CHALLAN, action, challan.getChallanNumber(), details1.toString());
			auditEvent.setPkId(challan.getId());
			this.auditEventService.createAuditEvent(auditEvent, Challan.class);
			auditEvent.setDetails2(details2.toString());
		}
	*/}

	/**
	 * @param userId
	 *            userId
	 * @return User object for given userId
	 */
	public User getUserById(String userId) {
		return userService.getUserById(Long.valueOf(userId));
	}

	/**
	 * @param user
	 *            User
	 * @return Location object for given user
	 */
	public Location getLocationByUser(String userId) {
		User user = userService.getUserById(Long.valueOf(userId));
		return (Location) persistenceService.findByNamedQuery(CollectionConstants.QUERY_LOCATION_BY_USER, user.getUsername());
	}

	/**
	 * @param auditEventService
	 *            the auditEventService to set
	 *//*
	public void setAuditEventService(AuditEventService auditEventService) {
		this.auditEventService = auditEventService;
	}

	*//**
	 * @return the auditEventService
	 *//*
	public AuditEventService getAuditEventService() {
		return auditEventService;
	}*/

}
