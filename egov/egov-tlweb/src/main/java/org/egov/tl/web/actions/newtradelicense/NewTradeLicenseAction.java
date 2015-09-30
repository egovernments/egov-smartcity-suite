/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) <2015>  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *  	1) All versions of this program, verbatim or modified must carry this
 *  	   Legal Notice.
 *
 *  	2) Any misrepresentation of the origin of the material is prohibited. It
 *  	   is required that all modified versions of this material be marked in
 *  	   reasonable ways as different from the original version.
 *
 *  	3) This license does not grant any rights to any user of the program
 *  	   with regards to rights under trademark law for use of the trade names
 *  	   or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.tl.web.actions.newtradelicense;


import static org.egov.tl.utils.Constants.BUTTONAPPROVE;
import static org.egov.tl.utils.Constants.LOCALITY;
import static org.egov.tl.utils.Constants.LOCATION_HIERARCHY_TYPE;
import static org.egov.tl.utils.Constants.TRANSACTIONTYPE_CREATE_LICENSE;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.tl.domain.entity.License;
import org.egov.tl.domain.entity.LicenseDocumentType;
import org.egov.tl.domain.entity.LicenseStatus;
import org.egov.tl.domain.entity.Licensee;
import org.egov.tl.domain.entity.MotorDetails;
import org.egov.tl.domain.entity.TradeLicense;
import org.egov.tl.domain.entity.WorkflowBean;
import org.egov.tl.domain.service.BaseLicenseService;
import org.egov.tl.domain.service.TradeService;
import org.egov.tl.domain.service.masters.LicenseCategoryService;
import org.egov.tl.domain.service.masters.LicenseSubCategoryService;
import org.egov.tl.domain.service.masters.UnitOfMeasurementService;
import org.egov.tl.utils.Constants;
import org.egov.tl.web.actions.BaseLicenseAction;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Results({ @Result(name = NewTradeLicenseAction.NEW, location = "newTradeLicense-new.jsp"),
        @Result(name = Constants.ACKNOWLEDGEMENT, location = "newTradeLicense-" + Constants.ACKNOWLEDGEMENT + ".jsp"),
        @Result(name = Constants.MESSAGE, location = "newTradeLicense-" + Constants.MESSAGE + ".jsp"),
        @Result(name = Constants.BEFORE_RENEWAL, location = "newTradeLicense-" + Constants.BEFORE_RENEWAL + ".jsp"),
        @Result(name = Constants.ACKNOWLEDGEMENT_RENEW, location = "newTradeLicense-" + Constants.ACKNOWLEDGEMENT_RENEW + ".jsp")})
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
    private LicenseCategoryService licenseCategoryService;
    @Autowired
    private LicenseSubCategoryService licenseSubCategoryService;
    @Autowired
    private BaseLicenseService baseLicenseService;
    @Autowired
    private UnitOfMeasurementService unitOfMeasurementService;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("en", "IN"));
    

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
    @SkipValidation
    @Action(value = "/newtradelicense/newTradeLicense-approve")
    public String approve() {
        tradeLicense = (TradeLicense) persistenceService.find("from TradeLicense where id=?", getSession().get("model.id"));
        if (BUTTONAPPROVE.equals(workFlowAction)) {
            license().setCreationAndExpiryDate();
            if (!license().isPaid())
                throw new ValidationException("applicationNumber", "license.fee.notcollected", license().getApplicationNumber());
            if (license().getTempLicenseNumber() == null) {
                final String nextRunningLicenseNumber = service().getNextRunningLicenseNumber(
                        "egtl_" + license().getFeeTypeStr()
                                + "_license_number");
                license().generateLicenseNumber(nextRunningLicenseNumber);
            }
            final LicenseStatus activeStatus = (LicenseStatus) persistenceService
                    .find("from org.egov.tl.domain.entity.LicenseStatus where code='ACT'");
            license().setStatus(activeStatus);
        }
        return super.approve();
    }

    @ValidationErrorPage(Constants.NEW)
    @Action(value = "/newtradelicense/newTradeLicense-create")
    public String create() {
        LOGGER.debug("Trade license Creation Parameters:<<<<<<<<<<>>>>>>>>>>>>>:" + tradeLicense);
        if (tradeLicense.getLicenseZoneId() != null && tradeLicense.getBoundary() == null) {
            final Boundary boundary = boundaryService.getBoundaryById(tradeLicense.getLicenseZoneId()); 
            tradeLicense.setBoundary(boundary);
        }

        if (tradeLicense.getLicenseeZoneId() != null && tradeLicense.getLicensee().getBoundary() == null) {
            final Boundary boundary = boundaryService.getBoundaryById(tradeLicense.getLicenseeZoneId());
            tradeLicense.getLicensee().setBoundary(boundary);
        }
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
        LOGGER.debug(" Create Trade License Application Name of Establishment:<<<<<<<<<<>>>>>>>>>>>>>:"
                + tradeLicense.getNameOfEstablishment());
        return super.create(); 
    }

    @Override
    public void prepareNewForm() {
        super.prepareNewForm();
        setDocumentTypes(service().getDocumentTypesByTransaction(TRANSACTIONTYPE_CREATE_LICENSE));
        tradeLicense.setHotelGradeList(tradeLicense.populateHotelGradeList());
        tradeLicense.setHotelSubCatList(ts.getHotelCategoriesForTrade());
        setOwnerShipTypeMap(Constants.OWNERSHIP_TYPE); 
        final List<Boundary> localityList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                LOCALITY, LOCATION_HIERARCHY_TYPE);
        addDropdownData("localityList", localityList);
        addDropdownData("tradeTypeList", baseLicenseService.getAllNatureOfBusinesses());
        addDropdownData("categoryList", licenseCategoryService.findAll());
        addDropdownData("subCategoryList", Collections.EMPTY_LIST);
        addDropdownData("uomList", unitOfMeasurementService.findAllActiveUOM());
    }

    @Override
    @SkipValidation
    @Action(value = "/newtradelicense/newTradeLicense-renewal")
    public String renew() {
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
    }

    @Override
    @SkipValidation
    @Action(value = "/newtradelicense/newTradeLicense-beforeRenew")
    public String beforeRenew() {
        LOGGER
        .debug("Entering in the beforeRenew method:<<<<<<<<<<>>>>>>>>>>>>>:");
        tradeLicense = (TradeLicense) ts.getPersistenceService()
                .find("from License where id=?", tradeLicense.getId());
        LOGGER
        .debug("Exiting from the beforeRenew method:<<<<<<<<<<>>>>>>>>>>>>>:");
        return super.beforeRenew();
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

    public void setDocumentTypes(List<LicenseDocumentType> documentTypes) {
        this.documentTypes = documentTypes;
    }

    public Map<String, String> getOwnerShipTypeMap() {
        return ownerShipTypeMap;
    }

    public void setOwnerShipTypeMap(Map<String, String> ownerShipTypeMap) {
        this.ownerShipTypeMap = ownerShipTypeMap;
    }


}
