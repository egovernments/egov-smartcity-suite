package org.egov.erpcollection.web.actions.service;

import java.util.Collection;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.infstr.models.ServiceCategory;
import org.egov.infstr.services.PersistenceService;
import org.egov.web.actions.BaseFormAction;

@Result(name = ServiceCategoryAction.SUCCESS, type="redirect", location = "serviceCategory.action")
@ParentPackage("egov")
public class ServiceCategoryAction extends BaseFormAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PersistenceService<ServiceCategory, Long> serviceCategoryService;
	private Collection<ServiceCategory> serviceCategoryList = null;
	private ServiceCategory serviceCategoryInstance = new ServiceCategory();

	@Override
	public String execute() throws Exception {
		return list();
	}

	public String newform() {
		return NEW;
	}

	public String list() {
		serviceCategoryList = serviceCategoryService
				.findAll(CollectionConstants.SERVICECATEGORY_CODE);
		return INDEX;
	}

	public String edit() {
		serviceCategoryInstance = serviceCategoryService.findById(
				serviceCategoryInstance.getId(), false);
		return EDIT;
	}

	public String save() {
		serviceCategoryService.update(serviceCategoryInstance);
		return SUCCESS;
	}

	public String create() {
		serviceCategoryService.create(serviceCategoryInstance);
		return SUCCESS;
	}

	public Object getModel() {
		return serviceCategoryInstance;
	}

	public void setModel(ServiceCategory serviceCategoryInstance) {
		this.serviceCategoryInstance = serviceCategoryInstance;
	}

	/**
	 * @return the serviceCategoryList
	 */
	public Collection<ServiceCategory> getServiceCategoryList() {
		return serviceCategoryList;
	}

	/**
	 * @param serviceCategoryService
	 *            the serviceCategoryService to set
	 */
	public void setServiceCategoryService(
			PersistenceService<ServiceCategory, Long> serviceCategoryService) {
		this.serviceCategoryService = serviceCategoryService;
	}

}
