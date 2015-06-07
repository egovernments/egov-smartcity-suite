package org.egov.infra.admin.master.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.CompositeUnique;
import org.egov.search.domain.Searchable;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import com.google.gson.annotations.Expose;

@Entity
@CompositeUnique(
        id = "id",
        tableName = "eg_appconfig", 
        compositefields = {"keyName", "module"}, 
        compositecolumnName = {"key_name","module"},
        enableDfltMsg=true ,
        message = "KeyName and Module combination allready exists"
)


@Table( name = "eg_appconfig")
@SequenceGenerator(name = AppConfig.SEQ_APPCONFIG, sequenceName = AppConfig.SEQ_APPCONFIG, allocationSize = 1)
public class AppConfig extends AbstractAuditable {

	private static final long serialVersionUID = 8904645810221559541L;

	public static final String SEQ_APPCONFIG = "SEQ_EG_APPCONFIG";

    @Expose
    @DocumentId
    @Id
    @GeneratedValue(generator = SEQ_APPCONFIG, strategy = GenerationType.SEQUENCE)
    private Long id;
	

	@NotBlank
	@SafeHtml
	@Length(max = 250)
	@Column(name = "key_name")
	@Searchable
	private String keyName;

	
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "module", nullable = false)
	@Searchable
	private Module module;



	@NotBlank
	@SafeHtml
	@Length(max = 250)
	@Searchable
	@Column(name = "description")
	private String description;

	@OneToMany(mappedBy = "key", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval=true)
	private List<AppConfigValues> appDataValues = new ArrayList<AppConfigValues>(
			0);

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public Module getModule() {

		return module;
	}

	public void setModule(final Module module) {
		this.module = module;
	}

	public void addAppDataValues(AppConfigValues appDataValues) {
		getAppDataValues().add(appDataValues);
	}

	public List<AppConfigValues> getAppDataValues() {
		return appDataValues;
	}

	public void setAppDataValues(final List<AppConfigValues> appDataValues) {
		this.appDataValues = appDataValues;
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
        result = prime * result + (keyName == null ? 0 : keyName.hashCode());
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
        final AppConfig other = (AppConfig) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (keyName == null) {
            if (other.keyName != null)
                return false;
        } else if (!keyName.equals(other.keyName))
            return false;
        return true;
    }
	


}