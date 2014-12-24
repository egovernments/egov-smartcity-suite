<%@ include file="/includes/taglibs.jsp" %>
<%@ page buffer = "16kb" %>
<%@ page language="java"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>  


<html>  
	<head>  
	<center>
	    <title>Head Count Report</title>
	   
		<SCRIPT type="text/javascript">		
		function setDateToCurrentDate()
		{		
			if(document.getElementById("givenDate").value=="")
				document.getElementById("givenDate").value="<%=(new java.text.SimpleDateFormat("dd/MM/yyyy")).format(new java.util.Date())%>";
		}
		
		function checkOnSubmit()
		{			
			if(document.getElementById("givenDate").value=="")
			{
				alert("Please enter date");
				document.getElementById("givenDate").focus();
				return false;
			}
			else
			{
				var isValid = valCurrentDate(document.getElementById("givenDate").value);
				//alert("isValid="+isValid);
				if(!isValid)
				{
					alert("Date must be less than current date");
					document.getElementById("givenDate").focus();
					return false;
				}
				else
					return true;
			}						
				
		}	
		
		
		</SCRIPT>

    


	    </head>
	    <body onload="setDateToCurrentDate()">

	<s:actionmessage />
	<s:form name="empHeadCountReport" action ="headCountReport"  theme="simple"  onsubmit="return checkOnSubmit()">	
		<div class="formmainbox">
			<div class="insidecontent">
		  		<div class="rbroundbox2">
					<div class="rbtop2">
						<div></div>
					</div>
					<div class="rbcontent2">

						<!-- Header Section Begins -->
						<!-- Header Section Ends -->
						<table width="95%" cellpadding ="0" cellspacing ="0" border = "0" id="table2">
							<tr>
								<td>
									<!-- Tab Navigation Begins -->
									<!-- Tab Navigation Ends -->
									<!-- Body Begins -->
									<!-- Body Begins -->
									<div>
										<center>
											<table width="100%" cellpadding ="0" cellspacing ="0" border = "0"  >
												<tbody>
													<tr><td>&nbsp;</td></tr>
													<tr>
			  											<td colspan="8" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
															<p>
															<div class="headplacer">Search for Total Number of Employees under each Designation for a given Department</div>
															</p>
														</td>
			  										</tr>
			  										<tr><td>&nbsp;</td></tr>
			  										<tr>
														<td  class="whiteboxwk">Department</td>
														<td  class="whitebox2wk">
														
															<s:select name="departmentId" id="departmentId" list="dropdownData.departmentList" listKey="id" listValue="deptName" 
    															headerKey="" headerValue="------Select------" />
															
														</td>
														
														<td class="whiteboxwk" colspan="2"/>
													</tr>
													<tr id="dateRangeRow">
								        				<td class="greyboxwk" id="fromDateLabel"><span class="mandatory">*</span>As On Date (dd/MM/yyyy)</td>
														<td class="greybox2wk" id="fromDateTxt">
														
														<s:date name="givenDate" var="givenDateValue" format="dd/MM/yyyy"/>
														<s:textfield name="givenDate" id="givenDate" value="%{givenDateValue}" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);"/>
														<a 	name="dateFromAnchor" id="dateFromAnchor" href="javascript:show_calendar('forms[0].givenDate');"	onmouseover="window.status='Date Picker';return true;" 
																	onmouseout="window.status='';return true;"><img src="<%=request.getContextPath()%>/common/image/calendar.png"  border="0">
														</a>
														
														</td>
																				          				
													</tr>		
																										

												
												</table>

											<br>												
												<s:if test="deptDesigList != null" >												
												<br>
										  			<table align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
														<tr>
															<td class="headingwk" colspan="5">
														  		<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
														  		<div align="left" style="margin-top:4px;">Head Count Report</div>
														  	</td>
														</tr>
													</table>
													
										 			<display:table name="deptDesigList" id="currentRowObject" requestURI="" style="width: 750;" 										 			
										  				export="true" defaultsort="2" pagesize = "20" sort="list"  class="its">
													<br>
														<display:caption style="text-align: center">Total Number of Employees under each designation as on <s:if test="givenDate==null"><%=(new java.text.SimpleDateFormat("dd/MM/yyyy")).format(new java.util.Date())%></s:if><s:else><fmt:formatDate pattern="dd/MM/yyyy" value="${givenDate}" /></s:else> </display:caption>
														<display:column title="Department" >
														      <c:out value="${currentRowObject[0]}-${currentRowObject[1]}"/>														      
    														</display:column>
    														
    														<display:column title="Designation" >
															<c:out value="${currentRowObject[2]}"/>														      
    														</display:column>
    														<display:column title="Total No. of Employees" >
															<c:out value="${currentRowObject[3]}"/>														      
    														</display:column>																																																														
																			
														<div STYLE="display:table-header-group;">			      
													  		<display:setProperty name="paging.banner.placement" value="bottom" />
													  		<display:setProperty name="basic.show.header" value="true" />
													  		<display:setProperty name="export.pdf.filename" value="headCountReportPdf.pdf" />
															<display:setProperty name="export.excel.filename" value="headCountReportByDate.xls"/>
															<display:setProperty name="export.csv" value="false" />
															<display:setProperty name="export.xml" value="false" />
															
														</div>
														
													</display:table>
													
												</s:if>
												
											</table>
											<br>
										</center>
									</div>
								</td>
							</tr>
							<tr><td>&nbsp;</td></tr>
							<tr>
           						<td><div align="right" class="mandatory">* Mandatory Fields</div></td>
         					</tr>
						</table>
						
			<div class="rbbot2"><div></div></div>
		</div>
</div></div>

		
		
		
		<div><s:submit name="action" value="SUBMIT" cssClass="buttonfinal" method="search"/>
  		 <input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close();"/></div>
	</s:form>
	
</body>
</html>
	    