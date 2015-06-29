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
package org.egov.ptis.actions.view;

import static java.math.BigDecimal.ZERO;
import static org.egov.demand.model.EgdmCollectedReceipt.RCPT_CANCEL_STATUS;
import static org.egov.ptis.constants.PropertyTaxConstants.BEANNAME_PROPERTY_TAX_BILLABLE;
import static org.egov.ptis.constants.PropertyTaxConstants.CANCELLED_RECEIPT_STATUS;
import static org.egov.ptis.constants.PropertyTaxConstants.CITIZENUSER;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.DCBException;
import org.egov.commons.Installment;
import org.egov.dcb.bean.DCBDisplayInfo;
import org.egov.dcb.bean.DCBReport;
import org.egov.dcb.bean.Receipt;
import org.egov.dcb.service.DCBService;
import org.egov.dcb.service.DCBServiceImpl;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.beanfactory.ApplicationContextBeanProvider;
import org.egov.ptis.client.model.PropertyArrearBean;
import org.egov.ptis.client.util.DCBUtils;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyArrear;
import org.egov.ptis.domain.entity.property.PropertyReceipt;
import org.egov.ptis.exceptions.PropertyNotFoundException;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;
import org.springframework.beans.factory.annotation.Autowired;


@Namespace("/view")
@ResultPath("/WEB-INF/jsp/")
@Results({ 
    @Result(name = ViewDCBPropertyAction.VIEW, location = "view/viewDCBProperty-view.jsp")
})
@SuppressWarnings("serial")
@ParentPackage("egov")
public class ViewDCBPropertyAction extends BaseFormAction implements ServletRequestAware {

	private static final String HEADWISE_DCB = "headwiseDcb";
	private String propertyId;
	private String wardName;
	private String ownerName;
	private String propertyAddress;
	private String propertyType;
	private String currTaxAmount;

	private BasicProperty basicProperty;
	private DCBDisplayInfo dcbDispInfo;
	private Map<String, BigDecimal> propertyArrearsMap = new TreeMap<String, BigDecimal>();
	private List<PropertyArrearBean> propertyArrearsList = new ArrayList<PropertyArrearBean>();

	private DCBReport dcbReport = new DCBReport();
	private final Logger LOGGER = Logger.getLogger(getClass());

	public static final String VIEW = "view";
	public static final String RESULT_ARREAR = "viewArrear";
	private HttpSession session = null;
	private HttpServletRequest request;
	private Long userId;
	private Boolean isCitizen;
	private String encodedConsumerCode;
	private List<PropertyReceipt> propReceiptList = new ArrayList<PropertyReceipt>();
	final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private List<Receipt> cancelRcpt = new ArrayList<Receipt>();
	private List<Receipt> activeRcpts = new ArrayList<Receipt>();
	private String demandEffectiveYear;
	private Integer noOfDaysForInactiveDemand;
	private String errorMessage;
	
	@Autowired
	private UserService userService;
	@Autowired
	private BasicPropertyDAO basicPropertyDAO;
	@Autowired
	private PtDemandDao ptDemandDAO;
	@Autowired
        private ApplicationContextBeanProvider beanProvider;
	@Autowired
	private DCBService dcbService;

	public ViewDCBPropertyAction() {
	}

	@Override
	public Object getModel() {
		return dcbReport;
	}

	@Override
	public void prepare() {
		setBasicProperty(basicPropertyDAO.getBasicPropertyByPropertyID(propertyId));
	}

	/**
	 * 
	 * @return String the target page
	 */

