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
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("localDiskFileStoreService")
public class LocalDiskFileStoreService implements FileStoreService {

    private static final Logger LOG = LoggerFactory.getLogger(LocalDiskFileStoreService.class);
    
    private String fileStoreBaseDir;
    
    @Autowired
    public LocalDiskFileStoreService(@Value("#{egovErpProperties.fileStoreBaseDir}")String fileStoreBaseDir) {
        if (fileStoreBaseDir.isEmpty())
            this.fileStoreBaseDir = System.getProperty("user.home")+File.separator+"egovfilestore";
        else 
            this.fileStoreBaseDir = fileStoreBaseDir;
    }

    @Override
    public FileStoreMapper store(final File sourceFile, final String moduleName) {
        try {
            final FileStoreMapper fileMapper = new FileStoreMapper(UUID.randomUUID().toString(), sourceFile.getName());
            final Path newFilePath = createNewFilePath(fileMapper, moduleName);
            Files.copy(sourceFile.toPath(), newFilePath);
            fileMapper.setContentType(Files.probeContentType(newFilePath));
            return fileMapper;
        } catch (final IOException e) {
            throw new EGOVRuntimeException(String.format("Error occurred while storing files at %s/%s", fileStoreBaseDir,moduleName), e);
        }
    }

    @Override
    public FileStoreMapper store(final InputStream sourceFileStream, final String moduleName) {
        try {
            final FileStoreMapper fileMapper = new FileStoreMapper(UUID.randomUUID().toString(),"noname");
            final Path newFilePath = createNewFilePath(fileMapper, moduleName);
            Files.copy(sourceFileStream, newFilePath);
            fileMapper.setContentType(Files.probeContentType(newFilePath));
            sourceFileStream.close();
            return fileMapper;
        } catch (final IOException e) {
            throw new EGOVRuntimeException(String.format("Error occurred while storing files at %s/%s", fileStoreBaseDir,moduleName), e);
        }
    }

    @Override
    public Set<FileStoreMapper> store(final Set<File> files, final String moduleName) {
        return files.stream().map((file) -> store(file, moduleName)).collect(Collectors.toSet());
    }

    @Override
    public Set<FileStoreMapper> storeStreams(final Set<InputStream> fileStreams, final String moduleName) {
        return fileStreams.stream().map((fileStream) -> store(fileStream, moduleName)).collect(Collectors.toSet());
    }

    @Override
    public File fetch(final FileStoreMapper fileMapper, final String moduleName) {
        return fetch(fileMapper.getFileStoreId(), moduleName);
    }

    @Override
    public Set<File> fetchAll(final Set<FileStoreMapper> fileMappers, final String moduleName) {
        return fileMappers.stream().map((fileMapper) -> fetch(fileMapper.getFileStoreId(), moduleName)).collect(Collectors.toSet());
    }
    
    @Override
    public File fetch(final String fileStoreId, final String moduleName) {
        final Path path = Paths.get(fileStoreBaseDir + File.separator + moduleName);
        if (!Files.exists(path))
            throw new EGOVRuntimeException(String.format("File Store does not exist at Path : %s/%s",fileStoreBaseDir,moduleName));
        return Paths.get(path.toString() + File.separator + fileStoreId).toFile();
    }
    
    private Path createNewFilePath(final FileStoreMapper fileMapper, final String moduleName) throws IOException {
        final Path fileStoreDir = Paths.get(fileStoreBaseDir + File.separator + moduleName);
        if (!Files.exists(fileStoreDir)) {
            LOG.info("File Store Directory {}/{} not found, creating one", fileStoreBaseDir, moduleName);
            Files.createDirectories(fileStoreDir);
            LOG.info("Created File Store Directory {}/{}", fileStoreBaseDir,moduleName);
        }
        return Paths.get(fileStoreDir.toString() + File.separator + fileMapper.getFileStoreId());
    }
}