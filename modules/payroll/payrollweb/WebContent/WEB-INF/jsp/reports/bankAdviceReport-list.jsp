<%@ include file="/includes/taglibs.jsp" %>
<%@ page buffer = "16kb" %>
<%@ page language="java" 
	import="java.util.*,java.sql.*,org.egov.infstr.utils.EgovMasterDataCaching,
			org.egov.infstr.utils.*,
			org.egov.payroll.utils.PayrollConstants,
			org.egov.payroll.utils.PayrollManagersUtill" %>

<html>

<head>
	<title>Bank Advice Report</title>

<script language="JavaScript"  type="text/JavaScript">
	function getReport(format) {
		document.bankAdviceReportForm.fileFormat.value=format;
		document.bankAdviceReportForm.action="bankAdviceReport!showBankAdviceList.action";
		document.bankAdviceReportForm.submit();
	}
	
	function init(){
		document.bankAdviceReportForm.fileFormat.value="HTM";
	}

</script>   

</head>

<body onload="init()">
	
					 <s:if test="%{hasErrors()}">
						    <div id="errorstyle" class="errorstyle" >
						      <s:actionerror/>
						      <s:fielderror/>
						    </div>
						</s:if>
						<s:if test="%{hasActionMessages()}">
						    <div class="messagestyle">
							<s:property value="%{estimateNumber}"/> &nbsp; <s:actionmessage theme="simple"/>

						    </div>
    					   </s:if>
					<s:form name="bankAdviceReportForm" action ="bankAdviceReport"  theme="simple">	
					<s:hidden name="fileFormat" styleId="fileFormat" />
					 	<center>
					 		<table style="width: 810;" align="center" cellpadding="0" cellspacing="0" border="0" id="bankAdviceReport">
								<tr>
				                	<td colspan="5" class="headingwk">
					                	<div class="arrowiconwk">
					                		<img src="../common/image/arrow.gif" />
					                	</div>
					                  	<div class="headplacer">Bank Advice Report</div>
				                  	</td>
				              	</tr>
				   			
						<tr>
							<td class="whiteboxwk" >Month<font color="red">*</font></td>
							<td class="whitebox2wk">
									
								<s:select  headerValue="Choose"  headerKey="-1"  
									list="dropdownData.monthList" listKey="value" listValue="label" 
									label="month" id="month" name="month" />
									
							</td>	
							<td class="whiteboxwk">Year<font color="red">*</font></td>
							<td class="whitebox2wk">
									
								<s:select  headerValue="Choose"  headerKey="-1"  
									list="dropdownData.finYearList" listKey="id" listValue="finYearRange" 
									label="financialyearId" id="financialyearId" name="financialyearId" />			    		
							</td>
						</tr>
						    
						<tr>
							<td class="greyboxwk" >Group By : </td>
							
							<td class="greybox2wk"> <s:checkbox label="employeeType" name="groupByEmployeeType" value="groupByEmployeeType" fieldValue="true"/> Employee Type
							&nbsp;&nbsp;<s:checkbox label="groupByDept" name="groupByDept" value="groupByDept" fieldValue="true"/> Department </td>
							<td class="greybox2wk" colspan="2">
										    		
							</td>
						    </tr>

						<tr>
							<td colspan="5">
								<div class="buttonholderwk">
									<s:submit name="action" value="submit" cssClass="buttonfinal" method="showBankAdviceList" onclick="init()" />
								</div>
							</td>
						</tr>
						</table>
							   
						<s:if test="%{bankAdviceList != null}" >
								<table style="width: 810;" align="center" cellpadding="0" cellspacing="0" border="0" >
								<tr>
						          <td colspan="4" >
									<div class="tbl3-container" id="tbl-container">
										<display:table name="bankAdviceList" uid="currentRowObject" class="its" export="false" style="width:790px" requestURI="" >
									      	
											<display:caption class="headerbold">Payroll Bankwise Details for the month of  ${monthName} - ${financialYearRange} as on Date : <egovtags:now /></display:caption>
											<s:if test="%{groupByEmployeeType}">
											<display:column style="border:0;" property="empType" title="Employee type" group="1"></display:column>
											</s:if>
											<display:column style="border:0;" property="bankName" title="Bank" group="2"></display:column>
											<s:if test="%{groupByDept}">
											<display:column style="border:0;" property="deptName" title="Department" ></display:column>
											</s:if>
											<display:column style="border:0;" property="netAmount" title="Net pay" format="{0,number,0,000.00}" total="true"></display:column>											  
									      	<div STYLE="display:table-header-group;">			      
										  		<display:setProperty name="basic.show.header" value="true" />
										  		<display:setProperty name="basic.empty.showtable" value="true" />
										  		<display:setProperty name="basic.msg.empty_list_row">
										  		<tr class="empty"><td colspan="{0}">No records found.</td></tr> 
										  		
										  		</display:setProperty>
										  		<display:setProperty name="export.pdf" value="true" />
												<display:setProperty name="export.pdf.filename" value="BankAdvice.pdf" />
												<display:setProperty name="export.excel" value="true" />
												<display:setProperty name="export.excel.filename" value="BankAdvice.xls"/>
												<display:setProperty name="export.csv" value="false" />
												<display:setProperty name="export.xml" value="false" />
								    		</div>
								    	</display:table >
								    </div>
								     <div  class="exportlinks">
								   	<table><td colspan="2"> Export Options: </td><td  colspan="5"><a href="javascript:getReport('PDF')">PDF</a> | <a href="javascript:getReport('RTF')">RTF </a> | <a href="javascript:getReport('TXT')">Text</a></td></table>
								    </div>
						        </td>
						    </tr>
						    </table>
						 </s:if>
	
					    	
				   		
					    </center>
					</s:form>
				
</body>
</html>
