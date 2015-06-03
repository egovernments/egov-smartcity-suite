package org.egov.infra.persistence.entity;

import java.io.Serializable;

public interface Persistable<ID extends Serializable> extends Serializable {
    
    ID getId();

    void setId(ID id);
    
    Long getVersion();
    
    default boolean isNew() {
        return getId() == null;
    }
}
