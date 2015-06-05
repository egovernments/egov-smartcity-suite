package org.egov.infra.admin.master.entity;

import java.util.Date;

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
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.search.domain.Searchable;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "eg_appconfig_values")
@SequenceGenerator(name = AppConfigValues.SEQ_APPCONFIG_VALUE, sequenceName = AppConfigValues.SEQ_APPCONFIG_VALUE, allocationSize = 1)
public class AppConfigValues extends AbstractAuditable {

	private static final long serialVersionUID = 1L;
	public static final String SEQ_APPCONFIG_VALUE = "SEQ_EG_APPCONFIG_VALUES";

    @Expose
    @DocumentId
    @Id
    @GeneratedValue(generator = SEQ_APPCONFIG_VALUE, strategy = GenerationType.SEQUENCE)
    private Long id;
    
	@Column(name = "value")
	 @NotBlank
	  @SafeHtml
	  @Length(max = 4000)
	 @Searchable
	private String value;

	@NotNull
	@Column(name = "effective_from")
	@Temporal(TemporalType.TIMESTAMP)
	private Date effectiveFrom;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "key_id")
	private AppConfig key;

		
	public Date getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public AppConfig getKey() {
		return key;
	}

	public void setKey(final AppConfig key) {
		this.key = key;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	  @Override
	    public int hashCode() {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result + (id == null ? 0 : id.hashCode());
	        result = prime * result + (value == null ? 0 : value.hashCode());
	        return result;
	    }

	    @Override
	    public boolean equals(final Object obj) {
	        if (this == obj)
	            return true;
	        if (obj == null)
	            return false;
	        if (getClass() != obj.getClass())
	            return false;
	        final AppConfigValues other = (AppConfigValues) obj;
	        if (id == null) {
	            if (other.id != null)
	                return false;
	        } else if (!id.equals(other.id))
	            return false;
	        if (value == null) {
	            if (other.value != null)
	                return false;
	        } else if (!value.equals(other.value))
	            return false;
	        return true;
	    }
}