	@ValidationErrorPage(value = VIEW)
	@Action(value = "/viewDCBProperty-displayPropInfo")
	public String displayPropInfo() {

		LOGGER.debug("Entered into method displayPropInfo");
		LOGGER.debug("displayPropInfo : Index Number : " + propertyId);
		StringBuilder encodedPropertyid = new StringBuilder();
		DCBUtils dcbUtils = new DCBUtils();
		session = request.getSession();
		if (session.getAttribute("com.egov.user.LoginUserId") == null) {
			User user = userService.getUserByUsername(CITIZENUSER);
			userId = user.getId();
			EgovThreadLocals.setUserId(userId);
			session.setAttribute("com.egov.user.LoginUserName", user.getUsername());

			if (user != null) {
				setCitizen(Boolean.TRUE);
			}

		} else {
			setCitizen(Boolean.FALSE);
		}

		Property property = getBasicProperty().getProperty();
		PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();
		Map<String, BigDecimal> demandCollMap = ptDemandDAO.getDemandCollMap(property);

		try {
			if (getBasicProperty() == null) {
				throw new PropertyNotFoundException();
			} else {
				LOGGER.debug("BasicProperty : " + basicProperty);
				setOwnerName(ptisCacheMgr.buildOwnerFullName(property.getPropertyOwnerSet()));
				setPropertyAddress(ptisCacheMgr.buildAddressByImplemetation(basicProperty
						.getAddress()));
				setWardName(basicProperty.getPropertyID().getWard().getName());
				setPropertyType(property.getPropertyDetail().getPropertyTypeMaster().getType());
				setCurrTaxAmount(demandCollMap.get(CURR_DMD_STR).toString());
				LOGGER.debug("Owner name : " + getOwnerName() + ", " + "Property address : "
						+ getPropertyAddress() + ", " + "Ward name : " + getWardName() + ", "
						+ "Property type : " + getPropertyType() + ", " + "Current tax : "
						+ getCurrTaxAmount());

			}
		} catch (PropertyNotFoundException e) {
			LOGGER.error("Property not found with given Index Number " + propertyId, e);
		}
		encodedPropertyid.append(propertyId).append("(Zone:")
				.append(basicProperty.getPropertyID().getZone().getBoundaryNum()).append(" ")
				.append("Ward:").append(basicProperty.getPropertyID().getWard().getBoundaryNum())
				.append(")");
		LOGGER.debug("Consumer Code : " + encodedPropertyid.toString());
		try {
			setEncodedConsumerCode(URLEncoder.encode(encodedPropertyid.toString(), "UTF-8"));
			LOGGER.debug("ecoded Consumer Code : " + getEncodedConsumerCode());
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Error occured in method displayPropInfo while ecoding index no ", e);
		}
		
		PropertyTaxBillable billable = (PropertyTaxBillable) beanProvider.getBean(BEANNAME_PROPERTY_TAX_BILLABLE);
		billable.setBasicProperty(basicProperty);
		dcbService.setBillable(billable);
		
		dcbDispInfo = dcbUtils.prepareDisplayInfo();

		try {
			dcbReport = dcbService.getCurrentDCBAndReceipts(dcbDispInfo);
		} catch (DCBException e) {
			errorMessage = "Demand details does not exists !";
			LOGGER.warn(errorMessage);
		}

		LOGGER.debug("Exit from method displayPropInfo");
		return VIEW;
	}

	public String displayHeadwiseDcb() {
		return HEADWISE_DCB;
	}

	private List<Receipt> populateActiveReceiptsOnly(Map<Installment, List<Receipt>> receipts) {

		List<Receipt> rcpt = new ArrayList<Receipt>();
		for (Map.Entry<Installment, List<Receipt>> entry : receipts.entrySet()) {
			for (Receipt r : entry.getValue()) {
				if (!rcpt.contains(r) && !r.getReceiptStatus().equals(RCPT_CANCEL_STATUS)) {
					rcpt.add(r);
				}
			}
		}
		return rcpt;
	}

	private void receiptsInDescendingOrderOfReceiptDate() {
		LOGGER.debug("Entered into receiptsInDescendingOrderOfReceiptDate");

		for (Map.Entry<Installment, List<Receipt>> receiptMap : dcbReport.getReceipts().entrySet()) {
			List<Receipt> receipts = receiptMap.getValue();
			Collections.sort(receipts, new Comparator<Receipt>() {

				@Override
				public int compare(Receipt r1, Receipt r2) {
					int returnValue = 0;

					if (r1.getReceiptDate().before(r2.getReceiptDate())) {
						returnValue = 1;
					} else if (r1.getReceiptDate().after(r2.getReceiptDate())) {
						returnValue = -1;
					} else if (r1.getReceiptDate().equals(r2.getReceiptDate())) {
						returnValue = 0;
					}
					return returnValue;
				}
			});
		}

		LOGGER.debug("Exiting from receiptsInDescendingOrderOfReceiptDate");
	}

	private List<Receipt> populateCancelledReceiptsOnly(Map<Installment, List<Receipt>> receipts) {

		List<Receipt> rcpt = new ArrayList<Receipt>();
		for (Map.Entry<Installment, List<Receipt>> entry : receipts.entrySet()) {
			for (Receipt r : entry.getValue()) {
				if (!rcpt.contains(r) && r.getReceiptStatus().equals(RCPT_CANCEL_STATUS)) {
					rcpt.add(r);
				}
			}
		}
		return rcpt;
	}

	/**
	 * Called to get the cancelled receipts
	 * 
	 * @return List of Receipts
	 */
	public List<Receipt> getCancelledReceipts() {

		LOGGER.debug("Entered into method getCancelledReceipts");

		List<Receipt> cancelledReceipts = new ArrayList<Receipt>();
		for (Installment inst : dcbReport.getReceipts().keySet()) {
			List<Receipt> receipts = dcbReport.getReceipts().get(inst);
			LOGGER.debug("Installment : " + inst);
			LOGGER.debug("Cancelled receipts : ");
			for (Receipt rcpt : receipts) {
				if (!cancelledReceipts.contains(rcpt)
						&& rcpt.getReceiptStatus().equals(CANCELLED_RECEIPT_STATUS)) {
					LOGGER.debug("Receipt : " + rcpt);
					cancelledReceipts.add(rcpt);
				}
			}
		}

		LOGGER.debug("Number of cancelled receitps : "
				+ (cancelledReceipts != null ? cancelledReceipts.size() : ZERO));
		LOGGER.debug("Exit from method getCancelledREceipts");

		return cancelledReceipts;
	}

