package org.egov.commons;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Unique(id = "id", tableName = "EG_REGIONALHEIRARCHY", fields = { "name","code" }, columnName = {"name","code" }, enableDfltMsg = true)
@Table(name = "EG_REGIONALHEIRARCHY")
@SequenceGenerator(name = RegionalHeirarchy.SEQ_REGIONALHEIRARCHY, sequenceName = RegionalHeirarchy.SEQ_REGIONALHEIRARCHY, allocationSize = 1)
public class RegionalHeirarchy extends AbstractAuditable {
   
    private static final long serialVersionUID = -6147843882272796650L;

    public static final String SEQ_REGIONALHEIRARCHY = "seq_eg_regionlaHeirarchy";

    @Id
    @GeneratedValue(generator = SEQ_REGIONALHEIRARCHY, strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Length(max = 512)
    @SafeHtml
    @NotNull
    private String name;
    
    @NotNull
    private Long code;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    @Fetch(value = FetchMode.SELECT)
    private RegionalHeirarchy parent;
    
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private RegionalHeirarchyType type;

    private boolean isHistory;

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

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public RegionalHeirarchy getParent() {
        return parent;
    }

    public void setParent(RegionalHeirarchy parent) {
        this.parent = parent;
    }

    public RegionalHeirarchyType getType() {
        return type;
    }

    public void setType(RegionalHeirarchyType type) {
        this.type = type;
    }

    public boolean isHistory() {
        return isHistory;
    }

    public void setHistory(boolean isHistory) {
        this.isHistory = isHistory;
    }

   
}
