package org.egov.mrs.application.reports.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.mrs.domain.entity.MarriageCertificate;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MarriageRegistrationReportsService {
	
	 @PersistenceContext
	    private EntityManager entityManager;
	 
	 
	 private Session getCurrentSession() {
	        return entityManager.unwrap(Session.class);
	    }
	 
	 @SuppressWarnings("unchecked")
	    public List<Object[]> searchMarriageRegistrationsForCertificateReport(MarriageCertificate certificate) throws ParseException {
		 
	    	Map<String, String> params = new HashMap<String, String>();
	    	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	
		 final StringBuilder queryStrForRegistration = new StringBuilder(500);
		 queryStrForRegistration.append("Select reg.registrationno,reg.dateofmarriage,reg.applicationdate,reg.rejectionreason,cert.certificateno,cert.certificatetype,cert.certificatedate, brndy.name,(Select concat(concat(concat(app.firstname, ' '), app.middlename, ' '), app.lastname) as hus_name from egmrs_applicant app where app.id = reg.husband),(Select concat(concat(concat(app.firstname, ' '), app.middlename, ' '), app.lastname) as wife_name from egmrs_applicant app where app.id = reg.wife),reg.id");
		 queryStrForRegistration.append(" from egmrs_registration reg, egmrs_certificate cert, eg_boundary brndy,egw_status st");
		 queryStrForRegistration.append(" where reg.zone = brndy.id and reg.status = st.id and st.code in('REGISTERED')  and reg.id = cert.registration and cert.reissue is null ");
		 if(certificate.getRegistration().getZone() != null){
			 queryStrForRegistration.append(" and reg.zone=to_number(:zone,'999999')");
			 params.put("zone", String.valueOf(certificate.getRegistration().getZone().getId()));
		 }
		 
		 if(certificate.getCertificateType() != null && !certificate.getCertificateType().equals("ALL")){
			 queryStrForRegistration.append(" and cert.certificatetype=:certificatetype");
			 params.put("certificatetype", certificate.getCertificateType());
		 }else if(certificate.getCertificateType() != null && certificate.getCertificateType().equals("ALL")){
			 queryStrForRegistration.append(" and cert.certificatetype in('REGISTRATION','REISSUE','REJECTION')");
			
		 }
		 
		 if(certificate.getFromDate() != null && certificate.getToDate() == null){
			 queryStrForRegistration.append(" and cert.certificatedate between to_timestamp(:fromDate,'yyyy-MM-dd HH24:mi:ss') and to_timestamp(:toDate,'YYYY-MM-DD HH24:MI:SS')");
			 params.put("fromDate",sf.format(certificate.getFromDate()).toString());
			 params.put("toDate", certificate.getToDate() != null ? sf.format(certificate.getToDate()).toString() :sf.format(new Date()).toString());
		 }
		 
		 if(certificate.getRegistration().getRegistrationNo() != null){
			 queryStrForRegistration.append(" and reg.registrationno=:registrationNo");
			 params.put("registrationNo", certificate.getRegistration().getRegistrationNo());
		 }
		 
		 final StringBuilder queryStrForReissue = new StringBuilder(500);
		 queryStrForReissue.append("Select reg.registrationno,reg.dateofmarriage,reg.applicationdate,reg.rejectionreason,cert.certificateno,cert.certificatetype,cert.certificatedate, brndy.name,(Select concat(concat(concat(app.firstname, ' '), app.middlename, ' '), app.lastname) as hus_name from egmrs_applicant app where app.id = reg.husband),(Select concat(concat(concat(app.firstname, ' '), app.middlename, ' '), app.lastname) as wife_name from egmrs_applicant app where app.id = reg.wife),reg.id");
		 queryStrForReissue.append(" from egmrs_registration reg,egmrs_reissue reis, egmrs_certificate cert, eg_boundary brndy,egw_status st ");
		 queryStrForReissue.append("where reg.zone = brndy.id and reg.id=reis.registration and reis.status = st.id and st.code in('CERTIFICATEREISSUED','REJECTION')  and reis.id = cert.reissue and cert.registration is null");
		 if(certificate.getRegistration().getZone() != null){
			 queryStrForReissue.append(" and reg.zone=to_number(:zone,'999999')");
			 params.put("zone", String.valueOf(certificate.getRegistration().getZone().getId()));
		 }
		 
		 if(certificate.getCertificateType() != null && !certificate.getCertificateType().equals("ALL")){
			 queryStrForReissue.append(" and cert.certificatetype=:certificatetype");
			 params.put("certificatetype", certificate.getCertificateType());
		 }else if(certificate.getCertificateType() != null && certificate.getCertificateType().equals("ALL")){
			 queryStrForReissue.append(" and cert.certificatetype in('REGISTRATION','REISSUE','REJECTION')");
		 }
		 
		 if(certificate.getFromDate() != null && certificate.getToDate() == null){
			 queryStrForReissue.append(" and cert.certificatedate between to_timestamp(:fromDate,'yyyy-MM-dd HH24:mi:ss') and to_timestamp(:toDate,'YYYY-MM-DD HH24:MI:SS')");
			 params.put("fromDate", sf.format(certificate.getFromDate()).toString());
			 params.put("toDate", certificate.getToDate() != null ? sf.format(certificate.getToDate()).toString() :sf.format(new Date()).toString());
		 }
		 
		 if(certificate.getRegistration().getRegistrationNo() != null){
			 queryStrForReissue.append(" and reg.registrationno=:registrationNo");
			 params.put("registrationNo", certificate.getRegistration().getRegistrationNo());
		 }
		 
		 final StringBuilder aggregateQueryStr = new StringBuilder();
		 aggregateQueryStr.append(queryStrForRegistration.toString());
		 
		 aggregateQueryStr.append(" union ");
		 aggregateQueryStr.append(queryStrForReissue.toString());
		 
		 org.hibernate.Query query =	getCurrentSession().createSQLQuery(aggregateQueryStr.toString());
		 for (String param : params.keySet()) {
	            query.setParameter(param, params.get(param));
	        }
			return query.list();
		 
	 }
	 
}
