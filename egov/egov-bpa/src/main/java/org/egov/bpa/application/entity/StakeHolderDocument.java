/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
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
package org.egov.bpa.application.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractAuditable;

@Entity
@Table(name = "EGBPA_STAKEHOLDER_Document")
@SequenceGenerator(name = StakeHolderDocument.SEQ_STAKEHOLDER_DOCUMENT, sequenceName = StakeHolderDocument.SEQ_STAKEHOLDER_DOCUMENT, allocationSize = 1)
public class StakeHolderDocument extends AbstractAuditable {

    private static final long serialVersionUID = 3078684328383202788L;
    public static final String SEQ_STAKEHOLDER_DOCUMENT = "SEQ_EGBPA_STAKEHOLDER_Document";
    @Id
    @GeneratedValue(generator = SEQ_STAKEHOLDER_DOCUMENT, strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Checklist checklist;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stakeHolder")
    private StakeHolder stakeHolder;
    private Boolean isAttached;
    private Long documentId;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public void setChecklist(final Checklist checklist) {
        this.checklist = checklist;
    }

    public StakeHolder getStakeHolder() {
        return stakeHolder;
    }

    public void setStakeHolder(final StakeHolder stakeHolder) {
        this.stakeHolder = stakeHolder;
    }

    public Boolean getIsAttached() {
        return isAttached;
    }

    public void setIsAttached(final Boolean isAttached) {
        this.isAttached = isAttached;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(final Long documentId) {
        this.documentId = documentId;
    }
}