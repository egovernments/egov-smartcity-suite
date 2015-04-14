package org.egov.infra.admin.master.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Represents the top classification for Boundary
 * 
 * @author nayeem
 * @see Boundary
 * @see BoundaryType
 */

@Entity
@Unique(
        id = "id",
        tableName = "eg_hierarchy_type", 
        fields = {"name", "code"}, 
        columnName = {"name", "code"}
)
@Table(name = "eg_hierarchy_type")
public class HierarchyType extends AbstractAuditable<User, Long> {

    private static final long serialVersionUID = -7131667806935923935L;

    @NotBlank
    @Length(max = 128)
    private String name;

    @NotBlank
    @Length(max = 50)
    private String code;
    
    @Length(max = 256)
    private String localName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }
}
