/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.infra.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.repository.FileStoreMapperRepository;
import org.egov.infra.filestore.service.FileStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.egov.infra.utils.ApplicationConstant.CONTENT_DISPOSITION;
import static org.egov.infra.utils.ApplicationConstant.CONTENT_DISPOSITION_ATTACH;
import static org.egov.infra.utils.ApplicationConstant.CONTENT_DISPOSITION_INLINE;
import static org.egov.infra.utils.ImageUtils.compressImage;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

@Service
public class FileStoreUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileStoreUtils.class);

    @Autowired
    @Qualifier("fileStoreService")
    private FileStoreService fileStoreService;

    @Autowired
    private FileStoreMapperRepository fileStoreMapperRepository;

    public Path getFileAsPath(String fileStoreId, String moduleName) {
        return fileStoreService.fetchAsPath(fileStoreId, moduleName);
    }

    public Optional<FileStoreMapper> getFileStoreMapper(String fileStoreId) {
        return Optional.ofNullable(this.fileStoreMapperRepository.findByFileStoreId(fileStoreId));
    }

    public ResponseEntity<InputStreamResource> fileAsResponseEntity(String fileStoreId, String moduleName, boolean toSave) {
        try {
            Optional<FileStoreMapper> fileStoreMapper = getFileStoreMapper(fileStoreId);
            if (fileStoreMapper.isPresent()) {
                Path file = getFileAsPath(fileStoreId, moduleName);
                byte[] fileBytes = Files.readAllBytes(file);
                return ResponseEntity
                        .ok()
                        .contentType(MediaType.parseMediaType(fileStoreMapper.get().getContentType()))
                        .cacheControl(CacheControl.noCache())
                        .contentLength(fileBytes.length)
                        .header(CONTENT_DISPOSITION, format(toSave ? CONTENT_DISPOSITION_ATTACH : CONTENT_DISPOSITION_INLINE,
                                fileStoreMapper.get().getFileName())).
                                body(new InputStreamResource(new ByteArrayInputStream(fileBytes)));
            }
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            LOGGER.error("Error occurred while creating response entity from file mapper", e);
            return ResponseEntity.badRequest().build();
        }
    }

    public void writeToHttpResponseStream(String fileStoreId, String moduleName, HttpServletResponse response) {
        try {
            FileStoreMapper fileStoreMapper = this.fileStoreMapperRepository.findByFileStoreId(fileStoreId);
            if (fileStoreMapper != null) {
                File file = this.fileStoreService.fetch(fileStoreMapper, moduleName);
                response.setHeader(CONTENT_DISPOSITION, format(CONTENT_DISPOSITION_INLINE, fileStoreMapper.getFileName()));
                response.setContentType(fileStoreMapper.getContentType());
                OutputStream out = response.getOutputStream();
                IOUtils.write(FileUtils.readFileToByteArray(file), out);
            }
        } catch (IOException e) {
            LOGGER.error("Error occurred while writing file to response stream", e);
        }
    }

    public Set<FileStoreMapper> addToFileStore(MultipartFile[] files, String moduleName) {
        return this.addToFileStore(files, moduleName, false);
    }

    public Set<FileStoreMapper> addToFileStore(MultipartFile[] files, String moduleName, boolean compressImage) {
        if (ArrayUtils.isNotEmpty(files))
            return Arrays.stream(files)
                    .filter(file -> !file.isEmpty())
                    .map(file -> {
                        try {
                            if (compressImage && file.getContentType().contains("image"))
                                return this.fileStoreService.store(compressImage(file),
                                        file.getOriginalFilename(), "image/jpeg", moduleName);
                            else
                                return this.fileStoreService.store(file.getInputStream(), file.getOriginalFilename(),
                                        file.getContentType(), moduleName);
                        } catch (Exception e) {
                            throw new ApplicationRuntimeException("err.input.stream", e);
                        }
                    }).collect(Collectors.toSet());
        else
            return Collections.emptySet();
    }

    public void copyFileToPath(Path newFilePath, String fileStoreId, String moduleName) throws IOException {
        Optional<FileStoreMapper> fileStoreMapper = getFileStoreMapper(fileStoreId);
        if (fileStoreMapper.isPresent()) {
            File file = fileStoreService.fetch(fileStoreMapper.get(), moduleName);
            Files.copy(file.toPath(), newFilePath);
        }
    }

    public byte[] fileAsByteArray(String fileStoreId, String moduleName) {
        try {
            Optional<FileStoreMapper> fileStoreMapper = getFileStoreMapper(fileStoreId);
            if (fileStoreMapper.isPresent()) {
                Path file = getFileAsPath(fileStoreId, moduleName);
                return Files.readAllBytes(file);
            } else {
                return new byte[0];
            }
        } catch (IOException ioe) {
            LOGGER.error("Error occurred while converting file to byte array", ioe);
            return new byte[0];
        }
    }

    public ResponseEntity<InputStreamResource> fileAsPDFResponse(String fileStoreId, String fileName, String moduleName) {
        try {
            File signedFile = fileStoreService.fetch(fileStoreId, moduleName);
            byte[] signFileBytes = FileUtils.readFileToByteArray(signedFile);
            return ResponseEntity.
                    ok().
                    contentType(MediaType.parseMediaType(APPLICATION_PDF_VALUE)).
                    cacheControl(CacheControl.noCache()).
                    contentLength(signFileBytes.length).
                    header("content-disposition", "inline;filename=" + fileName + ".pdf").
                    body(new InputStreamResource(new ByteArrayInputStream(signFileBytes)));
        } catch (IOException e) {
            throw new ApplicationRuntimeException("Error while reading file", e);
        }
    }
}
