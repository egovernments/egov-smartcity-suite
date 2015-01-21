package org.egov.infra.filestore;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.AbstractAuditable;
import org.egov.lib.rjbac.user.UserImpl;
import org.hibernate.annotations.Type;

@Table(name = "eg_filestoremap")
@Entity
public class FileStoreMap extends AbstractAuditable<UserImpl, Serializable> {
    private static final long serialVersionUID = -2997164207274266823L;

    @NotNull
    @Type(type="uuid-binary")
    @Column(length = 60, unique = true, nullable = false)
    private UUID uniqueId;

    @NotNull
    @Column
    private String name;

    private FileStoreMap() {
	// For Hibernate
    }

    public FileStoreMap(final UUID uniqueId, final String name) {
	this.uniqueId = uniqueId;
	this.name = name;
    }

    public UUID getUniqueId() {
	return uniqueId;
    }

    public void setUniqueId(final UUID uniqueId) {
	this.uniqueId = uniqueId;
    }

    public String getName() {
	return name;
    }

    public void setName(final String name) {
	this.name = name;
    }
}
