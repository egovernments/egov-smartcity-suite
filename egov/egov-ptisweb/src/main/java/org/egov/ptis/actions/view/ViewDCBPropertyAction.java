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
package org.egov.ptis.actions.view;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.DCBException;
import org.egov.commons.Installment;
import org.egov.dcb.bean.DCBDisplayInfo;
import org.egov.dcb.bean.DCBReport;
import org.egov.dcb.bean.Receipt;
import org.egov.dcb.service.DCBService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.ptis.client.model.PropertyArrearBean;
import org.egov.ptis.client.util.DCBUtils;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyReceipt;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.exceptions.PropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.math.BigDecimal.ZERO;
import static org.egov.demand.model.EgdmCollectedReceipt.RCPT_CANCEL_STATUS;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.BEANNAME_PROPERTY_TAX_BILLABLE;
import static org.egov.ptis.constants.PropertyTaxConstants.CANCELLED_RECEIPT_STATUS;
import static org.egov.ptis.constants.PropertyTaxConstants.CITIZENUSER;
import static org.egov.ptis.constants.PropertyTaxConstants.SESSIONLOGINID;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Results({ @Result(name = ViewDCBPropertyAction.VIEW, location = "viewDCBProperty-view.jsp"),
        @Result(name = ViewDCBPropertyAction.HEADWISE_DCB, location = "viewDCBProperty-headwiseDcb.jsp"),
        @Result(name = ViewDCBPropertyAction.RESULT_MIGDATA, location = "viewDCBProperty-viewMigData.jsp") })
public class ViewDCBPropertyAction extends BaseFormAction implements ServletRequestAware {
    private static final Logger LOGGER = Logger.getLogger(ViewDCBPropertyAction.class);
    public static final String HEADWISE_DCB = "headwiseDcb";
    public static final String VIEW = "view";
    public static final String RESULT_MIGDATA = "viewMigData";

    private Map<String, BigDecimal> propertyArrearsMap = new TreeMap<String, BigDecimal>();
    private List<PropertyArrearBean> propertyArrearsList = new ArrayList<PropertyArrearBean>();
    private DCBReport dcbReport = new DCBReport();

    private String propertyId;
    private BasicProperty basicProperty;
    private DCBDisplayInfo dcbDispInfo;
    private HttpSession session = null;
    private HttpServletRequest request;
    private Long userId;
    private Boolean isCitizen = Boolean.FALSE;
    private List<PropertyReceipt> propReceiptList = new ArrayList<PropertyReceipt>();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private List<Receipt> cancelRcpt = new ArrayList<Receipt>();
    private List<Receipt> activeRcpts = new ArrayList<Receipt>();
    private List<Receipt> mutationRcpts = new ArrayList<Receipt>();
    private String demandEffectiveYear;
    private Integer noOfDaysForInactiveDemand;
    private String errorMessage;
    private String roleName;
    private Map<String, Object> viewMap;

    @Autowired
    private UserService userService;
    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    @Autowired
    private PtDemandDao ptDemandDAO;

    @Autowired
    private ApplicationContext beanProvider;

    @Autowired
    private DCBService dcbService;
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

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
     * @return String the target page
     */

