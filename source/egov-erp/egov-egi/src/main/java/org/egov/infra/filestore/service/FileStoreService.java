package org.egov.infra.filestore.service;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

import org.egov.infra.filestore.FileStoreMap;

public interface FileStoreService {

    FileStoreMap store(File file);

    FileStoreMap store(InputStream fileStream);

    Set<FileStoreMap> store(Set<File> files);

    Set<FileStoreMap> storeStreams(Set<InputStream> fileStreams);

    File fetch(FileStoreMap fileMappers);

    Set<File> fetchAll(Set<FileStoreMap> fileMappers);
}
