package org.egov.infra.admin.common.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.AbstractPersistable;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;

@Entity
@Table(name = "eg_identityrecovery")
@SequenceGenerator(name = IdentityRecovery.SEQ_IDENTITYRECOVERY, sequenceName = IdentityRecovery.SEQ_IDENTITYRECOVERY, allocationSize = 1)
public class IdentityRecovery extends AbstractPersistable<Long> {

    private static final long serialVersionUID = -1636403427637104041L;
    public static final String SEQ_IDENTITYRECOVERY = "SEQ_EG_IDENTITYRECOVERY";

    @DocumentId
    @Id
    @GeneratedValue(generator = SEQ_IDENTITYRECOVERY, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    private String token;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userid", nullable = false, updatable = false)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiry;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

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
