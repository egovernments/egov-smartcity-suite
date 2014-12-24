<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<br/>
<table width="100%">
	<tr>
		<td colspan="5" class="headingwk">
			<div class="arrowiconwk">
				<img src="${pageContext.request.contextPath}/images/arrow.gif" height="20" />
			</div>
			<div class="headplacer">
				<s:text name='license.title.feedetails' />
			</div>
		</td>
	</tr>
</table>

	
<table width="50%" border="1" cellspacing="0" cellpadding="0" align="center">
<br/>
	<tr>
		<th class="<c:out value="${trclass}"/>">
			<s:text name='license.feename' />
		</th>
		<th class="<c:out value="${trclass}"/>">
			<s:text name="license.fee.amount" />
		</th>
	</tr>
	
		<s:iterator var="demandDetails" value="getCurrentDemand().getEgDemandDetails()">
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
			<s:property value="#demandDetails.egDemandReason.egDemandReasonMaster.reasonMaster" /> - <s:property value="#demandDetails.egDemandReason.getEgInstallmentMaster().getDescription()" />
				</td>
				<td text-align="center" class="<c:out value="${trclass}"/>">
						<s:text name="fee.rupee.symbol"/><s:property value="#demandDetails.amount" />
				</td>
			</tr>
			<s:if test="#demandDetails.amtRebate!=null && #demandDetails.amtRebate!=0">
			<tr>
			<td text-align="center" class="<c:out value="${trclass}"/>">
			<s:text name="Deduction"/> - <s:property value="#demandDetails.egDemandReason.getEgInstallmentMaster().getDescription()" />
				</td>
				<td text-align="center" class="<c:out value="${trclass}"/>">
					<s:text name="fee.rupee.symbol"/><s:property value="#demandDetails.amtRebate" />
				</td>
			</tr>
			</s:if>
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
				<b> <s:text name="license.total.fee.amount" /> </b>
			</td>
			<td align="center" class="<c:out value="${trclass}"/>">
				<b><s:text name="fee.rupee.symbol"/><s:property value="getAapplicableDemand(getCurrentDemand())" /> </b>
			</td>
		</tr>
	
</table>
