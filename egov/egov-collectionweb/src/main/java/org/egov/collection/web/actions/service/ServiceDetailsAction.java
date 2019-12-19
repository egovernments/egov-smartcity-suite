/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.collection.web.actions.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.service.CategoryService;
import org.egov.collection.service.ServiceDetailsService;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.Fund;
import org.egov.commons.dao.ChartOfAccountsDAO;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.models.ServiceAccountDetails;
import org.egov.infstr.models.ServiceCategory;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.models.ServiceSubledgerInfo;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Results({ @Result(name = ServiceDetailsAction.NEW, location = "serviceDetails-new.jsp"),
		@Result(name = ServiceDetailsAction.LIST, location = "serviceDetails-list.jsp"),
		@Result(name = ServiceDetailsAction.BEFORECREATE, location = "serviceDetails-beforeCreate.jsp"),
		@Result(name = ServiceDetailsAction.CODEUNIQUECHECK, location = "serviceDetails-codeUniqueCheck.jsp"),
		@Result(name = ServiceDetailsAction.MESSAGE, location = "serviceDetails-message.jsp"),
		@Result(name = ServiceDetailsAction.VIEW, location = "serviceDetails-view.jsp"),
		@Result(name = ServiceDetailsAction.SUCCESS, location = "serviceDetails-view.jsp"),
		@Result(name = ServiceDetailsAction.BEFOREMODIFY, location = "serviceDetails-beforeModify.jsp"), })
public class ServiceDetailsAction extends BaseFormAction {

	private static final Logger LOGGER = Logger.getLogger(ServiceDetailsAction.class);
	private static final long serialVersionUID = 1L;
	private ServiceDetails serviceDetails = new ServiceDetails();
	protected static final String BEFORECREATE = "beforeCreate";
	protected static final String BEFOREMODIFY = "beforeModify";
	protected static final String LIST = "list";
	protected static final String MESSAGE = "message";
	protected static final String CODEUNIQUECHECK = "codeUniqueCheck";
	private List<ServiceAccountDetails> accountDetails = new ArrayList<>(0);
	private List<ServiceSubledgerInfo> subledgerDetails = new ArrayList<>(0);
	private List<Long> departmentList = new ArrayList<>();
	private List<ServiceDetails> serviceList;
	private Boolean isVoucherApproved = Boolean.FALSE;
	private Long serviceCategoryId;
	protected List<String> headerFields;
	protected List<String> mandatoryFields;
	private SortedMap<String, String> serviceTypeMap = new TreeMap<>();
	private String deptId;
	private EgovCommon egovCommon;
	private Boolean isVoucherOnReceiptAndStatusDisplay = Boolean.FALSE;

	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private CollectionsUtil collectionsUtil;
	@Autowired
	private FunctionHibernateDAO functionDAO;
	@Autowired
	private ChartOfAccountsDAO chartOfAccountsDAO;
	@Autowired
	private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ServiceDetailsService serviceDetailsService;
	@Autowired
	private CategoryService serviceCategoryService;

	public ServiceDetailsAction() {
		addRelatedEntity("serviceCategory", ServiceCategory.class);
		addRelatedEntity("fund", Fund.class);
		addRelatedEntity("function", CFunction.class);
	}

	@Override
	public ServiceDetails getModel() {
		return serviceDetails;
	}

	@Action(value = "/service/serviceDetails-newform")
	public String newform() {
		addDropdownData("serviceCategoryList", serviceCategoryService.getActiveServiceCategoryList());
		return NEW;
	}

	@Override
	@Action(value = "/service/serviceDetails")
	public String execute() {
		return SUCCESS;
	}

