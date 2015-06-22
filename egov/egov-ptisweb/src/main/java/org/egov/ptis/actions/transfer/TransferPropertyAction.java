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
package org.egov.ptis.actions.transfer;

import static org.egov.ptis.constants.PropertyTaxConstants.DOCS_MUTATION_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.PTCREATOR_ROLE;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_CANCELLED;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISHISTORY;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.TRANSFER_AUDIT_ACTION;
import static org.egov.ptis.constants.PropertyTaxConstants.WFOWNER;
import static org.egov.ptis.constants.PropertyTaxConstants.WFSTATUS;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_NOTICE_GENERATION_PENDING;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.actions.workflow.WorkflowAction;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyMutationMasterDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyDocs;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyOwner;
import org.egov.ptis.domain.service.transfer.TransferOwnerService;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.validator.annotations.Validations;

@Validations
@Results({
    @Result(name = BaseFormAction.NEW, location = "transfer/transferProperty-new.jsp"),
    @Result(name = BaseFormAction.EDIT, location = "transfer/transferProperty-edit.jsp"),
    @Result(name = "workFlowError", location = "workflow/workflow-error.jsp"),
    @Result(name = TransferPropertyAction.ACK, location = "transfer/transferProperty-ack.jsp"),
    @Result(name = "balance", location = "transfer/transferProperty-balance.jsp")
})
@Namespace("/transfer")
public class TransferPropertyAction extends WorkflowAction {
    private static final long serialVersionUID = 1L;
    
    public  static final String ACK = "ack";
    private static final String MISC_RECEIPT = "miscReceipt";
    private static final String VIEW = "view";
    private static final String WORKFLOW_END = "END";
    private static final String MSG_REJECT_SUCCESS = " Change Property Rejected Successfully ";

    private final Logger LOGGER = Logger.getLogger(getClass());
    private String oldOwnerName;
    private String propAddress;
    private PropertyAddress prpAddress;
    private String noticeType;
    private BasicProperty basicProperty;
    private PropertyMutation propertyMutation = new PropertyMutation();
    private String mobileNo;
    private String email;
    private boolean chkIsCorrIsDiff;
    private Address corrAddress;
    private String corrAddress1;
    private String corrAddress2;
    private String corrPinCode;
    private PersistenceService<BasicProperty, Long> basicPrpertyService;
    private PersistenceService<Property, Long> propertyImplService;
    private List<PropertyMutation> propMutationList;
    private TransferOwnerService transferOwnerService;
    private BigDecimal currDemand;
    private BigDecimal arrDemand;
    private String extra_field4;
    private BigDecimal currDemandDue;
    private List<PropertyOwner> propertyOwnerProxy = new ArrayList<PropertyOwner>();
    private PropertyImpl property;
    private String statvalue;
    private String nextUser;
    private PropertyImpl nonHistProperty;
    private String docNumber;
    private String transRsnId;
    private String ackMessage;
    private Integer idMutationMaster;
    private BillReceiptInfo billReceiptInfo;
    @Autowired
    private UserService UserService;
    @Autowired
    private EisCommonService eisCommonService;
    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    @Autowired
    private PropertyMutationMasterDAO propertyMutationMasterDAO;
    @Autowired
    private PtDemandDao ptDemandDAO;

    @Override
    public Object getModel() {
        return propertyMutation;
    }

    @SkipValidation
    @Action(value = "/transferProperty-transferForm")
    public String transferForm() {
        LOGGER.debug("Entered into transferForm method");
        LOGGER.debug("transferForm : Index Number : " + indexNumber);
        String target;
        final boolean dmdBalNotExist = true;
        populateNonHistProperty();
        final Map<String, String> wfMap = nonHistProperty.getBasicProperty().getPropertyWfStatus();
        final String wfStatus = wfMap.get(WFSTATUS);
        if (wfStatus.equalsIgnoreCase("TRUE")) {
            getSession().put(WFOWNER, wfMap.get(WFOWNER));
            target = "workFlowError";
        } else // uncomment the below line once collection is integrated
            // boolean dmdBalNotExist = checkForDemandBal();
            if (dmdBalNotExist) {
            populateExistingPropertyDetails();
            setDocNumber(nonHistProperty.getDocNumber());
            target = "new";
        } else
            target = "balance";
        LOGGER.debug("Exit from method transferForm");
        return target;
    }

