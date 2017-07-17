/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.filestore.service.impl;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

import static java.io.File.separator;
import static java.util.UUID.randomUUID;
import static org.apache.commons.io.FileUtils.getUserDirectoryPath;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.egov.infra.config.core.ApplicationThreadLocals.getCityCode;
import static org.slf4j.LoggerFactory.getLogger;

@Component("localDiskFileStoreService")
public class LocalDiskFileStoreService implements FileStoreService {

    private static final Logger LOG = getLogger(LocalDiskFileStoreService.class);

    private String fileStoreBaseDir;

    @Autowired
    public LocalDiskFileStoreService(@Value("${filestore.base.dir}") String fileStoreBaseDir) {
        if (fileStoreBaseDir.isEmpty())
            this.fileStoreBaseDir = getUserDirectoryPath() + separator + "egovfilestore";
        else
            this.fileStoreBaseDir = fileStoreBaseDir;
    }

    @Override
    public FileStoreMapper store(File sourceFile, String fileName, String mimeType, String moduleName) {
        return store(sourceFile, fileName, mimeType, moduleName, true);
    }

    @Override
    public FileStoreMapper store(InputStream sourceFileStream, String fileName, String mimeType, String moduleName) {
        return store(sourceFileStream, fileName, mimeType, moduleName, true);
    }

    @Override
    public FileStoreMapper store(File file, String fileName, String mimeType, String moduleName, boolean deleteFile) {
        try {
            FileStoreMapper fileMapper = new FileStoreMapper(randomUUID().toString(),
                    defaultString(fileName, file.getName()));
            Path newFilePath = this.createNewFilePath(fileMapper, moduleName);
            Files.copy(file.toPath(), newFilePath);
            fileMapper.setContentType(mimeType);
            if (deleteFile && file.delete())
                LOG.info("File store source file deleted");
            return fileMapper;
        } catch (IOException e) {
            throw new ApplicationRuntimeException(String.format("Error occurred while storing files at %s/%s/%s",
                    this.fileStoreBaseDir, getCityCode(), moduleName), e);
        }
    }

    @Override
    public FileStoreMapper store(InputStream fileStream, String fileName, String mimeType, String moduleName, boolean closeStream) {
        try {
            FileStoreMapper fileMapper = new FileStoreMapper(randomUUID().toString(), fileName);
            Path newFilePath = this.createNewFilePath(fileMapper, moduleName);
            Files.copy(fileStream, newFilePath);
            fileMapper.setContentType(mimeType);
            if (closeStream)
                fileStream.close();
            return fileMapper;
        } catch (IOException e) {
            throw new ApplicationRuntimeException(String.format("Error occurred while storing files at %s/%s/%s",
                    this.fileStoreBaseDir, getCityCode(), moduleName), e);
        }
    }

    @Override
    public File fetch(FileStoreMapper fileMapper, String moduleName) {
        return this.fetch(fileMapper.getFileStoreId(), moduleName);
    }

    @Override
    public Set<File> fetchAll(Set<FileStoreMapper> fileMappers, String moduleName) {
        return fileMappers.stream().map(fileMapper -> this.fetch(fileMapper.getFileStoreId(), moduleName))
                .collect(Collectors.toSet());
    }

    @Override
    public File fetch(String fileStoreId, String moduleName) {
        Path fileDirPath = this.getFileDirectoryPath(moduleName);
        if (!fileDirPath.toFile().exists())
            throw new ApplicationRuntimeException(String.format("File Store does not exist at Path : %s/%s/%s",
                    this.fileStoreBaseDir, getCityCode(), moduleName));
        return this.getFilePath(fileDirPath, fileStoreId).toFile();
    }

    @Override
    public void delete(String fileStoreId, String moduleName) {
        Path fileDirPath = this.getFileDirectoryPath(moduleName);
        if (!fileDirPath.toFile().exists()) {
            Path filePath = this.getFilePath(fileDirPath, fileStoreId);
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new ApplicationRuntimeException(String.format("Could not remove document %s", filePath.getFileName()), e);
            }
        }
    }

    private Path createNewFilePath(FileStoreMapper fileMapper, String moduleName) throws IOException {
        Path fileDirPath = this.getFileDirectoryPath(moduleName);
        if (!fileDirPath.toFile().exists()) {
            LOG.info("File Store Directory {}/{}/{} not found, creating one", this.fileStoreBaseDir, getCityCode(),
                    moduleName);
            Files.createDirectories(fileDirPath);
            LOG.info("Created File Store Directory {}/{}/{}", this.fileStoreBaseDir, getCityCode(), moduleName);
        }
        return this.getFilePath(fileDirPath, fileMapper.getFileStoreId());
    }

    private Path getFileDirectoryPath(String moduleName) {
        return Paths.get(new StringBuilder().append(this.fileStoreBaseDir).append(separator).
                append(getCityCode()).append(separator).append(moduleName).toString());
    }

    private Path getFilePath(Path fileDirPath, String fileStoreId) {
        return Paths.get(fileDirPath + separator + fileStoreId);
    }
}