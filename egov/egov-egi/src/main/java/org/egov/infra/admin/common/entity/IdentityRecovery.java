package org.egov.infra.admin.common.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.AbstractPersistable;
import org.joda.time.DateTime;

@Entity
@Table(name = "eg_identityrecovery")
public class IdentityRecovery extends AbstractPersistable<Long> {

    private static final long serialVersionUID = -1636403427637104041L;

    private String token;
    
    @ManyToOne(optional=false) 
    @JoinColumn(name="userid", nullable=false, updatable=false)
    private User user;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiry;

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public DateTime getExpiry() {
        return null == expiry ? null : new DateTime(expiry);
    }

    public void setExpiry(final Date expiry) {
        this.expiry = expiry;
    }

}
