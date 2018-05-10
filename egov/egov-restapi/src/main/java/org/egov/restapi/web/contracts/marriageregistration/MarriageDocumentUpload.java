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
package org.egov.restapi.web.contracts.marriageregistration;

import org.springframework.web.multipart.MultipartFile;

public class MarriageDocumentUpload {

    private MultipartFile marriagePhotoFile;

    private MultipartFile husbandPhotoFile;

    private MultipartFile wifePhotoFile;

    private MultipartFile memorandumOfMarriage;

    private MultipartFile husbandBirthCertificate;

    private MultipartFile husbandRationCard;

    private MultipartFile husbandAadhar;

    private MultipartFile wifeBirthCertificate;

    private MultipartFile wifeRationCard;

    private MultipartFile wifeAadhar;

    private MultipartFile dataSheet;
    
    private MultipartFile marriageCertificate;
    
    private String ApplicationKey; 
    
    public MultipartFile getMarriagePhotoFile() {
        return marriagePhotoFile;
    }

    public void setMarriagePhotoFile(final MultipartFile marriagePhotoFile) {
        this.marriagePhotoFile = marriagePhotoFile;
    }

    public MultipartFile getHusbandPhotoFile() {
        return husbandPhotoFile;
    }

    public void setHusbandPhotoFile(final MultipartFile husbandPhotoFile) {
        this.husbandPhotoFile = husbandPhotoFile;
    }

    public MultipartFile getWifePhotoFile() {
        return wifePhotoFile;
    }

    public void setWifePhotoFile(final MultipartFile wifePhotoFile) {
        this.wifePhotoFile = wifePhotoFile;
    }

    public MultipartFile getMemorandumOfMarriage() {
        return memorandumOfMarriage;
    }

    public void setMemorandumOfMarriage(final MultipartFile memorandumOfMarriage) {
        this.memorandumOfMarriage = memorandumOfMarriage;
    }

    public MultipartFile getHusbandBirthCertificate() {
        return husbandBirthCertificate;
    }

    public void setHusbandBirthCertificate(final MultipartFile husbandBirthCertificate) {
        this.husbandBirthCertificate = husbandBirthCertificate;
    }

    public MultipartFile getHusbandRationCard() {
        return husbandRationCard;
    }

    public void setHusbandRationCard(final MultipartFile husbandRationCard) {
        this.husbandRationCard = husbandRationCard;
    }

    public MultipartFile getHusbandAadhar() {
        return husbandAadhar;
    }

    public void setHusbandAadhar(final MultipartFile husbandAadhar) {
        this.husbandAadhar = husbandAadhar;
    }

    public MultipartFile getWifeBirthCertificate() {
        return wifeBirthCertificate;
    }

    public void setWifeBirthCertificate(final MultipartFile wifeBirthCertificate) {
        this.wifeBirthCertificate = wifeBirthCertificate;
    }

    public MultipartFile getWifeRationCard() {
        return wifeRationCard;
    }

    public void setWifeRationCard(final MultipartFile wifeRationCard) {
        this.wifeRationCard = wifeRationCard;
    }

    public MultipartFile getWifeAadhar() {
        return wifeAadhar;
    }

    public void setWifeAadhar(final MultipartFile wifeAadhar) {
        this.wifeAadhar = wifeAadhar;
    }

    public MultipartFile getDataSheet() {
        return dataSheet;
    }

    public void setDataSheet(MultipartFile dataSheet) {
        this.dataSheet = dataSheet;
    }

    public MultipartFile getMarriageCertificate() {
        return marriageCertificate;
    }

    public void setMarriageCertificate(MultipartFile marriageCertificate) {
        this.marriageCertificate = marriageCertificate;
    }

    public String getApplicationKey() {
        return ApplicationKey;
    }

    public void setApplicationKey(String applicationKey) {
        ApplicationKey = applicationKey;
    }

   

}
