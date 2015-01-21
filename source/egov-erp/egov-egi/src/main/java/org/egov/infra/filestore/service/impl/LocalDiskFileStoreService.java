package org.egov.infra.filestore.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.filestore.FileStoreMap;
import org.egov.infra.filestore.service.FileStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalDiskFileStoreService implements FileStoreService {
    private static final Logger LOG = LoggerFactory.getLogger(LocalDiskFileStoreService.class);

    private final String fileStorePath;

    public LocalDiskFileStoreService(final String fileStorePath) {
	this.fileStorePath = fileStorePath;
    }

    @Override
    public FileStoreMap store(final File sourceFile) {
	try {
	    final FileStoreMap fileMapper = new FileStoreMap(UUID.randomUUID(), sourceFile.getName());
	    Files.copy(sourceFile.toPath(), createNewFilePath(fileMapper));
	    return fileMapper;
	} catch (final IOException e) {
	    throw new EGOVRuntimeException("Error occurred while storing files at " + fileStorePath, e);
	}
    }

    @Override
    public FileStoreMap store(final InputStream sourceFileStream) {
	try {
	    final FileStoreMap fileMapper = new FileStoreMap(UUID.randomUUID(), "noname");
	    Files.copy(sourceFileStream, createNewFilePath(fileMapper));
	    return fileMapper;
	} catch (final IOException e) {
	    throw new EGOVRuntimeException("Error occurred while storing files at " + fileStorePath, e);
	}
    }

    @Override
    public Set<FileStoreMap> store(final Set<File> files) {
	return files.stream().map((file) -> store(file)).collect(Collectors.toSet());
    }

    @Override
    public Set<FileStoreMap> storeStreams(final Set<InputStream> fileStreams) {
	return fileStreams.stream().map((fileStream) -> store(fileStream)).collect(Collectors.toSet());
    }

    @Override
    public File fetch(final FileStoreMap fileMapper) {
	final Path path = Paths.get(fileStorePath);
	if (!Files.exists(path))
	    throw new EGOVRuntimeException("File Store does not exist at Path : "+fileStorePath);
	return Paths.get(path.toString() + System.getProperty("file.separator")+ fileMapper.getUniqueId().toString()).toFile();
    }

    @Override
    public Set<File> fetchAll(final Set<FileStoreMap> fileMappers) {
	return fileMappers.stream().map((fileMapper) -> fetch(fileMapper)).collect(Collectors.toSet());
    }

    private Path createNewFilePath(final FileStoreMap fileMapper) throws IOException {
	final Path path = Paths.get(fileStorePath);
	if (!Files.exists(path)) {
	    LOG.info("File Store Directory {} not found, creating one", fileStorePath);
	    Files.createDirectory(path);
	    LOG.info("Created File Store Directory {}", fileStorePath);
	}
	return Paths.get(path.toString() + System.getProperty("file.separator")+ fileMapper.getUniqueId());
    }
}
