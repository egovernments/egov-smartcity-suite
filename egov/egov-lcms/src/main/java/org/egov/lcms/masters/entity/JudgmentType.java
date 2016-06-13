package org.egov.lcms.masters.entity;

import javax.validation.constraints.Max;

import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infstr.models.BaseModel;
import org.egov.lcms.utils.LcmsConstants;
import org.hibernate.validator.constraints.Length;

@Unique(fields = { "judgmentType", "code" }, id = "id", tableName = "EGLC_JUDGMENTTYPE_MASTER", columnName = {
        "JUDGMENTTYPE", "CODE" }, message = "masters.judgmentType.isunique")
public class JudgmentType extends BaseModel {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1L;

    @Required(message = "masters.code.null")
    @Length(max = 8, message = "masters.code.length")
    @OptionalPattern(regex = "[0-9A-Za-z-]*", message = "masters.code.alpha2")
    private String code;
    private Boolean active;

    public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Required(message = "masters.judgmentType.null")
    @Length(max = 32, message = "masters.judgmentType.length")
    @OptionalPattern(regex = LcmsConstants.mixedChar, message = "masters.judgmentType.mixedChar")
    private String judgmentType;

    @Length(max = 128, message = "masters.description.length")
    private String description;
    @Max(value = 1000, message = "masters.orderNumber.length")
    private Long orderNumber;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the judgmentType
     */
    public String getJudgmentType() {
        return judgmentType;
    }

    /**
     * @param judgmentType the judgmentType to set
     */
    public void setJudgmentType(String judgmentType) {
        this.judgmentType = judgmentType;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the order_Number
     */
    public Long getOrderNumber() {
        return orderNumber;
    }

    /**
     * @param order_Number the order_Number to set
     */
    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }
}
