/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
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

@Component("localDiskFileStoreService")
public class LocalDiskFileStoreService implements FileStoreService {

    private static final Logger LOG = LoggerFactory.getLogger(LocalDiskFileStoreService.class);

    private String fileStoreBaseDir;

    @Autowired
    public LocalDiskFileStoreService(final ApplicationProperties applicationProperties) {
        if (applicationProperties.fileStoreBaseDir().isEmpty())
            fileStoreBaseDir = System.getProperty("user.home") + File.separator + "egovfilestore";
        else
            fileStoreBaseDir = applicationProperties.fileStoreBaseDir();
    }

    @Override
    public FileStoreMapper store(final File sourceFile, final String fileName, final String mimeType, final String moduleName) {
        try {
            final FileStoreMapper fileMapper = new FileStoreMapper(UUID.randomUUID().toString(),
                    StringUtils.defaultString(fileName, sourceFile.getName()));
            final Path newFilePath = createNewFilePath(fileMapper, moduleName);
            Files.copy(sourceFile.toPath(), newFilePath);
            fileMapper.setContentType(mimeType);
            return fileMapper;
        } catch (final IOException e) {
            throw new ApplicationRuntimeException(
                    String.format("Error occurred while storing files at %s/%s/%s", fileStoreBaseDir, EgovThreadLocals.getCityCode(), moduleName), e);
        }
    }

    @Override
    public FileStoreMapper store(final InputStream sourceFileStream, final String fileName, final String mimeType, final String moduleName) {
        try {
            final FileStoreMapper fileMapper = new FileStoreMapper(UUID.randomUUID().toString(), fileName);
            final Path newFilePath = createNewFilePath(fileMapper, moduleName);
            Files.copy(sourceFileStream, newFilePath);
            fileMapper.setContentType(mimeType);
            sourceFileStream.close();
            return fileMapper;
        } catch (final IOException e) {
            throw new ApplicationRuntimeException(
                    String.format("Error occurred while storing files at %s/%s/%s", fileStoreBaseDir, EgovThreadLocals.getCityCode(), moduleName), e);
        }
    }

    @Override
    public File fetch(final FileStoreMapper fileMapper, final String moduleName) {
        return fetch(fileMapper.getFileStoreId(), moduleName);
    }

    @Override
    public Set<File> fetchAll(final Set<FileStoreMapper> fileMappers, final String moduleName) {
        return fileMappers.stream().map((fileMapper) -> fetch(fileMapper.getFileStoreId(), moduleName))
                .collect(Collectors.toSet());
    }

    @Override
    public File fetch(final String fileStoreId, final String moduleName) {
        final Path path = Paths.get(fileStoreBaseDir + File.separator + EgovThreadLocals.getCityCode() + File.separator + moduleName);
        if (!Files.exists(path))
            throw new ApplicationRuntimeException(String.format("File Store does not exist at Path : %s/%s/%s", fileStoreBaseDir,
                    EgovThreadLocals.getCityCode(), moduleName));
        return Paths.get(path.toString() + File.separator + fileStoreId).toFile();
    }

    private Path createNewFilePath(final FileStoreMapper fileMapper, final String moduleName) throws IOException {
        final Path fileStoreDir = Paths.get(fileStoreBaseDir + File.separator + EgovThreadLocals.getCityCode() + File.separator + moduleName);
        if (!Files.exists(fileStoreDir)) {
            LOG.info("File Store Directory {}/{}/{} not found, creating one", fileStoreBaseDir, EgovThreadLocals.getCityCode(),
                    moduleName);
            Files.createDirectories(fileStoreDir);
            LOG.info("Created File Store Directory {}/{}/{}", fileStoreBaseDir, EgovThreadLocals.getCityCode(), moduleName);
        }
        return Paths.get(fileStoreDir.toString() + File.separator + fileMapper.getFileStoreId());
    }

    @Override
    public void delete(final String fileStoreId, final String moduleName) {
        final Path fileStoreDir = Paths.get(fileStoreBaseDir + File.separator + EgovThreadLocals.getCityCode() + File.separator + moduleName);
        if (Files.exists(fileStoreDir)) {
            final Path filePath = Paths.get(fileStoreDir.toString() + File.separator + fileStoreId);
            try {
                Files.deleteIfExists(filePath);
            } catch (final IOException e) {
                LOG.error("Could not remove document {}", filePath.getFileName(), e);
                throw new ApplicationRuntimeException("Could not remove document", e);
            }
        }
    }
}