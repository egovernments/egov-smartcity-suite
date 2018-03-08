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
package org.egov.commons.dao;

import java.util.Date;
import java.util.List;

import org.egov.commons.Installment;
import org.egov.infra.admin.master.entity.Module;

public interface InstallmentDao {
    public List<Installment> getInsatllmentByModule(Module module);

    public List<Installment> getInsatllmentByModule(Module module, Date year);

    public Installment getInsatllmentByModule(Module module, Date year, Integer installmentNumber);

    public Installment getInsatllmentByModuleForGivenDate(Module module, Date installmentDate);

    public List<Installment> getEffectiveInstallmentsforModuleandDate(Date dateToCompare, int noOfMonths, Module mod);

    public Installment getInsatllmentByModuleForGivenDateAndInstallmentType(Module module, Date installmentDate,
            String installmentType);

    List<Installment> getInstallmentsByModuleForGivenDateAndInstallmentType(Module module, Date installmentDate,
            String installmentType);

    List<Installment> getInstallmentsByModuleAndFromDateAndInstallmentType(Module module, Date fromDate, Date currentDate,
            String installmentType);

    List<Installment> getInstallmentsByModuleBetweenFromDateAndToDateAndInstallmentType(Module module, Date fromDate, Date toDate,
            String installmentType);

    List<Installment> fetchInstallments(final Module module, final Date toInstallmentDate, final int noOfInstallments);

    List<Installment> getAllInstallmentsByModuleAndStartDate(final Module module, final Date installmentDate);

    Installment fetchInstallmentByModuleAndInstallmentNumber(final Module module, final Integer installmentNumber);

    public List<Installment> fetchPreviousInstallmentsInDescendingOrderByModuleAndDate(Module module, Date installmentDate,
            int noOfInstallmentToFetch);

    public Installment getInsatllmentByModuleAndDescription(Module moduleByName, String description);

    List<Installment> fetchNextInstallmentsByModuleAndDate(final Module module, final Date date);

    public List<Installment> getInstallmentsByModuleAndInstallmentYearOrderByInstallmentYearDescending(final Module module,
            final int year);
}
