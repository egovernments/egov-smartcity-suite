<%@ include file="/includes/taglibs.jsp" %>
<%@ page
	import="org.egov.payroll.dao.*,java.util.*,org.egov.infstr.*,org.egov.infstr.commons.dao.*,org.egov.infstr.commons.*,org.egov.payroll.model.* "%>

<html>

<head>


	<title>Regular Payslips</title>



<script language="JavaScript"  type="text/JavaScript">

	function getOrder(sorteBy){		
		document.payslipApproveFrom.sortedBy.value = sorteBy;
		document.payslipApproveFrom.submit();
	}

	function selectAllApprovePayslip(){
		var length = document.payslipApproveFrom.approvedPayslips.length;
		if(document.getElementById("allApprovePayslip").checked){
			document.getElementById("allRejectPayslip").checked = false;
			for(var i = 0 ; i < length ; i++){
				document.payslipApproveFrom.rejectedPayslips[i].checked=false;
				document.payslipApproveFrom.approvedPayslips[i].checked=true;				
				document.payslipApproveFrom.rejectComments[i].disabled = true;
			}
		}
		else{
			for(var i = 0 ; i < length ; i++){
				document.payslipApproveFrom.approvedPayslips[i].checked=false;
				document.payslipApproveFrom.rejectComments[i].disabled = true;
			}
		}

	}

  	function selectAllRejectPayslip(){
		var length = document.payslipApproveFrom.rejectedPayslips.length;
		if(document.getElementById("allRejectPayslip").checked){
			document.getElementById("allApprovePayslip").checked = false;
			for(var i = 0 ; i < length ; i++){
				document.payslipApproveFrom.approvedPayslips[i].checked=false;
				document.payslipApproveFrom.rejectedPayslips[i].checked=true;
				document.payslipApproveFrom.rejectComments[i].disabled = false;
			}
		}
		else{
			for(var i = 0 ; i < length ; i++){
				document.payslipApproveFrom.rejectedPayslips[i].checked=false;
				document.payslipApproveFrom.rejectComments[i].disabled = true;
			}
		}

	}

  	function checkApprove(){  		
  		if(document.payslipApproveFrom.approvedPayslips.length > 0){		
	  		for(var i = 0 ; i < document.payslipApproveFrom.approvedPayslips.length ; i++){
	  			if(document.payslipApproveFrom.approvedPayslips[i].checked){
	  				document.payslipApproveFrom.rejectedPayslips[i].checked = false;
	  				document.getElementById("allRejectPayslip").checked = false;
	  				document.payslipApproveFrom.rejectComments[i].disabled = true;
	  			}	  			
	  		}
	  	}
	  	else{
	  		if(document.payslipApproveFrom.approvedPayslips.checked){
  				document.payslipApproveFrom.rejectedPayslips.checked = false;
  				document.payslipApproveFrom.rejectComments.disabled = true;
	  		}	  		
	  	}
	  		
  	}

  	function checkReject(obj){
  		if(document.payslipApproveFrom.rejectedPayslips.length > 0){		
	  		for(var i = 0 ; i < document.payslipApproveFrom.rejectedPayslips.length ; i++){
	  			if(document.payslipApproveFrom.rejectedPayslips[i].checked){
	  				document.payslipApproveFrom.approvedPayslips[i].checked = false;
	  				document.getElementById("allApprovePayslip").checked = false;
	  				document.payslipApproveFrom.rejectComments[i].disabled = false;
	  			}
	  			else	  				
	  				document.payslipApproveFrom.rejectComments[i].disabled = true;	  			
	  		}
	  	}
	  	else{
	  		if(document.payslipApproveFrom.rejectedPayslips.checked){
	  			document.payslipApproveFrom.approvedPayslips.checked = false;
	  			document.payslipApproveFrom.rejectComments.disabled = false;
	  		}
	  		else
	  			document.payslipApproveFrom.rejectComments.disabled = true;
	  	}	
  	}