	@Override
	@SkipValidation
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	@SuppressWarnings("unchecked")
	public String getPropertyArrears() {
		LOGGER.debug("Entered into getPropertyArrears");
		LOGGER.debug("getPropertyArrears - propertyid: " + getPropertyId());
		PropertyTaxUtil ptUtil = new PropertyTaxUtil();
		List<PropertyArrear> arrears = getPersistenceService().findAllBy(
				"from PropertyArrear pa where pa.basicProperty = ?", getBasicProperty());
		propertyArrearsList = ptUtil.getPropertyArrears(arrears);

		// List of property receipts
		propReceiptList = getPersistenceService().findAllBy(
				"from PropertyReceipt where basicProperty.id=?", getBasicProperty().getId());
		for (PropertyReceipt propReceipt : propReceiptList) {
			try {
				propReceipt.setReceiptDate(sdf.parse(sdf.format(propReceipt.getReceiptDate())));
				propReceipt.setFromDate(sdf.parse(sdf.format(propReceipt.getFromDate())));
				propReceipt.setToDate(sdf.parse(sdf.format(propReceipt.getToDate())));
			} catch (ParseException e) {
				LOGGER.error("ParseException in getPropertyArrears method for Property"
						+ propertyId, e);
			}
		}
		LOGGER.debug("getPropertyArrears - total arrears: " + propertyArrearsMap.size());
		LOGGER.debug("Exiting from getPropertyArrears");
		return RESULT_ARREAR;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getPropertyAddress() {
		return propertyAddress;
	}

	public void setPropertyAddress(String propertyAddress) {
		this.propertyAddress = propertyAddress;
	}

	public BasicProperty getBasicProperty() {
		return basicProperty;
	}

	public void setBasicProperty(BasicProperty basicProperty) {
		this.basicProperty = basicProperty;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public DCBService getDcbService() {
		return dcbService;
	}

	public void setDcbService(DCBService dcbService) {
		this.dcbService = dcbService;
	}

	public DCBDisplayInfo getDcbDispInfo() {
		return dcbDispInfo;
	}

	public void setDcbDispInfo(DCBDisplayInfo dcbDispInfo) {
		this.dcbDispInfo = dcbDispInfo;
	}

	public DCBReport getDcbReport() {
		return dcbReport;
	}

	public void setDcbReport(DCBReport dcbReport) {
		this.dcbReport = dcbReport;
	}

	public String getCurrTaxAmount() {
		return currTaxAmount;
	}

	public void setCurrTaxAmount(String currTaxAmount) {
		this.currTaxAmount = currTaxAmount;
	}

	public Map<String, BigDecimal> getPropertyArrearsMap() {
		return propertyArrearsMap;
	}

	public void setPropertyArrearsMap(Map<String, BigDecimal> propertyArrearsMap) {
		this.propertyArrearsMap = propertyArrearsMap;
	}

	public Boolean isCitizen() {
		return isCitizen;
	}

	public void setCitizen(Boolean isCitizen) {
		this.isCitizen = isCitizen;
	}

	public List<PropertyArrearBean> getPropertyArrearsList() {
		return propertyArrearsList;
	}

	public void setPropertyArrearsList(List<PropertyArrearBean> propertyArrearsList) {
		this.propertyArrearsList = propertyArrearsList;
	}

	public String getEncodedConsumerCode() {
		return encodedConsumerCode;
	}

	public void setEncodedConsumerCode(String encodedConsumerCode) {
		this.encodedConsumerCode = encodedConsumerCode;
	}

	public List<PropertyReceipt> getPropReceiptList() {
		return propReceiptList;
	}

	public void setPropReceiptList(List<PropertyReceipt> propReceiptList) {
		this.propReceiptList = propReceiptList;
	}

	public List<Receipt> getCancelRcpt() {
		return cancelRcpt;
	}

	public void setCancelRcpt(List<Receipt> cancelRcpt) {
		this.cancelRcpt = cancelRcpt;
	}

	public List<Receipt> getActiveRcpts() {
		return activeRcpts;
	}

	public void setActiveRcpts(List<Receipt> activeRcpts) {
		this.activeRcpts = activeRcpts;
	}

	public String getDemandEffectiveYear() {
		return demandEffectiveYear;
	}

	public void setDemandEffectiveYear(String demandEffectiveYear) {
		this.demandEffectiveYear = demandEffectiveYear;
	}

	public Integer getNoOfDaysForInactiveDemand() {
		return noOfDaysForInactiveDemand;
	}

	public void setNoOfDaysForInactiveDemand(Integer noOfDaysForInactiveDemand) {
		this.noOfDaysForInactiveDemand = noOfDaysForInactiveDemand;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
