/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */
package org.egov.tl.web.actions.newtradelicense;

import org.springframework.beans.factory.annotation.Qualifier;
import static org.egov.tl.utils.Constants.LOCALITY;
import static org.egov.tl.utils.Constants.LOCATION_HIERARCHY_TYPE;
import static org.egov.tl.utils.Constants.TRANSACTIONTYPE_CREATE_LICENSE;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.web.struts.annotation.ValidationErrorPageExt;
import org.egov.infstr.services.PersistenceService;
import org.egov.tl.domain.entity.FeeMatrixDetail;
import org.egov.tl.domain.entity.License;
import org.egov.tl.domain.entity.LicenseAppType;
import org.egov.tl.domain.entity.LicenseDocumentType;
import org.egov.tl.domain.entity.Licensee;
import org.egov.tl.domain.entity.MotorDetails;
import org.egov.tl.domain.entity.TradeLicense;
import org.egov.tl.domain.entity.WorkflowBean;
import org.egov.tl.domain.service.BaseLicenseService;
import org.egov.tl.domain.service.FeeMatrixService;
import org.egov.tl.domain.service.TradeService;
import org.egov.tl.domain.service.masters.LicenseCategoryService;
import org.egov.tl.domain.service.masters.LicenseSubCategoryService;
import org.egov.tl.domain.service.masters.UnitOfMeasurementService;
import org.egov.tl.utils.Constants;
import org.egov.tl.web.actions.BaseLicenseAction;
import org.egov.tl.web.actions.domain.CommonTradeLicenseAjaxAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@ParentPackage("egov")
@Results({
	@Result(name = Constants.EDIT, location = "editTradeLicense-" + Constants.EDIT + ".jsp"),
	@Result(name = Constants.NEW, location = "newTradeLicense-" + Constants.NEW + ".jsp"),
	@Result(name = Constants.MESSAGE, location = "editTradeLicense-" + Constants.MESSAGE + ".jsp")
})
public class EditTradeLicenseAction extends BaseLicenseAction {
	private static final long serialVersionUID = 1L;
	private TradeLicense tradeLicense = new TradeLicense();
	private TradeService ts;
	private PersistenceService<TradeLicense, Long> tps;
	private boolean isOldLicense = false;
	@Autowired
	private BoundaryService boundaryService;
	@Autowired
	private SecurityUtils securityUtils;
	private List<LicenseDocumentType> documentTypes = new ArrayList<>();
	private String mode;
	private Map<String, String> ownerShipTypeMap;
	@Autowired
	@Qualifier("licenseCategoryService")
	private LicenseCategoryService licenseCategoryService;
	@Autowired
	@Qualifier("licenseSubCategoryService")
	private LicenseSubCategoryService licenseSubCategoryService;
	@Autowired
	private BaseLicenseService baseLicenseService;
	@Autowired
	@Qualifier("unitOfMeasurementService")
	private UnitOfMeasurementService unitOfMeasurementService;
	@Autowired
	private FeeMatrixService feeMatrixService;
	BigDecimal totalAmount = BigDecimal.ZERO;
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("en", "IN"));
	private List<MotorDetails> installedMotorList = new ArrayList<MotorDetails>();

	private Long id;
	public EditTradeLicenseAction() {
		super();
		tradeLicense.setLicensee(new Licensee());
	}

	/* to log errors and debugging information */
	private final Logger LOGGER = Logger.getLogger(getClass());

	@Override
	public License getModel() {
		return tradeLicense;
	}

	public void setModel(final TradeLicense tradeLicense) {
		this.tradeLicense = tradeLicense;
	}

	public void prepareBeforeEdit() {
		LOGGER.debug("Entering in the prepareBeforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");
		prepareNewForm();
		setDocumentTypes(service().getDocumentTypesByTransaction(TRANSACTIONTYPE_CREATE_LICENSE));
		Long id = null;
		if (tradeLicense.getId() == null)
			if (getSession().get("model.id") != null) {
				id = (Long) getSession().get("model.id");
				getSession().remove("model.id");
			}
			else
				id = tradeLicense.getId();
		else
			id = tradeLicense.getId();
		tradeLicense = (TradeLicense) persistenceService.find("from TradeLicense where id = ?", id);
		if (tradeLicense.getOldLicenseNumber() != null)
			isOldLicense = org.apache.commons.lang.StringUtils.isNotBlank(tradeLicense.getOldLicenseNumber());
		final Boundary licenseboundary = boundaryService.getBoundaryById(tradeLicense.getBoundary().getId());
		List cityZoneList = new ArrayList();
		//  cityZoneList = licenseUtils.getAllZone();
		tradeLicense.setLicenseZoneList(cityZoneList);
		if (licenseboundary.getName().contains("Zone"))
			addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE, Collections.EMPTY_LIST);
		else if (tradeLicense.getLicensee().getBoundary() != null)
			addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE,
					new ArrayList(tradeLicense.getBoundary().getParent().getChildren())); 


		final Long userId = securityUtils.getCurrentUser().getId(); 
		if (userId != null)
			setRoleName(licenseUtils.getRolesForUserId(userId));

		LOGGER.debug("Exiting from the prepareBeforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");

		setOwnerShipTypeMap(Constants.OWNERSHIP_TYPE);
		final List<Boundary> localityList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
				LOCALITY, LOCATION_HIERARCHY_TYPE);
		addDropdownData("localityList", localityList);
		addDropdownData("tradeTypeList", baseLicenseService.getAllNatureOfBusinesses());
		addDropdownData("categoryList", licenseCategoryService.findAll());
		addDropdownData("uomList", unitOfMeasurementService.findAllActiveUOM());

		final CommonTradeLicenseAjaxAction ajaxTradeLicenseAction = new CommonTradeLicenseAjaxAction();
		populateSubCategoryList(ajaxTradeLicenseAction,tradeLicense.getCategory()!=null);

	}

	/**
	 * @param ajaxTradeLicenseAction
	 * @param categoryPopulated
	 */
	protected void populateSubCategoryList(final CommonTradeLicenseAjaxAction ajaxTradeLicenseAction, final boolean categoryPopulated)  {
		if (categoryPopulated) {
			ajaxTradeLicenseAction.setCategoryId(tradeLicense.getCategory().getId());
			ajaxTradeLicenseAction.setLicenseSubCategoryService(licenseSubCategoryService);
			ajaxTradeLicenseAction.populateSubCategory();
			addDropdownData("subCategoryList", ajaxTradeLicenseAction.getSubCategoryList());
		} else
			addDropdownData("subCategoryList", Collections.emptyList());  
	}


	@SkipValidation
	@Action(value = "/newtradelicense/editTradeLicense-beforeEdit")
	public String beforeEdit() {
		mode=EDIT;
		return NEW;
	}
	public void setupBeforeEdit() {
		LOGGER.debug("Entering in the setupBeforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");
		prepareBeforeEdit();
		setupWorkflowDetails();
		LOGGER.debug("Exiting from the setupBeforeEdit method:<<<<<<<<<<>>>>>>>>>>>>>:");
	}

	public void prepare ()
	{
		if(id!= null) {
			tradeLicense = tps.findById(id, false);     
		}
	}

	@ValidationErrorPageExt(
			action = "edit", makeCall = true, toMethod = "setupBeforeEdit")
	@Action(value = "/newtradelicense/editTradeLicense-edit")
	public String edit() {
		LOGGER.debug("Edit Trade License Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + tradeLicense.toString());
		if (tradeLicense.getState() == null && !isOldLicense)
			service().transitionWorkFlow(tradeLicense, workflowBean); 
		if (!isOldLicense)
			processWorkflow(NEW);
		if (installedMotorList != null) {
			final List<MotorDetails> motorDetailsList = new ArrayList<MotorDetails>();
			final Iterator<MotorDetails> motorDetails = installedMotorList.iterator();
			while (motorDetails.hasNext()) {
				final MotorDetails installedMotor = motorDetails.next();
				if (installedMotor != null && installedMotor.getHp() != null && installedMotor.getNoOfMachines() != null
						&& installedMotor.getHp().compareTo(BigDecimal.ZERO) != 0
						&& installedMotor.getNoOfMachines().compareTo(Long.valueOf("0")) != 0){
					installedMotor.setLicense(tradeLicense);
					motorDetailsList.add(installedMotor);
				}
			}
			if (!tradeLicense.getInstalledMotorList().isEmpty()) {
				for (final MotorDetails md : tradeLicense.getInstalledMotorList()) 
					tradeLicense.getInstalledMotorList().remove(getPersistenceService().findById(md.getId(), false));
			}
			if(installedMotorList!=null  && !installedMotorList.isEmpty()){
				tradeLicense.getInstalledMotorList().clear();
				tradeLicense.getInstalledMotorList().addAll(motorDetailsList);
			}
		}

		service().processAndStoreDocument(tradeLicense.getDocuments());

		LicenseAppType newAppType = (LicenseAppType)persistenceService.find("from  LicenseAppType where name='New' ");
		tradeLicense.setLicenseAppType(newAppType);

		tradeLicense= (TradeLicense)  persistenceService.update(tradeLicense); 
		List<FeeMatrixDetail> feeList = feeMatrixService.findFeeList(tradeLicense);
		totalAmount = service().recalculateDemand(feeList,tradeLicense);

		/*
		 * if (tradeLicense.getOldLicenseNumber() != null) doAuditing(AuditModule.TL, AuditEntity.TL_LIC, AuditEvent.MODIFIED,
		 * tradeLicense.getAuditDetails());
		 */
		LOGGER.debug("Exiting from the edit method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return Constants.MESSAGE;

	}

	@Override
	public boolean acceptableParameterName(final String paramName) {
		final List<String> nonAcceptable = Arrays.asList(new String[] { "licensee.boundary.parent", "boundary.parent",
		"tradeName.name" });
		final boolean retValue = super.acceptableParameterName(paramName);
		return retValue ? !nonAcceptable.contains(paramName) : retValue;
	}

	public WorkflowBean getWorkflowBean() {
		return workflowBean;
	}

	@Override
	protected License license() {
		return tradeLicense;
	}

	@Override
	protected BaseLicenseService service() {
		ts.getPersistenceService().setType(TradeLicense.class);
		return ts;
	}

	public void setTs(final TradeService ts) {
		this.ts = ts;
	}

	public void setWorkflowBean(final WorkflowBean workflowBean) {
		this.workflowBean = workflowBean;
	}

	public boolean getIsOldLicense() {
		return isOldLicense;
	}

	public void setIsOldLicense(final boolean isOldLicense) {
		this.isOldLicense = isOldLicense;
	}

	public List<LicenseDocumentType> getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(List<LicenseDocumentType> documentTypes) {
		this.documentTypes = documentTypes;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Map<String, String> getOwnerShipTypeMap() {
		return ownerShipTypeMap;
	}

	public void setOwnerShipTypeMap(Map<String, String> ownerShipTypeMap) {
		this.ownerShipTypeMap = ownerShipTypeMap;
	}

	public void setTps(PersistenceService<TradeLicense, Long> tps) {
		this.tps = tps;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<MotorDetails> getInstalledMotorList() {
		return installedMotorList;
	}

	public void setInstalledMotorList(List<MotorDetails> installedMotorList) {
		this.installedMotorList = installedMotorList;
	}

}