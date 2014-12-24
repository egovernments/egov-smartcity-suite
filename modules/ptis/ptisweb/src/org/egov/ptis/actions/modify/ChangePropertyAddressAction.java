package org.egov.ptis.actions.modify;

import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISHISTORY;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.AUDITDATA_STRING_SEP;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.CHANGEADDRESS_AUDIT_ACTION;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DOCS_ADDRESS_CHANGE_PROPERTY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PTCREATOR_ROLE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.QUERY_BASICPROPERTY_BY_UPICNO;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.QUERY_PROPERTY_BY_UPICNO_AND_STATUS;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_CHANGEADDRESS;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_CREATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_STEP_SAVE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFOWNER;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFSTATUS;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WF_STATE_APPROVAL_PENDING;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.StringUtils;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.pims.commons.Position;
import org.egov.ptis.actions.workflow.WorkflowAction;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyDocs;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.web.annotation.ValidationErrorPage;

import com.opensymphony.xwork2.validator.annotations.Validation;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Validation()
@Results({ @Result(name = "workFlowError", type="redirect", location = "workflow", params = {
		"namespace", "/workflow", "method", "workFlowError" }) })
public class ChangePropertyAddressAction extends WorkflowAction {

	private BasicProperty basicProperty;
	private PropertyAddress address = new PropertyAddress();

	private Integer area;
	private String ackMessage;
	private PropertyImpl property;
	private PropertyAddress addr = new PropertyAddress();
	private WorkflowService<PropertyImpl> propertyWorkflowService;
	private PersistenceService<BasicProperty, Long> basicPrpertyService;
	private PersistenceService<Property, Long> propertyImplService;
	public static final String NEW = "new";
	public static final String VIEW = "view";
	public static final String ACK = "ack";
	public static final String FORWARD_ACK = "forwardAck";

	private String docNumber;

	/* to log errors and debugging information */
	private final Logger LOGGER = Logger.getLogger(getClass());

	public ChangePropertyAddressAction() {
	}

	@Override
	public Object getModel() {
		return address;
	}

	/**
	 * 
	 * @return String value indicating the view page to be called
	 */

	@SkipValidation
	public String newForm() {
		LOGGER.debug("Entered into the newForm method, Index Number " + indexNumber + ", BasicProperty: "
				+ basicProperty);
		String target = "";
		Map<String, String> wfMap = basicProperty.getPropertyWfStatus();
		String wfStatus = wfMap.get(WFSTATUS);
		if (wfStatus.equalsIgnoreCase("TRUE")) {
			getSession().put(WFOWNER, wfMap.get(WFOWNER));
			target = "workFlowError";
		} else {
			if (getDocNumber() != null && getDocNumber() != "") {
				setDocNumber(getDocNumber());
			} else {
				setDocNumber(basicProperty.getProperty().getDocNumber());
			}
			target = NEW;
		}
		LOGGER.debug("Exit from newForm method");
		return target;
	}

	@SkipValidation
	public String view() {

		LOGGER.debug("Entered into view method, ModelId: " + getModelId() + ", Address: " + address);

		property = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
				Long.valueOf(getModelId()));
		LOGGER.debug("view: Property by ModelId: " + property);
		basicProperty = property.getBasicProperty();
		LOGGER.debug("view: BasicProperty on property: " + basicProperty);
		String[] addFields = property.getBasicProperty().getExtraField2().split("\\|");

		address.setStreetAddress1(addFields[0]);
		address.setHouseNo(addFields[1]);

		if (addFields[2].equals(" ") || addFields[2].isEmpty()) {
			address.setDoorNumOld("N/A");
		} else {
			address.setDoorNumOld(addFields[2]);
		}
		if (!addFields[3].equals(" ") && !addFields[3].isEmpty()) {
			address.setPinCode(Integer.parseInt(addFields[3]));
		}
		if (addFields[4].equals(" ") || addFields[4].isEmpty()) {
			address.setMobileNo("N/A");
		} else {
			address.setMobileNo(addFields[4]);
		}

		address.setExtraField1(StringUtils.isNotEmpty(addFields[5]) ? addFields[5] : "");
		address.setExtraField2(StringUtils.isNotEmpty(addFields[6]) ? addFields[6] : "");
		address.setExtraField3(StringUtils.isNotEmpty(addFields[7]) ? addFields[7] : "");
		address.setExtraField4(StringUtils.isNotEmpty(addFields[8]) ? addFields[8] : "");

		setDocNumber(property.getDocNumber());
		LOGGER.debug("Address: " + address + "\nExit from view method");

