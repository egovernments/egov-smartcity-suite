<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="org.egov.payroll.dao.*,java.util.*,org.egov.infstr.*,org.egov.infstr.commons.dao.*,
					org.egov.infstr.commons.*,org.egov.payroll.model.* "%>

<html>
<head>
	<title>Show advances before view</title>
<script language="JavaScript"  type="text/JavaScript">

   function showAdvInfo(salAdvId){   
   		//alert(document.advanceForm.employeeCodeId.value);
   		window.location = "${pageContext.request.contextPath}/salaryadvance/modifyAdvanceInfo.do?salAdvId="+ salAdvId +"&selectedEmp="+ document.advanceForm.employeeCodeId.value+"&mode="+document.advanceForm.mode.value;
		//var url="${pageContext.request.contextPath}/salaryadvance/modifyAdvanceInfo.do?salAdvId="+ salAdvId +"&selectedEmp="+ empId;
		//window.open(url,'AdvanceInfo','width=2000,height=800,top=150,left=30,right=30,scrollbars=yes,resizable=yes');
   }
    
</script>   

</head>
<body >

<html:form action ="/salaryadvance/modifyAdvanceInfo">

	<html:hidden name="advanceForm" property="employeeCodeId"/>
	<html:hidden property="mode" />
	
	<table width="95%" cellpadding ="0" cellspacing ="0" border = "0" id="employee">
		<tr>
	    	
	    	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">List of Advances </div></td>
	    </tr>	
	    <tr>
	  		<td class="whiteboxwk" ><b><bean:message key="EmployeeCode"/>:</b></td>
	  		<td class="whitebox2wk">
  			<bean:write name="advanceForm" property="employeeCode"/>
  			</td>
		  	<td class="whiteboxwk"><b><bean:message key="EmployeeName"/>:</b></td>
		  	<td class="whitebox2wk">
		  	<bean:write name="advanceForm" property="employeeName"/>
     		</td>
	    </tr>	
	    <tr></tr>  
	    
	</table> 			
	
 
  <table width="90%" align="center" cellpadding="0" cellspacing="0" border="0" id="paytable">   	
   	<tr>	   
   		<td bgcolor="#bbbbbb" align="center"  class="tablesubheadwk">Employee Code</td>
		<td bgcolor="#bbbbbb" align="center" class="tablesubheadwk">Employee Name</td>
		<td bgcolor="#bbbbbb" align="center"  class="tablesubheadwk"><bean:message key="Advance"/></td>
		<td bgcolor="#bbbbbb" align="center"  class="tablesubheadwk"><bean:message key="AdvanceAmount"/></td>
		<td bgcolor="#bbbbbb" align="center"  class="tablesubheadwk"><bean:message key="InterestAmount"/></td>
		<td bgcolor="#bbbbbb" align="center"  class="tablesubheadwk"><bean:message key="MonthlyPayment"/></td>
		<td bgcolor="#bbbbbb" align="center"  class="tablesubheadwk"><bean:message key="PendingAmount"/>  </td>		
		<td bgcolor="#bbbbbb" align="center"  class="tablesubheadwk"><bean:message key="AdvanceStatus"/> </td>
   	</tr>
   	
	<c:if test="${fn:length(salaryadvances) == 0}">
		<tr>
			<td  colspan="7" align="center">
				<font color="red">No created salary advance </font>
			</td>
		</tr>
	</c:if>
	
	<logic:empty name="advanceForm" property="employeeCodeId">
   	<c:forEach var="salAdvObj" items="${salaryadvances}">
   	<tr>   		   		
   		<td class="whitebox3wk">   			
   				<c:out value="${salAdvObj.employee.employeeCode}"/> &nbsp;
   		</td>
   		<td class="whitebox3wk">
   			<c:out value="${salAdvObj.employee.employeeFirstName}"/>&nbsp; 
		  		<c:if test="${salAdvObj.employee.employeeMiddleName != null}">
		  			<c:out value="${salAdvObj.employee.employeeMiddleName}"/>&nbsp; 
		  		</c:if>	
		  	<c:out value="${salAdvObj.employee.employeeLastName}"/>	
   		</td>
   		<td class="whitebox3wk">
   				<a href="#" onclick="showAdvInfo('${salAdvObj.id}');">
	   				<c:out value="${salAdvObj.salaryCodes.head}"/> &nbsp;
				</a>	
   		</td>
   		<td class="whitebox3wk" align="right">
   			<c:out value="${salAdvObj.advanceAmt}"/> &nbsp;
   		</td>
   		<td class="whitebox3wk" align="right">
   			<c:out value="${salAdvObj.interestAmt}"/>&nbsp; 
   		</td>
   		<td class="whitebox3wk" align="right">
   			<c:out value="${salAdvObj.instAmt}"/> &nbsp;
   		</td>   		
   		<td class="whitebox3wk" align="right">
   			<c:out value="${salAdvObj.pendingAmt}"/> &nbsp;
   		</td>  
   		<td class="whitebox3wk" align="right">
   			<c:if test="${(salAdvObj.isLegacyAdvance=='Y') || (salAdvObj.isLegacyAdvance=='N'  && salAdvObj.salaryARF==null)}">
		   		<c:out value="${salAdvObj.status.description}"/> &nbsp;
			</c:if>
			<c:if test="${salAdvObj.isLegacyAdvance=='N'  && salAdvObj.salaryARF!=null}">
				<c:out value="${salAdvObj.salaryARF.status.description}"/> &nbsp;
			</c:if>
   		</td>  		
   	</tr>   	
   	</c:forEach>
	</logic:empty>
	
	<logic:notEmpty name="advanceForm" property="employeeCodeId">
   	<c:forEach var="salAdvObj" items="${salaryadvances}">  
   	<logic:equal name="advanceForm" property="employeeCodeId" value="${salAdvObj.employee.idPersonalInformation}"> 
   	<tr>   	   		
   		<td class="whitebox3wk">   			
	   			<c:out value="${salAdvObj.employee.employeeCode}"/> &nbsp;
   		</td>
   		<td class="whitebox3wk">
   			<c:out value="${salAdvObj.employee.employeeFirstName}"/>&nbsp; 
		  		<c:if test="${salAdvObj.employee.employeeMiddleName != null}">
		  			<c:out value="${salAdvObj.employee.employeeMiddleName}"/>&nbsp; 
		  		</c:if>	
		  	<c:out value="${salAdvObj.employee.employeeLastName}"/>	
   		</td>
   		<td class="whitebox3wk">
   				<a href="#" onclick="showAdvInfo('${salAdvObj.id}');">
	   				<c:out value="${salAdvObj.salaryCodes.head}"/> &nbsp;
				</a>	
   		</td>
   		<td class="whitebox3wk">
   			<c:out value="${salAdvObj.advanceAmt}"/> &nbsp;
   		</td>
   		<td class="whitebox3wk">
   			<c:out value="${salAdvObj.interestAmt}"/> &nbsp;
   		</td>
   		<td class="whitebox3wk">
   			<c:out value="${salAdvObj.instAmt}"/> &nbsp;
   		</td>
   		<td class="whitebox3wk" align="right">
   			<c:out value="${salAdvObj.pendingAmt}"/> &nbsp;
   		</td> 
   		<td class="whitebox3wk" align="right">
   			<c:if test="${(salAdvObj.isLegacyAdvance=='Y') || (salAdvObj.isLegacyAdvance=='N'  && salAdvObj.salaryARF==null)}">
		   		<c:out value="${salAdvObj.status.description}"/> &nbsp;
			</c:if>
			<c:if test="${salAdvObj.isLegacyAdvance=='N'  && salAdvObj.salaryARF!=null}">
				<c:out value="${salAdvObj.salaryARF.status.description}"/> &nbsp;
			</c:if>
   		</td> 
   	</tr>   	
   	</logic:equal>
   	</c:forEach>
	</logic:notEmpty>


   </table>
  </html:form>
</body>
</html>
