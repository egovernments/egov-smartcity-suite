package org.egov.eventnotification.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "egevntnotification_drafts")
@SequenceGenerator(name = NotificationDrafts.SEQ_EGEVENTNOTIFICATION_DRAFTS, sequenceName = NotificationDrafts.SEQ_EGEVENTNOTIFICATION_DRAFTS, allocationSize = 1)
public class NotificationDrafts extends AbstractAuditable {

    /**
     *
     */
    private static final long serialVersionUID = 6211626862831989672L;
    public static final String SEQ_EGEVENTNOTIFICATION_DRAFTS = "SEQ_EGEVENTNOTIFICATION_DRAFTS";

    @Id
    @GeneratedValue(generator = SEQ_EGEVENTNOTIFICATION_DRAFTS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @SafeHtml
    private String name;

    @SafeHtml
    private String type;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id")
    private TemplateModule module;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private ModuleCategory category;

    @Column(name = "notification_message")
    @SafeHtml
    private String message;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TemplateModule getModule() {
        return module;
    }

    public void setModule(TemplateModule module) {
        this.module = module;
    }

    public ModuleCategory getCategory() {
        return category;
    }

    public void setCategory(ModuleCategory category) {
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
