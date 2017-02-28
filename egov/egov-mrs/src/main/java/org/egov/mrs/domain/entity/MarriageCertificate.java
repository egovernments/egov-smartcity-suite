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

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.mrs.domain.enums.MarriageCertificateType;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "egmrs_certificate")
@SequenceGenerator(name = MarriageCertificate.SEQ_MARRIAGECERTIFICATE, sequenceName = MarriageCertificate.SEQ_MARRIAGECERTIFICATE, allocationSize = 1)
public class MarriageCertificate extends AbstractAuditable {

    private static final long serialVersionUID = 1L;

    public static final String SEQ_MARRIAGECERTIFICATE = "SEQ_EGMRS_CERTIFICATE";

    @Id
    @GeneratedValue(generator = SEQ_MARRIAGECERTIFICATE, strategy = GenerationType.SEQUENCE)
    private Long id;
    @SafeHtml
    private String certificateNo;

    private Date certificateDate;

    @Enumerated(EnumType.STRING)
    private MarriageCertificateType certificateType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration")
    private MarriageRegistration registration;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "filestore", nullable = false)
    private FileStoreMapper fileStore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reissue")
    private ReIssue reIssue;

    private boolean certificateIssued;

    @Transient
    private Date fromDate;
    @Transient
    private Date toDate;

    @Transient
    private String frequency;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(final String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public Date getCertificateDate() {
        return certificateDate;
    }

    public void setCertificateDate(final Date certificateDate) {
        this.certificateDate = certificateDate;
    }

    public MarriageCertificateType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(final MarriageCertificateType certificateType) {
        this.certificateType = certificateType;
    }

    public MarriageRegistration getRegistration() {
        return registration;
    }

    public void setRegistration(final MarriageRegistration registration) {
        this.registration = registration;
    }

    public FileStoreMapper getFileStore() {
        return fileStore;
    }

    public void setFileStore(final FileStoreMapper fileStore) {
        this.fileStore = fileStore;
    }

    public ReIssue getReIssue() {
        return reIssue;
    }

    public void setReIssue(final ReIssue reIssue) {
        this.reIssue = reIssue;
    }

    public boolean isCertificateIssued() {
        return certificateIssued;
    }

    public void setCertificateIssued(final boolean certificateIssued) {
        this.certificateIssued = certificateIssued;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public void setFrequency(final String frequency) {
        this.frequency = frequency;
    }

    public String getFrequency() {
        return frequency;
    }

}
