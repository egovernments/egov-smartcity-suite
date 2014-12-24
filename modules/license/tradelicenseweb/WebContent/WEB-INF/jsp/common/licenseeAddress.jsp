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
		&nbsp;
	</td>
	<td class="<c:out value="${trclass}"/>" align="right">
		<s:text name="license.zone" />
		<span class="mandatory">*</span>
	</td>
	<td class="<c:out value="${trclass}"/>" align="left">
		<s:select headerKey="" headerValue="%{getText('license.default.select')}" name="licenseeZoneId" id="licenseeZoneId" list="dropdownData.zoneList" listKey="id" listValue='name' onChange="setupLicenseeAjaxDivision(this);" />
		<egov:ajaxdropdown id="populateLicenseeDivision" fields="['Text','Value']" dropdownId='licenseedivision' url='common/commonTradeLicenseAjax!populateDivisions.action' />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="license.division" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:select headerKey="" headerValue="%{getText('license.default.select')}" disabled="%{sDisabled}" name="licensee.boundary" id="licenseedivision" list="dropdownData.divisionListLicensee" listKey="id" listValue='name' onChange="setupAjaxArea(this);" />
		<egov:ajaxdropdown id="populateLicenseeArea" fields="['Text','Value']" dropdownId='licenseeArea' url='common/commonAjax!populateAreas.action' />
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
	<td class="<c:out value="${trclass}"/>" width="5%">
		&nbsp;
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name='license.housenumber' />
		<span class="mandatory">*</span>
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:textfield name="licensee.address.houseNo" maxlength="10"/>
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name='license.housenumber.old' />
	</td>
	<td class="<c:out value="${trclass}"/>" >
		<s:textfield name="licensee.address.streetAddress2" maxlength="10"/>
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
	<td class="<c:out value="${trclass}"/> width="5%">&nbsp;</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name='licensee.remainingaddress' />
	</td>
	<td class="<c:out value="${trclass}"/>" colspan="3">
		<s:textarea name="licensee.address.streetAddress1" rows="3" cols="40" maxlength="500"/>
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
	<td class="<c:out value="${trclass}"/>" width="5%">
		&nbsp;
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name='license.pincode' />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:textfield name="licensee.address.pinCode" maxlength="6" onKeyPress="return numbersonly(this, event)"/>
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name='licensee.homephone' />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:textfield name="licensee.phoneNumber" maxlength="15" onKeyPress="return numbersonly(this, event)"/>
	</td>
</tr>
<c:choose>
	<c:when test="${trclass=='greybox'}"><c:set var="trclass" value="bluebox"/></c:when>
	<c:when test="${trclass=='bluebox'}"><c:set var="trclass" value="greybox"/></c:when>
</c:choose>
<tr>
		<td class="<c:out value="${trclass}"/> width="5%"></td>
		<td class="<c:out value="${trclass}"/>"><s:text name='licensee.mobilephone' /></td>
		<td class="<c:out value="${trclass}"/>"> <s:textfield name="licensee.mobilePhoneNumber" onKeyPress="return numbersonly(this, event)" maxlength="15"/></td>
		<td class="<c:out value="${trclass}"/>"><s:text name='licensee.emailId' /></td>
	    <td class="<c:out value="${trclass}"/>"><s:textfield    name="licensee.emailId" onBlur="validateEmail(this);checkLength(this,50)" maxlength="50"/></td>
</tr>	    
<c:choose>
	<c:when test="${trclass=='greybox'}"><c:set var="trclass" value="bluebox"/></c:when>
	<c:when test="${trclass=='bluebox'}"><c:set var="trclass" value="greybox"/></c:when>
</c:choose>
<tr>	    
		<td class="<c:out value="${trclass}"/> width="5%"></td>
		<td class="<c:out value="${trclass}"/>"><s:text name='licensee.uid' /></td>
		<td class="<c:out value="${trclass}"/>" colspan="3"> <s:textfield name="licensee.uid"  onBlur="checkLength(this,12)" maxlength="12"/></td>
</tr>