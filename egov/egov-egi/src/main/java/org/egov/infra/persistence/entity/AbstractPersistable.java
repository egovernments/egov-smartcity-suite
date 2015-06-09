package org.egov.infra.persistence.entity;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class AbstractPersistable<PK extends Serializable> implements Serializable {

    private static final long serialVersionUID = 7094572260034458544L;

    @Version
    private Long version;

    protected abstract void setId(PK id);
    
    public abstract PK getId();
    
    public Long getVersion() {
        return version;
    }

    public boolean isNew() {
        return null == getId();
    }

    @Override
    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
    }

    @Override
    public boolean equals(final Object obj) {
        if (null == obj)
            return false;

        if (this == obj)
            return true;

        if (!getClass().equals(obj.getClass()))
            return false;

        final AbstractPersistable<?> that = (AbstractPersistable<?>) obj;

        return null == this.getId() ? false : this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += null == getId() ? 0 : getId().hashCode() * 31;
        return hashCode;
    }
}
