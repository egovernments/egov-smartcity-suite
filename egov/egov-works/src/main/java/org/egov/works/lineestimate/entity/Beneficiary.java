package org.egov.works.lineestimate.entity;

import org.apache.commons.lang.StringUtils;

public enum Beneficiary {

    SC_ST,
    BC,
    GENERAL,
    OTHERS;
    
    @Override
    public String toString() {
        return StringUtils.replace("_", "_", "/");
    }
}
