package org.egov.infra.admin.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.search.domain.Searchable;
import org.hibernate.validator.constraints.Length;

/**
 * @author subhash
 */

@Entity
//@Unique(id = "id", tableName = "eg_department", columnName = { "name", "code" }, fields = { "name", "code" })
@Table(name = "eg_department")
@Searchable
public class Department extends AbstractAuditable<User, Long> {
    private static final long serialVersionUID = 1L;

    @Length(max = 128)
    @Column(name = "name")
    @Searchable(name="name")
    private String name;

    @NotNull
    @Length(max = 128)
    @Column(name = "code")
    private String code;

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

}
