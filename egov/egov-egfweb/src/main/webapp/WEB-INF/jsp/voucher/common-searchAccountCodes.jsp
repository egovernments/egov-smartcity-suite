<html>
<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<head>
<script type="text/javascript">
function goToParent(glcode,coaid) {
	window.opener.popupCallbackForAccountCode(glcode,coaid);
	window.close();
}
</script>
<style type="text/css">
.searchscroll {
	overflow-y: scroll;
	height: 390px;
	position: relative;
}
</style>

</head>
<body>
	<s:form action="common">
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td>
					<table width="95%" border="0" align="left" cellpadding="0"
						cellspacing="0">
						<tr>
							<th class="bluebgheadtd" width="30%"><s:property
									value="%{accountDetailTypeName}" />&nbsp;&nbsp;Code</th>
							<th class="bluebgheadtd" width="70%">Description</th>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td style="width: 345px; overflow-y: scroll;">
					<div style="overflow: auto; width: 370px; height: 380px;">
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="0">
							<s:iterator var="s" value="accountCodesList" status="status">
								<tr>
									<td align="left" width="40%">&nbsp;&nbsp;<a href="#"
										onclick="goToParent('<s:property value="%{glcode}" />','<s:property value="%{id}" />');"><s:property
												value="%{glcode}" /></a></td>
									<td align="left" width="60%">&nbsp;&nbsp;<s:property
											value="%{name}" /></td>
								</tr>
							</s:iterator>
							<s:if test="accountCodesList == null || accountCodesList.size==0">
								<tr>
									<td colspan="2"><div class="subheadsmallnew">No
											Records Found</div></td>
								</tr>
							</s:if>
						</table>
					</div>
				</td>
			</tr>
		</table>
		<br />
		<div class="buttonbottom">
			<input type="submit" value="Close"
				onclick="javascript:window.close()" class="button" />
		</div>
	</s:form>




</body>
</html>

