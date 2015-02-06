package org.egov.infra.filestore.service;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

import org.egov.infra.filestore.FileStoreMapper;

public interface FileStoreService {

    FileStoreMapper store(File file);

    FileStoreMapper store(InputStream fileStream);

    Set<FileStoreMapper> store(Set<File> files);

    Set<FileStoreMapper> storeStreams(Set<InputStream> fileStreams);

    File fetch(FileStoreMapper fileMappers);

    Set<File> fetchAll(Set<FileStoreMapper> fileMappers);
}
