<%@ include file="/includes/taglibs.jsp" %>

<%@ page
	import="org.egov.payroll.dao.*,java.util.*,org.egov.infstr.*,org.egov.infstr.commons.dao.*,org.egov.infstr.commons.*,org.egov.payroll.model.* "%>

<html>

<head>


	<title>Show payslips</title>
	
	<LINK REL=stylesheet HREF="<%=request.getContextPath()%>/ccMenu.css" TYPE="text/css">

<script language="JavaScript"  type="text/JavaScript">
 
   function showPayslipInfo(payslipId){   
	   	window.location = "${pageContext.request.contextPath}/payslip/viewPaySlip.do?payslipId="+payslipId 
   }
    
</script>   

</head>
<body >


<html:form action ="/salaryadvance/beforeSanction">

 <center>	
 <table width="95%" cellpadding="0" cellspacing="0" border="0" id="paytable" >   	
   	
 		<input type="hidden" name="employeeCodeId" value="<bean:write name="advanceForm" property="employeeCodeId"/>" />	
		<tr>
		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">List of payslips  </div></td>
	</tr>
<!--	<table  style="width: 700;" colspan="6" cellpadding ="0" cellspacing ="0" border = "1" id="employee">
		<tr>
	    	<td colspan="6" height=30 bgcolor=#bbbbbb align=middle  class="tablesubcaption"><p align="center"><b>List of payslips </b></td>
	    </tr>	
	    
	</table> 			-->
	</table>
	
 	
  <table  width="95%"  cellpadding="0" cellspacing="0" border="0" id="paytable">   	
   	<tr>	   
   		<td height=20 bgcolor=#bbbbbb  >Employee Code</td>
		<td  bgcolor=#bbbbbb >Employee Name</td>
		<td  bgcolor=#bbbbbb >Designation</td>		
   		<td  bgcolor=#bbbbbb  >Gross pay</td>   				
   		<td   bgcolor=#bbbbbb  >Net pay</td>
   		<td bgcolor=#bbbbbb >Pay type</td>   	
		<td bgcolor=#bbbbbb >&nbsp;</td> 
   	</tr>
   	
	<c:if test="${fn:length(payslips) == 0}">
		<tr>
			<td  >
				<font color="red">No payslip</font>
			</td>
		</tr>
	</c:if>
	<c:forEach var="payslipObj" items="${payslips}">
   	<tr>   		   		
   		<td class="whitebox2wk" align="center">   			
   			<c:out value="${payslipObj.employee.employeeCode}"/>
   		</td>	
   		<td class="whitebox2wk" align="center">   			
   			<c:out value="${payslipObj.employee.employeeName}"/>
   		</td>	
   		<td class="whitebox2wk" align="center">   			
   			<c:out value="${payslipObj.empAssignment.desigId.designationName}"/>
   		</td>	
   		<td class="whitebox2wk" align="right">   			
   			<c:out value="${payslipObj.grossPay}"/>&nbsp;
   		</td>	
   		<td class="whitebox2wk" align="right">   			
   			<c:out value="${payslipObj.netPay}"/>&nbsp;
   		</td>	   		   		   		
   		<td class="whitebox2wk" align="center">   			
   			<c:out value="${payslipObj.payType.paytype}"/>
   		</td>	   		   		   		   		
   		<td class="whitebox2wk" align="center">   			
   			<a href="#" onclick="showPayslipInfo('${payslipObj.id}');">
   			   <font size="2">view</font>
   			</a>   
   		</td>	
   	</tr>	
	</c:forEach>

   </table>
 
 

</html:form>
</body>
</html>
