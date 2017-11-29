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
package org.egov.ptis.actions.citizen.collection;

import com.opensymphony.xwork2.validator.annotations.Validations;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import static org.egov.ptis.constants.PropertyTaxConstants.ADVANCE_COLLECTION_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_AUTO;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_SECONDHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.MAX_ADVANCES_ALLOWED;
import static org.egov.ptis.constants.PropertyTaxConstants.NOT_AVAILABLE;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_BASICPROPERTY_BY_UPICNO;

@Namespace("/citizen/collection")
@ResultPath("/WEB-INF/jsp/")
@Results({
        @Result(name = CollectionAction.RESULT_COLLECTTAX, location = "citizen/collection/collection-collectTax.jsp"),
        @Result(name = CollectionAction.RESULT_TAXPAID, location = "citizen/collection/collection-taxPaid.jsp"),
        @Result(name = CollectionAction.USER_DETAILS, location = "citizen/collection/collection-ownerDetails.jsp"),
        @Result(name = CollectionAction.UPDATEMOBILE_FORM, location = "citizen/collection/collection-updateMobileNo.jsp")})
@SuppressWarnings("serial")
@ParentPackage("egov")
@Validations
public class CollectionAction extends BaseFormAction {
    private static final long serialVersionUID = -153634114075566642L;
    private static final Logger LOGGER = Logger.getLogger(CollectionAction.class);

    public static final String RESULT_COLLECTTAX = "collectTax";
    public static final String RESULT_TAXPAID = "taxPaid";
    protected static final String USER_DETAILS = "ownerDetails";
    protected static final String UPDATEMOBILE_FORM = "updateMobileNo";

    private PersistenceService<BasicProperty, Long> basicPropertyService;
    private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
    private PTBillServiceImpl ptBillServiceImpl;
    private BasicProperty basicProperty;
    private User propertyOwner;

    private String collectXML;
    private String assessmentNumber;
    private String doorNo;
    private String mobileNumber;

    private Long userId;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private PropertyTaxBillable propertyTaxBillable;

    @Override
    public void prepare() {
        LOGGER.debug("Entered into prepare method");
        setUserId(ApplicationThreadLocals.getUserId());
        basicProperty = basicPropertyService.findByNamedQuery(QUERY_BASICPROPERTY_BY_UPICNO, assessmentNumber);
        LOGGER.debug("Exit from prepare method");
    }

    /**
     * @return
     */
    @Action(value = "/collection-generateBill")
    public String generateBill() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered method generatePropertyTaxBill, assessment Number: " + assessmentNumber);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("generatePropertyTaxBill : BasicProperty :" + basicProperty);
        final Map<String, BigDecimal> demandCollMap = propertyTaxUtil.getDemandAndCollection(basicProperty
                .getProperty());
        final BigDecimal currDue = demandCollMap.get(CURR_DMD_STR).subtract(demandCollMap.get(CURR_COLL_STR));
        final BigDecimal arrDue = demandCollMap.get(ARR_DMD_STR).subtract(demandCollMap.get(ARR_COLL_STR));
        /*
         * Advance collection should also be considered for full payment validation. 
         * Current year second installment demand will be the demand for all the advance installments
         */
        BigDecimal advanceCollected = demandCollMap.get(ADVANCE_COLLECTION_STR);
        BigDecimal secondHalfTax = demandCollMap.get(CURR_SECONDHALF_DMD_STR);
        BigDecimal actualAdvanceToBeCollected = secondHalfTax.multiply(new BigDecimal(MAX_ADVANCES_ALLOWED));
        BigDecimal advanceBalance = actualAdvanceToBeCollected.subtract(advanceCollected);

        if (currDue.compareTo(BigDecimal.ZERO) <= 0 && arrDue.compareTo(BigDecimal.ZERO) <= 0 && advanceBalance.compareTo(BigDecimal.ZERO) <= 0)
            return RESULT_TAXPAID;

