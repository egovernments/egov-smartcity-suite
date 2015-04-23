<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title><s:text name='objection.record.message.title' /></title>
</head>

<body onload="refreshInbox()">
	<s:push value="model">
		<div class="formmainbox">
			<s:if test="%{hasActionMessages()}">
				<font style='color: green; font-weight: bold'> <s:actionmessage />
				</font>
			</s:if>

		
		<div class="formheading"></div>
		<div class="headingbg"><s:text name="objection.record.message.title" /></div>
		<s:if test="objectionRejected == true">
			<script>
	 			if(opener && opener.top.document.getElementById('inboxframe'))
					opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
				alert("Generating Rejection Letter");
				window.location = "${pageContext.request.contextPath}/objection/rejectionLetter!print.action?model.id="+'<s:property value="objection.id"/>';
			</script>
		</s:if>
		<s:if test="isShowAckMessage == true">			
			<div align="center" style="font-size:15px;">
				<p>
					<span class="mandatory">
						No valid assessment data exists for property 
					</span>
					<a href='../view/viewProperty!viewForm.action?propertyId=<s:property value="%{propertyId}" />' style="fond-size:15px;">
						<s:property value="%{propertyId}" /> 
					</a> 
					<span class="mandatory"> 
						, Please update the data before proceeding with Objection 
					</span>
				</p>
			</div>
		</s:if>
		<div class="buttonbottom" align="center">
			<table>				
				<tr>
					<s:if test="isShowAckMessage == true">
						<td>
							<input type="button" class="button" name="SearchProperty"
								id="SearchProperty" value="Search Property" 
								onclick="window.location='../search/searchProperty!searchForm.action';" />	
						</td>
					</s:if>
					<td>
						<input type="button" name="button2" id="button2"
						value="Close" class="button" onclick="window.close();" />
					</td>

				</tr>
			</table>
		</div>
	</s:push>
	</div>
	<script>
 
		function refreshInbox() {
			if (opener && opener.top.document.getElementById('inboxframe'))
				opener.top.document.getElementById('inboxframe').contentWindow.egovInbox
						.refresh();
		}
	</script>
</body>

</html>