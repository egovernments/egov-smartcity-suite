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

package org.egov.infra.security.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.commons.lang.RandomStringUtils;
import org.egov.infra.exception.ApplicationRuntimeException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

import static com.google.zxing.EncodeHintType.CHARACTER_SET;
import static com.google.zxing.EncodeHintType.MARGIN;
import static org.egov.infra.config.core.LocalizationSettings.encoding;
import static org.egov.infra.utils.ImageUtils.PNG_EXTN;
import static org.egov.infra.utils.ImageUtils.PNG_FORMAT_NAME;

public final class SecureCodeUtils {

    private static final int DEFAULT_WIDTH = 125;
    private static final int DEFAULT_HEIGHT = 125;

    private SecureCodeUtils() {
        //static API's only
    }

    public static File generateQRCode(String content) {
        return generateQRCode(content, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public static File generateQRCode(String content, int qrImgWidth, int qrImgHeight) {
        return generateSecureCode(content, BarcodeFormat.QR_CODE, qrImgWidth, qrImgHeight);
    }

    public static File generatePDF417Code(String content) {
        return generatePDF417Code(content, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public static File generatePDF417Code(String content, int imgWidth, int imgHeight) {
        return generateSecureCode(content, BarcodeFormat.PDF_417, imgWidth, imgHeight);
    }

    public static File generateSecureCode(String content, BarcodeFormat format, int imgWidth, int imgHeight) {
        try {
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(CHARACTER_SET, encoding());
            hints.put(MARGIN, 1);
            BitMatrix secureCodeMatrix = new MultiFormatWriter().encode(content, format, imgWidth, imgHeight, hints);
            Path secureCodePath = Files.createTempFile(RandomStringUtils.randomAlphabetic(5), PNG_EXTN);
            MatrixToImageWriter.writeToPath(secureCodeMatrix, PNG_FORMAT_NAME, secureCodePath);
            return secureCodePath.toFile();
        } catch (WriterException | IOException e) {
            throw new ApplicationRuntimeException("Error occurred while generating Secure Code", e);
        }
    }
}
