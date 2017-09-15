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

package org.egov.ptis.domain.service.bulkboundaryupdatation;

import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_BULK_BOUNDARY;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;

import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.ptis.bean.BulkBoundaryRequest;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.view.PropertyMVInfo;
import org.egov.ptis.domain.service.property.PropertyPersistenceService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.repository.bulkboundaryupdation.BulkBoundaryUpdationRepository;
import org.egov.ptis.repository.spec.BulkBoundarySpec;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BulkBoundaryService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PropertyPersistenceService basicPropertyService;

	@Autowired
	private PropertyService propService;

	@Autowired
	private BulkBoundaryUpdationRepository bulkBoundaryUpdationRepository;

	public BasicProperty getBasicPropertyByPropertyID(String propertyId) {
		Query qry = null;
		BasicProperty basicProperty = null;
		if (propertyId != null && !propertyId.equals("")) {
			qry = entityManager.unwrap(Session.class)
					.createQuery("from BasicPropertyImpl BP where BP.upicNo =:propertyId and BP.active='Y' ");
			qry.setString("propertyId", propertyId);
			basicProperty = (BasicProperty) qry.uniqueResult();
		}
		return basicProperty;
	}

	@Transactional
	public boolean updateBasicPropertyByBoundary(List<BasicProperty> basicProperties) {
		boolean success = false;
		try {
			for (BasicProperty basicProperty : basicProperties) {
				basicProperty.addPropertyStatusValues(propService.createPropStatVal(basicProperty,
						PROPERTY_MODIFY_REASON_BULK_BOUNDARY, null, null, null, null, null));
				basicPropertyService.update(basicProperty);
				updatePropertyMvInfo(basicProperty);
				success = true;
			}
			if (success) {
				refreshViewPropertyInfo();
			}

		} catch (Exception e) {
			throw new ApplicationRuntimeException("Error occured : " + e.getMessage(), e);
		}
		return success;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public void updatePropertyMvInfo(BasicProperty basicProperty) {
		final ProcedureCall procedureCall = entityManager.unwrap(Session.class)
				.createStoredProcedureCall("update_Boundary_propertymvInfo");
		procedureCall.registerParameter("upicno", String.class, ParameterMode.IN);
		procedureCall.getParameterRegistration("upicno").bindValue(basicProperty.getUpicNo());
		procedureCall.registerParameter("locality", Long.class, ParameterMode.IN);
		procedureCall.getParameterRegistration("locality")
				.bindValue(basicProperty.getPropertyID().getLocality().getId());
		procedureCall.registerParameter("block", Long.class, ParameterMode.IN);
		procedureCall.getParameterRegistration("block").bindValue(basicProperty.getPropertyID().getArea().getId());
		procedureCall.registerParameter("ward", Long.class, ParameterMode.IN);
		procedureCall.getParameterRegistration("ward").bindValue(basicProperty.getPropertyID().getWard().getId());
		procedureCall.registerParameter("electionWard", Long.class, ParameterMode.IN);
		procedureCall.getParameterRegistration("electionWard")
				.bindValue(basicProperty.getPropertyID().getElectionBoundary().getId());
		procedureCall.getOutputs();

	}

	@Transactional
	public void refreshViewPropertyInfo() {
		final ProcedureCall procedureCall = entityManager.unwrap(Session.class)
				.createStoredProcedureCall("refresh_propertymvInfo");
		procedureCall.getOutputs();
	}

	@ReadOnly
	public Page<PropertyMVInfo> pagedBulkBoundaryRecords(final BulkBoundaryRequest bulkBoundaryRequest) {

		return bulkBoundaryUpdationRepository.findAll(
				BulkBoundarySpec.bulkBoundarySpecification(bulkBoundaryRequest),
				new PageRequest(bulkBoundaryRequest.pageNumber(), bulkBoundaryRequest.pageSize(),
						bulkBoundaryRequest.orderDir(), bulkBoundaryRequest.orderBy()));
	}

}
