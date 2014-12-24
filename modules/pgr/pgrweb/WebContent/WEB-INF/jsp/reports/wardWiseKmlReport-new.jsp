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
	<title><s:text name="ward.wise.report" /></title>

</head>

<body class="yui-skin-sam">
<link type="text/css"  rel="stylesheet" href="${pageContext.request.contextPath}/css/displaytag.css" rel="stylesheet"  />
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:form theme="simple" name="wardWiseKmlReportForm" action="wardWiseKmlReport" method="post">
	
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
						<a href="javascript:show_calendar('wardWiseKmlReportForm.fromDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img  src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)
					</td>
					<td width="25%" class="whiteboxwk"><s:text name="toDate" /></td>
					<td width="25%" class="whitebox2wk">
						<s:date name="toDate" id="toDateId" format="dd/MM/yyyy"/>
						<s:textfield name="toDate" id="toDate" value="%{toDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
						<a href="javascript:show_calendar('wardWiseKmlReportForm.toDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)
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
									<s:submit type="submit" id="save" value="SEARCH" cssClass="buttonfinal" method="search"  />
										
										<input type="button" class="buttonfinal" value="CLOSE"
											id="closeButton" name="button" onclick="window.close();" />
									</div>
								</td>
							</tr>
							
				</table>
		<s:if test="%{null != wardWiseComplaints && wardWiseComplaints.size() >0}">
				<table  width="100%" border="0" cellspacing="0" cellpadding="0">
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
											src="${pageContext.request.contextPath}/images/arrow.gif" />
									</div>
									<div class="headplacer" align="left">
										<s:text name="header.map"/>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<table border="1" width="870" align="center" bgcolor="#EFEFEF"  cellspacing="0"  height="5"><tr>
					<TD width="100" height="300"><div id="map_canvas" style="width:100%; height:100%"></div> </TD></tr>
         	 			<c:import url="/commons/googleMapWardWiseKmlReport.jsp" context="/egi"></c:import>
	      		 	</table> 
			
				</table>
		</s:if>
	</s:form>
	


</body>
</html>