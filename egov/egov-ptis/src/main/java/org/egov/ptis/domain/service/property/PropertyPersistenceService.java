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

package org.egov.ptis.domain.service.property;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.DateUtils;
import org.egov.infstr.services.PersistenceService;
import org.egov.portal.entity.Citizen;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.report.bean.PropertyAckNoticeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;

public class PropertyPersistenceService extends PersistenceService<BasicProperty, Long> {

    private static final String FROM_USER_WHERE_NAME_AND_MOBILE_NUMBER_AND_GENDER = "From User where name = ? and mobileNumber = ? and gender = ? ";
    private static final Logger LOGGER = Logger.getLogger(PropertyPersistenceService.class);
    private static final String CREATE_ACK_TEMPLATE = "mainCreatePropertyAck";
    @Autowired
    private UserService userService;
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    @Autowired
    private ReportService reportService;

    @Autowired
    private CityService cityService;

    public PropertyPersistenceService() {
        super(BasicProperty.class);
    }

    public PropertyPersistenceService(final Class<BasicProperty> type) {
        super(type);
    }

    public void createOwners(final Property property, final BasicProperty basicProperty, final Address ownerAddress) {

        LOGGER.debug("createOwners for property: " + property + ", basicProperty: " + basicProperty
                + ", ownerAddress: " + ownerAddress);
        int orderNo = 0;
        basicProperty.getPropertyOwnerInfo().clear();
        for (final PropertyOwnerInfo ownerInfo : property.getBasicProperty().getPropertyOwnerInfoProxy()) {
            orderNo++;
            if (ownerInfo != null) {
                User user;
                if (StringUtils.isNotBlank(ownerInfo.getOwner().getAadhaarNumber()))
                    user = userService.getUserByAadhaarNumber(ownerInfo.getOwner().getAadhaarNumber());
                else
                    user = (User) find(FROM_USER_WHERE_NAME_AND_MOBILE_NUMBER_AND_GENDER, ownerInfo
                            .getOwner().getName(), ownerInfo.getOwner().getMobileNumber(), ownerInfo.getOwner()
                                    .getGender());
                if (user == null) {
                    final Citizen newOwner = new Citizen();
                    user = createNewOwner(ownerInfo, newOwner);
                    persistUponPaymentResponse(basicProperty);
                    ownerInfo.setBasicProperty(basicProperty);
                    ownerInfo.setOwner(user);
                    ownerInfo.setOrderNo(orderNo);
                    ownerInfo.getOwner().addAddress(ownerAddress);
                } else {
                    // If existing user, then do not add correspondence address
                    user.setEmailId(ownerInfo.getOwner().getEmailId());
                    user.setGuardian(ownerInfo.getOwner().getGuardian());
                    user.setGuardianRelation(ownerInfo.getOwner().getGuardianRelation());
                    ownerInfo.setOwner(user);
                    ownerInfo.setOrderNo(orderNo);
                    ownerInfo.setBasicProperty(basicProperty);
                }
            }

            basicProperty.addPropertyOwners(ownerInfo);
        }
    }
    
    public void createOwnersForAppurTenant(final Property property, final BasicProperty basicProperty,
            final Address ownerAddress) {
        int orderNo = 0;
        basicProperty.getPropertyOwnerInfo().clear();
        for (final PropertyOwnerInfo ownerInfo : property.getBasicProperty().getPropertyOwnerInfoProxy()) {
            PropertyOwnerInfo owner = new PropertyOwnerInfo();
            orderNo++;
            if (ownerInfo != null) {
                User user;
                if (StringUtils.isNotBlank(ownerInfo.getOwner().getAadhaarNumber()))
                    user = userService.getUserByAadhaarNumber(ownerInfo.getOwner().getAadhaarNumber());
                else
                    user = (User) find(FROM_USER_WHERE_NAME_AND_MOBILE_NUMBER_AND_GENDER, ownerInfo
                            .getOwner().getName(), ownerInfo.getOwner().getMobileNumber(), ownerInfo.getOwner()
                                    .getGender());
                if (user == null) {
                    final Citizen newOwner = new Citizen();
                    user = createNewOwner(ownerInfo, newOwner);
                    persistUponPaymentResponse(basicProperty);
                    owner.setBasicProperty(basicProperty);
                    owner.setOwner(user);
                    owner.setOrderNo(orderNo);
                    owner.setOwnerType(ownerInfo.getOwnerType());
                    owner.setSource(ownerInfo.getSource());
                    LOGGER.debug("createOwners: OwnerAddress: " + ownerAddress);
                    owner.getOwner().addAddress(ownerAddress);
                } else {
                    // If existing user, then do not add correspondence address
                    user.setEmailId(ownerInfo.getOwner().getEmailId());
                    user.setGuardian(ownerInfo.getOwner().getGuardian());
                    user.setGuardianRelation(ownerInfo.getOwner().getGuardianRelation());
                    owner.setOwner(user);
                    owner.setOrderNo(orderNo);
                    owner.setBasicProperty(basicProperty);
                    owner.setOwnerType(ownerInfo.getOwnerType());
                    owner.setSource(ownerInfo.getSource());
                }
            }

            basicProperty.addPropertyOwners(owner);
        }
    }