        if (OWNERSHIP_TYPE_VAC_LAND.equals(basicProperty.getProperty().getPropertyDetail().getPropertyTypeMaster().getCode())) {
            propertyTaxBillable.setVacantLandTaxPayment(Boolean.TRUE);
        }
        propertyTaxBillable.setBasicProperty(basicProperty);
        propertyTaxBillable.setLevyPenalty(true);
        propertyTaxBillable.setReferenceNumber(propertyTaxNumberGenerator.generateBillNumber(basicProperty
                .getPropertyID().getWard().getBoundaryNum().toString()));
        propertyTaxBillable.setBillType(propertyTaxUtil.getBillTypeByCode(BILLTYPE_AUTO));
        try {
            collectXML = URLEncoder.encode(ptBillServiceImpl.getBillXML(propertyTaxBillable), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.info("Exiting method generatePropertyTaxBill, collectXML: " + collectXML);

        return RESULT_COLLECTTAX;
    }

    @Action(value = "/collection-searchOwnerDetails")
    public String searchOwnerDetails() {
        if (null != basicProperty) {
            setPropertyOwner(basicProperty.getPrimaryOwner());
        }
        setMobileNumber(getPropertyOwner().getMobileNumber());
        if (StringUtils.isBlank(propertyOwner.getMobileNumber())) {
            propertyOwner.setMobileNumber("N/A");
        }
        for (final PropertyOwnerInfo propOwner : basicProperty.getPropertyOwnerInfo()) {
            final List<Address> addrSet = propOwner.getOwner().getAddress();
            for (final Address address : addrSet) {
                setDoorNo(address.getHouseNoBldgApt() == null ? NOT_AVAILABLE : address.getHouseNoBldgApt());
                break;
            }
        }
        return USER_DETAILS;
    }

    @Action(value = "/collection-updateMobileNo")
    public String updateMobileNo() {
        if (null != basicProperty) {
            setPropertyOwner(basicProperty.getPrimaryOwner());
        }
        if (StringUtils.isNotBlank(mobileNumber)) {
            propertyOwner.setMobileNumber(mobileNumber);
            userService.updateUser(propertyOwner);
        }
        return UPDATEMOBILE_FORM;
    }

    public PersistenceService<BasicProperty, Long> getBasicPropertyService() {
        return basicPropertyService;
    }

    public void setbasicPropertyService(final PersistenceService<BasicProperty, Long> basicPropertyService) {
        this.basicPropertyService = basicPropertyService;
    }

    public PropertyTaxNumberGenerator getPropertyTaxNumberGenerator() {
        return propertyTaxNumberGenerator;
    }

    public void setPropertyTaxNumberGenerator(final PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
        this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
    }

    public PropertyTaxUtil getPropertyTaxUtil() {
        return propertyTaxUtil;
    }

    public void setPropertyTaxUtil(final PropertyTaxUtil propertyTaxUtil) {
        this.propertyTaxUtil = propertyTaxUtil;
    }

    public String getCollectXML() {
        return collectXML;
    }

    public void setCollectXML(final String collectXML) {
        this.collectXML = collectXML;
    }

    @Override
    public Object getModel() {

        return null;
    }

    public String getAssessmentNumber() {
        return assessmentNumber;
    }

    public void setAssessmentNumber(final String assessmentNumber) {
        this.assessmentNumber = assessmentNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public PTBillServiceImpl getPtBillServiceImpl() {
        return ptBillServiceImpl;
    }

    public void setPtBillServiceImpl(final PTBillServiceImpl ptBillServiceImpl) {
        this.ptBillServiceImpl = ptBillServiceImpl;
    }

    public BasicProperty getBasicProperty() {
        return basicProperty;
    }

    public void setBasicProperty(BasicProperty basicProperty) {
        this.basicProperty = basicProperty;
    }

    public User getPropertyOwner() {
        return propertyOwner;
    }

    public void setPropertyOwner(User propertyOwner) {
        this.propertyOwner = propertyOwner;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

}
