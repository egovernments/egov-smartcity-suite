<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<link href="<egov:url path='/css/displaytag.css'/>" rel="stylesheet" type="text/css" />
<html>
<head>

	<title>Service order search</title>
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

	<s:form action="serviceOrder" theme="simple"name="serviceOrderSearchForm">
		
		
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
				<td class="whiteboxwk" width="25%" ><s:text name="so.arch"></s:text> </td>
						<td class="whitebox2wk" width="25%" ><s:select headerKey=""headerValue="%{getText('list.default.select')}"
						name="serviceOrder.detailkeyid" id="detailkeyid" cssClass="selectwk"list="dropdownData.archtectList" listKey="id"listValue='name'/></td>	
				<td width="25%" class="whiteboxwk"><s:text name="so.number" /></td>
				<td width="25%" class="whitebox2wk">
				<s:textfield name="serviceOrder.serviceordernumber" id="serviceordernumber" value="%{serviceordernumber}" />				</td>
			
				</tr>
				<tr>
					
				<td width="25%" class="greyboxwk"><s:text name="so.fromdate"></s:text> </td>
				<td width="25%" class="greybox2wk">
				<s:date name="fromDate" id="fromDateId" format="dd/MM/yyyy" />
				<s:textfield name="fromDate" id="fromDate" value="%{fromDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" size="10"/>
				<a href="javascript:show_calendar('serviceOrderSearchForm.fromDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
				<td width="25%" class="greyboxwk"><s:text name="so.todate"></s:text> </td>
				<td width="25%" class="greybox2wk">
				<s:date name="toDate" id="toDateId" format="dd/MM/yyyy" />
				<s:textfield name="toDate" id="toDate" value="%{toDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" size="10"/>
				<a href="javascript:show_calendar('serviceOrderSearchForm.toDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>	
				</tr>
				
				</table></table>
				
			
			<table width="100%">
							
							<tr>
								<td colspan="4">
									<div class="buttonholderwk">
									<s:submit type="submit" id="search" value="SEARCH" cssClass="buttonfinal" method="list" />
										
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
					
				<display:column  title="Service Order  Number" style="width:10%;text-align:center" >
					<s:if test="%{value.equalsIgnoreCase('modify')}">
					<a href="${pageContext.request.contextPath}/serviceOrder/serviceOrder!beforeEdit.action?serviceOrder.id=<s:property value='%{#attr.currentRowObject.serviceOrderId}'/>">
							<s:property value="%{#attr.currentRowObject.serviceordernumber}" /> 
						</a>
					</s:if>
					<s:else> 
						<a href="${pageContext.request.contextPath}/serviceOrder/serviceOrder!view.action?serviceOrder.id=<s:property value='%{#attr.currentRowObject.serviceOrderId}'/>">
							<s:property value="%{#attr.currentRowObject.serviceordernumber}" /> 
						</a>
					</s:else>
				</display:column>
				<display:column  title="Estimate Number" style="width:10%;text-align:center"  property="estimateNumber" />
				<display:column  title="Service Order Date" style="width:10%;text-align:center" property="serviceorderdate"  />
				<display:column  title="Architect" style="width:10%;text-align:center" property="architect"  />
				
										
				</display:table>
				  	</td>
				  </tr>
				 	
				 </s:if>
				<s:elseif test="%{searchResult.fullListSize == 0}">
					<tr><td colspan="7" align="center"><font color="red">No record Found.</font></td>
																	
					</tr>
			</s:elseif>
		</table>
		<s:hidden name="value"></s:hidden>
	</s:form>


</body>
</html>