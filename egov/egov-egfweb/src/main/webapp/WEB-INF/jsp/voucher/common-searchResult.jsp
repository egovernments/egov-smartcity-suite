<html>
<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<head>
<script type="text/javascript">
function goToParent(code, name, id) {
	var srchType = '<s:property value="%{searchType}" />';
	var passingValue = code+"^#"+name+"^#"+id;
	window.opener.popupCallback(passingValue, srchType);
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
							<th class="bluebgheadtd" width="70%">Name</th>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td style="width: 345px; overflow-y: scroll;">
					<div style="overflow: auto; width: 370px; height: 380px;">
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="0">
							<s:if test="%{searchType == 'EntitySearch'}">
								<s:iterator var="s" value="entitiesList" status="status">
									<tr>
										<td align="left" width="40%">&nbsp;&nbsp;<a href="#"
											onclick="goToParent('<s:property value="%{code}" />', '<s:property value="%{name}" />', '<s:property value="%{id}" />' );"><s:property
													value="%{code}" /></a></td>
										<td align="left" width="60%">&nbsp;&nbsp;<s:property
												value="%{name}" /></td>
									</tr>
								</s:iterator>
								<s:if test="entitiesList == null || entitiesList.size==0">
									<tr>
										<td colspan="2"><div class="subheadsmallnew">No
												Records Found</div></td>
									</tr>
								</s:if>
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

