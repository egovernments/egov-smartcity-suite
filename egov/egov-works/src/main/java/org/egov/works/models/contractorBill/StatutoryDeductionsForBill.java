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
package org.egov.works.models.contractorBill;

import org.egov.commons.EgPartytype;
import org.egov.commons.EgwTypeOfWork;
import org.egov.infstr.models.BaseModel;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.works.contractorbill.entity.ContractorBillRegister;

public class StatutoryDeductionsForBill extends BaseModel {

    private static final long serialVersionUID = 8699958489217979541L;
    private EgPartytype subPartyType;
    private EgwTypeOfWork typeOfWork;
    private ContractorBillRegister egBillReg;
    private EgBillPayeedetails egBillPayeeDtls;

    public EgPartytype getSubPartyType() {
        return subPartyType;
    }

    public void setSubPartyType(final EgPartytype subPartyType) {
        this.subPartyType = subPartyType;
    }

    public EgwTypeOfWork getTypeOfWork() {
        return typeOfWork;
    }

    public void setTypeOfWork(final EgwTypeOfWork typeOfWork) {
        this.typeOfWork = typeOfWork;
    }

    public ContractorBillRegister getEgBillReg() {
        return egBillReg;
    }

    public void setEgBillReg(final ContractorBillRegister egBillReg) {
        this.egBillReg = egBillReg;
    }

    public EgBillPayeedetails getEgBillPayeeDtls() {
        return egBillPayeeDtls;
    }

    public void setEgBillPayeeDtls(final EgBillPayeedetails egBillPayeeDtls) {
        this.egBillPayeeDtls = egBillPayeeDtls;
    }

}
