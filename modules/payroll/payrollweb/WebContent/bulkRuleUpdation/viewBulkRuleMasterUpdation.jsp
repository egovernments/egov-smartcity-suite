<%@ include file="/includes/taglibs.jsp" %>
<%@ page
	import="org.egov.payroll.dao.*,java.util.*,java.lang.*,org.egov.infstr.*,org.egov.infstr.commons.dao.*,
	org.egov.infstr.commons.*,org.egov.payroll.model.*,
	org.egov.infstr.client.filter.EGOVThreadLocals,
	org.egov.pims.utils.*,
	java.math.BigDecimal,
	org.egov.commons.CFinancialYear,
	org.egov.infstr.utils.EGovConfig "%>
	
<style>
.labelcellH {
	font: 11px Verdana, Geneva, Arial, Helvetica, sans-serif;
	color: #3670A7;
	background-color: #efefef;
	width: 150px;
}
</style>

<html>
<head>
<title><bean:message key="bulkUpdateRuleMstr"/></title>
<script>

function execute()
{
	
	var target="<%=(request.getAttribute("alertMessage"))%>";
	if(target!=null && target != "null")
	 {
		alert("<%=request.getAttribute("alertMessage")%>");
		<%	request.setAttribute("alertMessage",null);	%>
	 }

}


</script>
</head>

<body onload ="execute()">
	<div class="navibarshadowwk"></div>
		<div class="formmainbox"><div class="insidecontent">
		<div class="rbroundbox2">
		<div class="rbtop2"><div></div></div>
		<div class="rbcontent2">
		<div class="datewk"></div>
<html:form action="/bulkRuleUpdation/BulkUpdateMasterAction">




	<table width="95%" class="tableStyle"  cellpadding="0" cellspacing="0" border="0" id="paytable">
		<tr>
			<td colspan="6" height=30 align=middle class="tableheader">
			<p align="center"><b><bean:message key="bulkUpdateRuleMstr"/>&nbsp;&nbsp;&nbsp;</b>
			</td>
		</tr>
		

	
	<tr>
	<td class="labelcellH" align="left"><bean:message key="catType"/>	:</td>
			<td align="left">
				  <c:if test="${BulkMasterForm.categoryType=='D'}"> 
					<c:out value="DEDUCTION	"/>
				</c:if>

				<c:if test="${BulkMasterForm.categoryType=='E'}"> 
					<c:out value="EARNING	"/>
				</c:if>
			</td>
			
				<c:forEach items="${empGrps}" var="empGrpsList">
					<c:if test = "${empGrpsList.id == BulkMasterForm.empGrpMstr}">
					<td align="left" class="labelcellH">Employee Group</td>
					<td>
						<c:out value="${empGrpsList.name}"/>
					</td>	
					</c:if>
				</c:forEach>
					
		</tr>

	



 <tr>
		<td class="labelcellH"  align="left" ><bean:message key="PayHead"/>	:</td>
		<td >
		<c:forEach var="salObj" items="${allSalaryCodes}">						
				 <c:if test = "${salObj.id == BulkMasterForm.payHead}">		
					<c:out value="${salObj.head}"/>
				 </c:if>
		</c:forEach>

		</td>

		<td class="labelcellH"  align="left" ><bean:message key="CalculationType"/>	:</td>
		<td >
			<c:out value="${BulkMasterForm.calType}"/>
		</td>


	</tr>

	<c:if test="${BulkMasterForm.calType=='ComputedValue'}">
	<tr id = "percentage" >

    <td  class="labelcellH" width="149"><bean:message key="percentageChange"/>	:</td>

		 <td>
		   <c:if test="${BulkMasterForm.percentage!=null}"> 
			<c:out value="${BulkMasterForm.percentage}"/>
		 </c:if>
		</td>

  </tr>
</c:if>

<c:if test="${BulkMasterForm.calType=='MonthlyFlatRate'}">
  <tr id = "amount" >

		<td  class="labelcellH" width="149"><bean:message key="Amount"/>	:</td>

		  <td>
			<c:if test="${BulkMasterForm.amount!=null}"> 
				<c:out value="${BulkMasterForm.amount}"/>
			</c:if>
		</td>

  </tr>
</c:if>

<tr>
  
			
<c:set var= "monthId" value="${BulkMasterForm.monthId}" />
  <%
			int monReq;
			String monthStrId = (String)pageContext.getAttribute("monthId"); 
			monReq = Integer.parseInt(monthStrId.trim());
			Map mMap=null;
			Map finMap=null;
			%>
			
			<td  class="labelcellH" width="149" align = "left"><bean:message key="Month"/> 	:</td>
			
			<td >

			<%
			
			 mMap =(Map)session.getAttribute("monthMap");
			for (Iterator it = mMap.entrySet().iterator(); it.hasNext(); )
			{
				Map.Entry id = (Map.Entry) it.next();
				if(id.getKey().toString().equals(Integer.toString(monReq)))
				{%>
					
					<%=id.getValue()%>
				<%}
			}
			%>
			</td>

			</td>

			</td>

			</td>

		<c:set var= "financialYr" value="${BulkMasterForm.finYear}" />
		 <%
			int finId;
			String financialId = (String)pageContext.getAttribute("financialYr"); 
			finId = Integer.parseInt(financialId.trim());
			
			%>

			<td  class="labelcellH" width="149"><bean:message key="FinancialYear"/>	:</td>
			<td>


			<%
			
			 finMap =(Map)session.getAttribute("finMap");
			for (Iterator it = finMap.entrySet().iterator(); it.hasNext(); )
			{
				Map.Entry id = (Map.Entry) it.next();
				if(id.getKey().toString().equals(Integer.toString(finId)))
				{%>
					
					<%=id.getValue()%>
				<%}
			}
			%>

		


			</td>
			</td>


  </tr>
  
	</table>
</div>
		<div class="rbbot2"><div></div></div>
		</div>
		</div>
		</div>
		<div class="buttonholderwk"><input type="submit" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close()"  />
		</div>
		<div class="urlwk">City Administration System Designed and Implemented by 
		<a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div>
	
	
</html:form>


</body>
</html>
