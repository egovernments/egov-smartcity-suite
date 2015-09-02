package org.egov.tradelicense.domain.entity;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.OptionalPattern;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.Unique;
import org.egov.infstr.models.validator.constants.ValidatorConstants;
import org.hibernate.validator.Length;

@Unique(fields = { "tradeNature" }, id = "id", tableName = "EGTL_TRADE_NATURE", columnName = { "TRADE_NATURE" }, message = "masters.tradenature.isunique")
public class NatureOfBusiness extends BaseModel {
	@Required(message = "tradelic.master.tradenature.null")
	@Length(max = 256, message = "masters.tradenature.length")
	@OptionalPattern(regex = ValidatorConstants.alphaNumericwithSpace, message = "tradelicense.error.tradenature.text")
	private String name;
	public static final String BY_NAME = "NATUREOFBUSINESS_BY_NAME"; 
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("NatureOfBusiness={");
		str.append("  name=").append(name == null ? "null" : name.toString());
		str.append("}");
		return str.toString();
	}
}