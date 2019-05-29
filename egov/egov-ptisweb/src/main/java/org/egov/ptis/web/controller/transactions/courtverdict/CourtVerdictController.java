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
package org.egov.ptis.web.controller.transactions.courtverdict;

import static org.egov.ptis.constants.PropertyTaxConstants.ANONYMOUS_USER;
import static org.egov.ptis.constants.PropertyTaxConstants.BUILTUP_PROPERTY_DMDRSN_CODE_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.SOURCE_ONLINE;
import static org.egov.ptis.constants.PropertyTaxConstants.TARGET_WORKFLOW_ERROR;
import static org.egov.ptis.constants.PropertyTaxConstants.VACANT_PROPERTY_DMDRSN_CODE_MAP;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.ptis.actions.edit.EditDemandAction;
import org.egov.ptis.bean.demand.DemandDetail;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.CourtVerdict;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.PropertyCourtCase;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.service.courtverdict.CourtVerdictService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.master.service.PropertyCourtCaseService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/courtVerdict")
public class CourtVerdictController extends GenericWorkFlowController {

    private static final String IS_NEW = "isNew";
    private static final String COLLECTION = "collection";
    private static final String AMOUNT = "amount";
    private static final Logger LOGGER = Logger.getLogger(EditDemandAction.class);

    private static final String APPROVAL_POSITION = "approvalPosition";
    private static final String APPLICATION_SOURCE = "applicationSource";

    private Boolean loggedUserIsMeesevaUser = Boolean.FALSE;
    private boolean citizenPortalUser = false;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private PropertyCourtCaseService propCourtCaseService;
    @Autowired
    private CourtVerdictService courtVerdictService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    private List<DemandDetail> demandDetailBeanList = new ArrayList<>();
    private Map<String, String> demandReasonMap = new HashMap<>();

    private static final String ERROR_MSG = "errorMsg";
    private static final String COURT_VERDICT = "courtVerdict";
    private static final String LOGGED_IN_USER = "loggedUserIsEmployee";
    private static final String CITIZEN_PORTAL_USER = "citizenPortalUser";
    private static final String CURRENT_STATE = "currentState";
    private static final String STATE_TYPE = "stateType";
    private static final String ENDRSMNT_NOTICE = "endorsementNotices";
    private static final String CV_FORM = "courtVerdict-form";
    private static final String PROPERTY = "property";
    private static final String CREATED = "Created";
    private static final String APPROVAL_COMMENT = "approvalComent";
    private static final String WF_ACTION = "workFlowAction";

    CourtVerdict oldCourtVerdict;
    @Autowired
    private PtDemandDao ptDemandDAO;

    @ModelAttribute
    public CourtVerdict courtVerdict(@PathVariable("assessmentNo") String assessmentNo) {
        CourtVerdict courtVerdict = new CourtVerdict();
        BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        PropertyCourtCase propCourtCase = propCourtCaseService.getByAssessmentNo(assessmentNo);
        if (basicProperty != null && propCourtCase != null) {
            courtVerdict.setBasicProperty((BasicPropertyImpl) basicProperty);
            courtVerdict.setPropertyCourtCase(propCourtCase);
            PropertyImpl property = (PropertyImpl) basicProperty.getActiveProperty().createPropertyclone();
            courtVerdict.setProperty(property);

        }
        return courtVerdict;
    }

