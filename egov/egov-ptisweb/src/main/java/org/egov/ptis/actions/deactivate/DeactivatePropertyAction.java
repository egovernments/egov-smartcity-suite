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
/**
 * @author nayeem

 */

package org.egov.ptis.actions.deactivate;

import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISHISTORY;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.ASSISTANT_ROLE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.AUDITDATA_STRING_SEP;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEACTIVE_AUDIT_ACTION;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DOCS_DEACTIVATE_PROPERTY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.END_APPROVER_DESGN;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROP_STATUS_TYPE_DEACT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.QUERY_PROPERTY_BY_UPICNO_AND_STATUS;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.VOUCH_CREATE_RSN_DEACTIVATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFOWNER;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFSTATUS;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Installment;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.StringUtils;
import org.egov.ptis.actions.workflow.WorkflowAction;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.dao.property.PropertyMutationMasterDAO;
import org.egov.ptis.domain.dao.property.PropertyStatusDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDocs;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyStatus;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.exceptions.PropertyNotFoundException;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.util.FinancialUtil;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;
import org.egov.web.annotation.ValidationErrorPage;

import com.opensymphony.xwork2.validator.annotations.Validation;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Validation()
@Results({ @Result(name = "workFlowError", type = ServletActionRedirectResult.class, value = "workflow", params = {
		"namespace", "/workflow", "method", "workFlowError" }) })
public class DeactivatePropertyAction extends WorkflowAction {

	private BasicProperty basicProp;
	private String ownerName;
	private String address;
	private String propertyType;
	private PropertyImpl property;
	private String referenceNo;
	private Date referenceDate;
	private String remarks;
	private String ackMessage;

	/* reason for de activating the property */
	private String reason;
	private String docNumber;

	public static final String NEW = "new";
	public static final String ACK = "ack";
	public static final String RESULT_VIEW = "view";
	public static final String FORWARD_ACK = "forwardAck";
	public static final String PENDING_DMD = "pnd_ack";
	private static final String RSN_COURTORDER = "COURTORDER";
	private static final String PROP_STATUS_INACTIVE = "INACTIVE";
	private static final String WORKFLOW_END = "END";
	private static final String MSG_REJECT_SUCCESS = " Deactivate Property Rejected Successfully ";

	private PropertyService propService;
	private PersistenceService<BasicProperty, Long> basicPrpertyService;
	private PersistenceService<Property, Long> propertyImplService;
	FinancialUtil financialUtil = new FinancialUtil();
	/* status values along with order date and order number */
	PropertyStatusValues propStatusVal = new PropertyStatusValues();
	PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();
	private final Logger LOGGER = Logger.getLogger(getClass());

	public DeactivatePropertyAction() {
	}

	/**
	 * @return propertyStatusValues the property status data
	 */
	@Override
	public Object getModel() {
		return propStatusVal;
	}

	/**
	 * Gets the Property on property id, from this the fields will be displayed
	 * in read only form
	 * 
	 * @return String
	 */

	@SkipValidation
	public String newForm() {

		LOGGER.debug("Entered into the newForm method, Index Number " + indexNumber);
		String target = "";
		try {
			if (getBasicProp() == null) {
				throw new PropertyNotFoundException();
			} else {
				LOGGER.debug("newForm: BasicProperty: " + getBasicProp());
				Map<String, String> wfMap = basicProp.getPropertyWfStatus();
				String wfStatus = wfMap.get(WFSTATUS);
				if (wfStatus.equalsIgnoreCase("TRUE")) {
					getSession().put(WFOWNER, wfMap.get(WFOWNER));
					target = "workFlowError";
				} else {
					setOwnerName(ptisCacheMgr.buildOwnerFullName(basicProp.getProperty().getPropertyOwnerSet()));
					setAddress(ptisCacheMgr.buildAddressByImplemetation(basicProp.getAddress()));
					setDocNumber(basicProp.getProperty().getDocNumber());
					target = NEW;
				}
			}
		} catch (PropertyNotFoundException e) {
			LOGGER.error("Property not found with given Index Number " + indexNumber, e);
		}
		LOGGER.debug("Exit from newForm method");
		return target;
	}

