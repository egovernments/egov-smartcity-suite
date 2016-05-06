/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.filestore.service.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.infra.config.properties.ApplicationProperties;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.utils.EgovThreadLocals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component("localDiskFileStoreService")
public class LocalDiskFileStoreService implements FileStoreService {

    private static final Logger LOG = LoggerFactory.getLogger(LocalDiskFileStoreService.class);

    private final String fileStoreBaseDir;

    @Autowired
    public LocalDiskFileStoreService(final ApplicationProperties applicationProperties) {
        if (applicationProperties.fileStoreBaseDir().isEmpty())
            this.fileStoreBaseDir = FileUtils.getUserDirectoryPath()+ File.separator + "egovfilestore";
        else
            this.fileStoreBaseDir = applicationProperties.fileStoreBaseDir();
    }

    @Override
    public FileStoreMapper store(final File sourceFile, final String fileName, final String mimeType, final String moduleName) {
        try {
            final FileStoreMapper fileMapper = new FileStoreMapper(UUID.randomUUID().toString(),
                    StringUtils.defaultString(fileName, sourceFile.getName()));
            final Path newFilePath = this.createNewFilePath(fileMapper, moduleName);
            Files.copy(sourceFile.toPath(), newFilePath);
            fileMapper.setContentType(mimeType);
            return fileMapper;
        } catch (final IOException e) {
            throw new ApplicationRuntimeException(
                    String.format("Error occurred while storing files at %s/%s/%s", this.fileStoreBaseDir, EgovThreadLocals.getCityCode(), moduleName), e);
        }
    }

    @Override
    public FileStoreMapper store(final InputStream sourceFileStream, final String fileName, final String mimeType, final String moduleName) {
        try {
            final FileStoreMapper fileMapper = new FileStoreMapper(UUID.randomUUID().toString(), fileName);
            final Path newFilePath = this.createNewFilePath(fileMapper, moduleName);
            Files.copy(sourceFileStream, newFilePath);
            fileMapper.setContentType(mimeType);
            sourceFileStream.close();
            return fileMapper;
        } catch (final IOException e) {
            throw new ApplicationRuntimeException(
                    String.format("Error occurred while storing files at %s/%s/%s", this.fileStoreBaseDir, EgovThreadLocals.getCityCode(), moduleName), e);
        }
    }

    @Override
    public File fetch(final FileStoreMapper fileMapper, final String moduleName) {
        return this.fetch(fileMapper.getFileStoreId(), moduleName);
    }

    @Override
    public Set<File> fetchAll(final Set<FileStoreMapper> fileMappers, final String moduleName) {
        return fileMappers.stream().map(fileMapper -> this.fetch(fileMapper.getFileStoreId(), moduleName))
                .collect(Collectors.toSet());
    }

    @Override
    public File fetch(final String fileStoreId, final String moduleName) {
        final Path fileDirPath = this.getFileDirectoryPath(moduleName);
        if (!Files.exists(fileDirPath))
            throw new ApplicationRuntimeException(String.format("File Store does not exist at Path : %s/%s/%s", this.fileStoreBaseDir,
                    EgovThreadLocals.getCityCode(), moduleName));
        return this.getFilePath(fileDirPath, fileStoreId).toFile();
    }

    @Override
    public void delete(final String fileStoreId, final String moduleName) {
        final Path fileDirPath = this.getFileDirectoryPath(moduleName);
        if (Files.exists(fileDirPath)) {
            final Path filePath = this.getFilePath(fileDirPath, fileStoreId);
            try {
                Files.deleteIfExists(filePath);
            } catch (final IOException e) {
                throw new ApplicationRuntimeException(String.format("Could not remove document %s", filePath.getFileName()), e);
            }
        }
    }

    private Path createNewFilePath(final FileStoreMapper fileMapper, final String moduleName) throws IOException {
        final Path fileDirPath = this.getFileDirectoryPath(moduleName);
        if (!Files.exists(fileDirPath)) {
            LOG.info("File Store Directory {}/{}/{} not found, creating one", this.fileStoreBaseDir, EgovThreadLocals.getCityCode(),
                    moduleName);
            Files.createDirectories(fileDirPath);
            LOG.info("Created File Store Directory {}/{}/{}", this.fileStoreBaseDir, EgovThreadLocals.getCityCode(), moduleName);
        }
        return this.getFilePath(fileDirPath, fileMapper.getFileStoreId());
    }

    private Path getFileDirectoryPath(final String moduleName) {
        return Paths.get(new StringBuilder().append(this.fileStoreBaseDir).append(File.separator).append(EgovThreadLocals.getCityCode()).append(File.separator).append(moduleName).toString());
    }

    private Path getFilePath(final Path fileDirPath, final String fileStoreId) {
        return Paths.get(fileDirPath + File.separator + fileStoreId);
    }
}