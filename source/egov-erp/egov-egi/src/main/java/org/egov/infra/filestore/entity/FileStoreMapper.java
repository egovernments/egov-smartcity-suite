package org.egov.infra.filestore.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractPersistable;

@Table(name = "eg_filestoremap")
@Entity
public class FileStoreMapper extends AbstractPersistable<Long> {
    private static final long serialVersionUID = -2997164207274266823L;

    @NotNull
    @Column(length = 36, unique = true, nullable = false)
    private String fileStoreId;

    @NotNull
    private String fileName;

    private String contentType;
    
    protected FileStoreMapper() {
        // For Hibernate
    }

    public FileStoreMapper(final String fileStoreId, final String fileName) {
        this.fileStoreId = fileStoreId;
        this.fileName = fileName;
    }

    public String getFileStoreId() {
        return fileStoreId;
    }

    public void setFileStoreId(final String fileStoreId) {
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
}