#-------------------------------------------------------------------------------
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
#-------------------------------------------------------------------------------
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.egov.tradelicense.Constants"%>
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
	<td colspan="5" class="headingwk">
		<div class="arrowiconwk">
			<img src="${pageContext.request.contextPath}/images/arrow.gif"
				height="20" />
		</div>
		<div class="headplacer">
			<s:text name='license.title.applicantiondetails' />
		</div>
	</td>
</tr>
<tr>
	<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name='license.licensenumber' /> </b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="licenseNumber" /></td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name="license.applicationnumber" /> </b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="applicationNumber" /></td>
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
	<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name="license.applicationdate" /> </b></td>
	<s:date name='applicationDate' id="VTL.applicationDate"
		format='dd/MM/yyyy' />
	<td class="<c:out value="${trclass}"/>"><s:property
			value="%{VTL.applicationDate}" /></td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name="license.tradename" /> </b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="tradeName.name" /></td>
</tr>
<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>

<c:choose>
	<c:when test="${hotelGrade!=null && hotelGrade!=''}">
		<td class="<c:out value="${trclass}"/>">&nbsp;</td>

		<td class="<c:out value="${trclass}"/>"><b><s:text
					name="license.hotel.grade" /> </b></td>
		<td class="<c:out value="${trclass}"/>" colspan="3"><s:property
				value="hotelGrade" /></td>
	</c:when>
</c:choose>

<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>
<tr>
	<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name="license.establishmentname" /> </b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="nameOfEstablishment" /></td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name='license.ownbuilding' /> </b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="buildingType" /></td>
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
	<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name="license.rentpaid" /> </b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="rentPaid" /></td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name='license.numberofrooms' /> </b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="noOfRooms" /></td>
</tr>
<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>
<s:if test="%{boundary.parent.name.equalsIgnoreCase(@org.egov.tradelicense.utils.Constants@CITY_NAME)}">
	<tr>
		<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
		<td class="<c:out value="${trclass}"/>"><b><s:text
					name="license.zone" /></b></td>
		<td class="<c:out value="${trclass}"/>"><s:property
				value="boundary.name" /></td>
		<td class="<c:out value="${trclass}"/>" colspan="2"></td>
	</tr>
</s:if>
<s:else>
	<tr>
		<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
		<td class="<c:out value="${trclass}"/>"><b><s:text
					name="license.zone" /> </b></td>
		<td class="<c:out value="${trclass}"/>"><s:property
				value="boundary.parent.name" /></td>
		<td class="<c:out value="${trclass}"/>"><b><s:text
					name="license.division" /> </b></td>
		<td class="<c:out value="${trclass}"/>"><s:property
				value="boundary.name" /></td>
	</tr>
</s:else>
<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>
<tr>
	<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name='license.housenumber' /> </b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="address.houseNo" /></td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name='license.housenumber.old' /> </b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="address.streetAddress2" /></td>
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
	<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name="license.remainingaddress" /> </b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="address.streetAddress1" /></td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name='license.pincode' /> </b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="address.pinCode" /></td>

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
	<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name='license.address.phonenumber' /> </b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="phoneNumber" /></td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name="license.remarks" /> </b></td>
	<td class="<c:out value="${trclass}"/>" width="30%"><s:property
			value="remarks" /></td>
</tr>
<tr>
	<td colspan="5" class="headingwk">
		<div class="arrowiconwk">
			<img src="${pageContext.request.contextPath}/images/arrow.gif"
				height="20" />
		</div>
		<div class="headplacer">
			<s:text name='license.title.applicantdetails' />
		</div>
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
	<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name="licensee.applicantname" /> </b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="licensee.applicantName" /></td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name="licensee.nationality" /> </b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="licensee.nationality" /></td>
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
	<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name="licensee.fatherorspousename" /> </b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="licensee.fatherOrSpouseName" /></td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name="licensee.qualification" /> </b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="licensee.qualification" /></td>
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
	<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name='licesee.age' /> </b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="licensee.age" /></td>
	<td class="<c:out value="${trclass}" />"><b><s:text
				name="licensee.gender" /> </b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="licensee.gender" /></td>
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
	<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
	<td class="<c:out value="${trclass}" />"><b><s:text
				name='licensee.pannumber' /> </b></td>
	<td class="<c:out value="${trclass}"/>" colspan="3"><s:property
			value="licensee.panNumber" /></td>
</tr>
<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>
<s:if test="%{boundary.parent.name.equalsIgnoreCase(@org.egov.tradelicense.utils.Constants@CITY_NAME)}">
	<tr>
		<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
		<td class="<c:out value="${trclass}"/>"><b><s:text
					name="license.zone" /></b></td>
		<td class="<c:out value="${trclass}"/>"><s:property
				value="licensee.boundary.name" /></td>
		<td class="<c:out value="${trclass}"/>" colspan="2"></td>
	</tr>
</s:if>
<s:else>
	<tr>
		<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
		<td class="<c:out value="${trclass}"/>"><b><s:text
					name="license.zone" /></b></td>
		<td class="<c:out value="${trclass}"/>"><s:property
				value="licensee.boundary.parent.name" /></td>
		<td class="<c:out value="${trclass}"/>"><b><s:text
					name="license.division" /></b></td>
		<td class="<c:out value="${trclass}"/>"><s:property
				value="licensee.boundary.name" /></td>
	</tr>
</s:else>

<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>
<tr>
	<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name='license.housenumber' /></b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="licensee.address.houseNo" /></td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name='license.housenumber.old' /></b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="licensee.address.streetAddress2" /></td>
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
	<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name="licensee.remainingaddress" /></b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="licensee.address.streetAddress1" /></td>
	<td class="<c:out value="${trclass}"/>" colspan="2"></td>
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
	<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name='license.pincode' /> </b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="licensee.address.pinCode" /></td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name='licensee.homephone' /> </b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="licensee.phoneNumber" /></td>
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
	<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name='licensee.mobilephone' /></b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="licensee.mobilePhoneNumber" /></td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name='licensee.emailId' /></b></td>
	<td class="<c:out value="${trclass}"/>"><s:property
			value="licensee.emailId" /></td>
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
	<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
	<td class="<c:out value="${trclass}"/>"><b><s:text
				name='licensee.uid' /></b></td>
	<td class="<c:out value="${trclass}"/>" colspan="3"><s:property
			value="licensee.uid" /></td>
</tr>
