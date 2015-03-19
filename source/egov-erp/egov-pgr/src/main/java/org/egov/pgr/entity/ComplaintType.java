package org.egov.pgr.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.search.domain.Searchable;
import org.egov.search.util.Serializer;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.json.simple.JSONObject;

@Entity
@Table(name = "pgr_complainttype", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@Searchable
public class ComplaintType extends AbstractPersistable<Long> {
    private static final long serialVersionUID = 8904645810221559541L;
    
    public static final String COMPLAINT_STATUS_COMPLETED = "COMPLETED";
    public static final String COMPLAINT_STATUS_REJECTED = "REJECTED";
    public static final String COMPLAINT_STATUS_WITHDRAWN = "WITHDRAWN";

    @NotBlank
    @SafeHtml
    @Length(max = 150)
    @Column(name = "name")
    @Searchable
    private String name;

    @Valid
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    @Searchable
    private DepartmentImpl department;

    @Column(name = "location_required")
    private boolean locationRequired;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public DepartmentImpl getDepartment() {
        return department;
    }

    public void setDepartment(final DepartmentImpl department) {
        this.department = department;
    }

    public boolean isLocationRequired() {
        return locationRequired;
    }

    public void setLocationRequired(final boolean locationRequired) {
        this.locationRequired = locationRequired;
    }

    public JSONObject toJsonObject() {
        return Serializer.fromJson(Serializer.toJson(this), JSONObject.class);
    }
}