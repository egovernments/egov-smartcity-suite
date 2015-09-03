/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) <2015>  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *  	1) All versions of this program, verbatim or modified must carry this
 *  	   Legal Notice.
 *
 *  	2) Any misrepresentation of the origin of the material is prohibited. It
 *  	   is required that all modified versions of this material be marked in
 *  	   reasonable ways as different from the original version.
 *
 *  	3) This license does not grant any rights to any user of the program
 *  	   with regards to rights under trademark law for use of the trade names
 *  	   or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.tl.entertradelicense.web;

import java.math.BigDecimal;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.web.struts.annotation.ValidationErrorPageExt;
import org.egov.infstr.ValidationException;
import org.egov.tl.domain.entity.License;
import org.egov.tl.domain.entity.Licensee;
import org.egov.tl.domain.entity.MotorDetails;
import org.egov.tl.domain.entity.TradeLicense;
import org.egov.tl.domain.service.BaseLicenseService;
import org.egov.tl.domain.service.TradeService;
import org.egov.tl.utils.Constants;
import org.egov.tl.web.BaseLicenseAction;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@ParentPackage("egov")
@Result(name = "viewlicense", type = "redirect", location = "viewTradeLicense", params = { "namespace", "/viewtradelicense/web",
        "method", "view", "modelId", "${model.id}" })
public class EnterTradeLicenseAction extends BaseLicenseAction {
    private static final long serialVersionUID = 1L;
    private TradeService ts;
    private final TradeLicense tradeLicense = new TradeLicense();
    @Autowired
    private BoundaryService boundaryService;

    public EnterTradeLicenseAction() {
        super();
        tradeLicense.setLicensee(new Licensee());
    }

    /* to log errors and debugging information */
    private final Logger LOGGER = Logger.getLogger(getClass());

