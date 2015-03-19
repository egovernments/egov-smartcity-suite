package org.egov.infra.admin.master.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "eg_role")
public class Role extends AbstractAuditable<UserImpl, Long> {

    private static final long serialVersionUID = 7034114743461088547L;

    @NotBlank
    @SafeHtml
    @Length(max = 32)
    private String name;

    @SafeHtml
    @Length(max = 150)
    private String description;

    @SafeHtml
    @Length(max = 64)
    private String localName;

    @SafeHtml
    @Length(max = 150)
    private String localDescription;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(final String localName) {
        this.localName = localName;
    }

    public String getLocalDescription() {
        return localDescription;
    }

    public void setLocalDescription(final String localDescription) {
        this.localDescription = localDescription;
    }

}
