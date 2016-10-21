package org.egov.council.entity;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import static org.egov.council.entity.CouncilDesignation.SEQ_COUNCILDESIGNATION;

@Entity
@Unique(fields = {"name"}, enableDfltMsg = true)
@Table(name = "egcncl_designation")
@SequenceGenerator(name = SEQ_COUNCILDESIGNATION, sequenceName = SEQ_COUNCILDESIGNATION, allocationSize = 1)
public class CouncilDesignation extends AbstractAuditable {

    public static final String SEQ_COUNCILDESIGNATION = "seq_egcncl_designation";
    private static final long serialVersionUID = -1288716453376474299L;
    @Id
    @GeneratedValue(generator = SEQ_COUNCILDESIGNATION, strategy = GenerationType.SEQUENCE)
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
