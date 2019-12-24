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

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.service.CategoryService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.models.ServiceCategory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Results({ @Result(name = ServiceCategoryAction.NEW, location = "serviceCategory-new.jsp"),
		@Result(name = ServiceCategoryAction.EDIT, location = "serviceCategory-edit.jsp"),
		@Result(name = ServiceCategoryAction.INDEX, location = "serviceCategory-index.jsp"),
		@Result(name = ServiceCategoryAction.MESSAGE, location = "serviceCategory-message.jsp") })
public class ServiceCategoryAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private Collection<ServiceCategory> serviceCategoryList = null;
	private ServiceCategory serviceCategoryInstance = new ServiceCategory();
	private String code;
	protected static final String MESSAGE = "message";

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private CategoryService serviceCategoryService;

	@Action(value = "/service/serviceCategory-newform")
	public String newform() {
		return NEW;
	}

	@Action(value = "/service/serviceCategory-list")
	public String list() {
		final Query query = entityManager.createQuery("from ServiceCategory order by code ");
		serviceCategoryList = query.getResultList();
		return INDEX;
	}

	@Action(value = "/service/serviceCategory-edit")
	public String edit() {
		final Query query = entityManager.createNamedQuery(CollectionConstants.QUERY_SERVICE_CATEGORY_BY_CODE);
		query.setParameter(1, code);
		serviceCategoryInstance = (ServiceCategory) query.getSingleResult();
		return EDIT;
	}

	@Action(value = "/service/serviceCategory-save")
	public String save() {
		validateServiceCategory();
		if (hasActionErrors())
			return EDIT;
		serviceCategoryService.persist(serviceCategoryInstance);
		return list();
	}

	@Action(value = "/service/serviceCategory-create")
	public String create() {
		validateServiceCategory();
		if (hasActionErrors())
			return NEW;
		serviceCategoryService.persist(serviceCategoryInstance);
		addActionMessage(getText("service.create.success.msg",
				new String[] { serviceCategoryInstance.getCode(), serviceCategoryInstance.getName() }));
		return MESSAGE;

	}


	public void validateServiceCategory() {
		if (StringUtils.isEmpty(serviceCategoryInstance.getName()))
			addActionError(getText("serviceCategoryName.null.validation"));
		if (StringUtils.isEmpty(serviceCategoryInstance.getCode()))
			addActionError(getText("serviceCategoryCode.null.validation"));
		else {
			if ((serviceCategoryInstance.getId() == null
					&& serviceCategoryService.getServiceCategoryByCode(serviceCategoryInstance.getCode()).isPresent())
					|| (serviceCategoryInstance.getId() != null
							&& serviceCategoryService.getServiceCategoryByCodeAndId(serviceCategoryInstance.getCode(),
									serviceCategoryInstance.getId()).isPresent()))
				addActionError(getText("masters.serviceCategoryCode.isunique"));
		}

	}

	@Override
	public Object getModel() {
		return serviceCategoryInstance;
	}

	/**
	 * @return the serviceCategoryList
	 */
	public Collection<ServiceCategory> getServiceCategoryList() {
		return serviceCategoryList;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}
}