//		FOR SHOWING TEXTAREA	
	  				
	function getingTextArea(object){
		if(document.payslipApproveFrom.approvedPayslips.length > 0){		
	  		for(var i = 0 ; i < document.payslipApproveFrom.rejectedPayslips.length ; i++){
	  			if(document.payslipApproveFrom.rejectedPayslips[i].checked){
					var tbl  = document.getElementById(object.value);	  
				    var row = tbl.rows[0];				   
				    var cell = row.insertCell(0);
				    var index = 1;
			        cell.width="48%" ;
					cell.innerHTML="<textarea name=\'descrip\' cols=\'10\' rows=\'2\'  id=\'newDescription_" + index  + "'/>";
					break;
	  			}
	  		}
	  	}
	  	else{
	  		if(document.payslipApproveFrom.rejectedPayslips.checked)
	  			document.payslipApproveFrom.approvedPayslips.checked = false;	  		
		  	}
	}


  	function checkOnSubmit(){
  		alert("ok");
  		alert(document.payslipApproveFrom.action.value);
  		document.payslipApproveFrom.submit();
  	}
	
	function showPayslipInfo(content){			
	    var url='/payslipapprove/payslipInfo.do?payslipId=' + content ;
		window.open(url,'PayslipInfo','width=800,height=600,top=150,left=80,scrollbars=yes,resizable=yes');
    }
    
   
    
</script>    

<%
		SalaryCodesDAO salaryCodesDAO = null;
		List earningPayheads = new ArrayList();
		salaryCodesDAO = PayrollDAOFactory.getDAOFactory().getSalaryCodesDAO();
		earningPayheads =(List)salaryCodesDAO.getAllSalarycodesByCategoryType("E");

		List deductionPayheads = new ArrayList();
		deductionPayheads =(List)salaryCodesDAO.getAllSalarycodesByCategoryType("D");
		Collections.sort(deductionPayheads,SalaryCodes.CategoryComparator);

		List otherDeductions = new ArrayList();
		DeductionsDAO deductionsDAO = PayrollDAOFactory.getDAOFactory().getDeductionsDAO();
		otherDeductions = deductionsDAO.getAllOtherDeductionChartOfAccount();

		int size = deductionPayheads.size() + otherDeductions.size();
%>



</head>


<body >

