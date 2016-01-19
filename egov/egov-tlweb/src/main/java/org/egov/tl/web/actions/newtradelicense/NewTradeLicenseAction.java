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
import static org.egov.tl.utils.Constants.BUTTONAPPROVE;
import static org.egov.tl.utils.Constants.LOCALITY;
import static org.egov.tl.utils.Constants.LOCATION_HIERARCHY_TYPE;
import static org.egov.tl.utils.Constants.TRANSACTIONTYPE_CREATE_LICENSE;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseAppType;
import org.egov.tl.entity.LicenseDocumentType;
import org.egov.tl.entity.LicenseStatus;
import org.egov.tl.entity.Licensee;
import org.egov.tl.entity.MotorDetails;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.service.BaseLicenseService;
import org.egov.tl.service.TradeService;
import org.egov.tl.service.masters.LicenseCategoryService;
import org.egov.tl.service.masters.LicenseSubCategoryService;
import org.egov.tl.service.masters.UnitOfMeasurementService;
import org.egov.tl.utils.Constants;
import org.egov.tl.web.actions.BaseLicenseAction;
import org.egov.tl.web.actions.domain.CommonTradeLicenseAjaxAction;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Results({ @Result(name = NewTradeLicenseAction.NEW, location = "newTradeLicense-new.jsp"),
	@Result(name = Constants.ACKNOWLEDGEMENT, location = "newTradeLicense-" + Constants.ACKNOWLEDGEMENT + ".jsp"),
	@Result(name = Constants.PFACERTIFICATE, location = "/WEB-INF/jsp/viewtradelicense/viewTradeLicense-" + Constants.PFACERTIFICATE + ".jsp"),
	@Result(name = Constants.MESSAGE, location = "newTradeLicense-" + Constants.MESSAGE + ".jsp"),
	@Result(name = Constants.BEFORE_RENEWAL, location = "newTradeLicense-" + Constants.BEFORE_RENEWAL + ".jsp"),
	@Result(name = Constants.ACKNOWLEDGEMENT_RENEW, location = "newTradeLicense-" + Constants.ACKNOWLEDGEMENT_RENEW + ".jsp") })
public class NewTradeLicenseAction extends BaseLicenseAction {