		if (PTCREATOR_ROLE.equals(userRole)) {
			if (address.getDoorNumOld().equals("N/A")) {
				address.setDoorNumOld("");
			}
			if (address.getMobileNo().equals("N/A")) {
				address.setMobileNo("");
			}
			if (getDocNumber() != null && getDocNumber() != "") {
				setDocNumber(getDocNumber());
			} else {
				setDocNumber(basicProperty.getProperty().getDocNumber());
			}
			return NEW;
		}
		return VIEW;
	}

	/**
	 * Updates the input address
	 * 
	 * @return String value indicating the view page to be called
	 */

	@ValidationErrorPage(value = "new")
	public String save() {

		LOGGER.debug("Entered into the newForm method, Index Number : " + indexNumber + ", Address : " + address
				+ "BasicProperty: " + basicProperty);
		String addrStr1 = address.getStreetAddress1();
		addrStr1 = propertyTaxUtil.antisamyHackReplace(addrStr1);
		address.setStreetAddress1(addrStr1);
		basicProperty.setAddress(address);

		if (property.getStatus().equals(STATUS_WORKFLOW)) {
			PropertyImpl nonHistProperty = (PropertyImpl) getPersistenceService().findByNamedQuery(
					QUERY_PROPERTY_BY_UPICNO_AND_STATUS, property.getBasicProperty().getUpicNo(), STATUS_ISACTIVE);
			nonHistProperty.setStatus(STATUS_ISHISTORY);
			property.setStatus(STATUS_ISACTIVE);
		}

		transitionWorkFlow();

		// docs upload
		if (getDocNumber() != null && !getDocNumber().equals("")) {
			PropertyDocs pd = createPropertyDocs(basicProperty, getDocNumber());
			basicProperty.addDocs(pd);
		}

		propertyImplService.update(property);
		basicProperty = basicPrpertyService.update(basicProperty);
		getWorkflowBean().setActionName(WFLOW_ACTION_NAME_CREATE);

		LOGGER.debug("Exit from save method");

		return ACK;
	}

	@SkipValidation
	public String forward() {

		LOGGER.debug("Entered into forward, BasicProperty: " + basicProperty + ", Address: " + address);

		// try {
		Integer userId = propertyTaxUtil.getLoggedInUser(getSession()).getId();
		UserDAO userDao = new UserDAO();
		User user = userDao.getUserByID(userId);
		String roleName = "";
		String propDocNum = "";
		for (Role role : user.getRoles()) {
			if (role.getRoleName().equalsIgnoreCase(PTCREATOR_ROLE)) {
				roleName = role.getRoleName();
				break;
			}
		}
		if (roleName.equalsIgnoreCase(PTCREATOR_ROLE)) {

			this.validate();
			if (hasErrors()) {
				return NEW;
			}

			// clone the property only if the workflow property doesn't exist
			if (getModelId() == null || getModelId().equals("")) {
				property = (PropertyImpl) basicProperty.getProperty().createPropertyclone();

				property.getBasicProperty().setExtraField2(PropertyTaxUtil.buildAddress(address));
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
				LOGGER.debug("Property is getting added to BasicProperty: " + property);
				basicProperty.addProperty(property);
				basicProperty = basicPrpertyService.update(basicProperty);
			} else {
				property.getBasicProperty().setExtraField2(PropertyTaxUtil.buildAddress(address));
				propertyImplService.update(property);
				basicProperty = basicPrpertyService.update(basicProperty);
			}

		} else {
			super.validate();
			if (hasErrors()) {
				return "view";
			}
		}

		transitionWorkFlow();
		User approverUser = userDao.getUserByID(getWorkflowBean().getApproverUserId());
		setAckMessage("Property " + basicProperty.getUpicNo() + " Successfully Forwarded to "
				+ approverUser.getUserName());
		/*
		 * } catch (Exception e) { throw new EGOVRuntimeException("Exception : "
		 * + e); }
		 */
		LOGGER.debug("forward: AckMessage: " + getAckMessage());
		LOGGER.debug("Exit from forward");

		return FORWARD_ACK;
	}

	@SkipValidation
	public String approve() {

		LOGGER.debug("Enetered into approve, BasicProperty: " + basicProperty + ", Address : "
				+ address.getStreetAddress1() + " HouseNo" + address.getHouseNo() + "DoorNumOld "
				+ address.getDoorNumOld() + " PinCode" + address.getPinCode());

		try {
			PropertyImpl nonHistProperty = (PropertyImpl) getPersistenceService().findByNamedQuery(
					QUERY_PROPERTY_BY_UPICNO_AND_STATUS, property.getBasicProperty().getUpicNo(), STATUS_ISACTIVE);
			nonHistProperty.setStatus(STATUS_ISHISTORY);
			property.setStatus(STATUS_ISACTIVE);

			transitionWorkFlow();

			basicProperty.setAddress(address);

			// upload docs
			if (property.getDocNumber() != null && !property.getDocNumber().equals("")) {
				PropertyDocs pd = createPropertyDocs(basicProperty, property.getDocNumber());
				basicProperty.addDocs(pd);
			}

			propertyTaxUtil.makeTheEgBillAsHistory(basicProperty);
			basicProperty = basicPrpertyService.update(basicProperty);

			changePropertyAuditTrail(basicProperty, CHANGEADDRESS_AUDIT_ACTION, null);
		} catch (Exception e) {
			throw new EGOVRuntimeException("Exception : " + e);
		}

		LOGGER.debug("Exit from approve");

		return ACK;
	}

	@SkipValidation
	public String reject() {
		LOGGER.debug("reject: Change Property rejection started");
		LOGGER.debug("reject: Property: " + property);
		BasicProperty basicProperty = property.getBasicProperty();
		LOGGER.debug("reject: BasicProperty: " + basicProperty);
		transitionWorkFlow();
		setAckMessage("Change Property Rejected Successfully and forwarded to Initiator : "
				+ property.getCreatedBy().getUserName());
		LOGGER.debug("reject: BasicProperty: " + basicProperty + "AckMessage: " + getAckMessage());
		LOGGER.debug("reject: Change Property rejection ended");

		return FORWARD_ACK;
	}

	public void prepare() {

		LOGGER.debug("Entered into prepare, ModelId: " + getModelId() + ", IndexNumber: " + indexNumber);

		if (getModelId() != null && !getModelId().isEmpty()) {
			property = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
					Long.valueOf(getModelId()));
			LOGGER.debug("prepare: Property by model id: " + property);
		}

		if (indexNumber != null && !indexNumber.equals("")) {
			basicProperty = basicPrpertyService.findByNamedQuery(QUERY_BASICPROPERTY_BY_UPICNO, indexNumber);
			LOGGER.debug("prepare: BasicProperty by index number : " + basicProperty);
		}
		setupWorkflowDetails();
		setUserInfo();
		LOGGER.debug("Exit from prepare");
	}

	@Override
	public void validate() {

		LOGGER.debug("Entered into the validate method Address : " + address.getStreetAddress1() + " HouseNo"
				+ address.getHouseNo() + "DoorNumOld " + address.getDoorNumOld() + " PinCode" + address.getPinCode());

		/* Validates the input data in case the form is submitted */

		if (address.getStreetAddress1() == null || StringUtils.equals(address.getStreetAddress1(), "")
				|| StringUtils.isEmpty(address.getStreetAddress1())) {
			addActionError(getText("mandatory.addr"));
		}
		if (address.getHouseNo() == null || StringUtils.equals(address.getHouseNo(), "")) {
			addActionError(getText("mandatory.houseNo"));
		} else {
			validateHouseNumber(basicProperty.getPropertyID().getWard().getId(), address.getHouseNo(), basicProperty);
		}
		if (address.getPinCode() != null) {
			String pincode = StringUtils.trim(address.getPinCode().toString());
			if (!pincode.equals("") && pincode.length() < 6) {
				addActionError(getText("mandatory.pincode.size"));
			}
		}
		String mobNo = StringUtils.trim(address.getMobileNo());

		if (mobNo != null && !mobNo.equals("") && mobNo.length() < 10) {
			addActionError(getText("mandatory.mobileNo.size"));
		}
		super.validate();
		newForm();

		LOGGER.debug("Exit from validate method");

	}

	private PropertyDocs createPropertyDocs(BasicProperty basicProperty, String docNumber) {
		PropertyDocs pd = new PropertyDocs();
		pd.setDocNumber(docNumber);
		pd.setBasicProperty(basicProperty);
		pd.setReason(DOCS_ADDRESS_CHANGE_PROPERTY);
		return pd;
	}

	private void startWorkFlow() {
		LOGGER.debug("Entered into startWorkFlow, UserId: " + EGOVThreadLocals.getUserId());
		LOGGER.debug("startWorkFlow: Workflow is starting for Property: " + property);
		Position position = eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		propertyWorkflowService.start(property, position, "Property Workflow Started");

		LOGGER.debug("Exiting from startWorkFlow, Workflow started");
	}

	/**
	 * This method ends the workflow. The Property is transitioned to END state.
	 */
	private void endWorkFlow() {
		LOGGER.debug("Enter method endWorkFlow, UserId: " + EGOVThreadLocals.getUserId());
		LOGGER.debug("endWorkFlow: Workflow will end for Property: " + property);
		Position position = eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		propertyWorkflowService.end(property, position, "Property Workflow End");
		LOGGER.debug("Exit method endWorkFlow, Workflow ended");
	}

	private void transitionWorkFlow() {
		if (workflowBean != null) {
			LOGGER.debug("Entered method : transitionWorkFlow. Action : " + workflowBean.getActionName() + "Property: "
					+ property);
		} else {
			LOGGER.debug("transitionWorkFlow: workflowBean is NULL");
		}

		if (property.getState() == null) {
			startWorkFlow();
		}

		Position nextPosition = null;
		String wflowAction = null;
		StringBuffer nextStateValue = new StringBuffer();
		if (workflowBean.getApproverUserId() != null && !workflowBean.getApproverUserId().equals(new Integer(-1))) {
			nextPosition = eisCommonsService.getPositionByUserId(workflowBean.getApproverUserId());
		}
		String beanActionName[] = workflowBean.getActionName().split(":");
		String actionName = beanActionName[0];
		if (beanActionName.length > 1) {
			wflowAction = beanActionName[1];// save or forward or approve or
											// reject
		}
		if (WFLOW_ACTION_NAME_CHANGEADDRESS.equals(actionName)) {
			nextStateValue = nextStateValue.append(WFLOW_ACTION_NAME_CHANGEADDRESS).append(":");
		}

		if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(wflowAction) || WFLOW_ACTION_STEP_SAVE.equals(wflowAction)) {
			endWorkFlow();
		} else if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(wflowAction)) {
			nextStateValue = nextStateValue.append(nextPosition.getDesigId().getDesignationName()).append("_")
					.append(WF_STATE_APPROVAL_PENDING);
			property.changeState(nextStateValue.toString(), nextPosition, workflowBean.getComments());
		} else if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(wflowAction)) {
			nextPosition = eisCommonsService.getPositionByUserId(property.getCreatedBy().getId());
			nextStateValue = nextStateValue.append(nextPosition.getDesigId().getDesignationName()).append("_")
					.append(WF_STATE_APPROVAL_PENDING);
			property.changeState(nextStateValue.toString(), nextPosition, workflowBean.getComments());
		}

		LOGGER.debug("transitionWorkFlow: Property transitioned to " + property.getState().getValue());
		propertyImplService.persist(property);

		LOGGER.debug("Exiting method : transitionWorkFlow");
	}

	private void changePropertyAuditTrail(BasicProperty basicProperty, String action, String auditDetails2) {
		StringBuilder auditDetail1 = new StringBuilder();
		auditDetail1
				.append("Address : ")
				.append(basicProperty.getAddress().getStreetAddress1() != null ? basicProperty.getAddress()
						.getStreetAddress1() : "").append(AUDITDATA_STRING_SEP)
				.append("House Number : ")
				.append(basicProperty.getAddress().getHouseNo() != null ? basicProperty.getAddress().getHouseNo() : "");
		LOGGER.debug("Audit String : "+auditDetail1.toString());
		propertyTaxUtil.generateAuditEvent(action, basicProperty, auditDetail1.toString(), auditDetails2);
	}

	public BasicProperty getBasicProperty() {
		return basicProperty;
	}

	public void setBasicProperty(BasicProperty basicProperty) {
		this.basicProperty = basicProperty;
	}

	public PropertyAddress getAddress() {
		return address;
	}

	public void setAddress(PropertyAddress address) {
		this.address = address;
	}

	public Integer getArea() {
		return area;
	}

	public void setArea(Integer area) {
		this.area = area;
	}

	public void setPropertyWorkflowService(WorkflowService<PropertyImpl> propertyWorkflowService) {
		this.propertyWorkflowService = propertyWorkflowService;
	}

	public void setBasicPrpertyService(PersistenceService<BasicProperty, Long> basicPrpertyService) {
		this.basicPrpertyService = basicPrpertyService;
	}

	public String getAckMessage() {
		return ackMessage;
	}

	public void setAckMessage(String ackMessage) {
		this.ackMessage = ackMessage;
	}

	public WorkflowService<PropertyImpl> getPropertyWorkflowService() {
		return propertyWorkflowService;
	}

	public PersistenceService<BasicProperty, Long> getBasicPrpertyService() {
		return basicPrpertyService;
	}

	public void setPropertyImplService(PersistenceService<Property, Long> propertyImplService) {
		this.propertyImplService = propertyImplService;
	}

	public PropertyAddress getAddr() {
		return addr;
	}

	public void setAddr(PropertyAddress addr) {
		this.addr = addr;
	}

	public void setProperty(PropertyImpl property) {
		this.property = property;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

}
