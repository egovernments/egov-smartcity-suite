<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<script type="text/javascript" src="<c:url value='/resources/js/app/escalationview.js'/>"></script>

<script>
	function deleteRow(obj) {
		var tb1 = document.getElementById("escalationTable");
		var lastRow = (tb1.rows.length) - 1;

		if (lastRow == 1) {
			alert('you cannot delete this row ');
			return false;
		} else {
			tb1.deleteRow(lastRow);
			return true;
		}
	}
</script>		
<div class="row">
	<div class="col-md-12">
		<div class="panel " data-collapsed="0">
			<div class="panel-body">
				<c:if test="${not empty warning}">
                	<div class="alert alert-danger" role="alert">${warning}</div>
           		</c:if>
				<form:form id="viewEscalation" method="post" class="form-horizontal form-groups-bordered" modelAttribute="escalationForm">
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading ">
							<div class="panel-title">
								<strong><spring:message code="lbl.escalation.heading.view"/></strong>
							</div>
						</div> 
						<div class="panel-body custom-form">
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.escalation.complaintType" /> 
								</label>
								<div class="col-sm-6">
									<form:input id="com_type" path="complaintRouter.complaintType.name" type="text" class="form-control typeahead is_valid_alphabet" placeholder="" autocomplete="off" readonly="true" />
									<form:hidden path="complaintRouter.complaintType.id" id="complaintTypeId" value="${complaintRouter.complaintType.id}" />
									<form:errors path="complaintRouter.complaintType" cssClass="add-margin error-msg"/>
									<div class="error-msg eithererror all-errors display-hide"></div>
								<%-- 	<input id="routerId" type="hidden" value="<c:out value="${complaintRouter.id}" />" /> 
								 --%>	
									<form:hidden path="complaintRouter.id" id="routerId"/>
									
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.escalation.BoundaryType" /></label>
								<div class="col-sm-6 add-margin">
                     				<form:select path="complaintRouter.boundary.boundaryType" id="boundary_type_id"
									cssClass="form-control" cssErrorClass="form-control error" disabled="true" >
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
									<form:options items="${complaintRouter.boundaryTypes}" itemValue="id"
										itemLabel="name" />
								</form:select>
		                   		</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.escalation.Boundary"/>
								</label>
								<div class="col-sm-6">
									<form:input id="com_boundry" path="complaintRouter.boundary.name" type="text" class="form-control typeahead is_valid_alphabet" placeholder="" autocomplete="off" disabled="true"/>
									<form:hidden path="complaintRouter.boundary.id" id='boundaryId'   value="${complaintRouter.boundary.id}" />
									<form:errors path="complaintRouter.boundary" cssClass="error-msg"/>
			                    	<div class="error-msg boundaryerror all-errors display-hide"></div>
								</div>
			                 </div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.escalation.position" /></label>
								<div class="col-sm-6">
									<form:input id="com_position" path="complaintRouter.position.name" type="text" class="form-control typeahead is_valid_alphabet" placeholder="" autocomplete="off" disabled="true"/>
									<form:hidden path="complaintRouter.position.id" id="positionId" value="${complaintRouter.position.id}" />
									<form:errors path="complaintRouter.position" cssClass="error-msg" />
									<div class="error-msg positionerror all-errors display-hide"></div>
								</div>
							</div>
					</div>
					</div>
				
						<table id="escalationTable" table width="100%" border="0"
							cellpadding="0" cellspacing="0" class="table table-bordered">
							<thead>
								<tr>
									<th><spring:message code="lbl.escalationTime.button.srNo" /></th>
									<th><spring:message code="lbl.escalation.heading.fromPosition" /></th>
										<th><spring:message code="lbl.escalation.heading.department" /></th>
										<th><spring:message code="lbl.escalation.heading.designation" /></th>
									<th><spring:message code="lbl.escalation.heading.toPosition" /></th>
								</tr>
							</thead>
							<tbody>
								
								<c:forEach var="contact" items="${escalationForm.positionHierarchyList}" varStatus="status">
									<tr>
										<td>${status.count}</td>
										<td>
											
											<input type=hidden
											id="positionHierarchyFromPositionId${status.index}"
											name="positionHierarchyList[${status.index}].fromPosition.id"
											value="${contact.fromPosition.id}"> 
											
									       <input type="text" class="form-control is_valid_alphabet"
											id="positionHierarchyfromPositionName${status.index}"
											value="${contact.fromPosition.name}" 
											name="positionHierarchyList[${status.index}].fromPosition.name"
											 autocomplete="off" required="required"  readonly="readonly">
											<%-- 
											<input type=hidden
											id="positionHierarchyId${status.index}"
											name="positionHierarchyList[${status.index}].id"
											value="${contact.id}">
											 --%>
										</td>
									<td>
										<input type="hidden"
											id="positionHierarchySubType${status.index}"
											class="form-control is_valid_alphanumeric positionHierarchySubType${status.index}"
											value="${contact.objectSubType}" 
											name="positionHierarchyList[${status.index}].objectSubType"
											>
											<input type="hidden"
											id="positionHierarchyobjectType${status.index}"
											class="form-control is_valid_alphanumeric positionHierarchyobjectType${status.index}"
											value="${contact.objectType.id}" 
											name="positionHierarchyList[${status.index}].objectType.id">		
											
											<form:select path="" data-first-option="false"  value="${contact.toPosition.deptDesig.department.id}"
												id="approvalDepartment${status.index}" cssClass="form-control approvalDepartment${status.index}"
												cssErrorClass="form-control error">
												<form:option value="">Select </form:option>		
												<%-- <form:options items="${approvalDepartmentList}" itemValue="id"
														itemLabel="name"  value="${contact.toPosition.deptDesig.department.id}"/>     
												 --%>
												   <c:forEach items="${approvalDepartmentList}" var="dept">
										            <option <c:if test="${dept.id eq contact.toPosition.deptDesig.department.id}">selected="selected"</c:if>    value="${dept.id}">${dept.name} </option>
										         </c:forEach>
										       
										       </form:select>  
											<%-- <select  value="${contact.toPosition.deptDesig.department.id}"
											name="positionHierarchyList[${status.index}].toPosition.deptDesig.department.id"
												id="approvalDepartment${status.index}" class="form-control approvalDepartment${status.index}"
												cssErrorClass="form-control error">
												<option value="">Select </option>		
												<c:forEach items="${approvalDepartmentList}" var="dept">
                       								<option value="${dept.id}"> ${dept.name}</option>
                       								</c:forEach>
												</select>
												
										 --%>		
											</td>
										<td>	
											<form:select path="" data-first-option="false"  data-optvalue="${contact.toPosition.deptDesig.designation.id}"
												id="approvalDesignation${status.index}" cssClass="form-control approvalDesignation${status.index}"
												cssErrorClass="form-control error">  
												<form:option value="">
													Select
												</form:option>
												<%-- <form:option value="${contact.toPosition.deptDesig.designation.id}">
													<c:out value="${contact.toPosition.deptDesig.designation.name}"/> 
												</form:option> --%>
												<%-- <form:options items="${approvalDesignationList}" itemValue="id"
													itemLabel="name" /> --%> 
												   <c:forEach items="${approvalDesignationList}" var="desig">
										            <option <c:if test="${desig.id eq contact.toPosition.deptDesig.designation.id}">selected="selected"</c:if>    value="${desig.id}">${desig.name} </option>
										         </c:forEach>
										         	
													
											</form:select>	
											</td>
										<td>		
										<select name="positionHierarchyList[${status.index}].toPosition.id" 
											id="positionHierarchyToPositionid${status.index}" data-optvalue="${contact.toPosition.id}" 
											class="form-control positionHierarchyToPositionid${status.index}" required="required">
												<option value="">Select</option>
										</select>
											<script>calltypeahead('${status.count}'-1); 
											 $(function () {
 												$('#approvalDepartment'+('${status.count}'-1)).change();
 												$('#approvalDesignation'+('${status.count}'-1)).change();
 											}); 
											</script>
									<%-- 	<input type=hidden
											id="positionHierarchyToPositionId${status.index}"
											name="positionHierarchyList[${status.index}].toPosition.id"
											value="${contact.toPosition.id}">  --%>
											</td>
									
									</tr>
								</c:forEach>
				
							</tbody>
						</table>
						
						<div class="form-group">
								<div class="text-center">
									<button type="button" id="btn-add" 
												class="btn btn-success btn-add">Add New Row</button>
											
											<button type="button" onclick="deleteRow(this)" id="delete"
												class="btn btn-success">Delete Last Row</button> </td>
								</div>
							</div>	
					<div class="form-group">
								<div class="text-center">
								<br><br>
																					
									
										<button type="submit" class="btn btn-success escalationBtn" onclick="return checkUniqueDesignationSelected();" id="escalationBtn">
										<spring:message code="lbl.update"/>
										</button>
										<a href="javascript:void(0);" onclick="self.close()" class="btn btn-default">Close</a>
								</div>
							</div>
				</form:form>
			</div>
		</div>
	</div>
</div>
		
