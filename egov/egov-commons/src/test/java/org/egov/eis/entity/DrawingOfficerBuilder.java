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
package org.egov.eis.entity;

import org.egov.commons.Bank;
import org.egov.commons.Bankbranch;
import org.egov.pims.commons.Position;

import java.lang.reflect.Field;

public class DrawingOfficerBuilder {

    private final DrawingOfficer drawingOfficer;

    private static long count = 0;

    public DrawingOfficerBuilder() {
        drawingOfficer = new DrawingOfficer();
        count++;
    }

    public DrawingOfficer build() {
        return drawingOfficer;
    }

    public DrawingOfficerBuilder withName(final String name) {
        drawingOfficer.setName(name);
        return this;
    }

    public DrawingOfficerBuilder withCode(final String code) {
        drawingOfficer.setCode(code);
        return this;
    }

    public DrawingOfficerBuilder withBand(final Bank bank) {
        drawingOfficer.setBank(bank);
        return this;
    }

    public DrawingOfficerBuilder withBankbranch(final Bankbranch bankBranch) {
        drawingOfficer.setBankBranch(bankBranch);
        return this;
    }

    public DrawingOfficerBuilder withPosition(final Position position) {
        drawingOfficer.setPosition(position);
        return this;
    }

    public DrawingOfficerBuilder withAccountNumber(final String accountNumber) {
        drawingOfficer.setAccountNumber(accountNumber);
        return this;
    }

    public DrawingOfficerBuilder withTan(final String tan) {
        drawingOfficer.setTan(tan);
        return this;
    }

    public DrawingOfficerBuilder withId(final long id) {
        try {
            final Field idField = drawingOfficer.getClass().getSuperclass()
                    .getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(drawingOfficer, id);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public DrawingOfficerBuilder withDefaults() {
        if (null == drawingOfficer.getId())
            withId(count);

        return this;
    }
}
