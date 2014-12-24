<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java" import="java.util.*,java.sql.*,org.egov.infstr.utils.EgovMasterDataCaching,org.egov.infstr.utils.*,org.egov.payroll.utils.PayrollConstants" %>
<html>

<head>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">

	<title><bean:message key="payslip.history.report"/>  </title>

<%

/*master data catching for financial year master*/
ArrayList finYrList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-activeFinYr");


//To get column names from hashmap keys.
List columnNamesArr =new ArrayList();
List payslipHistoryDetailsList = (ArrayList)request.getAttribute("payslipHistoryDetails");
	
if(payslipHistoryDetailsList!=null && payslipHistoryDetailsList.size()>0){								      	
	Map payslipMap = (LinkedHashMap) payslipHistoryDetailsList.get(0);
	columnNamesArr.addAll(payslipMap.keySet());
}
%>

<c:set var="financialYear" value="<%=finYrList%>" scope="page" />
<script language="JavaScript"  type="text/JavaScript">

	var employeeName;	
			
	function onBodyLoad()
	{
	  	<%
			  	String msg=(String)request.getAttribute("alertMessage");
			  	if( msg!= null )
			  	{
			  	%>
			  	   alert("<%=msg %>");
			  	   //document.getElementById("results").style.display = "none";
			 	<%
			  	}
	  	%>
	}
	
	/**
	* this function validates the form fields
	**/
	function validation()
	{
		<c:choose>
			<c:when test="${ess  == 1 or (not empty param.ess)}" >
			document.payslipSearchForm.action = "selfService.do?reportType=showPayslipHistory";
			</c:when>
			<c:otherwise>
			document.payslipSearchForm.action = "payslipHistoryReport.do";
			</c:otherwise>
 		</c:choose>
		 if(document.getElementById("employeeCode").value==null || document.getElementById("employeeCode").value=="")
		 {
		    alert('<bean:message key="alert.employee"/>');
		 	return false;
		 }
		 if(document.getElementById("fromMonth").value=="-1" || document.getElementById("toMonth").value=="-1" || document.getElementById("fromFinYr").value=="-1" || document.getElementById("toFinYr").value=="-1" )
		 {
			 alert('<bean:message key="alert.fromTo.date"/>');
  		     return false;
		 }
		   
	  	return true;
	}
	
	
	var empCodeSelectionHandler = function(sType, arguments)
	{ 
	    var oData = arguments[2];
		var empDetails = oData[0];
		var empCode = empDetails.split(EMPCODE_SEP)[0];
		var empName = empDetails.split(EMPCODE_SEP)[1];
		dom.get("empid").value = oData[1];	 	
		dom.get("employeeCode").value = empCode;
		employeeName=empName;
		empcode=empCode;
	}
	
	var empCodeSelectionEnforceHandler = function(sType, arguments) {
	    		warn('improperEmpCodeSelection');
	}
</script>   

