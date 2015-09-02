<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<br/>
<table width="100%">
	<tr>
		<td colspan="5" class="headingwk">
			<div class="arrowiconwk">
				<img src="${pageContext.request.contextPath}/images/arrow.gif" height="20"/>
			</div>
			<div class="headplacer">
				<s:text name='license.title.motordetail' />
			</div>
		</td>
	</tr>
</table>
<table width="50%" border="1" cellspacing="0" cellpadding="0" align="center">
<br/>
	<tr>
		<th class="<c:out value="${trclass}"/>">
			<s:text name='license.horsepower' />
		</th>
		<th class="<c:out value="${trclass}"/>">
			<s:text name="license.noofmachines" />
		</th>
	</tr>
	<s:iterator var="p" value="installedMotorList">
		<c:choose>
			<c:when test="${trclass=='greybox'}">
				<c:set var="trclass" value="bluebox" />
			</c:when>
			<c:when test="${trclass=='bluebox'}">
				<c:set var="trclass" value="greybox" />
			</c:when>
		</c:choose>
		<tr>
			<td text-align="center" class="<c:out value="${trclass}"/>">
				<s:property value="#p.hp" />
			</td>
			<td text-align="center" class="<c:out value="${trclass}"/>">
				<s:property value="#p.noOfMachines" />
			</td>

		</tr>
	</s:iterator>
	<c:choose>
		<c:when test="${trclass=='greybox'}">
			<c:set var="trclass" value="bluebox" />
		</c:when>
		<c:when test="${trclass=='bluebox'}">
			<c:set var="trclass" value="greybox" />
		</c:when>
	</c:choose>
	<tr>
		<td align="center" class="<c:out value="${trclass}"/>">
			<b><s:text name="license.total.horsepower" /> </b>
		</td>
		<td align="center" class="<c:out value="${trclass}"/>">
			<b><s:property value="totalHP" /> </b>
		</td>
	</tr>
</table>



