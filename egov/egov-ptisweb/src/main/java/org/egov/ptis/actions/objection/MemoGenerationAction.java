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
package org.egov.ptis.actions.objection;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.client.model.PropertyBillInfo;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.objection.RevisionPetition;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * when the objection is accepted this will get invoke to show memo PDF
 * 
 * @author suhasini
 * 
 */

@SuppressWarnings("serial")
public class MemoGenerationAction extends BaseFormAction {
	private final Logger LOGGER = Logger.getLogger(MemoGenerationAction.class);
	private RevisionPetition objection = new RevisionPetition();
	private static final String OBJECTIONMEMO = "ObjectionMemo";
	private static final String PROPERTYSATUSFORMEMO = "getPropertySatusForMemo";
	private PersistenceService<RevisionPetition, Long> objectionService;
	protected ReportService reportService;
	private Map<String, Map<String, BigDecimal>> reasonwiseDues;
	private String reportId;
	public static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
	@Autowired
	private ReportViewerUtil reportViewerUtil;

	@Override
	public Object getModel() {

		return null;
	}

	@Override
	public void prepare() {

		if (null != objection.getId()) {
			objection = objectionService.findById(objection.getId(), false);
		}
	}

	public PersistenceService<RevisionPetition, Long> getObjectionService() {
		return objectionService;
	}

	public void setObjectionService(PersistenceService<RevisionPetition, Long> objectionService) {
		this.objectionService = objectionService;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	@SkipValidation
	public String print() {
		LOGGER.debug("Memo Print Started...");
		PropertyTaxUtil propertyTaxUtil = new PropertyTaxUtil();
		// checking to see the active property's mutation is is objection
		if (objection.getBasicProperty().getProperty().getPropertyDetail().getPropertyMutationMaster().getCode()
				.equalsIgnoreCase(PropertyTaxConstants.MUTATIONRS_OBJECTION_CODE)) {

			List obj = persistenceService.findAllByNamedQuery(PROPERTYSATUSFORMEMO, objection.getBasicProperty()
					.getUpicNo(), PropertyTaxConstants.PROPERTY_MODIFY_REASON_MODIFY, objection.getDateOfOutcome());
			if (obj != null && !obj.isEmpty()) {
				reasonwiseDues = propertyTaxUtil.getDemandDues(objection.getBasicProperty().getUpicNo());
				PropertyBillInfo propertyBillInfo = new PropertyBillInfo(reasonwiseDues, objection.getBasicProperty(),
						null);
				ReportRequest reportRequest = new ReportRequest(OBJECTIONMEMO, propertyBillInfo, getParamMap());
				reportRequest.setPrintDialogOnOpenReport(true);
				ReportOutput reportOutput = reportService.createReport(reportRequest);
				reportId = addingReportToSession(reportOutput);
			}
		} else {
			addActionMessage("Can not generate Memo since Bill Not Generated");
		}
		LOGGER.debug("Memo Print Ended...");
		return "print";
	}

	private Map<String, Object> getParamMap() {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("date", dateFormat.format(new Date()));
		paramMap.put("objectionNo", objection.getObjectionNumber());
		paramMap.put("dateOfOutcome", dateFormat.format(objection.getDateOfOutcome()));
		paramMap.put("objectionDate", dateFormat.format(objection.getRecievedOn()));
		Boundary zone = objection.getBasicProperty().getBoundary().getParent();
		paramMap.put("zoneNo", zone != null ? zone.getBoundaryNum().toString() : "");
		paramMap.put("owner", objection.getBasicProperty().getFullOwnerName());
		paramMap.put("memoNo", propertyTaxNumberGenerator.generateMemoNumber());

		return paramMap;
	}

	protected String addingReportToSession(ReportOutput reportOutput) {
		return reportViewerUtil.addReportToTempCache(reportOutput);
	}

	public RevisionPetition getObjection() {
		return objection;
	}

	public void setObjection(RevisionPetition objection) {
		this.objection = objection;
	}

	public String getReportId() {
		return reportId;
	}

	public void setPropertyTaxNumberGenerator(PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
		this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
	}

}
