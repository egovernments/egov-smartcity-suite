/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.stms.transactions.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.persistence.entity.AbstractAuditable;

@Entity
@Table(name = "egswtax_fieldinspection")
@SequenceGenerator(name = SewerageFieldInspection.SEQ_FIELDINSPECTION, sequenceName = SewerageFieldInspection.SEQ_FIELDINSPECTION, allocationSize = 1)
public class SewerageFieldInspection extends AbstractAuditable {

    /**
     *
     */
    private static final long serialVersionUID = -5214615658016831591L;

    public static final String SEQ_FIELDINSPECTION = "SEQ_EGSWTAX_FIELDINSPECTION";

    @Id
    @GeneratedValue(generator = SEQ_FIELDINSPECTION, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicationdetails", nullable = false)
    private SewerageApplicationDetails applicationDetails;

    private boolean isActive = true;

    @Temporal(value = TemporalType.DATE)
    private Date inspectionDate;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "filestoreid")
    private FileStoreMapper fileStore;

    @OrderBy("id desc")
    @OneToMany(mappedBy = "fieldInspection", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SewerageFieldInspectionDetails> fieldInspectionDetails = new ArrayList<SewerageFieldInspectionDetails>(
            0);
    @Transient
    private List<SewerageFieldInspectionDetails> fieldInspectionDetailsForUpdate = new ArrayList<SewerageFieldInspectionDetails>(
            0);
    
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(final boolean isActive) {
        this.isActive = isActive;
    }

    public Date getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(final Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public List<SewerageFieldInspectionDetails> getFieldInspectionDetails() {
        return fieldInspectionDetails;
    }

    public void setFieldInspectionDetails(final List<SewerageFieldInspectionDetails> fieldInspectionDetails) {
        this.fieldInspectionDetails = fieldInspectionDetails;
    }

    public SewerageApplicationDetails getApplicationDetails() {
        return applicationDetails;
    }

    public void setApplicationDetails(final SewerageApplicationDetails applicationDetails) {
        this.applicationDetails = applicationDetails;
    }

    public FileStoreMapper getFileStore() {
        return fileStore;
    }

    public void setFileStore(final FileStoreMapper fileStore) {
        this.fileStore = fileStore;
    }

    public List<SewerageFieldInspectionDetails> getFieldInspectionDetailsForUpdate() {
        return fieldInspectionDetailsForUpdate;
    }

    public void setFieldInspectionDetailsForUpdate(List<SewerageFieldInspectionDetails> fieldInspectionDetailsForUpdate) {
        this.fieldInspectionDetailsForUpdate = fieldInspectionDetailsForUpdate;
    }

}
