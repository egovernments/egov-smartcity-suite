<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java" import="java.util.*,java.sql.*,org.egov.infstr.utils.EgovMasterDataCaching,org.egov.infstr.utils.*,
									org.egov.payroll.utils.PayrollConstants,org.egov.payroll.utils.PayrollManagersUtill" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Pay Bill Summary Report</title>
	<link href="../common/css/eispayroll.css" rel="stylesheet" type="text/css" />
	<link href="../common/css/commonegov.css" rel="stylesheet" type="text/css" />


<%
ArrayList functionaryList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-functionary");

List financialYears = PayrollManagersUtill.getCommonsService().getAllActivePostingFinancialYear();
List columnNamesArr =new ArrayList();
List payBillDetailsList = (ArrayList)request.getAttribute("payslipSet");
if(payBillDetailsList!=null && payBillDetailsList.size()>0){								      	
	Map payslipMap = (HashMap) payBillDetailsList.get(0);
	columnNamesArr.addAll(payslipMap.keySet());
}

String username ="";

if(request.getAttribute("username")!=null){
	username =request.getAttribute("username").toString();
}

%>

<c:set var="functionaryList" value="<%=functionaryList%>" scope="page" />
<c:set var="financialYears" value="<%=financialYears%>" scope="page" />

<script language="JavaScript"  type="text/JavaScript">

	
	function checkOnSubmit()
	{
		if(document.getElementById('month').value==""){
			alert("Please select month");
			return false;
		}
		if(document.getElementById('finYr').value==""){
			alert("Please select year");
			return false;
		}
		if(document.getElementById("deptid").value==""){
			alert("Please select department");
			return false;
		}
		if(document.getElementById("functionaryId").value==""){
			alert("Please select functionary");
			return false;
		}

		return true;
	}	  
	
	
</script>   

</head>

<body>
	<html:form action ="/reports/PayBillSummaryReport">
		
							 		<table style="width: 810;" align="center" cellpadding="0" cellspacing="0" border="0" id="payBillSummary">
										<tr>
						                	<td colspan="4" class="headingwk">
							                	<div class="arrowiconwk">
							                		<img src="../common/image/arrow.gif" />
							                	</div>
							                  	<div class="headplacer">Pay Bill Summary Report</div>
						                  	</td>
						              	</tr>	
										
										<tr>
									    	<td class="whiteboxwk"><span class="mandatory">*</span>Month</td>
											<td class="whitebox2wk">
											<select name="month" id="month" styleClass="selectwk"  style="width:160px">
												<option value="">Choose</option>
												<option value="1">JAN</option>
												<option value="2">FEB</option>
												<option value="3">MAR</option>
												<option value="4">APR</option>
												<option value="5">MAY</option>
												<option value="6">JUN</option>
												<option value="7">JUL</option>
												<option value="8">AUG</option>
												<option value="9">SEP</option>
												<option value="10">OCT</option>
												<option value="11">NOV</option>
												<option value="12">DEC</option>
												</select>
											</td>
											<td class="whiteboxwk"><span class="mandatory">*</span>Year</td>
											<td class="whitebox2wk">	
												<select name="finYr" id="finYr" styleClass="selectwk"  style="width:160px">
													<option value="">Choose</option>
													<c:forEach var="financialYearObj" items="${financialYears}">
													<c:if test = "${financialYearObj.isActive=='1'}">
													<option value="${financialYearObj.id}">${financialYearObj.finYearRange}</option>
													</c:if>
												</c:forEach>
												</select>
											</td>
									    </tr>
									    <tr>
									    	<td class="greyboxwk"><span class="mandatory">*</span>Department</td>
											<td class="greybox2wk">	
												<select name="deptid" id="deptid" styleClass="selectwk">
													<option value="">Choose</option>
													<egovtags:filterByDeptSelect/>
												</select>
											</td>
									    	<td class="greyboxwk"><span class="mandatory">*</span>Functionary</td>
									    	<td class="greybox2wk">	
												<select name="functionaryId" id="functionaryId" styleClass="selectwk">
													<option value="">Choose</option>
													<c:forEach var="funcObj" items="${functionaryList}" >
														<option value="${funcObj.id}">${funcObj.name}</option>
													</c:forEach>
												</select>
											</td>
										</tr>	
									  
									   	<tr>
									   		<td colspan="4" align="center" >
									   			<html:submit value="Submit" property="buttonSubmit" styleClass="buttonfinal" onclick="return checkOnSubmit();" />
									   		</td>
									   	</tr>
									   
									   <%
						    	
							    		if(payBillDetailsList!=null && payBillDetailsList.size()>0 )
										{
										%>
										<tr>
								          <td colspan="4" >
											<div class="tbl3-container" id="tbl-container">
												<display:table name="${payslipSet}" uid="currentRowObject" class="its" export="true" style="width:850px"  requestURI=""  pagesize="16">
											      	
													<%
														String functionaryName =(String)((Map)pageContext.findAttribute("currentRowObject")).get("FunctionaryName").toString();
														String deptName =(String)((Map)pageContext.findAttribute("currentRowObject")).get("DeptName").toString();
														String monthAndYear =(String)((Map)pageContext.findAttribute("currentRowObject")).get("MonthAndYear").toString();
														
													%>
													<display:caption>
													Payslip for the month :&nbsp;<%=monthAndYear%>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													Department : &nbsp;<%=deptName%>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													Functionary :&nbsp;<%=functionaryName%> 				
													</display:caption>
						
													<!-- first element in map is payid , not displaying here-->
													<display:column style="border:0;" property="SrlNo" title="Srl No." ></display:column>
													<display:column style="border:0;" property="EmployeeCode" title="Employee No." ></display:column>
													<display:column style="border:0;" property="EmployeeName" title="Name"></display:column>
													<display:column style="border:0;" property="CreatedBy" title="Created By" ></display:column>
													<display:column style="border:0;" property="CreatedDate" format="{0,date,dd/MM/yyyy}" title="Created Date" ></display:column>
													<display:column style="border:0;" property="BankDetail" title="Bank Detail"></display:column>
													<display:column style="border:0;" property="BasicAndGradePay" title="Basic Pay/Grade Pay"></display:column>
													<display:column style="border:0;" property="AbsentAmountAndDays" title="Absent Amount/Absent Days"></display:column>
													<display:column style="border:0;text-align:right;" property="TotalEarnings" title="Gross Amount"></display:column>
													<display:column style="border:0;text-align:right;" property="NetPay" title="Net Payable"></display:column>
													
										      		  
											      	<div STYLE="display:table-header-group;">			      
												  		<display:setProperty name="basic.show.header" value="true" />
												  		<display:setProperty name="export.pdf" value="true" />
														<display:setProperty name="export.pdf.filename" value="PayBillSummary.pdf" />
														<display:setProperty name="export.excel" value="true" />
														<display:setProperty name="export.excel.filename" value="PayBillSummary.xls"/>
														<display:setProperty name="export.csv" value="false" />
														<display:setProperty name="export.xml" value="false" />
										    		</div>
										    	</display:table >
										    </div>	
								        </td>
								    </tr>
						
								    <%
								    }
							    	else if(payBillDetailsList!=null){
							    	%>	
							    		<tr>
								          <td colspan="4" ><font size="2" color="red">Nothing found to display</font></td>
								        </tr>
							    	<%		
							    	}
								    %>			  		
						   		</table>
							</html:form>
</body>
</html>
