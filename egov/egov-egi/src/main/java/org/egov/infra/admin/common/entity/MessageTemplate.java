package org.egov.infra.admin.common.entity;

import java.util.Locale;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.egov.infra.persistence.entity.Persistable;
import org.hibernate.search.annotations.DocumentId;

@Entity
@Table(name = "eg_messagetemplate")
@Cacheable
@SequenceGenerator(name = MessageTemplate.SEQ_MESSAGETEMPLATE, sequenceName = MessageTemplate.SEQ_MESSAGETEMPLATE, allocationSize = 1)
public class MessageTemplate implements Persistable<Long> {
    private static final long serialVersionUID = 3650406178933970435L;
    public static final String SEQ_MESSAGETEMPLATE = "SEQ_EG_MESSAGETEMPLATE";
    
    @DocumentId
    @Id
    @GeneratedValue(generator = SEQ_MESSAGETEMPLATE, strategy = GenerationType.SEQUENCE)
    private Long id;

    private String templateName;
    private String template;
    private String locale;

    @Version
    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(final String templateName) {
        this.templateName = templateName;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(final String template) {
        this.template = template;
    }

    public Locale getLocale() {
        return new Locale(locale);
    }

    public void setLocale(final String locale) {
        this.locale = locale;
    }

    public Long getVersion() {
        return version;
    }

}