<html:form action ="/payslipapprove/approvePayslip">


	<html:hidden name="payslipApproveFrom" property="sortedBy"/>
	
	
 <div id="leftNaviProposal"   style="overflow-y:scroll;  height:550px; "> 	
  <table style="width: 750; " align="center" cellpadding="0" cellspacing="0" border="1" id="paytable">
   	<tr>
   		<td colspan="30" height=20 bgcolor=#bbbbbb align="left"  class="tablesubcaption">
   			Department :- <bean:write name="payslipApproveFrom" property="deptName"/>
   		</td>
   	</tr>
   	<tr>
   		<td colspan="1" height=20 bgcolor=#bbbbbb align=center  class="labelcellH"><a href="#" onclick="getOrder('empCode')">Employee Code</a></td>
		<td colspan="1" height=20 bgcolor=#bbbbbb align=center  class="labelcellH"><a href="#" onclick="getOrder('empName')">Employee Name</a></td>
		<td colspan="1" height=20 bgcolor=#bbbbbb align=center  class="labelcellH"><a href="#" onclick="getOrder('empDesig')">Designation</a></td>
		<td colspan="1" height=20 bgcolor=#bbbbbb align=center  class="labelcellH">Fund</td>
		<td colspan="1" height=20 bgcolor=#bbbbbb align=center  class="labelcellH">Function</td>
		<td colspan="1" height=20 bgcolor=#bbbbbb align=center  class="labelcellH">Field</td>
		
   		<td colspan="1" height=20 bgcolor=#bbbbbb align=center  class="labelcellH">Gross pay</td>
		<td colspan="1" height=20 bgcolor=#bbbbbb align=center  class="labelcellH">Total Deduction </td>
   		<td colspan="1" height=20 bgcolor=#bbbbbb align=center  class="labelcellH">Net pay</td>
   		<td colspan="1" height=20 bgcolor=#bbbbbb align=center  class="labelcellH">View Payslips</td>
   		<td colspan="1" height=20 bgcolor=#bbbbbb align=center  class="labelcellH">Approve </td>
   		<td colspan="1" height=20 bgcolor=#bbbbbb align=center  class="labelcellH">Reject </td>
   	</tr>
	<tr>
		<td class="labelcell" colspan="10">&nbsp;</td>
		<bean:size id="noOfPayslip" name="payslipApproveFrom" property="empPayrolls"/>				
		<td class="labelcell">
		<logic:notEqual name="noOfPayslip" value="1">
			<b>Approve All</b>			
			<input type="checkbox" id="allApprovePayslip"  onclick="selectAllApprovePayslip()"/>
		</logic:notEqual>&nbsp;	
		</td>
		<td class="labelcell">
		<logic:notEqual name="noOfPayslip" value="1">
			<b>Reject All</b>			
			<input type="checkbox" id="allRejectPayslip"  onclick="selectAllRejectPayslip()"/>
		</logic:notEqual>&nbsp;	
		</td>
   	</tr>  		
	    
   	<logic:notEmpty name="payslipApproveFrom" property="empPayrolls">
   	<logic:iterate name="payslipApproveFrom" property="empPayrolls" id="empPayroll">	
   	<tr>		
   	
   		<td class="labelcell"><bean:write name="empPayroll" property="employee.employeeCode"/></td>
   		<td class="labelcell">
   			<bean:write name="empPayroll" property="employee.employeeFirstName"/>&nbsp;
			<logic:notEmpty name="empPayroll" property="employee.employeeMiddleName">			
	   			<bean:write name="empPayroll" property="employee.employeeMiddleName"/>&nbsp;
	   		</logic:notEmpty>	
			<bean:write name="empPayroll" property="employee.employeeLastName"/>
   		</td>
   		<td class="labelcell"><bean:write name="empPayroll" property="empAssignment.desigId.designationName"/></td>
   		<td class="labelcell">
   			<logic:notEmpty name="empPayroll" property="empAssignment.fundId">
   				<bean:write name="empPayroll" property="empAssignment.fundId.name"/>
   			</logic:notEmpty>&nbsp;
   		</td>
   		<td class="labelcell">
   			<logic:notEmpty name="empPayroll" property="empAssignment.functionId">
	   			<bean:write name="empPayroll" property="empAssignment.functionId.name"/>
	   		</logic:notEmpty>&nbsp;
	   	</td>
   		<td class="labelcell">&nbsp;</td>	
	    <td class="labelcell" align="right"><bean:write name="empPayroll" property="grossPay"/></td>
		<td class="labelcell" align="right"><bean:write name="empPayroll" property="totalDeductions"/></td>
		<td class="labelcell" align="right"><bean:write name="empPayroll" property="netPay"/></td>
		<td class="labelcell" align="center">
			<a href="#" onclick="showPayslipInfo('<bean:write name="empPayroll" property="id"/>');"> view payslip </a>			
		</td>
	  	<td class="labelcell">
			<input type="checkbox" name="approvedPayslips"  onclick="checkApprove(this)" value="<bean:write name="empPayroll" property="id"/>" />
		</td>
	  	<td class="labelcell">
			<input type="checkbox" name="rejectedPayslips"   onclick="checkReject(this);" value="<bean:write name="empPayroll" property="id"/>" />
   			<textarea name="rejectComments" disabled="disabled" rows="2" cols="10"></textarea>
		</td>
   	</tr>
   	</logic:iterate>
 	</logic:notEmpty> 

	<logic:notEmpty name="payslipApproveFrom" property="empPayrolls">
 	<tr>
 		<td class="labelcell" colspan="6">&nbsp;</td>
 
 		<td class="labelcell" align="right">
 			<b><bean:write name="payslipApproveFrom" property="grossTotal"/></b>
 		</td>  
		<td class="labelcell" align="right">
 			<b><bean:write name="payslipApproveFrom" property="deductionTotal"/></b>
 		</td>  		
 		<td class="labelcell" align="right">
 			<b><bean:write name="payslipApproveFrom" property="netTotal"/></b>
 		</td>
 		<td class="labelcell" colspan="3">&nbsp;</td>
 	</tr>
	</logic:notEmpty>
   </table>
  </div> 
 

<br>
<table align='center' id="table2">	
 	<tr>
 		<td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>
  		<td >
		  	<p style="text-align: center"><html:submit property="action" value="submit"/></p>
		 </td>
		
	</tr>
  </table>	
</html:form>
</body>
</html>