	@Override
	public void prepare() {
		super.prepare();
		if (null != parameters.get("serviceId") && StringUtils.isNotEmpty(parameters.get("serviceId")[0])) {
			serviceDetails = entityManager.find(ServiceDetails.class, new Long(parameters.get("serviceId")[0]));
			accountDetails.addAll(serviceDetails.getServiceAccountDtls());
			for (final ServiceAccountDetails account : serviceDetails.getServiceAccountDtls())
				subledgerDetails.addAll(account.getSubledgerDetails());
			for (final ServiceSubledgerInfo serviceSubledgerInfo : subledgerDetails)
				if (serviceSubledgerInfo.getDetailType() != null && serviceSubledgerInfo.getDetailKeyId() != null) {
					EntityType entityType = null;
					try {
						entityType = egovCommon.getEntityType(serviceSubledgerInfo.getDetailType(),
								serviceSubledgerInfo.getDetailKeyId());
					} catch (final ApplicationException e) {
						LOGGER.error("Exception while setting subledger details", e);
						throw new ApplicationRuntimeException("Exception while setting subledger details", e);
					}
					if (entityType != null) {
						serviceSubledgerInfo.setDetailCode(entityType.getCode());
						serviceSubledgerInfo.setDetailKey(entityType.getName());
					}
				}
			for (final Department department : serviceDetails.getServiceDept()) {
				departmentList.add(department.getId());
				setDeptId(department.getId().toString());
			}
		} else if (null != serviceDetails.getServiceCategory()
				&& null != serviceDetails.getServiceCategory().getCode()) {
			final ServiceCategory category = entityManager.find(ServiceCategory.class,
					serviceDetails.getServiceCategory().getId());
			setServiceCategoryId(serviceDetails.getServiceCategory().getId());
			serviceDetails.setServiceCategory(category);
		} else if (null != serviceCategoryId) {
			final ServiceCategory category = entityManager.find(ServiceCategory.class, serviceCategoryId);
			serviceDetails.setServiceCategory(category);
		}
		headerFields = new ArrayList<>(0);
		mandatoryFields = new ArrayList<>(0);
		getHeaderMandateFields();
		setupDropdownDataExcluding();
		addHeaderMandateDropDown();
		prepareServiceTypeMap();
		setHeaderFields(headerFields);
		setMandatoryFields(mandatoryFields);
		setVoucherCreateFlag();
	}

