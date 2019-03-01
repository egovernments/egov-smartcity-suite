/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.infra.validation;

import org.egov.infra.cache.impl.LRUCache;
import org.egov.infra.config.core.EnvironmentSettings;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.security.utils.scanner.VirusScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static org.egov.infra.security.utils.scanner.VirusScanner.FOUND;
import static org.egov.infra.validation.constants.ValidationRegex.FILE_NAME;

@Service
public class FileValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileValidator.class);
    private static final Map<String, Pattern> FILE_NAME_PATTERN = new LRUCache<>(0, 50);
    private static final String ALLOWED_FILE_EXT_KEY_SUFFIX = ".allowed.file.ext";

    @Autowired
    private VirusScanner virusScanner;

    @Autowired
    private EnvironmentSettings settings;

    public boolean validFileName(String fileName, String moduleName) {
        boolean fileNameValid = validFileExtensionPattern(moduleName).matcher(fileName).matches();
        if (!fileNameValid)
            LOGGER.warn("Invalid file name found {}", fileName);
        return fileNameValid;
    }

    public boolean isInfected(InputStream fileStream) {
        return virusScanner.enabled() && FOUND.equals(virusScanner.scan(fileStream));
    }

    public boolean isInfected(File file) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            return virusScanner.enabled() && FOUND.equals(virusScanner.scan(randomAccessFile.getChannel()));
        } catch (IOException ioe) {
            throw new ApplicationRuntimeException("Error occurred while validating file InputStream for virus", ioe);
        }
    }

    public boolean fileValid(InputStream fileStream, String fileName, String moduleName) {
        return validFileName(fileName, moduleName) && !isInfected(fileStream);
    }

    public boolean fileValid(byte[] fileStreamBytes, String fileName, String moduleName) {
        return validFileName(fileName, moduleName) && !isInfected(new ByteArrayInputStream(fileStreamBytes));
    }

    public boolean fileValid(File file, String fileName, String moduleName) {
        return validFileName(fileName, moduleName) && !isInfected(file);
    }

    private Pattern validFileExtensionPattern(String moduleName) {
        String acceptableNamePattern = FILE_NAME.replace("[a-zA-Z]", settings.getProperty(moduleName, ALLOWED_FILE_EXT_KEY_SUFFIX));
        return FILE_NAME_PATTERN
                .computeIfAbsent(acceptableNamePattern, val -> Pattern.compile(acceptableNamePattern, CASE_INSENSITIVE));
    }
}