    @ValidationErrorPage(value = VIEW)
    @Action(value = "/view/viewDCBProperty-displayPropInfo")
    public String displayPropInfo() {

        LOGGER.debug("Entered into method displayPropInfo");
        LOGGER.debug("displayPropInfo : propertyId : " + propertyId);
        DCBUtils dcbUtils = new DCBUtils();
        session = request.getSession();
        if (session.getAttribute(SESSIONLOGINID) == null) {
            User user = userService.getUserByUsername(CITIZENUSER);
            userId = user.getId();
            EgovThreadLocals.setUserId(userId);
            session.setAttribute("com.egov.user.LoginUserName", user.getUsername());
            if (user != null)
                setCitizen(Boolean.TRUE);
        } else {
            setCitizen(Boolean.FALSE);
            final Long userId = (Long) session().get(SESSIONLOGINID);
            if (userId != null) {
                setRoleName(propertyTaxUtil.getRolesForUserId(userId));
            }
        }

        try {
            if (getBasicProperty() == null) {
                return VIEW;
            } else {
                LOGGER.debug("BasicProperty : " + basicProperty);
                basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(propertyId);
                viewMap = new HashMap<String, Object>();
                viewMap.put("propID", basicProperty.getPropertyID());
                PropertyTypeMaster propertyTypeMaster = basicProperty.getProperty().getPropertyDetail()
                        .getPropertyTypeMaster();
                viewMap.put("ownershipType", propertyTypeMaster.getType());
                Property property = getBasicProperty().getProperty();
                viewMap.put("propAddress", getBasicProperty().getAddress().toString());
                viewMap.put("ownerName", basicProperty.getFullOwnerName());
                viewMap.put("taxExempted", property.getIsExemptedFromTax());
                if (!property.getIsExemptedFromTax()) {
                    Map<String, BigDecimal> demandCollMap = ptDemandDAO.getDemandCollMap(property);
                    viewMap.put("currFirstHalfTaxAmount",
                            demandCollMap.get(PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR));
                    viewMap.put("currFirstHalfTaxDue", demandCollMap.get(PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR)
                            .subtract(demandCollMap.get(PropertyTaxConstants.CURR_FIRSTHALF_COLL_STR)));
                    viewMap.put("currSecondHalfTaxAmount",
                            demandCollMap.get(PropertyTaxConstants.CURR_SECONDHALF_DMD_STR));
                    viewMap.put("currSecondHalfTaxDue", demandCollMap.get(PropertyTaxConstants.CURR_SECONDHALF_DMD_STR)
                            .subtract(demandCollMap.get(PropertyTaxConstants.CURR_SECONDHALF_COLL_STR)));
                    viewMap.put("totalArrDue", demandCollMap.get(ARR_DMD_STR).subtract(demandCollMap.get(ARR_COLL_STR)));
                    PropertyTaxBillable billable = (PropertyTaxBillable) beanProvider
                            .getBean(BEANNAME_PROPERTY_TAX_BILLABLE);
                    billable.setBasicProperty(basicProperty);
                    dcbService.setBillable(billable);

                    dcbDispInfo = dcbUtils.prepareDisplayInfo();

                    dcbReport = dcbService.getCurrentDCBAndReceipts(dcbDispInfo);
                    // Display active receipt
                    activeRcpts = populateActiveReceiptsOnly(dcbReport.getReceipts());
                    // Display cancelled receipt
                    cancelRcpt = populateCancelledReceiptsOnly(dcbReport.getReceipts());
                    // Display name transfer receipts
                    populateMutationReceipts();
                } else {
                    viewMap.put("currTaxAmount", BigDecimal.ZERO);
                    viewMap.put("currTaxDue", BigDecimal.ZERO);
                    viewMap.put("totalArrDue", BigDecimal.ZERO);
                    dcbReport.setTotalDmdTax(BigDecimal.ZERO);
                    dcbReport.setTotalLpayPnlty(BigDecimal.ZERO);
                    dcbReport.setTotalDmdPnlty(BigDecimal.ZERO);
                    dcbReport.setTotalColTax(BigDecimal.ZERO);
                    dcbReport.setTotalColPnlty(BigDecimal.ZERO);
                    dcbReport.setTotalColLpayPnlty(BigDecimal.ZERO);
                    dcbReport.setTotalRebate(BigDecimal.ZERO);
                    dcbReport.setTotalBalance(BigDecimal.ZERO);
                }

            }

        } catch (DCBException e) {
            errorMessage = "Demand details does not exists !";
            LOGGER.warn(errorMessage);
        }
        LOGGER.debug("Exit from method displayPropInfo");
        return VIEW;
    }

    @Action(value = "/view/viewDCBProperty-displayHeadwiseDcb")
    public String displayHeadwiseDcb() {
        LOGGER.debug("Entered into method displayHeadwiseDcb");
        LOGGER.debug("displayPropInfo : Index Number : " + propertyId);
        DCBUtils dcbUtils = new DCBUtils();
        session = request.getSession();
        if (session.getAttribute("com.egov.user.LoginUserId") == null) {
            /*
             * UserDAO userDao = new UserDAO(); User user =
             * userDao.getUserByUserName(CITIZENUSER); userId = user.getId();
             * EGOVThreadLocals.setUserId(userId.toString());
             */
            // EGOVThreadLocals.setUserId("27613");
            session.setAttribute("com.egov.user.LoginUserName", CITIZENUSER);
            setCitizen(Boolean.TRUE);
        } else {
            setCitizen(Boolean.FALSE);
        }

        try {
            if (getBasicProperty() == null) {
                throw new PropertyNotFoundException();
            }
        } catch (PropertyNotFoundException e) {
            LOGGER.error("Property not found with given Index Number " + propertyId, e);
        }

        LOGGER.debug("BasicProperty : " + basicProperty);
        basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(propertyId);
        PropertyTaxBillable billable = (PropertyTaxBillable) beanProvider.getBean(BEANNAME_PROPERTY_TAX_BILLABLE);
        billable.setBasicProperty(basicProperty);
        dcbService.setBillable(billable);
        // dcbDispInfo = dcbUtils.prepareDisplayInfo();
        dcbDispInfo = dcbUtils.prepareDisplayInfoHeadwise();
        dcbReport = dcbService.getCurrentDCBAndReceipts(dcbDispInfo);
        LOGGER.debug("Exit from method displayHeadwiseDcb");
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
        return receiptsInDescendingOrderOfReceiptDate(rcpt);
    }

