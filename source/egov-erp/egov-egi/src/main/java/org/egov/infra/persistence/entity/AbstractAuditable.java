package org.egov.infra.persistence.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;

@MappedSuperclass
public abstract class AbstractAuditable<U, PK extends Serializable> extends AbstractPersistable<PK> {

    private static final long serialVersionUID = 8330295040331880486L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="createdBy")
    private U createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="lastModifiedBy")
    private U lastModifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    public U getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(final U createdBy) {
	this.createdBy = createdBy;
    }

    public DateTime getCreatedDate() {
	return null == createdDate ? null : new DateTime(createdDate);
    }

    public void setCreatedDate(final DateTime createdDate) {
	this.createdDate = null == createdDate ? null : createdDate.toDate();
    }

    public U getLastModifiedBy() {
	return lastModifiedBy;
    }

    public void setLastModifiedBy(final U lastModifiedBy) {
	this.lastModifiedBy = lastModifiedBy;
    }

    public DateTime getLastModifiedDate() {
	return null == lastModifiedDate ? null : new DateTime(lastModifiedDate);
    }

    public void setLastModifiedDate(final DateTime lastModifiedDate) {
	this.lastModifiedDate = null == lastModifiedDate ? null
		: lastModifiedDate.toDate();
    }
}
