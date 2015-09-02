package org.egov.tradelicense.domain.entity;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.OptionalPattern;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.Unique;
import org.egov.infstr.models.validator.constants.ValidatorConstants;
import org.hibernate.validator.Length;

/**
 * The Class LicenseAppType.
 */
@Unique(fields = { "licenseApplicationType" }, id = "id", tableName = "EGTL_MSTR_APP_TYPE", columnName = { "name" }, message = "masters.licenseApplicationType.isunique")
public class LicenseAppType extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final String BY_NAME = "LICENSE_APPTYPE_BY_NAME"; 
	
	@Required(message = "masters.licenseApplicationType.name.null")
	@Length(max = 256, message = "masters.licenseApplicationType.name.length")
	@OptionalPattern(regex = ValidatorConstants.alphaNumericwithSpace, message = "tradelicense.error.licenseapptype.text")
	private String name;

	public LicenseAppType() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("LicenseAppType={");
		str.append("serialVersionUID=").append(serialVersionUID);
		str.append("name=").append(name == null ? "null" : name.toString());
		str.append("}");
		return str.toString();
	}
}