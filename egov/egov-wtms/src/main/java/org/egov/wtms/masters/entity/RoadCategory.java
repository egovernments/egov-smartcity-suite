package org.egov.wtms.masters.entity;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "egwtr_road_category")
@SequenceGenerator(name = RoadCategory.SEQ_ROADCATEGORY, sequenceName = RoadCategory.SEQ_ROADCATEGORY, allocationSize = 1)
public class RoadCategory extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 1037497891990828671L;

    public static final String SEQ_ROADCATEGORY = "SEQ_EGWTR_ROAD_CATEGORY";

    @Id
    @GeneratedValue(generator = SEQ_ROADCATEGORY, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @SafeHtml
    @Length(max = 50)
    @Column(name = "name", unique = true)
    private String name;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

}
