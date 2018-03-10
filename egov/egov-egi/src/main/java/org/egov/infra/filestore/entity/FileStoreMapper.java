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

package org.egov.infra.filestore.entity;

import static org.egov.infra.filestore.entity.FileStoreMapper.SEQ_FILESTOREMAPPER;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Table(name = "eg_filestoremap")
@Entity
@SequenceGenerator(name = SEQ_FILESTOREMAPPER, sequenceName = SEQ_FILESTOREMAPPER, allocationSize = 1)
public class FileStoreMapper extends AbstractPersistable<Long> {
    public static final String SEQ_FILESTOREMAPPER = "SEQ_EG_FILESTOREMAP";
    private static final long serialVersionUID = -2997164207274266823L;
    @Id
    @GeneratedValue(generator = SEQ_FILESTOREMAPPER, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Length(max = 36)
    @Column(length = 36, unique = true, nullable = false)
    private String fileStoreId;

    @NotBlank
    @Length(max = 100)
    @SafeHtml
    private String fileName;

    @Length(max = 100)
    @SafeHtml
    private String contentType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();

    protected FileStoreMapper() {
        // For JPA
    }

    public FileStoreMapper(String fileStoreId, String fileName) {
        this.fileStoreId = fileStoreId;
        this.fileName = fileName;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getFileStoreId() {
        return fileStoreId;
    }

    public void setFileStoreId(String fileStoreId) {
        this.fileStoreId = fileStoreId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof FileStoreMapper))
            return false;
        final FileStoreMapper that = (FileStoreMapper) other;
        return that.id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}