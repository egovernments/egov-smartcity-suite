package org.egov.license.trade.entertradelicense.web;

import java.math.BigDecimal;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.egov.infstr.ValidationException;
import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.model.AuditModule;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.entity.Licensee;
import org.egov.license.domain.entity.MotorDetails;
import org.egov.license.domain.service.BaseLicenseService;
import org.egov.license.domain.web.BaseLicenseAction;
import org.egov.license.trade.domain.entity.TradeLicense;
import org.egov.license.trade.domain.service.TradeService;
import org.egov.license.utils.Constants;
import org.egov.web.annotation.ValidationErrorPageExt;
import org.egov.web.utils.ServletActionRedirectResult;

import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@ParentPackage("egov")
@Result(name = "viewlicense", type = ServletActionRedirectResult.class, value = "viewTradeLicense", params = { "namespace", "/viewtradelicense/web", "method", "view","modelId", "${model.id}"})
public class EnterTradeLicenseAction extends BaseLicenseAction {
	private static final long serialVersionUID = 1L;
	private TradeService ts;
	private TradeLicense tradeLicense = new TradeLicense();

	public EnterTradeLicenseAction() {
		super();
		this.tradeLicense.setLicensee(new Licensee());
	}
	
	/* to log errors and debugging information */
	private final Logger LOGGER = Logger.getLogger(getClass());

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
					@RequiredFieldValidator(fieldName = "licensee.address.houseNo", message = "", key = Constants.REQUIRED)},
					emails = {
							@EmailValidator(message = "Please enter the valid Email Id", fieldName="licensee.emailId", key="Please enter the valid Email Id")
							},
			stringLengthFields = { 
					@StringLengthFieldValidator(fieldName = "oldLicenseNumber", maxLength = "30",  message = "", key = "Maximum Length for Old License Number is 30"),
					@StringLengthFieldValidator(fieldName = "nameOfEstablishment", maxLength = "100",  message = "", key = "Name of Establishment can be upto 100 characters"),
					@StringLengthFieldValidator(fieldName = "remarks", maxLength = "500",  message = "", key = "Maximum length for Remarks is 500"),
					@StringLengthFieldValidator(fieldName = "address.streetAddress1", maxLength = "500",  message = "", key = "Maximum length for remaining address is 500"),
					@StringLengthFieldValidator(fieldName = "address.houseNo", maxLength = "10",  message = "", key = "Maximum length for house number is 10"),
					@StringLengthFieldValidator(fieldName = "address.streetAddress2", maxLength = "10",  message = "", key = "Maximum length for house number is 10"),
					@StringLengthFieldValidator(fieldName = "phoneNumber", maxLength = "15",  message = "", key = "Maximum length for Phone Number is 15"),
					@StringLengthFieldValidator(fieldName = "licensee.applicantName", maxLength = "100",  message = "", key = "Maximum length for Applicant Name is 100"),
					@StringLengthFieldValidator(fieldName = "licensee.nationality", maxLength = "50",  message = "", key = "Maximum length for Nationality is 50"),
					@StringLengthFieldValidator(fieldName = "licensee.fatherOrSpouseName", maxLength = "100",  message = "", key = "Maximum length for Father Or SpouseName is 100"),
					@StringLengthFieldValidator(fieldName = "licensee.qualification", maxLength = "50",  message = "", key = "Maximum length for Qualification is 50"),
					@StringLengthFieldValidator(fieldName = "licensee.panNumber", maxLength = "10",  message = "", key = "Maximum length for PAN Number is 10"),
					@StringLengthFieldValidator(fieldName = "licensee.address.houseNo", maxLength = "10",  message = "", key = "Maximum length for house number is 10"),
					@StringLengthFieldValidator(fieldName = "licensee.address.streetAddress2", maxLength = "10",  message = "", key = "Maximum length for house number is 10"),
					@StringLengthFieldValidator(fieldName = "licensee.address.streetAddress1", maxLength = "500",  message = "", key = "Maximum length for remaining address is 500"),
					@StringLengthFieldValidator(fieldName = "licensee.phoneNumber", maxLength = "15",  message = "", key = "Maximum length for Phone Number is 15"),
					@StringLengthFieldValidator(fieldName = "licensee.mobilePhoneNumber", maxLength = "15",  message = "", key = "Maximum length for Phone Number is 15"),
					@StringLengthFieldValidator(fieldName = "licensee.uid", maxLength = "12",  message = "", key = "Maximum length for UID is 12")
			},
			intRangeFields = {
					@IntRangeFieldValidator(fieldName = "noOfRooms", min="1", max = "999",  message = "", key = "Number of rooms should be in the range 1 to 999"),
					@IntRangeFieldValidator(fieldName = "address.pinCode", min="100000", max = "999999",  message = "", key = "Minimum and Maximum length for Pincode is 6 and all Digit Cannot be 0"),
					@IntRangeFieldValidator(fieldName = "licensee.age", min="1", max = "100",  message = "", key = "Age should be in the range of 1 to 100"),
					@IntRangeFieldValidator(fieldName = "licensee.address.pinCode", min="100000", max = "999999",  message = "", key = "Minimum and Maximum length for Pincode is 6 and all Digit Cannot be 0")
					}
			)
				
	@ValidationErrorPageExt(action = Constants.NEW, makeCall = true, toMethod = "prepareNewForm")
	public String enterExisting() {
		LOGGER.debug("Enter Existing Trade license Creation Parameters:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense);
		if (this.tradeLicense.getLicenseZoneId() != null && this.tradeLicense.getBoundary() == null) {
			Boundary boundary = (new BoundaryDAO()).getBoundary(tradeLicense.getLicenseZoneId().intValue());
			this.tradeLicense.setBoundary(boundary);
		}
		
		if (this.tradeLicense.getLicenseeZoneId() != null && this.tradeLicense.getLicensee().getBoundary() == null) {
			Boundary boundary = (new BoundaryDAO()).getBoundary(tradeLicense.getLicenseeZoneId().intValue());
			this.tradeLicense.getLicensee().setBoundary(boundary);
		}
		if (this.ts.getTps().findAllBy("from License where oldLicenseNumber = ?", this.tradeLicense.getOldLicenseNumber()).isEmpty()) {
			if (this.tradeLicense.getInstalledMotorList() != null) {
				final Iterator<MotorDetails> motorDetails = this.tradeLicense.getInstalledMotorList().iterator();
				while (motorDetails.hasNext()) {
					final MotorDetails installedMotor = motorDetails.next();
					if ((installedMotor != null) && (installedMotor.getHp() != null) && (installedMotor.getNoOfMachines() != null) && (installedMotor.getHp().compareTo(BigDecimal.ZERO) != 0) && (installedMotor.getNoOfMachines().compareTo(Long.valueOf("0")) != 0)) {
						installedMotor.setLicense(this.tradeLicense);
					} else {
						motorDetails.remove();
					}
				}
			}
			LOGGER.debug(" Enter Existing Trade License Name of Establishment:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense.getNameOfEstablishment());
			super.enterExisting();
			doAuditing(AuditModule.TL,AuditEntity.TL_LIC,Constants.ENTER_LICENSE, this.license().getAuditDetails());
			return "viewlicense";
		} else {
			throw new ValidationException("oldLicenseNumber", "license.number.exist", this.license().getOldLicenseNumber());
		}
	}
	
	@Override
	public void prepareNewForm() {
		super.prepareNewForm();
		this.tradeLicense.setHotelGradeList(this.tradeLicense.populateHotelGradeList());
		this.tradeLicense.setHotelSubCatList(ts.getHotelCategoriesForTrade());
	}

	@Override
	public Object getModel() {
		return this.tradeLicense;
	}
	
	public void setTs(TradeService ts) {
		this.ts = ts;
	}

	@Override
	protected License license() {
		return this.tradeLicense;
	}

	@Override
	protected BaseLicenseService service() {
		this.ts.getPersistenceService().setType(TradeLicense.class);
		return this.ts;
	}

}
