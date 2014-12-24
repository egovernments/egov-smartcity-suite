<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/struts-tags.tld" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="egovtags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="org.egov.payroll.client.reports.EmpDeductionInfo,java.math.BigDecimal"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title> <s:text name="deductionreport.salaryDeductionTitle"/></title>
<s:head />
</head>
<script type="text/javascript">

function setMonthStr(){ 
	//setOnSubmit();
}
function setOnSubmit(){
var monthIndex=document.getElementById("month").selectedIndex;
document.getElementById("monthStr").value=document.getElementById("month").options[monthIndex].text;

var yearIndex=document.getElementById("year").selectedIndex;
document.getElementById("yearStr").value=document.getElementById("year").options[yearIndex].text;

var departmentIndex=document.getElementById("department").selectedIndex;
document.getElementById("departmentStr").value=document.getElementById("department").options[departmentIndex].text;

var deductionSalIndex=document.getElementById("deductionSalId").selectedIndex;
document.getElementById("deductionSalStr").value=document.getElementById("deductionSalId").options[deductionSalIndex].text;
}
</script>
<body> 
<%
  BigDecimal totalAmount=BigDecimal.ZERO;

%>				
								<div class="datewk">	
									<span class="bold"><bean:message key="today"/></span><egovtags:now/>
								</div>	
				<div class="errorstyle" id="employeeSocietyDeduction_error" style="display: none;"></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>		
    <s:form action="employeeSocietyDeductionReport" name="employeeSalaryDeductionReportForm" theme="simple" onsubmit="setOnSubmit();">
   
							   	
							   	<center>
							 		<table width="100%" align="center" cellpadding="0" cellspacing="0" border="0" id="EmpwisePaymentReport">
										<tr>
						                	<td class="headingwk" colspan="5">
							                	<div class="arrowiconwk">
							                		<img src="../common/image/arrow.gif" />
							                	</div>
							                  	<div class="headplacer"><s:text name="deductionreport.salaryDeductionLabel" /></div>
						                  	</td>
						              	</tr>	
										 
									    <tr>
									    	<td class="greyboxwk"><font color="red">*</font><s:text name="deductionreport.deductionHead" /></td>
											<td class="greybox2wk">	
												<s:select id="deductionSalId" name="deductionSalId" cssClass="selectwk" headerValue="-- Choose --" headerKey="0" list="dropdownData.deductionList"
												listKey="id" listValue="description" />
												
												<s:hidden id="monthStr" name="monthStr" value="%{monthStr}"/>
													<s:hidden id="yearStr" name="yearStr" value="%{yearStr}"/>
														<s:hidden id="departmentStr" name="departmentStr" value="%{departmentStr}"/>
														
												<s:hidden id="deductionSalStr" name="deductionSalStr" value="%{deductionSalStr}"/>					
											</td>
											<td class="greyboxwk"><td class="greybox2wk">
										</tr>	
									  
									  	<%@ include file="commonSearchReport.jsp" %>
									   	</table>
									   	
						<table width="100%" border="0" cellspacing="0"
								cellpadding="0">
								<tr><td>&nbsp;</td></tr>
								<tr>
									<td class="headingwk">
										<div class="arrowiconwk">
											<img src="../common/image/arrow.gif" />
										</div>
										<div class="headplacer">
											<s:text name="title.search.result" />
										</div>
									</td>
								</tr>
							</table>				   
				    	
				<s:if test="salaryDeductionList != null && salaryDeductionList.getFullListSize()!= 0" >
					
									<div class="tbl3-container" id="tbl-container">
							
					 <display:table  name="salaryDeductionList"  export="false" requestURI=""   class="simple" style="width:100%;"  uid="currentRowObject"  >
 						
 						<display:caption class="headerbold">Salary Deduction details<br>${billNumberHeading}</display:caption>  
 						 	<display:column title=" SN" style="width:8%;text-align:left;"  >
 						 		  
 						 		 
 						 		 <s:property value="#attr.currentRowObject_rowNum + (page-1)*pageSize" />
 						 	</display:column>
 						 	<display:column  title=" EMP. NO." style="width:9%;text-align:left;"  property="empCode"  />
 						 	<display:column title="NAME" style="width:35%;text-align:left;"   >
          			 	     <s:property value="%{(#attr.currentRowObject.empName)}"/>
          			 	    </display:column>
          			 	   
 						   	<display:column  style="width:10%;text-align:right;"  property="amount" format="{0,number,0.00}" />
 						   	<% BigDecimal totalAmountAsString=(BigDecimal)(((EmpDeductionInfo)pageContext.findAttribute("currentRowObject")).getAmount());
 						   	if(totalAmountAsString!=null)
 						   		totalAmount=totalAmount.add(totalAmountAsString);  
 						   %>
 						   <display:footer>
	    										<tr>
	      											<td colspan="3" align="right"> <b> Page Total: &nbsp;&nbsp; </b> </td>
	      												<td align="right">
	      													<b><%=totalAmount %></b>
	      											
	      											</td>
	    										<tr>
	  						</display:footer>
	  						
	  						<s:if test="%{(pageSize*(page-1)+ salaryDeductionList.getList().size())==salaryDeductionList.fullListSize}" >
	 										<display:footer>
	    										<tr>
	      											<td colspan="3" align="right"> <b> <bean:message key="page.total"/> &nbsp;&nbsp; </b> </td>
	      												<td align="right">
	      												<b><%=totalAmount %></b>
	      											</td>
	    										<tr>
	    										<tr>
	      											<td colspan="3" align="right"><b><bean:message key="grand.total"/>&nbsp;&nbsp;</b></td>
	      											<td align="right">
	      													<b><s:text name="common.format.number" >
	      												<s:param name="grandTotal" value='%{grandTotal}' />
	      												</s:text></b>
	      											</td>
	    										<tr>
	  										</display:footer>
  							</s:if>
  										
						</display:table>
						
							<table width="100%" border="0" cellpadding="0"
											cellspacing="0">
											<tr><td>&nbsp;</td></tr>
											<tr>
												<td align="center">
												
												<s:submit name="action" value="EXPORT PDF" cssClass="buttonfinal" method="exportReportToPdf" />
											<s:submit name="action" value="EXPORT EXCEL" cssClass="buttonfinal" method="exportReportToExcel" />
											
												</td>
											</tr>
										</table>
										
								    </div>	
						   
				   		</s:if>
				   		 <s:elseif test="%{salaryDeductionList.fullListSize == 0}">
								<div>
									<table width="100%" border="0" cellpadding="0"
										cellspacing="0">
										<tr>
											<td align="center">
												<font color="red"><bean:message key="no.record.found"/></font>
											</td>
										</tr>
									</table>
								</div>
		 				</s:elseif>  
									   	</center>
        
        
    </s:form>
    
    </div>
						<div class="rbbot2">
					
			<div class="buttonholderwk"><html:button property="close"  styleId="button"  styleClass="buttonfinal"  onclick="window.close()"  ><bean:message key="close"/></html:button>
			
			</div>
			
</body>
</html>