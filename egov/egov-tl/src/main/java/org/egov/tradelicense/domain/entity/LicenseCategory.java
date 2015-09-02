package org.egov.tradelicense.domain.entity;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.OptionalPattern;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.Unique;
import org.egov.infstr.models.validator.constants.ValidatorConstants;
import org.hibernate.validator.Length;

/**
 * The Class TradeCategory.
 */
@Unique(fields = { "tradeCategory" }, id = "id", tableName = "EGTL_MSTR_LICENSE_CATEGORY", columnName = { "name" }, message = "masters.tradecategory.isunique")
public class LicenseCategory extends BaseModel {
	private static final long serialVersionUID = 1L;
	@Required(message = "tradelic.master.tradecategory.null")
	@Length(max = 256, message = "masters.tradecategory.length")
	@OptionalPattern(regex = ValidatorConstants.alphaNumericwithSpace, message = "tradelicense.error.tradecategory.text")
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("LicenseCategory={");
		str.append("serialVersionUID=").append(serialVersionUID);
		str.append("name=").append(name == null ? "null" : name.toString());
		str.append("}");
		return str.toString();
	}
	
}