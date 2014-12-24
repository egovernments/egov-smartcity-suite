<%@ include file="/includes/taglibs.jsp" %>
<%@ page buffer = "16kb" %>
<%@ page language="java"%>
<%@ page import="java.util.*"%>


<html>  
	<head>  
	<center>
	    <title>Vacant Position Report</title>
	   
		<SCRIPT type="text/javascript">
		
		function checkOnSubmit(){
		
		if(document.vacantPositionForm.fromDate.value==""){
			alert("Enter from date");
			return false;
		}		
		if(document.vacantPositionForm.toDate.value==""){
			alert("Enter to date");
			return false;
		}
		
		return true;
		}	
		
		</SCRIPT>

    


	    </head>
	    <body >

	<s:actionmessage />
	<s:form name="vacantPositionForm" action ="searchVacantPosition"  theme="simple">	
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
															<div class="headplacer">Search for Vacant Position</div>
															</p>
														</td>
			  										</tr>
			  										<tr><td>&nbsp;</td></tr>
			  										<tr>
														<td  class="whiteboxwk"><span class="mandatory">*</span>Designation</td>
														<td  class="whitebox2wk">
														
															<s:select name="designationId" id="designationId" list="dropdownData.designationList" listKey="designationId" listValue="designationName" 
    															headerKey="" headerValue="------Select------" />
															
														</td>
														
														<td class="whiteboxwk" colspan="2"/>
													</tr>
													<tr id="dateRangeRow">
								        				<td class="greyboxwk" id="fromDateLabel"><span class="mandatory">*</span>From Date (dd/MM/yyyy)</td>
														<td class="greybox2wk" id="fromDateTxt">
														
														<s:date name="fromDate" var="fromDateValue" format="dd/MM/yyyy"/>
														<s:textfield name="fromDate" value="%{fromDateValue}" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);"/>
														<a 	name="dateFromAnchor" id="dateFromAnchor" href="javascript:show_calendar('forms[0].fromDate');"	onmouseover="window.status='Date Picker';return true;" 
																	onmouseout="window.status='';return true;"><img src="<%=request.getContextPath()%>/common/image/calendar.png"  border="0">
														</a>
														
														</td>
												
								          				<td class="greyboxwk"><span class="mandatory">*</span>To Date (dd/MM/yyyy)</td>
														<td class="greybox2wk">
														
															<s:date name="toDate" var="toDateValue" format="dd/MM/yyyy"/>
															<s:textfield name="toDate" value="%{toDateValue}" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);"/>
															<a name="dateFromAnchor" id="dateFromAnchor" href="javascript:show_calendar('forms[0].toDate');"	onmouseover="window.status='Date Picker';return true;" 
																onmouseout="window.status='';return true;"><img src="<%=request.getContextPath()%>/common/image/calendar.png"  border="0">
															</a>
															
														</td>
													</tr>		
																										

												
												</table>

											<br>
												<c:if test="${not empty positionList }">
												
												<br>
										  			<table align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
														<tr>
															<td class="headingwk" colspan="5">
														  		<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
														  		<div align="left" style="margin-top:4px;">Vacant Positions</div>
														  	</td>
														</tr>
													</table>
													
										 			<display:table name="positionList" id="currentRowObject" requestURI="" style="width: 750;" 										 			
										  				export="true" defaultsort="2" pagesize = "10" sort="list"  class="its">
													<br>
														<display:caption style="text-align: center">Vacant Positions</display:caption>
														
														
														<display:column    property="name" title="Position Name" />
												 		<display:column    property="desigId.designationName" title="Designation Name" />
												 		 
												 		
												 		
														
														<div STYLE="display:table-header-group;">			      
													  		<display:setProperty name="paging.banner.placement" value="bottom" />
													  		<display:setProperty name="basic.show.header" value="true" />
													  		<display:setProperty name="export.pdf.filename" value="vacantPositionByDatePdf.pdf" />
															<display:setProperty name="export.excel.filename" value="vacantPositionByDate.xls"/>
															<display:setProperty name="export.csv" value="false" />
															<display:setProperty name="export.xml" value="false" />
															
														</div>
														
													</display:table>
													
												</c:if>
												
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

		
		
		
		<div><s:submit name="action" value="submit" cssClass="buttonfinal" method="search" onclick="return checkOnSubmit();"/>
  		 <input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close();"/></div>
	</s:form>
	
</body>
</html>
	    