    @ValidationErrorPage(value = "new")
    @SkipValidation
    @Action(value = "/transferProperty-approve")
    public String approve() {
        String target = "failure";
        LOGGER.debug("Entered into approve method");
        try {
            validate();
            if (hasErrors())
                return EDIT;
            propertyMutation.setExtraField1(getWorkflowBean().getComments());
            LOGGER.debug("approve : PropertyMutation : " + propertyMutation);
            transitionWorkFlow();
            final PropertyImpl propertyPrevious = (PropertyImpl) super.getPersistenceService()
                    .findByNamedQuery("getPropertyByUpicNoAndStatus", getIndexNumber(), STATUS_ISACTIVE);
            propertyPrevious.setStatus(STATUS_ISHISTORY);
            LOGGER.debug("approve : Previous property : " + propertyPrevious);
            property.setStatus(STATUS_ISACTIVE);
            // docs upload
            final BasicProperty basicProperty = property.getBasicProperty();
            if (property.getDocNumber() != null && !property.getDocNumber().equals("")) {
                final PropertyDocs pd = createPropertyDocs(basicProperty, property.getDocNumber());
                basicProperty.addDocs(pd);
                basicPrpertyService.update(basicProperty);
            }
            final Set<PropertyOwner> owners = transferOwnerService.getNewPropOwnerAdd(property, chkIsCorrIsDiff, corrAddress1,
                    corrAddress2, corrPinCode, propertyOwnerProxy);
            for (final PropertyOwner owner : property.getPropertyOwnerSet())
                owner.getAddress().clear();
            property.getPropertyOwnerSet().clear();
            property.getPropertyOwnerSet().addAll(owners);
            basicPrpertyService.update(basicProperty);
            transferPropertyAuditTrail(basicProperty, property, TRANSFER_AUDIT_ACTION, null);

            propertyTaxUtil.makeTheEgBillAsHistory(basicProperty);
            LOGGER.debug("approve : property : " + property);
            target = ACK;
        } catch (final Exception e) {
            LOGGER.error("Exception in Transfer Property: ", e);
        }
        LOGGER.debug("Exit from approve method");
        return target;
    }

    @ValidationErrorPage(value = "new")
    @Action(value="/transferProperty")
    public String save() {
        LOGGER.debug("Entered into save method");
        LOGGER.debug("save : Index Number : " + indexNumber);
        String target = "failure";
        final BasicProperty basicProp = basicPropertyDAO.getBasicPropertyByPropertyID(indexNumber);
        LOGGER.debug("save : BasicProperty : " + basicProp);
        // upload docs
        if (getDocNumber() != null && !getDocNumber().equals("")) {
            final PropertyDocs pd = createPropertyDocs(basicProp, getDocNumber());
            basicProp.addDocs(pd);
        }

        property = transferOwnerService.createPropertyClone(basicProp, propertyMutation, propertyOwnerProxy, chkIsCorrIsDiff,
                corrAddress1, corrAddress2, corrPinCode, email, mobileNo, getDocNumber());
        property.setStatus(STATUS_WORKFLOW);
        propertyMutation.setExtraField1(getWorkflowBean().getComments());
        propertyMutation.setOwnerNameOld(oldOwnerName);
        LOGGER.debug("save : PropertyMutation : " + propertyMutation);
        transitionWorkFlow();
        setExtra_field4(property.getExtra_field4());

        if (getModelId() != null && !getModelId().isEmpty()) {
            final PropertyImpl propWF = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                    Long.valueOf(getModelId()));
            propWF.setStatus(STATUS_ISHISTORY);
            endWorkFlow(propWF);
        }
        final PropertyImpl propertyPrevious = (PropertyImpl) super.getPersistenceService()
                .findByNamedQuery("getPropertyByUpicNoAndStatus", getIndexNumber(), STATUS_ISACTIVE);
        propertyPrevious.setStatus(STATUS_ISHISTORY);
        LOGGER.debug("save : Previous property : " + propertyPrevious);
        property.setStatus(STATUS_ISACTIVE);
        LOGGER.debug("save : property : " + property);
        target = ACK;

