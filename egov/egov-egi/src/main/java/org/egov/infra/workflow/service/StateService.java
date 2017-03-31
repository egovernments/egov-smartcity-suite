/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.workflow.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.contract.StateModel;
import org.egov.infra.workflow.repository.StateRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StateService {

	private final StateRepository stateRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public StateService(final StateRepository stateRepository) {
		this.stateRepository = stateRepository;
	}

	public boolean isPositionUnderWorkflow(final Long posId, final Date givenDate) {
		return stateRepository.countByOwnerPosition_IdAndCreatedDateGreaterThanEqual(posId, givenDate) > 0;
	}

	public List<String> getAssignedWorkflowTypeNames(final List<Long> ownerIds) {
		return stateRepository.findAllTypeByOwnerAndStatus(ownerIds);
	}

	public State getStateById(final Long id) {
		return stateRepository.findOne(id);
	}

	@Transactional
	public State create(final State state) {
		return stateRepository.save(state);
	}

	@Transactional
	public State update(final State state) {
		return stateRepository.save(state);
	}

	public Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	public List<StateModel> getStates(List<Long> ownerIds, List<String> types, Long userId) {
		String tenantId = ApplicationThreadLocals.getTenantID();
		List<Object[]> result = getSession()
				.createSQLQuery(new StringBuilder()
						.append("select state.id as id , state.type as type , state.value as value ,state.comments as comments ,state.createdDate as createdDate,state.natureofTask as natureOfTask, state.extraInfo as extraInfo,state.senderName as senderName, state.myLinkId as myLinkId from microservice.eg_wf_states state where state.tenantid =:tenantId and state.type in (:types) and state.owner_pos in(:ownerIds) and state.status <> 2 and not(state.status = 0 and state.createdby =:createdBy) order by state.createddate desc")
						.toString())
				.setParameterList("types", types).setParameterList("ownerIds", ownerIds).setLong("createdBy", userId)
				.setString("tenantId", tenantId).list();

		return populateStates(result);

	}

	private List<StateModel> populateStates(List<Object[]> result) {
		List<StateModel> states = new ArrayList<StateModel>();
		StateModel state;
		for (final Object[] element : result) {
			state = new StateModel();
			state.setId(Long.valueOf(element[0] != null ? element[0].toString() : "0"));
			state.setType(element[1] != null ? element[1].toString() : "");
			state.setValue(element[2] != null ? element[2].toString() : "");
			state.setComments(element[3] != null ? element[3].toString() : "");
			if (element[4] != null){
				try {
					state.setCreatedDate(new SimpleDateFormat("yyyy-MM-dd").parse(element[4].toString()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			state.setNatureOfTask(element[5] != null ? element[5].toString() : "");
			state.setExtraInfo(element[6] != null ? element[6].toString() : "");
			state.setSenderName(element[7] != null ? element[7].toString() : "");
			state.setMyLinkId(
					element[8] != null ? element[8].toString().replaceAll(":ID", state.getId().toString()) : "");
			states.add(state);
		}
		return states;
	}
}