package org.egov.infra.filestore;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.lib.rjbac.user.UserImpl;
import org.hibernate.annotations.Type;

@Table(name = "eg_filestoremap")
@Entity
public class FileStoreMapper extends AbstractAuditable<UserImpl, Serializable> {
    private static final long serialVersionUID = -2997164207274266823L;

    @NotNull
    @Type(type = "uuid-binary")
    @Column(length = 60, unique = true, nullable = false)
    private UUID fileStoreId;

    @NotNull
    @Column
    private String fileName;

    private String contentType;

    @Column(name = "ref_id")
    private String referenceId;

    private String moduleName;
    
    private FileStoreMapper() {
        // For Hibernate
    }

    public FileStoreMapper(final UUID fileStoreId, final String fileName, final String moduleName) {
        this.fileStoreId = fileStoreId;
        this.fileName = fileName;
        this.moduleName = moduleName;
    }

    public UUID getFileStoreId() {
        return fileStoreId;
    }

    public void setFileStoreId(final UUID fileStoreId) {
        this.fileStoreId = fileStoreId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(final String referenceId) {
        this.referenceId = referenceId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