</head>
<body onLoad="onBodyLoad();">
	<html:form action ="/reports/payslipHistoryReport" onsubmit="return validation();" method="post">

				  		<table width="100%" border="0" cellspacing="0" cellpadding="0" id="payHistorytable" >   	
						   	<tr>
							    <td colspan="3" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/common/image/arrow.gif" /></div>
		  					 	 <div class="headplacer"> <bean:message key="payslip.history.reports"/> </div>
		   						 </td>
						    </tr>
			   				<tr>
			   					<td class="labelcellforbg" align="right" colspan="3">&nbsp;</td>
			   				</tr>
			   				
			   				<html:hidden property="empid" styleId="empid" />
			   				
			   				<c:choose>
							<c:when test="${(empty ess) and (empty param.ess)}">
						   	<tr>
							  	<td class="whitebox3wknew" width="11%" ><bean:message key="emp.code"/>
							  	</td>
							  	
							  	<td  class="whitebox2wk" width="20%" valign="top">  	
							  		<div class="yui-skin-sam">
								    	<div id="empSearch_autocomplete">
								    	    <input type="text"  class="selectwk" name="employeeCode" id ="employeeCode" /> 	    
								   	   <div id="empCodeSearchResults"></div> 
								    	</div>
									</div>		    	
							   	    <egovtags:autocomplete name="employeeCode"  field="employeeCode" 
							   	    	url="${pageContext.request.contextPath}/common/employeeSearch!getEmpListByEmpCodeLike.action"   results="empCodeSearchResults" 
							   	    	handler="empCodeSelectionHandler" forceSelectionHandler="empCodeSelectionEnforceHandler"/>
							   	    <span class='warning' id="improperempCodeSelectionWarning"></span>
							   	</td>
							   	
							  	<td class="whiteboxwk" width="69%"></td>
						    </tr>
						    </c:when>
						    <c:otherwise>
						    	<input type="hidden" name="employeeCode" id="employeeCode" value="${empcode}"/>
						    </c:otherwise>
						    </c:choose>
						    
						    <tr>
						    	<td class="whiteboxnew" colspan="3">
									<table cellspacing="0" cellpadding="0" border="0" align="center" width="100%">
										<tbody>
											<tr>
												<td width="10%" class="tablesubheadwklatest"><bean:message key ="date" /></td>
							                    <td width="10%" class="tablesubheadwklatest"><bean:message key ="month" /></td>
							                    <td width="80%" class="tablesubheadwklatest"><bean:message key ="year" /></td>
							                </tr>
							                <tr>
							                    <td class="whitebox3wknew"><span class="mandatory">*</span><bean:message key="from.date"/>: </td>
							                    <td class="whitebox3wknew">
													<html:select property="fromMonth" styleId="fromMonth" styleClass="whitebox3wknew">
															<html:option value="-1">Choose</html:option>
															<html:option value="1">Jan</html:option>
												    		<html:option value="2">Feb</html:option>
												    		<html:option value="3">Mar</html:option>
												    		<html:option value="4">Apr</html:option>
												    		<html:option value="5">May</html:option>
												    		<html:option value="6">Jun</html:option>
												    		<html:option value="7">Jul</html:option>
												    		<html:option value="8">Aug</html:option>
												    		<html:option value="9">Sep</html:option>
												    		<html:option value="10">Oct</html:option>
												    		<html:option value="11">Nov</html:option>
												    		<html:option value="12">Dec</html:option>
														</html:select>
												</td>
												<td class="whitebox3wknew">
													<html:select property="fromFinYr" styleId="fromFinYr" styleClass="whitebox3wknew">
													    	<html:option value="-1">Choose</html:option>
													    	<c:forEach var="financialYearObj" items="${financialYear}">
													    	<c:if test = "${financialYearObj.isActive=='1'}">
													    	<html:option value="${financialYearObj.id}">${financialYearObj.finYearRange}</html:option>
													    	</c:if>
													    	</c:forEach>
													 	</html:select>
												</td>              
							              	</tr>
							              	<tr>
							                    <td class="whitebox3wknew"><span class="mandatory">*</span><bean:message key ="to.date" />:</td>
							                    <td class="whitebox3wknew">
													<html:select property="toMonth" styleId="toMonth" styleClass="whitebox3wknew">
														<html:option value="-1">Choose</html:option>
														<html:option value="1">Jan</html:option>
											    		<html:option value="2">Feb</html:option>
											    		<html:option value="3">Mar</html:option>
											    		<html:option value="4">Apr</html:option>
											    		<html:option value="5">May</html:option>
											    		<html:option value="6">Jun</html:option>
											    		<html:option value="7">Jul</html:option>
											    		<html:option value="8">Aug</html:option>
											    		<html:option value="9">Sep</html:option>
											    		<html:option value="10">Oct</html:option>
											    		<html:option value="11">Nov</html:option>
											    		<html:option value="12">Dec</html:option>
													</html:select>
												</td>
												<td class="whitebox3wknew">
													<html:select property="toFinYr" styleId="toFinYr" styleClass="whitebox3wknew">
												    	<html:option value="-1">Choose</html:option>
												    	<c:forEach var="financialYearObj" items="${financialYear}">
												    	<c:if test = "${financialYearObj.isActive=='1'}">
												    	<html:option value="${financialYearObj.id}">${financialYearObj.finYearRange}</html:option>
												    	</c:if>
												    	</c:forEach>
													</html:select>
												</td>
							              	</tr>
		                    			</tbody>
		                    		</table>
								</td>
		    				</tr>
						</table>
				<c:set var="displayTagRequestURI" value="${pageContext.request.contextPath}/reports/payslipHistoryReport.do" />
						
				<c:if test="${ess  == 1 or (not empty param.ess)}" >
					<table WIDTH="100%"  cellpadding ="0" cellspacing ="0" border = "0" >
					 <tr>
						<td class="greyboxwk">Employee Code:</td>
						<td class="greybox2wk">${empcode}</td>
						<td class="greyboxwk" >Employee Name:</td>
						<td class="greybox2wk">${employeeName}</td>
						<td class="greyboxwk" >Date Of Joining: </td>
						<td class="greybox2wk"> ${yearOfJoining}</td>
					</tr>
					</table>
					<c:set var="displayTagRequestURI" value="${pageContext.request.contextPath}/reports/selfService.do?reportType=showPayslipHistory" />	
				</c:if>
				
		     	        <%
		    	
			    		if(payslipHistoryDetailsList!=null && payslipHistoryDetailsList.size()>0 )
						{
						%>
         					
		        		
						<div class="ScrollAuto">
							<table WIDTH="100%"  cellpadding ="0" cellspacing ="0" border = "0" >	
								<tr>
			        				<td colspan="4">
										<display:table name="${payslipHistoryDetails}" export="true" cellspacing="0" cellpadding="0" id="payslipHistory" class="its"  
										 requestURI="${displayTagRequestURI}"  pagesize="20" >  
									      	
											<display:caption style="text-align: center">PAYSLIP HISTORY</display:caption>
											<!-- first element in map is payid , not displaying here-->
											<div id="tbl-container" style="width:900px;overflow-x:scroll;">	
											<display:column style="border:0" property="PayId" media="none"></display:column>
                                            	
											<%
											
											for(int i=1;i<columnNamesArr.size();i++)
											{
												String columnName = (String)columnNamesArr.get(i);
											%>
												
												<display:column style="border:0;" property="<%=columnName%>" ></display:column>
												
											<% 
											}
						      		      	%>
									      		</div>	      
										  		<display:setProperty name="basic.show.header" value="true" />
										  		<display:setProperty name="export.pdf" value="true" />
												<display:setProperty name="export.pdf.filename" value="PayslipHistoryRep.pdf" />
												<display:setProperty name="export.excel" value="true" />
												<display:setProperty name="export.excel.filename" value="PayslipHistoryRep.xls"/>
												<display:setProperty name="export.csv" value="false" />
												<display:setProperty name="export.xml" value="false" />
								    		
							    		</display:table >
			        				</td>
			      				</tr>
			      			</table>
			      		
						<%
						}//end of if statement
						%>
					
			
		<div class="buttonholderwk">
			<html:submit value="Submit" styleClass="buttonfinal" property="b1" />
			<input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close()"/>
		</div>
		
	</html:form>
</body>
</html>