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

import org.egov.infra.admin.master.entity.User;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "egevntnotification_drafts")
@SequenceGenerator(name = NotificationDrafts.SEQ_EGEVENTNOTIFICATION_DRAFTS, sequenceName = NotificationDrafts.SEQ_EGEVENTNOTIFICATION_DRAFTS, allocationSize = 1)
public class NotificationDrafts {

    public static final String SEQ_EGEVENTNOTIFICATION_DRAFTS = "SEQ_EGEVENTNOTIFICATION_DRAFTS";
    
    @Id
    @GeneratedValue(generator = SEQ_EGEVENTNOTIFICATION_DRAFTS, strategy = GenerationType.SEQUENCE)
    private Long id;
    
    private String name;

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
    private String message;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdBy")
    @CreatedBy
    private User createdBy;

    @CreatedDate
    private Long createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updatedby")
    @LastModifiedBy
    private User updatedby;

    @LastModifiedDate
    private Long updatedDate;

    	
	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Long getId() {
        return id;
    }

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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(User updatedby) {
        this.updatedby = updatedby;
    }
}