    @GetMapping(value = "/viewForm/{assessmentNo}")
    public String view(@ModelAttribute CourtVerdict courtVerdict, Model model, final HttpServletRequest request) {

        BasicProperty basicProperty = courtVerdict.getBasicProperty();
        PropertyImpl property = courtVerdict.getProperty();

        User loggedInUser = securityUtils.getCurrentUser();
        citizenPortalUser = propertyService.isCitizenPortalUser(loggedInUser);

        if (basicProperty.isUnderWorkflow() || courtVerdict.getPropertyCourtCase() == null) {
            model.addAttribute("wfPendingMsg",
                    "Could not do Court Verdict now, property is undergoing some work flow.");
            return TARGET_WORKFLOW_ERROR;
        }

        if (courtVerdict.getPropertyCourtCase() != null) {
            model.addAttribute(COURT_VERDICT, courtVerdict);
            model.addAttribute(PROPERTY, property);
            model.addAttribute(CITIZEN_PORTAL_USER, citizenPortalUser);
            model.addAttribute(CURRENT_STATE, CREATED);
            model.addAttribute(STATE_TYPE, courtVerdict.getClass().getSimpleName());
            model.addAttribute(ENDRSMNT_NOTICE, new ArrayList<>());
            model.addAttribute(LOGGED_IN_USER, propertyService.isEmployee(loggedInUser));
            courtVerdictService.addModelAttributes(model, basicProperty, request);

            Set<EgDemandDetails> demandDetails = (ptDemandDAO.getNonHistoryCurrDmdForProperty(basicProperty.getProperty()))
                    .getEgDemandDetails();

            List<EgDemandDetails> dmndDetails = new ArrayList<>(demandDetails);
            if (!dmndDetails.isEmpty())
                Collections.sort(dmndDetails, (o1, o2) -> o1.getEgDemandReason().getEgInstallmentMaster()
                        .compareTo(o2.getEgDemandReason().getEgInstallmentMaster()));

            setDemandBeanList(demandDetails, basicProperty);

            model.addAttribute("dmndDetails", demandDetailBeanList);
            prepareWorkflow(model, courtVerdict, new WorkflowContainer());

        }
        oldCourtVerdict = courtVerdict;
        return CV_FORM;
    }

