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

package org.egov.mrs.domain.entity;

import org.apache.commons.lang.WordUtils;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.mrs.application.MarriageConstants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;

/**
 * A Bean class which is being used as input the certificate jrxml
 * @author nayeem
 *
 */
public class RegistrationCertificate {

    private static Path tempFilePath = Paths.get(System.getProperty("user.home") + File.separator + "testtmpr");
    public static final String STYLE_TAG_BEGIN = "<style forecolor=\"#000000\" isBold=\"true\" pdfFontName=\"SansSerif\" pdfEncoding=\"Cp1252\">";
    public static final String STYLE_TAG_END = "</style>";
    private final DateFormat dateFormatter = new SimpleDateFormat(MarriageConstants.DATE_FORMAT_DDMMYYYY);
    private MarriageRegistration registration;
    private User user;
    private byte[] husbandPhoto;
    private byte[] wifePhoto;
    private byte[] marriagePhoto;

    public RegistrationCertificate(final MarriageRegistration registration, final User user, byte[] husbandPhoto,
            byte[] wifePhoto, byte[] marriagePhoto) {
        this.registration = registration;
        this.user = user;
        this.husbandPhoto = husbandPhoto;
        this.wifePhoto = wifePhoto;
        this.marriagePhoto = marriagePhoto;

    }

    public MarriageRegistration getRegistration() {
        return registration;
    }

    public void setRegistration(final MarriageRegistration registration) {
        this.registration = registration;
    }

    public String getZoneName() {
        return registration.getZone().getName();
    }

    public String getHusbandName() {
        return registration.getHusband().getFullName();
    }

    public String getHusbandAddress() {
        return registration.getHusband().getContactInfo().getResidenceAddress();
    }

    public String getWifeName() {
        return registration.getWife().getFullName();
    }

    public String getWifeAddress() {
        return registration.getWife().getContactInfo().getResidenceAddress();
    }

    public String getDateOfMarriage() {
        return dateFormatter.format(registration.getDateOfMarriage());
    }

    public String getPlaceOfMarriage() {
        return registration.getPlaceOfMarriage();
    }

    public String getDateOfRegistration() {
        return dateFormatter.format(registration.getCreatedDate());
    }

    public InputStream getWifePhoto() {
        if (this.wifePhoto != null)
            return new java.io.ByteArrayInputStream(this.wifePhoto);
        return null;
    }

    public InputStream getHusbandPhoto() {
        if (this.husbandPhoto != null)
            return new java.io.ByteArrayInputStream(this.husbandPhoto);
        return null;
    }

    public InputStream getMarriagePhoto() {
        if (this.marriagePhoto != null)
            return new java.io.ByteArrayInputStream(this.marriagePhoto);
        return null;
    }

    public String getRoleName() {
        return new ArrayList<Role>(user.getRoles()).get(0).getName();
    }

    public static InputStream decodePhoto(final String encodedString) throws IOException {

        final OutputStream os = new ByteArrayOutputStream();
        os.write(0xFF);
        os.write(216);
        final byte[] orginalFileData = Base64.getDecoder().decode(encodedString);
        final byte[] fileData = new byte[orginalFileData.length + 2];
        fileData[0] = (byte) 0xFF;
        fileData[1] = (byte) 216;
        int j = 2;

        for (int i = 0; i < orginalFileData.length - 1; i++)
            fileData[j++] = orginalFileData[i];

        return new ByteArrayInputStream(fileData);

    }

    public String getUserName() {
        final String salutation = user.getSalutation() == null ? "" : user.getSalutation().concat(" ");
        return salutation.concat(WordUtils.capitalizeFully(user.getName()));
    }

    public String getRejectionDate() {
        return dateFormatter.format(registration.getState().getCreatedDate());
    }

    public String getRejectionReason() {
        return registration.getRejectionReason();
    }

    public String getRegistrationNumber() {
        return registration.getRegistrationNo();
    }

    public String getApplicationNumber() {
        return registration.getApplicationNo();
    }

}