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
package org.egov.ptis.actions.modify;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.actions.common.PropertyTaxBaseAction;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.egov.ptis.constants.PropertyTaxConstants.ASSISTANT_ROLE;
import static org.egov.ptis.constants.PropertyTaxConstants.DOCS_ADDRESS_CHANGE_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.END_APPROVER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_BASICPROPERTY_BY_UPICNO;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPERTY_BY_UPICNO_AND_STATUS;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISHISTORY;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_CREATE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFOWNER;
import static org.egov.ptis.constants.PropertyTaxConstants.WFSTATUS;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Results({ @Result(name = "workFlowError", location = "workflow", params = { "namespace",
		"/workflow", "method", "workFlowError" }) })
@Namespace("/modify")
public class ChangePropertyAddressAction extends PropertyTaxBaseAction {

	private BasicProperty basicProperty;
	private PropertyAddress address = new PropertyAddress();

	private Integer area;
	private String ackMessage;
	private PropertyImpl property;
	private PropertyAddress addr = new PropertyAddress();
	private PersistenceService<BasicProperty, Long> basicPropertyService;
	private PersistenceService<Property, Long> propertyImplService;
	public static final String NEW = "new";
	public static final String VIEW = "view";
	public static final String ACK = "ack";
	public static final String FORWARD_ACK = "forwardAck";
	private static final String WORKFLOW_END = "END";
	private static final String MSG_REJECT_SUCCESS = " Change Property Rejected Successfully ";

	private String docNumber;

	@Autowired
	private UserService userService;

	@Autowired
	private EisCommonService eisCommonService;

	/* to log errors and debugging information */
	private final Logger LOGGER = Logger.getLogger(getClass());

	public ChangePropertyAddressAction() {
	}

	//FIX ME
	/*@Override
	public Object getModel() {
		return address;
	}*/

	/**
	 * 
	 * @return String value indicating the view page to be called
	 */

