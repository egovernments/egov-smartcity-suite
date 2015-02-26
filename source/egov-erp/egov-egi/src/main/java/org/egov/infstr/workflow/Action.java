/*
 * @(#)Action.java 3.0, 17 Jun, 2013 4:28:27 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved.
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.workflow;

import org.egov.infstr.models.BaseModel;

public class Action extends BaseModel {

    private static final long serialVersionUID = 1L;
    public static final String BY_NAME_AND_TYPE = "BY_NAME_AND_TYPE";
    public static final String IN_NAMES_AND_TYPE = "IN_NAMES_AND_TYPE";

    private String name;
    private String description;
    private String type;

    private Action() {
    }

    public Action(final String name, final String type, final String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    protected void setType(String type) {
        this.type = type;
    }
}
