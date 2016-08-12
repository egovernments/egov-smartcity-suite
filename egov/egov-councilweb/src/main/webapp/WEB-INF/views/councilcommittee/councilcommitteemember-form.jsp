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
										<th><input type="checkbox" id="committeechk" name="chkbox"/></th> 
										<th>Member Name</th>
										<th>Election Ward</th>
										<th>Designation</th>
										<th>Qualification</th>
										<th>Party Affiliation</th>
									</tr>
								</thead>
								
								<tbody>
									<c:set var="index" value="0"/>
									<c:forEach items="${committeeType.commiteemembers}" var="coumcilmem" varStatus="counter">
									<tr>
										<td> 
										<input type="checkbox" name="commiteemembers[${index}].councilMember" data-change-to="commiteemembers[${index}].checked" class="councilcommitmem" checked  id="${coumcilmem.councilMember.id}" value="${coumcilmem.councilMember.id}"/>
										<input type="hidden" name="commiteemembers[${index}].checked" id="councilcommitmemchk" class="councilcommitmemchk" value="true" />
										</td>
										<td><c:out value="${coumcilmem.councilMember.name}" /></td>
										<td><c:out value="${coumcilmem.councilMember.electionWard.name}" /></td>
										<td><c:out value="${coumcilmem.councilMember.designation.name}" /></td>
										<td><c:out value="${coumcilmem.councilMember.qualification.name}" /></td>	
										<td><c:out value="${coumcilmem.councilMember.partyAffiliation.name}" /></td>		
									</tr>
									<c:set var="index" value="${index+1}"/>
									</c:forEach>  
									<c:forEach items="${councilMembers}" var="coumcilmem" varStatus="counter">
									<tr>
										<td>
											<input type="checkbox"  name="commiteemembers[${index}].councilMember" class="councilcommitmem" data-change-to="commiteemembers[${index}].councilMember.checked"  id="${coumcilmem.id}"  value="${coumcilmem.id}"/>
											<input type="hidden" name="commiteemembers[${index}].councilMember.checked"  id="councilcommitmemchk" class="councilcommitmemchk" value="false"/>
											<input type="hidden"  name="commiteemembers[${index}].councilMember"  value="${coumcilmem.id}"/>
										</td>
										<td><c:out value="${coumcilmem.name}" /></td>
										<td><c:out value="${coumcilmem.electionWard.name}" /></td>
										<td><c:out value="${coumcilmem.designation.name}" /></td>
										<td><c:out value="${coumcilmem.qualification.name}" /></td>	
										<td><c:out value="${coumcilmem.partyAffiliation.name}" /></td>	
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