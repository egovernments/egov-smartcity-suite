/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
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
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.commons;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import static org.egov.commons.CChartOfAccountDetail.SEQ_CHARTOFACCOUNTDETAIL;

@Entity
@Table(name = "CHARTOFACCOUNTDETAIL")
@SequenceGenerator(name = SEQ_CHARTOFACCOUNTDETAIL, sequenceName = SEQ_CHARTOFACCOUNTDETAIL, allocationSize = 1)
@AuditOverrides({
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate")
})
@Audited
public class CChartOfAccountDetail extends AbstractAuditable {

    public static final String SEQ_CHARTOFACCOUNTDETAIL = "SEQ_CHARTOFACCOUNTDETAIL";
    private static final long serialVersionUID = -8517026729631829413L;
    @Id
    @GeneratedValue(generator = SEQ_CHARTOFACCOUNTDETAIL, strategy = GenerationType.SEQUENCE)
    private Long id;

    @AuditJoinTable
    @ManyToOne
    @JoinColumn(name = "glcodeid")
    private CChartOfAccounts glCodeId;

    @ManyToOne
    @JoinColumn(name = "detailtypeid")
    @AuditJoinTable
    private Accountdetailtype detailTypeId;

    public CChartOfAccounts getGlCodeId() {
        return glCodeId;
    }

    public void setGlCodeId(final CChartOfAccounts glCodeId) {
        this.glCodeId = glCodeId;
    }

    public Accountdetailtype getDetailTypeId() {
        return detailTypeId;
    }

    public void setDetailTypeId(final Accountdetailtype detailTypeId) {
        this.detailTypeId = detailTypeId;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

}
