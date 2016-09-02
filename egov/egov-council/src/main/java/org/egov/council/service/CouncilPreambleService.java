/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2016>  eGovernments Foundation
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
package org.egov.council.service;
import static org.egov.council.utils.constants.CouncilConstants.ADJOURNED;
import static org.egov.council.utils.constants.CouncilConstants.APPROVED;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.EgwStatus;
import org.egov.council.autonumber.SumotoNumberGenerator;
import org.egov.council.entity.CouncilPreamble;
import org.egov.council.entity.MeetingMOM;
import org.egov.council.entity.enums.PreambleType;
import org.egov.council.repository.CouncilPreambleRepository;
import org.egov.council.service.workflow.PreambleWorkflowCustomImpl;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouncilPreambleService {

    private final CouncilPreambleRepository CouncilPreambleRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PreambleWorkflowCustomImpl preambleWorkflowCustomImpl;
    
    @Autowired
    protected AutonumberServiceBeanResolver autonumberServiceBeanResolver;
    
    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public CouncilPreambleService(final CouncilPreambleRepository CouncilPreambleRepository) {
        this.CouncilPreambleRepository = CouncilPreambleRepository;
    }

    @Transactional
    public CouncilPreamble create(final CouncilPreamble councilPreamble, Long approvalPosition, String approvalComment, String workFlowAction) {
       
        if (approvalPosition != null && approvalPosition > 0 && StringUtils.isNotEmpty(workFlowAction))
            preambleWorkflowCustomImpl.createCommonWorkflowTransition(councilPreamble,
                    approvalPosition, approvalComment,workFlowAction);
        
         CouncilPreambleRepository.save(councilPreamble);
         return councilPreamble;
    }

    @Transactional
    public CouncilPreamble update(final CouncilPreamble councilPreamble, Long approvalPosition, String approvalComment,
            String workFlowAction) {
        if (approvalPosition != null && StringUtils.isNotEmpty(workFlowAction))
            preambleWorkflowCustomImpl.createCommonWorkflowTransition(councilPreamble, approvalPosition,approvalComment, workFlowAction);
        CouncilPreambleRepository.save(councilPreamble);
        return councilPreamble;
    }

    public CouncilPreamble findOne(Long id) {
        return CouncilPreambleRepository.findById(id);
    }

    @SuppressWarnings("unchecked")
    public List<CouncilPreamble> searchForPreamble(CouncilPreamble councilPreamble) {
        final Criteria criteria = buildSearchCriteria(councilPreamble);
        criteria.add(Restrictions.in("status.code", new String[] { APPROVED, ADJOURNED }));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<CouncilPreamble> search(CouncilPreamble councilPreamble) {
        final Criteria criteria = buildSearchCriteria(councilPreamble);
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
    
    public Criteria buildSearchCriteria(CouncilPreamble councilPreamble) {
        final Criteria criteria = getCurrentSession().createCriteria(CouncilPreamble.class, "councilPreamble")
                .createAlias("councilPreamble.status", "status");

        if (null != councilPreamble.getDepartment())
            criteria.add(Restrictions.eq("councilPreamble.department", councilPreamble.getDepartment()));

        if (null!=councilPreamble.getFromDate() && null!=councilPreamble.getToDate()) {
            criteria.add(Restrictions.between("councilPreamble.createdDate", councilPreamble.getFromDate(),
                    DateUtils.addDays(councilPreamble.getToDate(), 1)));
        }
        if (null != councilPreamble.getPreambleNumber())
            criteria.add(Restrictions.ilike("councilPreamble.preambleNumber", councilPreamble.getPreambleNumber(),MatchMode.ANYWHERE));


        return criteria;
    }

}