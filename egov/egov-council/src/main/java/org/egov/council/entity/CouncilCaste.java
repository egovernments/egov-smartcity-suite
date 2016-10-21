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

import static org.egov.council.entity.CouncilCaste.SEQ_COUNCILCASTE;

@Entity
@Unique(fields = {"code", "name"}, enableDfltMsg = true)
@Table(name = "egcncl_caste")
@SequenceGenerator(name = SEQ_COUNCILCASTE, sequenceName = SEQ_COUNCILCASTE, allocationSize = 1)
public class CouncilCaste extends AbstractAuditable {

    public static final String SEQ_COUNCILCASTE = "seq_egcncl_caste";
    private static final long serialVersionUID = 6603829370535410709L;
    @Id
    @GeneratedValue(generator = SEQ_COUNCILCASTE, strategy = GenerationType.SEQUENCE)
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
