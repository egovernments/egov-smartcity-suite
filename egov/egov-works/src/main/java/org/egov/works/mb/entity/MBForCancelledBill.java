/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */
package org.egov.works.mb.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.works.contractorbill.entity.ContractorBillRegister;

@Entity
@Table(name = "EGW_CANCELLED_BILL")
@NamedQueries({
        @NamedQuery(name = MBForCancelledBill.MBLIST_FOR_CANCELLEDBILL, query = " select mbcb.mbHeader from MBForCancelledBill mbcb where  mbcb.mbHeader.isLegacyMB = false and  mbcb.contractorBillRegister.id = ? and mbcb.contractorBillRegister.status.code = ? ") })
@SequenceGenerator(name = MBForCancelledBill.SEQ_EGW_CANCELLEDBILL, sequenceName = MBForCancelledBill.SEQ_EGW_CANCELLEDBILL, allocationSize = 1)
public class MBForCancelledBill extends AbstractAuditable {

    private static final long serialVersionUID = -6540546979562987332L;

    public static final String SEQ_EGW_CANCELLEDBILL = "SEQ_EGW_CANCELLED_BILL";

    public static final String MBLIST_FOR_CANCELLEDBILL = "getMBListForCancelledBill";

    @Id
    @GeneratedValue(generator = SEQ_EGW_CANCELLEDBILL, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mbheader", nullable = false)
    private MBHeader mbHeader;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractorbillregister", nullable = false)
    private ContractorBillRegister contractorBillRegister;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public MBHeader getMbHeader() {
        return mbHeader;
    }

    public void setMbHeader(final MBHeader mbHeader) {
        this.mbHeader = mbHeader;
    }

    public ContractorBillRegister getContractorBillRegister() {
        return contractorBillRegister;
    }

    public void setContractorBillRegister(final ContractorBillRegister contractorBillRegister) {
        this.contractorBillRegister = contractorBillRegister;
    }

}