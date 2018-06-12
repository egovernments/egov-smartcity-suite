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

import javaxt.io.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Optional;

import static javax.imageio.ImageIO.createImageOutputStream;
import static javax.imageio.ImageIO.getImageWritersByFormatName;
import static javax.imageio.ImageIO.read;
import static javax.imageio.ImageWriteParam.MODE_EXPLICIT;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultString;

public final class ImageUtils {
    public static final String JPG_EXTN = ".jpg";
    public static final String JPG_FORMAT_NAME = "jpeg";
    public static final String PNG_EXTN = ".png";
    public static final String PNG_FORMAT_NAME = "png";
    public static final String PNG_MIME_TYPE = "image/png";
    public static final String JPG_MIME_TYPE = "image/jpeg";

    private static final Logger LOG = LoggerFactory.getLogger(ImageUtils.class);

    private ImageUtils() {
        //Not to be initialized
    }

    public static File compressImage(MultipartFile imageFile) throws IOException {
        return compressImage(imageFile.getInputStream(), imageFile.getOriginalFilename(), true);
    }

    public static File compressImage(final InputStream imageStream, String imageFileName, boolean closeStream) throws IOException {
        File compressedImage = Paths.get(imageFileName).toFile();
        try (final ImageOutputStream imageOutput = createImageOutputStream(compressedImage)) {
            ImageWriter writer = getImageWritersByFormatName(defaultString(getExtension(imageFileName), JPG_FORMAT_NAME)).next();
            writer.setOutput(imageOutput);
            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            if (writeParam.canWriteCompressed()) {
                writeParam.setCompressionMode(MODE_EXPLICIT);
                writeParam.setCompressionType(writeParam.getCompressionTypes()[0]);
                writeParam.setCompressionQuality(0.05F);
            }
            writer.write(null, new IIOImage(read(imageStream), null, null), writeParam);
            writer.dispose();
            if (closeStream)
                imageStream.close();
        }
        return compressedImage;
    }

    public static double[] findGeoCoordinates(File jpegImage) {
        Optional<double[]> coordinates = Optional.empty();
        if (JPG_FORMAT_NAME.equalsIgnoreCase(imageFormat(jpegImage))) {
            Image image = new Image(jpegImage);
            coordinates = Optional.ofNullable(image.getGPSCoordinate());
        }
        return coordinates.orElse(new double[]{0D, 0D});
    }

    public static String imageFormat(File image) {
        try (ImageInputStream iis = ImageIO.createImageInputStream(image)) {
            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
            return imageReaders.hasNext() ? imageReaders.next().getFormatName() : EMPTY;
        } catch (IOException e) {
            LOG.warn("Could not read image format from file", e);
            return EMPTY;
        }
    }
}
