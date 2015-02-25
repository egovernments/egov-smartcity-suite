package org.egov.infra.persistence.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.DocumentId;

/**
 * Abstract base class for entities. Allows parameterization of id type, chooses
 * auto-generation and implements {@link #equals(Object)} and
 * {@link #hashCode()} based on that id.
 */
@MappedSuperclass
public abstract class AbstractPersistable<PK extends Serializable> implements Serializable {

    private static final long serialVersionUID = -477198900757851804L;

    @Id
    @GenericGenerator(name = "seq_id", strategy = "org.egov.infra.persistence.utils.SequenceIdGenerator")
    @GeneratedValue(generator = "seq_id")
    @DocumentId
    private PK id;

    public PK getId() {
        return id;
    }

    protected void setId(final PK id) {
        this.id = id;
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