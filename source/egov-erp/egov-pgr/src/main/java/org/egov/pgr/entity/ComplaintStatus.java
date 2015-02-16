package org.egov.pgr.entity;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.search.domain.Searchable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "pgr_complaintstatus")
@Searchable
public class ComplaintStatus extends AbstractPersistable<Long> {
    private static final long serialVersionUID = -9009821412847211632L;
    @NotNull
    @Searchable
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
