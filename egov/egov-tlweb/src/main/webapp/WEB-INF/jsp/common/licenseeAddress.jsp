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

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<tr>
	<td class="greybox">
		&nbsp;
	</td>
	<td class="greybox" align="right">
		<s:text name="license.zone" />
		<span class="mandatory"></span>
	</td>
	<td class="greybox" align="left">
		<s:select headerKey="" headerValue="%{getText('default.select')}" name="licenseeZoneId" id="licenseeZoneId" list="dropdownData.zoneList" listKey="id" listValue='name' onChange="setupLicenseeAjaxDivision(this);" />
		<egov:ajaxdropdown id="populateLicenseeDivision" fields="['Text','Value']" dropdownId='licenseedivision' url='domain/commonTradeLicenseAjax-populateDivisions.action' />
	</td>
	<td class="greybox">
		<s:text name="license.division" />
	</td>
	<td class="greybox">
		<s:select headerKey="" headerValue="%{getText('default.select')}" disabled="%{sDisabled}" name="licensee.boundary" id="licenseedivision" list="dropdownData.divisionListLicensee" listKey="id" listValue='name' onChange="setupAjaxArea(this);" />
		<egov:ajaxdropdown id="populateLicenseeArea" fields="['Text','Value']" dropdownId='licenseeArea' url='domain/commonAjax-populateAreas.action' />
	</td>
</tr>
<tr>
	<td class="greybox">
		&nbsp;
	</td>
	<td class="greybox">
		<s:text name='license.housenumber' />
		<span class="mandatory"></span>
	</td>
	<td class="greybox">
		<s:textfield name="licensee.address.houseNo" maxlength="10"/>
	</td>
	<td class="greybox">
		<s:text name='license.housenumber.old' />
	</td>
	<td class="greybox" >
		<s:textfield name="licensee.address.streetAddress2" maxlength="10"/>
	</td>
</tr>
<tr>
	<td class="greybox">&nbsp;</td>
	<td class="greybox">
		<s:text name='license.remainingaddress' />
	</td>
	<td class="greybox" colspan="3">
		<s:textarea name="licensee.address.streetAddress1" rows="3" cols="100" maxlength="500"/>
	</td>
</tr>
<tr>
	<td class="greybox">
		&nbsp;
	</td>
	<td class="greybox">
		<s:text name='license.pincode' />
	</td>
	<td class="greybox">
		<s:textfield name="licensee.address.pinCode" maxlength="6" onKeyPress="return numbersonly(this, event)"/>
	</td>
	<td class="greybox">
		<s:text name='licensee.homephone' />
	</td>
	<td class="greybox">
		<s:textfield name="licensee.phoneNumber" maxlength="15" onKeyPress="return numbersonly(this, event)"/>
	</td>
</tr>
<tr>
		<td class="greybox"></td>
		<td class="greybox"><s:text name='licensee.mobilephone' /></td>
		<td class="greybox"> <s:textfield name="licensee.mobilePhoneNumber" onKeyPress="return numbersonly(this, event)" maxlength="15"/></td>
		<td class="greybox"><s:text name='licensee.emailId' /></td>
	    <td class="greybox"><s:textfield    name="licensee.emailId" onBlur="validateEmail(this);checkLength(this,50)" maxlength="50"/></td>
</tr>	  
<tr>	    
		<td class="greybox"></td>
		<td class="greybox"><s:text name='licensee.uid' /></td>
		<td class="greybox" colspan="3"> <s:textfield name="licensee.uid"  onBlur="checkLength(this,12)" maxlength="12"/></td>
</tr>