    @PostMapping(value = "/viewForm/{assessmentNo}")
    public String save(@ModelAttribute("courtVerdict") CourtVerdict courtVerdict, final BindingResult resultBinder,
            final RedirectAttributes redirectAttributes, final Model model, final HttpServletRequest request,
            @RequestParam String workFlowAction, @PathVariable String assessmentNo) {
        String target = null;
        PropertyImpl property = courtVerdict.getProperty();
        PropertyImpl oldProperty = oldCourtVerdict.getBasicProperty().getActiveProperty();

        final Boolean loggedUserIsEmployee = Boolean.valueOf(request.getParameter(LOGGED_IN_USER));
        User loggedInUser = securityUtils.getCurrentUser();
        String action = courtVerdict.getAction();
        Map<String, String> errorMessages = new HashMap<>();
        courtVerdictSource(courtVerdict, request);
        if (action.equals("")) {
            errorMessages.put("action", "action.required");
            model.addAttribute(ERROR_MSG, errorMessages);
            model.addAttribute(COURT_VERDICT, oldCourtVerdict);
            model.addAttribute(PROPERTY, oldProperty);
            model.addAttribute(CITIZEN_PORTAL_USER, citizenPortalUser);
            model.addAttribute(CURRENT_STATE, CREATED);
            model.addAttribute(STATE_TYPE, oldCourtVerdict.getClass().getSimpleName());
            model.addAttribute(ENDRSMNT_NOTICE, new ArrayList<>());
            model.addAttribute(LOGGED_IN_USER, propertyService.isEmployee(loggedInUser));
            courtVerdictService.addModelAttributes(model, oldCourtVerdict.getBasicProperty(), request);

            prepareWorkflow(model, oldCourtVerdict, new WorkflowContainer());

            target = CV_FORM;

        } else {
            if (action.equalsIgnoreCase("RE_ASSESS")) {
                errorMessages = courtVerdictService.validateProperty(courtVerdict);
                if (errorMessages.isEmpty()) {
                    Long approvalPosition = 0l;
                    String approvalComent = "";

                    if (request.getParameter(APPROVAL_COMMENT) != null)
                        approvalComent = request.getParameter(APPROVAL_COMMENT);
                    if (request.getParameter(WF_ACTION) != null)
                        workFlowAction = request.getParameter(WF_ACTION);
                    if (request.getParameter(APPROVAL_POSITION) != null
                            && !request.getParameter(APPROVAL_POSITION).isEmpty())
                        approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION));

                    courtVerdictService.updatePropertyDetails(courtVerdict);
                    PropertyImpl modProperty = courtVerdictService.modifyDemand(property, oldProperty);

                    if (modProperty == null)
                        courtVerdict.getBasicProperty().addProperty(property);
                    else
                        courtVerdict.getBasicProperty().addProperty(modProperty);

                    courtVerdictService.saveCourtVerdict(courtVerdict, approvalPosition, approvalComent, null,
                            workFlowAction, loggedUserIsEmployee);

                    final String successMsg = "Court Verdict Saved Successfully in the System and forwarded to : "
                            + propertyTaxUtil.getApproverUserName(courtVerdict.getState().getOwnerPosition().getId())
                            + " with application number : " + courtVerdict.getApplicationNumber();

                    model.addAttribute("successMessage", successMsg);

                    model.addAttribute("showAckBtn", Boolean.TRUE);
                    model.addAttribute("isOnlineApplication",
                            ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName()));
                    model.addAttribute("propertyId", courtVerdict.getBasicProperty().getUpicNo());
                    if (loggedUserIsMeesevaUser)
                        return "redirect:/courtVerdict/generate-meesevareceipt/"
                                + courtVerdict.getBasicProperty().getUpicNo() + "?transactionServiceNumber="
                                + courtVerdict.getApplicationNumber();
                    else
                        target = "courtVerdict-success";
                } else if (action.equalsIgnoreCase("UPDATE_DMND_DIRECTLY")) {
                    Long approvalPosition = 0l;
                    String approvalComent = "";

                    if (request.getParameter(APPROVAL_COMMENT) != null)
                        approvalComent = request.getParameter(APPROVAL_COMMENT);
                    if (request.getParameter(WF_ACTION) != null)
                        workFlowAction = request.getParameter(WF_ACTION);
                    if (request.getParameter(APPROVAL_POSITION) != null
                            && !request.getParameter(APPROVAL_POSITION).isEmpty())
                        approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION));

                    courtVerdictService.updatePropertyDetails(courtVerdict);

                    courtVerdictService.saveCourtVerdict(courtVerdict, approvalPosition, approvalComent, null,
                            workFlowAction, loggedUserIsEmployee);
                    final String successMsg = "Court Verdict Saved Successfully in the System and forwarded to : "
                            + propertyTaxUtil.getApproverUserName(courtVerdict.getState().getOwnerPosition().getId())
                            + " with application number : " + courtVerdict.getApplicationNumber();

                    model.addAttribute("successMessage", successMsg);

                    model.addAttribute("showAckBtn", Boolean.TRUE);
                    model.addAttribute("isOnlineApplication",
                            ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName()));
                    model.addAttribute("propertyId", courtVerdict.getBasicProperty().getUpicNo());
                    if (loggedUserIsMeesevaUser)
                        return "redirect:/courtVerdict/generate-meesevareceipt/"
                                + courtVerdict.getBasicProperty().getUpicNo() + "?transactionServiceNumber="
                                + courtVerdict.getApplicationNumber();
                    else
                        target = "courtVerdict-success";
                } else {
                    model.addAttribute(ERROR_MSG, errorMessages);
                    model.addAttribute(COURT_VERDICT, courtVerdict);

                    model.addAttribute(PROPERTY, courtVerdict.getBasicProperty().getActiveProperty());
                    model.addAttribute(CITIZEN_PORTAL_USER, citizenPortalUser);
                    model.addAttribute(CURRENT_STATE, CREATED);
                    model.addAttribute(STATE_TYPE, courtVerdict.getClass().getSimpleName());
                    model.addAttribute(ENDRSMNT_NOTICE, new ArrayList<>());
                    model.addAttribute(LOGGED_IN_USER, propertyService.isEmployee(loggedInUser));
                    courtVerdictService.addModelAttributes(model, courtVerdict.getBasicProperty(), request);
                    prepareWorkflow(model, courtVerdict, new WorkflowContainer());

                    target = CV_FORM;
                }
            }
        }
        return target;
    }

    private void setDemandBeanList(Set<EgDemandDetails> demandDetails, BasicProperty basicProperty) {

        Date effectiveDate = null;
        if (null != basicProperty.getActiveProperty())
            if (basicProperty.getActiveProperty().getPropertyDetail().getPropertyTypeMaster().getCode()
                    .equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
                demandReasonMap = VACANT_PROPERTY_DMDRSN_CODE_MAP;
            else
                demandReasonMap = BUILTUP_PROPERTY_DMDRSN_CODE_MAP;

        if (!basicProperty.getActiveProperty().getPropertyDetail().getPropertyTypeMaster().getCode()
                .equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            effectiveDate = getLowestDtOfCompFloorWise(basicProperty.getActiveProperty().getPropertyDetail().getFloorDetails());
        else
            effectiveDate = basicProperty.getActiveProperty().getPropertyDetail().getDateOfCompletion();
        final PropertyTaxBillable billable = new PropertyTaxBillable();
        billable.setBasicProperty(basicProperty);
        final Map<Installment, List<String>> newDDMap = new HashMap<>();
        String reason;
        Installment existingInst;
        if (!demandDetails.isEmpty())
            for (final EgDemandDetails dd : demandDetails) {
                List<String> existingReasons = new ArrayList<>();
                reason = dd.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster();
                existingInst = dd.getEgDemandReason().getEgInstallmentMaster();
                if (newDDMap.get(existingInst) == null) {
                    existingReasons = new ArrayList<>();
                    existingReasons.add(reason);
                    newDDMap.put(existingInst, existingReasons);
                } else if (newDDMap.get(existingInst) != null) {
                    existingReasons.add(reason);
                    newDDMap.get(existingInst).addAll(existingReasons);
                } else {
                    existingReasons = new ArrayList<>();
                    existingReasons.add(reason);
                    newDDMap.get(existingInst).addAll(existingReasons);
                }

            }

        final Map<Installment, Map<String, Map<String, Object>>> newMap = new LinkedHashMap<>();
        final Map<String, Map<String, Object>> rsnList = new LinkedHashMap<>();

        if (!demandDetails.isEmpty()) {
            for (final EgDemandDetails dd : demandDetails)
                if (newMap.get(dd.getEgDemandReason().getEgInstallmentMaster()) == null) {
                    final Map<String, Map<String, Object>> rsns = new LinkedHashMap<>();
                    final Map<String, Object> dtls = new HashMap<>();
                    dtls.put(AMOUNT, dd.getAmount());
                    dtls.put(COLLECTION, dd.getAmtCollected());
                    dtls.put(IS_NEW, false);
                    rsns.put(dd.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster(), dtls);
                    newMap.put(dd.getEgDemandReason().getEgInstallmentMaster(), rsns);
                } else if (newMap.get(dd.getEgDemandReason().getEgInstallmentMaster()) != null
                        && dd.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                    final Map<String, Object> dtls = new HashMap<>();
                    dtls.put(AMOUNT, BigDecimal.ZERO);
                    dtls.put(COLLECTION, BigDecimal.ZERO);
                    dtls.put(IS_NEW, false);
                    newMap.get(dd.getEgDemandReason().getEgInstallmentMaster())
                            .put(dd.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster(), dtls);

                } else if (newMap.get(dd.getEgDemandReason().getEgInstallmentMaster()) != null
                        && dd.getAmount().compareTo(BigDecimal.ZERO) != 0) {
                    final Map<String, Map<String, Object>> rsns = new LinkedHashMap<>();
                    final Map<String, Object> dtls = new HashMap<>();
                    dtls.put(AMOUNT, dd.getAmount());
                    dtls.put(COLLECTION, dd.getAmtCollected());
                    dtls.put(IS_NEW, false);
                    rsns.put(dd.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster(), dtls);
                    newMap.get(dd.getEgDemandReason().getEgInstallmentMaster()).putAll(rsns);
                }

            for (final String rsn : demandReasonMap.keySet())
                for (final Installment inst : newDDMap.keySet())
                    if (!newDDMap.get(inst).contains(rsn))
                        if (newMap.get(inst) == null) {
                            final Map<String, Object> dtls = new HashMap<>();
                            dtls.put(AMOUNT, BigDecimal.ZERO);
                            dtls.put(COLLECTION, BigDecimal.ZERO);
                            dtls.put(IS_NEW, true);
                            rsnList.put(rsn, dtls);
                            newMap.put(inst, rsnList);
                        } else if (newMap.get(inst) != null) {
                            final Map<String, Object> dtls = new HashMap<>();
                            dtls.put(AMOUNT, BigDecimal.ZERO);
                            dtls.put(COLLECTION, BigDecimal.ZERO);
                            dtls.put(IS_NEW, true);
                            rsnList.put(rsn, dtls);
                            newMap.get(inst).put(rsn, dtls);
                        }
            for (final Installment inst1 : newMap.keySet())
                for (final String rsn : newMap.get(inst1).keySet()) {
                    final Map<String, Map<String, Object>> amtMap = newMap.get(inst1);
                    final Map<String, Object> dtls = amtMap.get(rsn);
                    final DemandDetail dmdDtl2 = createDemandDetailBean(inst1, rsn, (BigDecimal) dtls.get(AMOUNT),
                            (BigDecimal) dtls.get(COLLECTION), (Boolean) dtls.get(IS_NEW), effectiveDate);
                    demandDetailBeanList.add(dmdDtl2);
                }
        } else
            for (final Map.Entry<String, String> entry : demandReasonMap.entrySet()) {
                final DemandDetail dmdDtl = createDemandDetailBean(null, entry.getKey(), null, null, true, null);
                demandDetailBeanList.add(dmdDtl);
            }
    }

    private DemandDetail createDemandDetailBean(final Installment installment, final String reasonMaster,
            final BigDecimal amount, final BigDecimal amountCollected, final Boolean isNew, final Date effectiveDate) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into createDemandDetailBean");
            LOGGER.debug("createDemandDetailBean - installment=" + installment + ", reasonMaster=" + reasonMaster
                    + ", amount=" + amount + ", amountCollected=" + amountCollected);
        }

        final DemandDetail demandDetail = new DemandDetail();
        demandDetail.setInstallment(installment);
        demandDetail.setReasonMaster(reasonMaster);
        demandDetail.setActualAmount(amount);
        demandDetail.setActualCollection(amountCollected);
        demandDetail.setIsCollectionEditable(true);
        demandDetail.setIsNew(isNew);
        demandDetail.setReadOnly(false);
        if (effectiveDate == null)
            demandDetail.setReadOnly(false);
        else {
            final List<Installment> effectiveInstallment = propertyTaxUtil
                    .getInstallmentsListByEffectiveDate(effectiveDate);
            for (final Installment inst : effectiveInstallment)
                if (inst.equals(demandDetail.getInstallment()))
                    demandDetail.setReadOnly(true);
        }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug(
                    "createDemandDetailBean - demandDetail= " + demandDetail + "\nExiting from createDemandDetailBean");
        return demandDetail;
    }

    @RequestMapping(value = "/generate-meesevareceipt/{assessmentNo}", method = RequestMethod.GET)
    public RedirectView generateMeesevaReceipt(final HttpServletRequest request, final Model model) {
        final String keyNameArray = request.getParameter("transactionServiceNumber");

        final RedirectView redirect = new RedirectView(PropertyTaxConstants.MEESEVA_REDIRECT_URL + keyNameArray, false);
        final FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
        if (outputFlashMap != null)
            outputFlashMap.put("url", request.getRequestURL());
        return redirect;
    }

    private void courtVerdictSource(final CourtVerdict courtVerdict, final HttpServletRequest request) {
        User loggedInUser = securityUtils.getCurrentUser();
        if (StringUtils.isNotBlank(request.getParameter(APPLICATION_SOURCE))
                && SOURCE_ONLINE.equalsIgnoreCase(request.getParameter(APPLICATION_SOURCE))
                && ANONYMOUS_USER.equalsIgnoreCase(loggedInUser.getName())) {
            courtVerdict.setSource(propertyTaxCommonUtils.setSourceOfProperty(loggedInUser, Boolean.TRUE));
        } else {
            courtVerdict.setSource(propertyTaxCommonUtils.setSourceOfProperty(loggedInUser, Boolean.FALSE));
        }
    }

    private Date getLowestDtOfCompFloorWise(final List<Floor> floorList) {
        LOGGER.debug("Entered into getLowestDtOfCompFloorWise, floorList: " + floorList);
        Date completionDate = null;
        for (final Floor floor : floorList) {
            Date floorDate;
            if (floor != null) {
                floorDate = floor.getOccupancyDate();
                if (floorDate != null)
                    if (completionDate == null)
                        completionDate = floorDate;
                    else if (completionDate.after(floorDate))
                        completionDate = floorDate;
            }
        }
        LOGGER.debug("completionDate: " + completionDate + "\nExiting from getLowestDtOfCompFloorWise");
        return completionDate;
    }
}
