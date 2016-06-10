package org.egov.lcms.masters.entity;

import javax.validation.constraints.Max;

import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infstr.models.BaseModel;
import org.egov.lcms.utils.LcmsConstants;
import org.hibernate.validator.constraints.Length;

@Unique(fields = { "interimOrderType", "code" }, id = "id", tableName = "EGLC_INTERIMTYPE_MASTER", columnName = {
        "INTERIMORDERTYPE", "CODE" }, message = "masters.interimOrderType.isunique")
public class InterimOrder extends BaseModel {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1L;

    @Required(message = "masters.interimOrderType.null")
    @Length(max = 32, message = "masters.interimOrderType.length")
    @OptionalPattern(regex = LcmsConstants.mixedChar, message = "masters.interimOrderType.mixedChar")
    private String interimOrderType;

    @Required(message = "masters.code.null")
    @Length(max = 8, message = "masters.code.length")
    @OptionalPattern(regex = "[0-9A-Za-z-]*", message = "masters.code.alpha2")
    private String code;

    @Length(max = 128, message = "masters.description.length")
    private String description;
    @Max(value = 1000, message = "masters.orderNumber.length")
    private Long orderNumber;

    public String getInterimOrderType() {
        return interimOrderType;
    }

    public void setInterimOrderType(String interimOrderType) {
        this.interimOrderType = interimOrderType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

}
