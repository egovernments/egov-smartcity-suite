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

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<script src="<cdn:url value='/resources/js/app/connectiondetails.js?rnd=${app_release_no}'/>"></script>
<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
		<spring:message code="lbl.connection.details" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.connectiontype" /><span class="mandatory"></span></label>
	<c:choose>
		<c:when test="${mode != 'fieldInspection'}">
			<c:if test="${mode !='dataEntry' }">
			<div class="col-sm-3 add-margin">
				<form:select path="connectionType" data-first-option="false" cssClass="form-control" required="required"> 
					<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${connectionTypes}" />
				</form:select>
				<form:errors path="connectionType" cssClass="add-margin error-msg" />
			</div>
			</c:if>
			<c:if test="${mode =='dataEntry' }">
			<div class="col-sm-3 add-margin">
				<form:select path="connectionType" data-first-option="false" cssClass="form-control" required="required"  onchange="getEmptyValues();"> 
					<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${connectionTypes}" />
				</form:select>
				<form:errors path="connectionType" cssClass="add-margin error-msg" />
			</div>
			</c:if>
		</c:when>
		<c:otherwise>
			<div class="col-sm-3 add-margin">
				<form:select path="connectionType" data-first-option="false" cssClass="form-control" disabled="true"> 
					<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${connectionTypes}" />
				</form:select>
				<form:errors path="connectionType" cssClass="add-margin error-msg" />
			</div>
		</c:otherwise>
	</c:choose>
	
    <label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.watersourcetype" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:select path="waterSource" data-first-option="false" 
			cssClass="form-control" required="required">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			
			<form:options items="${waterSourceTypes}" itemValue="id" id="waterSourceDropdown"
				itemLabel="waterSourceType" />
		</form:select>
		<form:errors path="waterSource" cssClass="add-margin error-msg" />
	</div>
</div>
<div class="form-group">
    <label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.propertytype" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:select path="propertyType" data-first-option="false" id="propertyType"
			cssClass="form-control" required="required" >
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${propertyTypes}" itemValue="id"
				itemLabel="name" />
		</form:select>		
		<form:errors path="propertyType" cssClass="add-margin error-msg" />					
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.category" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:select path="category" data-first-option="false" id="connectionCategorie"
			cssClass="form-control" required="required">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${connectionCategories}" itemValue="id"
				itemLabel="name" />
		</form:select>
		<form:errors path="category" cssClass="add-margin error-msg" />
	</div>
	
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.usagetype" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:select path="usageType" data-first-option="false" id="usageType"
			cssClass="form-control" required="required">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${usageTypes}" itemValue="id"
				itemLabel="name" />
		</form:select>
		<form:errors path="usageType" cssClass="add-margin error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.hscpipesize.inches" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:select path="pipeSize" data-first-option="false" id="pipeSize"
			cssClass="form-control" required="required" >
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${pipeSizes}" itemValue="id" 
				itemLabel="code" />
		</form:select>		
		<form:errors path="pipeSize" cssClass="add-margin error-msg" />					
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.sumpcapacity.litres" /><!-- <span class="mandatory"></span> --></label> 
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation" data-pattern="number" maxlength="6" id="sumpCapacity" path="sumpCapacity" />
		<form:errors path="sumpCapacity" cssClass="add-margin error-msg" />		
	</div>
	<label class="col-sm-2 control-label text-right" id="rooms" style="display:none"><spring:message
		code="lbl.no.of.rooms" /></label>
	<div class="col-sm-3 add-margin" style="display:none" id="roomsdiv">
			<form:input class="form-control patternvalidation" data-pattern="number" maxlength="3" id="numberOfRooms" path="numberOfRooms" />
			<form:errors path="numberOfRooms" cssClass="add-margin error-msg" />		
		</div>	
	<label class="col-sm-2 control-label text-right" id="persons"><spring:message
			code="lbl.no.of.persons" /></label> 
		<div class="col-sm-3 add-margin" id="personsdiv">
			<form:input class="form-control patternvalidation" data-pattern="number" maxlength="3" id="numberOfPerson" path="numberOfPerson" />
			<form:errors path="numberOfPerson" cssClass="add-margin error-msg" />		
		</div>
</div>
<div class="form-group display-hide showfields" id="waterSupplyDiv" style="display:none">
<label class="col-sm-3 control-label text-right"><spring:message 
			code="lbl.watersupplytype"/><span class="mandatory"/></label>
	<div class="col-sm-3 add-margin">
		<form:select path="waterSupply" data-first-option="false" 
					cssClass="form-control" id = "waterSupplyType">
			<form:option value="">
				<spring:message code="lbl.select"/>
			</form:option>
			
			<form:options items="${waterSupplyTypes}" itemValue="id" itemLabel="waterSupplyType" />
			
		</form:select>
		<form:errors path="waterSupply" cssClass="add-margin error-msg"/>
	
	</div>
	
	<div id="buildingNameDiv">
		<label class="col-sm-2 control-label text-right"><spring:message code="lbl.apartmentorcomplexname"/><span class="mandatory"/></label>
		<div class="col-sm-3 add-margin">
			<form:input class="form-control patternvalidation" data-pattern="alphabetwithspace" maxlength="50" id="buildingName" path="buildingName"/>
			<form:errors path="buildingName" cssClass="add-margin error-msg"/>
		</div>
	</div>
</div>
<div class="form-group display-hide showfields" id="donationChargesDiv" style="display:none">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.donationorcontribution"/><span class="mandatory"/></label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation" path="donationCharges" data-pattern="decimalvalue" id="donationChargesInput" value='${donationCharges}'/>
	</div>
</div>

<div class="form-group" id ="cardHolderDiv">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.bpl.cardholdername" /><span class="mandatory"></span></label> 
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation" data-pattern="alphabetwithspace" maxlength="50" id="bplCardHolderName" path="bplCardHolderName" required="required"  />
		<form:errors path="bplCardHolderName" cssClass="add-margin error-msg" />		
	</div>
</div>
<form:hidden id="typeOfConnection" path="" value="${typeOfConnection}"/>
<c:if test="${mode=='addconnection'}">
<div class="form-group">
		<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.addconnection.reason" /><span class="mandatory"></span></label> 
		<div class="col-sm-8 add-margin">
			<form:textarea class="form-control patternvalidation" data-pattern="string" maxlength="1024" id="connectionReason" path="connectionReason" required="required" />
			<form:errors path="connectionReason" cssClass="add-margin error-msg" />		
		</div>
</div>	
</c:if>	

<script>
	if($("#connectionType").val()=='METERED'){
		$('#donationChargesDiv').show();
		$('#donationChargesInput').attr('required','required');
	}
	else if($('#connectionType').val()=='NON_METERED'){
		$('#donationChargesDiv').hide();
		$('#donationChargesInput').attr('disabled', true);
	}
</script>
		