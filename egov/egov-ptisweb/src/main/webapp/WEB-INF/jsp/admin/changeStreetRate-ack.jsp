<%@page contentType="text/html; charset=ISO-8859-1" %>
<%@include file="/includes/taglibs.jsp" %>


<html>
<head>
	<title> <s:text name="ChBaseRateArea.title"></s:text> </title>	
</head>
	<body>
	<s:form name="deactivate" >
		<div style="margin-left: 30px">
		<table border="1" width="100%" cellpadding="0" cellspacing="0" style="margin-left: 8px">
			<div class="formmainbox"></div>
			  <div class="formheading"></div>
					<div class="headingbg">
						<s:text name="ChBaseRateAreaHeader"/>
					</div>
					<table  border="0" cellspacing="0" cellpadding="0" width="100%">
						<tr>
							<td class="bluebox">
								&nbsp;&nbsp;&nbsp;
							</td>
						</tr>
						<tr>
					        <td colspan="5" style="background-color: #FDF7F0;font-size: 13px;" align="center">
					        	<span class="bold"><s:text name="AreaRateChanged"></s:text>					        	
					        		<s:property value="%{boundary.name}"/>
					        	</span>
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
		</table>
		</div>
	</s:form>
	</body>
</html>
