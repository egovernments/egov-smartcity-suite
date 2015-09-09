<!-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#     accountability and the service delivery of the government  organizations.
#  
#      Copyright (C) <2015>  eGovernments Foundation
#  
#      The updated version of eGov suite of products as by eGovernments Foundation 
#      is available at http://www.egovernments.org
#  
#      This program is free software: you can redistribute it and/or modify
#      it under the terms of the GNU General Public License as published by
#      the Free Software Foundation, either version 3 of the License, or
#      any later version.
#  
#      This program is distributed in the hope that it will be useful,
#      but WITHOUT ANY WARRANTY; without even the implied warranty of
#      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#      GNU General Public License for more details.
#  
#      You should have received a copy of the GNU General Public License
#      along with this program. If not, see http://www.gnu.org/licenses/ or 
#      http://www.gnu.org/licenses/gpl.html .
#  
#      In addition to the terms of the GPL license to be adhered to in using this
#      program, the following additional terms are to be complied with:
#  
#  	1) All versions of this program, verbatim or modified must carry this 
#  	   Legal Notice.
#  
#  	2) Any misrepresentation of the origin of the material is prohibited. It 
#  	   is required that all modified versions of this material be marked in 
#  	   reasonable ways as different from the original version.
#  
#  	3) This license does not grant any rights to any user of the program 
#  	   with regards to rights under trademark law for use of the trade names 
#  	   or trademarks of eGovernments Foundation.
#  
#    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------->>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>
<tr>
	<td class="<c:out value="${trclass}"/>" width="5%">
	</td>
	<td class="<c:out value="${trclass}"/>" align="right">
		<s:text name="license.zone" />
		<span class="mandatory">*</span>
	</td>
	<td class="<c:out value="${trclass}"/>" align="left">
		<s:select headerKey="" headerValue="%{getText('license.default.select')}" name="licenseZoneId" id="licenseZoneId" list="dropdownData.zoneList" listKey="id" listValue='name' onChange="setupAjaxDivision(this);" />
		<egov:ajaxdropdown id="populateDivision" fields="['Text','Value']" dropdownId='division' url='domain/web/commonTradeLicenseAjax!populateDivisions.action' />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="license.division" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:select headerKey="" headerValue="%{getText('license.default.select')}" disabled="%{sDisabled}" name="boundary" id="division" list="dropdownData.divisionListLicense" listKey="id" listValue='name' value="boundary.id" onChange="setupAjaxArea(this);" />
		<egov:ajaxdropdown id="populateArea" fields="['Text','Value']" dropdownId='area' url='domain/web/commonAjax!populateAreas.action' />
	</td>
</tr>
<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>
<tr>
	<td class="<c:out value="${trclass}"/> width="5%">
	<td class="<c:out value="${trclass}"/>">
		<s:text name='license.housenumber' />
		<span class="mandatory">*</span>
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:textfield name="address.houseNo" maxlength="10" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name='license.housenumber.old' />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:textfield name="address.streetAddress2" maxlength="10" />
	</td>
</tr>
<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>
<tr>
	<td class="<c:out value="${trclass}"/> width="5%">
	<td class="<c:out value="${trclass}"/>">
		<s:text name='license.remainingaddress' />
	</td>
	<td class="<c:out value="${trclass}"/>" colspan="3">
		<s:textarea name="address.streetAddress1" maxlength="500" rows="3" cols="40" />
	</td>
</tr>

<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>
<tr>
	<td class="<c:out value="${trclass}"/> width="5%"></td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name='license.pincode' />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:textfield name="address.pinCode" maxlength="6" onKeyPress="return numbersonly(this, event)"/>
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name='license.address.phonenumber' />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:textfield name="phoneNumber" maxlength="15" onKeyPress="return numbersonly(this, event)"/>
	</td>
</tr>
