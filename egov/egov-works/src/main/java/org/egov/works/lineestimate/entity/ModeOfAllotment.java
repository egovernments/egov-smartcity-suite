package org.egov.works.lineestimate.entity;

import org.apache.commons.lang.StringUtils;

public enum ModeOfAllotment {
    NOMINATION,
    TENDERING,
    ePROCUREMENT;
    
    @Override
    public String toString() {
        return StringUtils.capitalize(name());
    }
}
