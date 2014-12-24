<%@ taglib uri="/WEB-INF/struts-tags.tld" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="egovtags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>EmployeewisePaymentReport</title>
<s:head />
</head>
<script type="text/javascript">
function setMonthStr(){
var monthIndex=document.getElementById("month").selectedIndex;
document.getElementById("monthStr").value=document.getElementById("month").options[monthIndex].text;
}
</script>
<body>
				
								<div class="datewk">	
									<span class="bold">Today:</span><egovtags:now/>
								</div>	
					<s:actionmessage />
								<s:fielderror cssStyle="font-size:12px;font-weight:bold;" cssClass="mandatory"/>
					
    <s:form action="employeewisePaymentReport" name="empwisePaymentReportForm" theme="simple">
   
							   	
							   	<center>
							 		<table style="width: 760;" align="center" cellpadding="0" cellspacing="0" border="0" id="EmpwisePaymentReport">
										<tr>
						                	<td class="headingwk" colspan="5">
							                	<div class="arrowiconwk">
							                		<img src="../common/image/arrow.gif" />
							                	</div>
							                  	<div class="headplacer">EmpwisePaymentReport</div>
						                  	</td>
						              	</tr>	
										
										<tr>
									    	<td class="whiteboxwk"><font color="red">*</font>Month</td>
											<td class="whitebox2wk" style="width: 20;">
											<s:select name="month" id="month"  cssClass="selectwk" headerValue="Choose" headerKey="0"
											list="#{'1':'JAN','2':'FEB','3':'MAR','4':'APR','5':'MAY','6':'JUN','7':'JUL',
											'8':'AUG','9':'SEP','10':'OCT','11':'NOV','12':'DEC'}" onchange="setMonthStr()"/>
											<s:hidden name="monthStr" id="monthStr"></s:hidden>
											</td>
											<td class="whiteboxwk"><font color="red">*</font>Year</td>
											<td class="whitebox2wk">	
												<s:select name="year" cssClass="selectwk"  list="dropdownData.finYearList" 
												listKey="id" listValue="finYearRange" headerValue="Choose" headerKey="0" onselect="yearString"/>
											</td>
									    </tr>
									    <tr>
									    	<td class="greyboxwk">Bank</td>
											<td class="greybox2wk">	
												<s:select name="bank" cssClass="selectwk" headerValue="Choose" headerKey="0" list="dropdownData.empBankList"
												listKey="id" listValue="name" />
													
											</td>
											<td class="greyboxwk">Groupby EmployeeType</td>
											<td class="greybox2wk">	
												<s:checkbox name="groupByEmpType" cssClass="selectwk"  />
													
											</td>
									    	
										</tr>	
									  
									   	<tr>
									   		<td colspan="4" align="center" >
									   			<s:submit name="action" value="submit" cssClass="buttonfinal" method="search" />
									   		</td>
									   	</tr>
									   	</table>
									   	
									   
				    	
					    		<s:if test="empwisePaymentList != null" >
								<table style="width: 400;" align="center" cellpadding="0" cellspacing="0" border="0" > 
								<tr>
						          <td colspan="4" >
									<div class="tbl3-container" id="tbl-container">
										<display:table name="empwisePaymentList" uid="currentRowObject" class="its" export="true" style="width:790px" requestURI=""  pagesize="4" decorator="org.displaytag.decorator.TotalTableDecorator">

											<display:caption style="text-align: center">
											</br>CORPORATION OF CHENNAI</br>
											
											 payroll Bankwise,Branchwise Details for the month of 
											<s:property value="monthStr"/>-<s:property value="yearStr" />     Date : <s:date name="todayDate" format="dd-MMM-yyyy" /> 
											
											</display:caption>
											<s:if test=" groupByEmpType">
											<display:column style="border:0;" property="empType" title="Employee Type" group="3"/></s:if>
											
											<display:column style="border:0;" property="bankName" title="Bank Name" group="2"></display:column>
											<display:column style="border:0;" property="branchName" title="Branch Name"  group="1"></display:column> 
											
											<display:column style="border:0;" property="empCode" title="Emp. No"></display:column>
											<display:column style="border:0;" property="empName" title="Name"></display:column>
											
											<display:column style="border:0;" property="empBankAcNo" title="SB A/C No."></display:column>
											<display:column style="border:0;" property="netAmount" title="Nett Amount"  total="true"></display:column><!-- format="{0,number, currency}" -->
											
											
																								  
									      	<div STYLE="display:table-header-group;">			      
										  		<display:setProperty name="basic.show.header" value="true" />
										  		<display:setProperty name="export.pdf" value="true" />
												<display:setProperty name="export.pdf.filename" value="EmpwisePaymentReport.pdf" />
												<display:setProperty name="export.excel" value="true" />
												<display:setProperty name="export.excel.filename" value="EmpwisePaymentReport..xls"/>
												<display:setProperty name="export.csv" value="false" />
												<display:setProperty name="export.xml" value="false" />
								    		</div>
								    	</display:table >
								    </div>	
						        </td>
						    </tr>
						    		  		
				   		</table>
				   		</s:if>
				   		
									   	</center>
        
        
    </s:form>
    
    </div>
						<div class="rbbot2">
					
			<div class="buttonholderwk"><html:button property="close"  styleId="button"  styleClass="buttonfinal"  onclick="window.close()"  ><bean:message key="close"/></html:button>
			
			</div>
			
</body>
</html>