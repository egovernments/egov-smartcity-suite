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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.utils.WebUtils;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.domain.autonumber.MarriageCertificateNumberGenerator;
import org.egov.mrs.domain.entity.MarriageCertificate;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.RegistrationCertificate;
import org.egov.mrs.domain.repository.MarriageCertificateRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
		REGISTRATION, REJECTION,REISSUE
	}

	@PersistenceContext
	private EntityManager entityManager;
	 
	@Autowired
	private ReportService reportService;

	@Autowired
	private SecurityUtils securityUtils;
	
	@Autowired
	@Qualifier("fileStoreService")
	protected FileStoreService fileStoreService;
	
	@Autowired
    private MarriageCertificateNumberGenerator marriageCertificateNumberGenerator;
	@Autowired
	private MarriageCertificateRepository marriageCertificateRepository;
	private InputStream generateCertificatePDF;
	
	private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }
	
	/**
	 * Generates Marriage Registration Certificate and returns the reportOutput
	 * 
	 * @param registration
	 * @param certificateType
	 * @param cityName
	 * @param logopath
	 * @return
	 * @throws IOException 
	 */
	public ReportOutput generate(MarriageRegistration registration, CertificateType certificateType, String cityName,
			String logopath) throws IOException {
		ReportRequest reportInput = null;
		ReportOutput reportOutput = null;
		Map<String, Object> reportParams = new HashMap<String, Object>();
		String template = (certificateType == MarriageCertificateService.CertificateType.REGISTRATION
				? CERTIFICATE_TEMPLATE_REGISTRATION : CERTIFICATE_TEMPLATE_REJECTION);
		byte[] husbandPhoto=null,wifePhoto=null;
		reportParams.put("cityName", cityName);
		reportParams.put("certificateDate", new Date());
		reportParams.put("logoPath", logopath);
		if (registration.getWife() != null && registration.getWife().getPhotoFileStore()!= null)
			wifePhoto=FileUtils.readFileToByteArray(fileStoreService.fetch(registration.getWife().getPhotoFileStore(), MarriageConstants.FILESTORE_MODULECODE));
		if (registration.getHusband() != null && registration.getHusband().getPhotoFileStore()!= null)
			 husbandPhoto=FileUtils.readFileToByteArray(fileStoreService.fetch(registration.getHusband().getPhotoFileStore(), MarriageConstants.FILESTORE_MODULECODE));
		
		reportInput = new ReportRequest(template,
				new RegistrationCertificate(registration, securityUtils.getCurrentUser(),husbandPhoto,wifePhoto), reportParams);
		reportOutput = reportService.createReport(reportInput);
		// registration.setCertificateIssued(true);
		// marriageRegistrationService.update(registration);
		return reportOutput;
	}
	
	public MarriageCertificate generateMarriageCertificate(final MarriageRegistration marriageRegistration,
	       final HttpServletRequest request) throws IOException {
	    MarriageCertificate marriageCertificate = null;
	    ReportOutput reportOutput = null;
	    final String cityName = request.getSession().getAttribute("citymunicipalityname").toString();
	    final String url = WebUtils.extractRequestDomainURL(request, false);
	    final String cityLogo = url.concat(MarriageConstants.IMAGE_CONTEXT_PATH)
                    .concat((String) request.getSession().getAttribute("citylogo"));
	  
	    reportOutput = generate(marriageRegistration, CertificateType.REGISTRATION,cityName,cityLogo);
	    if (reportOutput != null && reportOutput.getReportOutputData() != null) {
	        generateCertificatePDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
	        marriageCertificate = saveRegisteredCertificate(marriageRegistration,generateCertificatePDF,request.getSession().getAttribute("cityCode").toString());
	    }
	    return marriageCertificate;
	}
	
	public MarriageCertificate saveRegisteredCertificate(final MarriageRegistration marriageRegistration,
	        final InputStream fileStream,String cityCode) {
	    MarriageCertificate marriageCertificate = new MarriageCertificate();
	    if(marriageRegistration!=null){
	        String certificateNo = marriageCertificateNumberGenerator.generateCertificateNumber(cityCode);
	        final String fileName = certificateNo + ".pdf";
	        buildCertificate(marriageRegistration,marriageCertificate,certificateNo,CertificateType.REGISTRATION.toString());
	        final FileStoreMapper fileStore = fileStoreService.store(fileStream, fileName, "application/pdf",
	                MarriageConstants.FILESTORE_MODULECODE);
	        marriageCertificate.setFileStore(fileStore);
	    }
	    return marriageCertificate;
	}
	
	private void buildCertificate(final MarriageRegistration marriageRegistration,
	        MarriageCertificate marriageCertificate, String certificateNumber,String certificateType) {
	    marriageCertificate.setCertificateDate(new Date());
	    marriageCertificate.setCertificateIssued(true);
	    marriageCertificate.setCertificateNo(certificateNumber);
	    marriageCertificate.setCertificateType(certificateType);
	    marriageCertificate.setRegistration(marriageRegistration);
	}
	
	@SuppressWarnings("unchecked")
   	public List<MarriageCertificate> searchMarriageCertificates(MarriageCertificate certificate) {
   		 Criteria criteria = getCurrentSession().createCriteria(MarriageCertificate.class,"certificate").createAlias("certificate.registration", "registration");
   		if (certificate.getRegistration() != null && certificate.getRegistration().getRegistrationNo() != null)
			criteria.add(Restrictions.ilike("registration.registrationNo", certificate.getRegistration().getRegistrationNo().trim(),MatchMode.ANYWHERE));
   		if (certificate.getCertificateNo() != null)
			criteria.add(Restrictions.ilike("certificateNo", certificate.getCertificateNo().trim(),MatchMode.ANYWHERE));
   		if (certificate.getCertificateType() != null)
			criteria.add(Restrictions.eq("certificateType", certificate.getCertificateType().trim()));
   		if (certificate.getFromDate() != null && certificate.getToDate() != null) {
			criteria.add(Restrictions.between("certificateDate", certificate.getFromDate(),
					DateUtils.addDays(certificate.getToDate(), 1)));
		}
   		if(certificate.getFrequency() != null){
   			if(certificate.getFrequency().equalsIgnoreCase("LATEST")){
   				 criteria.addOrder(Order.desc("createdDate"));
   			}
   		}
   		return criteria.list();
   	}
	
	public MarriageCertificate findById(final long id){
		return marriageCertificateRepository.findOne(id);
	}

}
