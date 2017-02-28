<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2016>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<form:form role="form" action="/council/councilmeeting/attendance/update" method="post"
	modelAttribute="councilMeeting" name="councilMeetingform" id="councilMeetingform"
	cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
		<div class="row">
			<div class="col-md-12">
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading">
						<div class="panel-title">Council Committee Members Attendance</div>
					</div>
					<div class="panel-body">
						<div class="form-group">
								<table class="table table-bordered  multiheadertbl" name="councilcommittee"
								id=councilcommittee>
								<thead>
									<tr>
										<th><input type="checkbox" id="committeechk" name="chkbox"/></th> 
										<th>Member Name</th>
										<th>Election Ward</th>
										<th>Designation</th>
										<th>Qualification</th>
										<th>Party Affiliation</th>
									</tr>
								</thead>
								
								<tbody>
								<c:forEach items="${councilMeeting.updateMeetingAttendance}" var="attend" varStatus="counter">
								<c:choose>
									<c:when test="${attend.attendedMeeting}">
										<tr>
											<td>
												<input type="checkbox" name="updateMeetingAttendance[${counter.index}].councilMember" class="councilcommitmem" data-change-to="updateMeetingAttendance[${counter.index}].checked"  id="${attend.councilMember.id}" checked  value="${attend.councilMember.id}"/>
												<input type="hidden"   name="updateMeetingAttendance[${counter.index}].checked"  id="councilcommitmemchk" class="councilcommitmemchk" value="true"/>
												<input type="hidden"   name="updateMeetingAttendance[${counter.index}].checked"  value="${attend.councilMember.id}"/>
												<input type="hidden"   name="updateMeetingAttendance[${counter.index}].councilMember"  value="${attend.councilMember.id}"/>
												<input type="hidden"   name="updateMeetingAttendance[${counter.index}].meeting" value="${councilMeeting.id}" />
											</td>
											<td><c:out value="${attend.councilMember.name}" /></td>
											<td><c:out value="${attend.councilMember.electionWard.name}" /></td>
											<td><c:out value="${attend.councilMember.designation.name}" /></td>
											<td><c:out value="${attend.councilMember.qualification.name}" /></td>	
											<td><c:out value="${attend.councilMember.partyAffiliation.name}" /></td>
									   </tr>
								</c:when>
								<c:otherwise>
								<tr>
											<td>
												<input type="checkbox" name="updateMeetingAttendance[${counter.index}].councilMember" class="councilcommitmem" data-change-to="updateMeetingAttendance[${counter.index}].checked"  id="${attend.councilMember.id}"  value="${attend.councilMember.id}"/>
												<input type="hidden"   name="updateMeetingAttendance[${counter.index}].checked"  id="councilcommitmemchk" class="councilcommitmemchk" value="false"/>
												<input type="hidden"   name="updateMeetingAttendance[${counter.index}].checked"  value="${attend.councilMember.id}"/>
												<input type="hidden"   name="updateMeetingAttendance[${counter.index}].councilMember"  value="${attend.councilMember.id}"/>
												<input type="hidden"   name="updateMeetingAttendance[${counter.index}].meeting" value="${councilMeeting.id}" />
											</td>
											<td><c:out value="${attend.councilMember.name}" /></td>
											<td><c:out value="${attend.councilMember.electionWard.name}" /></td>
											<td><c:out value="${attend.councilMember.designation.name}" /></td>
											<td><c:out value="${attend.councilMember.qualification.name}" /></td>	
											<td><c:out value="${attend.councilMember.partyAffiliation.name}" /></td>
									   </tr>	
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<input type="hidden" name="councilMeeting" value="${councilMeeting.id}" />
								</tbody>
							</table>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="error-msg">
				<div>Note :- </div> <label class="checkbox-inline"><input type="checkbox" value="" checked disabled>Attended</label>
				<label class="checkbox-inline"><input type="checkbox" value="" disabled>Not Attended</label>
			</div>
		<div class="text-center">
		<button  class='btn btn-primary' id="btnsubmit" >
			<spring:message code='lbl.update' />
		</button>
		<button  id="finalizeAttendanceBtn" class='btn btn-primary'><spring:message code='lbl.finalize.attendance'/></button>
		<a href='javascript:void(0)' class='btn btn-default'
			onclick='self.close()'><spring:message code='lbl.close' /></a>
	</div>
</form:form>

<script type="text/javascript"
	src="<cdn:url value='/resources/app/js/councilAttendance.js?rnd=${app_release_no}'/>"></script>
