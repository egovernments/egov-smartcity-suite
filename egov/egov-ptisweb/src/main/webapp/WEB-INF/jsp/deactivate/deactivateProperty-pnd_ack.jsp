<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/includes/taglibs.jsp" %>


<html>
<head>
	<title><s:text name="deactivate.prop"/></title>	
</head>
	<body>
	<s:form name="deactivate" >
		<table border="0" width="100%" cellpadding="0" cellspacing="0">
			<div class="formmainbox">
			  <div class="formheading"></div>
					<div class="headingbg"><s:text name="deactivate.prop.ack"/></div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="bluebox">
							&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
					<tr>
				        <td colspan="5" style="background-color: #FDF7F0;font-size: 13px;" align="center">
				        	<span class="bold"><s:text name="prop.deactivate.failure"></s:text> </span>
				        	
				        		<a href="../view/viewProperty!viewForm.action?propertyId=<s:property value='%{propertyId}'/>" ><s:property value="%{propertyId}"/></a>
				        	
				       </td>
					</tr>
					<tr>
						<td class="bluebox">
							&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
					<tr style="background-color:#E4E4E4; padding-top: 5px; padding-bottom: 5px" height="40px">
				        <td colspan="5" css="greybox" align="center">
				        	<input type="button" class="button" value="Close" onclick="window.close()">
				       </td>
					</tr>
					</table>
			</div>
		</table>
	</s:form>
	</body>
</html>