	@SkipValidation
	@Action(value = "/changePropertyAddress-newForm", results = { @Result(name = NEW, location = "/changePropertyAddress-new.jsp") })
	public String newForm() {
		LOGGER.debug("Entered into the newForm method, Index Number " + indexNumber
				+ ", BasicProperty: " + basicProperty);
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
	@Action(value = "/changePropertyAddress-view", results = { @Result(name = VIEW, location = "/changePropertyAddress-view.jsp") })
	public String view() {

		LOGGER.debug("Entered into view method, ModelId: " + getModelId() + ", Address: " + address);
		LOGGER.debug("view: Property by ModelId: " + property);
		basicProperty = property.getBasicProperty();
		LOGGER.debug("view: BasicProperty on property: " + basicProperty);
		String[] addFields = property.getBasicProperty().getExtraField2().split("\\|");

		address.setLandmark(addFields[0]);
		address.setHouseNoBldgApt(addFields[1]);

		if (userDesgn.equalsIgnoreCase(END_APPROVER_DESGN)) {
			setIsApprPageReq(Boolean.FALSE);
		}

		setDocNumber(property.getDocNumber());
		LOGGER.debug("Address: " + address + "\nExit from view method");

		/*
		 * if (PTCREATOR_ROLE.equals(userRole)) { if
		 * (address.getDoorNumOld().equals("N/A")) { address.setDoorNumOld("");
		 * } if (address.getMobileNo().equals("N/A")) { address.setMobileNo("");
		 * } if (getDocNumber() != null && getDocNumber() != "") {
		 * setDocNumber(getDocNumber()); } else {
		 * setDocNumber(basicProperty.getProperty().getDocNumber()); } return
		 * NEW; }
		 */
		return VIEW;
	}

	/**
	 * Updates the input address
	 * 
	 * @return String value indicating the view page to be called
	 */

	@ValidationErrorPage(value = "new")
	@Action(value = "/changePropertyAddress-save", results = { @Result(name = ACK, location = "/changePropertyAddress-ack.jsp") })
	public String save() {

		LOGGER.debug("Entered into the newForm method, Index Number : " + indexNumber
				+ ", Address : " + address + "BasicProperty: " + basicProperty);
		String addrStr1 = address.getLandmark();
		addrStr1 = propertyTaxUtil.antisamyHackReplace(addrStr1);
		address.setLandmark(addrStr1);
		basicProperty.setAddress(address);

		// docs upload
		processAndStoreDocumentsWithReason(basicProperty, DOCS_ADDRESS_CHANGE_PROPERTY);

		// propertyImplService.update(property);
		basicProperty = basicPropertyService.update(basicProperty);
		getWorkflowBean().setActionName(WFLOW_ACTION_NAME_CREATE);

		LOGGER.debug("Exit from save method");

		return ACK;
	}

	@SkipValidation
	@Action(value = "/changePropertyAddress-forward", results = { @Result(name = FORWARD_ACK, location = "/changePropertyAddress-forwardAck.jsp") })
	public String forward() {

		LOGGER.debug("Entered into forward, BasicProperty: " + basicProperty + ", Address: "
				+ address);

		// try {
		String propDocNum = "";

		/**
		 * commented as roleName is already been getting by calling to
		 * setUserInfo
		 */
		/*
		 * for (Role role : user.getRoles()) { if
		 * (role.getRoleName().equalsIgnoreCase(ASSISTANT_ROLE)) { roleName =
		 * role.getRoleName(); break; } }
		 */
		if (userRole.equalsIgnoreCase(ASSISTANT_ROLE) && isBlank(getModelId())) {

			this.validate();
			if (hasErrors()) {
				return NEW;
			}

			// clone the property only if the workflow property doesn't exist
			if (getModelId() == null || getModelId().equals("")) {
				property = (PropertyImpl) basicProperty.getProperty().createPropertyclone();

				property.getBasicProperty().setExtraField2(PropertyTaxUtil.buildAddress(address));
				property.setStatus(STATUS_WORKFLOW);
			
				if (getDocNumber() != null && getDocNumber() != "") {
					propDocNum = getDocNumber();
				} else {
					propDocNum = property.getDocNumber();
				}
				property.setDocNumber(propDocNum);
				LOGGER.debug("Property is getting added to BasicProperty: " + property);
				basicProperty.addProperty(property);
				basicProperty = basicPropertyService.update(basicProperty);
			} else {
				property.getBasicProperty().setExtraField2(PropertyTaxUtil.buildAddress(address));
				propertyImplService.update(property);
				basicProperty = basicPropertyService.update(basicProperty);
			}

		} else {
			super.validate();
			if (hasErrors()) {
				return view();
			}
		}

		transitionWorkFlow();
		User approverUser = userService.getUserById(getWorkflowBean().getApproverUserId()
				.longValue());
		setAckMessage("Property " + basicProperty.getUpicNo() + " Successfully Forwarded to "
				+ approverUser.getUsername());
		/*
		 * } catch (Exception e) { throw new ApplicationRuntimeException("Exception : "
		 * + e); }
		 */
		LOGGER.debug("forward: AckMessage: " + getAckMessage());
		LOGGER.debug("Exit from forward");

		return FORWARD_ACK;
	}

	@SkipValidation
	@Action(value = "/changePropertyAddress-approve", results = { @Result(name = ACK, location = "/changePropertyAddress-ack.jsp") })
	public String approve() {

		LOGGER.debug("Enetered into approve, BasicProperty: " + basicProperty + ", Address : "
				+ address.getLandmark() + " HouseNo" + address.getHouseNoBldgApt() + "DoorNumOld "
				+ " PinCode" + address.getPinCode());

		try {
			PropertyImpl nonHistProperty = (PropertyImpl) getPersistenceService().findByNamedQuery(
					QUERY_PROPERTY_BY_UPICNO_AND_STATUS, property.getBasicProperty().getUpicNo(),
					STATUS_ISACTIVE);
			nonHistProperty.setStatus(STATUS_ISHISTORY);
			property.setStatus(STATUS_ISACTIVE);

			transitionWorkFlow();

			basicProperty.setAddress(address);

			// upload docs
			processAndStoreDocumentsWithReason(basicProperty, DOCS_ADDRESS_CHANGE_PROPERTY);
			propertyTaxUtil.makeTheEgBillAsHistory(basicProperty);
			basicProperty = basicPropertyService.update(basicProperty);

		} catch (Exception e) {
			throw new ApplicationRuntimeException("Exception : " + e);
		}

		LOGGER.debug("Exit from approve");

		return ACK;
	}

	@SkipValidation
	@Action(value = "/changePropertyAddress-reject", results = { @Result(name = FORWARD_ACK, location = "/changePropertyAddress-forwardAck.jsp") })
	public String reject() {
		LOGGER.debug("reject: Change Property rejection started");
		LOGGER.debug("reject: Property: " + property);
		BasicProperty basicProperty = property.getBasicProperty();
		LOGGER.debug("reject: BasicProperty: " + basicProperty);

		transitionWorkFlow();

		if (WORKFLOW_END.equalsIgnoreCase(property.getState().getValue())) {
			basicProperty.getProperty().setStatus(STATUS_ISHISTORY);
			property.setStatus(STATUS_ISACTIVE);
			setAckMessage(MSG_REJECT_SUCCESS);
			propertyImplService.update(property);
			basicPropertyService.update(basicProperty);
		} else {
			setAckMessage(MSG_REJECT_SUCCESS + " and forwarded to initiator : "
					+ property.getCreatedBy().getUsername());
		}

		LOGGER.debug("reject: BasicProperty: " + basicProperty + "AckMessage: " + getAckMessage());
		LOGGER.debug("reject: Change Property rejection ended");

		return FORWARD_ACK;
	}

	@Override
	public void prepare() {

		LOGGER.debug("Entered into prepare, ModelId: " + getModelId() + ", IndexNumber: "
				+ indexNumber);

		if (getModelId() != null && !getModelId().isEmpty()) {
			property = (PropertyImpl) getPersistenceService().findByNamedQuery(
					QUERY_PROPERTYIMPL_BYID, Long.valueOf(getModelId()));
			LOGGER.debug("prepare: Property by model id: " + property);
		}

		if (indexNumber != null && !indexNumber.equals("")) {
			basicProperty = basicPropertyService.findByNamedQuery(QUERY_BASICPROPERTY_BY_UPICNO,
					indexNumber);
			LOGGER.debug("prepare: BasicProperty by index number : " + basicProperty);
		}
		setupWorkflowDetails();
		setUserInfo();
		LOGGER.debug("Exit from prepare");
	}

	@Override
	public void validate() {

		LOGGER.debug("Entered into the validate method Address : " + address.getLandmark()
				+ " HouseNo" + address.getHouseNoBldgApt() + "DoorNumOld "
				+ " PinCode" + address.getPinCode());

		/* Validates the input data in case the form is submitted */

		if (address.getLandmark() == null
				|| org.apache.commons.lang.StringUtils.equals(address.getLandmark(), "")
				|| org.apache.commons.lang.StringUtils.isEmpty(address.getLandmark())) {
			addActionError(getText("mandatory.addr"));
		}
		if (address.getHouseNoBldgApt() == null
				|| org.apache.commons.lang.StringUtils.equals(address.getHouseNoBldgApt(), "")) {
			addActionError(getText("mandatory.houseNo"));
		} else {
			validateHouseNumber(basicProperty.getPropertyID().getWard().getId(),
					address.getHouseNoBldgApt(), basicProperty);
		}
		if (address.getPinCode() != null) {
			String pincode = org.apache.commons.lang.StringUtils.trim(address.getPinCode()
					.toString());
			if (!pincode.equals("") && pincode.length() < 6) {
				addActionError(getText("mandatory.pincode.size"));
			}
		}
		String mobNo = org.apache.commons.lang.StringUtils.trim(address.getUser().getMobileNumber());

		if (mobNo != null && !mobNo.equals("") && mobNo.length() < 10) {
			addActionError(getText("mandatory.mobileNo.size"));
		}
		super.validate();
		newForm();

		LOGGER.debug("Exit from validate method");

	}

	private void transitionWorkFlow() {

		LOGGER.debug("Entered method : transitionWorkFlow");

		if (workflowBean == null) {
			LOGGER.debug("transitionWorkFlow: workflowBean is NULL");
		} else {
			LOGGER.debug("transitionWorkFlow - action : " + workflowBean.getActionName()
					+ "property: " + property);
		}

		LOGGER.debug("transitionWorkFlow: Property transitioned to "
				+ property.getState().getValue());
		propertyImplService.persist(property);

		LOGGER.debug("Exiting method : transitionWorkFlow");
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

	public void setbasicPropertyService(PersistenceService<BasicProperty, Long> basicPropertyService) {
		this.basicPropertyService = basicPropertyService;
	}

	public String getAckMessage() {
		return ackMessage;
	}

	public void setAckMessage(String ackMessage) {
		this.ackMessage = ackMessage;
	}

	public PersistenceService<BasicProperty, Long> getBasicPropertyService() {
		return basicPropertyService;
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

	@Override
	public void setProperty(PropertyImpl property) {
		this.property = property;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

    @Override
    public StateAware getModel() {

        return null;
    }

}
