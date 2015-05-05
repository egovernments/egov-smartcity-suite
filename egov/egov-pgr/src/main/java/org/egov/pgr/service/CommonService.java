/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
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
package org.egov.pgr.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.exceptions.TooManyValuesException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.services.EISServeable;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.lib.admbndry.HeirarchyTypeDAO;
import org.egov.pims.commons.DesignationMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Deprecated
//FIXME This need to be goes to corresponding services and access directly from it
public class CommonService {

	private static Logger LOG = Logger.getLogger(CommonService.class);

	@Autowired
	private HeirarchyTypeDAO hierHeirarchyTypeDAO;
	@Autowired
	private BoundaryTypeDAO boundaryTypeDAO;
	@Autowired
	private BoundaryDAO boundaryDAO;
	@Autowired
	private EISServeable eisService;

	@SuppressWarnings("unchecked")
	public List<Boundary> getZones() {
		List<Boundary> zones = new ArrayList<Boundary>();
		try {
			final HierarchyType hierarchyTypeByName = hierHeirarchyTypeDAO.getHierarchyTypeByName("ADMINISTRATION");
			final BoundaryType boundaryType = boundaryTypeDAO.getBoundaryType("ZONE", hierarchyTypeByName);
			zones = boundaryDAO.getAllBoundariesByBndryTypeId(boundaryType.getId());
		} catch (final NoSuchObjectException e) {
			LOG.error(e.getMessage());
		} catch (final TooManyValuesException e) {
			LOG.error(e.getMessage());
		}
		return zones;
	}

	public List<Boundary> getWards(final Long zoneId) {

		if (zoneId == null || zoneId <= 0)
			throw new EGOVRuntimeException("Zone or Zoneid is not passed");
		List<Boundary> wards = new ArrayList<Boundary>();
		try {
			wards = boundaryDAO.getChildBoundaries(zoneId);
		} catch (final Exception e) {
			LOG.error(e.getMessage());

		}
		return wards;
	}

	public List<DesignationMaster> getDesignations(final Integer departmentId) {
		return eisService.getAllDesignationByDept(departmentId, new Date());

	}

	public List<User> getPosistions(final Integer departmentId, final Integer designationId) {
		return eisService.getUsersByDeptAndDesig(departmentId, designationId, new Date());
	}

}