	@SkipValidation
	public String viewForm() {
		LOGGER.debug("Entered into viewForm");

		property = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
				Long.valueOf(getModelId()));
		LOGGER.debug("viewForm: Property: " + property);
		
		if (userDesgn.equalsIgnoreCase(END_APPROVER_DESGN)) {
			setIsApprPageReq(Boolean.FALSE);
		}
		
		basicProp = property.getBasicProperty();
		LOGGER.debug("viewForm: BasicProperty: " + basicProp);
		PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();
		propStatusVal = (PropertyStatusValues) getPersistenceService()
				.find("from PropertyStatusValues PSV where PSV.basicProperty.upicNo = ? and PSV.isActive = 'N' and PSV.propertyStatus.statusCode=?",
						basicProp.getUpicNo(), PROP_STATUS_INACTIVE);
		setPropertyType(property.getPropertyDetail().getPropertyTypeMaster().getType());
		setOwnerName(ptisCacheMgr.buildOwnerFullName(property.getPropertyOwnerSet()));
		setAddress(ptisCacheMgr.buildAddressByImplemetation(getBasicProp().getAddress()));
		setDocNumber(property.getDocNumber());
		LOGGER.debug("Exit from viewForm");

		/*if (PTCREATOR_ROLE.equals(userRole)) {
			setReason(propStatusVal.getExtraField2());
			return NEW;
		}*/
		return RESULT_VIEW;
	}

	/**
	 * Prepares drop down data for de activation Reason from Property Mutation
	 * Master
	 * 
	 * @see org.egov.web.actions.BaseFormAction#prepare()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void prepare() {

		LOGGER.debug("Entered into the prepare method");
		if (getModelId() != null && !getModelId().isEmpty()) {
			property = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
					Long.valueOf(getModelId()));
			LOGGER.debug("prepare: Property: " + property);
		}
		if (indexNumber != null && !"".equals(indexNumber)) {
			basicProp = basicPrpertyService.findByNamedQuery(NMCPTISConstants.QUERY_BASICPROPERTY_BY_UPICNO,
					indexNumber);
			LOGGER.debug("prepare: BasicProperty: " + basicProp);
		}

		PropertyMutationMasterDAO propertyMutationMasterDAO = PropertyDAOFactory.getDAOFactory()
				.getPropertyMutationMstrDAO();
		List<PropertyMutationMaster> propMutationMstr = propertyMutationMasterDAO
				.getAllPropertyMutationMastersByType(PROP_STATUS_TYPE_DEACT);
		addDropdownData("Reason", propMutationMstr);

		setupWorkflowDetails();
		setUserInfo();
		LOGGER.debug("Exit from prepare method");
	}

	@ValidationErrorPage(value = "new")
	public String save() {
		LOGGER.debug("Entered into the save method");
		LOGGER.debug("save: BasicProperty: " + basicProp);
		LOGGER.debug("save: PropertyStatusValues for deactivation: " + propStatusVal);
		try {
			Map<Installment, Map<String, BigDecimal>> amounts = propService
					.prepareRsnWiseDemandForPropToBeDeactivated(basicProp.getProperty());
			financialUtil.createVoucher(basicProp.getUpicNo(), amounts, VOUCH_CREATE_RSN_DEACTIVATE);
			basicProp.setActive(false);

			PropertyStatusDAO propertyStatusDAO = PropertyDAOFactory.getDAOFactory().getPropertyStatusDAO();
			PropertyStatus propertyStatus = propertyStatusDAO.getPropertyStatusByCode(PROP_STATUS_INACTIVE);
			LOGGER.debug("save: PropertyStatus: " + propertyStatus);
			propStatusVal.setPropertyStatus(propertyStatus);
			propStatusVal.setIsActive("Y");
			propStatusVal.setExtraField2(getReason());
			basicProp.addPropertyStatusValues(propStatusVal);

			// docs upload

			if (getDocNumber() != null && !getDocNumber().equals("")) {
				PropertyDocs pd = createPropertyDocs(basicProp, getDocNumber());
				basicProp.addDocs(pd);
			}

			basicProp = basicPrpertyService.update(basicProp);
			LOGGER.debug("Exit from save method");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new EGOVRuntimeException("Exception : " + e);
		}
		return ACK;
	}

	@SkipValidation
	public String forward() {
		LOGGER.debug("Entered into forward method");
		LOGGER.debug("forward: BasicProperty: " + basicProp);
		LOGGER.debug("forward: PropertyStatusValues for deactivation: " + propStatusVal);
		String propDocNum = "";
		try {
			UserDAO userDao = new UserDAO();			
			if (userRole.equalsIgnoreCase(ASSISTANT_ROLE)) {

				this.validate();
				if (hasErrors()) {
					return NEW;
				}

				if (StringUtils.isNotBlank(getModelId())) {
					property.setDocNumber(propDocNum);
					PropertyStatusValues propStatVal = (PropertyStatusValues) getPersistenceService()
							.find("from PropertyStatusValues PSV where PSV.basicProperty.upicNo = ? and PSV.isActive = 'N' and PSV.propertyStatus.statusCode=?",
									basicProp.getUpicNo(), PROP_STATUS_INACTIVE);
					propStatVal.setExtraField2(getReason());
					propStatVal.setReferenceNo(propStatusVal.getReferenceNo());
					propStatVal.setReferenceDate(propStatusVal.getReferenceDate());
					propStatVal.setRemarks(propStatusVal.getRemarks());
					getPersistenceService().setType(PropertyStatusValues.class);
					getPersistenceService().update(propStatVal);
				} else {
					property = (PropertyImpl) basicProp.getProperty().createPropertyclone();
					property.setStatus(STATUS_WORKFLOW);
					property.setExtra_field1("");
					property.setExtra_field2("");
					property.setExtra_field3("");
					property.setExtra_field4("");
					if (getDocNumber() != null && getDocNumber() != "") {
						propDocNum = getDocNumber();
					} else {
						propDocNum = property.getDocNumber();
					}
					property.setDocNumber(propDocNum);
					basicProp.addProperty(property);

					PropertyStatusDAO propertyStatusDAO = PropertyDAOFactory.getDAOFactory().getPropertyStatusDAO();
					PropertyStatus propertyStatus = propertyStatusDAO.getPropertyStatusByCode(PROP_STATUS_INACTIVE);

					propStatusVal.setPropertyStatus(propertyStatus);
					propStatusVal.setIsActive("N");
					propStatusVal.setExtraField2(getReason());
					LOGGER.debug("forward: PropertyStatusValues after setting status, isActive & reason fields: "
							+ propStatusVal);
					basicProp.addPropertyStatusValues(propStatusVal);
					basicProp = basicPrpertyService.update(basicProp);
				}
			} else {
				propStatusVal = (PropertyStatusValues) getPersistenceService()
						.find("from PropertyStatusValues PSV where PSV.basicProperty.upicNo = ? and PSV.isActive = 'N' and PSV.propertyStatus.statusCode=?",
								basicProp.getUpicNo(), PROP_STATUS_INACTIVE);
				LOGGER.debug("forward: PropertyStatusValues for deactivation: " + propStatusVal);
				super.validate();
				if (hasErrors()) {
					return RESULT_VIEW;
				}
				LOGGER.debug("Exit from forward method");
			}
			transitionWorkFlow();
			User approverUser = userDao.getUserByID(getWorkflowBean().getApproverUserId());
			setAckMessage("Property Successfully Forwarded to " + approverUser.getUserName());

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new EGOVRuntimeException("Exception : " + e);
		}
		LOGGER.debug("Ack message: " + ackMessage);
		return FORWARD_ACK;
	}

	@SkipValidation
	public String approve() {
		LOGGER.debug("Entered into approve");
		LOGGER.debug("approve: BasicProperty: " + basicProp);
		try {
			PropertyImpl nonHistProperty = (PropertyImpl) getPersistenceService().findByNamedQuery(
					QUERY_PROPERTY_BY_UPICNO_AND_STATUS, property.getBasicProperty().getUpicNo(), STATUS_ISACTIVE);
			nonHistProperty.setStatus(STATUS_ISHISTORY);
			property.setStatus(STATUS_ISACTIVE);
			transitionWorkFlow();
			PropertyStatusDAO propertyStatusDAO = PropertyDAOFactory.getDAOFactory().getPropertyStatusDAO();
			PropertyStatus propertyStatus = propertyStatusDAO.getPropertyStatusByCode(PROP_STATUS_INACTIVE);
			propStatusVal = (PropertyStatusValues) getPersistenceService().find(
					"from PropertyStatusValues PSV where PSV.basicProperty = ? and PSV.propertyStatus = ?", basicProp,
					propertyStatus);
			if (propStatusVal != null) {
				propStatusVal.setIsActive("Y");
			}
			LOGGER.debug("approve: PropertyStatusValues for deactivation made active: " + propStatusVal);
			Map<Installment, Map<String, BigDecimal>> amounts = propService
					.prepareRsnWiseDemandForPropToBeDeactivated(basicProp.getProperty());
			financialUtil.createVoucher(basicProp.getUpicNo(), amounts, VOUCH_CREATE_RSN_DEACTIVATE);
			property.getBasicProperty().setActive(false);

			// upload docs
			if (property.getDocNumber() != null && !property.getDocNumber().equals("")) {
				PropertyDocs pd = createPropertyDocs(basicProp, property.getDocNumber());
				basicProp.addDocs(pd);
			}
			basicProp = basicPrpertyService.update(basicProp);

			deactivatePropertyAuditTrail(basicProp, propStatusVal, DEACTIVE_AUDIT_ACTION, null);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new EGOVRuntimeException("Exception : " + e);
		}
		LOGGER.debug("Exit from approve");
		return ACK;
	}

	@SkipValidation
	public String reject() {
		LOGGER.debug("reject: Property rejection started");
		property = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
				Long.valueOf(getModelId()));
		LOGGER.debug("reject: Property: " + property);
		BasicProperty basicProperty = property.getBasicProperty();
		setBasicProp(basicProperty);
		LOGGER.debug("reject: BasicProperty: " + basicProperty);
		propStatusVal = (PropertyStatusValues) getPersistenceService()
				.find("from PropertyStatusValues PSV where PSV.basicProperty.upicNo = ? and PSV.isActive = 'N' and PSV.propertyStatus.statusCode=?",
						basicProp.getUpicNo(), PROP_STATUS_INACTIVE);
		LOGGER.debug("reject: PropertyStatusValues for deactivation: " + propStatusVal);
		transitionWorkFlow();
		
		if (WORKFLOW_END.equalsIgnoreCase(property.getState().getValue())) {
			basicProperty.getProperty().setStatus(STATUS_ISHISTORY);
			property.setStatus(STATUS_ISACTIVE);
			setAckMessage(MSG_REJECT_SUCCESS);
			propertyImplService.update(property);
			basicPrpertyService.update(basicProperty);
		} else {
			setAckMessage(MSG_REJECT_SUCCESS + property.getCreatedBy().getUserName());
		}
		
		LOGGER.debug("reject: BasicProperty: " + getBasicProp() + "AckMessage: " + getAckMessage());
		LOGGER.debug("reject: Property rejection ended");

		return FORWARD_ACK;
	}

	public void validate() {

		LOGGER.debug("Entered into the validate method");
		LOGGER.debug("validate: PropertyStatusValues : " + propStatusVal);
		String comments = StringUtils.trim(propStatusVal.getRemarks());
		if (comments == null || StringUtils.isEmpty(comments)) {
			addActionError(getText("mandatory.remarks"));
		}

		if (reason == null || StringUtils.equals(reason, "none")) {
			addActionError(getText("mandatory.deactRsn"));
		}

		if (StringUtils.isNumeric(reason)) {
			addActionError(getText("mandatory.properRsn"));
		}

		if (StringUtils.equals(reason, RSN_COURTORDER)) {

			if (propStatusVal.getReferenceNo() == null || StringUtils.isEmpty(propStatusVal.getReferenceNo())) {
				addActionError(getText("mandatory.refNo"));
			} else {
				Pattern p = Pattern.compile("[^a-zA-Z0-9,/-]");
				Matcher m = p.matcher(propStatusVal.getReferenceNo());
				if (m.find()) {
					addActionError(getText("mandatory.validOrderNo"));
				}
			}
			if (propStatusVal.getReferenceDate() == null) {
				addActionError(getText("mandatory.refDate"));
			}
		}
		if (propStatusVal.getReferenceDate() != null) {
			if (propStatusVal.getReferenceDate().after(new Date())) {
				addActionError(getText("mandatory.ordDateBeforeCurr"));
			}
		}
		super.validate();
		LOGGER.debug("Exit from validate method");
	}

	private PropertyDocs createPropertyDocs(BasicProperty basicProperty, String docNumber) {
		PropertyDocs pd = new PropertyDocs();
		pd.setDocNumber(docNumber);
		pd.setBasicProperty(basicProperty);
		pd.setReason(DOCS_DEACTIVATE_PROPERTY);
		return pd;
	}


	private void transitionWorkFlow() {
		
		LOGGER.debug("Entered method : transitionWorkFlow"); 
		
		if (workflowBean == null) {
			LOGGER.debug("transitionWorkFlow: workflowBean is NULL");
		} else {
			LOGGER.debug("transitionWorkFlow - action : " + workflowBean.getActionName() + "property: " + property);
		}
		
		workflowAction = propertyTaxUtil.initWorkflowAction(property, workflowBean,
				Integer.valueOf(EGOVThreadLocals.getUserId()), eisCommonsManager);
		
		if (workflowAction.isNoWorkflow()) {
			startWorkFlow();
		}
		
		if (workflowAction.isStepRejectAndOwnerNextPositionSame() || workflowAction.isApproveOrSave()) {
			endWorkFlow();
		} else {
			workflowAction.changeState();
		}
		
		
		LOGGER.debug("transitionWorkFlow: Property transitioned to " + property.getState().getValue());
		propertyImplService.persist(property);

		LOGGER.debug("Exiting method : transitionWorkFlow");	
	}

	private void deactivatePropertyAuditTrail(BasicProperty basicProperty, PropertyStatusValues propStatVal,
			String action, String auditDetails2) {
		PropertyMutationMasterDAO propertyMutationMasterDAO = PropertyDAOFactory.getDAOFactory()
				.getPropertyMutationMstrDAO();
		String deactivateRsn = "";
		StringBuilder auditDetail1 = new StringBuilder();
		if (propStatVal.getExtraField2() != null) {
			deactivateRsn = propertyMutationMasterDAO.getPropertyMutationMasterByCode(propStatVal.getExtraField2())
					.getMutationName();
		}
		auditDetail1.append("Reason For Deactivation : ")
				.append(deactivateRsn)
				.append(AUDITDATA_STRING_SEP).append("Comments : ")
				.append(propStatVal.getRemarks() != null ? propStatVal.getRemarks() : "");
		LOGGER.debug("Audit String : "+auditDetail1.toString());
		propertyTaxUtil.generateAuditEvent(action, basicProperty, auditDetail1.toString(), auditDetails2);
	}

	public String getPropertyId() {
		return indexNumber;
	}

	public void setPropertyId(String propertyId) {
		this.indexNumber = propertyId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BasicProperty getBasicProp() {
		return basicProp;
	}

	public void setBasicProp(BasicProperty basicProp) {
		this.basicProp = basicProp;
	}

	public PropertyStatusValues getPropStatusVal() {
		return propStatusVal;
	}

	public void setPropStatusVal(PropertyStatusValues propertyStatusValues) {
		this.propStatusVal = propertyStatusValues;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public void setBasicPrpertyService(PersistenceService<BasicProperty, Long> basicPrpertyService) {
		this.basicPrpertyService = basicPrpertyService;
	}

	public void setPropertyImplService(PersistenceService<Property, Long> propertyImplService) {
		this.propertyImplService = propertyImplService;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public Date getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(Date referenceDate) {
		this.referenceDate = referenceDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAckMessage() {
		return ackMessage;
	}

	public void setAckMessage(String ackMessage) {
		this.ackMessage = ackMessage;
	}

	public PropertyService getPropService() {
		return propService;
	}

	public void setPropService(PropertyService propService) {
		this.propService = propService;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

}
