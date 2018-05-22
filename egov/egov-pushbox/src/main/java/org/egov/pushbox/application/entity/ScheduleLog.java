package org.egov.pushbox.application.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.persistence.entity.AbstractAuditable;

@Entity
@Table(name = "egevntnotification_schedule_log")
@SequenceGenerator(name = ScheduleLog.SEQ_SCHEDULE_LOG, sequenceName = ScheduleLog.SEQ_SCHEDULE_LOG, allocationSize = 1)
public class ScheduleLog extends AbstractAuditable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String SEQ_SCHEDULE_LOG = "seq_egevntnotification_schedule_log";

    @Id
    @GeneratedValue(generator = SEQ_SCHEDULE_LOG, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "filestore")
    private FileStoreMapper filestore;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public FileStoreMapper getFilestore() {
        return filestore;
    }

    public void setFilestore(FileStoreMapper filestore) {
        this.filestore = filestore;
    }
}
