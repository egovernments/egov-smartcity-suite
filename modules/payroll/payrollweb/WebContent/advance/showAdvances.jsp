<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="org.egov.payroll.dao.*,java.util.*,org.egov.infstr.*,org.egov.infstr.commons.dao.*,
					org.egov.infstr.commons.*,org.egov.payroll.model.* "%>
<html>
<head>
	<title>Show advances</title>
<script language="JavaScript"  type="text/JavaScript">
 
   function showAdvInfo(salAdvId,empId){   
	   	window.location = "${pageContext.request.contextPath}/salaryadvance/salaryadvanceInfo.do?salAdvId="+ salAdvId +"&selectedEmp="+ empId;
   }
    
</script>   

</head>
<body >

<html:form action ="/salaryadvance/beforeSanction">
 <center>	
 
 		<input type="hidden" name="employeeCodeId" value="<bean:write name="advanceForm" property="employeeCodeId"/>" />	
	
	<table width="95%" cellpadding ="0" cellspacing ="0" border = "0" id="employee">
		
	    <tr>
		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">List of Advances </div></td>
	</tr>
	    <tr>
	  		<td class="whiteboxwk" ><bean:message key="EmployeeCode"/>:</td>
	  		<td class="whitebox2wk">
  			<input type="text" class="selectwk" name="ememployeeCode" value="<bean:write name="advanceForm" property="employeeCode"/>" readonly="true"/>
  			</td>
		  	<td class="whiteboxwk"><bean:message key="EmployeeName"/>:</td>
		  	<td class="whitebox2wk">
		  	<input type="text" class="selectwk" name="ememployeeName" value="<bean:write name="advanceForm" property="employeeName"/>" readonly="true"/>
     		</td>
	    </tr>	
	    <tr></tr>  	
	</table> 			
	<br>
	
  <table width="90%" align="center" cellpadding="0" cellspacing="0" border="0" id="paytable">   	
   	<tr>	   
   		<td bgcolor="#bbbbbb" align="center"  class="tablesubheadwk"><bean:message key="EmployeeCode"/></td>
		<td bgcolor="#bbbbbb" align="center"  class="tablesubheadwk"><bean:message key="EmployeeName"/></td>
		<td  bgcolor="#bbbbbb" align="center" class="tablesubheadwk"><bean:message key="Advance"/></td>
		<td  bgcolor="#bbbbbb" align="center"  class="tablesubheadwk"><bean:message key="AdvanceAmount"/> </td>
		<td  bgcolor="#bbbbbb" align="center"  class="tablesubheadwk"><bean:message key="InterestAmount"/> </td>
		<td  bgcolor="#bbbbbb" align="center"  class="tablesubheadwk"><bean:message key="MonthlyPayment"/>   </td>
		<td  bgcolor="#bbbbbb" align="center"  class="tablesubheadwk"><bean:message key="PendingAmount"/>  </td>		
   	</tr>
   	
	<c:if test="${fn:length(salaryadvances) == 0}">
		<tr>
			<td  colspan="7" align="center">
				<font color="red">No salary advance for sanction</font>
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
   			<a href="#" onclick="showAdvInfo('${salAdvObj.id}','${salAdvObj.employee.idPersonalInformation}');">
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
   			<a href="#" onclick="showAdvInfo('${salAdvObj.id}','${salAdvObj.employee.idPersonalInformation}');">
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
   	</tr>   	
   	</logic:equal>
   	</c:forEach>
	</logic:notEmpty>


   </table>
   </center>
   </html:form>
  </div> 
 


	<table align='center' id="table2">	
	<html:form action="">
	 	<tr>
	 		<td class="labelcell"><a href="<%=request.getContextPath()%>/advance/searchAdvances.jsp">Show Advances</a></td>		
		</tr>
	  </table>	
	</html:form>
</body>
</html>
