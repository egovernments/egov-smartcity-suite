package org.egov.pgr.entity.enums;

import org.apache.commons.lang3.StringUtils;

//any future Receiving mode addition should be added at the end of this
// enum
// since we are asking hibernate use its ordinal to be persisted
public enum ReceivingMode {
    WEBSITE, SMS, CALL, EMAIL, PAPER, MOBILE;
    public String toString() {
        return StringUtils.capitalize(name());
    }
}