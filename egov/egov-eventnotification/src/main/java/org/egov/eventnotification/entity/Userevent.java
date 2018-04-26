package org.egov.eventnotification.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.AbstractPersistable;

@Entity
@Table(name = "eg_userevent")
@SequenceGenerator(name = Userevent.SEQ_EG_USEREVENT, sequenceName = Event.SEQ_EG_EVENT, allocationSize = 1)
public class Userevent extends AbstractPersistable<Long> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String SEQ_EG_USEREVENT = "SEQ_EG_USEREVENT";

    @Id
    @GeneratedValue(generator = SEQ_EG_USEREVENT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private User userid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventid")
    private Event eventid;

    private Long version;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public User getUserid() {
        return userid;
    }

    public void setUserid(User userid) {
        this.userid = userid;
    }

    public Event getEventid() {
        return eventid;
    }

    public void setEventid(Event eventid) {
        this.eventid = eventid;
    }

    @Override
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
