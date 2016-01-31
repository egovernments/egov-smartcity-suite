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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;

import org.apache.commons.lang.WordUtils;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.mrs.application.Constants;

/**
 * A Bean class which is being used as input the certificate jrxml
 * @author NPathan
 *
 */
public class RegistrationCertificate {
    
    public static final String STYLE_TAG_BEGIN = "<style forecolor=\"#000000\" isBold=\"true\" pdfFontName=\"SansSerif\" pdfEncoding=\"Cp1252\">";
    public static final String STYLE_TAG_END = "</style>";
    private DateFormat dateFormatter = new SimpleDateFormat(Constants.DATE_FORMAT_DDMMYYYY);
    private Registration registration;
    private User user;
    
    public RegistrationCertificate() {
    }
    
    public RegistrationCertificate(Registration registration, User user) {
        this.registration = registration;
        this.user = user;
    }

    public Registration getRegistration() {
        return registration;
    }
    
    public void setRegistration(Registration registration) {
        this.registration = registration;
    }
    
    public String getZoneName() {
        return this.registration.getZone().getName();
    }
    
    public String getHusbandName() {
        return this.registration.getHusband().getFullName();
        //return String.join(" ", STYLE_TAG_BEGIN, this.registration.getHusband().getFullName(), STYLE_TAG_END);
    }
    
    public String getHusbandAddress() {
        return this.registration.getHusband().getContactInfo().getResidenceAddress();
        //return String.join(" ", STYLE_TAG_BEGIN, this.registration.getHusband().getContactInfo().getResidenceAddress(), STYLE_TAG_END);
    }
    
    public String getWifeName() {
        return this.registration.getWife().getFullName();
        //return String.join(" ", STYLE_TAG_BEGIN, this.registration.getWife().getFullName(), STYLE_TAG_END);
    }
    
    public String getWifeAddress() {
        return this.registration.getWife().getContactInfo().getResidenceAddress();
//        return String.join(" ", STYLE_TAG_BEGIN, this.registration.getWife().getContactInfo().getResidenceAddress(), STYLE_TAG_END);
    }
    
    public String getDateOfMarriage() {
        return dateFormatter.format(this.registration.getDateOfMarriage());
        //return String.join(" ", STYLE_TAG_BEGIN, dateFormatter.format(this.registration.getDateOfMarriage()), STYLE_TAG_END);
    }
    
    public String getPlaceOfMarriage() {
        return this.registration.getPlaceOfMarriage();
        //return String.join(" ", STYLE_TAG_BEGIN, this.registration.getPlaceOfMarriage(), STYLE_TAG_END);
    }
    
    public String getDateOfRegistration() {
        return dateFormatter.format(this.registration.getCreatedDate());
        //return String.join(" ", STYLE_TAG_BEGIN, dateFormatter.format(this.registration.getCreatedDate()), STYLE_TAG_END);
    }
    
    public String getWifePhoto() {
        return Base64.getEncoder().encodeToString(this.registration.getWife().getPhoto());
    }
    
    public byte[] getHusbandPhoto() {
        return this.registration.getHusband().getPhoto();
        //new java.io.ByteArrayInputStream(javax.xml.DatatypeConverter.parseBase64Binary($F{ImageField}))
        //return Base64.getEncoder().encodeToString(this.registration.getHusband().getPhoto());
    }
    
    public String getRoleName() {
        return new ArrayList<Role>(user.getRoles()).get(0).getName();
    }
    
    public String getUserName() {
        String salutation = user.getSalutation() == null ? "" : user.getSalutation().concat(" ");
        return salutation.concat(WordUtils.capitalizeFully(user.getName()));
    }
    
    public String getRejectionDate() {
        return dateFormatter.format(registration.getState().getCreatedDate());
    }
    
    public String getRejectionReason() {
        return registration.getRejectionReason();
    }
 } 