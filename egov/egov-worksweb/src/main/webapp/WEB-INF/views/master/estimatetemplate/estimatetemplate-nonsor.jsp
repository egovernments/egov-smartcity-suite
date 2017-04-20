<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div id = "baseNonSORTable" class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="title.nonsor" />
		</div>
	</div>
	<input type="hidden" value="${estimateTemplate.tempEstimateTemplateNonSorActivities.size() }"
		id="detailsSize" />
	<input type="hidden" id="errordescription" value="<spring:message code='error.nonsor.description' />">
	<input type="hidden" id="erroruom" value="<spring:message code='error.nonsor.uom' />">
	<div class="panel-body">
		<div class="col-sm-12 text-right ">
			<button id="addRowBtn" type="button" class="btn btn-primary">
				<i class="fa fa-plus"></i><spring:message code="lbl.addnonsor" />
			</button>
		</div>
		<div class="col-sm-12 text-right "></div>
		<table class="table table-bordered" id="tblNonSor">
			<thead>
				<tr>
					<th><spring:message code="lbl.slNo" /></th>
					<th><spring:message code="lbl.description" /><span class="mandatory" /></th>
					<th><spring:message code="lbl.uom" /><span class="mandatory" /></th>
					<th><spring:message code="lbl.unitrate" /><span class="mandatory" /></th>
					<th><spring:message code="lbl.delete" /></th>
				</tr>
			</thead>
			<tbody id="nonSorTable">
				<c:choose>
					<c:when test="${estimateTemplate.tempEstimateTemplateNonSorActivities.size() == 0}">
						<tr id="nonSorRow">
							<td><span class="spansno">1</span> 
								<form:hidden
									path="tempEstimateTemplateNonSorActivities[0].id" 
									value="${tempEstimateTemplateNonSorActivities[0].id}"
									class="form-control table-input hidden-input"/>
							</td>
							<td><form:textarea path="tempEstimateTemplateNonSorActivities[0].nonSor.description"
									data-idx="0"
									data-optional="0"
									data-errormsg="Description is mandatory!"
									maxlength = "1024"
									class="form-control nonsordescription table-input" required = "required" />
								<form:errors path="tempEstimateTemplateNonSorActivities[0].nonSor.description"
									cssClass="add-margin error-msg" /></td>
							<td><form:select path="tempEstimateTemplateNonSorActivities[0].nonSor.uom.id"
									data-idx="0" 
									data-optional="0"
									data-errormsg="UOM is mandatory!"
									data-first-option="false" 
									class="form-control nonsoruom" 
									required = "required" >
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
									<c:forEach items="${uomList }" var="uom">
										<form:option value="${uom.id }">${uom.uomCategory.category } -- ${uom.uom }</form:option>
									</c:forEach>
								</form:select>
								<form:errors path="tempEstimateTemplateNonSorActivities[0].nonSor.uom.id" 
									cssClass="add-margin error-msg" /></td>
							<td><form:input path="tempEstimateTemplateNonSorActivities[0].value"
									data-idx="0"
									data-optional="0"
									data-errormsg="Unit Rate is mandatory!"
									class="form-control table-input text-right removeDefaultValues unitrate patternvalidation" 
									data-pattern="decimalvalue"
									required = "required" /> 
								<form:errors path="tempEstimateTemplateNonSorActivities[0].value"
									cssClass="add-margin error-msg" /></td>
							<td><span class="add-padding deletenonsor"
								onclick="deleteNonSor(this);"><i class="fa fa-trash"
									data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${estimateTemplate.tempEstimateTemplateNonSorActivities}" var="nonSorItem" varStatus="item">
							<tr id="nonSorRow">
								<td><span class="spansno"><c:out
											value="${item.index + 1}" /></span> <form:hidden
										path="tempEstimateTemplateNonSorActivities[${item.index}].id"
										value="${nonSorItem.id}"
										class="form-control table-input hidden-input" />
								</td>
								<td><form:textarea
										path="tempEstimateTemplateNonSorActivities[${item.index}].nonSor.description"
										data-idx="0"
										data-optional="0"
										data-errormsg="Description is mandatory!"
										class="form-control table-input nonsordescription removeDefaultValues"
										maxlength="1024" required = "required" /> 
									<form:errors
										path="tempEstimateTemplateNonSorActivities[${item.index}].nonSor.description"
										cssClass="add-margin error-msg" /></td>
								<td><form:select path="tempEstimateTemplateNonSorActivities[${item.index}].nonSor.uom.id"
										data-idx="0" 
										data-optional="0"
										data-errormsg="UOM is mandatory!"
										data-first-option="false" 
										class="form-control nonsoruom" 
										required = "required" >
										<form:option value="">
											<spring:message code="lbl.select" />
										</form:option>
										<c:forEach items="${uomList }" var="uom">
											<form:option value="${uom.id }">${uom.uomCategory.category } -- ${uom.uom }</form:option>
										</c:forEach>
									</form:select>
									<form:errors path="tempEstimateTemplateNonSorActivities[${item.index}].nonSor.uom.id" 
										cssClass="add-margin error-msg" /></td>
								<td><form:input path="tempEstimateTemplateNonSorActivities[${item.index}].value"
										data-idx="0"
										data-optional="0"
										data-errormsg="Unit Rate is mandatory!"
										class="form-control table-input text-right removeDefaultValues unitrate patternvalidation" 
										data-pattern="decimalvalue"
										required = "required" /> 
									<form:errors path="tempEstimateTemplateNonSorActivities[${item.index}].value"
										cssClass="add-margin error-msg" /></td>
								<td><span class="add-padding deletenonsor"
									onclick="deleteNonSor(this);"><i class="fa fa-trash"
										data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
								</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>
</div>

 