        LOGGER.debug("Exit from save method");
        return target;
    }

    @SkipValidation
    public String view() {
        LOGGER.debug("Entered into view method");
        property = (PropertyImpl) super.getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                Long.valueOf(getModelId()));
        final String currWfState = property.getState().getValue();
        if (currWfState.endsWith(WF_STATE_NOTICE_GENERATION_PENDING))
            setIsApprPageReq(Boolean.FALSE);
        setNoticeType(property.getExtra_field2());
        basicProperty = property.getBasicProperty();
        LOGGER.debug("view : BasicProperty : " + basicProperty);
        setIndexNumber(basicProperty.getUpicNo());
        populateNonHistProperty();
        LOGGER.debug("view : Non-History property : " + nonHistProperty);
        setStatvalue(property.getState().getValue());
        final Set<PropertyMutation> propMutSet = basicProperty.getPropMutationSet();
        PropertyMutation pm1 = null;
        for (final PropertyMutation pm : propMutSet)
            pm1 = pm;
        setPropertyMutation(pm1);
        setDocNumber(property.getDocNumber());
        LOGGER.debug("view : Property : " + property);
        LOGGER.debug("Exit from view method");
        if (PTCREATOR_ROLE.equals(userRole) && !currWfState.endsWith(WF_STATE_NOTICE_GENERATION_PENDING)) {
            setWFPropertyMutation(propertyMutation);
            return NEW;
        }
        populateExistingPropertyDetails();
        setPropertyOwnerProxy(new ArrayList(property.getPropertyOwnerSet()));
        if (property.getPropertyOwnerSet() != null && !property.getPropertyOwnerSet().isEmpty()) {
            final PropertyOwner owner = property.getPropertyOwnerSet().iterator().next();
            final Set<Address> addrSet = (Set<Address>) owner.getAddress();
            for (final Address addr : addrSet)
                if (addr.getLandmark() != null || addr.getAreaLocalitySector() != null || addr.getPinCode() != null) {
                    setChkIsCorrIsDiff(true);
                    if (addr.getLandmark() != null)
                        setCorrAddress1(addr.getLandmark());
                    if (addr.getAreaLocalitySector() != null)
                        setCorrAddress2(addr.getAreaLocalitySector());
                    if (addr.getPinCode() != null)
                        setCorrPinCode(addr.getPinCode().toString());
                }
            setCorrAddress(owner.getAddress().iterator().next());
        }
        if (currWfState.endsWith(WF_STATE_NOTICE_GENERATION_PENDING))
            return VIEW;
        else
            return EDIT;
    }

    @ValidationErrorPage(value = "new")
    @SkipValidation
    public String forward() {
        LOGGER.debug("Entered into forward method");
        String target = "failure";
        LOGGER.debug("forward : Index Number : " + indexNumber);
        final Long userId = propertyTaxUtil.getLoggedInUser(getSession()).getId();
        UserService.getUserById(userId);
        String propDocNum = "";
        if (getModelId() == null || getModelId().equals("")) {
            validate();
            if (hasErrors())
                return NEW;
            final BasicProperty basicProp = basicPropertyDAO.getBasicPropertyByPropertyID(indexNumber);
            LOGGER.debug("forward : BasicProperty : " + basicProp);

            if (getDocNumber() != null && getDocNumber() != "")
                propDocNum = getDocNumber();
            else
                propDocNum = basicProp.getProperty().getDocNumber();

            // if there is a workflow property then set the status as history
            if (getModelId() != null && !getModelId().equals("")) {
                final PropertyImpl propWF = (PropertyImpl) super.getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                        Long.valueOf(getModelId()));
                if (propWF.getStatus().equals(STATUS_WORKFLOW)) {
                    propWF.setStatus(STATUS_ISHISTORY);
                    endWorkFlow(propWF);
                }
            }

            property = transferOwnerService.createPropertyClone(basicProp, propertyMutation, propertyOwnerProxy, chkIsCorrIsDiff,
                    corrAddress1, corrAddress2, corrPinCode, email, mobileNo, propDocNum);
            propertyMutation.setExtraField1(getWorkflowBean().getComments());
            propertyMutation.setOwnerNameOld(oldOwnerName);
            BigDecimal feeAmount = propertyMutation.getMutationFee();
            if (propertyMutation.getOtherFee() != null)
                feeAmount = feeAmount.add(propertyMutation.getOtherFee());
            billReceiptInfo = transferOwnerService.generateMiscReceipt(basicProp, feeAmount);
            propertyMutation.setReceiptNum(billReceiptInfo.getReceiptNum());

            LOGGER.debug("forward : Property : " + property);
        } else {
            if (idMutationMaster != null && idMutationMaster != -1) {
                final PropertyMutationMaster propMutMstr = (PropertyMutationMaster) getPersistenceService()
                        .find("from PropertyMutationMaster PM where PM.idMutation = ?", idMutationMaster);
                propertyMutation.setPropMutationMstr(propMutMstr);
            }
            property = (PropertyImpl) super.getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                    Long.valueOf(getModelId()));
            LOGGER.debug("forward : Property : " + property);
            final BasicProperty basicProp = property.getBasicProperty();
            if (getDocNumber() != null && getDocNumber() != "")
                property.setDocNumber(docNumber);
            final Set<PropertyMutation> propMutSet = basicProp.getPropMutationSet();
            for (final PropertyMutation pm : propMutSet)
                if (pm.getId().equals(propertyMutation.getId())) {
                    propMutSet.remove(pm);
                    propMutSet.add(propertyMutation);
                }
            validate();
            if (hasErrors())
                return EDIT;
            final Set<PropertyOwner> owners = transferOwnerService.getNewPropOwnerAdd(property, chkIsCorrIsDiff, corrAddress1,
                    corrAddress2, corrPinCode, propertyOwnerProxy);
            for (final PropertyOwner owner : property.getPropertyOwnerSet())
                owner.getAddress().clear();
            property.getPropertyOwnerSet().clear();
            property.getPropertyOwnerSet().addAll(owners);
            basicPrpertyService.update(basicProp);
        }
        transitionWorkFlow();
        setNextUser(UserService.getUserById(getWorkflowBean().getApproverUserId().longValue()).getUsername());
        if (getModelId() == null || getModelId().equals(""))
            target = MISC_RECEIPT;
        else
            target = ACK;
        LOGGER.debug("Exit from forward method");
        return target;

    }

    @SkipValidation
    public String reject() {
        LOGGER.debug("Entered into reject method");
        String target = "failure";
        LOGGER.debug("reject : Index Number : " + indexNumber);

        property = (PropertyImpl) super.getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                Long.valueOf(getModelId()));
        LOGGER.debug("forward : Property : " + property);
        final Set<PropertyMutation> propMutSet = property.getBasicProperty().getPropMutationSet();
        PropertyMutation pm1 = null;
        for (final PropertyMutation pm : propMutSet)
            pm1 = pm;
        setPropertyMutation(pm1);
        transitionWorkFlow();

        if (WORKFLOW_END.equalsIgnoreCase(property.getState().getValue())) {
            property.setStatus(STATUS_CANCELLED);
            setAckMessage(MSG_REJECT_SUCCESS);
            propertyImplService.update(property);
        }

        setNextUser(UserService.getUserById(property.getCreatedBy().getId()).getUsername());
        target = ACK;
        LOGGER.debug("Exit from forward method");
        return target;

    }

    @Override
    public String toString() {
        final StringBuilder sbf = new StringBuilder();
        sbf.append("indexNum: ").append(indexNumber).append("mobileNo: ").append(mobileNo).append("email: ").append(email)
                .append("corrAddress1: ").append(corrAddress1).append("corrAddress2: ").append(corrAddress2)
                .append("corrPinCode: ").append(corrPinCode);
        return sbf.toString();
    }

    @Override
    public void prepare() {
        LOGGER.debug("Entered into prepare method");
        if (getModelId() != null && !getModelId().isEmpty()) {
            property = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                    Long.valueOf(getModelId()));
            setBasicProperty(property.getBasicProperty());
            LOGGER.debug("prepare : Property : " + property);
        }
        final List mutRsnMstrList = propertyMutationMasterDAO.getAllPropertyMutationMastersByType("TRANSFER");
        addDropdownData("MutationReason", mutRsnMstrList);
        setupWorkflowDetails();
        setUserInfo();
        if (propertyMutation != null && propertyMutation.getId() != null)
            propertyMutation = (PropertyMutation) getPersistenceService().find("from PropertyMutation where id = ?",
                    propertyMutation.getId());
        LOGGER.debug("prepare :  Exit from prepare method");
    }

    private void populateExistingPropertyDetails() {
        LOGGER.debug("Entered into populateExistingPropertyDetails method");
        final PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();
        setBasicProperty(nonHistProperty.getBasicProperty());
        if (propertyMutation != null && propertyMutation.getOwnerNameOld() != null
                && !propertyMutation.getOwnerNameOld().equals(""))
            setOldOwnerName(propertyMutation.getOwnerNameOld());
        else
            setOldOwnerName(ptisCacheMgr.buildOwnerFullName(nonHistProperty.getPropertyOwnerSet()));
        setPropAddress(ptisCacheMgr.buildAddressByImplemetation(getBasicProperty().getAddress()));
        LOGGER.debug("populateExistingPropertyDetails : Old Owner name : " + getOldOwnerName() + ", " + "Property address : "
                + getPropAddress());
        LOGGER.debug("Exit from populateExistingPropertyDetails method");
    }

    private void populateNonHistProperty() {
        LOGGER.debug("Entered into populateNonHistProperty method");
        nonHistProperty = (PropertyImpl) getPersistenceService().findByNamedQuery("getPropertyByUpicNoAndStatus", indexNumber,
                STATUS_ISACTIVE);
        LOGGER.debug("Non-History property in populateNonHistProperty method : " + nonHistProperty);
        LOGGER.debug("Exit from method populateNonHistProperty");
    }

    @Override
    public void validate() {
        LOGGER.debug("Entered into validate method");
        if (propertyMutation.getNoticeDate() == null || propertyMutation.getNoticeDate().equals("")
                || propertyMutation.getNoticeDate().equals("DD/MM/YYYY"))
            addActionError(getText("mandatory.applicant.date"));
        else if (propertyMutation.getNoticeDate().after(new Date()))
            addActionError(getText("mandatory.applicant.date.beforeCurr"));
        if (propertyMutation.getApplicantName() == null || propertyMutation.getApplicantName().equals(""))
            addActionError(getText("mandatory.applicant.name"));
        if (propertyMutation.getPropMutationMstr() == null || propertyMutation.getPropMutationMstr().getIdMutation() == -1)
            addActionError(getText("mandatory.trRsnId"));
        else {
            final PropertyMutationMaster propMutMstr = (PropertyMutationMaster) getPersistenceService().find(
                    "from PropertyMutationMaster PM where PM.idMutation = ?",
                    propertyMutation.getPropMutationMstr().getIdMutation());

            if (propMutMstr != null || StringUtils.isNotEmpty(propMutMstr.getMutationName()))
                if (propMutMstr.getMutationName().equals("SALE DEED")) {
                    if (StringUtils.isEmpty(propertyMutation.getExtraField3())
                            || StringUtils.isBlank(propertyMutation.getExtraField3()))
                        addActionError(getText("mandatory.saleDtl"));
                } else if (propMutMstr.getMutationName().equals("COURT ORDER")) {
                    if (StringUtils.isEmpty(propertyMutation.getMutationNo())
                            || StringUtils.isBlank(propertyMutation.getMutationNo()))
                        addActionError(getText("mandatory.crtOrdNo"));
                } else if (propMutMstr.getMutationName().equals("OTHERS"))
                    if (StringUtils.isEmpty(propertyMutation.getExtraField4())
                            || StringUtils.isBlank(propertyMutation.getExtraField4()))
                        addActionError(getText("mandatory.mutationReason"));
        }
        if (StringUtils.isEmpty(propertyMutation.getExtraField2()) || StringUtils.isBlank(propertyMutation.getExtraField2()))
            addActionError(getText("mandatory.subRgName"));
        if (propertyMutation.getMutationFee() == null)
            addActionError(getText("mandatory.mutationFee"));
        else if (!(propertyMutation.getMutationFee().compareTo(BigDecimal.ZERO) == 1))
            addActionError(getText("madatory.mutFeePos"));

        if (getModelId() != null && !getModelId().isEmpty()) {
            if (propertyMutation.getMutationDate() == null || propertyMutation.getMutationDate().equals("")
                    || propertyMutation.getMutationDate().equals("DD/MM/YYYY"))
                addActionError(getText("mandatory.mutationDate"));
            else if (propertyMutation.getMutationDate().after(new Date()))
                addActionError(getText("mandatory.mutationDateBeforeCurr"));
            for (final PropertyOwner owner : getPropertyOwnerProxy())
                if (owner.getName().equals(""))
                    addActionError(getText("mandatory.ownerName"));
        }

        super.validate();
        LOGGER.debug("Exit from validate method");
    }

    private PropertyDocs createPropertyDocs(final BasicProperty basicProperty, final String docNumber) {
        final PropertyDocs pd = new PropertyDocs();
        pd.setDocNumber(docNumber);
        pd.setBasicProperty(basicProperty);
        pd.setReason(DOCS_MUTATION_PROPERTY);

        return pd;
    }

    private void transitionWorkFlow() {

        LOGGER.debug("Entered method : transitionWorkFlow");

        if (workflowBean == null)
            LOGGER.debug("transitionWorkFlow: workflowBean is NULL");
        else
            LOGGER.debug("transitionWorkFlow - action : " + workflowBean.getActionName() + "property: " + property);

        workflowAction = propertyTaxUtil.initWorkflowAction(property, workflowBean, EgovThreadLocals.getUserId(),
                eisCommonService);

        if (workflowAction.isNoWorkflow())
            startWorkFlow();

        if (workflowAction.isStepRejectAndOwnerNextPositionSame())
            endWorkFlow();
        else
            workflowAction.changeState();

        if (workflowAction.isNoticeGenerated())
            endWorkFlow();

        LOGGER.debug("transitionWorkFlow: Property transitioned to " + property.getState().getValue());
        propertyImplService.persist(property);

        LOGGER.debug("Exiting method : transitionWorkFlow");
    }

    private void setWFPropertyMutation(final PropertyMutation propMutation) {
        final PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();

        transRsnId = propMutation.getPropMutationMstr().getIdMutation().toString();
        setMobileNo(getBasicProperty().getAddress().getUser().getMobileNumber());
        setEmail(getBasicProperty().getAddress().getUser().getEmailId());
        setOldOwnerName(ptisCacheMgr.buildOwnerFullName(property.getPropertyOwnerSet()));
        setPropAddress(ptisCacheMgr.buildAddressByImplemetation(getBasicProperty().getAddress()));
        final PropertyOwner owner = property.getPropertyOwnerSet().iterator().next();
        final Set<Address> addrSet = (Set<Address>) owner.getAddress();
        for (final Address addr : addrSet)
            if (addr.getLandmark() != null || addr.getAreaLocalitySector() != null || addr.getPinCode() != null) {
                setChkIsCorrIsDiff(true);
                if (addr.getLandmark() != null)
                    setCorrAddress1(addr.getLandmark());
                if (addr.getAreaLocalitySector() != null)
                    setCorrAddress1(addr.getAreaLocalitySector());
                if (addr.getPinCode() != null)
                    setCorrPinCode(addr.getPinCode().toString());
            }
        setPropertyOwnerProxy(new ArrayList(property.getPropertyOwnerSet()));
    }

    private void transferPropertyAuditTrail(final BasicProperty basicProperty, final Property property, final String action,
            final String auditDetails2) {
        final PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();
        final StringBuilder auditDetail1 = new StringBuilder();
        final String ownerName = ptisCacheMgr.buildOwnerFullName(property.getPropertyOwnerSet());
        auditDetail1.append("Owner Name : ").append(ownerName);
        LOGGER.debug("Audit String : " + auditDetail1.toString());
        // propertyTaxUtil.generateAuditEvent(action, basicProperty,
        // auditDetail1.toString(), auditDetails2);
    }

    public String getOldOwnerName() {
        return oldOwnerName;
    }

    public void setOldOwnerName(final String oldOwnerName) {
        this.oldOwnerName = oldOwnerName;
    }

    public String getPropAddress() {
        return propAddress;
    }

    public void setPropAddress(final String propAddress) {
        this.propAddress = propAddress;
    }

    public BasicProperty getBasicProperty() {
        return basicProperty;
    }

    public void setBasicProperty(final BasicProperty basicProperty) {
        this.basicProperty = basicProperty;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(final String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public boolean isChkIsCorrIsDiff() {
        return chkIsCorrIsDiff;
    }

    public void setChkIsCorrIsDiff(final boolean chkIsCorrIsDiff) {
        this.chkIsCorrIsDiff = chkIsCorrIsDiff;
    }

    public String getCorrAddress1() {
        return corrAddress1;
    }

    public void setCorrAddress1(final String corrAddress1) {
        this.corrAddress1 = corrAddress1;
    }

    public String getCorrAddress2() {
        return corrAddress2;
    }

    public void setCorrAddress2(final String corrAddress2) {
        this.corrAddress2 = corrAddress2;
    }

    public String getCorrPinCode() {
        return corrPinCode;
    }

    public void setCorrPinCode(final String corrPinCode) {
        this.corrPinCode = corrPinCode;
    }

    public void setPropertyImplService(final PersistenceService<Property, Long> propertyImplService) {
        this.propertyImplService = propertyImplService;
    }

    public void setBasicPrpertyService(final PersistenceService<BasicProperty, Long> basicPrpertyService) {
        this.basicPrpertyService = basicPrpertyService;
    }

    public List<PropertyMutation> getPropMutationList() {
        return propMutationList;
    }

    public void setPropMutationList(final List<PropertyMutation> propMutationList) {
        this.propMutationList = propMutationList;
    }

    public void setTransferOwnerService(final TransferOwnerService transferOwnerService) {
        this.transferOwnerService = transferOwnerService;
    }

    public BigDecimal getCurrDemand() {
        return currDemand;
    }

    public void setCurrDemand(final BigDecimal currDemand) {
        this.currDemand = currDemand;
    }

    public BigDecimal getArrDemand() {
        return arrDemand;
    }

    public void setArrDemand(final BigDecimal arrDemand) {
        this.arrDemand = arrDemand;
    }

    public BigDecimal getCurrDemandDue() {
        return currDemandDue;
    }

    public void setCurrDemandDue(final BigDecimal currDemandDue) {
        this.currDemandDue = currDemandDue;
    }

    public PropertyMutation getPropertyMutation() {
        return propertyMutation;
    }

    public void setPropertyMutation(final PropertyMutation propertyMutation) {
        this.propertyMutation = propertyMutation;
    }

    public List<PropertyOwner> getPropertyOwnerProxy() {
        return propertyOwnerProxy;
    }

    public void setPropertyOwnerProxy(final List<PropertyOwner> propertyOwnerProxy) {
        this.propertyOwnerProxy = propertyOwnerProxy;
    }

    public PropertyAddress getPrpAddress() {
        return prpAddress;
    }

    public void setPrpAddress(final PropertyAddress prpAddress) {
        this.prpAddress = prpAddress;
    }

    public void setNoticeType(final String noticeType) {
        this.noticeType = noticeType;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setExtra_field4(final String extra_field4) {
        this.extra_field4 = extra_field4;
    }

    public String getExtra_field4() {
        return extra_field4;
    }

    public void setStatvalue(final String statvalue) {
        this.statvalue = statvalue;
    }

    public String getStatvalue() {
        return statvalue;
    }

    public void setNextUser(final String nextUser) {
        this.nextUser = nextUser;
    }

    public String getNextUser() {
        return nextUser;
    }

    public PropertyImpl getNonHistProperty() {
        return nonHistProperty;
    }

    public void setNonHistProperty(final PropertyImpl nonHistProperty) {
        this.nonHistProperty = nonHistProperty;
    }

    public void setCorrAddress(final Address corrAddress) {
        this.corrAddress = corrAddress;
    }

    public Address getCorrAddress() {
        return corrAddress;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(final String docNumber) {
        this.docNumber = docNumber;
    }

    @Override
    public PropertyImpl getProperty() {
        return property;
    }

    @Override
    public void setProperty(final PropertyImpl property) {
        this.property = property;
    }

    public String getTransRsnId() {
        return transRsnId;
    }

    public void setTransRsnId(final String transRsnId) {
        this.transRsnId = transRsnId;
    }

    public String getAckMessage() {
        return ackMessage;
    }

    public void setAckMessage(final String ackMessage) {
        this.ackMessage = ackMessage;
    }

    public Integer getIdMutationMaster() {
        return idMutationMaster;
    }

    public void setIdMutationMaster(final Integer idMutationMaster) {
        this.idMutationMaster = idMutationMaster;
    }

    public List<PropertyOwner> getPropOwnerProxy() {
        return getPropertyOwnerProxy();
    }

    public BillReceiptInfo getBillReceiptInfo() {
        return billReceiptInfo;
    }

    public void setBillReceiptInfo(final BillReceiptInfo billReceiptInfo) {
        this.billReceiptInfo = billReceiptInfo;
    }

}
