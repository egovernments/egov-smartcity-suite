<%@ include file="/includes/taglibs.jsp" %>

<html>

<head>
	<title>Branch wise Summary Report</title>

<script language="JavaScript"  type="text/JavaScript">

	function getReport(format) {
		document.bankAdviceReportForm.fileFormat.value=format;
		document.bankAdviceReportForm.action="bankAdviceReport!showBranchWiseReport.action";
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
					 	<center>
					 		<table style="width: 810;" align="center" cellpadding="0" cellspacing="0" border="0" id="bankAdviceReport">
								<tr>
				                	<td colspan="5" class="headingwk">
					                	<div class="arrowiconwk">
					                		<img src="../common/image/arrow.gif" />
					                	</div>
					                  	<div class="headplacer">Branch-wise Summary Report</div>
				                  	</td>
				              	</tr>
				   		
						<tr>
							<s:hidden name="fileFormat" styleId="fileFormat" />
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
							<td class="greyboxwk" >Bank</td>
							<td class="greybox2wk">

								<s:select  headerValue="Choose"  headerKey="-1"  
									list="dropdownData.bankList" listKey="id" listValue="name" 
									label="bankId" id="bankId" name="bankId" />
																
							</td>	
							
							<td class="greybox2wk"> Group by </td>
							<td class="greybox2wk">
								<s:checkbox label="employeeType" name="groupByEmployeeType" value="groupByEmployeeType" fieldValue="true"/> Employee Type    		
							</td>
						    </tr>
						    
						     

						<tr>
							<td colspan="4">
								<div class="buttonbottom" align="center">

									<s:submit name="action" value="submit" cssClass="buttonsubmit" method="showBranchWiseReport" onclick="init()"/>
								</div>
							</td>
						</tr>
						</table>
							   
						<s:if test="%{branchWiseSummaryList != null}" >
								<table style="width: 810;" align="center" cellpadding="0" cellspacing="0" border="0" >
								<tr>
						          <td colspan="4" >
									<div class="tbl3-container" id="tbl-container">
									<display:table name="branchWiseSummaryList" uid="currentRowObject" class="its" export="false" style="width:790px" requestURI="" decorator="org.displaytag.decorator.TotalTableDecorator" >
										
										<display:caption class="headerbold">Payroll Bankwise,Branchwise Details for the month of ${monthName} - ${financialYearRange} as on Date : <egovtags:now /></display:caption>
										<s:if test="%{groupByEmployeeType}">
										<display:column style="border:0;" property="empType" title="Employee type" group="1"></display:column>
										</s:if>
										<display:column style="border:0;" property="bankName" title="Bank" group="1"></display:column>
										<display:column style="border:0;" property="branchCode" title="Code" ></display:column>
										<display:column style="border:0;" property="branchName"title="Branch"  ></display:column>
										<display:column style="border:0;" property="empCount" title="No. of Employees" format="{0,number}" total="true"></display:column>
										<display:column style="border:0;" property="netAmount" title="Net Pay" format="{0,number}" total="true" ></display:column>											  
										
										<div STYLE="display:table-header-group;">			      
											<display:setProperty name="basic.show.header" value="true" />
											<display:setProperty name="basic.empty.showtable" value="true" />
											<display:setProperty name="basic.msg.empty_list_row">
											<tr class="empty"><td colspan="{0}">No records found.</td></tr> 																					  		
										  	</display:setProperty>


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
