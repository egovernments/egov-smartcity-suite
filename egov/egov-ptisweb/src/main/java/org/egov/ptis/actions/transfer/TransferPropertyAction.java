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
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.actions.workflow.WorkflowAction;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyMutationMasterDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
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
    private String oldOwnerName;
    private String propAddress;
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
    private String transRsnId;
    private String ackMessage;
    private BillReceiptInfo billReceiptInfo;
    @Autowired
    private UserService UserService;
    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    @Autowired
    private PropertyMutationMasterDAO propertyMutationMasterDAO;

    public TransferPropertyAction() {
        addRelatedEntity("propMutationMstr", PropertyMutationMaster.class);
    }
    
    @Override
    public Object getModel() {
        return propertyMutation;
    }

    @SkipValidation
    @Action(value = "/property-new")
    public String transferForm() {
        String target;
        final boolean dmdBalNotExist = true;
        populateNonHistProperty();
        final Map<String, String> wfMap = nonHistProperty.getBasicProperty().getPropertyWfStatus();
        final String wfStatus = wfMap.get(WFSTATUS);
        if (wfStatus.equalsIgnoreCase("TRUE")) {
            getSession().put(WFOWNER, wfMap.get(WFOWNER));
            target = "workFlowError";
        } else {
            // uncomment the below line once collection is integrated
            //dmdBalNotExist = checkForDemandBal();
            if (dmdBalNotExist) {
                populateExistingPropertyDetails();
                target = "new";
            } else {
                target = "balance";
            }
        }
        return target;
    }

    @ValidationErrorPage(value = EDIT)
    @Action(value = "/property-approve")
    public String approve() {
        propertyMutation.setExtraField1(getWorkflowBean().getComments());
        transitionWorkFlow();
        final PropertyImpl propertyPrevious = (PropertyImpl) super.getPersistenceService()
                .findByNamedQuery("getPropertyByUpicNoAndStatus", getIndexNumber(), STATUS_ISACTIVE);
        propertyPrevious.setStatus(STATUS_ISHISTORY);
        property.setStatus(STATUS_ISACTIVE);
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(property.getBasicProperty().getPropertyID());
        processAndStoreDocumentsWithReason(basicProperty, DOCS_MUTATION_PROPERTY);
        propertyTaxUtil.makeTheEgBillAsHistory(basicProperty);
        basicPrpertyService.persist(basicProperty);
        return ACK;
    }

    @ValidationErrorPage(value = NEW)
    @Action(value = "/property-save")
    public String save() {
        final BasicProperty basicProp = basicPropertyDAO.getBasicPropertyByPropertyID(indexNumber);
        processAndStoreDocumentsWithReason(basicProp, DOCS_MUTATION_PROPERTY);
        propertyMutation.setExtraField1(getWorkflowBean().getComments());
        propertyMutation.setOwnerNameOld(oldOwnerName);
        transferOwnerService.applyAuditing(propertyMutation);
        property = transferOwnerService.createPropertyClone(basicProp, propertyMutation, propertyOwnerProxy, chkIsCorrIsDiff,
                corrAddress1, corrAddress2, corrPinCode, email, mobileNo );
        transitionWorkFlow();
        setExtra_field4(property.getExtra_field4());
        if (getModelId() != null && !getModelId().isEmpty()) {
            final PropertyImpl propWF = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                    Long.valueOf(getModelId()));
            propWF.setStatus(STATUS_ISHISTORY);
            endWorkFlow(propWF);
            propertyImplService.persist(propWF);
        }
        final PropertyImpl propertyPrevious = (PropertyImpl) super.getPersistenceService()
                .findByNamedQuery("getPropertyByUpicNoAndStatus", getIndexNumber(), STATUS_ISACTIVE);
        propertyPrevious.setStatus(STATUS_ISHISTORY);
        property.setStatus(STATUS_ISACTIVE);
        propertyImplService.persist(property);
        return ACK;
    }

    @SkipValidation
    @Action(value = "/property-view")
    public String view() {
        property = (PropertyImpl) super.getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                Long.valueOf(getModelId()));
        final String currWfState = property.getState().getValue();
        if (currWfState.endsWith(WF_STATE_NOTICE_GENERATION_PENDING))
            setIsApprPageReq(Boolean.FALSE);
        setNoticeType(property.getExtra_field2());
        basicProperty = property.getBasicProperty();
        setIndexNumber(basicProperty.getUpicNo());
        populateNonHistProperty();
        setStatvalue(property.getState().getValue());
        final Set<PropertyMutation> propMutSet = basicProperty.getPropMutationSet();
        PropertyMutation pm1 = null;
        for (final PropertyMutation pm : propMutSet)
            pm1 = pm;
        setPropertyMutation(pm1);
        if (PTCREATOR_ROLE.equals(userRole) && !currWfState.endsWith(WF_STATE_NOTICE_GENERATION_PENDING)) {
            setWFPropertyMutation(propertyMutation);
            return NEW;
        }
        populateExistingPropertyDetails();
        getPropertyOwnerProxy().addAll(property.getPropertyOwnerSet());
        if (property.getPropertyOwnerSet() != null && !property.getPropertyOwnerSet().isEmpty()) {
            final PropertyOwner owner = property.getPropertyOwnerSet().iterator().next();
            for (final Address addr : owner.getAddress())
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
       
        return currWfState.endsWith(WF_STATE_NOTICE_GENERATION_PENDING) ? VIEW : EDIT;
    }

    @ValidationErrorPage(value = EDIT)
    @Action(value = "/property-forward")
    public String forward() {
        property = (PropertyImpl) super.getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                    Long.valueOf(getModelId()));
        BasicProperty basicProp = property.getBasicProperty();
            final Set<PropertyMutation> propMutSet = basicProp.getPropMutationSet();
            for (final PropertyMutation pm : propMutSet)
                if (pm.getId().equals(propertyMutation.getId())) {
                    propMutSet.remove(pm);
                    propMutSet.add(propertyMutation);
                }
        transitionWorkFlow();
        setNextUser(UserService.getUserById(getWorkflowBean().getApproverUserId().longValue()).getUsername());
        basicPrpertyService.update(basicProp);
        return ACK;

    }

    @SkipValidation
    @Action(value = "/property-reject")
    public String reject() {
        property = (PropertyImpl) super.getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                Long.valueOf(getModelId()));
        final Set<PropertyMutation> propMutSet = property.getBasicProperty().getPropMutationSet();
        for (final PropertyMutation pm : propMutSet)
            if (pm.getId().equals(propertyMutation.getId())) {
                setPropertyMutation(pm);
            }
        
        transitionWorkFlow();

        if (property.stateIsEnded()) {
            property.setStatus(STATUS_CANCELLED);
            setAckMessage(" Change Property Rejected Successfully ");
        }

        setNextUser(UserService.getUserById(property.getCreatedBy().getId()).getUsername());
        propertyImplService.update(property);
        return ACK;

    }

    @Override
    public void prepare() {
        if (getModelId() != null && !getModelId().isEmpty()) {
            property = (PropertyImpl) getPersistenceService().find("from PropertyImpl where id = ?",
                    Long.valueOf(getModelId()));
            setBasicProperty(property.getBasicProperty());
        }
        addDropdownData("MutationReason", propertyMutationMasterDAO.getAllPropertyMutationMastersByType("TRANSFER"));
        setupWorkflowDetails();
        setUserInfo();
        if (propertyMutation != null && propertyMutation.getId() != null)
            propertyMutation = (PropertyMutation) getPersistenceService().find("from PropertyMutation where id = ?",
                    propertyMutation.getId());
        super.prepare();
    }

    private void populateExistingPropertyDetails() {
        final PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();
        setBasicProperty(nonHistProperty.getBasicProperty());
        if (propertyMutation != null && propertyMutation.getOwnerNameOld() != null
                && !propertyMutation.getOwnerNameOld().equals(""))
            setOldOwnerName(propertyMutation.getOwnerNameOld());
        else
            setOldOwnerName(ptisCacheMgr.buildOwnerFullName(nonHistProperty.getPropertyOwnerSet()));
        setPropAddress(ptisCacheMgr.buildAddressByImplemetation(getBasicProperty().getAddress()));
    }

    private void populateNonHistProperty() {
        nonHistProperty = (PropertyImpl) getPersistenceService().findByNamedQuery("getPropertyByUpicNoAndStatus", indexNumber,
                STATUS_ISACTIVE);
    }

    @Override
    public void validate() {
        if (propertyMutation.getNoticeDate() == null)
            addActionError(getText("mandatory.applicant.date"));
        else if (propertyMutation.getNoticeDate().after(new Date()))
            addActionError(getText("mandatory.applicant.date.beforeCurr"));
        if (StringUtils.isBlank(propertyMutation.getApplicantName()))
            addActionError(getText("mandatory.applicant.name"));
        if (propertyMutation.getPropMutationMstr() == null || propertyMutation.getPropMutationMstr().getId() == -1)
            addActionError(getText("mandatory.trRsnId"));
        else {
            final PropertyMutationMaster propMutMstr = propertyMutation.getPropMutationMstr();

            if (propMutMstr != null)
                if (propMutMstr.getMutationName().equals(PropertyTaxConstants.MUTATIONRS_SALES_DEED)) {
                    if (StringUtils.isBlank(propertyMutation.getExtraField3()))
                        addActionError(getText("mandatory.saleDtl"));
                } else if (propMutMstr.getMutationName().equals(PropertyTaxConstants.MUTATIONRS_COURT_ORDER)) {
                    if ( StringUtils.isBlank(propertyMutation.getMutationNo()))
                        addActionError(getText("mandatory.crtOrdNo"));
                } else if (propMutMstr.getMutationName().equals(PropertyTaxConstants.MUTATIONRS_OTHERS))
                    if (StringUtils.isBlank(propertyMutation.getExtraField4()))
                        addActionError(getText("mandatory.mutationReason"));
        }
        if (StringUtils.isBlank(propertyMutation.getExtraField2()))
            addActionError(getText("mandatory.subRgName"));
        if (propertyMutation.getMutationFee() == null)
            addActionError(getText("mandatory.mutationFee"));
        else if (!(propertyMutation.getMutationFee().compareTo(BigDecimal.ZERO) == 1))
            addActionError(getText("madatory.mutFeePos"));

        if (StringUtils.isNotBlank(getModelId())) {
            if (propertyMutation.getMutationDate() == null)
                addActionError(getText("mandatory.mutationDate"));
            else if (propertyMutation.getMutationDate().after(new Date()))
                addActionError(getText("mandatory.mutationDateBeforeCurr"));
            for (final PropertyOwner owner : getPropertyOwnerProxy())
                if (owner.getName().equals(""))
                    addActionError(getText("mandatory.ownerName"));
        }

        super.validate();
    }

    private void transitionWorkFlow() {

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

    }

    private void setWFPropertyMutation(final PropertyMutation propMutation) {
        final PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();

        transRsnId = propMutation.getPropMutationMstr().getId().toString();
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
