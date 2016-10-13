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

package org.egov.mrs.domain.entity;

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

import org.apache.commons.lang.WordUtils;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.mrs.application.MarriageConstants;

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

    

    public RegistrationCertificate(final MarriageRegistration registration, final User user,byte[] husbandPhoto, byte[] wifePhoto) {
        this.registration = registration;
        this.user = user;
        this.husbandPhoto=husbandPhoto;
        this.wifePhoto=wifePhoto;
        
        
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
        // return String.join(" ", STYLE_TAG_BEGIN, this.registration.getHusband().getFullName(), STYLE_TAG_END);
    }

    public String getHusbandAddress() {
        return registration.getHusband().getContactInfo().getResidenceAddress();
        // return String.join(" ", STYLE_TAG_BEGIN, this.registration.getHusband().getContactInfo().getResidenceAddress(),
        // STYLE_TAG_END);
    }

    public String getWifeName() {
        return registration.getWife().getFullName();
        // return String.join(" ", STYLE_TAG_BEGIN, this.registration.getWife().getFullName(), STYLE_TAG_END);
    }

    public String getWifeAddress() {
        return registration.getWife().getContactInfo().getResidenceAddress();
        // return String.join(" ", STYLE_TAG_BEGIN, this.registration.getWife().getContactInfo().getResidenceAddress(),
        // STYLE_TAG_END);
    }

    public String getDateOfMarriage() {
        return dateFormatter.format(registration.getDateOfMarriage());
        // return String.join(" ", STYLE_TAG_BEGIN, dateFormatter.format(this.registration.getDateOfMarriage()), STYLE_TAG_END);
    }

    public String getPlaceOfMarriage() {
        return registration.getPlaceOfMarriage();
        // return String.join(" ", STYLE_TAG_BEGIN, this.registration.getPlaceOfMarriage(), STYLE_TAG_END);
    }

    public String getDateOfRegistration() {
        return dateFormatter.format(registration.getCreatedDate());
        // return String.join(" ", STYLE_TAG_BEGIN, dateFormatter.format(this.registration.getCreatedDate()), STYLE_TAG_END);
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
        // final File imageFile = Files.createTempFile(tempFilePath, "mrphoto", ".jpg").toFile();
        // FileUtils.writeByteArrayToFile(imageFile, fileData);
        // System.out.println("===== " + imageFile.getAbsolutePath() + "============");
        // return imageFile.getAbsolutePath();

        /*
         * bais.mark(2); int byte1; try { byte1 = bais.read(); while(byte1 != -1) { if (byte1 == 0xFF || byte1 == 216) {
         * //System.out.println("Is a jpeg image.."); System.out.print( "  " + byte1); } byte1 = bais.read(); } } catch
         * (IOException e1) { // TODO Auto-generated catch block e1.printStackTrace(); } bais.reset();
         */
        /*
         * Image image = null; try { image = ImageIO.read(bais); } catch (Exception e) { System.out.println("decodePhoto --- " +
         * e); } return image;
         */
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
}