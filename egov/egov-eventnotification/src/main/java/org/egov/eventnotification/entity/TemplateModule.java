package org.egov.eventnotification.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "egevntnotification_module")
@SequenceGenerator(name = TemplateModule.SEQ_EGEVNTNOTIFICATION_MODULE, sequenceName = TemplateModule.SEQ_EGEVNTNOTIFICATION_MODULE, allocationSize = 1)
public class TemplateModule implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 7392974152674723576L;

    public static final String SEQ_EGEVNTNOTIFICATION_MODULE = "seq_egevntnotification_module";

    @Id
    @GeneratedValue(generator = SEQ_EGEVNTNOTIFICATION_MODULE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @SafeHtml
    @Length(max = 100)
    private String name;

    @NotNull
    private boolean active;

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
