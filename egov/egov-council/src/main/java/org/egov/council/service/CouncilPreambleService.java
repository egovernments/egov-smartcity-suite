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
package org.egov.council.service;

import static org.egov.council.utils.constants.CouncilConstants.ADJOURNED;
import static org.egov.council.utils.constants.CouncilConstants.APPROVED;
import static org.egov.council.utils.constants.CouncilConstants.IMPLEMENTATION_STATUS_FINISHED;
import static org.egov.council.utils.constants.CouncilConstants.MODULE_FULLNAME;
import static org.egov.council.utils.constants.CouncilConstants.REJECTED;
import static org.egov.council.utils.constants.CouncilConstants.RESOLUTION_APPROVED_PREAMBLE;
import static org.egov.infra.utils.ApplicationConstant.ANONYMOUS_USERNAME;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.council.autonumber.PreambleNumberGenerator;
import org.egov.council.autonumber.SumotoNumberGenerator;
import org.egov.council.entity.CouncilPreamble;
import org.egov.council.entity.CouncilSearchRequest;
import org.egov.council.entity.MeetingMOM;
import org.egov.council.entity.enums.PreambleType;
import org.egov.council.repository.CouncilPreambleRepository;
import org.egov.council.service.workflow.PreambleWorkflowCustomImpl;
import org.egov.council.utils.constants.CouncilConstants;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.workflow.entity.StateAware;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class CouncilPreambleService {

	private static final Logger LOGGER = Logger.getLogger(CouncilPreambleService.class);
    private static final String STATUS_CODE = "status.code";
    private static final String PREAMBLE_NUMBER_AUTO = "PREAMBLE_NUMBER_AUTO";

    private final CouncilPreambleRepository councilPreambleRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PreambleWorkflowCustomImpl preambleWorkflowCustomImpl;
    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    protected AutonumberServiceBeanResolver autonumberServiceBeanResolver;
    
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;
    @Autowired
    private UserService userService;
    @Autowired
    protected FileStoreService fileStoreService;

    @Autowired
    public CouncilPreambleService(final CouncilPreambleRepository councilPreambleRepository) {
        this.councilPreambleRepository = councilPreambleRepository;
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Transactional
    public CouncilPreamble create(final CouncilPreamble councilPreamble, Long approvalPosition, String approvalComment,
            String workFlowAction, MultipartFile attachments) {

        if (approvalPosition != null && approvalPosition > 0 && StringUtils.isNotEmpty(workFlowAction))
            preambleWorkflowCustomImpl.createCommonWorkflowTransition(councilPreamble,
                    approvalPosition, approvalComment, workFlowAction);

        if(councilPreamble.isValidApprover()){
        	if (isAutoPreambleNoGenEnabled()){
                PreambleNumberGenerator preamblenumbergenerator = autonumberServiceBeanResolver
                        .getAutoNumberServiceFor(PreambleNumberGenerator.class);
                councilPreamble.setPreambleNumber(preamblenumbergenerator
                        .getNextNumber(councilPreamble));
             }
        	attachFiles(councilPreamble, attachments);
        	councilPreambleRepository.save(councilPreamble);
        }
        return councilPreamble;
    }
    
    @Transactional
    public CouncilPreamble createPreambleAPI(final CouncilPreamble councilPreamble) {

        PreambleNumberGenerator preamblenumbergenerator = autonumberServiceBeanResolver
                .getAutoNumberServiceFor(PreambleNumberGenerator.class);
        councilPreamble.setPreambleNumber(preamblenumbergenerator
                .getNextNumber(councilPreamble));
        
        
        
        councilPreamble.setStatus(egwStatusHibernateDAO
                .getStatusByModuleAndCode(CouncilConstants.PREAMBLE_MODULENAME,
                        CouncilConstants.PREAMBLE_STATUS_CREATED));
        councilPreamble.setType(PreambleType.GENERAL);
        councilPreamble.setCreatedBy(userService.getUserByUsername(ANONYMOUS_USERNAME)) ;
        preambleWorkflowCustomImpl.onCreatePreambleAPI(councilPreamble);
        councilPreambleRepository.save(councilPreamble);
        return councilPreamble;
    }

    @Transactional
    public CouncilPreamble update(final CouncilPreamble councilPreamble, Long approvalPosition, String approvalComment,
            String workFlowAction, MultipartFile attachments) {
        if (approvalPosition != null && StringUtils.isNotEmpty(workFlowAction))
            preambleWorkflowCustomImpl.createCommonWorkflowTransition(councilPreamble, approvalPosition, approvalComment,
                    workFlowAction);
        if(councilPreamble.isValidApprover()){
        	attachFiles(councilPreamble, attachments);
        	councilPreambleRepository.save(councilPreamble);
        }
        return councilPreamble;
    }
    
    private void attachFiles(CouncilPreamble councilPreamble, MultipartFile attachments){
    	if (attachments != null && attachments.getSize() > 0) {
            try {
                councilPreamble.setFilestoreid(fileStoreService.store(
                        attachments.getInputStream(),
                        attachments.getOriginalFilename(),
                        attachments.getContentType(),
                        CouncilConstants.MODULE_NAME));
            } catch (IOException e) {
                LOGGER.error("Error in attaching documents", e);
            }
    	}
    }

    @Transactional
    public CouncilPreamble updateImplementationStatus(final CouncilPreamble councilPreamble) {
        councilPreambleRepository.save(councilPreamble);
        return councilPreamble;
    }

    public CouncilPreamble findOne(Long id) {
        return councilPreambleRepository.findById(id);
    }
    
    public CouncilPreamble findbyPreambleNumber(String preambleNumber) {
        return councilPreambleRepository.findByPreambleNumber(preambleNumber);
    }
    
    public CouncilPreamble findbyReferenceNumber(String referenceNumber) {
        return councilPreambleRepository.findByReferenceNumber(referenceNumber);
    }
    
    public Boolean autoGenerationModeEnabled(final String moduleName, final String keyName) {
        final List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(moduleName, keyName);
        return !appConfigValues.isEmpty() && "YES".equals(appConfigValues.get(0).getValue());
    }
    

    @SuppressWarnings("unchecked")
    public List<CouncilPreamble> searchForPreamble(CouncilSearchRequest councilSearchRequest) {
        final Criteria criteria = buildSearchCriteria(councilSearchRequest);
        criteria.add(Restrictions.in(STATUS_CODE, APPROVED,ADJOURNED));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<CouncilPreamble> searchPreambleForWardwiseReport(CouncilSearchRequest councilSearchRequest) {
        final Criteria criteria = buildSearchCriteria(councilSearchRequest);
        criteria.add(Restrictions.ne(STATUS_CODE,  REJECTED));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<CouncilPreamble> search(CouncilSearchRequest councilSearchRequest) {
        final Criteria criteria = buildSearchCriteria(councilSearchRequest);
        criteria.add(Restrictions.ne(STATUS_CODE,  REJECTED));
        return criteria.list();
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public List<CouncilPreamble> searchFinalizedPreamble(
            CouncilSearchRequest councilSearchRequest) {
        final Criteria criteria = buildSearchCriteria(councilSearchRequest);
        criteria.createAlias("councilPreamble.implementationStatus",
                "implementationStatus", CriteriaSpecification.LEFT_JOIN)
                .add(Restrictions.or(Restrictions
                        .isNull("implementationStatus.code"), Restrictions.ne(
                                "implementationStatus.code",
                                IMPLEMENTATION_STATUS_FINISHED)))
                .add(Restrictions.and(Restrictions.in(STATUS_CODE,
                        RESOLUTION_APPROVED_PREAMBLE )));
        return criteria.list();
    }

    public CouncilPreamble buildSumotoPreamble(MeetingMOM meetingMOM,
            EgwStatus preambleStatus) {
        CouncilPreamble councilPreamble = new CouncilPreamble();
        SumotoNumberGenerator sumotoResolutionNumberGenerator = autonumberServiceBeanResolver
                .getAutoNumberServiceFor(SumotoNumberGenerator.class);
        councilPreamble.setPreambleNumber(sumotoResolutionNumberGenerator
                .getNextNumber(councilPreamble));
        councilPreamble.setStatus(preambleStatus);
        councilPreamble.setDepartment(meetingMOM.getPreamble().getDepartment());
        councilPreamble.setGistOfPreamble(meetingMOM.getPreamble()
                .getGistOfPreamble());
        councilPreamble.setSanctionAmount(meetingMOM.getPreamble()
                .getSanctionAmount());
        councilPreamble.setType(PreambleType.SUMOTO);

        return councilPreamble;

    }
    
	public Criteria buildSearchCriteria(CouncilSearchRequest councilSearchRequest) {
		final Criteria criteria = getCurrentSession().createCriteria(CouncilPreamble.class, "councilPreamble")
				.createAlias("councilPreamble.status", "status");

		if (councilSearchRequest.getDepartment() != null)
			criteria.add(Restrictions.eq("councilPreamble.department.id", councilSearchRequest.getDepartment()));

		if (councilSearchRequest.getFromDate() != null && councilSearchRequest.getToDate() != null) {
			criteria.add(Restrictions.between("councilPreamble.createdDate", councilSearchRequest.getFromDate(),
					DateUtils.addDays(councilSearchRequest.getToDate(), 1)));
		}
		if (councilSearchRequest.getPreambleNumber() != null)
			criteria.add(Restrictions.ilike("councilPreamble.preambleNumber", councilSearchRequest.getPreambleNumber(),
					MatchMode.ANYWHERE));

		if (councilSearchRequest.getWards() != null && !councilSearchRequest.getWards().isEmpty()) {
			criteria.createAlias("councilPreamble.wards", "wards")
					.add(Restrictions.in("wards.id", councilSearchRequest.getWards()));
		}

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria;
	}
   
    public Boolean isAutoPreambleNoGenEnabled() {
        return autoGenerationModeEnabled(
                MODULE_FULLNAME, PREAMBLE_NUMBER_AUTO);
    }
    
    public boolean isApplicationOwner(StateAware state) {
    	return preambleWorkflowCustomImpl.isApplicationOwner(state);
    }

}