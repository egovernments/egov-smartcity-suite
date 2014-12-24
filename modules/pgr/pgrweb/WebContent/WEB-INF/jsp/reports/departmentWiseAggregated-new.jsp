<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ page isELIgnored="false" %>
<html>

<head>
	<title><s:text name="department.wise.report" /></title>

</head>

<body class="yui-skin-sam">
<link type="text/css"  rel="stylesheet" href="${pageContext.request.contextPath}/css/displaytag.css" rel="stylesheet"  />
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:form theme="simple" name="departmentWiseAggregatedForm" action="departmentWiseAggregated" method="post">
	
			<table  width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table  width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img
											src="${pageContext.request.contextPath}/images/arrow.gif" />
									</div>
									<div class="headplacer" align="left">
										<s:text name="subheader.date"></s:text>
										
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr><tr>
			<table width="100%">
				<tr>
					<td width="25%" class="whiteboxwk"> <s:text name="fromDate" /></td>
					<td width="25%" class="whitebox2wk"><s:date name="fromDate" id="fromDateId" format="dd/MM/yyyy"/>
						<s:textfield name="fromDate" id="fromDate" value="%{fromDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
						<a href="javascript:show_calendar('departmentWiseAggregatedForm.fromDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img  src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)
					</td>
					<td width="25%" class="whiteboxwk"><s:text name="toDate" /></td>
					<td width="25%" class="whitebox2wk">
						<s:date name="toDate" id="toDateId" format="dd/MM/yyyy"/>
						<s:textfield name="toDate" id="toDate" value="%{toDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
						<a href="javascript:show_calendar('departmentWiseAggregatedForm.toDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)
					</td>
				</tr>
			
		 	</table>
				</table>
				 <div class="rbroundbox2">
				
					
				</div>
			
			<table width="100%">
							
							<tr>
								<td colspan="4">
									<div class="buttonholderwk">
									<s:submit type="submit" id="save" value="SEARCH" cssClass="buttonfinal" method="list"  />
										
										<input type="button" class="buttonfinal" value="CLOSE"
											id="closeButton" name="button" onclick="window.close();" />
									</div>
								</td>
							</tr>
							
				</table>
				
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
				<td colspan="7" class="headingwk">
					<div class="arrowiconwk"><img src="${pageContext.request.contextPath}/images/arrow.gif" /></div>
						<div class="headplacer" align="left"><s:text name="search.result" /></div>											
						</td>
		</tr>
			<s:if test="%{searchResult.fullListSize != 0}">
				
			
			<tr align="center">
				<td>
				<display:table name="searchResult" export="true"  id="searchResultid" uid="currentRowObject" requestURI="" sort="external"  class="its" varTotals="colTotal" >	
				<display:column  title="Sl.No" style="width:3%;text-align:center">
						<s:property value="%{#attr.currentRowObject_rowNum}"/>
				</display:column>		
				<display:column  title="Department" style="width:10%;text-align:center"  property="deptName"/>
				<s:set var="pendingComplaint" value="0"></s:set>
				<s:set var="attendedComplaint" value="0"></s:set>
				<s:set var="receivedcomplaint" value="0"></s:set>
				<s:iterator value="statusList"  >
					<s:set value="%{getNumberOfComplaints(#attr.currentRowObject.id +'-'+name)}" var="noOfComp"> </s:set>
						<s:if test="%{name.equalsIgnoreCase('REGISTERED') || name.equalsIgnoreCase('FORWARDED') ||
							name.equalsIgnoreCase('PROCESSING') || name.equalsIgnoreCase('REOPENED') }">
						 	 <s:set var="pendingComplaint" value="#noOfComp+#pendingComplaint"></s:set>
						</s:if>
						<s:elseif test="%{name.equalsIgnoreCase('COMPLETED') || name.equalsIgnoreCase('REJECTED')}">
							 <s:set var="attendedComplaint" value="#noOfComp+#attendedComplaint"></s:set>
						</s:elseif>
						 
				 </s:iterator>
				 <s:set var="receivedcomplaint" value="#pendingComplaint+#attendedComplaint"></s:set>
				  <display:column title="Total Complaints Pending <br>(REGISTERED / FORWARDED / <br> PROCESSING / REOPENED)" style="width:10%;text-align:center" value="${pendingComplaint}"  total="true"></display:column>
				  <display:column title="Total Complaints Attended <br>(COMPLETED / REJECTED) " style="width:10%;text-align:center" value="${attendedComplaint}"  total="true"></display:column>
				 <display:column title="Total Complaints Received" style="width:10%;text-align:center" value="${receivedcomplaint}"  total="true" />
				  <display:footer media="excel pdf html" >    
				
                        <tr><td></td>
                            <td align="center" ><font  style='font-weight:bold '>Grand Total </font></td>   
                            <td align="center"><font  style='font-weight:bold '><bean:write  name="${colTotal.column3}" format="######.##"/> </font>  </td>   
						    <td align="center"><font  style='font-weight:bold '><bean:write  name="${colTotal.column4}" format="######.##"/></font>   </td>
						    <td align="center"><font  style='font-weight:bold '><bean:write  name="${colTotal.column5}" format="######.##"/></font>   </td>    
                        </tr> 
                    </display:footer>					
			 		
					<display:caption  media="pdf">
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Department Wise Report
				   </display:caption>
				    <display:setProperty name="export.pdf" value="true" />
					<display:setProperty name="export.pdf.filename" value="deptwisereport-Report.pdf" /> 
					<display:setProperty name="export.excel" value="true" />
					<display:setProperty name="export.excel.filename" value="deptwisereport-Report.xls"/>	
					<display:setProperty name="export.csv" value="false" />	
					<display:setProperty name="export.xml" value="false" />							
				</display:table>
				  	</td>
				  </tr>
				 	
				 </s:if>
				<s:elseif test="%{searchResult.fullListSize == 0}">
					<tr><td colspan="7" align="center"><font color="red">No record Found.</font></td>
																	
					</tr>
			</s:elseif>
			</table>
				
	</s:form>
	


</body>
</html>