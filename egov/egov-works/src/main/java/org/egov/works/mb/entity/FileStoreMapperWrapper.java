package org.egov.works.mb.entity;

import java.util.List;

import org.egov.infra.filestore.entity.FileStoreMapper;

public class FileStoreMapperWrapper {
    private List<FileStoreMapper> fileStoreMappers;

    public List<FileStoreMapper> getFileStoreMappers() {
        return fileStoreMappers;
    }

    public void setFileStoreMappers(List<FileStoreMapper> fileStoreMappers) {
        this.fileStoreMappers = fileStoreMappers;
    }
}
