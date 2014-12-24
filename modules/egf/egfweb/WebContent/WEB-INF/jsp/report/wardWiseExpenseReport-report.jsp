<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>  
<head>  
    <title><s:text name="ward.wise.expense.report" /></title>
    
</head>

	<body>  
		<s:form action="wardWiseExpenseReport" name="wardWiseExpenseReport" theme="simple"  method="post">
			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>

				<div class="formmainbox">
					<div class="subheadnew">
						<s:text name="ward.wise.expense.report" />
					</div>
				</div>
			<table border="1" width="870" align="center" bgcolor="#EFEFEF"  height="5">
				<tr><TD width="500" height="500"><div id="map_canvas" style="width:100%; height:100%"></div> </TD>
         	 		<c:import url="/commons/googleMapWardWiseKmlReport.jsp" context="/egi">
	       			</c:import>
	       		</tr>
			</table>
			
				
		</s:form>  
		
	</body>
</html>