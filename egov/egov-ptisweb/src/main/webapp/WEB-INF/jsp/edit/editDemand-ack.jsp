<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/includes/taglibs.jsp" %>


<html>
<head>
	<title><s:text name="editDemand" /></title>	
</head>
	<body>
	<s:form name="editDemandAckForm" >
		<s:token />
		<table border="0" width="100%" cellpadding="0" cellspacing="0">
			<div class="formmainbox">
			  <div class="formheading"></div>
					<div class="headingbg"><s:text name="editDemand"/></div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="bluebox">
							&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
					<tr>
				        <td colspan="5" style="background-color: #FDF7F0;font-size: 13px;" align="center">
				        	<span class="bold">
				        		<s:text name="editDemandSuccessMessage"/>
				        	</span>
				        	
				        		
				        		
				       </td>
					</tr>
					<tr>
						<td class="bluebox">
							&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
					</table>
				<div class="buttonbottom" align="center">
					<input type="button" name="button2" id="button2" value="Close"
						class="button" onclick="window.close();" />
				</div>
			</div>
		</table>
	</s:form>
	</body>
</html>
