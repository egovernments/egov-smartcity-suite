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

package org.egov.mrs.domain.service;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.mrs.application.service.RegistrationDemandService;
import org.egov.mrs.application.service.workflow.RegistrationWorkflowService;
import org.egov.mrs.domain.entity.Applicant;
import org.egov.mrs.domain.entity.Registration;
import org.egov.mrs.domain.entity.SearchModel;
import org.egov.mrs.domain.enums.ApplicationStatus;
import org.egov.mrs.domain.repository.RegistrationRepository;
import org.egov.mrs.masters.entity.Fee;
import org.egov.mrs.masters.service.ActService;
import org.egov.mrs.masters.service.FeeService;
import org.egov.mrs.masters.service.ReligionService;
import org.egov.mrs.utils.MarriageRegistrationNoGenerator;
import org.elasticsearch.common.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    private Session getCurrentSession() {
            return entityManager.unwrap(Session.class);
    }

    @Autowired
    private ReligionService religionService;

    @Autowired
    private ActService actService;

    @Autowired
    private FeeService feeService;

    @Autowired
    private RegistrationDemandService registrationDemandService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;

    @Autowired
    private RegistrationWorkflowService workflowService;

    @Autowired
    private MarriageRegistrationNoGenerator registrationNoGenerator;

    @Autowired
    public RegistrationService(final RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    @Transactional
    public void create(final Registration registration) {
        registrationRepository.save(registration);
    }

    @Transactional
    public Registration update(final Registration registraion) {
        return registrationRepository.saveAndFlush(registraion);
    }

    public Registration get(Long id) {
        return registrationRepository.findById(id);
    }
    
    public Registration get(String registrationNo) {
        return registrationRepository.findByRegistrationNo(registrationNo);
    }

    @Transactional
    public void createRegistration(final Registration registration, WorkflowContainer workflowContainer) {

        if (StringUtils.isBlank(registration.getApplicationNo()))
            registration.setApplicationNo(applicationNumberGenerator.generate());

        registration.getHusband().setReligion(religionService.getProxy(registration.getHusband().getReligion().getId()));
        registration.getWife().setReligion(religionService.getProxy(registration.getWife().getReligion().getId()));
        registration.getWitnesses().forEach(witness -> witness.setRegistration(registration));
        registration.setMarriageAct(actService.getAct(registration.getMarriageAct().getId()));
        // final Fee fee = feeService.getFeeForDate(registration.getDateOfMarriage());
        final Fee fee = feeService.getFee(1L);
        registration.setFeeCriteria(fee.getCriteria());
        registration.setFeePaid(fee.getFees());
        registration.setDemand(registrationDemandService.createDemand(new BigDecimal(fee.getFees())));
        registration.setStatus(ApplicationStatus.Created);

        if (registration.getPriest().getReligion().getId() != null)
            registration.getPriest().setReligion(religionService.getProxy(registration.getPriest().getReligion().getId()));
        else
            registration.setPriest(null);

        registration.setZone(boundaryService.getBoundaryById(registration.getZone().getId()));

        workflowService.transition(registration, workflowContainer, registration.getApprovalComent());

        create(registration);
    }

    @Transactional
    public Registration forwardRegistration(Long id, WorkflowContainer workflowContainer) {
        Registration registration = get(id);
        workflowService.transition(registration, workflowContainer, registration.getApprovalComent());
        return update(registration);
    }

    @Transactional
    public Registration approveRegistration(Long id, WorkflowContainer workflowContainer) {
        Registration registration = get(id);
        registration.setStatus(ApplicationStatus.Approved);
        registration.setRegistrationNo(registrationNoGenerator.generateRegistrationNo());
        workflowService.transition(registration, workflowContainer, registration.getApprovalComent());

        return update(registration);
    }

    @Transactional
    public Registration rejectRegistration(Long id, WorkflowContainer workflowContainer) {
        Registration registration = get(id);
        // Capture the reason for rejection
        registration.setStatus(ApplicationStatus.Rejected);
        registration.setRejectionReason(workflowContainer.getApproverComments());
        workflowService.transition(registration, workflowContainer, registration.getApprovalComent());

        return update(registration);
    }
    
    public List<Registration> getRegistrations() {
        return registrationRepository.findAll();
    }

    public List<Registration> searchRegistration(SearchModel searchModel) {
        Criteria criteria = getCurrentSession().createCriteria(Registration.class, "registration");
        Criteria husbandCriteria = getCurrentSession().createCriteria(Applicant.class, "husband");
        
        if (StringUtils.isNotBlank(searchModel.getRegistrationNo()))
            criteria.add(Restrictions.eq("registrationNo", searchModel.getRegistrationNo()));
        
        if (searchModel.getDateOfMarriage() != null)
            criteria.add(Restrictions.eq("dateOfMarriage", searchModel.getDateOfMarriage()));
        
       // criteria.add(Restrictions.eq("husbandName.firstName", searchModel.getHusbandName()));
        
        if (StringUtils.isNotBlank(searchModel.getHusbandName())) 
            criteria.createCriteria("husband").add(Restrictions.like("name.firstName", searchModel.getHusbandName()));
        
        if (StringUtils.isNotBlank(searchModel.getWifeName()))
            criteria.createCriteria("wife").add(Restrictions.like("name.firstName", searchModel.getWifeName()));
        
        return criteria.list();
    }

    /*private static class RegistrationExpressions {
        public static Expression withRegistrationNo(String registrationNo) {
            return QRegistration.registration.registrationNo.eq(registrationNo);
        }

        public static Expression withDateOfMarrige(Date dateOfMarriage) {
            return QRegistration.registration.dateOfMarriage.eq(dateOfMarriage);
        }
    }*/

}