	private void setVoucherCreateFlag() {
		if (collectionsUtil
				.getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
						CollectionConstants.APPCONFIG_VALUE_ISVOUCHERCREATIONONRECEIPTANDSTATUSDISPLAY)
				.equals(CollectionConstants.YES))
			isVoucherOnReceiptAndStatusDisplay = Boolean.TRUE;
	}

	private void prepareServiceTypeMap() {
		serviceTypeMap.putAll(CollectionConstants.SERVICE_TYPE_CLASSIFICATION);
		serviceTypeMap.remove(CollectionConstants.SERVICE_TYPE_PAYMENT);
		serviceTypeMap.remove(CollectionConstants.SERVICE_TYPE_BILLING);
	}

	protected void getHeaderMandateFields() {
		final List<AppConfigValues> appConfigValuesList = collectionsUtil.getAppConfigValues(
				CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
				CollectionConstants.MISMandatoryAttributesKeyCollection);

		for (final AppConfigValues appConfigVal : appConfigValuesList) {
			final String value = appConfigVal.getValue();
			final String header = value.substring(0, value.indexOf(CollectionConstants.PIPE));
			headerFields.add(header);
			final String mandate = value.substring(value.indexOf(CollectionConstants.PIPE) + 1);
			if (mandate.equalsIgnoreCase(CollectionConstants.Mandatory))
				mandatoryFields.add(header);
		}
	}

	private void addHeaderMandateDropDown() {
		if (headerFields.contains(CollectionConstants.DEPARTMENT))
			addDropdownData("departmentList",
					entityManager.createNamedQuery(CollectionConstants.QUERY_ALL_DEPARTMENTS).getResultList());
		if (headerFields.contains(CollectionConstants.FUNCTIONARY))
			addDropdownData("functionaryList",
					entityManager.createNamedQuery(CollectionConstants.QUERY_ALL_FUNCTIONARY).getResultList());
		if (headerFields.contains(CollectionConstants.FUND))
			addDropdownData("fundList", collectionsUtil.getAllFunds());
		if (headerFields.contains(CollectionConstants.FUNCTION))
			addDropdownData("functionList", functionDAO.getAllActiveFunctions());
		if (headerFields.contains(CollectionConstants.FIELD))
			addDropdownData("fieldList",
					entityManager.createNamedQuery(CollectionConstants.QUERY_ALL_FIELD).getResultList());
		if (headerFields.contains(CollectionConstants.FUNDSOURCE))
			addDropdownData("fundsourceList",
					entityManager.createNamedQuery(CollectionConstants.QUERY_ALL_FUNDSOURCE).getResultList());
	}

	@Action(value = "/service/serviceDetails-beforeCreate")
	public String beforeCreate() {
		if (serviceDetails.getServiceCategory() == null || serviceDetails.getServiceCategory().getId() == null)
			addActionError(getText("service.category.null"));
		if (hasActionErrors())
			return NEW;
		accountDetails.add(new ServiceAccountDetails());
		subledgerDetails.add(new ServiceSubledgerInfo());
		if (!isVoucherOnReceiptAndStatusDisplay) {
			serviceDetails.setVoucherCreation(Boolean.TRUE);
			serviceDetails.setIsVoucherApproved(Boolean.TRUE);
		}
		return BEFORECREATE;
	}

	@ValidationErrorPage(value = BEFORECREATE)
	@Action(value = "/service/serviceDetails-create")
	public String create() {
		validateServiceDetails();
		if (getCodeCheck())
			addActionError(getText("service.code.already.exists"));
		insertOrUpdateService();
		if (hasActionErrors())
			return BEFORECREATE;
		return MESSAGE;
	}

	public void validateServiceDetails() {

		if (StringUtils.isNotEmpty(serviceDetails.getServiceType())
				&& serviceDetails.getServiceType().equals(CollectionConstants.SERVICE_TYPE_PAYMENT)) {
			if (StringUtils.isEmpty(serviceDetails.getServiceUrl()))
				addActionError(getText("service.serviceurl.null"));

			if (StringUtils.isEmpty(serviceDetails.getCallBackurl()))
				addActionError(getText("service.callbackurl.null"));
		}

		// validate mandatory fields
		if (isFieldMandatory(CollectionConstants.FUND) && serviceDetails.getFund() == null)
			addActionError(getText("miscreceipt.fundcode.errormessage"));
		if (isFieldMandatory(CollectionConstants.DEPARTMENT) && StringUtils.isEmpty(deptId))
			addActionError(getText("miscreceipt.deptcode.errormessage"));
		if (isFieldMandatory(CollectionConstants.SCHEME) && serviceDetails.getScheme() == null)
			addActionError(getText("miscreceipt.schemeId.errormessage"));
		if (isFieldMandatory(CollectionConstants.SUBSCHEME) && serviceDetails.getSubscheme() == null)
			addActionError(getText("miscreceipt.subschemeId.errormessage"));
		if (isFieldMandatory(CollectionConstants.FUNCTIONARY) && serviceDetails.getFunctionary() == null)
			addActionError(getText("miscreceipt.functionarycode.errormessage"));
		if (isFieldMandatory(CollectionConstants.FUNDSOURCE) && serviceDetails.getFundSource() == null)
			addActionError(getText("miscreceipt.fundsourcecode.errormessage"));
		if (isFieldMandatory(CollectionConstants.FUNCTION) && serviceDetails.getFunction() == null)
			addActionError(getText("miscreceipt.functioncode.errormessage"));
	}

	@Action(value = "/service/serviceDetails-listServices")
	public String listServices() {
		if (serviceDetails.getServiceCategory() == null || serviceDetails.getServiceCategory().getId() == null)
			addActionError(getText("service.category.null"));
		if (hasActionErrors())
			return NEW;
		return "list";
	}

	@Action(value = "/service/serviceDetails-view")
	public String view() {
		serviceTypeMap.putAll(CollectionConstants.SERVICE_TYPE_CLASSIFICATION);
		return BaseFormAction.VIEW;
	}

	@ValidationErrorPage(value = BEFOREMODIFY)
	@Action(value = "/service/serviceDetails-beforeModify")
	public String beforeModify() {

		if (null == accountDetails || accountDetails.isEmpty())
			accountDetails.add(new ServiceAccountDetails());

		if (null == subledgerDetails || subledgerDetails.isEmpty())
			subledgerDetails.add(new ServiceSubledgerInfo());
		serviceTypeMap.putAll(CollectionConstants.SERVICE_TYPE_CLASSIFICATION);
		if (serviceDetails.getServiceType().equals(CollectionConstants.SERVICE_TYPE_CHALLAN_COLLECTION)
				|| serviceDetails.getServiceType().equals(CollectionConstants.SERVICE_TYPE_MISC_COLLECTION)) {
			serviceTypeMap.remove(CollectionConstants.SERVICE_TYPE_PAYMENT);
			serviceTypeMap.remove(CollectionConstants.SERVICE_TYPE_BILLING);
		}
		return BEFOREMODIFY;
	}

	@SuppressWarnings("unchecked")
	@ValidationErrorPage(value = BEFOREMODIFY)
	@Action(value = "/service/serviceDetails-modify")
	public String modify() {
		validateServiceDetails();
		insertOrUpdateService();
		if (hasActionErrors())
			return BEFOREMODIFY;
		return MESSAGE;
	}

	private void insertOrUpdateService() {
		removeEmptyRowsAccoutDetail(accountDetails);
		removeEmptyRowsSubledger(subledgerDetails);
		if (validateAccountDetails()) {
			formatServiceDetails();
			if (serviceDetails.getVoucherCreation().equals(Boolean.TRUE)) {
				isVoucherApproved = serviceDetails.getIsVoucherApproved();
				serviceDetails.setIsVoucherApproved(isVoucherApproved);
			}
			serviceDetailsService.persist(serviceDetails);
			addActionMessage(
					getText("service.create.success.msg", new String[] { getModel().getCode(), getModel().getName() }));
		}
		if (subledgerDetails.isEmpty())
			subledgerDetails.add(new ServiceSubledgerInfo());
		if (accountDetails.isEmpty())
			accountDetails.add(new ServiceAccountDetails());
	}

	private void formatServiceDetails() {

		for (final Long departId : departmentList) {
			final Department department = departmentService.getDepartmentById(departId);
			serviceDetails.addServiceDept(department);
		}
		for (final ServiceAccountDetails account : accountDetails) {

			final ServiceAccountDetails serviceAccount = new ServiceAccountDetails();
			serviceAccount.setAmount(account.getAmount());
			final CChartOfAccounts glCodeId = chartOfAccountsDAO.findById(account.getGlCodeId().getId(), false);
			serviceAccount.setGlCodeId(glCodeId);
			serviceAccount.setServiceDetails(serviceDetails);
			for (final ServiceSubledgerInfo subledger : subledgerDetails)
				if (subledger.getServiceAccountDetail().getGlCodeId().getId().equals(account.getGlCodeId().getId())) {

					final ServiceSubledgerInfo subledgerInfo = new ServiceSubledgerInfo();
					final Accountdetailtype accdetailtype = entityManager
							.createNamedQuery(CollectionConstants.QUERY_ACCOUNTDETAILTYPE_BY_ID,
									Accountdetailtype.class)
							.setParameter(1, subledger.getDetailType()).getSingleResult();
					subledgerInfo.setDetailType(accdetailtype);
					subledgerInfo.setDetailKeyId(subledger.getDetailKeyId());
					subledgerInfo.setAmount(subledger.getAmount());
					subledgerInfo.setServiceAccountDetail(serviceAccount);
					serviceAccount.addSubledgerDetails(subledgerInfo);
				}
			serviceDetails.addServiceAccountDtls(serviceAccount);
		}

	}

	private void removeEmptyRowsAccoutDetail(final List<ServiceAccountDetails> list) {
		for (final Iterator<ServiceAccountDetails> detail = list.iterator(); detail.hasNext();) {
			final ServiceAccountDetails next = detail.next();
			if (null != next
					&& (null == next.getGlCodeId() || null == next.getGlCodeId().getId()
							|| next.getGlCodeId().getId().toString().trim().isEmpty())
					&& next.getAmount().compareTo(BigDecimal.ZERO) == 0)
				detail.remove();
			else if (null == next)
				detail.remove();
		}
	}

	protected void removeEmptyRowsSubledger(final List<ServiceSubledgerInfo> list) {
		for (final Iterator<ServiceSubledgerInfo> detail = list.iterator(); detail.hasNext();) {
			final ServiceSubledgerInfo next = detail.next();
			if (null != next
					&& (null == next.getServiceAccountDetail() || null == next.getServiceAccountDetail().getGlCodeId()
							|| null == next.getServiceAccountDetail().getGlCodeId().getId()
							|| next.getServiceAccountDetail().getGlCodeId().getId() == 0
							|| next.getServiceAccountDetail().getGlCodeId().getId() == -1))
				detail.remove();
			else if (null == next)
				detail.remove();

		}

	}

	private boolean validateAccountDetails() {
		int index = 0;
		for (final ServiceAccountDetails account : accountDetails)
			if (account.getAmount().compareTo(BigDecimal.ZERO) > 0
					&& (null == account.getGlCodeId() || null == account.getGlCodeId().getId())) {
				addActionError(
						getText("service.accdetail.accmissing", new String[] { CollectionConstants.BLANK + ++index }));
				return Boolean.FALSE;
			}
		return validateSubledger();
	}

	private boolean validateSubledger() {
		final Map<String, BigDecimal> accountDetailAmount = new HashMap<>(0);
		for (final ServiceAccountDetails account : accountDetails) {
			final Set<CChartOfAccountDetail> chartOfAccountDetail = chartOfAccountsHibernateDAO
					.getCChartOfAccountsByGlCode(account.getGlCodeId().getGlcode()).getChartOfAccountDetails();
			if (null != chartOfAccountDetail && !chartOfAccountDetail.isEmpty())
				accountDetailAmount.put(account.getGlCodeId().getGlcode(), account.getAmount());
		}

		final Map<String, BigDecimal> subledgerAmount = new HashMap<>(0);

		for (final ServiceSubledgerInfo subledger : subledgerDetails)
			if (null == subledger.getDetailType() || null == subledger.getDetailType().getId()
					|| subledger.getDetailType().getId() == 0) {

				addActionError(getText("service.accdetailType.entrymissing",
						new String[] { subledger.getServiceAccountDetail().getGlCodeId().getGlcode() }));
				return Boolean.FALSE;
			}

			else if (null != subledgerAmount.get(subledger.getServiceAccountDetail().getGlCodeId().getGlcode())) {

				final BigDecimal amount = subledgerAmount
						.get(subledger.getServiceAccountDetail().getGlCodeId().getGlcode());
				subledgerAmount.put(subledger.getServiceAccountDetail().getGlCodeId().getGlcode(),
						amount.add(subledger.getAmount()));
			} else {
				subledgerAmount.put(subledger.getServiceAccountDetail().getGlCodeId().getGlcode(),
						subledger.getAmount());
			}

		for (final Map.Entry<String, BigDecimal> entry : accountDetailAmount.entrySet()) {

			final String key = entry.getKey();
			final BigDecimal value = entry.getValue();
			if (null == subledgerAmount.get(key)) {
				addActionError(getText("service.accdetail.entrymissing", new String[] { key }));
				return Boolean.FALSE;
			} else if (subledgerAmount.get(key).compareTo(value) != 0) {
				addActionError(getText("service.subledger.amtnotmatchinng", new String[] { key }));
				return Boolean.FALSE;
			}
		}

		return Boolean.TRUE;
	}

	public boolean isFieldMandatory(final String field) {
		return mandatoryFields.contains(field);
	}

	public boolean shouldShowHeaderField(final String field) {
		return headerFields.contains(field);
	}

	@Action(value = "/service/serviceDetails-codeUniqueCheck")
	public String codeUniqueCheck() {
		return CODEUNIQUECHECK;
	}

	public boolean getCodeCheck() {
		return serviceDetailsService.findServiceDetailsByCode(serviceDetails.getCode()).isPresent();
	}

	public ServiceDetails getServiceDetails() {
		return serviceDetails;
	}

	public void setServiceDetails(final ServiceDetails serviceDetails) {
		this.serviceDetails = serviceDetails;
	}

	public List<ServiceAccountDetails> getAccountDetails() {
		return accountDetails;
	}

	public void setAccountDetails(final List<ServiceAccountDetails> accountDetails) {
		this.accountDetails = accountDetails;
	}

	public List<ServiceSubledgerInfo> getSubledgerDetails() {
		return subledgerDetails;
	}

	public void setSubledgerDetails(final List<ServiceSubledgerInfo> subledgerDetails) {
		this.subledgerDetails = subledgerDetails;
	}

	public List<Long> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(final List<Long> departmentList) {
		this.departmentList = departmentList;
	}

	public List<ServiceDetails> getServiceList() {
		return serviceList;
	}

	public Long getServiceCategoryId() {
		return serviceCategoryId;
	}

	public void setServiceCategoryId(final Long serviceCategoryId) {
		this.serviceCategoryId = serviceCategoryId;
	}

	public List<String> getHeaderFields() {
		return headerFields;
	}

	public void setHeaderFields(final List<String> headerFields) {
		this.headerFields = headerFields;
	}

	public List<String> getMandatoryFields() {
		return mandatoryFields;
	}

	public void setMandatoryFields(final List<String> mandatoryFields) {
		this.mandatoryFields = mandatoryFields;
	}

	public SortedMap<String, String> getServiceTypeMap() {
		return serviceTypeMap;
	}

	public void setServiceTypeMap(final SortedMap<String, String> serviceTypeMap) {
		this.serviceTypeMap = serviceTypeMap;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(final String deptId) {
		this.deptId = deptId;
	}

	public void setEgovCommon(final EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}

	public Boolean getIsVoucherOnReceiptAndStatusDisplay() {
		return isVoucherOnReceiptAndStatusDisplay;
	}

	public void setIsVoucherOnReceiptAndStatusDisplay(Boolean isVoucherOnReceiptAndStatusDisplay) {
		this.isVoucherOnReceiptAndStatusDisplay = isVoucherOnReceiptAndStatusDisplay;
	}

	public Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}
}
