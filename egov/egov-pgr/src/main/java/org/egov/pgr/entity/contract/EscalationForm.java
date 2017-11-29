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

package org.egov.pgr.entity.contract;

import org.egov.eis.entity.PositionHierarchy;
import org.egov.pgr.entity.ComplaintRouter;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.Escalation;
import org.egov.pims.commons.Position;

import java.util.ArrayList;
import java.util.List;

public class EscalationForm {
    private ComplaintType complaintType;
    private List<Escalation> escalationList = new ArrayList<Escalation>();
    private List<PositionHierarchy> positionHierarchyList = new ArrayList<PositionHierarchy>();
    private ComplaintRouter complaintRouter;
    private Position  position;
    
    
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public ComplaintRouter getComplaintRouter() {
        return complaintRouter;  
    }

    public void setComplaintRouter(final ComplaintRouter complaintRouter) {
        this.complaintRouter = complaintRouter;
    }

    public ComplaintType getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(final ComplaintType complaintType) {
        this.complaintType = complaintType;
    }

    public List<Escalation> getEscalationList() {
        return escalationList;
    }

    public void setEscalationList(final List<Escalation> escalationList) {
        this.escalationList = escalationList;
    }

    public void addEscalationList(final Escalation escalationObj) {
        escalationList.add(escalationObj);
    }

    public List<PositionHierarchy> getPositionHierarchyList() {
        return positionHierarchyList;
    }
    public void  addPositionHierarchyList(final PositionHierarchy positionHierarchyObj) {
        positionHierarchyList.add(positionHierarchyObj);
    }
    public void setPositionHierarchyList(final List<PositionHierarchy> positionHierarchyList) {
        this.positionHierarchyList = positionHierarchyList;
    }

}
