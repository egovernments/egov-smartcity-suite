<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>
<tr>
	<td class="<c:out value="${trclass}"/>">
		&nbsp;
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="licensee.applicantname" />
		<span class="mandatory">*</span>
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:textfield name="licensee.applicantName" id="applicantName" maxlength="100"/>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="licensee.nationality" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:textfield name="licensee.nationality" id="nationality" maxlength="50"/>
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
	<td class="<c:out value="${trclass}"/>">
		&nbsp;
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="licensee.fatherorspousename" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:textfield name="licensee.fatherOrSpouseName" maxlength="100" id="fatherOrSpouseName" />
	<td class="<c:out value="${trclass}"/>">
		<s:text name="licensee.qualification" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:textfield name="licensee.qualification" id="qualification" maxlength="50" />
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
	<td class="<c:out value="${trclass}" />">
		&nbsp;
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name='licesee.age' />
	</td>
	<td class="<c:out value="${trclass}" />">
		<s:textfield name="licensee.age" id="age" size="3" onKeyPress="return numbersonly(this, event)"  maxlength="3"/>
	</td>
	<td class="<c:out value="${trclass}" />">
		<s:text name="licensee.gender" />
		<span class="mandatory">*</span>
	</td>
	<td class="<c:out value="${trclass}" />">
		<s:radio name="licensee.gender" list="genderList" id="gender" />
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
	<td class="<c:out value="${trclass}" />">
		&nbsp;
	</td>
	<td class="<c:out value="${trclass}" />">
		<s:text name='licensee.pannumber' />
	</td>
	<td class="<c:out value="${trclass}" /> ">
		<s:textfield name="licensee.panNumber" id="licensee.panNumber" onblur="validatePan(this)" maxlength="10"/>
	</td>
	<td class="<c:out value="${trclass}" />">
		&nbsp;
	</td>
	<td class="<c:out value="${trclass}" />">
		&nbsp;
	</td>
</tr>
