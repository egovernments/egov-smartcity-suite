package org.egov.infra.admin.common.entity;

import java.util.Locale;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractPersistable;

@Entity
@Table(name = "eg_messagetemplate")
@Cacheable
public class MessageTemplate extends AbstractPersistable<Long> {
    private static final long serialVersionUID = 3650406178933970435L;

    private String templateName;
    private String template;
    private String locale;

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

}