    private User createNewOwner(final PropertyOwnerInfo ownerInfo, final Citizen newOwner) {
        newOwner.setAadhaarNumber(StringUtils.isNotBlank(ownerInfo.getOwner().getAadhaarNumber()) ? ownerInfo.getOwner().getAadhaarNumber() : null);
        newOwner.setMobileNumber(ownerInfo.getOwner().getMobileNumber());
        newOwner.setEmailId(ownerInfo.getOwner().getEmailId());
        newOwner.setGender(ownerInfo.getOwner().getGender());
        newOwner.setGuardian(ownerInfo.getOwner().getGuardian());
        newOwner.setGuardianRelation(ownerInfo.getOwner().getGuardianRelation());
        newOwner.setName(ownerInfo.getOwner().getName());
        newOwner.setSalutation(ownerInfo.getOwner().getSalutation());
        newOwner.setPassword("NOTSET");
        newOwner.setUsername(propertyTaxUtil.generateUserName(ownerInfo.getOwner().getName()));
        return userService.createUser(newOwner);
    }

    public BasicProperty persistUponPaymentResponse(final BasicProperty basicProperty) {
        return basicProperty;
    }

    public BasicProperty createBasicProperty(final BasicProperty basicProperty, final HashMap meesevaParams) {
        return persist(basicProperty);
    }

    public ReportOutput propertyAcknowledgement(final PropertyImpl property) {
        final Map<String, Object> reportParams = new HashMap<>();
        final PropertyAckNoticeInfo ackBean = new PropertyAckNoticeInfo();
        ackBean.setOwnerName(property.getBasicProperty().getFullOwnerName());
        ackBean.setOwnerAddress(property.getBasicProperty().getAddress().toString());
        ackBean.setApplicationDate(new SimpleDateFormat("dd/MM/yyyy").format(property.getBasicProperty()
                .getCreatedDate()));
        ackBean.setApplicationNo(property.getApplicationNo());
        ackBean.setApprovedDate(new SimpleDateFormat("dd/MM/yyyy").format(property.getState().getCreatedDate()));
        final Date tempNoticeDate = DateUtils.add(property.getState().getCreatedDate(), Calendar.DAY_OF_MONTH, 15);
        ackBean.setNoticeDueDate(tempNoticeDate);
        reportParams.put("logoPath", cityService.getCityLogoURL());
        reportParams.put("cityName", cityService.getMunicipalityName());
        reportParams.put("loggedInUsername", userService.getUserById(ApplicationThreadLocals.getUserId()).getName());
        final ReportRequest reportInput = new ReportRequest(CREATE_ACK_TEMPLATE, ackBean, reportParams);
        reportInput.setReportFormat(ReportFormat.PDF);
        return reportService.createReport(reportInput);
    }