	private static final long serialVersionUID = 1L;
	protected TradeLicense tradeLicense = new TradeLicense();
	WorkflowService<TradeLicense> tradeLicenseWorkflowService;
	private TradeService ts;
	@Autowired
	private BoundaryService boundaryService;
	private List<LicenseDocumentType> documentTypes = new ArrayList<>();
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
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("en", "IN"));
	private String mode;

	public NewTradeLicenseAction() {
		super();
		tradeLicense.setLicensee(new Licensee());
	}

	/* to log errors and debugging information */
	private final Logger LOGGER = Logger.getLogger(getClass());

	@Override
	@SkipValidation
	@Action(value = "/newtradelicense/newTradeLicense-newForm")
	public String newForm() {
		tradeLicense.setApplicationDate(new Date());
		return super.newForm();
	}

	@Override
	@ValidationErrorPage(Constants.NEW)
	@Action(value = "/newtradelicense/newTradeLicense-approve")
	public String approve() {

		tradeLicense = (TradeLicense) persistenceService.find("from TradeLicense where id=?", getSession().get("model.id"));
		if(mode.equalsIgnoreCase(VIEW) && tradeLicense!=null && !tradeLicense.isPaid() &&
				(!workFlowAction.equalsIgnoreCase(Constants.BUTTONREJECT))){ 
			prepareNewForm();
			ValidationError vr=new ValidationError("license.fee.notcollected", "license.fee.notcollected");
			throw new ValidationException(Arrays.asList(vr) );
		}
		if (BUTTONAPPROVE.equals(workFlowAction)) {
			license().setCreationAndExpiryDate();
			if (license().getTempLicenseNumber() == null) {
				final String nextRunningLicenseNumber = service().getNextRunningLicenseNumber(
						"egtl_license_number");
				license().generateLicenseNumber(nextRunningLicenseNumber);
			}
			final LicenseStatus activeStatus = (LicenseStatus) persistenceService
					.find("from org.egov.tl.entity.LicenseStatus where code='ACT'");
			license().setStatus(activeStatus);
		}

		return super.approve();
	}

	@Override
	@ValidationErrorPage(Constants.NEW)
	@Action(value = "/newtradelicense/newTradeLicense-create")
	public String create() {
		try {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Trade license Creation Parameters:<<<<<<<<<<>>>>>>>>>>>>>:" + tradeLicense);
			if (tradeLicense.getInstalledMotorList() != null) {
				final Iterator<MotorDetails> motorDetails = tradeLicense.getInstalledMotorList().iterator();
				while (motorDetails.hasNext()) {
					final MotorDetails installedMotor = motorDetails.next();
					if (installedMotor != null && installedMotor.getHp() != null && installedMotor.getNoOfMachines() != null
							&& installedMotor.getHp().compareTo(BigDecimal.ZERO) != 0
							&& installedMotor.getNoOfMachines().compareTo(Long.valueOf("0")) != 0)
						installedMotor.setLicense(tradeLicense);
					else
						motorDetails.remove();
				}

			}
			if (LOGGER.isDebugEnabled())
				LOGGER.debug(" Create Trade License Application Name of Establishment :"
						+ tradeLicense.getNameOfEstablishment());
			LicenseAppType newAppType = (LicenseAppType)persistenceService.find("from  LicenseAppType where name='New' ");
			tradeLicense.setLicenseAppType(newAppType);
			return super.create(tradeLicense);
		} catch (RuntimeException e) {
			ValidationError vr=new ValidationError(e.getMessage(), e.getMessage());
			throw new ValidationException(Arrays.asList(vr) );
		} 
	}

	@Override
	public void prepareNewForm() {
		super.prepareNewForm();
		if(license()!=null && license().getId()!=null)
			tradeLicense = (TradeLicense) persistenceService.find("from TradeLicense where id=?", license().getId());
		setDocumentTypes(service().getDocumentTypesByTransaction(TRANSACTIONTYPE_CREATE_LICENSE));
		tradeLicense.setHotelGradeList(tradeLicense.populateHotelGradeList());
		tradeLicense.setHotelSubCatList(ts.getHotelCategoriesForTrade());
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


	@Override
	@ValidationErrorPage(Constants.BEFORE_RENEWAL)
	@SkipValidation
	@Action(value = "/newtradelicense/newTradeLicense-renewal")
	public String renew() {
		try {
			LOGGER.debug("Trade license renew Parameters:<<<<<<<<<<>>>>>>>>>>>>>:"
					+ tradeLicense);
			final BigDecimal deduction = tradeLicense.getDeduction();
			final BigDecimal otherCharges = tradeLicense.getOtherCharges();
			final BigDecimal swmFee = tradeLicense.getSwmFee();
			tradeLicense = (TradeLicense) ts.getPersistenceService()
					.find("from License where id=?", tradeLicense.getId());
			tradeLicense.setOtherCharges(otherCharges);
			tradeLicense.setDeduction(deduction);
			tradeLicense.setSwmFee(swmFee);
			LOGGER
			.debug("Renew Trade License Application Name of Establishment:<<<<<<<<<<>>>>>>>>>>>>>:"
					+ tradeLicense.getNameOfEstablishment());
			return super.renew();
		} catch (Exception e) {
			throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
		}
	}

	@Override
	@SkipValidation
	@Action(value = "/newtradelicense/newTradeLicense-beforeRenew")
	public String beforeRenew() {
		LOGGER
		.debug("Entering in the beforeRenew method:<<<<<<<<<<>>>>>>>>>>>>>:");
		tradeLicense = (TradeLicense) ts.getPersistenceService()
				.find("from License where id=?", tradeLicense.getId());

		License license=(License)tradeLicense;

		if(!license.getState().getValue().isEmpty())
		{
			license.transition(true).withStateValue("");
		}

		LOGGER.debug("Exiting from the beforeRenew method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return super.beforeRenew();
	}

	/*
	 * Invoked from Workflow users Inbox 
	 */
	@Override
	@Action(value = "/newtradelicense/newTradeLicense-showForApproval")
	public String showForApproval() {
		mode=VIEW;
		return super.showForApproval();
	}

	@Override
	public License getModel() {
		return tradeLicense;
	}

	public WorkflowBean getWorkflowBean() {
		return workflowBean;
	}

	@Override
	protected License license() {
		return tradeLicense;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected BaseLicenseService service() {
		ts.getPersistenceService().setType(TradeLicense.class);
		return ts;
	}

	@SuppressWarnings("unchecked")
	public void setTradeLicenseWorkflowService(
			final WorkflowService tradeLicenseWorkflowService) {
		this.tradeLicenseWorkflowService = tradeLicenseWorkflowService;
	}

	public void setTs(final TradeService ts) {
		this.ts = ts;
	}

	public void setWorkflowBean(final WorkflowBean workflowBean) {
		this.workflowBean = workflowBean;
	}

	public List<LicenseDocumentType> getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(final List<LicenseDocumentType> documentTypes) {
		this.documentTypes = documentTypes;
	}

	public Map<String, String> getOwnerShipTypeMap() {
		return ownerShipTypeMap;
	}

	public void setOwnerShipTypeMap(final Map<String, String> ownerShipTypeMap) {
		this.ownerShipTypeMap = ownerShipTypeMap;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
}