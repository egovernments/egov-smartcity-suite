package org.egov.council.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.search.domain.Searchable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Unique(id = "id", tableName = "egcncl_qualification", fields = { "name" }, columnName = { "name" }, enableDfltMsg = true)
@Table(name = "egcncl_qualification")
@Searchable
@SequenceGenerator(name = CouncilQualification.SEQ_COUNCILQUALIFICATION, sequenceName = CouncilQualification.SEQ_COUNCILQUALIFICATION, allocationSize = 1)
public class CouncilQualification extends AbstractAuditable {

    private static final long serialVersionUID = -8211020959691716235L;

    public static final String SEQ_COUNCILQUALIFICATION = "seq_egcncl_qualification";

    @Id
    @GeneratedValue(generator = SEQ_COUNCILQUALIFICATION, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Length(min = 2, max = 100)
    private String name;

    @NotNull
    @Length(max = 20)
    @Column(name = "code", updatable = false)
    private String code;
        
    @NotNull
    private Boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
