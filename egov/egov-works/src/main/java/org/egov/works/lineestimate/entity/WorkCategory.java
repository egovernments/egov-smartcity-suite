package org.egov.works.lineestimate.entity;

import org.apache.commons.lang.StringUtils;

public enum WorkCategory {
    SLUM_WORK,
    NON_SLUM_WORK;

    @Override
    public String toString() {
        return StringUtils.capitalize(name());
    }

}
