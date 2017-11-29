<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


	<div class="main-content">
		<div class="row">
			<div class="col-md-12">
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading">
						<div class="panel-title">Council Committee Members</div>
					</div>
					<div class="panel-body">
						<div class="form-group">
						
								<table class="table table-bordered  multiheadertbl" name="councilcommittee"
								id=councilcommittee>
								<thead>
									<tr>
										<th><input type="checkbox" id="committeechk" name="chkbox" <c:if test="${isSelectedAll}"> checked="checked" </c:if>/></th> 
										<th>Member Name</th>
										<th>Election Ward</th>
										<th>Designation</th>
										<th>Qualification</th>
										<th>Party Affiliation</th>
										<th>Status</th>
									</tr>
								</thead>
								
								<tbody>
									<c:set var="index" value="0"/>
									
									<c:forEach items="${councilMembers}" var="councilmem" varStatus="counter">
										<tr>
											<td>
												<c:choose>
													<c:when test="${councilmem.checked}">
														<input type="checkbox"  name="commiteemembers[${index}].councilMember" class="councilcommitmem" data-change-to="commiteemembers[${index}].councilMember.checked" checked="checked"
														  id="${councilmem.id}"  value="${councilmem.id}"/>
														<input type="hidden" name="commiteemembers[${index}].councilMember.checked"  id="councilcommitmemchk" class="councilcommitmemchk" value="true"/>
													</c:when>
													<c:otherwise>
														<input type="checkbox"  name="commiteemembers[${index}].councilMember" class="councilcommitmem" data-change-to="commiteemembers[${index}].councilMember.checked"
														  id="${councilmem.id}"  value="${councilmem.id}"/>
														<input type="hidden" name="commiteemembers[${index}].councilMember.checked"  id="councilcommitmemchk" class="councilcommitmemchk" value="false"/>
													</c:otherwise>
												</c:choose>
												<input type="hidden"  name="commiteemembers[${index}].councilMember"  value="${councilmem.id}"/>
											</td>
											<td><c:out value="${councilmem.name}" /></td>
											<td><c:out value="${councilmem.electionWard.name}" /></td>
											<td><c:out value="${councilmem.designation.name}" /></td>
											<td><c:out value="${councilmem.qualification.name}" /></td>	
											<td><c:out value="${councilmem.partyAffiliation.name}" /></td>
											<td><c:out value="${councilmem.status}" /></td>	
										</tr>
									
									<c:set var="index" value="${index+1}"/>
									</c:forEach>
								</tbody>
							</table>
							
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	<script>
	$('#buttonSubmit').click(function(e) {
		if ($('form').valid()) {
		} else {
			e.preventDefault();
		}
	});
	
</script>