    @Override
    @Validations(
            requiredFields = {
                    @RequiredFieldValidator(fieldName = "oldLicenseNumber", message = "", key = Constants.REQUIRED),
                    @RequiredFieldValidator(fieldName = "dateOfCreation", message = "", key = Constants.REQUIRED),
                    @RequiredFieldValidator(fieldName = "licensee.applicantName", message = "", key = Constants.REQUIRED),
                    @RequiredFieldValidator(fieldName = "licenseeZoneId", message = "", key = Constants.REQUIRED),
                    @RequiredFieldValidator(fieldName = "licenseZoneId", message = "", key = Constants.REQUIRED),
                    @RequiredFieldValidator(fieldName = "tradeName", message = "", key = Constants.REQUIRED),
                    @RequiredFieldValidator(fieldName = "applicationDate", message = "", key = Constants.REQUIRED),
                    @RequiredFieldValidator(fieldName = "licensee.gender", message = "", key = Constants.REQUIRED),
                    @RequiredFieldValidator(fieldName = "nameOfEstablishment", message = "", key = Constants.REQUIRED),
                    @RequiredFieldValidator(fieldName = "address.houseNo", message = "", key = Constants.REQUIRED),
                    @RequiredFieldValidator(fieldName = "licensee.address.houseNo", message = "", key = Constants.REQUIRED) },
            emails = {
                    @EmailValidator(message = "Please enter the valid Email Id", fieldName = "licensee.emailId", key = "Please enter the valid Email Id")
            },
            stringLengthFields = {
                    @StringLengthFieldValidator(fieldName = "oldLicenseNumber", maxLength = "30", message = "", key = "Maximum Length for Old License Number is 30"),
                    @StringLengthFieldValidator(fieldName = "nameOfEstablishment", maxLength = "100", message = "", key = "Name of Establishment can be upto 100 characters"),
                    @StringLengthFieldValidator(fieldName = "remarks", maxLength = "500", message = "", key = "Maximum length for Remarks is 500"),
                    @StringLengthFieldValidator(fieldName = "address.streetAddress1", maxLength = "500", message = "", key = "Maximum length for remaining address is 500"),
                    @StringLengthFieldValidator(fieldName = "address.houseNo", maxLength = "10", message = "", key = "Maximum length for house number is 10"),
                    @StringLengthFieldValidator(fieldName = "address.streetAddress2", maxLength = "10", message = "", key = "Maximum length for house number is 10"),
                    @StringLengthFieldValidator(fieldName = "phoneNumber", maxLength = "15", message = "", key = "Maximum length for Phone Number is 15"),
                    @StringLengthFieldValidator(fieldName = "licensee.applicantName", maxLength = "100", message = "", key = "Maximum length for Applicant Name is 100"),
                    @StringLengthFieldValidator(fieldName = "licensee.nationality", maxLength = "50", message = "", key = "Maximum length for Nationality is 50"),
                    @StringLengthFieldValidator(fieldName = "licensee.fatherOrSpouseName", maxLength = "100", message = "", key = "Maximum length for Father Or SpouseName is 100"),
                    @StringLengthFieldValidator(fieldName = "licensee.qualification", maxLength = "50", message = "", key = "Maximum length for Qualification is 50"),
                    @StringLengthFieldValidator(fieldName = "licensee.panNumber", maxLength = "10", message = "", key = "Maximum length for PAN Number is 10"),
                    @StringLengthFieldValidator(fieldName = "licensee.address.houseNo", maxLength = "10", message = "", key = "Maximum length for house number is 10"),
                    @StringLengthFieldValidator(fieldName = "licensee.address.streetAddress2", maxLength = "10", message = "", key = "Maximum length for house number is 10"),
                    @StringLengthFieldValidator(fieldName = "licensee.address.streetAddress1", maxLength = "500", message = "", key = "Maximum length for remaining address is 500"),
                    @StringLengthFieldValidator(fieldName = "licensee.phoneNumber", maxLength = "15", message = "", key = "Maximum length for Phone Number is 15"),
                    @StringLengthFieldValidator(fieldName = "licensee.mobilePhoneNumber", maxLength = "15", message = "", key = "Maximum length for Phone Number is 15"),
                    @StringLengthFieldValidator(fieldName = "licensee.uid", maxLength = "12", message = "", key = "Maximum length for UID is 12")
            },
            intRangeFields = {
                    @IntRangeFieldValidator(fieldName = "noOfRooms", min = "1", max = "999", message = "", key = "Number of rooms should be in the range 1 to 999"),
                    @IntRangeFieldValidator(fieldName = "address.pinCode", min = "100000", max = "999999", message = "", key = "Minimum and Maximum length for Pincode is 6 and all Digit Cannot be 0"),
                    @IntRangeFieldValidator(fieldName = "licensee.age", min = "1", max = "100", message = "", key = "Age should be in the range of 1 to 100"),
                    @IntRangeFieldValidator(fieldName = "licensee.address.pinCode", min = "100000", max = "999999", message = "", key = "Minimum and Maximum length for Pincode is 6 and all Digit Cannot be 0")
            }
            )
            @ValidationErrorPageExt(action = Constants.NEW, makeCall = true, toMethod = "prepareNewForm")
            public String enterExisting() {
        LOGGER.debug("Enter Existing Trade license Creation Parameters:<<<<<<<<<<>>>>>>>>>>>>>:" + tradeLicense);
        if (tradeLicense.getLicenseZoneId() != null && tradeLicense.getBoundary() == null) {
            final Boundary boundary = boundaryService.getBoundaryById(tradeLicense.getLicenseZoneId());
            tradeLicense.setBoundary(boundary);
        }

        if (tradeLicense.getLicenseeZoneId() != null && tradeLicense.getLicensee().getBoundary() == null) {
            final Boundary boundary = boundaryService.getBoundaryById(tradeLicense.getLicenseeZoneId());
            tradeLicense.getLicensee().setBoundary(boundary);
        }
        if (ts.getTps().findAllBy("from License where oldLicenseNumber = ?", tradeLicense.getOldLicenseNumber()).isEmpty()) {
            if (tradeLicense.getInstalledMotorList() != null) {
                final Iterator<MotorDetails> motorDetails = tradeLicense.getInstalledMotorList().iterator();
                while (motorDetails.hasNext()) {
                    final MotorDetails installedMotor = motorDetails.next();
                    if (installedMotor != null && installedMotor.getHp() != null && installedMotor.getNoOfMachines() != null
                            && installedMotor.getHp().compareTo(BigDecimal.ZERO) != 0
                            && installedMotor.getNoOfMachines().compareTo(Long.valueOf("0")) != 0)
                        installedMotor.setLicense(tradeLicense);
                    else
                        motorDetails.remove();
                }
            }
            LOGGER.debug(" Enter Existing Trade License Name of Establishment:<<<<<<<<<<>>>>>>>>>>>>>:"
                    + tradeLicense.getNameOfEstablishment());
            super.enterExisting();
            // doAuditing(AuditModule.TL,AuditEntity.TL_LIC,Constants.ENTER_LICENSE, this.license().getAuditDetails());
            return "viewlicense";
        } else
            throw new ValidationException("oldLicenseNumber", "license.number.exist", license().getOldLicenseNumber());
    }

    @Override
    public void prepareNewForm() {
        super.prepareNewForm();
        tradeLicense.setHotelGradeList(tradeLicense.populateHotelGradeList());
        tradeLicense.setHotelSubCatList(ts.getHotelCategoriesForTrade());
    }

    @Override
    public Object getModel() {
        return tradeLicense;
    }

    public void setTs(final TradeService ts) {
        this.ts = ts;
    }

    @Override
    protected License license() {
        return tradeLicense;
    }

    @Override
    protected BaseLicenseService service() {
        ts.getPersistenceService().setType(TradeLicense.class);
        return ts;
    }

}
