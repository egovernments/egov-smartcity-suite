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

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintRouter;
import org.egov.pgr.repository.ComplaintRouterRepository;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComplaintRouterService {

    private final ComplaintRouterRepository complaintRouterRepository;

    @Autowired
    public ComplaintRouterService(final ComplaintRouterRepository complaintRouterRepository) {
        this.complaintRouterRepository = complaintRouterRepository;
    }

    /**
     * @param complaint
     * @return This api takes responsibility of returning suitable position for
     *         the given complaint Api considers two fields from complaint a.
     *         complaintType b. Boundary The descision is taken as below 1. If
     *         complainttype and boundary from complaint is found in router then
     *         return corresponding position 2. If only complainttype from
     *         complaint is found search router for matching entry in router and
     *         return position 3. If no postion found for above then search
     *         router with only boundary of given complaint and return
     *         corresponding position 4. If none of the above gets position then
     *         return GO 5. GO is default for all complaints. It expects the
     *         data in the following format Say ComplaintType CT1,CT2,CT3
     *         Present with CT1 locationRequired is true Boundary B1 to B5 are
     *         child boundaries and B0 is the top boundary (add only child
     *         boundaries not the top or middle ones) Postion P1 to P10 are
     *         different positions then ComplaintRouter is populate like this
     *         ComplaintType Boundary Position
     *         ===================================================== 1. CT1 B1
     *         P1 2. CT1 B2 P2 3. CT1 B3 P3 4. CT1 B4 P4 5. CT1 B5 P5 6. CT1
     *         null P6 This is complaintType default 7. null B5 P7 This is
     *         Boundary default 8. null B0 P8 This is GO. he is city level
     *         default. This data is mandatory . Line 6 and 7 are exclusive
     *         means if 6 present 7 will not be considered . If you want
     *         boundary level default then dont add complaint type default
     *         search result complaint is registered with complaint type CT1 and
     *         boundary B1 will give P1 CT1 and Boundary is not provided will
     *         give p6, if line 6 not added then it will give P8
     */
    public Position getAssignee(final Complaint complaint) {
        Position position = null;
        ComplaintRouter complaintRouter = null;
        if (null != complaint.getComplaintType() && null != complaint.getLocation())
            complaintRouter = complaintRouterRepository.findByComplaintTypeAndBoundary(complaint.getComplaintType(),
                    complaint.getLocation());
        if (null == complaintRouter && null != complaint.getLocation())
            complaintRouter = complaintRouterRepository.findByBoundary(complaint.getLocation());

        if (null == complaintRouter && null != complaint.getComplaintType())
            complaintRouter = complaintRouterRepository.findByOnlyComplaintType(complaint.getComplaintType());

        if (complaintRouter == null)
            complaintRouter = complaintRouterRepository.findGrievanceOfficer();

        if (complaintRouter != null)
            position = complaintRouter.getPosition();
        else
            throw new EGOVRuntimeException("PGR.001");

        return position;
    }

    @Transactional
    public ComplaintRouter createComplaintRouter(final ComplaintRouter complaintRouter) {
        return complaintRouterRepository.save(complaintRouter);
    }

    @Transactional
    public ComplaintRouter updateComplaintRouter(final ComplaintRouter complaintRouter) {
        return complaintRouterRepository.save(complaintRouter);
    }

    @Transactional
    public void deleteComplaintRouter(final ComplaintRouter complaintRouter) {
        complaintRouterRepository.delete(complaintRouter);
    }

    public Boolean validateRouter(final ComplaintRouter complaintRouter) {
        Boolean exist = false;
        ComplaintRouter queryResult = null;
        if (null != complaintRouter.getComplaintType() && null != complaintRouter.getBoundary()
                && null != complaintRouter.getPosition())
            queryResult = complaintRouterRepository.findByComplaintTypeAndBoundaryAndPosition(
                    complaintRouter.getComplaintType(), complaintRouter.getBoundary(), complaintRouter.getPosition());
        if (null != complaintRouter.getPosition() && null != complaintRouter.getBoundary()
                && null == complaintRouter.getComplaintType())
            queryResult = complaintRouterRepository.findByBoundaryAndPosition(complaintRouter.getBoundary(),
                    complaintRouter.getPosition());
        if (null != complaintRouter.getComplaintType() && null != complaintRouter.getPosition()
                && null == complaintRouter.getBoundary())
            queryResult = complaintRouterRepository.findByComplaintTypeAndPosition(complaintRouter.getComplaintType(),
                    complaintRouter.getPosition());
        if (queryResult != null)
            exist = true;
        return exist;
    }

    public ComplaintRouter getRouterById(final Long id) {
        return complaintRouterRepository.findOne(id);
    }

    public Page<ComplaintRouter> getPageOfRouters(final Integer pageNumber, final Integer pageSize,
            final Long boundaryTypeId, final Long complaintTypeId, final Long boundaryId) {
        final Pageable pageable = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "position");
        if (boundaryId != 0 && complaintTypeId != 0 && boundaryTypeId != 0)
            return complaintRouterRepository.findRoutersByComplaintTypeAndBoundaryTypeAndBoundary(complaintTypeId,
                    boundaryTypeId, boundaryId, pageable);
        else if (boundaryTypeId != 0 && boundaryId == 0 && complaintTypeId != 0)
            return complaintRouterRepository.findRoutersByComplaintTypeAndBoundaryType(complaintTypeId, boundaryTypeId,
                    pageable);
        else if (boundaryTypeId != 0 && boundaryId != 0 && complaintTypeId == 0)
            return complaintRouterRepository.findRoutersByBoundaryAndBoundaryType(boundaryTypeId, boundaryId, pageable);
        else if (boundaryTypeId != 0 && boundaryId == 0 && complaintTypeId == 0)
            return complaintRouterRepository.findRoutersByBoundaryType(boundaryTypeId, pageable);
        else if (boundaryTypeId == 0 && boundaryId == 0 && complaintTypeId != 0)
            return complaintRouterRepository.findRoutersByComplaintType(complaintTypeId, pageable);
        else
            return complaintRouterRepository.findRoutersByAll(pageable);
    }
}
