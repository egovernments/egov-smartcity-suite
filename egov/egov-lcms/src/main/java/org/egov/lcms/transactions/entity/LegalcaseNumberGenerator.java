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
package org.egov.lcms.transactions.entity;

import java.util.Calendar;

import javax.persistence.SequenceGenerator;

import org.egov.infra.script.entity.Script;
import org.egov.infstr.services.PersistenceService;

public class LegalcaseNumberGenerator {
	public String getLcNumber(final Legalcase legalcase, final String manualNumber, final Integer caseYear) {
		/*
		 * Script validTransitionScript = scriptService.findAllByNamedQuery(
		 * Script.BY_NAME, LcmsConstants.SCRIPT_LEGALCASE_LCNUMBER).get(0); if
		 * (legalcase.getLcNumberType().equals(
		 * LcmsConstants.LC_NUMBER_AUTOMATED_TYPE)) return (String)
		 * validTransitionScript.eval(Script.createContext( "legalcase",
		 * legalcase, "seqGenerator", seqGenerator, "currentYear",
		 * getCurrentYear())); else if (legalcase.getLcNumberType().equals(
		 * LcmsConstants.LC_NUMBER_OPTIONAL_TYPE)) return (String)
		 * validTransitionScript.eval(Script.createContext( "legalcase",
		 * legalcase, "manualNumber", manualNumber, "caseYear",
		 * caseYear.toString()));
		 */
		return null;
	}

	public String getCaseNumber(final Legalcase legalcase, final String caseNumber) {
		/*
		 * Script validTransitionScript = scriptService.findAllByNamedQuery(
		 * Script.BY_NAME, LcmsConstants.SCRIPT_LEGALCASE_CASENUMBER).get( 0);
		 * return (String) validTransitionScript.eval(Script.createContext(
		 * "legalcase", legalcase, "caseNumber", caseNumber));
		 */
		return null;
	}

	public void setSeqGenerator(final SequenceGenerator seqGenerator) {
	}

	public void setScriptService(final PersistenceService<Script, Long> persistenceService) {
	}

	protected String getCurrentYear() {
		final Calendar cal = Calendar.getInstance();
		return Integer.valueOf(cal.get(Calendar.YEAR)).toString();
	}
}
