package org.egov.stms.masters.entity.enums;

import org.apache.commons.lang3.StringUtils;

public enum PropertyType {

    RESIDENTIAL, NON_RESIDENTIAL, MIXED;

    @Override
    public String toString() {
        return StringUtils.capitalize(name());
    }
}
