package org.egov.infra.workflow.multitenant.model;

import java.util.List;

public class Attribute {

    private Boolean variable;
    private String code;
    private String datatype;
    private Boolean required;
    private String datatype_description;
    private List values;

    public Boolean getVariable() {
        return variable;
    }

    public void setVariable(Boolean variable) {
        this.variable = variable;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getDatatype_description() {
        return datatype_description;
    }

    public void setDatatype_description(String datatype_description) {
        this.datatype_description = datatype_description;
    }

    public List getValues() {
        return values;
    }

    public void setValues(List values) {
        this.values = values;
    }

    
}