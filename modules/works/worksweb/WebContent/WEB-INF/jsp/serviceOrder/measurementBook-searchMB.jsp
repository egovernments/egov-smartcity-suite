<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<link href="<egov:url path='/css/displaytag.css'/>" rel="stylesheet" type="text/css" />
<html>
<head>
	<title>Measurement Book search</title>
</head>

<body class="yui-skin-sam">
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
<div id="jserrorid" class="errorstyle" style="display:none" >
	<p class="error-block" id="lblError" ></p>
</div>
	<s:if test="%{hasActionMessages()}">
		<div class="messagestyle">
			<s:actionmessage theme="simple" />
		</div>
	</s:if>

	<s:form action="measurementBook" theme="simple"name="mesurementBookSearchMBForm">
		
		
			<table id="formTable" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table  width="100%" border="0"
							cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img
											src="${pageContext.request.contextPath}/image/arrow.gif" />
									</div>
									<div class="headplacer" align="left">
										<s:text name="so.header.details"></s:text>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<table width="100%">
				<tr>
				<td class="whiteboxwk" width="25%" >Department : </td>
						<td class="whitebox2wk" width="25%" ><s:select headerKey="-1"headerValue="%{getText('list.default.select')}"
						name="soObjDetail.serviceOrder.departmentId.id" id="departmentId" cssClass="selectwk"list="dropdownData.departmentList" listKey="id"listValue='deptName' /></td>
				<td width="25%" class="whiteboxwk">MB Number : </td>
					<td width="25%" class="whitebox2wk">
							<s:textfield  name="mbNumber" id="mbNumber"  />
					
					</td>			
				</tr>
				
				<tr>
					
				<td width="25%" class="greyboxwk"><s:text name="so.fromdate"></s:text> </td>
				<td width="25%" class="greybox2wk">
				<s:date name="fromDate" id="fromDateId" format="dd/MM/yyyy" />
				<s:textfield name="fromDate" id="fromDate" value="%{fromDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" size="10"/>
				<a href="javascript:show_calendar('mesurementBookSearchMBForm.fromDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
				<td width="25%" class="greyboxwk"><s:text name="so.todate"></s:text> </td>
				<td width="25%" class="greybox2wk">
				<s:date name="toDate" id="toDateId" format="dd/MM/yyyy" />
				<s:textfield name="toDate" id="toDate" value="%{toDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" size="10"/>
				<a href="javascript:show_calendar('mesurementBookSearchMBForm.toDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>	
				</tr>
				
				</table></table>
				
			
			<table width="100%">
							
							<tr>
								<td colspan="4">
									<div class="buttonholderwk">
									<s:submit type="submit" id="search" value="SEARCH" cssClass="buttonfinal" method="searchMB"  />
										
										<input type="button" class="buttonfinal" value="CLOSE"
											id="closeButton" name="button" onclick="window.close();" />
									</div>
								</td>
							</tr>
							
				</table>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
			
			<s:if test="%{searchResult.fullListSize != 0}">
			<tr align="center">
				<td >
				<display:table name="searchResult" export="false"  id="searchResultid" uid="currentRowObject" cellpadding="0" cellspacing="0"
				requestURI="" sort="external"  class="its" style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
									
				<display:column  title="Sl.No" style="width:3%;text-align:center">
						<s:property value="%{#attr.currentRowObject_rowNum+ (page-1)*pageSize}"/>
				</display:column>			
				<display:column title="MB Number" style="width:10%;text-align:center">	
					<a href="${pageContext.request.contextPath}/serviceOrder/measurementBook!view.action?mHeader.id=<s:property value='%{#attr.currentRowObject.id}'/>">
							<s:property value="%{#attr.currentRowObject.mbNumber}" /> 
						</a>
					
				</display:column>
				<display:column  title="MB Date" style="width:10%;text-align:center" >
							<s:date name="#attr.currentRowObject.measurementDate"  format="dd/MM/yyyy"/>
				</display:column>
				<display:column  title="Estimate Number" style="width:10%;text-align:center"  property="serviceOrderObjectDetail.abstractEstimate.estimateNumber" />
				<display:column  title="Department" style="width:10%;text-align:center"  property="serviceOrderObjectDetail.serviceOrder.departmentId.deptName" />
				<display:column  title="Bill#" style="width:10%;text-align:center" >
					<s:iterator var="s" value="mbBills" status="mbBills">
						<s:property value="%{egBillregister.billnumber}" /><s:if test="!#mbBills.last">,</s:if>
					</s:iterator>
				</display:column> 
				
										
										
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