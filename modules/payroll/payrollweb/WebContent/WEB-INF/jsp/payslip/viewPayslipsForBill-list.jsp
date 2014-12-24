<%@ include file="/includes/taglibs.jsp" %>
<html>

<head>
<script language="JavaScript"  type="text/JavaScript">

function showPayslipInfo(payslipId){
		window.open("${pageContext.request.contextPath}/payslip/viewPaySlip.do?payslipId="+payslipId,"ViewPayslip","height=900pt, width=900pt,scrollbars=yes,left=30,top=30,status=yes");   
	   	//window.location = "${pageContext.request.contextPath}/payslip/viewPaySlip.do?payslipId="+payslipId 
}
</script>
</head>

 <s:form name="viewPayslipsForm" action="viewPayslipsForBill" theme="simple" > 
 	<center>
		
			<div class="formmainbox">
				<div class="insidecontent">
			  		<div class="rbroundbox2">
						<div class="rbtop2">
							<div>
							</div>
						</div>
						<div class="rbcontent2">
							
	<!-- Decorator -->	
			  
			 
			 <span id="msg">
				<s:actionerror cssClass="mandatory"/>  
				<s:fielderror cssClass="mandatory"/>
				<s:actionmessage cssClass="actionmessage"/>
			 </span>
			 
			 
	<table width="100%" cellpadding="0" cellspacing="0" border="0" align="center">
	  <tbody>					
		  	<tr>
		  	 	<td class="headingwk">
			  	 	<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
			   			<p>
			   				<div class="headplacer">List of payslips</div>
			   			</p>
			   	</td>		   		 
		   	</tr>
	   </tbody>
	</table>
	<br/>			 
 	<table width="96%" cellpadding="0" cellspacing="0" border="0" align="center">
 		
		<tr>
          <td>				
			  	<display:table style="margin-left:4px;width:100%" name="payslipSet" uid="currentRowObject"  class="its"  export="false"  sort="list" requestURI="" pagesize="10">
			     	<display:column  property="paySlip.employee.employeeCode" title="Employee Code " />
			     	<display:column  property="paySlip.employee.employeeFirstName" title="Employee Name "  />			     	
			     	<display:column  property="paySlip.toDate" format="{0,date,MMMM}"  title="Month "  />
			     	<display:column  property="paySlip.financialyear.finYearRange"  title="Financial year "  />
			     	<display:column  property="paySlip.empAssignment.desigId.designationName" title="Designation "  />
			     	<display:column  property="paySlip.empAssignment.deptId.deptName" title="Department "  />
			     	<display:column  property="paySlip.empAssignment.fundId.name"  title="Fund " />
			     	<display:column  property="paySlip.empAssignment.functionId.name" title="Function "  />
			     	<display:column  property="paySlip.empAssignment.functionary.name" title="Functionary "  />			     	
			     	<display:column  property="paySlip.grossPay"  title="Gross pay " />			     	
			     	<display:column  property="paySlip.netPay"  title="Net pay " />			     	
			     	<display:column  title="View Payslips" >
			     		<a href="#" onclick="showPayslipInfo(<s:property value='#attr.currentRowObject.paySlip.id' />)">view payslip </a>
			  		</display:column>
			   	</display:table>
		   	
		   
		  </td>
		 </tr>		
 	</table>
 
 </div>
 	<div class="rbbot2"><div/></div>
	</div>
	</div>
	</div>
	</div>
	</center>
 
 </s:form> 



</html>