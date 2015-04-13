package org.egov.infra.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.egov.infra.persistence.entity.AbstractPersistable;

@Entity
@Table(name="eg_favourites",uniqueConstraints=
@UniqueConstraint(columnNames={"userId", "actionId"}))
public class Favourites extends AbstractPersistable<Long> {
   
    private static final long serialVersionUID = 8966137226966715994L;
    
    private Long userId;
    private Integer actionId;
    private String name;
    private String contextRoot;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(final Integer actionId) {
        this.actionId = actionId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getContextRoot() {
        return contextRoot;
    }

    public void setContextRoot(final String contextRoot) {
        this.contextRoot = contextRoot;
    }

}
