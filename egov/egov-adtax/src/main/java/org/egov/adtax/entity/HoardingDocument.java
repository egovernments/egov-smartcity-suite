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

package org.egov.adtax.entity;

import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "egadtax_hoardingdocument")
@SequenceGenerator(name = HoardingDocument.SEQ_HOARDING_DOCUMENT, sequenceName = HoardingDocument.SEQ_HOARDING_DOCUMENT, allocationSize = 1)
public class HoardingDocument extends AbstractAuditable {

    public static final String SEQ_HOARDING_DOCUMENT = "SEQ_EGADTAX_DOCUMENT";
    private static final long serialVersionUID = 1938612090916339332L;
    @Id
    @GeneratedValue(generator = SEQ_HOARDING_DOCUMENT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctype")
    private HoardingDocumentType doctype;

    private String description;

    @Temporal(TemporalType.DATE)
    private Date docDate;

    private boolean enclosed;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(name = "EGADTAX_DOCUMENT_FILES", joinColumns = @JoinColumn(name = "document"), inverseJoinColumns = @JoinColumn(name = "filestore"))
    private Set<FileStoreMapper> files = new HashSet<>();

    @Transient
    private MultipartFile[] attachments;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public HoardingDocumentType getDoctype() {
        return doctype;
    }

    public void setDoctype(final HoardingDocumentType doctype) {
        this.doctype = doctype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Date getDocDate() {
        return docDate;
    }

    public void setDocDate(final Date docDate) {
        this.docDate = docDate;
    }

    public boolean isEnclosed() {
        return enclosed;
    }

    public void setEnclosed(final boolean enclosed) {
        this.enclosed = enclosed;
    }

    public Set<FileStoreMapper> getFiles() {
        return files;
    }

    public void setFiles(final Set<FileStoreMapper> files) {
        this.files = files;
    }

    public void addFiles(final Set<FileStoreMapper> files) {
        this.files.addAll(files);
    }


    public MultipartFile[] getAttachments() {
        return attachments;
    }

    public void setAttachments(final MultipartFile[] attachments) {
        this.attachments = attachments;
    }
}
