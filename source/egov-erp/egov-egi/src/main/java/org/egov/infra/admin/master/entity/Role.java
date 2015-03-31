package org.egov.infra.admin.master.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.lib.rrbac.model.Action;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "eg_role")
public class Role extends AbstractAuditable<User, Long> {

    private static final long serialVersionUID = 7034114743461088547L;

    @NotBlank
    @SafeHtml
    @Length(max = 32)
    private String name;

    @SafeHtml
    @Length(max = 150)
    private String description;

    
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



}
