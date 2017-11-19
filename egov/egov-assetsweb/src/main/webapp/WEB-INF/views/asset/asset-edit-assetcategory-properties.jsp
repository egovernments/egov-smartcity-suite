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
  
<%@ include file="/includes/taglibs.jsp"%>
<c:choose>
	<c:when test="${not empty asset.assetCategory.getCategoryProperties()}">
		<div class="panel-heading">
			<div class="panel-title" id="customFieldsTitle">${asset.assetCategory.name} Details</div>
		</div>
		<c:set var="properties" value="${asset.categoryPropertyTypeList}"/>
		<c:forEach items="${asset.assetCategory.categoryProperties}"
			var="categoryProperties" varStatus="vs">
			<c:if test="${categoryProperties.isMandatory==true}">
				<c:set var="mandatoryLable" value='<span class="mandatory"></span>' />
				<c:set var="required" value='required="required" ' />
			</c:if>
			<c:if test="${categoryProperties.isMandatory==false}">
				<c:set var="mandatoryLable" value='<span class=""></span>' />
			</c:if>
			<c:if test="${empty categoryProperties.localText}">
				<c:set var="localText" value="" />
			</c:if>
			<c:if test="${not empty categoryProperties.localText}">
				<c:set var="localText" value="/${categoryProperties.localText}" />
			</c:if>
			<c:if test="${vs.index%2!=0}">
				<c:set var="labelClass" value='col-sm-2 control-label text-right' />
			</c:if>
			<c:if test="${vs.index%2==0}">
				<div class="form-group">
				<c:set var="labelClass" value='col-sm-3 control-label text-right' />
			</c:if>
			<label class="${labelClass}">
				${categoryProperties.name}${localText} ${mandatoryLable} </label>
			<div class="col-sm-3 add-margin">
				<input type="hidden" name="categoryProperties[${vs.index}].id"
					value="${categoryProperties.id}">

				<c:if test="${categoryProperties.dataType=='String'}">
					<input type="text" name="categoryProperties[${vs.index}].value" 
					    <c:if test = "${categoryProperties.id==properties[vs.index].id}">
					    value="${properties[vs.index].value}"
					    </c:if>
						id="${categoryProperties.name}" class="form-control text-left"
						<c:if test="${categoryProperties.isMandatory==true}">${required}</c:if> />
				</c:if>
				<c:if test="${categoryProperties.dataType=='Number'}">
					<input type="text" name="categoryProperties[${vs.index}].value" 
					<c:if test = "${categoryProperties.id==properties[vs.index].id}">
						value="${properties[vs.index].value}"
					</c:if>
						id="${categoryProperties.name}"
						class="form-control text-right patternvalidation"
						data-pattern="number" <c:if test="${categoryProperties.isMandatory==true}">${required}</c:if> />
				</c:if>
				<c:if test="${categoryProperties.dataType=='Enumeration'}">
					<select name="categoryProperties[${vs.index}].value"
						id="${categoryProperties.name}" class="form-control"
						data-pattern="number" <c:if test="${categoryProperties.isMandatory==true}">${required}</c:if>>
						<option>
							<spring:message code="lbl.select" />
						</option>
						<c:forTokens items="${categoryProperties.enumValues}" delims=","
							var="val">
							<option value="${val}"
								<c:if test = "${val==properties[vs.index].value}">
						              selected = "selected"
					            </c:if>>${val}</option>
						</c:forTokens>
					</select>
				</c:if>
				<c:if test="${categoryProperties.dataType=='Date'}">
					<input type="text"
						<%-- name="categoryProperties[${vs.index}].${categoryProperties.name}" --%>
						name="categoryProperties[${vs.index}].value"
						id="${categoryProperties.name}" class="form-control datepicker"
						data-date-end-date="0d" data-inputmask="'mask': 'd/m/y'"
						<%-- <c:if test="${not empty properties[vs.index].value}">value="${properties[vs.index].value}"</c:if> --%>
						value="${properties[vs.index].value}"
						<c:if test="${categoryProperties.isMandatory==true}">${required}</c:if> data-set-date="dd-mm-yyyy"/>
				</c:if>
				<c:if test="${categoryProperties.dataType=='DateTime'}">
					<input type="text"
						name="categoryProperties[${vs.index}].value"
						id="${categoryProperties.name}" class="form-control datetimepicker"
						<c:if test="${categoryProperties.isMandatory==true}">${required}</c:if> />
				</c:if>
				<c:if test="${categoryProperties.dataType=='MasterData'}">
					<input type="hidden" name="categoryProperties[${vs.index}].value"
						id="categoryProperties" value="${properties[vs.index].value}">
					<input type="hidden" name="categoryProperties[${vs.index}].id"
						id="${categoryProperties.enumValues}" value="${properties[vs.index].id}">
					<input type="text" name="${categoryProperties.name}" value="${properties[vs.index].value}"
						id="${categoryProperties.id}" class="form-control autocomplete"
						data-hidden-elem2="categoryProperties[${vs.index}].value"
						data-hidden-elem="categoryProperties[${vs.index}].id"
						data-source-type="${categoryProperties.id}" <c:if test="${categoryProperties.isMandatory==true}">${required}</c:if>/>
				</c:if>
			</div>
			<c:if test="${vs.index%2==1}">
			</div>
			</c:if>
		</c:forEach>
	</c:when>
</c:choose>