    private List<Receipt> receiptsInDescendingOrderOfReceiptDate(List<Receipt> receipts) {
        LOGGER.debug("Entered into receiptsInDescendingOrderOfReceiptDate");

        Collections.sort(receipts, new Comparator<Receipt>() {

            @Override
            public int compare(Receipt r1, Receipt r2) {
                return r2.getReceiptDate().compareTo(r1.getReceiptDate());
            }
        });

        LOGGER.debug("Exiting from receiptsInDescendingOrderOfReceiptDate");
        return receipts;
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
        return receiptsInDescendingOrderOfReceiptDate(rcpt);
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
                if (!cancelledReceipts.contains(rcpt) && rcpt.getReceiptStatus().equals(CANCELLED_RECEIPT_STATUS)) {
                    LOGGER.debug("Receipt : " + rcpt);
                    cancelledReceipts.add(rcpt);
                }
            }
        }

        LOGGER.debug("Number of cancelled receitps : " + (cancelledReceipts != null ? cancelledReceipts.size() : ZERO));
        LOGGER.debug("Exit from method getCancelledREceipts");

        return cancelledReceipts;
    }

    @Override
    @SkipValidation
    public void setServletRequest(HttpServletRequest arg0) {
        this.request = arg0;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/view/viewDCBProperty-showMigData")
    public String getMigratedData() {
        LOGGER.debug("Entered into getMigratedData");
        LOGGER.debug("getMigratedData - propertyId: " + getPropertyId());
        // List of property receipts
        propReceiptList = getPersistenceService().findAllBy(
                "from PropertyReceipt where basicProperty.id=? order by receiptDate desc", getBasicProperty().getId());
        for (PropertyReceipt propReceipt : propReceiptList) {
            try {
                propReceipt.setReceiptDate(sdf.parse(sdf.format(propReceipt.getReceiptDate())));
                propReceipt.setFromDate(sdf.parse(sdf.format(propReceipt.getFromDate())));
                propReceipt.setToDate(sdf.parse(sdf.format(propReceipt.getToDate())));
            } catch (ParseException e) {
                LOGGER.error("ParseException in getPropertyArrears method for Property" + propertyId, e);
            }
        }
        LOGGER.debug("Exiting from getMigratedData");
        return RESULT_MIGDATA;
    }

    private void populateMutationReceipts() {
        Receipt receipt = null;
        for (PropertyMutation propMutation : basicProperty.getPropertyMutations()) {
            if (propMutation.getReceiptNum() != null && !propMutation.getReceiptNum().isEmpty()) {
                receipt = new Receipt();
                receipt.setReceiptNumber(propMutation.getReceiptNum());
                receipt.setReceiptAmt(propMutation.getMutationFee());
                receipt.setReceiptDate(propMutation.getReceiptDate());
                receipt.setConsumerCode(propMutation.getApplicationNo());
                mutationRcpts.add(receipt);
            }
        }
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public BasicProperty getBasicProperty() {
        return basicProperty;
    }

    public void setBasicProperty(BasicProperty basicProperty) {
        this.basicProperty = basicProperty;
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

    public Map<String, BigDecimal> getPropertyArrearsMap() {
        return propertyArrearsMap;
    }

    public void setPropertyArrearsMap(Map<String, BigDecimal> propertyArrearsMap) {
        this.propertyArrearsMap = propertyArrearsMap;
    }

    public Boolean getIsCitizen() {
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

    public List<Receipt> getMutationRcpts() {
        return mutationRcpts;
    }

    public void setMutationRcpts(List<Receipt> mutationRcpts) {
        this.mutationRcpts = mutationRcpts;
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

    public Map<String, Object> getViewMap() {
        return viewMap;
    }

    public void setViewMap(Map<String, Object> viewMap) {
        this.viewMap = viewMap;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