    public String updateOwners(final Property property, final BasicProperty basicProperty, final String doorNumber,
            final BindingResult errors) {
        LOGGER.debug("Update Owner and door number for property: " + property + ", basicProperty: " + basicProperty
                + ", doorNumber: " + doorNumber);
        basicProperty.getAddress().setHouseNoBldgApt(doorNumber);
        final StringBuilder errorMesg = new StringBuilder();
        for (final PropertyOwnerInfo ownerInfo : basicProperty.getPropertyOwnerInfo())
            if (ownerInfo != null) {
                User user = null;
                for (final Address address : ownerInfo.getOwner().getAddress())
                    address.setHouseNoBldgApt(doorNumber);
                if (StringUtils.isNotBlank(ownerInfo.getOwner().getAadhaarNumber()))
                    user = userService.getUserByAadhaarNumber(ownerInfo.getOwner().getAadhaarNumber());
                if (user == null || user.getId().equals(ownerInfo.getOwner().getId()))
                    userService.updateUser(ownerInfo.getOwner());
                else {
                    final BasicProperty basicProp = find("select basicProperty from PropertyOwnerInfo where owner = ?",
                            user.getId());
                    errorMesg.append("With entered aadhar number - ").append(ownerInfo.getOwner().getAadhaarNumber())
                            .append(" there is already owner present with owner name: ")
                            .append(user.getName()).append(" for assessment number : ")
                            .append(basicProp.getUpicNo());
                    break;
                }
            }
        persist(basicProperty);
        LOGGER.debug("Exit from updateOwners");
        return errorMesg.toString();
    }

    /**
     * Update the owners for a property
     * @param property
     * @param basicProp
     * @param ownerAddress
     */
    public void updateOwners(final Property property, final BasicProperty basicProp, final Address ownerAddress) {
        int orderNo = 0;
        basicProp.getPropertyOwnerInfo().clear();
        for (final PropertyOwnerInfo ownerInfo : property.getBasicProperty().getPropertyOwnerInfoProxy()) {

            if (ownerInfo != null) {
                User user;
                if (StringUtils.isNotBlank(ownerInfo.getOwner().getAadhaarNumber()))
                    user = userService.getUserByAadhaarNumber(ownerInfo.getOwner().getAadhaarNumber());
                else
                    user = (User) find(FROM_USER_WHERE_NAME_AND_MOBILE_NUMBER_AND_GENDER, ownerInfo
                            .getOwner().getName(), ownerInfo.getOwner().getMobileNumber(), ownerInfo.getOwner()
                                    .getGender());
                if (user == null) {
                    orderNo++;
                    Citizen newOwner = new Citizen();
                    user = createNewOwner(ownerInfo, newOwner);
                    ownerInfo.setBasicProperty(basicProp);
                    ownerInfo.setOwner(user);
                    ownerInfo.setOrderNo(orderNo);
                    LOGGER.debug("createOwners: OwnerAddress: " + ownerAddress);
                    ownerInfo.getOwner().addAddress(ownerAddress);
                } else {
                    // If existing user, then update the address
                    user.setAadhaarNumber(ownerInfo.getOwner().getAadhaarNumber());
                    user.setMobileNumber(ownerInfo.getOwner().getMobileNumber());
                    user.setName(ownerInfo.getOwner().getName());
                    user.setGender(ownerInfo.getOwner().getGender());
                    user.setEmailId(ownerInfo.getOwner().getEmailId());
                    user.setGuardian(ownerInfo.getOwner().getGuardian());
                    user.setGuardianRelation(ownerInfo.getOwner().getGuardianRelation());
                    ownerInfo.setOwner(user);
                    ownerInfo.setBasicProperty(basicProp);
                }
            }
            basicProp.addPropertyOwners(ownerInfo);
        }
    }

    public BasicProperty updateBasicProperty(final BasicProperty basicProperty, final HashMap<String, String> meesevaParams) {
        return update(basicProperty);
    }
    
    public void createAmalgamatedOwners(final List<PropertyOwnerInfo> childOwners, final BasicProperty basicProperty) {
        int orderNo = 0;
        List<Long> parentOwners = new ArrayList<>();
        for (PropertyOwnerInfo ownerInfo : basicProperty.getPropertyOwnerInfo()) {
            if (ownerInfo.getOrderNo() != null && orderNo < ownerInfo.getOrderNo())
                orderNo = ownerInfo.getOrderNo();
            parentOwners.add(ownerInfo.getOwner().getId());
        }
        for (PropertyOwnerInfo ownerInfo : childOwners) {
            orderNo++;
            if (ownerInfo != null && !parentOwners.contains(ownerInfo.getOwner().getId())) {
                PropertyOwnerInfo childOwnerInfo = new PropertyOwnerInfo();
                childOwnerInfo.setOwner(ownerInfo.getOwner());
                childOwnerInfo.setOrderNo(orderNo);
                childOwnerInfo.setSource(ownerInfo.getSource());
                childOwnerInfo.setBasicProperty(basicProperty);
                basicProperty.addPropertyOwners(childOwnerInfo);
            }
        }
    }
}
