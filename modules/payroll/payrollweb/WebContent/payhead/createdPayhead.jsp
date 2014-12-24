<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="org.egov.payroll.dao.*,java.util.*,org.egov.infstr.*,org.egov.infstr.commons.dao.*,org.egov.infstr.commons.* "%>


<html>
<head>
	<title>Payhead Created/Updated</title>
	<LINK REL=stylesheet HREF="/css/egov.css" TYPE="text/css">
<script language="JavaScript"  type="text/JavaScript">
	
	function printpage(obj){  
	  obj.style.visibility = "hidden";
  	  window.print();
	  obj.style.visibility = "visible";
  	}


</script>
</head>
<body>

	<table width="95%" cellpadding="0" cellspacing="0" border="0" id="paytable">
		<tr>
			
			<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">Payhead Details</div></td>
		</tr>		
		<tr>
			<td class="whitebox2wk"><b><bean:message key="PayheadName"/></td>
			<td class="whitebox2wk"><c:out value="${payhead.head}"/></td>
			<td class="whitebox2wk"><b><bean:message key="PayheadOrder"/></td>
			<td class="whitebox2wk"><c:out value="${payhead.orderId}"/></td>	
		</tr>
		<tr>
			<td class="whitebox2wk"><b><bean:message key="PayheadDescription"/></td>
			<td class="whitebox2wk"><c:out value="${payhead.description}"/></td>
			<td class="whitebox2wk"><b><bean:message key="LocalDescription"/> </td>
			<td class="whitebox2wk"><c:out value="${payhead.localLangDesc}"/></td>
		</tr>
		<tr>
			<td class="whitebox2wk"><b><bean:message key="PayheadType"/></td>
			<td class="whitebox2wk">
				<c:out value="${payhead.categoryMaster.catType}"/>
			</td>
			<td class="whitebox2wk"><b><bean:message key="PayheadCategory"/></td>
			<td id="earningcategory" class="whitebox2wk">
				<c:out value="${payhead.categoryMaster.name}"/>
			</td>			
		</tr>
		<tr>
			<td class="whitebox2wk"><b><bean:message key="CalculationType"/> </td>
			<td class="whitebox2wk">
				<c:out value="${payhead.calType}"/>
			</td>
			<td class="whitebox2wk"><b><bean:message key="%Basis"/></td>
			<td id="pctBasis" class="whitebox2wk">
				<c:out value="${payhead.salaryCodes.head}"/>
			</td>

		</tr>
		 <c:if test = "${payhead.categoryMaster.catType == 'E'}">	
			<td class="whitebox2wk"><b><bean:message key="SlabDetails"/> </td>
		 </c:if>
		 <c:if test = "${payhead.categoryMaster.catType == 'D'}">	
			<td class="whitebox2wk"><b><bean:message key="RecoveryAccount"/> </td>
		 </c:if>				
			<td class="whitebox2wk">
			<c:if test="${payhead.tdsId!=null}"> 
				<c:out value="${payhead.tdsId.type}"/>
			</c:if>
			</td>
		</tr>
 	 	<tr>
			<td class="whitebox2wk"><b><bean:message key="AccountCode"/> </td>
			<td class="whitebox2wk">
			<c:if test="${payhead.chartofaccounts!=null}"> 
				<c:out value="${payhead.chartofaccounts.glcode}"/>
			</c:if>	
			<c:if test="${payhead.tdsId!=null}"> 
				<c:out value="${payhead.tdsId.chartofaccounts.glcode}"/>
			</c:if>				
			</td>
			<td class="whitebox2wk"><b><bean:message key="AccountName"/>  </td>
			<td class="whitebox2wk">
			<c:if test="${payhead.chartofaccounts!=null}"> 
				<c:out value="${payhead.chartofaccounts.name}"/>
			</c:if>	
			<c:if test="${payhead.tdsId!=null}"> 
				<c:out value="${payhead.tdsId.chartofaccounts.name}"/>
			</c:if>	
		</tr>
		<tr>			
			<td class="whitebox2wk">
			<b><bean:message key="IsAttendanceBased"/>/<bean:message key="IsRecomputed"/></td>
			<td class="whitebox2wk">
			<c:out value="${payhead.isAttendanceBased}"/>/<c:out value="${payhead.isRecomputed}"/>
			</td>
			<td class="whitebox2wk"><b><bean:message key="IsRecurring"/></td>
			<td class="whitebox2wk">
				<c:out value="${payhead.isRecurring}"/>
			</td>
		</tr>
		
		<tr>
			<td class="whitebox2wk"><b><bean:message key="Taxable"/></td>
			<td class="whitebox2wk">
				<c:out value="${payhead.isTaxable}"/>
			</td>
			<td class="whitebox2wk"><b><bean:message key="IsBasicRateCapture"/></td>
			<td class="whitebox2wk">
				<c:out value="${payhead.captureRate}"/>
			</td>
		</tr>
		<tr>
			<td class="whitebox2wk"><b>Interest bearing </td>
			<td class="whitebox2wk">
				<c:if test="${payhead.interestAccount != null}">
					Y
				</c:if>
				<c:if test="${payhead.interestAccount == null}">
					N
				</c:if>

			</td>
		</tr>
		<tr>
			<td class="whitebox2wk"><b>Interest Account Code</td>
			<td class="whitebox2wk">
			<c:if test="${payhead.interestAccount!=null}"> 
				<c:out value="${payhead.interestAccount.glcode}"/>
			</c:if>						
			</td>
			<td class="whitebox2wk"><b>Account Name</td>
			<td class="whitebox2wk">
			<c:if test="${payhead.interestAccount!=null}"> 
				<c:out value="${payhead.interestAccount.name}"/>
			</c:if>				
		</tr>
		<tr>
			<td class="whitebox2wk"><b>Advance validation script</td>
			<td class="whitebox2wk">
			<c:out value="${payhead.validationRuleScript.description}"/>
								
			</td>
		</tr>

	<tr>
	<td colspan="4" align="center">
		<input type="button" name="printButton" onclick="printpage(this)" value="PRINT" class="buttonfinal" />		
	</td>
	</tr>
	</table>
 

</body>
</html>
