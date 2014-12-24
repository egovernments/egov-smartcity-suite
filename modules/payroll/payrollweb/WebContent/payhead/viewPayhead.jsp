<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="org.egov.payroll.dao.*,java.util.*,org.egov.infstr.*,org.egov.infstr.commons.dao.*,org.egov.infstr.commons.*,org.egov.payroll.model.* "%>


<html>
<head>
	<title>View Payhead</title>

<script language="JavaScript"  type="text/JavaScript">
	
	function printpage(obj)
  {  
  window.print();
  }
<%
//SalaryCodes s = (SalaryCodes)request.getAttribute("test");
//System.out.println(s.getId());

%>

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
			<td class="whitebox2wk"><c:out value="${salarycode.head}"/></td>
			<td class="whitebox2wk"><b><bean:message key="PayheadOrder"/></td>
			<td class="whitebox2wk"><c:out value="${salarycode.orderId}"/></td>	
		</tr>
		<tr>
			<td class="whitebox2wk"><b><bean:message key="PayheadDescription"/></td>
			<td class="whitebox2wk"><c:out value="${salarycode.description}"/></td>
			<td class="whitebox2wk"><b><bean:message key="LocalDescription"/></b> </td>
			<td class="whitebox2wk"><c:out value="${salarycode.localLangDesc}"/></td>
		</tr>
		<tr>
			<td class="whitebox2wk"><b><bean:message key="PayheadType"/></td>
			<td class="whitebox2wk">
				<c:out value="${salarycode.categoryMaster.catType}"/>
			</td>
			<td class="whitebox2wk"><b><bean:message key="PayheadCategory"/></td>
			<td id="earningcategory" class="whitebox2wk">
				<c:out value="${salarycode.categoryMaster.name}"/>
			</td>			
		</tr>
		<tr>
			<td class="whitebox2wk"><b><bean:message key="CalculationType"/> </td>
			<td class="whitebox2wk">
				<c:out value="${salarycode.calType}"/>
			</td>
			<td class="whitebox2wk"><b><bean:message key="%Basis"/></td>
			<td id="pctBasis" class="whitebox2wk">
				<c:out value="${salarycode.salaryCodes.head}"/>
			</td>

		</tr>
		<c:if test="${salarycode.categoryMaster.catType=='E'}"> 
			<c:if test="${salarycode.tdsId!=null}"> 
				<tr>
					<td class="whitebox2wk"><b><bean:message key="SlabDetails"/> </td>
					<td class="whitebox2wk">
					
						<c:out value="${salarycode.tdsId.type}"/>
					
					</td>
				</tr>
			</c:if>
		</c:if>
		<c:if test="${salarycode.categoryMaster.catType=='D'}"> 
 	 	<tr>
			<td class="whitebox2wk"><b><bean:message key="RecoveryAccount"/> </td>
			<td class="whitebox2wk">
			<c:if test="${salarycode.tdsId!=null}"> 
				<c:out value="${salarycode.tdsId.type}"/>
			</c:if>
			</td>
		</tr>
		</c:if>
 	 	<tr>
			<td class="whitebox2wk"><b><bean:message key="AccountCode"/>  </td>
			<td class="whitebox2wk">
			<c:if test="${salarycode.chartofaccounts!=null}"> 
				<c:out value="${salarycode.chartofaccounts.glcode}"/>
			</c:if>	
			<c:if test="${salarycode.tdsId!=null}"> 
				<c:out value="${salarycode.tdsId.chartofaccounts.glcode}"/>
			</c:if>				
			</td>
			<td class="whitebox2wk"><b><bean:message key="AccountName"/>  </td>
			<td class="whitebox2wk">
			<c:if test="${salarycode.chartofaccounts!=null}"> 
				<c:out value="${salarycode.chartofaccounts.name}"/>
			</c:if>	
			<c:if test="${salarycode.tdsId!=null}"> 
				<c:out value="${salarycode.tdsId.chartofaccounts.name}"/>
			</c:if>	
		</tr>
		<tr>			
			<td class="whitebox2wk">
			<b><bean:message key="IsAttendanceBased"/>/<bean:message key="IsRecomputed"/></td>
			<td class="whitebox2wk">
			<c:out value="${salarycode.isAttendanceBased}"/>/<c:out value="${salarycode.isRecomputed}"/>
			</td>
			<td class="whitebox2wk"><b><bean:message key="IsRecurring"/></td>
			<td class="whitebox2wk">
				<c:out value="${salarycode.isRecurring}"/>
			</td>
		</tr>
		<tr>
			<td class="whitebox2wk"><b><bean:message key="Taxable"/></td>
			<td class="whitebox2wk">
				<c:out value="${salarycode.isTaxable}"/>
			</td>
			<td class="whitebox2wk"><b><bean:message key="IsBasicRateCapture"/></td>
			<td class="whitebox2wk">
				<c:out value="${salarycode.captureRate}"/>
			</td>
		</tr>
		<tr>
			<td class="whitebox2wk"><b>Interest bearning</td>
			<td class="whitebox2wk">
				<c:if test="${salarycode.interestAccount != null}">
					Y
				</c:if>
				<c:if test="${salarycode.interestAccount == null}">
					N
				</c:if>

			</td>
		</tr>
		<tr>
			<td class="whitebox2wk"><b>Interest Account Code</td>
			<td class="whitebox2wk">
			<c:if test="${salarycode.interestAccount!=null}"> 
				<c:out value="${salarycode.interestAccount.glcode}"/>
			</c:if>						
			</td>
			<td class="whitebox2wk"><b>Account Name</td>
			<td class="whitebox2wk">
			<c:if test="${salarycode.interestAccount!=null}"> 
				<c:out value="${salarycode.interestAccount.name}"/>
			</c:if>				
		</tr>
		<tr>
			<td class="whitebox2wk"><b>Advance validation script</td>
			<td class="whitebox2wk">
			<c:out value="${salarycode.validationRuleScript.description}"/>
								
			</td>
		</tr>

	<tr>
	<td colspan="4" align="center">
		<input type="button" class="buttonfinal" name="buttoninactive" onclick="printpage(this)" value="PRINT"  />		
	</td>
	</tr>
	</table>

	


</body>
</html>
