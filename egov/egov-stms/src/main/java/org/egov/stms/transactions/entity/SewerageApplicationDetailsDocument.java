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
package org.egov.stms.transactions.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.stms.masters.entity.DocumentTypeMaster;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "egswtax_applicationdetails_documents")
@SequenceGenerator(name = SewerageApplicationDetailsDocument.SEQ_APPLICATIONDETAILS_DOCUMENT, sequenceName = SewerageApplicationDetailsDocument.SEQ_APPLICATIONDETAILS_DOCUMENT, allocationSize = 1)
public class SewerageApplicationDetailsDocument extends AbstractAuditable {

    /**
     *
     */
    private static final long serialVersionUID = 8860169180238684889L;

    public static final String SEQ_APPLICATIONDETAILS_DOCUMENT = "SEQ_EGSWTAX_APPLICATIONDETAILS_DOCUMENTS";

    @Id
    @GeneratedValue(generator = SEQ_APPLICATIONDETAILS_DOCUMENT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicationdetails", nullable = false)
    private SewerageApplicationDetails applicationDetails;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "egswtax_documents", joinColumns = @JoinColumn(name = "applicationDetailDocument") , inverseJoinColumns = @JoinColumn(name = "filestoreid") )
    private Set<FileStoreMapper> fileStore;
    
    @ManyToOne
    @NotNull
    @JoinColumn(name = "documenttypemaster", nullable = false)  
    private DocumentTypeMaster documentTypeMaster;
    
    
    @SafeHtml
    @Length(max = 50)
    private String documentNumber;

   
    @Temporal(value = TemporalType.DATE)
    private Date documentDate;
    
    private transient MultipartFile[] files;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public SewerageApplicationDetails getApplicationDetails() {
        return applicationDetails;
    }

    public void setApplicationDetails(final SewerageApplicationDetails applicationDetails) {
        this.applicationDetails = applicationDetails;
    }


    public Set<FileStoreMapper> getFileStore() {
        return fileStore;
    }

    public void setFileStore(Set<FileStoreMapper> fileStore) {
        this.fileStore = fileStore;
    }

    public DocumentTypeMaster getDocumentTypeMaster() {
        return documentTypeMaster;
    }

    public void setDocumentTypeMaster(DocumentTypeMaster documentTypeMaster) {
        this.documentTypeMaster = documentTypeMaster;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }
    
    

}
