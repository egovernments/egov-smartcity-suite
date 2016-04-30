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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.bnd.web.actions.registration;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bnd.model.CRelation;
import org.egov.bnd.model.NonAvailability;
import org.egov.bnd.model.Registrar;
import org.egov.bnd.model.RegistrationUnit;
import org.egov.bnd.services.common.NumberGenerationService;
import org.egov.bnd.services.registration.NonAvailabilityRegistrationService;
import org.egov.bnd.utils.BndConstants;
import org.egov.bnd.web.actions.common.BndCommonAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.workflow.entity.StateAware;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Map;


@Validations(

        requiredStrings = {
                @RequiredStringValidator(fieldName = "applicantName", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "applicantRelationName", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "citizenName", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "citizenRelationName", type = ValidatorType.FIELD, message = "Required", key = "")

        },

        requiredFields = {
                @RequiredFieldValidator(fieldName = "yearOfEvent", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredFieldValidator(fieldName = "no_Of_copies", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredFieldValidator(fieldName = "cost", type = ValidatorType.FIELD, message = "Required", key = "")

        })
@Namespace("/registration")
@ParentPackage("egov")
@Transactional(readOnly = true)
public class NonAvailabilityRegistrationAction extends BndCommonAction {

    private static final long serialVersionUID = 429651100769629974L;
    private static final String AJAX_RESULT_VIEWCOLLECTTAX = "viewCollectTax";
    
    private NonAvailability nonAvailableReg = new NonAvailability();
    private static final Logger LOGGER = Logger.getLogger(NonAvailabilityRegistrationAction.class);
    private Integer birtDeathFlag;
    private NonAvailabilityRegistrationService nonAvailableRegService;
    private NumberGenerationService numberGenerationService;
    private final String[] relations = { "SON", "WIFE", "DAUGHTER" };
    protected Integer reportId = -1;
    // private FeeCollectionServiceImpl feeCollectionServiceImpl;
    // private FeeCollectionService feeCollectionService;
    private String collectXML;
    // private BndFeeBillable bndFeeBillable;
    public static final String PRINTNA = "printna";
    private InputStream nonAvailCertificatePDF;

    public NonAvailabilityRegistrationAction() {
        addRelatedEntity("citizenRelationType", CRelation.class);
        addRelatedEntity("applicantRelationType", CRelation.class);
        addRelatedEntity("modifiedBy", User.class);
        addRelatedEntity("createdBy", User.class);
    }

    public void setNumberGenerationService(final NumberGenerationService numberGenerationService) {
        this.numberGenerationService = numberGenerationService;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(final Integer reportId) {
        this.reportId = reportId;
    }

    public void setNonAvailableRegService(final NonAvailabilityRegistrationService nonAvailableRegService) {
        this.nonAvailableRegService = nonAvailableRegService;
    }

    public Integer getBirtDeathFlag() {
        return birtDeathFlag;
    }

    public void setBirtDeathFlag(final Integer birtDeathFlag) {
        this.birtDeathFlag = birtDeathFlag;
    }

    public NonAvailability getNonAvailableReg() {
        return nonAvailableReg;
    }

    public void setNonAvailableReg(final NonAvailability nonAvailableReg) {
        this.nonAvailableReg = nonAvailableReg;
    }

    @Override
    public StateAware getModel() {
        return nonAvailableReg;
    }

    @Override
    @SkipValidation
    @Transactional
    @Action(value = "/nonAvailabilityRegistration-newform", results = { @Result(name = NEW, type = "dispatcher") })
    public String newform() {
        final Registrar registrar = bndCommonService.getRegistrarByLoggedInUser();
        if (registrar == null)
            throw new EGOVRuntimeException(getText("user.registrar.error"));
        else {
            final RegistrationUnit regUnit = registrar.getRegUnitId();
            if (regUnit != null)
                // TODO egifix-address
                
                  if(regUnit.getRegUnitAddress()!=null&&regUnit.getRegUnitAddress
                  ().getSubdistrict()!=null)
                  nonAvailableReg.setTalukName(regUnit.getRegUnitAddress
                  ()!=null?regUnit.getRegUnitAddress().getSubdistrict():"");
                 
                if (regUnit.getRegUnitAddress() != null && regUnit.getRegUnitAddress().getDistrict() != null)
                    nonAvailableReg.setDistrictName(regUnit.getRegUnitAddress() != null ? regUnit.getRegUnitAddress()
                            .getDistrict() : "");
                if (regUnit.getRegUnitAddress() != null && regUnit.getRegUnitAddress().getState() != null){
                    nonAvailableReg.setStateName(regUnit.getRegUnitAddress() != null ? regUnit
                            .getRegUnitAddress().getState() : "");
                }
             
            nonAvailableReg.setEventType(BndConstants.BIRTH);
            setValuesforNAform(nonAvailableReg);
        }

        LOGGER.debug("Completed Prepare a new form method");
        return NEW;
    }

    @Override
    @Transactional
    @Action(value = "nonAvailability-create", results = {@Result(name = AJAX_RESULT_VIEWCOLLECTTAX, type = "dispatcher")} )
    public String create() {
        saveOrUpdate();
        // FeeCollection feeCollection=saveFeeCollectionObjectAndGenerateBill();
        // FeeCollection
        // feeCollection=feeCollectionService.saveFeeCollectionUsingNonAvlRegnObject(nonAvailableReg);
        // bndFeeBillable.setFeeCollection(feeCollection);
        // collectXML = URLEncoder.encode(feeCollectionServiceImpl
        // .getBillXML(bndFeeBillable));

        mode = VIEW;
        return AJAX_RESULT_VIEWCOLLECTTAX;
    }

    @Override
    @Transactional
    protected void saveOrUpdate() {

        LOGGER.debug("Started saveOrUpdate method");
        nonAvailableReg.setEventType(getFormMap().get(getBirtDeathFlag()));
        if (nonAvailableReg.getRegistrationNo() == null || "".equals(nonAvailableReg.getRegistrationNo().trim()))
            nonAvailableReg.setRegistrationNo(numberGenerationService.getNonAvailableRegNumber(nonAvailableReg));
        nonAvailableRegService.save(nonAvailableReg, null);
        setMode(VIEW);
        setValuesforNAform(nonAvailableReg);
        LOGGER.debug("Completed saveOrUpdate method");

    }

    /*
     * private FeeCollection saveFeeCollectionObjectAndGenerateBill() {
     * FeeCollection feeCollection = new FeeCollection();
     * feeCollection.setType(BndConstants.SEARCHNONAVAILABILITY);
     * feeCollection.setApplicantAddress(nonAvailableReg.getTalukName() + " " +
     * nonAvailableReg.getDistrictName());
     * feeCollection.setApplicantName(nonAvailableReg.getApplicantName()); if
     * (EgovThreadLocals.getUserId() != null)
     * feeCollection.setCreatedBy(bndCommonService
     * .getUserByPassingUserId(Integer.valueOf(EgovThreadLocals .getUserId())));
     * feeCollection.setCollectionDate(DateUtils.today());
     * feeCollection.setNo_Of_copies(nonAvailableReg.getNo_Of_copies());
     * feeCollection.setRemarks(nonAvailableReg.getRemarks());
     * feeCollection.setReportId(nonAvailableReg.getId());
     * feeCollection.setStatusType(bndCommonService.getStatusByModuleAndCode(
     * BndConstants.BNDFEECOLLECTIONSTATUS,
     * BndConstants.BNDFEECOLLECTIONCREATEDSTATUS)); feeCollection
     * .setTotalAmount((nonAvailableReg.getCost()).multiply(BigDecimal
     * .valueOf(nonAvailableReg.getNo_Of_copies())));
     * buildFeeCollectionDetails(feeCollection); return
     * feeCollectionService.save(feeCollection); }
     */

    /*
     * private void buildFeeCollectionDetails(FeeCollection feeCollection) {
     * FeeCollectionDetails feeCollDetail= new FeeCollectionDetails();
     * feeCollDetail.setFeeCollection(feeCollection);
     * feeCollDetail.setAmount((nonAvailableReg
     * .getCost()).multiply(BigDecimal.valueOf
     * (nonAvailableReg.getNo_Of_copies())));
     * feeCollDetail.setFeeType(bndCommonService
     * .getBndFeeTypesByCode(BndConstants.NAFORMFEE)); }
     */

    public void setValuesforNAform(final NonAvailability nonAvailableReg) {

        LOGGER.debug("Started setValuesforNAform method");
        nonAvailableReg.setCost(bndCommonService.getCertFeeConfigValueforNAForm(BndConstants.BNDMODULE,
                BndConstants.KEYFORCERTIFICATECOST, "2"));
        addDropdownData("relationList", bndCommonService.getRelationTypesbyConstant(relations));
        nonAvailableReg.setTotalFee((nonAvailableReg.getCost() != null ? nonAvailableReg.getCost() : BigDecimal.ZERO)
                .multiply(BigDecimal.valueOf(nonAvailableReg.getNo_Of_copies() != null ? nonAvailableReg
                        .getNo_Of_copies() : 0)));
        setBirtDeathFlag(nonAvailableReg.getEventType().equals(BndConstants.BIRTH) ? Integer.valueOf(0) : Integer
                .valueOf(1));
        LOGGER.debug("Completed setValuesforNAform method");
    }

    @Override
    public void prepareView() {
        LOGGER.debug("Started prepareView method");
        nonAvailableReg = nonAvailableRegService.getNonAvailableRegById(idTemp);
        setValuesforNAform(nonAvailableReg);
        LOGGER.debug("Completed prepareView method");
    }

    /*@SkipValidation
    public String print() {

        reportId = generateCertificateService.generateCertificate(idTemp, BndConstants.SEARCHNONAVAILABILITY,
                getRoleNameByLoginUserId(), BndConstants.NONAVLREGN_TEMPLATE, getSession());
        return "report";
        
         * ReportOutput reportOutput =
         * generateCertificateService.generateCertificateBirthDeathNA(idTemp,
         * BndConstants.SEARCHNONAVAILABILITY,getRoleNameByLoginUserId(),
         * BndConstants.NONAVLREGN_TEMPLATE, getSession()); if (reportOutput !=
         * null && reportOutput.getReportOutputData() != null)
         * nonAvailCertificatePDF = new
         * ByteArrayInputStream(reportOutput.getReportOutputData()); return
         * PRINTNA;
         
    }*/

    @Override
    @Transactional
    public void prepareBeforeEdit() {
        LOGGER.debug("Started prepareBeforeEdit method");
        nonAvailableReg = nonAvailableRegService.getNonAvailableRegById(idTemp);
        setValuesforNAform(nonAvailableReg);
        LOGGER.debug("Completed prepareBeforeEdit method");
    }

    public Map<Integer, String> getFormMap() {
        return BndConstants.FormMap;
    }

    public String getCollectXML() {
        return collectXML;
    }

    @SuppressWarnings("deprecation")
    public void setCollectXML(final String collectXML) {
        this.collectXML = java.net.URLDecoder.decode(collectXML);
    }

    /*
     * public void setFeeCollectionServiceImpl( FeeCollectionServiceImpl
     * feeCollectionServiceImpl) { this.feeCollectionServiceImpl =
     * feeCollectionServiceImpl; } public void
     * setFeeCollectionService(FeeCollectionService feeCollectionService) {
     * this.feeCollectionService = feeCollectionService; } public void
     * setBndFeeBillable(BndFeeBillable bndFeeBillable) { this.bndFeeBillable =
     * bndFeeBillable; }
     */

    public InputStream getNonAvailCertificatePDF() {
        return nonAvailCertificatePDF;
    }

    public void setNonAvailCertificatePDF(final InputStream nonAvailCertificatePDF) {
        this.nonAvailCertificatePDF = nonAvailCertificatePDF;
    }
}
