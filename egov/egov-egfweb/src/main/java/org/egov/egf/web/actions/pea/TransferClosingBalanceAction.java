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
package org.egov.egf.web.actions.pea;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.services.pea.TransferClosingBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

@ParentPackage("egov")
@Results({ @Result(name = TransferClosingBalanceAction.NEW, location = "transferClosingBalance-new.jsp") })
public class TransferClosingBalanceAction extends BaseFormAction {

	private static final long serialVersionUID = 7217194113772563333L;

	@Autowired
	@Qualifier("financialYearDAO")
	private FinancialYearHibernateDAO financialYearDAO;

	@Autowired
	@Qualifier("transferClosingBalanceService")
	private TransferClosingBalanceService transferClosingBalanceService;

	private Long financialYear;

	@Override
	public StateAware getModel() {
		return null;

	}

	private CFinancialYear fy;
	private CFinancialYear previousFinancialYear;
	private CFinancialYear nextFinancialYear;

	@Override
	public void prepare() {
		super.prepare();
		addDropdownData("financialYearList",
				financialYearDAO.getAllNotClosedFinancialYears());
	}

	public void prepareNewform() {

	}

	@SkipValidation
	@Action(value = "/pea/transferClosingBalance-new")
	public String newform() {
		return NEW;
	}

	@SkipValidation
	@ValidationErrorPage(value = NEW)
	@Action(value = "/pea/transferClosingBalance-transfer")
	public String transfer() {
		try {

			fy = financialYearDAO.getFinancialYearById(financialYear);

			try {
				previousFinancialYear = financialYearDAO
						.getPreviousFinancialYearByDate(fy.getStartingDate());
			} catch (final ApplicationRuntimeException e) {
				// Ignore

			} catch (final Exception e) {

				final List<ValidationError> errors = new ArrayList<ValidationError>();
				errors.add(new ValidationError("exp", e.getMessage()));
				throw new ValidationException(errors);
			}

			try {
				nextFinancialYear = financialYearDAO
						.getNextFinancialYearByDate(fy.getStartingDate());
			} catch (final ApplicationRuntimeException e) {
				throw new ValidationException(
						"Next Financial Year does not exist in system.",
						"Next Financial Year does not exist in system.");

			} catch (final Exception e) {

				final List<ValidationError> errors = new ArrayList<ValidationError>();
				errors.add(new ValidationError("exp", e.getMessage()));
				throw new ValidationException(errors);
			}

			/*
			 * if (!validatePreviousFinancialYear()) throw new
			 * ValidationException
			 * ("Previous Financial Year is Open, it can not be transferred",
			 * "Previous Financial Year is Open, it can not be transferred");
			 */

			if (nextFinancialYear == null || !nextFinancialYear.getIsActive())
				throw new ValidationException(
						"Next Financial Year is not active",
						"Next Financial Year is not active");

			transferClosingBalanceService.transfer(financialYear, fy,
					nextFinancialYear);

			addActionMessage("Transfer Closing Balance Successful");
		} catch (final ValidationException e) {

			final List<ValidationError> errors = new ArrayList<ValidationError>();
			errors.add(new ValidationError("exp", e.getErrors().get(0)
					.getMessage()));
			throw new ValidationException(errors);
		} catch (final Exception e) {

			final List<ValidationError> errors = new ArrayList<ValidationError>();
			errors.add(new ValidationError("exp", e.getMessage()));
			throw new ValidationException(errors);
		}
		return NEW;
	}

	private boolean validatePreviousFinancialYear() {

		return previousFinancialYear != null ? previousFinancialYear
				.getIsClosed() : true;

	}

	public Long getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(Long financialYear) {
		this.financialYear = financialYear;
	}

	public CFinancialYear getFy() {
		return fy;
	}

	public void setFy(CFinancialYear fy) {
		this.fy = fy;
	}

	public CFinancialYear getPreviousFinancialYear() {
		return previousFinancialYear;
	}

	public void setPreviousFinancialYear(CFinancialYear previousFinancialYear) {
		this.previousFinancialYear = previousFinancialYear;
	}

	public CFinancialYear getNextFinancialYear() {
		return nextFinancialYear;
	}

	public void setNextFinancialYear(CFinancialYear nextFinancialYear) {
		this.nextFinancialYear = nextFinancialYear;
	}

}