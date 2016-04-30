package org.egov.bpa.services.extd.lettertoparty;

import org.apache.log4j.Logger;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.CMDALetterToPartyExtn;
import org.egov.bpa.models.extd.LetterToPartyExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.masters.CheckListDetailsExtn;
import org.egov.bpa.models.extd.masters.ServiceTypeExtn;
import org.egov.bpa.services.extd.common.BpaNumberGenerationExtnService;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
public class LetterToPartyExtnService extends
		PersistenceService<LetterToPartyExtn, Long> {

	@PersistenceContext
	private EntityManager entityManager;

	protected Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	private Logger LOGGER = Logger.getLogger(getClass());
	private PersistenceService persistenceService;
	private BpaNumberGenerationExtnService bpaNumberGenerationExtnService;

	public RegistrationExtn getRegistrationObjectbyId(Long registrationId) {

		return (RegistrationExtn) persistenceService.find(
				"from RegistrationExtn where id=?", registrationId);
	}

	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public ServiceTypeExtn getServiceTypeById(Long serviceTypeId) {

		return (ServiceTypeExtn) persistenceService.find(
				" from ServiceTypeExtn where id=?", serviceTypeId);
	}

	public List<CheckListDetailsExtn> getCheckListforService(Long serviceTypeId) {

		Criteria checkListDet = getSession().createCriteria(
				CheckListDetailsExtn.class, "checklistdet");
		checkListDet.createAlias("checklistdet.checkList", "checkList");
		checkListDet.createAlias("checkList.serviceType", "servicetype");
		checkListDet.add(Restrictions.eq("servicetype.id", serviceTypeId));
		checkListDet.add(Restrictions.ilike("checkList.checklistType",
				"DOCUMENTATION"));
		return checkListDet.list();
	}

	public LetterToPartyExtn getLatestLetterToPartyForRegObj(
			RegistrationExtn registrationObj) {
		LetterToPartyExtn lettertoParty = null;

		Criteria lpCriteria = getSession().createCriteria(
				LetterToPartyExtn.class, "letterToParty");
		lpCriteria.createAlias("letterToParty.registration", "registration");
		lpCriteria.add(Restrictions.eq("registration", registrationObj));
		lpCriteria.addOrder(Order.desc("letterToParty.id"));
		lpCriteria.setMaxResults(1);

		lettertoParty = (LetterToPartyExtn) lpCriteria.uniqueResult();
		return lettertoParty;
	}

	public CMDALetterToPartyExtn getLatestCMDALetterToPartyForRegObj(
			RegistrationExtn registrationObj) {
		CMDALetterToPartyExtn lettertoParty = null;

		Criteria lpCriteria = getSession().createCriteria(
				CMDALetterToPartyExtn.class, "letterToParty");
		lpCriteria.createAlias("letterToParty.registration", "registration");
		lpCriteria.add(Restrictions.eq("registration", registrationObj));
		lpCriteria.addOrder(Order.desc("letterToParty.id"));
		lpCriteria.setMaxResults(1);

		lettertoParty = (CMDALetterToPartyExtn) lpCriteria.uniqueResult();
		return lettertoParty;
	}

	/*
	 * To Check Approved letter To Party is present for Registration....
	 */
	public LetterToPartyExtn getLatestLetterToPartyForRegnTOUniqueValidate(
			RegistrationExtn registrationObj) {
		LetterToPartyExtn lettertoParty = null;

		Criteria lpCriteria = getSession().createCriteria(
				LetterToPartyExtn.class, "letterToParty");
		lpCriteria.createAlias("letterToParty.registration", "registration");
		lpCriteria.createAlias("registration.egwStatus", "status");
		lpCriteria.add(Restrictions.eq("registration", registrationObj));
		lpCriteria.add(Restrictions.ne("status.code",
				BpaConstants.CREATEDLETTERTOPARTY));
		lpCriteria.add(Restrictions.isNull("letterToParty.sentDate"));
		lpCriteria.add(Restrictions.isNull("letterToParty.isHistory"));
		lpCriteria.addOrder(Order.desc("letterToParty.id"));
		lpCriteria.setMaxResults(1);

		lettertoParty = (LetterToPartyExtn) lpCriteria.uniqueResult();
		return lettertoParty;
	}

	public List<LetterToPartyExtn> getListofLetterToPartyForRegObj(
			RegistrationExtn registrationObj, LetterToPartyExtn letterToParty) {

		Criteria lpCriteria = getSession().createCriteria(
				LetterToPartyExtn.class, "letterToParty");
		lpCriteria.createAlias("letterToParty.registration", "registration");
		lpCriteria.add(Restrictions.eq("registration", registrationObj));
		lpCriteria.add(Restrictions.isNull("isHistory"));
		if (letterToParty != null && letterToParty.getId() != null)
			lpCriteria.add(Restrictions.ne("id", letterToParty.getId()));
		lpCriteria.addOrder(Order.desc("letterToParty.id"));

		return lpCriteria.list();

	}

	public List<CMDALetterToPartyExtn> getListofCMDALetterToPartyForRegObj(
			RegistrationExtn registrationObj,
			CMDALetterToPartyExtn letterToParty) {

		Criteria lpCriteria = getSession().createCriteria(
				CMDALetterToPartyExtn.class, "letterToParty");
		lpCriteria.createAlias("letterToParty.registration", "registration");
		lpCriteria.add(Restrictions.eq("registration", registrationObj));
		lpCriteria.add(Restrictions.isNull("isHistory"));
		if (letterToParty != null && letterToParty.getId() != null)
			lpCriteria.add(Restrictions.ne("id", letterToParty.getId()));
		lpCriteria.addOrder(Order.desc("letterToParty.id"));

		return lpCriteria.list();

	}

	/*
	 * Get API for Comparinng LP sentDate and sysdate . sysdate-LP sentDate<=10
	 * days allow letter to reply action and syadate-LP sentDate>10 allow reject
	 * registration
	 */
	public List<LetterToPartyExtn> getLetterToPartyForRegnByComparingLPSentDateWithSysDateToTenDays(
			RegistrationExtn registrationObj, Date sentDate) {
		StringBuilder stringLp = new StringBuilder();
		stringLp.append("FROM LetterToPartyExtn lpextn where (:todaysdate)-lpextn.sentDate > :ten and lpextn.registration.id= :regnId and isHistory is null");
		Query query = getCurrentSession().createQuery(stringLp.toString());
		query.setDate("todaysdate", new Date());
		query.setString("ten", BpaConstants.DATECOMPARETEN);
		query.setLong("regnId", registrationObj.getId());
		List<LetterToPartyExtn> queryList = (List<LetterToPartyExtn>) query
				.list();
		return queryList;
	}

	public BpaNumberGenerationExtnService getBpaNumberGenerationExtnService() {
		return bpaNumberGenerationExtnService;
	}

	public void setBpaNumberGenerationExtnService(
			BpaNumberGenerationExtnService bpaNumberGenerationService) {
		this.bpaNumberGenerationExtnService = bpaNumberGenerationService;
	}

}