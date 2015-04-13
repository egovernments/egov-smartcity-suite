package org.egov.infra.admin.master.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.egov.infra.common.constants.CommonConstants;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.search.domain.Searchable;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.json.simple.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Unique(
        id = "id",
        tableName = "eg_boundary", 
        fields = {"name"}, 
        columnName = {"name"}
)
@Table(name = "EG_BOUNDARY")
@Searchable
@NamedQuery(name = "Boundary.findBoundariesByBoundaryType", query = "select b from Boundary b where b.boundaryType.id = :boundaryTypeId")
public class Boundary extends AbstractAuditable<User, Long> {

    private static final long serialVersionUID = -224771387323975327L;
    
    @Length(max = 512)
    private String name;
    
    private Long boundaryNum;
    
    @Valid
    @ManyToOne
    @JoinColumn(name = "boundaryType", updatable = false)
    private BoundaryType boundaryType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    @Fetch(value = FetchMode.SELECT)
    private Boundary parent;
    
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "parent")
    @Fetch(value = FetchMode.SELECT)
    private Set<Boundary> children = new HashSet<Boundary>();
 
    @DateFormat
    @DateTimeFormat(pattern = CommonConstants.DATE_FORMAT_DDMMYYYY)
    private Date fromDate;
    
    private Date toDate;
    
    private boolean isHistory;
    private Long bndryId;
    private String localName;
    
    private Float longitude;
    private Float latitude;
    
    @Length(max = 32)
    private String materializedPath;
    
    @Transient
    @Searchable(name = "detail")
    private JSONObject boundaryJson;
    
    @Transient
    private Long parentBoundaryNum;

    @Transient
    private Long hierarchyTypeId;
    
    @Transient
    private Long boundaryTypeId;
    
    @Valid
    @Transient
    private CityWebsite cityWebsite = new CityWebsite();
    
    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String boundaryNameLocal) {
        this.localName = boundaryNameLocal;
    }

    public Boundary getParent() {
        return parent;
    }

    public void setParent(Boundary parent) {
        this.parent = parent;
    }

    public Set getChildren() {
        return children;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public boolean isLeaf() {
        return (getChildren().isEmpty());
    }

    public BoundaryType getBoundaryType() {
        return boundaryType;
    }

    public Long getBoundaryNum() {
        return boundaryNum;
    }

    public void addChild(Boundary arg1) {
        arg1.setParent(this);
        children.add(arg1);
    }

    public void deleteChild(Boundary arg1) {
        children.remove(arg1);
    }

    public void addChildren(Set arg1) {
        children.addAll(arg1);

    }

    public void deleteChildren(Set arg1) {
        children.removeAll(arg1);
    }

    public boolean isRoot() {
        return (getParent() == null ? true : false);
    }

    public void setBoundaryType(BoundaryType boundaryType) {
        this.boundaryType = boundaryType;
    }

    public void setBoundaryNum(Long number) {

        this.boundaryNum = number;
    }

    public void setChildren(Set arg1) {
        children = arg1;

    }

    public Long getParentBoundaryNum() {
        return parentBoundaryNum;
    }

    public void setParentBoundaryNum(Long parentBoundaryNum) {
        this.parentBoundaryNum = parentBoundaryNum;
    }

    public Long getHierarchyTypeId() {
        return hierarchyTypeId;
    }

    public void setHierarchyTypeId(Long hierarchyTypeId) {
        this.hierarchyTypeId = hierarchyTypeId;
    }

    public Long getBoundaryTypeId() {
        return boundaryTypeId;
    }

    public void setBoundaryTypeId(Long boundaryTypeId) {
        this.boundaryTypeId = boundaryTypeId;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public boolean isHistory() {
        return this.isHistory;
    }

    public void setHistory(boolean isHistory) {
        this.isHistory = isHistory;
    }

    public Long getBndryId() {
        return bndryId;
    }

    public void setBndryId(Long bndryId) {
        this.bndryId = bndryId;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Boundary))
            return false;
        final Boundary other = (Boundary) obj;

        if (this.getId() != null && this.getId().equals(other.getId()))
            return true;
        // Boundary number can be null
        if (this.getBoundaryNum() != null && !this.getBoundaryNum().equals(other.getBoundaryNum())) {
            return false;
        }
        if (!this.getBoundaryType().equals(other.getBoundaryType())) {
            return false;
        }
        if (this.getName() != null && !this.getName().equals(other.getName())) {
            return false;
        }
        if (this.getLocalName() != null && !(this.getLocalName().equals(other.getLocalName()))) {
            return false;
        }
        if (this.getParent() != null && !this.getParent().equals(other.getParent())) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int hashCode = 0;

        // assumes boundary number is never null.
        if (getBoundaryNum() != null)
            hashCode = hashCode + getBoundaryNum().hashCode();

        // assumes boundary name is never null.
        if (getName() != null)
            hashCode = hashCode + getName().hashCode();

        // assumes boundary type name is never null.
        if (getBoundaryType() != null && getBoundaryType().getName() != null)
            hashCode = hashCode + getBoundaryType().getName().hashCode();

        // assumes top boundary id is never null.
        Long i = getTopLevelBndryId(this);
        if (i != null)
            hashCode = hashCode + i.hashCode();

        return hashCode;

    }

    private Long getTopLevelBndryId(Boundary bn) {
        Boundary localBndry = bn;
        if (localBndry == null)
            return null;

        while (localBndry.getParent() != null) {
            localBndry = localBndry.getParent();
        }

        return localBndry.getId();
    }

    public String getMaterializedPath() {
        return materializedPath;
    }

    public void setMaterializedPath(String materializedPath) {
        this.materializedPath = materializedPath;
    }
    
    public CityWebsite getCityWebsite() {
        return cityWebsite;
    }

    public void setCityWebsite(CityWebsite cityWebsite) {
        this.cityWebsite = cityWebsite;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Boundary [id=").append(getId())
                .append(", boundaryType=").append(boundaryType)
                .append(", name: ").append(name)
                .append(", number=").append(boundaryNum)
                .append(", parentBoundaryNum=").append(parentBoundaryNum)
                .append(", isHistory=").append(isHistory)
                .append(", materializedPath: ").append(materializedPath)
                .append("]").toString();
    }

    public JSONObject getBoundaryJson() {
        Map<String, Object> map = new HashMap<>();
        this.addNameAndValue(map);
        return new JSONObject(map);
    }

    public void addNameAndValue(Map<String, Object> map) {
        map.put(this.getBoundaryType().getName(), this.getName());
        if (this.getParent() != null) {
            ((Boundary) this.getParent()).addNameAndValue(map);
        }
    }
}
