package org.egov.works.lineestimate.entity;

import org.apache.commons.lang.StringUtils;

public enum TypeOfSlum {

    NOTIFIED,
    NON_NOTIFIED;

    @Override
    public String toString() {
        return StringUtils.capitalize(name());
    }
}
