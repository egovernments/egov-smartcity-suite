/**
 * 
 */
package org.egov.eis.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.commons.ObjectType;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.pims.commons.Position;
import org.egov.search.domain.Searchable;

/**
 * @author Vaibhav.K
 *
 */
@Entity
@Table(name = "egeis_position_hierarchy")
@Searchable
public class PositionHierarchy extends AbstractAuditable<User,Long>{

    private static final long serialVersionUID = 8666462146278384384L;
    
    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="position_from")
    private Position fromPosition;
    
    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="position_to")
    private Position toPosition;
    
    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "object_type_id")
    private ObjectType objectType;
    
    @Column(name= "object_sub_type")
    private String objectSubType;

    public Position getFromPosition() {
        return fromPosition;
    }

    public void setFromPosition(Position fromPosition) {
        this.fromPosition = fromPosition;
    }

    public Position getToPosition() {
        return toPosition;
    }

    public void setToPosition(Position toPosition) {
        this.toPosition = toPosition;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public String getObjectSubType() {
        return objectSubType;
    }

    public void setObjectSubType(String objectSubType) {
        this.objectSubType = objectSubType;
    }
    
}
