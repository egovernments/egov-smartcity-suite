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
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>

<div id="baseSORTable" class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="title.sor" />
		</div>
	</div>
	<div class="panel-body" id="sorHeaderTable">
		<div class="form-group">
			<input type="hidden" id="msgschedulecategory" value="<spring:message code='msg.select.scheduleofcategory' />">
			<input type="hidden" id="erroradded" value="<spring:message code='error.sor.added' />">
			<input type="hidden" id="errorsornonsor" value="<spring:message code='error.sor.nonsor.required' />">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.schedulecategory" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<form:select path="" multiple="true" name="scheduleCategory" data-first-option="false" id="scheduleCategory" class="form-control" required = "required">
					<c:forEach items="${scheduleCategoryList }" var="scheduleCategory">
						<form:option value="${scheduleCategory.id }">${scheduleCategory.code } -- ${scheduleCategory.description }</form:option>
					</c:forEach>
				</form:select>
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.addsor" />
			</label>
			<div class="col-sm-8 add-margin">
				<div id="sorSearch_autocomplete">
				   <div class="right-inner-addon">
					    <input id="sorSearch" placeholder="Start typing SOR code or description and select an item" type="text" name="item" class="form-control">
	    			</div>    
			    </div> 
			</div>
		</div>
		
		<div class="form-group no-margin-bottom">
			<div class="col-sm-offset-2 col-sm-8">
				<div class="alert alert-danger no-margin mt-5" id="sor_error" style="display:none;"></div>
			</div>
		</div>
		<table class="table table-bordered" id="tblSor">
			<thead>
				<tr>
					<th><spring:message code="lbl.slNo" /></th>
					<th><spring:message code="lbl.sor.category" /></th>
					<th><spring:message code="lbl.code" /></th>
					<th><spring:message code="lbl.description" /></th>
					<th><spring:message code="lbl.uom" /></th>
					<th><spring:message code="lbl.delete" /></th>
				</tr>
			</thead>
			<tbody id="sorTable">
				<c:choose>
					<c:when test="${estimateTemplate.tempEstimateTemplateSorActivities.size() == 0}">
						<tr id="sorRow">
							<td>
								<span class="spansorno">1</span>
 								<form:hidden path="tempEstimateTemplateSorActivities[0].schedule" class="sorhiddenid" />
							</td>
							<td>
								<span class="sorCategoryCode"></span>
								<form:hidden path="tempEstimateTemplateSorActivities[0].schedule.scheduleCategory" class="sorschedulecategoryhiddenid" />
							</td>
							<td>
								<span class="sorCode"></span>
							</td>
							<td>
								<span class="sorSummary"></span>
								<span class="hintanchor sorDescription"/></span>
							</td>
							<td>
								<span class="sorUom"></span>
								<form:hidden path="tempEstimateTemplateSorActivities[0].schedule.uom" class="soruomhiddenid"/>
							</td>
							<td>
								<span class="add-padding deletesor" onclick="deleteSor(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
							</td>
						</tr>

					</c:when>
					<c:otherwise>
						<c:forEach items="${estimateTemplate.tempEstimateTemplateSorActivities}" var="sor" varStatus="item">
							<c:if test="${sor.schedule != null }">
								<tr id="sorRow" class="sorRow" align="center">
									<td>
										<span class="spansorno">${item.index + 1 }</span>
 										<form:hidden path="tempEstimateTemplateSorActivities[${item.index }].schedule" id="id_${item.index }" class="sorhiddenid" value="${activity.schedule.id }" />
									</td>
									<td>
										<span class="sorCategoryCode">${sor.schedule.scheduleCategory.code }</span>
										<form:hidden path="tempEstimateTemplateSorActivities[${item.index }].schedule.scheduleCategory" class="sorschedulecategoryhiddenid" />
									</td>
									<td>
										<span class="sorCode">${sor.schedule.code }</span>
									</td>
									<td>
										<span class="sorSummary">${sor.schedule.getSummary() }</span>
										<span class="hintanchor sorDescription"/><a href="#" class="hintanchor" title="${sor.schedule.description }"><i class="fa fa-question-circle" aria-hidden="true"></i></a></span>
									</td>
									<td>
										<span class="sorUom">${sor.schedule.uom.uom }</span>
										<form:hidden path="tempEstimateTemplateSorActivities[${item.index }].schedule.uom" class="soruomhiddenid" value="${sor.schedule.uom.id }"/>
									</td>
									<td>
										<span class="add-padding deletesor" onclick="deleteSor(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
									</td>
								</tr>
							</c:if>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>
</div>