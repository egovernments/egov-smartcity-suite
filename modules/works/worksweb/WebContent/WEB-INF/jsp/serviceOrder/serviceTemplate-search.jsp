<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<html>
<head>
	<title>Service Template search</title>
	<link href="<egov:url path='/css/displaytag.css'/>" rel="stylesheet" type="text/css" />
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
<div class="formmainbox">
	<div class="insidecontent">
  		<div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			<div class="rbcontent2">
	<s:form action="serviceTemplate" theme="simple"name="serviceTemplateSearchForm">
		
		
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
										Template Search
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<table width="100%">
				<tr>
						<td width="25%" class="whiteboxwk"> Template Code</td>
						<td width="25%" class="whitebox2wk"><s:textfield id="templateCode" name="templateCode"></s:textfield> </td>
						<td width="25%" class="whiteboxwk"> Template Name </td>
						<td width="25%" class="whitebox2wk"> <s:textfield id="templateName" name="templateName"></s:textfield></td>
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
				requestURI="" sort="external"  style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
									
				<display:column  headerClass="pagetableth" class="pagetabletd" title="Sl.No" style="width:3%">
						<s:property value="%{#attr.currentRowObject_rowNum+ (page-1)*pageSize}"/>
				</display:column>			
					
				<display:column  headerClass="pagetableth" class="pagetabletd" title="Template Code" style="width:10%" >
					<s:if test="%{type.equalsIgnoreCase('modify')}">
					<a href="${pageContext.request.contextPath}/serviceOrder/serviceTemplate!beforeEdit.action?template.id=<s:property value='%{#attr.currentRowObject.id}'/>">
							<s:property value="%{#attr.currentRowObject.templateCode}" /> 
						</a>
					</s:if>
					<s:else> 
						<a href="${pageContext.request.contextPath}/serviceOrder/serviceTemplate!view.action?template.id=<s:property value='%{#attr.currentRowObject.id}'/>">
							<s:property value="%{#attr.currentRowObject.templateCode}" /> 
						</a>
					</s:else>
				</display:column>
				<display:column  headerClass="pagetableth" class="pagetabletd" title="Template Name" style="width:10%"  property="templateName" />
				<display:column  headerClass="pagetableth" class="pagetabletd" title="Template Description" style="width:10%" property="templateDesc"  />
				
				</display:table>
				  	</td>
				  </tr>
				 	
				 </s:if>
				<s:elseif test="%{searchResult.fullListSize == 0}">
					<tr><td colspan="7" align="center"><font color="red">No record Found.</font></td>
																	
					</tr>
			</s:elseif>
		</table>
		<s:hidden name="type"></s:hidden>
	</s:form>
			</div>
		</div>
	</div>
</div>

</body>
</html>