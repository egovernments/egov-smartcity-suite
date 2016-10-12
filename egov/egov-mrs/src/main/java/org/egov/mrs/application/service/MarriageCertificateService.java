/* eGov suite of products aim to improve the internal efficiency,transparency,
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

package org.egov.mrs.application.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.RegistrationCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class to generate the Marriage Registration/Reissue Certificate
 * 
 * @author Pradeep
 *
 */
@Service
public class MarriageCertificateService {

	private static final String CERTIFICATE_TEMPLATE_REGISTRATION = "registrationcertificate";
	private static final String CERTIFICATE_TEMPLATE_REJECTION = "rejectioncertificate";

	public enum CertificateType {
		REGISTRATION, REJECTION
	}

	@Autowired
	private ReportService reportService;

	@Autowired
	private SecurityUtils securityUtils;

	/**
	 * Generates Marriage Registration Certificate and returns the reportOutput
	 * 
	 * @param registration
	 * @param certificateType
	 * @param cityName
	 * @param logopath
	 * @return
	 */
	public ReportOutput generate(MarriageRegistration registration, CertificateType certificateType, String cityName,
			String logopath) {
		ReportRequest reportInput = null;
		ReportOutput reportOutput = null;
		Map<String, Object> reportParams = new HashMap<String, Object>();
		String template = (certificateType == MarriageCertificateService.CertificateType.REGISTRATION
				? CERTIFICATE_TEMPLATE_REGISTRATION : CERTIFICATE_TEMPLATE_REJECTION);

		reportParams.put("cityName", cityName);
		reportParams.put("certificateDate", new Date());
		reportParams.put("logoPath", logopath);

		reportInput = new ReportRequest(template,
				new RegistrationCertificate(registration, securityUtils.getCurrentUser()), reportParams);
		reportOutput = reportService.createReport(reportInput);
		// registration.setCertificateIssued(true);
		// marriageRegistrationService.update(registration);
		return reportOutput;
	}

}
